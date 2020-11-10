package com.north47.r2dbcdemo;

import com.github.javafaker.Faker;
import com.north47.r2dbcdemo.models.Customer;
import com.north47.r2dbcdemo.repositories.CustomerRepository;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.connectionfactory.init.ConnectionFactoryInitializer;
import org.springframework.data.r2dbc.connectionfactory.init.ResourceDatabasePopulator;
import org.springframework.data.r2dbc.core.DatabaseClient;

import java.time.Duration;

@SpringBootApplication
@Slf4j
public class R2dbcDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(R2dbcDemoApplication.class, args);
    }

    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

        return initializer;
    }

    @Bean
    public CommandLineRunner demo(CustomerRepository repository) {

        return (args) -> {
            ConnectionFactory connectionFactory = ConnectionFactories.get("r2dbc:h2:mem:///test?options=DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=TRUE");
            DatabaseClient client = DatabaseClient.create(connectionFactory);

            client.insert()
                    .into(Customer.class)
                    .using(new Customer("Bojan", "Trajkovski"))
                    .then()
                    .doOnSuccess(System.out::println)
                    .block();

            client.execute("SELECT * FROM CUSTOMER WHERE lastname = 'Trajkovski'")
                    .as(Customer.class)
                    .fetch()
                    .all()
                    .doOnNext(customer -> log.info("Fetched element from database [{}]", customer))
                    .blockLast(Duration.ofSeconds(2));


            Faker faker = new Faker();
            int numberOfCustomersToSave = 50;
            for (int i = 0; i < numberOfCustomersToSave; i++) {
                String gotCharacter = faker.gameOfThrones().character();
                Customer customer;
                if (gotCharacter.contains(" ")) {
                    String[] splitted = gotCharacter.split(" ");
                    customer = new Customer(splitted[0], splitted[1]);
                } else {
                    customer = new Customer(gotCharacter, "");
                }

                log.info(gotCharacter);
                Customer savedCustomer = repository.save(customer).block(Duration.ofSeconds(1));
                log.info("Saved customer {}", savedCustomer);
            }

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            repository.findAll().doOnNext(customer -> {
                log.info(customer.toString());
            }).blockLast(Duration.ofSeconds(10));

            log.info("");

            // fetch an individual customer by ID
            repository.findById(1).doOnNext(customer -> {
                log.info("Customer found with findById(1L):");
                log.info("--------------------------------");
                log.info(customer.toString());
                log.info("");
            }).block(Duration.ofSeconds(10));
        };
    }
}
