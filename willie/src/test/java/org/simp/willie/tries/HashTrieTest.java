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
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Set;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

public class HashTrieTest {

    private Object mObject1;
    private Object mObject2;
    private Object mObject3;

    private HashTrie<Object> mTrie;

    @Before
    public void setUp() {
        mObject1 = new Object();
        mObject2 = new Object();
        mObject3 = new Object();

        mTrie = new HashTrie<>();
    }

    @Test
    public void testConstructor() {
        assertThat(mTrie.mSize, is(0));
        assertThat(mTrie.mRoot.hasValue(), is(false));
        assertThat(mTrie.mRoot.hasChildren(), is(false));
        assertThat(mTrie.mModCount, is(0));
        assertThat(mTrie.mEntriesView, is(nullValue()));
        assertThat(mTrie.mKeysView, is(nullValue()));
        assertThat(mTrie.mValuesView, is(nullValue()));
    }

    @Test(expected = NullPointerException.class)
    public void testPut_NullKey() {
        mTrie.put(null, new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPut_EmptyKey() {
        mTrie.put("", new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPut_WrongKey() {
        mTrie.put("1231234.sdf", new Object());
    }

    @Test(expected = NullPointerException.class)
    public void testPut_NullValue() {
        mTrie.put("aads", null);
    }

    @Test
    public void testPut_SingleCharKey() {
        Character key = 'к';

        mTrie.put(Character.toString(key), mObject1);

        assertThat(mTrie.mRoot.getChildFor(key), is(notNullValue()));
        assertThat(mTrie.mRoot.children().size(), is(1));
        assertThat(mTrie.mRoot.hasValue(), is(false));

        assertThat(mTrie.mRoot.getChildFor(key).mValue, is(mObject1));
        assertThat(mTrie.mRoot.getChildFor(key).hasChildren(), is(false));
    }

    @Test
    public void testPut_SingleCharKeyTwiceWithDifferentValue() {
        Character key = 'к';

        mTrie.put(Character.toString(key), mObject1);
        mTrie.put(Character.toString(key), mObject2);

        assertThat(mTrie.mRoot.getChildFor(key), is(notNullValue()));
        assertThat(mTrie.mRoot.children().size(), is(1));
        assertThat(mTrie.mRoot.hasValue(), is(false));

        assertThat(mTrie.mRoot.getChildFor(key).mValue, is(mObject2));
        assertThat(mTrie.mRoot.getChildFor(key).hasChildren(), is(false));
    }

    @Test
    public void testPut_DoubleCharKey() {
        String key = "к例";

        mTrie.put(key, mObject1);

        assertThat(mTrie.mRoot.getChildFor(key.charAt(0)), is(notNullValue()));
        assertThat(mTrie.mRoot.children().size(), is(1));
        assertThat(mTrie.mRoot.hasValue(), is(false));

        HashTrie.Node<Object> child1 = mTrie.mRoot.getChildFor(key.charAt(0));
        assertThat(child1.getChildFor(key.charAt(1)), is(notNullValue()));
        assertThat(child1.hasValue(), is(false));
        assertThat(child1.children().size(), is(1));

        HashTrie.Node<Object> child2 = child1.getChildFor(key.charAt(1));
        assertThat(child2.hasChildren(), is(false));
        assertThat(child2.mValue, is(mObject1));
    }

    @Test
    public void testPut_DoubleCharKeyTwiceWithDifferentValue() {
        String key = "к例";

        mTrie.put(key, mObject1);
        mTrie.put(key, mObject2);

        assertThat(mTrie.mRoot.getChildFor(key.charAt(0)), is(notNullValue()));
        assertThat(mTrie.mRoot.children().size(), is(1));
        assertThat(mTrie.mRoot.hasValue(), is(false));

        HashTrie.Node<Object> child1 = mTrie.mRoot.getChildFor(key.charAt(0));
        assertThat(child1.getChildFor(key.charAt(1)), is(notNullValue()));
        assertThat(child1.hasValue(), is(false));
        assertThat(child1.children().size(), is(1));

        HashTrie.Node<Object> child2 = child1.getChildFor(key.charAt(1));
        assertThat(child2.hasChildren(), is(false));
        assertThat(child2.mValue, is(mObject2));
    }

    @Test
    public void testPut_DifferentKeys() {
        String key1 = "к例";
        String key2 = "к";
        String key3 = "iк";

        mTrie.put(key1, mObject1);
        mTrie.put(key2, mObject2);
        mTrie.put(key3, mObject3);

        // check root
        HashTrie.Node<Object> root = mTrie.mRoot;
        assertThat(root.getChildFor(key2.charAt(0)), is(notNullValue()));
        assertThat(root.getChildFor(key3.charAt(0)), is(notNullValue()));
        assertThat(root.children().size(), is(2));
        assertThat(root.hasValue(), is(false));

        // check child1
        HashTrie.Node<Object> child1 = root.getChildFor(key1.charAt(0));
        assertThat(child1.getChildFor(key1.charAt(1)), is(notNullValue()));
        assertThat(child1.mValue, is(mObject2));
        assertThat(child1.children().size(), is(1));

        // check child2
        HashTrie.Node<Object> child2 = child1.getChildFor(key1.charAt(1));
        assertThat(child2.hasChildren(), is(false));
        assertThat(child2.mValue, is(mObject1));

        // check child3
        HashTrie.Node<Object> child3 = root.getChildFor(key3.charAt(0));
        assertThat(child3.getChildFor(key3.charAt(1)), is(notNullValue()));
        assertThat(child3.children().size(), is(1));
        assertThat(child3.hasValue(), is(false));

        // check child4
        HashTrie.Node<Object> child4 = child3.getChildFor(key3.charAt(1));
        assertThat(child4.hasChildren(), is(false));
        assertThat(child4.mValue, is(mObject3));
    }

    @Test(expected = NullPointerException.class)
    public void testRemove_NullKey() {
        mTrie.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemove_EmptyKey() {
        mTrie.remove("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemove_WrongKey() {
        mTrie.remove("sdfsdf dfsdf");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemove_ValueNotThere() {
        mTrie.put("a", mObject1);
        mTrie.put("b", mObject2);

        mTrie.remove("a1");
    }

    @Test
    public void testRemove_SingleValue() {
        mTrie.put("abc", mObject1);

        mTrie.remove("abc");

        assertThat(mTrie.mRoot.hasChildren(), is(false));
        assertThat(mTrie.mRoot.hasValue(), is(false));
        assertThat(mTrie.mSize, is(0));
    }

    @Test
    public void testRemove_FewValuesWithCleanup() {
        mTrie.put("a", mObject1);
        mTrie.put("abc", mObject2);

        mTrie.remove("abc");

        assertThat(mTrie.mRoot.children().size(), is(1));
        assertThat(mTrie.mRoot.hasValue(), is(false));
        assertThat(mTrie.mRoot.getChildFor('a'), is(notNullValue()));
        assertThat(mTrie.mRoot.getChildFor('a').hasChildren(), is(false));
        assertThat(mTrie.mRoot.getChildFor('a').mValue, is(mObject1));
        assertThat(mTrie.mSize, is(1));
    }

    @Test(expected = NullPointerException.class)
    public void testGet_NullKey() {
        mTrie.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_EmptyKey() {
        mTrie.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGet_WrongKey() {
        mTrie.get("\tdfsd");
    }

    @Test
    public void testGet_ValueIsNotThere() {
        assertThat(mTrie.get("a"), is(nullValue()));
    }

    @Test
    public void testGet_SingleValueIsThere() {
        mTrie.put("a", mObject1);
        assertThat(mTrie.get("a"), is(mObject1));
        assertThat(mTrie.get("b"), is(nullValue()));
    }

    @Test
    public void testGet_SingleValueAddedTwice() {
        mTrie.put("a", mObject1);
        assertThat(mTrie.get("a"), is(mObject1));
        mTrie.put("a", mObject2);
        assertThat(mTrie.get("a"), is(mObject2));
    }

    @Test
    public void testGet_MulValueIsThere() {
        String key1 = "к例";
        String key2 = "к";
        String key3 = "iк";

        mTrie.put(key1, mObject1);
        mTrie.put(key2, mObject2);
        mTrie.put(key3, mObject3);

        assertThat(mTrie.get(key1), is(mObject1));
        assertThat(mTrie.get(key2), is(mObject2));
        assertThat(mTrie.get(key3), is(mObject3));
    }

    @Test(expected = NullPointerException.class)
    public void testContainsKey_WithNullValue() {
        mTrie.containsKey(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsKey_EmptyKey() {
        mTrie.containsKey("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsKey_WrongKey() {
        mTrie.containsKey("-fsdfs");
    }

    @Test
    public void testContainsKey_NoValue() {
        assertThat(mTrie.containsKey("a"), is(false));
    }

    @Test
    public void testContainsKey_WithValue() {
        mTrie.put("a", mObject1);
        assertThat(mTrie.containsKey("a"), is(true));
        assertThat(mTrie.containsKey("b"), is(false));
    }

    @Test
    public void testIsEmpty_WhenEmpty() {
        assertThat(mTrie.isEmpty(), is(true));
    }

    @Test
    public void testIsEmpty_WhenNotEmpty() {
        mTrie.put("a", mObject1);

        assertThat(mTrie.isEmpty(), is(false));
    }

    @Test
    public void testSize_SinglePut() {
        assertThat(mTrie.size(), is(0));

        mTrie.put("a", mObject1);

        assertThat(mTrie.size(), is(1));
    }

    @Test
    public void testSize_DoublePut() {
        assertThat(mTrie.size(), is(0));

        mTrie.put("a", mObject1);
        mTrie.put("b", mObject1);

        assertThat(mTrie.size(), is(2));
    }

    @Test
    public void testSize_DoublePutOfSameKey() {
        assertThat(mTrie.size(), is(0));

        mTrie.put("a", mObject1);
        mTrie.put("a", mObject2);

        assertThat(mTrie.size(), is(1));
    }

    @Test
    public void testClear_WithoutValues() {
        mTrie.clear();

        assertThat(mTrie.mSize, is(0));
        assertThat(mTrie.mRoot.hasValue(), is(false));
        assertThat(mTrie.mRoot.hasChildren(), is(false));
        assertThat(mTrie.mModCount, is(1));
        assertThat(mTrie.mEntriesView, is(nullValue()));
        assertThat(mTrie.mKeysView, is(nullValue()));
        assertThat(mTrie.mValuesView, is(nullValue()));
    }

    @Test
    public void testClear_WithValue() {
        mTrie.put("a", mObject1);

        mTrie.clear();

        assertThat(mTrie.mSize, is(0));
        assertThat(mTrie.mRoot.hasValue(), is(false));
        assertThat(mTrie.mRoot.hasChildren(), is(false));
        assertThat(mTrie.mModCount, is(2));
        assertThat(mTrie.mEntriesView, is(nullValue()));
        assertThat(mTrie.mKeysView, is(nullValue()));
        assertThat(mTrie.mValuesView, is(nullValue()));
    }

    @Test
    public void testValuesCollection() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);

        HashTrie.Values values = mTrie.new Values();

        assertThat(values.size(), is(mTrie.mSize));
        assertThat(values.iterator(), is(instanceOf(HashTrie.ValueIterator.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueCollection_AddIsUnsupported() {
        mTrie.new Values().add(mObject1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueCollection_RemoveIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new Values().remove(mObject1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testValueCollection_ClearIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new Values().clear();
    }

    @Test
    public void testEntrySet() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);

        HashTrie.EntrySet entrySet = mTrie.new EntrySet();
        assertThat(entrySet.size(), is(mTrie.mSize));
        assertThat(entrySet.iterator(), is(instanceOf(HashTrie.EntryIterator.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_AddIsUnsupported() {
        mTrie.new EntrySet().add(new AbstractMap.SimpleEntry<>("sdf", null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_RemoveIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new EntrySet().remove(mTrie.new EntryIterator().next());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySet_ClearIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new EntrySet().clear();
    }

    @Test
    public void testKeysSet() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);

        HashTrie.KeySet keySet = mTrie.new KeySet(mTrie.mRoot, "");

        assertThat(keySet.size(), is(mTrie.mSize));
        assertThat((HashTrie.Node<Object>) keySet.mNode, is(mTrie.mRoot));
        assertThat(keySet.mPrefix, is(""));
        assertThat(keySet.iterator(), is(instanceOf(HashTrie.KeyIterator.class)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeysSet_AddIsUnsupported() {
        mTrie.new KeySet(mTrie.mRoot, "").add("a");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeysSet_RemoveIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new KeySet(mTrie.mRoot, "").remove("d");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testKeySet_ClearIsUnsupported() {
        mTrie.put("d", mObject1);
        mTrie.new KeySet(mTrie.mRoot, "").clear();
    }

    @Test
    public void testValues_EmptyTrie() {
        Collection<Object> values = mTrie.values();

        assertThat(values, is(empty()));

        for (Object object : values) {
            throw new AssertionError("Should not be contain any items");
        }
    }

    @Test
    public void testValues_MultipleValues() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);

        Collection<Object> values = mTrie.values();

        assertThat(values.size(), is(2));
        assertThat(values, containsInAnyOrder(mObject1, mObject2));
    }

    @Test
    public void testKeys_EmptyTrie() {
        Set<String> keys = mTrie.keys();

        assertThat(keys, is(empty()));

        for (String key : keys) {
            throw new AssertionError("Should not be contain any items");
        }
    }

    @Test
    public void testKeys_MultipleValues() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);
        mTrie.put("ab", mObject1);
        mTrie.put("abg", mObject2);

        Set<String> keys = mTrie.keys();

        assertThat(keys.size(), is(4));
        assertThat(keys, containsInAnyOrder("sf", "sfg", "ab", "abg"));
    }

    @Test(expected = NullPointerException.class)
    public void testKeysWithPrefix_NullPrefix() {
        mTrie.keysWithPrefix(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeysWithPrefix_EmptyPrefix() {
        mTrie.keysWithPrefix("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKeysWithPrefix_WrongPrefix() {
        mTrie.keysWithPrefix("*sdgf4");
    }

    @Test
    public void testKeysWithPrefix_EmptyTrie() {
        Set<String> keys = mTrie.keysWithPrefix("sdfsdf");

        assertThat(keys, is(empty()));

        for (String key : keys) {
            throw new AssertionError("Should not be contain any items");
        }
    }

    @Test
    public void testKeysWithPrefix_PrefixDoesNotExists() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);
        mTrie.put("ab", mObject1);
        mTrie.put("abg", mObject2);

        Set<String> keys = mTrie.keysWithPrefix("l");

        assertThat(keys, is(empty()));

        for (String key : keys) {
            throw new AssertionError("Should not be contain any items");
        }
    }

    @Test
    public void testKeysWithPrefix_PrefixCoversAllKeys() {
        mTrie.put("sf", mObject1);
        mTrie.put("sfg", mObject2);
        mTrie.put("sfh", mObject1);
        mTrie.put("sfgj", mObject2);

        Set<String> keys = mTrie.keysWithPrefix("sf");

        assertThat(keys, containsInAnyOrder("sf", "sfg", "sfh", "sfgj"));
    }

    @Test
    public void testKeysWithPrefix_PrefixCoversSingleKeys() {
        mTrie.put("sf", mObject1);

        Set<String> keys = mTrie.keysWithPrefix("sf");

        assertThat(keys, containsInAnyOrder("sf"));
    }

    @Test
    public void testSerialization_EmptyTrie() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(bos);

        mTrie.keys();
        mTrie.values();
        mTrie.entrySet();

        outStream.writeObject(mTrie);
        outStream.close();

        ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        HashTrie<Object> trie = (HashTrie<Object>) inStream.readObject();

        inStream.close();

        assertThat(trie.mSize, is(0));
        assertThat(trie.mRoot.hasValue(), is(false));
        assertThat(trie.mRoot.hasChildren(), is(false));
        assertThat(trie.mModCount, is(0));
        assertThat(trie.mKeysView, is(nullValue()));
        assertThat(trie.mEntriesView, is(nullValue()));
        assertThat(trie.mValuesView, is(nullValue()));
    }

    @Test
    public void testSerialization_TrieWithSingleValue() throws IOException, ClassNotFoundException {
        HashTrie<String> orgTrie = new HashTrie<>();
        orgTrie.put("asd", "dsa");

        orgTrie.entrySet();
        orgTrie.keys();
        orgTrie.values();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream outStream = new ObjectOutputStream(bos);

        outStream.writeObject(orgTrie);
        outStream.close();

        ObjectInputStream inStream = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));

        HashTrie<String> dstTrie = (HashTrie<String>) inStream.readObject();

        inStream.close();

        assertThat(dstTrie.mSize, is(1));
        assertThat(dstTrie.mRoot.hasValue(), is(false));
        assertThat(dstTrie.mRoot.hasChildren(), is(true));
        assertThat(dstTrie.mModCount, is(1));
        assertThat(dstTrie.mKeysView, is(nullValue()));
        assertThat(dstTrie.mEntriesView, is(nullValue()));
        assertThat(dstTrie.mValuesView, is(nullValue()));

        assertThat(dstTrie.get("asd"), is("dsa"));
    }

    @Test(expected = NotSerializableException.class)
    public void testSerialization_TrieWithNonSerializableValues() throws IOException {
        mTrie.put("sdf", mObject1);

        new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(mTrie);
    }

    @Test
    public void testClone() {
        HashTrie<String> orgTrie = new HashTrie<>();
        orgTrie.put("asd", "asd");

        HashTrie<String> clonedTrie = orgTrie.clone();

        assertThat(clonedTrie.containsKey("asd"), is(true));

        clonedTrie.put("a", "b");

        assertThat(orgTrie.size(), is(1));
        assertThat(clonedTrie.size(), is(2));
    }

}
