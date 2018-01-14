package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MathLinksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        TextView textView = (TextView) findViewById(R.id.linkTitle);
        textView.setText(R.string.subject_1_topic);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playLayout);

        Button buttonTest = new Button(this);
        buttonTest.setText(R.string.test);
        buttonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTest(v);
            }
        });
        buttonTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        Button buttonMultiplos = new Button(this);
        buttonMultiplos.setText(R.string.multiples);
        buttonMultiplos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toMultiplos(v);
            }
        });
        buttonMultiplos.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        linearLayout.addView(buttonTest);
        linearLayout.addView(buttonMultiplos);
    }

    public void toTest(View view) {
        Toast.makeText(this, "TEST", Toast.LENGTH_SHORT).show();
    }

    public void toMultiplos(View view) {
        Toast.makeText(this, "MULTIPLOS", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }
}
