package Lista;

import java.util.Iterator;



public class ElementIterator<E> implements Iterator<E> {

	//Atributos de instacia
	protected Position<E> cursor;
	protected PositionList<E> lista;

	//Constructor

	public ElementIterator(PositionList<E> L) {
		lista=L;
		try {
			if(lista.isEmpty())
				cursor=null;
			else
				cursor=L.first();
		}
		catch(EmptyListException e)
		{
			System.out.println("La lista est� vacia");
		}
	}
	public boolean hasNext() {
		return cursor !=null;
	}

	public E next() {
		E toReturn=cursor.element();
		try {
			if(cursor!=lista.last())
				cursor=lista.next(cursor);
			else
				cursor=null;
		}
		catch(InvalidPositionException | BoundaryViolationException | EmptyListException e){
		}
		return toReturn;
	}
	
}	
