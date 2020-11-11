package ru.x0xdc.otus.java.atm.model;

import java.util.*;

import static ru.x0xdc.otus.java.atm.model.Denomination.*;

public final class Change {

    private final Map<Denomination, Integer> quantities;

    private static final Change EMPTY = new Change(Collections.emptyMap());

    public static Change empty() {
        return EMPTY;
    }

    public static ChangeBuilder builder() {
        return new ChangeBuilder();
    }

    private Change(Map<Denomination, Integer> src) {
        quantities = Collections.unmodifiableMap(src);
    }

    public int getQuantity(Denomination denomination) {
        return quantities.getOrDefault(denomination, 0);
    }

    public Set<Denomination> getDenominations() {
        return quantities.keySet();
    }

    public Map<Denomination, Integer> getQuantities() {
        return quantities;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Change change = (Change) o;
        return quantities.equals(change.quantities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantities);
    }

    @Override
    public String toString() {
        return quantities.toString();
    }


    public static final class ChangeBuilder {
        private Map<Denomination, Integer> quantities;

        private ChangeBuilder() {
            quantities = new EnumMap<>(Denomination.class);
        }

        public ChangeBuilder rub5(int quantity) {
            return setQuantity(RUB5, quantity);
        }

        public ChangeBuilder rub10(int quantity) {
            return setQuantity(RUB10, quantity);
        }

        public ChangeBuilder rub50(int quantity) {
            return setQuantity(RUB50, quantity);
        }

        public ChangeBuilder rub100(int quantity) {
            return setQuantity(RUB100, quantity);
        }

        public ChangeBuilder rub200(int quantity) {
            return setQuantity(RUB200, quantity);
        }

        public ChangeBuilder rub500(int quantity) {
            return setQuantity(RUB500, quantity);
        }

        public ChangeBuilder rub1000(int quantity) {
            return setQuantity(RUB1000, quantity);
        }

        public ChangeBuilder rub2000(int quantity) {
            return setQuantity(RUB2000, quantity);
        }

        public ChangeBuilder rub5000(int quantity) {
            return setQuantity(RUB5000, quantity);
        }

        public ChangeBuilder setQuantity(Denomination denomination, int quantity) {
            if (quantity < 0) throw new IllegalArgumentException("Quantity must be positive");
            if (quantity != 0) {
                quantities.put(denomination, quantity);
            } else {
                quantities.remove(denomination);
            }
            return this;
        }

        public ChangeBuilder setQuantities(Map<Denomination, Integer> quantities) {
            this.quantities.clear();
            for (var entry: quantities.entrySet()) {
                if (entry.getValue() != null && entry.getValue() > 0) {
                    this.quantities.put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        public Change build() {
            return new Change(quantities);
        }
    }
}
