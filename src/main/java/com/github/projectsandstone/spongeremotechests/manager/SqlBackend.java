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

import com.flowpowered.math.vector.Vector3i;
import com.github.jonathanxd.iutils.object.Lazy;
import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;
import com.github.projectsandstone.spongeremotechests.api.factory.RemoteContainerFactory;
import com.github.projectsandstone.spongeremotechests.config.DatabaseConfig;
import com.github.projectsandstone.spongeremotechests.util.Resources;
import com.github.projectsandstone.spongeremotechests.util.Resources.Type;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.sql.SqlService;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import javax.sql.DataSource;

/**
 * Backend to a SQL {@link javax.sql.DataSource}. All calls are backed to SQL server asynchronously.
 */
public class SqlBackend extends Backend {

    private final Logger logger;
    private final Lazy<UserStorageService> service;
    private final DatabaseConfig databaseConfig;
    private final DataSource dataSource;

    public SqlBackend(Object plugin, Logger logger, DatabaseConfig databaseConfig) {
        this.logger = logger;
        this.databaseConfig = databaseConfig;
        SqlService sqlService = Sponge.getServiceManager().provide(SqlService.class).orElseThrow(NullPointerException::new);

        try {
            this.dataSource = sqlService.getDataSource(plugin, this.databaseConfig.getUrl());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (Connection con = this.dataSource.getConnection(); Statement stm = con.createStatement()) {
            stm.execute(Resources.getSqlQuery(Type.CONTAINERS_STRUCT));
        } catch (SQLException e) {
            e.printStackTrace();
        }


        this.service = Lazy.lazy(() -> {
            Optional<UserStorageService> provide = Sponge.getServiceManager().provide(UserStorageService.class);

            if (!provide.isPresent())
                throw new IllegalStateException("UserStorageService required too early!");

            return provide.get();
        });
    }

    @Override
    public @NotNull CompletableFuture<Boolean> hasAnyContainer(@NotNull User user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection con = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(Resources.getSqlQuery(Type.QUERY_USER))) {

                preparedStatement.setString(1, user.getUniqueId().toString());
                ResultSet set = preparedStatement.executeQuery();

                return set.next();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        });
    }

    @Override
    public @NotNull CompletableFuture<Set<RemoteContainer>> getAllContainers(@NotNull User user) {
        return this.query(user).thenApply(userSetMap -> userSetMap.getOrDefault(user, Collections.emptySet()));
    }

    @Override
    public @NotNull CompletableFuture<Boolean> isOwner(@NotNull User user, @NotNull RemoteContainer container) {
        return this.query(user).thenApply(userSetMap -> userSetMap.containsKey(user) && userSetMap.get(user).contains(container));
    }

    @Override
    public @NotNull CompletableFuture<Map<User, Set<RemoteContainer>>> getAllContainers() {
        return query(null);
    }

    @Override
    public @NotNull CompletableFuture<Boolean> register(@NotNull User user, @NotNull RemoteContainer remoteContainer) {
        return CompletableFuture.supplyAsync(() -> {

            Location<World> worldLocation = remoteContainer.getLocation();

            try (Connection con = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(Resources.getSqlQuery(Type.QUERY_CONTAINER))) {

                preparedStatement.setString(1, user.getUniqueId().toString());
                preparedStatement.setString(2, worldLocation.getExtent().getUniqueId().toString());
                preparedStatement.setInt(3, worldLocation.getBlockX()); // x
                preparedStatement.setInt(4, worldLocation.getBlockY()); // y
                preparedStatement.setInt(5, worldLocation.getBlockZ()); // z

                if (preparedStatement.executeQuery().next())
                    return Boolean.FALSE;

                try(PreparedStatement create = con.prepareStatement(Resources.getSqlQuery(Type.INSERT_CONTAINER))) {

                    create.setString(1, user.getUniqueId().toString());
                    preparedStatement.setString(2, worldLocation.getExtent().getUniqueId().toString());

                    Optional<String> name = remoteContainer.getName();

                    if (name.isPresent())
                        preparedStatement.setString(3, name.get());
                    else
                        preparedStatement.setNull(3, Types.VARCHAR);

                    preparedStatement.setInt(4, worldLocation.getBlockX()); // x
                    preparedStatement.setInt(5, worldLocation.getBlockY()); // y
                    preparedStatement.setInt(6, worldLocation.getBlockZ()); // z

                    if (preparedStatement.executeUpdate() > 0)
                        return Boolean.TRUE;
                    else
                        return Boolean.FALSE;
                }

            } catch (SQLException e) {
                this.logger.error("Query failed", e);
            }

            return Boolean.FALSE;
        });

    }

    @Override
    public @NotNull CompletableFuture<Boolean> unregister(@NotNull User user, @NotNull Predicate<RemoteContainer> predicate) {
        return this.getAllContainers(user).thenCompose(remoteContainers -> CompletableFuture.supplyAsync(() -> {
            try (Connection con = this.dataSource.getConnection()) {
                con.setAutoCommit(false);
                boolean any = false;

                try(PreparedStatement preparedStatement = con.prepareStatement(Resources.getSqlQuery(Type.DELETE_CONTAINER))) {
                    for (RemoteContainer remoteContainer : remoteContainers) {

                        if (predicate.test(remoteContainer)) {

                            Location<World> worldLocation = remoteContainer.getLocation();

                            preparedStatement.setString(1, user.getUniqueId().toString());
                            preparedStatement.setString(2, worldLocation.getExtent().getUniqueId().toString());
                            preparedStatement.setInt(3, worldLocation.getBlockX()); // x
                            preparedStatement.setInt(4, worldLocation.getBlockY()); // y
                            preparedStatement.setInt(5, worldLocation.getBlockZ()); // z

                            any |= preparedStatement.executeUpdate() > 0;

                        }
                    }

                }

                con.commit();

                return any;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return Boolean.FALSE;
        }));
    }

    private @NotNull CompletableFuture<Map<User, Set<RemoteContainer>>> query(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection con = this.dataSource.getConnection();
                 PreparedStatement preparedStatement = con.prepareStatement(Resources.getSqlQuery(user != null ? Type.QUERY_USER : Type.QUERY_ALL))) {

                if (user != null) {
                    preparedStatement.setString(1, user.getUniqueId().toString());
                }

                ResultSet set = preparedStatement.executeQuery();

                Map<User, Set<RemoteContainer>> map = new HashMap<>();


                while (set.next()) {
                    int id = set.getInt("id");
                    UUID ownerUuid = UUID.fromString(set.getString("owner"));
                    UUID worldUuid = UUID.fromString(set.getString("world"));
                    String name = set.getString("name");
                    int x = set.getInt("x");
                    int y = set.getInt("y");
                    int z = set.getInt("z");

                    Optional<World> world = Sponge.getServer().getWorld(worldUuid);

                    if (!world.isPresent()) {
                        this.logger.info("Cannot find world with uuid '" + worldUuid.toString() + "', skipping entry id '" + id + "'.");
                        continue;
                    }

                    Optional<User> owner = this.service.get().get(ownerUuid);

                    if (!owner.isPresent()) {
                        this.logger.info("Cannot find user with uuid '" + ownerUuid.toString() + "', skipping entry id '" + id + "'.");
                        continue;
                    }

                    if (!map.containsKey(owner.get()))
                        map.put(owner.get(), new HashSet<>());

                    Location<World> location = world.get().getLocation(new Vector3i(x, y, z));

                    map.get(owner.get())
                            .add(RemoteContainerFactory.createChest(name, location));
                }

                return map;
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return Collections.emptyMap();
        });
    }


}
