package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.states.MenuState;
import com.panic.tdt4240.util.GlobalConstants;

/**
 * Created by victor on 05.03.2018.
 */

public class MenuView extends AbstractView {


    private TextButton createGameBtn, joinGameBtn, settingsBtn;
    private Skin skin;
    private BitmapFont font;
    private Table table;
    private Texture background;

    public MenuView(final MenuState menuState) {
        super(menuState);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        background = new Texture("misc/background.png");
        cam.setToOrtho(false,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table = new Table();
        font = new BitmapFont();
        float textScale = GlobalConstants.GET_TEXT_SCALE();
        font.getData().scale(2*textScale);

        TextButton.TextButtonStyle style = skin.get("default", TextButton.TextButtonStyle.class);
        style.font=font;
        createGameBtn = new TextButton("Create New Game", style);
        joinGameBtn = new TextButton("Join Game", style);
        settingsBtn = new TextButton("Settings", style);

        Label.LabelStyle labelStyle = skin.get("default", Label.LabelStyle.class);
        labelStyle.font=font;
        Label title = new Label(PanicGame.TITLE,labelStyle);
        Label fullTitle = new Label(PanicGame.FULL_TITLE,labelStyle);


        table.setFillParent(true);
        table.add(title).top().padBottom(GlobalConstants.PADDING);
        table.row().center();
        table.add(fullTitle).padBottom(GlobalConstants.PADDING);
        table.center();
        table.row();
        table.add(createGameBtn).width(GlobalConstants.SCALE_WIDTH).height(GlobalConstants.SCALE_HEIGHT).pad(GlobalConstants.PADDING);
        table.row();
        table.add(joinGameBtn).width(GlobalConstants.SCALE_WIDTH).height(GlobalConstants.SCALE_HEIGHT).pad(GlobalConstants.PADDING);
        table.row();
//        table.add(settingsBtn).width(GlobalConstants.SCALE_WIDTH).height(GlobalConstants.SCALE_HEIGHT).pad(padding);

        table.background(new TextureRegionDrawable(new TextureRegion(background)));
        table.pack();

        createGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState.handleInput( 1);
            }
        });

        joinGameBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState.handleInput( 2);
            }
        });

        settingsBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                menuState.handleInput( 3);
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
        stage.act();
        stage.draw();
    }


    public void dispose(){
        font.dispose();
        stage.dispose();
        background.dispose();
    }



}
