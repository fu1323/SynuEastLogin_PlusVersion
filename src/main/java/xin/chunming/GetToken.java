package xin.chunming;

import okhttp3.*;
import xin.chunming.*;
import xin.chunming.bean.Router;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetToken {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    public static String getToken(Router r) throws IOException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String bodyJson = "{"
                + "\"username\":\"" + r.getUsername() + "\","
                + "\"passwd\":\"" + r.getPassword() + "\","
                + "\"pass\":\"" + r.getPass() + "\","
                + "\"remember_password\":\"\""
                + "}";

        RequestBody body = RequestBody.create(JSON, bodyJson);

        Request request = new Request.Builder()
                .url("http://" + r.getAddr() + "/Action/login")
                .post(body)
                .addHeader("User-Agent",
                        "Mozilla/5.0")
                .addHeader("Cookie",
                        "username=" + r.getUsername() + "; sess_key=")
                .build();

        try (Response response = CLIENT.newCall(request).execute()) {

            String cookie = response.header("Set-Cookie");

            if (cookie == null) {
                return null;
            }

            Pattern pattern = Pattern.compile("=(.*?);");
            Matcher matcher = pattern.matcher(cookie);

            if (matcher.find()) {
                return matcher.group(1);
            }

            return null;
        }
    }
}