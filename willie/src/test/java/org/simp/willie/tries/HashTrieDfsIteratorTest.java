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

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashTrieDfsIteratorTest {

    private Object mObject1;

    private HashTrie<Object> mTrie;

    @Before
    public void setUp() {
        mObject1 = new Object();
        mTrie = new HashTrie<>();
    }

    private HashTrie.DfsIterator createIteratorForTrie() {
        return mTrie.new DfsIterator() {
        };
    }

    @Test
    public void testConstructor() {
        HashTrie.DfsIterator iterator = createIteratorForTrie();

        assertThat(iterator.mExpectedModCount, is(mTrie.mModCount));
        assertThat(iterator.mStringBuilder.length(), is(0));
        assertThat(iterator.mIteratorPath.size(), is(1));
    }

    @Test
    public void testConstructor_WithPredefinedValues() {
        mTrie.put("sd", mObject1);

        HashTrie.DfsIterator iterator = mTrie.new DfsIterator(mTrie.mRoot.getChildFor('s').getChildFor('d'), "sd") {
        };

        assertThat(iterator.mExpectedModCount, is(mTrie.mModCount));
        assertThat(iterator.mStringBuilder.toString(), is("sd"));
        assertThat((String) iterator.mNextEntry.getKey(), is("sd"));
        assertThat(iterator.mIteratorPath.size(), is(2));
    }

    @Test
    public void testHasNext() {
        HashTrie.DfsIterator iterator = createIteratorForTrie();

        assertThat(iterator.hasNext(), is(false));

        mTrie.put("a", new Object());

        iterator = createIteratorForTrie();

        assertThat(iterator.hasNext(), is(true));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() throws Exception {
        createIteratorForTrie().remove();
    }

    @Test(expected = NoSuchElementException.class)
    public void textNextEntry_NoSuchElementException() {
        mTrie.put("a", mObject1);

        HashTrie.DfsIterator iterator = createIteratorForTrie();

        iterator.nextEntry();
        iterator.nextEntry();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void textNextEntry_ConcurrentModificationPut() {
        mTrie.put("a", mObject1);

        HashTrie.DfsIterator iterator = createIteratorForTrie();

        mTrie.put("b", mObject1);

        iterator.nextEntry();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void textNextEntry_ConcurrentModificationClear() {
        mTrie.put("a", mObject1);

        HashTrie.DfsIterator iterator = createIteratorForTrie();

        mTrie.clear();

        iterator.nextEntry();
    }

    @Test(expected = ConcurrentModificationException.class)
    public void textNextEntry_ConcurrentModificationRemove() {
        mTrie.put("a", mObject1);

        HashTrie.DfsIterator iterator = createIteratorForTrie();

        mTrie.remove("a");

        iterator.nextEntry();
    }

    @Test
    public void testNextEntry_SingleValue() {
        mTrie.put("a", mObject1);
        HashTrie.DfsIterator iterator = createIteratorForTrie();

        assertThat(iterator.hasNext(), is(true));

        Map.Entry<String, Object> entry = iterator.nextEntry();

        assertThat(entry.getValue(), is(mObject1));
        assertThat(entry.getKey(), is("a"));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testNextEntry_DoubleValues() {
        mTrie.put("gb", mObject1);
        mTrie.put("fsdgdfgdfgdfgdgf", mObject1);
        HashTrie.DfsIterator iterator = createIteratorForTrie();

        //fsdgdfgdfgdfgdgf
        assertThat(iterator.hasNext(), is(true));

        Map.Entry<String, Object> entry = iterator.nextEntry();

        assertThat(entry.getValue(), is(mObject1));
        assertThat(entry.getKey(), is("fsdgdfgdfgdfgdgf"));

        // gb
        assertThat(iterator.hasNext(), is(true));

        entry = iterator.nextEntry();

        assertThat(entry.getValue(), is(mObject1));
        assertThat(entry.getKey(), is("gb"));

        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testNextEntry_MultipleValues() {
        Map<String, Object> data = new HashMap<>();

        for (int i = 0; i < 10000; i++) {
            Object value = new Object();
            data.put(Integer.toString(i), value);
            mTrie.put(Integer.toString(i), value);
        }

        HashTrie.DfsIterator iterator = createIteratorForTrie();

        for (int i = 0; i < 10000; i++) {
            assertThat(iterator.hasNext(), is(true));

            Map.Entry<String, Object> entry = iterator.nextEntry();

            assertThat(data.containsKey(entry.getKey()), is(true));
            data.remove(entry.getKey());
        }

        assertThat(iterator.hasNext(), is(false));
        assertThat(data.isEmpty(), is(true));
    }

}
