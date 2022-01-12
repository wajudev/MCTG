package com.example.mctg.cards;


import org.junit.jupiter.api.*;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {
    static CardService cardService;

    @BeforeAll
    static void beforeAll() {
        cardService = CardService.getInstance();
    }

    @Test
    @DisplayName("1 Insert a monster card with random element and monster type in database")
    void testInsertCard__randomMonster(){
            MonsterCard monsterCard = MonsterCard.builder()
                    .id("50")
                    .name("Elianor")
                    .monsterType(MonsterType.randomMonsterType())
                    .elementType(ElementType.randomElement())
                    .damage(30.50f)
                    .build();

            Assertions.assertTrue(cardService.insertCard(monsterCard));
    }

    @Test
    @DisplayName("2 Insert a spell card with random element type in database")
    void testInsertCard__randomSpell(){
        SpellCard spellCard = SpellCard.builder()
                .id("51")
                .name("Lorex")
                .elementType(ElementType.randomElement())
                .damage(35.00f)
                .build();

        Assertions.assertTrue(cardService.insertCard(spellCard));
    }

    @Test
    @DisplayName("3 Insert and get a dragon card with the element type normal")
    void testGetCard__normalDragon(){
        MonsterCard monsterCard = MonsterCard.builder()
                .id("52")
                .name("DragonMonster")
                .monsterType(MonsterType.DRAGON)
                .elementType(ElementType.NORMAL)
                .damage(40.60f)
                .build();

        Assertions.assertTrue(cardService.insertCard(monsterCard));

        MonsterCard card = (MonsterCard) cardService.getCard("52");

        assertEquals("DragonMonster", card.getName());
        assertEquals(40.60f, card.getDamage());
        assertEquals(CardType.MONSTER, card.getCardType());
        assertEquals(MonsterType.DRAGON, card.getMonsterType());
        assertEquals(ElementType.NORMAL, card.getElementType());

    }

    @Test
    @DisplayName("4 Insert and get a spell card  with element type water")
    void testGetCard__waterSpell(){
        SpellCard spellCard = SpellCard.builder()
                .id("53")
                .name("WaterSpell")
                .elementType(ElementType.WATER)
                .damage(30.56f)
                .build();

        Assertions.assertTrue(cardService.insertCard(spellCard));

        SpellCard card = (SpellCard) cardService.getCard("53");

        assertEquals("WaterSpell", card.getName());
        assertEquals(30.56f, card.getDamage());
        assertEquals(CardType.SPELL, card.getCardType());
        assertNull(card.getMonsterType());
        assertEquals(ElementType.WATER, card.getElementType());
    }

    @Test
    @DisplayName("5 Insert random card")
    void testGetCard__randomTableFiller(){
        SpellCard spellCard = SpellCard.builder()
                .id("54")
                .name("Tablefiller")
                .elementType(ElementType.randomElement())
                .damage(32.45f)
                .build();

        Assertions.assertTrue(cardService.insertCard(spellCard));

        SpellCard card = (SpellCard) cardService.getCard("54");

        assertEquals("Tablefiller", card.getName());
        assertEquals(32.45f, card.getDamage());
        assertEquals(CardType.SPELL, card.getCardType());
        assertNull(card.getMonsterType());
        assertNotNull(card.getElementType());
    }




    @Test
    @DisplayName("6 Get all cards in database")
    void testGetAllCards(){

        List<Card> cards = cardService.getAllCards();

        assertNotNull(cards);
        assertEquals(5, cards.size());
    }

    @Test
    @DisplayName("7 Delete cards")
    void testDeleteCard(){
        Assertions.assertTrue(cardService.deleteCard("50"));
        Assertions.assertTrue(cardService.deleteCard("51"));
        Assertions.assertTrue(cardService.deleteCard("52"));
        Assertions.assertTrue(cardService.deleteCard("53"));
        Assertions.assertTrue(cardService.deleteCard("54"));
    }



}