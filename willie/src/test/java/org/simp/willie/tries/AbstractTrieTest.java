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

import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbstractTrieTest {

    private AbstractTrie<String> mTrie = new AbstractTrie<String>() {
        @Override
        public int size() {
            return entrySet().size();
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean containsKey(String key) {
            return false;
        }

        @Override
        public String get(String key) {
            return null;
        }

        @Override
        public String put(String key, String value) {
            return null;
        }

        @Override
        public String remove(String key) {
            return null;
        }

        @Override
        public void clear() {

        }

        @Override
        public Set<String> keys() {
            return null;
        }

        @Override
        public Set<String> keysWithPrefix(String prefix) {
            return null;
        }

        @Override
        public Collection<String> values() {
            return null;
        }

        @Override
        public Set<Map.Entry<String, String>> entrySet() {
            return mEntrySet;
        }
    };

    private Set<Map.Entry<String, String>> mEntrySet;

    @Before
    public void setUp() {
        mEntrySet = new HashSet<>();
    }

    @Test
    public void testEquals_WithNull() {
        assertThat(mTrie.equals(null), is(false));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertThat(mTrie.equals(new Object()), is(false));
    }

    @Test
    public void testEquals_WithSame() {
        assertThat(mTrie.equals(mTrie), is(true));
    }

    @Test
    public void testEquals_WithTrieDifferentSize() {
        Trie<String> trie = new HashTrie<>();
        trie.put("sd", "sdf");

        assertThat(mTrie.equals(trie), is(false));
    }

    @Test
    public void testEquals_WithSameSizeAndDifferentValues() {
        mEntrySet.add(new AbstractMap.SimpleEntry<>("sdsdf", "sdfsdf"));

        Trie<String> trie = new HashTrie<>();
        trie.put("sd", "sdf");

        assertThat(mTrie.equals(trie), is(false));
    }

    @Test
    public void testEquals_WithSameKeysAndDifferentValues() {
        mEntrySet.add(new AbstractMap.SimpleEntry<>("sd", "a"));
        mEntrySet.add(new AbstractMap.SimpleEntry<>("sdf", "b"));

        Trie<String> trie = new HashTrie<>();
        trie.put("sd", "a1");
        trie.put("sdf", "b1");

        assertThat(mTrie.equals(trie), is(false));
    }

    @Test
    public void testEquals_WithSameValuesAndDifferentKeys() {
        mEntrySet.add(new AbstractMap.SimpleEntry<>("a", "a1"));
        mEntrySet.add(new AbstractMap.SimpleEntry<>("b", "b1"));

        Trie<String> trie = new HashTrie<>();
        trie.put("c", "a1");
        trie.put("d", "b1");

        assertThat(mTrie.equals(trie), is(false));
    }

    @Test
    public void testEquals_WithSameValues() {
        mEntrySet.add(new AbstractMap.SimpleEntry<>("a", "a1"));
        mEntrySet.add(new AbstractMap.SimpleEntry<>("b", "b1"));

        Trie<String> trie = new HashTrie<>();
        trie.put("a", "a1");
        trie.put("b", "b1");

        assertThat(mTrie.equals(trie), is(true));
    }

    @Test
    public void testHashCode_Empty() {
        assertThat(mTrie.hashCode(), is(0));
    }

    @Test
    public void testHashCode_WithValues() {
        Map.Entry<String, String> entry1 = new AbstractMap.SimpleEntry<>("a", "a1");
        Map.Entry<String, String> entry2 = new AbstractMap.SimpleEntry<>("b", "b1");

        mEntrySet.add(entry1);
        mEntrySet.add(entry2);

        assertThat(mTrie.hashCode(), is(entry1.hashCode() + entry2.hashCode()));
    }

    @Test
    public void testToString_EmptyTrie() {
        assertThat(mTrie.toString(), is("{}"));
    }

    @Test
    public void testToString_WithValues() {
        mEntrySet.add(new AbstractMap.SimpleEntry<>("a", "a1"));
        mEntrySet.add(new AbstractMap.SimpleEntry<>("b", "b1"));

        assertThat(mTrie.toString(), is("{a=a1, b=b1}"));
    }

}