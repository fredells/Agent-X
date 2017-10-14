package com.ells.agentex.tables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ells.agentex.actors.ChangingOpacityLabel;
import com.ells.agentex.actors.OverflowButton;

public class OptionsOverlay extends Table{
	
	private Slider slider;
	private boolean optionsVisible = false;
	private ChangingOpacityLabel sliderLabel;

	public OptionsOverlay(Skin skin) {
		super();
		this.debug();
		this.setFillParent(true);
		slider = new Slider(0.2f, 1, 0.01f, false, skin);
		slider.setValue(Gdx.app.getPreferences("agentx").getFloat("equationTableOpacity"));
		 sliderLabel = new ChangingOpacityLabel("Equation Table Opacity", skin, "style");
		this.add(sliderLabel);
		OverflowButton close = new OverflowButton(skin);
		close.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				OptionsOverlay.this.remove();
			}
		});

		this.add(close).width(Gdx.graphics.getWidth()/15).height(Gdx.graphics.getWidth()/15).padRight(-close.getWidth()).padTop(-close.getHeight());
		this.row();
		this.add(slider);
		
		
	}
	public void update() {
		Gdx.app.getPreferences("agentx").putFloat("equationTableOpacity", slider.getValue());
	}
	
}
