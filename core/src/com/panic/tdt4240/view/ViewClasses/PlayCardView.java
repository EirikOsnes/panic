package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.panic.tdt4240.PanicGame;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.view.TextureClasses.HandTexture;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;


/**
 * Created by victor on 05.03.2018.
 */

public class PlayCardView extends AbstractView{

    Renderer renderer;
    private HandTexture hv;
    private ArrayList<TextButton> cardButtons;
    private ArrayList<TextButton.TextButtonStyle> buttonStyles;
    private Stage stage;
    private Table table;
    //private Texture background;
    public TextArea cardInfo;
    private int amountCards;
    private Skin skin;
    public boolean selectTarget = false;
    private Map map;

    //TODO Render the map, add clicklisteners on each asteroid and vehicle
    public PlayCardView(final PlayCardState state){
        super(state);
        map = state.map;
        renderer = Renderer.getInstance();
        //background = new Texture("misc/background.png");
        //cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        amountCards = state.player.getHand().size();
        cardButtons = new ArrayList<>(amountCards);
        stage = new Stage();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        setUpMap();
        table = new Table();
        table.setWidth(Gdx.graphics.getWidth());
        table.left().bottom();
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        skin.addRegions(buttonAtlas);
        buttonStyles = new ArrayList<>();

        //Create a button for each card
        for (int i = 0; i < amountCards; i++) {
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = font;
            //Images the button has in the normal up-position, and when it is pressed down
            buttonStyle.up = skin.getDrawable("button-up");
            buttonStyle.down = skin.getDrawable("button-down");

            buttonStyles.add(buttonStyle);

            TextButton button = new TextButton("", buttonStyle);
            cardButtons.add(i, button);

            final int index = i;
            cardButtons.get(index).addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    state.handleInput(index);
                }
            });
            table.add(cardButtons.get(index)).width(SCREEN_WIDTH/amountCards);
        }
        //table.background(new TextureRegionDrawable(new TextureRegion(background)));
        //table.pack();


        stage.addActor(table);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.WHITE;
        style.font = new BitmapFont();
        cardInfo = new TextArea("",style);

        cardInfo.setPosition(SCREEN_WIDTH/2, SCREEN_HEIGHT/10);
        cardInfo.setWidth(SCREEN_WIDTH/4);
        cardInfo.setHeight(SCREEN_HEIGHT/8);
        stage.addActor(cardInfo);
    }

    /**
     * Method for setting up the map with listeners on each asteroid and vehicle
     */
    //TODO Lagre alle asteroider og vehicles i stage, legge til listeners ol, linjer mellom tilknyttede asteroider
    private void setUpMap(){
        final ArrayList<Asteroid> asteroids = map.getAsteroids();
        ArrayList<String> vehicles = new ArrayList<>();

        for (int i = 0; i < asteroids.size(); i++) {
            vehicles.addAll(asteroids.get(i).getVehicles());
            Sprite sprite = new Sprite(new Texture("asteroids/meteorBrown_big1.png"));
            AsteroidActor asteroid = new AsteroidActor(sprite);
            asteroid.setOrigin(sprite.getWidth()/2, sprite.getHeight()/2);
            asteroid.setPosition(
                    asteroids.get(i).getPosition().x * Gdx.graphics.getWidth() - asteroid.getOriginX(),
                    asteroids.get(i).getPosition().y * Gdx.graphics.getHeight() - asteroid.getOriginY());

            System.out.println("X: " + asteroids.get(i).getPosition().x + " Y: " + asteroids.get(i).getPosition().y);

            //System.out.println("X: " + asteroid.getX() + " Y:" + asteroid.getY());

            final int index = i;
            asteroid.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    state.handleInput(asteroids.get(index).getId());
                }
            });

            stage.addActor(asteroid);
        }
    }

    /**
     * Method to change visuals of the buttons depending on if they're pressed down or not
     * @param button ID of the button/card that has been pressed
     * @param checked Whether the button is pressed down or unpressed
     *      if it is pressed down:
     *                set the up-image to the card-pressed image
     *      else:
     *                set the up-image to the normal card-up image
     */
    public void clickedButton(Integer button, boolean checked){
        if(checked){
            buttonStyles.get(button).up = skin.getDrawable("button-gone");
        }
        else{
            buttonStyles.get(button).up = skin.getDrawable("button-up");
        }
        cardButtons.get(button).setStyle(buttonStyles.get(button));
    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        stage.draw();
        /*
        renderBackground();
        renderMap();
        renderVehicles();
        */
    }

    public void dispose(){
        stage.dispose();
        //background.dispose();
        renderer.dispose();
    }

    private class AsteroidActor extends Actor{
        private Sprite asteroid;
        private AsteroidActor(Sprite asteroid){
            this.asteroid = asteroid;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            asteroid.draw(batch);
        }
    }

}
