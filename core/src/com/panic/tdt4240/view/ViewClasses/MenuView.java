package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private TextButton.TextButtonStyle createButtonStyle;
    private TextButton.TextButtonStyle joinButtonStyle;
    private TextButton.TextButtonStyle settingsButtonStyle;
    private Table table;
    private Texture background;

    public MenuView(MenuState menuState) {
        super(menuState);
        background = new Texture("misc/background.png");
        cam.setToOrtho(false,PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        table = new Table();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        buttonAtlas = new TextureAtlas("start_menu_buttons/buttons.pack");
        skin.addRegions(buttonAtlas);

        createButtonStyle = new TextButton.TextButtonStyle();
        joinButtonStyle = new TextButton.TextButtonStyle();

        settingsButtonStyle = new TextButton.TextButtonStyle();
        createButtonStyle.font = font;
        createButtonStyle.up = skin.getDrawable("button-up");
        createButtonStyle.down = skin.getDrawable("button-down");

        joinButtonStyle.font = font;
        joinButtonStyle.up = skin.getDrawable("button-up");
        joinButtonStyle.down = skin.getDrawable("button-down");

        settingsButtonStyle.font = font;
        settingsButtonStyle.up = skin.getDrawable("button-up");
        settingsButtonStyle.down = skin.getDrawable("button-down");

        createGameBtn = new TextButton("Create New Game", createButtonStyle);
        joinGameBtn = new TextButton("Join Game", joinButtonStyle);
        settingsBtn = new TextButton("Settings", settingsButtonStyle);

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
        table.background(new TextureRegionDrawable(new TextureRegion(background)));
        table.pack();

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
    public void render() {
        renderer.sb.setProjectionMatrix(cam.combined);
        renderer.sb.begin();
        renderer.sb.draw(background,0,0,PanicGame.WIDTH,PanicGame.HEIGHT);
        stage.draw();
        renderer.sb.end();
    }

    public void dispose(){
        renderer.dispose();
        font.dispose();
    }



}
