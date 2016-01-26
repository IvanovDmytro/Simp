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

public class BooleansArrayTest {

    private boolean[] mArray;

    @Before
    public void setUp() {
        mArray = new boolean[]{true, false, true, false};
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrap_NullPointerException() {
        BooleansArray.wrap(null);
    }

    @Test
    public void testWrap_UsesSameArray() {
        mArray[0] = true;

        final BooleansArray data = BooleansArray.wrap(mArray);

        mArray[0] = false;

        assertThat(data.itemAt(0), equalTo(false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopy_NullPointerException() {
        BooleansArray.copy(null);
    }

    @Test
    public void testCopyCreateNewArray() {
        mArray[0] = true;

        final BooleansArray data = BooleansArray.copy(mArray);

        mArray[0] = false;

        assertThat(data.itemAt(0), equalTo(true));
    }

    @Test
    public void testItemAt() {
        final BooleansArray data = BooleansArray.wrap(mArray);

        for (int i = 0; i < mArray.length; i++) {
            assertThat(data.itemAt(i), equalTo(mArray[i]));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testItemAt_IndexOutOfBound() {
        BooleansArray.wrap(mArray).itemAt(-1);
    }

    @Test
    public void testLength() {
        assertThat(BooleansArray.wrap(mArray).length(), equalTo(mArray.length));
    }

}
