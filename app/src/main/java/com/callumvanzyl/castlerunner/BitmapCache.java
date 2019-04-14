package com.callumvanzyl.castlerunner;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

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
            InputStream inputStream;
            try {
                inputStream = assetManager.open(path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                Bitmap image = BitmapFactory.decodeStream(inputStream, null, options);
                cache.put(path, image);
                return image;
            } catch (Exception error) {
                Log.e("CR-ERRORS", "Invalid path", error);
            }
        }
        return null;
    }

}
