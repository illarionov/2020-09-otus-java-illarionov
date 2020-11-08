package ru.x0xdc.otus.java.aop;

public class Main {

    public static void main(String[] args) {
        TestLogging logging = Ioc.getInstance().createTestLogging();

        logging.calculation();
        logging.calculation(1);
        logging.calculation(2,3);
        logging.calculation(4, 5, "6");
        logging.calculation(new int[]{7});
    }

}

