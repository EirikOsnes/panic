package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 15.04.2018.
 */

public class Explosion  extends AnimatedActor {

    public static final String EXPLOSION = "explosion";
    public static final String POISON = "poison";

    public Explosion(float maxFrameTime, String cloudType) {
        super(maxFrameTime);
        TextureAtlas atlas = new TextureAtlas("animations/" + cloudType +".atlas");
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        setAnimation(regions);
    }


    @Override
    public void act(float dt){
        super.act(dt);
        currentFrame = getCurrentFrame(dt,false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        batch.draw(currentFrame, getX(), getY(),getOriginX(),getOriginY(),getWidth(),getHeight(), (Gdx.graphics.getWidth()/6)/getWidth(),(Gdx.graphics.getWidth()/6)/getWidth(),0);
    }
}
