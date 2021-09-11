package com.prupe.mcpatcher.mal.item;

import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import net.minecraft.src.Item;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class ItemAPI {
    private static final HashMap<String, Integer> canonicalIdByName = new HashMap<String, Integer>();

    static {
        canonicalIdByName.put("minecraft:iron_shovel", 256);
        canonicalIdByName.put("minecraft:iron_pickaxe", 257);
        canonicalIdByName.put("minecraft:iron_axe", 258);
        canonicalIdByName.put("minecraft:flint_and_steel", 259);
        canonicalIdByName.put("minecraft:apple", 260);
        canonicalIdByName.put("minecraft:bow", 261);
        canonicalIdByName.put("minecraft:arrow", 262);
        canonicalIdByName.put("minecraft:coal", 263);
        canonicalIdByName.put("minecraft:diamond", 264);
        canonicalIdByName.put("minecraft:iron_ingot", 265);
        canonicalIdByName.put("minecraft:gold_ingot", 266);
        canonicalIdByName.put("minecraft:iron_sword", 267);
        canonicalIdByName.put("minecraft:wooden_sword", 268);
        canonicalIdByName.put("minecraft:wooden_shovel", 269);
        canonicalIdByName.put("minecraft:wooden_pickaxe", 270);
        canonicalIdByName.put("minecraft:wooden_axe", 271);
        canonicalIdByName.put("minecraft:stone_sword", 272);
        canonicalIdByName.put("minecraft:stone_shovel", 273);
        canonicalIdByName.put("minecraft:stone_pickaxe", 274);
        canonicalIdByName.put("minecraft:stone_axe", 275);
        canonicalIdByName.put("minecraft:diamond_sword", 276);
        canonicalIdByName.put("minecraft:diamond_shovel", 277);
        canonicalIdByName.put("minecraft:diamond_pickaxe", 278);
        canonicalIdByName.put("minecraft:diamond_axe", 279);
        canonicalIdByName.put("minecraft:stick", 280);
        canonicalIdByName.put("minecraft:bowl", 281);
        canonicalIdByName.put("minecraft:mushroom_stew", 282);
        canonicalIdByName.put("minecraft:golden_sword", 283);
        canonicalIdByName.put("minecraft:golden_shovel", 284);
        canonicalIdByName.put("minecraft:golden_pickaxe", 285);
        canonicalIdByName.put("minecraft:golden_axe", 286);
        canonicalIdByName.put("minecraft:string", 287);
        canonicalIdByName.put("minecraft:feather", 288);
        canonicalIdByName.put("minecraft:gunpowder", 289);
        canonicalIdByName.put("minecraft:wooden_hoe", 290);
        canonicalIdByName.put("minecraft:stone_hoe", 291);
        canonicalIdByName.put("minecraft:iron_hoe", 292);
        canonicalIdByName.put("minecraft:diamond_hoe", 293);
        canonicalIdByName.put("minecraft:golden_hoe", 294);
        canonicalIdByName.put("minecraft:wheat_seeds", 295);
        canonicalIdByName.put("minecraft:wheat", 296);
        canonicalIdByName.put("minecraft:bread", 297);
        canonicalIdByName.put("minecraft:leather_helmet", 298);
        canonicalIdByName.put("minecraft:leather_chestplate", 299);
        canonicalIdByName.put("minecraft:leather_leggings", 300);
        canonicalIdByName.put("minecraft:leather_boots", 301);
        canonicalIdByName.put("minecraft:chainmail_helmet", 302);
        canonicalIdByName.put("minecraft:chainmail_chestplate", 303);
        canonicalIdByName.put("minecraft:chainmail_leggings", 304);
        canonicalIdByName.put("minecraft:chainmail_boots", 305);
        canonicalIdByName.put("minecraft:iron_helmet", 306);
        canonicalIdByName.put("minecraft:iron_chestplate", 307);
        canonicalIdByName.put("minecraft:iron_leggings", 308);
        canonicalIdByName.put("minecraft:iron_boots", 309);
        canonicalIdByName.put("minecraft:diamond_helmet", 310);
        canonicalIdByName.put("minecraft:diamond_chestplate", 311);
        canonicalIdByName.put("minecraft:diamond_leggings", 312);
        canonicalIdByName.put("minecraft:diamond_boots", 313);
        canonicalIdByName.put("minecraft:golden_helmet", 314);
        canonicalIdByName.put("minecraft:golden_chestplate", 315);
        canonicalIdByName.put("minecraft:golden_leggings", 316);
        canonicalIdByName.put("minecraft:golden_boots", 317);
        canonicalIdByName.put("minecraft:flint", 318);
        canonicalIdByName.put("minecraft:porkchop", 319);
        canonicalIdByName.put("minecraft:cooked_porkchop", 320);
        canonicalIdByName.put("minecraft:painting", 321);
        canonicalIdByName.put("minecraft:golden_apple", 322);
        canonicalIdByName.put("minecraft:sign", 323);
        canonicalIdByName.put("minecraft:wooden_door", 324);
        canonicalIdByName.put("minecraft:bucket", 325);
        canonicalIdByName.put("minecraft:water_bucket", 326);
        canonicalIdByName.put("minecraft:lava_bucket", 327);
        canonicalIdByName.put("minecraft:minecart", 328);
        canonicalIdByName.put("minecraft:saddle", 329);
        canonicalIdByName.put("minecraft:iron_door", 330);
        canonicalIdByName.put("minecraft:redstone", 331);
        canonicalIdByName.put("minecraft:snowball", 332);
        canonicalIdByName.put("minecraft:boat", 333);
        canonicalIdByName.put("minecraft:leather", 334);
        canonicalIdByName.put("minecraft:milk_bucket", 335);
        canonicalIdByName.put("minecraft:brick", 336);
        canonicalIdByName.put("minecraft:clay_ball", 337);
        canonicalIdByName.put("minecraft:reeds", 338);
        canonicalIdByName.put("minecraft:paper", 339);
        canonicalIdByName.put("minecraft:book", 340);
        canonicalIdByName.put("minecraft:slime_ball", 341);
        canonicalIdByName.put("minecraft:chest_minecart", 342);
        canonicalIdByName.put("minecraft:furnace_minecart", 343);
        canonicalIdByName.put("minecraft:egg", 344);
        canonicalIdByName.put("minecraft:compass", 345);
        canonicalIdByName.put("minecraft:fishing_rod", 346);
        canonicalIdByName.put("minecraft:clock", 347);
        canonicalIdByName.put("minecraft:glowstone_dust", 348);
        canonicalIdByName.put("minecraft:fish", 349);
        canonicalIdByName.put("minecraft:cooked_fished", 350);
        canonicalIdByName.put("minecraft:dye", 351);
        canonicalIdByName.put("minecraft:bone", 352);
        canonicalIdByName.put("minecraft:sugar", 353);
        canonicalIdByName.put("minecraft:cake", 354);
        canonicalIdByName.put("minecraft:bed", 355);
        canonicalIdByName.put("minecraft:repeater", 356);
        canonicalIdByName.put("minecraft:cookie", 357);
        canonicalIdByName.put("minecraft:filled_map", 358);
        canonicalIdByName.put("minecraft:shears", 359);
        canonicalIdByName.put("minecraft:melon", 360);
        canonicalIdByName.put("minecraft:pumpkin_seeds", 361);
        canonicalIdByName.put("minecraft:melon_seeds", 362);
        canonicalIdByName.put("minecraft:beef", 363);
        canonicalIdByName.put("minecraft:cooked_beef", 364);
        canonicalIdByName.put("minecraft:chicken", 365);
        canonicalIdByName.put("minecraft:cooked_chicken", 366);
        canonicalIdByName.put("minecraft:rotten_flesh", 367);
        canonicalIdByName.put("minecraft:ender_pearl", 368);
        canonicalIdByName.put("minecraft:blaze_rod", 369);
        canonicalIdByName.put("minecraft:ghast_tear", 370);
        canonicalIdByName.put("minecraft:gold_nugget", 371);
        canonicalIdByName.put("minecraft:nether_wart", 372);
        canonicalIdByName.put("minecraft:potion", 373);
        canonicalIdByName.put("minecraft:glass_bottle", 374);
        canonicalIdByName.put("minecraft:spider_eye", 375);
        canonicalIdByName.put("minecraft:fermented_spider_eye", 376);
        canonicalIdByName.put("minecraft:blaze_powder", 377);
        canonicalIdByName.put("minecraft:magma_cream", 378);
        canonicalIdByName.put("minecraft:brewing_stand", 379);
        canonicalIdByName.put("minecraft:cauldron", 380);
        canonicalIdByName.put("minecraft:ender_eye", 381);
        canonicalIdByName.put("minecraft:speckled_melon", 382);
        canonicalIdByName.put("minecraft:spawn_egg", 383);
        canonicalIdByName.put("minecraft:experience_bottle", 384);
        canonicalIdByName.put("minecraft:fire_charge", 385);
        canonicalIdByName.put("minecraft:writable_book", 386);
        canonicalIdByName.put("minecraft:written_book", 387);
        canonicalIdByName.put("minecraft:emerald", 388);
        canonicalIdByName.put("minecraft:item_frame", 389);
        canonicalIdByName.put("minecraft:flower_pot", 390);
        canonicalIdByName.put("minecraft:carrot", 391);
        canonicalIdByName.put("minecraft:potato", 392);
        canonicalIdByName.put("minecraft:baked_potato", 393);
        canonicalIdByName.put("minecraft:poisonous_potato", 394);
        canonicalIdByName.put("minecraft:map", 395);
        canonicalIdByName.put("minecraft:golden_carrot", 396);
        canonicalIdByName.put("minecraft:skull", 397);
        canonicalIdByName.put("minecraft:carrot_on_a_stick", 398);
        canonicalIdByName.put("minecraft:nether_star", 399);
        canonicalIdByName.put("minecraft:pumpkin_pie", 400);
        canonicalIdByName.put("minecraft:fireworks", 401);
        canonicalIdByName.put("minecraft:firework_charge", 402);
        canonicalIdByName.put("minecraft:enchanted_book", 403);
        canonicalIdByName.put("minecraft:comparator", 404);
        canonicalIdByName.put("minecraft:netherbrick", 405);
        canonicalIdByName.put("minecraft:quartz", 406);
        canonicalIdByName.put("minecraft:tnt_minecart", 407);
        canonicalIdByName.put("minecraft:hopper_minecart", 408);
        canonicalIdByName.put("minecraft:iron_horse_armor", 417);
        canonicalIdByName.put("minecraft:golden_horse_armor", 418);
        canonicalIdByName.put("minecraft:diamond_horse_armor", 419);
        canonicalIdByName.put("minecraft:lead", 420);
        canonicalIdByName.put("minecraft:name_tag", 421);
        canonicalIdByName.put("minecraft:record_13", 2256);
        canonicalIdByName.put("minecraft:record_cat", 2257);
        canonicalIdByName.put("minecraft:record_blocks", 2258);
        canonicalIdByName.put("minecraft:record_chirp", 2259);
        canonicalIdByName.put("minecraft:record_far", 2260);
        canonicalIdByName.put("minecraft:record_mall", 2261);
        canonicalIdByName.put("minecraft:record_mellohi", 2262);
        canonicalIdByName.put("minecraft:record_stal", 2263);
        canonicalIdByName.put("minecraft:record_strad", 2264);
        canonicalIdByName.put("minecraft:record_ward", 2265);
        canonicalIdByName.put("minecraft:record_11", 2266);
        canonicalIdByName.put("minecraft:record_wait", 2267);
    }

    public static Item getFixedItem(String name) {
        Item item = parseItemName(name);
        if (item == null) {
            throw new IllegalArgumentException("unknown item " + name);
        } else {
            return item;
        }
    }

    public static Item parseItemName(String name) {
        if (MCPatcherUtils.isNullOrEmpty(name)) {
            return null;
        }
        
        Integer id;
        
        if (name.matches("\\d+")) {
            id = Integer.parseInt(name);
        }
        else {
        	id = canonicalIdByName.get(getFullName(name));
        }
        
        return id == null ? null : id >= 0 && id < Item.itemsList.length ? Item.itemsList[id] : null;
    }

    public static String getItemName(Item item) {
    	if (item == null) {
    		return "(null)";
    	}
    	
    	int id = item.itemID;
        for (Map.Entry<String, Integer> entry : canonicalIdByName.entrySet()) {
            if (id == entry.getValue()) {
                return entry.getKey();
            }
        }
        return String.valueOf(id);
    }

    public static List<Item> getAllItems() {
        List<Item> items = new ArrayList<Item>();
        for (Iterator<Item> i = Arrays.asList(Item.itemsList).iterator(); i.hasNext(); ) {
            Item item = i.next();
            if (item != null && !items.contains(item)) {
                items.add(item);
            }
        }
        return items;
    }

    public static String getFullName(String name) {
        return name == null ? null : name.indexOf(':') >= 0 ? name : "minecraft:" + name;
    }

    public static String expandTileName(String tileName) {
        return tileName;
    }
}
