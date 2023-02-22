package pro.wtao.framework.security;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import pro.wtao.framework.security.config.*;

/**
 * @author tacrux
 */
@EnableConfigurationProperties({
  SecurityProperties.class})
@ImportAutoConfiguration({
  AuthorizationServerAutoConfig.class,
  ResourceServerAutoConfig.class,
  GatewayAutoConfig.class,
  RedisBeans.class})
@ComponentScan("pro.wtao.framework.security")
//确保自定义SecurityFilterChain优先加载
@AutoConfiguration(before = org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class)
public class SecurityAutoConfiguration {

}


