package pro.wtao.framework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import pro.wtao.framework.security.constants.AccessValidateMode;
import pro.wtao.framework.security.constants.ServerType;

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
@Data
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
public class SecurityProperties {
    public static final String PREFIX = "wtao.security";

    private Client client = new Client();
    /**
     * jwt有效时间
     */
    public long jwtExp = 10 * 60 * 1000L;

    private ServerType serverType = ServerType.RESOURCE_SERVER;

    private AccessValidateMode accessValidateMode = AccessValidateMode.LOCAL;

    @Data
    public static class Client{
        private String systemCode;

        private String[] publicUrls;
    }


}
