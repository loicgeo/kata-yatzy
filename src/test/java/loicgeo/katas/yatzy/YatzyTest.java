package loicgeo.katas.yatzy;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import loicgeo.katas.yatzy.exception.FonctionalException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static loicgeo.katas.yatzy.Yatzy.RollType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

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
                new Object[]{1, 1, 1, 1, 1, ONES, 5}
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
        assertThat(score).isEqualTo(expectedScore);
    }

    @Test
    public void test_2s() {
        assertEquals(4, Yatzy.twos(1, 2, 3, 2, 6));
        assertEquals(10, Yatzy.twos(2, 2, 2, 2, 2));
    }

    @Test
    public void test_threes() {
        assertEquals(6, Yatzy.threes(1, 2, 3, 2, 3));
        assertEquals(15, Yatzy.threes(3, 3, 3, 3, 3));
    }

    @Test
    public void fours_test() throws FonctionalException {
        assertEquals(12, new Yatzy(4, 4, 4, 5, 5).fours());
        assertEquals(8, new Yatzy(4, 4, 5, 5, 5).fours());
        assertEquals(4, new Yatzy(4, 5, 5, 5, 5).fours());
    }

    @Test
    public void fives() throws FonctionalException {
        assertEquals(10, new Yatzy(4, 4, 4, 5, 5).fives());
        assertEquals(15, new Yatzy(4, 4, 5, 5, 5).fives());
        assertEquals(20, new Yatzy(4, 5, 5, 5, 5).fives());
    }

    @Test
    public void sixes_test() throws FonctionalException {
        assertEquals(0, new Yatzy(4, 4, 4, 5, 5).sixes());
        assertEquals(6, new Yatzy(4, 4, 6, 5, 5).sixes());
        assertEquals(18, new Yatzy(6, 5, 6, 6, 5).sixes());
    }

    @Test
    public void one_pair() {
        assertEquals(6, Yatzy.score_pair(3, 4, 3, 5, 6));
        assertEquals(10, Yatzy.score_pair(5, 3, 3, 3, 5));
        assertEquals(12, Yatzy.score_pair(5, 3, 6, 6, 5));
        assertEquals(0, Yatzy.score_pair(1, 3, 4, 6, 5));
    }

    @Test
    public void two_Pair() {
        assertEquals(16, Yatzy.two_pair(3, 3, 5, 4, 5));
        assertEquals(16, Yatzy.two_pair(3, 3, 5, 5, 5));
        assertEquals(0, Yatzy.two_pair(3, 1, 5, 2, 4));
    }

    @Test
    public void three_of_a_kind() {
        assertEquals(0, Yatzy.three_of_a_kind(1, 3, 5, 4, 5));
        assertEquals(9, Yatzy.three_of_a_kind(3, 3, 3, 4, 5));
        assertEquals(9, Yatzy.three_of_a_kind(3, 3, 3, 3, 5));
    }

    @Test
    public void four_of_a_knd() {
        assertEquals(12, Yatzy.four_of_a_kind(3, 3, 3, 3, 5));
        assertEquals(20, Yatzy.four_of_a_kind(5, 5, 5, 4, 5));
        assertEquals(0, Yatzy.four_of_a_kind(3, 1, 2, 3, 3));
    }

    @Test
    public void smallStraight() {
        assertEquals(15, Yatzy.smallStraight(1, 2, 3, 4, 5));
        assertEquals(15, Yatzy.smallStraight(2, 3, 4, 5, 1));
        assertEquals(0, Yatzy.smallStraight(1, 2, 2, 4, 5));
    }

    @Test
    public void largeStraight() {
        assertEquals(20, Yatzy.largeStraight(6, 2, 3, 4, 5));
        assertEquals(20, Yatzy.largeStraight(2, 3, 4, 5, 6));
        assertEquals(0, Yatzy.largeStraight(1, 2, 2, 4, 5));
    }

    @Test
    public void fullHouse() {
        assertEquals(18, Yatzy.fullHouse(6, 2, 2, 2, 6));
        assertEquals(0, Yatzy.fullHouse(2, 3, 4, 5, 6));
    }
}
