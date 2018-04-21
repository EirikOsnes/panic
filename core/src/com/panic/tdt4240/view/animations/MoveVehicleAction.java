package com.panic.tdt4240.view.animations;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;



/**
 * Created by Choffa for panic on 19-Apr-18.
 * On permission can be used outside panic.
 */
public class MoveVehicleAction extends MoveToAction {

    public MoveVehicleAction(float endX, float endY) {
        super.setPosition(endX, endY);
        super.setDuration(2);
    }

    @Override
    protected void begin() {
        super.begin();
        float dx = getX() - target.getX();
        float dy = getY() - target.getY();
        Vector2 vec = new Vector2(dx, dy);
        float rot = vec.angle() - 90f;
        target.setRotation(rot);

    }

    @Override
    protected void end() {
        super.end();
        target.setRotation(0.0f);
    }
}
