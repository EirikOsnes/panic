package com.panic.tdt4240.util;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;


/**
 * Created by Hermann on 09.03.2018.
 */

public class GlobalConstants {

    //Base number of cards drawn each round
    public static final int BASE_DRAW_CARDS = 5;
    //Base number of cards played each round
    public static final int BASE_PLAY_CARDS = 3;
    //Max number of players for a map, might not be needed
    public static final int MAX_PLAYERS = 4;

    //public static final float TEXT_SCALE = Gdx.graphics.getHeight()/Gdx.graphics.getWidth()*1.5f;

    //Scaling factor for android devices
    public static float GET_TEXT_SCALE(){
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            return Gdx.graphics.getHeight()/Gdx.graphics.getWidth()*1.5f;
        }
        return 0;
    }

}
