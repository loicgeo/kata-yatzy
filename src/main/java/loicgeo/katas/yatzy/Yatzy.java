package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Yatzy {

    private static final Logger LOGGER = Logger.getLogger(Yatzy.class.getName());

    private DiceSerie diceSerie;
    private int[] dice;

    public enum RollType {
        // sum all values
        CHANCE(diceSerie ->
                diceSerie.getValues().stream()
                        .reduce(0, Integer::sum)
        ),

        // only one distinct value
        YATZY(diceSerie -> {
            boolean serieHasOnlyOneDifferentNumber = diceSerie.getValues().stream()
                    .distinct().count() == 1;
            return serieHasOnlyOneDifferentNumber ? 50 : 0;
        }),

        // sum all dice of value 1
        ONES(diceSerie ->
                diceSerie.getValues().stream()
                        .filter(v -> v == 1)
                        .reduce(0, Integer::sum)
        );

        private final ToIntFunction<DiceSerie> scoringFunction;

        RollType(ToIntFunction<DiceSerie> scoringFunction) {
            this.scoringFunction = scoringFunction;
        }

        public int score(DiceSerie diceSerie) {
            return scoringFunction.applyAsInt(diceSerie);
        }
    }

    /**
     * Instantiation a Yatzy dice set.
     *
     * @param d1 number value for dice #1
     * @param d2 number value for dice #2
     * @param d3 number value for dice #3
     * @param d4 number value for dice #4
     * @param d5 number value for dice #5
     * @throws FonctionalException in case of invalid dice serie. See details on {@link DiceSerie}.
     * @see DiceSerie
     * @since 0.2
     */
    public Yatzy(int d1, int d2, int d3, int d4, int d5) throws FonctionalException {
        try {
            diceSerie = new DiceSerie(d1, d2, d3, d4, d5);
        } catch (FonctionalException exception) {
            LOGGER.log(Level.SEVERE, "Unable to create Yatzy", exception);
            throw exception;
        }

        dice = new int[5];
        dice[0] = d1;
        dice[1] = d2;
        dice[2] = d3;
        dice[3] = d4;
        dice[4] = d5;
    }

    public int scoreDices(RollType rollType) {
        return rollType.score(diceSerie);
    }

    public static int twos(int d1, int d2, int d3, int d4, int d5) {
        int sum = 0;
        if (d1 == 2)
            sum += 2;
        if (d2 == 2)
            sum += 2;
        if (d3 == 2)
            sum += 2;
        if (d4 == 2)
            sum += 2;
        if (d5 == 2)
            sum += 2;
        return sum;
    }

    public static int threes(int d1, int d2, int d3, int d4, int d5) {
        int s;
        s = 0;
        if (d1 == 3)
            s += 3;
        if (d2 == 3)
            s += 3;
        if (d3 == 3)
            s += 3;
        if (d4 == 3)
            s += 3;
        if (d5 == 3)
            s += 3;
        return s;
    }

    public int fours() {
        int sum;
        sum = 0;
        for (int at = 0; at != 5; at++) {
            if (dice[at] == 4) {
                sum += 4;
            }
        }
        return sum;
    }

    public int fives() {
        int s = 0;
        int i;
        for (i = 0; i < dice.length; i++)
            if (dice[i] == 5)
                s = s + 5;
        return s;
    }

    public int sixes() {
        int sum = 0;
        for (int at = 0; at < dice.length; at++)
            if (dice[at] == 6)
                sum = sum + 6;
        return sum;
    }

    public static int score_pair(int d1, int d2, int d3, int d4, int d5) {
        int[] counts = new int[6];
        counts[d1 - 1]++;
        counts[d2 - 1]++;
        counts[d3 - 1]++;
        counts[d4 - 1]++;
        counts[d5 - 1]++;
        int at;
        for (at = 0; at != 6; at++)
            if (counts[6 - at - 1] >= 2)
                return (6 - at) * 2;
        return 0;
    }

    public static int two_pair(int d1, int d2, int d3, int d4, int d5) {
        int[] counts = new int[6];
        counts[d1 - 1]++;
        counts[d2 - 1]++;
        counts[d3 - 1]++;
        counts[d4 - 1]++;
        counts[d5 - 1]++;
        int n = 0;
        int score = 0;
        for (int i = 0; i < 6; i += 1)
            if (counts[6 - i - 1] >= 2) {
                n++;
                score += (6 - i);
            }
        if (n == 2)
            return score * 2;
        else
            return 0;
    }

    public static int four_of_a_kind(int _1, int _2, int d3, int d4, int d5) {
        int[] tallies;
        tallies = new int[6];
        tallies[_1 - 1]++;
        tallies[_2 - 1]++;
        tallies[d3 - 1]++;
        tallies[d4 - 1]++;
        tallies[d5 - 1]++;
        for (int i = 0; i < 6; i++)
            if (tallies[i] >= 4)
                return (i + 1) * 4;
        return 0;
    }

    public static int three_of_a_kind(int d1, int d2, int d3, int d4, int d5) {
        int[] t;
        t = new int[6];
        t[d1 - 1]++;
        t[d2 - 1]++;
        t[d3 - 1]++;
        t[d4 - 1]++;
        t[d5 - 1]++;
        for (int i = 0; i < 6; i++)
            if (t[i] >= 3)
                return (i + 1) * 3;
        return 0;
    }

    public static int smallStraight(int d1, int d2, int d3, int d4, int d5) {
        int[] tallies;
        tallies = new int[6];
        tallies[d1 - 1] += 1;
        tallies[d2 - 1] += 1;
        tallies[d3 - 1] += 1;
        tallies[d4 - 1] += 1;
        tallies[d5 - 1] += 1;
        if (tallies[0] == 1 && tallies[1] == 1 && tallies[2] == 1 && tallies[3] == 1 && tallies[4] == 1)
            return 15;
        return 0;
    }

    public static int largeStraight(int d1, int d2, int d3, int d4, int d5) {
        int[] tallies;
        tallies = new int[6];
        tallies[d1 - 1] += 1;
        tallies[d2 - 1] += 1;
        tallies[d3 - 1] += 1;
        tallies[d4 - 1] += 1;
        tallies[d5 - 1] += 1;
        if (tallies[1] == 1 && tallies[2] == 1 && tallies[3] == 1 && tallies[4] == 1 && tallies[5] == 1)
            return 20;
        return 0;
    }

    public static int fullHouse(int d1, int d2, int d3, int d4, int d5) {
        int[] tallies;
        boolean _2 = false;
        int i;
        int _2_at = 0;
        boolean _3 = false;
        int _3_at = 0;

        tallies = new int[6];
        tallies[d1 - 1] += 1;
        tallies[d2 - 1] += 1;
        tallies[d3 - 1] += 1;
        tallies[d4 - 1] += 1;
        tallies[d5 - 1] += 1;

        for (i = 0; i != 6; i += 1)
            if (tallies[i] == 2) {
                _2 = true;
                _2_at = i + 1;
            }

        for (i = 0; i != 6; i += 1)
            if (tallies[i] == 3) {
                _3 = true;
                _3_at = i + 1;
            }

        if (_2 && _3)
            return _2_at * 2 + _3_at * 3;
        else
            return 0;
    }

}



