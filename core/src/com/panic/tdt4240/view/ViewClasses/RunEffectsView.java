package com.panic.tdt4240.view.ViewClasses;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.panic.tdt4240.models.Asteroid;
import com.panic.tdt4240.models.GameInstance;
import com.panic.tdt4240.models.Vehicle;
import com.panic.tdt4240.states.State;
import com.panic.tdt4240.util.MapConnections;
import com.panic.tdt4240.util.MapMethods;

import java.util.ArrayList;

/**
 * Created by Hermann on 14.04.2018.
 */

public class RunEffectsView extends AbstractView {

    private GameInstance gameInstance;
    private Stage stage;
    private ShapeRenderer sr;
    private ArrayList<Image> vehicleImages;
    private ArrayList<Image> asteroidImages;
    private MapConnections mapConnections;

    public RunEffectsView(State state) {
        super(state);
        sr = new ShapeRenderer();
        sr.setColor(1,1,1,0);
        sr.setAutoShapeType(true);
        gameInstance = GameInstance.getInstance();
        stage = new Stage();
        setUpMap();
    }
    private void setUpMap() {
        mapConnections = new MapConnections(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        asteroidImages = new ArrayList<>();
        vehicleImages = new ArrayList<>();
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
            Texture texture = new Texture("asteroids/" + asteroids.get(i).getTexture() + ".png");
            Image asteroid = new Image(texture);
            asteroid.setSize(SCREEN_WIDTH / 5, SCREEN_WIDTH / 5);
            asteroidDimensions.add(i, new Vector2(asteroid.getWidth(), asteroid.getHeight()));
            asteroid.setPosition(
                    //Image should be rendered inside the window and above the table
                    asteroids.get(i).getPosition().x * (Gdx.graphics.getWidth() - asteroid.getWidth()),
                    asteroids.get(i).getPosition().y * (Gdx.graphics.getHeight() - table - asteroid.getHeight()) + table);
            asteroidPositions.add(i, new Vector2(asteroid.getX(), asteroid.getY()));
            asteroidImages.add(asteroid);
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
            vehicleImages.add(vehicle);
            stage.addActor(vehicle);
        }
    }

    private ArrayList<Vector2[]> getConnections(){
        return mapConnections.getConnections();
    }

    @Override
    public void render() {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        ArrayList<Vector2[]> lines = getConnections();
        for(Vector2[] points : lines){
            sr.rectLine(points[0], points[1], 5.0f);
        }
        sr.end();
        stage.draw();
    }

    public void dispose(){
        sr.dispose();
        stage.dispose();
    }
}
