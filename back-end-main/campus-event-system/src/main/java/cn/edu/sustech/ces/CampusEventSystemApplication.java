package cn.edu.sustech.ces;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableScheduling
public class CampusEventSystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(CampusEventSystemApplication.class, args);
    }

//    @Bean
//    CommandLineRunner runner(OrderRepository repository) {
//        repository.findAll().forEach(System.out::println);
//        return args -> repository.save(new Order(1, "potato", "I order a lot of potato"));
//    }

}
