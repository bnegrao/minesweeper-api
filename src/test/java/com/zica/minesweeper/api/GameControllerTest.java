package com.zica.minesweeper.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zica.minesweeper.RestfulApiApplication;
import com.zica.minesweeper.api.dto.response.GameDTO;
import com.zica.minesweeper.api.dto.request.StartGameDTO;
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
        assertEquals(5, gameDTO.getCells().length);
        assertEquals(5, gameDTO.getCells()[0].length);
        assertNull(gameDTO.getCells()[0][0].getProperties());
    }

    @Test
    public void testOpenCell() throws Exception {
        GameDTO gameDTO = postNewGame(new StartGameDTO("asdf@asdf.com", 5, 5, 3));
        String gameId = gameDTO.getId();
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
