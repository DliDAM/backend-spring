package com.dlidam.authentication.application.util;

import java.util.Random;

public class RandomCustomIdGenerator {

    private static final int ID_LENGTH = 10;

    private static final Random random = new Random();

    private RandomCustomIdGenerator(){}

    public static String generate() {
        StringBuilder id = new StringBuilder();

        for (int i = 0; i < ID_LENGTH; i++){
            int digit = random.nextInt(10);
            id.append(digit);
        }

        return id.toString();
    }
}
