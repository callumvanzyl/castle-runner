package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class GameThread implements Runnable {

    private static final int TARGET_MSPF = 16;

    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    private GameContext gameContext;
    private Vector2 screenSize;

    private ScrollingBackground background;

    private ChunkManager chunkManager;

    private boolean isRunning;
    private long previousTime;

    private volatile ScheduledFuture<?> self;

    GameThread(GameContext gameContext) {
        this.gameContext = gameContext;

        isRunning = false;
    }

    public void startGame(int width, int height) {
        Log.d("CR-GAMELOOP", "GameThread has invoked startGame");

        screenSize = new Vector2(width, height);

        background = new ScrollingBackground(gameContext.getContext(), 25);
        background.changeScreenSize(screenSize);
        background.setSprite("textures/world/background/full.png");

        chunkManager = new ChunkManager(gameContext.getContext());

        isRunning = true;

        self = executor.scheduleAtFixedRate(this, 0, TARGET_MSPF, TimeUnit.MILLISECONDS);
    }

    public void pauseGame() {
        Log.d("CR-GAMELOOP", "GameThread has invoked pauseGame");

        isRunning = false;
    }

    public void resumeGame(int width, int height) {
        Log.d("CR-GAMELOOP", "GameThread has invoked resumeGame");

        screenSize = new Vector2(width, height);

        background.changeScreenSize(screenSize);
        chunkManager.changeScreenSize(screenSize);

        isRunning = true;
    }

    private void drawGame(Canvas canvas) {
        Log.d("CR-GAMELOOP", "GameThread has invoked drawGame");

        canvas.drawColor(Color.rgb(100, 149, 237));

        background.draw(canvas);
        chunkManager.drawChunks(canvas);
    }

    private void updateGame() {
        Log.d("CR-GAMELOOP", "GameThread has invoked updateGame");

        long currentTime = System.nanoTime();
        float deltaTime = (float) (currentTime - previousTime)/1000000;

        Log.d("CR-PERFORMANCE", "Time since last update: " + Float.toString(deltaTime) + " ms");

        if (deltaTime < 999) {
            background.update(deltaTime);
            chunkManager.updateChunks(deltaTime);
        }

        previousTime = currentTime;
    }

    @Override
    public void run() {
        Canvas canvas = null;
        if (isRunning) {
            updateGame();

            try {
                canvas = gameContext.getSurfaceHolder().lockCanvas(null);
                if (canvas != null) {
                    synchronized (gameContext.getSurfaceHolder()) {
                        drawGame(canvas);
                    }
                }
            } finally {
                if (canvas != null) {
                    gameContext.getSurfaceHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
