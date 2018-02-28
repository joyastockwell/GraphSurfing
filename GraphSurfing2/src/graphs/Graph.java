package graphs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Set;

/**
 * Abstract class to represent the Graph ADT. It is assumed that every vertex
 * contains some data of type T, which serves as the identity of that node and
 * provides access to it.
 * 
 * @author Nate Chenette; Joy Stockwell (worked with Reid Witte on algorithm
 *         development)
 *
 * @param <T>
 */
public abstract class Graph<T> {

	/**
	 * Returns the number of vertices in the graph.
	 * 
	 * @return
	 */
	public abstract int size();

	/**
	 * Returns the number of edges in the graph.
	 * 
	 * @return
	 */
	public abstract int numEdges();

	/**
	 * Adds a directed edge from the vertex containing "from" to the vertex
	 * containing "to".
	 * 
	 * @param from
	 * @param to
	 * @return true if the add is successful, false if the edge is already in
	 *         the graph.
	 * @throws NoSuchElementException
	 *             if either key is not found in the graph
	 */
	public abstract boolean addEdge(T from, T to);

	/**
	 * Determines whether a vertex is in the graph.
	 * 
	 * @param key
	 * @return true if the graph has a vertex containing key.
	 */
	public abstract boolean hasVertex(T key);

	/**
	 * Determines whether an edge is in the graph.
	 * 
	 * @param from
	 * @param to
	 * @return true if the directed edge (from, to) is in the graph, otherwise
	 *         false.
	 * @throws NoSuchElementException
	 *             if either key is not found in the graph
	 */
	public abstract boolean hasEdge(T from, T to) throws NoSuchElementException;

	/**
	 * Removes an edge from the graph.
	 * 
	 * @param from
	 * @param to
	 * @return true if the remove is successful, false if the edge is not in the
	 *         graph.
	 * @throws NoSuchElementException
	 *             if either key is not found in the graph
	 */
	public abstract boolean removeEdge(T from, T to) throws NoSuchElementException;

	/**
	 * Computes out-degree of a vertex.
	 * 
	 * @param key
	 * @return the number of successors of the vertex containing key
	 * @throws NoSuchElementException
	 *             if the key is not found in the graph
	 */
	public abstract int outDegree(T key);

	/**
	 * Computes in-degree of a vertex.
	 * 
	 * @param key
	 * @return the number of predecessors of the vertex containing key
	 * @throws NoSuchElementException
	 *             if the key is not found in the graph
	 */
	public abstract int inDegree(T key);

	/**
	 * Returns the Set of vertex keys in the graph.
	 * 
	 * @return
	 */
	public abstract Set<T> vertexSet();

	/**
	 * Returns a List of keys that are successors of the given key.
	 * 
	 * @param key
	 * @return
	 */
	public abstract List<T> successorList(T key);

	/**
	 * Returns a List of keys that are predecessors of the given key.
	 * 
	 * @param key
	 * @return
	 */
	public abstract List<T> predecessorList(T key);

	/**
	 * Returns an Iterator that traverses the keys who are successors of the
	 * given key.
	 * 
	 * @param key
	 * @return
	 */
	public abstract Iterator<T> successorIterator(T key);

	/**
	 * Returns an Iterator that traverses the keys who are successors of the
	 * given key.
	 * 
	 * @param key
	 * @return
	 */
	public abstract Iterator<T> predecessorIterator(T key);

	/**
	 * Finds the strongly-connected component of the provided key.
	 * 
	 * @param key
	 * @return a set containing all data in the strongly connected component of
	 *         the vertex containing key
	 */
	/**
	 * Finds the strongly-connected component of the provided key.
	 * 
	 * @param key
	 * @return a set containing all data in the strongly connected component of
	 *         the vertex containing key
	 */
	public Set<T> stronglyConnectedComponent(T key) {
		// get every vertex we can reach by going forward and put them in a
		// hashset starting with the successors of key
		List<T> successors;
		HashSet<T> forwardVertices = new HashSet<T>();
		// keep the vertices who can be reached by going forward but whose
		// successors we have not gotten and who we have not added to the
		// hashset in a linked list
		LinkedList<T> checking = new LinkedList<T>();
		checking.add(key);
		while (!checking.isEmpty()) {
			// while there is stuff in checking, pull the front elem off (call
			// it curr)
			T curr = checking.poll();
			// unless curr is already in the hashset, in which case its
			// successors are already in checking, get curr's successors and put
			// them in checking and then add curr to the hashset
			if (!forwardVertices.contains(curr)) {
				successors = successorList(curr);
				forwardVertices.add(curr);
				checking.addAll(successors);
			}
			// eventually, checking will empty out and we'll leave this loop, at
			// which point we will have all the vertices we can reach by going
			// forward
		}
		// now get all the vertices we can by going backwards
		List<T> predecessors;
		HashSet<T> backwardVertices = new HashSet<T>();
		checking.add(key);
		while (!checking.isEmpty()) {
			T curr = checking.poll();
			if (!backwardVertices.contains(curr)) {
				predecessors = predecessorList(curr);
				backwardVertices.add(curr);
				checking.addAll(predecessors);
			}
		}
		// put all the things in both sets into the returned set
		HashSet<T> retSet = new HashSet<T>();
		for (T obj : forwardVertices) {
			if (backwardVertices.contains(obj))
				retSet.add(obj);
		}
		return retSet;
	}

	/**
	 * Searches for the shortest path between start and end points in the graph.
	 * 
	 * @param start
	 * @param end
	 * @return a list of data, starting with start and ending with end, that
	 *         gives the path through the graph, or null if no such path is
	 *         found.
	 */
	public List<T> shortestPath(T startLabel, T endLabel) {
		// a queue of arraylists, the last elem in which we want to investigate
		Queue<List<T>> untried = new LinkedList<List<T>>();
		// put in the front, remove from the back: FIFO
		// this allows it to be a breadth first rather than depth first search
		List<T> justFirst = new ArrayList<T>();
		justFirst.add(startLabel);
		// add an arrayList with just the starting element to the front of the
		// queue
		untried.add(justFirst);
		// keep a list of places you've already been so you don't get stuck in a
		// loop
		HashSet<T> triedVertices = new HashSet<T>();
		// while there are vertices untried in the queue
		while (!untried.isEmpty()) {
			// take the arrayList from the back of the queue
			List<T> taken = untried.poll();
			// the vertex we want to investigate: the one at the depth we are
			// exploring
			T current = (T) taken.get(taken.size() - 1);
			// make sure not to add anything we've already tried; that would
			// waste time
			if (!triedVertices.contains(current)) {
				// if we have removed the endLabel and put it on our list of
				// potential vertices, we can return that list; it is the right
				// one
				if (current.equals(endLabel)) {
					return taken;
				}
				// add the successors of the vertex removed from the queue to
				// arrayLists and add those arrayLists to the queue
				for (T elem : successorList(current)) {
					List<T> arrl = new ArrayList<T>(taken);
					arrl.add(elem);
					untried.add(arrl);
				}
				// add the vertex just tried to tried vertices
				triedVertices.add(current);
			}
		}
		return null;
	}

}