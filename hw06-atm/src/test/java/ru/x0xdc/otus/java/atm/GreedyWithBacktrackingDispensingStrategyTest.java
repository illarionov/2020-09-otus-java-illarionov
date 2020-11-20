package ru.x0xdc.otus.java.atm;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.x0xdc.otus.java.atm.model.Change;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class GreedyWithBacktrackingDispensingStrategyTest extends DispensingStrategyTestBase {

    @Override
    protected DispensingStrategy createStrategy() {
        return new GreedyWithBacktrackingDispensingStrategy();
    }

    @ParameterizedTest(name = "[{index}] Sum of {0} with leftovers: {1} should dispense with notes: {2}")
    @MethodSource
    void calculateDispenseShouldWorkCorrectly(int sum, Change leftovers, Change expectedResult) {
        Change result = strategy.calculateDispense(leftovers, sum);
        assertThat(result.getQuantities())
                .containsExactlyInAnyOrderEntriesOf(expectedResult.getQuantities());
    }

    static Stream<Arguments> calculateDispenseShouldWorkCorrectly() {
        return Stream.of(
                arguments(6000,
                        Change.builder().rub100(10).rub2000(10).rub5000(2).build(),
                        Change.builder().rub2000(3).build()),
                arguments(6000, Change.builder().rub2000(10).rub5000(2).build(),
                        Change.builder().rub2000(3).build()),
                arguments(6000000, Change.builder().rub2000(10).rub5000(2).rub1000(1001).rub500(100).rub200(801).rub100(100).rub5(5000000).build(),
                        Change.builder().rub5000(2).rub2000(10).rub1000(1001).rub500(100).rub200(801).rub100(100).rub5(949760).build()
                )
                );
    }
}