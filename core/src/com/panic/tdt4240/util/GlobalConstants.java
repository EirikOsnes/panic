package com.panic.tdt4240.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;


/**
 * Global variables for the game
 * Created by Hermann on 09.03.2018.
 */

public class GlobalConstants {

    //Base number of cards drawn each round
    public static final int BASE_DRAW_CARDS = 5;
    //Base number of cards played each round
    public static final int BASE_PLAY_CARDS = 3;
    //Max number of players for a map
    public static final int MAX_PLAYERS = 4;
    //Horizontal scale factor for buttons
    public static final float SCALE_WIDTH = Gdx.graphics.getWidth()/2;
    //Vertical scale factor for buttons
    public static final float SCALE_HEIGHT = Gdx.graphics.getHeight()/15;

    /**
     * Text scale factor for android devices
     * @return scale factor
     */
    public static float GET_TEXT_SCALE(){
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            return Gdx.graphics.getHeight()/Gdx.graphics.getWidth()*1.5f;
        }
        return 1;
    }

}
