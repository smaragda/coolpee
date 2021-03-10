package cz.marcis.coolpee.router.service;

import cz.marcis.coolpee.router.dto.Countries;
import cz.marcis.coolpee.router.dto.Country;
import cz.marcis.coolpee.router.dto.Route;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutingService {

    private final Countries countries;

    public Mono<Route> findRoute(String source, String destination) {
        // validation
        val src = countries
                .findByName(source)
                .orElseThrow(NoSuchCountryException::new);
        val dst = countries
                .findByName(destination)
                .orElseThrow(NoSuchCountryException::new);

        // easy way
        if (src.isDirectNeighbour(dst)) {
            return Mono.just(Route.simple(source, destination));
        }

        // longer way
        val enRoute1 = radarRoute(src, dst);
        val enRoute2 = radarRoute(dst, src);
        val countries = findPath(enRoute1, enRoute2);


        Route routeResponse = new Route();
        routeResponse.setRoute(
                countries
                        .stream()
                        .map(Country::getCca3)
                        .collect(Collectors.toList())
        );
        return Mono.just(routeResponse);
    }

    public List<List<Country>> radarRoute(Country src, Country dest) {
        List<List<Country>> filteredCtriesOnThePath = new ArrayList<>();
        filteredCtriesOnThePath.add(Collections.singletonList(src));

        boolean routeExists = false;
        List<Country> borderCountries = Collections.singletonList(src);

        int MAX_HOPS = 10;
        for (int i = 1; i <= MAX_HOPS; i++) {
            borderCountries = getBorderStep(borderCountries);
            filteredCtriesOnThePath.add(borderCountries);

            if (borderCountries.contains(dest)) {
                routeExists = true;
                break;
            }
        }
        if (!routeExists) {
            throw new NoSuchRouteException();
        }

        return filteredCtriesOnThePath;
    }

    private List<Country> findPath(List<List<Country>> enRoute1, List<List<Country>> enRoute2) {
        List<Country> route = new ArrayList<>();
        for (int i = 0; i < enRoute1.size(); i++) {
            List<Country> intersection = getIntersection(
                    enRoute1.get(i),
                    enRoute2.get(enRoute1.size() - i - 1)
            );
            route.add(intersection.stream().findFirst().orElseThrow());
        }
        return route;
    }

    private List<Country> getIntersection(List<Country> countries1, List<Country> countries2) {
        return countries1.stream()
                .distinct()
                .filter(countries2::contains)
                .collect(Collectors.toList());
    }

    public List<Country> getBorderStep(List<Country> baseLevel) {
        List<Country> nextLevel = baseLevel
                .stream()
                .map(Country::getCca3)
                .distinct()
                .flatMap(s -> countries.getBordersFor(s).stream())
                .distinct()
                .collect(Collectors.toList());
        nextLevel.removeAll(baseLevel);
        return nextLevel;
    }
}
