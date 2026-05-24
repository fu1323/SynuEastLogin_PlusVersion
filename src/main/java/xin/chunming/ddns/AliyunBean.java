package xin.chunming.ddns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class AliyunBean {
    String AccessKeyId;
    String AccessKeySecret;
    Domain domain;
}
