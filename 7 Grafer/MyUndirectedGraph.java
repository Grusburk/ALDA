/*
 * Henrik Thulin heth7132
 * Mattin Lotfi malo5163
 */

package alda.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.Queue;

public class MyUndirectedGraph<T> implements UndirectedGraph<T> {

	Map<T, Node<T>> nodes = new HashMap<>();

	public MyUndirectedGraph() {

	}

	@Override
	public int getNumberOfNodes() {
		//
		return nodes.values().size();
	}

	@Override
	public int getNumberOfEdges() {

		int edges = 0;

		for (Node<T> n : nodes.values()) {
			for (Node<T> c : n.getConnections().keySet()) {
				if (n.getID() <= c.getID())
					edges++;
			}
		}

		return edges;
	}

	@Override
	public boolean add(T newNode) {

		if (nodes.containsKey(newNode))
			return false;

		nodes.put(newNode, new Node(newNode));
		return true;
	}

	@Override
	public boolean connect(T node1, T node2, int cost) {

		if (cost <= 0)
			return false;

		Node<T> n1 = nodes.get(node1);
		Node<T> n2 = nodes.get(node2);

		if (n1 == null || n2 == null)
			return false;

		n1.addConnection(n2, cost);
		n2.addConnection(n1, cost);

		return true;
	}

	@Override
	public boolean isConnected(T node1, T node2) {

		Node<T> n1 = nodes.get(node1);
		Node<T> n2 = nodes.get(node2);

		return n1 == null || n2 == null ? false : n1.isConnected(n2);
	}

	@Override
	public int getCost(T node1, T node2) {
		Node<T> n1 = nodes.get(node1);
		Node<T> n2 = nodes.get(node2);

		if (n1 == null || n2 == null)
			return -1;

		return n1.getCost(n2);
	}

	@Override
	public List<T> depthFirstSearch(T start, T end) {

		Node<T> nstart = nodes.get(start);
		Node<T> nend = nodes.get(end);

		if (nstart == null || nend == null)
			return null;

		Stack<Node<T>> s = new Stack<>();
		s.push(nstart);
		s.peek().setMarked(true);

		while (!s.isEmpty()) {

			if (s.peek().equals(nend))
				break;

			boolean allMarked = true;

			for (Node<T> n : s.peek().getConnections().keySet()) {

				if (!n.isMarked()) {
					s.push(n);
					n.setMarked(true);
					allMarked = false;
					break;
				}
			}

			if (allMarked)
				s.pop();
		}

		clearMarkings();

		if (s.isEmpty())
			return null;

		ArrayList<T> ret = new ArrayList<>();

		for (int i = 0; i < s.size(); i++)
			ret.add(s.get(i).getData());

		return ret;
	}

	@Override
	public List<T> breadthFirstSearch(T start, T end) {
		Node<T> nstart = nodes.get(start);
		Node<T> nend = nodes.get(end);

		if (nstart == null || nend == null)
			return null;

		Queue<Node<T>> s = new LinkedList<>();

		s.add(nstart);
		// nstart.setMarked(true);

		Map<Node<T>, Node<T>> previous = new HashMap<>();

		boolean found = false;

		while (!s.isEmpty() && !found) {
			Node<T> c = s.poll();

			for (Node<T> n : c.getConnections().keySet()) {
				if (!n.isMarked()) {
					previous.put(n, c);
					s.add(n);
					n.setMarked(true);

					if (n.getData().equals(end)) {
						found = true;
						break;
					}
				}
			}
		}

		clearMarkings();

		ArrayList<T> ret = new ArrayList<>();

		if (!found)
			return ret;

		while (nend != nstart) {

			ret.add(0, nend.getData());
			nend = previous.get(nend);
		}

		ret.add(0, nstart.getData());

		return ret;
	}

	@Override
	public UndirectedGraph minimumSpanningTree() {

		MyUndirectedGraph<T> ret = new MyUndirectedGraph<>();

		ArrayList<Node<T>> added = new ArrayList<>();

		Node<T> n = nodes.values().iterator().next();
		added.add(n);
		n.setMarked(true);

		while (ret.nodes.size() != nodes.size()) {

			Node<T> minNodeFrom = null; //
			Entry<Node<T>, Integer> minNode = null;

			for (Node<T> checkMin : added) {

				Iterator<Entry<Node<T>, Integer>> it = checkMin
						.getConnections()
						.entrySet()
						.stream()
						.filter(f -> !f.getKey().isMarked())
						.sorted(Map.Entry.comparingByValue())
						.limit(1)
						.iterator();

				if (it.hasNext()) {
					Entry<Node<T>, Integer> e = it.next();

					if (minNode == null || e.getValue() < minNode.getValue()) {
						minNodeFrom = checkMin;
						minNode = e;
					}
				}
			}

			if (minNode == null)
				break;

			minNode.getKey().setMarked(true);
			added.add(minNode.getKey());
			ret.add(minNodeFrom.getData());
			ret.add(minNode.getKey().getData());
			ret.connect(minNodeFrom.getData(), minNode.getKey().getData(), minNode.getValue());
		}

		clearMarkings();

		return ret;
	}

	private void clearMarkings() {
		nodes.values().forEach(n -> n.setMarked(false));
	}

}
