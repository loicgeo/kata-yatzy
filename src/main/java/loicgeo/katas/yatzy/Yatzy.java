package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Yatzy {

    private static final Logger LOGGER = Logger.getLogger(Yatzy.class.getName());

    private DiceSerie diceSerie;

    /**
     * Instantiation a Yatzy dice serie.
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
            LOGGER.log(Level.SEVERE, "Unable to instantiate Yatzy", exception);
            throw exception;
        }
    }

    public int scoreRoll(YatzyCategory category) {
        return category.score(diceSerie);
    }

}



