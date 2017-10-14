package com.ells.agentex.utils.loaders;


import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.utils.overlays.DarkOverlay;

public class DarkOverlayLoader extends AsynchronousAssetLoader<DarkOverlay, DarkOverlayLoader.DarkOverlayLoaderParameter>{
	public DarkOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class DarkOverlayLoaderParameter extends AssetLoaderParameters<DarkOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, DarkOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public DarkOverlay loadSync (AssetManager manager, String fileName, FileHandle file, DarkOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		DarkOverlay deathOverlay = new DarkOverlay(parameter.game);
		return deathOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, DarkOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}

