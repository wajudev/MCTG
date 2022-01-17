package com.example.mctg.controller;

import com.example.mctg.battle.Battle;

import com.example.mctg.battle.BattleLog;
import com.example.mctg.cards.Card;
import com.example.mctg.cards.CardService;
import com.example.mctg.cards.Deck;
import com.example.mctg.user.User;
import com.example.mctg.user.UserService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Builder
@Data
@AllArgsConstructor
public class BattleController {
    private ArrayBlockingQueue<User> players;
    private AtomicBoolean isFinished;
    private BattleLog battleLog;

    public void addPlayerToArena(User user){
        if (user.getDeck().getDeckList().isEmpty()){
            user.setDeck(new Deck(user));
        }

        players.add(user);
        this.isFinished.set(false);
    }

    public void startBattle() throws InterruptedException{
        User playerOne = players.take();
        User playerTwo = players.take();

        Battle newBattle = new Battle(playerOne,playerTwo);

        newBattle.startBattle();

        updateResults(newBattle.getPlayerOne());
        updateResults(newBattle.getPlayerTwo());

        battleLog = new BattleLog(
                UserService.getInstance().getUser(playerOne.getUsername()),
                UserService.getInstance().getUser(playerTwo.getUsername()),
                newBattle.getWinner(),
                newBattle.getRounds());

        System.out.println(battleLog.getBattleSummary());

        this.isFinished.set(true);
    }

    public void updateResults(User user){
        for (Card card: user.getDeck().getDeckList()){
            CardService.getInstance().addCardToNewUser(card.getId(), user.getId());
        }
        CardService.getInstance().removeFromDeck(user.getId());
        UserService.getInstance().updateUserStats(user);
        updateStack(user);
    }

    public void updateStack(User user) {
        List<Card> list = CardService.getInstance().getCardsByUser(user);
        if(!list.isEmpty()) {
            user.getStack().setStackList(list);
        }
    }
}
