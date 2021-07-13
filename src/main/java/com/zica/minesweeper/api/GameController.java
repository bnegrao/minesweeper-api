package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.exceptions.GameIdNotFoundException;
import com.zica.minesweeper.api.exceptions.InvalidCellPositionException;
import com.zica.minesweeper.game.GameIsOverException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Api(value = "/game")
@RestController
@RequestMapping(value = "/game")
public class GameController {

    Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService service;

    public GameController(GameService gameService) {
        this.service = gameService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game started", response = GameDTO.class)
    })
    @ApiOperation(value = "Creates a new Game with the parameters from StartGameDTO," +
            " saves it to the database and returns a GameDTO")
    @PostMapping()
    public ResponseEntity<GameDTO> startGame(@Valid @RequestBody StartGameDTO startGameDTO) {
        try {
            GameDTO gameDTO = service.startGame(startGameDTO);
            return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", e);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GameDTO.class),
            @ApiResponse(code = 400, message = "'the cell position invalid' OR 'the specified Game cannot be modified because it is not in RUNNING status'", response = DefaultErrorAttributes.class),
            @ApiResponse(code = 404, message = "the gameId was not found", response = DefaultErrorAttributes.class),
    })
    @ApiOperation(value = "Finds a Game by its gameId, opens the Cell at the position given in the query-string and returns an updated GameDTO. The property GameDTO.gameStatus may have changed after this operation.")
    @PutMapping("{gameId}")
    public GameDTO openCell(@PathVariable String gameId, @RequestParam int row, @RequestParam int column){
        try {
            return service.openCell(gameId, row, column);
        } catch (InvalidCellPositionException | GameIsOverException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (GameIdNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error", e);
        }
    }

}
