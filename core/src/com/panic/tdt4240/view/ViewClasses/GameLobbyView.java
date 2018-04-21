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
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.connection.Connection;
import com.panic.tdt4240.models.ModelHolder;
import com.panic.tdt4240.models.Vehicle;
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

    private Table playerTxtTable, bottomTable, topTable;
    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Skin skin;
    private BitmapFont font;
    private String lobbyName;

    private ArrayList<String> usedNames;

    private TextField waitingTxt, lobbyNameField;
    private ArrayList<TextField> playerTxtFields;
    private TextField.TextFieldStyle txtStyle;

    private Button exitBtn, readyBtn;
    private TextButton.TextButtonStyle btnStyle;

    private SelectBox<String> carSelectBox;
    private SelectBox.SelectBoxStyle boxStyle;

    private Actor backgroundActor;
    private GameLobbyState lobbyState;

    /** Lobby is retrieved after some time. Anything that is dependent on information
     * from the lobby object, must be generated in updateView().
     *
     * The constructor is used to set up styling for text, buttons, etc...
     * If changes are made post-release, they should mostly be here.
     * */
    public GameLobbyView(final GameLobbyState lobbyState) {
        super(lobbyState);
        lobbyName="";
        this.lobbyState=lobbyState;
        /** INIT SETUP */

        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        font = new BitmapFont();
        font.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);

        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        playerTxtFields = new ArrayList<>();

        txtStyle = new TextField.TextFieldStyle();
        txtStyle.font = font;
        txtStyle.fontColor = Color.WHITE;
        txtStyle.background = skin.getDrawable("textfield");

        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");

        backgroundActor = new Image(new TextureRegion(bg));
        backgroundActor.setZIndex(0);
        backgroundActor.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundActor);

        prepareMenuButtons();

        /** INIT SETUP END */
    }

    /** RUNS AFTER LOBBY DATA HAS BEEN UPDATED
     * */
    public void updateView(){
        // actors are to be removed and re-added.
        stage.clear();
        stage.addActor(backgroundActor);

        // Draw things when data is loaded
        if (lobbyState.isDataLoaded()) {
            preparePlayerListDisplay(); // generates playerTxtFields
            prepareSelectVehicleDisplay();
            lobbyName=lobbyState.getLobby().getLobbyname();
        }
        else {
            System.out.println("Object 'lobby' not created yet ---");
        }
        prepareMenuButtons();
        generateTextItems();

    }


    public void render(){
        stage.act();
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

    private void preparePlayerListDisplay(){
        String name;
        playerTxtFields = new ArrayList<>();
        usedNames = new ArrayList<>();

        for (Integer playerID : lobbyState.getLobby().getPlayerIDs()){
            int seed = playerID * lobbyState.getLobbyID();
            int offset = 0;
            while (true) {
                // modulo is used in the getName().
                name = PlayerNameGenerator.getName(seed+offset);
                offset++;
                if (! usedNames.contains(name)) {
                    usedNames.add(name);
                    if (playerID == Connection.getInstance().getConnectionID()) name = name + " (me)";
                    playerTxtFields.add(new TextField(name, txtStyle));
                    break;
        }}}

        playerTxtTable = new Table();
        playerTxtTable.setFillParent(true);

        // positioning
        playerTxtTable.center();
        // Fill table with actual contents
        for (TextField tf : playerTxtFields){
            playerTxtTable.add(tf).padBottom(20).width(SCREEN_WIDTH*3/4);
            playerTxtTable.row();
        }
        stage.addActor(playerTxtTable);

    }

    private void prepareSelectVehicleDisplay(){
        String[] carTypes = getVehicleNames();
//        String[] carTypes = {"HURR", "DURR", "HERP", "DERP"}; // USED FOR TESTING
        carSelectBox = new SelectBox<>(skin);
        BitmapFont boxFont = new BitmapFont();
        boxFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);

        SelectBox.SelectBoxStyle boxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        boxStyle.font = boxFont;
        boxFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);

        carSelectBox.setStyle(boxStyle);
        carSelectBox.setSize(SCREEN_WIDTH/3, SCREEN_HEIGHT/16);
        carSelectBox.setItems(carTypes);
        carSelectBox.getScrollPane().scaleBy(GlobalConstants.GET_TEXT_SCALE()*2);
        carSelectBox.setSelectedIndex(0); // default value
        carSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        carSelectBox.scaleBy(GlobalConstants.GET_TEXT_SCALE()*2);
        carSelectBox.setPosition(SCREEN_WIDTH/2 - carSelectBox.getWidth()/2, SCREEN_HEIGHT/10 * 2);

        stage.addActor(carSelectBox);

    }

    private void prepareMenuButtons(){
        readyBtn = new TextButton("Ready up", btnStyle);
        if (lobbyState.isPlayerReady()) readyBtn.setColor(Color.GRAY);
        readyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            if (lobbyState.isPlayerReady()) {
                System.out.println("'Ready' set to " + lobbyState.isPlayerReady());
                return;
            }
            // else
            generateWaitingText();
            lobbyState.setPlayerReady(true);
            readyBtn.setColor(Color.GRAY);
            lobbyState.handleInput("-2");
//                Connection.getInstance().chooseVehicleType(carSelectBox.getSelected(), lobbyState.getLobbyID());
            }
        });

        exitBtn = new TextButton("Exit lobby", btnStyle);
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });
        bottomTable = new Table();
        bottomTable.center();
        bottomTable.add(readyBtn).pad(20);
        bottomTable.add(exitBtn).pad(20);
        bottomTable.row();
        bottomTable.setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/10);

        stage.addActor(bottomTable);
    }

    private void generateTextItems(){
        lobbyNameField = new TextField("Lobby name: " + lobbyName, txtStyle);
        lobbyNameField.setPosition(SCREEN_WIDTH/2- lobbyNameField.getWidth()/2,
                SCREEN_HEIGHT * 9/10);
        lobbyNameField.setScale(GlobalConstants.GET_TEXT_SCALE());
        lobbyNameField.setWidth(300);
        lobbyNameField.setPosition(SCREEN_WIDTH/2 - lobbyNameField.getWidth()/2,
                SCREEN_HEIGHT*9/10);
        stage.addActor(lobbyNameField);
        if (lobbyState.isPlayerReady()) generateWaitingText();

//        topTable = new Table().center().top();
//        topTable.add(lobbyNameField).width(SCREEN_WIDTH);
//        topTable.row();
//        stage.addActor(topTable);

    }

    private void generateWaitingText() {
        waitingTxt =  new TextField("Waiting for other players...", txtStyle);
        waitingTxt.getStyle().font.getData().scale(GlobalConstants.GET_TEXT_SCALE() * 3f);
        waitingTxt.setWidth(180);
        waitingTxt.setPosition(SCREEN_WIDTH /2 - waitingTxt.getWidth()/2, SCREEN_HEIGHT*7.5f/10);
        stage.addActor(waitingTxt);
//        topTable.add(waitingTxt).width(SCREEN_WIDTH);
    }

    private String[] getVehicleNames(){
        ArrayList<Vehicle> v = ModelHolder.getInstance().getAllVehicles();
        String[] s = new String[v.size()];
        for (int i = 0; i < v.size(); i++) {
            s[i] = v.get(i).getVehicleType();
        }
        return s;
    }

    public String getSelectedVehicle(){
        return carSelectBox.getSelected();
    }


}