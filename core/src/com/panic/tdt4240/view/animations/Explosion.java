package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.FloatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 15.04.2018.
 */

public class Explosion  extends Actor{
    private Array<Image> frames;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;

    private int frameCount;
    private static final int DIM = 64;

    private float currentFrameTime;
    private float maxFrameTime;

    private float x,y;

    public Explosion() {
        this(1/20f, 16);
    }

    public Explosion(float maxFrameTime, int frameCount){
        this.currentFrameTime = 99f;
        this.maxFrameTime = maxFrameTime;
        this.frameCount = frameCount;
        frames = new Array<>();
        Texture texture = new Texture("animations/explosion.png");
        TextureRegion[][] tmp = TextureRegion.split(texture,DIM,DIM);
        TextureRegion[] regions = new TextureRegion[frameCount];
        for(int i=0;i<tmp.length;i++){
            for(int j=0;j<tmp[i].length;j++){
                regions[tmp[i].length*i+j] = tmp[i][j];
            }
        }
        animation = new Animation<>(maxFrameTime,regions);

        /*
        TextureAtlas textureAtlas = new TextureAtlas("animations/explosion.atlas");
        Array<Sprite> tmp = textureAtlas.createSprites();

        for(Sprite sprite:tmp){
            frames.add(new Image(sprite));
        }
        */
    }

    @Override
    public void act(float dt){
        super.act(dt);
        currentFrameTime+=dt;

        currentFrame = animation.getKeyFrame(currentFrameTime,false);

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if(!animation.isAnimationFinished(currentFrameTime)) {
            batch.draw(currentFrame, x, y);
        }

    }

    public void startAnimation(float x, float y){
        currentFrameTime=0;
        this.x = x;
        this.y = y;
    }

    public float getDuration() {
        return animation.getAnimationDuration();
    }
/*
    public void update(float dt){
        if(frame>=0) {
            currentFrameTime += dt;
            if (currentFrameTime > maxFrameTime) {
                frame++;
                currentFrameTime = 0;
            }
            if (frame >= frameCount) {
                frame = -1;
            }
        }
    }

    public Image getFrame(){
        if(frame>=0) {
            return frames.get(frame);
        }
        else{
            return null;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean shouldAnimate(){
        return frame>=0;
    }
    */

}
