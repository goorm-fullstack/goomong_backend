package R.VD.goomong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoomongApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoomongApplication.class, args);
    }



}
