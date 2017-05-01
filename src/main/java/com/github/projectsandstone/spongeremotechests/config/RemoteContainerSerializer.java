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

import com.google.common.reflect.TypeToken;

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;
import com.github.projectsandstone.spongeremotechests.api.factory.RemoteContainerFactory;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class RemoteContainerSerializer implements TypeSerializer<RemoteContainer> {

    private static final TypeToken<Location<World>> WORLD_LOCATION_TOKEN = new TypeToken<Location<World>>() {};

    @Override
    public RemoteContainer deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        @Nullable String name = null;
        @NotNull Location<World> location = value.getNode("location").getValue(WORLD_LOCATION_TOKEN);

        ConfigurationNode nameNode = value.getNode("name");

        if(!nameNode.isVirtual())
            name = nameNode.getString();


        return RemoteContainerFactory.createChest(name, location);
    }

    @Override
    public void serialize(TypeToken<?> type, RemoteContainer obj, ConfigurationNode value) throws ObjectMappingException {

        if(obj.getName() != null) {
            value.getNode("name").setValue(obj.getName());
        }

        value.getNode("location").setValue(WORLD_LOCATION_TOKEN, obj.getLocation());
    }
}
