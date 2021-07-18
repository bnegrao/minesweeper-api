package com.zica.minesweeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zica.minesweeper.RestfulApiApplication;
import com.zica.minesweeper.api.dto.request.StartGameDTO;
import com.zica.minesweeper.api.dto.response.CellDTO;
import com.zica.minesweeper.api.dto.response.GameDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * In order to run these integration tests you need to have a working instance of MongoDB
 * available and set the environment variables required by the class MongoConfig.
 *
 * TODO: use embedded mongo instance for tests: https://www.baeldung.com/spring-boot-embedded-mongodb
 */
@SpringBootTest(classes = RestfulApiApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @Test
    public void startGameTest() throws Exception {
        GameDTO gameDTO = postNewGame(new StartGameDTO("asdf@asdf.com", 5, 5, 3));

        assertEquals("asdf@asdf.com", gameDTO.getPlayerEmail());
        assertNotNull(gameDTO.getId());
        assertEquals(GameDTO.GameStatus.RUNNING, gameDTO.getGameStatus());
        assertEquals(5, gameDTO.getCells().length);
        assertEquals(5, gameDTO.getCells()[0].length);
        assertNull(gameDTO.getCells()[0][0].getProperties());
    }

    @Test
    public void testOpenExistingClosedCell_thenItsPropertiesShouldBecomeVisible() throws Exception {
        GameDTO gameDTO = postNewGame(new StartGameDTO("asdf@asdf.com", 5, 5, 3));
        String gameId = gameDTO.getId();

        // opening cell at position 0,0
        MvcResult result = mvc.perform(put("/game/" + gameId)
                .param("row", "0")
                .param("column", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        gameDTO = objectMapper.readValue(responseBody, GameDTO.class);

        assertNotNull(gameDTO.getCells()[0][0].getProperties());
    }

    /**
     * Creating a Game with 0 mines. After opening the first cell the game
     * should transition to GAME_WON status and be removed from the database.
     * After that, if I try to open any cell, the http status should be 404
     * because the game no longer exists on the database
     */
    @Test
    public void testOpenCellAfterGameIsOver_thenShouldReceive404GameNotFound() throws Exception {

        GameDTO gameDTO = postNewGame(new StartGameDTO("asdf@asdf.com", 5, 5, 0));
        String gameId = gameDTO.getId();

        // opening cell at position 0,0
        MvcResult result = mvc.perform(put("/game/" + gameId)
                .param("row", "0")
                .param("column", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        gameDTO = objectMapper.readValue(responseBody, GameDTO.class);

        assertEquals(GameDTO.GameStatus.GAME_WON, gameDTO.getGameStatus());

        mvc.perform(put("/game/" + gameId)
                .param("row", "0")
                .param("column", "0"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    public void testOpeningACellInInvalidPosition_thenResultShouldBeHttp400() throws Exception {
        GameDTO gameDTO = postNewGame(new StartGameDTO("asdf@asdf.com", 5, 5, 3));
        String gameId = gameDTO.getId();

        // opening cell at position 6,-1
        mvc.perform(put("/game/" + gameId)
                .param("row", "6")
                .param("column", "-1"))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    @Test
    public void testResumeGameSession() throws Exception {
        long millis = System.currentTimeMillis();
        String playerEmail = "asdf"+millis+"@asdf.com";
        GameDTO gameDTO = postNewGame(new StartGameDTO(playerEmail, 5, 5, 3));
        MvcResult result = mvc.perform(get("/game/")
                .param("playerEmail", playerEmail))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        GameDTO gameDTO2 = objectMapper.readValue(responseBody, GameDTO.class);

        CellDTO[][] cells = gameDTO.getCells();

        for (int row = 0;row<cells.length; row++){
            for (int col = 0; col<cells[0].length; col++) {
                assertEquals(cells[row][col].getProperties(), gameDTO2.getCells()[row][col].getProperties());
            }
        }
    }


    private GameDTO postNewGame(StartGameDTO startGameDTO) throws Exception {
        MvcResult result = mvc.perform(post("/game")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(startGameDTO)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, GameDTO.class);
    }


    private String toJson(StartGameDTO startGameDTO) throws JsonProcessingException {
        return objectMapper.writeValueAsString(startGameDTO);
    }
}
