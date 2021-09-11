package com.prupe.mcpatcher.ctm;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.block.BlockStateMatcher;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

import java.util.*;

abstract public class TileOverrideIterator implements Iterator<ITileOverride> {
    private static final int MAX_RECURSION = Config.getInt(MCPatcherUtils.CONNECTED_TEXTURES, "maxRecursion", 4);

    private final Map<Block, List<BlockStateMatcher>> allBlockOverrides;
    private final Map<String, List<ITileOverride>> allTileOverrides;

    protected Icon currentIcon;

    private List<BlockStateMatcher> blockOverrides;
    private List<ITileOverride> tileOverrides;
    private final Set<ITileOverride> skipOverrides = new HashSet<ITileOverride>();

    private RenderBlockState renderBlockState;
    private int blockPos;
    private int iconPos;
    private boolean foundNext;
    private ITileOverride nextOverride;
    private ITileOverride lastMatchedOverride;

    protected TileOverrideIterator(Map<Block, List<BlockStateMatcher>> allBlockOverrides, Map<String, List<ITileOverride>> allTileOverrides) {
        this.allBlockOverrides = allBlockOverrides;
        this.allTileOverrides = allTileOverrides;
    }

    void clear() {
        currentIcon = null;
        blockOverrides = null;
        tileOverrides = null;
        nextOverride = null;
        lastMatchedOverride = null;
        skipOverrides.clear();
    }

    private void resetForNextPass() {
        blockOverrides = null;
        tileOverrides = allTileOverrides.get(IconAPI.getIconName(currentIcon));
        blockPos = 0;
        iconPos = 0;
        foundNext = false;
    }

    @Override
    public boolean hasNext() {
        if (foundNext) {
            return true;
        }
        if (tileOverrides != null) {
            while (iconPos < tileOverrides.size()) {
                if (checkOverride(tileOverrides.get(iconPos++))) {
                    renderBlockState.setFilter(null);
                    return true;
                }
            }
        }
        if (blockOverrides != null) {
            while (blockPos < blockOverrides.size()) {
                BlockStateMatcher matcher = blockOverrides.get(blockPos++);
                if (renderBlockState.match(matcher) && checkOverride((ITileOverride) matcher.getData())) {
                    renderBlockState.setFilter(matcher);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ITileOverride next() {
        if (!foundNext) {
            throw new IllegalStateException("next called before hasNext() == true");
        }
        foundNext = false;
        return nextOverride;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("remove not supported");
    }

    private boolean checkOverride(ITileOverride override) {
        if (override != null && !override.isDisabled() && !skipOverrides.contains(override)) {
            foundNext = true;
            nextOverride = override;
            return true;
        } else {
            return false;
        }
    }

    public ITileOverride go(RenderBlockState renderBlockState, Icon origIcon) {
        this.renderBlockState = renderBlockState;
        renderBlockState.setFilter(null);
        currentIcon = origIcon;
        blockOverrides = allBlockOverrides.get(renderBlockState.getBlock());
        tileOverrides = allTileOverrides.get(IconAPI.getIconName(origIcon));
        blockPos = 0;
        iconPos = 0;
        foundNext = false;
        nextOverride = null;
        lastMatchedOverride = null;
        skipOverrides.clear();

        pass:
        for (int pass = 0; pass < MAX_RECURSION; pass++) {
            while (hasNext()) {
                ITileOverride override = next();
                Icon newIcon = getTile(override, renderBlockState, origIcon);
                if (newIcon != null) {
                    lastMatchedOverride = override;
                    skipOverrides.add(override);
                    currentIcon = newIcon;
                    resetForNextPass();
                    continue pass;
                }
            }
            break;
        }
        return lastMatchedOverride;
    }

    public Icon getIcon() {
        return currentIcon;
    }

    abstract protected Icon getTile(ITileOverride override, RenderBlockState renderBlockState, Icon origIcon);

    public static final class IJK extends TileOverrideIterator {
        IJK(Map<Block, List<BlockStateMatcher>> blockOverrides, Map<String, List<ITileOverride>> tileOverrides) {
            super(blockOverrides, tileOverrides);
        }

        @Override
        protected Icon getTile(ITileOverride override, RenderBlockState renderBlockState, Icon origIcon) {
            return override.getTileWorld(renderBlockState, origIcon);
        }
    }

    public static final class Metadata extends TileOverrideIterator {
        Metadata(Map<Block, List<BlockStateMatcher>> blockOverrides, Map<String, List<ITileOverride>> tileOverrides) {
            super(blockOverrides, tileOverrides);
        }

        @Override
        protected Icon getTile(ITileOverride override, RenderBlockState renderBlockState, Icon origIcon) {
            return override.getTileHeld(renderBlockState, origIcon);
        }
    }
}
