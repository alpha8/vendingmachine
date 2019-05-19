package com.yihuyixi.vendingmachine;

import android.util.SparseArray;

public class Channels {
    private SparseArray<String> channels = new SparseArray<>();

    private Channels() {
        this.initialize();
    }

    public static Channels getInstance() {
        return new Channels();
    }

    public String getRandomChannel() {
        int seed = (int) (Math.random() * channels.size());
        return channels.get(channels.keyAt(seed));
    }

    private void initialize() {
        for (int i=0; i<30; ++i) {
            channels.put(i, "A" + i);
            if (i % 10 == 8) {
                i += 1;
            }
        }

        for (int i=60; i<80; ++i) {
            if (i == 77) {
                continue;
            }
            channels.put(i, "B" + i);
        }
    }
}
