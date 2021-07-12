package com.zica.minesweeper.api;

import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.dto.request.StartGameDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Api(value = "/game")
@RestController
@RequestMapping(value = "/game")
public class GameController {

    private final GameService service;

    public GameController(GameService gameService) {
        this.service = gameService;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Game started", response = GameDTO.class)
    })
    @ApiOperation(value = "Creates a new Game with the requests parameters," +
            " saves it to the database and returns a DTO that represents it.")
    @PostMapping()
    public ResponseEntity<GameDTO> startGame(@Valid @RequestBody StartGameDTO startGameDTO) {
        GameDTO gameDTO = service.startGame(startGameDTO);
        return new ResponseEntity<>(gameDTO, HttpStatus.CREATED);
    }

    @PutMapping("{gameId}")
    public GameDTO openCell(@PathVariable String gameId, @RequestParam int row, @RequestParam int column){
        try {
            return service.openCell(gameId, row, column);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}
