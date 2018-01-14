package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScienceLinksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);

        TextView textView = (TextView) findViewById(R.id.linkTitle);
        textView.setText(R.string.subject_2_topic);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.playLayout);

        Button button = new Button(this);
        button.setText(R.string.lets_play);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toGame(v);
            }
        });
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);


        linearLayout.addView(button);
    }

    public void toGame(View view) {
        Toast.makeText(this, "MATCH", Toast.LENGTH_SHORT).show();
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
