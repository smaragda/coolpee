package cz.marcis.coolpee.router.dto;

import lombok.Data;

import java.util.List;

@Data
public class Country {

    private String cca3;
    private List<String> borders;

    public boolean isDirectNeighbour(Country dst) {
        return this.getBorders().contains(dst.getCca3());
    }
}
