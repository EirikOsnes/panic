package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.view.ViewClasses.GameListView;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameListState extends State {

    GameListView view;

    public GameListState(GameStateManager gsm){
        super(gsm);
        view = new GameListView(this);
    }

    @Override
    public void handleInput(Object o) {

    }


    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
