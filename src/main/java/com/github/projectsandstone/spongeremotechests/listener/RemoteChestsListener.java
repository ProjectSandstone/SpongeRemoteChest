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
package com.github.projectsandstone.spongeremotechests.listener;

import com.github.projectsandstone.spongeremotechests.api.manager.ContainerManager;
import com.github.projectsandstone.spongeremotechests.config.Config;
import com.github.projectsandstone.spongeremotechests.config.Recipes;

import org.spongepowered.api.Game;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.service.ChangeServiceProviderEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class RemoteChestsListener {

    private final Game game;
    private ContainerManager containerManager;
    private Config config;

    public RemoteChestsListener(Game game, Config config) {
        this.game = game;
        this.containerManager = this.game.getServiceManager().provideUnchecked(ContainerManager.class);
    }

    @Listener
    public void providerChange(ChangeServiceProviderEvent event) {
        if (event.getService() == ContainerManager.class) {
            this.containerManager = (ContainerManager) event.getNewProvider();
        }
    }

    @Listener
    public void rightClick(InteractBlockEvent.Secondary event, @First Player player) {

        List<ItemStack> itemStackList = new ArrayList<>();

        player.getInventory().slots().forEach(inventories -> inventories.peek().ifPresent(itemStackList::add));

    }


}
