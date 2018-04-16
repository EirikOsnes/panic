package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.states.GameListState;

/**
 * Created by victor on 12.03.2018.
 *
 * Should:  1. display a list of lobbies, perhaps nearby games
 *          2. allow direct connection by entering lobby id
 */

public class GameListView extends AbstractView {

    private ScrollPane scroller;
    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle btnStyle;
    private Table table;
    private Texture bg;
    private TextButton exitToMainMenuBtn;

    public static final String error0 = "Error: full lobby.";
    public static final String error1 = "Error: lobby not found.";

    public GameListView(final GameListState listState) {
        super(listState);
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        table = new Table();
        font = new BitmapFont();
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-up");

        table.setFillParent(true);
        table.center();

        for (int i = 0; i < listState.getLobbies().size(); i++){
            final String data[] = listState.getLobbies().get(i);
            TextButton button = new TextButton(data[0], btnStyle);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    listState.handleInput(data[1]); // lobbyID
                }
            });
            table.add(button).width(300); table.row();
        }
        /**/

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listState.handleInput(-1);
            }
        });

        // TODO: a button for text input and direct connection to a game lobby?

        table.add(exitToMainMenuBtn).row();

        scroller = new ScrollPane(table);
        scroller.setScrollingDisabled(true, false);
        table.pack();

//        table.add(scroller).fill().expand();
//        stage.addActor(scroller);
        stage.addActor(table);

    }

    // should pop up with the appropriate error message and update lobbylist
    public void popup(String errorMsg){
        Dialog popup = new Dialog("Error", skin, "dialog");
        popup.text(errorMsg);
        popup.button("OK");
        popup.pack();
        popup.show(stage);
    }

    public void render(){
        stage.act(); // this allows the scroller to have any effect, supposedly.
        stage.draw();
    }


    public void dispose(){
        stage.dispose();
        font.dispose();
    }
}

