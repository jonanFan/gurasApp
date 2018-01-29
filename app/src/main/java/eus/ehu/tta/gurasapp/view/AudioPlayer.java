package eus.ehu.tta.gurasapp.view;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.MediaController;

import java.io.IOException;

/**
 * Created by jontx on 07/01/2018.
 */

public class AudioPlayer implements MediaController.MediaPlayerControl, MediaPlayer.OnPreparedListener {

    private ViewGroup view;
    private MediaPlayer player;
    private AudioController controller;

    public AudioPlayer(ViewGroup view, final Runnable onExit) {
        this.view = view;
        player = new MediaPlayer();
        player.setOnPreparedListener(this);

        controller = new AudioController(view.getContext()) {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    release();
                    onExit.run();
                }

                return super.dispatchKeyEvent(event);
            }
        };

        controller.setAnchorView(view);
        controller.show(0);
    }

    public void setAudioUri(String url) throws IOException {
        if (player != null) {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(view.getContext(), Uri.parse(url));
            player.prepareAsync();
            //player.start();
        }
    }

    public void setAudioUri(Uri uri) throws IOException {
        if (player != null) {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(view.getContext(), uri);
            player.prepareAsync();
            //player.start();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (controller != null) {
            controller.setMediaPlayer(this);
            controller.setEnabled(true);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.seekTo(0);
                    controller.finished();
                }
            });
          /*  controller.setAnchorView(view);
            controller.show(0);*/
        }
    }

    @Override
    public void start() {

        if (player != null)
            player.start();
    }

    @Override
    public void pause() {
        if (player != null)
            player.pause();
    }

    public void stop() {
        if (controller != null) {
            controller.stop();
        }
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
        return player != null ? player.getDuration() : 0;
    }

    @Override
    public int getCurrentPosition() {

        return player != null ? player.getCurrentPosition() : 0;
    }

    @Override
    public void seekTo(int pos) {

        if (player != null)
            player.seekTo(pos);
    }

    @Override
    public boolean isPlaying() {
        return player != null ? player.isPlaying() : false;
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
        return player != null ? player.getAudioSessionId() : 0;
    }
}
