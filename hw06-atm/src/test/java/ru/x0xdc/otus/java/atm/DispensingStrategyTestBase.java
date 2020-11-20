package ru.x0xdc.otus.java.atm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.x0xdc.otus.java.atm.model.Change;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public abstract class DispensingStrategyTestBase {

    DispensingStrategy strategy;

    protected abstract DispensingStrategy createStrategy();

    @BeforeEach
    protected void before() {
        strategy = createStrategy();
    }

    @ParameterizedTest(name = "[{index}] Sum of {0} with leftovers: {1} should dispense with notes: {2}")
    @MethodSource
    void calculateDispenseShouldWorkCorrectlyBase(int sum, Change leftovers, Change expectedResult) {
        Change result = strategy.calculateDispense(leftovers, sum);
        assertThat(result.getQuantities())
                .containsExactlyInAnyOrderEntriesOf(expectedResult.getQuantities());
    }

    static Stream<Arguments> calculateDispenseShouldWorkCorrectlyBase() {
        return Stream.of(
                arguments(0, Change.empty(), Change.empty()),
                arguments(0, Change.builder().rub100(1).build(), Change.empty()),
                arguments(100, Change.builder().rub100(1).build(), Change.builder().rub100(1).build()),
                arguments(1000,
                        Change.builder().rub50(10).rub200(10).rub500(10).rub1000(1000).rub2000(10).rub5000(10).build(),
                        Change.builder().rub1000(1).build()),
                arguments(3100,
                        Change.builder().rub50(10).rub100(10).rub500(10).rub1000(1).rub2000(10).rub5000(10).build(),
                        Change.builder().rub2000(1).rub1000(1).rub100(1).build()),
                arguments(3100,
                        Change.builder().rub50(10).rub100(10).rub500(10).rub1000(1).rub5000(10).build(),
                        Change.builder().rub1000(1).rub500(4).rub100(1).build()),
                arguments(6000000, Change.builder().rub2000(10).rub5000(2).rub1000(1001).rub500(100).rub200(801).rub100(100).rub5(5000000).build(),
                        Change.builder().rub5000(2).rub2000(10).rub1000(1001).rub500(100).rub200(801).rub100(100).rub5(949760).build()
                )
        );
    }

    @ParameterizedTest(name = "[{index}] Should throw exception on sum of {0} with leftovers: {1}")
    @MethodSource
    void shouldThrowExceptionOnNoEnoughMoneyBase(int sum, Change leftovers) {

        assertThatThrownBy(() -> {
            strategy.calculateDispense(leftovers, sum);
        }).isInstanceOf(DispenseException.class);
    }

    static Stream<Arguments> shouldThrowExceptionOnNoEnoughMoneyBase() {
        return Stream.of(
                arguments(5, Change.empty()),
                arguments(50, Change.builder().rub100(1).build()),
                arguments(3200, Change.builder().rub500(3).rub1000(2).rub5000(1).build())
        );
    }

}
