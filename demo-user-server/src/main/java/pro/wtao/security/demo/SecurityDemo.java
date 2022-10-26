package pro.wtao.security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
// 不指定包路径导致默认的安全过滤器链被加载
@ComponentScan({"pro.wtao.framework.security","pro.wtao.security.demo"})
public class SecurityDemo {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDemo.class, args);
    }

}
