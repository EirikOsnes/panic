package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.util.XMLParser;
import com.panic.tdt4240.view.ViewClasses.MenuView;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by magnus on 12.03.2018.
 */

public class MenuState extends State {
    

    MenuView menuView;

    public MenuState(GameStateManager gsm){
        super(gsm);
        menuView = new MenuView(this);

    }

    @Override
    public void handleInput(Object o) {
        if (o == (Integer) 1) {
            gsm.push(new CreateGameState(gsm));
            System.out.println("Creating game...");
        } else if (o == (Integer) 2) {
            gsm.push(new GameListState(gsm));
            System.out.println("Listing lobbies...");
        } else if (o == (Integer) 3) {
            gsm.push(new SettingsState(gsm));
            System.out.println("Settings...");
        }
    //For testing the PlayCardState class
        startPlayCard();
    }

    /** FOR TESTING VIEWS **/
    private void startLobby(GameStateManager gsm){
        gsm.set(new GameLobbyState(gsm, "0"));
        System.out.println("lobby state created");
    }

    private void startResults(GameStateManager gsm){
        ArrayList<Player> dedbodies = new ArrayList<>();
        Player p1 = new Player(new Stack<Card>()); dedbodies.add(p1);
        Player p2 = new Player(new Stack<Card>()); dedbodies.add(p2);
        Player p3 = new Player(new Stack<Card>()); dedbodies.add(p3);
        Player p4 = new Player(new Stack<Card>()); dedbodies.add(p4);
        Player p5 = new Player(new Stack<Card>()); dedbodies.add(p5);
        Player p6 = new Player(new Stack<Card>()); dedbodies.add(p6);
        gsm.set(new GameResultsState(gsm, dedbodies));
        System.out.println("results state created");
    }

    private void startCreateGame(GameStateManager gsm){
        gsm.set(new CreateGameState(gsm));
        System.out.println("'Create Game' state created");
    }

    private void startPlayCard() {
        Stack<Card> cards = new Stack<>();
        for (int i = 0; i < 10; i++) {
            Card card = new Card(i + "");
            card.setTooltip("Card nr:" + i + "\nSomething else............\nabcdefghijklmnopqrstuvwxyz");
            card.setTargetType(Card.TargetType.ASTEROID);
            card.setAllowedTarget(Card.AllowedTarget.ALL);
            cards.push(card);
        }
        Player player = new Player(cards);
        XMLParser parser = new XMLParser();
        Map map = parser.parseMaps().get(0);
        gsm.set(new PlayCardState(gsm, player, map)); 
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        menuView.render();
    }

    @Override
    public void dispose() {

    }
}
