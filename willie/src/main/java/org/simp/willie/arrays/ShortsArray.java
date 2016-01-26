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

/**
 * Immutable array of {@code short}.
 * Does not use autoboxing, threadsafe.
 *
 * @author Dmytro Ivanov
 */
@SuppressWarnings({"PMD.UseVarargs", "PMD.AvoidUsingShortType"})
public final class ShortsArray {

    /**
     * Wrapped mutable array.
     */
    private final short[] mData;

    /**
     * Creates new instance of {@link ShortsArray} by wrapping provided in a parameters array.
     * Normally method should be used for relatively large array that you do not want to create a copy.
     * WARNING: method should be used just in case caller will not hold reference and modify content
     * of given array.
     *
     * @param data array to be wrapped
     * @return new instance of {@link ShortsArray}
     */
    public static ShortsArray wrap(final short... data) {
        if (data == null) {
            throw new IllegalArgumentException("Given array could not be null.");
        }

        return new ShortsArray(data);
    }

    /**
     * Creates new instance of {@link ShortsArray} by copying provided in a parameters array.
     * Method should be used for most cases of {@link ShortsArray} creation.
     * WARNING: creates new instance of given array, might not be efficient for big arrays.
     *
     * @param data array to be copied
     * @return new instance of {@link ShortsArray}
     */
    public static ShortsArray copy(final short... data) {
        if (data == null) {
            throw new IllegalArgumentException("Given array could not be null.");
        }

        final short[] copy = new short[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);

        return new ShortsArray(copy);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    private ShortsArray(final short[] data) {
        mData = data;
    }

    /**
     * @param index of the item
     * @return item of the array on given index
     */
    public short itemAt(final int index) {
        return mData[index];
    }

    /**
     * @return length of the array
     */
    public int length() {
        return mData.length;
    }

}
