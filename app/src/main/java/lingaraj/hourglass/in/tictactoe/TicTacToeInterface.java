package lingaraj.hourglass.in.tictactoe;

/**
 * Created by lingaraj on 9/3/17.
 */

public interface TicTacToeInterface {
    void endGame(boolean is_tie, String score_key, int player);
    void nextPlayerMove(int previously_played_player);

}
