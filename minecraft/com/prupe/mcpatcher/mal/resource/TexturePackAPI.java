package com.prupe.mcpatcher.mal.resource;

import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Pattern;

public class TexturePackAPI {
    private static final MCLogger logger = MCLogger.getLogger("Texture Pack");

    public static final String DEFAULT_NAMESPACE = "minecraft";

    private static final TexturePackAPI instance = new TexturePackAPI();

    public static final String MCPATCHER_SUBDIR = "/";
    public static final FakeResourceLocation ITEMS_PNG = new FakeResourceLocation("/gui/items.png");
    public static final FakeResourceLocation BLOCKS_PNG = new FakeResourceLocation("terrain.png");

    private final List<Field> textureMapFields = new ArrayList<Field>();
    
    public static boolean isInitialized() {
        return instance != null && Minecraft.getMinecraft().texturePackList != null;
    }

    public static void scheduleTexturePackRefresh() {
        Minecraft.getMinecraft().scheduleTexturePackRefresh();
    }

    public static List<ITexturePack> getResourcePacks(String namespace) {
        List<ITexturePack> resourcePacks = new ArrayList<ITexturePack>();
        
        ITexturePack resourcePack = instance.getTexturePack();
        if (resourcePack != null) {
            resourcePacks.add(resourcePack);
        }

        return resourcePacks;
    }

    public static Set<String> getNamespaces() {
        Set<String> set = new HashSet<String>();
        set.add(DEFAULT_NAMESPACE);
        return set;
    }

    public static boolean isDefaultTexturePack() {
    	ITexturePack texturePack = instance.getTexturePack();
        return texturePack == null || texturePack instanceof TexturePackDefault;
    }
    
    public static InputStream getInputStream(FakeResourceLocation resource) {
    	if (resource == null) {
    		return null;
    	}
    	
    	ITexturePack resourcePack;
    	
        if (resource instanceof ResourceLocationWithSource) {
            resourcePack = ((ResourceLocationWithSource) resource).getSource();
        }
        else {
        	resourcePack = instance.getTexturePack();
        }
    	
        try {
			return resourcePack == null ? null : resourcePack.getResourceAsStream(resource.getPath());
		} catch (IOException e) {
			return null;
		}
    }

    public static boolean hasResource(FakeResourceLocation resource) {
        if (resource == null) {
            return false;
        } else if (resource.getPath().endsWith(".png")) {
            return getImage(resource) != null;
        } else if (resource.getPath().endsWith(".properties")) {
            return getProperties(resource) != null;
        } else {
            InputStream is = getInputStream(resource);
            MCPatcherUtils.close(is);
            return is != null;
        }
    }

    public static boolean hasCustomResource(FakeResourceLocation resource) {
        InputStream jar = null;
        InputStream pack = null;
        try {
            String path = resource.getPath();
            pack = getInputStream(resource);
            if (pack == null) {
                return false;
            }
            jar = Minecraft.class.getResourceAsStream(path);
            if (jar == null) {
                return true;
            }
            byte[] buffer1 = new byte[4096];
            byte[] buffer2 = new byte[4096];
            int read1;
            int read2;
            while ((read1 = pack.read(buffer1)) > 0) {
                read2 = jar.read(buffer2);
                if (read1 != read2) {
                    return true;
                }
                for (int i = 0; i < read1; i++) {
                    if (buffer1[i] != buffer2[i]) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            MCPatcherUtils.close(jar);
            MCPatcherUtils.close(pack);
        }
        return false;
    }

    public static BufferedImage getImage(FakeResourceLocation resource) {
        if (resource == null) {
            return null;
        }
        InputStream input = getInputStream(resource);
        BufferedImage image = null;
        if (input != null) {
            try {
                image = ImageIO.read(input);
            } catch (IOException e) {
                logger.error("could not read %s", resource);
                e.printStackTrace();
            } finally {
                MCPatcherUtils.close(input);
            }
        }
        return image;
    }

    public static Properties getProperties(FakeResourceLocation resource) {
        Properties properties = new Properties();
        if (getProperties(resource, properties)) {
            return properties;
        } else {
            return null;
        }
    }

    public static boolean getProperties(FakeResourceLocation resource, Properties properties) {
        if (properties != null) {
            InputStream input = getInputStream(resource);
            try {
                if (input != null) {
                    properties.load(input);
                    return true;
                }
            } catch (IOException e) {
                logger.error("could not read %s", resource);
                e.printStackTrace();
            } finally {
                MCPatcherUtils.close(input);
            }
        }
        return false;
    }

    public static FakeResourceLocation transformResourceLocation(FakeResourceLocation resource, String oldExt, String newExt) {
        return new FakeResourceLocation(resource.getNamespace(), resource.getPath().replaceFirst(Pattern.quote(oldExt) + "$", newExt));
    }

    public static FakeResourceLocation parsePath(String path) {
        return MCPatcherUtils.isNullOrEmpty(path) ? null : new FakeResourceLocation(path.replace(File.separatorChar, '/'));
    }

    public static FakeResourceLocation parseResourceLocation(String path) {
        return parseResourceLocation(new FakeResourceLocation(DEFAULT_NAMESPACE, "a"), path);
    }

    public static FakeResourceLocation parseResourceLocation(FakeResourceLocation baseResource, String path) {
    	if (MCPatcherUtils.isNullOrEmpty(path)) {
    		return null;
    	}
        
        if (path.startsWith("~/")) {
            // Relative to namespace mcpatcher dir:
            // ~/path -> /path
            path = path.substring(1);
        }
        if (path.startsWith("./")) {
            // Relative to properties file:
            // ./path -> (dir of base file)/path
            return new FakeResourceLocation(baseResource.getNamespace(), baseResource.getPath().replaceFirst("[^/]+$", "") + path.substring(2));
        } else if (path.startsWith("/")) {
            return new FakeResourceLocation(path);
        } else {
            return new FakeResourceLocation(baseResource.getNamespace(), baseResource.getPath().replaceFirst("[^/]+$", "") + path);
        }
    }

    public static FakeResourceLocation newMCPatcherResourceLocation(String path) {
    	return new FakeResourceLocation(MCPATCHER_SUBDIR + path.replaceFirst("^/+", ""));
    }

    public static int getTextureIfLoaded(FakeResourceLocation resource) {       
        if (resource == null) {
        	return -1;
        }
        
        RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
        String path = resource.getPath();
        if (path.equals("/terrain.png") || path.equals("/gui/items.png")) {
            return renderEngine.getTexture(path);
        }
        for (Field field : instance.textureMapFields) {
            try {
                HashMap map = (HashMap) field.get(renderEngine);
                if (map != null) {
                    Object value = map.get(resource.toString());
                    if (value instanceof Integer) {
                        return (Integer) value;
                    }
                }
            } catch (IllegalAccessException e) {
            }
        }
        return -1;
    }

    public static boolean isTextureLoaded(FakeResourceLocation resource) {
        return getTextureIfLoaded(resource) >= 0;
    }

    public static void bindTexture(FakeResourceLocation resource) {
        if (resource != null) {
        	Minecraft.getMinecraft().renderEngine.bindTexture(resource.toString());
        }
    }

    public static void unloadTexture(FakeResourceLocation resource) {
        if (resource != null) {
        	int texture = getTextureIfLoaded(resource);
            if (texture >= 0) {
                logger.finest("unloading texture %s", resource);
                RenderEngine renderEngine = Minecraft.getMinecraft().renderEngine;
                renderEngine.deleteTexture(texture);
                for (Field field : instance.textureMapFields) {
                    try {
                        HashMap map = (HashMap) field.get(renderEngine);
                        if (map != null) {
                            map.remove(resource.toString());
                        }
                    } catch (IllegalAccessException e) {
                    }
                }
            }
        }
    }

    public static void flushUnusedTextures() {
    	// switching packs is so hopelessly broken in 1.5 that there's no point
    }
    
    private TexturePackAPI() {
        try {
            for (Field field : RenderEngine.class.getDeclaredFields()) {
                if (HashMap.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    textureMapFields.add(field);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    private ITexturePack getTexturePack() {
        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft == null) {
            return null;
        }
        TexturePackList texturePackList = minecraft.texturePackList;
        if (texturePackList == null) {
            return null;
        }
        return texturePackList.getSelectedTexturePack();
    }
}
