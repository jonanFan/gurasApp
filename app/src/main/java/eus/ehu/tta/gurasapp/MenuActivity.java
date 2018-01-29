package eus.ehu.tta.gurasapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import eus.ehu.tta.gurasapp.presentation.LocalStorage;
import eus.ehu.tta.gurasapp.presentation.Preferences;

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(R.layout.actionbar_menu, null);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(actionBarLayout);

    }

    public void toMath(View view) {
        startBaseActivity(MathActivity.class);
    }

    public void toScience(View view) {
        startBaseActivity(ScienceActivity.class);
    }

    public void toForums(View view) {
        startBaseActivity(ForumsActivity.class);
        //Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
    }

    public void logout(View view) {
        final Context context = this;

        TextView title = new TextView(this);
        title.setText("titulo");
        title.setGravity(Gravity.CENTER);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        //TODO
        alertDialog.setCustomTitle(title);
        //TODO
        alertDialog.setMessage("TE SALES");
        alertDialog.setPositiveButton("si", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    //Borrar posible basura de un user anterior
                    LocalStorage.deleteForums(context);
                    File dir = new File(String.format("%s/%s/%s", Environment.getExternalStorageDirectory(), getApplicationContext().getPackageName(), AUDIO_DIR));

                    if (dir.exists()) {
                        for (File file : dir.listFiles())
                            file.delete();
                    }

                    Preferences.clearPreferences(context);
                    startBaseActivityWithFlags(IntroActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app

                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, DELETE_PERMISSION_CODE);
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case DELETE_PERMISSION_CODE: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) { //Si no los ha aceptado no se ha guardado nothing

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

                Preferences.clearPreferences(this);
                startBaseActivityWithFlags(IntroActivity.class, Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); //Ñapa para hacer que cuando le des a tras en el menu se salga la app

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
