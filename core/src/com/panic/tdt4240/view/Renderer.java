package com.panic.tdt4240.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.Hand;

/**
 * Created by victor on 05.03.2018.
 * The class should mainly render sprites, in their positions. Preferably, avoid accessing objects.
 * Use SpriteBatch, render Textures for static images, TextureRegions for animations.
 * Renderer should take all the various views and render them in order, depending on the runtime
 * state.
 *
 * E.g. play state, from bottom: background, asteroids, vehicles, cards.
 * Background can be attached to this class.
 *
 */

public class Renderer {

    private boolean PlayState;
    public SpriteBatch sb;
    OrthographicCamera cam;

    private static Renderer INSTANCE = new Renderer();

    public static Renderer getInstance(){ //
        if (INSTANCE == null) INSTANCE = new Renderer();
        return INSTANCE;
    }

    private Renderer(){
        sb = new SpriteBatch();
        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

/*    public void render(Texture img, int x, int y, int width, int height){
        sb.draw(img, x, y, width, height);
    }

    public void render(Sprite img, int x, int y, int width, int height){
        sb.draw(img, x, y, width, height);
    } /**/

    public void dispose(){
        sb.dispose();
    }

}
