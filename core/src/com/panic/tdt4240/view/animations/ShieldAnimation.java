package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

/**
 * Created by magnus on 20.04.2018.
 */

public class ShieldAnimation extends CloudAnimation {
    public ShieldAnimation(){
        this(0.1f);
    }

    public ShieldAnimation(float maxFrameTime) {
        super(AnimationType.SHIELD, maxFrameTime);
    }

    @Override
    void setAnimation(Array<TextureAtlas.AtlasRegion> regions) {
        super.setAnimation(regions);
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public float getDuration() {
        return 2*super.getDuration()-maxFrameTime;
    }
}
