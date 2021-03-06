package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.Card;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.PlayCardState;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.util.MapMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by victor on 05.03.2018.
 * View for selecting cards and targets. Sets up the map with asteroids, vehicles and asteroid connections.
 * Sets up the player hand of cards, sends ids of clicked cards and targets to PlayCardState
 */
public class PlayCardView extends AbstractView{

    private Skin skin, dialogSkin;
    private BitmapFont font;
    private TextureAtlas textureAtlas, btnAtlas;
    private ShapeRenderer sr;

    private ArrayList<TextButton> cardButtons;
    private ArrayList<TextButton.TextButtonStyle> buttonStyles;
    private Table table;
    private TextButton cardInfo;
    private boolean selectTarget = false;
    private ArrayList<String[]> vehicleOnAsteroid;
    private TextField timer;
    private Label invalidTarget;
    private TextButton.TextButtonStyle buttonStyle;
    private float timeLeft;
    private boolean isLeaving = false;
    private int currentButton;

    public PlayCardView(PlayCardState playCardState){
        super(playCardState);
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        int amountCards = ((PlayCardState) state).getHandSize();
        cardButtons = new ArrayList<>(amountCards);
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table = new Table();

        table.setWidth(Gdx.graphics.getWidth());
        table.setHeight(Gdx.graphics.getHeight() / 5);
        table.left().bottom();
        font = new BitmapFont();
        float textScale = GlobalConstants.GET_TEXT_SCALE();

        font.getData().scale(textScale);
        textureAtlas = new TextureAtlas("cards/card_textures.atlas");
        skin = new Skin();
        skin.addRegions(textureAtlas);
        buttonStyles = new ArrayList<>();

        textureAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin.addRegions(textureAtlas);

        currentButton = -1;

        //Setup cardInfo button, it is removed when clicked
        final TextButton.TextButtonStyle cardInfoStyle = new TextButton.TextButtonStyle();
        cardInfoStyle.font = font;
        cardInfoStyle.fontColor = Color.BLACK;
        cardInfo = new TextButton("", cardInfoStyle);
        cardInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                cardInfo.remove();
            }
        });
        cardInfo.setPosition(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 4);
        cardInfo.setWidth(Gdx.graphics.getWidth() / 2);
        cardInfo.setHeight(Gdx.graphics.getHeight() / 2);

        //Create a button for each card
        for (int i = 0; i < amountCards; i++) {
            final TextButton.TextButtonStyle cardButtonStyle = new TextButton.TextButtonStyle();
            cardButtonStyle.font = font;
            //Images the button has in the normal up-position, and when it is pressed down
            final String cardType = ((PlayCardState) state).getCardType(i);
            cardButtonStyle.up = skin.getDrawable(cardType + "_icon");
            cardButtonStyle.down = skin.getDrawable(cardType + "_selected");

            buttonStyles.add(cardButtonStyle);

            TextButton button = new TextButton("", cardButtonStyle);
            cardButtons.add(i, button);

        //TODO Idea: Let every button have value 0 to 2, which keeps track of their state
            //ClickListener for each card. Sends the card index to PlayCardState, which changes the
            //visual look of the card to signify selection. The ClickListener sets up the cardInfo
            //button when it gets clicked
            final int index = i;
            cardButtons.get(index).addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    cardInfo.remove();
                    //Currentbutton == -1, a brand new card can be selected
                    //Currentbutton == index, the currently selected button should be deselected
                    //Currentbutton != index or -1, we want to deselect the previous button and select a new one

                    if(currentButton != index) {
                        //Checks if current button should be deselected
                        if(currentButton != -1){
                            state.handleInput(currentButton);
                        }
                        currentButton = index;

                        Card card = ((PlayCardState) state).getCard(index);
                        //Setup for the tooltip
                        String[] words = ((PlayCardState) state).getCardToolTip(index);
                        String tooltip = String.format("%s\n\n", card.getName());

                        int length = 0;
                        for (String string : words) {
                            length = length + string.length() + 1;
                            if (length > 20) {
                                length = 0;
                                tooltip = tooltip.concat(String.format("\n%s ", string));
                            } else {
                                tooltip = tooltip.concat(String.format("%s ", string));
                            }
                        }

                        tooltip = tooltip.concat(String.format("\n\nCan effect: %s", card.getAllowedTarget().name().toLowerCase()));
                        tooltip = tooltip.concat(String.format("\nCan be aimed at: %s", card.getTargetType().name().toLowerCase()));
                        tooltip = tooltip.concat(String.format(Locale.ENGLISH,"\nMin range: %d", card.getMinRange()));
                        tooltip = tooltip.concat(String.format(Locale.ENGLISH,"\nMax range: %d", card.getMaxRange()));
                        tooltip = tooltip.concat(String.format(Locale.ENGLISH,"\nPriority: %d", card.getPriority()));

                        cardInfo.getStyle().up = skin.getDrawable(((PlayCardState) state).getCardType(index));
                        cardInfo.setText(tooltip);
                        if(!((PlayCardState)state).isCardSelected(index)){
                            stage.addActor(cardInfo);
                        }
                    }
                    state.handleInput(index);
                }
            });
            table.add(cardButtons.get(index)).width(Gdx.graphics.getWidth()/amountCards).height(Gdx.graphics.getHeight()/5);
        }

        //table.pack();
        stage.addActor(table);

        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        buttonStyle.up = skin.getDrawable("button-up");
        buttonStyle.down = skin.getDrawable("button-down");
        final TextButton finishedButton = new TextButton("", buttonStyle);
        finishedButton.setWidth(Gdx.graphics.getWidth()/5);
        finishedButton.setHeight(Gdx.graphics.getWidth()/10);
        finishedButton.setPosition(4*Gdx.graphics.getWidth()/5, table.getHeight());

        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        dialogSkin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);

        //If the player is alive, the button finishes this turn and sends cards to server
        if(((PlayCardState)state).getPlayerAlive()){
            finishedButton.setText("Finish Turn");
            finishedButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!((PlayCardState) state).isLockedIn()) { //Only send the cards to the server if you have not already locked in.
                        finishedButton.setColor(Color.GRAY);
                        BitmapFont labelFont = new BitmapFont();
                        labelFont.getData().scale(1 + GlobalConstants.GET_TEXT_SCALE());
                        Label waitLabel = new Label("Waiting for other players",new Label.LabelStyle(labelFont, Color.GREEN));
                        waitLabel.setPosition((Gdx.graphics.getWidth() - waitLabel.getWidth())/2, Gdx.graphics.getHeight()/2 + finishedButton.getHeight());
                        stage.addActor(waitLabel);
                        ((PlayCardState) state).finishRound();
                    }
                }
            });
        }
        //If player is dead, next turn is set automatically and the button allows the player to
        //leave the game with a confirmation dialog
        else{
            final Dialog dialog = new Dialog("", dialogSkin, "dialog"){
                @Override
                protected void result(Object object) {
                    Boolean bool = (Boolean) object;
                    if(bool){
                        isLeaving = true;
                        ((PlayCardState)state).leaveGame();
                    }
                    else{
                        remove();
                    }
                }
            };
            Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
            dialog.text("Are you sure you want to leave?", labelStyle);
            dialog.button("Yes",true, buttonStyle);
            dialog.button("Cancel", false, buttonStyle);
            dialog.hide();

            finishedButton.setText("Leave");
            finishedButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if(!isLeaving){
                        stage.addActor(dialog);
                        dialog.show(stage);
                    }
                }
            });
        }
        stage.addActor(finishedButton);

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = font;
        style.fontColor = Color.WHITE;
        timer = new TextField(String.valueOf(timeLeft), style);
        timer.setPosition(0, Gdx.graphics.getHeight() - timer.getHeight());
        stage.addActor(timer);

        //Prepares the short error message the player sees when they target a wrong target
        invalidTarget = new ToastMessage("Default", new Label.LabelStyle(font, Color.RED));
        invalidTarget.setPosition(0, table.getHeight() + invalidTarget.getHeight());

        stage.addActor(invalidTarget);
        setUpMap();
    }

    public void resetCurrentButton(){
        currentButton = -1;
    }

    /**
     * Method for setting up the map with listeners on each asteroid and vehicle and sets up visual
     * connections between them
     */
    private void setUpMap(){
        final ArrayList<Asteroid> asteroids = ((PlayCardState) state).getMap().getAsteroids();
        vehicleOnAsteroid = new ArrayList<>();
        ArrayList<Vector2> asteroidPositions = new ArrayList<>();
        ArrayList<Vector2> asteroidDimensions = new ArrayList<>();

        textureAtlas = new TextureAtlas(Gdx.files.internal("cars/cars.atlas"));
        skin.addRegions(textureAtlas);
        //Sets up asteroids with clickListeners and connections between them.
        //Finds positions of vehicles for when vehicles are setup
        for (int i = 0; i < asteroids.size(); i++) {
            vehicleOnAsteroid.addAll(MapMethods.getVehiclesOnAsteroid(asteroids.get(i), i));

            Image asteroid = new Image(new Texture("asteroids/" + asteroids.get(i).getTexture() + ".png"));
            asteroid.setSize(Gdx.graphics.getWidth()/5, Gdx.graphics.getWidth()/5);
            asteroidDimensions.add(i, new Vector2(asteroid.getWidth(), asteroid.getHeight()));
            asteroid.setPosition(
                    //Image should be rendered inside the window and above the table
                    asteroids.get(i).getPosition().x *(Gdx.graphics.getWidth() - asteroid.getWidth()),
                    asteroids.get(i).getPosition().y *(Gdx.graphics.getHeight() - table.getHeight() - asteroid.getHeight()) + table.getHeight());
            asteroidPositions.add(i, new Vector2(asteroid.getX(), asteroid.getY()));

            stage.addActor(asteroid);

            //When an asteroid is clicked and a target can be selected, sends the id to PlayCardState
            final int index = i;
            asteroid.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    if(selectTarget){
                        System.out.println("Clicked asteroid:" + asteroids.get(index).getId());
                        state.handleInput(asteroids.get(index).getId());
                    }
                }
            });
            //Sets up connections between asteroids
            for(Asteroid neighbour: asteroids.get(i).getNeighbours()){
                ((PlayCardState) state).addConnection(asteroids.get(i), neighbour, asteroid.getWidth(), asteroid.getHeight(), table.getHeight());
            }
        }
        //Sets up vehicles with clickListeners
        for (int j = 0; j < vehicleOnAsteroid.size(); j++) {
            int asteroid = Integer.valueOf(vehicleOnAsteroid.get(j)[2]);
            String colorCar = ((PlayCardState) state).getColorCar(vehicleOnAsteroid.get(j)[0]);

            Vector2 asteroidPos = asteroidPositions.get(asteroid);

            final Image vehicle = new Image(skin.getDrawable(colorCar));
            Vector2 position = MapMethods.asteroidPositions(asteroidPos.x, asteroidPos.y,
                    asteroidDimensions.get(asteroid).x, asteroidDimensions.get(asteroid).y,
                    colorCar);
            vehicle.setPosition(position.x, position.y);
            vehicle.setSize(asteroidDimensions.get(asteroid).x/3, asteroidDimensions.get(asteroid).y/2);

            //If we can select a target, sends the id to PlayCardState. If not we show vehicleInfo popup
            final int vIndex = j;
            final Dialog vehicleInfo = createVehicleInfo(GameInstance.getInstance().getVehicleById(vehicleOnAsteroid.get(j)[0]));
            vehicle.addListener(new ClickListener(){
                public void clicked(InputEvent event, float x, float y){
                    if(selectTarget){
                        System.out.println("Clicked vehicle:" + vehicleOnAsteroid.get(vIndex)[0]);
                        state.handleInput(vehicleOnAsteroid.get(vIndex)[0].concat(vehicleOnAsteroid.get(vIndex)[1]));
                    }
                    else{
                        stage.addActor(vehicleInfo);
                        vehicleInfo.show(stage);
                    }
                }
            });
            stage.addActor(vehicle);
        }
        stage.addActor(setUpPlayerInfoTable());
    }

    private Table setUpPlayerInfoTable(){
        Vehicle playerVehicle = ((PlayCardState)state).getPlayerVehicle();
        float health = playerVehicle.getStatusHandler().getStatusResultant("health");
        float maxHealth = playerVehicle.getStatusHandler().getStatusBaseValue("health");

        Image player = new Image(skin.getDrawable(playerVehicle.getColorCar()));
        player.rotateBy(270);
        String hp = String.format(Locale.ENGLISH,"HP: %.1f/%.0f", health, maxHealth);
        Label label = new Label(hp,new Label.LabelStyle(font, Color.RED));
        float width = label.getWidth();

        Table playerTable = new Table();
        playerTable.setWidth(width);
        playerTable.setHeight(Gdx.graphics.getWidth()/20);
        playerTable.add(player).width(Gdx.graphics.getWidth()/20).height(Gdx.graphics.getWidth()/10).row();
        playerTable.add(label).width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/7).row();
        playerTable.pack();

        playerTable.setPosition(Gdx.graphics.getWidth() - width,Gdx.graphics.getHeight() - playerTable.getHeight()*2/3);

        return playerTable;
    }
    private Dialog createVehicleInfo(Vehicle vehicle){
        HashMap<String, Float> effectsMap =  vehicle.getStatusHandler().getAllResultants();
        String effects = "";
        for(String key : effectsMap.keySet()){
            effects = effects.concat(String.format(Locale.ENGLISH,"%s = %.1f\n",key, effectsMap.get(key)));
        }
        final Dialog vehicleInfo = new Dialog("Info", dialogSkin, "dialog");
        vehicleInfo.getTitleLabel().setFontScale(GlobalConstants.GET_TEXT_SCALE() + 1);
        BitmapFont infoFont  = new BitmapFont();
        infoFont.getData().scale(GlobalConstants.GET_TEXT_SCALE()*1.5f);
        Label.LabelStyle labelStyle = new Label.LabelStyle(infoFont, Color.WHITE);
        vehicleInfo.text(effects, labelStyle);

        vehicleInfo.button("Ok",false, buttonStyle);
        return vehicleInfo;
    }

    /**
     * Creates a short text onscreen saying the target is invalid
     * @param targetType vehicle or asteroid
     */
    public void showInvalidTarget(String targetType){
        String target = "You cannot target this " + targetType;
        invalidTarget.setText(target);
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

    public void setSelectTarget(boolean selectTarget) {
        this.selectTarget = selectTarget;
    }

    public void setTimeLeft(float timeLeft){
        this.timeLeft = timeLeft;
    }

    public void update(float dt){
        timeLeft -= dt;
        if(timeLeft < 0){
            timeLeft = 0;
        }
        timer.setText(String.format(Locale.ENGLISH,"%.1f", timeLeft));
        invalidTarget.act(dt);

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
        stage.act();
        stage.draw();
    }

    public void dispose(){
        stage.dispose();
        sr.dispose();
        font.dispose();
        skin.dispose();
        dialogSkin.dispose();
        textureAtlas.dispose();
        btnAtlas.dispose();
    }

    private class ToastMessage extends Label{
        private float duration;

        private ToastMessage(CharSequence text, LabelStyle style) {
            super(text, style);
            duration = 0;
            setVisible(false);
        }
        @Override
        public void setText(CharSequence newText) {
            duration = 2;
            setVisible(true);
            super.setText(newText);
        }
        @Override
        public void act(float delta) {
            if(duration > 0){
                duration-= delta;
            }
            else {
                setVisible(false);
            }
        }
    }
}
