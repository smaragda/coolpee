package cz.marcis.coolpee.router.controller;

import cz.marcis.coolpee.router.dto.Route;
import cz.marcis.coolpee.router.service.RoutingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @GetMapping(value = "/routing/{source}/{destination}")
    public Mono<Route> main(@PathVariable("source") String source,
                            @PathVariable("destination") String destination) {
        log.info("Request to get route from {} to {}", source, destination);

        return routingService.findRoute(source, destination);
    }
}

