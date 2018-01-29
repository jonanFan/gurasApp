package eus.ehu.tta.gurasapp;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eus.ehu.tta.gurasapp.presentation.ProgressTask;

public class MatchActivity extends BaseActivity {

    private final static String SELECTED = "eus.ehu.tta.gurasapp.match.selected";
    private final static String CHECKED = "eus.ehu.tta.gurasapp.match.checked";
    private final static String MATCH = "eus.ehu.tta.gurasapp.match.match";


    private final int FEMALE_MATCH = 0;
    private final int MALE_MATCH = 1;

    private ArrayList<Integer> selected;
    private boolean checked;
    private int match;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean reload = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);


        if (savedInstanceState != null) {
            reload = true;
            selected = savedInstanceState.getIntegerArrayList(SELECTED);
            checked = savedInstanceState.getBoolean(CHECKED, false);
            match = savedInstanceState.getInt(MATCH);
            savedInstanceState.clear();
        } else {
            checked = false;
            match = FEMALE_MATCH;
        }

        List<String> solutions = Arrays.asList(getResources().getStringArray(match == FEMALE_MATCH ? R.array.science_female_array : R.array.science_male_array));

        if (!reload)
            selected = new ArrayList<>(Arrays.asList(new Integer[solutions.size()]));

        createGame(match == FEMALE_MATCH ? R.drawable.science_female_match : R.drawable.science_male_match, solutions);

        if (reload) {
            reloadGame(solutions);

            new ProgressTask<Void>(this, null) {//Ñapa para arreglar un problema con lo de colorear
                @Override
                protected Void background() throws Exception {
                    Thread.sleep(200);
                    return null;
                }

                @Override
                protected void onFinish(Void result) {
                    if (checked)
                        check_match(null);
                }
            }.execute();
        }
    }

    private void reloadGame(List<String> solutions) {
        GridLayout layout = findViewById(R.id.matchOptions);
        Log.d(GURASAPP_ACTIVITY_TAG, "RELOAD");
        for (int i = 0; i < layout.getChildCount(); i++) {
            Spinner spinner = (Spinner) ((RelativeLayout) layout.getChildAt(i)).getChildAt(0);
            for (int j = 1; j < spinner.getAdapter().getCount(); j++) {
                if (selected.get(i) != -1 && spinner.getItemAtPosition(j).toString().compareTo(solutions.get(selected.get(i))) == 0) {
                    spinner.setSelection(j);
                    break;
                }
            }
        }
    }

    private void createGame(int imageId, final List<String> solutions) {
        ImageView image = findViewById(R.id.matchImage);
        image.setImageResource(imageId);

        GridLayout layout = findViewById(R.id.matchOptions);
        layout.removeAllViews();

        for (int i = 0; i < solutions.size(); i++) {
            List<String> elements = new ArrayList<>(solutions);
            Collections.shuffle(elements);
            elements.add(0, getResources().getString(R.string.select));
            ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, elements) {
                @Override
                public boolean isEnabled(int position) {
                    if (position == 0)
                        return false;
                    else
                        return true;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView text = (TextView) view;
                    if (position == 0)
                        text.setTextColor(Color.GRAY);
                    else
                        text.setTextColor(Color.BLACK);

                    return view;
                }
            };
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            Spinner spinner = new Spinner(this);
            Spinner.LayoutParams params = new Spinner.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            spinner.setLayoutParams(params);
            spinner.setGravity(Gravity.CENTER_HORIZONTAL);
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((RelativeLayout) parent.getParent()).setBackgroundColor(Color.TRANSPARENT);

                    Log.d(GURASAPP_ACTIVITY_TAG, "El position es " + position + " y El dato es " + parent.getItemAtPosition(position) + " y Index es " + solutions.indexOf(parent.getItemAtPosition(position)));
                    selected.set(parent.getId(), solutions.indexOf(parent.getItemAtPosition(position)));

                    if (position != 0)
                        findViewById(R.id.matchCheckButton).setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            RelativeLayout spinner_layout = new RelativeLayout(this);
            spinner_layout.setGravity(RelativeLayout.CENTER_HORIZONTAL);
            spinner.setId(i);
            spinner_layout.addView(spinner);
            layout.addView(spinner_layout);
        }
    }

    public void zoomImage(View view) {

        ImageView image = (ImageView) view;

        Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_img);

        PhotoView photo = (PhotoView) dialog.findViewById(R.id.photoView);
        photo.setImageDrawable(image.getDrawable());
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void check_match(View view) {
        int count = 0;
        checked = true;
        GridLayout layout = findViewById(R.id.matchOptions);
        for (int i = 0; i < layout.getChildCount(); i++) {
            RelativeLayout spinner_layout = (RelativeLayout) layout.getChildAt(i);
            Spinner spinner = (Spinner) spinner_layout.getChildAt(0);
            if (selected.get(i) == i) {
                spinner_layout.setBackgroundColor(getResources().getColor(R.color.green));
                spinner.setEnabled(false);
                count++;
            } else {
                spinner_layout.setBackgroundColor(getResources().getColor(R.color.red));
            }
        }

        if (count == layout.getChildCount()) {
            Button button = findViewById(R.id.matchNextButton);
            if (match != FEMALE_MATCH)
                button.setText(R.string.finish);
            button.setVisibility(View.VISIBLE);

        }
    }

    public void nextButtonAction(View view) {
        if (match == FEMALE_MATCH) {
            match = MALE_MATCH;
            List<String> solutions = Arrays.asList(getResources().getStringArray(R.array.science_male_array));
            selected = new ArrayList<>(Arrays.asList(new Integer[solutions.size()]));
            checked = false;
            createGame(R.drawable.science_male_match, solutions);
            findViewById(R.id.matchCheckButton).setVisibility(View.GONE);
            findViewById(R.id.matchNextButton).setVisibility(View.GONE);
        } else {
            finish();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putIntegerArrayList(SELECTED, selected);
        outState.putBoolean(CHECKED, checked);
        outState.putInt(MATCH, match);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }
}

