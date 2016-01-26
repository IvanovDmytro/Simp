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

public class StringsArrayTest {

    private String[] mArray;

    @Before
    public void setUp() {
        mArray = new String[]{"a", "b", "c", "d"};
    }

    @Test
    public void testWrap_ZeroLength() {
        assertThat(StringsArray.wrap().length(), equalTo(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrap_NullPointerException() {
        StringsArray.wrap((String[]) null);
    }

    @Test
    public void testWrap_UsesSameArray() {
        mArray[0] = "a";

        final StringsArray data = StringsArray.wrap(mArray);

        mArray[0] = "b";

        assertThat(data.itemAt(0), equalTo("b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCopy_NullPointerException() {
        StringsArray.copy((String[]) null);
    }

    @Test
    public void testCopy_CreateNewArray() {
        mArray[0] = "a";

        final StringsArray data = StringsArray.copy(mArray);

        mArray[0] = "b";

        assertThat(data.itemAt(0), equalTo("a"));
    }

    @Test
    public void testItemAt() {
        final StringsArray data = StringsArray.wrap(mArray);

        for (int i = 0; i < mArray.length; i++) {
            assertThat(data.itemAt(i), equalTo(mArray[i]));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testItemAt_IndexOutOfBound() {
        StringsArray.wrap(mArray).itemAt(-1);
    }

    @Test
    public void testLength() {
        assertThat(StringsArray.wrap(mArray).length(), equalTo(mArray.length));
    }

}
