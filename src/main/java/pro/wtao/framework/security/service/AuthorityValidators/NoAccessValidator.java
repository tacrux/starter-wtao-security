/**
 * 
 */
package pro.wtao.framework.security.service.AuthorityValidators;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pro.wtao.framework.security.annotation.NonAccess;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * <pre>
 * <b>无需权限过滤器.</b>
 * <b>Description:</b> 
 *	无需权限，匹配@NoAccess,默认返回true
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
@Component
public class NoAccessValidator extends AbstractPreAccessValidator {


	@Override
	public boolean validate(HttpServletRequest request, Authentication authentication) {
		return true;
	}

	@Override
	public boolean isSupport(Annotation annotation) {
		return annotation!=null && annotation.annotationType().isAssignableFrom(NonAccess.class);
	}

}
