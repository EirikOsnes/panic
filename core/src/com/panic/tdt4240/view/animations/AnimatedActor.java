package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class AnimatedActor extends Actor {
    protected Animation<TextureRegion> animation;

    protected TextureRegion currentFrame;

    protected float currentFrameTime;
    protected float maxFrameTime;

    protected int frameCount;

    public AnimatedActor(float maxFrameTime,int frameCount){
        this.currentFrameTime = 99f;
        this.maxFrameTime = maxFrameTime;
        this.frameCount = frameCount;
    }

    public void setAnimation(Array<TextureRegion> regions){
        animation = new Animation<>(maxFrameTime,regions);
    }
    public void startAnimation(float x, float y){
        if(animation==null){
            throw new IllegalStateException("Animation is not yet defined");
        }
        currentFrameTime=0;
        setPosition(x,y);
    }

    public TextureRegion getCurrentFrame(float dt, boolean looping){
        currentFrameTime+=dt;
        return animation.getKeyFrame(currentFrameTime,looping);
    }

    public float getDuration() {
        return animation.getAnimationDuration();
    }
}
