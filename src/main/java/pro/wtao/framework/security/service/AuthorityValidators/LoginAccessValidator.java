/**
 * 
 */
package pro.wtao.framework.security.service.AuthorityValidators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import pro.wtao.framework.security.annotation.LoginAccess;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * <pre>
 * <b>登录即可访问权限过滤器.</b>
 * <b>Description:</b> 
 *	登录即可访问权限，匹配@LoginAccess
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
public class LoginAccessValidator implements AccessValidator {
	@Autowired
	private AuthenticationTrustResolver trustResolver;


	@Override
	public boolean validate(HttpServletRequest request, Authentication authentication) {
		return !trustResolver.isAnonymous(authentication) && authentication.isAuthenticated();
	}
	

	@Override
	public boolean isSupport(Annotation annotation) {
		return (annotation instanceof LoginAccess);
	}
}
