package eus.ehu.tta.gurasapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import eus.ehu.tta.gurasapp.presentation.Preferences;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;
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

        loadPreferences();

    }

    @SuppressLint("StaticFieldLeak")
    private void loadPreferences() {
        final String login;
        final String password;

        if (language != null) {
            login = Preferences.getLogin(this);
            password = Preferences.getPassword(this);

            Log.d(GURASAPP_ACTIVITY_TAG, "Login: " + login + " y pass " + password);
            if (login != null && password != null) {

                new ProgressTask<Boolean>(this, getString(R.string.wait_login)) {
                    @Override
                    protected Boolean background() throws Exception {
                        return business.login(login, password);
                    }

                    @Override
                    protected void onFinish(Boolean result) {
                        if (result) {
                            data.putUsername(login);
                            startBaseActivityWithFlags(MenuActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app
                        } else
                            Toast.makeText(context, getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            }

        } else
            super.changeLangToEs(null);
    }

    public void login(View view) {
        startBaseActivity(LoginActivity.class);
    }

    public void register(View view) {
        startBaseActivityWithFlags(RegisterActivity.class, Intent.FLAG_ACTIVITY_NO_HISTORY);
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
