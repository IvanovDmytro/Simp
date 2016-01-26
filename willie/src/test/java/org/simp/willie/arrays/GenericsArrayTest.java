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
package org.simp.willie.arrays;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class GenericsArrayTest {

    private ItemData[] mArray;

    static class ItemData {

    }

    @Before
    public void setUp() {
        mArray = new ItemData[]{new ItemData(), new ItemData(), new ItemData(), new ItemData()};
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrapNullPointerException() {
        GenericArray.<ItemData>wrap(null);
    }

    @Test
    public void testWrapUsesSameArray() {
        final ItemData oldData = new ItemData();
        final ItemData newData = new ItemData();

        mArray[0] = oldData;

        final GenericArray<ItemData> data = GenericArray.wrap(mArray);

        mArray[0] = newData;

        assertThat(data.itemAt(0), equalTo(newData));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopyNullPointerException() {
        GenericArray.<ItemData>copy(null);
    }

    @Test
    public void testCopyCreateNewArray() {
        final ItemData oldData = new ItemData();
        final ItemData newData = new ItemData();

        mArray[0] = oldData;

        final GenericArray<ItemData> data = GenericArray.copy(mArray);

        mArray[0] = newData;

        assertThat(data.itemAt(0), equalTo(oldData));
    }

    @Test
    public void testItemAt() {
        final GenericArray<ItemData> data = GenericArray.wrap(mArray);

        for (int i = 0; i < mArray.length; i++) {
            assertThat(data.itemAt(i), equalTo(mArray[i]));
        }
    }

    @Test
    public void testLength() {
        assertThat(GenericArray.wrap(mArray).length(), equalTo(mArray.length));
    }

}
