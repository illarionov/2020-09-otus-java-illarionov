package ru.otus.core.dao;

public class ConstraintViolationDaoException extends UserDaoException {

    private final String constraintName;

    public ConstraintViolationDaoException(String constraintName, Exception ex) {
        super(ex);
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return constraintName;
    }

    public String getContraintColumnName() {
        if ("uk_595avk25fuwifq27av9e3ixqn".equals(constraintName)) {
            return "login";
        } else if ("fkt99kx4fa0okdr2keid6re132i".equals(constraintName)) {
            return "address_id";
        } else if ("user__pkey".equals(constraintName)) {
            return  "id";
        } else {
            return "";
        }
    }

}
