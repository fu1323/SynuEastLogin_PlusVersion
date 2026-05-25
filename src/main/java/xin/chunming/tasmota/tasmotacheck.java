package xin.chunming.tasmota;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import xin.chunming.tasmota.bean.*;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class tasmotacheck {
    public static String checktasmota(ip ip, String now, OkHttpClient client) throws Exception {

        AtomicInteger court = new AtomicInteger(0);
        int temp = 0;
        ping.main(ip.getEthip(), court);
        System.out.println(court.get());
        if (court.get() != 0) {
            temp = court.get();

            if (!ip.getWlanip().isEmpty() || !(ip.getWlanip() == null) || !(ip.getWlanip().equalsIgnoreCase("null")) || !ip.getWlanip().isBlank()) {
                ping.main(ip.getWlanip(), court);
            }
        }
        if (temp > 0 && court.get() - temp != 0) {
            try {
                if (now != null) {
                    if (!now.equalsIgnoreCase(close.CLOSE)) {
                        close.operate(ip, close.CLOSE,client);
                        return close.CLOSE;
                    }
                } else {
                    close.operate(ip, close.CLOSE,client);
                    return close.CLOSE;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (now != null) {
                    if (!now.equalsIgnoreCase(close.OPEN)) {
                        close.operate(ip, close.OPEN,client);
                        return close.OPEN;
                    }
                } else {
                    close.operate(ip, close.OPEN,client);
                    return close.OPEN;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

}
