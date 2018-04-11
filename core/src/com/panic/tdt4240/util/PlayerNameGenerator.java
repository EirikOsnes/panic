package com.panic.tdt4240.util;

/**
 * Created by victor on 07.04.2018.
 */

public class PlayerNameGenerator {

    public static String getName(){

        // add names here at your leisure
        String[] names = {"Yellow Submarine", "Black Knight",
                "Evil Doctor", "Generic green alien", "Babel Fish",
                "Pink Unicorn", "A bowl of petunias", "Teapot in space",
                "Doctor who?", "Generic name", "Generated name",
                "I'm so meta, even this acronym", "Raging neighbour",
        "Defamatory Outrage", "Notoriously Thick-headed", "People",
        "Assaulting Neighbours", "Interstellar Comets",
        "A... thing", "Oh hi Mark"};
        int choice = (int) (Math.random() * names.length);



        return names[choice];
    }


}
