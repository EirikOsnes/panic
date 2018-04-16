package com.panic.tdt4240.states;

import com.panic.tdt4240.connection.Connection;

import java.util.Stack;

/**
 * Created by magnus on 12.03.2018.
 */

public class GameStateManager {
    private Stack<State> states;

    public GameStateManager(){
        states = new Stack<>();
    }

    public void push(State state){
        states.push(state);
        setAdapter();
        setInputProcessor();
    }

    public void pop(){
        states.pop().dispose();
        setAdapter();
        setInputProcessor();
    }

    public void set(State state){
        states.pop().dispose();
        states.push(state);
        setAdapter();
        setInputProcessor();
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
        push(new MenuState(this));
    }

    private void setAdapter() {
        Connection.getInstance().setAdapter(states.peek().callbackAdapter);
    }

    private void setInputProcessor() {
        states.peek().getView().setInputProcessor();
    }

}
