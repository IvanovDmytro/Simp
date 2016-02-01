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

/**
 * This class provides a specific view over {@code Trie} that has {@code Boolean}
 * as an value, because it takes less space that any other reference data type.
 *
 * The whole purpose of this class is to provide easy to use suggestions
 * data structure, so user of the class would not have to deal with {@code Trie}
 * API and can use adopted to this problem more comfortable API.
 *
 * @author Dmytro Ivanov
 * @see HashTrie
 * @see Trie
 */
public class PrefixTree implements Serializable, Cloneable {

    private static final long serialVersionUID = -4056615874998275521L;

    /**
     * Back up {@link Trie}, is used to store keys along with values.
     * {@link Boolean} is selected because it takes small amount of space and there are
     * just 2 instances of the class.
     */
    Trie<Boolean> mTrie;

    /**
     * Constructs new {@code PrefixTree} that is backed up by {@code HashTrie}.
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

    /**
     * Returns the number of values in this tree.
     *
     * Operation time complexity is O(1).
     *
     * @return the number of values in this tree
     */
    public int size() {
        return mTrie.size();
    }

    /**
     * Returns {@code true} if this tree contains no values.
     *
     * Operation time complexity is O(1).
     *
     * @return {@code true} if this tree contains no values.
     */
    public boolean isEmpty() {
        return mTrie.isEmpty();
    }

    /**
     * Returns {@code true} if this tree contains a specified value.
     *
     * Operation time complexity is O(S), where S is length of value.
     *
     * @param value value whose presence in this tree is to be tested
     * @return {@code true} if this tree contains value
     * @throws IllegalArgumentException if the value has non alphanumeric symbols
     * @throws NullPointerException if the specified value is null
     */
    public boolean contains(String value) {
        return mTrie.containsKey(value);
    }

    // Modification Operations

    /**
     * Add specified value to the tree. If value already is in there nothing happens.
     *
     * Operation time complexity is O(S), where S is length of value.
     *
     * @param value value that will be added to the tree
     * @throws IllegalArgumentException if the key has non alphanumeric symbols
     * @throws NullPointerException if the specified value is null
     */
    public void add(String value) {
        mTrie.put(value, Boolean.TRUE);
    }

    /**
     * Removes the specified value if present.
     *
     * Operation time complexity is O(S), where S is length of value.
     *
     * @param  value that is to be removed from the tree.
     */
    public void remove(String value) {
        mTrie.remove(value);
    }

    // Bulk Operations

    /**
     * Removes all of the values from this tree.
     * The tree will be empty after this call returns.
     *
     * Operation time complexity is O(1).
     */
    public void clear() {
        mTrie.clear();
    }

    // Views

    /**
     * Returns a {@link Set} view of the values contained in this tree.
     * The collection is backed by the tree, so changes to the tre are
     * reflected in the collection, vice-versa is not supported.  If the tree is
     * modified while an iteration over the collection is in progress
     * the results of the iteration are undefined. The collection does not supports
     * modification operations.
     *
     * @return a collection view of the values contained in this tree
     */
    public Set<String> values() {
        return mTrie.keys();
    }

    /**
     * Returns a {@link Set} view of the values contained in this tree that start with given prefix.
     * The collection is backed by the tree, so changes to the tre are
     * reflected in the collection, vice-versa is not supported.  If the tree is
     * modified while an iteration over the collection is in progress
     * the results of the iteration are undefined. The collection does not supports
     * modification operations.
     *
     * Operation time complexity is O(S), where S is length of prefix.
     *
     * @param prefix that is used to filter values
     * @return a collection view of the values contained in this tree that start with given prefix
     */
    public Set<String> valuesWithPrefix(String prefix) {
        return mTrie.keysWithPrefix(prefix);
    }

    /**
     * Returns a shallow copy of this {@code PrefixTree} instance: the values are not cloned.
     *
     * Operation time complexity is O(N), where N is number of values in a tree.
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

    /**
     * Compares the specified object with this tree for equality.  Returns
     * {@code true} if the given object is also a tree and the two trees
     * have same values.  More formally, two trees {@code t1} and
     * {@code t2} are same if {@code t1.mTrie().equals(t2.mTrie)}.
     *
     * Operation time complexity is O(N), where N is number of values in a tree.
     *
     * @param object the object to be compared for equality with this tree
     *
     * @return {@code true} if the specified object is equal to this tree
     *
     * @implSpec
     * This implementation first checks if the specified object is this tree;
     * if so it returns {@code true}.  Then, it checks if the specified
     * tree backed up with trie that is equals to trie that
     * backs up this tree.
     */
    @Override
    public boolean equals(Object object) {
        return object == this || object instanceof PrefixTree && mTrie.equals(((PrefixTree) object).mTrie);
    }

    /**
     * Returns the hash code value for this tree. The hash code of a tree is
     * defined to be hashcode of backing trie.
     *
     * Operation time complexity is O(N), where N is number of values in a tree.
     *
     * @return the hash code value for this tree.
     *
     * @implSpec
     * This implementation returns {@link AbstractTrie#hashCode()} of backing up trie.
     *
     * @see AbstractTrie#hashCode()
     */
    @Override
    public int hashCode() {
        return mTrie.hashCode();
    }

    /**
     * Returns a string representation of this tree.  The string representation
     * consists of a list of value in the order returned by the
     * tree's {@code values} view's iterator, enclosed in braces
     * ({@code "{}"}).  Adjacent values are separated by the characters
     * {@code ", "} (comma and space).
     *
     * Operation time complexity is O(N), where N is number of values in a tree.
     *
     * @return a string representation of this tree
     */
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
