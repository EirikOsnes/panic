package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
 */

public class GameResultsView extends AbstractView {

    private Renderer renderer;
    private ArrayList<TextButton> textButtons;
    private Stage stage;
    private Skin skin;
    TextButton.TextButtonStyle buttonStyle, rankingStyle;
    private TextureAtlas buttonAtlas;
    private Texture bg;
    private Table table;
    private BitmapFont font;
    TextButton exitToLobbyButton, exitToMainMenuButton;


    public GameResultsView(GameResultsState resultsState, ArrayList<Player> deadPlayers) {
        super(resultsState);
        renderer = Renderer.getInstance();

        bg = new Texture("misc/background.png");
        textButtons = new ArrayList<>(deadPlayers.size());
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setWidth(SCREEN_WIDTH/2);
        table.center();

        buttonAtlas = new TextureAtlas("start_menu_buttons/buttons.pack");
        skin.addRegions(buttonAtlas);
        font = new BitmapFont();
        skin.addRegions(buttonAtlas);
        buttonStyle = new TextButton.TextButtonStyle();
        rankingStyle = new TextButton.TextButtonStyle();

        /* TODO maybe make a special button for the winner?
        fancy schmancy visuals... needs another style for this. /**/

/*        TextButton button = new TextButton("1.  "
                + deadPlayers.get(deadPlayers.size()-1), buttonStyle);
        textButtons.add(button);
        table.add(textButtons.get(0)).width(SCREEN_WIDTH);
 /**/


        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // different styling for results and buttons
        rankingStyle.font = font;
        rankingStyle.up = skin.getDrawable("button-up");
        rankingStyle.down = skin.getDrawable("button-down");

        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("button-up");
        buttonStyle.down = skin.getDrawable("button-down");

        Label label = new Label("Match results", skin);

        // Using vehicleID until further ado
        for (int i = 0; i < Math.floor(deadPlayers.size()/2); i++){
            TextButton button = new TextButton(""
                    + (i+1) + ".  " +
                    deadPlayers.get(deadPlayers.size()-1-i).getVehicle().getVehicleID(), rankingStyle);
            textButtons.add(button);
        }

        // shitty copy of menuview's functionality
        table.setFillParent(true);
        table.add(label).top().padBottom(60);
        table.center();
        for (int i = 0; i < textButtons.size(); i++){
            table.row();
            table.add(textButtons.get(i)).width(300).height(50).pad(20);
        }

        exitToLobbyButton = new TextButton("Exit to lobby", buttonStyle);
        exitToMainMenuButton = new TextButton("Exit to main menu", buttonStyle);

        exitToLobbyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput( 1);
            }
        });

        exitToMainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(2);
            }
        });


    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        renderer.sb.begin();
        renderer.sb.draw(bg, 0, 0, PanicGame.WIDTH, PanicGame.HEIGHT);
        stage.draw();
        font.draw(renderer.sb, "HA! The worst of you don't deserve \n " +
                "mentioning, because you SUCK.",
                PanicGame.HEIGHT/2, PanicGame.WIDTH/2);
        renderer.sb.end();
    }

    public void dispose(){
        renderer.dispose();
        font.dispose();

    }
}
