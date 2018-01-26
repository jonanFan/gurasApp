package eus.ehu.tta.gurasapp.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jontx on 07/01/2018.
 */

public class Business implements BusinessInterface {

    protected final static String GURASAPP_BUSINESS_TAG = "gurasAppBusinessTag";

    private final static String REGISTER = "register";
    private final static String LOGIN = "login";
    private final static String TEST = "getTests";
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

        Log.d(GURASAPP_BUSINESS_TAG, REGISTER + ": se ha enviado " + json.toString() + " y la respuesta es " + response);

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

            Log.d(GURASAPP_BUSINESS_TAG, LOGIN + ": se ha enviado " + json.toString() + " y la respuesta es " + response);

            if (response != null && response.compareTo("LOGIN OK") == 0)
                bool = true;
        }

        return bool;
    }

    @Override
    public Tests getTest(String login) throws Exception {

        Tests tests = null;

        if (login != null) {
            JSONObject json = rest.getJson(String.format("%s?%s=%s", TEST, LOGIN, login));
            if (json != null) {
                Log.d(GURASAPP_BUSINESS_TAG, TEST + ": se ha enviado con login " + login + " y la respuesta es " + json.toString());
                tests = new Tests();
                tests.setTotal(json.getInt("total"));

                if (tests.getTotal() != 0 && !json.isNull("test")) {

                    List<Test> testArray = new ArrayList<>();
                    JSONArray jsonArray = json.getJSONArray("test");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject testJSON = jsonArray.getJSONObject(i);

                        if (!testJSON.isNull("questionES") && !testJSON.isNull("questionEU") && !testJSON.isNull("answerES") && !testJSON.isNull("answerEU") && !testJSON.isNull("correctAnswer")) {
                            Test test = new Test();
                            String questionES = testJSON.getString("questionES");
                            String questionEU = testJSON.getString("questionEU");


                            List<String> question = new ArrayList<>();
                            question.add(questionES);
                            question.add(questionEU);


                            JSONArray answerESJSON = testJSON.getJSONArray("answerES");
                            JSONArray answerEUJSON = testJSON.getJSONArray("answerEU");

                            List<String> answerES = new ArrayList<>();
                            for (int j = 0; j < answerESJSON.length(); j++) {
                                answerES.add(answerESJSON.getString(j));
                                // Log.d(GURASAPP_BUSINESS_TAG, "El string ES introducido es " + answerES.get(j));
                            }

                            List<String> answerEU = new ArrayList<>();
                            for (int j = 0; j < answerEUJSON.length(); j++) {
                                answerEU.add(answerEUJSON.getString(j));
                                //  Log.d(GURASAPP_BUSINESS_TAG, "El string EU introducido es " + answerEU.get(j));
                            }


                            List<List<String>> answer = new ArrayList<>();
                            answer.add(answerES);
                            answer.add(answerEU);

                            if (!testJSON.isNull("adviceES") && !testJSON.isNull("adviceEU")) {
                                String adviceES = testJSON.getString("adviceES");
                                String adviceEU = testJSON.getString("adviceEU");


                                List<String> advice = new ArrayList<>();
                                advice.add(adviceES);
                                advice.add(adviceEU);
                                test.setAdvice(advice);
                            }

                            test.setQuestion(question);
                            test.setAnswer(answer);
                            test.setCorrectAns(testJSON.getInt("correctAnswer"));
                            testArray.add(test);
                        }
                    }

                    tests.setTests(testArray);

                }
            }
        }

        return tests;
    }
}
