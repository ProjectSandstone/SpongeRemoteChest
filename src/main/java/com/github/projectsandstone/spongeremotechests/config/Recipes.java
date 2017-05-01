/**
 *      SpongeRemoteChests - Access your containers remotely.
 *
 *         The MIT License (MIT)
 *
 *      Copyright (c) 2017 Sandstone <https://github.com/ProjectSandstone/SpongeRemoteChests/>
 *      Copyright (c) contributors
 *
 *
 *      Permission is hereby granted, free of charge, to any person obtaining a copy
 *      of this software and associated documentation files (the "Software"), to deal
 *      in the Software without restriction, including without limitation the rights
 *      to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *      copies of the Software, and to permit persons to whom the Software is
 *      furnished to do so, subject to the following conditions:
 *
 *      The above copyright notice and this permission notice shall be included in
 *      all copies or substantial portions of the Software.
 *
 *      THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *      IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *      FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *      AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *      LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *      OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *      THE SOFTWARE.
 */
package com.github.projectsandstone.spongeremotechests.config;

import com.github.projectsandstone.spongeremotechests.data.RemoteChestsKeys;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.item.Enchantments;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.ShapedRecipe;

import java.util.Collections;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Recipes {

    @Setting(value = Paths.ENABLE_WAND_RECIPE_PATH, comment = "Enable the wand recipe.")
    private boolean enableWandRecipes = true;

    @Setting(value = Paths.ENABLE_WAND_PURCHASE_PATH, comment = "Enable the wand purchase.")
    private boolean enableWandPurchase = false;

    @Setting(value = Paths.WAND_COST_PATH, comment = "Wand cost.")
    private int wandCost = 0;

    @Setting(value = Paths.WAND_RECIPE_PATH, comment = "Recipe of the 'Linking Wand'.")
    private ShapedRecipe linkingWandRecipe = Sponge.getRegistry()
            .createBuilder(ShapedRecipe.Builder.class)
            .width(1)
            .height(3)
            .ingredient(1, 0, ItemStack.of(ItemTypes.ENDER_EYE, 1))
            .ingredient(1, 1, ItemStack.of(ItemTypes.BLAZE_ROD, 1))
            .ingredient(1, 2, ItemStack.of(ItemTypes.BLAZE_ROD, 1))
            .addResult(Recipes.createBlazeRodStack())
            .build();


    public boolean isEnableWandRecipes() {
        return this.enableWandRecipes;
    }

    public boolean isEnableWandPurchase() {
        return this.enableWandPurchase;
    }

    public int getWandCost() {
        return this.wandCost;
    }

    public ShapedRecipe getLinkingWandRecipe() {
        return this.linkingWandRecipe;
    }

    private static ItemStack createBlazeRodStack() {
        ItemStack of = ItemStack.of(ItemTypes.BLAZE_ROD, 1);

        of.offer(Keys.HIDE_ENCHANTMENTS, true);
        of.offer(Keys.ITEM_ENCHANTMENTS, Collections.singletonList(new ItemEnchantment(Enchantments.UNBREAKING, 1)));
        of.offer(RemoteChestsKeys.LINKING_WAND, true);

        return of;
    }

    private static final class Paths {
        static final String ENABLE_WAND_RECIPE_PATH = "enableWandRecipe";
        static final String ENABLE_WAND_PURCHASE_PATH = "enableWandPurchase";
        static final String WAND_COST_PATH = "wandCost";
        static final String WAND_RECIPE_PATH = "wandRecipe";
    }
}
