package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.graphics.Texture;
import com.panic.tdt4240.view.TextureClasses.HandTexture;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;

/**
 * Created by victor on 05.03.2018.
 */

public class PlayView extends AbstractView{

    Renderer renderer;
    private boolean PlayState = false; // animation state when cards are to
    private HandTexture hv;

    public PlayView(){
        super();
        renderer = Renderer.getInstance();
    }

    // TODO:
    public boolean loadResources(ArrayList<String> cardNames) {
        loadBackground();
        loadMap();
        loadVehicles();
        loadHand(cardNames);
        return (hv.getCardImgs().size() < ); // simple sanity check
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


}
