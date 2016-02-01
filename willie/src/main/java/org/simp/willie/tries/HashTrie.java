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

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * {@code Trie} interface implementation that uses {@code HashMap} in the node
 * to store references to the children.  This implementation provides all of the
 * optional trie operations.
 *
 * <p>This implementation provides O(S), where is S is lenght of the key
 * performance for the basic operations ({@code get} and {@code put}).
 * Iteration over collection views requires time proportional to the size of the
 * {@code Trie} instance.
 *
 * <p><strong>Note that this implementation is not synchronized.</strong>
 * If multiple threads access a trie concurrently, and at least one of
 * the threads modifies the trie structurally, it <i>must</i> be
 * synchronized externally.  (A structural modification is any operation
 * that adds or deletes one or more mappings; merely changing the value
 * associated with a key that an instance already contains is not a
 * structural modification.)  This is typically accomplished by
 * synchronizing on some object that naturally encapsulates the trie.
 *
 * <p>The iterators returned by all of this class's "collection view methods"
 * are <i>fail-fast</i>: if the trie is structurally modified at any time after
 * the iterator is created, in any way except through the iterator's own
 * {@code remove} method, the iterator will throw a
 * {@link ConcurrentModificationException}.  Thus, in the face of concurrent
 * modification, the iterator fails quickly and cleanly, rather than risking
 * arbitrary, non-deterministic behavior at an undetermined time in the
 * future.
 *
 * <p>Note that the fail-fast behavior of an iterator cannot be guaranteed
 * as it is, generally speaking, impossible to make any hard guarantees in the
 * presence of unsynchronized concurrent modification.  Fail-fast iterators
 * throw {@code ConcurrentModificationException} on a best-effort basis.
 * Therefore, it would be wrong to write a program that depended on this
 * exception for its correctness: <i>the fail-fast behavior of iterators
 * should be used only to detect bugs.</i>
 *
 * @param <V> the type of mapped values
 *
 * @author Dmytro Ivanov
 * @see     Trie
 */
@SuppressWarnings("PMD.GodClass")
public class HashTrie<V> extends AbstractTrie<V> implements Trie<V>, Serializable, Cloneable {

    private static final long serialVersionUID = -3275675110121867083L;

    /**
     * Node of the trie.
     * Keeps references to the children in a {@code Map} that has {@code Character}
     * as a key and V as an value.
     *
     * Provides convenience methods to operate with nodes children.
     *
     * @param <V> the type of stored values
     */
    @SuppressWarnings("PMD.ShortClassName")
    static final class Node<V> {

        /**
         * Value of the node, null if no value is present.
         */
        V mValue;

        /**
         * References to the children, never is {@code null}.
         */
        Map<Character, Node<V>> mChildren;

        Node() {
            mValue = null;
            mChildren = new HashMap<>();
        }

        boolean hasValue() {
            return mValue != null;
        }

        boolean hasChildren() {
            return !mChildren.isEmpty();
        }

        Node<V> getChildFor(Character character) {
            return mChildren.get(character);
        }

        void addChild(Character character, Node<V> child) {
            mChildren.put(character, child);
        }

        void removeChild(Character character) {
            mChildren.remove(character);
        }

        Set<Map.Entry<Character, Node<V>>> children() {
            return mChildren.entrySet();
        }

    }

    /**
     * Holds cached entrySet().
     */
    transient volatile Set<Map.Entry<String, V>> mEntriesView;

    /**
     * Holds cached keySet().
     */
    transient volatile Set<String> mKeysView;

    /**
     * Holds cached values().
     */
    transient volatile Collection<V> mValuesView;

    /**
     * The number of times this HashTrie has been structurally modified.
     * This field is used to make iterators on Collection-views of
     * the HashTrie fail-fast.
     */
    transient int mModCount;

    /**
     * The number of key-value mappings contained in this trie.
     */
    transient int mSize;

    /**
     * The root of the trie, should never be {@code null}.
     */
    transient Node<V> mRoot;

    public HashTrie() {
        mSize = 0;
        mRoot = new Node<>();
    }

    /**
     * Returns the number of key-value mappings in this trie.
     *
     * Operation time complexity is O(1).
     *
     * @return the number of key-value mappings in this trie
     */
    @Override
    public int size() {
        return mSize;
    }

    /**
     * Returns {@code true} if this trie contains no key-value mappings.
     *
     * Operation time complexity is O(1).
     *
     * @return {@code true} if this trie contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return mSize == 0;
    }

    /**
     * Returns {@code true} if this trie contains a mapping for the specified key.
     *
     * Operation time complexity is O(S), where S is length of key.
     *
     * @param   key   The key whose presence in this trie is to be tested
     * @return {@code true} if this trie contains a mapping for the specified key.
     */
    @Override
    public boolean containsKey(String key) {
        return get(key) != null;
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this trie contains no mapping for the key.
     *
     * Operation time complexity is O(S), where S is length of key.
     *
     * @see #put(String, Object)
     */
    @Override
    public V get(String key) {
        checkKey(key);

        final Node<V> node = findNode(key);
        return node == null ? null : node.mValue;
    }

    /**
     * Find node that contain value for a given {@code key}.
     * If there are no key in a trie, {@code null} is returned.
     */
    final Node<V> findNode(String key) {
        int keyIndex = 0;
        Node<V> node = mRoot;
        while (keyIndex < key.length() && node != null) {
            node = node.getChildFor(key.charAt(keyIndex));

            keyIndex++;
        }

        return node;
    }

    /**
     * Associates the specified value with the specified key in this trie.
     * If the trie previously contained a mapping for the key, the old
     * value is replaced.
     *
     * Operation time complexity is O(S), where S is length of key.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     */
    @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
    @Override
    public V put(String key, V value) {
        checkKey(key);
        checkValue(value);

        mModCount++;

        Node<V> node = mRoot;
        for (int i = 0; i < key.length(); i++) {
            final Character currChar = key.charAt(i);

            Node<V> nextNode = node.getChildFor(currChar);
            if (nextNode == null) {
                nextNode = new Node<>();
                node.addChild(currChar, nextNode);
            }

            node = nextNode;
        }

        V returnValue = null;

        if (node.hasValue()) {
            returnValue = node.mValue;
        } else {
            mSize++;
        }

        node.mValue = value;

        return returnValue;
    }

    /**
     * Removes the mapping for the specified key from this trie if present.
     *
     * Operation time complexity is O(S), where S is length of key.
     *
     * @implSpec After item is removed from the trie, trie compression
     * is running.
     *
     * @param  key key whose mapping is to be removed from the map
     * @return the previous value associated with {@code key}, or
     *         {@code null} if there was no mapping for {@code key}.
     */
    @Override
    public V remove(String key) {
        checkKey(key);

        mModCount++;

        // path needed to clean up path after
        Node[] path = new Node[key.length() + 1];

        int keyIndex = 0;
        Node<V> node = mRoot;
        while (keyIndex < key.length() && node != null) {
            path[key.length() - keyIndex] = node;
            node = node.getChildFor(key.charAt(keyIndex));

            keyIndex++;
        }

        if (node == null || !node.hasValue()) {
            return null;
        }

        final V value = node.mValue;

        node.mValue = null;
        mSize--;

        // clean up unnecessary nodes
        path[0] = node;
        for (int i = 0; i < path.length - 1; i++) {
            if (path[i].hasChildren() || path[i].hasValue()) {
                break;
            } else {
                path[i + 1].removeChild(key.charAt(key.length() - i - 1));
            }
        }

        return value;
    }

    /**
     * Check given {@code key} if it satisfies conditions to be the key of trie.
     * Key should not be null, empty or contain anything except digits ot letters.
     */
    final void checkKey(String key) {
        if (key == null) {
            throw new NullPointerException("Key could not be null.");
        }

        if (key.length() == 0) {
            throw new IllegalArgumentException("Key could not be empty string.");
        }

        for (int i = 0; i < key.length(); i++) {
            if (!Character.isLetterOrDigit(key.charAt(i))) {
                throw new IllegalArgumentException("Key should contain just letter or digit.");
            }
        }
    }

    /**
     * Check given value if it can be putted into te trie.
     * Value should not be null.
     */
    final void checkValue(V value) {
        if (value == null) {
            throw new NullPointerException("Value could not be null.");
        }
    }

    /**
     * Removes all of the mappings from this trie.
     *
     * Operation time complexity is O(1).
     *
     * The trie will be empty after this call returns.
     */
    @Override
    public void clear() {
        mModCount++;
        mSize = 0;
        mRoot = new Node<>();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this trie.
     * The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie
     * is modified while an iteration over the set is in progress , the results of
     * the iteration are undefined.  The set does not supports modification
     * operations.
     *
     * @return a set view of the keys contained in this trie
     */
    @Override
    public Set<String> keys() {
        Set<String> keySet;
        return (keySet = mKeysView) == null ? (mKeysView = new KeySet(mRoot, "")) : keySet;
    }

    /**
     * Returns a {@link Set} view of the keys contained in this trie that start with
     * given prefix. The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie
     * is modified while an iteration over the set is in progress , the results of
     * the iteration are undefined.  The set does not supports modification
     * operations.
     *
     * @param prefix the prefix that will be used to filter keys
     * @return a set view of the keys contained in this trie
     */
    @Override
    public Set<String> keysWithPrefix(String prefix) {
        checkKey(prefix);

        final Node<V> node = findNode(prefix);

        return node == null ? Collections.<String>emptySet() : new KeySet(node, prefix);
    }

    final class KeySet extends AbstractSet<String> {

        Node<V> mNode;
        String mPrefix;

        KeySet(Node<V> node, String prefix) {
            mNode = node;
            mPrefix = prefix;
        }

        public int size() {
            return mSize;
        }

        public Iterator<String> iterator() {
            return new KeyIterator(mNode, mPrefix);
        }

    }

    /**
     * Returns a {@link Collection} view of the values contained in this trie.
     * The collection is backed by the trie, so changes to the trie are
     * reflected in the collection, vice-versa is not supported.  If the trie is
     * modified while an iteration over the collection is in progress
     * the results of the iteration are undefined. The collection does not supports
     * modification operations.
     *
     * @return a collection view of the values contained in this trie
     */
    @Override
    public Collection<V> values() {
        Collection<V> valuesCollection;
        return (valuesCollection = mValuesView) == null ? (mValuesView = new Values()) : valuesCollection;
    }

    final class Values extends AbstractCollection<V> {

        public int size() {
            return mSize;
        }

        public Iterator<V> iterator() {
            return new ValueIterator();
        }

    }

    /**
     * Returns a {@link Set} view of the mappings contained in this trie.
     * The set is backed by the trie, so changes to the trie are
     * reflected in the set, vice-versa is not supported.  If the trie is modified
     * while an iteration over the set is in progress  the results of the iteration are undefined.
     * The set does not supports modification operations.
     *
     * @return a set view of the mappings contained in this trie
     */
    @Override
    public Set<Map.Entry<String, V>> entrySet() {
        Set<Map.Entry<String, V>> entriesView;
        return (entriesView = mEntriesView) == null ? (mEntriesView = new EntrySet()) : entriesView;
    }

    final class EntrySet extends AbstractSet<Map.Entry<String, V>> {

        public int size() {
            return mSize;
        }

        public Iterator<Map.Entry<String, V>> iterator() {
            return new EntryIterator();
        }

    }

    @SuppressWarnings({"PMD.AbstractClassWithoutAbstractMethod", "PMD.AvoidStringBufferField"})
    abstract class DfsIterator {
        Map.Entry<String, V> mNextEntry;
        StringBuilder mStringBuilder;
        Deque<Iterator<Map.Entry<Character, Node<V>>>> mIteratorPath;
        int mExpectedModCount;

        DfsIterator() {
            this(mRoot, "");
        }

        DfsIterator(Node<V> initialNode, String prefix) {
            mExpectedModCount = mModCount;
            mIteratorPath = new ArrayDeque<>();

            if (initialNode.hasValue()) {
                final Node<V> fakeStartNode = new Node<>();
                fakeStartNode.addChild(prefix.charAt(prefix.length() - 1), initialNode);

                mStringBuilder = new StringBuilder(prefix.substring(0, prefix.length() - 1));
                mIteratorPath.add(fakeStartNode.children().iterator());
            } else {
                mStringBuilder = new StringBuilder();
                mIteratorPath.add(initialNode.children().iterator());
            }

            updateNextEntry();
        }

        public final boolean hasNext() {
            return mNextEntry != null;
        }

        final void updateNextEntry() {
            Node<V> nodeWithValue = null;
            Iterator<Map.Entry<Character, Node<V>>> nodeIterator = mIteratorPath.getFirst();
            while (nodeWithValue == null
                    && (nodeIterator.hasNext() || mStringBuilder.length() > 0 && mIteratorPath.size() > 1)) {
                if (nodeIterator.hasNext()) {
                    final Map.Entry<Character, Node<V>> nextChild = nodeIterator.next();

                    nodeIterator = nextChild.getValue().children().iterator();

                    mStringBuilder.append(nextChild.getKey());
                    mIteratorPath.addFirst(nodeIterator);

                    if (nextChild.getValue().hasValue()) {
                        nodeWithValue = nextChild.getValue();
                    }
                } else {
                    mIteratorPath.removeFirst();
                    mStringBuilder.deleteCharAt(mStringBuilder.length() - 1);

                    nodeIterator = mIteratorPath.getFirst();
                }
            }

            mNextEntry = nodeWithValue == null
                    ? null
                    : new AbstractMap.SimpleEntry<>(mStringBuilder.toString(), nodeWithValue.mValue);
        }

        final Map.Entry<String, V> nextEntry() {
            if (mExpectedModCount != mModCount) {
                throw new ConcurrentModificationException();
            }

            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            final Map.Entry<String, V> currEntry = mNextEntry;

            updateNextEntry();

            return currEntry;
        }

        public final void remove() {
            throw new UnsupportedOperationException();
        }
    }

    final class KeyIterator extends DfsIterator implements Iterator<String> {
        KeyIterator(Node<V> node, String prefix) {
            super(node, prefix);
        }

        public String next() {
            return nextEntry().getKey();
        }
    }

    final class ValueIterator extends DfsIterator implements Iterator<V> {
        public V next() {
            return nextEntry().getValue();
        }
    }

    final class EntryIterator extends DfsIterator implements Iterator<Map.Entry<String, V>> {
        public Map.Entry<String, V> next() {
            return nextEntry();
        }
    }

    /**
     * Returns a shallow copy of this {@code HashTrie} instance: the keys and
     * values themselves are not cloned.
     *
     * Operation time complexity is O(N), where N is number of mappings in a trie.
     *
     * @return a shallow copy of this trie
     */
    @SuppressWarnings("unchecked")
    @Override
    public HashTrie<V> clone() {
        HashTrie<V> result;

        try {
            result = (HashTrie<V>) super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }

        result.reinitialize();

        for (Map.Entry<String, V> entry : entrySet()) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /**
     * Save the state of the {@code HashTrie} instance to a stream (i.e., serialize it).
     *
     * @serialData The <i>size</i> (an int, the number of key-value mappings), followed by the
     * key (String) and value (Object) for each key-value mapping.  The key-value mappings are
     * emitted in no particular order.
     */
    private void writeObject(java.io.ObjectOutputStream stream) throws IOException {
        stream.writeInt(mSize);

        for (Map.Entry<String, V> entry : entrySet()) {
            stream.writeObject(entry.getKey());
            stream.writeObject(entry.getValue());
        }
    }

    /**
     * Reconstitute the {@code HashTrie} instance from a stream (i.e., deserialize it).
     */
    private void readObject(java.io.ObjectInputStream stream) throws IOException, ClassNotFoundException {
        reinitialize();

        final int size = stream.readInt();
        if (size < 0) {
            throw new InvalidObjectException("Illegal size: " + size);
        } else if (size > 0) { // (if zero, use defaults)
            for (int i = 0; i < size; i++) {
                final String key = (String) stream.readObject();
                @SuppressWarnings("unchecked")
                final V value = (V) stream.readObject();
                put(key, value);
            }
        }
    }

    /**
     * Reset to initial default state.  Called by {@code clone} and {@code readObject}.
     */
    final void reinitialize() {
        mRoot = new Node<>();
        mKeysView = null;
        mValuesView = null;
        mEntriesView = null;
        mModCount = 0;
        mSize = 0;
    }

}
