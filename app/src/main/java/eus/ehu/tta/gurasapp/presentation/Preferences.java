package eus.ehu.tta.gurasapp.presentation;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jontx on 10/01/2018.
 */

public class Preferences {

    private final static String SHARED_PREFS = "eus.ehu.tta.gurasapp.shared_prefs";
    private final static String LOGIN = "eus.ehu.tta.gurasapp.shared_prefs.login";
    private final static String PASSWORD = "eus.ehu.tta.gurasapp.shared_prefs.password";
    private final static String LANGUAGE = "eus.ehu.tta.gurasapp.shared_prefs.language";
    private final static String DATE = "eus.ehu.tta.gurasapp.shared_prefs.date";

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

    public static int getDate(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        return preferences.getInt(DATE, 0);
    }

    public static void setDate(Context context, int date) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(DATE, date);
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
