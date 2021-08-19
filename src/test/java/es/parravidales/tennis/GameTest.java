package es.parravidales.tennis;

import es.parravidales.tennis.game.Match;
import es.parravidales.tennis.model.Player;
import es.parravidales.tennis.rules.DefaultRules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

  private Match match;
  private Player p1;
  private Player p2;

  @BeforeEach
  public void before() {
    p1 = new Player("Player 1");
    p2 = new Player("Player 2");
    match = new Match(p1, p2, DefaultRules.builder().build());
  }

  @Test
  @DisplayName("PLayer 1 wins with default rules")
  public void test_player_one_wins_with_default_rules() {

    match.init();
    match.playerOneScores(); // 15-0
    match.playerOneScores(); // 30-0
    match.playerOneScores(); // 40-0
    match.playerTwoScores(); // 40-15
    match.playerTwoScores(); // 40-30
    assertTrue(match.winner().isEmpty()); // No winner yet...

    match.playerOneScores(); // Player 1 wins!
    assertTrue(match.winner().isPresent());
    assertEquals(p1, match.winner().get());
  }

  @Test
  @DisplayName("PLayer 2 wins after deuce with advantage")
  public void test_player_two_wins_after_deuce_with_default_rules() {

    match.init();
    match.playerOneScores(); // 15-0
    match.playerOneScores(); // 30-0
    match.playerOneScores(); // 40-0
    assertTrue(match.winner().isEmpty()); // No winner yet...
    match.playerTwoScores(); // 40-15
    match.playerTwoScores(); // 40-30
    assertFalse(match.playerHasAdvantage());
    match.playerTwoScores(); // 40-40 -> DEUCE
    assertFalse(match.playerHasAdvantage());

    match.playerTwoScores(); // 40-A
    assertEquals("40:A", match.getScore()); // Player 2 has advantage
    assertTrue(match.winner().isEmpty());

    match.playerOneScores(); // 40-40 -> DEUCE
    assertTrue(match.winner().isEmpty());

    match.playerTwoScores(); // Back to 40-A
    assertEquals("40:A", match.getScore()); // Player 2 has advantage
    assertTrue(match.winner().isEmpty());

    match.playerTwoScores(); // Player 2 wins!
    assertTrue(match.winner().isPresent());
    assertEquals(p2, match.winner().get());
  }
}
