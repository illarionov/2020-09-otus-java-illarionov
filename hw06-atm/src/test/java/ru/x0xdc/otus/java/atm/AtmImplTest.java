package ru.x0xdc.otus.java.atm;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Result;
import ru.x0xdc.otus.java.atm.model.Status;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AtmImplTest {

    @Test
    @DisplayName("withdraw() возвращает успешный статус и корректный результат")
    void withdrawShouldWorkCorrectly() {
        Atm atm = new AtmImpl(Change.builder().rub100(10).rub500(10).rub1000(10).build());

        Result<List<Banknote>> result = atm.withdraw(600);

        assertThat(result.getStatus())
                .isEqualTo(Status.SUCCESS);

        assertThat(result.getResult())
                .containsExactlyInAnyOrder(Banknote.of(500), Banknote.of(100));
    }

    @Test
    @DisplayName("withdraw возвращает ошибку, если нет денег")
    void withdrawShouldFailOnNoEnoughMoney() {
        Atm atm = new AtmImpl(Change.builder().rub100(1).build());

        Result<List<Banknote>> result = atm.withdraw(200);

        assertThat(result.getStatus())
                .isEqualTo(Status.FAILED);

        assertThat(result.getError())
                .isInstanceOf(DispenseException.class);
    }

    @Test
    @DisplayName("deposit() возвращает верный результат")
    void depositShouldWork() {
        Atm atm = new AtmImpl();

        Result<Void> result = atm.deposit(List.of(Banknote.of(1000), Banknote.of(5)));

        assertThat(result.getStatus())
                .isEqualTo(Status.SUCCESS);
    }

    @Test
    @DisplayName("баланс у нового ATM нулевой")
    void getAvailableBalanceDefault() {
        Atm atm = new AtmImpl();
        assertThat(atm.getAvailableBalance()).isZero();
    }

    @Test
    @DisplayName("баланс у ATM с заданным начальным значением верен")
    void getAvailableBalanceWithInitialValue() {
        Atm atm = new AtmImpl(Change.builder().rub100(10).rub1000(1).build());
        assertThat(atm.getAvailableBalance()).isEqualTo(2000);
    }

    @Test
    @DisplayName("баланс у ATM после внесения наличных верен")
    void getAvailableBalanceAfterDeposit() {
        Atm atm = new AtmImpl(Change.builder().rub100(10).rub1000(1).build());

        atm.deposit(List.of(Banknote.of(100)));
        assertThat(atm.getAvailableBalance()).isEqualTo(2100);
    }

    @Test
    @DisplayName("баланс у ATM после снятия наличных верен")
    void getAvailableBalanceAfterWithdraw() {
        Atm atm = new AtmImpl(Change.builder().rub100(10).rub1000(1).build());

        atm.withdraw(100);
        assertThat(atm.getAvailableBalance()).isEqualTo(1900);
    }
}
