package com.panic.tdt4240.connection;

import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;

import java.util.ArrayList;

/**
 * Created by Eirik on 09-Apr-18.
 */

public class Connection {
    private static final Connection ourInstance = new Connection();

    public static Connection getInstance() {
        return ourInstance;
    }

    private Connection() {
    }

    /**
     * A method call to get all the Vehicles for this game. Should be instatiated copies, with ID.
     *
     * @return Returns all the Vehicles for this game.
     */
    public ArrayList<Vehicle> getAllVehicles() {
        ArrayList<Vehicle> result = null;

        return result;
    }

    /**
     * Get the ID of the current players vehicle.
     *
     * @return returns the ID.
     */
    public String getMyVehicle() {
        return null;
    }


    /**
     * Get the map ID for this game
     *
     * @return Returns the map ID.
     */
    public String getMapID() {
        return null;
    }

    /**
     * reads the history of the game. If the game has no history, the method returns null. The history string needs to be formatted as "CARDID&SENDERID&TARGETID&SEED//" where turns get separated
     * with "ENDTURN//".
     *
     * @param history The history String
     * @return An arrayList containing ArrayLists of CardIDs, SenderIDs, TargetIDs and Seeds
     */
    public ArrayList<ArrayList<String>> readHistory(String history) {
        if (history.equals("")) {
            return null;
        }
        ArrayList<String> cardIDs = new ArrayList<>();
        ArrayList<String> senderIDs = new ArrayList<>();
        ArrayList<String> targetIDs = new ArrayList<>();
        ArrayList<String> seed = new ArrayList<>();
        String[] data = history.split("//");
        for (String string : data) {
            if (string.equals("ENDTURN")) {
                cardIDs.add("ENDTURN");
                senderIDs.add("ENDTURN");
                targetIDs.add("ENDTURN");
                seed.add("ENDTURN");
            } else {
                String[] elements = string.split("&");
                if (elements.length != 4) {
                    throw new IllegalArgumentException("String is not formatted correctly");
                }
                cardIDs.add(elements[0]);
                senderIDs.add(elements[1]);
                targetIDs.add(elements[2]);
                seed.add(elements[3]);
            }
        }
        ArrayList<ArrayList<String>> returnArray = new ArrayList<>();
        returnArray.add(cardIDs);
        returnArray.add(senderIDs);
        returnArray.add(targetIDs);
        returnArray.add(seed);
        return returnArray;
    }


    /**
     * Generates a String to be sent to server containing cardID, senderID, targetID and priority. The String will be formatted such that the Server can process it.
     * @param moves An arrayList of the moves to be executed
     * @return The formatted string
     */
    public String createCardString(ArrayList<String[]> moves) {
        String returnString = "";
        ModelHolder mh = ModelHolder.getInstance();
        for (String[] move : moves) {
            Card card = mh.getCardById(move[0]);
            String priority = Integer.toString(card.getPriority());
            returnString = returnString + move[0] + "&" + move[2] + "&" + move[1] + "&" + priority + "//";
        }
        return returnString;
    }
}

