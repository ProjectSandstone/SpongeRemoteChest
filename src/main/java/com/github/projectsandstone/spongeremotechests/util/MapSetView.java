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

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * A view of a Map with unmodifiable {@link Set} values.
 */
public final class MapSetView<K, E> implements Map<K, Set<E>> {

    private final Map<K, Set<E>> original;

    public MapSetView(Map<K, Set<E>> original) {
        this.original = original;
    }

    @Override
    public int size() {
        return this.original.size();
    }

    @Override
    public boolean isEmpty() {
        return this.original.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return this.original.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return this.original.containsValue(value);
    }

    @Override
    public Set<E> get(Object key) {
        Set<E> es = this.original.get(key);

        if(es == null)
            return null;

        return Collections.unmodifiableSet(es);
    }

    @Override
    public Set<E> put(K key, Set<E> value) {
        throw new UnsupportedOperationException("Unmodifiable map");
    }

    @Override
    public Set<E> remove(Object key) {
        throw new UnsupportedOperationException("Unmodifiable map");
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends Set<E>> m) {
        throw new UnsupportedOperationException("Unmodifiable map");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Unmodifiable map");
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return Collections.unmodifiableSet(this.original.keySet());
    }

    @NotNull
    @Override
    public Collection<Set<E>> values() {
        return new UnmodValuesColl<>(this.original.values());
    }

    @NotNull
    @Override
    public Set<Entry<K, Set<E>>> entrySet() {
        return new UnmodEntrySet<>(this.original.entrySet());
    }

    static class UnmodEntrySet<K, E> implements Set<Entry<K, Set<E>>> {

        private final Set<Entry<K, Set<E>>> original;

        UnmodEntrySet(Set<Entry<K, Set<E>>> original) {
            this.original = original;
        }

        @Override
        public int size() {
            return this.original.size();
        }

        @Override
        public boolean isEmpty() {
            return this.original.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return this.original.contains(o);
        }

        @NotNull
        @Override
        public Iterator<Entry<K, Set<E>>> iterator() {
            return new UnmodEntrySetIterator<>(this.original.iterator());
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return this.original.stream()
                    .map(kSetEntry ->
                            new AbstractMap.SimpleImmutableEntry<>(kSetEntry.getKey(), Collections.unmodifiableSet(kSetEntry.getValue())))
                    .toArray(Object[]::new);
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return this.original.stream()
                    .map(kSetEntry ->
                            new AbstractMap.SimpleImmutableEntry<>(kSetEntry.getKey(), Collections.unmodifiableSet(kSetEntry.getValue())))
                    .toArray(value -> a);
        }

        @Override
        public boolean add(Entry<K, Set<E>> kSetEntry) {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return this.original.containsAll(c);
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Entry<K, Set<E>>> c) {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Unmodifiable set.");
        }
    }

    static class UnmodEntrySetIterator<K, E> implements Iterator<Entry<K, Set<E>>> {

        private final Iterator<Entry<K, Set<E>>> original;

        UnmodEntrySetIterator(Iterator<Entry<K, Set<E>>> original) {
            this.original = original;
        }

        @Override
        public boolean hasNext() {
            return this.original.hasNext();
        }

        @Override
        public Entry<K, Set<E>> next() {
            Entry<K, Set<E>> next = this.original.next();
            return new AbstractMap.SimpleImmutableEntry<>(next.getKey(), Collections.unmodifiableSet(next.getValue()));
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unmodifiable iterator.");
        }
    }

    static class UnmodValuesColl<E> implements Collection<Set<E>> {

        private final Collection<Set<E>> original;

        UnmodValuesColl(Collection<Set<E>> original) {
            this.original = original;
        }

        @Override
        public int size() {
            return this.original.size();
        }

        @Override
        public boolean isEmpty() {
            return this.original.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return this.original.contains(o);
        }

        @NotNull
        @Override
        public Iterator<Set<E>> iterator() {
            return new UnmodIterator<>(this.original.iterator());
        }

        @NotNull
        @Override
        public Object[] toArray() {
            return this.original.stream().map(Collections::unmodifiableSet).toArray(Object[]::new);
        }

        @NotNull
        @Override
        public <T> T[] toArray(@NotNull T[] a) {
            return this.original.stream().map(Collections::unmodifiableSet).toArray(value -> a);
        }

        @Override
        public boolean add(Set<E> es) {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }

        @Override
        public boolean remove(Object o) {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }

        @Override
        public boolean containsAll(@NotNull Collection<?> c) {
            return this.original.containsAll(c);
        }

        @Override
        public boolean addAll(@NotNull Collection<? extends Set<E>> c) {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }

        @Override
        public boolean removeAll(@NotNull Collection<?> c) {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }

        @Override
        public boolean retainAll(@NotNull Collection<?> c) {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }
    }

    static class UnmodIterator<E> implements Iterator<Set<E>> {

        private final Iterator<Set<E>> original;

        UnmodIterator(Iterator<Set<E>> original) {
            this.original = original;
        }

        @Override
        public boolean hasNext() {
            return this.original.hasNext();
        }

        @Override
        public Set<E> next() {
            return Collections.unmodifiableSet(this.original.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unmodifiable collection.");
        }
    }
}
