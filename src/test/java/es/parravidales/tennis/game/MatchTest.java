package es.parravidales.tennis.game;

import es.parravidales.tennis.model.Player;
import es.parravidales.tennis.rules.Rules;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;

class MatchTest {

  private Match sut;
  @Mock
  private Player p1 = mock(Player.class);
  private Player p2 = mock(Player.class);
  private Rules<Player> rules = mock(Rules.class);

  @BeforeEach
  public void before() {
    reset(p1);
    reset(p2);
    reset(rules);
    given(rules.getDeuce()).willReturn((s, r) -> false);
    given(rules.getWinner()).willReturn((s, r) -> Optional.empty());
    given(rules.getHasAdvantage()).willReturn((s, r) -> false);
    sut = new Match(p1, p2, rules);
  }

  @Test
  @DisplayName("When match init the game, scores should be reset")
  public void test_when_match_init_the_game_then_scores_should_be_reset() {
    willDoNothing().given(p1).reset();
    willDoNothing().given(p2).reset();
    sut.init();
    then(p1).should().reset();
    then(p2).should().reset();
  }

  @Test
  @DisplayName("When server increase score, rules should check deuce state")
  void test_when_server_increase_score_then_rules_should_check_deuce() {
    willDoNothing().given(p1).increaseScore();
    sut.playerOneScores();
    then(p1).should().increaseScore();
    then(p2).should(never()).increaseScore();
    then(rules).should().getDeuce();
    then(rules).should(never()).getHasAdvantage();
    then(rules).should(never()).getWinner();
  }

  @Test
  @DisplayName("When receiver increase score, rules should check deuce state")
  void test_when_receiver_increase_score_then_rules_should_check_deuce() {
    willDoNothing().given(p2).increaseScore();
    sut.playerTwoScores();
    then(p2).should().increaseScore();
    then(p1).should(never()).increaseScore();
    then(rules).should().getDeuce();
    then(rules).should(never()).getHasAdvantage();
    then(rules).should(never()).getWinner();
  }

  @Test
  @DisplayName("When someone checks the score, rules have to check the advantage")
  public void test_when_someone_checks_the_score_then_rules_should_check_advantage() {
    sut.getScore();
    then(rules).should(times(2)).getHasAdvantage(); // One per player
  }

  @Test
  @DisplayName("When someone ask for a winner, rules should return the winner if exists")
  void test_when_someone_ask_for_a_winner_then_rules_should_return_the_winner_if_exists() {
    sut.winner();
    then(rules).should().getWinner();
  }
}