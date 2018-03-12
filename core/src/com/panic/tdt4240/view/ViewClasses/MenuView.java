package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.panic.tdt4240.PanicGame;
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
    private TextureAtlas buttonAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle textButtonStyle;
    private Table table;

    public MenuView(MenuState menuState) {
        super(menuState);
        cam.setToOrtho(false,PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        table = new Table();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        buttonAtlas = new TextureAtlas("card_textures/buttons.pack");
        skin.addRegions(buttonAtlas);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("button-up");
        textButtonStyle.down = skin.getDrawable("button-down");

        createGameBtn = new TextButton("Create new game",textButtonStyle);
        joinGameBtn = new TextButton("Join",textButtonStyle);
        settingsBtn = new TextButton("SettingsView",textButtonStyle);

        Label label = new Label(PanicGame.TITLE,skin);

        table.setFillParent(true);
        table.add(label).top().padBottom(60);
        table.center();
        table.row();
        table.add(createGameBtn).width(200).height(50).pad(20);
        table.row();
        table.add(joinGameBtn).width(200).height(50).pad(20);
        table.row();
        table.add(settingsBtn).width(200).height(50).pad(20);

        createGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( 1);
            }
        });

        joinGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(2);
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(3);
            }
        });


        stage.addActor(table);

        renderer = Renderer.getInstance();
    }

    // TODO: legge inn input
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        stage.draw();
    }



}
