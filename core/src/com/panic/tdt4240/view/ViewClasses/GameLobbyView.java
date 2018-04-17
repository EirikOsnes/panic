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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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

    private ArrayList<String> usedNames;

    private ArrayList<TextField> playerTxtFields;
    private TextField.TextFieldStyle txtStyle;

    private Button exitBtn, readyBtn;
    private TextButton.TextButtonStyle exitBtnStyle;
    private ArrayList<Player> players;

    private SelectBox<String> carSelectBox;
    private SelectBox.SelectBoxStyle boxStyle;

    private GameLobbyState lobbyState;

    /** Lobby is retrieved after some time. Anything that is dependent on information
     * from the lobby object, must be generated in updateView().
     *
     * The constructor is used to set up styling for text, buttons, etc...
     * If changes are made post-release, they should mostly be here.
     * */
    public GameLobbyView(final GameLobbyState lobbyState) {
        super(lobbyState);
        this.lobbyState=lobbyState;
        // INIT SETUP
        //
        usedNames = new ArrayList<>();
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        table = new Table();

        font = new BitmapFont();
        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            font.getData().scale(SCREEN_HEIGHT/ SCREEN_WIDTH * 1.5f);
        }
        //

        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        //

        playerTxtFields = new ArrayList<>();

        txtStyle = new TextField.TextFieldStyle();
        txtStyle.font = font;
        txtStyle.background = skin.getDrawable("textfield");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        /** testing tools: initialised localUser, players */

        // TODO: make this fit the state-code.

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        table.setFillParent(true);
        table.row().center();
        table.add(new Label(PanicGame.TITLE, skin)).top().padBottom(10).row();

        stage.addActor(table);
    }

    public void updateView(){
        stage.dispose();
        table = new Table();
        stage = new Stage();
        // FIXME: FETCH DATA PROPERLY. Wherever this is supposed to come from...
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

        exitBtn = new TextButton("Exit lobby", exitBtnStyle);

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( "-1");
            }
        });

        preparePlayerList(); // generates playerTxtFields

        for (TextField tf : playerTxtFields){
            table.add(tf).width(Gdx.graphics.getWidth()/2).height(Gdx.graphics.getHeight()/15).pad(Gdx.graphics.getHeight()/40);
            table.row();
        }
        table.add(exitBtn);
        table.pack();

    }

    public void render(){
        stage.draw();
    }

    // TODO: adapt for actually connecting to the server...?
    /* CAN NOT RUN IN THE CONSTRUCTOR/**/
    public void dispose(){
        font.dispose();
        stage.dispose();
        bg.dispose();
        font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }

    private void preparePlayerList(){
        String name;
        playerTxtFields = new ArrayList<>();
        for (Integer playerID : lobbyState.getLobby().getPlayerIDs()){
            double seed = Math.floor(Math.random() * PlayerNameGenerator.getCount());
            name = PlayerNameGenerator.getName((int) seed);
            if (! usedNames.contains(name)) {
                // This should work so that each player can see (me) next to only
                // one name at a time.
                usedNames.add(name);
                if (playerID == Connection.getInstance().getConnectionID()){
                    name = name + " (me)";
                }
                TextField playerField = new TextField(name, txtStyle);
                playerTxtFields.add(playerField);
            }
        }
    }

}
