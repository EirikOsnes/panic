package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.State;
import com.panic.tdt4240.util.MapConnections;
import com.panic.tdt4240.util.MapMethods;
import com.panic.tdt4240.view.animations.Explosion;
import com.panic.tdt4240.view.animations.Missile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

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
    private final Explosion explosion;
    private final Missile missile;

    public RunEffectsView(State state) {
        super(state);
        sr = new ShapeRenderer();
        sr.setColor(1, 1, 1, 0);
        sr.setAutoShapeType(true);
        gameInstance = GameInstance.getInstance();
        setUpMap();
        animator = new AnimationAdapter();
        explosion = new Explosion();
        missile = new Missile(Missile.COLOR_RED);
        stage.addActor(explosion);
        System.out.println(vehicleImages.keySet().toString());
        System.out.println(asteroidImages.keySet().toString());
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
        float table = SCREEN_HEIGHT / 5;
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
        for (int j = 0; j < vehicleOnAsteroid.size(); j++) {
            int asteroid = Integer.valueOf(vehicleOnAsteroid.get(j)[2]);
            Vehicle activeVehicle = gameInstance.getVehicleById(vehicleOnAsteroid.get(j)[0]);
            Vector2 asteroidPos = asteroidPositions.get(asteroid);

            Image vehicle = new Image(skin.getDrawable(activeVehicle.getColorCar()));
            Vector2 position = MapMethods.asteroidPositions(asteroidPos.x, asteroidPos.y,
                    asteroidDimensions.get(asteroid).x, asteroidDimensions.get(asteroid).y,
                    activeVehicle.getColorCar());
            vehicle.setPosition(position.x, position.y);
            vehicle.setSize(asteroidDimensions.get(asteroid).x / 3, asteroidDimensions.get(asteroid).y * 2 / 3);
            vehicleImages.put(activeVehicle.getVehicleID(), vehicle);
            stage.addActor(vehicle);
        }
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

    //TODO: All of the following methods should add animation to a stack

    public void moveVehicle(String vehicleID, String asteroidID) {
        Actor actor = vehicleImages.get(vehicleID);
        Image asteroid = asteroidImages.get(asteroidID);
        Vector2 vec = MapMethods.asteroidPositions(asteroid.getX(), asteroid.getY(), asteroid.getWidth(),
                asteroid.getHeight(), gameInstance.getVehicleById(vehicleID).getColorCar());
        Action action = Actions.moveTo(vec.x, vec.y, 2);
        animator.addAction(action, actor);
    }

    public void attackVehicle(String vehicleID, String instigatorID) {
        //TODO: Animate the attacking of a vehicle

        final Image instigator = vehicleImages.get(instigatorID);
        final Image vehicle = vehicleImages.get(vehicleID);
        Runnable missileRunnable = new Runnable() {
            @Override
            public void run() {
                missile.startAnimation(instigator.getX(),instigator.getY(),vehicle.getX(),vehicle.getY());
            }
        };
        Runnable explosionRunnable = new Runnable() {
            @Override
            public void run() {
                explosion.startAnimation(vehicle.getX(), vehicle.getY());
            }
        };
        Action action1 = Actions.sequence(Actions.run(missileRunnable), Actions.moveTo(vehicle.getX(), vehicle.getY()));
        animator.addAction(action1, missile);
        Action action2 = Actions.sequence(Actions.run(explosionRunnable), Actions.delay(explosion.getDuration()));
        animator.addAction(action2, explosion);
    }

    public void attackAsteroid(String asteroidID) {
        //TODO: Animate the attacking of an asteroid
    }

    public void destroyVehicle(String vehicleID) {
        //TODO: Animate the destruction of a vehicle
    }

    public void dispose(){
        stage.dispose();
        animator.dispose();
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
        public void dispose(){
            sr.dispose();
            stage.dispose();
        }

        private boolean empty;

        AnimationAdapter() {
            actors = new LinkedList<>();
            actions = new LinkedList<>();
            empty = true;
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
