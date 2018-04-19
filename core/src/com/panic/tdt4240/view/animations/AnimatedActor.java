package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class AnimatedActor extends Actor {
    private Animation<TextureAtlas.AtlasRegion> animation;

    private float rotation;

    protected TextureRegion currentFrame;

    protected float currentFrameTime;
    protected float maxFrameTime;

    protected int frameCount;

    public AnimatedActor(float maxFrameTime,int frameCount){
        this.currentFrameTime = 99f;
        this.maxFrameTime = maxFrameTime;
        this.frameCount = frameCount;
    }


    /**
     * Sets the animation and defines the centre of the animation frames. All frames have to be of equal size
     * @param regions The textureRegions that make up the animation
     */
    public void setAnimation(Array<TextureAtlas.AtlasRegion> regions){
        animation = new Animation<>(maxFrameTime,regions);
        //setOrigin(regions.get(0).getRegionWidth()/2,regions.get(0).getRegionHeight()/2);
        this.setOrigin(Align.center);
    }

    /**
     * Starts the animation, and defines the position in which it will be drawn
     * @param x coordinate of where to be drawn
     * @param y coordinate of where to be drawn
     */
    public void startAnimation(float x, float y){
        if(animation==null){
            throw new IllegalStateException("Animation is not yet defined");
        }
        currentFrameTime=0;
        setPosition(x,y);
    }

    /**
     * Starts the animation and defines where it will start and end
     * @param startX
     * @param startY
     * @param endX
     * @param endY
     */
    public void startAnimation(float startX, float startY, float endX, float endY){
        startAnimation(startX,startY);
        rotation = ((float)(Math.atan((double)((endY-startY)/(endX-startX)))*(180/Math.PI)));
    }

    /**
     * returns the current frame of the animation
     * @param dt the time since the last time it was drawn
     * @param looping Whether the animation should repeat itself
     * @return The frame to be drawn
     */
    public TextureRegion getCurrentFrame(float dt, boolean looping){
        currentFrameTime+=dt;
        return animation.getKeyFrame(currentFrameTime,looping);
    }

    /**
     * @return The duration of the entire animation.
     */
    public float getDuration() {
        return animation.getAnimationDuration();
    }

    @Override
    public float getRotation() {
        return rotation;
    }
}
