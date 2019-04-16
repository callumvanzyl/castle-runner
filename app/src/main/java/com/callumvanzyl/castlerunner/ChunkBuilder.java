package com.callumvanzyl.castlerunner;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

class ChunkBuilder extends Thread {

    private volatile static ArrayList<Chunk> generated = new ArrayList<>();

    Context context;
    String path;

    ChunkBuilder(Context context, String path) {
        this.context = context;
        this.path = path;
    }

    public static Chunk getNext() {
        if (generated.size() > 0) {
            Chunk c = generated.get(0);
            generated.remove(0);
            return c;
        } else {
            return null;
        }
    }

    public void run() {
        ArrayList<String> all = new ArrayList<>();

        Scanner scanner = null;
        try {
            InputStream inputStream = context.getAssets().open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line = bufferedReader.readLine();
            while (line != null) {
                scanner = new Scanner(line);
                scanner.useDelimiter("\t");

                while (scanner.hasNext()) {
                    String next = scanner.next();
                    all.add(next);
                }

                line = bufferedReader.readLine();
            }
        } catch (Exception error) {
            Log.e("CR-ERRORS", "Invalid path", error);
            return;
        }

        try {
            int width = Integer.parseInt(all.get(0));
            int height = Integer.parseInt(all.get(1));

            Chunk chunk = new Chunk(new Vector2(width, height));

            int i = 2;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    String current = all.get(i);

                    if (!current.equals("x")) {
                        GameObject tile = new GameObject(context);
                        tile.setSize(new Vector2(120, 120));
                        tile.setPosition(new Vector2(120 * x, 120 * y));
                        tile.setSprite("textures/world/tiles/" + current + ".png");

                        chunk.addObject(tile);
                    }

                    i++;
                }
            }

            ChunkBuilder.generated.add(chunk);
        } catch (Exception error) {
            Log.e("CR-ERRORS", "Malformed chunk data file at " + path, error);
            return;
        }
    }


}
