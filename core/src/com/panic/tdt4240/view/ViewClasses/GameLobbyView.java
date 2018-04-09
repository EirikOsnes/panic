package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.states.GameLobbyState;
import com.panic.tdt4240.util.PlayerNameGenerator;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by victor on 12.03.2018.
 */

public class GameLobbyView extends AbstractView {


    private Renderer renderer;
    private Table table;
    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;

    private ArrayList<String> usedNames;

    private ArrayList<TextButton> textBtns;
    private Button playerBtn, exitBtn, launchGameBtn;
    private TextButton.TextButtonStyle playerBtnStyle, exitBtnStyle;

    private ArrayList<Player> players;

    public GameLobbyView(GameLobbyState lobbyState) {
        super(lobbyState);
        bg = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        font = new BitmapFont();
        textBtns = new ArrayList<>();

        buttonAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"),buttonAtlas);

        playerBtnStyle = new TextButton.TextButtonStyle();
        playerBtnStyle.font = font;
        playerBtnStyle.up = skin.getDrawable("button-up");
        playerBtnStyle.down = skin.getDrawable("button-up");

        exitBtnStyle = new TextButton.TextButtonStyle();
        exitBtnStyle.font = font;
        exitBtnStyle.up = skin.getDrawable("button-up");
        exitBtnStyle.down = skin.getDrawable("button-down");

        playerBtn = new TextButton("", playerBtnStyle);
        launchGameBtn = new TextButton("", exitBtnStyle);
        exitBtn = new TextButton("", exitBtnStyle);

        playerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( 0);
            }
        });
        launchGameBtn.addListener(new ChangeListener() {
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

        /** testing tools: initialised localUser, players */

        // TODO: some server-side code must be completed prior to completion...

        // For shitty testing
        usedNames = new ArrayList<>();
        players = new ArrayList<>();
        players.add(new Player(new Stack<Card>()));
        players.add(new Player(new Stack<Card>()));
        players.add(new Player(new Stack<Card>()));
        preparePlayerList();

        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        table.setFillParent(true);
        table.row().center();
        table.add(new Label(PanicGame.TITLE, skin)).top().padBottom(10);
        table.row().center();
        table.add(new Label(PanicGame.FULL_TITLE, skin));
        for (TextButton tb : textBtns){
            table.row().center();
            table.add(tb).width(200).height(50).pad(10);
        }
        table.pack();

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
        renderer.sb.draw(bg,0,0,PanicGame.WIDTH,PanicGame.HEIGHT);
        stage.draw();
        renderer.sb.end();
    }

    public void dispose(){
        renderer.dispose();
        font.dispose();
        stage.dispose();
    }

    private void preparePlayerList(){
        int counted = 0;
        String name;
        while (counted < players.size()-1){
            int rand = (int) Math.random() * (PlayerNameGenerator.getCount()-1);
            name = PlayerNameGenerator.getName(rand);
            if (! usedNames.contains(name)) {
                TextButton button = new TextButton(
                        name, playerBtnStyle);
                textBtns.add(button);
                System.out.println(counted);
                usedNames.add(name);
            }
            else System.out.println(name);
        }

    }
}
