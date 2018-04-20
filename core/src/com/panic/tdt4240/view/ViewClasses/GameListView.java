package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.states.GameListState;
import com.panic.tdt4240.util.GlobalConstants;

/**
 * Created by victor on 12.03.2018.
 *
 * Should:  1. display a list of lobbies, perhaps nearby games
 *          2. allow direct connection by entering lobby id
 */

public class GameListView extends AbstractView {

    private GameListState listState;
    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private Texture bg;
    private TextButton exitToMainMenuBtn, refreshBtn;
    private Table lobbyBtnTable;

    public static final String error0 = "Error: full lobby.";
    public static final String error1 = "Error: lobby not found.";

    public GameListView(GameListState listState) {
        super(listState);
        this.listState=listState;

//        System.out.println("CHECKPOINT 1");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);

        bg = new Texture("misc/background.png");
        font = new BitmapFont();
        float textScale = GlobalConstants.GET_TEXT_SCALE();
        font.getData().scale(textScale);
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);

        updateView();

        // TODO: a button for text input and direct connection to a game lobby?

    }

    public void updateView(){
        stage.clear();

        // background
        Actor backgroundActor = new Image(new TextureRegion(bg));
        backgroundActor.setZIndex(0);
        backgroundActor.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundActor);

        createLobbyList();
        generateMenuBtns();

    }

    private void createLobbyList(){
        lobbyBtnTable = new Table(skin);
        lobbyBtnTable.center();

        if (! listState.getLobbyListData().isEmpty()){
            for (int i = 0; i < listState.getLobbyListData().size(); i++) {
                final String data[] = listState.getLobbyListData().get(i);
                TextButton button = new TextButton(data[0], skin);
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.out.println("Lobby clicked; attempting to enter");
                        state.handleInput(data[1]); // lobbyID
                    }
                });
                lobbyBtnTable.add(button).width(300).pad(5);
                lobbyBtnTable.row();
            }
        }

        lobbyBtnTable.row();

        // scrolls child widgets.
        ScrollPane.ScrollPaneStyle style = skin.get("lobby", ScrollPane.ScrollPaneStyle.class);
        ScrollPane scroller = new ScrollPane(lobbyBtnTable, style);
        scroller.setScrollingDisabled(true, false);
        scroller.setOverscroll(false, false);
        scroller.setWidth(SCREEN_WIDTH*0.7f);
        scroller.setHeight(SCREEN_HEIGHT*3/4f);
        scroller.setPosition(SCREEN_HEIGHT/10f, SCREEN_WIDTH/10f*3f);
        stage.addActor(scroller);
    }


    private void generateMenuBtns(){
        exitToMainMenuBtn = new TextButton("Exit to main menu", skin);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });

        refreshBtn = new TextButton(    "Refresh list", skin);
        refreshBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listState.updateLobbyList();
            }
        });

        Table exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.bottom();
        exitTable.add(refreshBtn).pad(10);
        exitTable.add(exitToMainMenuBtn).pad(10);
        exitTable.pack();
        stage.addActor(exitTable);


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

