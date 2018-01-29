package eus.ehu.tta.gurasapp.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;

import eus.ehu.tta.gurasapp.R;

/**
 * Created by jontx on 29/01/2018.
 */

public class AudioController extends MediaController {

    Context context;
    MediaPlayerControl player;
    View view;
    ViewGroup anchorView;
    SeekBar seekBar;
    ImageButton imageButton;
    Handler handler;

    public AudioController(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        inflateView();
    }

    public AudioController(Context context, boolean useFastForward) {
        super(context, useFastForward);
        this.context = context;
        inflateView();

    }

    public AudioController(Context context) {
        super(context);
        this.context = context;
        inflateView();

    }

    private void inflateView() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.audiocontroller, null);
        seekBar = view.findViewById(R.id.seekBar);
        imageButton = view.findViewById(R.id.playButton);
        seekBar.setEnabled(false);
        imageButton.setEnabled(false);
    }

    public void setAnchorView(ViewGroup viewGroup) {
        anchorView = viewGroup;
        FrameLayout.LayoutParams frameParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        anchorView.removeAllViews();


        anchorView.addView(view, frameParams);


    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        seekBar.setEnabled(enabled);
        imageButton.setEnabled(enabled);
    }

    @Override
    public void setMediaPlayer(final MediaPlayerControl player) {
        if (player != null) {
            super.setMediaPlayer(player);
            this.player = player;


            seekBar.setMax(player.getDuration());
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (player != null && fromUser)
                        player.seekTo(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            imageButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (player != null) {

                        if (player.isPlaying()) {
                            player.pause();

                            if (handler != null) {//Si no pongo esto peta
                                handler.removeCallbacksAndMessages(null);
                                handler = null;
                            }

                            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                        } else {
                            player.start();
                            refreshSeekBar();
                            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.pause_icon));
                        }

                    }
                }
            });
        }
    }

    public void stop() {
        if (player != null) {
            if (player.isPlaying()) {
                player.pause();
            }

            player.seekTo(0);
        }

        if (handler != null) {//Si no pongo esto peta
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        imageButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
        seekBar.setProgress(0);
    }

    private void refreshSeekBar() {
        if (seekBar != null) {
            handler = new Handler();

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (player != null && seekBar != null) {
                        int currentPosition = player.getCurrentPosition();
                        seekBar.setProgress(currentPosition);

                        /*if (currentPosition + 1000 >= player.getDuration()) {
                            if (handler != null) {//Si no pongo esto peta
                                handler.removeCallbacksAndMessages(null);
                                handler = null;
                            }

                            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));
                            seekBar.setProgress(0);
                        }*/
                    }
                    if (handler != null)
                        handler.postDelayed(this, 1000);
                }
            });

        }
    }

    public void finished() {
        if (handler != null) {//Si no pongo esto peta
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }

        if (imageButton != null)
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.play_icon));

        if (seekBar != null)
            seekBar.setProgress(0);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (handler != null) {
                handler.removeCallbacksAndMessages(null);
                handler = null;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void show() {
        show(0);
    }

    @Override
    public void show(int timeout) {
        super.show(0);
    }

    @Override
    public void hide() {
        if (handler != null) {//Si no pongo esto peta
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        super.hide();
    }
}
