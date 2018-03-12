package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by magnus on 12.03.2018.
 */

public class MenuState extends State {
    


    public MenuState(GameStateManager gsm){
        super(gsm);
    }

    @Override
    public void handleInput(Object o) {
        if(o==1){
            gsm.push(new CreateGameState(gsm));
        }
        else if(o==2){
            gsm.push(new GameListState(gsm));
        }
        else if(o==3){
            gsm.push(new SettingsState(gsm));
        }
    }


    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {

    }

    @Override
    public void dispose() {

    }
}
