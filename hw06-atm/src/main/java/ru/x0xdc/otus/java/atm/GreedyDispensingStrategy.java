package ru.x0xdc.otus.java.atm;

import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Denomination;

import java.util.*;

public class GreedyDispensingStrategy implements DispensingStrategy {

    private static final Denomination[] denominationDescending;

    static {
        denominationDescending = Denomination.values();
        Arrays.sort(denominationDescending, Comparator.comparingInt(Denomination::getValue).reversed());
    }

    @Override
    public Change calculateDispense(Change leftovers, int sum) throws DispenseException  {
        if (sum == 0) {
            return Change.empty();
        }

        Change.ChangeBuilder result = Change.builder();

        for (Denomination denomination: denominationDescending) {
            int max = leftovers.getQuantity(denomination);
            int required = sum / denomination.getValue();
            int toDispense = Math.min(required, max);
            if (toDispense > 0) {
                result.setQuantity(denomination, toDispense);
                sum -= toDispense * denomination.getValue();
                if (sum == 0) {
                    break;
                }
            }
        }

        if (sum > 0) {
            throw new DispenseException("cannot dispense requested amount of " + sum);
        }

        return result.build();
    }
}
