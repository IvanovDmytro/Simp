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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class UnmodifiableTrieTest {

    private static final String sVal = "someVal";

    private Trie<String> mTrie;
    private Tries.UnmodifiableTrie<String> mUnmodifTrie;

    @Before
    public void setUp() {
        mTrie = (Trie<String>) mock(HashTrie.class);
        mUnmodifTrie = new Tries.UnmodifiableTrie<>(mTrie);
    }

    @Test
    public void testConstructor() {
        assertThat(mUnmodifTrie.mTrie, is(mTrie));
    }

    @Test
    public void testSize() {
        mUnmodifTrie.size();
        verify(mTrie, times(1)).size();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testIsEmpty() {
        mUnmodifTrie.isEmpty();
        verify(mTrie, times(1)).isEmpty();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testContainsKey() {
        mUnmodifTrie.containsKey(sVal);
        verify(mTrie, times(1)).containsKey(eq(sVal));

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testGet() {
        mUnmodifTrie.get(sVal);
        verify(mTrie, times(1)).get(eq(sVal));

        verifyNoMoreInteractions(mTrie);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testPut() {
        mUnmodifTrie.put("d", "d");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        mUnmodifTrie.remove("a");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testClear() {
        mUnmodifTrie.clear();
    }

    @Test
    public void testKeys() {
        mUnmodifTrie.keys();
        verify(mTrie, times(1)).keys();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testKeysWithPrefix() {
        mUnmodifTrie.keysWithPrefix(sVal);
        verify(mTrie, times(1)).keysWithPrefix(eq(sVal));

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testValues() {
        mUnmodifTrie.values();
        verify(mTrie, times(1)).values();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testEntrySet() {
        mUnmodifTrie.entrySet();
        verify(mTrie, times(1)).entrySet();

        verifyNoMoreInteractions(mTrie);
    }

    @Test
    public void testHashCode() {
        Trie<String> trie = new HashTrie<>();
        Trie<String> unmodTrie = Tries.unmodifiableTrie(trie);

        assertThat(trie.hashCode(), is(unmodTrie.hashCode()));

        trie.put("dfd", "sdf");

        assertThat(trie.hashCode(), is(unmodTrie.hashCode()));
    }

    @Test
    public void testToString() {
        Trie<String> trie = new HashTrie<>();
        Trie<String> unmodTrie = Tries.unmodifiableTrie(trie);

        assertThat(trie.toString(), is(unmodTrie.toString()));

        trie.put("dfd", "sdf");

        assertThat(trie.toString(), is(unmodTrie.toString()));
    }

    @Test
    public void testEquals_Same() {
        assertThat(mUnmodifTrie.equals(mUnmodifTrie), is(true));
    }

    @Test
    public void testEquals_Null() {
        assertThat(mUnmodifTrie.equals(null), is(false));
    }

    @Test
    public void testEquals_WithValues() {
        Trie<String> trie = new HashTrie<>();
        Trie<String> unmodTrie = Tries.unmodifiableTrie(trie);

        assertThat(trie.equals(unmodTrie), is(true));
        assertThat(unmodTrie.equals(trie), is(true));

        trie.put("dfd", "sdf");

        assertThat(trie.equals(unmodTrie), is(true));
        assertThat(unmodTrie.equals(trie), is(true));

        trie.put("dfdsdf", "sdsdfsdff");

        assertThat(trie.equals(unmodTrie), is(true));
        assertThat(unmodTrie.equals(trie), is(true));
    }

    @Test
    public void testSerializable() throws IOException, ClassNotFoundException {
        HashTrie<String> trie = new HashTrie<>();
        trie.put("a", "b");

        Tries.UnmodifiableTrie<String> orgUnmodTrie = new Tries.UnmodifiableTrie<>(trie);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(bos);

        outStream.writeObject(orgUnmodTrie);
        outStream.close();

        ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        Tries.UnmodifiableTrie<String> dstTrie = (Tries.UnmodifiableTrie<String>) inStream.readObject();
        inStream.close();

        assertThat(dstTrie.get("a"), is("b"));
    }

}