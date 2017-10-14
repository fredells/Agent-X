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
import com.ells.agentex.utils.overlays.PreGameOverlay;

public class PreGameOverlayLoader extends AsynchronousAssetLoader<PreGameOverlay, PreGameOverlayLoader.PreGameOverlayLoaderParameter>{
	public PreGameOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class PreGameOverlayLoaderParameter extends AssetLoaderParameters<PreGameOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, PreGameOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public PreGameOverlay loadSync (AssetManager manager, String fileName, FileHandle file, PreGameOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		PreGameOverlay preGameOverlay = new PreGameOverlay(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), parameter.game.manager.get("data/uiskin.json", Skin.class), parameter.game);
		return preGameOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, PreGameOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
