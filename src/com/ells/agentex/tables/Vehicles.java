package com.ells.agentex.tables;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.actors.StatsBar;
import com.ells.agentex.screens.MenuScreen;
import com.ells.agentex.screens.UnlockScreen;

public class Vehicles extends Table {

	Skin skin;
	String vKey, name, str, info;
	Map<String, Object> profile;
	int num, cost, coins;
	float speed, acceleration, control;
	Boolean unlocked, selected;
	Texture texture, newTexture;
	Image image;
	public float startX;
	private boolean upgrade = false;
	private TextButton costButton2, costButton3;
	private Stage stage;
	float screenWidth = Gdx.graphics.getWidth();
	float screenHeight = Gdx.graphics.getHeight();
	Array<Vehicles> pack;
	private float replacePos = 0;
	private float ratio = screenWidth / 1280;
	
	public Vehicles(final AgentExGame game, int number, Stage lvlStage, float position, final Array<Vehicles> packs) {
		stage = lvlStage;
		pack = packs;
		//reset the last selected position when unlocked
		if (position != 0) {
			replacePos = position;
		}
		skin = game.loader.skin;
		//this.setDebug(true);
		this.setFillParent(false);
		num = number;
		int relPos = num - (Integer) game.loader.getProfile().get("selectedVehicle");

		vKey = "v" + String.valueOf(number);
		System.out.println("vKey: " + vKey);
		profile = game.loader.getLevelProfile(vKey);

		name = (String) profile.get("name");
		info = (String) profile.get("info");
		unlocked = (Boolean) profile.get("unlocked");
		cost = (Integer) profile.get("cost");
		speed = (Float) profile.get("speed");
		acceleration = (Float) profile.get("acceleration");
		control = (Float) profile.get("control");
		if ((Integer) game.loader.getProfile().get("selectedVehicle") == number) {
			selected = true;
		} else {
			selected = false;
		}

		Label heading = new Label(name, skin, "subtitleStyle");
		Label infoLabel = new Label(info, skin, "textStyle");
		Label speedLabel = new Label("Max Speed:", skin, "numberStyle");
		Label accLabel = new Label("Acceleration:", skin, "numberStyle");
		Label controlLabel = new Label("Control:", skin, "numberStyle");

		StatsBar speedBarImg = new StatsBar(game.manager.get("img/backStats.png", Texture.class),
				game.manager.get("img/frontStats.png", Texture.class), (speed - 21) * 6);
		StatsBar accBarImg = new StatsBar(game.manager.get("img/backStats.png", Texture.class),
				game.manager.get("img/frontStats.png", Texture.class), (acceleration - 26) * 6);
		StatsBar controlBarImg = new StatsBar(game.manager.get("img/backStats.png", Texture.class),
				game.manager.get("img/frontStats.png", Texture.class), (control - 6) * 10);

		// for now just 1 image
		// texture = new Texture(Gdx.files.internal("img/v1.png"));

		// create stats table nested
		Table statsTable = new Table();
		//statsTable.setDebug(true);
		statsTable.setFillParent(false);
		statsTable.add(speedLabel).align(Align.right).spaceRight(20).spaceBottom(Gdx.graphics.getHeight()/60);
		statsTable.add(speedBarImg).width(200 * ratio).fill().spaceBottom(Gdx.graphics.getHeight()/60).row();
		statsTable.add(accLabel).align(Align.right).spaceRight(20).spaceBottom(Gdx.graphics.getHeight()/60);
		statsTable.add(accBarImg).width(200 * ratio).fill().spaceBottom(Gdx.graphics.getHeight()/60).row();
		statsTable.add(controlLabel).align(Align.right).spaceRight(20).spaceBottom(Gdx.graphics.getHeight()/60);
		statsTable.add(controlBarImg).width(200 * ratio).fill().spaceBottom(Gdx.graphics.getHeight()/60).row();

		this.add(heading).colspan(2).pad(Gdx.graphics.getHeight()/60).row();
		if (unlocked == true) {
			texture = new Texture(Gdx.files.internal("img/" + vKey + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
		} else {
			texture = new Texture(Gdx.files.internal("img/" + vKey + "l.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		image = new Image(texture);
		
		this.add(image).height(200 * ratio).width(300 * ratio).spaceRight(20);
		this.add(statsTable).row();
		this.add(infoLabel).colspan(2).pad(Gdx.graphics.getHeight()/60).row();
		// this.add(costButton).pad(20).colspan(2);
		// add text button that depends on unlock and selected status
		if (selected == true) {
			Label costButton1 = new Label("SELECTED", skin, "muroStyle");
			this.add(costButton1).align(Align.right).pad(Gdx.graphics.getHeight()/60).colspan(2);
		} else if (unlocked == true) {
			costButton2 = new TextButton("SELECT", skin, "upgradeStyle");
			costButton2.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (UnlockScreen.getPanning() == false) {
						game.loader.newProfileData("selectedVehicle", num);
						stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {

							@Override
							public void run() {

								game.setScreen(new MenuScreen(game));
							}
						})));
					}
				}
			});
			this.add(costButton2).align(Align.right).pad(Gdx.graphics.getHeight()/60).colspan(2);
		} else {
			costButton3 = new TextButton("Unlock for: " + cost + " EXP", skin, "upgradeStyle");
			coins = (Integer) game.loader.getProfile().get("coins");
			//if (cost > coins) { costButton3.setDisabled(true); }
			//if (cost <= coins) {
			costButton3.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					if (UnlockScreen.getPanning() == false) {
						if (cost < (Integer) game.loader.getProfile().get("coins")) {
						int lastSelected = (Integer) game.loader.getProfile().get("selectedVehicle");
						
						game.loader.setLevelData(vKey, "unlocked", true);
						game.loader.newProfileData("coins", ((Integer) game.loader.getProfile().get("coins") - cost));
						game.loader.newProfileData("selectedVehicle", num);
						//play a noise here
						game.fxManager.play("Sound/flip_reward.ogg");
						//game.loader.assetManager.get("Sound/flip_reward.ogg", Sound.class).play(0.3f, 1, 1);
						
						
						//trying to reset last selected
						//System.out.println("NUMBER OF ACTORS " + stage.getActors().size);
						float oldPos = stage.getActors().get(lastSelected - 1).getX();
						packs.add(new Vehicles(game, lastSelected, stage, oldPos, packs));
						
						stage.getActors().get(stage.getActors().size - 1).remove();
						stage.addActor(packs.get(packs.size - 1));
						stage.getActors().get(lastSelected - 1).setVisible(false);
						
						//change coin counter
						CoinCounter temp = new CoinCounter(game);
						temp.setX(stage.getViewport().getCamera().position.x + Gdx.graphics.getWidth()/2 - temp.expWidth - 50);
						stage.addActor(temp);
						//stage.getActors().get(13).
						
						image.addAction(sequence(Actions.alpha(0, 1), run(new Runnable() {
							@Override
							public void run() {
								newTexture = new Texture(Gdx.files.internal("img/" + vKey + ".png"));
								newTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
								image.setDrawable(new TextureRegionDrawable(new TextureRegion(newTexture)));
								//newTexture.dispose();
								
							}
						}), Actions.alpha(0), Actions.alpha(1, 1)));
						
						costButton3.setText("Selected");
						costButton3.setTouchable(Touchable.disabled);
					}
					}
				}
					
			});
			//}
			this.add(costButton3).align(Align.right).pad(20).colspan(2);
		}
		if (replacePos != 0) {
			this.setPosition(replacePos, screenHeight/2);
		}
		else {
			this.setPosition(relPos * screenWidth /1.5f + screenWidth / 2, screenHeight/2);
		}
		System.out.print("RELPOS" + relPos);
		startX = this.getX();
	}


	public boolean isImageLoaded() {
		if (image == null) {
			return false;
		} else
			return true;
	}

	public void loadImage() {
		if (unlocked == true) {
			texture = new Texture(Gdx.files.internal("img/" + vKey + ".png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
		} else {
			texture = new Texture(Gdx.files.internal("img/" + vKey + "l.png"));
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
		// for now just 1 image
		// texture = new Texture(Gdx.files.internal("img/v1.png"));
		Image tmp = new Image(texture);
		this.getCell(image).setActor(tmp);
		image= tmp;
	}

	public void dispose() {
		texture.dispose();
		newTexture.dispose();
	}

}
