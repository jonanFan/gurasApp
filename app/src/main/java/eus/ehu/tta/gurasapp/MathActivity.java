package eus.ehu.tta.gurasapp;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.Toast;

import eus.ehu.tta.gurasapp.presentation.NetworkChecker;
import eus.ehu.tta.gurasapp.view.VideoPlayer;

public class MathActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math);

        int connType = NetworkChecker.getConnType(this);
        if (connType != -1) {

            if (connType != ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, R.string.no_wifi_warning, Toast.LENGTH_SHORT).show();
            }

            WebView video = VideoPlayer.getIFramePlayer(this, getString(R.string.math_video));
            LinearLayout linearLayout = findViewById(R.id.mathVideo);
            linearLayout.addView(video);
        } else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
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
