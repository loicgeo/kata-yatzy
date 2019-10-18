package bdd;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import loicgeo.katas.yatzy.Yatzy;
import loicgeo.katas.yatzy.YatzyCategory;
import loicgeo.katas.yatzy.exception.FonctionalException;

import static java.lang.Character.getNumericValue;
import static org.assertj.core.api.Assertions.assertThat;

public class YatzySteps {
    private Yatzy yatzy;
    private YatzyCategory yatzyCategory;
    private int score;

    @Given("a dice serie {string}")
    public void aDiceSerie(String diceSerie) throws FonctionalException {
        String serie = diceSerie.replaceAll("[, ]", "");
        yatzy = new Yatzy(getValue(serie, 0), getValue(serie, 1), getValue(serie, 2), getValue(serie, 3), getValue(serie, 4));
    }

    private Integer getValue(String serie, int position) {
        return getNumericValue(serie.charAt(position));
    }

    @Given("a roll type {string}")
    public void aRollType(String rollType) {
        yatzyCategory = YatzyCategory.valueOf(rollType);
    }

    @When("Yatzy is scored")
    public void yatzyIsScored() {
        score = yatzy.scoreRoll(yatzyCategory);
    }

    @Then("the score is {int}")
    public void scoreShouldBe(int expectedScore) {
        assertThat(score).isEqualTo(expectedScore);
    }
}