package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Player;
import com.panic.tdt4240.states.GameResultsState;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;

/**
 * Created by victor on 12.03.2018.
 *
 * TLDR description: shitty copy of MenuView's functionality.
 *
 */

public class GameResultsView extends AbstractView {

    private Renderer renderer;
    private ArrayList<TextButton> textBtns;
    private Stage stage;
    private Skin skin;
    TextButton.TextButtonStyle btnStyle, rankingStyle;
    private TextureAtlas btnAtlas;
    private Texture bg;
    private Table table;
    private BitmapFont font;
    TextButton exitToLobbyBtn, exitToMainMenuBtn;

    public GameResultsView(final GameResultsState resultsState, ArrayList<Player> deadPlayers) {
        super(resultsState);
        renderer = Renderer.getInstance();
        Gdx.gl.glClearColor(0,0,0,0);

        bg = new Texture("misc/background.png");
        textBtns = new ArrayList<>(deadPlayers.size());
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setWidth(SCREEN_WIDTH/2);
        table.center();
        btnAtlas = new TextureAtlas("start_menu_buttons/button.atlas");

        skin = new Skin();
//        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);

        skin.addRegions(btnAtlas);
        font = new BitmapFont();
        btnStyle = new TextButton.TextButtonStyle();
        rankingStyle = new TextButton.TextButtonStyle();

        /* TODO: maybe make a special button for the winner?
        fancy schmancy visuals... needs another style for this. /**/

/*        TextButton button = new TextButton("1.  "
                + deadPlayers.get(deadPlayers.size()-1), btnStyle);
        textBtns.add(button);
        table.add(textBtns.get(0)).width(SCREEN_WIDTH);
 /**/

        // different styling for results and buttons. ranking buttons
        // don't need different looks when pressed
        rankingStyle.font = font;
        rankingStyle.up = skin.getDrawable("button-up");
        rankingStyle.down = skin.getDrawable("button-down");

        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");


        for (int i = 0; i < Math.floor(deadPlayers.size()/2); i++){
            TextButton button = new TextButton(""
                    + (i+1) + ". Player " +
                    (i+1), rankingStyle);
            textBtns.add(button);
        }

        exitToLobbyBtn = new TextButton("Exit to lobby", btnStyle);
        exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);

        exitToLobbyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // state type: State.java... runs GameResultsState.handleInput()
                resultsState.handleInput( 0);
            }
        });

        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resultsState.handleInput(-1);
                // insert whatever should happen

            }
        });

        table.setFillParent(true);
        table.center();
        for (int i = 0; i < textBtns.size(); i++){
            table.row().center();
            table.add(textBtns.get(i)).width(300).height(40).pad(20).width(PanicGame.WIDTH);
        }
        table.row().center().padTop(50);
        table.add(exitToLobbyBtn); table.row().center().padTop(20);
        table.add(exitToMainMenuBtn);
        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        stage.addActor(table);

    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        renderer.sb.begin();
        renderer.sb.draw(bg, 0, 0, PanicGame.WIDTH, PanicGame.HEIGHT);
        stage.draw();
/*        font.draw(renderer.sb, "Ha! The worst of you don't deserve \n " +
                "mentioning because YOU SUCK.",
                PanicGame.HEIGHT/2, PanicGame.WIDTH/2); /**/
        renderer.sb.end();
    }

    public void dispose(){
        renderer.dispose();
        font.dispose();
        stage.dispose();
    }
}
