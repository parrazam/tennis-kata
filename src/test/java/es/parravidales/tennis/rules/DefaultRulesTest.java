package es.parravidales.tennis.rules;

import es.parravidales.tennis.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class DefaultRulesTest {

  private DefaultRules sut;
  private Player playerOne;
  private Player playerTwo;

  @BeforeEach
  public void before() {

    sut = DefaultRules.builder().build();
    playerOne = mock(Player.class);
    given(playerOne.getScore()).willCallRealMethod();
    playerTwo = mock(Player.class);
    given(playerTwo.getScore()).willCallRealMethod();
  }

  @Test
  @DisplayName("No winner while no ones reach forty")
  void test_winner_is_empty_while_no_ones_reach_forty() {

    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    mockScore(1,0); // 15-0
    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    mockScore(2,0); // 30-0
    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    mockScore(3,0); // 40-0
    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    mockScore(4,0); // 40-0 and match point
    assertFalse(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    assertEquals(playerOne, sut.getWinner().apply(playerOne, playerTwo).get());
  }

  @Test
  @DisplayName("Winner is the first to reach +40 with difference of 2 or more points")
  void test_winner_is_the_first_to_reach_forty_with_a_minimum_difference_of_two_points() {

    mockScore(3,1);
    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());

    mockScore(4,3);
    assertTrue(sut.getWinner().apply(playerOne, playerTwo).isEmpty());

    mockScore(2,4);
    assertFalse(sut.getWinner().apply(playerOne, playerTwo).isEmpty());
    assertEquals(playerTwo, sut.getWinner().apply(playerOne, playerTwo).get());
  }

  @Test
  @DisplayName("Player has advantage when both have forty or more and difference is one point")
  void test_player_has_advantage_over_forty_and_with_a_difference_of_one_point_at_least() {

    mockScore(3,1);
    assertFalse(sut.getHasAdvantage().apply(playerOne, playerTwo));

    // Advantage requires have 40 or more for both players
    mockScore(4,1);
    assertFalse(sut.getHasAdvantage().apply(playerOne, playerTwo));

    mockScore(4,4);
    assertFalse(sut.getHasAdvantage().apply(playerOne, playerTwo));

    // A-40 after a deuce state
    mockScore(5,4);
    assertTrue(sut.getHasAdvantage().apply(playerOne, playerTwo));

    // 40-A
    mockScore(3,4);
    assertTrue(sut.getHasAdvantage().apply(playerOne, playerTwo));
  }

  @Test
  @DisplayName("Deuce is only when both players reach 40 and have same score")
  void test_deuce_is_reached_only_with_same_score_over_forty() {

    mockScore(2, 2);
    assertFalse(sut.getDeuce().apply(playerOne, playerTwo));

    mockScore(5, 4);
    assertFalse(sut.getDeuce().apply(playerOne, playerTwo));

    mockScore(3, 3);
    assertTrue(sut.getDeuce().apply(playerOne, playerTwo));

    mockScore(10, 10);
    assertTrue(sut.getDeuce().apply(playerOne, playerTwo));
  }

  private void mockScore(int playerOneScore, int playerTwoScore) {

    given(playerOne.getScore()).willReturn(playerOneScore);
    given(playerTwo.getScore()).willReturn(playerTwoScore);
  }
}