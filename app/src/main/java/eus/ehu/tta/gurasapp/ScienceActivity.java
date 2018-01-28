package eus.ehu.tta.gurasapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;

import eus.ehu.tta.gurasapp.view.AudioPlayer;

public class ScienceActivity extends BaseActivity {

    private AudioPlayer audioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_science);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.audioScience);


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
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audioPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
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


        PhotoView photo = (PhotoView) dialog.findViewById(R.id.photoView);
        photo.setImageResource(R.drawable.science);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    public void toLinks(View view) {
        startBaseActivity(ScienceLinksActivity.class);
    }
}
