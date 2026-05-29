package xin.chunming;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import xin.chunming.bean.PortalUser;
import xin.chunming.bean.Router;
import xin.chunming.ddns.AliyunBean;
import xin.chunming.ddns.Domain;
import xin.chunming.ddns.Sample;
import xin.chunming.tasmota.bean.ip;
import xin.chunming.tasmota.close;
import xin.chunming.tasmota.tasmotacheck;
import xin.chunming.tasmota.bean.tasmotastatus;

import java.io.*;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static xin.chunming.Login.login_lnuni;

@Slf4j
public class Main {
    static tasmotastatus status = new tasmotastatus();
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) throws Exception {
        String path = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();

        // 2. 处理路径（如果是 JAR 运行，获取其父目录）
        File jarFile = new File(path);
        String jarDir = jarFile.getParentFile().getAbsolutePath();

        // 3. 拼接配置文件的完整路径
        File configFile = new File(jarDir, "synueast_config.json");
        if (!configFile.exists()) {
            String s = "{\n" +
                    "  \"portal\": {\n" +
                    "    \"PortalAddr\": \"42.177.95.156:9919\",\n" +
                    "    \"brasIp\": \"218.25.0.169\",\n" +
                    "    \"username\": \"\",\n" +
                    "    \"password_secrete\": \"\",\n" +
                    "    \"auto_check_minutes\": \"3\",\n" +
                    "    \"router\": {\n" +
                    "      \"addr\": \"192.168.9.1\",\n" +
                    "      \"username\": \"\",\n" +
                    "      \"password\": \"\",\n" +
                    "      \"pass\": \"\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"aliyun_ddns\": {\n" +
                    "    \"enable\": \"false\",\n" +
                    "    \"AccessKeyId\": \"\",\n" +
                    "    \"AccessKeySecret\": \"\",\n" +
                    "    \"domain\": {\n" +
                    "      \"RecordId\": \"\",\n" +
                    "      \"RR\": \"\",\n" +
                    "      \"Type\": \"\"\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"tasmota\": {\n" +
                    "    \"enable\": \"false\",\n" +
                    "    \"pcip\": \"\",\n" +
                    "    \"pcipbak\": \"\",\n" +
                    "    \"tasmotaip\": \"\"\n" +
                    "  }\n" +
                    "}";
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
            bufferedWriter.write(s);
            bufferedWriter.flush();
            bufferedWriter.close();
            System.out.println("json配置文件不存在,已生成 请填写后再次运行");
            log.info("json配置文件不存在,已生成 请填写后再次运行");
        } else {


            ObjectMapper objectMapper = new ObjectMapper();
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            System.out.println(stringBuilder.toString());
            JsonNode jsonNode = objectMapper.readTree(stringBuilder.toString());
            Router r = new Router(null, null,
                    jsonNode.get("portal").get("router").get("username").asText(),
                    jsonNode.get("portal").get("router").get("password").asText(),
                    jsonNode.get("portal").get("router").get("addr").asText(),
                    jsonNode.get("portal").get("router").get("pass").asText()
            );
            PortalUser pu = new PortalUser(
                    jsonNode.get("portal").get("username").asText(),
                    jsonNode.get("portal").get("brasIp").asText(),
                    jsonNode.get("portal").get("password_secrete").asText(),
                    jsonNode.get("portal").get("PortalAddr").asText(),
                    jsonNode.get("portal").get("auto_check_minutes").asText()
            );
            AliyunBean aliyunBean = new AliyunBean(
                    jsonNode.get("aliyun_ddns").get("AccessKeyId").asText(),
                    jsonNode.get("aliyun_ddns").get("AccessKeySecret").asText(),
                    new Domain(
                            jsonNode.get("aliyun_ddns").get("domain").get("RecordId").asText(),
                            jsonNode.get("aliyun_ddns").get("domain").get("RR").asText(),
                            jsonNode.get("aliyun_ddns").get("domain").get("Type").asText()
                    ),
                    Boolean.parseBoolean(jsonNode.get("aliyun_ddns").get("enable").asText())
            );
            ip ip = new ip(
                    Boolean.parseBoolean(jsonNode.get("tasmota").get("enable").asText()),
//                    jsonNode.get("tasmota").get("pcipbak").asText(),
                    jsonNode.get("tasmota").get("pcip").asText(),
                    jsonNode.get("tasmota").get("tasmotaip").asText()


            );
            if (ip.isEnadle()) {
                status.setStatus(close.query(ip, client));
                Thread.sleep(3000);
                System.out.println(status.getStatus());
            }
            if (args.length > 0) {
                if (String.valueOf(args[0]).equalsIgnoreCase("login")) {
                    Authorize(r, pu, aliyunBean);
                } else if (String.valueOf(args[0]).equalsIgnoreCase("tasmota")) {
                    status.setStatus(close.query(ip, client));

                    tasmotacheck.checktasmota(ip, status, client);

                }
            } else {
                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

                // scheduleWithFixedDelay: 上次执行完毕后，再等3分钟
                executor.scheduleWithFixedDelay(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    check(r, pu, aliyunBean);
                                    if (ip.isEnadle()) {
                                        if (status.getStatus() != null) {
                                            // System.out.println("tasmotabefore:" + (status.getStatus().equalsIgnoreCase("ON") ? "开" : "关"));
                                            log.info("tasmotabefore:" + (status.getStatus().equalsIgnoreCase("ON") ? "开" : "关"));
                                        }
                                        tasmotacheck.checktasmota(ip, status, client);
                                        // System.out.println("tasmotanow:" + (status.getStatus().equalsIgnoreCase("ON") ? "开" : "关"));

                                        status.setStatus(close.query(ip, client));
                                        Thread.sleep(3000);
                                        log.info("tasmotanow:" + (status.getStatus().equalsIgnoreCase("ON") ? "开" : "关"));
                                    }
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                    StackTraceElement[] stackTrace = e.getStackTrace();
                                    for (StackTraceElement stackTraceElement : stackTrace) {
                                        log.error(stackTraceElement.toString());
                                    }

                                }
                            }
                        },   // 要执行的方法
                        0,              // 初始延迟（立即开始）
                        Integer.parseInt(pu.getAuto_check_minutes()),              // 间隔
                        TimeUnit.MINUTES
                );

                // 阻止主线程退出（daemon线程不需要这行）
                // 如果是独立进程，main退出后executor线程也会结束，需要保活
                //Thread.currentThread().join();
            }
        }
    }

    private static void Authorize(Router r, PortalUser pu, AliyunBean aliyunBean) throws Exception {
        Date date = new Date();
        System.out.println(date);
        log.info(String.valueOf(date));


        System.out.println("获取token...");
        log.info("获取token...");
        r.setToken(GetToken.getToken(r, client));
        System.out.println(r.getToken());
        log.info(r.getToken());
        log.info("重置wanip");
        System.out.println("重置wanip");
        ResetWanip.reConn(r, ResetWanip.DOWN, client);
        Thread.sleep(2000);
        ResetWanip.reConn(r, ResetWanip.UP, client);
        Thread.sleep(2000);
        ResetWanip.reConn(r, ResetWanip.RECONNECT, client);
        Thread.sleep(3500);
        System.out.println("获取wanip...");
        log.info("获取wanip...");
        GetWanip getWanip = new GetWanip(r);

        getWanip.get(client);
        if (r.getWanip() != null) {
            System.out.println(r.getWanip());
            log.info(r.getWanip());
            log.info("请求portal认证...");
            System.out.println("请求portal认证...");

            login_lnuni(r.getWanip(), pu, client);
            if (aliyunBean.isEnable()) {
                Sample.wanip = r.getWanip();
                Sample.ab = aliyunBean;
                Sample.reg();
            }
        } else {
            System.out.println("发生问题!");
            log.info("发生问题!");
        }
    }

    private static void check(Router r, PortalUser pu, AliyunBean aliyunBean) throws Exception {

        Request request = new Request.Builder()
                .url("http://223.5.5.5")  //
                .build();
        Response execute = null;
        try {
            execute = client.newCall(request).execute();
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
        }
        if (execute.body().string().contains("统一接入认证")) {
            Authorize(r, pu, aliyunBean);
            execute.body().close();
            execute.close();
        } else System.out.println("正常");
    }

}
