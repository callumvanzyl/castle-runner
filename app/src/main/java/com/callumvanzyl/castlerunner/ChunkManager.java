package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

class ChunkManager {

    private static final int BUFFER_SIZE = 2;

    private static final String[] ACTIVE_CHUNKS = {
            "data/chunks/generic-01",
            "data/chunks/generic-02",
            "data/chunks/generic-03",
            "data/chunks/generic-04",
    };

    private Context context;

    private ArrayList<Chunk> chunkQueue;

    private Vector2 screenSize;

    ChunkManager(Context context) {
        this.context = context;

        chunkQueue = new ArrayList<>();
    }

    public void addToQueue(Chunk chunk) {
        Vector2 offset = Vector2.ZERO;

        for (Chunk current: chunkQueue) {
            Vector2 bounds = current.getMaxBounds();
            offset = offset.add(new Vector2(bounds.x, 0));
        }

        chunk.offsetChunk(offset);

        chunkQueue.add(chunk);
    }

    public void changeScreenSize(Vector2 screenSize) {
        this.screenSize = screenSize;
    }

    public void drawChunks(Canvas canvas) {
        for (Chunk chunk: chunkQueue) {
            chunk.drawChunk(canvas);
        }
    }

    public void updateChunks(float deltaTime) {
        if (chunkQueue.size() > 0) {
            if (chunkQueue.get(0).getMaxBounds().x < 0) {
                Log.d("CR-CHUNKS", "Removing chunk from queue");
                chunkQueue.remove(0);
            }
        }

        if (chunkQueue.size() < BUFFER_SIZE) {
            Log.d("CR-CHUNKS", "Adding new chunk to queue");

            Chunk chunk = ChunkBuilder.getNext();

            if (chunk == null) {
                int rnd = new Random().nextInt(ACTIVE_CHUNKS.length);
                ChunkBuilder cb = new ChunkBuilder(context, ACTIVE_CHUNKS[rnd]);
                cb.run();
            } else {
                addToQueue(chunk);
            }
        }

        for (Chunk chunk: chunkQueue) {
            chunk.updateChunk(deltaTime);

            int offsetX = (int) (50*(deltaTime/100));
            chunk.offsetChunk(new Vector2(-offsetX, 0));
        }
    }

}
