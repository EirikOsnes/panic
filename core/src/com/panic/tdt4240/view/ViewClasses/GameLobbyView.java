package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.Lobby;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.states.GameLobbyState;
import com.panic.tdt4240.util.PlayerNameGenerator;
import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyView extends AbstractView {

    private Table table;
    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Skin skin;
    private BitmapFont font;

    private Lobby lobby;

    private ArrayList<String> usedNames;
    private ArrayList<TextButton> textBtns;
    private Button playerBtn, exitBtn, launchGameBtn, readyBtn;
    private TextButton.TextButtonStyle playerBtnStyle, exitBtnStyle;
    private SelectBox<String> carSelectBox;
    private SelectBox.SelectBoxStyle boxStyle;

    private ArrayList<Player> players;

    public GameLobbyView(final GameLobbyState lobbyState) {
        super(lobbyState);
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        table = new Table();
        // Data container
        lobby = lobbyState.getLobby();

        font = new BitmapFont();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            font.getData().scale(SCREEN_HEIGHT/ SCREEN_WIDTH * 1.5f);
        }
        //

        textBtns = new ArrayList<>();
        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        // FIXME
        String[] carTypes = {"Eddison"};

        carSelectBox = new SelectBox<>(skin);
        carSelectBox.setName("Select vehicle");
        carSelectBox.setItems(carTypes);
        carSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // FIXME

            }
        });
        carSelectBox.scaleBy(1.3f);
        carSelectBox.pack();
        table.add(carSelectBox);

        table.add(carSelectBox);

        playerBtnStyle = new TextButton.TextButtonStyle();
        playerBtnStyle.font = font;
        playerBtnStyle.up = skin.getDrawable("button-up");
        playerBtnStyle.down = skin.getDrawable("button-up");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        playerBtn = new TextButton("", playerBtnStyle);
        launchGameBtn = new TextButton("Launch game", exitBtnStyle);
        exitBtn = new TextButton("Exit lobby", exitBtnStyle);

        playerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lobbyState.handleInput( "0");
            }
        });
        launchGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lobbyState.handleInput( "1");
            }
        });
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lobbyState.handleInput( "-1");
            }
        });

        /** testing tools: initialised localUser, players */

        // TODO: make this fit the state-code

        usedNames = new ArrayList<>();
        players = new ArrayList<>();

        preparePlayerList();

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        table.setFillParent(true);
        table.row().center();
        table.add(new Label(PanicGame.TITLE, skin)).top().padBottom(10).row();
        for (TextButton tb : textBtns){
            table.add(tb).width(200).height(50).pad(10);
            table.row();
        }
        table.add(exitBtn);
        table.pack();
        stage.addActor(table);
    }

    public void updateView(){

    }

    public void playerJoined(Integer id){
        // add more buttons for each player who joins
        lobby.getPlayerIDs().add(id);
        updateView();
    }


    public void render(){
        stage.draw();
    }

    // TODO: adapt for actually connecting to the server...?
    private void preparePlayerList(){
        String name;
        for (Integer playerID : lobby.getPlayerIDs()){
            double seed = Math.floor(Math.random() * PlayerNameGenerator.getCount());
            name = PlayerNameGenerator.getName((int) seed);
            if (playerID == Connection.getInstance().getConnectionID()){
                name = name + " (me)";
            }
            if (! usedNames.contains(name)) {
                TextButton button = new TextButton(
                        name, playerBtnStyle);
                textBtns.add(button);
                usedNames.add(name);
            }
        }

    }

    public void dispose(){
        font.dispose();
        stage.dispose();
    }
}
