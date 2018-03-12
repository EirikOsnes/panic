package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.panic.tdt4240.states.GameStateManager;
import com.panic.tdt4240.states.MenuState;
import com.panic.tdt4240.view.Renderer;

/**
 * Created by victor on 05.03.2018.
 */

public class MenuView extends AbstractView {

    private Renderer renderer;
    private Stage stage;
    private Button createGameBtn;
    private Button joinGameBtn;
    private Button settingsBtn;
    TextureAtlas buttonAtlas;
    Skin skin;
    BitmapFont font;
    private TextButton.TextButtonStyle textButtonStyle;

    public MenuView(MenuState menuState) {
        super(menuState);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin();
        buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
        skin.addRegions(buttonAtlas);
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");

        createGameBtn = new TextButton("Create new",textButtonStyle);
        joinGameBtn = new TextButton("Join",textButtonStyle);
        settingsBtn = new TextButton("Settings",textButtonStyle);

        createGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(createGameBtn);
            }
        });


        stage.addActor(createGameBtn);
        stage.addActor(joinGameBtn);
        stage.addActor(settingsBtn);

        renderer = Renderer.getInstance();
    }

    // TODO: legge inn input
    public void render() {
        stage.draw();
    }



}
