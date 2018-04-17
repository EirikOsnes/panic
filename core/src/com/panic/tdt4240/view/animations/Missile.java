package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class Missile extends AnimatedActor {

    public static final String COLOR_RED = "RED";
    public static final String COLOR_GREEN = "GREEN";

    public Missile(float maxFrameTime, int frameCount, String color){
        super(maxFrameTime,frameCount);
        TextureAtlas atlas = new TextureAtlas("animations/missiles.atlas");
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions(color);
        animation = new Animation<TextureRegion>(maxFrameTime,regions);
    }

    @Override
    public void act(float dt){
        super.act(dt);
        //TODO
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        //TODO
    }
}
