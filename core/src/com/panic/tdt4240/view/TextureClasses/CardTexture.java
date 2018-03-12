package com.panic.tdt4240.view.TextureClasses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.models.Card;

import java.nio.file.Path;

/**
 * Created by victor on 09.03.2018.
 */

public class CardTexture {

//    private Texture img;
    private Texture img;

    public CardTexture(Card c){
        if (!loadImg(c)) throw new RuntimeException("Card texture loading failed." +
                "\n Check parameters or assets.");
    }
    public CardTexture(String cardName){
        if (!loadImg(cardName)) throw new RuntimeException("Card texture loading failed." +
                "\n Check parameters or assets.");
    }

/*    public void render(SpriteBatch sb, int x, int y){
        sb.draw(img, x, y, img.getWidth(), img.getHeight());
    }
/**/

    public Texture getTexture(){
        return img;
    }

    private boolean loadImg(String cardName){
        try{
            img = new Texture(cardName + ".png");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private boolean loadImg(Card c){
        try{
            img = new Texture(c.getName() + ".png");
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

/*    public static CardTexture valueOf(String cardName){
        return new CardTexture(cardName);
    }

    public static CardTexture valueOf(Card c){
        return new CardTexture(c);
    }
    /**/

}
