package com.panic.tdt4240.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by magnus on 12.03.2018.
 */

public class MenuState extends State {
    
    private Button createGameBtn;
    private Button joinGameBtn;
    private Button settingsBtn;
    TextureAtlas buttonAtlas;
    Skin skin;
    BitmapFont font;

    public MenuState(GameStateManager gsm){
        super(gsm);
    }

    private void init(){
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        font = new BitmapFont();
        skin = new Skin();
        skin.addRegions(buttonAtlas);
        createGameBtn = new Button();
        joinGameBtn = new Button();
        settingsBtn = new Button();
    }
    @Override
    protected void handleInput() {

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
