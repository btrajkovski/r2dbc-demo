package com.north47.r2dbcdemo.configuration;

import com.north47.r2dbcdemo.models.Customer;
import com.north47.r2dbcdemo.repositories.CustomerRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements ApplicationRunner {
    private final CustomerRepository customerRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE");
        DatabaseClient client = DatabaseClient.create(connectionFactory);

        log.info("creating DB");

//        client.execute("CREATE TABLE customer" +
//                "(id INT PRIMARY KEY," +
//                "firstname VARCHAR(255)," +
//                "lastname VARCHAR(255))")
//                .fetch()
//                .rowsUpdated()
//                .doOnSuccess(System.out::println)
//                .block();


        customerRepository.save(new Customer("john", "snow"));
//        client.insert()
//                .into(Customer.class)
//                .using(new Customer("John", "Snow"))
//                .then()
//                .doOnSuccess(System.out::println)
//                .block();

//        client.select()
//                .from(Customer.class)
//                .fetch()
//                .first()
//                .doOnNext(it -> log.info("customer: {}", it))
//                .block();

    }
}
