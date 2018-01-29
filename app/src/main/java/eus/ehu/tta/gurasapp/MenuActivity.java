package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.view.View;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toMath(View view) {
        startBaseActivity(MathActivity.class);
    }

    public void toScience(View view) {
        startBaseActivity(ScienceActivity.class);
    }

    public void toForums(View view) {
        startBaseActivity(ForumsActivity.class);
        //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
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
