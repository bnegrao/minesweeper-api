# Project minesweeper-api

## Server API

The server api is a restful java application deployed to AWS at http://minesweeperapi-env.eba-gpc4cmgm.us-west-2.elasticbeanstalk.com 

##### Server API Documentation

The API documentation in the [swagger-ui](http://minesweeperapi-env.eba-gpc4cmgm.us-west-2.elasticbeanstalk.com/swagger-ui/)

##### Server API Implementation notes

I started by coding the game logic. When I implemented the persistence feature, 
I didn't refactor the classes to separate the model (the data that should be persisted) from the game logic.
I am aware the best practice is to have model and business logic separated and I would refactor the code if 
I had more time. 

## Client API

The API client library is written in Golang and is in the folder [client/minesweeper](client/minesweeper)

## Client APP

There is a