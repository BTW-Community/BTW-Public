package com.prupe.mcpatcher;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipFile;

/**
 * Collection of static methods available to mods at runtime.  This class is always injected into
 * the output minecraft jar.
 */
public class MCPatcherUtils {
    private static File minecraftDir;
    private static File gameDir;
    private static boolean isGame;
    private static String minecraftVersion;
    private static String patcherVersion;
    private static Properties patcherProperties;

    public static final String EXTENDED_HD = "Extended HD";
    public static final String HD_TEXTURES = "HD Textures";
    public static final String HD_FONT = "HD Font";
    public static final String RANDOM_MOBS = "Random Mobs";
    public static final String CUSTOM_COLORS = "Custom Colors";
    public static final String CONNECTED_TEXTURES = "Connected Textures";
    public static final String BETTER_SKIES = "Better Skies";
    public static final String BETTER_GRASS = "Better Grass";
    public static final String BETTER_GLASS = "Better Glass";
    public static final String CUSTOM_ITEM_TEXTURES = "Custom Item Textures";
    public static final String GLSL_SHADERS = "GLSL Shaders";
    public static final String CUSTOM_TEXTURES_MODELS = "Custom Textures and Models";
    public static final String BASE_MOD = "__Base";
    public static final String TEXTURE_PACK_API_MOD = "__TexturePackAPI";
    public static final String TESSELLATOR_API_MOD = "__TessellatorAPI";
    public static final String TILESHEET_API_MOD = "__TilesheetAPI";
    public static final String NBT_API_MOD = "__NBTAPI";
    public static final String BLOCK_API_MOD = "__BlockAPI";
    public static final String ITEM_API_MOD = "__ItemAPI";
    public static final String BIOME_API_MOD = "__BiomeAPI";

    public static final String CUSTOM_ANIMATIONS = "Custom Animations";
    public static final String MIPMAP = "Mipmap";

    public static final String GL11_CLASS = "org.lwjgl.opengl.GL11";

    public static final String UTILS_CLASS = "com.prupe.mcpatcher.MCPatcherUtils";
    public static final String LOGGER_CLASS = "com.prupe.mcpatcher.MCLogger";
    public static final String CONFIG_CLASS = "com.prupe.mcpatcher.Config";
    public static final String JSON_UTILS_CLASS = "com.prupe.mcpatcher.JsonUtils";
    public static final String PROFILER_API_CLASS = "com.prupe.mcpatcher.ProfilerAPI";
    public static final String MAL_CLASS = "com.prupe.mcpatcher.MAL";

    public static final String INPUT_HANDLER_CLASS = "com.prupe.mcpatcher.mal.util.InputHandler";
    public static final String WEIGHTED_INDEX_CLASS = "com.prupe.mcpatcher.mal.util.WeightedIndex";

    public static final String TEXTURE_PACK_API_CLASS = "com.prupe.mcpatcher.mal.resource.TexturePackAPI";
    public static final String TEXTURE_PACK_CHANGE_HANDLER_CLASS = "com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler";
    public static final String BLEND_METHOD_CLASS = "com.prupe.mcpatcher.mal.resource.BlendMethod";
    public static final String RESOURCE_LIST_CLASS = "com.prupe.mcpatcher.mal.resource.mal.resource.ResourceList";
    public static final String RESOURCE_LOCATION_WITH_SOURCE_CLASS = "com.prupe.mcpatcher.mal.resource.ResourceLocationWithSource";
    public static final String FAKE_RESOURCE_LOCATION_CLASS = "com.prupe.mcpatcher.mal.resource.FakeResourceLocation";

    public static final String TILE_LOADER_CLASS = "com.prupe.mcpatcher.mal.tile.TileLoader";
    public static final String FACE_INFO_CLASS = "com.prupe.mcpatcher.mal.tile.FaceInfo";
    public static final String TESSELLATOR_UTILS_CLASS = "com.prupe.mcpatcher.TessellatorUtils";

    public static final String AA_HELPER_CLASS = "com.prupe.mcpatcher.hd.AAHelper";
    public static final String BORDERED_TEXTURE_CLASS = "com.prupe.mcpatcher.hd.BorderedTexture";
    public static final String CUSTOM_ANIMATION_CLASS = "com.prupe.mcpatcher.hd.CustomAnimation";
    public static final String FANCY_DIAL_CLASS = "com.prupe.mcpatcher.hd.FancyDial";
    public static final String FONT_UTILS_CLASS = "com.prupe.mcpatcher.hd.FontUtils";
    public static final String MIPMAP_HELPER_CLASS = "com.prupe.mcpatcher.hd.MipmapHelper";
    public static final String FANCY_COMPASS_CLASS = "com.prupe.mcpatcher.hd.FancyCompass";
    public static final String TEXTURE_UTILS_CLASS = "com.prupe.mcpatcher.hd.TextureUtils";
    public static final String TILE_SIZE_CLASS = "com.prupe.mcpatcher.hd.TileSize";

    public static final String BLOCK_API_CLASS = "com.prupe.mcpatcher.mal.block.BlockAPI";
    public static final String BLOCK_AND_METADATA_CLASS = "com.prupe.mcpatcher.mal.block.BlockAndMetadata";
    public static final String RENDER_PASS_API_MAL_CLASS = "com.prupe.mcpatcher.mal.block.RenderPassAPI";
    public static final String RENDER_BLOCKS_UTILS_CLASS = "com.prupe.mcpatcher.mal.block.RenderBlocksUtils";
    public static final String ITEM_API_CLASS = "com.prupe.mcpatcher.mal.item.ItemAPI";
    public static final String NBT_RULE_CLASS = "com.prupe.mcpatcher.mal.nbt.NBTRule";
    public static final String BIOME_API_CLASS = "com.prupe.mcpatcher.mal.biome.BiomeAPI";
    public static final String BIOME_HELPER_CLASS = "com.prupe.mcpatcher.cc.BiomeHelper";

    public static final String RANDOM_MOBS_CLASS = "com.prupe.mcpatcher.mob.MobRandomizer";
    public static final String MOB_RULE_LIST_CLASS = "com.prupe.mcpatcher.mob.MobRuleList";
    public static final String MOB_OVERLAY_CLASS = "com.prupe.mcpatcher.mob.MobOverlay";
    public static final String LINE_RENDERER_CLASS = "com.prupe.mcpatcher.mob.LineRenderer";

    public static final String COLORIZER_CLASS = "com.prupe.mcpatcher.cc.Colorizer";
    public static final String COLORIZE_WORLD_CLASS = "com.prupe.mcpatcher.cc.ColorizeWorld";
    public static final String COLORIZE_ITEM_CLASS = "com.prupe.mcpatcher.cc.ColorizeItem";
    public static final String COLORIZE_ENTITY_CLASS = "com.prupe.mcpatcher.cc.ColorizeEntity";
    public static final String COLORIZE_BLOCK_CLASS = "com.prupe.mcpatcher.cc.ColorizeBlock";
    public static final String COLORIZE_BLOCK18_CLASS = "com.prupe.mcpatcher.cc.ColorizeBlock18";
    public static final String LIGHTMAP_CLASS = "com.prupe.mcpatcher.cc.Lightmap";

    public static final String CTM_UTILS_CLASS = "com.prupe.mcpatcher.ctm.CTMUtils";
    public static final String CTM_UTILS18_CLASS = "com.prupe.mcpatcher.ctm.CTMUtils18";
    public static final String TILE_OVERRIDE_INTERFACE = "com.prupe.mcpatcher.ctm.ITileOverride";
    public static final String TILE_OVERRIDE_CLASS = "com.prupe.mcpatcher.ctm.TileOverride";
    public static final String TILE_OVERRIDE_IMPL_CLASS = "com.prupe.mcpatcher.ctm.TileOverrideImpl";
    public static final String TILE_OVERRIDE_ITERATOR_CLASS = "com.prupe.mcpatcher.ctm.TileOverrideIterator";
    public static final String BLOCK_ORIENTATION_CLASS = "com.prupe.mcpatcher.ctm.BlockOrientation";
    public static final String GLASS_PANE_RENDERER_CLASS = "com.prupe.mcpatcher.ctm.GlassPaneRenderer";
    public static final String RENDER_PASS_API_CLASS = "com.prupe.mcpatcher.ctm.RenderPassAPI";
    public static final String SUPER_TESSELLATOR_CLASS = "com.prupe.mcpatcher.ctm.SuperTessellator";

    public static final String RENDER_PASS_CLASS = "com.prupe.mcpatcher.renderpass.RenderPass";
    public static final String RENDER_PASS_MAP_CLASS = "com.prupe.mcpatcher.renderpass.RenderPassMap";

    public static final String SKY_RENDERER_CLASS = "com.prupe.mcpatcher.sky.SkyRenderer";
    public static final String FIREWORKS_HELPER_CLASS = "com.prupe.mcpatcher.sky.FireworksHelper";

    public static final String CIT_UTILS_CLASS = "com.prupe.mcpatcher.cit.CITUtils";
    public static final String CIT_UTILS18_CLASS = "com.prupe.mcpatcher.cit.CITUtils18";
    public static final String OVERRIDE_BASE_CLASS = "com.prupe.mcpatcher.cit.OverrideBase";
    public static final String ITEM_OVERRIDE_CLASS = "com.prupe.mcpatcher.cit.ItemOverride";
    public static final String ENCHANTMENT_CLASS = "com.prupe.mcpatcher.cit.Enchantment";
    public static final String ENCHANTMENT_LIST_CLASS = "com.prupe.mcpatcher.cit.EnchantmentList";
    public static final String ARMOR_OVERRIDE_CLASS = "com.prupe.mcpatcher.cit.ArmorOverride";
    public static final String POTION_REPLACER_CLASS = "com.prupe.mcpatcher.cit.PotionReplacer";

    public static final String SHADERS_CLASS = "com.prupe.mcpatcher.glsl.Shaders";

    public static final String BLANK_PNG_FORMAT = "blank_%08x.png";

    private MCPatcherUtils() {
    }

    static File getDefaultGameDir() {
        String os = System.getProperty("os.name").toLowerCase();
        String baseDir = null;
        String subDir = ".minecraft";
        if (os.contains("win")) {
            baseDir = System.getenv("APPDATA");
        } else if (os.contains("mac")) {
            subDir = "Library/Application Support/minecraft";
        }
        if (baseDir == null) {
            baseDir = System.getProperty("user.home");
        }
        return new File(baseDir, subDir);
    }

    static boolean setGameDir(File dir) {
        if (dir != null && dir.isDirectory() &&
            new File(dir, "assets").isDirectory() &&
            new File(dir, "libraries").isDirectory() &&
            new File(dir, "versions").isDirectory() &&
            new File(dir, Config.LAUNCHER_JSON).isFile()) {
            minecraftDir = dir.getAbsoluteFile();
        } else {
            minecraftDir = null;
        }
        gameDir = minecraftDir;
        return minecraftDir != null && Config.load(minecraftDir, false);
    }

    /**
     * Get the path to a file/directory within the minecraft folder.
     *
     * @param subdirs zero or more path components
     * @return combined path
     */
    public static File getMinecraftPath(String... subdirs) {
        File f = minecraftDir;
        for (String s : subdirs) {
            f = new File(f, s);
        }
        return f;
    }

    /**
     * Get the path to a file/directory within the game folder.  The game folder is usually the same as the minecraft
     * folder, but can be overridden via a launcher profile setting.
     *
     * @param subdirs zero or more path components
     * @return combined path
     */
    public static File getGamePath(String... subdirs) {
        File f = gameDir;
        for (String s : subdirs) {
            f = new File(f, s);
        }
        return f;
    }

    /**
     * Returns true if running inside game, false if running inside MCPatcher.  Useful for
     * code shared between mods and runtime classes.
     *
     * @return true if in game
     */
    public static boolean isGame() {
        return isGame;
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static String getStringProperty(Properties properties, String key, String defaultValue) {
        if (properties == null) {
            return defaultValue;
        } else {
            return properties.getProperty(key, defaultValue).trim();
        }
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static int getIntProperty(Properties properties, String key, int defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim();
            if (!value.equals("")) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                }
            }
        }
        return defaultValue;
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static int getHexProperty(Properties properties, String key, int defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim();
            if (!value.equals("")) {
                try {
                    return Integer.parseInt(value, 16);
                } catch (NumberFormatException e) {
                }
            }
        }
        return defaultValue;
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static boolean getBooleanProperty(Properties properties, String key, boolean defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim().toLowerCase();
            if (!value.equals("")) {
                return Boolean.parseBoolean(value);
            }
        }
        return defaultValue;
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static float getFloatProperty(Properties properties, String key, float defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim();
            if (!value.equals("")) {
                try {
                    return Float.parseFloat(value);
                } catch (NumberFormatException e) {
                }
            }
        }
        return defaultValue;
    }

    /**
     * Get a value from a properties file.
     *
     * @param properties   properties file
     * @param key          property name
     * @param defaultValue default value if not found in properties file
     * @return property value
     */
    public static double getDoubleProperty(Properties properties, String key, double defaultValue) {
        if (properties != null) {
            String value = properties.getProperty(key, "").trim();
            if (!value.equals("")) {
                try {
                    return Double.parseDouble(value);
                } catch (NumberFormatException e) {
                }
            }
        }
        return defaultValue;
    }

    /**
     * Convenience method to close a stream ignoring exceptions.
     *
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Convenience method to close a zip file ignoring exceptions.
     *
     * @param zip zip file
     */
    public static void close(ZipFile zip) {
        if (zip != null) {
            try {
                zip.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns true if string is null or consists only of whitespace.
     *
     * @param s possibly null string
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Returns true if collection is null or empty.
     *
     * @param c possibly null collection
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * Returns true if map is null or empty.
     *
     * @param m possibly null map
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    public static void dumpCommandLine(String[] args) {
        StringBuilder sb = new StringBuilder("Command line args:");
        if (args == null) {
            sb.append(" (null)");
        } else {
            for (String arg : args) {
                sb.append(' ');
                if (arg.matches("\\S+")) {
                    sb.append(arg);
                } else {
                    sb.append('"').append(arg).append('"');
                }
            }
        }
        System.out.println(sb.toString());
    }

    public static void setMinecraft(File gameDir, File assetsDir, String minecraftVersion, String patcherVersion) {
        isGame = true;
        Config.setReadOnly(true);
        if (assetsDir == null || !assetsDir.isDirectory()) {
            minecraftDir = getDefaultGameDir();
        } else {
            assetsDir = assetsDir.getAbsoluteFile();
            if (assetsDir.getName().equals("legacy")) {
                File parent = assetsDir.getParentFile();
                if (parent != null && parent.getName().equals("virtual")) {
                    parent = parent.getParentFile();
                    if (parent != null && parent.getName().equals("assets")) {
                        assetsDir = parent;
                    }
                }
            }
            minecraftDir = assetsDir.getParentFile();
        }
        if (gameDir == null || !gameDir.isDirectory()) {
            MCPatcherUtils.gameDir = minecraftDir;
        } else {
            MCPatcherUtils.gameDir = gameDir.getAbsoluteFile();
        }
        MCPatcherUtils.minecraftVersion = minecraftVersion;
        MCPatcherUtils.patcherVersion = patcherVersion;
        System.out.println();
        System.out.printf("MCPatcherUtils initialized:\n");
        System.out.printf("Minecraft directory: %s\n", minecraftDir);
        System.out.printf("  (assets, libraries, versions)\n");
        System.out.printf("Game directory:      %s\n", MCPatcherUtils.gameDir);
        System.out.printf("  (resourcepacks, saves)\n");
        System.out.printf("Minecraft version:   %s\n", minecraftVersion);
        System.out.printf("MCPatcher version:   %s\n", patcherVersion);
        System.out.printf("Max heap memory:     %.1fMB\n", Runtime.getRuntime().maxMemory() / 1048576.0f);
        try {
            Class<?> vm = Class.forName("sun.misc.VM");
            Method method = vm.getDeclaredMethod("maxDirectMemory");
            long memory = (Long) method.invoke(null);
            System.out.printf("Max direct memory: %.1fMB\n", memory / 1048576.0f);
        } catch (Throwable e) {
            //e.printStackTrace();
        }
        Config.load(minecraftDir, true);
        System.out.printf("Launcher profile:  %s\n", Config.getInstance().getSelectedProfileName());
        System.out.println();
    }

    /**
     * Get shortened version of currently running Minecraft, e.g., 1.9pre4.
     *
     * @return string
     */
    public static String getMinecraftVersion() {
        return minecraftVersion;
    }

    /**
     * Get version of MCPatcher.
     *
     * @return string
     */
    public static String getPatcherVersion() {
        return patcherVersion;
    }

    /**
     * Attempts to read image.  Closes input stream regardless of success or failure.
     *
     * @param input open input stream
     * @return image or null
     */
    public static BufferedImage readImage(InputStream input) {
        BufferedImage image = null;
        if (input != null) {
            try {
                image = ImageIO.read(input);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(input);
            }
        }
        return image;
    }

    /**
     * Attempts to read a properties file.  Closes input stream regardless of success or failure.
     *
     * @param input open input stream
     * @return properties object or null
     */
    public static Properties readProperties(InputStream input) {
        Properties properties = new Properties();
        if (readProperties(input, properties)) {
            return properties;
        } else {
            return null;
        }
    }

    /**
     * Attempts to read a properties file.  Closes input stream regardless of success or failure.
     *
     * @param input      open input stream
     * @param properties initial properties object
     * @return true if properties were successfully read
     */
    public static boolean readProperties(InputStream input, Properties properties) {
        if (input != null && properties != null) {
            try {
                properties.load(input);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close(input);
            }
        }
        return false;
    }

    /**
     * Get array of rgb values from image.
     *
     * @param image input image
     * @return rgb array
     */
    public static int[] getImageRGB(BufferedImage image) {
        if (image == null) {
            return null;
        } else {
            int width = image.getWidth();
            int height = image.getHeight();
            int[] rgb = new int[width * height];
            image.getRGB(0, 0, width, height, rgb, 0, width);
            return rgb;
        }
    }

    /**
     * Parse a comma-separated list of integers/ranges.
     *
     * @param list     comma- or space-separated list, e.g., 2-4,5,8,12-20
     * @param minValue smallest value allowed in the list
     * @param maxValue largest value allowed in the list
     * @return possibly empty integer array
     */
    public static int[] parseIntegerList(String list, int minValue, int maxValue) {
        ArrayList<Integer> tmpList = new ArrayList<Integer>();
        Pattern p = Pattern.compile("(\\d*)-(\\d*)");
        for (String token : list.replace(',', ' ').split("\\s+")) {
            try {
                if (token.matches("\\d+")) {
                    tmpList.add(Integer.parseInt(token));
                } else {
                    Matcher m = p.matcher(token);
                    if (m.matches()) {
                        String a = m.group(1);
                        String b = m.group(2);
                        int min = a.equals("") ? minValue : Integer.parseInt(a);
                        int max = b.equals("") ? maxValue : Integer.parseInt(b);
                        for (int i = min; i <= max; i++) {
                            tmpList.add(i);
                        }
                    }
                }
            } catch (NumberFormatException e) {
            }
        }
        if (minValue <= maxValue) {
            for (int i = 0; i < tmpList.size(); ) {
                if (tmpList.get(i) < minValue || tmpList.get(i) > maxValue) {
                    tmpList.remove(i);
                } else {
                    i++;
                }
            }
        }
        int[] a = new int[tmpList.size()];
        for (int i = 0; i < a.length; i++) {
            a[i] = tmpList.get(i);
        }
        return a;
    }

    static Properties getPatcherProperties() {
        if (patcherProperties == null) {
            patcherProperties = new Properties();
            InputStream input = null;
            try {
                input = MCPatcherUtils.class.getResourceAsStream("/" + Config.MCPATCHER_PROPERTIES);
                if (input == null) {
                    System.out.printf("ERROR: could not read /%s\n", Config.MCPATCHER_PROPERTIES);
                }
                readProperties(input, patcherProperties);
            } finally {
                close(input);
            }
        }
        return patcherProperties;
    }
}
