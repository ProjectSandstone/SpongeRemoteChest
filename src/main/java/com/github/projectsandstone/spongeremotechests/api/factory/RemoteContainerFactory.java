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
package com.github.projectsandstone.spongeremotechests.api.factory;

import com.github.projectsandstone.spongeremotechests.api.RemoteContainer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public enum RemoteContainerFactory {
    ;

    public static RemoteContainer createChest(@Nullable String name, @NotNull Location<World> location) {
        return new RemoteContainerImpl(name, location);
    }

    public static RemoteContainer createChest(@NotNull Location<World> location) {
        return RemoteContainerFactory.createChest(null, location);
    }

    private static final class RemoteContainerImpl implements RemoteContainer {

        @Nullable
        private final String name;
        @NotNull
        private final Location<World> location;

        private RemoteContainerImpl(@Nullable String name, @NotNull Location<World> location) {
            this.name = name;
            this.location = location;
        }

        @Override
        public Optional<String> getName() {
            return Optional.ofNullable(this.name);
        }

        @Override
        public @NotNull Location<World> getLocation() {
            return this.location;
        }

        @Override
        public int hashCode() {
            return this.getLocation().hashCode();
        }

        @Override
        public boolean equals(Object obj) {

            if (obj instanceof RemoteContainer) {
                RemoteContainer other = (RemoteContainer) obj;

                return this.getLocation().equals(other.getLocation());
            }

            return super.equals(obj);
        }
    }
}
