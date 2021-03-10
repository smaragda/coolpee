package cz.marcis.coolpee.router.dto;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Route {

    private List<String> route;

    public static Route simple(String a, String b) {
        Route response = new Route();
        response.setRoute(Arrays.asList(a, b));
        return response;
    }
}
