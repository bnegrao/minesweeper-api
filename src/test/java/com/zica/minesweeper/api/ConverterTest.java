package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.response.CellDTO;
import com.zica.minesweeper.game.Cell;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConverterTest {

    private Converter converter = new Converter();

    @Test
    public void testCellConverter() {
        Cell cell = new Cell(0, 1, false, 2);
        cell.setFlag(Cell.Flags.MINE);
        cell.setClosed(false);

        CellDTO cellDTO = converter.convertEntityToDTO(cell);

        assertEquals(cell.getFlag().name(), cellDTO.getFlag());
        assertEquals(cell.getAdjacentMines(), cellDTO.getProperties().getAdjacentMines());
        assertEquals(cell.isMine(), cellDTO.getProperties().isMine());
    }

}
