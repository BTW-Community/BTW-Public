package com.prupe.mcpatcher.mal.biome;

import net.minecraft.src.IBlockAccess;

import java.util.Collection;

import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;

public interface IColorMap {
    boolean isHeightDependent();

    int getColorMultiplier();

    int getColorMultiplier(IBlockAccess blockAccess, int i, int j, int k);

    float[] getColorMultiplierF(IBlockAccess blockAccess, int i, int j, int k);

    void claimResources(Collection<FakeResourceLocation> resources);

    IColorMap copy();
}
