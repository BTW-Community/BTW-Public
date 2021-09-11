package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;

import net.minecraft.src.TextureStitched;

import org.lwjgl.opengl.PixelFormat;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

public class AAHelper {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.MIPMAP);

    private static final int debugColor = Config.getBoolean(MCPatcherUtils.EXTENDED_HD, "debugBorder", false) ? 0xff0000ff : 0;
    private static final int aaSamples = Config.getInt(MCPatcherUtils.EXTENDED_HD, "antiAliasing", 1);

    private static Field addressField;

    public static int lastBorder;

    public static PixelFormat setupPixelFormat(PixelFormat pixelFormat) {
        if (aaSamples > 1) {
            logger.config("setting AA samples to %d", aaSamples);
            return pixelFormat.withSamples(aaSamples);
        } else {
            return pixelFormat;
        }
    }

    static BufferedImage addBorder(FakeResourceLocation name, BufferedImage input) {
        lastBorder = 0;
        if (input == null) {
            return input;
        }
        if (name != null && MipmapHelper.useMipmapsForTexture(name.getPath())) {
            input = MipmapHelper.fixTransparency(name, input);
        }
        int width = input.getWidth();
        int height = input.getHeight();
        int numFrames = height / width;
        height = width;
        int border = lastBorder = getBorderWidth(width);
        if (border <= 0) {
            return input;
        }
        int newWidth = width + 2 * border;
        int newHeight = height + 2 * border;
        BufferedImage output = new BufferedImage(newWidth, numFrames * newHeight, BufferedImage.TYPE_INT_ARGB);
        for (int frame = 0; frame < numFrames; frame++) {
            int sy = frame * height;
            int dy = frame * newHeight;

            copyRegion(input, 0, sy, output, 0, dy, border, border, true, true);
            copyRegion(input, 0, sy, output, border, dy, width, border, false, true);
            copyRegion(input, width - border, sy, output, width + border, dy, border, border, true, true);

            copyRegion(input, 0, sy, output, 0, dy + border, border, width, true, false);
            copyRegion(input, 0, sy, output, border, dy + border, width, height, false, false);
            copyRegion(input, width - border, sy, output, width + border, dy + border, border, width, true, false);

            copyRegion(input, 0, sy + height - border, output, 0, dy + height + border, border, border, true, true);
            copyRegion(input, 0, sy + height - border, output, border, dy + height + border, width, border, false, true);
            copyRegion(input, width - border, sy + height - border, output, width + border, dy + height + border, border, border, true, true);

            addDebugOutline(output, dy, width, height, border);
        }
        return output;
    }

    static boolean useAAForTexture(String texture) {
        return (aaSamples > 1 || MipmapHelper.anisoLevel > 1) && MipmapHelper.useMipmapsForTexture(texture);
    }

    private static int getBorderWidth(int size) {
        int border;
        if (aaSamples <= 1 && MipmapHelper.anisoLevel <= 1) {
            border = 0;
        } else if (MipmapHelper.mipmapEnabled && MipmapHelper.maxMipmapLevel > 0) {
            border = 1 << Math.max(Math.min(MipmapHelper.maxMipmapLevel, 4), 0);
        } else {
            border = 2;
        }
        return Math.min(border, size);
    }

    private static void copyRegion(BufferedImage input, int sx, int sy, BufferedImage output, int dx, int dy, int w, int h, boolean flipX, boolean flipY) {
        int[] rgb = new int[w * h];
        input.getRGB(sx, sy, w, h, rgb, 0, w);
        if (flipX || flipY) {
            int[] rgbFlipped = new int[w * h];
            for (int i = 0; i < w; i++) {
                for (int j = 0; j < h; j++) {
                    rgbFlipped[w * j + i] = rgb[w * (flipY ? h - 1 - j : j) + (flipX ? w - 1 - i : i)];
                }
            }
            output.setRGB(dx, dy, w, h, rgbFlipped, 0, w);
        } else {
            output.setRGB(dx, dy, w, h, rgb, 0, w);
        }
    }

    private static void addDebugOutline(BufferedImage output, int dy, int width, int height, int border) {
        if (debugColor != 0) {
            for (int i = 0; i < width; i++) {
                output.setRGB(i + border, dy + border, debugColor);
                output.setRGB(i + border, dy + height + border, debugColor);
            }
            for (int i = 0; i < height; i++) {
                output.setRGB(border, dy + i + border, debugColor);
                output.setRGB(height + border, dy + i + border, debugColor);
            }
        }
    }
}
