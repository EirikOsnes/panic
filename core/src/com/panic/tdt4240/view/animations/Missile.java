package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 17.04.2018.
 */

public class Missile extends AnimatedActor {

    public static final String COLOR_RED = "RED";
    public static final String COLOR_GREEN = "GREEN";

    private float targetX;
    private float targetY;

    private float totalFrameTime;

    public Missile(float maxFrameTime, int frameCount, String color){
        super(maxFrameTime,frameCount);
        totalFrameTime = 1;
        TextureAtlas atlas = new TextureAtlas("animations/missiles.atlas");
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for(int i=0;i<frameCount;i++){
            String regionName = color.toLowerCase() + "-" + (i+1);
            regions.add(atlas.findRegion(regionName));
        }
        setAnimation(regions);
    }

    @Override
    public void startAnimation(float startX, float startY, float endX, float endY){
        super.startAnimation(startX,startY,endX,endY);
        //this.addAction(Actions.moveTo(endX, endY, 2));
        //targetX=endX;
        //targetY=endY;
    }

    public Missile(String color){
        this(0.1f,3,color);
    }

    @Override
    public void startAnimation(float startX, float startY, float endX, float endY) {
        super.startAnimation(startX, startY, endX, endY);
        targetX = endX;
        targetY = endY;
    }

    @Override
    public void act(float dt){
        super.act(dt);
        System.out.printf("Current pos: [%f, %f]\n", getX(), getY());
        //setPosition(getX()+(targetX-getX())*dt,getY()+(targetY-getY())*dt);
        currentFrameTime+=dt;
        currentFrame = getCurrentFrame(dt,true);
        //TODO
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        super.draw(batch,parentAlpha);
        if(currentFrameTime<totalFrameTime) {
            batch.draw(currentFrame, getX() + (targetX - getX()) * currentFrameTime / totalFrameTime, getY() + (targetY - getY()) * currentFrameTime / totalFrameTime, getOriginX(), getOriginY(), currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), 1, 1, getRotation(), true);
        }
    }
}
