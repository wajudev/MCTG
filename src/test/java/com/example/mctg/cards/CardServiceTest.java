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
    @DisplayName("Insert a monster card with random element and monster type in database")
    void testInsertCard__randomMonster(){
            MonsterCard monsterCard = MonsterCard.builder()
                    .name("Elianor")
                    .monsterType(MonsterType.randomMonsterType())
                    .elementType(ElementType.randomElement())
                    .damage(30.50f)
                    .build();

            Assertions.assertTrue(cardService.insertCard(monsterCard));
    }
    @Test
    @DisplayName("Delete  card")
    void testDeleteCard(){
        Assertions.assertTrue(cardService.deleteCard("35"));
    }



    @Test
    @DisplayName("Insert a spell card with random element type in database")
    void testInsertCard__randomSpell(){
        SpellCard spellCard = SpellCard.builder()
                .name("Lorex")
                .elementType(ElementType.randomElement())
                .damage(35.00f)
                .build();

        Assertions.assertTrue(cardService.insertCard(spellCard));
    }




    @Test
    @DisplayName("Get a dragon card with the element type normal")
    void testGetCard__normalDragon(){

            MonsterCard card = (MonsterCard) cardService.getCard("0");

            assertEquals("RandomMonster", card.getName());
            assertEquals(40.60f, card.getDamage());
            assertEquals(CardType.MONSTER, card.getCardType());
            assertEquals(MonsterType.DRAGON, card.getMonsterType());
            assertEquals(ElementType.NORMAL, card.getElementType());

    }

    @Test
    @DisplayName("Get a spell card(already in database) with element type water")
    void testGetCard__waterSpell(){

            SpellCard card = (SpellCard) cardService.getCard("32");

            assertEquals("Elixiro", card.getName());
            assertEquals(30.56f, card.getDamage());
            assertEquals(CardType.SPELL, card.getCardType());
            assertNull(card.getMonsterType());
            assertEquals(ElementType.WATER, card.getElementType());
    }

    @Test
    @DisplayName("Get all cards in database")
    void testGetAllCards(){

        List<Card> cards = cardService.getAllCards();

        assertNotNull(cards);
        assertEquals(5, cards.size());
    }



}