package PriorityQueue;

import java.util.Comparator;

public class PriorityQ_heap<K, V>  {

	protected Entrada<K, V>[] elems;
	protected Comparator<K> comp;
	protected int cant;
	protected static int maxElems = 100;

	public PriorityQ_heap() {
		elems = (Entrada<K, V>[]) new Entrada[maxElems];
		cant = 0;
		comp = new Default_comparator();
	}

	public PriorityQ_heap(Comparator<K> comp) {
		elems = (Entrada<K, V>[]) new Entrada[maxElems];
		this.comp = comp;
		cant = 0;
	}

	public int size() {
		return cant;
	}

	public boolean isEmpty() {
		return cant == 0;
	}

	public Entrada<K, V> min() throws EmptyPriorityQueueException {
		if (cant == 0)
			throw new EmptyPriorityQueueException("No se puede pedir el min de una cola vacía");
		return elems[1];
	}

	public Entrada<K, V> insert(K key, V value) throws InvalidKeyException {
		if (key == null)
			throw new InvalidKeyException("La clave es inválida");
		if (cant == elems.length - 1) {
			Entrada<K, V>[] nuevo = (Entrada<K, V>[]) new Entrada[elems.length * 2];
			for (int i = 1; i < elems.length; i++)
				nuevo[i] = elems[i];
			elems = nuevo;
		}

		Entrada<K, V> entrada = new Entrada<K, V>(key, value);
		cant++;
		elems[cant] = entrada;
		int i = cant;
		boolean seguir = true;
		Entrada<K, V> actual = null;
		Entrada<K, V> padre = null;
		Entrada<K, V> aux = null;
		while (i > 1 && seguir) {
			actual = elems[i];
			padre = elems[i / 2];
			if (comp.compare(actual.getKey(), padre.getKey()) < 0) {
				aux = elems[i];
				elems[i] = elems[i / 2];
				elems[i / 2] = aux;
				i = i / 2;
			} else
				seguir = false;
		}
		return entrada;
	}

	public Entrada<K, V> removeMin() throws EmptyPriorityQueueException {
		if (cant == 0)
			throw new EmptyPriorityQueueException("Cola vacía");
		int menor = 0;
		Entrada<K, V> nuevo = elems[1];
		if (cant == 1) {
			elems[1] = null;
			cant--;
		} else {
			elems[1] = elems[cant];
			elems[cant] = null;
			cant--;
			int i = 1;
			boolean ordenado = false;
			while (!ordenado) {
				int hijoI = i * 2;
				int hijoD = (i * 2) + 1;
				boolean hasLC = hijoI <= cant;
				boolean hasRC = hijoD <= cant;
				if (!hasLC)
					ordenado = true;
				else {
					if (hasRC) {
						if (comp.compare(elems[hijoI].getKey(), elems[hijoD].getKey()) < 0)
							menor = hijoI;
						else
							menor = hijoD;
					} else
						menor = hijoI;
					if (comp.compare(elems[i].getKey(), elems[menor].getKey()) > 0) {
						Entrada<K, V> auxiliar = elems[i];
						elems[i] = elems[menor];
						elems[menor] = (Entrada<K, V>) auxiliar;
						i = menor;
					} else
						ordenado = true;
				}
			}
		}

		return nuevo;
	}
}
