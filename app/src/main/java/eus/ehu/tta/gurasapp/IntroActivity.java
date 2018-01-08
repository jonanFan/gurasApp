package eus.ehu.tta.gurasapp;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import eus.ehu.tta.gurasapp.view.VideoPlayer;

public class IntroActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        RelativeLayout layout = findViewById(R.id.videoLayout);
        VideoView videoView = VideoPlayer.getVideoPlayer(this, getString(R.string.intro_video), new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });

        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

        videoView.setLayoutParams(relativeParams);

        layout.addView(videoView, 0);
    }

    public void login(View view) {
        startBaseActivity(LoginActivity.class);
    }

    public void register(View view) {
        startBaseActivity(RegisterActivity.class);
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
