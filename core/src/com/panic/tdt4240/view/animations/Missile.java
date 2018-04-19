package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class Missile extends AnimatedActor {

    public static final String COLOR_RED = "RED";
    public static final String COLOR_GREEN = "GREEN";

    public Missile(float maxFrameTime, String color){
        super(maxFrameTime);
        setVisible(false);
        TextureAtlas atlas = new TextureAtlas("animations/missiles.atlas");
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for(int i=0;i<3;i++){
            String regionName = color.toLowerCase() + "-" + (i+1);
            regions.add(atlas.findRegion(regionName));
        }

        setAnimation(regions);
    }

    public Missile(String color){
        this(0.1f, color);
    }

    @Override
    public void act(float dt){
        super.act(dt);
        currentFrameTime+=dt;
        currentFrame = getCurrentFrame(dt,true);
        //TODO
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if (isVisible()) {
            batch.draw(currentFrame,getX(),getY(),getOriginX(),getOriginY(),currentFrame.getRegionWidth(),currentFrame.getRegionHeight(),1,1,getRotation(),true);
        }
    }
}
