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

    private Table table, bottomTable;
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
    private boolean lobbiesFetched;

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

        lobbiesFetched=false;
        usedNames = new ArrayList<>();
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        table = new Table();
        bottomTable = new Table();
        font = new BitmapFont();

        buttonAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), buttonAtlas);

        playerTxtFields = new ArrayList<>();

        txtStyle = new TextField.TextFieldStyle();
        txtStyle.font = font;
        txtStyle.background = skin.getDrawable("textfield");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));
        table.setFillParent(true);
        table.row().center();
        table.add(new Label(PanicGame.TITLE, skin)).top().pad(10).row();

        // both sequences reach this point
        System.out.println("Thread check 5: " + Thread.currentThread().toString());

        createAndSetMenuBtns();

        /** INIT SETUP END */

        stage.addActor(table);
    }

    /** SHOULD RUN AFTER LOBBY DATA HAS BEEN UPDATED
     *  CURRENTLY DOES NOT RUN AT ALL
     * */
    public void updateView(){

        // FIXME: FETCH carTypes PROPERLY. Wherever this is supposed to come from...

        String[] carTypes = {"Eddison"};
        carSelectBox = new SelectBox<>(skin);
        BitmapFont boxFont = new BitmapFont();
        SelectBox.SelectBoxStyle boxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        boxStyle.font = boxFont;
        boxFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);
        boxStyle.font = boxFont;
        carSelectBox.setName("Select vehicle");
        carSelectBox.setItems(carTypes);
        carSelectBox.setSelectedIndex(0);
        carSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // FIXME

            }
        });
        carSelectBox.scaleBy(1.3f);

        // DO NOT CREATE PLAYER LIST UNTIL LOBBY DATA EXISTS
        if (lobbiesFetched) {
            System.out.println((lobbyState.getLobby()==null) + "Object 'lobby' not yet created");
            preparePlayerList(); // generates playerTxtFields
        }
        else {
            System.out.println("Object 'lobby' created!");
        }

        table.center();

        for (TextField tf : playerTxtFields){
            table.add(tf).width(Gdx.graphics.getWidth()/2).height(Gdx.graphics.getHeight()/15).pad(Gdx.graphics.getHeight()/80);
//            table.add(carSelectBox);
            table.row();
        }
        stage.addActor(table);

        bottomTable = new Table();
        bottomTable.bottom();
        createAndSetMenuBtns();
        bottomTable.add(readyBtn).pad(10);
        bottomTable.add(exitBtn).pad(10);

        stage.addActor(table);
        stage.addActor(bottomTable);
        // table should already be in the stage; adding to
    }


    public void handleInput(){
        if(Gdx.input.justTouched()){
            lobbyState.setReady();
        }
    }

    public void render(){
//        handleInput();
        stage.draw();
    }

    /* CAN NOT RUN IN THE CONSTRUCTOR/**/
    public void dispose(){
        font.dispose();
        stage.dispose();
        bg.dispose();
        font.dispose();
        skin.dispose();
        buttonAtlas.dispose();
    }

    // TODO: should server distribute generated names? If so, trigger updateLobby on player join
    private void preparePlayerList(){

        // DO NOT RUN UNTIL LOBBY HAS BEEN UPDATED
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
                state.handleInput( "-1");
            }
        });
        bottomTable = new Table();
        bottomTable.bottom();
        bottomTable.add(readyBtn).pad(10);
        bottomTable.add(exitBtn).pad(10);
        bottomTable.row();
        stage.addActor(bottomTable);
    }

}
