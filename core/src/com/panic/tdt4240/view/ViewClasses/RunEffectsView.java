package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.RunEffectsState;
import com.panic.tdt4240.states.State;
import com.panic.tdt4240.util.GlobalConstants;
import com.panic.tdt4240.util.MapConnections;
import com.panic.tdt4240.util.MapMethods;
import com.panic.tdt4240.view.animations.CloudAnimation;
import com.panic.tdt4240.view.animations.CloudAnimation.AnimationType;
import com.panic.tdt4240.view.animations.Missile;
import com.panic.tdt4240.view.animations.Missile.MissileType;
import com.panic.tdt4240.view.animations.MissileAction;
import com.panic.tdt4240.view.animations.MoveVehicleAction;
import com.panic.tdt4240.view.animations.ShieldAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;

/**
 * Created by Hermann on 14.04.2018.
 */

public class RunEffectsView extends AbstractView {

    private GameInstance gameInstance;
    private ShapeRenderer sr;
    private HashMap<String, Image> vehicleImages;
    private HashMap<String, Image> asteroidImages;
    private AnimationAdapter animator;
    private MapConnections mapConnections;
    private final CloudAnimation explosion, poison, shield, healing, debuff;
    private BitmapFont font;
    private Skin skin;
    private boolean isLeaving = false;
    private TextureAtlas btnAtlas;
    private final Missile redMissile, greenMissile, cyanMissile, yellowMissile;
    private Label hpLabel;
    private float health;
    private float maxHealth;

    public RunEffectsView(State state) {
        super(state);
        sr = new ShapeRenderer();
        sr.setColor(1, 1, 1, 0);
        sr.setAutoShapeType(true);
        gameInstance = GameInstance.getInstance();
        font = new BitmapFont();
        font.getData().scale(GlobalConstants.GET_TEXT_SCALE());

        animator = new AnimationAdapter();
        poison = new CloudAnimation(AnimationType.GREENCLOUD);
        explosion = new CloudAnimation(AnimationType.EXPLOSION);
        redMissile = new Missile(MissileType.RED);
        greenMissile = new Missile(MissileType.GREEN);
        cyanMissile = new Missile(MissileType.CYAN);
        yellowMissile = new Missile(MissileType.YELLOW);
        shield = new ShieldAnimation();
        healing = new CloudAnimation(AnimationType.HEALING, 0.05f);
        debuff = new CloudAnimation(AnimationType.DEBUFF, 0.05f);
        setUpMap();
        btnAtlas = new TextureAtlas("skins/uiskin.atlas");
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"), btnAtlas);
        if(!((RunEffectsState)state).getPlayerAlive()){
            setUpLeaveButton();
        }
    }

    //TODO: Call this method when a player dies to let them leave the game
    public void setUpLeaveButton(){

        final TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("button-up");
        buttonStyle.down = skin.getDrawable("button-down");
        TextButton finishedButton = new TextButton("", buttonStyle);
        finishedButton.setWidth(Gdx.graphics.getWidth()/5);
        finishedButton.setHeight(Gdx.graphics.getWidth()/10);
        finishedButton.setPosition(4*Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);

        TextButton.TextButtonStyle ButtonStyle = new TextButton.TextButtonStyle();
        ButtonStyle.font = font;
        ButtonStyle.up = skin.getDrawable("button-up");
        ButtonStyle.down = skin.getDrawable("button-down");
        final FinishDialog dialog = new FinishDialog("Leave", skin, "dialog");
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        dialog.text("Are you sure you want to leave?", labelStyle);
        dialog.button("Yes",true, ButtonStyle);
        dialog.button("Cancel", false, ButtonStyle);

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
        stage.addActor(finishedButton);
    }

    private class FinishDialog extends Dialog {
        private FinishDialog(String title, Skin skin, String windowStyleName) {
            super(title, skin, windowStyleName);
        }
        @Override
        protected void result(Object object) {
            Boolean bool = (Boolean) object;
            if(bool){
                isLeaving = true;
                ((RunEffectsState)state).leaveGame();
            }
            else{
                remove();
            }
        }
    }

    private void setUpMap() {
        mapConnections = new MapConnections(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        asteroidImages = new HashMap<>();
        vehicleImages = new HashMap<>();
        mapConnections = new MapConnections(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        ArrayList<Asteroid> asteroids = gameInstance.getMap().getAsteroids();

        ArrayList<String[]> vehicleOnAsteroid = new ArrayList<>();
        ArrayList<Vector2> asteroidPositions = new ArrayList<>();
        ArrayList<Vector2> asteroidDimensions = new ArrayList<>();
        float table = Gdx.graphics.getHeight() / 5;
        TextureAtlas carsAtlas = new TextureAtlas(Gdx.files.internal("cars/cars.atlas"));
        Skin skin = new Skin(carsAtlas);
        for (int i = 0; i < asteroids.size(); i++) {
            for (int j = 0; j < asteroids.get(i).getVehicles().size(); j++) {
                String[] onAsteroid = new String[3];
                onAsteroid[0] = asteroids.get(i).getVehicles().get(j);
                onAsteroid[1] = asteroids.get(i).getId();
                onAsteroid[2] = i + "";
                vehicleOnAsteroid.add(onAsteroid);
            }
            Texture texture = new Texture("asteroids/" + asteroids.get(i).getTexture() + ".png");
            Image asteroid = new Image(texture);
            asteroid.setSize(SCREEN_WIDTH / 5, SCREEN_WIDTH / 5);
            asteroid.setOrigin(Align.center);
            asteroidDimensions.add(i, new Vector2(asteroid.getWidth(), asteroid.getHeight()));
            asteroid.setPosition(
                    //Image should be rendered inside the window and above the table
                    asteroids.get(i).getPosition().x * (Gdx.graphics.getWidth() - asteroid.getWidth()),
                    asteroids.get(i).getPosition().y * (Gdx.graphics.getHeight() - table - asteroid.getHeight()) + table);
            asteroidPositions.add(i, new Vector2(asteroid.getX(), asteroid.getY()));
            asteroidImages.put(asteroids.get(i).getId(), asteroid);
            stage.addActor(asteroid);

            for (Asteroid neighbour : asteroids.get(i).getNeighbours()) {
                mapConnections.addConnection(asteroids.get(i), neighbour, asteroid.getWidth(), asteroid.getHeight(), table);
            }
        }
        stage.addActor(redMissile);
        stage.addActor(greenMissile);
        for (int j = 0; j < vehicleOnAsteroid.size(); j++) {
            int asteroid = Integer.valueOf(vehicleOnAsteroid.get(j)[2]);
            Vehicle activeVehicle = gameInstance.getVehicleById(vehicleOnAsteroid.get(j)[0]);
            Vector2 asteroidPos = asteroidPositions.get(asteroid);

            Image vehicle = new Image(skin.getDrawable(activeVehicle.getColorCar()));
            Vector2 position = MapMethods.asteroidPositions(asteroidPos.x, asteroidPos.y,
                    asteroidDimensions.get(asteroid).x, asteroidDimensions.get(asteroid).y,
                    activeVehicle.getColorCar());
            vehicle.setPosition(position.x, position.y);
            vehicle.setSize(asteroidDimensions.get(asteroid).x/3, asteroidDimensions.get(asteroid).y/2);
            vehicleImages.put(activeVehicle.getVehicleID(), vehicle);
            vehicle.setOrigin(Align.center);
            stage.addActor(vehicle);
        }
        Table playerTable = new Table();
        playerTable.setWidth(Gdx.graphics.getWidth()/10);
        playerTable.setHeight(Gdx.graphics.getWidth()/20);
        Vehicle playerVehicle = ((RunEffectsState)state).getPlayerVehicle();
        health = playerVehicle.getStatusHandler().getStatusResultant("health");
        maxHealth = playerVehicle.getStatusHandler().getStatusBaseValue("health");

        Image player = new Image(skin.getDrawable(playerVehicle.getColorCar()));
        player.rotateBy(270);

        String hp = String.format(Locale.ENGLISH,"HP: %.1f/%.0f", health, maxHealth);
        hpLabel = new Label(hp,new Label.LabelStyle(font, Color.RED));
        float width = hpLabel.getWidth();
        playerTable.add(player).width(Gdx.graphics.getWidth()/20).height(Gdx.graphics.getWidth()/10).row();
        playerTable.add(hpLabel).width(Gdx.graphics.getWidth()/10).height(Gdx.graphics.getWidth()/7).row();
        playerTable.pack();
        playerTable.setPosition(Gdx.graphics.getWidth() - width,Gdx.graphics.getHeight() - playerTable.getHeight()*2/3);

        stage.addActor(playerTable);
        stage.addActor(explosion);
        stage.addActor(poison);
        stage.addActor(shield);
        stage.addActor(healing);
        stage.addActor(debuff);
    }

    private void updateHealth(){
        //TODO Call this method when an animation is finished
        health = ((RunEffectsState)state).getPlayerVehicle().getStatusHandler().getStatusResultant("health");
        String hp = String.format(Locale.ENGLISH,"HP: %.1f/%.0f", health, maxHealth);
        hpLabel.setText(hp);
    }

    @Override
    public void render() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Vector2[]> lines = mapConnections.getConnections();
        for(Vector2[] points : lines) {
            sr.rectLine(points[0], points[1], 5.0f);
        }
        sr.end();

        stage.act();
        stage.draw();
    }

    public void moveVehicle(String vehicleID, String asteroidID) {
        Actor actor = vehicleImages.get(vehicleID);
        Image asteroid = asteroidImages.get(asteroidID);
        Vector2 vec = MapMethods.asteroidPositions(asteroid.getX(), asteroid.getY(), asteroid.getWidth(),
                asteroid.getHeight(), gameInstance.getVehicleById(vehicleID).getColorCar());
        Action action = new MoveVehicleAction(vec.x, vec.y);
        animator.addAction(action, actor);
    }

    public void addMissileAnimation(String targetID, String instigatorID, MissileType mt) {
        final Actor target, instigator;
        final Missile missile;

        // Find the target Actor
        target = getActorFromID(targetID);

        // Find the instigator Actor
        instigator = getActorFromID(instigatorID);

        //find the missile to use
        if (mt == MissileType.RED) {
            missile = redMissile;
        } else if (mt == MissileType.GREEN) {
            missile = greenMissile;
        } else {
            throw new IllegalArgumentException("Only red and green missiles implemented");
        }

        Runnable missileRunnable = new Runnable() {
            @Override
            public void run() {
                missile.startAnimation(instigator.getX(Align.center),instigator.getY(Align.center), target.getX(Align.center), target.getY(Align.center), Align.center);
            }
        };
        Action action = Actions.sequence(Actions.run(missileRunnable), new MissileAction(target, instigator));
        animator.addAction(action, missile);
    }

    public void addCloudAnimation(String targetID, AnimationType at) {
        final Actor target;
        final CloudAnimation cloud;

        target = getActorFromID(targetID);

        if (at == AnimationType.EXPLOSION) {
            cloud = explosion;
        } else if (at == AnimationType.GREENCLOUD) {
            cloud = poison;
        } else if (at == AnimationType.HEALING) {
            cloud = healing;
        } else if (at == AnimationType.SHIELD){
            cloud = shield;
        } else if (at == AnimationType.DEBUFF) {
            cloud = debuff;
        } else {
            throw new IllegalArgumentException("Invalid AnimationType");
        }

        Runnable explosionRunnable = new Runnable() {
            @Override
            public void run() {
                cloud.startAnimation(target.getX(Align.center), target.getY(Align.center), Align.center);
            }
        };
        Action action = Actions.sequence(Actions.run(explosionRunnable), Actions.delay(cloud.getDuration()));
        animator.addAction(action, target);
    }

    public boolean isDoneAnimating(){
        return animator.isEmpty();
    }

    private Actor getActorFromID(String ID) {
        if (ID.matches("A-\\d\\d\\d")) {
            return asteroidImages.get(ID);
        } else if (ID.matches("V-\\d\\d\\d")) {
            return vehicleImages.get(ID);
        } else {
            throw new IllegalArgumentException("Something is wrong with the target ID");
        }
    }

    public void dispose(){
        stage.dispose();
        sr.dispose();
        skin.dispose();
        btnAtlas.dispose();
    }


    private class AnimationAdapter {
        private LinkedList<Actor> actors;
        private LinkedList<Action> actions;
        private Runnable run = new Runnable() {
            @Override
            public void run() {
                if (!empty) {
                    nextAction();
                }
            }
        };

        private boolean empty;

        AnimationAdapter() {
            actors = new LinkedList<>();
            actions = new LinkedList<>();
            empty = true;
        }

        public boolean isEmpty() {
            return empty;
        }

        void addAction(Action action, Actor actor) {
            System.out.println("An action was added to the AnimationAdapter");
            Action sAction = Actions.sequence(action, Actions.run(run));
            actions.add(sAction);
            actors.add(actor);
            if (empty) {
                empty = false;
                nextAction();
            }
        }

        void nextAction() {
            System.out.println("Next Action called");
            if (actions.size() == 0) {
                empty = true;
                return;
            }
            actors.pop().addAction(actions.pop());
        }
    }
}
