package com.panic.tdt4240.connection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by magnus on 09.04.2018.
 */

@RunWith(JUnit4.class)
public class testConnection {
    Connection connection;

    @Before
    public void setUp(){
        connection = Connection.getInstance();
    }

    @Test
    public void shouldReturnNullArray(){
        ArrayList<ArrayList<String[]>> testArray = connection.readTurns("");
        assertNull(testArray);
    }

    @Test
    public void shouldReturnHistoryArray(){
        String testString = "c1&s1&t1&11111//c2&s2&t2&22222//ENDTURN//c3&s3&t3&33333//c4&s4&t4&44444//ENDTURN";
        ArrayList<ArrayList<String[]>> testArray = connection.readTurns(testString);
        assertEquals("c1", testArray.get(0).get(0)[0]);
        assertEquals("s2", testArray.get(0).get(1)[2]);
        assertEquals("t3", testArray.get(1).get(0)[1]);
        assertEquals("44444", testArray.get(1).get(1)[3]);
    }
/*
    @Test
    public void shouldCreateCardString(){
        String[] m1 = new String[3];
        m1[0] = "MOVE";
        m1[1] = "t1";
        m1[2] = "s1";

        String[] m2 = new String[3];
        m2[0] = "PEW";
        m2[1] = "t2";
        m2[2] = "s2";

        String[] m3 = new String[3];
        m3[0] = "POISON";
        m3[1] = "t3";
        m3[2] = "s3";

        ArrayList<String[]> testArray = new ArrayList<>();
        testArray.add(m1);
        testArray.add(m2);
        testArray.add(m3);

        String result = connection.createCardString(testArray);
        assertEquals("MOVE&s1&t1&5//PEW&s2&t2&3//POISON&s3&t3&1//", result);
    }
 */
}
