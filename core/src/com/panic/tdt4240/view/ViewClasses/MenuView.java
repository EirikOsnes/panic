package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Application;
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
import com.panic.tdt4240.util.GlobalConstants;

/**
 * Created by victor on 05.03.2018.
 */

public class MenuView extends AbstractView {


    private Button createGameBtn, joinGameBtn, settingsBtn;
    private TextureAtlas buttonAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle ButtonStyle;
    private Table table;
    private Texture background;

    public MenuView(final MenuState menuState) {
        super(menuState);
        background = new Texture("misc/background.png");
        cam.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table = new Table();
        font = new BitmapFont();
        float textScale = GlobalConstants.GET_TEXT_SCALE();

        font.getData().scale(textScale);
        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"),buttonAtlas);

        ButtonStyle = new TextButton.TextButtonStyle();

        ButtonStyle.font = font;
        ButtonStyle.up = skin.getDrawable("button-up");
        ButtonStyle.down = skin.getDrawable("button-down");

        createGameBtn = new TextButton("Create New Game", ButtonStyle);
        joinGameBtn = new TextButton("Join Game", ButtonStyle);
        settingsBtn = new TextButton("Settings", ButtonStyle);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        Label title = new Label(PanicGame.TITLE,style);
        Label fullTitle = new Label(PanicGame.FULL_TITLE,style);

        float width = Gdx.graphics.getHeight()/2;
        float height = Gdx.graphics.getHeight()/15;
        float padding = Gdx.graphics.getHeight()/40;

        table.setFillParent(true);
        table.add(title).top().padBottom(padding/2);
        table.row().center(); table.add(fullTitle);
        table.center();
        table.row();
        table.add(createGameBtn).width(width).height(height).pad(padding);
        table.row();
        table.add(joinGameBtn).width(width).height(height).pad(padding);
        table.row();
        table.add(settingsBtn).width(width).height(height).pad(padding);
        table.background(new TextureRegionDrawable(new TextureRegion(background)));
        table.pack();

        createGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState.handleInput( 1);
            }
        });

        joinGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState.handleInput(2);
            }
        });

        settingsBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuState.handleInput(3);
            }
        });
        stage.addActor(table);

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
        stage.draw();
    }


    public void dispose(){
        font.dispose();
        stage.dispose();
    }



}
