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
import eus.ehu.tta.gurasapp.presentation.Preferences;

public abstract class BaseActivity extends AppCompatActivity {

    protected final static String GURASAPP_ACTIVITY_TAG = "gurasAppActivityTag";
    private final static String URL = "http://u017633.ehu.eus:28080/gurasApp/rest/gurasApp";

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

        business = new Business(URL);

        String lang = Preferences.getLanguage(this);
        if (lang != null)
            setLocale(lang);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    protected <T> void startBaseActivity(Class<T> tClass) {
        Intent intent = newIntent(tClass);
        startActivity(intent);
    }

    protected <T> void startBaseActivityWithFlags(Class<T> tClass, int flags) {
        Intent intent = newIntent(tClass);
        intent.setFlags(flags);
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

    protected void setLanguage(String lang) {
        String storedLang = Preferences.getLanguage(this);
        if (lang != null && (storedLang == null || lang.compareTo(storedLang) != 0)) {
            Preferences.setLanguage(this, lang);
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
