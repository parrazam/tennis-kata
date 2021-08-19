package es.parravidales.tennis.model;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PlayerTest {

  @Test
  @DisplayName("Test player translate score correctly")
  public void test_translate_score_increase_correctly() {

    Player sut = new Player(UUID.randomUUID().toString());
    assertAll("Love",
      () -> {
        assertEquals("0", sut.translatedScore());
        sut.increaseScore();
        assertAll("Fifteen",
          () -> {
            assertEquals("15", sut.translatedScore());
            sut.increaseScore();
            assertAll("Thirty",
              () -> {
                assertEquals("30", sut.translatedScore());
                sut.increaseScore();
                assertAll("Forty",
                  () -> {
                    assertEquals("40", sut.translatedScore());
                    sut.increaseScore();
                  });
              });
          });
      });
  }

  @Test
  @DisplayName("When someone reset score, should be set to 0")
  public void test_when_someone_reset_score_then_should_be_set_to_zero() {
    Player sut = new Player(UUID.randomUUID().toString());
    sut.increaseScore();
    assertEquals(1, sut.getScore());
    sut.reset();
    assertEquals(0, sut.getScore());
  }
}