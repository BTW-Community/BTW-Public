package com.prupe.mcpatcher.mal.biome;

public class ColorUtils {
    public static void intToFloat3(int rgb, float[] f, int offset) {
        if ((rgb & 0xffffff) == 0xffffff) {
            f[offset] = f[offset + 1] = f[offset + 2] = 1.0f;
        } else {
            f[offset] = (float) (rgb & 0xff0000) / (float) 0xff0000;
            f[offset + 1] = (float) (rgb & 0xff00) / (float) 0xff00;
            f[offset + 2] = (float) (rgb & 0xff) / (float) 0xff;
        }
    }

    public static void intToFloat3(int rgb, float[] f) {
        intToFloat3(rgb, f, 0);
    }

    public static int float3ToInt(float[] f, int offset) {
        return ((int) (255.0f * f[offset])) << 16 | ((int) (255.0f * f[offset + 1])) << 8 | (int) (255.0f * f[offset + 2]);
    }

    public static int float3ToInt(float[] f) {
        return float3ToInt(f, 0);
    }

    public static float clamp(float f) {
        if (f < 0.0f) {
            return 0.0f;
        } else if (f > 1.0f) {
            return 1.0f;
        } else {
            return f;
        }
    }

    public static double clamp(double d) {
        if (d < 0.0) {
            return 0.0;
        } else if (d > 1.0) {
            return 1.0;
        } else {
            return d;
        }
    }

    public static void clamp(float[] f) {
        for (int i = 0; i < f.length; i++) {
            f[i] = clamp(f[i]);
        }
    }
}
