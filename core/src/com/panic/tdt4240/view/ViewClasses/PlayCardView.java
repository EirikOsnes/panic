package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.states.CardPlayState;
import com.panic.tdt4240.view.TextureClasses.CardDrawable;
import com.panic.tdt4240.view.TextureClasses.HandTexture;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;

/**
 * Created by victor on 05.03.2018.
 */

public class PlayCardView extends AbstractView{

    Renderer renderer;
    private HandTexture hv;
    private ArrayList<Card> hand;
    private ArrayList<ImageTextButton> cardButtons;
    private Stage stage;
    private Table table;
    private ArrayList<Boolean> selectedCard;

    public PlayCardView(final CardPlayState state){
        super(state);
        renderer = Renderer.getInstance();

        hand = state.player.getHand();
        cardButtons = new ArrayList<>(hand.size());
        selectedCard = new ArrayList<>(hand.size());
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        Table table = new Table();
        //TODO fiks posisjoneringen, sentreres ved venstre side når center() brukes
        table.setWidth(SCREEN_WIDTH);
        table.left().bottom().pad(10);
        BitmapFont font = new BitmapFont();
        Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("card_textures/buttons.pack");
        skin.addRegions(buttonAtlas);

        ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButton.ImageTextButtonStyle();
        buttonStyle.font = font;

        for (int i = 0; i < hand.size(); i++) {
            selectedCard.add(i, false);
            buttonStyle.imageUp = skin.getDrawable("button-up");
            buttonStyle.imageDown = skin.getDrawable("button-down");

            ImageTextButton button = new ImageTextButton("", buttonStyle);
            cardButtons.add(i, button);

            final int index = i;
            cardButtons.get(index).addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!selectedCard.get(index)){
                        cardButtons.get(index).getLabel().setText(hand.get(index).getTooltip());
                        state.handleInput(cardButtons.get(index));
                        selectedCard.set(index, true);
                    }
                    else{
                        cardButtons.get(index).getLabel().setText("");
                        selectedCard.set(index, false);
                    }
                }
            });
            table.add(cardButtons.get(index)).width(SCREEN_WIDTH/hand.size());
        }

        /*
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = skin.getDrawable("up-button");
        textButtonStyle.down = skin.getDrawable("down-button");
        textButtonStyle.checked = skin.getDrawable("checked-button");

        createGameBtn = new TextButton("Create new",textButtonStyle);
        joinGameBtn = new TextButton("Join",textButtonStyle);
        settingsBtn = new TextButton("SettingsView",textButtonStyle);

        createGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                state.handleInput(createGameBtn);
            }
        });
         */

        stage.addActor(table);
    }


    public void render(SpriteBatch sb){
        sb.setProjectionMatrix(cam.combined);
        stage.draw();
        /*
        renderBackground();
        renderMap();
        renderVehicles();
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
/*
    private void renderHand(SpriteBatch sb){
        int numCards = cardButtons.size();
        for (int i = 0; i < numCards; i++) {
            renderer.render(new CardDrawable(hand.get(i)).getDrawable(),SCREEN_WIDTH/numCards - cardButtons.get(i).getWidth(),
                    20.0f, getCardWidth(i), getCardHeight(i));
        }

    }
*/

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
    // Make card more visible... maybe by bringing it further up on the screen?
    private void zoomCard(int i){

    }

    // FOR ITERATION
    private Drawable getCard(int i){
        return hv.getCardImgs().get(i).getDrawable();
    }

    private float getCardHeight(int i){
        return hv.getCardImgs().get(i).getHeight();
    }

    private float getCardWidth(int i){
        return hv.getCardImgs().get(i).getWidth();
    }

    public void dispose(){
        renderer.dispose();
    }

}
