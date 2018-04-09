package com.panic.tdt4240.models;

import com.panic.tdt4240.util.XMLParser;

import java.util.ArrayList;

/**
 * Created by Choffa for panic on 06-Apr-18.
 * On permission can be used outside panic.
 */
public class ModelHolder {

    private static ModelHolder mh;

    private static ArrayList<Card> cards;
    private static ArrayList<Vehicle> vehicles;
    private static ArrayList<Map> maps;

    private ModelHolder(){}

    public Card getCardById(String id) {
        for (int i = 0; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (c.getId().equalsIgnoreCase(id)) {
                return c;
            }
        }
        return null;
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
        if (mh == null) {
            mh = new ModelHolder();
            XMLParser parser = new XMLParser();

            // TODO: Change to actual path
            String base = "android/assets/";
            cards = parser.parseCards(base + "cards/card_test.xml");
            maps = parser.parseMaps(base + "maps/map_test.xml");
            vehicles = parser.parseVehicles(base + "vehicles/vehicle_test.xml");
        }
        return mh;
    }
}
