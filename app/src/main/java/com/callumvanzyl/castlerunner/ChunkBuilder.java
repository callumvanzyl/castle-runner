package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

class ChunkBuilder {

    private static final String DELIMITER = "\t";

    private ArrayList<Chunk> generated = new ArrayList<>();

    private boolean isBusy;

    class BuilderThread extends Thread {

        private GameContext gameContext;
        private String path;

        BuilderThread(GameContext gameContext, String path) {
            this.gameContext = gameContext;
            this.path = path;
        }

        @Override
        public void run() {
            ArrayList<String> all = new ArrayList<>();

            Scanner scanner = null;
            try {
                InputStream inputStream = gameContext.getContext().getAssets().open(path);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

                String line = bufferedReader.readLine();
                while (line != null) {
                    scanner = new Scanner(line);
                    scanner.useDelimiter(DELIMITER);

                    while (scanner.hasNext()) {
                        String next = scanner.next();
                        all.add(next);
                    }

                    line = bufferedReader.readLine();
                }
            } catch (Exception error) {
                Log.e("CR-ERRORS", "Invalid path", error);
                isBusy = false;
                return;
            }

            try {
                int tileSize = ChunkManager.getTileSize();

                int width = Integer.parseInt(all.get(0));
                int height = Integer.parseInt(all.get(1));

                Chunk chunk = new Chunk();

                int i = 2;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        String current = all.get(i);

                        if (!current.equals("x")) {
                            if (current.equals("c")) {
                                Coin coin = new Coin(gameContext);
                                coin.setCollidable(true);
                                coin.setColliderSizeAndOffset(new Vector2(65, 65), new Vector2(30, 30));
                                coin.setSize(new Vector2(tileSize, tileSize));
                                coin.setPosition(new Vector2(tileSize * x, tileSize * y));
                                coin.addTag("Loot");
                                chunk.addObject(coin);
                            } else if (current.equals("m")) {
                                Skeleton skeleton = new Skeleton(gameContext);
                                skeleton.setCollidable(true);
                                skeleton.setColliderSizeAndOffset(new Vector2(85, 105), new Vector2(-10, -10));
                                skeleton.setSize(new Vector2(100, 100));
                                skeleton.setPosition(new Vector2(tileSize * x, tileSize * y));
                                skeleton.addTag("Monster");
                                skeleton.setVelocityEnabled(true);
                                chunk.addObject(skeleton);
                            } else {
                                GameObject tile = new GameObject(gameContext);
                                tile.setCollidable(true);
                                tile.setColliderSizeAndOffset(new Vector2(tileSize, tileSize), Vector2.ZERO);
                                tile.setSize(new Vector2(tileSize, tileSize));
                                tile.setPosition(new Vector2(tileSize * x, tileSize * y));
                                tile.setSprite("textures/world/tiles/" + current + ".png");
                                tile.addTag("Ground");
                                chunk.addObject(tile);
                            }
                        }

                        i++;
                    }
                }

                isBusy = false;
                generated.add(chunk);
            } catch (Exception error) {
                Log.e("CR-ERRORS", "Malformed chunk data file at " + path, error);
                isBusy = false;
                return;
            }
        }
    }

    ChunkBuilder() {
        isBusy = false;
    }

    public void generate(GameContext gameContext, String path) {
        isBusy = true;
        BuilderThread builderThread = new BuilderThread(gameContext, path);
        builderThread.start();
    }

    public Chunk getNext() {
        if (generated.size() > 0) {
            Chunk c = generated.get(0);
            generated.remove(0);
            return c;
        } else {
            return null;
        }
    }

    public ArrayList<Chunk> getGenerated() {
        return generated;
    }

    public boolean isBusy() {
        return isBusy;
    }

}
