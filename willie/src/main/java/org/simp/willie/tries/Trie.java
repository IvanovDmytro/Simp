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

    int size();

    boolean isEmpty();

    boolean containsKey(String key);

    V get(String key);

    // Modification Operations

    V put(String key, V value);

    V remove(String key);

    // Bulk Operations

    void clear();

    // Views

    Set<String> keys();

    Set<String> keysWithPrefix(String prefix);

    Collection<V> values();

    Set<Map.Entry<String, V>> entrySet();

}
