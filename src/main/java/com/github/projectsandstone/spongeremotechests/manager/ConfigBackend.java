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
package com.github.projectsandstone.spongeremotechests.manager;

import com.google.common.reflect.TypeToken;

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;
import com.github.projectsandstone.spongeremotechests.util.MapSetView;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.User;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

/**
 * Cache values in a map and save in a configuration.
 */
public final class ConfigBackend extends Backend {
    private static final TypeToken<Map<User, Set<RemoteContainer>>> MAP_TOKEN = new TypeToken<Map<User, Set<RemoteContainer>>>() {
    };

    private final ConfigurationLoader<? extends ConfigurationNode> loader;
    private final ConfigurationNode node;
    private final Map<User, Set<RemoteContainer>> map;
    private final MapSetView<User, RemoteContainer> view;

    public ConfigBackend(ConfigurationLoader<? extends ConfigurationNode> loader) {
        this.loader = loader;

        try {
            this.node = this.loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.map = Objects.requireNonNull(this.node).getValue(MAP_TOKEN, new HashMap<>());
            this.view = new MapSetView<>(this.map);
        } catch (ObjectMappingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAnyContainer(@NotNull User user) {
        return CompletableFuture.completedFuture(
                !this.map.containsKey(user)
                        || Objects.requireNonNull(this.map.get(user)).isEmpty()
        );
    }

    @Override
    public @NotNull CompletableFuture<Set<RemoteContainer>> getAllContainers(@NotNull User user) {
        if (!this.map.containsKey(user))
            return CompletableFuture.completedFuture(Collections.emptySet());


        return CompletableFuture.completedFuture(Collections.unmodifiableSet(this.map.get(user)));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isOwner(@NotNull User user, @NotNull RemoteContainer container) {
        return CompletableFuture.completedFuture(this.map.containsKey(user) && this.map.get(user).contains(container));
    }

    @Override
    public @NotNull CompletableFuture<Map<User, Set<RemoteContainer>>> getAllContainers() {
        return CompletableFuture.completedFuture(this.view);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> register(@NotNull User user, @NotNull RemoteContainer remoteContainer) {

        if(!this.map.containsKey(user))
            this.map.put(user, new HashSet<>());
        else if(this.map.get(user).contains(remoteContainer))
            return CompletableFuture.completedFuture(Boolean.FALSE);

        this.map.get(user).add(remoteContainer);

        return CompletableFuture.completedFuture(Boolean.TRUE);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> unregister(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate) {

        if(!this.map.containsKey(user))
            return CompletableFuture.completedFuture(Boolean.FALSE);

        return CompletableFuture.completedFuture(this.map.get(user).removeIf(predicate));
    }

}
