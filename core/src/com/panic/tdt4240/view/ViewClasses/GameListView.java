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
import com.badlogic.gdx.utils.Align;
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

    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private Texture bg;

    private GameListState listState;
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
        font.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);
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
TextButton.TextButtonStyle btnStyle = skin.get(TextButton.TextButtonStyle.class);
        btnStyle.font = font;
        if (! listState.getLobbyListData().isEmpty()){
            for (int i = 0; i < listState.getLobbyListData().size(); i++) {
                final String data[] = listState.getLobbyListData().get(i);
                TextButton button = new TextButton(data[0], btnStyle);
                button.setHeight(button.getHeight()*2.2f);
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.out.println("Lobby clicked; attempting to enter");
                        state.handleInput(data[1]); // lobbyID
                    }
                });
                lobbyBtnTable.add(button).width(GlobalConstants.SCALE_WIDTH*3.0f/2.0f).height(GlobalConstants.SCALE_HEIGHT).pad(GlobalConstants.PADDING);
                lobbyBtnTable.row();
            }

        }
        //lobbyBtnTable.row();

        // scrolls child widgets.
        ScrollPane.ScrollPaneStyle style = skin.get("lobby", ScrollPane.ScrollPaneStyle.class);
        ScrollPane scroller = new ScrollPane(lobbyBtnTable, style);
        stage.addActor(scroller);
        scroller.setScrollingDisabled(true, false);
        scroller.setOverscroll(false, false);
        scroller.setWidth(SCREEN_WIDTH);
        scroller.setHeight(SCREEN_HEIGHT*3/4f);
        scroller.setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/2, Align.center);
    }


    private void generateMenuBtns(){
        TextButton.TextButtonStyle btnStyle = skin.get(TextButton.TextButtonStyle.class);
        btnStyle.font = font;
        TextButton exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.getLabelCell().pad(exitToMainMenuBtn.getLabelCell().getPadTop(), GlobalConstants.PADDING, exitToMainMenuBtn.getLabelCell().getPadBottom(), GlobalConstants.PADDING);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });

        TextButton refreshBtn = new TextButton("Refresh list", btnStyle);
        refreshBtn.getLabelCell().pad(refreshBtn.getLabelCell().getPadTop(), GlobalConstants.PADDING, refreshBtn.getLabelCell().getPadBottom(), GlobalConstants.PADDING);
        refreshBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                listState.updateLobbyList();
            }
        });

        Table exitTable = new Table();
        exitTable.setFillParent(true);
        exitTable.bottom();
        exitTable.add(refreshBtn).height(GlobalConstants.SCALE_HEIGHT).pad(GlobalConstants.PADDING);
        exitTable.add(exitToMainMenuBtn).height(GlobalConstants.SCALE_HEIGHT).pad(GlobalConstants.PADDING);
        exitTable.pack();
        stage.addActor(exitTable);
        //TODO
        lobbyBtnTable.setHeight(lobbyBtnTable.getHeight()*2.0f);

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

