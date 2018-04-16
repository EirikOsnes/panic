package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by magnus on 15.04.2018.
 */

public class Explosion  extends Actor{
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;

    private int frameCount;

    private float currentFrameTime;
    private float maxFrameTime;

    public Explosion(float maxFrameTime, int frameCount){
        this.maxFrameTime = maxFrameTime;
        this.frameCount = frameCount;
        Texture texture = new Texture("animations/explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(texture,64,64);
        TextureRegion[] regions = new TextureRegion[frameCount];
        for(int i=0;i<tmp.length;i++){
            for(int j=0;j<tmp[i].length;j++){
                regions[tmp[i].length*i+j] = tmp[i][j];
            }
        }
        animation = new Animation<>(maxFrameTime,regions);
    }

    @Override
    public void act(float dt){
        currentFrameTime+=dt;

        currentFrame = animation.getKeyFrame(currentFrameTime,false);
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if(!animation.isAnimationFinished(currentFrameTime)) {
            batch.draw(currentFrame, getX(), getY());
        }
    }

    public void startAnimation(float x, float y){
        currentFrameTime=0;
        setPosition(x,y);
    }
}
