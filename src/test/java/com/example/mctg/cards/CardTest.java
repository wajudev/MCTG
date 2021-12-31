package com.example.mctg.cards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CardTest {

    @Mock
    Card cardMock;

    @Test
    @DisplayName("Monster cards should be of CardType.MONSTER")
    void testGetCardType__Monster(){
        Card monsterCard = new MonsterCard();
        CardType cardType = monsterCard.getCardType();

        assertNotNull(cardType);
        assertEquals(CardType.MONSTER, cardType);
    }

    @Test
    @DisplayName("Monster cards created with the builder should be of CardType.Monster")
    void testGetCardType__WithBuilderMonster(){
        Card monsterCard = new MonsterCard.MonsterCardBuilder().build();

        CardType cardType = monsterCard.getCardType();

        assertNotNull(cardType);
        assertEquals(CardType.MONSTER, cardType);
    }

    @Test
    @DisplayName("Monster cards should be of CardType.SPELL")
    void testGetCardType__Spell(){
        Card spellCard = new SpellCard();
        CardType cardType = spellCard.getCardType();

        assertNotNull(cardType);
        assertEquals(CardType.SPELL, cardType);
    }

    @Test
    @DisplayName("Spell cards created with the builder should be of CardType.SPELL")
    void testGetCardType__WithBuilderSpell(){
        Card spellCard = new SpellCard.SpellCardBuilder().build();
        CardType cardType = spellCard.getCardType();

        assertNotNull(cardType);
        assertEquals(CardType.SPELL, cardType);
    }

    @Test
    @DisplayName("Dragons defeats Goblins")
    void testDefeats__dragonGoblin(){
        Card dragon = MonsterCard.builder().monsterType(MonsterType.DRAGON).build();
        when(cardMock.getCardType()).thenReturn(CardType.MONSTER);
        when(cardMock.getMonsterType()).thenReturn(MonsterType.GOBLIN);


        boolean result = dragon.defeats(cardMock);

        assertTrue(result);
    }

    @Test
    @DisplayName("Wizard defeats Orks")
    void testDefeats__wizardOrks(){
        Card wizard = MonsterCard.builder().monsterType(MonsterType.WIZARD).build();
        when(cardMock.getCardType()).thenReturn(CardType.MONSTER);
        when(cardMock.getMonsterType()).thenReturn(MonsterType.ORK);


        boolean result = wizard.defeats(cardMock);

        assertTrue(result);
    }

    @Test
    @DisplayName("The armor of Knights is so heavy that WaterSpells make them drown them instantly.")
    void testDefeats__waterspellKnights(){
        Card waterSpell = SpellCard.builder().elementType(ElementType.WATER).build();
        when(cardMock.getCardType()).thenReturn(CardType.MONSTER);
        when(cardMock.getMonsterType()).thenReturn(MonsterType.KNIGHT);


        boolean result = waterSpell.defeats(cardMock);

        assertTrue(result);
    }

    @Test
    @DisplayName("Kraken is immune to all spells")
    void testDefeats__krakenAllSpells(){
        Card kraken = MonsterCard.builder().monsterType(MonsterType.KRAKEN).build();
        when(cardMock.getCardType()).thenReturn(CardType.SPELL);

        boolean result = kraken.defeats(cardMock);

        assertTrue(result);
    }

    @Test
    @DisplayName("The FireElves know Dragons since they were little and can evade their attacks.")
    void testDefeats__fireElvesDragons(){
        Card fireElf = MonsterCard.builder().monsterType(MonsterType.FIRE_ELF).build();
        when(cardMock.getCardType()).thenReturn(CardType.MONSTER);
        when(cardMock.getMonsterType()).thenReturn(MonsterType.DRAGON);

        boolean result = fireElf.defeats(cardMock);

        assertTrue(result);
    }
}