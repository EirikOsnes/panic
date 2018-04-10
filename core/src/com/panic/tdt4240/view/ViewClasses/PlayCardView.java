package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.view.TextureClasses.HandTexture;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;


/**
 * Created by victor on 05.03.2018.
 */

public class PlayCardView extends AbstractView{

    Renderer renderer;
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
    private ArrayList<AsteroidConnection> connections;
    private ShapeRenderer sr;

    //TODO Render the map, add clicklisteners on each asteroid and vehicle
    public PlayCardView(final PlayCardState state){
        super(state);
        map = state.map;
        renderer = Renderer.getInstance();
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        //background = new Texture("misc/background.png");
        //cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        amountCards = state.player.getHand().size();
        cardButtons = new ArrayList<>(amountCards);
        stage = new Stage();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        table.setWidth(Gdx.graphics.getWidth());
        table.left().bottom();
        BitmapFont font = new BitmapFont();
        skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("cards/card_textures.atlas");
        skin.addRegions(buttonAtlas);
        buttonStyles = new ArrayList<>();
        //Create a button for each card
        for (int i = 0; i < amountCards; i++) {
            TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
            buttonStyle.font = font;
            //Images the button has in the normal up-position, and when it is pressed down
            buttonStyle.up = skin.getDrawable("attack_icon");
            buttonStyle.down = skin.getDrawable("attack_selected");

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
            table.add(cardButtons.get(index)).width(SCREEN_WIDTH/amountCards).height(Gdx.graphics.getHeight()/5);
        }

        //table.background(new TextureRegionDrawable(new TextureRegion(background)));
        table.pack();

        stage.addActor(table);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.fontColor = Color.WHITE;
        style.font = new BitmapFont();
        cardInfo = new TextArea("",style);

        cardInfo.setPosition(3*SCREEN_WIDTH/4, table.getHeight());
        cardInfo.setWidth(SCREEN_WIDTH/4);
        cardInfo.setHeight(SCREEN_HEIGHT/8);
        stage.addActor(cardInfo);

        setUpMap();
    }

    /**
     * Method for setting up the map with listeners on each asteroid and vehicle
     */
    //TODO Lagre alle vehicles i stage, legge til listeners ol,
    private void setUpMap(){
        final ArrayList<Asteroid> asteroids = map.getAsteroids();
        ArrayList<String> vehicles = new ArrayList<>();
        connections = new ArrayList<>();

        for (int i = 0; i < asteroids.size(); i++) {
            vehicles.addAll(asteroids.get(i).getVehicles());
            Texture texture = new Texture("asteroids/meteorBrown_big1.png");
            Image asteroid = new Image(texture);

            asteroid.setOrigin(texture.getWidth()/2, texture.getHeight()/2);
            asteroid.setPosition(
                    asteroids.get(i).getPosition().x *(Gdx.graphics.getWidth() - asteroid.getWidth()),
                    asteroids.get(i).getPosition().y *(Gdx.graphics.getHeight() - table.getHeight() - asteroid.getHeight()) + table.getHeight());

            stage.addActor(asteroid);

            final int index = i;
            asteroid.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    if(selectTarget){
                        System.out.println("Clicked asteroid:" + asteroids.get(index).getId());
                        state.handleInput(asteroids.get(index).getId());
                    }
                };
            });
            for(Asteroid neighbour: asteroids.get(i).getNeighbours()){
                if(notConnected(asteroids.get(i).getId(), neighbour.getId())){
                    AsteroidConnection connection = new AsteroidConnection(
                            new Vector2(asteroids.get(i).getPosition().x *(Gdx.graphics.getWidth() - asteroid.getWidth()) + asteroid.getWidth()/2,
                                    asteroids.get(i).getPosition().y *(Gdx.graphics.getHeight() - table.getHeight() - asteroid.getHeight()) + table.getHeight()
                                            + asteroid.getHeight()/2),
                            new Vector2(neighbour.getPosition().x *(Gdx.graphics.getWidth() - asteroid.getWidth()) + asteroid.getWidth()/2,
                                    neighbour.getPosition().y *(Gdx.graphics.getHeight() - table.getHeight() - asteroid.getHeight()) + table.getHeight()
                                            + asteroid.getHeight()/2),
                            asteroids.get(i).getId(), neighbour.getId());
                    connections.add(connection);
                }
            }
        }
    }
    private boolean notConnected(String startID, String endID){
        for(AsteroidConnection connection: connections){
            if(connection.startID.equals(endID) && connection.endID.equals(startID)){
                return false;
            }
            else if(connection.startID.equals(startID) && connection.endID.equals(endID)){
                return false;
            }
        }
        return true;
    }

    /**
     * Method to change visuals of the buttons depending on if they're pressed down or not
     * @param button ID of the button/card that has been pressed
     * @param checked New position for the card: 0: unchecked, 1: checked, -1: finished
     *      if it is pressed down:
     *                set the up-image to the card-pressed image
     *      else:
     *                set the up-image to the normal card-up image
     */
    public void clickedButton(Integer button, int checked){
        if(checked == -1){
            buttonStyles.get(button).up = skin.getDrawable("attack_trans");
        }
        else if(checked == 0){
            buttonStyles.get(button).up = skin.getDrawable("attack_icon");
        }
        else if(checked == 1){
            buttonStyles.get(button).up = skin.getDrawable("attack_selected");
        }
        cardButtons.get(button).setStyle(buttonStyles.get(button));
    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for(AsteroidConnection connection :connections){
            sr.rectLine(connection.start, connection.end, 5.0f);
        }
        sr.end();
        stage.draw();
    }

    public void dispose(){
        sr.dispose();
        stage.dispose();
        //background.dispose();
        renderer.dispose();
    }

    private class AsteroidConnection {
        private Vector2 start;
        private Vector2 end;
        private String startID;
        private String endID;
        private AsteroidConnection(Vector2 start, Vector2 end, String startID, String endID){
            this.start = start;
            this.startID = startID;
            this.end = end;
            this.endID = endID;
        }
    }

}
