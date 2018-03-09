package com.panic.tdt4240.util;

import com.panic.tdt4240.models.Card;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Eirik on 05-Mar-18.
 */

public class XMLParser {

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
}
