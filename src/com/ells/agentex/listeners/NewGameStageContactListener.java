package com.ells.agentex.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.ells.agentex.actors.Axis;
import com.ells.agentex.screens.LevelScreen;
import com.ells.agentex.stages.NewGameStage;
import com.ells.agentex.tables.CoinCounter;
import com.ells.agentex.utils.SimpleSpatial;
import com.ells.agentex.utils.overlays.WinOverlay;

public class NewGameStageContactListener implements ContactListener{
	
	private NewGameStage stage;
	private Array<Integer> collectedCoins = new Array<Integer>();

	public NewGameStageContactListener(NewGameStage stage) {
		this.stage = stage;
	}

	private boolean isHead (Body body) {
		return body.getUserData() != null &&  
			   body.getUserData().equals("head");
	}

	private boolean isWheel (Body body) {
		return body.getUserData() != null &&  
				body.getUserData().toString().contains("wheel");
	}

	private boolean isCoin (Body body) {
		return body.getUserData() != null && 
			   body.getUserData().toString().contains("coin");
	}

	private boolean isBoost (Body body) {
		return body.getUserData() != null && 
			   body.getUserData() instanceof ParticleEffect &&
			   stage.getBoostParticles().contains((ParticleEffect) body.getUserData(), true);
	}

	private boolean isFire (Body body) {
		return body.getUserData() != null && 
			   body.getUserData() instanceof ParticleEffect &&
			   stage.getFireParticles().contains((ParticleEffect) body.getUserData(), true);
	}

	private boolean isPortal (Body body) {
		return body.getUserData() != null && 
			   (body.getUserData()).equals(stage.getPortalParticle());
	}

	private String bodyString (Body b){
		return (String) (isCoin(b) ? "coin " + stage.getmScene().getCustom(b, "coinNumber", null) :
		                isWheel(b) ? "wheel "  + stage.getmScene().getCustom(b, "type", null) :
		                (b.getUserData() instanceof String) ? b.getUserData() :
		                "MYSTERY");
	}

	private String contactString (Contact c) {
		return bodyString(c.getFixtureA().getBody()) + " -> " + 
	           bodyString(c.getFixtureB().getBody());
	}
	

	@Override
	public void beginContact(Contact contact) {
		//Gdx.app.log("debugging", "WHEEL COLLISIONS: " + stage.getPlayer().getWheelCollisions());
		
		Body player, other;
		if (stage.isPlayer(contact.getFixtureA())) {
			player = contact.getFixtureA().getBody();
			other = contact.getFixtureB().getBody();
		}
		else if (stage.isPlayer(contact.getFixtureB())) {
			player = contact.getFixtureB().getBody();
			other = contact.getFixtureA().getBody();
		} 
		else return;
		
		if (isBoost(other)) {
			stage.getPlayer().addWheelCollisions();
			return;
		}
		
		else if (isCoin(other) || isFire(other) || isPortal(other)) {
			return;
		}

		if (isHead(player)) {
			if (!stage.getPlayer().isDead()) {
				stage.getPlayer().setDead(true);
				stage.getGame().fxManager.play("Sound/death.ogg");
			}
		}

		if (isWheel(player) && !isCoin(other)) {
			stage.getPlayer().addWheelCollisions();
			if (stage.getPlayer().getPlayer().getLinearVelocity().y < -6 && TimeUtils.millis() - stage.lastHit > 100) {
				// stage.getGame().loader.assetManager.get("Sound/impact.ogg",
				// Sound.class)
				// .play(Math.abs(stage.getPlayer().getPlayer().getLinearVelocity().y)
				// / 48, 1, 0.05f);
				stage.getGame().fxManager.play("Sound/impact.ogg");
			}
			stage.lastHit = TimeUtils.millis();
			// TO FIX: this seems terrible
			//System.out.println("time " + TimeUtils.millis() + " stage.lastHit " +stage.lastHit);
			stage.flipAction(stage.getPlayer().getFrontFlips(), stage.getPlayer().getBackFlips());
		}
	}

	@Override
	public void endContact(Contact contact) {
		//Gdx.app.log("debugging", "WHEEL COLLISIONS: " + stage.getPlayer().getWheelCollisions());
		
		Fixture fixtureA = contact.getFixtureA();
		Fixture fixtureB = contact.getFixtureB();
		Body bodyA = fixtureA.getBody();
		Body bodyB = fixtureB.getBody();

		// all this section is for wheel collisions and flips so far
		if ((bodyA.getUserData() != null)) {
			if (bodyA.getUserData().equals("wheel") && !isBoost(bodyB)) {
				stage.getPlayer().subWheelCollisions();
			}
		}

		if ((bodyB.getUserData() != null)) {
			if (bodyB.getUserData().equals("wheel") && !isBoost(bodyB)) {
				stage.getPlayer().subWheelCollisions();
			}
		}
	}


	private void collectCoin (Body coin) {
		Integer index = (Integer) stage.getmScene().getCustom(coin, "coinNumber", null);
		Array<Float> coinFlags = (Array<Float>) stage.getGame().loader.nestedLevelProfile.get(LevelScreen.currentLevel).get("coins");
		Boolean firstTimeGlobal = coinFlags.get(index) == 0;
		Boolean firstTimeLocal = stage.getCoinsCollected().get(index) == 0;
		
		/*Gdx.app.log("debugging", "firstTimeGlobal: " + firstTimeGlobal );
		Gdx.app.log("debugging", "firstTimeLocal : " + firstTimeLocal);*/
		
		/*Gdx.app.log("debugging", "coinFlags Perm : " + coinFlags );
		Gdx.app.log("debugging", "coinFlags Temp : " + stage.getCoinsCollected() );
		Gdx.app.log("debugging", "coin           : " + index );
		Gdx.app.log("debugging", "firstTimeGlobal: " + firstTimeGlobal );
		Gdx.app.log("debugging", "firstTimeLocal : " + firstTimeLocal);*/
		
		if (!collectedCoins.contains(index, true)) {
			if (firstTimeGlobal) {
				stage.bonusCoins++;
				stage.getCoinsCollected().set(index, 1f);
			}
			
			Gdx.app.log("debugging", "COIN COLLECTED : " + index);
			stage.destroy(coin);
			stage.coinLabel(firstTimeGlobal);
			stage.getCoinMap().get(index).mSprite.setAlpha(0);
			stage.getGame().fxManager.play("Sound/pickup.ogg");
			collectedCoins.add(index);
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		//Gdx.app.log("debugging", "NewGameStageContactListener.preSolve :: " + contactString(contact));
		
		
		Body player, other;
		if (stage.isPlayer(contact.getFixtureA())) {
			player = contact.getFixtureA().getBody();
			other = contact.getFixtureB().getBody();
		}
		else if (stage.isPlayer(contact.getFixtureB())) {
			player = contact.getFixtureB().getBody();
			other = contact.getFixtureA().getBody();
		} 
		else return;

		if (isCoin(other)) {
			contact.setEnabled(false);
			collectCoin(other);
		}
		else if (isBoost(other) || isBoost(player)) {
			contact.setEnabled(false);
			stage.getPlayer().setBoostAngle(other.getAngle());
			stage.getPlayer().setBoosting(true);
			System.out.println("boosting!");
		}
		else if (isFire(other)) {
			contact.setEnabled(false);
			stage.getPlayer().setDead(true);
			stage.getGame().fxManager.play("Sound/death.ogg");
		}
		else if (isPortal(other)){
			if (stage.deadState == stage.deadState.ALIVE) {
				contact.setEnabled(false);
				if (stage.isWinState() == false) {
					stage.setWinState(true);
					stage.levelComplete();
					stage.getGame().manager.get("WinOverlay", WinOverlay.class).addWinOverlay(stage.getUiStage());
					CoinCounter bitcoins = new CoinCounter(stage.getGame());
					stage.getUiStage().addActor(bitcoins);
				}
			}
		}
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

}
