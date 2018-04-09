package com.panic.tdt4240.states;

import com.panic.tdt4240.view.Renderer;

import java.util.Stack;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameStateManager {
    private Stack<State> states;
    private Renderer renderer;

    public GameStateManager(){
        states = new Stack<>();
        renderer = Renderer.getInstance();
    }

    public void push(State state){
        states.push(state);
    }

    public void pop(){
        states.pop().dispose();

    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
    }

    public void update(float dt){
        states.peek().update(dt);
    }

    public void render(){
        states.peek().render();
    }

    public void clear(){
        for (State s : states) s.dispose();
        states = new Stack<>();
    }

    public void reset(){
        clear();
        states.push(new MenuState(this));
    }

}
