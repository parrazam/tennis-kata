# Tennis kata

This is a simple library to play a tennis game.

It contains default rules to check if a player won the game, if has advantage or the game is in _deuce_.

To use it, you have to create two players, providing the name for each one:

```java
Player playerOne = new Player("Awesome player");
Player playerTwo = new Player("Not too bad player");
```

Then, you can create the **Match**, providing the players and the rules set to use and you can start playing:

```java
Match match = new Match(playerOne, playerTwo, DefaultRules.builder.build());
match.init(); // Init reset scores for both players
```

With the **Match** class, you can control the whole game. You will able to:

- `playerOneScores()`/`playerTwoScores()` -> Call first method when server scores and second one when receiver scores.
- `getScore()` -> This method retrieves the score, first player one and second play two.
- `winner()` -> This method return an `Optional` that contains the player which won the game, or `empty()` if the game is still in course.
- `playerHasAdvantage()` -> If you need to know if there is a player with advantage, you can check it.

## Play tennis with custom rules

This library provides a default rules set following the standard rules:

- If score is 40:40, then the game is in _DEUCE_.
- If score is _DEUCE_ and one player scores, it will have the advantage.
- Player needs more than 40 and a difference of two points to win:
  - 40:30 and player one scores -> Player one wins.
  - A:40 and player one scores -> Player one wins.
  - 40:40 and player one scores -> He has the advantage, but isn't the winner yet.

If you want to define your custom rules, you have two ways:

### Implement the `Rules` interface

Implementing the `Rules<P extends Player>` interface is a way to define a class with your custom rules:

```java
public class MyRules implements Rules<Player> {

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
```

### Define rules from default ones and override only what you need

If you want to create just a custom rule, you can use the `DefaultRules` class and override dynamically those rules that you want to change:

```java
// With this custom rule, winner only needs a difference of one point to win the game, without advantage
DefaultRules.builder()
      .winner((s, r) -> {
        if (s.getScore() >= 4 && s.getScore() - r.getScore() >= 1) {
          return Optional.of(s);
        } else if (r.getScore() >= 4 && r.getScore() - s.getScore() >= 1) {
          return Optional.of(r);
        }
        return Optional.empty();
      })
      .build();
```

You can see both ways in `GameWithCustomRulesTest.java` file.
