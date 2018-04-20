package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class Missile extends AnimatedActor {

    public enum MissileType {NONE, RED, GREEN}

    public Missile(float maxFrameTime, MissileType color){
        super(maxFrameTime);
        setVisible(false);
        TextureAtlas atlas = new TextureAtlas("animations/missiles.atlas");
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for(int i=0;i<3;i++){
            String regionName = color.name().toLowerCase() + "-" + (i+1);
            regions.add(atlas.findRegion(regionName));
        }
        setAnimation(regions);
    }

    public Missile(MissileType color){
        this(0.1f, color);
    }

    @Override
    public void act(float dt){
        super.act(dt);
        currentFrameTime+=dt;
        currentFrame = getCurrentFrame(dt);
        //TODO
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if (isVisible()) {
            batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),currentFrame.getRegionWidth(),currentFrame.getRegionHeight(), (Gdx.graphics.getWidth()/20)/getWidth(), (Gdx.graphics.getWidth()/20)/getHeight(),getRotation(),true);
        }
    }
}
