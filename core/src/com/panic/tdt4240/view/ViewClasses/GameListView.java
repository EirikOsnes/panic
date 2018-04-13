package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
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
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.states.GameListState;
import com.panic.tdt4240.view.Renderer;
import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 *
 * Should:  1. display a list of lobbies, perhaps nearby games
 *          2. allow direct connection by entering lobby id
 */

public class GameListView extends AbstractView {

    private Renderer renderer;
    private ScrollPane scroller;
    private Stage stage;
    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle btnStyle;
    private Table table;
    private Texture bg;
    private TextButton exitToMainMenuBtn;

    private static String err_full_lobby = "Error: full lobby.";
    private static String err_lobby404 = "Error: lobby not found.";

    public GameListView(final GameListState listState) {
        super(listState);
        renderer = Renderer.getInstance();
        renderer.dispose();

        bg = new Texture("misc/bg.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        table = new Table();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        btnAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button_up");
        btnStyle.down = skin.getDrawable("button_up");

        scroller = new ScrollPane(table);


        String info;

        table.setFillParent(true);
        table.center();

        for (int i = 0; i < listState.getLobbies().size(); i++){
            final Lobby l = listState.getLobbies().get(i);
            info = l.toString();
            TextButton button = new TextButton(info, btnStyle);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    state.handleInput(l.getLobbyID());
                }
            });
            table.add(button); table.row();
        }
        /**/

        table.add(scroller).fill().expand();

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(1);
                // insert whatever should happen
            }
        });

        // TODO: a button for text input and direct connection to a game lobby?

        table.add(exitToMainMenuBtn).row();

        stage.addActor(table);

    }

    // should pop up with the appropriate error message and update lobbylist
    public void popup(String errorMsg){
        Dialog popup = new Dialog("Error", skin, "dialog");
        popup.text(errorMsg);
        popup.button("OK");
        popup.show(stage);
    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        renderer.sb.begin();
        renderer.sb.draw(bg, 0, 0, PanicGame.WIDTH, PanicGame.HEIGHT);
        stage.act();
        stage.draw();
        renderer.sb.end();
    }

    public void dispose(){
        stage.dispose();
        font.dispose();
        renderer.dispose();
    }


}

