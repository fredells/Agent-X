package com.ells.agentex.utils.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ells.agentex.AgentExGame;
import com.ells.agentex.utils.overlays.TutorialOverlay;

public class TutorialOverlayLoader extends AsynchronousAssetLoader<TutorialOverlay, TutorialOverlayLoader.TutorialOverlayLoaderParameter>{
	public TutorialOverlayLoader (FileHandleResolver resolver) {
		super(resolver);
	}

	public static class TutorialOverlayLoaderParameter extends AssetLoaderParameters<TutorialOverlay>{
		public AgentExGame game;
	}

	@Override
	public void loadAsync (AssetManager manager, String fileName, FileHandle file, TutorialOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");		
	}

	@Override
	public TutorialOverlay loadSync (AssetManager manager, String fileName, FileHandle file, TutorialOverlayLoaderParameter parameter) {
		if(parameter == null) throw new RuntimeException("FreetypeFontParameter must be set in AssetManager#load to point at a TTF file!");
		
		TutorialOverlay tutorialOverlay = new TutorialOverlay(parameter.game.manager.get("data/uiskin.json", Skin.class));
		return tutorialOverlay;
	}

	@Override
	public Array<AssetDescriptor> getDependencies (String fileName, FileHandle file, TutorialOverlayLoaderParameter parameter) {
		Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
		return deps;
	}
}
