package ru.x0xdc.otus.java.atm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.x0xdc.otus.java.atm.model.Change;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GreedyDispensingStrategyTest extends DispensingStrategyTestBase {

    @Override
    protected DispensingStrategy createStrategy() {
        return new GreedyDispensingStrategy();
    }

    @ParameterizedTest(name="[{index}] Sum of {0} with leftovers: {1} should dispense with notes: {2}")
    @MethodSource
    void calculateDispenseShouldWorkCorrectly(int sum,  Change leftovers, Change expectedResult) {
        Change result = strategy.calculateDispense(leftovers, sum);
        assertThat(result.getQuantities())
                .containsExactlyInAnyOrderEntriesOf(expectedResult.getQuantities());
    }

    static Stream<Arguments> calculateDispenseShouldWorkCorrectly() {
        return Stream.of(
                arguments(6000,
                        Change.builder().rub100(10).rub2000(10).rub5000(2).build(),
                        Change.builder().rub5000(1).rub100(10).build()) // XXX это не минимальное кол-ве купюр
        );
    }

    @ParameterizedTest(name="[{index}] Should throw exception on sum of {0} with leftovers: {1}")
    @MethodSource
    void shouldThrowExceptionOnNoEnoughMoney(int sum, Change leftovers) {
        assertThatThrownBy(() -> strategy.calculateDispense(leftovers, sum))
                .isInstanceOf(DispenseException.class);
    }

    static Stream<Arguments> shouldThrowExceptionOnNoEnoughMoney() {
        return Stream.of(
                arguments(6000, Change.builder().rub2000(10).rub5000(2).build()) // XXX в этом варианте возможно выдать деньги, но не в данном алгоритме
        );
    }
}
