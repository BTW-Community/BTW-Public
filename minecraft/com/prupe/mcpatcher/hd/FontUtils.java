package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import net.minecraft.src.FontRenderer;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FontUtils {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.HD_FONT);

    private static final boolean enable = Config.getBoolean(MCPatcherUtils.EXTENDED_HD, "hdFont", true);
    private static final boolean enableNonHD = Config.getBoolean(MCPatcherUtils.EXTENDED_HD, "nonHDFontWidth", false);

    private static final int ROWS = 16;
    private static final int COLS = 16;

    public static final char[] AVERAGE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123467890".toCharArray();
    public static final int[] SPACERS = new int[]{0x02028bfe, 0x02808080, 0x0dffffff};

    private static final boolean showLines = false;

    private static final Set<FontRenderer> allRenderers = new HashSet<FontRenderer>();

    static {
        TexturePackChangeHandler.register(new TexturePackChangeHandler(MCPatcherUtils.HD_FONT, 1) {
            @Override
            public void initialize() {
            }

            @Override
            public void beforeChange() {
            }

            @Override
            public void afterChange() {
                for (FontRenderer renderer : allRenderers) {
                    renderer.readFontData();
                }
            }
        });
    }

    static void init() {
    }

    public static FakeResourceLocation getFontName(FontRenderer fontRenderer, FakeResourceLocation font, float hdFontAdj) {
        if (fontRenderer.getDefaultFont() == null) {
            fontRenderer.setDefaultFont(font);
        }
        FakeResourceLocation defaultFont = fontRenderer.getDefaultFont();
        if (fontRenderer.getHDFont() == null) {
            String namespace = defaultFont.getNamespace();
            String name = defaultFont.getPath().replaceAll(".*/", "");
            fontRenderer.setHDFont(new FakeResourceLocation(namespace, TexturePackAPI.MCPATCHER_SUBDIR + "font/" + name));
        }
        FakeResourceLocation hdFont = fontRenderer.getHDFont();
        FakeResourceLocation newFont;
        if (enable && TexturePackAPI.hasResource(hdFont)) {
            if (!hdFont.equals(defaultFont)) {
                logger.fine("using %s instead of %s", hdFont, defaultFont);
            }
            fontRenderer.isHD = true;
            newFont = hdFont;
        } else {
            logger.fine("using default %s", defaultFont);
            fontRenderer.isHD = enable && enableNonHD;
            newFont = defaultFont;
        }
        fontRenderer.fontAdj = fontRenderer.isHD ? hdFontAdj : 1.0f;
        return newFont;
    }

    public static float[] computeCharWidthsf(FontRenderer fontRenderer, FakeResourceLocation filename, BufferedImage image, int[] rgb, int[] charWidth) {
        float[] charWidthf = new float[charWidth.length];
        if (!fontRenderer.isHD) {
            for (int i = 0; i < charWidth.length; i++) {
                charWidthf[i] = charWidth[i];
            }
            charWidthf[32] = 4.0f;
            return charWidthf;
        }
        allRenderers.add(fontRenderer);
        int width = image.getWidth();
        int height = image.getHeight();
        int colWidth = width / COLS;
        int rowHeight = height / ROWS;
        for (int ch = 0; ch < charWidth.length; ch++) {
            int row = ch / COLS;
            int col = ch % COLS;
            outer:
            for (int colIdx = colWidth - 1; colIdx >= 0; colIdx--) {
                int x = col * colWidth + colIdx;
                for (int rowIdx = 0; rowIdx < rowHeight; rowIdx++) {
                    int y = row * rowHeight + rowIdx;
                    int pixel = rgb[x + y * width];
                    if (isOpaque(pixel)) {
                        if (printThis(ch)) {
                            logger.finer("%d '%c' pixel (%d, %d) = %08x, colIdx = %d", ch, (char) ch, x, y, pixel, colIdx);
                        }
                        charWidthf[ch] = (128.0f * (float) (colIdx + 1)) / (float) width + 1.0f;
                        if (showLines) {
                            for (int i = 0; i < rowHeight; i++) {
                                y = row * rowHeight + i;
                                for (int j = 0; j < Math.max(colWidth / 16, 1); j++) {
                                    image.setRGB(x + j, y, (i == rowIdx ? 0xff0000ff : 0xffff0000));
                                    image.setRGB(col * colWidth + j, y, 0xff00ff00);
                                }
                            }
                        }
                        break outer;
                    }
                }
            }
        }
        for (int ch = 0; ch < charWidthf.length; ch++) {
            if (charWidthf[ch] <= 0.0f) {
                charWidthf[ch] = 2.0f;
            } else if (charWidthf[ch] >= 7.99f) {
                charWidthf[ch] = 7.99f;
            }
        }
        boolean[] isOverride = new boolean[charWidth.length];
        try {
            getCharWidthOverrides(filename, charWidthf, isOverride);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (!isOverride[32]) {
            charWidthf[32] = defaultSpaceWidth(charWidthf);
        }
        for (int ch = 0; ch < charWidth.length; ch++) {
            charWidth[ch] = Math.round(charWidthf[ch]);
            if (printThis(ch)) {
                logger.finer("charWidth[%d '%c'] = %f", ch, (char) ch, charWidthf[ch]);
            }
        }
        return charWidthf;
    }

    private static float getCharWidthf(FontRenderer fontRenderer, char ch) {
        float width = fontRenderer.getCharWidth(ch);
        if (width < 0 || fontRenderer.charWidthf == null || ch >= fontRenderer.charWidthf.length) {
            return width;
        } else {
            return fontRenderer.charWidthf[ch];
        }
    }

    public static float getCharWidthf(FontRenderer fontRenderer, int[] charWidth, int ch) {
        return fontRenderer.isHD ? fontRenderer.charWidthf[ch] * fontRenderer.FONT_HEIGHT / 8.0f : (float) charWidth[ch];
    }

    public static float getStringWidthf(FontRenderer fontRenderer, String s) {
        float totalWidth = 0.0f;
        if (s != null) {
            boolean isLink = false;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                float cWidth = getCharWidthf(fontRenderer, c);
                if (cWidth < 0.0f && i < s.length() - 1) {
                    i++;
                    c = s.charAt(i);
                    if (c == 'l' || c == 'L') {
                        isLink = true;
                    } else if (c == 'r' || c == 'R') {
                        isLink = false;
                    }
                    cWidth = 0.0f;
                }
                totalWidth += cWidth;
                if (isLink) {
                    totalWidth++;
                }
            }
        }
        return totalWidth;
    }

    public static FakeResourceLocation getUnicodePage(FakeResourceLocation resource) {
        if (enable && resource != null) {
            FakeResourceLocation newResource = new FakeResourceLocation(resource.getNamespace(), resource.getPath().replaceFirst("^textures/", "mcpatcher/"));
            if (!newResource.equals(resource) && TexturePackAPI.hasResource(newResource)) {
                logger.fine("using %s instead of %s", newResource, resource);
                return newResource;
            }
        }
        return resource;
    }

    private static boolean isOpaque(int pixel) {
        for (int i : SPACERS) {
            if (pixel == i) {
                return false;
            }
        }
        return ((pixel >> 24) & 0xf0) > 0;
    }

    private static boolean printThis(int ch) {
        return "ABCDEF abcdef0123456789".indexOf(ch) >= 0;
    }

    private static float defaultSpaceWidth(float[] charWidthf) {
        if (TexturePackAPI.isDefaultTexturePack()) {
            return 4.0f;
        }
        float sum = 0.0f;
        int n = 0;
        for (char ch : AVERAGE_CHARS) {
            if (charWidthf[ch] > 0.0f) {
                sum += charWidthf[ch];
                n++;
            }
        }
        if (n > 0) {
            return sum / (float) n * 7.0f / 12.0f;
        } else {
            return 4.0f;
        }
    }

    private static void getCharWidthOverrides(FakeResourceLocation font, float[] charWidthf, boolean[] isOverride) {
        FakeResourceLocation textFile = TexturePackAPI.transformResourceLocation(font, ".png", ".properties");
        PropertiesFile props = PropertiesFile.get(logger, textFile);
        if (props == null) {
            return;
        }
        logger.fine("reading character widths from %s", textFile);
        for (Map.Entry<String, String> entry : props.entrySet()) {
            String key = entry.getKey().trim();
            String value = entry.getValue().trim();
            if (key.matches("^width\\.\\d+$") && !value.equals("")) {
                try {
                    int ch = Integer.parseInt(key.substring(6));
                    float width = Float.parseFloat(value);
                    if (ch >= 0 && ch < charWidthf.length) {
                        logger.finer("setting charWidthf[%d '%c'] to %f", ch, (char) ch, width);
                        charWidthf[ch] = width;
                        isOverride[ch] = true;
                    }
                } catch (NumberFormatException e) {
                }
            }
        }
    }
}
