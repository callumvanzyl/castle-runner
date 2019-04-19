package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

class ChunkManager {

    private static final int BUFFER_SIZE = 5;
    private static final int TILE_SIZE = 120;

    private static final String[] ACTIVE_CHUNKS = {
            "data/chunks/flat-small",
            //"data/chunks/floaty-big",
            //"data/chunks/jumpy-big",
            //"data/chunks/jumpy-small",
            //"data/chunks/platforms-big",
            //"data/chunks/platforms-small",
            //"data/chunks/treasure-hi",
            //"data/chunks/treasure-pyramid",
    };

    private Context context;

    private ChunkBuilder chunkBuilder;
    private ArrayList<Chunk> chunkQueue;

    private Vector2 screenSize;

    ChunkManager(Context context) {
        this.context = context;

        chunkBuilder = new ChunkBuilder();
        chunkQueue = new ArrayList<>();
    }

    public void addToQueue(Chunk chunk) {
        Vector2 offset = Vector2.ZERO;
        if (chunkQueue.size() > 0) {
            offset = offset.add(chunkQueue.get(chunkQueue.size()-1).getMaxBounds().x, 0);
        }
        chunk.offsetChunk(offset.add(new Vector2(TILE_SIZE, 0)));
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
        if (!chunkBuilder.isBusy() && chunkBuilder.getGenerated().size() < BUFFER_SIZE) {
            int rand = new Random().nextInt(ACTIVE_CHUNKS.length);
            chunkBuilder.generate(context, ACTIVE_CHUNKS[rand]);
        }

        if (chunkQueue.size() > 0) {
            if (chunkQueue.get(0).getMaxBounds().x + TILE_SIZE < 0) {
                chunkQueue.remove(0);
            }
        }

        if (chunkQueue.size() == 0) {
            Chunk chunk = chunkBuilder.getNext();
            if (chunk != null) {
                addToQueue(chunk);
            }
        } else {
            if (chunkQueue.get(chunkQueue.size()-1).getMaxBounds().x < screenSize.x) {
                Chunk chunk = chunkBuilder.getNext();
                if (chunk != null) {
                    addToQueue(chunk);
                }
            }
        }

        for (Chunk chunk: chunkQueue) {
            chunk.updateChunk(deltaTime);

            int offsetX = (int) (50*(deltaTime/100));
            chunk.offsetChunk(new Vector2(-offsetX, 0));
        }
    }

    public ArrayList<Chunk> getActiveChunks() {
        return chunkQueue;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }
}
