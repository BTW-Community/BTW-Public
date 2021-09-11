package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

// 1.5 only
public class Wrapper15 {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.MIPMAP);

    public static TextureMap currentAtlas;

    private static boolean flippedTextureLogged;

    public static void setupTexture(Texture texture, String textureName) {
        int width = texture.getWidth();
        int height = texture.getHeight();
        MipmapHelper.setupTexture(width, height, false, false, textureName);
        copySubTexture(texture, texture.getTextureData(), 0, 0, width, height);
    }

    public static void setupTexture(RenderEngine renderEngine, BufferedImage image, int glTextureId, boolean blur, boolean clamp, FakeResourceLocation textureName) {
        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            int[] rgb = MCPatcherUtils.getImageRGB(image);
            GLAPI.glBindTexture(glTextureId);
            MipmapHelper.setupTexture(rgb, width, height, 0, 0, blur, clamp, textureName.getPath());
        }
    }

    public static void copySubTexture(Texture dst, Texture src, int x, int y, boolean flipped) {
        if (flipped && !flippedTextureLogged) {
            flippedTextureLogged = true;
            logger.warning("copySubTexture(%s, %s, %d, %d, %s): flipped texture not yet supported",
                dst.getTextureName(), src.getTextureName(), x, y, flipped
            );
        }
        GLAPI.glBindTexture(dst.getGlTextureId());
        copySubTexture(getMipmaps(src), x, y, src.getWidth(), src.getHeight());
    }

    public static void copySubTexture(Texture dst, ByteBuffer srcBuffer, int x, int y, int width, int height) {
        copySubTexture(getMipmaps(reformatTextureData(srcBuffer), width, height), x, y, width, height);
    }

    private static void copySubTexture(IntBuffer[] mipmaps, int x, int y, int width, int height) {
        if (mipmaps == null) {
            return;
        }
        for (int level = 0; level < mipmaps.length; level++) {
            IntBuffer mipmap = mipmaps[level];
            if (mipmap != null) {
                GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, level, x, y, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, mipmap);
            }
            if (level >= mipmaps.length) {
                break;
            }
            x >>= 1;
            y >>= 1;
            width >>= 1;
            height >>= 1;
        }
    }

    private static IntBuffer[] getMipmaps(IntBuffer buffer, int width, int height) {
        if (buffer.capacity() == 0) {
            return null;
        }
        int levels = MipmapHelper.getMipmapLevelsForCurrentTexture();
        IntBuffer[] mipmaps = new IntBuffer[levels + 1];
        buffer.position(0);
        mipmaps[0] = buffer;
        for (int level = 1; level < mipmaps.length; level++) {
            mipmaps[level] = MipmapHelper.newIntBuffer(mipmaps[level - 1].capacity());
            MipmapHelper.scaleHalf(mipmaps[level - 1], width, height, mipmaps[level], 0);
            width >>= 1;
            height >>= 1;
        }
        return mipmaps;
    }

    private static IntBuffer[] getMipmaps(Texture texture) {
        if (texture.mipmapData == null) {
            texture.mipmapData = getMipmaps(reformatTextureData(texture.getTextureData()), texture.getWidth(), texture.getHeight());
        }
        return texture.mipmapData;
    }

    private static IntBuffer reformatTextureData(ByteBuffer buffer) {
        buffer.position(0);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return getDirectByteBuffer(buffer).asIntBuffer();
    }

    private static ByteBuffer getDirectByteBuffer(ByteBuffer buffer) {
        if (buffer.isDirect()) {
            return buffer;
        } else {
            ByteBuffer newBuffer = ByteBuffer.allocateDirect(buffer.capacity());
            newBuffer.order(buffer.order());
            newBuffer.put(buffer);
            newBuffer.flip();
            return newBuffer;
        }
    }

    public static BufferedImage addAABorder(String name, BufferedImage input) {
        if (input == null || currentAtlas == null || !MipmapHelper.useMipmapsForTexture(currentAtlas.basePath)) {
            return input;
        } else {
            return AAHelper.addBorder(new FakeResourceLocation(currentAtlas.basePath), input);
        }
    }

    public static TextureStitched createSprite(String name) {
        if (currentAtlas == null) {
            return new TextureStitched(name);
        } else {
            return BorderedTexture.create(currentAtlas.basePath, name);
        }
    }
}
