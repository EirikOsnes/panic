package com.panic.tdt4240;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.states.GameStateManager;
import com.panic.tdt4240.states.MenuState;

public class PanicGame extends ApplicationAdapter {

	public static final String TITLE = "DON'T PANIC";

	public static final int WIDTH = 400;
	public static final int HEIGHT = 800;

	SpriteBatch batch;
	GameStateManager gsm;

	
	@Override
	public void create () {
		batch = new SpriteBatch();
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(1, 0, 0, 1);
		gsm.push(new MenuState(gsm));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
