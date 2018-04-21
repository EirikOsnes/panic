package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.util.XMLParser;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
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
        menuView.isConnecting(true); //Tell the menu view that the connection is loading
        if(Connection.getInstance().getConnectionID()== 0){
            Connection.getInstance().findConnectionID();
        }
        else{
            menuView.isConnecting(false);
        }
    }

    @Override
    public void handleInput(Object o) {

        if (o == (Integer) 1) {
            gsm.push(new CreateGameState(gsm));
            System.out.println("Creating game...");
            //startPlayCard();
        } else if (o == (Integer) 2) {
            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    gsm.push(new GameListState(gsm));
                }
            });
            System.out.println("Listing lobbies...");
        } else if (o == (Integer) 3) {
            gsm.push(new GameResultsState(gsm));
            System.out.println("Settings...");
        }

        // TESTING: forcibly push a state on the gsm stack

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
        menuView.dispose();
    }

    @Override
    public AbstractView getView() {
        return menuView;
    }

    @Override
    protected void setUpAdapter() {
        callbackAdapter = new MenuAdapter();
    }

    private class MenuAdapter implements ICallbackAdapter {

        @Override
        public void onMessage(String message) {
            String[] strings = message.split(":");

            switch (strings[0]){
                case "CONNECTION_ID":
                    if(Connection.getInstance().getConnectionID()==0){
                        Connection.getInstance().setConnectionID(Integer.parseInt(strings[1]));
                        System.out.println("Received connection ID: "+strings[1]);
                        menuView.isConnecting(false);
                    }
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
            }
        }
    }
}
