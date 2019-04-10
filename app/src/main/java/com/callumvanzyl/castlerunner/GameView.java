package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameContext gameContext;
    private GameThread gameThread = null;

    public GameView(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        gameContext = new GameContext(this, context, null, holder);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (hasWindowFocus) {
            gameThread.resumeGame();
        } else {
            gameThread.pauseGame();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameThread == null) {
            gameThread = new GameThread(gameContext);
            gameThread.startGame();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
