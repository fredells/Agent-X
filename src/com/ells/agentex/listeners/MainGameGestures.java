package com.ells.agentex.listeners;

import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ells.agentex.stages.UIStage;

public class MainGameGestures implements GestureListener {
	private Vector2 oldInitialFirstPointer = null, oldInitialSecondPointer = null;
	private float oldScale;
	private UIStage uiStage;
	private Rectangle bounds;
	private float lastX = 0;
	private float lastY = 0;
	public boolean firstPan = true;
	private Vector2 speed = new Vector2(0, 0);

	public MainGameGestures(UIStage uiStage) {
		this.uiStage = uiStage;
		this.bounds = uiStage.bounds;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {
		// TODO Auto-generated method stub
		if (uiStage.gameMode == "preGame") {
		uiStage.cameraVelocity.x = -velocityX * uiStage.camera.zoom * 0.0005f;
		uiStage.cameraVelocity.y = velocityY * uiStage.camera.zoom * 0.0005f;
		uiStage.setFling(true);
		return false;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean longPress(float arg0, float arg1) {
		System.out.println("longPress");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if (uiStage.gameMode == "preGame") {
		if (!firstPan) {
			speed.x += deltaX;
			speed.y += deltaY;
			uiStage.gameStage.snapMode = false;
			uiStage.camera.translate(-deltaX / 50, deltaY / 50);
			uiStage.camera.update();
		}
		firstPan = false;
		lastX = x;
		lastY = y;
		return false;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean pinch(Vector2 initialFirstPointer, Vector2 initialSecondPointer, Vector2 firstPointer,
			Vector2 secondPointer) {
		if (uiStage.gameMode == "pregame") {
		if (!(initialFirstPointer.equals(oldInitialFirstPointer)
				&& initialSecondPointer.equals(oldInitialSecondPointer))) {
			oldInitialFirstPointer = initialFirstPointer.cpy();
			oldInitialSecondPointer = initialSecondPointer.cpy();
			oldScale = uiStage.camera.zoom;
		}
		Vector3 center = new Vector3((firstPointer.x + initialSecondPointer.x) / 2,
				(firstPointer.y + initialSecondPointer.y) / 2, 0);
		zoomCamera(center, oldScale * initialFirstPointer.dst(initialSecondPointer) / firstPointer.dst(secondPointer));
		return false;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean tap(float x, float y, int arg2, int arg3) {
		if (!uiStage.gameStage.cameraFollow) {
			uiStage.gameStage.cameraSliding = false;
			uiStage.gameStage.cameraFollow = false;
			uiStage.gameStage.snapMode = false;
			Vector3 ThreeDClickCords = new Vector3(uiStage.camera.unproject(new Vector3((float) x, (float) y, 0f)));
			Vector2 TwoDClickCords = new Vector2(ThreeDClickCords.x, ThreeDClickCords.y);

			for (int i = 0; i < uiStage.gameStage.getAxes().size; i++) {

				if (uiStage.gameStage.getAxes().get(i).getHitBox().contains(TwoDClickCords)) {
					for (int j = 0; j < uiStage.gameStage.getAxes().size; j++) {
						if (uiStage.gameStage.getAxes().get(j).getEquationTable() != null) {
							uiStage.gameStage.getAxes().get(j).getEquationTable().remove();
						}
					}
					uiStage.movingToAxis = true;
					uiStage.activeAxis = i;
					uiStage.gameStage.focus = i + 1;
					uiStage.cameraMode = "axisFocus";
					uiStage.gameStage.snapMode = true;
					uiStage.gameStage.cameraFollow = false;
					//experimental
					uiStage.gameStage.setFirstTime(0);
					//uiStage.gameStage.renderAxes(true);
					return true;
				}
			}
		}
		return true;
	}

	@Override
	public boolean touchDown(float x, float y, int arg2, int arg3) {

		return false;
	}

	@Override
	public boolean zoom(float originalDistance, float currentDistance) {
		return false;
	}

	private void zoomCamera(Vector3 origin, float scale) {
		uiStage.camera.update();
		Vector3 oldUnprojection = uiStage.camera.unproject(origin.cpy()).cpy();
		uiStage.camera.zoom = scale; // Larger value of zoom = small images,
										// border view
		uiStage.camera.zoom = Math.min(2.0f, Math.max(uiStage.camera.zoom, 0.5f));
		uiStage.camera.update();
		Vector3 newUnprojection = uiStage.camera.unproject(origin.cpy()).cpy();
		uiStage.camera.position.add(oldUnprojection.cpy().add(newUnprojection.cpy().scl(-1f)));
	}

	@Override
	public boolean panStop(float arg0, float arg1, int arg2, int arg3) {
		return false;
	}

}
