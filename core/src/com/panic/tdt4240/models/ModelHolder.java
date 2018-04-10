package com.panic.tdt4240.models;

import com.panic.tdt4240.util.XMLParser;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Choffa for panic on 06-Apr-18.
 * On permission can be used outside panic.
 */
public class ModelHolder {

    private static ModelHolder mh;

    private static ArrayList<Card> cards;
    private static HashMap<String, Card> cardMap;
    private static ArrayList<Vehicle> vehicles;
    private static ArrayList<Map> maps;

    private ModelHolder(){}

    public Card getCardById(String id) {
        return cardMap.get(id);
    }

    public ArrayList<Card> getAllCards() {
        return cards;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public Map getMapById(String id){
        for (Map m : maps) {
            if(m.getId().equalsIgnoreCase(id)){
                return m;
            }
        }

        return null;
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
            cardMap = new HashMap<>();

            // TODO: Change to actual path
            String base = "android/assets/";
            cards = parser.parseCards();
            for (Card card : cards) {
                cardMap.put(card.getId(),card);
            }
            maps = parser.parseMaps();
            vehicles = parser.parseVehicles();
        }
        return mh;
    }
}
