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

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public final class Linking {
    @Setting(value = Paths.LIMIT_PATH, comment = "Container limit. You can also define this using Permission Options, read 'Permission Options' section of the docs.")
    private int limit = 10;

    @Setting(value = Paths.ENABLE_LIMIT_PURCHASE_PATH, comment = "Enable container limit purchase.")
    private boolean enableLimitPurchase = false;

    @Setting(value = Paths.LIMIT_COST_PATH, comment = "Limit cost (requires 'enableLimitPurchase').")
    private int limitCost = 1000;

    @Setting(value = Paths.CONSUME_WAND_PATH, comment = "Consume 'Linking Wand' when a link to a container is created.")
    private boolean consumeLinkingWand = true;

    @Setting(value = Paths.LINKING_COST_PATH, comment = "Cost of linking, 0 to free.")
    private int linkingCost = 15000;

    public int getLimit() {
        return this.limit;
    }

    public boolean isEnableLimitPurchase() {
        return this.enableLimitPurchase;
    }

    public int getLimitCost() {
        return this.limitCost;
    }

    public boolean isConsumeLinkingWand() {
        return this.consumeLinkingWand;
    }

    public int getLinkingCost() {
        return this.linkingCost;
    }

    private static final class Paths {
        static final String LIMIT_PATH = "limit";
        static final String ENABLE_LIMIT_PURCHASE_PATH = "enableLimitPurchase";
        static final String LIMIT_COST_PATH = "limitCost";
        static final String CONSUME_WAND_PATH = "consumeWand";
        static final String LINKING_COST_PATH = "linkingCost";
    }
}
