package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 15.04.2018.
 */

public class CloudAnimation extends AnimatedActor {

    public enum AnimationType {NONE, EXPLOSION, GREENCLOUD, HEALING, SHIELD, DEBUFF}


    public CloudAnimation(AnimationType cloudType){
        this(cloudType, 0.1f);
    }

    public CloudAnimation(AnimationType cloudType, float maxFrameTime) {
        super(maxFrameTime);
        TextureAtlas atlas = new TextureAtlas("animations/" + cloudType.name().toLowerCase() +".atlas");
        Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
        setAnimation(regions);
    }

    @Override
    public void act(float dt){
        super.act(dt);
        currentFrame = getCurrentFrame(dt);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if(currentFrameTime<getDuration()) {
            batch.draw(currentFrame, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), (Gdx.graphics.getWidth() / 6) / getWidth(), (Gdx.graphics.getWidth() / 6) / getWidth(), 0);
        }
    }
}
