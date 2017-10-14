
package com.ells.agentex.tables;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.screens.LevelScreen.LEVEL_PACK_LETTER;

public class LevelPack extends Table implements Disposable{

	Skin skin;
	String str;
	int var;
	public boolean unlocked;
	boolean completed;
	public ImageButton level;
	public LEVEL_PACK_LETTER levelSet;
	private AgentExGame game;
	private LevelScreen levelScreen;
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();
	private float ratio = screenWidth / 1280;
	private ClickListener levelListener;
	
	public LevelPack(final Stage stage, final LEVEL_PACK_LETTER a, final AgentExGame game, final LevelScreen levelScreen) {
		this.levelScreen = levelScreen;
		this.levelSet = a;
		this.skin =  game.loader.skin;
		//this.debug();
		this.setTouchable(Touchable.enabled);
		// creating heading & other labels
		System.out.println("LEVELPACK LETTER" + levelSet);
		System.out.println("LEVELPACK LETTER" + a.toString() + "0");
		str = (String) game.loader.nestedLevelProfile.get(a.toString() + "0").get("title");
		Label heading = new Label(str, skin, "muroStyle");
		str = (String) game.loader.nestedLevelProfile.get(a.toString() + "0").get("subTitle");
		Label subHeading = new Label(str, skin, "numberStyle");
						
		var = (Integer) game.loader.nestedLevelProfile.get(a.toString() + "0").get("levelsCompleted");
		Label completion = new Label("levels  ", skin, "numberStyle");
		Label completionNum = new Label(var + " /8", skin, "textStyle");
		var = (Integer) game.loader.nestedLevelProfile.get(a.toString() + "0").get("coinsCollected");
		Label data = new Label("data  ", skin, "numberStyle");
		Label dataNum = new Label(var + " /" + game.loader.nestedLevelProfile.get(a.toString() + "0").get("totalCoins"), skin, "textStyle");
		Texture texture = new Texture(Gdx.files.internal("data/pack" + a + ".png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureRegion textureReg = new TextureRegion(texture);
		TextureRegionDrawable textureDraw = new TextureRegionDrawable(textureReg);
		level = new ImageButton(textureDraw);
		//level.getImage().setFillParent(true);
		//level.setScale(ratio);
		level.getImage().setScale( (1+ratio) / 2);
		level.pack();
		level.getImage().setOrigin(Align.center);
		//level.getImage().setScale(ratio);
		levelListener = new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					System.out.println(LevelScreen.getPanning());
					if (LevelScreen.getPanning() == false) {
						if(x < level.getWidth() && y < level.getHeight() && x > 0 && y >0) {
							stage.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {
								
								@Override
								public void run() {
									stage.clear();
									levelScreen.packShown = true;
									LevelScreen.levelSelectMenu(LevelPack.this.levelSet, game);
								}
							})));
						}
					}
		    		}

		};	
		level.addListener(levelListener);
		// creating buttons
		
		//change to lock levels
		//unlocked = (Boolean) game.loader.nestedLevelProfile.get(a + "0").get("unlocked");
		unlocked = true;
		if(!unlocked) {
			level.setDisabled(true);
			level.setTouchable(Touchable.disabled);
		}
		completed = (Boolean) game.loader.nestedLevelProfile.get(a + "0").get("completed");
		//this.setWidth(400);
		this.add(heading).align(Align.center).colspan(2).spaceBottom(25).row();
		this.add(subHeading).align(Align.center).colspan(2).row();
		this.add(level).width(500 * (1 +ratio)/2).height(300 * (1 +ratio)/2).pad(15).colspan(2).row();
		this.add(completion).align(Align.right).spaceRight(15).spaceBottom(15);
		this.add(completionNum).align(Align.left).spaceBottom(15).row();
		this.add(data).align(Align.right).spaceRight(15);
		this.add(dataNum).align(Align.left).row();
		this.setY(Gdx.graphics.getHeight()/2);
		this.setX((550 * ratio + 50 * ratio)*a.value() + Gdx.graphics.getWidth()/4);

	}

	@Override
	public void dispose() {
		level.removeListener(levelListener);
		this.clearChildren();
		((TextureRegionDrawable)level.getImage().getDrawable()).getRegion().getTexture().dispose();
		
		
	}
	
}
