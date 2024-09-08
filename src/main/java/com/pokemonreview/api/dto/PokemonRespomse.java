package com.pokemonreview.api.dto;

import lombok.Data;

import java.util.List;
@Data
public class PokemonRespomse {

    private List<PokemonDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;

}
