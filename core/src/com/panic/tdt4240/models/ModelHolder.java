package com.panic.tdt4240.models;

import com.panic.tdt4240.util.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Choffa for panic on 06-Apr-18.
 * On permission can be used outside panic.
 * This class loads and holds all the models defined in xml
 * and should be initialised on startup
 */
public class ModelHolder {

    private static ModelHolder mh = new ModelHolder();

    private static ArrayList<Card> cards;
    private static HashMap<String, Card> cardMap;
    private static ArrayList<Vehicle> vehicles;
    private static ArrayList<Map> maps;

    private ModelHolder() {
        XMLParser parser = new XMLParser();

        // TODO: Change to actual path
        String base = "android/assets/";
        cards = parser.parseCards(base + "cards/card_test.xml");
        map = parser.parseMap(base + "maps/map_test.xml");
        vehicles = parser.parseVehicles(base + "vehicles/vehicle_test.xml");
    }

    public Card getCardById(String id) {
        return cardMap.get(id);
    }

    public ArrayList<Card> getAllCards() {
        return cards;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public Vehicle getVehicleByName(String name) {
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            if (v.getVehicleType().equalsIgnoreCase(name)) {
                return v;
            }
        }
        return null;
    }

    public ArrayList<Vehicle> getAllVehicles() {
        return vehicles;
    }

    public static ModelHolder getInstance() {
        return mh;
    }
}
