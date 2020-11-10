package com.north47.r2dbcdemo.web;

import com.north47.r2dbcdemo.handlers.CustomerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
class CustomerRouter {
    @Bean
    public RouterFunction<ServerResponse> route(CustomerHandler customerHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/customers"), customerHandler.getAllCustomers())
                .andRoute(RequestPredicates.POST("/customers"), customerHandler.saveCustomer());
    }


}
