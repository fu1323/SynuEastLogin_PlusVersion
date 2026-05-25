package xin.chunming;


import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.*;
import okhttp3.Response;
import xin.chunming.bean.PortalUser;


import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
public class Login {
//    private static OkHttpClient client = new OkHttpClient.Builder()
//            .connectTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
//            .readTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
//            .build();


    public static void login_lnuni(String wanip, PortalUser pu, OkHttpClient client) throws MalformedURLException {
        HttpUrl url = HttpUrl.parse("http://" + pu.getPortaladdr() + "/api/portal/auth").newBuilder()
                .addQueryParameter("userName", pu.getName())
                .addQueryParameter("password", pu.getPwdSecrete())
                .addQueryParameter("userIp", wanip)
                .addQueryParameter("brasIp", pu.getBrasIp())
                .build();

        // 2. 构建 Request
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MediaType.parse("application/json"), "{}"))
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .header("Accept-Encoding", "gzip, deflate")
                .header("Accept-Language", "zh-CN,zh;q=0.9")
                .build();

        try (Response response = client.newCall(request).execute()) {
            // 无论成功还是 400/500，都可以通过 response.body() 获取内容
            String s = response.body() != null ? response.body().string() : "";
            if (response.isSuccessful()) {


                log.info(s);
                System.out.println(s);
                System.out.println("Success 登陆成功!");
                log.info("Success 登陆成功!");
                response.close();

            } else {
                response.close();
                System.out.println(s + "请求登录错误");
                log.info(s + "请求登录错误");

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}
