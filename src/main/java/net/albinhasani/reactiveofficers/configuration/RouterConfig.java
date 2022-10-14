package net.albinhasani.reactiveofficers.configuration;

import net.albinhasani.reactiveofficers.controller.OfficerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> route(OfficerHandler handler) {
        return RouterFunctions
                .route(GET("/route/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getOfficer)
                .andRoute(GET("/reoute").and(accept(MediaType.APPLICATION_JSON)), handler::listOfficers)
                .andRoute(POST("/route").and(contentType(MediaType.APPLICATION_JSON)), handler::createOfficer);
    }

}
