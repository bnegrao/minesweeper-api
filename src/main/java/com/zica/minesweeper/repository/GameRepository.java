package com.zica.minesweeper.repository;

import com.zica.minesweeper.game.Game;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GameRepository extends MongoRepository<Game, String> {
}
