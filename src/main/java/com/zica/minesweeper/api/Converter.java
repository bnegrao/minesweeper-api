package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.CellDTO;
import com.zica.minesweeper.api.dto.response.CellPropertiesDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.game.Cell;
import com.zica.minesweeper.game.Game;
import org.springframework.stereotype.Service;

@Service
public class Converter {
    
    public GameDTO convertEntityToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCells(convertEntityToDTO(game.getCells()));
        gameDTO.setGameStatus(convertEntityToDTO(game.getGameStatus()));
        gameDTO.setId(game.getId());
        gameDTO.setPlayerEmail(game.getPlayerEmail());
        gameDTO.setStartDate(game.getStartDate());
        return gameDTO;
    }

    public GameDTO.GameStatus convertEntityToDTO(Game.GameStatus gameStatus) {
        switch (gameStatus) {
            case RUNNING:
                return GameDTO.GameStatus.RUNNING;
            case GAME_LOST:
                return GameDTO.GameStatus.GAME_LOST;
            case GAME_WON:
                return GameDTO.GameStatus.GAME_WON;
        }
        throw new RuntimeException("GameStatus "+ gameStatus + " cannot be converted");
    }

    public CellDTO[][] convertEntityToDTO(Cell[][] cells) {
        CellDTO[][] cellDTO2D = new CellDTO[cells.length][cells[0].length];
        for (int row = 0; row < cells.length; row++){
            for (int column = 0; column < cells[0].length; column++){
                cellDTO2D[row][column] = convertEntityToDTO(cells[row][column]);
            }
        }
        return cellDTO2D;
    }

    public Game convertDTOtoEntity(StartGameDTO dto) {
        return new Game(dto.getPlayerEmail(), dto.getRows(), dto.getColumns(), dto.getMines());
    }

    public CellDTO convertEntityToDTO(Cell cell) {
        CellDTO cellDTO = new CellDTO();
        if (cell.getFlag() != null){
            cellDTO.setFlag(cell.getFlag().name());
        }
        if (cell.isOpened()){
            cellDTO.setProperties(new CellPropertiesDTO(cell.getAdjacentMines(), cell.isMine()));
        }
        return cellDTO;
    }
}
