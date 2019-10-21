package loicgeo.katas.yatzy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import loicgeo.katas.yatzy.exception.FonctionalException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.joining;
import static loicgeo.katas.yatzy.Yatzy.RollType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(JUnitParamsRunner.class)
public class YatzyTest {

    @Test
    public void should_throw_an_functional_exception_for_a_wrong_dice_serie_scoring() {
        // given
        ThrowableAssert.ThrowingCallable yatzyInstantiation = () -> new Yatzy(0, 3, 4, 5, 1);

        // when
        // then
        assertThatThrownBy(yatzyInstantiation)
                .isInstanceOf(FonctionalException.class)
                .hasMessageContaining("Wrong dice values");
    }

    @Test
    public void should_score_a_dice_serie_for_a_chance_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 4, 5, 1);
        // when
        int score = yatzy.scoreDices(CHANCE);
        // then
        assertThat(score).isEqualTo(16);
    }

    @Test
    public void should_score_50_a_dice_serie_for_a_yatzy_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(4, 4, 4, 4, 4);
        // when
        int score = yatzy.scoreDices(YATZY);
        // then
        assertThat(score).isEqualTo(50);
    }

    @Test
    public void should_score_0_a_dice_serie_for_a_non_yatzy_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(6, 6, 6, 6, 3);
        // when
        int score = yatzy.scoreDices(YATZY);
        // then
        assertThat(score).isEqualTo(0);
    }

    public static Object[] givenSeriesForEachKindOfValues() {
        return new Object[]{
                new Object[]{1, 2, 3, 4, 5, ONES, 1},
                new Object[]{1, 2, 1, 4, 5, ONES, 2},
                new Object[]{6, 2, 2, 4, 5, ONES, 0},
                new Object[]{1, 1, 1, 1, 1, ONES, 5},

                new Object[]{1, 2, 3, 2, 6, TWOS, 4},
                new Object[]{2, 2, 2, 2, 2, TWOS, 10},

                new Object[]{1, 2, 3, 2, 3, THREES, 6},
                new Object[]{3, 3, 3, 3, 3, THREES, 15},

                new Object[]{4, 4, 4, 5, 5, FOURS, 12},
                new Object[]{4, 4, 5, 5, 5, FOURS, 8},
                new Object[]{4, 5, 5, 5, 5, FOURS, 4},

                new Object[]{4, 4, 4, 5, 5, FIVES, 10},
                new Object[]{4, 4, 5, 5, 5, FIVES, 15},
                new Object[]{4, 5, 5, 5, 5, FIVES, 20},

                new Object[]{4, 4, 4, 5, 5, SIXES, 0},
                new Object[]{4, 4, 6, 4, 5, SIXES, 6},
                new Object[]{6, 5, 6, 6, 5, SIXES, 18}
        };
    }

    @Test
    @Parameters(method = "givenSeriesForEachKindOfValues")
    public void should_score_several_dice_series_for_each_kind_of_value(int d1, int d2, int d3, int d4, int d5, Yatzy.RollType rollType, int expectedScore) throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(d1, d2, d3, d4, d5);
        // when
        int score = yatzy.scoreDices(rollType);
        // then
        assertThat(score)
                .overridingErrorMessage(format("A {0} roll should score {1} for values ''{2}''",
                        rollType,
                        expectedScore,
                        Stream.of(d1, d2, d3, d4, d5).map(Object::toString).collect(joining(", "))))
                .isEqualTo(expectedScore)
        ;
    }

    @Test
    public void should_score_dice_serie_for_pair_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 4, 3, 5, 6);
        // when
        int score = yatzy.scoreDices(PAIR);
        // then
        assertThat(score).isEqualTo(6);
    }

    @Test
    public void should_score_0_for_pair_roll_having_none_pair() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 3, 4, 6, 5);
        // when
        int score = yatzy.scoreDices(PAIR);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_score_the_best_pair_for_pair_roll_having_2_pairs() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(5, 3, 6, 6, 5);
        // when
        int score = yatzy.scoreDices(PAIR);
        // then
        assertThat(score).isEqualTo(12);
    }

    @Test
    public void should_score_only_2_values_for_a_pair_roll_composed_of_3_same_values() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 3, 4, 1);
        // when
        int score = yatzy.scoreDices(PAIR);
        // then
        assertThat(score).isEqualTo(6);
    }

    @Test
    public void should_score_a_2_pairs_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 5, 4, 5);
        // when
        int score = yatzy.scoreDices(TWO_PAIRS);
        // then
        assertThat(score).isEqualTo(16);
    }

    @Test
    public void should_score_a_2_pairs_roll_composed_of_3_same_values() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 5, 5, 5);
        // when
        int score = yatzy.scoreDices(TWO_PAIRS);
        // then
        assertThat(score).isEqualTo(16);
    }

    @Test
    public void should_score_0_a_2_pairs_roll_without_2_pairs() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 1, 2, 3, 4);
        // when
        int score = yatzy.scoreDices(TWO_PAIRS);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_score_three_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 3, 4, 5);
        // when
        int score = yatzy.scoreDices(THREE_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(9);
    }

    @Test
    public void should_score_0_for_none_match_three_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 3, 5, 4, 5);
        // when
        int score = yatzy.scoreDices(THREE_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_only_3_values_of_a_4_occurrenced_values_for_a_three_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 3, 3, 5);
        // when
        int score = yatzy.scoreDices(THREE_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(9);
    }

    @Test
    public void should_score_four_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 3, 3, 3, 5);
        // when
        int score = yatzy.scoreDices(FOUR_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(12);
    }

    @Test
    public void should_score_0_for_none_match_four_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(3, 1, 2, 3, 3);
        // when
        int score = yatzy.scoreDices(FOUR_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_only_4_values_of_a_5_occurrenced_values_for_a_three_values_of_a_kind() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(2, 2, 2, 2, 2);
        // when
        int score = yatzy.scoreDices(FOUR_Of_A_KIND);
        // then
        assertThat(score).isEqualTo(8);
    }

    @Test
    public void should_sum_values_of_a_small_straight() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(2, 3, 4, 5, 1);
        // when
        int score = yatzy.scoreDices(SMALL_STRAIGHT);
        // then
        assertThat(score).isEqualTo(15);
    }

    @Test
    public void should_sum_0_for_non_existing_small_straight() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 2, 2, 4, 5);
        // when
        int score = yatzy.scoreDices(SMALL_STRAIGHT);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_0_on_large_straight_for_a_small_straight_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(2, 3, 4, 5, 6);
        // when
        int score = yatzy.scoreDices(SMALL_STRAIGHT);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_values_of_a_large_straight() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(6, 2, 3, 4, 5);
        // when
        int score = yatzy.scoreDices(LARGE_STRAIGHT);
        // then
        assertThat(score).isEqualTo(20);
    }

    @Test
    public void should_sum_0_for_non_existing_large_straight() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 2, 2, 4, 5);
        // when
        int score = yatzy.scoreDices(LARGE_STRAIGHT);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_0_on_small_straight_for_a_large_straight_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(1, 2, 3, 4, 5);
        // when
        int score = yatzy.scoreDices(LARGE_STRAIGHT);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_a_full_house_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(6, 2, 2, 2, 6);
        // when
        int score = yatzy.scoreDices(FULL_HOUSE);
        // then
        assertThat(score).isEqualTo(18);
    }

    @Test
    public void should_sum_0_a_non_full_house_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(2, 3, 4, 5, 6);
        // when
        int score = yatzy.scoreDices(FULL_HOUSE);
        // then
        assertThat(score).isEqualTo(0);
    }

    @Test
    public void should_sum_0_a_yatzy_for_a_full_house_roll() throws FonctionalException {
        // given
        Yatzy yatzy = new Yatzy(2, 2, 2, 2, 2);
        // when
        int score = yatzy.scoreDices(FULL_HOUSE);
        // then
        assertThat(score).isEqualTo(0);
    }
    
}
