package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCPatcherUtils;

import net.minecraft.src.ITexturePack;

import java.util.Comparator;
import java.util.regex.Pattern;

public class ResourceLocationWithSource extends FakeResourceLocation {
    private final ITexturePack source;
    private final int order;
    private final boolean isDirectory;

    public ResourceLocationWithSource(ITexturePack source, FakeResourceLocation resource) {
        super(resource.getNamespace(), resource.getPath().replaceFirst("/$", ""));
        this.source = source;
        order = ResourceList.getResourcePackOrder(source);
        isDirectory = resource.getPath().endsWith("/");
    }

    public ITexturePack getSource() {
        return source;
    }

    public int getOrder() {
        return order;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    static class Comparator1 implements Comparator<ResourceLocationWithSource> {
        private final boolean bySource;
        private final String suffixExpr;

        Comparator1() {
            this(false, null);
        }

        Comparator1(boolean bySource, String suffix) {
            this.bySource = bySource;
            this.suffixExpr = MCPatcherUtils.isNullOrEmpty(suffix) ? null : Pattern.quote(suffix) + "$";
        }

        @Override
        public int compare(ResourceLocationWithSource o1, ResourceLocationWithSource o2) {
            int result;
            if (bySource) {
                result = o1.getOrder() - o2.getOrder();
                if (result != 0) {
                    return result;
                }
            }
            String n1 = o1.getNamespace();
            String n2 = o2.getNamespace();
            result = n1.compareTo(n2);
            if (result != 0) {
                return result;
            }
            String p1 = o1.getPath();
            String p2 = o2.getPath();
            if (suffixExpr != null) {
                String f1 = p1.replaceAll(".*/", "").replaceFirst(suffixExpr, "");
                String f2 = p2.replaceAll(".*/", "").replaceFirst(suffixExpr, "");
                result = f1.compareTo(f2);
                if (result != 0) {
                    return result;
                }
            }
            return p1.compareTo(p2);
        }
    }
}
