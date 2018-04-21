package com.panic.tdt4240.view.ViewClasses;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.states.CreateGameState;
import com.panic.tdt4240.util.GlobalConstants;

/**
 * Created by victor on 12.03.2018.
 *
 * 18.04. NOTES: attempts to enter lobby from Create Game gives "Thread check" sequence:
 *                  output: LWJGL
 *                  order: 2, 4, 5
 *               Game List sequence:
 *                  output: LWJGL
 *                  order:
 *
 */

public class CreateGameView extends AbstractView {

    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font, boxFont;
    private TextButton.TextButtonStyle btnStyle, btnStyle2;
    private Table table;
    private Texture bg;
    private TextButton createLobbyBtn, exitToMainMenuBtn;

    private TextField lobbyNameTxtField;
    private SelectBox<String> mapSelectBox, maxPlayersSelectBox;
    private boolean haveSetName;

    public CreateGameView(final CreateGameState cgState) {
        super(cgState);

        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH, PanicGame.HEIGHT);
        table = new Table();
        font = new BitmapFont();
        boxFont = new BitmapFont();
        font.setColor(Color.WHITE);
        float textScale = GlobalConstants.GET_TEXT_SCALE();
        font.getData().scale(textScale);
        boxFont.getData().scale(textScale);
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font=font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");
        haveSetName = false;

        table.setFillParent(true);
        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = boxFont;
        textStyle.background = skin.getDrawable("textfield");

        textStyle.fontColor = skin.getColor("white");
        lobbyNameTxtField = new TextField("Set lobby name", textStyle);
        final TextField.OnscreenKeyboard keyboard = new TextField.OnscreenKeyboard() {
            @Override
            public void show(boolean visible) {
                Gdx.input.getTextInput(new Input.TextInputListener(){
                       @Override
                       public void input(String text){
                           if(text.length() > 0){
                               lobbyNameTxtField.setText(text);
                               cgState.setName(text);
                               System.out.println(text);
                               haveSetName = true;
                           }
                       }
                       @Override
                       public void canceled(){
                           System.out.println("Cancelled.");
                       }
                   },
                        "Set lobby name", "", "Set lobby name");
            }
        };
        lobbyNameTxtField = new TextField("Set lobby name", textStyle);
        lobbyNameTxtField.setOnscreenKeyboard(keyboard);
        keyboard.show(true);

        table.add(lobbyNameTxtField).center().top().pad(30);
        table.row();

        String[] mapIDs = ((CreateGameState)state).getMapIDs();
        SelectBox.SelectBoxStyle boxStyle = new SelectBox.SelectBoxStyle(skin.get(SelectBox.SelectBoxStyle.class));
        boxStyle.font = boxFont;

        mapSelectBox = new SelectBox<>(skin);
        mapSelectBox.setStyle(boxStyle);
        mapSelectBox.setName("Select map");
        mapSelectBox.setItems(mapIDs);
        mapSelectBox.getScrollPane().scaleBy(GlobalConstants.GET_TEXT_SCALE());

        mapSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cgState.setMapID(mapSelectBox.getSelected());
            }
        });
        //First map in the array is the default value
        cgState.setMapID(mapIDs[0]);
        mapSelectBox.pack();

        String[] max_players = ((CreateGameState)state).getMaxPlayers();
        maxPlayersSelectBox = new SelectBox<>(skin);
        maxPlayersSelectBox.setStyle(boxStyle);
        maxPlayersSelectBox.setName("Max number of players");
        maxPlayersSelectBox.setItems(max_players);
        maxPlayersSelectBox.getScrollPane().scaleBy(GlobalConstants.GET_TEXT_SCALE());

        maxPlayersSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cgState.setMaxPlayerCount(Integer.valueOf(maxPlayersSelectBox.getSelected()));
            }
        });
        //First value in the array is the default value
        cgState.setMaxPlayerCount(Integer.valueOf(max_players[0]));
        maxPlayersSelectBox.pack();

        createLobbyBtn = new TextButton("Create lobby", btnStyle);
        createLobbyBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(haveSetName){
                    System.out.println("Create btn pressed");
                    cgState.createButtonClick();
                }
                else {
                    keyboard.show(true);
                }
            }
        });
        
        createLobbyBtn.pack();

        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cgState.handleInput("-1");
//                System.out.println("IS IT WORKING");
            }
        });
        exitToMainMenuBtn.pack();

        table.add(lobbyNameTxtField).top().padTop(SCREEN_HEIGHT / 16f)
				.width(SCREEN_WIDTH/4).height(SCREEN_HEIGHT/20).row();
        table.add(mapSelectBox).padTop(SCREEN_HEIGHT / 16f)
				.width(SCREEN_WIDTH/4).height(SCREEN_HEIGHT/20).row();
        table.add(maxPlayersSelectBox).padTop(SCREEN_HEIGHT / 16f)
				.width(SCREEN_WIDTH/8).height(SCREEN_HEIGHT/20).row();

        // TODO: a button for text input and direct connection to a game lobby?
        table.add(createLobbyBtn).width(SCREEN_WIDTH/2)
				.height(SCREEN_HEIGHT/15).pad(SCREEN_HEIGHT/40).row();
        table.add(exitToMainMenuBtn).width(SCREEN_WIDTH/2)
				.height(SCREEN_HEIGHT/15).pad(SCREEN_HEIGHT/40).row();

        table.pack();

        stage.addActor(table);

        // TODO: fetch ID from server, be added to list of lobbies.
        // TODO: display list of settings, previews... etc.
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        bg.dispose();
        font.dispose();
        boxFont.dispose();
        skin.dispose();
        btnAtlas.dispose();
    }
}
