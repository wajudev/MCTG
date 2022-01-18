package com.example.mctg.trade;

import com.example.mctg.cards.Card;
import com.example.mctg.cards.MonsterCard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Trade {
    private String id;
    private boolean isMonster;
    private float minDamage;
    private String cardId;
    private int userId;

    public String tradeSummary(){
        String type = (this.isMonster) ? "Monster" : "Spell";
        return "\t\tTrading Summary: -> " +
                " - Type: " + type +
                " - Minimum Damage: "  + this.minDamage +
                " - Card owner(ID): " + this.userId + "\n\n";
    }

    public String tradeValidator(Trade trade, Card cardTobeTraded, int userId){

        if (cardTobeTraded == null){
            return "Card with specified Id doesn't exist";
        }
        if (trade.getUserId() == userId){
            return "You can't trade cards with yourself";
        }
        if (cardTobeTraded.isLocked()){
            return "Cards in deck are untradable";
        }
        if (cardTobeTraded.getUserId() != userId){
            return "You can't offer cards you don't own";
        }
        if (this.isMonster() != cardTobeTraded instanceof MonsterCard){
            return "Trade invalid, CardType offered doesn't match card required";
        }
        if (this.getMinDamage() > cardTobeTraded.getDamage()){
            return "Trade invalid, Card doesn't meet damage requirement";
        }

        return null;
    }

}
