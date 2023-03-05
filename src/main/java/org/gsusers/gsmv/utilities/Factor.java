package org.gsusers.gsmv.utilities;

import org.gsusers.gsmv.model.Facet;
import org.gsusers.gsmv.model.SampleSizeTree;
/**
 * This class encapsulates design factors - factors
 * within a configuration. It  calculates the absolute size
 * of this factor, and the indices dependent count for
 * this factor. That makes it easy to calculate size and count
 * for the configuration.
 * A 'factor' encapsulates the structure and properties of individual or nested facet combinations
 * forming factors' in crossed designs.
 *
 */


public class Factor {

	/**
	 * pointer to SampleSizeTree object
	 */
	private SampleSizeTree tree;
	
	/**
	 * formal definition of factor in terms of Facets and their relations
	 */
	private String sFactor;
	
	/**
	 * array of 'Floors' - parts of Effects in a nesting level
	 */
	private String[] sarFloors;
	
	/**
	 * counter of 'Floors'
	 */
	private int iFloors = 0;
	
	/**
	 * local copy of sample size arrays from Tree
	 */
	private int[][][] iarSizes;
	
	/**
	 * local copy of original Facet dictionary
	 */
	private String sDictionary;
	
	/**
	 * pointer to Facet object
	 */
	private Facet facet;
	
	/**
	 * length of sDictionary
	 */
	private int iDL;
	
	/**
	 * description of 'Floor' content
	 */
	private String sFloor;

	/**
	 * Constructor
	 *
	 * @param _tree  pointer to SampleSizeTree
	 * @param _sFactor  description of actual 'Factor' content
	 */
	public Factor (SampleSizeTree _tree, String _sFactor) {
		tree = _tree;
		sDictionary = tree.getDictionary();
		iDL = sDictionary.length();
		sFactor = new StringBuilder(_sFactor).reverse().toString();
		sarFloors = sFactor.split(":");
		iFloors = sarFloors.length;
		iarSizes = new int[iFloors][][];
	}

	/**
	 * Getter of factor size, i.e. the maximal number of states, this factor
	 * can assuem.
	 *
	 * @return  int maximal number of states
	 */
	public int getSize() {
		int iF = -1;
		int[] iProducts = null;
		int iProduct = 1;
		int iSum = 0;
		int iL = 0;
		char cN0 = '$';			// previous Nestor designation
		char cN = '@';				// current Nestor designation
		int iFloor = iFloors - 1;
		sFloor = sarFloors[iFloor];
		iarSizes[iFloor] = new int [iDL][];
		iProduct = 1;
		for (char cF : sFloor.toCharArray()) {
			iF = sDictionary.indexOf(cF);
			facet = tree.getFacet(cF);
			cN = facet.getNestor();
			if (iFloor == 0) {
				iarSizes[iFloor][iF] = tree.getSizes(cF).clone();
				iProduct = iarSizes[iFloor][iF][0];
			} else if(cN == cN0) {
				int[] iarFactors = tree.getSizes(cF);
				for (int i = 0; i < iL; i++)
					iProducts[i] *= iarFactors[i];
			} else {
				if ((cN != '$') && (iProducts != null)) {
					iSum = 0;
					for (int i : iProducts)
						iSum += i;
					iProduct *= iSum;
				}
				iL = tree.getDim(cN);
				iProducts = (tree.getSizes(cF)).clone();
				cN0 = cN;
			}
		}
		iSum = 0;
		if (iProducts != null) {
			for (int i : iProducts)
				iSum += i;
			iProduct *= iSum;
		}
		return iProduct;
	}

	/**
	 * Current count of states in the factor, as function of the
	 * relevant facet indices.
	 *
	 * @return  int number of states
	 */
	public int getCount() {
		int iF = -1;
		int[] iProducts = null;
		int iProduct = 1;
		int iSum = 0;
		int iL = 0;
		char cN0 = '$';			// previous Nestor designation
		char cN = '@';				// current Nestor designation
		for (int iFloor = 0; iFloor < iFloors; iFloor++) {
			sFloor = sarFloors[iFloor];
			iarSizes[iFloor] = new int [iDL][];
			iProduct = 1;
			for (char cF : sFloor.toCharArray()) {
				iF = sDictionary.indexOf(cF);
				facet = tree.getFacet(cF);
				cN = facet.getNestor();
				if (iFloor == 0) {
					iarSizes[iFloor][iF] = tree.getSizes(cF).clone();
					iProduct = iarSizes[iFloor][iF][0];
				} else if(cN == cN0) {
					int[] iarFactors = tree.getSizes(cF);
					for (int i = 0; i < iL; i++)
						iProducts[i] *= iarFactors[i];
				} else {
					if ((cN != '$') && (iProducts != null)) {
						iSum = 0;
						for (int i : iProducts)
							iSum += i;
						iProduct *= iSum;
					}
					iL = tree.getDim(cN);
					iProducts = (tree.getSizes(cF)).clone();
					cN0 = cN;
				}
			}
			iSum = 0;
			if (iProducts != null) {
				for (int i : iProducts)
					iSum += i;
				iProduct *= iSum;
			}
		}
		return iProduct;
	}
}

