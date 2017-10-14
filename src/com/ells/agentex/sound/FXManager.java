package com.ells.agentex.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class FXManager implements Disposable {
	private final AssetManager assets;

	private float volume;
	private boolean muted;
	private float COLLISION_DELAY = 1;
	private ObjectMap<String, Long> sounds = new ObjectMap<String, Long>();

	public FXManager(boolean muted, float volume, AssetManager assets) {
		this.assets = assets;
		this.muted = muted;
		this.volume = 1;
	}

	public void play(String fileName) {
		if (muted) {
			System.out.println("muted");
			return;
		}
		if (!sounds.containsKey(fileName)) {
			sounds.put(fileName, 0l);
		}
		if (TimeUtils.millis() - sounds.get(fileName) > COLLISION_DELAY * 1000) {
			Gdx.app.log("AgentEx sound", "Playing sound: " + fileName);

			Sound resource = assets.get(fileName, Sound.class);
			resource.setVolume(resource.play(), volume);
			sounds.put(fileName, TimeUtils.millis());

		}
	}

	public void setVolume(float volume) {
		Gdx.app.log("AgentEx sound", "Adjusting music volume to: " + volume);
		this.volume = volume;
	}

	public float getVolume() {
		return this.volume;
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public void dispose() {
		Gdx.app.log("AgentEx sound", "Disposing music manager");
	}
}