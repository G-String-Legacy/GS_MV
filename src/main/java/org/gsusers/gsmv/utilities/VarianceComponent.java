package org.gsusers.gsmv.utilities;

import org.gsusers.gsmv.model.Facet;
import org.gsusers.gsmv.model.Nest;

/**
 * 'VarianceComponent' (vc) is a class of objects for the calculation of
 * Generalizability Coefficients used in 'Nest', method 'formatResults'.
 * There is one vc object for each actual variance component value.
 * The object calculates the contribution of this variance component
 * to the final Generalization Coefficient.
 * VC also handles Brennan's rules for calculating sigma2(tau), sigma2(delta),
 * and sigma2(Delta) [Brennan, Generalizability Theory, pp 144/5]
 * For each variance component it thus determines three booleans (b_tau, b_delta,
 * and b_Delta) to signify that this variance component gets added to the corresponding
 * sigma square. This step then occurs in Nest.formatResults.
 * During D-Studies it changes the signature item of fixed facets to 'f', thus
 * excluding them from contributing to the error terms delta and Delta.
 *
 * @see <a href="https://github.com/G-String-Legacy/G_String/blob/main/workbench/GS_L/src/utilities/VarianceComponent.java">utilities.VarianceComponent</a>
 * @author ralph
 * @version %v..%
 *
 */
public class VarianceComponent {
	
	/**
	 * nesting pattern
	 */
	private final String sPattern;
	
	/**
	 * char array of pattern
	 */
	private final char[] cPattern;
	
	/**
	 * contributes to tau
	 */
	private Boolean b_tau = false;

	/**
	 * contributes to delta
	 */
	private Boolean b_delta = false;

	/**
	 * contributes to Delta
	 */
	private Boolean b_Delta = false;

	/**
	 * variance component
	 */
	private Double dVC;

	/**
	 * interpretation of pattern
	 */
	private String sSignature;

	/**
	 * pointer to Nest
	 */
	private final Nest myNest;
	
	/**
	 * Facet dictionary, original order
	 */
	private final String sDictionary;
	
	/**
	 * array of facets
	 */
	private final Facet[] farFacets;
	
	/**
	 * double variable for the value of the coefficient denominator
	 */
	private Double dDenominator = 1.0;

	/**
	 * pointer to logger
	 */
	private final gsLogger logger;
	
	/**
	 * Class constructor
	 *
	 * @param _nest  pointer to Nest
	 * @param _line  String argument formally characterizing the configuration of the variance component
	 * @param _logger pointer to org.gs_users.gs_lv.GS_Application logger
	 */
	public VarianceComponent(Nest _nest, String _line, gsLogger _logger) {
		/*
		 * constructor for G-Analysis
		 */
		logger = _logger;
		myNest = _nest;
		sDictionary = myNest.getDictionary();
		farFacets = myNest.getFacets();
		String[] sWords = _line.split("\\s+");
		dVC = Double.parseDouble(sWords[5]);
		if (dVC < 0.0)			// ignore negative variance components
			dVC = 0.0;
		sPattern = sWords[0];
		cPattern = sPattern.toCharArray();
	}

	/**
	 * Assembles formal lexical description of coefficient
	 *
	 * @param sbOut pointer to string builder returning the description
	 */
	public void doCoefficient(StringBuilder sbOut) {
			StringBuilder sb = new StringBuilder();
			Double dFactor;
			boolean bFirst = true;
			dDenominator = 1.0;
			sSignature = sign(sPattern);
			for (char c : cPattern) {
				if (c != ':') {
					Facet f = farFacets[sDictionary.indexOf(c)];
					if (f.getFacetType() == 'd') 		// no factor from level of differentiation
						dFactor = 1.00;
					else if (f.getFacetType() == 's')	// no factor from level of stratification
						dFactor = 1.00;
					else
						dFactor = f.dGetLevel();
					dDenominator *= dFactor;
					if (bFirst)
						sb.append(String.format("%.2f", dFactor));
					else
						sb.append(" x ").append(String.format("%.2f", dFactor));
					bFirst = false;
				}
		}
	
		/*
		 * now analyze contribution to tau, delta and Delta.
		 * in words:
		 *   tau: always has to contain d-type,  but no random facets;
		 *   delta: at least one 'g', but no 'd';
		 *   Delta: at least one 'g' facet
		 */
		b_tau = !has('g') && has('d');
		b_delta = has('g') && has('d');
		b_Delta = has('g');
		String sTarget = null;
		if (b_tau)
			sTarget = "τ only";
		else if (b_delta && !b_Delta)
			sTarget = "δ only";
		else if (!b_delta && b_Delta)
			sTarget = "Δ only";
		else if (b_delta)
			sTarget = "both δ and Δ";
		/*
		 * string for denominator
		 */
		String sDenominator = sb.toString();
		if (sbOut != null) {
			try {
				sbOut.append("Variance component '").append(sPattern).append("' (").append(sSignature).append(") is ").append(dVC).append("; denominator is ").append(sDenominator).append(";  ").append(sTarget).append("\n");
			} catch (Exception e) {
				logger.log("VarianceComponent", 178, "", e);
			}
		}
	}

	/**
	 * Tests if Variance component contains a specific facet
	 *
	 * @param _c  char of facet
	 * @return Boolean of confirmation
	 */
	private Boolean has(char _c) {
		/*
		 * simplified 'Signature contains' function
		 */
		return (sSignature.indexOf(_c) >= 0);
	}

	/**
	 * getter of vc denominator
	 *
	 * @return Double denominator
	 */
	public Double getDenominator() {
		return dDenominator;
	}

	/**
	 *getter of vc value
	 *
	 * @return Double vc value
	 */
	public Double getVarianceComponent() {
		return dVC;
	}

	/**
	 * getter of signature of vc in D-Studies ('d', 's' and 'r' or 'f' for facets of generalization
	 *
	 * @param _pattern String, lexical description in terms of 'd', 'g', 's' and ':'
	 * @return signature of vc pattern
	 */
	private String sign(String _pattern) {
		char[] cPattern = _pattern.toCharArray();
		char cTemp;
		StringBuilder sb = new StringBuilder();
		for (char c : cPattern) {
			if (c == ':')
				sb.append(c);
			else {
				int iFacet = myNest.getDictionary().indexOf(c);
				Facet f = farFacets[iFacet];
				cTemp = f.getFacetType();
				if (f.getFixed())		// that excludes them from error terms
					cTemp = 'f';
				sb.append(cTemp);
			}
		}
		return sb.toString();
	}

	/**
	 * getter of confirmation that vc is a tau
	 *
	 * @return Boolean confirmation
	 */
	public Boolean b_tau() {
		return b_tau;
	}

	/**
	 * getter of confirmation that vc is delta
	 *
	 * @return Boolean confirmation
	 */
	public Boolean b_delta() {
		return b_delta;
	}

	/**
	 * getter of confirmation that vc is Delta
	 *
	 * @return Boolean confirmation
	 */
	public Boolean b_Delta() {
		return b_Delta;
	}
}
