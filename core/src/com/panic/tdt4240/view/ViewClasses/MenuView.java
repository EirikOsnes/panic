package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
    private Button createGameBtn, joinGameBtn, settingsBtn;
    private TextureAtlas buttonAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle ButtonStyle;
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
        buttonAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"),buttonAtlas);

        ButtonStyle = new TextButton.TextButtonStyle();

        ButtonStyle.font = font;
        ButtonStyle.up = skin.getDrawable("button_up");
        ButtonStyle.down = skin.getDrawable("button_down");

        createGameBtn = new TextButton("Create New Game", ButtonStyle);
        joinGameBtn = new TextButton("Join Game", ButtonStyle);
        settingsBtn = new TextButton("Settings", ButtonStyle);

        Label title = new Label(PanicGame.TITLE,skin);
        Label fullTitle = new Label(PanicGame.FULL_TITLE,skin);

        table.setFillParent(true);
        table.add(title).top().padBottom(10);
        table.row().center(); table.add(fullTitle);
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

    /**
     * A method to be called from MenuState to tell the view that it's waiting for a connection.
     * @param isConnecting
     */
    public void isConnecting(boolean isConnecting){
        if(isConnecting){
            //TODO: Set a "Connecting to server" message or spinner or something
        }else{
            //TODO: Unset the message.
        }
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
