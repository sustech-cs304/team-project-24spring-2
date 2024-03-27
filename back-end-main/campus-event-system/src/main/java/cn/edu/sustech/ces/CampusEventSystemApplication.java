package cn.edu.sustech.ces;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CampusEventSystemApplication {


    public static void main(String[] args) {
        SpringApplication.run(CampusEventSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(OrderRepository repository) {
        repository.findAll().forEach(System.out::println);
        return args -> repository.save(new Order(1, "potato", "I order a lot of potato"));
    }

}
