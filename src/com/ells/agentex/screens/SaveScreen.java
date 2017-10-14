package com.ells.agentex.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;

public class SaveScreen implements Screen {

	private Stage stage;
	private Skin skin;
	private Table table, nameSet;
	private AgentExGame game;
	private TextButton back;
	private String str;
	private Array<Texture> textures = new Array<Texture>();
	private float screenWidth = Gdx.graphics.getWidth();
	private float screenHeight = Gdx.graphics.getHeight();
	private float ratio = screenWidth / 1280;
	
	public SaveScreen(AgentExGame game) {
		this.game = game;
	}
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.background.render(delta);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		//stage.setViewport(width, height, false);
		//table.invalidateHierarchy();
		//nameSet.invalidateHierarchy();
	}
	
	@Override
	public void show() {
		skin = game.loader.skin;
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);

		back = new TextButton("back", skin, "backStyle");
		back.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//game.background.boost(new Vector2(-3,0), 1.5f);
				stage.addAction(new SequenceAction(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {
						game.setScreen(new MenuScreen(game));
					}
				})));

			}
		});
		back.setPosition(20, 0);
		stage.addActor(back);
		//calls menu table
		chooseSave();
		
	}
	
	private void chooseSave() {
		table = new Table(skin);
		table.setFillParent(true);
		//table.setDebug(true);

		// creating heading
		//Label heading = new Label(AgentExGame.TITLE, skin.get("largeStyle", LabelStyle.class));
		
		//instructions & labels
		Label instructions = new Label("Select a save file", skin.get("subtitleStyle", LabelStyle.class));
		Label name1 = new Label(game.loader.getNameFromSave("save1"), skin, "muroStyle");
		Label name2 = new Label(game.loader.getNameFromSave("save2"), skin, "muroStyle");
		Label name3 = new Label(game.loader.getNameFromSave("save3"), skin, "muroStyle");
		name1.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				table.addAction(sequence(Actions.alpha(0, 1),Actions.removeActor()));
				game.loader.setSave("3");
				game.loader.load(game);
				enterName(3);
				
			}
		});
		name2.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				table.addAction(sequence(Actions.alpha(0, 1),Actions.removeActor()));
				game.loader.setSave("3");
				game.loader.load(game);
				enterName(3);
				
			}
		});
		name3.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				table.addAction(sequence(Actions.alpha(0, 1),Actions.removeActor()));
				game.loader.setSave("3");
				game.loader.load(game);
				enterName(3);
				
			}
		});
		Label experience = new Label("EXPERIENCE  ", skin, "numberStyle");
		Label coinsNum1 = new Label("" + game.loader.getObjectFromSave("save1", "coins"), skin, "textStyle");
		Label coinsNum2 = new Label("" + game.loader.getObjectFromSave("save2", "coins"), skin, "textStyle");
		Label coinsNum3 = new Label("" + game.loader.getObjectFromSave("save3", "coins"), skin, "textStyle");
		Label dataCollected = new Label("DATA COLLECTED  ", skin, "numberStyle");
		Label dataNum1 = new Label("" + game.loader.getObjectFromSave("save1", "totalData") + " /200", skin, "textStyle");
		Label dataNum2 = new Label("" + game.loader.getObjectFromSave("save2", "totalData") + " /200", skin, "textStyle");
		Label dataNum3 = new Label("" + game.loader.getObjectFromSave("save3", "totalData") + " /200", skin, "textStyle");
		
		
		// creating buttons
		//ImageButton save1 = new ImageButton(skin, "default");
		//get the image of selected vehicle
		str = String.valueOf(game.loader.getSelectedVehicleFromSave("save1"));		
		Texture texture = new Texture(Gdx.files.internal("img/v" + str + "l.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.add(texture);
		TextureRegion textureReg = new TextureRegion(texture);
		TextureRegionDrawable textureDraw = new TextureRegionDrawable(textureReg);
		ImageButton save1 = new ImageButton(textureDraw);
		
		save1.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				//table.addAction(sequence(moveTo(0, -stage.getHeight(), .5f)));
				table.addAction(sequence(Actions.alpha(0, 0.5f)));
				game.loader.setSave("1");
				game.loader.load(game);
				enterName(1);
				
			}
		});
		//save1.pad(25);
		
		str = String.valueOf(game.loader.getSelectedVehicleFromSave("save2"));		
		texture = new Texture(Gdx.files.internal("img/v" + str + "l.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.add(texture);
		textureReg = new TextureRegion(texture);
		textureDraw = new TextureRegionDrawable(textureReg);
		ImageButton save2 = new ImageButton(textureDraw);
		save2.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				table.addAction(sequence(Actions.alpha(0, 0.5f)));
				game.loader.setSave("2");
				game.loader.load(game);
				enterName(2);
				
			}
		});
		//save2.pad(25);

		str = String.valueOf(game.loader.getSelectedVehicleFromSave("save3"));		
		texture = new Texture(Gdx.files.internal("img/v" + str + "l.png"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		textures.add(texture);
		textureReg = new TextureRegion(texture);
		textureDraw = new TextureRegionDrawable(textureReg);
		ImageButton save3 = new ImageButton(textureDraw);
		save3.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				table.addAction(sequence(Actions.alpha(0, 0.5f)));
				game.loader.setSave("3");
				game.loader.load(game);
				enterName(3);
				
			}
		});
		//save3.pad(25);
		
		table.setY(Gdx.graphics.getHeight() / 20f);
		// putting stuff together
		table.add(instructions).colspan(6).spaceBottom(25).row();
		table.add(save1).colspan(2).height(200).width(300).spaceBottom(25).spaceRight(50);
		table.add(save2).colspan(2).height(200).width(300).spaceBottom(25).spaceRight(50);
		table.add(save3).colspan(2).height(200).width(300).spaceBottom(25).row();
		table.add(name1).colspan(2).spaceBottom(15).spaceRight(50);
		table.add(name2).colspan(2).spaceBottom(15).spaceRight(50);
		table.add(name3).colspan(2).spaceBottom(15).row();
		//adding experience
		table.add(experience).align(Align.right).spaceBottom(15); experience = new Label("EXPERIENCE ", skin, "numberStyle");
		table.add(coinsNum1).align(Align.left).spaceBottom(15).spaceRight(50);
		table.add(experience).align(Align.right).spaceBottom(15); experience = new Label("EXPERIENCE ", skin, "numberStyle");
		table.add(coinsNum2).align(Align.left).spaceBottom(15).spaceRight(50);
		table.add(experience).align(Align.right).spaceBottom(15);
		table.add(coinsNum3).align(Align.left).spaceBottom(15).row();
		//data collected
		table.add(dataCollected).align(Align.right); dataCollected = new Label("DATA COLLECTED ", skin, "numberStyle");
		table.add(dataNum1).align(Align.left).spaceRight(50);
		table.add(dataCollected).align(Align.right); dataCollected = new Label("DATA COLLECTED ", skin, "numberStyle");
		table.add(dataNum2).align(Align.left).spaceRight(50);
		table.add(dataCollected).align(Align.left).align(Align.right);
		table.add(dataNum3);
		

		stage.addActor(table);
		stage.addAction(Actions.alpha(0));
		stage.addAction(Actions.alpha(1, 1));
		
		//table.setPosition(stage.getWidth()/2,0);
		//table.addAction(Actions.moveTo(0, 0, .25f));

	}
	private void enterName(final int save) {
		
		//do not prompt name change on select save
		if (game.loader.getName().equals("Empty") == false) {
			game.loader.setSave(String.valueOf(save));
			stage.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {
				@Override
				public void run() {
					((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
				}
			})));
			
		}
		
		//prompt name change when selecting empty save
		else {
		
		nameSet = new Table();
		nameSet.setFillParent(true);
		nameSet.invalidateHierarchy();
		//added after animations merge
		//labels
		Label instructions = new Label("Choose your code name", skin, "muroStyle");
		
		skin.get("default", TextFieldStyle.class).font = game.loader.fonts.get("largeFont");
		//skin.get("default", TextFieldStyle.class).background = new TextureRegionDrawable(new TextureRegion(new Texture("img/backStats.png")));
		skin.get("default", TextFieldStyle.class).background = null;
		
		
		String tmpText = "";
		if (save == 1) {
			tmpText = "Agent X";
		}
		else if (save == 2) {
			tmpText = "Agent Y";
		}
		else {
			tmpText = "Agent Z";
		}
		final TextField textField = new TextField(tmpText, skin, "default");
		
		textField.setOnlyFontChars(true);
		textField.setMaxLength(12);
		TextButton confirm = new TextButton("READY", skin, "menuButtonStyle");
		confirm.addListener(new ClickListener() {

			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.loader.setSave(String.valueOf(save));
				game.loader.newProfileData("name", textField.getText());
				nameSet.addAction(sequence(Actions.alpha(0, 0.5f), run(new Runnable() {

					@Override
					public void run() {
						((AgentExGame) Gdx.app.getApplicationListener()).setScreen(new MenuScreen(game));
					}
				})));
			
			}
		});
		confirm.pad(15);

		// putting stuff together
		nameSet.add(instructions).spaceBottom(25).row();
		nameSet.add(textField).size(450 * ratio, 80 * ratio).spaceBottom(25).row();
		nameSet.add(confirm).spaceBottom(25).row();
		stage.addActor(nameSet);
		stage.getActors().first().addAction(Actions.alpha(0, 0.5f));
		stage.getActors().first().clearListeners();
		
		nameSet.addAction(sequence(Actions.alpha(0), Actions.delay(1), Actions.alpha(1, 1)));
		//nameSet.addAction(Actions.alpha(0));
		//nameSet.addAction(Actions.alpha(1, 1));

		}
	}
	
	
	@Override
	public void hide() {
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		stage.dispose();
		for(Texture t: textures) {
			t.dispose();
		}
		//skin.dispose();
	}

}