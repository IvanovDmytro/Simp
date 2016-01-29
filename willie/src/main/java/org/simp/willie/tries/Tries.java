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

/**
 * This class consists exclusively of static methods that operate on or return
 * tries.  It contains "wrappers", which return a new tries backed by a
 * specified trie, and a few other odds and ends.
 * <p/>
 * <p>The methods of this class all throw a {@code NullPointerException}
 * if the tries or class objects provided to them are {@code null}.
 * <p/>
 * <p>The "destructive" algorithms contained in this class, that is, the
 * algorithms that modify the trie on which they operate, are specified
 * to throw {@code UnsupportedOperationException} if the trie does not
 * support the appropriate mutation primitive(s), such as the {@code set}
 * method.  These algorithms may, but are not required to, throw this
 * exception if an invocation would have no effect on the trie.
 *
 * @author Dmytro Ivanov
 * @see PrefixTree
 * @see Trie
 */
public final class Tries {

    private Tries() {
        // Not meant to be instantiated
    }

    /**
     * Returns an unmodifiable view of the specified {@code PrefixTree}.  This method
     * allows modules to provide users with "read-only" access to internal
     * trees. Query operations on the returned trie "read through"
     * to the specified trie, and attempts to modify the returned
     * tree, whether direct or via its views, result in an
     * {@code UnsupportedOperationException}.<p>
     * <p/>
     * The returned trie will be serializable if the specified map
     * is serializable.
     *
     * @param prefixTree the tree for which an unmodifiable view is to be returned.
     * @return an unmodifiable view of the specified tree.
     */
    public static PrefixTree unmodifiablePrefixTree(PrefixTree prefixTree) {
        return new PrefixTree(unmodifiableTrie(prefixTree.mTrie));
    }

    /**
     * Returns an unmodifiable view of the specified trie.  This method
     * allows modules to provide users with "read-only" access to internal
     * tries. Query operations on the returned trie "read through"
     * to the specified trie, and attempts to modify the returned
     * trie, whether direct or via its views, result in an
     * {@code UnsupportedOperationException}.<p>
     * <p/>
     * The returned trie will be serializable if the specified map
     * is serializable.
     *
     * @param <V>  the class of the trie values
     * @param trie the trie for which an unmodifiable view is to be returned.
     * @return an unmodifiable view of the specified trie.
     */
    public static <V> Trie<V> unmodifiableTrie(Trie<V> trie) {
        return new UnmodifiableTrie<>(trie);
    }

    /**
     * @serial include
     */
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
