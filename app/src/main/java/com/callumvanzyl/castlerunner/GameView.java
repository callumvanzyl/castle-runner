package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.mortbay.jetty.Main;

class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameContext gameContext;
    private GameThread gameThread = null;

    private boolean isGameInitialised;

    public boolean isDone = false;

    GameView(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        gameContext = new GameContext(this, context, null, holder);

        isGameInitialised = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (!isGameInitialised) {
            gameThread = new GameThread(gameContext);
            gameThread.startGame(width, height);
            isGameInitialised = true;
        } else {
            gameThread.resumeGame(width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.pauseGame();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gameContext.endGame) {
            gameThread.end();
            isDone = true;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                gameContext.setFingerPosition(new Vector2((int) event.getX(), (int) event.getY()));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                gameContext.setFingerPosition(Vector2.ZERO);
            }
        }

        return true;
    }
}
