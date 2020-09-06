# EyePlaySolitaire
Course project in CDIO (Conceive, Design, Implement and Operate).

A Solitaire solver to suggest most promising move after processing physical gamestate by the means of camera.
The structure of the project consist of three components, Maching learning for card recognition, Artificial intelligence for finding most promising move and Javafx for the graphical user interface.

The game follows following rules: https://playingcarddecks.com/blogs/how-to-play/solitaire-game-rules

## Usage
When running the program, you will be met with the starting menu offering to toggle following three things: the next move suggestor (AI), the image recognition (CV), and the predefined stock. It is **not** possible to run the program with CV without further setup of webcame.  

![Image of starting menu](https://github.com/NicolaiNisbeth/EyePlaySolitaire/blob/master/src/main/resources/images/start_menu.JPG?raw=true)

After pressing the start button, the game will be begin and instructions will be appear in the bottom left corner.

![Image of game](https://github.com/NicolaiNisbeth/EyePlaySolitaire/blob/master/src/main/resources/images/game_gui.JPG?raw=true)

## Results
The AI is using the MCTS agent simply because it outperformed alternatives, as seen below. For clarification, score describes number of cards in the tableau.
![Image of system](https://github.com/NicolaiNisbeth/EyePlaySolitaire/blob/master/src/main/resources/images/results_ai.png?raw=true)

For the fun of comparison, we also tested our agent on a relaxed version of solitaire where every card in the stack (top left corner) is available. As expected the win ratio increases drastically. Winning as much as 64% of the games.
![Image of system](https://github.com/NicolaiNisbeth/EyePlaySolitaire/blob/master/src/main/resources/images/results_ai_relaxed.png?raw=true)
