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
import java.util.Iterator;
import java.util.Set;

public class PrefixTree implements Serializable, Cloneable {

    private static final long serialVersionUID = -4056615874998275521L;

    /**
     * Back up {@link Trie}, is used to store keys along with values.
     * {@link Boolean} is selected because it takes small amount of space and there are
     * just 2 instances of the class.
     */
    Trie<Boolean> mTrie;

    /**
     * Constructs new {@link PrefixTree} that is backed up by {@link HashTrie}.
     */
    public PrefixTree() {
        this(new HashTrie<Boolean>());
    }

    /**
     * Package specific constructor for {@link Tries#unmodifiablePrefixTree(PrefixTree)}
     * and for white box testing.
     *
     * @param trie instance of the {@link Trie} to back up {@link PrefixTree}
     */
    PrefixTree(Trie<Boolean> trie) {
        this.mTrie = trie;
    }

    // Query Operations

    public int size() {
        return mTrie.size();
    }

    public boolean isEmpty() {
        return mTrie.isEmpty();
    }

    public boolean contains(String value) {
        return mTrie.containsKey(value);
    }

    // Modification Operations

    public void add(String value) {
        mTrie.put(value, Boolean.TRUE);
    }

    public void remove(String value) {
        mTrie.remove(value);
    }

    // Bulk Operations

    public void clear() {
        mTrie.clear();
    }

    // Views

    public Set<String> values() {
        return mTrie.keys();
    }

    public Set<String> valuesWithPrefix(String prefix) {
        return mTrie.keysWithPrefix(prefix);
    }

    /**
     * Returns a shallow copy of this {@code PrefixTree} instance: the values are not cloned.
     *
     * @return a shallow copy of this tree
     */
    @Override
    public PrefixTree clone() {
        PrefixTree tree;

        try {
            tree = (PrefixTree) super.clone();
        } catch (CloneNotSupportedException excep) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(excep);
        }

        tree.mTrie = new HashTrie<>();

        for (String value : values()) {
            tree.add(value);
        }

        return tree;
    }

    @Override
    public boolean equals(Object object) {
        return object == this || object instanceof PrefixTree && mTrie.equals(((PrefixTree) object).mTrie);
    }

    @Override
    public int hashCode() {
        return mTrie.hashCode();
    }

    @Override
    public String toString() {
        final Iterator<String> valuesIterator = values().iterator();

        if (!valuesIterator.hasNext()) {
            return "{}";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');

        while (true) {
            stringBuilder.append(valuesIterator.next());
            if (!valuesIterator.hasNext()) {
                return stringBuilder.append('}').toString();
            }

            stringBuilder.append(',').append(' ');
        }
    }

}
