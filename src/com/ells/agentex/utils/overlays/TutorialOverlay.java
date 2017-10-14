package com.ells.agentex.utils.overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;

public class TutorialOverlay {
	Array<Image> images = new Array<Image>();
	ImageButton button, button2;
	Array<Texture> textures = new Array<Texture>();
	public int currentImage = 0;
	Skin skin;
	Stage uiStage;
	private AgentExGame game;

	public TutorialOverlay(Skin skin) {
		game = (AgentExGame) Gdx.app.getApplicationListener();
		this.skin = skin;
		button = new ImageButton(skin);
		button.setColor(Color.CLEAR);
		button.setFillParent(true);
		
		button2 = new ImageButton(skin);
		button2.setColor(Color.CLEAR);
		button2.setFillParent(true);
	}

	public void loadImages(Array<String> imageNames) {
		images.clear();
		currentImage = 0;
		for (String s : imageNames) {
			Texture t = new Texture(Gdx.files.internal(s));
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			Image img = new Image(t);
			textures.add(t);
			/*
			 * float SCALE_RATIO = img.getWidth() / img.getHeight();
			 * img.setSize(img.getWidth()/SCALE_RATIO*Gdx.graphics.getWidth()/
			 * 500, img.getHeight()/SCALE_RATIO*Gdx.graphics.getWidth()/500);
			 * img.setPosition((Gdx.graphics.getWidth()/2) - img.getWidth()/2,
			 * (Gdx.graphics.getHeight()/2) - img.getHeight()/2);
			 */
			//float ratio = Gdx.graphics.getWidth() / img.getWidth();
			float ratio = Gdx.graphics.getWidth() / 1280f;
			img.setSize(img.getWidth() * ratio, img.getHeight() * ratio);
			img.setPosition((Gdx.graphics.getWidth()*2/3) - img.getWidth()/2, (Gdx.graphics.getHeight()/2) - img.getHeight()/2);
			img.setTouchable(Touchable.disabled);
			images.add(img);
		}
	}

	public void initializeListener(final Stage stage) {
		this.uiStage = stage;
		button.clearListeners();
		button.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (currentImage < 100) {
					try {
						images.get(currentImage).remove();
					} catch (Exception e) {

					}
				}
				currentImage++;
				if (images.size > currentImage) {
					stage.addActor(images.get(currentImage));
				} else {
					TutorialOverlay.this.button.remove();
					currentImage = 100;
					for (Texture i : textures) {
						i.dispose();
					}
				}
			}
		});
	}

	public void addToStage() {
		currentImage = 0;
		if (images.size > 0) {
			//was reversed
			uiStage.addActor(images.get(0));
			uiStage.addActor(button);
		}
	}
	
	public void addToStageAfterTutorial() {
		button.clearListeners();
		button.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				if (currentImage < 100) {
					try {
						images.get(currentImage).remove();
					} catch (Exception e) {

					}
				}
				currentImage++;
				if (images.size > currentImage) {
					uiStage.addActor(images.get(currentImage));
				} else {
					TutorialOverlay.this.button.remove();
					currentImage = 100;
					for (Texture i : textures) {
						i.dispose();
					}
					game.manager.get("PauseOverlay", PauseOverlay.class).showAfterTutorial();
					
				}
			}
		});
		currentImage = 0;
		if (images.size > 0) {
			//was reversed
			uiStage.addActor(images.get(0));
			uiStage.addActor(button);
		}
		
	}
	
	public void showUp(Stage stage) {
		currentImage = 0;
		if(images.size > 0) {
		stage.addActor(images.get(0));
		stage.addActor(button);
		}
	}

	public int size() {
		return images.size;
	}
}
