package xin.chunming.tasmota;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xin.chunming.tasmota.bean.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

@Slf4j
public class close {

    public static void operate(ip addr, String operater, OkHttpClient client) throws IOException {

        Request.Builder requestBuilder2 = new Request.Builder()
                .url("http://" + addr.getTasmotaip() + "/?m=1");
        Response response2 = client.newCall(requestBuilder2.build()).execute();
        Reader reader = response2.body().charStream();
        BufferedReader bufferedReader = new BufferedReader(reader);
//        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.read() != -1) {
            String s = bufferedReader.readLine();
            System.out.println(s);
            System.out.println(operater);
            if (s.contains(operater.equalsIgnoreCase("OFF")?"ON":"OFF")) {
                toggle(addr.getTasmotaip(), client);
                log.info("tasmota:toggle ok");
                System.out.println("OK");
            } else {
                System.out.println("已经处于状态" + (operater.equalsIgnoreCase("ON") ? "开" : "关"));
                log.info("已经处于状态" + (operater.equalsIgnoreCase("ON") ? "开" : "关"));
            }
            break;
        }
        response2.body().close();
        response2.close();
    }

    public static void toggle(String ip, OkHttpClient client) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url("http://" + ip + "/?m=1&o=1");
        Response response = client.newCall(requestBuilder.build()).execute();
        response.close();

    }

    public static String query(ip addr, OkHttpClient client) throws IOException {
        Request.Builder requestBuilder2 = new Request.Builder()
                .url("http://" + addr.getTasmotaip() + "/?m=1");
        Response response2 = client.newCall(requestBuilder2.build()).execute();
        String resp = response2.body().string();
        return resp.contains("ON")?"ON":"OFF";

    }
}
