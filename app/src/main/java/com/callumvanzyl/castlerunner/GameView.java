package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.logging.Handler;

class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Context context;
    private Handler messageHandler;

    private GameThread gameThread;

    public GameView(Context context) {
        super(context);

        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        this.context = context;

        gameThread = new GameThread(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameThread.getState() == Thread.State.TERMINATED) {
            gameThread = new GameThread(this);
            gameThread.start();
        } else {
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (Exception ignored) {}
        }
    }

    public Handler getMessageHandler() {
        return messageHandler;
    }

    public GameThread getGameThread() {
        return gameThread;
    }

}
