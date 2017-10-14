package com.ells.agentex.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Equation {
	float power;
	float aValue;
	float horizontalTranslation;
	float verticalTranslation;
	float kValue;
	int lines = 0;
	//should be less ugly, should be fixed, im just lazy
	public Vector2[] line1;
	public Vector2[] line2;
	public Vector2[] line3;

	public Equation(float aValue, float kValue, float horizontalTranslation, float verticalTranslation, float power) {
		this.aValue = aValue;
		this.horizontalTranslation = horizontalTranslation;
		this.verticalTranslation = verticalTranslation;
		this.power = power;
		this.kValue = kValue;
	}
	public float output(float input) {
		float yValue = (float)((aValue)*(Math.pow(kValue*(input - horizontalTranslation), power)) + verticalTranslation);
		return yValue;
	}
	public void resetLines() {
		line1 = null;
		line2 = null;
		lines = 0;
	}
	public Array<Float> getFormula() {
		Array<Float> formula = new Array<Float>();
		formula.add(aValue);
		formula.add(power);
		formula.add(horizontalTranslation);
		formula.add(verticalTranslation);
		return formula;
			
	}
	public int getLines() {
		return lines;
	}
	public void setLines(int lines) {
		this.lines = lines;
	}
	public Vector2[] getLine1() {
		return line1;
	}
	public void setLine1(Vector2[] line1) {
		this.line1 = line1;
	}
	public Vector2[] getLine2() {
		return line2;
	}
	public void setLine2(Vector2[] line2) {
		this.line2 = line2;
	}
	public Vector2[] getLine3() {
		return line3;
	}
	public void setLine3(Vector2[] line3) {
		this.line3 = line3;
	}
	public float getkValue() {
		return kValue;
	}
	public float getPower() {
		return power;
	}
	public void setPower(float power) {
		this.power = power;
	}
	public float getaValue() {
		return aValue;
	}
	public void setaValue(float aValue) {
		this.aValue = aValue;
	}
	public float getHorizontalTranslation() {
		return horizontalTranslation;
	}
	public void setHorizontalTranslation(float horizontalTranslation) {
		this.horizontalTranslation = horizontalTranslation;
	}
	public float getVerticalTranslation() {
		return verticalTranslation;
	}
	public void setVerticalTranslation(float verticalTranslation) {
		this.verticalTranslation = verticalTranslation;
	}
	public float getAValue() {
		return aValue;
	}
	public void setkValue(float kValue) {
		this.kValue = kValue;
	}
	@Override
	public String toString() {
		return "aValue = " + aValue+ "   power "+ power+ "  horizontal Translation " + horizontalTranslation + "  verticalTranslation " + verticalTranslation + " kValue " + kValue;
		
	}
}
