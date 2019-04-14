package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;

import java.util.ArrayList;

class ChunkManager {

    private static final int CHUNK_WIDTH = 16;
    private static final int CHUNK_HEIGHT = 9;

    private ChunkBuilder chunkBuilder;
    private ArrayList<Chunk> chunkQueue;

    private Vector2 screenSize;

    ChunkManager() {
        chunkBuilder = new ChunkBuilder();
        chunkQueue = new ArrayList<>();

        screenSize = Vector2.ZERO;
    }

    public void addToQueue(Chunk chunk) {
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
        for (Chunk chunk: chunkQueue) {
            chunk.updateChunk(deltaTime);
        }
    }

}
