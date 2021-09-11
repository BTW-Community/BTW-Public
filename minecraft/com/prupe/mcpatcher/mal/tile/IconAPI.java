package com.prupe.mcpatcher.mal.tile;

import net.minecraft.src.Icon;
import net.minecraft.src.TextureStitched;

public class IconAPI {
    public static boolean needRegisterTileAnimations() {
        return false;
    }

    public static int getIconOriginX(TextureStitched icon) {
        return icon.getOriginX();
    }

    public static int getIconOriginY(TextureStitched icon) {
        return icon.getOriginY();
    }

    public static int getIconWidth(Icon icon) {
    	try {
            return Math.round(icon.getSheetWidth() * (icon.getMaxU() - icon.getMinU()));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int getIconHeight(Icon icon) {
    	try {
            return Math.round(icon.getSheetHeight() * (icon.getMaxV() - icon.getMinV()));
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static String getIconName(Icon icon) {
        return icon.getIconName();
    }
}
