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
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class PrefixTreeTest {

    private static final String sVal = "someVal";

    private Trie<Boolean> mTrie;

    private PrefixTree mPrefixTree;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        mTrie = (HashTrie<Boolean>) Mockito.mock(HashTrie.class);
        mPrefixTree = new PrefixTree(mTrie);
    }

    @Test
    public void testConstructor() {
        assertThat(new PrefixTree().mTrie, is(notNullValue()));
    }

    @Test
    public void testSize() {
        mPrefixTree.size();
        verify(mTrie, times(1)).size();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testIsEmpty() {
        mPrefixTree.isEmpty();
        verify(mTrie, times(1)).isEmpty();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testContains() {
        mPrefixTree.contains(sVal);
        verify(mTrie, times(1)).containsKey(eq(sVal));

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testAdd() {
        mPrefixTree.add(sVal);
        verify(mTrie, times(1)).put(eq(sVal), eq(true));

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testRemove() {
        mPrefixTree.remove(sVal);
        verify(mTrie, times(1)).remove(eq(sVal));

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testClear() {
        mPrefixTree.clear();
        verify(mTrie, times(1)).clear();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testValues() {
        mPrefixTree.values();
        verify(mTrie, times(1)).keys();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testKeysWithPrefix() {
        mPrefixTree.valuesWithPrefix(sVal);
        verify(mTrie, times(1)).keysWithPrefix(sVal);

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testEquals_Null() {
        assertThat(mPrefixTree.equals(null), is(false));
    }

    @Test
    public void testEquals_Same() {
        assertThat(mPrefixTree.equals(mPrefixTree), is(true));
    }

    @Test
    public void testEquals_AnotherObject() {
        assertThat(mPrefixTree.equals(new Object()), is(false));
    }

    @Test
    public void testEquals_EqualsPrefixTree() {
        PrefixTree firstTree = new PrefixTree();
        PrefixTree secondTree = new PrefixTree();

        assertThat(firstTree.equals(secondTree), is(true));
        assertThat(secondTree.equals(firstTree), is(true));

        firstTree.add("add");
        secondTree.add("add");

        assertThat(firstTree.equals(secondTree), is(true));
        assertThat(secondTree.equals(firstTree), is(true));
    }

    @Test
    public void testEquals_NonEqualsPrefixTree() {
        PrefixTree firstTree = new PrefixTree();
        PrefixTree secondTree = new PrefixTree();

        firstTree.add("add123");

        assertThat(firstTree.equals(secondTree), is(false));
        assertThat(secondTree.equals(firstTree), is(false));

        secondTree.add("add");

        assertThat(firstTree.equals(secondTree), is(false));
        assertThat(secondTree.equals(firstTree), is(false));
    }

    @Test
    public void testHashCode() {
        PrefixTree tree = new PrefixTree();

        assertThat(tree.hashCode(), is(tree.mTrie.hashCode()));

        tree.add("val");

        assertThat(tree.hashCode(), is(tree.mTrie.hashCode()));
    }

    @Test
    public void testToString_EmptyTree() {
        PrefixTree tree = new PrefixTree();

        assertThat(tree.toString(), is("{}"));
    }

    @Test
    public void testToString_NonEmptyTree() {
        PrefixTree tree = new PrefixTree();

        tree.add("a");
        tree.add("c");
        tree.add("b");

        assertThat(tree.toString(), is("{a, b, c}"));
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        PrefixTree orgTree = new PrefixTree();
        orgTree.add(sVal);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(bos);

        mPrefixTree.values();

        outStream.writeObject(orgTree);
        outStream.close();

        ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        PrefixTree tree = (PrefixTree) inStream.readObject();
        inStream.close();

        assertThat(tree.contains(sVal), is(true));
    }

    @Test
    public void testClone() {
        PrefixTree orgTree = new PrefixTree();
        orgTree.add(sVal);

        PrefixTree clonedTree = orgTree.clone();

        assertThat(clonedTree.contains(sVal), is(true));

        clonedTree.add(sVal + "s");

        assertThat(orgTree.size(), is(1));
        assertThat(clonedTree.size(), is(2));
    }

}