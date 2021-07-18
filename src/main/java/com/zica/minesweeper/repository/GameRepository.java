package com.zica.minesweeper.repository;

import com.zica.minesweeper.game.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByPlayerEmail(String playerEmail);
}
