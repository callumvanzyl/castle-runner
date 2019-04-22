package com.callumvanzyl.castlerunner;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.math.MathUtils;
import android.util.Log;
import android.view.MotionEvent;

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

    private Player player;

    private Button jumpButton;
    private Button attackButton;

    private GameObject scorePanel;

    private Paint scorePaint;

    private volatile ScheduledFuture<?> self;

    GameThread(GameContext gameContext) {
        this.gameContext = gameContext;

        isRunning = false;
    }

    public void startGame(int width, int height) {
        Log.d("CR-GAMELOOP", "GameThread has invoked startGame");

        screenSize = new Vector2(width, height);

        background = new ScrollingBackground(gameContext, 25);
        background.changeScreenSize(screenSize);
        background.setCollidable(false);
        background.setSprite("textures/world/background/full.png");

        chunkManager = new ChunkManager(gameContext);
        chunkManager.setScrollingSpeed(50);
        chunkManager.changeScreenSize(screenSize);

        player = new Player(gameContext);
        player.setCollidable(true);
        player.setColliderSizeAndOffset(new Vector2(70, 160), new Vector2(50, 70));
        player.setPosition(new Vector2(128, 0));
        player.setSize(new Vector2(225, 225));
        player.setVelocityEnabled(true);

        jumpButton = new Button(gameContext, "textures/ui/jump_pressed.png", "textures/ui/jump_standard.png");
        jumpButton.setPosition(new Vector2(screenSize.x - 250, screenSize.y - 250));
        jumpButton.setSize(new Vector2(200, 200));
        gameContext.setJumpButton(jumpButton);

        attackButton = new Button(gameContext, "textures/ui/attack_standard.png", "textures/ui/attack_standard.png");
        attackButton.setPosition(new Vector2(screenSize.x - 500, screenSize.y - 250));
        attackButton.setSize(new Vector2(200, 200));
        gameContext.setAttackButton(attackButton);

        scorePanel = new UiElement(gameContext);
        scorePanel.setPosition(new Vector2(screenSize.x - 750, 0));
        scorePanel.setSize(new Vector2(750, 275));
        scorePanel.setSprite("textures/ui/score_panel.png");

        scorePaint = new Paint();
        scorePaint.setColor(Color.WHITE);
        scorePaint.setStyle(Paint.Style.FILL);
        scorePaint.setTextSize(85);

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

        jumpButton.setPosition(new Vector2(screenSize.x - 250, screenSize.y - 250));
        attackButton.setPosition(new Vector2(screenSize.x - 500, screenSize.y - 250));

        scorePanel.setPosition(new Vector2(screenSize.x - 750, 0));

        isRunning = true;
    }

    private void drawGame(Canvas canvas) {
        Log.d("CR-GAMELOOP", "GameThread has invoked drawGame");

        background.draw(canvas);
        chunkManager.drawChunks(canvas);

        player.draw(canvas);

        jumpButton.draw(canvas);
        attackButton.draw(canvas);

        scorePanel.draw(canvas);
        canvas.drawText(Integer.toString(player.getScore()), screenSize.x - 500, 165, scorePaint);
    }

    private void updateGame() {
        Log.d("CR-GAMELOOP", "GameThread has invoked updateGame");

        long currentTime = System.nanoTime();
        float deltaTime = (float) (currentTime - previousTime)/1000000;

        Log.d("CR-PERFORMANCE", "Time since last update: " + Float.toString(deltaTime) + " ms");

        if (deltaTime < 999) {
            background.update(deltaTime);
            chunkManager.updateChunks(deltaTime);

            GameObject.setActiveChunks(chunkManager.getActiveChunks());
            player.update(deltaTime);

            jumpButton.update(deltaTime);
            attackButton.update(deltaTime);

            if (player.isDead()) {
                chunkManager.setScrollingSpeed(0);
                background.setScrollingSpeed(0);
            }
        }

        final Runtime runtime = Runtime.getRuntime();
        final long usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        final long maxHeapSizeInMB=runtime.maxMemory() / 1048576L;
        final long availHeapSizeInMB = maxHeapSizeInMB - usedMemInMB;

        Log.d("CR-PERFORMANCE", "Available memory: " + Long.toString(availHeapSizeInMB) + " MB");

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
