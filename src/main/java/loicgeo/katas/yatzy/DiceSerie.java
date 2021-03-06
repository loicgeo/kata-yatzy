package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public class DiceSerie {

    private static final int MIN_DICE_VALUE = 1;
    private static final int MAX_DICE_VALUE = 6;

    private static final Predicate<? super Integer> NOT_ALLOWED_VALUE = value -> value < MIN_DICE_VALUE || value > MAX_DICE_VALUE;

    private Collection<Integer> values;

    /**
     * Initialize a dice serie of 5 int values.
     *
     * @param value1 value dice 1
     * @param value2 value dice 2
     * @param value3 value dice 3
     * @param value4 value dice 4
     * @param value5 value dice 5
     * @throws FonctionalException in case of not allowed values.
     */
    public DiceSerie(int value1, int value2, int value3, int value4, int value5) throws FonctionalException {
        values = asList(value1, value2, value3, value4, value5);
        validateValues();
    }

    private void validateValues() throws FonctionalException {
        if (values.stream().anyMatch(NOT_ALLOWED_VALUE)) {
            throw new FonctionalException(
                    format("Wrong dice values: a dice serie must be composed of values between {0} and {1} included.",
                            MIN_DICE_VALUE,
                            MAX_DICE_VALUE
                    ));
        }
    }

    /**
     * Get number of dices of a value, for each possible value.
     * For example:
     * <li>for dices serie `1, 2, 3, 4, 5`, returns array int[]{0, 1, 1, 1, 1, 1, 0}</li>
     * <li>for dices serie `2, 2, 2, 5, 5`, returns array int[]{0, 0, 3, 0, 0, 2, 0}</li>
     *
     * @return an array of length MAX_DICE_VALUE + 1 (0 is ignored), composed of the number of dices having a value, for each value (i.e. the array indexes).
     */
    public int[] getNbOccurrenciesByValue() {
        int[] countsByValue = new int[MAX_DICE_VALUE + 1];

        Map<Integer, Long> countsByValueMap = values.stream().collect(groupingBy(identity(), counting()));
        countsByValueMap.forEach((value, count) -> countsByValue[value] = count.intValue());

        return countsByValue;
    }

    public boolean isComposedOfOnlyOneKindOfValue() {
        return values.stream().distinct().count() == 1;
    }

    public String getValuesAsString(String delimiter) {
        return values.stream()
                .sorted()
                .map(Object::toString)
                .collect(joining(delimiter));
    }

    public Collection<Integer> getValues() {
        return values;
    }
}
