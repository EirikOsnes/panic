package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Hand;
import com.panic.tdt4240.states.CardPlayState;
import com.panic.tdt4240.states.State;
import com.panic.tdt4240.view.TextureClasses.CardTexture;
import com.panic.tdt4240.view.TextureClasses.HandTexture;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;

/**
 * Created by victor on 05.03.2018.
 */

public class PlayCardView extends AbstractView{

    Renderer renderer;
    private boolean PlayState = false; // animation state when cards are to
    private HandTexture hv;
    private ArrayList<Card> hand;

    private Stage stage;
    private ImageTextButton button;
    Skin skin;
    TextureAtlas buttonAtlas;
    ImageTextButton.ImageTextButtonStyle buttonStyle;

    public PlayCardView(CardPlayState state){
        super(state);
        renderer = Renderer.getInstance();

        hand = state.player.getHand();
        stage = new Stage();

        for (int i = 0; i < hand.size(); i++) {
            Gdx.input.setInputProcessor(stage);
            skin = new Skin();
            buttonAtlas = new TextureAtlas(Gdx.files.internal("buttons/buttons.pack"));
            skin.addRegions(buttonAtlas);



        }
        /*
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");

        createGameBtn = new TextButton("Create new",textButtonStyle);
        joinGameBtn = new TextButton("Join",textButtonStyle);
        settingsBtn = new TextButton("Settings",textButtonStyle);

        createGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(createGameBtn);
            }
        });
         */


    }

    // TODO:
    public boolean loadResources(ArrayList<String> cardNames) {
        loadBackground();
        loadMap();
        loadVehicles();
        loadHand(cardNames);

        return (hv.getCardImgs().size() < 5); // simple sanity check
    }


    public void render(){
        if (PlayState){
            renderBackground();
            renderMap();
            renderVehicles();
            renderHand();
        }
    }


    private void renderBackground(){

    }

    private void renderMap(){

    }

    private void renderVehicles(){

    }

    private void loadBackground(){

    }

    private void loadMap(){

    }

    private void loadVehicles(){

    }

    private void loadHand(ArrayList<String> cardNames){
        this.hv = new HandTexture();
        for (int i = 0; i < cardNames.size(); i++) {
            hv.addCard(cardNames.get(i));
        }
    }

    private void renderHand(){
        for (int i = 0; i < hv.getCardImgs().size(); i++){
            renderer.render(getCard(i), SCREEN_WIDTH/5*i-getCard(i).getWidth()/2,
                    20, getCardWidth(i), getCardHeight(i) );
        }
    }

    // Make card more visible... maybe by bringing it further up on the screen?
    private void zoomCard(int i){

    }

    // FOR ITERATION
    private Texture getCard(int i){
        return hv.getCardImgs().get(i).getTexture();
    }

    private int getCardHeight(int i){
        return hv.getCardImgs().get(i).getTexture().getHeight();
    }

    private int getCardWidth(int i){
        return hv.getCardImgs().get(i).getTexture().getWidth();
    }

    public void dispose(){
        renderer.dispose();
    }

}
