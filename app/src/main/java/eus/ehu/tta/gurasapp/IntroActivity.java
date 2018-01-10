package eus.ehu.tta.gurasapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import eus.ehu.tta.gurasapp.presentation.Preferences;
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

    private void loadPreferences() {
        String language = Preferences.getLanguage(this);
        String login;
        String password;

        if (language != null) {
            login = Preferences.getLogin(this);
            password = Preferences.getPassword(this);

            Log.d(GURASAPP_ACTIVITY_TAG, "Login: " + login + " y pass " + password);
            if (login != null && password != null) {
                if (business.login(login, password)) {
                    data.putUsername(login);
                    startBaseActivityWithFlags(MenuActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app
                } else
                    Toast.makeText(this, R.string.bad_login, Toast.LENGTH_SHORT).show();
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
