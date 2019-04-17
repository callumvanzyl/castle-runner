package com.callumvanzyl.castlerunner;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;

class ScaledBitmapCache {

    private HashMap<String, Bitmap> cache = new HashMap<>();

    private AssetManager assetManager;

    ScaledBitmapCache(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    Bitmap get(String path, Vector2 size) {
        Bitmap cached = cache.get(path);
        if (cached != null && cached.getWidth() == size.x && cached.getHeight() == size.y) {
            return cached;
        } else {
            InputStream inputStream;
            try {
                inputStream = assetManager.open(path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;

                Log.d("aa", "CACHE");

                Bitmap image = BitmapFactory.decodeStream(inputStream, null, options);
                image = Bitmap.createScaledBitmap(image, size.x, size.y, false);
                cache.put(path, image);
                return image;
            } catch (Exception error) {
                Log.e("CR-ERRORS", "Invalid path", error);
            }
        }
        return null;
    }

}
