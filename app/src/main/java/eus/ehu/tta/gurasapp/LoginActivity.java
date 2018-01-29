package eus.ehu.tta.gurasapp;

import android.Manifest;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import eus.ehu.tta.gurasapp.model.Forum;
import eus.ehu.tta.gurasapp.model.Forums;
import eus.ehu.tta.gurasapp.presentation.LocalStorage;
import eus.ehu.tta.gurasapp.presentation.Preferences;
import eus.ehu.tta.gurasapp.presentation.ProgressTask;

public class LoginActivity extends BaseActivity {

    private boolean storeData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        String username = data.getUsername();

        if (username != null) {
            ((EditText) findViewById(R.id.loginUsername)).setText(username);
        }
    }

    public void login(View view) {
        final String login = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
        final String pass = ((EditText) findViewById(R.id.loginPassword)).getText().toString();
        storeData = ((CheckBox) findViewById(R.id.loginCheckbox)).isChecked();

        if (!login.isEmpty() && !pass.isEmpty()) {

            new ProgressTask<Boolean>(this, getString(R.string.wait_login)) {
                @Override
                protected Boolean background() throws Exception {
                    return business.login(login, pass);
                }

                @Override
                protected void onFinish(Boolean result) {
                    if (result) {
                        if (storeData) {
                            Preferences.setLogin(context, login);
                            Preferences.setPassword(context, pass);
                        }

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

        } else
            Toast.makeText(this, R.string.not_filled, Toast.LENGTH_SHORT).show();
    }

    private void getForums() {
        new ProgressTask<Forums>(this, null) {
            @Override
            protected Forums background() throws Exception {
                //return business.getForums(login, Preferences.getDate(context));
                return business.getForums(data.getUsername(), 0);
            }

            @Override
            protected void onFinish(Forums result) {
                if (result != null && result.getTotal() != 0) {
                    if (storeData) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DATE);
                        int date = year * 10000 + month * 100 + day;
                        Log.d(GURASAPP_ACTIVITY_TAG, "EL date es " + date);
                        Preferences.setDate(context, date);
                    }

                    LocalStorage.putForums(context, result);


                    final List<Forum> forumList = result.getForums();
                    for (int i = 0; i < result.getTotal(); i++) {
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
                } else {
                    LocalStorage.deleteForums(context);
                }
            }
        }.execute();
    }

    @Override
    public void changeLangToEs(View view) {
        super.changeLangToEs(view);
    }

    @Override
    public void changeLangToEu(View view) {
        super.changeLangToEu(view);
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
        }
    }
}
