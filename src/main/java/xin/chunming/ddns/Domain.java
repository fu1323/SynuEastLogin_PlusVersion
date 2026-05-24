package xin.chunming.ddns;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Domain {
    private String RecordId;
    public String RR;
    public String Type;

}
