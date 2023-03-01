package org.gsusers.gsmv.utilities;

import java.util.ArrayList;

import org.gsusers.gsmv.model.Nest;
import org.gsusers.gsmv.model.SampleSizeTree;

/**
 * Class CombConstr generates 4 structural arrays fully describing the design,
 * and passes them to the SampleSizeTree class.
 * 
 * the 4 arrays are:
 * 		iarConfigurations[]
 * 		iarDepths[]
 * 		iarCeilings[]
 * 		iarProducts[][]
 */
public class CombConstrct {

	/**
	 * pointer to <code>SampleSizeTree</code>
	 */
	private final SampleSizeTree myTree;

	/**
	 * 2-Dim string array of factored configurations
	 */
	String[][] sarProducts;

	/**
	 * number of configurations (combinations of products
	 */
	private final int iConfigurationCount;
	
	/**
	 * constructor
	 *
	 * @param _nest  pointer to <code>Nest</code>
	 */
	public CombConstrct (Nest _nest){
		/*
		 * pointer to <code>Nest</code>
		 */
		myTree = _nest.getTree();
		/*
		 * Array of initial nested names
		 */
		String[] sarNestedNames = _nest.getNestedNames();
		/*
		 * Array list of pure facet products
		 */
		ArrayList<String> salConfigs = new ArrayList<>(0);
		int iComplexity = sarNestedNames.length;
		int[] iMasks = new int[iComplexity];
		int iTop = 1 << iComplexity;
		int iTemp;
		String sNN;
		boolean bContained;
		String sProduct;
		String sTemp;
		/*
		 * initialize ArrayList salProducts with existing nests
		 */
		for (int i = 0; i < iComplexity; i++) {
			iTemp = 1 << i;
			iMasks[i] = iTemp;
			sTemp = sReverse(sarNestedNames[i]);
			if (sTemp.length() > 1)
				sTemp = "(" + sTemp + ")";
			salConfigs.add(sTemp);
		}
		/*
		 * Add all permitted products of nests to salProducts
		 * In order to get all allowed configurations in the
		 * correct sequence, we employ a binary enumeration, 
		 * where each power of 2 corresponds to a specific facet
		 * in the appropriate nesting context.
		 */
		char cLead;
		for (int i = 1; i <= iTop; i++) {
			StringBuilder sb = new StringBuilder(0);
			for (int j = 0; j < iComplexity; j++) {
				sNN = sReverse(sarNestedNames[j]);
				cLead = sNN.toCharArray()[0];
				if (((i & iMasks[j]) == iMasks[j]) && (sb.toString().indexOf(cLead) < 0)) {
					if (sNN.length() > 1)
						sNN = "(" + sNN + ")";
					sb.append(sNN);
				}
			}
			sProduct = sb.toString();
			bContained = false;
			for (String s : salConfigs) {
				bContained = (s.contains(sProduct));
				if (bContained)
					break;
			}
			if (!bContained) {
				salConfigs.add(sProduct);
			}
		}
		/*
		 * 1-Dim String array of Configurations
		 */
		String[] sarConfigs = salConfigs.toArray(new String[0]);
		/*
		 * Parse array of strings salProducts into
		 * 2-Dim array of strings sarConfigs.
		 */
		int L = sarConfigs.length;
		sarProducts = new String[L][];
		ArrayList<String> salFactors;
		String sP;
		char[] cPs;
		String s;
		boolean bComplex;
		StringBuilder sb = null;
		for (int i = 0; i < L; i++) {
			salFactors = new ArrayList<>(0);
			sP = sarConfigs[i];
			cPs = sP.toCharArray();
			bComplex = ((sP.indexOf(':') >= 0) && (sP.indexOf('(') < 0));
			if (bComplex)
				sb = new StringBuilder(0);
			for (char c : cPs) {
				switch (c) {
					case '(' -> {
						bComplex = true;
						sb = new StringBuilder(0);
					}
					case ':' -> {
						assert sb != null;
						sb.append(":");
					}
					case ')' -> {
						bComplex = false;
						assert sb != null;
						salFactors.add("(" + sb + ")");
					}
					default -> {
						s = String.valueOf(c);
						if (bComplex)
							sb.append(s);
						else
							salFactors.add(s);
					}
				}
			}
			if (salFactors.isEmpty()) {
				assert sb != null;
				salFactors.add("(" + sb + ")");
			}
			sarProducts[i] = salFactors.toArray(new String[0]);
		}
		
		iConfigurationCount = sarConfigs.length;
		myTree.setConfigurations(sarConfigs);
		int[] iarDepths = new int[iConfigurationCount];
		String[] sF;
		int iProduct;
		for (int i = 0; i < iConfigurationCount; i++) {
			sF = sarProducts[i];
			iProduct = 1;
			for (String sS : sF) {
				iProduct *= getMaxSum (sS);
			}
			iarDepths[i] = iProduct;
		}
		myTree.setConfigurations(sarConfigs);
		myTree.setDepths(iarDepths);
		myTree.setProducts(sarProducts);
	}
	
	/**
	 * auxiliary method, returns maximal state sum of a nested facet from SampleSizeTree.
	 * 
	 * @param _sFac	 nested facet
	 * @return  maximal number of states
	 */
	private int getMaxSum(String _sFac) {
		String s = _sFac.replace("(","").replace(")","");
		char[] cArray = s.toCharArray();
		int L = cArray.length - 1;
		return myTree.getMaxSum(cArray[L]);
	}
	
	private String sReverse(String s) {
		StringBuilder sb = new StringBuilder(0);
		sb.append(s);
		return sb.reverse().toString();
	}
	
	public int getConfigurationCount() {
		return iConfigurationCount;
	}
}

