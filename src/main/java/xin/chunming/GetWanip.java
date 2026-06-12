package xin.chunming;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import xin.chunming.bean.Router;

import java.io.IOException;

@Slf4j
public class GetWanip {

    //  private static final OkHttpClient CLIENT = new OkHttpClient();

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final Router router;

    public GetWanip(Router router) {
        this.router = router;
    }

    public String get(OkHttpClient client) throws IOException {

        String json = "{"
                + "\"func_name\":\"wan\","
                + "\"action\":\"show\","
                + "\"param\":{"
                + "\"id\":\"1\","
                + "\"TYPE\":\"support_wisp,total,data,lte_antenna_support\""
                + "}"
                + "}";

        RequestBody body = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .url("http://" + router.getAddr() + "/Action/call")
                .post(body)
                .addHeader(
                        "Cookie",
                        "username=" + router.getUsername()
                                + "; sess_key=" + router.getToken()
                )
                .addHeader(
                        "Referer",
                        "http://" + router.getAddr()
                )
                .addHeader(
                        "User-Agent",
                        "Mozilla/5.0"
                )
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "Request failed: " + response.code()
                );
            }

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                return null;
            }

            String responseJson = responseBody.string();

            JsonNode jsonNode = MAPPER.readTree(responseJson);

            JsonNode ipNode = jsonNode
                    .path("results")
                    .path("data")
                    .get(0)
                    .path("dhcp_ip_addr");

            if (ipNode.isMissingNode()) {
                return null;
            }

            String wanIp = ipNode.asText();

            router.setWanip(wanIp);
            responseBody.close();
            response.close();
            return wanIp;
        }
    }
}