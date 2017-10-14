package com.ells.agentex.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ells.agentex.AgentExGame;

public class TableButton extends ImageButton {
	private float valueToEdit;
	private boolean addOrSubtract;
	private Equation equation;
	private Axis axis;
	private CustomTable table;
	private boolean pressed = false;
	private long pressedStartTime = 0;
	private long pressedTime = 0;

	public float getPressedTime() {
		return pressedTime;
	}

	public void setPressedTime(long pressedTime) {
		this.pressedTime = pressedTime;
	}

	public TableButton(Skin skin, String styleName, float valueToEdit, boolean addOrSubtract, Equation equation,
			Axis axis, CustomTable table) {
		super(new TextureRegionDrawable(new TextureRegion(((AgentExGame)Gdx.app.getApplicationListener()).manager.get("img/"+styleName+".png", Texture.class))));
		this.valueToEdit = valueToEdit;
		this.addOrSubtract = addOrSubtract;
		this.equation = equation;
		this.axis = axis;
		this.table = table;
	}

	public void setValueToEdit(float valueToEdit) {
		this.valueToEdit = valueToEdit;
	}

	public void update(float delta) {
		if (!addOrSubtract) {
			delta = -delta;
		}
		if (valueToEdit == 0) {
			equation.setaValue(equation.getAValue() + delta);
		} else if (valueToEdit == 1) {
			equation.setHorizontalTranslation(equation.horizontalTranslation + 4 * delta);
		} else if (valueToEdit == 2) {
			equation.setVerticalTranslation(equation.verticalTranslation + 4 * delta);
		}
		// so it doesn't try to delet a body that doesn't exist, ie. outside of
		// axis.
		if (axis.getEquation().getLine1() != null) {
			axis.destroyBody(axis.getBody());
		}
		axis.getEquation().resetLines();
		Vector2[] points = axis.equationToPoints();
		if (points.length >= 2) {
			axis.createLine(axis.scene, points);
		}
		table.generateTable(equation, axis.getGame().gameStage.getGame().loader.skin);
	}

	public void update(float transformationAmount, boolean rounded) {
		if (!addOrSubtract) {
			transformationAmount = -transformationAmount;
		}
		if (!rounded) {
			if (valueToEdit == 0) {
				equation.setaValue(equation.getAValue() + transformationAmount);
			} else if (valueToEdit == 1) {
				equation.setHorizontalTranslation(equation.horizontalTranslation + transformationAmount);
			} else if (valueToEdit == 2) {
				equation.setVerticalTranslation(equation.verticalTranslation + transformationAmount);
			}
		} else if(rounded){
			if (addOrSubtract) {
				if (valueToEdit == 0) {
					equation.setaValue((float) (Math.ceil((equation.getAValue()) * 10) / 10)+ transformationAmount/10);
				} else if (valueToEdit == 1) {
					equation.setHorizontalTranslation(
							(float) Math.ceil(equation.horizontalTranslation+ 0.00000000001));
				} else if (valueToEdit == 2) {
					equation.setVerticalTranslation((float) Math.ceil(equation.verticalTranslation+ 0.00000000001));
				}
			} else if (!addOrSubtract){
				if (valueToEdit == 0) {
					equation.setaValue((float) (Math.floor((equation.getAValue()) * 10) / 10) + transformationAmount/10);
				} else if (valueToEdit == 1) {
					equation.setHorizontalTranslation(
							(float) Math.floor(equation.horizontalTranslation- 0.00000000001));
				} else if (valueToEdit == 2) {
					equation.setVerticalTranslation((float) Math.floor(equation.verticalTranslation- 0.00000000001));
				}
			}
		}
		// so it doesn't try to delet a body that doesn't exist, ie. outside of
		// axis.
		if (axis.getEquation().getLine1() != null) {
			axis.destroyBody(axis.getBody());
		}
		axis.getEquation().resetLines();
		Vector2[] points = axis.equationToPoints();
		if (points.length >= 2) {
			axis.createLine(axis.scene, points);
		}
		table.generateTable(equation, axis.getGame().gameStage.getGame().loader.skin);
	}

	public void setPressed(boolean pressed) {
		this.pressed = pressed;
	}

	public boolean isPressed() {
		return pressed;
	}

	public void setPressedStartTime(long time) {
		this.pressedStartTime = time;
	}

	public long getPressedStartTime() {
		return this.pressedStartTime;
	}
}
