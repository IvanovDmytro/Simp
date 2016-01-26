/*
 * Copyright (C) 2016 Dmytro Ivanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.simp.willie.tries;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class Tries {

    private Tries() {
        // Not meant to be instantiated
    }

    public static PrefixTree unmodifiablePrefixTree(PrefixTree prefixTree) {
        return new PrefixTree(unmodifiableTrie(prefixTree.mTrie));
    }

    public static <V> Trie<V> unmodifiableTrie(Trie<V> trie) {
        return new UnmodifiableTrie<>(trie);
    }

    static class UnmodifiableTrie<V> implements Trie<V>, Serializable {

        private static final long serialVersionUID = -5954618687810466135L;

        final Trie<V> mTrie;

        UnmodifiableTrie(Trie<V> trie) {
            if (trie == null) {
                throw new NullPointerException();
            }

            mTrie = trie;
        }

        @Override
        public int size() {
            return mTrie.size();
        }

        @Override
        public boolean isEmpty() {
            return mTrie.isEmpty();
        }

        @Override
        public boolean containsKey(String key) {
            return mTrie.containsKey(key);
        }

        @Override
        public V get(String key) {
            return mTrie.get(key);
        }

        @Override
        public V put(String key, V value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public V remove(String key) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Set<String> keys() {
            return mTrie.keys();
        }

        @Override
        public Set<String> keysWithPrefix(String prefix) {
            return mTrie.keysWithPrefix(prefix);
        }

        @Override
        public Collection<V> values() {
            return mTrie.values();
        }

        @Override
        public Set<Map.Entry<String, V>> entrySet() {
            return mTrie.entrySet();
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override
        public boolean equals(Object object) {
            return object == this || mTrie.equals(object);
        }

        @Override
        public int hashCode() {
            return mTrie.hashCode();
        }

        @Override
        public String toString() {
            return mTrie.toString();
        }

    }

}
