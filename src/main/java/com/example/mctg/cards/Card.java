package com.example.mctg.cards;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class Card implements ICard {
   private String id;
   private String name;
   private MonsterType monsterType;
   private ElementType elementType;
   private float damage;
   private boolean locked;
   private int userId;



    @Override
    public boolean defeats(Card card){
        // Monster card vs Monster card
        if (CardType.MONSTER.equals(this.getCardType()) && CardType.MONSTER.equals(card.getCardType())){

            // Dragons defeats Goblins
            if (MonsterType.DRAGON.equals(this.getMonsterType()) && MonsterType.GOBLIN.equals(card.getMonsterType())){
                return true;
            }

            // Wizards defeats Orks
            if (MonsterType.WIZARD.equals(this.getMonsterType()) && MonsterType.ORK.equals(card.getMonsterType())){
                return true;
            }

            // Fire elves defeats Dragons
            if (MonsterType.FIRE_ELF.equals(this.getMonsterType()) && MonsterType.DRAGON.equals(card.getMonsterType())){
                return true;
            }
        }

        // Spell card vs Monster card
        if (CardType.SPELL.equals(getCardType()) && CardType.MONSTER.equals(card.getCardType())){

            // Water Spell drowns Knight
            if (ElementType.WATER.equals(this.getElementType()) && MonsterType.KNIGHT.equals(card.getMonsterType())){
                return true;
            }
        }

        // Monster card vs Spell card
        if (CardType.MONSTER.equals(getCardType()) && CardType.SPELL.equals(card.getCardType())){
            // Kraken immune to all Spells
            //noinspection RedundantIfStatement
            if (MonsterType.KRAKEN.equals(getMonsterType())){
                return true;
            }
        }
        return false;
    }


    // Effectiveness for Spell Cards
    @Override
    public float calculateEffectiveness(Card card){
        if (CardType.SPELL.equals(this.getCardType())){
            // Double Damage Effective
            if ((ElementType.WATER.equals(this.getElementType()) && ElementType.FIRE.equals(card.getElementType())) ||
                    (ElementType.FIRE.equals(this.getElementType()) && ElementType.NORMAL.equals(card.getElementType())) ||
                    (ElementType.NORMAL.equals(this.getElementType()) && ElementType.WATER.equals(card.getElementType()))){
                System.out.println(2 * this.getDamage());
                return 2 * this.getDamage();
            }

            // Halved Damage Not Effective
            if ((ElementType.FIRE.equals(this.getElementType()) && ElementType.WATER.equals(card.getElementType())) ||
                    (ElementType.NORMAL.equals(this.getElementType()) && ElementType.FIRE.equals(card.getElementType())) ||
                    (ElementType.WATER.equals(this.getElementType()) && ElementType.NORMAL.equals(card.getElementType()))) {
                System.out.println(this.getDamage() / 2);
                return this.getDamage() / 2;
            }
        }

        // No Effect
        System.out.println(this.getDamage());
        return this.getDamage();

    }

    @Override
    public String getCardStats() {
        return "\tid: " + this.id +
                " - Name: " + this.name +
                " - Element: " + this.elementType.getElementName() +
                " - Damage: " + this.damage + "\n";
    }

    public static Card buildCard(String id, String name, String cardTypeString, String monsterTypeString, String elementTypeString, float damage, boolean locked, int userId){
        CardType cardType;
        MonsterType monsterType;
        ElementType elementType;
        Card card;

        try {
            cardType = CardType.valueOf(cardTypeString);
        } catch (IllegalArgumentException e){
            cardType = CardType.MONSTER;
        }

        try {
            elementType = ElementType.valueOf(elementTypeString);
        } catch (IllegalArgumentException e) {
            elementType = ElementType.randomElement();
        }

        if (CardType.MONSTER.equals(cardType)){
            try {
                monsterType = MonsterType.valueOf(monsterTypeString);
            } catch (IllegalArgumentException e) {
                monsterType = MonsterType.randomMonsterType();
            }

            // Create Monster Card
            card = MonsterCard.builder()
                    .id(id)
                    .name(name)
                    .monsterType(monsterType)
                    .elementType(elementType)
                    .damage(damage)
                    .userId(userId)
                    .build();

        } else {
            // Otherwise create Spell Card
            card = SpellCard.builder()
                    .id(id)
                    .name(name)
                    .elementType(elementType)
                    .damage(damage)
                    .userId(userId)
                    .build();
        }

        return card;
    }
}
