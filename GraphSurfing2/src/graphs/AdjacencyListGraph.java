package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyListGraph<T> extends Graph<T> {
	Map<T, Vertex> keyToVertex;

	private class Vertex {
		T key;
		List<Vertex> successors;
		List<Vertex> predecessors;

		Vertex(T key) {
			this.key = key;
			this.successors = new ArrayList<Vertex>();
			this.predecessors = new ArrayList<Vertex>();
		}
	}

	AdjacencyListGraph(Set<T> keys) {
		this.keyToVertex = new HashMap<T, Vertex>();
		for (T key : keys) {
			Vertex v = new Vertex(key);
			this.keyToVertex.put(key, v);
		}
	}

	@Override
	public int size() {
		return this.keyToVertex.size();
	}

	@Override
	public int numEdges() {
		int successors = 0;
		for (Vertex v : this.keyToVertex.values()) {
			successors += v.successors.size();
		}
		return successors;
	}

	/**
	 * return true if actually adding edge, return false if already there
	 */
	@Override
	public boolean addEdge(T from, T to) {
		// check that the graph contains from and to
		if (!(this.hasVertex(from)))
			throw new NoSuchElementException("couldn't find " + from.toString());
		if (!(this.hasVertex(to)))
			throw new NoSuchElementException("couldn't find " + to.toString());
		Vertex fromV = this.keyToVertex.get(from);
		Vertex toV = this.keyToVertex.get(to);
		List<Vertex> fromSuccs = fromV.successors;
		List<Vertex> toPreds = toV.predecessors;
		// if from and to are already connected
		if (fromSuccs.contains(toV)) {
			return false;
		}
		// add to to the list of successors of from and add from to the list of
		// predecessors of to
		fromSuccs.add(toV);
		toPreds.add(fromV);
		return true;
	}

	@Override
	public boolean hasVertex(T key) {
		return this.keyToVertex.containsKey(key);
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		// check that the graph contains from and to
		if (!(this.hasVertex(from)))
			throw new NoSuchElementException("couldn't find " + from.toString());
		if (!(this.hasVertex(to)))
			throw new NoSuchElementException("couldn't find " + to.toString());
		Vertex fromV = this.keyToVertex.get(from);
		Vertex toV = this.keyToVertex.get(to);
		List<Vertex> fromSuccs = fromV.successors;
		List<Vertex> toPreds = toV.predecessors;
		// if from and to are already connected
		if (fromSuccs.contains(toV)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		// check that the graph contains from and to
		if (!(this.hasVertex(from)))
			throw new NoSuchElementException("couldn't find " + from.toString());
		if (!(this.hasVertex(to)))
			throw new NoSuchElementException("couldn't find " + to.toString());
		Vertex fromV = this.keyToVertex.get(from);
		Vertex toV = this.keyToVertex.get(to);
		List<Vertex> fromSuccs = fromV.successors;
		List<Vertex> toPreds = toV.predecessors;
		// if from and to are connected
		if (fromSuccs.contains(toV)) {
			// remove to from the list of successors of from and remove from
			// from the list of predecessors of to
			fromSuccs.remove(toV);
			toPreds.remove(fromV);
			return true;
		}
		// if nothing needs be done because they were never connected
		return false;
	}

	@Override
	public int outDegree(T key) {
		return this.keyToVertex.get(key).successors.size();
	}

	@Override
	public int inDegree(T key) {
		return this.keyToVertex.get(key).predecessors.size();
	}

	@Override
	public Set<T> vertexSet() {
		return this.keyToVertex.keySet();
	}

	@Override
	public List<T> successorList(T key) {
		List<Vertex> lv = this.keyToVertex.get(key).successors;
		List<T> retL = new ArrayList<T>();
		for (int i = 0; i < lv.size(); i++) {
			retL.add(lv.get(i).key);
		}
		return retL;
	}

	@Override
	public List<T> predecessorList(T key) {
		List<Vertex> lv = this.keyToVertex.get(key).predecessors;
		List<T> retL = new ArrayList<T>();
		for (int i = 0; i < lv.size(); i++) {
			retL.add(lv.get(i).key);
		}
		return retL;
	}

	@Override
	public Iterator<T> successorIterator(T key) {
		List<T> thisL = this.successorList(key);
		return new myIter(thisL);
	}
	
	private class myIter implements Iterator<T>{
		
		private List<T> myList = null;
		private int ind = 0;
		private int mlsize = 0;
		
		public myIter(List<T> myList) {
			this.myList = myList;
			this.mlsize = this.myList.size();
		}

		@Override
		public boolean hasNext() {
			if(this.ind < this.mlsize)
				return true;
			return false;
		}

		@Override
		public T next() {
			if(!this.hasNext())
				throw new NoSuchElementException();
			T retVal = this.myList.get(this.ind);
			this.ind++;
			return retVal;
		}
		
	}

	@Override
	public Iterator<T> predecessorIterator(T key) {
		List<T> thisL = this.predecessorList(key);
		return new myIter(thisL);
	}

}
