package pro.wtao.framework.security.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 11:44    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Accessors(chain = true)
public class AuthenticationToken<T extends AuthReq> extends AbstractAuthenticationToken {

    @Setter
    @Getter
    private String principal;

    @Setter
    @Getter
    private T credentials;

    @Setter
    @Getter
    private LoginUser details;

    public AuthenticationToken(String principal, T credentials) {
        super(Collections.emptyList());
        this.principal = principal;
        this.credentials = credentials;
    }
}

