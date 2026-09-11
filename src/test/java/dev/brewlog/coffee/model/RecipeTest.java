package dev.brewlog.coffee.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    @ParameterizedTest
    @CsvSource({
            "18, 270, 15.00" ,
            "15, 240, 16.00" ,
            "18, 280, 15.56" ,
            "19, 270, 14.21"
    })
    void dividesWaterByCoffeeRoundedToTwoDecimals(String coffee, String water, String expected) {
        Recipe recipe = new Recipe();
        recipe.setCoffeeDoseGrams(new BigDecimal(coffee));
        recipe.setWaterDoseGrams(new BigDecimal(water));

        assertEquals(new BigDecimal(expected), recipe.getRatio());
    }

    @Test
    void ratioIsZeroWhenCoffeeIsNull() {
        Recipe recipe = new Recipe();
        recipe.setCoffeeDoseGrams(null);
        recipe.setWaterDoseGrams(new BigDecimal("270"));

        assertEquals(BigDecimal.ZERO, recipe.getRatio());
    }

    @Test
    void ratioIsZeroWhenCoffeeIsZero() {
        Recipe recipe = new Recipe();
        recipe.setCoffeeDoseGrams(new BigDecimal("0"));
        recipe.setWaterDoseGrams(new BigDecimal("270"));

        assertEquals(BigDecimal.ZERO, recipe.getRatio());
    }

    @Test
    void ratioIsZeroWhenCoffeeIsZeroWithScale() {
        Recipe recipe = new Recipe();
        recipe.setCoffeeDoseGrams(new BigDecimal("0.00"));
        recipe.setWaterDoseGrams(new BigDecimal("270"));

        assertEquals(BigDecimal.ZERO, recipe.getRatio());
    }

    @Test
    void ratioThrowsWhenWaterIsNull() {
        Recipe recipe = new Recipe();
        recipe.setCoffeeDoseGrams(new BigDecimal("18"));
        recipe.setWaterDoseGrams(null);

        assertThrows(NullPointerException.class, recipe::getRatio);
    }

}