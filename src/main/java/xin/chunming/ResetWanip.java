package xin.chunming;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import xin.chunming.bean.Router;

import java.io.IOException;
@Slf4j
public class ResetWanip {

    public static final String DOWN = "link_dhcp_down";
    public static final String UP = "link_dhcp_up";
    public static final String RECONNECT = "link_dhcp_reconnect";

    //private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static void reConn(Router r, String action,OkHttpClient client) throws IOException {

        String json = "{"
                + "\"func_name\":\"wan\","
                + "\"action\":\"" + action + "\","
                + "\"param\":{\"id\":1}"
                + "}";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("http://" + r.getAddr() + "/Action/call")
                .post(body)
                .addHeader(
                        "Cookie",
                        "username=" + r.getUsername()
                                + "; sess_key=" + r.getToken()
                )
                .addHeader("Referer", "http://" + r.getAddr())
                .addHeader("User-Agent", "Mozilla/5.0")
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "Request failed: " + response.code()
                );
            }

            ResponseBody responseBody = response.body();

            if (responseBody != null) {
                System.out.println(responseBody.string());
            }
        }
    }
}