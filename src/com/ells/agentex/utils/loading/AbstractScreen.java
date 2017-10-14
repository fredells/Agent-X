package com.ells.agentex.utils.loading;

import com.badlogic.gdx.Screen;
import com.ells.agentex.AgentExGame;

/**
 * @author Mats Svensson
 */
public abstract class AbstractScreen implements Screen {

    protected AgentExGame game;

    public AbstractScreen(AgentExGame game) {
        this.game = game;
    }


    public void renderBG(float delta) {
    	
    }
    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}