package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.states.GameLobbyState;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.util.PlayerNameGenerator;
import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 *
 * SEQUENCE OF EVENTS MUST BE:
 *      1. CREATE STATE, VIEW, BUT LET VIEW DO NOTHING
 *      2. ENSURE ADAPTER IS CONNECTED;  onMessage() MUST RUN
 *      3. UPDATE DATA FOUND IN STATE
 *      4. UPDATE VIEW WITH DATA
 *      */

public class GameLobbyView extends AbstractView {

    private Table playerTxtTable, bottomTable;
    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Skin skin;
    private BitmapFont font;

    private ArrayList<String> usedNames;

    private ArrayList<TextField> playerTxtFields;
    private TextField.TextFieldStyle txtStyle;

    private Button exitBtn, readyBtn;
    private TextButton.TextButtonStyle exitBtnStyle;

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
        /** INIT SETUP */

        usedNames = new ArrayList<>();
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        font = new BitmapFont();

        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        playerTxtFields = new ArrayList<>();

        txtStyle = new TextField.TextFieldStyle();
        txtStyle.font = font;
        txtStyle.fontColor = Color.WHITE;
        txtStyle.background = skin.getDrawable("textfield");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        Actor backgroundActor = new Image(new TextureRegion(bg));
        backgroundActor.setZIndex(0);
        backgroundActor.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundActor);

        // both sequences reach this point
        System.out.println("Thread check 5: " + Thread.currentThread().toString());

        createAndSetMenuBtns();

        /** INIT SETUP END */
    }

    /** SHOULD RUN AFTER LOBBY DATA HAS BEEN UPDATED
     *  CURRENTLY DOES NOT RUN AT ALL
     * */
    public void updateView(){

        stage.clear(); // needs to run addActor(...) again

        // FIXME: FETCH carTypes PROPERLY. Wherever this is supposed to come from...

        // DO NOT CREATE PLAYER LIST UNTIL LOBBY DATA EXISTS
        if (lobbyState.getDataLoaded()) {
            System.out.println(" Object 'lobby' should exist now");
            createAndSetPlayerList(); // generates playerTxtFields
        }
        else {
            System.out.println("Object 'lobby' not created yet ---");
        }

        createAndSetMenuBtns();

        // playerTxtTable should already be in the stage; adding to
    }


    public void render(){
        stage.draw();
    }

    /* CAN NOT RUN IN THE CONSTRUCTOR/**/
    public void dispose(){
        font.dispose();
        stage.dispose();
        bg.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }

    // TODO: should server distribute generated names? If so, trigger updateLobby on player join

    private void createAndSetPlayerList(){

        // RUNS ONLY WHEN lobbyState IS LOADED

        String name;
        playerTxtFields = new ArrayList<>();
        for (Integer playerID : lobbyState.getLobby().getPlayerIDs()){
            int seed = playerID * lobbyState.getLobbyID();
            // modulo is used in the getName().
            name = PlayerNameGenerator.getName(seed);
            int offset = 0;
            while (usedNames.contains(name)){
                name = PlayerNameGenerator.getName(seed+offset);
                offset++;
                if (! usedNames.contains(name)) {
                    usedNames.add(name);
                    if (playerID == Connection.getInstance().getConnectionID()){
                        name = name + " (me)";
                        // TODO: ADD CAR SELECTION BOX
                    }
                    TextField playerField = new TextField(name, txtStyle);
                    playerTxtFields.add(playerField);
                }
            }
        }

        playerTxtTable = new Table();
        playerTxtTable.setFillParent(true);
        playerTxtTable.row().center();
        playerTxtTable.add(new Label(PanicGame.TITLE, skin)).top().pad(10).row();

        String[] carTypes = {"Eddison"};
        carSelectBox = new SelectBox<>(skin);
        BitmapFont boxFont = new BitmapFont();
        boxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        boxStyle.font = boxFont;
        boxFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);
        boxStyle.font = boxFont;
        carSelectBox.setName("Select vehicle");
        carSelectBox.setItems(carTypes);
        carSelectBox.setSelectedIndex(0);
        carSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Connection.getInstance().chooseVehicleType(carSelectBox.getSelected(), lobbyState.getLobbyID());
            }
        });
        carSelectBox.scaleBy(1.3f);

        // Fill with actual contents
        for (TextField tf : playerTxtFields){
            playerTxtTable.add(tf).pad(15);
//            playerTxtTable.add(carSelectBox);
            playerTxtTable.row();
        }

        stage.addActor(playerTxtTable);


    }

    private void createAndSetMenuBtns(){
        readyBtn = new TextButton("Ready up", exitBtnStyle);
        readyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lobbyState.setReady();
            }
        });

        exitBtn = new TextButton("Exit lobby", exitBtnStyle);

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });
        bottomTable = new Table();
        bottomTable.center().bottom();
        bottomTable.add(readyBtn).pad(20);
        bottomTable.add(exitBtn).pad(20);
        bottomTable.row();
        stage.addActor(bottomTable);
    }

}