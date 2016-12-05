/*
 * Henrik Thulin heth7132
 * Mattin Lotfi malo5163
 */

package alda.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Node<T> {

	private static final AtomicLong ids = new AtomicLong(0);		
	private long id; 
		
	private T data;
	private boolean _isMarked = false;

	private Map<Node<T>, Integer> connections = new HashMap<>();

	public Node(T data) {
		
		this.id = ids.incrementAndGet();
		this.data = data;
	}

	public void addConnection(Node<T> to, int cost) {
		connections.put(to, cost);
	}

	public boolean isConnected(Node<T> to) {
		return connections.containsKey(to);
	}

	public int getCost(Node<T> to) {

		if (connections.containsKey(to))
			return connections.get(to);
		else
			return -1;
	}


	public Map<Node<T>, Integer> getConnections() {
		return connections;
	}

	public void setMarked(boolean marked) {		
		this._isMarked = marked;
	}

	public boolean isMarked() {
		return this._isMarked;
	}

	public boolean isConnectionsMarked() {

		for (Node<T> n : getConnections().keySet())
			if (n.isMarked())
				return true;

		return false;
	}
	
	public T getData() {
		return this.data;
	}
	
	public long getID() {
		return this.id;
	}
}
