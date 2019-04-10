package com.callumvanzyl.castlerunner;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.util.HashMap;

class BitmapCache {

    private HashMap<String, Bitmap> cache = new HashMap<>();

    private AssetManager assetManager;

    BitmapCache(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Bitmap get(String path) {
        Bitmap cached = cache.get(path);
        if (cached != null) {
            return cached;
        } else {
            InputStream inputStream = null;
            try {
                inputStream = assetManager.open(path);
                Bitmap image = BitmapFactory.decodeStream(inputStream);
                cache.put(path, image);
                return image;
            } catch (Exception ignored) {}
        }
        return null;
    }

}