package com.north47.r2dbcdemo.handlers;

import com.north47.r2dbcdemo.models.Customer;
import com.north47.r2dbcdemo.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CustomerHandler {
    private final CustomerRepository customerRepository;

    public HandlerFunction<ServerResponse> getAllCustomers() {
        return serverRequest -> {
            Flux<Customer> customers = customerRepository.findAll();
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_STREAM_JSON)
                    .body(customers.delayElements(Duration.ofMillis(500)), Customer.class);
        };
    }

    public HandlerFunction<ServerResponse> saveCustomer() {
        return serverRequest -> {
            Mono<Customer> savedCustomer = serverRequest
                    .bodyToMono(Customer.class)
                    .flatMap(customerRepository::save);

            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(savedCustomer , Customer.class);
        };
    }
}
