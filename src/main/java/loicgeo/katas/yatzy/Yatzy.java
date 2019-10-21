package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Yatzy {

    private static final Logger LOGGER = Logger.getLogger(Yatzy.class.getName());

    private DiceSerie diceSerie;

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

        // sum all dice by value
        ONES(diceSerie ->
                sumOccurrencesByValue(diceSerie, 1)
        ),
        TWOS(diceSerie ->
                sumOccurrencesByValue(diceSerie, 2)
        ),
        THREES(diceSerie ->
                sumOccurrencesByValue(diceSerie, 3)
        ),
        FOURS(diceSerie ->
                sumOccurrencesByValue(diceSerie, 4)
        ),
        FIVES(diceSerie ->
                sumOccurrencesByValue(diceSerie, 5)
        ),
        SIXES(diceSerie ->
                sumOccurrencesByValue(diceSerie, 6)
        ),

        // sum values for the best pair
        PAIR(diceSerie -> {
            return scoreByNbOccurrencesAndNbMatches(diceSerie, 2, 1);
        }),

        // sum values for the best 2 pairs
        TWO_PAIRS(diceSerie -> {
            return scoreByNbOccurrencesAndNbMatches(diceSerie, 2, 2);
        }),

        // sum values for value having 3 occurrences
        THREE_Of_A_KIND(diceSerie -> {
            return scoreByNbOccurrencesAndNbMatches(diceSerie, 3, 1);
        }),

        // sum values for value having 4 occurrences
        FOUR_Of_A_KIND(diceSerie -> {
            return scoreByNbOccurrencesAndNbMatches(diceSerie, 4, 1);
        });

        /**
         * Score a dice serie according:
         * <li></li>
         *
         * @param diceSerie          a set of dice values
         * @param nbValueOccurrences number of occurrences to find for any value
         * @param nbMatches          number to reach of this motif (ex: for a pair, 2 pairs, etc)
         * @return
         */
        private static int scoreByNbOccurrencesAndNbMatches(DiceSerie diceSerie, final int nbValueOccurrences, final int nbMatches) {
            int[] countsByValue = diceSerie.getOccurrenciesByDiceValue();

            int score = 0;
            int nbMatchs = 0;
            for (int currentValue = countsByValue.length - 1; currentValue > 0; currentValue--) {
                int nbOccurrencesOfCurrentValue = countsByValue[currentValue];
                if (nbOccurrencesOfCurrentValue >= nbValueOccurrences) {
                    nbMatchs++;
                    score += currentValue * Math.min(nbOccurrencesOfCurrentValue, nbValueOccurrences);
                    if (nbMatchs >= nbMatches) {
                        break;
                    }
                }
            }
            return nbMatches == nbMatchs ? score : 0;
        }

        private static Integer sumOccurrencesByValue(DiceSerie diceSerie, int value) {
            return diceSerie.getValues().stream()
                    .filter(v -> v == value)
                    .reduce(0, Integer::sum);
        }

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
    }

    public int scoreDices(RollType rollType) {
        return rollType.score(diceSerie);
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



