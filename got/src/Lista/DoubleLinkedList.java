package Lista;

import java.util.Iterator;

public class DoubleLinkedList<E> implements PositionList<E> {
	private DNodo<E> inicio;
	private DNodo<E> fin;
	private int cant;

	public DoubleLinkedList() {
		inicio = new DNodo<E>(null, null, null);
		fin = new DNodo<E>(null, null, null);
		cant = 0;
		inicio.setSiguiente(fin);
		fin.setAnterior(inicio);
	}

	public int size() {
		return cant;
	}

	public boolean isEmpty() {
		return cant == 0;
	}

	public Position<E> first() throws EmptyListException {
		if (cant == 0)
			throw new EmptyListException("La lista está vacia");
		return inicio.getSiguiente();
	}

	public Position<E> last() throws EmptyListException {
		if (cant == 0)
			throw new EmptyListException("La lista está vacia");
		return fin.getAnterior();
	}

	public Position<E> next(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNodo<E> n = checkPosition(p);
		if (n.getSiguiente() == fin)
			throw new BoundaryViolationException("No se puede pedir el siguiente del fin");
		return n.getSiguiente();
	}

	public Position<E> prev(Position<E> p) throws InvalidPositionException, BoundaryViolationException {
		DNodo<E> n = checkPosition(p);
		if (n.getAnterior() == inicio)
			throw new BoundaryViolationException("No se puede pedir el anterior del inicio");
		return n.getAnterior();
	}

	public void addFirst(E element) {
		DNodo<E> nuevo = new DNodo<E>(element, inicio, inicio.getSiguiente());
		inicio.getSiguiente().setAnterior(nuevo);
		inicio.setSiguiente(nuevo);
		cant++;
	}

	public void addLast(E element) {
		DNodo<E> nuevo = new DNodo<E>(element, fin.getAnterior(), fin);
		fin.getAnterior().setSiguiente(nuevo);
		fin.setAnterior(nuevo);
		cant++;
	}

	public void addAfter(Position<E> p, E element) throws InvalidPositionException {
		DNodo<E> n = checkPosition(p);
		DNodo<E> nuevo = new DNodo<E>(element, n, n.getSiguiente());
		n.getSiguiente().setAnterior(nuevo);
		n.setSiguiente(nuevo);
		cant++;
	}

	public void addBefore(Position<E> p, E element) throws InvalidPositionException {
		DNodo<E> n = checkPosition(p);
		DNodo<E> nuevo = new DNodo<E>(element, n.getAnterior(), n);
		n.getAnterior().setSiguiente(nuevo);
		n.setAnterior(nuevo);
		cant++;
	}

	public E remove(Position<E> p) throws InvalidPositionException {
		DNodo<E> n = checkPosition(p);
		E x = n.element();
		n.getAnterior().setSiguiente(n.getSiguiente());
		n.getSiguiente().setAnterior(n.getAnterior());
		n.setSiguiente(null);
		n.setAnterior(null);
		cant--;
		return x;
	}

	public E set(Position<E> p, E element) throws InvalidPositionException {
		DNodo<E> n = checkPosition(p);
		E x = n.element();
		n.setElemento(element);
		return x;
	}

	public Iterator<E> iterator() {
		return new ElementIterator(this);
	}

	public Iterable<Position<E>> positions() {
		PositionList<Position<E>> l = new DoubleLinkedList<Position<E>>();
		if (!isEmpty()) {
			DNodo<E> cursor = inicio.getSiguiente();
			boolean seguir = true;
			while (seguir) {
				l.addLast(cursor);
				if (cursor == fin.getAnterior())
					seguir = false;
				else
					cursor = cursor.getSiguiente();
			}
		}
		return l;
	}

	private DNodo<E> checkPosition(Position<E> p) throws InvalidPositionException {

		try {

			if (p == null)
				throw new InvalidPositionException("Posicion nula.");

			if (p == inicio)
				throw new InvalidPositionException("El nodo inicio no es una posicion valida.");

			if (p == fin)
				throw new InvalidPositionException("El nodo fin no es una posicion valida.");

			if (this.isEmpty())
				throw new InvalidPositionException("Lista vacia.");

			return ((DNodo<E>) p);

		} catch (ClassCastException e) {

			throw new InvalidPositionException("Posicion invalida.");
		}
	}

	public void addBeforeConditional(Position<E> p, E e, E x) throws InvalidPositionException {

		if (p == null)
			throw new InvalidPositionException("Posicion nula.");

		DNodo<E> nuevo = new DNodo<E>(e, null, null);
		DNodo<E> n;
		try {
			n = ((DNodo<E>) p);
			if (cant > 1 && n.getSiguiente() != null && n.getSiguiente().element() != null) { //
				if (n.getSiguiente().element().equals(x)) {
					nuevo.setAnterior(n.getAnterior());
					nuevo.setSiguiente(n);
					n.getAnterior().setSiguiente(nuevo);
					n.setAnterior(nuevo);
					cant++;
				}
			}
		} catch (ClassCastException e1) {
			throw new InvalidPositionException("Posicion invalida.");
		}
	}

	public String toString() {
		String ret = new String(" ");
		DNodo<E> h = inicio.getSiguiente();
		while (h.getSiguiente() != null) {
			ret += h.element() + " ";
			h = h.getSiguiente();
		}
		return ret;
	}

//Metodos practico -......................................................................................-
	public void invertir() {// Ej 7
		DNodo<E> cursorD = new DNodo<E>(inicio.getSiguiente().element(), inicio, inicio.getSiguiente().getSiguiente()); // c1
		DNodo<E> cursorT = new DNodo<E>(fin.getAnterior().element(), fin.getAnterior().getAnterior(), fin);// c2
		for (int i = 0; i < cant / 2; i++) {// n
			E aux = cursorD.element();// c3
			cursorD.setElemento(cursorT.element());// c4
			cursorT.setElemento(aux);// c5
			cursorD = cursorD.getSiguiente();// c6
			cursorT = cursorT.getAnterior();// c7
		}

	}
	// t(n)= c1+c2+n(c3+c4+c5+c6+c7), es del orden del mayor es O(n).

	public PositionList<E> zigzag() {// Ej 10
		PositionList<E> l = new DoubleLinkedList<E>();// c1
		DNodo<E> cursorD = inicio.getSiguiente();// c2
		DNodo<E> cursorT = fin.getAnterior();// c3
		int x = size() / 2;// c4
		int contador = 0;// c5
		while (contador < x) {// n
			l.addLast(cursorD.element());// c6
			l.addLast(cursorT.element());// c7
			contador++;// c8
			cursorD = cursorD.getSiguiente();// c9
			cursorT = cursorT.getAnterior();// c10
			if (cursorD.element().equals(cursorT.element()))
				l.addLast(cursorD.element());// c11
		}
		return l;// c12
	}

	// t(n)=c1+c2+c3+c4+c5+n(c6+c7+c8+c9+c10+c11)+c12, es del orden del mayor, es
	// O(n).

	public void contactenar(PositionList<E> l) {
		Position<E> cursor = null;
		if (!l.isEmpty()) {
			Iterator<Position<E>> it = l.positions().iterator();
			while (it.hasNext()) {
				cursor = it.next();
				this.addLast(cursor.element());
			}
		}
	}

}
