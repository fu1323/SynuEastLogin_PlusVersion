// This file is auto-generated, don't edit it. Thanks.
package xin.chunming.ddns;

import com.aliyun.tea.TeaException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sample {
    public static String wanip;
    public static AliyunBean ab;

    /**
     * <b>description</b> :
     * <p>使用AK&amp;SK初始化账号Client</p>
     *
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.alidns20150109.Client createClient() throws Exception {
        // 工程代码泄露可能会导致 AccessKey 泄露，并威胁账号下所有资源的安全性。以下代码示例仅供参考。
        // 建议使用更安全的 STS 方式，更多鉴权访问方式请参见：https://help.aliyun.com/document_detail/378657.html。
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID。
                .setAccessKeyId(ab.getAccessKeyId())
                // 必填，请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
                .setAccessKeySecret(ab.getAccessKeySecret());
        // Endpoint 请参考 https://api.aliyun.com/product/Alidns
        config.endpoint = "dns.aliyuncs.com";
        return new com.aliyun.alidns20150109.Client(config);
    }

    public static void reg() throws Exception {
        System.out.println("注册阿里DDNS domain:");
        log.info("注册阿里DDNS domain:");
        // java.util.List<String> args = java.util.Arrays.asList(args_);
        com.aliyun.alidns20150109.Client client = Sample.createClient();
        com.aliyun.alidns20150109.models.UpdateDomainRecordRequest updateDomainRecordRequest = new com.aliyun.alidns20150109.models.UpdateDomainRecordRequest()
                .setRecordId(ab.getDomain().getRecordId())
                .setRR(ab.getDomain().getRR())
                .setType(ab.getDomain().getType())
                .setValue(wanip);
        com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
        try {
            com.aliyun.alidns20150109.models.UpdateDomainRecordResponse resp = client.updateDomainRecordWithOptions(updateDomainRecordRequest, runtime);
            com.aliyun.teaconsole.Client.log(com.aliyun.teautil.Common.toJSONString(resp));
        } catch (TeaException error) {
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            log.error(error.getMessage());
            System.out.println(error.getMessage());
            // 诊断地址
            // System.out.println(error.getData().get("Recommend"));
            // com.aliyun.teautil.Common.assertAsString(error.message);
        } catch (Exception _error) {
            log.error(_error.getMessage());
            //TeaException error = new TeaException(_error.getMessage(), _error);
            // 此处仅做打印展示，请谨慎对待异常处理，在工程项目中切勿直接忽略异常。
            // 错误 message
            //System.out.println(error.getMessage());
            // 诊断地址
            //System.out.println(error.getData().get("Recommend"));
            // com.aliyun.teautil.Common.assertAsString(error.message);
        }
        System.out.println("注册成功");
        log.info("注册成功");
    }
}