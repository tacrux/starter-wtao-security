package pro.wtao.framework.security;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import pro.wtao.framework.security.config.SecurityMainConfiguration;
import pro.wtao.framework.security.config.RedisBeans;
import pro.wtao.framework.security.config.SecurityBeans;
import pro.wtao.framework.security.config.SecurityProperties;

/**
 * @author tacrux
 */
@EnableConfigurationProperties({
        SecurityProperties.class})
@ImportAutoConfiguration({
        SecurityBeans.class,
        SecurityMainConfiguration.class,
        RedisBeans.class})
//@ComponentScan("pro.wtao.security.demo.security")
public class SecurityAutoConfiguration {

}
