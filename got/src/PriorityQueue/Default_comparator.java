package PriorityQueue;

import java.util.Comparator;

public class Default_comparator<K extends Comparable<K>> implements Comparator<K> {
	public int compare(K o2, K o1) {
		return o1.compareTo(o2);
	}
}


