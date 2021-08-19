package es.parravidales.tennis;

import es.parravidales.tennis.game.Match;
import es.parravidales.tennis.model.Player;
import es.parravidales.tennis.rules.DefaultRules;
import es.parravidales.tennis.rules.Rules;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

public class GameWithCustomRulesTest {

  private Match match;
  private Player p1;
  private Player p2;

  @BeforeEach
  public void before() {
    p1 = new Player("Player 1");
    p2 = new Player("Player 2");
  }

  @Test
  @DisplayName("PLayer 1 wins when reach forty with custom rules")
  public void test_first_player_reach_forty_wins_with_custom_rules() {

    match = new Match(p1, p2, new MyRules());

    match.init();
    match.playerOneScores(); // 15-0
    match.playerOneScores(); // 30-0
    match.playerTwoScores(); // 30-15
    match.playerTwoScores(); // 30-30
    match.playerOneScores(); // 40-30 -> Player 1 wins!
    assertTrue(match.winner().isPresent());
    assertEquals(p1, match.winner().get());
  }

  @Test
  @DisplayName("Game is restarted when deuce is reached and player 2 wins")
  public void test_game_is_restarted_when_deuce_is_reached_and_then_player_two_wins_with_custom_rules() {

    match = new Match(p1, p2, getDynamicRules());

    match.init();
    match.playerOneScores(); // 15-0
    match.playerOneScores(); // 30-0
    match.playerOneScores(); // 40-0
    assertTrue(match.winner().isEmpty()); // No winner yet...
    match.playerTwoScores(); // 40-15
    match.playerTwoScores(); // 40-30
    assertFalse(match.playerHasAdvantage());
    assertEquals("40", p1.translatedScore());
    assertEquals("30", p2.translatedScore());

    match.playerTwoScores(); // 40-40 -> Game restarted...
    assertFalse(match.playerHasAdvantage());
    assertEquals("0", p1.translatedScore());
    assertEquals("0", p2.translatedScore());

    match.playerOneScores(); // 15-0
    match.playerOneScores(); // 30-0
    match.playerTwoScores(); // 30-15
    match.playerTwoScores(); // 30-30
    match.playerTwoScores(); // 30-40
    assertTrue(match.winner().isEmpty());
    assertFalse(match.playerHasAdvantage());

    match.playerTwoScores(); // Player 2 wins!
    assertTrue(match.winner().isPresent());
    assertEquals(p2, match.winner().get());
  }

  private Rules<Player> getDynamicRules() {
    return DefaultRules.builder()
      .deuce((s, r) -> {
        if (s.getScore() >= 3 && s.getScore() == r.getScore()) {
          s.reset();
          r.reset();
        }
        return false;
      })
      .winner((s, r) -> {
        if (s.getScore() >= 4 && s.getScore() - r.getScore() >= 1) {
          return Optional.of(s);
        } else if (r.getScore() >= 4 && r.getScore() - s.getScore() >= 1) {
          return Optional.of(r);
        }
        return Optional.empty();
      })
      .build();
  }

  @Getter
  class MyRules implements Rules<Player> {

    private BiFunction<Player, Player, Optional<Player>> winner = (s, r) -> {
      if (s.getScore() >= 3 && s.getScore() - r.getScore() >= 1) {
        return Optional.of(s);
      } else if (r.getScore() >= 3 && r.getScore() - s.getScore() >= 1) {
        return Optional.of(r);
      }
      return Optional.empty();
    };

    private BiFunction<Player, Player, Boolean> deuce = (s, r) -> false;

    private BiFunction<Player, Player, Boolean> hasAdvantage = (s, r) -> false;
  }
}
