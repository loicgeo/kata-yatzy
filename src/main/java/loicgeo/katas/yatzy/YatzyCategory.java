package loicgeo.katas.yatzy;

import java.util.function.ToIntFunction;

import static loicgeo.katas.yatzy.YatzyCategory.DefaultScores.*;

public enum YatzyCategory {

    // sum all values
    CHANCE(dices -> dices.getValues().stream().reduce(0, Integer::sum)),

    // only one distinct value
    YATZY(dices -> dices.isComposedOfOnlyOneKindOfValue() ? SCORE_YATZY.getScore() : 0),

    // sum all dice by value
    ONES(dices -> sumByDiceValue(dices, 1)),
    TWOS(dices -> sumByDiceValue(dices, 2)),
    THREES(dices -> sumByDiceValue(dices, 3)),
    FOURS(dices -> sumByDiceValue(dices, 4)),
    FIVES(dices -> sumByDiceValue(dices, 5)),
    SIXES(dices -> sumByDiceValue(dices, 6)),

    // sum values for the best pair
    PAIR(dices -> scoreValues(dices, 2, 1)),
    // sum values for the best 2 pairs
    TWO_PAIRS(dices -> scoreValues(dices, 2, 2)),

    // sum values for value having 3 occurrences
    THREE_Of_A_KIND(dices -> scoreValues(dices, 3, 1)),
    // sum values for value having 4 occurrences
    FOUR_Of_A_KIND(dices -> scoreValues(dices, 4, 1)),

    // sum values for a small straight, meaning composed of a continuous sequence of at least 4 values, starting by 1
    SMALL_STRAIGHT(dices -> dices.getValuesAsString("").contains("12345") ? SCORE_SMALL_STRAIGHT.getScore() : 0),
    // sum values for a large straight, meaning composed of a continuous sequence of at least 4 values, finishing by 6
    LARGE_STRAIGHT(dices -> dices.getValuesAsString("").contains("23456") ? SCORE_LARGE_STRAIGHT.getScore() : 0),

    // sum values for a set of 3 values X and a couple of values Y, where X is different of Y
    FULL_HOUSE(dices -> {
        if (dices.isComposedOfOnlyOneKindOfValue()) {
            return 0;
        }
        return scoreValues(dices, 3, 1)
                + scoreValues(dices, 2, 1);
    });

    private final ToIntFunction<DiceSerie> scoringFunction;

    YatzyCategory(ToIntFunction<DiceSerie> scoringFunction) {
        this.scoringFunction = scoringFunction;
    }

    public int score(DiceSerie diceSerie) {
        return scoringFunction.applyAsInt(diceSerie);
    }

    /**
     * Score a dice serie according:
     * <li>the number of occurrences for a value</li>
     * <li>the number of times this match is repeated</li>
     *
     * @param diceSerie                    a set of dice values
     * @param havingNbOccurrencesForAValue number of occurrences to find any value
     * @param nbTimesIntheSerie            number to match this motif (ex: for a pair, 2 pairs, etc)
     * @return
     */
    private static int scoreValues(DiceSerie diceSerie, final int havingNbOccurrencesForAValue, final int nbTimesIntheSerie) {
        int[] countsByValue = diceSerie.getNbOccurrenciesByValue();

        int score = 0;
        int nbMatchs = 0;
        for (int currentValue = countsByValue.length - 1; currentValue > 0; currentValue--) {
            int nbOccurrencesOfCurrentValue = countsByValue[currentValue];
            if (nbOccurrencesOfCurrentValue >= havingNbOccurrencesForAValue) {
                nbMatchs++;
                score += currentValue * Math.min(nbOccurrencesOfCurrentValue, havingNbOccurrencesForAValue);
                if (nbMatchs >= nbTimesIntheSerie) {
                    break;
                }
            }
        }
        return nbTimesIntheSerie == nbMatchs ? score : 0;
    }

    private static Integer sumByDiceValue(DiceSerie diceSerie, int value) {
        return diceSerie.getValues().stream()
                .filter(v -> v == value)
                .reduce(0, Integer::sum);
    }

    protected enum DefaultScores {
        SCORE_YATZY(50),
        SCORE_SMALL_STRAIGHT(15),
        SCORE_LARGE_STRAIGHT(20);

        private int score;

        DefaultScores(int defaultScore) {
            this.score = defaultScore;
        }

        public int getScore() {
            return score;
        }
    }

}
