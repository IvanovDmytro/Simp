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

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class provides a skeletal implementation of the {@code Trie}
 * interface, to minimize the effort required to implement this interface.
 * <p/>
 * <p>The programmer should generally provide a void (no argument)
 * constructor, as per the recommendation in the {@code Trie} interface
 * specification.
 * <p/>
 * <p>The documentation for each non-abstract method in this class describes its
 * implementation in detail.  Each of these methods may be overridden if the
 * trie being implemented admits a more efficient implementation.
 *
 * @param <V> the type of mapped values
 *
 * @author Dmytro Ivanov
 * @see Trie
 */
public abstract class AbstractTrie<V> implements Trie<V> {

    /**
     * Sole constructor.  (For invocation by subclass constructors, typically
     * implicit.)
     */
    protected AbstractTrie() {
        // This constructor is intentionally empty. Nothing special is needed here.
    }

    /**
     * Compares the specified object with this trie for equality.  Returns
     * {@code true} if the given object is also a trie and the two tries
     * represent the same mappings.  More formally, two tries {@code t1} and
     * {@code t2} represent the same mappings if
     * {@code t1.entrySet().equals(t2.entrySet())}.  This ensures that the
     * {@code equals} method works properly across different implementations
     * of the {@code Trie} interface.
     *
     * @param object the object to be compared for equality with this trie
     *
     * @return {@code true} if the specified object is equal to this trie
     *
     * @implSpec
     * This implementation first checks if the specified object is this trie;
     * if so it returns {@code true}.  Then, it checks if the specified
     * object is a trie whose size is identical to the size of this trie; if
     * not, it returns {@code false}.  If so, it iterates over this trie's
     * {@code entrySet} collection, and checks that the specified trie
     * contains each mapping that this trie contains.  If the specified trie
     * fails to contain such a mapping, {@code false} is returned.  If the
     * iteration completes, {@code true} is returned.
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }

        if (!(object instanceof Trie)) {
            return false;
        }

        final Trie<?> trie = (Trie<?>) object;

        if (trie.size() != size()) {
            return false;
        }

        for (Map.Entry<String, V> entry : entrySet()) {
            if (!entry.getValue().equals(trie.get(entry.getKey()))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the hash code value for this trie. The hash code of a trie is
     * defined to be the sum of the hash codes of each entry in the trie's
     * {@code entrySet()} view.  This ensures that {@code t1.equals(t2)}
     * implies that {@code t1.hashCode()==t2.hashCode()} for any two tries
     * {@code t1} and {@code t2}, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this trie
     *
     * @implSpec
     * This implementation iterates over {@code entrySet()}, calling
     * {@link Map.Entry#hashCode hashCode()} on each element (entry) in the
     * set, and adding up the results.
     *
     * @see Map.Entry#hashCode()
     * @see Object#equals(Object)
     * @see Set#equals(Object)
     */
    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Map.Entry<String, V> entry : entrySet()) {
            hashCode += entry.hashCode();
        }
        return hashCode;
    }

    /**
     * Returns a string representation of this trie.  The string representation
     * consists of a list of key-value mappings in the order returned by the
     * trie's {@code entrySet} view's iterator, enclosed in braces
     * ({@code "{}"}).  Adjacent mappings are separated by the characters
     * {@code ", "} (comma and space).  Each key-value mapping is rendered as
     * the key followed by an equals sign ({@code "="}) followed by the
     * associated value.  Keys and values are converted to strings as by
     * {@link String#valueOf(Object)}.
     *
     * @return a string representation of this trie
     */
    @Override
    public String toString() {
        final Iterator<Map.Entry<String, V>> entriesIterator = entrySet().iterator();
        if (!entriesIterator.hasNext()) {
            return "{}";
        }

        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        while (true) {
            final Map.Entry<String, V> entry = entriesIterator.next();
            stringBuilder.append(entry.getKey());
            stringBuilder.append('=');
            stringBuilder.append(entry.getValue() == this ? "(this Trie)" : entry.getValue());

            if (!entriesIterator.hasNext()) {
                return stringBuilder.append('}').toString();
            }

            stringBuilder.append(',').append(' ');
        }
    }

}
