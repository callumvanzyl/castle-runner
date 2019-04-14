package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameContext gameContext;
    private GameThread gameThread = null;

    private boolean isGameInitialised;

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

}
