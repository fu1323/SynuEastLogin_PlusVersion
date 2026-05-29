package xin.chunming.tasmota;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import xin.chunming.tasmota.bean.*;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class tasmotacheck {
    public static void checktasmota(ip ip, tasmotastatus now, OkHttpClient client) throws Exception {

        AtomicInteger court = new AtomicInteger(0);
        //int temp = 0;
        ping.main(ip.getEthip(), court);
        System.out.println(court.get());
//        if (court.get() != 0) {
//            temp = court.get();
//
//            if (!ip.getWlanip().isEmpty() || !(ip.getWlanip() == null) || !(ip.getWlanip().equalsIgnoreCase("null")) || !ip.getWlanip().isBlank()) {
//                ping.main(ip.getWlanip(), court);
//            }
//        }
       // System.out.println(court.get());
        if (court.get()>0) {
            try {
                if (now != null) {
                    if (!now.getStatus().equalsIgnoreCase("OFF")) {
                       // System.out.println(1);
                        close.operate(ip, "OFF",client);
                        now.setStatus("OFF");
                    }
                }
//                else {
//                    close.operate(ip, close.CLOSE,client);
//                    now.setStatus(close.CLOSE);
//
//                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                if (now != null) {
                    if (!now.getStatus().equalsIgnoreCase("ON")) {
                        close.operate(ip, "ON",client);
                        now.setStatus("ON");

                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
