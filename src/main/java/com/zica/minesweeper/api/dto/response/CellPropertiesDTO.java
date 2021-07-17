package com.zica.minesweeper.api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;

@ApiModel(description = "Properties of a CellDTO. This object is only visible after a Cell is opened")
public class CellPropertiesDTO {

    private int adjacentMines;
    private boolean isMine;

    public CellPropertiesDTO(int adjacentMines, boolean isMine) {
        this.adjacentMines = adjacentMines;
        this.isMine = isMine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }

    @JsonProperty("isMine")
    public boolean isMine() {
        return isMine;
    }

    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }

    @JsonProperty("isMine")
    public void setMine(boolean mine) {
        isMine = mine;
    }
}
