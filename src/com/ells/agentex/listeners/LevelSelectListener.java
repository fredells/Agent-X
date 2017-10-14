package com.ells.agentex.listeners;


import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.tables.LevelPack;

public class LevelSelectListener implements GestureListener{
	private Array<LevelPack> tables;
	private double progress;
	private Stage stage;
	private AgentExGame game;
	public LevelSelectListener(Array<LevelPack> tables, double progress, Stage stage, AgentExGame game) {
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
    	for(final LevelPack t: tables) {
    		Rectangle r = new Rectangle(t.getX(), t.getY(), t.getWidth(), t.getHeight());
    		if(r.contains(new Vector2(x, y))) {
					LevelScreen.stringPack = t.levelSet;
					for (int i = 0; i < tables.size; i++) {
						tables.get(i).addAction(moveTo(tables.get(i).getX(), -stage.getHeight(), .5f));
					}
				
				
					stage.addAction(sequence(Actions.delay(0.75f), run(new Runnable() {
						
						@Override
						public void run() {
							LevelScreen.levelSelectMenu(t.levelSet, game);
							System.out.print(t.levelSet);
						}
					})));
    		}
    	}
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {

        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
    	progress -= velocityX/3000;
    	if(progress <0) {
    		progress = 0;
    		
    	} else if(progress > tables.size-1) {
    		progress = tables.size-1;
    	}
    	int focus = (int) Math.round(progress);
    	int level = 0;
    	int size = 0;
    	for(LevelPack t: tables) {
    		size += t.getWidth();
    		if(level == focus-1) {
    			t.addAction(Actions.scaleTo((float) (0.5f), (float) (0.5f), 0.2f));
    			t.addAction(Actions.moveTo(game.loader.screenWidth*0.25f-t.getWidth()/2, t.getY(), 0.25f));
    		} else if(level == focus+1) {
    			t.addAction(Actions.scaleTo((float) (0.5f), (float) (0.5f), 0.2f));
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

