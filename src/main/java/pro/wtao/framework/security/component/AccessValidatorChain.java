package pro.wtao.framework.security.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import pro.wtao.framework.security.annotation.CustomAccess;
import pro.wtao.framework.security.component.AnnotationAccessProviders.AnnotationAccessProvider;
import pro.wtao.framework.security.component.AuthorityValidators.AbstractPreAccessValidator;
import pro.wtao.framework.security.component.AuthorityValidators.AccessValidator;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 * <b>权限校验责任链</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:52    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
@Slf4j
public class AccessValidatorChain {

    /**
     * 权限过滤器.
     */
    private final List<? extends AccessValidator> accessValidators;

    private final AnnotationAccessProvider annotationAccessProvider;

    /**
     * <pre>
     * 	无参构造，初始化权限过滤器
     * </pre>
     */
    public AccessValidatorChain(List<? extends AccessValidator> accessValidators, AnnotationAccessProvider annotationAccessProvider) {
        this.accessValidators = accessValidators;
        this.annotationAccessProvider = annotationAccessProvider;
    }

    /**
     * <pre>
     * 	权限校验,按照支持的过滤器检查访问权限
     * </pre>
     */
    public boolean validate(HttpServletRequest request, Authentication authentication) {

        Annotation annotation = annotationAccessProvider.getAccessAnnotation(request);

        // 自定义注解，使用注解指定校验器
        if (annotation instanceof CustomAccess) {
            return customValidate(request, authentication, (CustomAccess)annotation);
        }

        return preValidate(request, authentication, annotation);

    }

    /**
     * 调用自定义校验器
     */
    private boolean customValidate(HttpServletRequest request, Authentication authentication, CustomAccess annotation) {
        boolean result;
        Class<? extends AccessValidator> customValidatorClazz = annotation.validator();

        //获取自定义校验器bean
        AccessValidator accessValidator = accessValidators.stream()
                .filter(validator -> validator.getClass().isAssignableFrom(customValidatorClazz)).findAny()
                .orElseThrow(() -> new AccessDeniedException("未找到" + customValidatorClazz.getName() + "的bean"));
        //调用校验方法
        try {
            Method validate =
                    customValidatorClazz.getMethod("validate", HttpServletRequest.class, Authentication.class);
            result = (Boolean)validate.invoke(accessValidator, request, authentication);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            throw new AccessDeniedException("自定义权限校验失败");
        }
        return result;
    }

    /**
     * 调用预制校验器
     */
    private boolean preValidate(HttpServletRequest request, Authentication authentication, Annotation annotation) {
        return accessValidators.stream().filter(a -> a instanceof AbstractPreAccessValidator)
                .map(a -> (AbstractPreAccessValidator)(a)).filter(a -> a.isSupport(annotation))
                .anyMatch(a -> a.validate(request, authentication));
    }

}

