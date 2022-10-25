package pro.wtao.security.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pro.wtao.framework.security.filter.AbstractAuthenticationFilter;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 *
 * <b>Author:</b> wangtao@360humi.com
 * <b>Date:</b> 2022/9/14 17:23
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/14 17:23    wangtao@360humi.com     new file.
 * </pre>
 */
@Component
public class UsernamePasswordFilter extends AbstractAuthenticationFilter<UsernamePasswordReqVo> {

    public UsernamePasswordFilter(@Autowired AuthenticationSuccessHandler successHandler) {
        super("/login/pwd", HttpMethod.POST,successHandler);
    }

}
