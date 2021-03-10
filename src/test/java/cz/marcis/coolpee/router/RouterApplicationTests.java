package cz.marcis.coolpee.router;

import cz.marcis.coolpee.router.dto.Route;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Arrays;

@SpringBootTest(properties = "spring.main.web-application-type=reactive")
@AutoConfigureWebTestClient
class RouterApplicationTests {


    @Test
    void simpleRoute(@Autowired WebTestClient webClient) {
        // given
        Route expectedSimpleRoute = Route.simple("CZE", "POL");

        // when, then
        webClient.get()
                .uri("/routing/CZE/POL")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Route.class)
                .isEqualTo(expectedSimpleRoute);
    }

    @Test
    void longerRoute(@Autowired WebTestClient webClient) {
        // given
        Route expected = new Route();
        expected.setRoute(Arrays.asList(
                "CZE", "POL", "RUS", "CHN", "IND"
        ));

        // when, then
        webClient.get()
                .uri("/routing/CZE/IND")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Route.class)
                .isEqualTo(expected);
    }
}
