package es.parravidales.tennis.rules;

import es.parravidales.tennis.model.Player;

import java.util.Optional;
import java.util.function.BiFunction;

public interface Rules<P extends Player> {

  BiFunction<P, P, Optional<P>> getWinner();
  BiFunction<P, P, Boolean> getHasAdvantage();
  BiFunction<P, P, Boolean> getDeuce();
}
