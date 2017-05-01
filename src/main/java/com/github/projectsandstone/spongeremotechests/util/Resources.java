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
package com.github.projectsandstone.spongeremotechests.util;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

import com.github.projectsandstone.spongeremotechests.SpongeRemoteChestsPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Resources {

    public static String getSqlQuery(Type type) {
        InputStream resourceAsStream = SpongeRemoteChestsPlugin.class.getClassLoader().getResourceAsStream("/sql/" + type.name);

        String query;

        try (InputStreamReader reader = new InputStreamReader(resourceAsStream, Charsets.UTF_8)) {
            query = CharStreams.toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return query;
    }

    public enum Type {
        CONTAINERS_STRUCT("containers.sql"),
        QUERY_USER("query_user.sql"),
        QUERY_ALL("query_all.sql"),
        QUERY_CONTAINER("query_container.sql"),
        INSERT_CONTAINER("insert_container.sql"),
        DELETE_CONTAINER("delete_container.sql");

        final String name;

        Type(String name) {
            this.name = name;
        }


    }
}
