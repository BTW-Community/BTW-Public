package com.prupe.mcpatcher.cit;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.item.ItemAPI;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.ResourceList;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import com.prupe.mcpatcher.mal.tile.TileLoader;
import net.minecraft.src.*;

import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.util.*;

public class CITUtils {
    private static final MCLogger logger = MCLogger.getLogger(MCPatcherUtils.CUSTOM_ITEM_TEXTURES, "CIT");

    static final String CIT_PROPERTIES = "cit.properties";
    private static final FakeResourceLocation CIT_PROPERTIES1 = TexturePackAPI.newMCPatcherResourceLocation(CIT_PROPERTIES);
    private static final FakeResourceLocation CIT_PROPERTIES2 = TexturePackAPI.newMCPatcherResourceLocation("cit/" + CIT_PROPERTIES);
    static final FakeResourceLocation FIXED_ARMOR_RESOURCE = new FakeResourceLocation("textures/models/armor/iron_layer_1.png");

    static final int MAX_ENCHANTMENTS = 256;

    private static Item itemEnchantedBook;
    static Item itemCompass;
    static Item itemClock;

    static final boolean enableItems = Config.getBoolean(MCPatcherUtils.CUSTOM_ITEM_TEXTURES, "items", true);
    static final boolean enableEnchantments = Config.getBoolean(MCPatcherUtils.CUSTOM_ITEM_TEXTURES, "enchantments", true);
    static final boolean enableArmor = Config.getBoolean(MCPatcherUtils.CUSTOM_ITEM_TEXTURES, "armor", true);

    private static TileLoader tileLoader;
    private static final Map<Item, List<ItemOverride>> items = new IdentityHashMap<Item, List<ItemOverride>>();
    private static final Map<Item, List<Enchantment>> enchantments = new IdentityHashMap<Item, List<Enchantment>>();
    private static final List<Enchantment> allItemEnchantments = new ArrayList<Enchantment>();
    private static final Map<Item, List<ArmorOverride>> armors = new IdentityHashMap<Item, List<ArmorOverride>>();

    static boolean useGlint;

    private static EnchantmentList armorMatches;
    private static int armorMatchIndex;

    private static ItemStack lastItemStack;
    private static int lastRenderPass;
    static Icon lastOrigIcon;
    private static Icon lastIcon;

    private static Field potionItemStackField;

    static {
        for (Field f : EntityPotion.class.getDeclaredFields()) {
            if (ItemStack.class.isAssignableFrom(f.getType())) {
                f.setAccessible(true);
                potionItemStackField = f;
                break;
            }
        }

        TexturePackChangeHandler.register(new TexturePackChangeHandler(MCPatcherUtils.CUSTOM_ITEM_TEXTURES, 3) {
            @Override
            public void beforeChange() {
                itemEnchantedBook = ItemAPI.getFixedItem("minecraft:enchanted_book");
                itemCompass = ItemAPI.getFixedItem("minecraft:compass");
                itemClock = ItemAPI.getFixedItem("minecraft:clock");

                tileLoader = new TileLoader("textures/items", logger);
                items.clear();
                enchantments.clear();
                allItemEnchantments.clear();
                armors.clear();
                lastOrigIcon = null;
                lastIcon = null;

                BufferedImage image = TexturePackAPI.getImage(FIXED_ARMOR_RESOURCE);
                if (image == null) {
                    Enchantment.baseArmorWidth = 64.0f;
                    Enchantment.baseArmorHeight = 32.0f;
                } else {
                    Enchantment.baseArmorWidth = image.getWidth();
                    Enchantment.baseArmorHeight = image.getHeight();
                }

                PropertiesFile properties = PropertiesFile.get(logger, CIT_PROPERTIES1);
                if (properties == null) {
                    properties = PropertiesFile.getNonNull(logger, CIT_PROPERTIES2);
                }
                useGlint = properties.getBoolean("useGlint", true);
                EnchantmentList.setProperties(properties);

                if (enableItems || enableEnchantments || enableArmor) {
                    for (FakeResourceLocation resource : ResourceList.getInstance().listResources(TexturePackAPI.MCPATCHER_SUBDIR + "cit", ".properties", true)) {
                        registerOverride(OverrideBase.create(resource));
                    }
                    if (enableItems) {
                        PotionReplacer replacer = new PotionReplacer();
                        for (ItemOverride override : replacer.overrides) {
                            registerOverride(override);
                        }
                    }
                }
            }

            @Override
            public void afterChange() {
                for (List<ItemOverride> list : items.values()) {
                    for (OverrideBase override : list) {
                        ((ItemOverride) override).registerIcon(tileLoader);
                    }
                    Collections.sort(list);
                }
                for (List<Enchantment> list : enchantments.values()) {
                    list.addAll(allItemEnchantments);
                    Collections.sort(list);
                }
                Collections.sort(allItemEnchantments);
                for (List<ArmorOverride> list : armors.values()) {
                    Collections.sort(list);
                }
            }

            @SuppressWarnings("unchecked")
            private void registerOverride(OverrideBase override) {
                if (override != null && override.properties.valid()) {
                    Map map;
                    if (override instanceof ItemOverride) {
                        ((ItemOverride) override).preload(tileLoader);
                        map = items;
                    } else if (override instanceof Enchantment) {
                        map = enchantments;
                    } else if (override instanceof ArmorOverride) {
                        map = armors;
                    } else {
                        logger.severe("unknown ItemOverride type %d", override.getClass().getName());
                        return;
                    }
                    if (override.items == null) {
                        if (override instanceof Enchantment) {
                            logger.fine("registered %s to all items", override);
                            allItemEnchantments.add((Enchantment) override);
                        }
                    } else {
                        int i = 0;
                        for (Item item : override.items) {
                            registerOverride(map, item, override);
                            if (i < 10) {
                                logger.fine("registered %s to item %s", override, ItemAPI.getItemName(item));
                            } else if (i == 10) {
                                logger.fine("... %d total", override.items.size());
                            }
                            i++;
                        }
                    }
                }
            }

            private void registerOverride(Map<Item, List<OverrideBase>> map, Item item, OverrideBase override) {
                List<OverrideBase> list = map.get(item);
                if (list == null) {
                    list = new ArrayList<OverrideBase>();
                    map.put(item, list);
                }
                list.add(override);
            }
        });
    }

    public static void init() {
    }

    public static Icon getIcon(Icon icon, ItemStack itemStack, int renderPass) {
        if (icon == lastIcon && itemStack == lastItemStack && renderPass == lastRenderPass) {
            return icon;
        }
        lastOrigIcon = lastIcon = icon;
        lastItemStack = itemStack;
        lastRenderPass = renderPass;
        if (enableItems) {
            ItemOverride override = findItemOverride(itemStack);
            if (override != null) {
                Icon newIcon = override.getReplacementIcon(icon);
                if (newIcon != null) {
                    lastIcon = newIcon;
                }
            }
        }
        return lastIcon;
    }

    public static Icon getEntityIcon(Icon icon, Entity entity) {
        if (entity instanceof EntityPotion) {
            if (potionItemStackField != null) {
                try {
                    return getIcon(icon, (ItemStack) potionItemStackField.get(entity), 1);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    potionItemStackField = null;
                }
            }
        }
        return icon;
    }

    public static FakeResourceLocation getArmorTexture(FakeResourceLocation texture, EntityLiving entity, ItemStack itemStack) {
        if (enableArmor) {
            ArmorOverride override = findArmorOverride(itemStack);
            if (override != null) {
                FakeResourceLocation newTexture = override.getReplacementTexture(texture);
                if (newTexture != null) {
                    return newTexture;
                }
            }
        }
        return texture;
    }

    private static <T extends OverrideBase> T findMatch(Map<Item, List<T>> overrides, ItemStack itemStack) {
        Item item = itemStack.getItem();
        List<T> list = overrides.get(item);
        if (list != null) {
            int[] enchantmentLevels = getEnchantmentLevels(item, itemStack.getTagCompound());
            boolean hasEffect = itemStack.hasEffect();
            for (T override : list) {
                if (override.match(itemStack, enchantmentLevels, hasEffect)) {
                    return override;
                }
            }
        }
        return null;
    }

    static ItemOverride findItemOverride(ItemStack itemStack) {
        return findMatch(items, itemStack);
    }

    static ArmorOverride findArmorOverride(ItemStack itemStack) {
        return findMatch(armors, itemStack);
    }

    static EnchantmentList findEnchantments(ItemStack itemStack) {
        return new EnchantmentList(enchantments, allItemEnchantments, itemStack);
    }

    public static boolean renderEnchantmentHeld(ItemStack itemStack, int renderPass) {
        if (itemStack == null || renderPass != 0) {
            return true;
        }
        if (!enableEnchantments) {
            return false;
        }
        EnchantmentList matches = findEnchantments(itemStack);
        if (matches.isEmpty()) {
            return !useGlint;
        }
        int width;
        int height;
        if (lastIcon == null) {
            width = height = 256;
        } else {
            width = IconAPI.getIconWidth(lastIcon);
            height = IconAPI.getIconHeight(lastIcon);
        }
        Enchantment.beginOuter3D();
        for (int i = 0; i < matches.size(); i++) {
            matches.getEnchantment(i).render3D(TessellatorAPI.getTessellator(), matches.getIntensity(i), width, height);
        }
        Enchantment.endOuter3D();
        return !useGlint;
    }

    public static boolean renderEnchantmentDropped(ItemStack itemStack) {
        return renderEnchantmentHeld(itemStack, lastRenderPass);
    }

    public static boolean renderEnchantmentGUI(ItemStack itemStack, int x, int y, float z) {
        if (!enableEnchantments || itemStack == null) {
            return false;
        }
        EnchantmentList matches = findEnchantments(itemStack);
        if (matches.isEmpty()) {
            return !useGlint;
        }
        Enchantment.beginOuter2D();
        for (int i = 0; i < matches.size(); i++) {
            matches.getEnchantment(i).render2D(TessellatorAPI.getTessellator(), matches.getIntensity(i), x, y, x + 16, y + 16, z);
        }
        Enchantment.endOuter2D();
        return !useGlint;
    }

    public static boolean setupArmorEnchantments(EntityLiving entity, int pass) {
        return setupArmorEnchantments(entity.getCurrentItemOrArmor(4 - pass));
    }

    public static boolean setupArmorEnchantments(ItemStack itemStack) {
        armorMatches = null;
        armorMatchIndex = 0;
        if (enableEnchantments && itemStack != null) {
            EnchantmentList tmpList = findEnchantments(itemStack);
            if (!tmpList.isEmpty()) {
                armorMatches = tmpList;
            }
        }
        return isArmorEnchantmentActive() || !useGlint;
    }

    public static boolean preRenderArmorEnchantment() {
        if (isArmorEnchantmentActive()) {
            Enchantment enchantment = armorMatches.getEnchantment(armorMatchIndex);
            if (enchantment.bindTexture(lastOrigIcon)) {
                enchantment.beginArmor(armorMatches.getIntensity(armorMatchIndex));
                return true;
            } else {
                return false;
            }
        } else {
            armorMatches = null;
            armorMatchIndex = 0;
            return false;
        }
    }

    public static boolean isArmorEnchantmentActive() {
        return armorMatches != null && armorMatchIndex < armorMatches.size();
    }

    public static void postRenderArmorEnchantment() {
        armorMatches.getEnchantment(armorMatchIndex).endArmor();
        armorMatchIndex++;
    }

    static int[] getEnchantmentLevels(Item item, NBTTagCompound nbt) {
        int[] levels = null;
        if (nbt != null) {
            NBTBase base;
            if (item == itemEnchantedBook) {
                base = nbt.getTag("StoredEnchantments");
            } else {
                base = nbt.getTag("ench");
            }
            if (base instanceof NBTTagList) {
                NBTTagList list = (NBTTagList) base;
                for (int i = 0; i < list.tagCount(); i++) {
                    base = list.tagAt(i);
                    if (base instanceof NBTTagCompound) {
                        short id = ((NBTTagCompound) base).getShort("id");
                        short level = ((NBTTagCompound) base).getShort("lvl");
                        if (id >= 0 && id < MAX_ENCHANTMENTS && level > 0) {
                            if (levels == null) {
                                levels = new int[MAX_ENCHANTMENTS];
                            }
                            levels[id] += level;
                        }
                    }
                }
            }
        }
        return levels;
    }
}
