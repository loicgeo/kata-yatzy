package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import static java.text.MessageFormat.format;
import static java.util.Arrays.asList;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

public class DiceSerie {

    private static final int MIN_DICE_VALUE = 1;
    private static final int MAX_DICE_VALUE = 6;
    private static final Predicate<? super Integer> NOT_ALLOWED_VALUE = value -> value < MIN_DICE_VALUE || value > MAX_DICE_VALUE;

    private Collection<Integer> values;

    /**
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

    public int[] getOccurrenciesByDiceValue() {
        int[] countsByValue = new int[MAX_DICE_VALUE + 1];

        Map<Integer, Long> countsByValueMap = values.stream().collect(groupingBy(identity(), counting()));
        countsByValueMap.forEach((value, count) -> countsByValue[value] = count.intValue());

        return countsByValue;
    }

    public Collection<Integer> getValues() {
        return values;
    }
}
