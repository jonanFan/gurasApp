package eus.ehu.tta.gurasapp;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import eus.ehu.tta.gurasapp.model.Forum;
import eus.ehu.tta.gurasapp.model.Forums;
import eus.ehu.tta.gurasapp.presentation.LocalStorage;
import eus.ehu.tta.gurasapp.presentation.NetworkChecker;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;
import eus.ehu.tta.gurasapp.view.AudioPlayer;

public class ForumsActivity extends BaseActivity {

    private final int AUDIO_REQUEST_CODE = 1;

    private Dialog dialog;
    private List<AudioPlayer> players;
    private String path;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forums);

        Forums forums = LocalStorage.getForums(this);
        players = new ArrayList<>();
        if (forums != null) {
            createScrollView(forums);
        }
    }

    private void createScrollView(Forums forums) {
        LinearLayout scrollView = findViewById(R.id.scrollForums);
        scrollView.removeAllViews();

        List<Forum> forumsList = forums.getForums();

        LayoutInflater inflater = LayoutInflater.from(ForumsActivity.this);


        for (int i = 0; i < forums.getTotal(); i++) {

            String title = forumsList.get(i).getTitle();
            String question = forumsList.get(i).getQuestion();
            String answer = forumsList.get(i).getAnswer();
            String teacher = forumsList.get(i).getTeacher();

            Log.d(GURASAPP_ACTIVITY_TAG, "Forum: La question es " + question);

            View view = inflater.inflate(R.layout.frame_forum, null, false);
            TextView textView = view.findViewById(R.id.forumTitle);
            textView.setText(title);

            LinearLayout audioLayout = view.findViewById(R.id.forumQuestion);

            final AudioPlayer audioPlayer = new AudioPlayer(audioLayout, new Runnable() {
                @Override
                public void run() {
                    for (AudioPlayer player : players)
                        player.release();
                    finish();
                }
            }) {
                @Override
                public void start() {
                    if (players != null)
                        for (AudioPlayer player : players) {
                            if (player != this) {
                                player.stop();
                            }
                        }
                    super.start();
                }
            };

            try {
                File file = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR), question);
                audioPlayer.setAudioUri(Uri.fromFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            players.add(audioPlayer);

            if (answer != null && !answer.isEmpty()) {
                textView = view.findViewById(R.id.forumAnswerContent);
                textView.setText(String.format("\t%s", answer));
                textView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.forumAnswerTitle).setVisibility(View.VISIBLE);
            }

            if (teacher != null && !teacher.isEmpty()) {
                textView = view.findViewById(R.id.forumTeacherContent);
                textView.setText(String.format("\t%s", teacher));
                textView.setVisibility(View.VISIBLE);
                view.findViewById(R.id.forumTeacherTitle).setVisibility(View.VISIBLE);
            }

            scrollView.addView(view);
        }
    }

    public void recordQuestion(View view) {

        if (players != null)
            for (AudioPlayer player : players) {
                player.stop();

            }

        dialog = new Dialog(this, android.R.style.Theme_Material_Light_Dialog);
        dialog.setContentView(R.layout.dialog_forum);

        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

    }

    public void record(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            record();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
        }
    }

    private void record() {
        EditText editText = dialog.findViewById(R.id.recordTitle);
        String title = editText.getText().toString();

        if (title != null && !title.isEmpty()) {
            if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE))
                Toast.makeText(this, R.string.no_micro, Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, AUDIO_REQUEST_CODE);
                    if (dialog != null)
                        dialog.dismiss();
                } else
                    Toast.makeText(this, R.string.no_app, Toast.LENGTH_SHORT).show();

            }
        } else
            Toast.makeText(this, R.string.title_needed, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case AUDIO_REQUEST_CODE:
                sendSolution(data.getData());
                break;
        }
    }

    private void sendSolution(Uri uri) {
        EditText editText = dialog.findViewById(R.id.recordTitle);
        title = editText.getText().toString();
        Log.d(GURASAPP_ACTIVITY_TAG, "SEND SOL: Tengo la URI " + uri);
        Cursor cursor = getContentResolver().query(uri, null, null, null, null, null);

        try {
            if (cursor != null && cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));

                if (path != null && !path.isEmpty()) {
                    Log.d(GURASAPP_ACTIVITY_TAG, "El path es " + path);
                    sendSolution();
                }
            }
        } finally

        {
            if (cursor != null)
                cursor.close();
        }

    }

    void sendSolution() {
        int connType = NetworkChecker.getConnType(this);
        if (connType != -1) {

            if (connType != ConnectivityManager.TYPE_WIFI) {
                Toast.makeText(this, R.string.no_wifi_warning, Toast.LENGTH_SHORT).show();
            }

            new ProgressTask<Forum>(this, null) {
                @Override
                protected Forum background() throws Exception {

                    Forum forum;
                    File dir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR));
                    if (!dir.exists())
                        dir.mkdirs();

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DATE);
                    int date = year * 10000 + month * 100 + day;

                    File src = new File(path);

                    String filename = String.format("%s-%s-%s.%s", data.getUsername(), title, date, path.substring(path.lastIndexOf(".") + 1));
                    File dst = new File(dir, filename);

                    FileChannel inChannel = new FileInputStream(src).getChannel();
                    FileChannel outChannel = new FileOutputStream(dst).getChannel();

                    try {
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                    } finally {
                        if (inChannel != null)
                            inChannel.close();

                        if (outChannel != null)
                            outChannel.close();

                        src.delete();
                    }

                    FileInputStream inputStream = new FileInputStream(dst);

                    forum = new Forum();
                    forum.setTitle(title);
                    forum.setQuestion(filename);
                    forum.setDate(date);

                    return business.addQuestion(data.getUsername(), forum, inputStream) ? forum : null;
                }

                @Override
                protected void onFinish(Forum result) {
                    if (result != null) {
                        Forums forums = LocalStorage.getForums(context);
                        if (forums != null) {
                            forums.getForums().add(result);
                            forums.setTotal(forums.getTotal() + 1);
                        } else {
                            forums = new Forums();
                            forums.setTotal(1);

                            List<Forum> list = new ArrayList<>();
                            list.add(result);
                            forums.setForums(list);
                        }
                        LocalStorage.putForums(context, forums);
                        recreate();
                    }
                }
            }.execute();
        } else
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    record();
                } else {
                    Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
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
