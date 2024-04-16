package org.konradchrzanowski.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication(scanBasePackages = {
        "org.konradchrzanowski.user"
})
@EnableDiscoveryClient
@EnableFeignClients(
        basePackages = "org.konradchrzanowski.clients"
)
@PropertySources(
        {
                @PropertySource("classpath:clients-${spring.profile.active}.properties")
        }
)
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}