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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An object that maps {@code String} to values.  A trie cannot contain duplicate keys;
 * each key can map to at most one value.
 *
 * <p>The {@code Trie} interface provides four <i>collection views</i>, which
 * allow a trie's contents to be viewed as a set of keys, filtered with prefix
 * set of keys, collection of values, or set of key-value mappings.
 * The <i>order</i> of a trie is defined as the order in which the iterators on
 * the map's collection views return their elements.
 *
 * <p>The "destructive" methods contained in this interface, that is, the
 * methods that modify the trie on which they operate, are specified to throw
 * {@code UnsupportedOperationException} if this trie does not support the
 * operation.  If this is the case, these methods may, but are not required
 * to, throw an {@code UnsupportedOperationException} if the invocation would
 * have no effect on the trie.  For example, invoking the {@link #clear()}
 * method on an unmodifiable trie may, but is not required to, throw the
 * exception if the trie whose mappings are to be "superimposed" is empty.
 *
 * <p>Interface prohibit null keys and values.  Attempting to insert an ineligible
 * key or value throws an unchecked exception, typically {@code NullPointerException}.
 * Attempting to query the presence of an ineligible key or value will throw an
 * exception.
 *
 * <p>Some trie operations which perform recursive traversal of the trie may fail
 * with an exception for self-referential instances where the trie directly or
 * indirectly contains itself. This includes the {@code clone()},
 * {@code equals()}, {@code hashCode()} and {@code toString()} methods.
 * Implementations may optionally handle the self-referential scenario, however
 * most current implementations do not do so.
 *
 * @param <V> the type of mapped values
 *
 * @author Dmytro Ivanov
 * @see Map
 */
@SuppressWarnings("PMD.ShortClassName")
public interface Trie<V> {

    // Query Operations

    /**
     * Returns the number of key-value mappings in this trie.
     *
     * @return the number of key-value mappings in this trie
     */
    int size();

    /**
     * Returns {@code true} if this trie contains no key-value mappings.
     *
     * @return {@code true} if this trie contains no key-value mappings
     */
    boolean isEmpty();

    /**
     * Returns {@code true} if this trie contains a mapping for the specified
     * key.  More formally, returns {@code true} if and only if
     * this trie contains a mapping for a key {@code k} such that
     * {@code (key.equals(k))}.  (There can be at most one such mapping.)
     *
     * @param key key whose presence in this trie is to be tested
     * @return {@code true} if this trie contains a mapping for the specified key
     * @throws IllegalArgumentException if the key has non alphanumeric symbols
     * @throws NullPointerException if the specified key is null
     */
    boolean containsKey(String key);

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this trie contains no mapping for the key.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this trie contains no mapping for the key
     * @throws IllegalArgumentException if the key has non alphanumeric symbols
     * @throws NullPointerException if the specified key is null
     */
    V get(String key);

    // Modification Operations

    /**
     * Associates the specified value with the specified key in this trie.
     * If the trie previously contained a mapping for the key, the old value is
     * replaced by the specified value.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     * @throws IllegalArgumentException if the key has non alphanumeric symbols
     * @throws NullPointerException if the specified key or value is null
     */
    V put(String key, V value);

    /**
     * Removes the mapping for a key from this trie if it is present.
     * More formally, if this trie contains a mapping from key {@code k}
     * to value {@code v} such that {@code (key.equals(k))}, that mapping
     * is removed.  (The trie can contain at most one such mapping.)
     *
     * <p>Returns the value to which this trie previously associated the key,
     * or {@code null} if the trie contained no mapping for the key.
     *
     * <p>The trie will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the trie
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     * @throws IllegalArgumentException if the key has non alphanumeric symbols
     * @throws NullPointerException if the specified key is null
     */
    V remove(String key);

    // Bulk Operations

    /**
     * Removes all of the mappings from this trie.
     * The trie will be empty after this call returns.
     */
    void clear();

    // Comparison and equals

    /**
     * Compares the specified object with this trie for equality.  Returns
     * {@code true} if the given object is also a trie and the two tries
     * represent the same mappings.  More formally, two tries {@code m1} and
     * {@code m2} represent the same mappings if
     * {@code m1.entrySet().equals(m2.entrySet())}.  This ensures that the
     * {@code equals} method works properly across different implementations
     * of the {@code Trie} interface.
     *
     * @param object object to be compared for equality with this trie
     * @return {@code true} if the specified object is equal to this trie
     */
    boolean equals(Object object);

    /**
     * Returns the hash code value for this trie.  The hash code of a trie is
     * defined to be the sum of the hash codes of each entry in the trie's
     * {@code entrySet()} view.  This ensures that {@code m1.equals(m2)}
     * implies that {@code m1.hashCode()==m2.hashCode()} for any two tries
     * {@code m1} and {@code m2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this trie
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    int hashCode();

    // Views

    /**
     * Returns a {@link Set} view of the keys contained in this trie.
     * The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie
     * is modified while an iteration over the set is in progress , the results of
     * the iteration are undefined.  The set does not supports modification
     * operations.
     *
     * @return a set view of the keys contained in this trie
     */
    Set<String> keys();

    /**
     * Returns a {@link Set} view of the keys contained in this trie that start with
     * given prefix. The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie
     * is modified while an iteration over the set is in progress , the results of
     * the iteration are undefined.  The set does not supports modification
     * operations.
     *
     * @param prefix the prefix that will be used to filter keys
     * @return a set view of the keys contained in this trie
     */
    Set<String> keysWithPrefix(String prefix);

    /**
     * Returns a {@link Collection} view of the values contained in this trie.
     * The collection is backed by the trie, so changes to the trie are
     * reflected in the collection, vice-versa is not supported.  If the trie is
     * modified while an iteration over the collection is in progress
     * the results of the iteration are undefined. The collection does not supports
     * modification operations.
     *
     * @return a collection view of the values contained in this trie
     */
    Collection<V> values();

    /**
     * Returns a {@link Set} view of the mappings contained in this trie.
     * The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie is modified
     * while an iteration over the set is in progress  the results of
     * the iteration are undefined. The set does not supports modification
     * operations.
     *
     * @return a set view of the mappings contained in this trie
     */
    Set<Map.Entry<String, V>> entrySet();

}
