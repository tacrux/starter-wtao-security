package pro.wtao.framework.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import static pro.wtao.framework.security.config.SecurityProperties.PREFIX;

/**
 * <pre>
 * <b>主配置读取类</b>
 * <b>Description:</b>
 * <b>Copyright:</b> Copyright 2022 Wangtao. All rights reserved.
 * <b>Changelog:</b>
 *   Ver   		Date                    Author               	 Detail
 *   ----------------------------------------------------------------------
 *   1.0   2022/9/30 10:45    Wangtao     new file.
 * </pre>
 *
 * @author Wangtao
 * @since 2022/9/30
 */
@ConfigurationProperties(prefix = PREFIX)
public class SecurityProperties {
    public static final String PREFIX = "wtao.security";
    /**
     * jwt key
     */
    public static String jwtKey = "wtao-token-key";
    /**
     * jwt有效时间
     */
    public static long jwtExp = 10 * 60 * 1000L;
}
