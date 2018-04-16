package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.util.MapMethods;

import java.util.ArrayList;

/**
 * Created by victor on 05.03.2018.
 * View for selecting cards and targets
 */
public class PlayCardView extends AbstractView{

    private ArrayList<TextButton> cardButtons;
    private ArrayList<TextButton.TextButtonStyle> buttonStyles;
    private Table table;
    //private Texture background;
    private TextButton cardInfo;
    private Skin skin;
    private boolean selectTarget = false;
    private ShapeRenderer sr;
    private ArrayList<String[]> vehicleOnAsteroid;
    private TextField timer;
    private Label invalidTarget;
    private BitmapFont font;
    private TextureAtlas textureAtlas;
    private ArrayList<Boolean> checked;


    //TODO Make the player vehicle more visible, inform if the targeting is wrong
    public PlayCardView(PlayCardState playCardState){
        super(playCardState);
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        int amountCards = ((PlayCardState) state).getHandSize();
        checked = new ArrayList<>();
        cardButtons = new ArrayList<>(amountCards);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table = new Table();

        table.setWidth(Gdx.graphics.getWidth());
        table.left().bottom();
        font = new BitmapFont();
        float textScale = 0;
        if(Gdx.app.getType() == Application.ApplicationType.Android){
            textScale = GlobalConstants.TEXT_SCALE;
        }
        font.getData().scale(textScale);
        skin = new Skin();
        textureAtlas = new TextureAtlas("cards/card_textures.atlas");
        skin.addRegions(textureAtlas);
        buttonStyles = new ArrayList<>();

        textureAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin.addRegions(textureAtlas);

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        //Create a button for each card
        for (int i = 0; i < amountCards; i++) {
            checked.add(false);
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
            cardButtons.get(index).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    if(!selectTarget){
                        //Sets up cardInfo, the tooltip for the card
                        if(!(cardInfo == null)){
                            cardInfo.remove();
                        }
                        TextButton.TextButtonStyle cardInfoStyle = new TextButton.TextButtonStyle();
                        cardInfoStyle.font = font;
                        cardInfoStyle.fontColor = Color.BLACK;
                        cardInfoStyle.up = skin.getDrawable(((PlayCardState) state).getCardType(index));

                        String[] words = ((PlayCardState) state).getCardToolTip(index);
                        String tooltip = ((PlayCardState) state).getCardName(index) + "\n\n";
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
                        String allowedTarget = ((PlayCardState)state).getAllowedTarget(index);
                        String targetType = ((PlayCardState)state).getTargetType(index);
                        tooltip = tooltip.concat("\n\nCan effect: " + allowedTarget);
                        tooltip = tooltip.concat("\nCan be aimed at: " + targetType);
                        if(!checked.get(index)) {
                            cardInfo = new TextButton(tooltip, cardInfoStyle);
                            cardInfo.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    cardInfo.remove();
                                }
                            });
                            cardInfo.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
                            cardInfo.setWidth(Gdx.graphics.getWidth() / 2);
                            cardInfo.setHeight(Gdx.graphics.getHeight() / 2);
                            stage.addActor(cardInfo);
                        }
                        checked.set(index, !checked.get(index));
                    }
                    else{
                        cardInfo.remove();
                        checked.set(index, false);
                    }
                    state.handleInput(index);
                    System.out.println(selectTarget);
                }
            });
            table.add(cardButtons.get(index)).width(Gdx.graphics.getWidth()/amountCards).height(Gdx.graphics.getHeight()/5);
        }

        table.pack();
        stage.addActor(table);

        //Images the button has in the normal up-position, and when it is pressed down
        buttonStyle.up = skin.getDrawable("button-up");
        buttonStyle.down = skin.getDrawable("button-down");

        TextButton finishedButton = new TextButton("Finish Turn", buttonStyle);
        finishedButton.setWidth(Gdx.graphics.getWidth()/5);
        finishedButton.setHeight(Gdx.graphics.getWidth()/10);
        finishedButton.setPosition(4*Gdx.graphics.getWidth()/5, table.getHeight());
        finishedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                ((PlayCardState) state).finishRound();
            }

        });
        stage.addActor(finishedButton);
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        timer = new TextField(timeLeft + "", style);
        timer.setPosition(0, Gdx.graphics.getHeight() - timer.getHeight());
        stage.addActor(timer);

        invalidTarget = new Label("Default", new Label.LabelStyle(font, Color.RED));
        invalidTarget.setPosition(0, table.getHeight() + invalidTarget.getHeight());
        stage.addActor(invalidTarget);
        invalidTarget.setVisible(false);

        setUpMap();
    }
    /**
     * Method for setting up the map with listeners on each asteroid and vehicle
     */
    private void setUpMap(){
        final ArrayList<Asteroid> asteroids = ((PlayCardState) state).getMap().getAsteroids();
        vehicleOnAsteroid = new ArrayList<>();
        ArrayList<Vector2> asteroidPositions = new ArrayList<>();
        ArrayList<Vector2> asteroidDimensions = new ArrayList<>();

        textureAtlas = new TextureAtlas(Gdx.files.internal("cars/cars.atlas"));
        skin.addRegions(textureAtlas);
        for (int i = 0; i < asteroids.size(); i++) {
            for(int j = 0; j < asteroids.get(i).getVehicles().size(); j++){
                String[] onAsteroid = new String[3];
                onAsteroid[0] = asteroids.get(i).getVehicles().get(j);
                onAsteroid[1] = asteroids.get(i).getId();
                onAsteroid[2] = i + "";
                vehicleOnAsteroid.add(onAsteroid);
            }
            Image asteroid = new Image(new Texture("asteroids/" + asteroids.get(i).getTexture() + ".png"));
            asteroid.setSize(Gdx.graphics.getWidth()/5, Gdx.graphics.getWidth()/5);
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
                }
            });
            for(Asteroid neighbour: asteroids.get(i).getNeighbours()){
                ((PlayCardState) state).addConnection(asteroids.get(i), neighbour, asteroid.getWidth(), asteroid.getHeight(), table.getHeight());
            }
        }
        for (int j = 0; j < vehicleOnAsteroid.size(); j++) {
            int asteroid = Integer.valueOf(vehicleOnAsteroid.get(j)[2]);
            String colorCar = ((PlayCardState) state).getColorCar(vehicleOnAsteroid.get(j)[0]);
            Vector2 asteroidPos = asteroidPositions.get(asteroid);

            Image vehicle = new Image(skin.getDrawable(colorCar));
            Vector2 position = MapMethods.asteroidPositions(asteroidPos.x, asteroidPos.y,
                    asteroidDimensions.get(asteroid).x, asteroidDimensions.get(asteroid).y,
                    colorCar);
            vehicle.setPosition(position.x, position.y);
            vehicle.setSize(asteroidDimensions.get(asteroid).x/3, asteroidDimensions.get(asteroid).y*2/3);
            final int vIndex = j;
            vehicle.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    if(selectTarget){
                        System.out.println("Clicked vehicle:" + vehicleOnAsteroid.get(vIndex)[0]);
                        state.handleInput(vehicleOnAsteroid.get(vIndex)[0].concat(vehicleOnAsteroid.get(vIndex)[1]));
                    }
                }
            });
            stage.addActor(vehicle);
        }

    }
    //TODO Should show: cannot target this asteroid/this vehicle
    public void showInvalidTarget(String targetID){
        String target = "You cannot target this ";
        if(targetID.substring(0,1).equals("a")){
            target = target.concat("asteroid");
        }
        else{
            target = target.concat("vehicle");
        }
        invalidTarget.setText(target);
        stage.addActor(invalidTarget);
    }

    /**
     * Creates a short text onscreen saying the target is invalid
     * @param targetType vehicle or asteroid
     */
    public void showInvalidTarget(String targetType){
        String target = "You cannot target this " + targetType;
        invalidTarget.setText(target);
        invalidTarget.setVisible(true);
        duration = 2;
    }
    private float duration = 0;
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

    public void setSelectTarget(boolean selectTarget) {
        this.selectTarget = selectTarget;
    }

    private float timeLeft;
    public void setTimeLeft(float timeLeft){
        this.timeLeft = timeLeft;
    }

    public void update(float dt){
        timeLeft -= dt;
        timer.setText(Math.round(timeLeft) + "");
        if(duration > 0){
            duration -= dt;
        }
        else if(invalidTarget.isVisible()){
            invalidTarget.setVisible(false);
        }
    }
    /**
     * Renders connections between asteroids, then the stage
     */
    public void render(){
        sr.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Vector2[]> lines = ((PlayCardState) state).getConnections();
        for(Vector2[] points : lines){
            sr.rectLine(points[0], points[1], 5.0f);
        }
        sr.end();
        stage.draw();
    }
    public void dispose(){
        stage.dispose();
        font.dispose();
        skin.dispose();
        textureAtlas.dispose();
    }
}
