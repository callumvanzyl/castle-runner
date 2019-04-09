package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.logging.Handler;

class GameThread extends Thread {

    private enum STATE {
        RUNNING,
        PAUSED
    }

    private GameView gameView;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private Handler messageHandler;

    private STATE state;

    private boolean isRunning;

    private long elapsed;
    private long now;

    GameThread(GameView gameView) {
        this.gameView = gameView;
        this.surfaceHolder = gameView.getHolder();
        this.context = gameView.getContext();
        this.messageHandler = gameView.getMessageHandler();

        isRunning = true;
    }

    private void doDraw(Canvas canvas) {
        if (canvas != null) {

        }
    }

    @Override
    public void run() {
        while (isRunning) {
            Canvas canvas = null;
            try {
                canvas = surfaceHolder.lockCanvas(null);
                synchronized (surfaceHolder) {
                    if (state == STATE.RUNNING) {
                        // Update code
                    }
                }
                doDraw(canvas);
            } finally {
                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
