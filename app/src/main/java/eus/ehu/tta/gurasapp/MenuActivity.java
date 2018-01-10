package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void toMath(View view) {
        Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
    }

    public void toScience(View view) {
        Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();

    }

    public void toForums(View view) {
        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();

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
