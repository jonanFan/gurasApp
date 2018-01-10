package eus.ehu.tta.gurasapp.presentation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jontx on 10/01/2018.
 */

public class Preferences {

    private final static String SHARED_PREFS = "eus.ehu.tta.gurasapp.shared_prefs";
    private final static String LOGIN = "eus.ehu.tta.gurasapp.login";
    private final static String PASSWORD = "eus.ehu.tta.gurasapp.password";
    private final static String LANGUAGE = "eus.ehu.tta.gurasapp.language";

    public static String getLogin(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(LOGIN, null);
    }

    public static void setLogin(Context context, String login) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LOGIN, login);
        editor.apply();
    }

    public static String getPassword(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(PASSWORD, null);
    }

    public static void setPassword(Context context, String password) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PASSWORD, password);
        editor.apply();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE, null);
    }

    public static void setLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LANGUAGE, language);
        editor.commit();
    }

    public static void clearPreferences(Context context) {
        //context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit().clear().apply(); //Quiero borrar todo menos el lenguaje
        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE).edit();
        editor.remove(LOGIN);
        editor.remove(PASSWORD);
        editor.commit();
    }
}
