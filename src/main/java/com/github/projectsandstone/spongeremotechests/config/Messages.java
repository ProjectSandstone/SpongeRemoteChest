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

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import java.util.Collections;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class Messages {
    @Setting(Paths.NOT_ENOUGH_FOUNDS_PATH)
    private TextTemplate notEnoughFunds = TextTemplate.of(TextColors.RED,
            "Not enough funds. You must to have: ",
            TextTemplate.arg("money").color(TextColors.GOLD).style(TextStyles.BOLD),
            " to create a container link.");

    @Setting(Paths.PROTECTED_CONTAINER_PATH)
    private Text protectedContainer = Text.of(TextColors.RED,
            "This container is protected! Use ",
            Text.of(TextColors.GREEN, "/rmc remove broken"),
            " to remove all broken links.");

    @Setting(Paths.BROKEN_LINK_PATH)
    private Text brokenLink = Text.of(TextColors.RED,
            "Cannot find container! Use ",
            Text.of(TextColors.GREEN, "/rmc remove broken"),
            " to remove all broken links.");


    @Setting(Paths.LIMIT_EXCEEDED_PATH)
    private TextTemplate limitExceeded = TextTemplate.of(TextColors.RED,
            "You have exceeded the max number of chests. Max chests: ",
            TextTemplate.arg("limit").color(TextColors.GREEN),
            " to remove all broken links.");


    public TextTemplate getNotEnoughFunds() {
        return this.notEnoughFunds;
    }

    public Text getProtectedContainer() {
        return this.protectedContainer;
    }

    public Text getBrokenLink() {
        return this.brokenLink;
    }

    public TextTemplate getLimitExceeded() {
        return this.limitExceeded;
    }

    public Text getNotEnoughFunds(int currentMoney) {
        return this.getNotEnoughFunds().apply(Collections.singletonMap("money", currentMoney)).build();
    }

    public Text getLimitExceeded(int limit) {
        return this.getLimitExceeded().apply(Collections.singletonMap("limit", limit)).build();
    }

    private static final class Paths {
        static final String NOT_ENOUGH_FOUNDS_PATH = "notEnoughFunds";
        static final String PROTECTED_CONTAINER_PATH = "protectedContainer";
        static final String BROKEN_LINK_PATH = "brokenLink";
        static final String LIMIT_EXCEEDED_PATH = "limitExceeded";
    }

}
