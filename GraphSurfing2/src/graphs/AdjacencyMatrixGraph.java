package graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class AdjacencyMatrixGraph<T> extends Graph<T> {
	Map<T, Integer> keyToIndex;
	List<T> indexToKey;
	// matrix entries: matrix[from][to]
	int[][] matrix;

	AdjacencyMatrixGraph(Set<T> keys) {
		int size = keys.size();
		this.keyToIndex = new HashMap<T, Integer>();
		this.indexToKey = new ArrayList<T>();
		this.matrix = new int[size][size];
		// need to populate keyToIndex and indexToKey with info from keys
		for (T thing : keys) {
			this.indexToKey.add(thing);
		}
		for (int i = 0; i < this.indexToKey.size(); i++) {
			this.keyToIndex.put(this.indexToKey.get(i), i);
		}
	}

	@Override
	public int size() {
		return this.indexToKey.size();
	}

	@Override
	public int numEdges() {
		int edges = 0;
		for (int i = 0; i < this.matrix.length; i++)
			for (int j = 0; j < this.matrix.length; j++)
				if (this.matrix[i][j] == 1)
					edges++;
		return edges;
	}

	@Override
	public boolean addEdge(T from, T to) {
		if (!this.keyToIndex.containsKey(from) || !this.keyToIndex.containsKey(to))
			throw new NoSuchElementException();
		int fromInd = this.keyToIndex.get(from);
		int toInd = this.keyToIndex.get(to);
		// return false if the edge is already there because we are not adding
		// it new
		if (this.matrix[fromInd][toInd] == 1) {
			return false;
		}
		// otherwise add the edge and return true
		this.matrix[fromInd][toInd] = 1;
		return true;
	}

	@Override
	public boolean hasVertex(T key) {
		if (this.keyToIndex.containsKey(key))
			return true;
		return false;
	}

	@Override
	public boolean hasEdge(T from, T to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(from) || !this.keyToIndex.containsKey(to))
			throw new NoSuchElementException();
		if (this.matrix[this.keyToIndex.get(from)][this.keyToIndex.get(to)] == 1)
			return true;
		return false;
	}

	@Override
	public boolean removeEdge(T from, T to) throws NoSuchElementException {
		if (!this.keyToIndex.containsKey(from) || !this.keyToIndex.containsKey(to))
			throw new NoSuchElementException();
		int fromInd = this.keyToIndex.get(from);
		int toInd = this.keyToIndex.get(to);
		if (this.matrix[fromInd][toInd] == 1) {
			this.matrix[fromInd][toInd] = 0;
			return true;
		}
		return false;
	}

	@Override
	public int outDegree(T key) {
		int count = 0;
		int fromInd = this.keyToIndex.get(key);
		for (int i = 0; i < this.matrix.length; i++) {
			if (this.matrix[fromInd][i] == 1)
				count++;
		}
		return count;
	}

	@Override
	public int inDegree(T key) {
		int count = 0;
		int toInd = this.keyToIndex.get(key);
		for (int i = 0; i < this.matrix.length; i++) {
			if (this.matrix[i][toInd] == 1)
				count++;
		}
		return count;
	}

	@Override
	public Set<T> vertexSet() {
		Set<T> retSet = new HashSet<T>();
		for (T thing : this.keyToIndex.keySet())
			retSet.add(thing);
		return retSet;
	}

	@Override
	public List<T> successorList(T key) {
		int fromInd = this.keyToIndex.get(key);
		ArrayList<T> retL = new ArrayList<T>();
		for (int i = 0; i < this.matrix.length; i++)
			if (this.matrix[fromInd][i] == 1)
				retL.add(this.indexToKey.get(i));
		return retL;
	}

	@Override
	public List<T> predecessorList(T key) {
		int toInd = this.keyToIndex.get(key);
		ArrayList<T> retL = new ArrayList<T>();
		for (int i = 0; i < this.matrix.length; i++)
			if (this.matrix[i][toInd] == 1)
				retL.add(this.indexToKey.get(i));
		return retL;
	}

	private class pI implements Iterator<T> {

		int myInd = 0;
		int pos = 0;
		int size = AdjacencyMatrixGraph.this.matrix.length;

		public pI(T key) {
			this.myInd = AdjacencyMatrixGraph.this.keyToIndex.get(key);
		}

		@Override
		public boolean hasNext() {
			boolean flag = false;
			for (int i = this.pos; i < this.size; i++) {
				if (AdjacencyMatrixGraph.this.matrix[i][this.myInd] == 1)
					flag = true;
			}
			return flag;
		}

		@Override
		public T next() {
			if (!this.hasNext() == false)
				;
			T retVal = null;
			for (int i = this.pos; i < this.size; i++) {
				if (AdjacencyMatrixGraph.this.matrix[i][this.myInd] == 1)
					retVal = AdjacencyMatrixGraph.this.indexToKey.get(i);
			}
			this.pos++;
			return retVal;
		}
	}

	@Override
	public Iterator<T> successorIterator(T key) {
		return new sI(key);
	}

	private class sI implements Iterator<T> {

		int myInd = 0;
		int pos = 0;
		int size = AdjacencyMatrixGraph.this.matrix.length;

		public sI(T key) {
			this.myInd = AdjacencyMatrixGraph.this.keyToIndex.get(key);
		}

		@Override
		public boolean hasNext() {
			for (int i = this.pos; i < this.size; i++) {
				if (AdjacencyMatrixGraph.this.matrix[this.myInd][i] == 1)
					return true;
			}
			return false;
		}

		@Override
		public T next() {
			if (!this.hasNext())
				throw new NoSuchElementException();
			T retVal = null;
			for (int i = this.pos; i < this.size; i++) {
				if (AdjacencyMatrixGraph.this.matrix[this.myInd][i] == 1) {
					retVal = AdjacencyMatrixGraph.this.indexToKey.get(i);
					this.pos = i + 1;
					break;
				}
			}
			return retVal;
		}
	}

	public Iterator<T> predecessorIterator(T key) {
		return new pI(key);
	}

}
