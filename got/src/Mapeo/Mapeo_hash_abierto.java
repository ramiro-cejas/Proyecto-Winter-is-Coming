package Mapeo;

import java.util.Comparator;
import java.util.Iterator;

import Lista.BoundaryViolationException;
import Lista.EmptyListException;
import Lista.InvalidPositionException;
import Lista.DoubleLinkedList;
import Lista.Position;
import Lista.PositionList;

public class Mapeo_hash_abierto<K, V> implements Map<K, V> {

	private PositionList<Entrada<K, V>>[] arregloB;
	private int N;
	private final double factorC = 0.9f;
	private int n;

	public Mapeo_hash_abierto() {
		N = 13;
		n = 0;
		arregloB = (PositionList<Entrada<K, V>>[]) new DoubleLinkedList[N];
		for (int i = 0; i < N; i++)
			arregloB[i] = new DoubleLinkedList<Entrada<K, V>>();
	}

	@Override
	public int size() {
		return n;
	}

	@Override
	public boolean isEmpty() {
		return n == 0;
	}

	public String toString() {
		String hashstring = "";
		for (Entry<K, V> entry : this.entries()) {
			hashstring = hashstring + (" " + entry.getKey() + ", {" + entry.getValue() + "} | "); // ELIMINAR YA
		}
		return hashstring;
	}

	@Override
	public V get(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("Clave nula");
		V valor = null;
		PositionList<Entrada<K, V>> l = arregloB[funcionHash(key)];
		Iterator<Entrada<K, V>> it = l.iterator();
		boolean encontre = false;
		Entrada<K, V> e = null;
		while (it.hasNext() && !encontre) {
			e = it.next();
			if (e.getKey().equals(key)) {
				encontre = true;
				valor = e.getValue();
			}
		}
		return valor;
	}

	@Override
	public V put(K key, V value) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("Clave nula");
		if (n / N > factorC)
			reHash();
		V valor = null;
		boolean encontre = false;
		PositionList<Entrada<K, V>> l = arregloB[funcionHash(key)];
		Iterator<Entrada<K, V>> it = l.iterator();
		Entrada<K, V> cursor = null;
		while (it.hasNext() && !encontre) {
			cursor = it.next();
			if (cursor.getKey().equals(key)) {
				encontre = true;
				valor = cursor.getValue();
				cursor.setValue(value);
			}
		}
		if (!encontre) {
			Entrada<K, V> entrada = new Entrada<K, V>(key, value);
			l.addLast(entrada);
			n++;
		}
		return valor;
	}

	@Override
	public V remove(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("Clave nula");
		boolean encontre = false;
		V toReturn = null;
		try {
			Position<Entrada<K, V>> pos = arregloB[funcionHash(key)].first();
			while (!encontre && pos != null) {
				if (pos.element().getKey().equals(key)) {
					toReturn = pos.element().getValue();
					arregloB[funcionHash(key)].remove(pos);
					encontre = true;
					n--;
				} else {
					if (pos == arregloB[funcionHash(key)].last())
						pos = null;
					else
						pos = arregloB[funcionHash(key)].next(pos);
				}
			}
		} catch (EmptyListException | InvalidPositionException | BoundaryViolationException q) {

		}
		return toReturn;
	}

	@Override
	public Iterable<K> keys() {
		PositionList<K> l = new DoubleLinkedList<K>();
		for (int i = 0; i < N; i++)
			for (Entrada<K, V> e : arregloB[i])
				l.addLast(e.getKey());
		return l;
	}

	@Override
	public Iterable<V> values() {
		PositionList<V> l = new DoubleLinkedList<V>();
		for (int i = 0; i < N; i++)
			for (Entrada<K, V> e : arregloB[i])
				l.addLast(e.getValue());
		return l;
	}

	@Override
	public Iterable<Entry<K, V>> entries() {
		PositionList<Entry<K, V>> l = new DoubleLinkedList<Entry<K, V>>();
		for (int i = 0; i < N; i++)
			for (Entrada<K, V> e : arregloB[i])
				l.addLast(e);
		return l;
	}



	private int funcionHash(K key) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("Clave nula");
		return (Math.abs(key.hashCode() % N));
	}

	private int nextPrimo(int n) {
		boolean es = false;
		n++;
		while (!es) {
			if (esPrimo(n))
				es = true;
			else
				n++;

		}
		return n;
	}

	private boolean esPrimo(int n) {
		boolean es = false;
		int divisor = 2;
		while (divisor < n && !es) {
			if (n % divisor == 0)
				es = true;
			else
				divisor++;
		}

		return es;
	}

	private void reHash() {
		Iterable<Entry<K, V>> entradas = entries();
		N = nextPrimo(N);
		n = 0;
		arregloB = new PositionList[N * 2];
		for (int i = 0; i < N; i++)
			arregloB[i] = new DoubleLinkedList();

		try {
			for (Entry<K, V> e : entradas)
				this.put(e.getKey(), e.getValue());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}

}
