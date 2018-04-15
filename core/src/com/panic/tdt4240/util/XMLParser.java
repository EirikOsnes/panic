package com.panic.tdt4240.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A class containing methods to parse XML files into java Objects.
 */

public class XMLParser {

    /**
     * Create ALL cards in an XML file, and return these in an ArrayList.
     * @param path The path to the XML file.
     * @return Returns an ArrayList of Cards.
     */
    public ArrayList<Card> parseCards(String path){

        ArrayList<Card> result = new ArrayList<>();

        try {
            FileHandle inputFile = Gdx.files.internal(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(inputFile.read());
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("card");

            for( int i = 0; i < nList.getLength(); i++){
                Node node = nList.item(i);
                System.out.println("\nCurrent Element :" + node.getNodeName());

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    Card myCard = new Card(element.getAttribute("id"));
                    myCard.setName(element.getElementsByTagName("name").item(0).getTextContent());
                    myCard.setPriority(Integer.parseInt(element.getElementsByTagName("priority").item(0).getTextContent()));
                    myCard.setCardType(Card.CardType.valueOf(element.getElementsByTagName("type_id").item(0).getTextContent()));
                    myCard.setTargetType(Card.TargetType.valueOf(element.getElementsByTagName("target_type").item(0).getTextContent()));
                    myCard.setAllowedTarget(Card.AllowedTarget.valueOf(element.getElementsByTagName("allowed_targets").item(0).getTextContent()));
                    myCard.setMaxRange(Integer.parseInt(element.getElementsByTagName("max_range").item(0).getTextContent()));
                    myCard.setMinRange(Integer.parseInt(element.getElementsByTagName("min_range").item(0).getTextContent()));
                    myCard.setTooltip(element.getElementsByTagName("tooltip").item(0).getTextContent());
                    NodeList effects = element.getElementsByTagName("card_effect");
                    for (int j = 0; j < effects.getLength(); j++) {
                        Node effectNode = effects.item(j);
                        if(effectNode.getNodeType() == Node.ELEMENT_NODE){
                            Element effectElement = (Element) effectNode;
                            myCard.addCardEffect(
                                    effectElement.getElementsByTagName("target_status").item(0).getTextContent(),
                                    Float.parseFloat(effectElement.getElementsByTagName("value").item(0).getTextContent()),
                                    Integer.parseInt(effectElement.getElementsByTagName("status_duration").item(0).getTextContent()),
                                    Integer.parseInt(effectElement.getElementsByTagName("splash_range").item(0).getTextContent()),
                                    Boolean.parseBoolean(effectElement.getElementsByTagName("friendly_fire").item(0).getTextContent()),
                                    (effectElement.getElementsByTagName("requirement_name").getLength()>0)
                                            ? effectElement.getElementsByTagName("requirement_name").item(0).getTextContent() : "none",
                                    (effectElement.getElementsByTagName("requirement_value").getLength()>0)
                                            ? Integer.parseInt(effectElement.getElementsByTagName("requirement_value").item(0).getTextContent()) : 0
                            );
                        }
                    }
                    System.out.println("EffectCount : " + element.getElementsByTagName("card_effects").getLength());
                    result.add(myCard);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;

    }

    /**
     * Create ALL cards in an XML file, and return these in an ArrayList.
     * @return Returns an ArrayList of Cards.
     */
    public ArrayList<Card> parseCards(){
        return parseCards("cards/card_test.xml");
    }

    /**
     * Create an array of Maps from an XML file, by passing in the path to this file. The Maps will already be set up with neighbourhood matrix.
     * @param path The path to the XML file to use.
     * @return Returns an ArrayList of instantiated Maps, with neighbourhood matrix finalized.
     */
    public ArrayList<Map> parseMaps(String path){

        ArrayList<Map> result = new ArrayList<>();

        try {
            FileHandle file = Gdx.files.internal(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file.read());
            doc.getDocumentElement().normalize();

            NodeList mapList = doc.getElementsByTagName("map");

            for (int j = 0; j < mapList.getLength(); j++) {

                Node mapNode = mapList.item(j);
                if(mapNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element mapElement = (Element) mapNode;
                    String id = mapElement.getAttribute("id");
                    NodeList asteroidNodeList = mapElement.getElementsByTagName("asteroid");
                    HashMap<String, Asteroid> asteroidHashMap = new HashMap<>();

                    for (int i = 0; i < asteroidNodeList.getLength(); i++) {
                        Node node = asteroidNodeList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            String asteroidId = element.getAttribute("id");
                            String spritename = "meteorBrown_big1";
                            if(element.hasAttribute("sprite")){
                                spritename = element.getAttribute("sprite");
                            }
                            Asteroid myAsteroid = new Asteroid(spritename, asteroidId);
                            myAsteroid.setPosition(new Vector2(
                                    Float.parseFloat(element.getElementsByTagName("posX").item(0).getTextContent()),
                                    Float.parseFloat(element.getElementsByTagName("posY").item(0).getTextContent())
                            ));
                            asteroidHashMap.put(asteroidId, myAsteroid);
                        }
                    }

            NodeList connectionNodeList = mapElement.getElementsByTagName("connection");
            for (int i = 0; i < connectionNodeList.getLength(); i++) {
                Node node = connectionNodeList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            Element element = (Element) node;
                            asteroidHashMap.get(element.getElementsByTagName("vertexID").item(0).getTextContent()).connect(
                                    asteroidHashMap.get(element.getElementsByTagName("vertexID").item(1).getTextContent())
                            );
                        }
                    }

                    Map resultMap = new Map(new ArrayList<>(asteroidHashMap.values()),id);
                    resultMap.generateAdjacencyMatrix();
                    result.add(resultMap);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Create an array of Maps from an XML file, by passing in the path to this file. The Maps will already be set up with neighbourhood matrix.
     * @return Returns an ArrayList of instantiated Maps, with neighbourhood matrix finalized.
     */
    public ArrayList<Map> parseMaps() {
        return parseMaps("maps/maps.xml");
    }


    /**
     * A method to create Vehicle objects from an xml.
     * @param path The path to the file containing the vehicle types.
     * @return Returns an ArrayList of possible vehicles.
     */
    public ArrayList<Vehicle> parseVehicles(String path){
        ArrayList<Vehicle> result = new ArrayList<>();

        try {
            FileHandle inputFile = Gdx.files.internal(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile.read());
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("vehicle");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    Vehicle myVehicle = new Vehicle(element.getAttribute("type"));
                    NodeList statusNodeList = element.getElementsByTagName("status");
                    for (int j = 0; j < statusNodeList.getLength(); j++) {
                        Node statusNode = statusNodeList.item(j);
                        if (statusNode.getNodeType() == Node.ELEMENT_NODE){
                            Element statusElement = (Element) statusNode;
                            myVehicle.getStatusHandler().addStatus(statusElement.getAttribute("name"), Float.parseFloat(statusElement.getAttribute("base")));
                        }
                    }
                    result.add(myVehicle);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * A method to create Vehicle objects from an xml.
     * @return Returns an ArrayList of possible vehicles.
     */
    public ArrayList<Vehicle> parseVehicles(){
        return parseVehicles("vehicles/vehicle_test.xml");
    }

    /**
     * parse the Stack of cards related to the given vehicle type. The stack will be empty if no such deck is defined.
     * @param path The path to the decks
     * @param vehicleType The vehicle type
     * @return Returns a Stack<Card> that is related to the given vehicle (will add all cards from all decks at this point)
     */
    public Stack<Card> parseCardStack(String path, String vehicleType){
        Stack<Card> result = new Stack<>();

        try {
            FileHandle inputFile = Gdx.files.internal(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile.read());
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("deck");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    if(element.getAttribute("vehicle_type").equalsIgnoreCase(vehicleType)) {
                        NodeList cardsNodeList = element.getElementsByTagName("card");
                        for (int j = 0; j < cardsNodeList.getLength(); j++) {
                            Node cardNode = cardsNodeList.item(j);
                            if(cardNode.getNodeType() == Node.ELEMENT_NODE){
                                Element cardElement = (Element) cardNode;
                                result.push(ModelHolder.getInstance().getCardById(cardElement.getTextContent()));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

    /**
     * parse the Stack of cards related to the given vehicle type. The stack will be empty if no such deck is defined.
     * @param vehicleType The vehicle type
     * @return Returns a Stack<Card> that is related to the given vehicle (will add all cards from all decks at this point)
     */
    public Stack<Card> parseCardStack(String vehicleType){
        return parseCardStack("decks/deck_test.xml",vehicleType);
    }

}
