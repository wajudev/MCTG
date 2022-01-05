package com.example.mctg.cards;

import com.example.mctg.database.DatabaseService;
import org.junit.jupiter.api.*;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;

class CardServiceTest {
    static CardService cardService;

    @BeforeAll
    static void beforeAll() {
        cardService = CardService.getInstance();
    }

    @BeforeEach
    void beforeEach() {
        // Delete card WHERE id < 0 before every test case
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM cards WHERE id < 0;");
            statement.close();
            connection.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Insert a monster card with random element type in database")
    void testInsertCard__randomMonster(){
        //try {
            MonsterCard monsterCard = MonsterCard.builder()
                    .name("Elianor")
                    .monsterType(MonsterType.randomMonsterType())
                    .elementType(ElementType.randomElement())
                    .damage(30.50f)
                    .build();

             cardService.insertCard(monsterCard);

           /* assertNotNull(card);
            assertNotNull(card.getName());

            Connection connection = DatabaseService.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, card_type, monster_type, element_type, damage FROM cards WHERE id=?;");
            preparedStatement.setInt(1, card.getId());
            ResultSet rs = preparedStatement.executeQuery();
            assertTrue(rs.next());
            assertEquals("Elianor", rs.getString(2));
            assertEquals(CardType.MONSTER, CardType.valueOf(rs.getString(3)));
            assertEquals(ElementType.randomElement(), ElementType.valueOf(rs.getString(4)));
            assertEquals(MonsterType.randomMonsterType(), MonsterType.valueOf(rs.getString(5)));
            assertEquals(30.50, rs.getFloat(6));


            // cleanup
            Statement sm = connection.createStatement();
            sm.executeUpdate("DELETE FROM cards WHERE id = " + card.getId() + ";");
            preparedStatement.close();
            rs.close();
            sm.close();
            connection.close();
        } catch (SQLException exception){
            exception.printStackTrace();

        }*/
    }




    @Test
    @DisplayName("Get a dragon card with the element type normal")
    void testGetCard__normalDragon(){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            Statement statement = connection.createStatement();

            MonsterCard card = (MonsterCard) cardService.getCard(0);

            statement.executeUpdate("DELETE FROM cards WHERE id = 0;");
            statement.close();
            connection.close();

            assertEquals("RandomMonster", card.getName());
            assertEquals(40.60f, card.getDamage());
            assertEquals(CardType.MONSTER, card.getCardType());
            assertEquals(MonsterType.DRAGON, card.getMonsterType());
            assertEquals(ElementType.NORMAL, card.getElementType());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    @DisplayName("Get a spell card(already in database) with element type water")
    void testGetCard__waterSpell(){
        try {
            Connection connection = DatabaseService.getInstance().getConnection();
            Statement statement = connection.createStatement();

            SpellCard card = (SpellCard) cardService.getCard(32);

            statement.executeUpdate("DELETE FROM cards WHERE id = 32;");
            statement.close();
            connection.close();

            assertEquals("Elixiro", card.getName());
            assertEquals(30.56f, card.getDamage());
            assertEquals(CardType.SPELL, card.getCardType());
            assertNull(card.getMonsterType());
            assertEquals(ElementType.WATER, card.getElementType());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


}