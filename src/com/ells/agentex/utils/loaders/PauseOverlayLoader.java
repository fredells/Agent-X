package com.ells.agentex.utils.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.utils.overlays.PauseOverlay;

public class PauseOverlayLoader extends AsynchronousAssetLoader<PauseOverlay, PauseOverlayLoader.PauseOverlayLoaderParameter>{
	public PauseOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class PauseOverlayLoaderParameter extends AssetLoaderParameters<PauseOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, PauseOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public PauseOverlay loadSync (AssetManager manager, String fileName, FileHandle file, PauseOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		PauseOverlay pauseOverlay = new PauseOverlay(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), parameter.game.manager.get("data/uiskin.json", Skin.class));
		return pauseOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, PauseOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
