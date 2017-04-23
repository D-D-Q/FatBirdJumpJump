package com.huanshi.game.support.spriter;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
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
public class SpriterDataLoader extends SynchronousAssetLoader<Data, SpriterDataLoader.Parameters> {
	
	/**
	 * scml文件和对应的Loader
	 */
	private ObjectMap<String, Loader<Sprite>> Loaders;
	
	public SpriterDataLoader(FileHandleResolver resolver) {
		super(resolver);
		Loaders = new ObjectMap<>(4); // TODO 默认Spriter的scml文件数量
	}

	/**
	 * 第二个执行，同步。返回已加载的资源
	 */
	@Override
	public Data load(AssetManager assetManager, String fileName, FileHandle file, Parameters parameter) {
		
		Data spriterData = new SCMLReader(file.read()).getData();

		Loader<Sprite> loader = new SpriterLibGdxLoader(spriterData);
		loader.load(file.file());
		
		Loaders.put(fileName, loader);
		
		return spriterData;
	}
	
	/**
	 * 获得Spriter的loader
	 * 
	 * @param scml
	 * @return
	 */
	public Loader<Sprite> getLoader(String scml){
		return Loaders.get(scml);
	}
	
	/**
	 * 销毁
	 */
	public void dispose(){
		
		for(Loader<Sprite> loader : Loaders.values())
			loader.dispose();
	}
	
	/**
	 * 第一个执行,异步还是同步，取决于依赖资源的类型
	 */
	@Override
	public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameters parameter) {
		return null;
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
