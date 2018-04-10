package com.panic.tdt4240.states;


import com.panic.tdt4240.view.ViewClasses.GameListView;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    GameListView view;

    public GameListState(GameStateManager gsm){
        super(gsm);
        view = new GameListView(this);
        // load available games from master server
        // ... maybe with ping?

    }

    @Override
    public void handleInput(Object o) {
        // when a lobby is clicked, enter it.
        if ( o.getClass() == String.class){
            try{
                int lobbyID = Integer.parseInt((String) o);
                connectToServer(lobbyID); // some shitty placeholder function
            } catch(Exception e){

            }
        }

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

    }

    // whatever dude I'm not Gandalf
    private void connectToServer(int lobbyID){

    }

}
