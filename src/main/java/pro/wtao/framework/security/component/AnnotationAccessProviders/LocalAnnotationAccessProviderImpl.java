package pro.wtao.framework.security.component.AnnotationAccessProviders;

import pro.wtao.framework.security.model.RequestMatchInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 * <b>注解-控制器映射 持有者</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/10/8 17:28    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/10/8
 */
public class LocalAnnotationAccessProviderImpl extends AbstractAnnotationAccessProvider {

    protected static final Map<RequestMatchInfo, Annotation> ANNOTATION_MAPPINGS = new HashMap<>();


    @Override
    void putAllAnnotationMappings(Map<RequestMatchInfo, Annotation> annotationMappings) {
        this.ANNOTATION_MAPPINGS.putAll(annotationMappings);
    }

    @Override
    public Annotation getAccessAnnotation(HttpServletRequest request) {
        return ANNOTATION_MAPPINGS.get(RequestMatchInfo.fromRequest(request));
    }

    public Map<RequestMatchInfo, Annotation> getAnnotationMappings() {
        return ANNOTATION_MAPPINGS;
    }
}
