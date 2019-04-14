package com.callumvanzyl.castlerunner;

import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;

class ChunkBuilder {

    private static Chunk fileToChunk(String path) {
        Log.d("CR-CHUNKS", "Generating chunk from data at " + path);

        String line = null;

        try {
            FileReader fileReader = new FileReader(path);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int current = 0;

        } catch (Exception ignored) {}
    }

}
