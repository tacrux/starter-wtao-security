/**
 *
 */
package pro.wtao.framework.security.component.AuthorityValidators;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pro.wtao.framework.security.annotation.RbacAccess;
import pro.wtao.framework.security.config.SecurityProperties;
import pro.wtao.framework.security.model.LoginUser;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * <pre>
 * <b>系统授权访问过滤器.</b>
 * <b>Description:</b>
 *	根据系统配置授权 用户-角色-资源访问权限过滤
 *	---------------------
 * <b>Author:</b> tacrux
 * <b>Date:</b> 2019年11月12日 下午1:46:59
 * <b>Copyright:</b> Copyright 2022 tacrux. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2019年11月12日 下午1:46:59    tacrux     new file.
 * </pre>
 */
@Slf4j
@Component
public class RbacAccessValidator extends AbstractPreAccessValidator {

    @Autowired
    private AuthenticationTrustResolver trustResolver;
    @Autowired
    private SecurityProperties properties;

    @Override
    public boolean validate(HttpServletRequest request, Authentication authentication) {

        Object details = authentication.getDetails();

        // 匿名用户直接返回false
        if (trustResolver.isAnonymous(authentication) || !authentication.isAuthenticated()) {
            return false;
        }

        LoginUser loginUser = (LoginUser) details;

        // 获取当前系统用户的权限
        String systemCode = properties.getClient().getSystemCode();
        if (StringUtils.isBlank(systemCode)) {
            log.warn("当前未设置系统编码：${wtao.security.client.system-code}");
            return false;
        }
        List<String> systemCodeSplits = Arrays.asList(systemCode.split(","));

        return loginUser.getAuthorities().stream()
                .filter(authority -> systemCodeSplits.contains(authority.getSystemCode()))
                .anyMatch(getRequestMatchInfo(request)::match);
    }

    @Override
    public boolean isSupport(Annotation annotation) {
        return annotation==null || annotation.annotationType().isAssignableFrom(RbacAccess.class);
    }

}

