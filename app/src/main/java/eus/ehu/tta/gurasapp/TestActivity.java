package eus.ehu.tta.gurasapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;

import eus.ehu.tta.gurasapp.model.Test;
import eus.ehu.tta.gurasapp.model.Tests;

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private final static String TESTS = "eus.ehu.tta.gurasapp.test.tests";
    private final static String TEST_NUMBER = "eus.ehu.tta.gurasapp.test.testnumber";
    private final static String TEST_SELECTED_ANS = "eus.ehu.tta.gurasapp.test.selectedans";
    private final static String TEST_CHECKED = "eus.ehu.tta.gurasapp.test.checked";
    private final static String TEST_CORRECT = "eus.ehu.tta.gurasapp.test.correct";


    private Tests tests;
    private int selectedChoice;
    private int testNumber;
    private boolean checked;
    private int correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        if (savedInstanceState != null) {
            testNumber = savedInstanceState.getInt(TEST_NUMBER);
            selectedChoice = savedInstanceState.getInt(TEST_SELECTED_ANS);
            correct = savedInstanceState.getInt(TEST_CORRECT);
            checked = savedInstanceState.getBoolean(TEST_CHECKED);
            tests = (Tests) savedInstanceState.getSerializable(TESTS);
        } else {
            selectedChoice = -1;
            tests = data.getTests();
            checked = false;
            correct = 0;
            Collections.shuffle(tests.getTests());
        }

        drawTest(testNumber);

        if (selectedChoice != -1) {
            findViewById(R.id.testSendButton).setVisibility(View.VISIBLE);
            if (checked)
                checkTest(null);
        }
    }

    private void drawTest(int index) {
        int languageIndex = (language == null || language.compareTo(CASTELLANO) == 0) ? 0 : 1;
        Test test = tests.getTests().get(index);

        TextView title = findViewById(R.id.testTitle);

        title.setText(test.getQuestion().get(languageIndex));
        title.setTextSize(20);

        RadioGroup choices = findViewById(R.id.testChoices);
        choices.removeAllViews();

        int i = 0;
        for (String answer : test.getAnswer().get(languageIndex)) {
            RadioButton radioChoice = new RadioButton(this);
            radioChoice.setId(i++);
            radioChoice.setText(answer);
            radioChoice.setTextSize(20);
            radioChoice.setOnClickListener(this);
            choices.addView(radioChoice);
        }

        if (index == tests.getTotal() - 1) {
            Button nextButton = findViewById(R.id.testNextButton);
            nextButton.setText(R.string.finish);
            nextButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(TestActivity.this, getResources().getString(R.string.test_finish) + " " + correct + "/" + tests.getTotal(), Toast.LENGTH_LONG).show();
                    finish();
                }
            });
        }
    }

    public void checkTest(View view) {
        RadioGroup choices = findViewById(R.id.testChoices);
        int correctAns;
        for (int i = 0; i < choices.getChildCount(); i++) {
            choices.getChildAt(i).setEnabled(false);
        }

        correctAns = tests.getTests().get(testNumber).getCorrectAns();
        choices.getChildAt(correctAns).setBackgroundColor(Color.GREEN);
        if (selectedChoice != correctAns) {
            choices.getChildAt(selectedChoice).setBackgroundColor(Color.RED);

            String help = tests.getTests().get(testNumber).getAdvice().get(((language == null || language.compareTo(CASTELLANO) == 0) ? 0 : 1));

            if (help != null && !help.isEmpty()) {
                findViewById(R.id.testAdviceButton).setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.testadviceText)).setText(help);
            }
        } else {
            if (!checked) {
                correct++;
            }
        }
        checked = true;

        findViewById(R.id.testNextButton).setVisibility(View.VISIBLE);

        findViewById(R.id.testSendButton).setVisibility(View.GONE);
    }

    public void showAdvice(View view) {
        findViewById(R.id.testAdviceButton).setEnabled(false);
        findViewById(R.id.testadviceText).setVisibility(View.VISIBLE);

    }

    public void nextTest(View view) {
        selectedChoice = -1;
        checked = false;
        findViewById(R.id.testNextButton).setVisibility(View.GONE);
        findViewById(R.id.testadviceText).setVisibility(View.GONE);
        findViewById(R.id.testAdviceButton).setVisibility(View.GONE);
        findViewById(R.id.testAdviceButton).setEnabled(true);
        drawTest(++testNumber);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(TESTS, tests);
        outState.putInt(TEST_NUMBER, testNumber);
        outState.putInt(TEST_SELECTED_ANS, selectedChoice);
        outState.putInt(TEST_CORRECT, correct);
        outState.putBoolean(TEST_CHECKED, checked);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        selectedChoice = v.getId();
        findViewById(R.id.testSendButton).setVisibility(View.VISIBLE);
    }
}
