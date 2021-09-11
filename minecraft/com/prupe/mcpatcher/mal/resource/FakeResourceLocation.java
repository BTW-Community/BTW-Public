package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class FakeResourceLocation {
    private static final MCLogger logger = MCLogger.getLogger("Texture Pack");

    private static final String GRID = "##";
    private static final String BLUR = "%blur%";
    private static final String CLAMP = "%clamp%";

    private final String path;
    private final boolean grid;
    private final boolean blur;
    private final boolean clamp;
    private final String toString;
    private final int hashCode;

    public static FakeResourceLocation wrap(String path) {
        return MCPatcherUtils.isNullOrEmpty(path) ? null : new FakeResourceLocation(path);
    }

    public static String unwrap(FakeResourceLocation resourceLocation) {
        return resourceLocation == null ? null : resourceLocation.toString();
    }

    public static IntBuffer getIntBuffer(IntBuffer buffer, int[] data) {
        buffer.clear();
        final int have = buffer.capacity();
        final int needed = data.length;
        if (needed > have) {
            logger.finest("resizing gl buffer from 0x%x to 0x%x", have, needed);
            buffer = ByteBuffer.allocateDirect(4 * needed).order(buffer.order()).asIntBuffer();
        }
        buffer.put(data);
        buffer.position(0).limit(needed);
        return buffer;
    }

    public FakeResourceLocation(String namespace, String path) {
        if ((grid = path.startsWith(GRID))) {
            path = path.substring(GRID.length());
        }
        if ((blur = path.startsWith(BLUR))) {
            path = path.substring(BLUR.length());
        }
        if ((clamp = path.startsWith(CLAMP))) {
            path = path.substring(CLAMP.length());
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        this.path = path;
        toString = (grid ? GRID : "") + (blur ? BLUR : "") + (clamp ? CLAMP : "") + path;
        hashCode = toString.hashCode();
    }

    public FakeResourceLocation(String path) {
        this(TexturePackAPI.DEFAULT_NAMESPACE, path);
    }

    public String getNamespace() {
        return TexturePackAPI.DEFAULT_NAMESPACE;
    }

    public String getPath() {
        return path;
    }

    public boolean isGrid() {
        return grid;
    }

    public boolean isBlur() {
        return blur;
    }

    public boolean isClamp() {
        return clamp;
    }

    @Override
    public String toString() {
        return toString;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        } else if (!(that instanceof FakeResourceLocation)) {
            return false;
        }
        return this.toString().equals(that.toString());
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
