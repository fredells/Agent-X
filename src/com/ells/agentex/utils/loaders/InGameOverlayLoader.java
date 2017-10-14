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
import com.ells.agentex.utils.overlays.InGameOverlay;

public class InGameOverlayLoader extends AsynchronousAssetLoader<InGameOverlay, InGameOverlayLoader.InGameOverlayLoaderParameter>{
	public InGameOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class InGameOverlayLoaderParameter extends AssetLoaderParameters<InGameOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, InGameOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public InGameOverlay loadSync (AssetManager manager, String fileName, FileHandle file, InGameOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		InGameOverlay inGameOverlay = new InGameOverlay(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), parameter.game.manager.get("data/uiskin.json", Skin.class), parameter.game);
		return inGameOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, InGameOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
