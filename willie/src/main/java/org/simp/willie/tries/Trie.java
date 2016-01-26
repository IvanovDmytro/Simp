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
