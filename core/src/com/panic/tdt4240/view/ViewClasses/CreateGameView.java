package com.panic.tdt4240.view.ViewClasses;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.states.CreateGameState;
import com.panic.tdt4240.view.Renderer;
/**
 * Created by victor on 12.03.2018.
 */

public class CreateGameView extends AbstractView {

    private Stage stage;
    private TextureAtlas btnAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle btnStyle;
    private Table table;
    private Texture bg;
    private TextButton exitToMainMenuBtn;
    private String lobbyName;

    private TextField in_LobbyName;
    private SelectBox<String> in_mapID;
    private SelectBox<String> in_maxPlayers;

    public CreateGameView(CreateGameState cgState) {
        super(cgState);

        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        table = new Table();
        Gdx.input.setInputProcessor(stage);
        font = new BitmapFont();
        btnAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button_up");
        btnStyle.down = skin.getDrawable("button_up");

        table.setFillParent(true);
        table.center();
        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        TextField.TextFieldStyle textStyle = new TextField.TextFieldStyle();
        textStyle.font = font;

        in_LobbyName = new TextField("Set lobby name", textStyle);
        table.add(in_LobbyName).center().top().pad(30); table.row();


        String[] mapIDs = {"test", "01"};

        //FIXME
        SelectBox.SelectBoxStyle boxStyle = new SelectBox.SelectBoxStyle();
        boxStyle.font=font;
        in_mapID = new SelectBox(skin);
        in_mapID.setItems(mapIDs);

        String[] max_players = {"2", "3", "4", "5", "6"};
        in_maxPlayers = new SelectBox(skin); in_maxPlayers.setItems(max_players);

        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);
        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(0);
            }
        });

        table.add(in_LobbyName).center().padTop(PanicGame.HEIGHT/9).row();
        table.add(in_mapID).center().padTop(PanicGame.HEIGHT/8).row();
        table.add(in_maxPlayers).center().padTop(PanicGame.HEIGHT/8).row();

        // TODO: a button for text input and direct connection to a game lobby?

        table.add(exitToMainMenuBtn).row();

        table.pack();

        stage.addActor(table);

        // TODO: fetch ID from server, be added to list of lobbies.
        // TODO: display list of settings, previews... etc.
    }

    public void handleInput(Object o){
        if (o.getClass() == String.class){

        }
    }

    public void render(){

    }

    public void dispose(){
        stage.dispose();
        btnAtlas.dispose();
        font.dispose();
        skin.dispose();
    }

    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            lobbyName = text;
        }
        @Override
        public void canceled () {
        }
    }
}
