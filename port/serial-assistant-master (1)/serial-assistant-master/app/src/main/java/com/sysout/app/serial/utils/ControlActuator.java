package com.sysout.app.serial.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Author:pengs
 * Date: 2022/5/27 10:18
 * Describe:Todo
 */
public class ControlActuator {

    private static final String KEY_SHAKE_NAME = "key.snake.zero.value";
    private static final String KEY_NOD_NAME = "key.nod.zero.value";
    private static final String KEY_SP_NAME = "control_actuator_sp_name";

    public static void writeShake(Context context, int zeroValue) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_SHAKE_NAME, zeroValue).commit();
    }

    public static void writeNod(Context context, int zeroValue) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(KEY_NOD_NAME, zeroValue).commit();
    }

    public static int getNod(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_NOD_NAME, -9999);
    }

    public static int getShake(Context context) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(KEY_SHAKE_NAME, -9999);
    }


    public static void put(Context context, String key, int value) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        preferences.edit().putInt(key, value).commit();
    }

    public static int get(Context context, String key) {
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(KEY_SP_NAME, Context.MODE_PRIVATE);
        return preferences.getInt(key, -9999);
    }
}
