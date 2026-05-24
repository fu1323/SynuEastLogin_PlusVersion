package xin.chunming.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UnloginBean {
    private String sign;
    private String nonce;
    private String timestamp;
    private String sessionId;
}
