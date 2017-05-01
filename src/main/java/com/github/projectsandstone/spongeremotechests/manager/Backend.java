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

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.User;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

public abstract class Backend {

    /**
     * Returns a {@link CompletableFuture} of a boolean that indicates if user has any {@link
     * RemoteContainer} or not.
     *
     * @param user User to get containers
     * @return {@link CompletableFuture} of a boolean that indicates if user has any {@link
     * RemoteContainer} or not.
     */
    @NotNull
    public abstract CompletableFuture<Boolean> hasAnyContainer(@NotNull User user);

    /**
     * Returns a {@link CompletableFuture} of immutable set of all containers of {@code user}.
     *
     * @param user User to check.
     * @return {@link CompletableFuture} of immutable set of all containers of {@code user}.
     */
    @NotNull
    public abstract CompletableFuture<Set<RemoteContainer>> getAllContainers(@NotNull User user);

    /**
     * Returns a {@link CompletableFuture} of a boolean that indicates if {@code user} is owner of
     * {@code container}.
     *
     * @param user      User to check.
     * @param container Container to check whether {@code user} is owner or not.
     * @return {@link CompletableFuture} of a boolean that indicates if {@code user} is owner of
     * {@code container}.
     */
    @NotNull
    public abstract CompletableFuture<Boolean> isOwner(@NotNull User user, @NotNull RemoteContainer container);

    /**
     * Returns a {@link CompletableFuture} of a view map of all registered containers.
     *
     * @return {@link CompletableFuture} of a view map of all registered containers.
     */
    @NotNull
    public abstract CompletableFuture<Map<User, Set<RemoteContainer>>> getAllContainers();

    /**
     * Registers the {@code remoteContainer} for {@code user}.
     *
     * @param user            Owner of container.
     * @param remoteContainer Container to link.
     * @return A {@link CompletableFuture} of a boolean that indicates if this container was
     * registered successfully. Normally, this boolean is only {@link Boolean#FALSE} if the
     * container is already registered.
     */
    @NotNull
    public abstract CompletableFuture<Boolean> register(@NotNull User user, @NotNull RemoteContainer remoteContainer);

    /**
     * Unregisters all {@code user} {@link RemoteContainer RemoteContainers} which matches {@code
     * predicate}.
     *
     * @param user      User to unregister
     * @param predicate Predicate to test which {@link RemoteContainer RemoteContainers} to
     *                  unregister.
     * @return {@link CompletableFuture} of a boolean that indicates if any {@link RemoteContainer}
     * was removed as result of this action.
     */
    @NotNull
    public abstract CompletableFuture<Boolean> unregister(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate);
}
