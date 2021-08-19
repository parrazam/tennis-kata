package es.parravidales.tennis.game;

import es.parravidales.tennis.model.Player;
import es.parravidales.tennis.rules.Rules;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
@Getter
@RequiredArgsConstructor
public class Match {

  private final Player playerOne;
  private final Player playerTwo;

  private final Rules<Player> rules;

  public void playerOneScores() {
    playerScores(playerOne);
  }

  public void playerTwoScores() {
    playerScores(playerTwo);
  }

  public Optional<Player> winner() {
    return rules.getWinner().apply(playerOne, playerTwo);
  }

  public void init() {
    log.debug("Resetting score...");
    playerOne.reset();
    playerTwo.reset();
  }

  public String getScore() {
    if (playerHasAdvantage() && playerOne.getScore() > playerTwo.getScore()) return "A:" + playerTwo.translatedScore();
    else if (playerHasAdvantage() && playerOne.getScore() < playerTwo.getScore()) return playerOne.translatedScore() + ":A";
    return playerOne.translatedScore() + ":" + playerTwo.translatedScore();
  }

  private void playerScores(Player player) {
    log.debug("{} scores", player);
    player.increaseScore();
    checkDeuce();
  }

  private boolean checkDeuce() {
    return rules.getDeuce().apply(playerOne, playerTwo);
  }

  public boolean playerHasAdvantage() {
    return rules.getHasAdvantage().apply(playerOne, playerTwo);
  }
}
