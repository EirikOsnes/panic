package com.panic.tdt4240.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by victor on 05.03.2018.
 */

public class AbstractController {

    private int x, y; // POINTER-posisjon


    // Various helper functions to remove terribly redundant code



    boolean clickedSprite(TextureRegion texture){
        return (texture.getRegionY() <= y && y <= texture.getRegionY() + texture.getRegionHeight() &&
                texture.getRegionX() <= x && x <= texture.getRegionX() + texture.getRegionWidth() ); }

    boolean isTouching() {return Gdx.input.isTouched();}
    boolean justTouched() {return Gdx.input.justTouched();}
    void getPointerPosition(){ x = Gdx.input.getX(); y = Gdx.input.getY(); }
}
