package com.example.mctg.battle;

import com.example.mctg.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BattleLog {
    private User playerOne;
    private User playerTwo;
    private User winner;
    private int rounds;

    public String getBattleSummary() {
        String result = this.winner != null ? "Winner: " + this.winner.getUsername() : ("No winner");
        return  result + "\n" +
                "Rounds: " + rounds + "\n\n" +
                this.playerOne.getUserStats() +
                this.playerTwo.getUserStats();
    }
}


