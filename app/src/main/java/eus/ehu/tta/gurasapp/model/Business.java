package eus.ehu.tta.gurasapp.model;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jontx on 07/01/2018.
 */

public class Business implements BusinessInterface {

    protected final static String GURASAPP_BUSINESS_TAG = "gurasAppBusinessTag";

    private final static String REGISTER = "register";
    private final static String LOGIN = "login";
    private RestClient rest;

    public Business(String url) {
        this.rest = new RestClient(url);
    }

    @Override
    public String register(String username, String email, String password) throws JSONException, IOException {
        String login = null;
        if (username == null || email == null || password == null)
            return null;

        JSONObject json = new JSONObject();
        json.put("name", username);
        json.put("email", email);
        json.put("password", password);

        String response = rest.postJson(json, REGISTER);

        Log.d(GURASAPP_BUSINESS_TAG, "Register: se ha enviado " + json.toString() + " y la respuesta es " + response);

        return response != null && response.compareTo("ERROR") != 0 ? response : null;
    }

    @Override
    public boolean login(String login, String password) throws JSONException, IOException {

        boolean bool = false;

        if (login != null || password != null) {
            JSONObject json = new JSONObject();
            json.put("login", login);
            json.put("password", password);

            String response = rest.postJson(json, LOGIN);

            Log.d(GURASAPP_BUSINESS_TAG, "Login: se ha enviado " + json.toString() + " y la respuesta es " + response);

            if (response != null && response.compareTo("LOGIN OK") == 0)
                bool = true;
        }

        return bool;
    }
}
