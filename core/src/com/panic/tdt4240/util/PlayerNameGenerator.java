package com.panic.tdt4240.util;

import com.badlogic.gdx.math.MathUtils;

/**
 * Created by victor on 07.04.2018.
 */

public class PlayerNameGenerator {

    private static final String[] names =
            {"Yellow Submarine", "Black Knight", "Evil Doctor",
                    "Generic green alien", "Babel Fish", "Peak performance alpha male",
                    "Pink Unicorn", "A bowl of petunias", "Teapot in space",
                    "Doctor who?", "Generic name", "Generated name",
                    "I'm so meta, even this acronym", "Raging neighbour",
                    "Defamatory Outrage", "Notoriously Thick-headed", "People",
                    "Assaulting Neighbours", "Interstellar Comets",
                    "A... thing", "Oh hi Mark", "It", "Newton's Flaming Laser Sword",
                    "Rubber Ducky", "win32", "Thunder from Down Under",
                    "Sleepwalker", "Menace from Venice", "Runtime Exception"};

    public static int getCount(){return names.length; }

    public static String getName(int i){

        return names[i % names.length];
    }


}
