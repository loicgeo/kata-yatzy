package loicgeo.katas.yatzy;

import loicgeo.katas.yatzy.exception.FonctionalException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DiceSerieTest {

    @Test
    public void should_create_a_DiceSerie_instance() throws FonctionalException {
        // given
        // when
        DiceSerie diceSerie = new DiceSerie(1, 2, 3, 5, 6);
        
        // then
        assertThat(diceSerie).isNotNull();
        assertThat(diceSerie.getValues()).containsExactlyInAnyOrder(5, 3, 2, 1, 6);
    }

    @Test
    public void should_throw_an_exception_trying_to_create_a_DiceSerie_with_not_allowed_value() {
        // given
        ThrowableAssert.ThrowingCallable diceInstantiation = () -> new DiceSerie(0, 2, 3, 5, 6);

        // when
        // then
        assertThatThrownBy(diceInstantiation).isInstanceOf(FonctionalException.class).hasMessageContaining(
                "Wrong dice values");
    }

}
