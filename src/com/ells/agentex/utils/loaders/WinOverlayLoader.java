package com.ells.agentex.utils.loaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.utils.overlays.WinOverlay;

public class WinOverlayLoader extends AsynchronousAssetLoader<WinOverlay, WinOverlayLoader.WinOverlayLoaderParameter>{
	public WinOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class WinOverlayLoaderParameter extends AssetLoaderParameters<WinOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, WinOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public WinOverlay loadSync (AssetManager manager, String fileName, FileHandle file, WinOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		WinOverlay winOverlay = new WinOverlay(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), parameter.game);
		return winOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, WinOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
