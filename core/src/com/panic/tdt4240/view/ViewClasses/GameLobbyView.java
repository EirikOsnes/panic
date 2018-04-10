package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.states.GameLobbyState;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyView extends AbstractView {


    private Renderer renderer;
    private Stage stage;
    private Button playerBtn, exitBtn;
    private TextureAtlas buttonAtlas;
    private Skin skin;
    private BitmapFont font;
    private TextButton.TextButtonStyle playerBtnStyle, exitBtnStyle;
    private Table table;
    private Texture background;
    private Player localUser;
    ArrayList<Player> players;

    public GameLobbyView(GameLobbyState lobbyState) {
        super(lobbyState);
        background = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        font = new BitmapFont();

        skin = new Skin(Gdx.files.internal("uiskin.json"));
        buttonAtlas = new TextureAtlas("start_menu_buttons/buttons.pack");
        skin.addRegions(buttonAtlas);

        playerBtnStyle = new TextButton.TextButtonStyle();
        playerBtnStyle.font = font;
        playerBtnStyle.up = skin.getDrawable("button-up");
        playerBtnStyle.down = skin.getDrawable("button-up");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        playerBtn = new TextButton("", playerBtnStyle);
        exitBtn = new TextButton("", exitBtnStyle);

        playerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( 0);
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( 1);
            }
        });

        /** not initialised: localUser, players
         *
         */

        // TODO: some server-side code must be completed before proper finishing...

        // For shitty testing
        localUser = new Player(new Stack<Card>());
        players.add(localUser);
        players.add(new Player(new Stack<Card>()));
        renderPlayerList();

        table.background(new TextureRegionDrawable(new TextureRegion(background)));

        //-------
        stage.addActor(table);

        renderer = Renderer.getInstance();
    }

    public void playerJoined(Player p){
        // add more buttons for each player who joins
        players.add(p);
    }


    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        renderer.sb.begin();
        renderer.sb.draw(background,0,0,PanicGame.WIDTH,PanicGame.HEIGHT);
        stage.draw();
        renderer.sb.end();
    }

    public void dispose(){
        renderer.dispose();
        font.dispose();
        stage.dispose();
    }

    private void renderPlayerList(){
        for (Player p : players){
            // generate btn for players
            if (p == localUser){

            }
        }

    }
}
