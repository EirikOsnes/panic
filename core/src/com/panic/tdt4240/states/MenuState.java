package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.MenuView;

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
        } else if (o == (Integer) 2) {
            gsm.push(new GameListState(gsm));
        } else if (o == (Integer) 3) {
            gsm.push(new SettingsState(gsm));
        }

    //For testing the PlayCardState class
        startPlayCard();
    }
    private void startPlayCard() {
        Stack<Card> cards = new Stack<>();
        for (int i = 0; i < 10; i++) {
            Card card = new Card(i + "");
            card.setTooltip("Card nr:" + i + "\nSomething else............\nabcdefghijklmnopqrstuvwxyz");
            cards.push(card);
        }
        Player player = new Player(cards);
        gsm.set(new PlayCardState(gsm, player));
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
