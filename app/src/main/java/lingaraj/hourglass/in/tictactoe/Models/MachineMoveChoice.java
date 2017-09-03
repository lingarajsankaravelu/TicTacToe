package lingaraj.hourglass.in.tictactoe.Models;

/**
 * Created by lingaraj on 9/2/17.
 */

public class MachineMoveChoice  {
    private boolean redAlert;
    private String boardKey;
    private boolean isNull;
    private boolean isScore;


    public MachineMoveChoice(boolean is_red_alert, String board_key, boolean is_null){
        this.redAlert = is_red_alert;
        this.boardKey = board_key;
        this.isNull = is_null;
    }

    public MachineMoveChoice(boolean is_null){
        this.isNull = is_null;
    }
    public boolean isScore() {
        return isScore;
    }

    public void setScore(boolean score) {
        isScore = score;
    }


    public boolean isNull() {
        return isNull;
    }


    public boolean isRedAlert() {
        return redAlert;
    }


    public String getBoardKey() {
        return boardKey;
    }

    public void setBoardKey(String boardKey) {
        this.boardKey = boardKey;
    }

    public void setRedAlert(boolean redAlert) {
        this.redAlert = redAlert;
    }

    public void setNull(boolean aNull) {
        isNull = aNull;
    }

}
