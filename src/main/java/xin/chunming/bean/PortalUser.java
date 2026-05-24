package xin.chunming.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class PortalUser {
    private String name;
    private String brasIp;
    private String pwdSecrete;
    private String portaladdr;
    private String auto_check_minutes;


}
