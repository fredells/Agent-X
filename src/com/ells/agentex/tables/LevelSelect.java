package com.ells.agentex.tables;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.GameScreen;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.LevelScreen.LEVEL_PACK_LETTER;

public class LevelSelect extends Table implements Disposable {

	Skin skin;
	String str, caption;
	int var, highScore, totalCoins, coinsCollected;
	Boolean unlocked;
	Boolean completed;
	Label below1;
	private AgentExGame game;
	private Texture texture, dot, dotFilled;
	private TextureRegion textureReg;
	private TextureRegionDrawable textureDraw;
	private ImageButton level;
	private Table table, table1, table2, table3, table4, table5, table6, table7, table8, c1, c2, c3, c4, c5, c6, c7, c8;
	private Image dotImage, dotFilledImage;
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();
	private float ratio = screenWidth / 1280;
	private ClickListener levelListener;
	
	//final Array<Integer> indeces = new Array<Integer>();
	

	public LevelSelect(final Stage stage, final LEVEL_PACK_LETTER levelset, final AgentExGame game) {
		System.out.println("LEVELSET PASSED: " + levelset );
		this.game = game;
		this.skin = game.loader.skin;
		//this.setDebug(true);
		this.setFillParent(true);
		this.setPosition(0, 30 * ratio);
		//this.setWidth(stage.getWidth());
		//this.setHeight(stage.getHeight());
		//this.setOrigin(this.getWidth() / 2f, this.getHeight() / 2f);
		//this.setTransform(true);

		
		table1 = new Table(); table2 = new Table(); table3 = new Table(); table4 = new Table();
		table5 = new Table(); table6 = new Table(); table7 = new Table(); table8 = new Table();
		c1 = new Table(); c2 = new Table(); c3 = new Table(); c4 = new Table();
		c5 = new Table(); c6 = new Table(); c7 = new Table(); c8 = new Table();
		//table1.setDebug(true);
		
		dot = new Texture(Gdx.files.internal("img/dot.png"));
		dot.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dotImage = new Image(dot);
		
		dotFilled = new Texture(Gdx.files.internal("img/dotFilled.png"));
		dotFilled.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		dotFilledImage = new Image(dotFilled);
		//indeces.addAll(1,2,3,4,5,6,7,8);
		
		str = (String) game.loader.nestedLevelProfile.get(levelset + "0").get("title");
		Label heading = new Label(str, skin.get("largeStyle", LabelStyle.class));
		this.add(heading).colspan(12).spaceBottom(10).row();
		
		List<Integer> listOfNumbers = new ArrayList<Integer>();
		listOfNumbers.add(1); listOfNumbers.add(2); listOfNumbers.add(3); listOfNumbers.add(4);
		listOfNumbers.add(5); listOfNumbers.add(6); listOfNumbers.add(7); listOfNumbers.add(8);
		
		
		for (Iterator<Integer> iter = listOfNumbers.iterator(); iter.hasNext(); )
		{
		    final int i = iter.next();
		    System.out.println("I: " + i);
		    
			// creating level buttons
			unlocked = (Boolean) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("unlocked");
			System.out.println("UNLCOKED STATUS " + game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("unlocked"));
			completed = (Boolean) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("completed");
			//var = (int) game.loader.nestedLevelProfile.get(levelset + "1").get("completed");
	
			// check the status of level completion and change the skin accordingly
			String tmp;
			tmp = checkImage(unlocked, completed);
			// high score label
			if (tmp == "default" && highScore > 0) {
				highScore = (Integer) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("highScore");
				caption = "High Score: " + highScore;
			} else {
				caption = "";
			}
			Label below = new Label(caption, skin, "textStyle");
			
			caption = (String) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("title");
			Label title = new Label(caption, skin, "numberStyle");
			if (unlocked == false) {
				title.setText("LOCKED");
			}
	
			//level = new ImageButton(skin, tmp);
			if (completed) {
				texture = new Texture(Gdx.files.internal("img/" + String.valueOf(i) + ".png"));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			else {
				texture = new Texture(Gdx.files.internal("img/" + String.valueOf(i) + "u.png"));
				texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			}
			textureReg = new TextureRegion(texture);
			textureDraw = new TextureRegionDrawable(textureReg);
			
			
			level = new ImageButton(textureDraw);
			level.setTransform(true);
			if (unlocked){ level.setTouchable(Touchable.enabled); }
			//COMMENT 2 LINES BELOW FOR SELECT ANY LEVEL -- godmode
			//else { level.setTouchable(Touchable.disabled); }
			//level.setDisabled(!unlocked);
			levelListener = new ClickListener() {
	
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (true) {
						while (!game.manager.update()) {
						}
						
						LevelScreen.setCurrentLevel(levelset + String.valueOf(i));
						LevelScreen.intLevel = i;
						LevelScreen.stringPack = levelset;
						stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {
							@Override
							public void run() {
								((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new GameScreen());
							}
						})));
						
					}
				}
			};
			level.addListener(levelListener);
			
			totalCoins = (Integer) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("coinsTotal");
			coinsCollected = (Integer) game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("coinsCollected");
			/*System.out.println("TOTAL" + game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("coinsTotal")
					+ " COLLECTED " + game.loader.nestedLevelProfile.get(levelset + String.valueOf(i)).get("coinsCollected"));*/
			
			int c, v;
			int size = (int) (120 * ratio), side = 10;
			int dotSize = 20;
			int spaceBottom = 10;
			float padLeft = 5f; float padFirst = 80; float width = 250;
			dotImage = new Image(dot);
			//table1.add(dotImage).size(dotSize, dotSize);
			dotFilledImage = new Image(dotFilled);
			//table1.add(dotFilledImage).size(dotSize, dotSize);
			
			if (i == 1) { 
				table1.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table1.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c1.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c1.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c1.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c1.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table1.add(c1);
			}

			else if (i == 2) { 
				table2.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table2.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c2.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c2.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c2.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c2.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table2.add(c2);
			}

			else if (i == 3) { 
				table3.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table3.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c3.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c3.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c3.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c3.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table3.add(c3);
			}

			else if (i == 4) { 
				table4.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table4.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c4.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c4.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c4.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c4.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table4.add(c4);
			}

			else if (i == 5) { 
				table5.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table5.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c5.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c5.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c5.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c5.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table5.add(c5);
			}

			else if (i == 6) { 
				table6.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table6.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c6.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c6.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c6.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c6.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table6.add(c6);
			}

			else if (i == 7) { 
				table7.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table7.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c7.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c7.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c7.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c7.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table7.add(c7);
			}

			else if (i == 8) { 
				table8.add(level).width(size).height(size).align(Align.center).pad(padLeft).row();
				table8.add(title).align(Align.center).padBottom(10).row();
				if (totalCoins > 0) {
					c = 1; v = 1;
					if (coinsCollected > 0) {
						for (c = 1; c < (coinsCollected + 1); c++) {
							if (c == 3) { c8.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
							else { c8.add(dotFilledImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
							dotFilledImage = new Image(dotFilled); 
						}
					}
					for (v = 1; v < (totalCoins - coinsCollected + 1); v++) {
						if ((v + coinsCollected) == 3) { c8.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side).row(); }
						else { c8.add(dotImage).size(dotSize, dotSize).align(Align.left).pad(padLeft).padRight(side).padLeft(side);}
						dotImage = new Image(dot); 
					}
				}
				table8.add(c8);
			}
			
			//System.out.println("TRYINGGGG" + levelset + String.valueOf(i));
			
			/*if (totalCoins > 0) {
				for (int c = 1; c < (coinsCollected + 1); c++) {
					table.add(dotFilledImage);
				}
				for (int v = 1; v < (totalCoins - coinsCollected+ 1); v++) {
					table.add(dotImage);
				}
			}*/
			
			//System.out.println("SIZE TABLE ");
			
		}
		
		
		// putting stuff together
		float width = 250;
		float lrpadding = 30 * ratio;
		float tbpadding = 15 * ratio;
		this.add(heading).colspan(8).spaceBottom(10).row();
		this.add(table1).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table2).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table3).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table4).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).row();
		this.add(table5).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table6).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table7).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10).spaceRight(15);
		this.add(table8).width(width).align(Align.top).pad(tbpadding, lrpadding, 0, lrpadding).spaceBottom(10);
		//this.add(table1);
		
		stage.addAction(Actions.alpha(0));
		stage.addAction(Actions.alpha(1, 1));

	}
	
	public String checkImage(Boolean unlocked, Boolean completed) {
		String tmp;
		if (unlocked) {
			tmp = "default";
		} else if (completed) {
			tmp = "default";
		} else {
			tmp = "emptySave";
		}
		return tmp;
	}

	@Override
	public void dispose() {
		level.removeListener(levelListener);
		this.clearChildren();
		((TextureRegionDrawable)level.getImage().getDrawable()).getRegion().getTexture().dispose();
		
	}


}
