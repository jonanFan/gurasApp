package eus.ehu.tta.gurasapp.model;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jontx on 12/01/2018.
 */

public class RestClient {
    private final static String GURASAPP_REST_TAG = "gurasAppRestTag";

    private final static String REST_PATH = "gurasApp/rest/gurasApp";
    private final static String AUTH = "Authorization";
    private final String baseUrl;
    private final Map<String, String> properties = new HashMap<>();


    public RestClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setHttpBasicAuth(String user, String password) {
        String basicAuth = Base64.encodeToString(String.format("%s:%s", user, password).getBytes(), Base64.DEFAULT);
        properties.put(AUTH, String.format("Basic %s", basicAuth));
    }

    public void setProperty(String name, String value) {
        properties.put(name, value);
    }

    private HttpURLConnection getConnection(String path) throws IOException {
        URL url = new URL(String.format("%s/%s/%s", baseUrl, REST_PATH, path));
        Log.d(GURASAPP_REST_TAG, "URL con la que se va a conectar: " + baseUrl + "/" + REST_PATH + "/" + path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        for (Map.Entry<String, String> property : properties.entrySet()) {
            conn.setRequestProperty(property.getKey(), property.getValue());
        }
        conn.setUseCaches(false);

        return conn;
    }

    public String getData(String path) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        String data = null;

        try {

            conn = getConnection(path);
            try {
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    data = reader.readLine();
                }

                return data;
            } finally {
                if (reader != null)
                    reader.close();

                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (inputStream != null)
                    inputStream.close();
            }
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    public JSONObject getJson(String path) throws IOException, JSONException {
        String data = getData(path);
        JSONObject json = null;

        if (data != null)
            json = new JSONObject(data);
        return json;
    }

    public int postFile(String path, InputStream inputStream, String filename) throws IOException {
        String boundary = Long.toString(System.currentTimeMillis());
        String newLine = "\r\n";
        String prefix = "--";
        HttpURLConnection conn = null;

        try {
            conn = getConnection(path);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            conn.setDoOutput(true);

            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());

            outputStream.writeBytes(prefix + boundary + newLine);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"filetype\"" + newLine);
            outputStream.writeBytes(newLine);
            outputStream.writeBytes("audio" + newLine);
            outputStream.writeBytes(prefix + boundary + newLine);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + filename + "\"" + newLine);
            outputStream.writeBytes("Content-Type: audio/mpeg" + newLine);
            outputStream.writeBytes(newLine);

            byte[] data = new byte[1024 * 1024]; //Buffer intermedio para enviar el archivo
            int len;

            while ((len = inputStream.read(data)) > 0)
                outputStream.write(data, 0, len);

            outputStream.writeBytes(newLine);
            outputStream.writeBytes(prefix + boundary + prefix + newLine);
            outputStream.close();

            return conn.getResponseCode();
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    public String postJson(final JSONObject json, String path) throws IOException {

        HttpURLConnection conn = null;
        BufferedReader reader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        String data = null;


        try {
            conn = getConnection(path);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            PrintWriter printWriter = new PrintWriter(conn.getOutputStream());
            printWriter.print(json.toString());
            printWriter.close();

            try {
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    inputStream = conn.getInputStream();
                    inputStreamReader = new InputStreamReader(inputStream);
                    reader = new BufferedReader(inputStreamReader);
                    data = reader.readLine();
                }

                return data;
            } finally {
                if (reader != null)
                    reader.close();

                if (inputStreamReader != null)
                    inputStreamReader.close();

                if (inputStream != null)
                    inputStream.close();
            }
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }


}
