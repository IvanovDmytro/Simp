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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class HashTrieNodeTest {

    private Object mObject;

    private List<HashTrie.Node<Object>> mChildren;

    private HashTrie.Node<Object> mNode;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        mNode = new HashTrie.Node<>();
        mObject = new Object();

        mChildren = new ArrayList<>();
        for (int i = 0; i < 26; i++) {
            mChildren.add(new HashTrie.Node<>());
        }
    }

    @Test
    public void testConstructor() {
        assertThat(mNode.mValue, is(nullValue()));
        assertThat(mNode.hasChildren(), is(false));
    }

    @Test
    public void testGetAndSetValue() {
        mNode.mValue = mObject;

        assertThat(mNode.mValue, is(mObject));
    }

    @Test
    public void testHasValue() {
        assertThat(mNode.hasValue(), is(false));

        mNode.mValue = mObject;

        assertThat(mNode.hasValue(), is(true));
    }

    @Test
    public void testHasChildren() {
        assertThat(mNode.hasChildren(), is(false));

        mNode.addChild('a', mChildren.get(0));

        assertThat(mNode.hasChildren(), is(true));
    }

    @Test
    public void testGetChildFor() {
        assertThat(mNode.getChildFor('b'), is(nullValue()));

        mNode.addChild('b', mChildren.get(0));

        assertThat(mNode.getChildFor('b'), is(mChildren.get(0)));
    }

    @Test
    public void testRemoveChild() {
        mNode.addChild('b', mChildren.get(0));
        mNode.removeChild('b');

        assertThat(mNode.getChildFor('b'), is(nullValue()));
    }

    @Test
    public void testChildren() {
        for (int i = 0; i < mChildren.size(); i++) {
            mNode.addChild((char) ('a' + i), mChildren.get(i));
        }

        assertThat(mNode.children().size(), is(mChildren.size()));
    }

}
