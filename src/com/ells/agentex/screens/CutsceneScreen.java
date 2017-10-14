package com.ells.agentex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;

public class CutsceneScreen implements Screen {
	Array<Image> images = new Array<Image>();
	Array<Texture> textures = new Array<Texture>();
	ImageButton button;
	public int currentImage = 0;
	private AgentExGame game;
	Stage stage;
	private Array<String> imageNames;

	public CutsceneScreen(AgentExGame game, Array<String> imageNames) {
		this.game = game;
		this.imageNames = imageNames;
		button = new ImageButton(game.loader.skin);
		button.setColor(Color.CLEAR);
		button.setFillParent(true);
	}

	@Override
	public void dispose() {
		for(Texture t: textures) {
			t.dispose();
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act(delta);
		stage.draw();

	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		stage = new Stage();
		currentImage = 0;
		for (String s : imageNames) {
			textures.add(new Texture(Gdx.files.internal(s)));
			Image img = new Image(textures.peek());
			/*
			 * float SCALE_RATIO = img.getWidth() / img.getHeight();
			 * img.setSize(img.getWidth()/SCALE_RATIO*Gdx.graphics.getWidth()/
			 * 500, img.getHeight()/SCALE_RATIO*Gdx.graphics.getWidth()/500);
			 * img.setPosition((Gdx.graphics.getWidth()/2) - img.getWidth()/2,
			 * (Gdx.graphics.getHeight()/2) - img.getHeight()/2);
			 */
			float ratio = Gdx.graphics.getWidth() / img.getWidth();
			img.setSize(img.getWidth() * ratio, img.getHeight() * ratio);
			img.setPosition((Gdx.graphics.getWidth() / 2) - img.getWidth() / 2,
					(Gdx.graphics.getHeight() / 2) - img.getHeight() / 2);
			img.setTouchable(Touchable.disabled);
			images.add(img);
		}
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				images.get(currentImage).remove();
				currentImage++;
				if(currentImage < images.size) {
				stage.addActor(images.get(currentImage));
				} else {
					game.setScreen(new MenuScreen(game));
				}
				return true;

			}
		});
		stage.addActor(images.get(0));

	}
}
