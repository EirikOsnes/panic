package com.panic.tdt4240.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.view.ViewClasses.PlayCardView;

/**
 * Created by Hermann on 12.03.2018.
 */

public class CardPlayState extends State {

    private PlayCardView playView;
    public Player player;
    public Map map;

    protected CardPlayState(GameStateManager gsm) {
        super(gsm);
        playView = new PlayCardView(this);
        //TODO Add player instance, + map
    }

    @Override
    public void handleInput(Object o) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {


    }

    @Override
    public void dispose() {
        playView.dispose();
    }
}
