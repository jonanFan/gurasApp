package eus.ehu.tta.gurasapp;

import android.app.Dialog;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

import eus.ehu.tta.gurasapp.presentation.NetworkChecker;
import eus.ehu.tta.gurasapp.view.AudioPlayer;

public class ScienceActivity extends BaseActivity {

    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);

        LinearLayout linearLayout = findViewById(R.id.audioScience);

        int connType = NetworkChecker.getConnType(this);
        if (connType != -1) {

            if (connType != ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, R.string.no_wifi_warning, Toast.LENGTH_SHORT).show();
            }
            audioPlayer = new AudioPlayer(linearLayout, new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });

            try {
                audioPlayer.setAudioUri(getString(R.string.science_audio));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (audioPlayer != null)
            audioPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (audioPlayer != null)
            audioPlayer.release();
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    public void zoomImage(View view) {

        Dialog dialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_img);


        PhotoView photo = dialog.findViewById(R.id.photoView);
        photo.setImageResource(R.drawable.science);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void toLinks(View view) {
        startBaseActivity(ScienceLinksActivity.class);
    }
}
