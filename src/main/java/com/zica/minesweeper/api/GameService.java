package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.CellDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.dto.response.CellPropertiesDTO;
import com.zica.minesweeper.game.Cell;
import com.zica.minesweeper.game.Game;
import com.zica.minesweeper.game.OpenCellResult;
import com.zica.minesweeper.repository.GameRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GameService {

    private GameRepository repository;

    public GameService(GameRepository repository){
        this.repository = repository;
    }

    /**
     * Retrieves the game from the database then opens the Cell at the given position.
     * If after opening the Cell is detected that the game is over, the Game is deleted
     * from the database.
     * @throws IllegalArgumentException if a game with the given gameId is not found or if the Cell position is invalid.
     */
    public GameDTO openCell(String gameId, int row, int column) throws IllegalArgumentException {
        Optional<Game> opGame = repository.findById(gameId);
        if (opGame.isEmpty()){
            throw new IllegalArgumentException("Game with id " + gameId + " does not exist");
        }
        Game game = opGame.get();
        OpenCellResult openCellResult = game.openCellAt(row, column);
        if (game.getGameStatus() != Game.GameStatus.RUNNING) {
            repository.delete(game);
        } else if (openCellResult == OpenCellResult.OPENED_OK) {
            // updates the game on the database after modifying its state
            repository.save(game);
        } else if (openCellResult == OpenCellResult.INVALID_POSITION){
            throw new IllegalArgumentException("There is no cell at position "+row+","+column);
        }
        return convertEntityToDTO(game);
    }

    public GameDTO startGame(StartGameDTO dto){
        Game game = repository.save(convertDTOtoEntity(dto));
        return convertEntityToDTO(game);
    }

    private GameDTO convertEntityToDTO(Game game) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setCells(convertEntityToDTO(game.getCells()));
        gameDTO.setGameStatus(convertEntityToDTO(game.getGameStatus()));
        gameDTO.setId(game.getId());
        gameDTO.setPlayerEmail(game.getPlayerEmail());
        gameDTO.setStartDate(game.getStartDate());
        return gameDTO;
    }

    private GameDTO.GameStatus convertEntityToDTO(Game.GameStatus gameStatus) {
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

    private CellDTO[][] convertEntityToDTO(Cell[][] cells) {
        CellDTO[][] cellDTO2D = new CellDTO[cells.length][cells[0].length];
        for (int row = 0; row < cells.length; row++){
            for (int column = 0; column < cells[0].length; column++){
                cellDTO2D[row][column] = convertEntityToDTO(cells[row][column]);
            }
        }
        return cellDTO2D;
    }

    private Game convertDTOtoEntity(StartGameDTO dto) {
        return new Game(dto.getPlayerEmail(), dto.getRows(), dto.getColumns(), dto.getMines());
    }

    private CellDTO convertEntityToDTO(Cell cell) {
        CellDTO cellDTO = new CellDTO();
        if (cell.isOpened()){
            cellDTO.setProperties(new CellPropertiesDTO(cell.getAdjacentMines(), cell.isMine()));
        }
        return cellDTO;
    }
}
