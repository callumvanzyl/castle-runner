package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

class ChunkManager {

    private static final int BUFFER_SIZE = 5;
    private static final int TILE_SIZE = 120;

    private int scrollingSpeed;

    private boolean hasUpdated = false;

    private static final String[] ACTIVE_CHUNKS = {
            "data/chunks/flat-monster",
            "data/chunks/flat-small",
            "data/chunks/flat-small-coins",
            "data/chunks/floaty-big",
            "data/chunks/floaty-big-monster",
            "data/chunks/jumpy-small",
            "data/chunks/platforms-monster",
            "data/chunks/platforms-small"
    };

    private GameContext gameContext;

    private ChunkBuilder chunkBuilder;
    private ArrayList<Chunk> chunkQueue;

    private Vector2 screenSize;

    ChunkManager(GameContext gameContext) {
        this.gameContext = gameContext;

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
            if (hasUpdated) {
                int rand = new Random().nextInt(ACTIVE_CHUNKS.length);
                chunkBuilder.generate(gameContext, ACTIVE_CHUNKS[rand]);
            } else {
                chunkBuilder.generate(gameContext, "data/chunks/flat-big");
                hasUpdated = true;
            }
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

            int offsetX = (int) (scrollingSpeed*(deltaTime/100));
            chunk.offsetChunk(new Vector2(-offsetX, 0));
        }
    }

    public void setScrollingSpeed(int scrollingSpeed) {
        this.scrollingSpeed = scrollingSpeed;
    }

    public ArrayList<Chunk> getActiveChunks() {
        return chunkQueue;
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }
}
