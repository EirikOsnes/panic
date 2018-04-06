package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.view.ViewClasses.MenuView;

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
        if(o==(Integer) 1){
            gsm.push(new CreateGameState(gsm));
        }
        else if(o==(Integer) 2){
            gsm.push(new GameListState(gsm));
        }
        else if(o==(Integer) 3){
            gsm.push(new SettingsState(gsm));
        }
    }


    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        menuView.render(sb);
    }

    @Override
    public void dispose() {

    }
}