package es.parravidales.tennis.rules;

import java.util.Optional;
import java.util.function.BiFunction;

import es.parravidales.tennis.model.Player;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
public class DefaultRules implements Rules<Player> {

  @Builder.Default
  BiFunction<Player, Player, Optional<Player>> winner = (s, r) -> {
    if (s.getScore() >= 4 && s.getScore() - r.getScore() >= 2) {
      return Optional.of(s);
    } else if (r.getScore() >= 4 && r.getScore() - s.getScore() >= 2) {
      return Optional.of(r);
    }
    return Optional.empty();
  };

  @Builder.Default
  private BiFunction<Player, Player, Boolean> hasAdvantage =
      (s, r) -> s.getScore() >= 3 && r.getScore() >= 3 && Math.abs(s.getScore() - r.getScore()) >= 1;

  @Builder.Default
  BiFunction<Player, Player, Boolean> deuce = (s, r) -> s.getScore() >= 3 && s.getScore() == r.getScore();

}
