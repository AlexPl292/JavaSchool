package com.tsystems.javaschool.util;

import java.util.Random;

/**
 * Created by alex on 20.08.16.
 * <p>
 * Password generator
 */
public class PassGen {

    private static final char[] symbols;

    static {
        StringBuilder tmp = new StringBuilder();
        for (char ch = '0'; ch <= '9'; ++ch)
            tmp.append(ch);
        for (char ch = 'a'; ch <= 'z'; ++ch)
            tmp.append(ch);
        symbols = tmp.toString().toCharArray();
    }

    private final Random random = new Random();

    private final char[] buf;

    /**
     * Init password generator
     *
     * @param length length of passwords
     */
    public PassGen(int length) {
        if (length < 1)
            throw new IllegalArgumentException("length < 1: " + length);
        buf = new char[length];
    }

    /**
     * Generate new password
     *
     * @return password
     */
    public String nextPassword() {
        for (int idx = 0; idx < buf.length; ++idx)
            buf[idx] = symbols[random.nextInt(symbols.length)];
        return new String(buf);
    }
}
