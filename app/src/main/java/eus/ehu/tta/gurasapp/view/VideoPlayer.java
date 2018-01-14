package eus.ehu.tta.gurasapp.view;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by jontx on 07/01/2018.
 */

public class VideoPlayer {
    public static VideoView getVideoPlayer(Context context, String url, final Runnable onExit) {
        VideoView video = new VideoView(context);
        video.setVideoURI(Uri.parse(url));

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);

        MediaController controller = new MediaController(context) {
            @Override
            public void hide() { //Con esto conseguimos que no se escondan los controles
            }

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    onExit.run();
                return super.dispatchKeyEvent(event);
            }
        };

        controller.setAnchorView(video);
        video.setMediaController(controller);
        //video.seekTo(0); //Se puede usar para poner como preview una parte del video
        return video;
    }

    public static WebView getIFramePlayer(Context context, String url) {
        WebView video = new WebView(context);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);

        WebSettings settings = video.getSettings();
        settings.setJavaScriptEnabled(true);

        video.loadData("<iframe width=\"100%\" height=\"100%\"+\n" +
                "src='" + url + "' ></iframe>", "text/html", null);
        video.setBackgroundColor(Color.TRANSPARENT);
        video.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);

        video.setVerticalScrollBarEnabled(false);
        video.setHorizontalScrollBarEnabled(false);
        return video;
    }
}
