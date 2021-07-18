package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.exceptions.GameNotFoundException;
import com.zica.minesweeper.api.exceptions.InvalidCellPositionException;
import com.zica.minesweeper.game.Game;
import com.zica.minesweeper.game.GameIsOverException;
import com.zica.minesweeper.game.OpenCellResult;
import com.zica.minesweeper.repository.GameRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GameService {

    private GameRepository repository;
    private Converter converter;

    public GameService(GameRepository repository, Converter converter){
        this.repository = repository;
        this.converter = converter;
    }

    /**
     * Retrieves the game from the database then opens the Cell at the given position.
     * If after opening the Cell is detected that the game is over, the Game is deleted
     * from the database.
     */
    public GameDTO openCell(String gameId, int row, int column) throws GameNotFoundException, InvalidCellPositionException, GameIsOverException {
        Optional<Game> opGame = repository.findById(gameId);
        if (opGame.isEmpty()){
            throw new GameNotFoundException("Game with id " + gameId + " does not exist");
        }
        Game game = opGame.get();
        OpenCellResult openCellResult = game.openCellAt(row, column);
        if (game.getGameStatus() != Game.GameStatus.RUNNING) {
            repository.delete(game);
        } else if (openCellResult == OpenCellResult.OPENED_OK) {
            // updates the game on the database after modifying its state
            repository.save(game);
        } else if (openCellResult == OpenCellResult.INVALID_POSITION){
            throw new InvalidCellPositionException(row, column);
        }
        return converter.convertEntityToDTO(game);
    }

    public GameDTO startGame(StartGameDTO dto){
        Game game = repository.save(converter.convertDTOtoEntity(dto));
        return converter.convertEntityToDTO(game);
    }

    public GameDTO resumeGame(String playerEmail) throws GameNotFoundException {
        List<Game> games = repository.findByPlayerEmail(playerEmail);
        if (games == null || games.size() == 0){
            throw new GameNotFoundException("Cannot find a game session for user " + playerEmail);
        }
        return converter.convertEntityToDTO(games.get(0));
    }
}
