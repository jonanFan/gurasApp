package eus.ehu.tta.gurasapp.view;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by jontx on 07/01/2018.
 */

public class AudioPlayer implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener {

    private View view;
    private MediaPlayer player;
    private MediaController controller;

    public AudioPlayer(View view, final Runnable onExit) {
        this.view = view;
        player = new MediaPlayer();
        player.setOnPreparedListener(this);

        controller = new MediaController(view.getContext()) {

            @Override
            public void show(int timeout) {
                super.show(0);
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    release();
                    onExit.run();
                }

                return super.dispatchKeyEvent(event);
            }
        };

    }

    public void setAudioUri(String url) throws IOException {
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDataSource(view.getContext(), Uri.parse(url));
        player.prepareAsync();
        //player.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView(view);
        controller.show(0);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public void pause() {
        if (player != null)
            player.pause();
    }

    public void stop() {
        if (player != null)
            player.stop();
    }

    public void release() {
        if (controller != null)
            controller.hide();

        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public void seekTo(int pos) {
        player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return player.getAudioSessionId();
    }
}
