package hale.bc.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;

@SpringBootApplication( exclude = { RedisAutoConfiguration.class } )
public class BlackClothServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlackClothServerApplication.class, args);
    }

}
