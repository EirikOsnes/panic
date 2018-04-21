package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Choffa for panic on 19-Apr-18.
 * On permission can be used outside panic.
 */
public class MissileAction extends MoveToAction {


    private final Actor missileTarget;
    private final Actor missileInstigator;

    public MissileAction(Actor target, Actor instigator) {
        this.missileTarget = target;
        this.missileInstigator = instigator;
        //TODO: calculate duration
        super.setDuration(2);
    }

    @Override
    protected void begin() {
        target.setPosition(missileInstigator.getX(Align.center), missileInstigator.getY(Align.center));
        target.setVisible(true);
        missileInstigator.setRotation(target.getRotation() - 90f);
        super.begin();
        super.setAlignment(Align.center);
        super.setX(missileTarget.getX(Align.center));
        super.setY(missileTarget.getY(Align.center));
    }

    @Override
    protected void end() {
        super.end();
        target.setVisible(false);
        missileInstigator.setRotation(0.0f);
    }

    @Override
    protected void update(float percent) {
        super.update(percent);
    }
}
