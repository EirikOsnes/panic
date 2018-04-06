package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.util.GlobalConstants;
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
    private ArrayList<TextButton> cardButtons;
    private Stage stage;
    private Table table;
    private ArrayList<Boolean> selectedCard;
    private Texture background;
    private int playedCards;
    private TextField cardInfo;

    public PlayCardView(final PlayCardState state){
        super(state);
        renderer = Renderer.getInstance();
        background = new Texture("misc/background.png");
        cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);

        hand = state.player.playCards();
        cardButtons = new ArrayList<>(hand.size());
        selectedCard = new ArrayList<>(hand.size());
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        table = new Table();
        table.setWidth(SCREEN_WIDTH);
        table.left().bottom();
        BitmapFont font = new BitmapFont();
        final Skin skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin.addRegions(buttonAtlas);

        for (int i = 0; i < hand.size(); i++) {
            final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = font;

            selectedCard.add(i, false);

            buttonStyle.up = skin.getDrawable("button-up");
            buttonStyle.down = skin.getDrawable("button-down");

            final TextButton button = new TextButton("", buttonStyle);
            cardButtons.add(i, button);

            final int index = i;
            cardButtons.get(index).addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(playedCards < GlobalConstants.BASE_PLAY_CARDS){
                        if(!selectedCard.get(index)){
                            buttonStyle.up = skin.getDrawable("button-gone");
                            button.setStyle(buttonStyle);
                            cardButtons.get(index).getLabel().setText(hand.get(index).getTooltip());
                            state.handleInput(cardButtons.get(index));
                            selectedCard.set(index, true);
                            playedCards++;
                            cardInfo.setText(hand.get(index).getTooltip());
                        }
                        else{
                            buttonStyle.up = skin.getDrawable("button-up");
                            button.setStyle(buttonStyle);
                            cardButtons.get(index).getLabel().setText("");
                            selectedCard.set(index, false);
                            playedCards--;
                            cardInfo.setText("");
                        }
                    }
                    else if(selectedCard.get(index)){
                        buttonStyle.up = skin.getDrawable("button-up");
                        button.setStyle(buttonStyle);
                        cardButtons.get(index).getLabel().setText("");
                        selectedCard.set(index, false);
                        playedCards--;
                        cardInfo.setText("");
                    }
                }
            });
            table.add(cardButtons.get(index)).width(SCREEN_WIDTH/hand.size());
        }
        table.background(new TextureRegionDrawable(new TextureRegion(background)));
        table.pack();
        stage.addActor(table);

        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = font;
        cardInfo = new TextField("", textFieldStyle);
        cardInfo.setWidth(SCREEN_WIDTH/4);
        cardInfo.setHeight(SCREEN_HEIGHT/10);
        cardInfo.setPosition(SCREEN_WIDTH/2, table.getHeight()*2);
        stage.addActor(cardInfo);

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
