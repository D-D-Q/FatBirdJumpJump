package com.pending.game.support.spriter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.brashmonkey.spriter.Data;
import com.brashmonkey.spriter.Loader;
import com.brashmonkey.spriter.SCMLReader;

/**
 * 给libgdx的AssetManager使用的loader
 * 封装使用了SpriterLibGdxLoader和SpriterLibGdxAtlasLoader
 * 
 * @author D
 * @date 2017年3月6日
 */
public class SpriterDataLoader extends AsynchronousAssetLoader<Data, SpriterDataLoader.Parameters> {
	
	/**
	 * scml文件和对应的Loader
	 */
	private ObjectMap<String, Loader<Sprite>> Loaders;
	
	public SpriterDataLoader(FileHandleResolver resolver) {
		super(resolver);
		Loaders = new ObjectMap<>(4); // TODO 默认Spriter的scml文件数量
	}

	@Override
	public void loadAsync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
		
		Data data = new SCMLReader(file.read()).getData();

		Loader<Sprite> loader = new SpriterLibGdxLoader(data);
		loader.load(file.file());
		
		Loaders.put(fileName, loader);
	}

	@Override
	public Data loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
		
		Loader<Sprite> loader = Loaders.get(fileName);
		return null;
	}

	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter) {
		
		return null;
	}
	
	public Loader<Sprite> getLoader(String scml){
		return Loaders.get(scml);
	}
	
	public static class Parameters extends AssetLoaderParameters<Data> {
		
		/**
		 * 资源是否是TextureAtlas格式
		 */
		public final boolean isTextureAtlas;
		
		public Parameters() {
			isTextureAtlas = false;
		}

		public Parameters(boolean isTextureAtlas) {
			this.isTextureAtlas = isTextureAtlas;
		}
	}
}
