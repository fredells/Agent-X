package com.ells.agentex.listeners;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.tables.LevelPack;

public class MenuGestures implements GestureListener{
	private Array<LevelPack> tables;
	private double progress;
	private Stage stage;
	private AgentExGame game;
	public MenuGestures(Array<LevelPack> tables, double progress, Stage stage, AgentExGame game) {
		this.game = game;
		this.tables = tables;
		this.progress = progress;
		this.stage = stage;
	}
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
    	//swipe sensetivity
    	progress -= velocityX/3000;
    	
    	if(progress <=0) {
    		progress = 0;
    	} else if(progress >= tables.size-1) {
    		progress = tables.size-1;
    	}
    	int focus = (int) Math.round(progress);
    	progress = focus;
    	int level = 0;
    	int size = 0;
    	for(LevelPack t: tables) {
    		size += t.getWidth();
    		if(level == focus-1) {
    			t.addAction(Actions.scaleTo((float) (0.6f), (float) (0.6f), 0.2f));
    			t.addAction(Actions.moveTo(game.loader.screenWidth*0.25f-t.getWidth()/2, t.getY(), 0.25f));
    		} else if(level == focus+1) {
    			t.addAction(Actions.scaleTo((float) (0.6f), (float) (0.6f), 0.2f));
    			t.addAction(Actions.moveTo(game.loader.screenWidth*0.75f-t.getWidth()/2, t.getY(), 0.25f));
    		} 
    		else if(level == focus) {
    			t.addAction(Actions.scaleTo((float) (1), (float) (1), 0.2f));
    			t.addAction(Actions.moveTo(game.loader.screenWidth*0.5f-t.getWidth()/2, t.getY(), 0.25f));
    		} else if (level > focus){
    			t.addAction(Actions.moveTo(game.loader.screenWidth*1.25f, t.getY(), 0.25f));
    		} else if(level < focus) {
    			t.addAction(Actions.moveTo(-game.loader.screenWidth*0.5f, t.getY(), 0.25f));
    		}
    		
    		
    		level ++;
    	}
    	/**
    	if(velocityX > 0) {
        	System.out.println(velocityX);
        	int tableNumber = 0;
        	float size = 0;
        	tables.reverse();
        	for(LevelPack t: tables) {
        		//t.sizeBy((float) (1/(progress -tableNumber)));
        		t.addAction(Actions.scaleTo((float) (progress/tableNumber), (float) (progress/tableNumber), 0.25f));
        		size += t.getWidth();
        		Tween.to(t, ActorAccessor.X, 1f).target(AssetLoader.screenWidth - size).start(tweenManager);
        		tableNumber++;
        	}
        	
    	} else {
        	System.out.println(velocityX);
        	int tableNumber = 0;
        	float size = 0;
        	tables.reverse();
        	for(LevelPack t: tables) {
        		//t.sizeBy((float) (1/(progress -tableNumber)));
        		t.addAction(Actions.scaleTo((float) (progress/tableNumber), (float) (progress/tableNumber), 0.25f));
        		size += t.getWidth();
        		Tween.to(t, ActorAccessor.X, 1f).target(AssetLoader.screenWidth -size).start(tweenManager);
        		tableNumber++;
        	}
        	tables.reverse();
    	}
    	 **/
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean zoom (float originalDistance, float currentDistance){

       return false;
    }

    @Override
    public boolean pinch (Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer, Vector2 secondPointer){

       return false;
    }
}
