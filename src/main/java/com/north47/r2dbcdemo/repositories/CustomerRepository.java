package com.north47.r2dbcdemo.repositories;

import com.north47.r2dbcdemo.models.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
}
