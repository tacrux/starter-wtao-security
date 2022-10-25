package pro.wtao.security.demo.security;

import lombok.Data;
import pro.wtao.framework.security.model.AuthReq;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 *
 * <b>Author:</b> wangtao@360humi.com
 * <b>Date:</b> 2022/9/14 17:22
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/14 17:22    wangtao@360humi.com     new file.
 * </pre>
 */
@Data
public class UsernamePasswordReqVo extends AuthReq {
    private String username;
    private String password;
    @Override
    public String getPrincipal() {
        return username;
    }
}
