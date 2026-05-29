package xin.chunming.tasmota;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicInteger;
@Slf4j
public class ping {
    public static void main(String ip, AtomicInteger a) {
        try {
            // 使用 ProcessBuilder
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("ping", ip,"-c" ,"4");
            Process process = processBuilder.start();

            // 读取标准输出
            String output = readStream(process.getInputStream(),a);
            log.info("tasmota:ping_output:{}",output);
            System.out.println(output);

            // 读取错误输出
            String error = readStream(process.getErrorStream(),a);
            if (!error.isEmpty()) {
                System.err.println("Error:\n" + error);
            }
            String out = output+error;
            if (out.contains("100% packet loss")||out.contains("Request timeout")||out.contains("Destination Host Unreachable")) {
                a.incrementAndGet();
            }

            // 等待命令完成
            int exitCode = process.waitFor();
            System.out.println("Exit Code: " + exitCode);
           log.info("tasmota:Exit Code: " + exitCode);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static String readStream(InputStream inputStream, AtomicInteger a) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                  result.append(line);


            }
        }
        return result.toString();
    }
}