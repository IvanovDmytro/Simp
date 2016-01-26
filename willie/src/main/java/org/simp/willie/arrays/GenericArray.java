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
 * Immutable array of objects.
 * Does not use autoboxing, threadsafe.
 *
 * @param <T> type of the arrays item, should be immutable.
 * @author Dmytro Ivanov
 */
@SuppressWarnings("PMD.UseVarargs")
public final class GenericArray<T> {

    /**
     * Wrapped mutable array.
     */
    private final T[] mData;

    /**
     * Creates new instance of {@link GenericArray} by wrapping provided in a parameters array.
     * Normally method should be used for relatively large array that you do not want to create a copy.
     * WARNING: method should be used just in case caller will not hold reference and modify content
     * of given array.
     *
     * @param data array to be wrapped
     * @param <T>  type of the arrays item, should be immutable.
     * @return new instance of {@link GenericArray}
     */
    public static <T> GenericArray<T> wrap(final T[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Given array could not be null.");
        }

        return new GenericArray<>(data);
    }

    /**
     * Creates new instance of {@link GenericArray} by copying provided in a parameters array.
     * Method should be used for most cases of {@link GenericArray} creation.
     * WARNING: creates new instance of given array, might not be efficient for big arrays.
     *
     * @param data array to be copied
     * @param <T>  type of the arrays item, should be immutable.
     * @return new instance of {@link GenericArray}
     */
    public static <T> GenericArray<T> copy(final T[] data) {
        if (data == null) {
            throw new IllegalArgumentException("Given array could not be null.");
        }

        final Object[] copy = new Object[data.length];
        System.arraycopy(data, 0, copy, 0, data.length);

        return new GenericArray<>((T[]) copy);
    }

    @SuppressWarnings("PMD.ArrayIsStoredDirectly")
    private GenericArray(final T[] data) {
        mData = data;
    }

    /**
     * @param index of the item
     * @return item of the array on given index
     */
    public T itemAt(final int index) {
        return mData[index];
    }

    /**
     * @return length of the array
     */
    public int length() {
        return mData.length;
    }

}
