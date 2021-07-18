package dtos

type StartGameDTO struct {
	PlayerEmail string `json:"playerEmail"`
	Rows        int    `json:"rows"`
	Columns     int    `json:"columns"`
	Mines       int    `json:"mines"`
}

type CellPropertiesDTO struct {
	AdjacentMines int  `json:"adjacentMines"`
	IsMine        bool `json:"isMine"`
}

type CellDTO struct {
	Properties *CellPropertiesDTO `json:"properties"`
	Flag       string             `json:"flag"`
}

type GameDTO struct {
	Id          string      `json:"id"`
	Cells       [][]CellDTO `json:"cells"`
	PlayerEmail string      `json:"playerEmail"`
	StartDate   string      `json:"startDate"`
	GameStatus  string      `json:"gameStatus"` //RUNNING, GAME_WON, GAME_LOST
}
