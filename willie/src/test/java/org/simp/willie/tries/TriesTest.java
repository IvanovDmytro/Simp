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

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TriesTest {

    @Test(expected = NullPointerException.class)
    public void testUnmodifiablePrefixTree_NPE() {
        Tries.unmodifiablePrefixTree(null);
    }

    @Test
    public void testUnmodifiablePrefixTree() {
        PrefixTree prefixTree = new PrefixTree();
        PrefixTree unmodifiableTree = Tries.unmodifiablePrefixTree(prefixTree);

        assertThat(unmodifiableTree != prefixTree, is(true));
        assertThat(unmodifiableTree.mTrie, is(instanceOf(Tries.UnmodifiableTrie.class)));
    }

    @Test(expected = NullPointerException.class)
    public void testUnmodifiableTrie_NPE() {
        Tries.unmodifiableTrie(null);
    }

    @Test
    public void testUnmodifiableTrie() {
        Trie<Object> trie = new HashTrie<>();
        Trie<Object> unmodifiableTrie = Tries.unmodifiableTrie(trie);

        assertThat(unmodifiableTrie != trie, is(true));
        assertThat(unmodifiableTrie, is(instanceOf(Tries.UnmodifiableTrie.class)));
        assertThat(((Tries.UnmodifiableTrie<Object>) unmodifiableTrie).mTrie, is(trie));
    }

}