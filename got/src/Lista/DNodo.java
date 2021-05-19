package Lista;

public class DNodo<E> implements Position<E>{
	//Atributos de instancia
	protected E elemento;
	protected DNodo<E> anterior,siguiente;
	
	//Constructor
	public DNodo(E elem,DNodo<E> anterior,DNodo<E> siguiente) {
		elemento=elem;
		this.anterior=anterior;
		this.siguiente=siguiente;
	}
	
	
	//Metodos
	
	public E element() {
		return elemento;
	}
	public void setElemento(E elemento) {
		this.elemento = elemento;
	}
	public DNodo<E> getAnterior() {
		return anterior;
	}
	public void setAnterior(DNodo<E> anterior) {
		this.anterior = anterior;
	}
	public DNodo<E> getSiguiente() {
		return siguiente;
	}
	public void setSiguiente(DNodo<E> siguiente) {
		this.siguiente = siguiente;
	}
	

}