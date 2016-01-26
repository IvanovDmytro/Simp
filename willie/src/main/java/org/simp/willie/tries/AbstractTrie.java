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

public abstract class AbstractTrie<V> implements Trie<V> {

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

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Map.Entry<String, V> entry : entrySet()) {
            hashCode += entry.hashCode();
        }
        return hashCode;
    }

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
            stringBuilder.append(entry.getValue() == this ? "(this Map)" : entry.getValue());

            if (!entriesIterator.hasNext()) {
                return stringBuilder.append('}').toString();
            }

            stringBuilder.append(',').append(' ');
        }
    }

}
