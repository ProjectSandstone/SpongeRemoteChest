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
package com.github.projectsandstone.spongeremotechests;

import com.google.common.reflect.TypeToken;

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;
import com.github.projectsandstone.spongeremotechests.api.manager.ContainerManager;
import com.github.projectsandstone.spongeremotechests.config.Config;
import com.github.projectsandstone.spongeremotechests.config.RemoteContainerSerializer;
import com.github.projectsandstone.spongeremotechests.listener.RemoteChestsListener;
import com.github.projectsandstone.spongeremotechests.manager.BackedContainerManager;
import com.github.projectsandstone.spongeremotechests.manager.Backend;
import com.github.projectsandstone.spongeremotechests.manager.ConfigBackend;
import com.github.projectsandstone.spongeremotechests.manager.SqlBackend;

import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.inject.Inject;

import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.GuiceObjectMapperFactory;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

@Plugin(id = "com.github.projectsandstone.spongeremotechests",
        name = "SpongeRemoteChests",
        description = "Access your containers remotely.",
        authors = "JonathanxD",
        version = "2.0-alpha",
        url = "https://github.com/ProjectSandstone/SpongeRemoteChests",
        dependencies = @Dependency(id = Platform.API_ID, version = "6.0.0"))
public class SpongeRemoteChestsPlugin {

    private final Logger logger;
    private final Game game;
    private final GuiceObjectMapperFactory factory;
    private final ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private final Path configDir;
    private ContainerManager manager;
    private Config config;
    private CommentedConfigurationNode configNode;
    private CommentedConfigurationNode savesNode;

    @Inject
    public SpongeRemoteChestsPlugin(Logger logger,
                                    Game game,
                                    @ConfigDir(sharedRoot = false) Path dir,
                                    GuiceObjectMapperFactory factory,
                                    ConfigurationLoader<CommentedConfigurationNode> configLoader) {
        this.logger = logger;
        this.game = game;
        this.factory = factory;
        this.configLoader = configLoader;
        this.configDir = dir;
    }

    @Listener
    public void pre(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        this.logger.info("Loading config...");
        this.load();
        this.logger.info("Loaded config!");

        Backend backend;

        if(!config.isUseDb()) {
            backend = new ConfigBackend(HoconConfigurationLoader.builder()
                    .setPath(this.configDir.resolve("saves.conf"))
                    .build());
        } else {
            backend = new SqlBackend(this, logger, service, this.config.getDatabaseConfig());
        }

        this.manager = new BackedContainerManager(backend);
    }

    @Listener
    public void init(GameInitializationEvent event) {
        SpongeRemoteChestsPlugin.registerSerializers();
        this.registerRecipes();
        this.game.getEventManager().registerListeners(this, new RemoteChestsListener(this.game, this.config));
    }

    @Listener
    public void reload(GameReloadEvent event) throws IOException, ObjectMappingException {
        this.logger.info("Reloading SpongeRemoteChests...");
        this.finish();
        this.load();
        this.registerRecipes();
        this.logger.info("SpongeRemoteChests reloaded!");
    }

    @Listener
    public void post(GamePostInitializationEvent event) {
        this.game.getServiceManager().setProvider(this, ContainerManager.class, this.manager);
    }

    @Listener
    public void stopping(GameStoppingEvent event) throws ObjectMappingException {
        this.save();
        this.finish();
    }

    private void registerRecipes() {
        if(this.config.getRecipes().isEnableWandRecipes()) {
            this.game.getRegistry().getRecipeRegistry().register(this.config.getRecipes().getLinkingWandRecipe());
        }
    }

    private static void registerSerializers() {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(RemoteContainer.class), new RemoteContainerSerializer());
    }

    private void load() throws IOException, ObjectMappingException {

        // Config
        this.configNode = this.configLoader.load(ConfigurationOptions.defaults().setObjectMapperFactory(factory));

        this.config = this.configNode.getValue(TypeToken.of(Config.class), new Config());

        // Saves
        this.savesNode = this.savesLoader.load();

        this.manager.set(this.savesNode.getValue(SAVE_TOKEN, this.manager.getAllContainers()));

    }

    private void save() throws ObjectMappingException {
        if(this.config != null) {
            this.configNode.setValue(TypeToken.of(Config.class), this.config);
        }

        this.savesNode.setValue(SAVE_TOKEN, this.manager.getAllContainers());
    }

    private void finish() {
        this.config = null;
        this.manager.set(new HashMap<>());
        this.game.getRegistry().getRecipeRegistry().remove(this.config.getRecipes().getLinkingWandRecipe());
    }

}
