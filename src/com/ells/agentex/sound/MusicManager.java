package com.ells.agentex.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class MusicManager extends Actor implements Disposable {
	// little note, make sure not to set audio negative, it fucks stuff up

	public enum MUSICSTATE {
		MENU, INGAME
	}

	private final AssetManager assets;

	private String current = null;
	private String fadeOutCurrent;
	private String pauseOutCurrent;
	private String fadeInCurrent;
	private float volume;
	private boolean muted;
	private float tempVol;
	private float fadeInVolume;
	private float pauseOutVol;
	private ObjectMap<MUSICSTATE, String> songs = new ObjectMap<MUSICSTATE, String>();

	private RepeatAction a;
	private RepeatAction b;
	private RepeatAction c;

	public MusicManager(boolean muted, float volume, AssetManager assets) {
		this.assets = assets;
		this.muted = muted;
		System.out.println("MUTED SETTING: " + muted);
		this.volume = 0.4f;
		Array<String> songNames = new Array<String>();
		songNames.add("Sound/Game-Menu_Looping.mp3");
		songNames.add("Sound/Background #2.ogg");
		Array<MUSICSTATE> states = new Array<MUSICSTATE>();
		states.add(MUSICSTATE.MENU);
		states.add(MUSICSTATE.INGAME);
		this.addMusic(songNames, states);
	}

	public void addMusic(Array<String> songNames, Array<MUSICSTATE> states) {
		for (int i = 0; i < songNames.size; i++) {
			try {
				songs.put(states.get(i), songNames.get(i));
			} catch (Exception e) {
				Gdx.app.log("Music error", "couldn't load " + songNames.get(i));
			}
		}
	}

	public void changeTrack(MUSICSTATE m) {
		if (current != songs.get(m)) {
			this.play(songs.get(m));
		}
	}

	public void play(String fileName) {
		if (muted) {
			System.out.println("MUSIC MUTED");
			return;
		}
		
		else if (current == fileName && !this.isPlaying()) {
			System.out.println("MUSIC RESUMED");
			this.resume();
			return;
		}
		else {
			instantStop();
			System.out.println("MUSIC STOPPED");
		}
		Gdx.app.log("AgentEx sound", "Playing music: " + fileName);

		Music resource = assets.get(fileName, Music.class);

		resource.setVolume(volume);
		resource.play();
		resource.setLooping(true);
		
		fadeInVolume = 0;
		fadeOutCurrent = current;
		
		if (fadeOutCurrent != null) {
			b = Actions.forever(Actions.run(new Runnable() {
				@Override
				public void run() {
					fadeInVolume += Gdx.graphics.getDeltaTime()*5;
					if (fadeInVolume > volume) {
						MusicManager.this.assets.get(fadeOutCurrent, Music.class).setVolume(fadeInVolume);
						MusicManager.this.removeAction(b);
						b.finish();
					} else {
						MusicManager.this.assets.get(fadeOutCurrent, Music.class).setVolume(fadeInVolume);
					}

				}

			}));
			this.addAction(b);
		}
		current = fileName;
	}

	public void resume() {
		if (current != null) {
			assets.get(current, Music.class).setVolume(volume);
			assets.get(current, Music.class).play();
		}
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
		if (muted)
			stop();
		current = null;
	}

	public void dispose() {
		Gdx.app.log("AgentEx sound", "Disposing music manager");
		stop();
	}

	public boolean isPlaying() {
		if (current != null) {
			return assets.get(current, Music.class).isPlaying();
		}
		return false;
	}

	public void stop() {
		if (current != null) {
			fadeOutCurrent = current;
			tempVol = volume;
			//current = "";
			c = Actions.forever(Actions.run(new Runnable() {
				@Override
				public void run() {
					tempVol -= Gdx.graphics.getDeltaTime()*5;
					if (tempVol < 0) {
						MusicManager.this.removeAction(c);
						c.finish();
						MusicManager.this.assets.get(fadeOutCurrent, Music.class).stop();
					} else {
						MusicManager.this.assets.get(fadeOutCurrent, Music.class).setVolume(tempVol);
					}

				}
			}));
			this.addAction(c);
		}
	}

	public void instantStop() {
		if (current != null) {
			MusicManager.this.assets.get(current, Music.class).stop();
		}
	}

	public void setVolume(float volume) {
		Gdx.app.log("AgentEx sound", "Adjusting music volume to: " + volume);

		this.volume = volume;

		if (current != null)
			assets.get(current, Music.class).setVolume(volume);
	}

	public float getVolume() {
		return this.volume;
	}

	public boolean isMuted() {
		return muted;
	}

	public void pause() {
		if (current != null) {
			pauseOutCurrent = current;
			pauseOutVol = volume;
			a = Actions.forever(Actions.run(new Runnable() {
				@Override
				public void run() {
					pauseOutVol -= Gdx.graphics.getDeltaTime()*5;
					if (pauseOutVol < 0) {
						MusicManager.this.removeAction(a);
						MusicManager.this.assets.get(pauseOutCurrent, Music.class).pause();
						a.finish();
					} else {
						MusicManager.this.assets.get(pauseOutCurrent, Music.class).setVolume(pauseOutVol);
					}

				}
			}));
			this.addAction(a);
		}
	}
}
//
// public void fadeStop() {
// tempVol = volume;
// fadeOutCurrent = current;
// Timer.schedule(new Task() {
// @Override
// public void run() {
// tempVol -= 0.05f;
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(tempVol);
// if (MusicManager.this.assets.get(fadeOutCurrent, Music.class).getVolume()
// < 0) {
// this.cancel();
// System.out.println("VOLUME NEGATIVE");
// MusicManager.this.stop();
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(volume);
// // assets.get(current, Music.class).setVolume(volume);
// }
// System.out.println(assets.get(fadeOutCurrent, Music.class).getVolume());
// System.out.println(tempVol);
// }
// }, 0, 0.05f);
// }
//
// public void fadePause() {
// if(current == null)
// return;
// tempVol = volume;
// fadeOutCurrent = current;
// Timer.schedule(new Task() {
// @Override
// public void run() {
// tempVol -= 0.05f;
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(tempVol);
// if (MusicManager.this.assets.get(fadeOutCurrent, Music.class).getVolume()
// < 0) {
// this.cancel();
// System.out.println("VOLUME NEGATIVE");
// MusicManager.this.pause();
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(volume);
// // assets.get(current, Music.class).setVolume(volume);
// }
// System.out.println(assets.get(fadeOutCurrent, Music.class).getVolume());
// System.out.println(tempVol);
// }
// }, 0, 0.05f);
// }
//
//
//
// public void fadeResume() {
// if(current == null)
// return;
// tempVol = 0;
// this.stop();
// fadeOutCurrent = current;
// MusicManager.this.assets.get(fadeOutCurrent, Music.class).play();
// Timer.schedule(new Task() {
// @Override
// public void run() {
// tempVol += 0.05f;
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(tempVol);
// if (MusicManager.this.assets.get(fadeOutCurrent, Music.class).getVolume()
// > volume) {
// this.cancel();
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(volume);
// // assets.get(current, Music.class).setVolume(volume);
// }
// System.out.println(assets.get(fadeOutCurrent, Music.class).getVolume());
// System.out.println(tempVol);
// }
// }, 0, 0.05f);
// }
//
// public void fadeResume(String songName) {
// tempVol = 0;
// fadeOutCurrent = songName;
// this.stop();
// current = songName;
// MusicManager.this.assets.get(fadeOutCurrent, Music.class).play();
// Timer.schedule(new Task() {
// @Override
// public void run() {
// tempVol += 0.05f;
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(tempVol);
// if (MusicManager.this.assets.get(fadeOutCurrent, Music.class).getVolume()
// > volume) {
// this.cancel();
// MusicManager.this.assets.get(fadeOutCurrent,
// Music.class).setVolume(volume);
// // assets.get(current, Music.class).setVolume(volume);
// }
// System.out.println(assets.get(fadeOutCurrent, Music.class).getVolume());
// System.out.println(tempVol);
// }
// }, 0, 0.05f);
// }