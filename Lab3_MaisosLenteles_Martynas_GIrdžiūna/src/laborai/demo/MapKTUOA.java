/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package laborai.demo;

import java.util.ArrayList;
import laborai.studijosktu.*;
import laborai.studijosktu.HashType;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Martynas
 */

public class MapKTUOA<K, V> implements MapADT<K, V>{
    
        // Maišos lentelė
    protected Entry<K, V>[] table;
    // Lentelėje esančių raktas-reikšmė porų kiekis
    protected int size = 0;
    // Apkrovimo faktorius
    protected float loadFactor;
    // Maišos metodas
    protected HashType ht;
    
    protected int rehashesCounter = 0;
    
    
    public static final int DEFAULT_INITIAL_CAPACITY = 20;
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    public static final HashType DEFAULT_HASH_TYPE = HashType.DIVISION;
    
    public MapKTUOA() {
        this(DEFAULT_HASH_TYPE);
    }

    public MapKTUOA(HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, ht);
    }

    public MapKTUOA(int initialCapacity, HashType ht) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, ht);
    }

    public MapKTUOA(float loadFactor, HashType ht) {
        this(DEFAULT_INITIAL_CAPACITY, loadFactor, ht);
    }

    public MapKTUOA(int initialCapacity, float loadFactor, HashType ht) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }

        if ((loadFactor <= 0.0) || (loadFactor > 1.0)) {
            throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
        }

        this.table = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.ht = ht;
    }
    
    @Override
    public boolean isEmpty()
    {
        if (size > 0)
        {
            return false;
        }
        return true;
    }

    /**
     * Grąžinamas atvaizdyje esančių porų kiekis.
     *
     * @return Grąžinamas atvaizdyje esančių porų kiekis.
     */
    @Override
    public int size()
    {
        return this.size;
    }

    /**
     * Išvalomas atvaizdis.
     *
     */
    @Override
    public void clear()
    {
        Arrays.fill(table, null);
        size = 0;
        rehashesCounter = 0;
    }

    @Override
    public String[][] toArray()
    {
        String[][] result = new String[table.length][];
        int count = 0;
        for (Entry<K, V> n : table)
        {
            result[count++][0] = (String) n.value;
        }
        return result;
    }
    
    public boolean containsValue(Object value)
    {
        if (!isEmpty()){
            for (Entry<K, V> n : table)
            {
                if (n.value.equals(value))
                    {
                        return true;
                    }
                }
            }
        return false;
    }
    
    public V putIfAbsent(K key, V value)
    {
        V searchedValue = get(key);
        if (searchedValue.equals(null)) 
        {
            put(key, value);
            return null;
        }
        return searchedValue;
    }
    
    public int numberOfEmpties()
    {
        int counter = 0;
        if (!isEmpty())
        {
            for (Entry<K, V> n : table)
            {
                if (n.value.equals(null))
                    {
                        counter++;
                    }
            }
        }
        return counter;
    }
    
   public List<V> values()
   {
       List<V> allValues = new ArrayList();
       if (!isEmpty())
               {
                   for (int i = 0; i < table.length; i++)
                   {
                           if (table[i] != null)
                           {
                               allValues.add(table[i].value);
                           }
                   }
               }
        return allValues;
   }
   
   public Set<K> keySet()
   {
       Set<K> allSet = new HashSet<K>();
       if (!isEmpty())
       {
            for (int i = 0; i < table.length; i++)
            {
                if (table[i] != null)
                {
                    allSet.add(table[i].key);
                }
            }
       }
       return allSet;
   }

    /**
     * Atvaizdis papildomas nauja pora.
     *
     * @param key raktas,
     * @param value reikšmė.
     * @return Grąžinama atvaizdžio poros reikšmė.
     */
    @Override
    public V put(K key, V value)
    {
        if (key == null || value == null)
        {
            throw new NullPointerException();
        }
        int index = findPosition(key);
        if (index == -1) 
        {
            rehash();
            put(key, value);
            return value;
        }
        if (table[index] == null)
        {
            table[index] = new Entry(key, value);
            size++;
            if (size > table.length * loadFactor)
            {
                rehash();
            }
        }
        else 
        { 
            table[index].value = value;
        }
        return value;
    }

    /**
     * Grąžinama atvaizdžio poros reikšmė.
     *
     * @param key raktas.
     * @return Grąžinama atvaizdžio poros reikšmė.
     */
    @Override
    public V get(K key)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("Key is null" + "in get(Key key");
        }
        int index = findPosition(key);
        if (index != -1)
        {
            return table[index].value;
        }
        return null;
    }

    /**
     * Iš atvaizdžio pašalinama pora.
     *
     * @param key raktas.
     * @return Grąžinama pašalinta atvaizdžio poros reikšmė.
     */
    @Override
     public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        int index = findPosition(key);
        if (contains(key)) {
            V delete = table[index].value;
            table[index] = new Entry<>(null,null);
            size--;
            return delete;
        }
        return null;

    }

    
    public int hash(K key, HashType hashType) {
        int h = key.hashCode();
        switch (hashType) {
            case DIVISION:
                return Math.abs(h) % table.length;
            case MULTIPLICATION:
                double k = (Math.sqrt(5) - 1) / 2;
                return (int) (((k * Math.abs(h)) % 1) * table.length);
            case JCF7:
                h ^= (h >>> 20) ^ (h >>> 12);
                h = h ^ (h >>> 7) ^ (h >>> 4);
                return h & (table.length - 1);
            case JCF8:
                h = h ^ (h >>> 16);
                return h & (table.length - 1);
            default:
                return Math.abs(h) % table.length;
        }
    }
    
    private void rehash() {
        MapKTUOA mapKTUOA
                = new MapKTUOA(table.length * 2, loadFactor, ht);
        for (Entry<K, V> node : table) {
            if (node != null) {
                mapKTUOA.put(node.key, node.value);
            }
        }
        table = mapKTUOA.table;
        rehashesCounter++;
    }
    
    public int getRehashesCounter() {
        return rehashesCounter;
    }

    /**
     * Grąžina maišos lentelės talpą.
     *
     * @return Maišos lentelės talpa.
     */
    public int getTableCapacity() {
        return table.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Entry<K, V> node : table) {
            if (node != null) {
                sb.append(node).append("\n");
            }
        }
        return sb.toString();
    }
    /**
     * Patikrinama ar atvaizdyje egzistuoja pora su raktu key.
     *
     * @param key raktas.
     * @return true, jei atvaizdyje egzistuoja pora su raktu key, kitu atveju -
     * false
     */
    public int findPosition(K key)
    {
        int index = hash(key, ht);
        int index0 = index;
        int i = 0;

        for (int j = 0; j < table.length; j++)
        {
            //System.out.println(table[index].key.equals(key));
            if (table[index] == null || table[index].key == null || table[index].key.equals(key))
            {
                return index;
            }
            i++;
            index = (index0 + i*i) % table.length;
        }
        return -1;
    }
    
    @Override
    public boolean contains(K key)
    {
        if (key == null)
        {
            throw new NullPointerException();
        }
        int index = findPosition(key);
        if (index != -1)
        {
            return true;
        }
        return false;
    }
    
        class Entry<K, V> 
    {

        // Raktas        
        protected K key;
        // Reikšmė
        protected V value;

        protected Entry() {
        }

        protected Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
}
