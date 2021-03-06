package com.panic.tdt4240.util;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Tests for the StatusHandler class
 */
public class StatusHandlerTest {

    private StatusHandler statusHandler;

    @Before
    public void init(){
        statusHandler = new StatusHandler(null);
        statusHandler.addStatus("health", 100);
        statusHandler.addStatus("damage_modifier", 1);
        statusHandler.addStatus("invulnerable", 0);
    }


    @Test
    public void setupBaseStatuses() throws Exception {
        statusHandler = new StatusHandler(null);
        HashMap<String, Float> checkMap = new HashMap<>();
        checkMap.put("health", StatusConstants.StatusValues.valueOf("health").getBaseValue());
        checkMap.put("damage_modifier", StatusConstants.StatusValues.valueOf("damage_modifier").getBaseValue());
        checkMap.put("defence_modifier", StatusConstants.StatusValues.valueOf("defence_modifier").getBaseValue());
        checkMap.put("movement_modifier", StatusConstants.StatusValues.valueOf("movement_modifier").getBaseValue());
        checkMap.put("max_damage", StatusConstants.StatusValues.valueOf("max_damage").getBaseValue());

        assertEquals(checkMap, statusHandler.getAllResultants());
    }

    @Test
    public void addStatusWithoutBaseValue() throws Exception {
        statusHandler.addStatus("poison");
        assertTrue(statusHandler.getStatusResultant("poison") == StatusConstants.StatusValues.valueOf("poison").getBaseValue());
    }

    @Test (expected = IllegalArgumentException.class)
    public void addStatusWithoutBaseValueError() throws Exception{
        statusHandler.addStatus("test_status");
    }

    @Test
    public void parseEffects() throws Exception {
        statusHandler.addStatusAddition("poison", 5, 1);
        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        statusHandler.parseEffects("poison");
        assertEquals(95, statusHandler.getStatusResultant("health"),0.01);

        statusHandler.addStatusAddition("invulnerable",1,1);
        assertEquals(1000, statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.parseEffects("invulnerable");
        assertEquals(0,statusHandler.getStatusResultant("max_damage"), 0.01);
    }

    @Test
    public void runEffects() throws Exception {

        statusHandler = new StatusHandler(null);

        statusHandler.addStatusAddition("poison", 5, 3);
        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.CARD_PLAYED);
        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        assertEquals(95,statusHandler.getStatusResultant("health"),0.01);
/*
        //Check the playedThisTurn flag
        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        assertEquals(95,statusHandler.getStatusResultant("health"),0.01);
*/
       // statusHandler.nextTurn();

        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        assertEquals(90,statusHandler.getStatusResultant("health"),0.01);

       // statusHandler.nextTurn();

        statusHandler.runEffects(StatusHandler.TimingType.TURN_START);
        assertEquals(90,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(1000,statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.addStatusAddition("invulnerable", 1, 2);
        assertEquals(1000,statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.CARD_PLAYED);
        assertEquals(0,statusHandler.getStatusResultant("max_damage"),0.01);

        //Check poison when invulnerable - no damage should be dealt.
        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        //assertEquals(0,statusHandler.getStatusResultant("max_damage"),0.01);
        //assertEquals(90,statusHandler.getStatusResultant("health"),0.01);

        assertEquals(1000,statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.TURN_START);
        assertEquals(0,statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        assertEquals(1000,statusHandler.getStatusResultant("max_damage"),0.01);
        statusHandler.runEffects(StatusHandler.TimingType.TURN_START);
        assertEquals(1000,statusHandler.getStatusResultant("max_damage"),0.01);
    }

    @Test
    public void getStatusResultant() throws Exception {
        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(1,statusHandler.getStatusResultant("damage_modifier"),0.01);
        assertEquals(0,statusHandler.getStatusResultant("invulnerable"),0.01);

        statusHandler.addStatusMultiplier("health", (float) -0.3, -1);
        assertEquals(70, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("health", 100, 1);
        assertEquals(140, statusHandler.getStatusResultant("health"),0.01);

        assertEquals(0,statusHandler.getStatusResultant("not_a_status"),0.01);
    }

    @Test
    public void getDamageModifier() throws Exception {
        assertEquals(1,statusHandler.getDamageModifier(),0.01);
        statusHandler.addStatusAddition("damage_modifier", -0.3f, 1);
        assertEquals(0.7,statusHandler.getDamageModifier(),0.01);
    }

    @Test
    public void getMovementModifier() throws Exception {
        assertEquals(1,statusHandler.getMovementModifier());
        statusHandler.addStatusAddition("movement_modifier", 0.5f, 1);
        assertEquals(2,statusHandler.getMovementModifier());
        statusHandler.runEffects(StatusHandler.TimingType.TURN_END);
        assertEquals(1,statusHandler.getMovementModifier());
    }

    @Test
    public void getBaseStats() throws Exception {
        HashMap<String, Float> checkMap = new HashMap<>();
        checkMap.put("health", StatusConstants.StatusValues.valueOf("health").getBaseValue());
        checkMap.put("damage_modifier", StatusConstants.StatusValues.valueOf("damage_modifier").getBaseValue());
        checkMap.put("defence_modifier", StatusConstants.StatusValues.valueOf("defence_modifier").getBaseValue());
        checkMap.put("movement_modifier", StatusConstants.StatusValues.valueOf("movement_modifier").getBaseValue());
        checkMap.put("max_damage", StatusConstants.StatusValues.valueOf("max_damage").getBaseValue());

        assertEquals(checkMap,statusHandler.getBaseStats());

        statusHandler.addStatus("test_status", 100);

        assertEquals(checkMap,statusHandler.getBaseStats());

        statusHandler.addStatusAddition("health", -50, 1);

        checkMap.put("health", 50f);

        assertEquals(checkMap,statusHandler.getBaseStats());

    }

    @Test
    public void addStatus() throws Exception {

        int currentSize = statusHandler.getAllResultants().size();

        statusHandler.addStatus("test_status", 1);
        statusHandler.addStatus("test_status2", 100);

        HashMap<String, Float> resultants = statusHandler.getAllResultants();

        assertEquals(currentSize+2,resultants.size());
        assertTrue(resultants.containsKey("test_status"));
        assertTrue(resultants.containsKey("test_status2"));
        assertTrue(resultants.get("test_status")==1);
        assertTrue(resultants.get("test_status2")==100);
    }

    @Test
    public void addStatusMultiplier() throws Exception {

        statusHandler.addStatusMultiplier("health", (float) -0.3, -1);
        assertEquals(70, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusMultiplier("health", (float) 0.1, 10);
        assertEquals(80, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusMultiplier("invulnerable", (float) 0.1, 10);
        assertEquals(0, statusHandler.getStatusResultant("invulnerable"), 0.01);
    }

    @Test
    public void addStatusAddition() throws Exception {

        statusHandler.addStatusAddition("health", (float) -50, -1);
        assertEquals(50, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("health", (float) 30, 10);
        assertEquals(80, statusHandler.getStatusResultant("health"), 0.01);

        statusHandler.addStatusAddition("invulnerable", (float) 1, 10);
        assertEquals(1, statusHandler.getStatusResultant("invulnerable"), 0.01);

    }

    @Test
    public void getAllResultants() throws Exception {

        assertTrue(statusHandler.getAllResultants().containsKey("health"));
        assertTrue(statusHandler.getAllResultants().containsKey("damage_modifier"));
        assertFalse(statusHandler.getAllResultants().containsKey("invulnerable"));
        assertEquals(100f,statusHandler.getAllResultants().get("health"),0.01);
        assertEquals(1f,statusHandler.getAllResultants().get("damage_modifier"),0.01);
        assertNull(statusHandler.getAllResultants().get("invulnerable"));
    }

    @Test
    public void clearStatus() throws Exception{
        assertEquals(0,statusHandler.getStatusResultant("poison"), 0.01);
        assertEquals(0,statusHandler.getStatusResultant("invulnerable"), 0.01);

        statusHandler.addStatusAddition("poison", 5, 2);
        statusHandler.addStatusAddition("poison", 5, -1);
        statusHandler.addStatusMultiplier("poison", -0.25f, 2);
        statusHandler.addStatusMultiplier("poison", -0.25f, -1);

        statusHandler.addStatusAddition("invulnerable", 5, 2);
        statusHandler.addStatusAddition("invulnerable", 5, -1);
        statusHandler.addStatusMultiplier("invulnerable", -0.25f, 2);
        statusHandler.addStatusMultiplier("invulnerable", -0.25f, -1);

        assertEquals(5, statusHandler.getStatusResultant("poison"), 0.01);
        assertEquals(5, statusHandler.getStatusResultant("invulnerable"), 0.01);

        statusHandler.addStatusAddition("antidote", 1, 1);
        statusHandler.runEffects(StatusHandler.TimingType.CARD_PLAYED);

        assertEquals(0, statusHandler.getStatusResultant("poison"), 0.01);
        assertEquals(5, statusHandler.getStatusResultant("invulnerable"), 0.01);

    }

    @Test
    public void nextTurn() throws Exception {

        statusHandler.addStatusMultiplier("health", (float) 0.5, 1);
        statusHandler.addStatusAddition("damage_modifier", (float) 1,2);
        statusHandler.addStatusAddition("damage_modifier", (float) 1, 1);
        statusHandler.addStatusAddition("damage_modifier", (float) 1, 0);

        assertEquals(150,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(4, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(3, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(2, statusHandler.getStatusResultant("damage_modifier"),0.01);

        statusHandler.nextTurn();

        assertEquals(100,statusHandler.getStatusResultant("health"),0.01);
        assertEquals(2, statusHandler.getStatusResultant("damage_modifier"),0.01);

    }

}