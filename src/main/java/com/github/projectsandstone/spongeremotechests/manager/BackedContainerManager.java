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

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;
import com.github.projectsandstone.spongeremotechests.api.manager.ContainerManager;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.User;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Implements a basic logic for container managers and back calls to {@link #backend}.
 */
public final class BackedContainerManager implements ContainerManager {

    private final Backend backend;

    public BackedContainerManager(Backend backend) {
        this.backend = backend;
    }

    @Override
    public CompletableFuture<Boolean> registerUserContainer(@NotNull User user, @NotNull RemoteContainer remoteContainer) {
        return this.register(user, remoteContainer);
    }

    @Override
    public CompletableFuture<Boolean> unregisterUserContainer(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate) {
        return this.unregister(user, predicate);
    }

    @Override
    public @NotNull CompletableFuture<Optional<RemoteContainer>> getUserContainer(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate) {

        return this.backend.hasAnyContainer(user)
                .thenCompose(aBoolean -> {
                    if (aBoolean == Boolean.FALSE)
                        return CompletableFuture.completedFuture(Optional.<RemoteContainer>empty());
                    else {
                        return this.backend.getAllContainers(user)
                                .thenApply(remoteContainers -> remoteContainers.stream().filter(predicate).findFirst());
                    }
                });
    }

    @Override
    public @NotNull CompletableFuture<Set<RemoteContainer>> getUserContainers(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate) {

        return this.backend.hasAnyContainer(user)
                .thenCompose(aBoolean -> {
                    if (aBoolean == Boolean.FALSE)
                        return CompletableFuture.completedFuture(Collections.emptySet());
                    else
                        return this.backend.getAllContainers(user)
                                .thenApply(remoteContainers -> remoteContainers.stream().filter(predicate).collect(Collectors.toSet()));
                });

    }

    @Override
    public CompletableFuture<Boolean> isOwner(@NotNull User user, @NotNull RemoteContainer container) {

        return this.backend.isOwner(user, container);
    }

    @Override
    public @NotNull CompletableFuture<Set<RemoteContainer>> getUserContainers(@NotNull User user) {
        return this.backend.hasAnyContainer(user)
                .thenCompose(aBoolean -> {
                    if (aBoolean == Boolean.FALSE)
                        return CompletableFuture.completedFuture(Collections.emptySet());
                    else
                        return this.backend.getAllContainers(user);
                });

    }

    @Override
    public @NotNull CompletableFuture<Map<User, Set<RemoteContainer>>> getAllContainers() {
        return this.backend.getAllContainers();
    }

    private CompletableFuture<Boolean> register(User user, RemoteContainer remoteContainer) {
        return this.backend.register(user, remoteContainer);
    }

    private CompletableFuture<Boolean> unregister(User user, Predicate<RemoteContainer> predicate) {
        return this.backend.unregister(user, predicate);
    }

}
