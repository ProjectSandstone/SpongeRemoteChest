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

/**
 *
 */
@ConfigSerializable
public final class Config {

    @Setting(value = Paths.OP_BYPASS_PROTECTION_PATH, comment = "OP bypass protection system. (Depending of the Protection System, this setting may or may not have effects).")
    private boolean opBypassProtection = false;

    @Setting(value = Paths.OP_BYPASS_LIMIT_PATH, comment = "OPs bypass container limit.")
    private boolean opBypassLimit = false;

    @Setting(value = Paths.OP_BYPASS_ECONOMY_PATH, comment = "OPs doesn't need to pay link creation.")
    private boolean opBypassEconomy = false;

    @Setting(value = Paths.USE_DB_PATH, comment = "Set to true to use database to save data (through jdbc).")
    private boolean useDb = false;

    @Setting(value = Paths.DB_PATH, comment = "Database configuration.")
    private DatabaseConfig databaseConfig = new DatabaseConfig();

    @Setting(value = Paths.WAND_OPTIONS_PATH)
    private Linking linkingConfig = new Linking();

    @Setting(Paths.MESSAGES_PATH)
    private Messages messages = new Messages();

    @Setting(Paths.RECIPES_PATH)
    private Recipes recipes = new Recipes();

    public boolean isOpBypassProtection() {
        return this.opBypassProtection;
    }

    public boolean isOpBypassLimit() {
        return this.opBypassLimit;
    }

    public boolean isOpBypassEconomy() {
        return this.opBypassEconomy;
    }

    public boolean isUseDb() {
        return this.useDb;
    }

    public DatabaseConfig getDatabaseConfig() {
        return this.databaseConfig;
    }

    public Linking getLinkingConfig() {
        return this.linkingConfig;
    }

    public Messages getMessagesConfig() {
        return this.messages;
    }

    public Recipes getRecipes() {
        return this.recipes;
    }

    private static final class Paths {
        static final String OP_BYPASS_PROTECTION_PATH = "opBypassProtection";
        static final String OP_BYPASS_LIMIT_PATH = "opBypassLimit";
        static final String OP_BYPASS_ECONOMY_PATH = "opBypassEconomy";
        static final String USE_DB_PATH = "useDB";
        static final String DB_PATH = "db";
        static final String WAND_OPTIONS_PATH = "linking";
        static final String MESSAGES_PATH = "messages";
        static final String RECIPES_PATH = "recipes";
    }

}
