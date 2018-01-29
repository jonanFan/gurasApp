package eus.ehu.tta.gurasapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import eus.ehu.tta.gurasapp.model.Forum;
import eus.ehu.tta.gurasapp.model.Forums;
import eus.ehu.tta.gurasapp.presentation.LocalStorage;
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

                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                getForums();
                                startBaseActivityWithFlags(MenuActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app
                            } else {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
                            }


                        } else
                            Toast.makeText(context, getString(R.string.bad_login), Toast.LENGTH_SHORT).show();
                    }
                }.execute();
            } else {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    //Borrar posible basura de un user anterior
                    LocalStorage.deleteForums(this);
                    File dir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR));

                    if (dir.exists()) {
                        for (File file : dir.listFiles())
                            file.delete();
                    }

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DELETE_PERMISSION_CODE);
                }
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


    private void getForums() {
        new ProgressTask<Forums>(this, null) {
            @Override
            protected Forums background() throws Exception {
                return business.getForums(data.getUsername(), Preferences.getDate(context));
                // return business.getForums(login, 0);
            }

            @Override
            protected void onFinish(Forums result) {
                Forums forums = null;
                if (result != null && result.getTotal() != 0) {

                    int date = Preferences.getDate(context);

                    forums = LocalStorage.getForums(context);

                    if (forums != null) {
                        List<Forum> forumList = forums.getForums();

                        for (int i = forums.getTotal() - 1; i >= 0; i--) { //Elimina los elementos repetidos
                            if (forumList.get(i).getDate() >= date) {
                                forumList.remove(i);
                                forums.setTotal(forums.getTotal() - 1);
                            }
                        }

                        List<Forum> resultList = result.getForums();

                        for (int i = 0; i < result.getTotal(); i++) //Elimina los actualizados
                            for (int j = forums.getTotal() - 1; j >= 0; j--) {
                                if (resultList.get(i).getQuestion().compareTo(forumList.get(j).getQuestion()) == 0) {
                                    forumList.remove(j);
                                    forums.setTotal(forums.getTotal() - 1);
                                }
                            }

                        for (int i = 0; i < result.getTotal(); i++) {
                            forumList.add(resultList.get(i));
                            forums.setTotal(forums.getTotal() + 1);
                        }

                        forums.setForums(forumList);
                        LocalStorage.putForums(context, forums);
                    } else {
                        LocalStorage.putForums(context, result);
                        forums = result;
                    }

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int day = calendar.get(Calendar.DATE);
                    date = year * 10000 + month * 100 + day;
                    // Log.d(GURASAPP_ACTIVITY_TAG, "EL date es " + date);
                    Preferences.setDate(context, date);

                    // Log.d(GURASAPP_ACTIVITY_TAG, "La salida es " + LocalStorage.getForums(context));


                    final List<Forum> forumList = forums.getForums();
                    for (int i = 0; i < forums.getTotal(); i++) {

                        File file = new File(String.format("%s/%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR, forumList.get(i).getQuestion()));

                        if (!file.exists()) {
                            final int finalI = i;
                            new ProgressTask<Void>(context, null) {
                                @Override
                                protected Void background() throws Exception {
                                    business.getForumQuestion(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR), forumList.get(finalI).getQuestion());
                                    return null;
                                }

                                @Override
                                protected void onFinish(Void result) {
                                    Log.d(GURASAPP_ACTIVITY_TAG, "He llegado al finish"); //TODO
                                }
                            }.execute();
                        }
                    }
                }
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_PERMISSION_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getForums();
                } else {
                    Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show();
                }

                startBaseActivityWithFlags(MenuActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app
                break;
            }
            case DELETE_PERMISSION_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Borrar posible basura de un user anterior
                    LocalStorage.deleteForums(this);
                    File dir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR));

                    if (dir.exists()) {
                        for (File file : dir.listFiles())
                            file.delete();
                    }

                } else {
                    Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
