package ru.x0xdc.otus.java.atm.vault;

import ru.x0xdc.otus.java.atm.model.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MoneyVault {

    private final Map<Denomination, Cassette> cassettes;

    public MoneyVault() {
        this(Change.empty());
    }

    public MoneyVault(Change quantities) {
        this.cassettes = new EnumMap<>(Denomination.class);

        for (Denomination denomination: Denomination.values()) {
            int initialValue = quantities.getQuantity(denomination);
            cassettes.put(denomination, new Cassette(denomination, initialValue));
        }
    }

    Result<List<Banknote>> dispenseCommand(Change change) {
        List<Banknote> result = new ArrayList<>();
        for (var denomination: change.getDenominations()) {
            int quantity = change.getQuantity(denomination);
            cassettes.get(denomination).removeNotes(quantity);
            for (int i = 0; i < quantity; ++i) {
                result.add(Banknote.of(denomination));
            }
        }

        return Result.success(result);
    }

    Result<Change> depositCommand(List<Banknote> banknotes) {
        Map<Denomination, Integer> quantities = banknotes
                .stream()
                .collect(Collectors.groupingBy(Banknote::getDenomination, Collectors.summingInt(b -> 1)));

        for (var item: quantities.entrySet()) {
            Denomination denomination = item.getKey();
            int quantity = item.getValue();
            cassettes.get(denomination).addNotes(quantity);
        }

        return Result.success(Change.builder().setQuantities(quantities).build());
    }

    public int getAvailableBalance() {
        return cassettes.values()
                .stream()
                .mapToInt(Cassette::getTotalBalance)
                .sum();
    }

    public Change getLeftover() {
        Map<Denomination, Integer> result = cassettes.values()
                .stream()
                .filter(c -> c.getQuantity() > 0)
                .collect(Collectors.toMap(Cassette::getDenomination, Cassette::getQuantity));
        return Change.builder().setQuantities(result).build();
    }

}
