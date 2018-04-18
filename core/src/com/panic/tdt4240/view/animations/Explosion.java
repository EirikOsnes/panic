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
        Array<TextureAtlas.AtlasRegion> regions = atlas.findRegions("expl");
        /*
        Texture texture = new Texture("animations/explosions.png");
        TextureRegion[][] tmp = TextureRegion.split(texture,DIM,DIM);
        Array<TextureRegion> regions = new Array<>();
        for(int i=0;i<tmp.length;i++){
            for(int j=0;j<tmp[i].length;j++){
                regions.add(tmp[i][j]);
            }
        }
        */
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
        batch.draw(currentFrame, getX(), getY());
    }
}
