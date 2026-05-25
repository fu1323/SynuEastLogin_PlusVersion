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
    //static OkHttpClient client = new OkHttpClient();
    public static String CLOSE = "ON";
    static String OPEN = "OFF";

    public static void operate(ip addr, String operater,OkHttpClient client) throws IOException {

        Request.Builder requestBuilder2 = new Request.Builder()
                .url("http://" + addr.getTasmotaip() + "/?m=1");
        Response response2 = client.newCall(requestBuilder2.build()).execute();
        Reader reader = response2.body().charStream();
        BufferedReader bufferedReader = new BufferedReader(reader);
//        StringBuilder stringBuilder = new StringBuilder();
        while (bufferedReader.read() != -1) {
            if (bufferedReader.readLine().contains(operater)) {
                toggle(addr.getTasmotaip(),client);
                log.info("tasmota:toggle ok");
                System.out.println("OK");
            } else {
                System.out.println("已经处于状态" + (operater.equalsIgnoreCase(close.CLOSE)?"开":"关"));
                log.info("已经处于状态" + (operater.equalsIgnoreCase(close.CLOSE)?"开":"关"));
            }
            break;
        }
        response2.body().close();
        response2.close();
    }

    public static void toggle(String ip,OkHttpClient client) throws IOException {
        Request.Builder requestBuilder = new Request.Builder()
                .url("http://" + ip + "/?m=1&o=1");
        Response response = client.newCall(requestBuilder.build()).execute();
        response.close();

    }
}