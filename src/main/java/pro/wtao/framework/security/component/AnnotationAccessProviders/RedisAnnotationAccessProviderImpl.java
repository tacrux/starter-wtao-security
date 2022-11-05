package pro.wtao.framework.security.component.AnnotationAccessProviders;

import org.springframework.data.redis.core.RedisTemplate;
import pro.wtao.framework.security.config.SecurityProperties;
import pro.wtao.framework.security.constants.Constant;
import pro.wtao.framework.security.model.RequestMatchInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * <pre>
 * <b></b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/11/4 16:46    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/11/4
 */
public class RedisAnnotationAccessProviderImpl extends AbstractAnnotationAccessProvider {
    private static final String REDIS_KEY_PREFIX = Constant.SECURITY_NAMESPACE + "annotationAccess:";
    private final SecurityProperties properties;
    private final RedisTemplate<String, ?> redisTemplate;


    public RedisAnnotationAccessProviderImpl(SecurityProperties properties, RedisTemplate<String, ?> redisTemplate) {
        this.properties = properties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    void putAllAnnotationMappings(Map<RequestMatchInfo, Annotation> annotationMappings) {
        redisTemplate.opsForHash().putAll(genRedisKey(), annotationMappings);
    }

    private String genRedisKey() {
        return REDIS_KEY_PREFIX + properties.getClient().getSystemCode();
    }

    @Override
    public Annotation getAccessAnnotation(HttpServletRequest request) {
        RequestMatchInfo hashKey = RequestMatchInfo.fromRequest(request);
        return (Annotation) redisTemplate.opsForHash().get(genRedisKey(), hashKey);
    }
}
