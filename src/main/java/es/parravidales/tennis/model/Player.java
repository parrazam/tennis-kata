package es.parravidales.tennis.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Player {

  final String name;
  int score = 0;

  public void increaseScore() {
    score++;
  }

  public void reset() {
    score = 0;
  }

  public String translatedScore() {
    switch (score) {
      case 0:
        return "0";
      case 1:
        return "15";
      case 2:
        return "30";
      default:
        return "40";
    }
  }

  @Override
  public String toString() {
    return name;
  }
}
