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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Map;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.view.Renderer;

import java.util.ArrayList;


/**
 * Created by victor on 05.03.2018.
 */

public class PlayCardView extends AbstractView{

    private Renderer renderer;
    private ArrayList<TextButton> cardButtons;
    private ArrayList<TextButton.TextButtonStyle> buttonStyles;
    private Stage stage;
    private Table table;
    //private Texture background;
    private TextButton cardInfo;
    private int amountCards;
    private Skin skin;
    private boolean selectTarget = false;
    private Map map;
    private ShapeRenderer sr;
    private TextButton finishedButton;
    private ArrayList<String[]> vehicleOnAsteroid;
    private GameInstance gameInstance;
//    private ArrayList<Boolean> removeDialog;

    public PlayCardView(PlayCardState playCardState){
        super(playCardState);
        gameInstance = GameInstance.getInstance();
        map = gameInstance.getMap();
        renderer = Renderer.getInstance();
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        //background = new Texture("misc/background.png");
        //cam.setToOrtho(false, PanicGame.WIDTH,PanicGame.HEIGHT);
        amountCards = gameInstance.getPlayer().getHand().size();
        cardButtons = new ArrayList<>(amountCards);
//        removeDialog = new ArrayList<>(amountCards);
        stage = new Stage();
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
        table = new Table();

        table.setWidth(Gdx.graphics.getWidth());
        table.left().bottom();
        final BitmapFont font = new BitmapFont();
        skin = new Skin();
        TextureAtlas buttonAtlas = new TextureAtlas("cards/card_textures.atlas");
        skin.addRegions(buttonAtlas);
        buttonStyles = new ArrayList<>();

        Window.WindowStyle ws =  new Window.WindowStyle();
        ws.titleFont = font;
        skin.add("dialog", ws);

        buttonAtlas = new TextureAtlas("start_menu_buttons/button.atlas");
        Skin finishedSkin = new Skin();
        finishedSkin.addRegions(buttonAtlas);
        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        //Create a button for each card
        for (int i = 0; i < amountCards; i++) {
//            removeDialog.add(false);
            final TextButton.TextButtonStyle cardButtonStyle = new TextButton.TextButtonStyle();
            cardButtonStyle.font = font;
            //Images the button has in the normal up-position, and when it is pressed down
            final String cardType = ((PlayCardState) state).getCardType(i);
            cardButtonStyle.up = skin.getDrawable(cardType + "_icon");
            cardButtonStyle.down = skin.getDrawable(cardType + "_selected");

            buttonStyles.add(cardButtonStyle);

            TextButton button = new TextButton("", cardButtonStyle);
            cardButtons.add(i, button);

            final int index = i;
            cardButtons.get(index).addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if(!selectTarget){
                        if(!(cardInfo == null)){
                            cardInfo.remove();
                        }
                        Card card = ((PlayCardState) state).getCard(index);
                        TextButton.TextButtonStyle cardInfoStyle = new TextButton.TextButtonStyle();
                        cardInfoStyle.font = font;
                        cardInfoStyle.fontColor = Color.BLACK;
                        cardInfoStyle.up = skin.getDrawable(((PlayCardState) state).getCardType(index));

                        String tooltip = "";
                        String[] words = card.getTooltip().split(" ");
                        int length = 0;
                        for(String string : words){
                            length = length + string.length() + 1;
                            if(length > 20){
                                length = 0;
                                tooltip = tooltip.concat("\n" + string + " ");
                            }
                            else{
                                tooltip = tooltip.concat(string + " ");
                            }
                        }
                        cardInfo = new TextButton(tooltip, cardInfoStyle);
                        cardInfo.addListener(new ChangeListener() {
                            @Override
                            public void changed(ChangeEvent event, Actor actor) {
//                                removeDialog.set(index, false);
                                cardInfo.remove();
                            }});
                        cardInfo.setPosition(SCREEN_WIDTH/4, SCREEN_HEIGHT/4);
                        cardInfo.setWidth(SCREEN_WIDTH/2);
                        cardInfo.setHeight(SCREEN_HEIGHT/2);
                        stage.addActor(cardInfo);
//                        removeDialog.set(index, true);
                    }
                    else{
//                        removeDialog.set(index, false);
                        cardInfo.remove();
                    }
                    state.handleInput(index);
                    System.out.println(selectTarget);
                }
            });
            table.add(cardButtons.get(index)).width(SCREEN_WIDTH/amountCards).height(Gdx.graphics.getHeight()/5);
        }

        table.pack();
        stage.addActor(table);

        //Images the button has in the normal up-position, and when it is pressed down
        buttonStyle.up = finishedSkin.getDrawable("button_up");
        buttonStyle.down = finishedSkin.getDrawable("button_down");
        finishedButton = new TextButton("Finish Turn", buttonStyle);
        finishedButton.setWidth(SCREEN_WIDTH/5);
        finishedButton.setHeight(SCREEN_WIDTH/10);
        finishedButton.setPosition(4*SCREEN_WIDTH/5, table.getHeight());
        finishedButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ((PlayCardState) state).finishRound();
            }
        });

        stage.addActor(finishedButton);
        setUpMap();
    }

    /**
     * Method for setting up the map with listeners on each asteroid and vehicle
     */
    private void setUpMap(){
        final ArrayList<Asteroid> asteroids = map.getAsteroids();
        vehicleOnAsteroid = new ArrayList<>();
        ArrayList<Vector2> asteroidPositions = new ArrayList<>();
        ArrayList<Vector2> asteroidDimensions = new ArrayList<>();

        TextureAtlas carsAtlas = new TextureAtlas(Gdx.files.internal("cars/cars.atlas"));
        Skin skin = new Skin(carsAtlas);
        for (int i = 0; i < asteroids.size(); i++) {
            for(int j = 0; j < asteroids.get(i).getVehicles().size(); j++){
                String[] onAsteroid = new String[3];
                onAsteroid[0] = asteroids.get(i).getVehicles().get(j);
                onAsteroid[1] = asteroids.get(i).getId();
                onAsteroid[2] = i + "";
                vehicleOnAsteroid.add(onAsteroid);
            }
            Texture texture = new Texture("asteroids/meteorBrown_big1.png");
            Image asteroid = new Image(texture);
            asteroid.setSize(SCREEN_WIDTH/5, SCREEN_WIDTH/5);
            asteroidDimensions.add(i, new Vector2(asteroid.getWidth(), asteroid.getHeight()));
            asteroid.setPosition(
                    //Image should be rendered inside the window and above the table
                    asteroids.get(i).getPosition().x *(Gdx.graphics.getWidth() - asteroid.getWidth()),
                    asteroids.get(i).getPosition().y *(Gdx.graphics.getHeight() - table.getHeight() - asteroid.getHeight()) + table.getHeight());
            asteroidPositions.add(i, new Vector2(asteroid.getX(), asteroid.getY()));

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
                ((PlayCardState) state).addConnection(asteroids.get(i), neighbour, asteroid.getWidth(), asteroid.getHeight(), table.getHeight());
            }
        }
        for (int j = 0; j < vehicleOnAsteroid.size(); j++) {
            int asteroid = Integer.valueOf(vehicleOnAsteroid.get(j)[2]);
            Vehicle activeVehicle = gameInstance.getVehicleById(vehicleOnAsteroid.get(j)[0]);
            Vector2 asteroidPos = asteroidPositions.get(asteroid);

            Image vehicle = new Image(skin.getDrawable(activeVehicle.getColorCar()));
            Vector2 position = ((PlayCardState) state).AsteroidPositions(asteroidPos.x, asteroidPos.y,
                    asteroidDimensions.get(asteroid).x, asteroidDimensions.get(asteroid).y,
                    activeVehicle.getColorCar());
            vehicle.setPosition(position.x, position.y);
            vehicle.setSize(asteroidDimensions.get(asteroid).x/3, asteroidDimensions.get(asteroid).y*2/3);
            final int vIndex = j;
            vehicle.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    if(selectTarget){
                        System.out.println("Clicked vehicle:" + vehicleOnAsteroid.get(vIndex)[0]);
                        state.handleInput(vehicleOnAsteroid.get(vIndex)[0].concat(vehicleOnAsteroid.get(vIndex)[1]));
                    }
                };
            });
            stage.addActor(vehicle);
        }
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
        String cardType = ((PlayCardState) state).getCardType(button);
        if(checked == -1){
            buttonStyles.get(button).up = skin.getDrawable(cardType + "_trans");
        }
        else if(checked == 0){
            buttonStyles.get(button).up = skin.getDrawable(cardType + "_icon");
        }
        else if(checked == 1){
            buttonStyles.get(button).up = skin.getDrawable(cardType + "_selected");
        }
        cardButtons.get(button).setStyle(buttonStyles.get(button));
    }

    public boolean isSelectTarget() {
        return selectTarget;
    }

    public void setSelectTarget(boolean selectTarget) {
        this.selectTarget = selectTarget;
    }
    public void setCardInfoText(String text){
        //cardInfo.setText(text);
    }

    public void render(){
        renderer.sb.setProjectionMatrix(cam.combined);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Vector2[]> lines = ((PlayCardState) state).getConnections();
        for(Vector2[] points : lines){
            sr.rectLine(points[0], points[1], 5.0f);
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
//TODO Større tekst

}
