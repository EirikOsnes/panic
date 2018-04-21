package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Skin skin;
    private BitmapFont font;

    private String lobbyName;
    private Table topTable;
    private ArrayList<Label> playerTxtFields;
    private Label.LabelStyle txtStyle;
    private Label.LabelStyle rdyStyle;

    private Button readyBtn;
    private TextButton.TextButtonStyle btnStyle;

    private SelectBox<String> carSelectBox;
    private SelectBox.SelectBoxStyle boxStyle;

    private Actor backgroundActor;
    private GameLobbyState lobbyState;

    private float scl;

    /** Lobby is retrieved after some time. Anything that is dependent on information
     * from the lobby object, must be generated in updateView().
     *
     * The constructor is used to set up styling for text, buttons, etc...
     * If changes are made post-release, they should mostly be here.
     *
     * // TODO: popup when only one player is present, and hits "Ready".
     * */
    public GameLobbyView(final GameLobbyState lobbyState) {
        super(lobbyState);
        lobbyName="";
        scl = 2f;
        this.lobbyState=lobbyState;
        /** INIT SETUP */

        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        font = new BitmapFont();
        font.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);

        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        playerTxtFields = new ArrayList<>();

        prepareStyles();

        generateBottomItems();

        /** INIT SETUP END */
    }

    /** RUNS AFTER LOBBY DATA HAS BEEN UPDATED
     * */
    public void updateView(){
        // actors are to be removed and re-added.
        stage.clear();
        stage.addActor(backgroundActor);

        generateTopItems();

        // Draw these when data is loaded or updated
        if (lobbyState.isDataLoaded()) {
            preparePlayerListDisplay(); // generates playerTxtFields
            prepareSelectVehicleDisplay();
            lobbyName=lobbyState.getLobby().getLobbyname();
        }
        else {
            System.out.println("Object 'lobby' not created yet ---");
        }
        generateBottomItems();
    }

    private void prepareStyles() {
        // create new bitmapfonts in order to create multiple font sizes; do not let changes propagate.
        // skin.getDrawable("textfield") generates the usual label background.
        txtStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        txtStyle.background = skin.getDrawable("textfield");
        System.out.println(GlobalConstants.GET_TEXT_SCALE());

        rdyStyle = new Label.LabelStyle(font, Color.BLACK);
        rdyStyle.background = skin.getDrawable("button-ready");

        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = new BitmapFont();
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");

        backgroundActor = new Image(new TextureRegion(bg));
        backgroundActor.setZIndex(0);
        backgroundActor.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundActor);

        boxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        boxStyle.font = new BitmapFont();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            boxStyle.font.getData().setScale(GlobalConstants.GET_TEXT_SCALE() * scl);
            txtStyle.font.getData().setScale(GlobalConstants.GET_TEXT_SCALE() * scl);
            rdyStyle.font.getData().setScale(GlobalConstants.GET_TEXT_SCALE() * scl);
            btnStyle.font.getData().setScale(GlobalConstants.GET_TEXT_SCALE() * scl);
        }
    }

    private void preparePlayerListDisplay(){
        String name;
        playerTxtFields = new ArrayList<>();
        ArrayList<String> usedNames = new ArrayList<>();

        for (int i = 0; i < lobbyState.getLobby().getPlayerIDs().size(); i++){
            int playerID = lobbyState.getLobby().getPlayerIDs().get(i);
            int seed = playerID * lobbyState.getLobbyID();
            int offset = 0;
            while (true) {
                // modulo is used in the getName().
                name = PlayerNameGenerator.getName(seed+offset);
                offset++;
                if (! usedNames.contains(name)) {
                    usedNames.add(name);
                    if (playerID == Connection.getInstance().getConnectionID()) name=name + " (me)";
                    Label t = new Label(name, skin);
                    // if player is ready/has selected vehicle
                    if (lobbyState.getLobby().getVehicleTypes().get(i) != null){
                        t.setStyle(rdyStyle);
                    }
                    playerTxtFields.add(new Label("   " + name, txtStyle));
                    break;
        }}}

        Table playerTxtTable = new Table();
        playerTxtTable.setFillParent(true);

        // positioning
        playerTxtTable.center();

        // Fill table with actual contents
        for (Label tf : playerTxtFields){
            playerTxtTable.add(tf).padBottom(20).width(SCREEN_WIDTH*3/4).height(SCREEN_HEIGHT*0.08f);
            playerTxtTable.row();
        }
        stage.addActor(playerTxtTable);

    }

    private void prepareSelectVehicleDisplay(){
//        String[] carTypes = {"HURR", "DURR", "HERP", "DERP"}; // USED FOR TESTING
        String[] carTypes = getVehicleNames();
        carSelectBox = new SelectBox<>(skin);

        // scaleby: adds to the current scale. setScale --> defines the number.
        // sizeby: adds to the size. setsize --> define the size (width, height).
        carSelectBox.setStyle(boxStyle);
        carSelectBox.setSize(SCREEN_WIDTH*4/5, SCREEN_HEIGHT/16);
        carSelectBox.setScale(SCREEN_WIDTH*4/5, GlobalConstants.GET_TEXT_SCALE());

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            carSelectBox.getScrollPane().scaleBy(GlobalConstants.GET_TEXT_SCALE() * 2);
        }
//        carSelectBox.getScrollPane().setWidth(SCREEN_WIDTH*4/5);
        carSelectBox.setItems(carTypes);
        carSelectBox.setSelectedIndex(0); // default value
        carSelectBox.setWidth(SCREEN_WIDTH/2);
        carSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            }
        });
        carSelectBox.setPosition(SCREEN_WIDTH/2 - carSelectBox.getWidth()/4, SCREEN_HEIGHT/10 * 2);
        carSelectBox.pack();

        stage.addActor(carSelectBox);

    }

    private void generateBottomItems(){
        // Specify consequences in the bottom of this file
        final LonelyGameDialog dialog = new LonelyGameDialog("", skin, "dialog");
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        dialog.text("Hitting 'Ready' now will start the game. Are you sure?", labelStyle);
        dialog.button("Yes",true, btnStyle);
        dialog.button("Cancel", false, btnStyle);
        dialog.hide();

        readyBtn = new TextButton("Ready up", btnStyle);

        if (lobbyState.isPlayerReady()) {
            readyBtn.setColor(Color.GRAY);
            int asdf = lobbyState.getLobby().getPlayerIDs().indexOf(Connection.getInstance().getConnectionID());
            playerTxtFields.get(asdf).setStyle(rdyStyle);
        }

        readyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (lobbyState.isPlayerReady()) {
                    System.out.println("'Ready' set to " + lobbyState.isPlayerReady());
                }
            // else
                else if (lobbyState.getLobby().getPlayerIDs().size() <= 1) {
                    dialog.show(stage);
                }
                else { // Player is ready
                    generateWaitingText();
                    lobbyState.setPlayerReady(true);
                    readyBtn.setColor(Color.GRAY);
                    lobbyState.handleInput("-2");
                }
            }
        });

        Button exitBtn = new TextButton("Exit lobby", btnStyle);
        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput("-1");
            }
        });
        exitBtn.pack();

        Table bottomTable = new Table();
        bottomTable.center();
        bottomTable.add(readyBtn).width(SCREEN_WIDTH/3).height(SCREEN_HEIGHT*0.08f).pad(20);
        bottomTable.add(exitBtn).width(SCREEN_WIDTH/3).height(SCREEN_HEIGHT*0.08f).pad(20);
        bottomTable.row();
        bottomTable.setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/10);

        stage.addActor(bottomTable);
    }

    private void generateTopItems(){
        topTable = new Table();
        topTable.center().top().row().setActorWidth(SCREEN_WIDTH*2/3);

        Label lobbyField = new Label("Lobby name: ", txtStyle);
        lobbyField.scaleBy(GlobalConstants.GET_TEXT_SCALE());
        lobbyField.pack();

        Label lobbyNameField = new Label(lobbyName, txtStyle);
        lobbyNameField.scaleBy(GlobalConstants.GET_TEXT_SCALE());

        lobbyField.setPosition(SCREEN_WIDTH/2 - lobbyField.getWidth()/2, SCREEN_HEIGHT*0.88f);
        lobbyNameField.setPosition(SCREEN_WIDTH/2 - lobbyNameField.getWidth()/2, SCREEN_HEIGHT*0.8f);

        stage.addActor(lobbyField);
        stage.addActor(lobbyNameField);

        if (lobbyState.isPlayerReady()) generateWaitingText();

//        topTable.add(lobbyField).row();
//        topTable.add(lobbyNameField).row();
//        stage.addActor(topTable);
    }

    private void generateWaitingText() {
        Label.LabelStyle waitStyle = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        waitStyle.background = skin.getDrawable("textfield");
        Label waitingTxt = new Label("Waiting for other players...", waitStyle);
        waitingTxt.getStyle().font.getData().scale(GlobalConstants.GET_TEXT_SCALE());
        waitingTxt.setWidth(SCREEN_WIDTH/2);
//        waitingTxt.setPosition(SCREEN_WIDTH /2 - waitingTxt.getWidth()/2, SCREEN_HEIGHT*7.5f/10f);

        topTable.add(waitingTxt).width(SCREEN_WIDTH*3/4);

// move elsewhere
//        topTable.center().top().padTop(SCREEN_HEIGHT * (0.8f));
//        stage.addActor(topTable);

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

    public void render(){
        stage.act();
        stage.draw();
    }

    public void dispose(){    // should be used by GameStateManager only
        font.dispose();
        stage.dispose();
        bg.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }

    private class LonelyGameDialog extends Dialog {

        private LonelyGameDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }

        @Override
        protected void result(Object object) {
            Boolean bool = (Boolean) object;
            if(bool){
                state.handleInput("-2");
            }
            else{
            }
        }
    }

}