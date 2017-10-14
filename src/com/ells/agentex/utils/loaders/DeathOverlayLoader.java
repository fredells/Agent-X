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
import com.ells.agentex.utils.overlays.DeathOverlay;

public class DeathOverlayLoader extends AsynchronousAssetLoader<DeathOverlay, DeathOverlayLoader.DeathOverlayLoaderParameter>{
	public DeathOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class DeathOverlayLoaderParameter extends AssetLoaderParameters<DeathOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, DeathOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public DeathOverlay loadSync (AssetManager manager, String fileName, FileHandle file, DeathOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		DeathOverlay deathOverlay = new DeathOverlay(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), parameter.game.manager.get("data/uiskin.json", Skin.class), parameter.game);
		return deathOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, DeathOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
