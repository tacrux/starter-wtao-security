package pro.wtao.framework.security.annotation;



import pro.wtao.framework.security.component.AuthorityValidators.AccessValidator;
import pro.wtao.framework.security.component.AuthorityValidators.RbacAccessValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface CustomAccess {
    /** 自定义权限校验器.默认为RBAC校验器 */
    Class<? extends AccessValidator> validator() default RbacAccessValidator.class;
}
