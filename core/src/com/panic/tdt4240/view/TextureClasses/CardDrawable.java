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

public class CardDrawable {

    private Drawable img;
    private float width;
    private float height;

    public CardDrawable(Card c){
        if (!loadImg(c)) throw new RuntimeException("Card texture loading failed." +
                "\n Check parameters or assets.");
    }
    public CardDrawable(String cardName){
        if (!loadImg(cardName)) throw new RuntimeException("Card texture loading failed." +
                "\n Check parameters or assets.");
    }

/*    public void render(SpriteBatch sb, int x, int y){
        sb.draw(img, x, y, img.getWidth(), img.getHeight());
    }
/**/

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Drawable getDrawable(){
        return img;
    }

    private boolean loadImg(String cardName){
        try{
            Texture texture = new Texture(cardName + ".xml");
            width = texture.getWidth();
            height = texture.getHeight();
            img = new TextureRegionDrawable(new TextureRegion(texture));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    private boolean loadImg(Card c){
        try{
            Texture texture = new Texture(c.getName() + ".xml");
            width = texture.getWidth();
            height = texture.getHeight();
            img = new TextureRegionDrawable(new TextureRegion(texture));
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

/*    public static CardDrawable valueOf(String cardName){
        return new CardDrawable(cardName);
    }

    public static CardDrawable valueOf(Card c){
        return new CardDrawable(c);
    }
    /**/

}
