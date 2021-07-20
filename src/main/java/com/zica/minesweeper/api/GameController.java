package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.exceptions.GameNotFoundException;
import com.zica.minesweeper.api.exceptions.InvalidCellPositionException;
import com.zica.minesweeper.game.Cell;
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
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GameDTO.class),
            @ApiResponse(code = 400, message = "'the cell position invalid' OR 'the specified Game cannot" +
                    " be modified because it is not in RUNNING status'", response = DefaultErrorAttributes.class),
            @ApiResponse(code = 404, message = "the gameId was not found", response = DefaultErrorAttributes.class),
    })
    @ApiOperation(value = "Opens the Cell at specified {position}" +
            "and returns an updated GameDTO. The property GameDTO.gameStatus may have changed after this operation."+
            "The {position} path parameter is a string made of two integers separated by '-', ex: 1-3")
    @PutMapping("{gameId}/cell/{position}")
    public GameDTO openCell(@PathVariable String gameId, @PathVariable @Pattern(regexp = "\\d+,\\d+") String position){
        try {
            int row = Integer.parseInt(position.split("-")[0]);
            int column = Integer.parseInt(position.split("-")[1]);
            return service.openCell(gameId, row, column);
        } catch (InvalidCellPositionException | GameIsOverException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Error");
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GameDTO.class),
            @ApiResponse(code = 400, message = "param playerEmail is not a valid email", response = GameDTO.class),
            @ApiResponse(code = 404, message = "the gameId was not found", response = DefaultErrorAttributes.class),
    })
    @ApiOperation(value = "Finds the last RUNNING Game session associated with a playerEmail")
    @GetMapping
    public GameDTO findLastRunningGameSession(@RequestParam @Email String playerEmail) {
        try{
            return service.findLastRunningGameSession(playerEmail);
        } catch (GameNotFoundException e) {
            System.out.println(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = GameDTO.class),
            @ApiResponse(code = 400, message = "'the cell position invalid' OR 'the specified Game cannot" +
                    " be modified because it is not in RUNNING status'", response = DefaultErrorAttributes.class),
            @ApiResponse(code = 404, message = "the gameId was not found", response = DefaultErrorAttributes.class),
    })
    @ApiOperation(value = "Toggles a flag in a cell. " +
            "The {position} path parameter is a string made of two integers separated by '-', ex: 1-3" +
            "The {flag} path parameter can be 'MINE|QUESTION'. " +
            "To unset a flag, just set the same flag at the same position.")
    @PutMapping("{gameId}/cell/{position}/flag/{flag}")
    public GameDTO toggleCellFlag(@PathVariable String gameId,
                                  @PathVariable @Pattern(regexp = "\\d+,\\d+") String position,
                                  @PathVariable @Pattern(regexp = "MINE|QUESTION") String flag) {
        Cell.Flags cellFlag;
        if (flag.equals("MINE")) {
            cellFlag = Cell.Flags.MINE;
        } else {
            cellFlag = Cell.Flags.QUESTION;
        }
        int row = Integer.parseInt(position.split("-")[0]);
        int column = Integer.parseInt(position.split("-")[1]);

        try {
            return service.toggleFlagAt(gameId, row, column, cellFlag);
        } catch (InvalidCellPositionException | GameIsOverException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (GameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
