package com.panic.tdt4240.util;

import com.badlogic.gdx.math.Vector2;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Vehicle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
            File inputFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
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
                                    Integer.parseInt(effectElement.getElementsByTagName("status_duration").item(0).getTextContent()),
                                    Integer.parseInt(effectElement.getElementsByTagName("splash_range").item(0).getTextContent()),
                                    Boolean.parseBoolean(effectElement.getElementsByTagName("friendly_fire").item(0).getTextContent())
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
     * Create a Map from an XML file, by passing in the path to this file. The Map will already be set up with neighbourhood matrix.
     * @param path The path to the XML file to use.
     * @return Returns an instatiated Map, with neighbourhood matrix finalized.
     */
    public Map parseMap(String path){

        try {
            File inputFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList asteroidNodeList = doc.getElementsByTagName("asteroid");
            HashMap<String,Asteroid> asteroidHashMap = new HashMap<>();

            for (int i = 0; i < asteroidNodeList.getLength(); i++) {
                Node node = asteroidNodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    Asteroid myAsteroid = new Asteroid(null);
                    //TODO: Sprite should be separated
                    String id = element.getAttribute("id");
                    myAsteroid.setId(id);
                    myAsteroid.setPosition(new Vector2(
                            Float.parseFloat(element.getElementsByTagName("posX").item(0).getTextContent()),
                            Float.parseFloat(element.getElementsByTagName("posY").item(0).getTextContent())
                    ));
                    asteroidHashMap.put(id, myAsteroid);
                }
            }

            NodeList connectionNodeList = doc.getElementsByTagName("connection");
            for (int i = 0; i < connectionNodeList.getLength(); i++) {
                Node node = connectionNodeList.item(i);

                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element element = (Element) node;
                    asteroidHashMap.get(element.getElementsByTagName("vertexID").item(0).getTextContent()).connect(
                            asteroidHashMap.get(element.getElementsByTagName("vertexID").item(1).getTextContent())
                    );
                }
            }

            Map result = new Map(new ArrayList<>(asteroidHashMap.values()));
            result.generateAdjacencyMatrix();
            return result;

        } catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }


    /**
     * A method to create Vehicle objects from an xml.
     * @param path The path to the file containing the vehicle types.
     * @return Returns an ArrayList of possible vehicles.
     */
    public ArrayList<Vehicle> parseVehicles(String path){
        ArrayList<Vehicle> result = new ArrayList<>();

        try {
            File inputFile = new File(path);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("vehicle");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Vehicle myVehicle = new Vehicle();
                    Element element = (Element) node;
                    NodeList statusNodeList = element.getElementsByTagName("status");
                    for (int j = 0; j < statusNodeList.getLength(); j++) {
                        Node statusNode = statusNodeList.item(j);
                        if (statusNode.getNodeType() == Node.ELEMENT_NODE){
                            Element statusElement = (Element) statusNode;
                            myVehicle.getStatusHandler().addStatus(statusElement.getAttribute("name"), Float.parseFloat(statusElement.getAttribute("base")));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return result;
    }

}
