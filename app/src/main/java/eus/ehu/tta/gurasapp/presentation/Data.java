package eus.ehu.tta.gurasapp.presentation;

import android.os.Bundle;

import eus.ehu.tta.gurasapp.model.Tests;

/**
 * Created by jontx on 06/01/2018.
 * En esta clase se guardan los datos a mantener para todas las actividades. Por ejemplo, login.
 */

public class Data {

    private final static String EXTRA_USERNAME = "eus.ehu.tta.gurasapp.username";
    private final static String EXTRA_TESTS = "eus.ehu.tta.gurasapp.tests";
    //private final static String EXTRA_LANGUAGE = "eus.ehu.tta.gurasapp.language";


    private final Bundle bundle;

    public Data(Bundle bundle) {
        if (bundle == null) {
            bundle = new Bundle();
        }

        this.bundle = bundle;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void putUsername(String username) {
        bundle.putString(EXTRA_USERNAME, username);
    }

    public String getUsername() {
        return bundle.getString(EXTRA_USERNAME, null);
    }

    public void putTests(Tests tests) {
        bundle.putSerializable(EXTRA_TESTS, tests);
    }

    public Tests getTests() {
        return (Tests) bundle.getSerializable(EXTRA_TESTS);
    }


    /*public void putLanguage(String language) {
        bundle.putString(EXTRA_LANGUAGE, language);
    }

    public String getLanguage() {
        return bundle.getString(EXTRA_LANGUAGE, null);
    }*/
}
