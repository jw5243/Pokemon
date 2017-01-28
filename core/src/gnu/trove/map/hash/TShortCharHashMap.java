///////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2001, Eric D. Friedman All Rights Reserved.
// Copyright (c) 2009, Rob Eden All Rights Reserved.
// Copyright (c) 2009, Jeff Randall All Rights Reserved.
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
///////////////////////////////////////////////////////////////////////////////

package gnu.trove.map.hash;


//////////////////////////////////////////////////
// THIS IS A GENERATED CLASS. DO NOT HAND EDIT! //
//////////////////////////////////////////////////

import gnu.trove.TCharCollection;
import gnu.trove.TShortCollection;
import gnu.trove.function.TCharFunction;
import gnu.trove.impl.HashFunctions;
import gnu.trove.impl.hash.THashPrimitiveIterator;
import gnu.trove.impl.hash.TPrimitiveHash;
import gnu.trove.impl.hash.TShortCharHash;
import gnu.trove.iterator.TCharIterator;
import gnu.trove.iterator.TShortCharIterator;
import gnu.trove.iterator.TShortIterator;
import gnu.trove.map.TShortCharMap;
import gnu.trove.procedure.TCharProcedure;
import gnu.trove.procedure.TShortCharProcedure;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.set.TShortSet;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Map;

/**
 * An open addressed Map implementation for short keys and char values.
 *
 * @author Eric D. Friedman
 * @author Rob Eden
 * @author Jeff Randall
 * @version $Id: _K__V_HashMap.template,v 1.1.2.16 2010/03/02 04:09:50 robeden Exp $
 */
public class TShortCharHashMap extends TShortCharHash implements TShortCharMap, Externalizable {
    static final long serialVersionUID = 1L;
    
    /**
     * the values of the map
     */
    protected transient char[] _values;
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance with the default
     * capacity and load factor.
     */
    public TShortCharHashMap() {
        super();
    }
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance with a prime
     * capacity equal to or greater than <tt>initialCapacity</tt> and
     * with the default load factor.
     *
     * @param initialCapacity an <code>int</code> value
     */
    public TShortCharHashMap(int initialCapacity) {
        super(initialCapacity);
    }
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance with a prime
     * capacity equal to or greater than <tt>initialCapacity</tt> and
     * with the specified load factor.
     *
     * @param initialCapacity an <code>int</code> value
     * @param loadFactor      a <code>float</code> value
     */
    public TShortCharHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance with a prime
     * capacity equal to or greater than <tt>initialCapacity</tt> and
     * with the specified load factor.
     *
     * @param initialCapacity an <code>int</code> value
     * @param loadFactor      a <code>float</code> value
     * @param noEntryKey      a <code>short</code> value that represents
     *                        <tt>null</tt> for the Key set.
     * @param noEntryValue    a <code>char</code> value that represents
     *                        <tt>null</tt> for the Value set.
     */
    public TShortCharHashMap(int initialCapacity, float loadFactor, short noEntryKey, char noEntryValue) {
        super(initialCapacity, loadFactor, noEntryKey, noEntryValue);
    }
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance containing
     * all of the entries in the map passed in.
     *
     * @param keys   a <tt>short</tt> array containing the keys for the matching values.
     * @param values a <tt>char</tt> array containing the values.
     */
    public TShortCharHashMap(short[] keys, char[] values) {
        super(Math.max(keys.length, values.length));
        
        int size = Math.min(keys.length, values.length);
        for(int i = 0; i < size; i++) {
            this.put(keys[i], values[i]);
        }
    }
    
    
    /**
     * Creates a new <code>TShortCharHashMap</code> instance containing
     * all of the entries in the map passed in.
     *
     * @param map a <tt>TShortCharMap</tt> that will be duplicated.
     */
    public TShortCharHashMap(TShortCharMap map) {
        super(map.size());
        if(map instanceof TShortCharHashMap) {
            TShortCharHashMap hashmap = (TShortCharHashMap)map;
            this._loadFactor = hashmap._loadFactor;
            this.no_entry_key = hashmap.no_entry_key;
            this.no_entry_value = hashmap.no_entry_value;
            //noinspection RedundantCast
            if(this.no_entry_key != (short)0) {
                Arrays.fill(_set, this.no_entry_key);
            }
            //noinspection RedundantCast
            if(this.no_entry_value != (char)0) {
                Arrays.fill(_values, this.no_entry_value);
            }
            setUp((int)Math.ceil(DEFAULT_CAPACITY / _loadFactor));
        }
        putAll(map);
    }
    
    /**
     * {@inheritDoc}
     */
    public char put(short key, char value) {
        int index = insertKey(key);
        return doPut(key, value, index);
    }
    
    
    /**
     * rehashes the map to the new capacity.
     *
     * @param newCapacity an <code>int</code> value
     */
    
    /**
     * {@inheritDoc}
     */
    public char putIfAbsent(short key, char value) {
        int index = insertKey(key);
        if(index < 0) {
            return _values[-index - 1];
        }
        return doPut(key, value, index);
    }
    
    /**
     * {@inheritDoc}
     */
    public void putAll(Map<? extends Short, ? extends Character> map) {
        ensureCapacity(map.size());
        // could optimize this for cases when map instanceof THashMap
        for(Map.Entry<? extends Short, ? extends Character> entry : map.entrySet()) {
            this.put(entry.getKey().shortValue(), entry.getValue().charValue());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void putAll(TShortCharMap map) {
        ensureCapacity(map.size());
        TShortCharIterator iter = map.iterator();
        while(iter.hasNext()) {
            iter.advance();
            this.put(iter.key(), iter.value());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public char get(short key) {
        int index = index(key);
        return index < 0 ? no_entry_value : _values[index];
    }
    
    /**
     * {@inheritDoc}
     */
    public char remove(short key) {
        char prev  = no_entry_value;
        int  index = index(key);
        if(index >= 0) {
            prev = _values[index];
            removeAt(index);    // clear key,state; adjust size
        }
        return prev;
    }
    
    /**
     * {@inheritDoc}
     */
    public TShortSet keySet() {
        return new TKeyView();
    }
    
    /**
     * {@inheritDoc}
     */
    public short[] keys() {
        short[] keys = new short[size()];
        if(keys.length == 0) {
            return keys;        // nothing to copy
        }
        short[] k      = _set;
        byte[]  states = _states;
        
        for(int i = k.length, j = 0; i-- > 0; ) {
            if(states[i] == FULL) {
                keys[j++] = k[i];
            }
        }
        return keys;
    }
    
    /**
     * {@inheritDoc}
     */
    public short[] keys(short[] array) {
        int size = size();
        if(size == 0) {
            return array;       // nothing to copy
        }
        if(array.length < size) {
            array = new short[size];
        }
        
        short[] keys   = _set;
        byte[]  states = _states;
        
        for(int i = keys.length, j = 0; i-- > 0; ) {
            if(states[i] == FULL) {
                array[j++] = keys[i];
            }
        }
        return array;
    }
    
    /**
     * {@inheritDoc}
     */
    public TCharCollection valueCollection() {
        return new TValueView();
    }
    
    /**
     * {@inheritDoc}
     */
    public char[] values() {
        char[] vals = new char[size()];
        if(vals.length == 0) {
            return vals;        // nothing to copy
        }
        char[] v      = _values;
        byte[] states = _states;
        
        for(int i = v.length, j = 0; i-- > 0; ) {
            if(states[i] == FULL) {
                vals[j++] = v[i];
            }
        }
        return vals;
    }
    
    /**
     * {@inheritDoc}
     */
    public char[] values(char[] array) {
        int size = size();
        if(size == 0) {
            return array;       // nothing to copy
        }
        if(array.length < size) {
            array = new char[size];
        }
        
        char[] v      = _values;
        byte[] states = _states;
        
        for(int i = v.length, j = 0; i-- > 0; ) {
            if(states[i] == FULL) {
                array[j++] = v[i];
            }
        }
        return array;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean containsValue(char val) {
        byte[] states = _states;
        char[] vals   = _values;
        
        for(int i = vals.length; i-- > 0; ) {
            if(states[i] == FULL && val == vals[i]) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean containsKey(short key) {
        return contains(key);
    }
    
    /**
     * {@inheritDoc}
     */
    public TShortCharIterator iterator() {
        return new TShortCharHashIterator(this);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean forEachKey(TShortProcedure procedure) {
        return forEach(procedure);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean forEachValue(TCharProcedure procedure) {
        byte[] states = _states;
        char[] values = _values;
        for(int i = values.length; i-- > 0; ) {
            if(states[i] == FULL && !procedure.execute(values[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean forEachEntry(TShortCharProcedure procedure) {
        byte[]  states = _states;
        short[] keys   = _set;
        char[]  values = _values;
        for(int i = keys.length; i-- > 0; ) {
            if(states[i] == FULL && !procedure.execute(keys[i], values[i])) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * {@inheritDoc}
     */
    public void transformValues(TCharFunction function) {
        byte[] states = _states;
        char[] values = _values;
        for(int i = values.length; i-- > 0; ) {
            if(states[i] == FULL) {
                values[i] = function.execute(values[i]);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean retainEntries(TShortCharProcedure procedure) {
        boolean modified = false;
        byte[]  states   = _states;
        short[] keys     = _set;
        char[]  values   = _values;
        
        
        // Temporarily disable compaction. This is a fix for bug #1738760
        tempDisableAutoCompaction();
        try {
            for(int i = keys.length; i-- > 0; ) {
                if(states[i] == FULL && !procedure.execute(keys[i], values[i])) {
                    removeAt(i);
                    modified = true;
                }
            }
        } finally {
            reenableAutoCompaction(true);
        }
        
        return modified;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean increment(short key) {
        return adjustValue(key, (char)1);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean adjustValue(short key, char amount) {
        int index = index(key);
        if(index < 0) {
            return false;
        } else {
            _values[index] += amount;
            return true;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public char adjustOrPutValue(short key, char adjust_amount, char put_amount) {
        int           index = insertKey(key);
        final boolean isNewMapping;
        final char    newValue;
        if(index < 0) {
            index = -index - 1;
            newValue = (_values[index] += adjust_amount);
            isNewMapping = false;
        } else {
            newValue = (_values[index] = put_amount);
            isNewMapping = true;
        }
        
        byte previousState = _states[index];
        
        if(isNewMapping) {
            postInsertHook(consumeFreeSlot);
        }
        
        return newValue;
    }
    
    private char doPut(short key, char value, int index) {
        char    previous     = no_entry_value;
        boolean isNewMapping = true;
        if(index < 0) {
            index = -index - 1;
            previous = _values[index];
            isNewMapping = false;
        }
        _values[index] = value;
        
        if(isNewMapping) {
            postInsertHook(consumeFreeSlot);
        }
        
        return previous;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return 0 == _size;
    }
    
    /**
     * {@inheritDoc}
     */
    public void clear() {
        super.clear();
        Arrays.fill(_set, 0, _set.length, no_entry_key);
        Arrays.fill(_values, 0, _values.length, no_entry_value);
        Arrays.fill(_states, 0, _states.length, FREE);
    }
    
    /**
     * {@inheritDoc}
     */
    protected void rehash(int newCapacity) {
        int oldCapacity = _set.length;
        
        short oldKeys[]   = _set;
        char  oldVals[]   = _values;
        byte  oldStates[] = _states;
        
        _set = new short[newCapacity];
        _values = new char[newCapacity];
        _states = new byte[newCapacity];
        
        for(int i = oldCapacity; i-- > 0; ) {
            if(oldStates[i] == FULL) {
                short o     = oldKeys[i];
                int   index = insertKey(o);
                _values[index] = oldVals[i];
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    protected void removeAt(int index) {
        _values[index] = no_entry_value;
        super.removeAt(index);  // clear key, state; adjust size
    }
    
    /**
     * initializes the hashtable to a prime capacity which is at least
     * <tt>initialCapacity + 1</tt>.
     *
     * @param initialCapacity an <code>int</code> value
     *
     * @return the actual capacity chosen
     */
    protected int setUp(int initialCapacity) {
        int capacity;
        
        capacity = super.setUp(initialCapacity);
        _values = new char[capacity];
        return capacity;
    }
    
    /**
     * {@inheritDoc}
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        // VERSION
        out.writeByte(0);
        
        // SUPER
        super.writeExternal(out);
        
        // NUMBER OF ENTRIES
        out.writeInt(_size);
        
        // ENTRIES
        for(int i = _states.length; i-- > 0; ) {
            if(_states[i] == FULL) {
                out.writeShort(_set[i]);
                out.writeChar(_values[i]);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // VERSION
        in.readByte();
        
        // SUPER
        super.readExternal(in);
        
        // NUMBER OF ENTRIES
        int size = in.readInt();
        setUp(size);
        
        // ENTRIES
        while(size-- > 0) {
            short key = in.readShort();
            char  val = in.readChar();
            put(key, val);
        }
    }
    
    /**
     * a view onto the keys of the map.
     */
    protected class TKeyView implements TShortSet {
        
        /**
         * {@inheritDoc}
         */
        public short getNoEntryValue() {
            return no_entry_key;
        }
        
        /**
         * {@inheritDoc}
         */
        public int size() {
            return _size;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean isEmpty() {
            return 0 == _size;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean contains(short entry) {
            return TShortCharHashMap.this.contains(entry);
        }
        
        /**
         * {@inheritDoc}
         */
        public TShortIterator iterator() {
            return new TShortCharKeyHashIterator(TShortCharHashMap.this);
        }
        
        /**
         * {@inheritDoc}
         */
        public short[] toArray() {
            return TShortCharHashMap.this.keys();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public short[] toArray(short[] dest) {
            return TShortCharHashMap.this.keys(dest);
        }
        
        
        /**
         * Unsupported when operating upon a Key Set view of a TShortCharMap
         * <p/>
         * {@inheritDoc}
         */
        public boolean add(short entry) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean remove(short entry) {
            return no_entry_value != TShortCharHashMap.this.remove(entry);
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(Collection<?> collection) {
            for(Object element : collection) {
                if(element instanceof Short) {
                    short ele = ((Short)element).shortValue();
                    if(!TShortCharHashMap.this.containsKey(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(TShortCollection collection) {
            TShortIterator iter = collection.iterator();
            while(iter.hasNext()) {
                if(!TShortCharHashMap.this.containsKey(iter.next())) {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(short[] array) {
            for(short element : array) {
                if(!TShortCharHashMap.this.contains(element)) {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * Unsupported when operating upon a Key Set view of a TShortCharMap
         * <p/>
         * {@inheritDoc}
         */
        public boolean addAll(Collection<? extends Short> collection) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * Unsupported when operating upon a Key Set view of a TShortCharMap
         * <p/>
         * {@inheritDoc}
         */
        public boolean addAll(TShortCollection collection) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * Unsupported when operating upon a Key Set view of a TShortCharMap
         * <p/>
         * {@inheritDoc}
         */
        public boolean addAll(short[] array) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings({"SuspiciousMethodCalls"})
        public boolean retainAll(Collection<?> collection) {
            boolean        modified = false;
            TShortIterator iter     = iterator();
            while(iter.hasNext()) {
                if(!collection.contains(Short.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean retainAll(TShortCollection collection) {
            if(this == collection) {
                return false;
            }
            boolean        modified = false;
            TShortIterator iter     = iterator();
            while(iter.hasNext()) {
                if(!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean retainAll(short[] array) {
            boolean changed = false;
            Arrays.sort(array);
            short[] set    = _set;
            byte[]  states = _states;
            
            for(int i = set.length; i-- > 0; ) {
                if(states[i] == FULL && (Arrays.binarySearch(array, set[i]) < 0)) {
                    removeAt(i);
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for(Object element : collection) {
                if(element instanceof Short) {
                    short c = ((Short)element).shortValue();
                    if(remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(TShortCollection collection) {
            if(this == collection) {
                clear();
                return true;
            }
            boolean        changed = false;
            TShortIterator iter    = collection.iterator();
            while(iter.hasNext()) {
                short element = iter.next();
                if(remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(short[] array) {
            boolean changed = false;
            for(int i = array.length; i-- > 0; ) {
                if(remove(array[i])) {
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public void clear() {
            TShortCharHashMap.this.clear();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean forEach(TShortProcedure procedure) {
            return TShortCharHashMap.this.forEachKey(procedure);
        }
        
        @Override
        public int hashCode() {
            int hashcode = 0;
            for(int i = _states.length; i-- > 0; ) {
                if(_states[i] == FULL) {
                    hashcode += HashFunctions.hash(_set[i]);
                }
            }
            return hashcode;
        }
        
        @Override
        public boolean equals(Object other) {
            if(!(other instanceof TShortSet)) {
                return false;
            }
            final TShortSet that = (TShortSet)other;
            if(that.size() != this.size()) {
                return false;
            }
            for(int i = _states.length; i-- > 0; ) {
                if(_states[i] == FULL) {
                    if(!that.contains(_set[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            forEachKey(new TShortProcedure() {
                private boolean first = true;
                
                
                public boolean execute(short key) {
                    if(first) {
                        first = false;
                    } else {
                        buf.append(", ");
                    }
                    
                    buf.append(key);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }
    }
    
    /**
     * a view onto the values of the map.
     */
    protected class TValueView implements TCharCollection {
        
        /**
         * {@inheritDoc}
         */
        public char getNoEntryValue() {
            return no_entry_value;
        }
        
        /**
         * {@inheritDoc}
         */
        public int size() {
            return _size;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean isEmpty() {
            return 0 == _size;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean contains(char entry) {
            return TShortCharHashMap.this.containsValue(entry);
        }
        
        /**
         * {@inheritDoc}
         */
        public TCharIterator iterator() {
            return new TShortCharValueHashIterator(TShortCharHashMap.this);
        }
        
        /**
         * {@inheritDoc}
         */
        public char[] toArray() {
            return TShortCharHashMap.this.values();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public char[] toArray(char[] dest) {
            return TShortCharHashMap.this.values(dest);
        }
        
        
        public boolean add(char entry) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean remove(char entry) {
            char[]  values = _values;
            short[] set    = _set;
            
            for(int i = values.length; i-- > 0; ) {
                if((set[i] != FREE && set[i] != REMOVED) && entry == values[i]) {
                    removeAt(i);
                    return true;
                }
            }
            return false;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(Collection<?> collection) {
            for(Object element : collection) {
                if(element instanceof Character) {
                    char ele = ((Character)element).charValue();
                    if(!TShortCharHashMap.this.containsValue(ele)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(TCharCollection collection) {
            TCharIterator iter = collection.iterator();
            while(iter.hasNext()) {
                if(!TShortCharHashMap.this.containsValue(iter.next())) {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean containsAll(char[] array) {
            for(char element : array) {
                if(!TShortCharHashMap.this.containsValue(element)) {
                    return false;
                }
            }
            return true;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean addAll(Collection<? extends Character> collection) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean addAll(TCharCollection collection) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean addAll(char[] array) {
            throw new UnsupportedOperationException();
        }
        
        
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings({"SuspiciousMethodCalls"})
        public boolean retainAll(Collection<?> collection) {
            boolean       modified = false;
            TCharIterator iter     = iterator();
            while(iter.hasNext()) {
                if(!collection.contains(Character.valueOf(iter.next()))) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean retainAll(TCharCollection collection) {
            if(this == collection) {
                return false;
            }
            boolean       modified = false;
            TCharIterator iter     = iterator();
            while(iter.hasNext()) {
                if(!collection.contains(iter.next())) {
                    iter.remove();
                    modified = true;
                }
            }
            return modified;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean retainAll(char[] array) {
            boolean changed = false;
            Arrays.sort(array);
            char[] values = _values;
            byte[] states = _states;
            
            for(int i = values.length; i-- > 0; ) {
                if(states[i] == FULL && (Arrays.binarySearch(array, values[i]) < 0)) {
                    removeAt(i);
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(Collection<?> collection) {
            boolean changed = false;
            for(Object element : collection) {
                if(element instanceof Character) {
                    char c = ((Character)element).charValue();
                    if(remove(c)) {
                        changed = true;
                    }
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(TCharCollection collection) {
            if(this == collection) {
                clear();
                return true;
            }
            boolean       changed = false;
            TCharIterator iter    = collection.iterator();
            while(iter.hasNext()) {
                char element = iter.next();
                if(remove(element)) {
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean removeAll(char[] array) {
            boolean changed = false;
            for(int i = array.length; i-- > 0; ) {
                if(remove(array[i])) {
                    changed = true;
                }
            }
            return changed;
        }
        
        
        /**
         * {@inheritDoc}
         */
        public void clear() {
            TShortCharHashMap.this.clear();
        }
        
        
        /**
         * {@inheritDoc}
         */
        public boolean forEach(TCharProcedure procedure) {
            return TShortCharHashMap.this.forEachValue(procedure);
        }
        
        
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder("{");
            forEachValue(new TCharProcedure() {
                private boolean first = true;
                
                public boolean execute(char value) {
                    if(first) {
                        first = false;
                    } else {
                        buf.append(", ");
                    }
                    
                    buf.append(value);
                    return true;
                }
            });
            buf.append("}");
            return buf.toString();
        }
    }
    
    class TShortCharKeyHashIterator extends THashPrimitiveIterator implements TShortIterator {
        
        /**
         * Creates an iterator over the specified map
         *
         * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
         */
        TShortCharKeyHashIterator(TPrimitiveHash hash) {
            super(hash);
        }
        
        /**
         * {@inheritDoc}
         */
        public short next() {
            moveToNextIndex();
            return _set[_index];
        }
        
        /** @{inheritDoc} */
        public void remove() {
            if(_expectedSize != _hash.size()) {
                throw new ConcurrentModificationException();
            }
            
            // Disable auto compaction during the remove. This is a workaround for bug 1642768.
            try {
                _hash.tempDisableAutoCompaction();
                TShortCharHashMap.this.removeAt(_index);
            } finally {
                _hash.reenableAutoCompaction(false);
            }
            
            _expectedSize--;
        }
    }
    
    class TShortCharValueHashIterator extends THashPrimitiveIterator implements TCharIterator {
        
        /**
         * Creates an iterator over the specified map
         *
         * @param hash the <tt>TPrimitiveHash</tt> we will be iterating over.
         */
        TShortCharValueHashIterator(TPrimitiveHash hash) {
            super(hash);
        }
        
        /**
         * {@inheritDoc}
         */
        public char next() {
            moveToNextIndex();
            return _values[_index];
        }
        
        /** @{inheritDoc} */
        public void remove() {
            if(_expectedSize != _hash.size()) {
                throw new ConcurrentModificationException();
            }
            
            // Disable auto compaction during the remove. This is a workaround for bug 1642768.
            try {
                _hash.tempDisableAutoCompaction();
                TShortCharHashMap.this.removeAt(_index);
            } finally {
                _hash.reenableAutoCompaction(false);
            }
            
            _expectedSize--;
        }
    }
    
    class TShortCharHashIterator extends THashPrimitiveIterator implements TShortCharIterator {
        
        /**
         * Creates an iterator over the specified map
         *
         * @param map the <tt>TShortCharHashMap</tt> we will be iterating over.
         */
        TShortCharHashIterator(TShortCharHashMap map) {
            super(map);
        }
        
        /**
         * {@inheritDoc}
         */
        public void advance() {
            moveToNextIndex();
        }
        
        /**
         * {@inheritDoc}
         */
        public short key() {
            return _set[_index];
        }
        
        /**
         * {@inheritDoc}
         */
        public char value() {
            return _values[_index];
        }
        
        /**
         * {@inheritDoc}
         */
        public char setValue(char val) {
            char old = value();
            _values[_index] = val;
            return old;
        }
        
        /** @{inheritDoc} */
        public void remove() {
            if(_expectedSize != _hash.size()) {
                throw new ConcurrentModificationException();
            }
            // Disable auto compaction during the remove. This is a workaround for bug 1642768.
            try {
                _hash.tempDisableAutoCompaction();
                TShortCharHashMap.this.removeAt(_index);
            } finally {
                _hash.reenableAutoCompaction(false);
            }
            _expectedSize--;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object other) {
        if(!(other instanceof TShortCharMap)) {
            return false;
        }
        TShortCharMap that = (TShortCharMap)other;
        if(that.size() != this.size()) {
            return false;
        }
        char[] values              = _values;
        byte[] states              = _states;
        char   this_no_entry_value = getNoEntryValue();
        char   that_no_entry_value = that.getNoEntryValue();
        for(int i = values.length; i-- > 0; ) {
            if(states[i] == FULL) {
                short key        = _set[i];
                char  that_value = that.get(key);
                char  this_value = values[i];
                if((this_value != that_value) && (this_value != this_no_entry_value) && (that_value != that_no_entry_value)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int    hashcode = 0;
        byte[] states   = _states;
        for(int i = _values.length; i-- > 0; ) {
            if(states[i] == FULL) {
                hashcode += HashFunctions.hash(_set[i]) ^ HashFunctions.hash(_values[i]);
            }
        }
        return hashcode;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder("{");
        forEachEntry(new TShortCharProcedure() {
            private boolean first = true;
            
            public boolean execute(short key, char value) {
                if(first) {
                    first = false;
                } else {
                    buf.append(", ");
                }
                
                buf.append(key);
                buf.append("=");
                buf.append(value);
                return true;
            }
        });
        buf.append("}");
        return buf.toString();
    }
    
    
} // TShortCharHashMap
