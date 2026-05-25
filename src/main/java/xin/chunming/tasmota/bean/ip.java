package xin.chunming.tasmota.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ip {
   private boolean enadle;
    private String wlanip;
    private String ethip;
    private String tasmotaip;
}
