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
package com.github.projectsandstone.spongeremotechests.api.manager;

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * All operations returns a {@link CompletableFuture}, but this does not means that all operations
 * will be async, some operations may complete instantly after method call, see {@link com.github.projectsandstone.spongeremotechests.manager.SqlBackend}
 * and {@link com.github.projectsandstone.spongeremotechests.manager.ConfigBackend} for more
 * specific info.
 */
public interface ContainerManager {

    /**
     * Register a User RemoteContainer.
     *
     * This method doesn't check for rights or economy founds.
     *
     * @param user            Owner of the container.
     * @param remoteContainer Container information.
     * @return True if registered with success, false if this containers is already registered.
     */
    CompletableFuture<Boolean> registerUserContainer(@NotNull User user, @NotNull RemoteContainer remoteContainer);

    /**
     * Unregister User remote container.
     *
     * @param user      Owner of the container.
     * @param predicate Predicate to test containers to remove.
     * @return True if unregistered with success, false if this containers is not registered.
     */
    CompletableFuture<Boolean> unregisterUserContainer(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate);

    /**
     * Gets first {@code user} {@link RemoteContainer} that matches {@code predicate}.
     *
     * @param user      Owner of the container
     * @param predicate Predicate to filter containers.
     * @return Optional of the first found {@link RemoteContainer}, or empty if no one container was
     * found.
     */
    @NotNull
    CompletableFuture<Optional<RemoteContainer>> getUserContainer(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate);

    /**
     * Gets all {@code user} {@link RemoteContainer RemoteContainers} filtered by {@code
     * predicate}.
     *
     * <br> <b>Note that changes made to the returned set will not be reflected to {@code user}
     * container {@link Set}, and the returned set may be immutable</b>
     *
     * @param user      Owner of the container
     * @param predicate Predicate to filter containers.
     * @return A {@link Set} view of all found containers, or empty {@link Set} if no one container
     * was found.
     */
    @NotNull
    CompletableFuture<Set<RemoteContainer>> getUserContainers(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate);

    /**
     * Check if the {@code user} is the owner of the {@code container}.
     *
     * @param user      User.
     * @param container Container.
     * @return True if the user is the owner of the container, false otherwise.
     */
    CompletableFuture<Boolean> isOwner(@NotNull User user, @NotNull RemoteContainer container);

    /**
     * Gets all containers of the User (including invalid containers).
     *
     * @param user Owner
     * @return All containers.
     */
    @NotNull
    CompletableFuture<Set<RemoteContainer>> getUserContainers(@NotNull User user);

    /**
     * Gets a view of all registered containers.
     *
     * @return A view of all registered containers.
     */
    @NotNull
    CompletableFuture<Map<User, Set<RemoteContainer>>> getAllContainers();

    /**
     * Unregister User remote container
     *
     * @param user            Owner of the container
     * @param remoteContainer Container to remove.
     * @return True if unregistered with success, false if this containers is not registered.
     */
    default CompletableFuture<Boolean> unregisterUserContainer(@NotNull User user, @NotNull RemoteContainer remoteContainer) {
        return this.unregisterUserContainer(user, remoteContainer0 -> remoteContainer0.equals(remoteContainer));
    }

    /**
     * Unregister all containers of {@code user} which has name: {@code name}.
     *
     * @param user User.
     * @param name Name of container.
     * @return True if any container was removed as result of this operation.
     */
    @NotNull
    default CompletableFuture<Optional<RemoteContainer>> getUserContainer(@NotNull User user, @NotNull String name) {
        return this.getUserContainer(user, remoteContainer -> remoteContainer.getName().filter(s -> s.equals(name)).isPresent());
    }

    /**
     * Unregister the container of {@code user} which is localized at {@code location}.
     *
     * @param user     User.
     * @param location Location of container.
     * @return True if any container was removed as result of this operation.
     */
    @NotNull
    default CompletableFuture<Optional<RemoteContainer>> getUserContainer(@NotNull User user, @NotNull Location<World> location) {
        return this.getUserContainer(user, remoteContainer -> remoteContainer.getLocation().equals(location));
    }

}
