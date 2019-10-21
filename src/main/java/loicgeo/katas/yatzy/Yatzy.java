package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.function.ToIntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.joining;

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
            return diceSerie.hasOnlyOneDifferentNumber() ? 50 : 0;
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
        }),

        // sum values for a small straight, meaning composed of a continuous sequence of at least 4 values, starting by 1
        SMALL_STRAIGHT(diceSerie -> getSortedValues(diceSerie).contains("12345") ? 15 : 0
        ),

        // sum values for a large straight, meaning composed of a continuous sequence of at least 4 values, finishing by 6
        LARGE_STRAIGHT(diceSerie -> getSortedValues(diceSerie).contains("23456") ? 20 : 0
        ),

        // sum values for a set of 3 values X and a couple of values Y, where X is different of Y
        FULL_HOUSE(diceSerie -> {
            if (diceSerie.hasOnlyOneDifferentNumber()) {
                return 0;
            }
            return scoreByNbOccurrencesAndNbMatches(diceSerie, 3, 1)
                    + scoreByNbOccurrencesAndNbMatches(diceSerie, 2, 1);
        });

        private static String getSortedValues(DiceSerie diceSerie) {
            return diceSerie.getValues().stream()
                    .sorted()
                    .map(Object::toString)
                    .collect(joining(""));
        }

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

}



