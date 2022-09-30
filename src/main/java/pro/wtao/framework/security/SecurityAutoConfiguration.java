package pro.wtao.framework.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import pro.wtao.framework.security.config.SecurityProperties;

/**
 * @author tacrux
 */
@Configuration
@EnableConfigurationProperties({
        SecurityProperties.class})
public class SecurityAutoConfiguration {

}
