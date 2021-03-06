package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.connection.ICallbackAdapter;
import com.panic.tdt4240.view.ViewClasses.AbstractView;
import com.panic.tdt4240.view.ViewClasses.MenuView;

/**
 * Created by magnus on 12.03.2018.
 */

public class MenuState extends State {

    public MenuState(GameStateManager gsm){
        super(gsm);
        view = new MenuView(this);
        ((MenuView) view).isConnecting(true); //Tell the menu view that the connection is loading
        if(Connection.getInstance().getConnectionID()== 0){
            Connection.getInstance().findConnectionID();
        }
        else{
            ((MenuView) view).isConnecting(false);
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
        view.render();
    }

    @Override
    public void dispose() {
        view.dispose();
    }

    @Override
    public AbstractView getView() {
        return view;
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
                        ((MenuView) view).isConnecting(false);
                    }
                    break;
                case "RECONNECT_GAME":
                    //TODO: Create a pop up, where you can choose to rejoin a game in progress.
                    break;
            }
        }
    }
}
