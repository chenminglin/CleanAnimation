package com.chenminglin.cleananimation;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        System.out.println(2 * (3 / 5f));

        System.out.println(Math.sqrt(9));

        System.out.println(Math.tan(90));

        System.out.println(Math.pow(3, 3));

        Random random = new Random(System.currentTimeMillis());

        int n = 0;
        while (n < 9999) {
            int i = random.nextInt(10);
            System.out.println(i);
            n++;
        }


    }
}