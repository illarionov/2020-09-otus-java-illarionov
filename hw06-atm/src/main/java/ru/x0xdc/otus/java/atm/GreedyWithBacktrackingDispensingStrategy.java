package ru.x0xdc.otus.java.atm;

import ru.x0xdc.otus.java.atm.model.Change;
import ru.x0xdc.otus.java.atm.model.Denomination;

import java.util.Arrays;
import java.util.Comparator;

public class GreedyWithBacktrackingDispensingStrategy implements DispensingStrategy {

    private static final Change INFINITY = Change.builder().rub5000(Integer.MAX_VALUE).build();

    private static final Denomination[] denominationDescending;

    static {
        denominationDescending = Denomination.values();
        Arrays.sort(denominationDescending, Comparator.comparingInt(Denomination::getValue).reversed());
    }

    @Override
    public Change calculateDispense(Change leftovers, int sum) throws DispenseException {
        if (sum == 0) return Change.empty();

        Change result = calculateDispenseRecursive(leftovers, sum);
        if (result == INFINITY) throw new DispenseException("cannot dispense requested amount of " + sum);

        return result;
    }


    private Change calculateDispenseRecursive(Change leftovers, int sum) {
        if (sum == 0) {
            return Change.empty();
        }

        Change change = INFINITY;

        for (Denomination denomination: denominationDescending) {
            int maxNotes = leftovers.getQuantity(denomination);
            if (maxNotes == 0 || denomination.getValue() > sum) {
                continue;
            }

            int numNotes = Math.min(maxNotes, sum / denomination.getValue());
            //int numNotes = 1;

            int newSum = sum - numNotes * denomination.getValue();
            Change res;
            if (newSum > 0) {
                Change leftoversNew = leftovers.toBuilder().add(denomination, -numNotes).build();
                res = calculateDispenseRecursive(leftoversNew, sum - numNotes * denomination.getValue());
            } else {
                res = Change.empty();
            }

            if (res != INFINITY) {
                int numCoinsNew = res.getNumNotes() + numNotes;
                if (change == INFINITY || numCoinsNew < change.getNumNotes()) {
                    change = res.toBuilder().add(denomination, numNotes).build();
                }
            }
        }
        return change;
    }
}
