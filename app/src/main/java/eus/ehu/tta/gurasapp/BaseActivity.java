package eus.ehu.tta.gurasapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.Locale;

import eus.ehu.tta.gurasapp.model.Business;
import eus.ehu.tta.gurasapp.presentation.Data;

public abstract class BaseActivity extends AppCompatActivity {

    private final static String EUSKERA = "eu";
    private final static String CASTELLANO = "es";

    private final static String EXTRA_DATA = "eus.ehu.tta.gurasapp.data";


    protected Data data;
    protected Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            data = new Data(savedInstanceState.getBundle(EXTRA_DATA));
        else
            data = new Data(getIntent().getExtras()); //Recoge los datos del Data que le pasa la otra activity;

        business = new Business();

        if (data.getLanguage() != null)
            setLocale(data.getLanguage());
    }

    protected <T> void startBaseActivity(Class<T> tClass) {
        Intent intent = newIntent(tClass);
        startActivity(intent);
    }

    protected <T> void startBaseActivityForResult(Class<T> tClass, int requestCode) {
        Intent intent = newIntent(tClass);
        startActivityForResult(intent, requestCode);
    }

    private <T> Intent newIntent(Class<T> tClass) { //Pasa los datos del Data a traves de un Intent
        Intent intent = new Intent(getApplicationContext(), tClass);
        intent.putExtras(data.getBundle());
        return intent;
    }

    private void setLanguage(String lang) {
        if (data.getLanguage() == null || data.getLanguage().compareTo(lang) != 0) {
            data.putLanguage(lang);
            recreate();
        }
    }

    private void setLocale(String lang) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
    }

    public void changeLangToEs(View view) {
        setLanguage(CASTELLANO);
    }

    public void changeLangToEu(View view) {
        setLanguage(EUSKERA);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(EXTRA_DATA, data.getBundle());
        super.onSaveInstanceState(outState);
    }
}
