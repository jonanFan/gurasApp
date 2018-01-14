package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;

import eus.ehu.tta.gurasapp.view.VideoPlayer;

public class MathActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        WebView video = VideoPlayer.getIFramePlayer(this, getString(R.string.math_video));
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mathVideo);
        linearLayout.addView(video);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    public void toLinks(View view) {
        startBaseActivity(MathLinksActivity.class);
    }
}
