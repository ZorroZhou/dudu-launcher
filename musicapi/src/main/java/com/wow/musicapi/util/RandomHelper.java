package com.wow.musicapi.util;

import java.util.Random;

/**
 * Created by haohua on 2018/2/18.
 */
public class RandomHelper {
    private final static class Holder {
        public static final Random instance = new Random();
    }

    public static Random getInstance() {
        return Holder.instance;
    }
}
