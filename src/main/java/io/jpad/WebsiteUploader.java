package io.jpad;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class WebsiteUploader {
    private static final String UPLOAD_URL = "http://jpad.io/upload";
    private static final String PASSWORD_CHECK_URL = "http://jpad.io/login-check";

    static boolean validLogin(String username, char[] password) throws IOException {
        Map<String, Object> m = new HashMap<>();
        m.put("username", username);
        m.put("password", password);
        String r = post("http://jpad.io/login-check", m);
        try {
            return (Integer.parseInt(r) == 1);
        } catch (NumberFormatException e) {

            return false;
        }
    }


    static UploadResult post(String username, String password, Snip snip) throws IOException {
        Map<String, Object> m = snip.toMap();
        m.put("username", username);
        m.put("password", password);
        String json = post("http://jpad.io/upload", m);
        Gson gson = new Gson();
        System.out.println(json);
        return gson.fromJson(json, UploadResult.class);
    }


    private static String post(String websiteUrl, Map<String, Object> params) throws IOException {
        URL url = new URL(websiteUrl);
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            Object o = param.getValue();
            if (o instanceof char[]) {
                o = new String((char[]) o);
            }
            postData.append(URLEncoder.encode(String.valueOf(o), StandardCharsets.UTF_8));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);

        try (InputStream stream = conn.getInputStream()) {
            return CharStreams.toString(new InputStreamReader(stream, Charsets.UTF_8));
        }
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\io\jpad\WebsiteUploader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */