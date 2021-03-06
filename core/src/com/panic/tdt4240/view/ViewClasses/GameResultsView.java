package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.states.GameResultsState;
import com.panic.tdt4240.util.GlobalConstants;

/**
 * Created by victor on 12.03.2018.
 *
 * TLDR description: shitty copy of MenuView's functionality.
 *
 */

public class GameResultsView extends AbstractView {

    private Skin skin;
    private TextureAtlas btnAtlas;
    private Texture bg;
    private BitmapFont font;

    private Label label;
    int counter = 0;

    public GameResultsView(final GameResultsState resultsState) {
        super(resultsState);
        bg = new Texture("misc/background.png");
        Table table = new Table();
        font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle();
        BitmapFont labelFont = new BitmapFont();
        style.font = labelFont;
        label = new Label("LOADING",style);
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");

        skin = new Skin(Gdx.files.internal("skins/uiskin.json"),btnAtlas);

        skin.addRegions(btnAtlas);
        font.getData().scale(GlobalConstants.GET_TEXT_SCALE());
        labelFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*2);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = font;
        btnStyle.up = skin.getDrawable("button-up");
        btnStyle.down = skin.getDrawable("button-down");


        TextButton exitToMainMenuBtn = new TextButton("Exit to main menu", btnStyle);

        table.setFillParent(true);
        table.add(label).top();
        table.center();
        table.row().center().padTop(50);
        table.add(exitToMainMenuBtn).width(GlobalConstants.SCALE_WIDTH).height(GlobalConstants.SCALE_HEIGHT).bottom();
        table.background(new TextureRegionDrawable(new TextureRegion(bg)));

        exitToMainMenuBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resultsState.handleInput(-1);
            }
        });

        stage.addActor(table);

    }

    public void setLabelText(String text){
        label.setText(text);
    }

    public void render(){
        stage.draw();
    }

    public void dispose(){
        font.dispose();
        stage.dispose();
        bg.dispose();
        font.dispose();
        skin.dispose();
        btnAtlas.dispose();
    }
}
