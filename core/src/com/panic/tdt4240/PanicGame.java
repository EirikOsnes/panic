package com.panic.tdt4240;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.panic.tdt4240.states.GameStateManager;
import com.panic.tdt4240.states.MenuState;

import java.awt.Color;

public class PanicGame extends ApplicationAdapter {

	public static final String TITLE = "DON'T PANIC";

	//public static final String FULL_TITLE = "Defamatory Outrage of Notoriously Thick-headed \n" +
	//		"People Assaulting Neighbours on Interstellar Comets";
	public static final String FULL_TITLE = "People Assaulting Neighbours\non Interstellar Comets";


	public static final int WIDTH = 400;
	public static final int HEIGHT = 800;

	GameStateManager gsm;

	
	@Override
	public void create () {
		gsm = new GameStateManager();
		Gdx.gl.glClearColor(0,0,0,0);
		gsm.push(new MenuState(gsm));

	}

	@Override
	public void render () {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render();
	}

}
