package messages

var GameTitle = `
███╗   ███╗██╗███╗   ██╗███████╗███████╗██╗    ██╗███████╗███████╗██████╗ ███████╗██████╗     
████╗ ████║██║████╗  ██║██╔════╝██╔════╝██║    ██║██╔════╝██╔════╝██╔══██╗██╔════╝██╔══██╗    
██╔████╔██║██║██╔██╗ ██║█████╗  ███████╗██║ █╗ ██║█████╗  █████╗  ██████╔╝█████╗  ██████╔╝    
██║╚██╔╝██║██║██║╚██╗██║██╔══╝  ╚════██║██║███╗██║██╔══╝  ██╔══╝  ██╔═══╝ ██╔══╝  ██╔══██╗    
██║ ╚═╝ ██║██║██║ ╚████║███████╗███████║╚███╔███╔╝███████╗███████╗██║     ███████╗██║  ██║    
╚═╝     ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝╚══════╝ ╚══╝╚══╝ ╚══════╝╚══════╝╚═╝     ╚══════╝╚═╝  ╚═╝    
                                                                                              
 ██████╗ ██████╗ ███╗   ██╗███████╗ ██████╗ ██╗     ███████╗                                  
██╔════╝██╔═══██╗████╗  ██║██╔════╝██╔═══██╗██║     ██╔════╝                                  
██║     ██║   ██║██╔██╗ ██║███████╗██║   ██║██║     █████╗                                    
██║     ██║   ██║██║╚██╗██║╚════██║██║   ██║██║     ██╔══╝                                    
╚██████╗╚██████╔╝██║ ╚████║███████║╚██████╔╝███████╗███████╗                                  
 ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝╚══════╝ ╚═════╝ ╚══════╝╚══════╝                                  
                                                                                              
 ██████╗  █████╗ ███╗   ███╗███████╗██╗██╗██╗                                                 
██╔════╝ ██╔══██╗████╗ ████║██╔════╝██║██║██║                                                 
██║  ███╗███████║██╔████╔██║█████╗  ██║██║██║                                                 
██║   ██║██╔══██║██║╚██╔╝██║██╔══╝  ╚═╝╚═╝╚═╝                                                 
╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗██╗██╗██╗                                                 
 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝╚═╝╚═╝╚═╝     
 
 It is a pleasure to have you here!
`

var EnterEmail = `Please enter your email [asdf@asdf.com]: `
var EnterNRows = `Please enter the number of rows in your game [9]: `
var EnterNColumns = `Please enter the number of columns in your game [9]: `
var EnterNMines = `Please enter the number of mines in your game [10]: `

var StartMenu = `
Please choose one of the options bellow:
1 - Start a new game
2 - Resume the last running game session
q - Quit
Option [1]: `

var OpenCellMenu = `
Please choose one of the options bellow:
1 - Open a cell
2 - Toggle a question mark '?' in a cell
3 - Toggle a mine mark '#' in a cell
4 - Save your game and quit
5 - Quit without saving
Option [1]: `

var AskCellPosition = `
Enter the cell position as two integers separated by space, ex: "1 1": `

var YouWon = `
You WON!!! Congratulations!!`
var YouLost = `
You LOST!! Sorry...`

var ErrorEmptyEmail = "Email cannot be empty. Please try again."
