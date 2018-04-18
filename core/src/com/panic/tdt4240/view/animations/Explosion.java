package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 15.04.2018.
 */

public class Explosion  extends AnimatedActor{

    private static final int DIM = 64;

    public Explosion() {
        this(1/20f, 16);
    }

    public Explosion(float maxFrameTime, int frameCount){
        super(maxFrameTime,frameCount);
        TextureAtlas atlas = new TextureAtlas("animations/explosion.atlas");
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for(int i=0;i<frameCount;i++){
            String regionName = "expl-" + (i+1);
            regions.add(atlas.findRegion(regionName));
        }
        setAnimation(regions);
    }


    @Override
    public void act(float dt){
        super.act(dt);
        currentFrame = getCurrentFrame(dt,true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        batch.draw(currentFrame, getX(), getY());
    }
}
