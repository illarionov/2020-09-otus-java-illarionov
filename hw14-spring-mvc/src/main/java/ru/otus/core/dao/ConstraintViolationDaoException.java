package ru.otus.core.dao;

public class ConstraintViolationDaoException extends UserDaoException {

    public static ConstraintViolationDaoException create(String constraintName, Exception ex) {
        switch (constraintName) {
            case "uk_595avk25fuwifq27av9e3ixqn":
                return new LoginAlreadyExistsException(ex);
            case "fkt99kx4fa0okdr2keid6re132i":
            case "user__pkey":
            default:
                return new ConstraintViolationDaoException(constraintName, ex);
        }
    }

    private final String constraintName;

    ConstraintViolationDaoException(String constraintName, Exception ex) {
        super(ex);
        this.constraintName = constraintName;
    }

    public String getConstraintName() {
        return constraintName;
    }
}
