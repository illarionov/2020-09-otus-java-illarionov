package ru.x0xdc.otus.java.atm;

import ru.x0xdc.otus.java.atm.model.Banknote;
import ru.x0xdc.otus.java.atm.model.Result;

import java.util.List;

public interface Atm {

    /**
     * Выдаать запрошенную сумму
     * @param sum сумма для вывода
     * @return банкноты с требуемой суммой, либо ошибка
     */
    Result<List<Banknote>> withdraw(int sum);

    /**
     * Внести сууму на счет
     * @param banknotes вносимые банкноты
     * @return результат выполнения: ошибка, если не удалось внести
     */
    Result<Void> deposit(List<Banknote> banknotes);

    /**
     * Выдавать сумму остатка денежных средств, которую может выдать банкомат
     */
    int getAvailableBalance();
}
