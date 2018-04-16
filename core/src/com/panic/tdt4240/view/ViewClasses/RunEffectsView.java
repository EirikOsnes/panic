package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Pool;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.State;
import com.panic.tdt4240.util.MapMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Created by Hermann on 14.04.2018.
 */

public class RunEffectsView extends AbstractView {

    private GameInstance gameInstance;
    private ShapeRenderer sr;
    private ArrayList<AsteroidConnection> connections;
    private HashMap<String, Image> vehicleImages;
    private HashMap<String, Image> asteroidImages;
    private AnimationAdapter animator;
    private Actor emptyActor;


    public RunEffectsView(State state) {
        super(state);
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        gameInstance = GameInstance.getInstance();
        Gdx.input.setInputProcessor(stage);
        setUpMap();
        animator = new AnimationAdapter();
        emptyActor = new Actor();
        stage.addActor(emptyActor);
        System.out.println(vehicleImages.keySet().toString());
        System.out.println(asteroidImages.keySet().toString());
    }
    private void setUpMap() {
        asteroidImages = new HashMap<>();
        vehicleImages = new HashMap<>();
        connections = new ArrayList<>();
        ArrayList<Asteroid> asteroids = gameInstance.getMap().getAsteroids();

        ArrayList<String[]> vehicleOnAsteroid = new ArrayList<>();
        ArrayList<Vector2> asteroidPositions = new ArrayList<>();
        ArrayList<Vector2> asteroidDimensions = new ArrayList<>();
        float table = SCREEN_HEIGHT/5;
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
            Texture texture = new Texture("asteroids/meteorBrown_big1.png");
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
                addConnection(asteroids.get(i), neighbour, asteroid.getWidth(), asteroid.getHeight(), table);
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

    }private ArrayList<Vector2[]> getConnections(){
        ArrayList<Vector2[]> lines = new ArrayList<>();
        for(AsteroidConnection connection : connections){
            Vector2[] line = new Vector2[2];
            line[0] = connection.start;
            line[1] = connection.end;
            lines.add(line);
        }
        return lines;
    }

    private void addConnection(Asteroid start, Asteroid end, float asteroidWidth, float asteroidHeight, float tableHeight) {
        if(notConnected(start.getId(), end.getId())){
            AsteroidConnection connection = new AsteroidConnection(
                    //Calculation of center point of the asteroids, see setUpMap() in PlayCardView
                    new Vector2(start.getPosition().x *(Gdx.graphics.getWidth() - asteroidWidth) + asteroidWidth/2,
                            start.getPosition().y *(Gdx.graphics.getHeight() - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    new Vector2(end.getPosition().x *(Gdx.graphics.getWidth() - asteroidWidth) + asteroidWidth/2,
                            end.getPosition().y *(Gdx.graphics.getHeight() - tableHeight - asteroidHeight) + tableHeight
                                    + asteroidHeight/2),
                    start.getId(), end.getId());
            connections.add(connection);
        }
    }

    @Override
    public void render() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Vector2[]> lines = getConnections();
        for(Vector2[] points : lines){
            sr.rectLine(points[0], points[1], 5.0f);
        }
        sr.end();
        stage.act();
        stage.draw();
    }

    //TODO: All of the following methods should add animation to a stack

    public void moveVehicle(String vehicleID, String asteroidID) {
        //TODO: Animate the moving of the vehicle
        Actor actor = vehicleImages.get(vehicleID);
        Image asteroid = asteroidImages.get(asteroidID);
        Vector2 vec = MapMethods.asteroidPositions(asteroid.getX(), asteroid.getY(), asteroid.getWidth(),
                asteroid.getHeight(), gameInstance.getVehicleById(vehicleID).getColorCar());
        Action action = Actions.moveTo(vec.x, vec.y, 2);
        animator.addAction(action, actor);
    }

    public void attackVehicle(String vehicleID) {
        //TODO: Animate the attacking of a vehicle

    }

    public void attackAsteroid(String asteroidID) {
        //TODO: Animate the attacking of an asteroid
    }

    public void destroyVehicle(String vehicleID) {
        //TODO: Animate the destruction of a vehicle
    }

    public void dispose(){

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
