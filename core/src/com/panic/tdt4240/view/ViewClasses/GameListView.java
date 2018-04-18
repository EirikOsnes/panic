package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
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

    GameListState listState;
    private ScrollPane scroller;
    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle btnStyle;
    private Table lobbyBtnTable, exitTable;
    private Texture bg;
    private TextButton exitToMainMenuBtn;

    public static final String error0 = "Error: full lobby.";
    public static final String error1 = "Error: lobby not found.";

    public GameListView(GameListState listState) {
        super(listState);
        this.listState=listState;

//        System.out.println("CHECKPOINT 1");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);

        bg = new Texture("misc/background.png");
        font = new BitmapFont();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            font.getData().scale(SCREEN_HEIGHT/ SCREEN_WIDTH * 1.5f);
        }
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");

        updateView();

        // TODO: a button for text input and direct connection to a game lobby?

    }

    public void updateView(){
        lobbyBtnTable = new Table();
        lobbyBtnTable.setFillParent(true);
        lobbyBtnTable.background(new TextureRegionDrawable(new TextureRegion(bg)));
        lobbyBtnTable.center();

        for (int i = 0; i < listState.getLobbyListData().size(); i++){
            final String data[] = listState.getLobbyListData().get(i);
            TextButton button = new TextButton(data[0], btnStyle);
            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    state.handleInput(data[1]); // lobbyID
                }
            });
            lobbyBtnTable.add(button).width(300).pad(5); lobbyBtnTable.row();
        }
        // scrolls child widgets.
        scroller = new ScrollPane(lobbyBtnTable);
        ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
        scrollStyle.background = skin.getDrawable("default-rect");
        scrollStyle.vScroll = skin.getDrawable("default-scroll");
        scrollStyle.vScrollKnob = skin.getDrawable("default-round-large");
        scroller.setScrollingDisabled(true, false);
        scroller.setStyle(scrollStyle);
        stage.addActor(scroller);

        createExitToMainMenuBtn();
        exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.bottom();
        exitTable.add(exitToMainMenuBtn).padTop(30).padBottom(30).bottom();
        exitTable.pack();
        lobbyBtnTable.pack();

        stage.addActor(scroller);
        stage.addActor(exitTable);
    }

    private void createExitToMainMenuBtn(){
        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });
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
        bg.dispose();
        font.dispose();
        skin.dispose();
        btnAtlas.dispose();
    }
}

