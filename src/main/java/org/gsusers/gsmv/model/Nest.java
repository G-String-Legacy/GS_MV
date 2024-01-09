package org.gsusers.gsmv.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import org.gsusers.gsmv.GS_Application;
import org.gsusers.gsmv.utilities.VarianceComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

import org.gsusers.gsmv.utilities.gsLogger;

/**
 * class Nest
 * Encapsulates the major parameters of the assessment design, and the central variables required for analysis.
 * <code>Nest</code> is used in most other classes as common repository to access central variables and parameters.
 *
 * @see <a href="https://github.com/G-String-Legacy/G_String/blob/main/workbench/GS_L/src/model/Nest.java">model.Nest</a>
 * @author ralph
 * @version %v..%
 * */
public class Nest {

	/**
	 * <code>cAsterisk</code> the designating char designation of the facet 
	 * carrying the asterisk, i.e. the facet associated with a carriage
	 * return in the data input file.
	 */
	private char cAsterisk;
	
	/**
	 * Designation char for replicating facet. '-' for none
	 */
	private char cReplicate = '-';

	/**
	 * <code>iFacetCount</code> the number of facets in the design.
	 */
	private Integer iFacetCount = 0;

	/**
	 * <code>iStep</code> keeps count of the current step in <code>AnaGroups</code> and <code>SynthGroups</code>.
	 */
	private Integer iStep = -1;

	/**
	 * <code>bDoOver</code> <code>true</code>: parameter input from script, <code>false</code>: manual entry.
	 */
	private Boolean bDoOver = false;

	/**
	 * <code>bSimulate</code> <code>false</code> (default): perform analysis, <code>true</code>: perform analysis.
	 */
	private Boolean bSimulate = false;

	/**
	 * <code>bReplicate</code> flag to operate with replicating measurements
	 */
	private Boolean bReplicate;

	/**
	 * <code>sScriptTitle</code> title in script; default 'gstudy'.
	 */
	private String sScriptTitle;

	/**
	 * <code>salComments</code> array list containing comments for script.
	 */
	private ArrayList<String> salComments = new ArrayList<>();

	/**
	 * <code>sbHFO</code> stringbuilder to build hierarchic facet directory <code>sHDictionary</code>,
	 * the order they were arranged subsequently.
	 */
	private final StringBuilder sbHFO;

	/**
	 * array list of facets; temporary if facets come in before number established
	 */
	private ArrayList<Facet> falFacets = null;
	
	/**
	 * <code>farFacets</code> array of all facets
	 */
	private Facet[] farFacets = null;

	/**
	 * <code>myTree</code> pointer to <code>SampleSizeTree</code>.
	 */
	private final SampleSizeTree myTree;
	
	/**
	 * <code>sarNestedNames</code> array of final, nested arrays in hierarchical order.
	 */
	private String[] sarNestedNames;

	/**
	 * <code>sDictionary</code> simple simulation of one character dictionary
	 * as concatenation of member characters in the original order,
	 * the facets were entered.
	 */
	private String sDictionary;

	/**
	 * <code>sHDictionary</code> the hierarchical dictionary orders the
	 * facets in the order the data appear in the data file.
	 */
	private String sHDictionary;

	/**
	 * <code>iNestCount</code> number of nested facets.
	 */
	private Integer iNestCount = 0;

	/**
	 * <code>scene</code> standard empty display <code>Scene</code> for use in objects.
	 */
	private Scene scene = null;

	/**
	 * <code>bDawdle</code> boolean <code>false</code> (default): proceed to normal next step;
	 * <code>true</code>: instead steps through sample size collection.
	 */
	private Boolean bDawdle = false;

	/**
	 * <code>bVarianceDawdle</code> boolean <code>false</code> (default): proceed to normal next step; <code>true</code>: instead steps through variance collection.
	 * if <code>true</code>, steps through variance components
	 */
	private Boolean bVarianceDawdle = false;

	/**
	 * <code>myMain</code> pointer to class <code>Main</code>.
	 */
	private final GS_Application myMain;

	/**
	 * <code>dGrandMeans</code> grand means of all scores in data file, as Double.
	 */
	private Double dGrandMeans = 0.0;

	/**
	 * <code>salVarianceComponents</code> array list of variance components.
	 */
	private final ArrayList<VarianceComponent> salVarianceComponents;

	/**
	 * <code>sTitle</code> default header in analysis output.
	 */
	private String sTitle = " G Study.";

	/**
	 * <code>dRel</code> Generalizability Coefficient.
	 */
	private Double dRel = 0.0;

	/**
	 * <code>dAbs</code> Index of Dependability.
	 */
	private Double dAbs = 0.0;

	/**
	 * <code>iFloor</code> lowest permitted score value in synthesis.
	 */
	private Integer iFloor = 0;

	/**
	 * <code>dMean</code> target value for mean in synthetic data output.
	 */
	private Double dMean = 0.0;

	/**
	 * <code>iCeiling</code> highest permitted score value in synthesis.
	 */
	private Integer iCeiling = 0;
	
	/**
	 * <code>iRepMinimum</code> lowest permitted number of replications.
	 */
	private Integer iRepMinimum = 0;
	
	/**
	 * <code>dRepRange</code> excess over replication minimum as variance.
	 * This serves to calculate the actual number of replications as random process.
	 */
	private Double dRepRange = 0.0;

	/**
	 * <code>darVarianceCoefficients</code> double array of Variance Coefficients.
	 */
	private Double[] darVarianceCoefficients = null;

	/**
	 * <code>logger</code> pointer to org.gs_users.gs_lv.GS_Application logger.
	 */
	private gsLogger logger;

	/**
	 * <code>salNestedNames</code> array list of nested configurations from AnaGroups step 6.
	 */
	private final ArrayList<String> salNestedNames = new ArrayList<>();
	
	/**
	 * Flag forcing non-regular program exit.	
	 */
	private int iProblem = 0;
	
	/**
	 * number of index columns in data file.
	 */
	private int iHighLight = 0;

	/**
	 * step, at which step-up is to resume after an 'Explain'.
	 * i.e. a setback.
	 */
	private int iResume = -1;

	/**
	 * Builds sDictionary and sHDictionary strings;
	 */
	private StringBuilder sbDictionary;

	/**
	 * Placeholder for subject facet.
	 */
	private Facet fSubject = null;

	/**
	 * Counter for missing data
	 */
	private int iMissing = 0;

	/**
	 * Constructor for class <code>Nest</code>
	 *
	 * @param _logger - pointer org.gs_users.gs_lv.GS_Application logger
	 * @param _myMain - pointer to Main class
	 * @param _prefs - pointer to Preferences API
	 */
	public Nest(gsLogger _logger, GS_Application _myMain, Preferences _prefs) {


		cAsterisk = '-';
		sbHFO = new StringBuilder();
		logger = _logger;
		myMain = _myMain;
		sbDictionary = new StringBuilder();
		/*
		 * <code>prefs</code> pointer to <code>Preferences</code> API.
		 */
		salVarianceComponents = new ArrayList<>();
		/*
		 * <code>sPlatform</code> name of current OS platform ('Linux', 'Mac', or 'Windows').
		 */
		myTree = new SampleSizeTree(this, logger, _prefs);
	}

	/**
	 * facet from order number
	 *
	 * @param iOrder - order in sDictionary
	 * @return facets.get(iOrder)- Facet object
	 */
	public Facet getFacet(int iOrder) {
		return farFacets[iOrder];
	}

	/**
	 * facet from char designation
	 *
	 * @param _cDesignation - char designation
	 * @return facets.get(index);
	 */
	public Facet getFacet(char _cDesignation) {
		int index = sDictionary.indexOf(_cDesignation);
		return farFacets[index];
	}

	/**
	 * getter for <code>bSimulate</code>.
	 *
	 * @return bSimulate
	 */
	public Boolean getSimulate() {
		return bSimulate;
	}

	/**
	 * getter for <code>sDictionary</code>.
	 *
	 * @return sDictionary
	 */
	public String getDictionary()	{
		return sDictionary;
	}

	/**
	 * getter for <code>sHDictionary</code>.
	 *
	 * @return sHDictionary
	 */
	public String getHDictionary()	{
		if (sHDictionary == null)
			sHDictionary = sDictionary;
		return sHDictionary;
	}

	/**
	 * setter for <code>sHDictionary</code>.
	 *
	 * @param _sHDictionary  hierarchic facet dictionary
	 */
	public void setHDictionary(String _sHDictionary) {
		sHDictionary = _sHDictionary;
		}

	/**
	 * setter of <code>iAsterisk</code> by facet char Designation.
	 * @param _cAsterisk  char facet designation
	 */
	public void setAsterisk(char _cAsterisk){
		cAsterisk = _cAsterisk;
		myTree.setAsterisk(cAsterisk);
	}

	/**
	 * getter of <code>iAsterisk</code> as hierarchical facet order.
	 * @return iAsterisk  facet index of asterisk
	 */
	public char getAsterisk() {
		return cAsterisk;
	}

	/**
	 * getter for minimal number of replications.
	 * 
	 * @return  iRepMinimum
	 */
	public int get_iMinRep() {
		return iRepMinimum;
	}
	
	/**
	 * getter for Replication range (variance)
	 * 
	 * @return  dRepRange
	 */
	public Double getRepRange() {
		return dRepRange;
	}

	/**
	 * conditional <code>iStep</code> incrementer.
	 * Depending on the <code>iProblem</code> switch:
	 * 		-1:	exit the program
	 * 		 0:	go to the next step
	 * 		greater than 0: go and explain problem
	 */
	public void incrementSteps() {
		if (iStep < -1)
			System.exit(iProblem);
		switch (iProblem) {
			case -1 -> System.exit(iProblem);
			case 0 -> {
				if (!bDawdle)
					iStep++;
			}
			default -> {
				iStep = iResume;
				iProblem = 0;
			}
		}
	}

	/**
	 * getter for <code>iStep</code>, i.e. position in data entry sequence.
	 *
	 * @return iStep
	 */
	public Integer getStep() {
		return iStep;
	}

	/**
	 * setter for <code>bDoOver</code>, i.e. 'script' vs 'manual' parameter entry.
	 *
	 * @param _bDoOver  flag to get input parameters from script
	 */
	public void setDoOver(Boolean _bDoOver) {
		bDoOver = _bDoOver;
	}

	/**
	 * getter for <code>bDoOver</code>, i.e. 'script' vs 'manual' parameter entry.
	 *
	 * @return bDoOver
	 */
	public Boolean getDoOver() {
		return bDoOver;
	}

	/**
	 * setter for <code>bSimulate</code> - flag 'Simulate' vs 'Analyze'.
	 *
	 * @param _bSimulate  flag to do synthesis
	 */
	public void setSimulate(Boolean _bSimulate) {
		bSimulate = _bSimulate;
	}

	/**
	 * Setter for script title
	 *
	 * @param _sTitle  String title of control file
	 */
	public void setTitle(String _sTitle) {
		sScriptTitle = _sTitle;
	}

	/**
	 * getter for <code>iFacetCount</code>.
	 *
	 * @return iFacetCount
	 */
	public Integer getFacetCount() {
		return iFacetCount;
	}

	/**
	 * setter for <code>iFacetCount</code>.
	 *
	 * @param _iFacetCount count of facets
	 */
	public void setFacetCount(Integer _iFacetCount) {
		iFacetCount = _iFacetCount;
	}

	/**
	 * Setter for subject facet
	 *
	 * @param  _fSubject  subject facet
	 */
	public void setSubject(Facet _fSubject) {
		fSubject = _fSubject;
	}
	
	/**
	 * Sets designation char of current facet
	 * 
	 * @param iFacetID  ID of current facet
	 * @param cDesignation  new facet designation char
	 */
	public void setFacetDesignation(int iFacetID, char cDesignation) {
		if (iFacetID == 0)
			fSubject.setDesignation(cDesignation);
		else
			farFacets[iFacetID].setDesignation(cDesignation);
	}
	
	/**
	 * Sets name of current facet
	 * 
	 * @param iFacetID  iFacetID  ID of current facet
	 * @param sFacetName  new facet name
	 */
	public void setFacetName(int iFacetID, String sFacetName) {
		if (iFacetID == 0)
			fSubject.setName(sFacetName);
		else			
			farFacets[iFacetID].setName(sFacetName);
	}
	
	/**
	 * Sets nested status of current facet
	 * 
	 * @param iFacetID   iFacetID  ID of current facet
	 * @param bFacetNested new facet status
	 */
	public void setFacetNested(int iFacetID, Boolean bFacetNested) {
		if (iFacetID == 0)
			fSubject.setNested(bFacetNested);
		else
			farFacets[iFacetID].setNested(bFacetNested);
	}

	/**
	 * getter for <code>sScriptTitle</code>.
	 *
	 * @return sScriptTitle
	 */
	public String getTitle() {
		return sScriptTitle;
	}

	/**
	 * getter for script comments.
	 *
	 * @return string array of comments
	 */
	public String[] getComments() {
		return salComments.toArray(new String[0]);
	}

	/**
	 * setter for script comments.
	 *
	 * @param _comments  flowing text with carriage returns.
	 */
	public void setComments(String _comments) {
		salComments = new ArrayList<>();
		String[] lines = _comments.split("\n");
		Collections.addAll(salComments, lines);
	}

	/**
	 * add commentLine to <code>salComments</code>.
	 *
	 * @param _sCommentLine  line of comment to be included in script
	 */
	public void addComment(String _sCommentLine) {
		salComments.add(_sCommentLine);

	}

	/**
	 * method to collect facet information from scripts
	 * 
	 * @param _f  facet to be added
	 */
	public void addFacet(Facet _f) {
		if (falFacets == null)
			falFacets = new ArrayList<>();
		_f.setAsterisk(false);
		falFacets.add(_f);
		sbDictionary.append(_f.getDesignation());
	}

	/**
	 * parser for 'Effect' line in analyze script
	 *
	 * @param _sEffect EFFECT describes nested or un-nested configuration of Facets
	 */
	public void addEffect(String _sEffect) {
		/*
		 * Builds up 'SampleSizeTree' as design elements are added.
		 * Basically it is a simple lexical analyzer.
		 */

		if ((farFacets == null) || (farFacets.length == 0)) {
			iFacetCount = falFacets.size();
			farFacets = new Facet[iFacetCount];
			for (int i = 0; i<iFacetCount; i++)
				farFacets[i] = falFacets.get(i);
			falFacets = null;
			sDictionary = sbDictionary.toString();
			sbDictionary = new StringBuilder();
		}
		boolean bAsterisk = false;
		char cTarget;
		char cNestor = '$';
		String[] words = _sEffect.trim().split("\\s+");
		String sNest = words[0];
		String[] sss;
		int iFirst = 1;
		Integer iLength = words[0].length();
		boolean bPrimary = true;
		if (sNest.indexOf("*") == 0) // indicates starred face;
		{
			bAsterisk = true;
			if (iLength.equals(1)) {
				sNest = words[1];
				iFirst = 2;
			} else {
				sNest = words[0].substring(1, iLength);
			}
			cAsterisk = sNest.toCharArray()[0];
			myTree.setAsterisk(cAsterisk);
			getFacet(cAsterisk).setAsterisk(true);
		}
		String[] sFacets = sNest.split(":");
		cTarget = sFacets[0].charAt(0);
		if (bAsterisk) {
			cAsterisk = cTarget;
			myTree.setAsterisk(cAsterisk);
		}
		if (sNest.length() > 1) {
			bPrimary = false;
			cNestor = sFacets[1].charAt(0);
			if ((cNestor == cAsterisk) && bReplicate)
				cReplicate = cTarget;
		}
		sbDictionary.append(cTarget);
		this.getFacet(cTarget).setNested(!bPrimary);
		this.getFacet(cTarget).setNestor(cNestor);
		salNestedNames.add(sNest);
		sbHFO.append(cTarget);
		sHDictionary = sbHFO.toString();
		sss = Arrays.copyOfRange(words,iFirst, words.length);
		myTree.setDictionary(sDictionary);
		sHDictionary = sDictionary;
		myTree.setHDictionary(sHDictionary);
		myTree.addSampleSize(cTarget, sss);
		iNestCount++;
	}

	/**
	 * setter for <code>sFormat</code>.
	 *
	 */
	public void addFormat() {
		/*
		 * <code>sFormat</code> official format specifications  for urGenova (see urGenova manual).
		 */
		sHDictionary = sbDictionary.toString();
	}

	/**
	 * setter for <code>sProcess</code>
	 *
	 */
	public void addProcess() {
		/*
		 * <code>sFormat</code> official process specifications for urGenova (see urGenova manual).
		 */
	}

	/**
	 * creates observable list of primary nesting from facet input
	 * for use in 'nesting grab and drop' (Step 7)
	 *
	 * @return ObservableList
	 */
	public ObservableList<String> getNests() {
		if (sarNestedNames == null || sarNestedNames.length == 0) {
			if (salNestedNames.isEmpty())
				return null;
			iNestCount = salNestedNames.size();
			sarNestedNames = new String[iNestCount];
			for (int i = 0; i < iNestCount; i++)
				sarNestedNames[i] = salNestedNames.get(i);
		}
		ArrayList<String> result = new ArrayList<>();
		for (char c : sHDictionary.toCharArray())
			result.add(sarNestedNames[sHDictionary.indexOf(c)]);
		return FXCollections.observableArrayList(result);
	}

	/**
	 * translates string array from AnaGroups step 7 into <code>Nest</code>,
	 * and <code>SampleSizeTree</code> data structures
	 *
	 * @param _nests  primary nested facet combinations
	 */
	public void setNests(String[] _nests) {
		/*
		 * The term 'nest' here is confusing. It has nothing to do with the similar class name.
		 * It is a historic relic for 'nested arrangements', and needs to be distinguished
		 * from the 'Nest'. The author apologizes.
		 */
		// convert observable list back to string array
		iNestCount = _nests.length;
			myTree.setHDictionary(sHDictionary);
		String sNest;
		sarNestedNames = new String[iNestCount];
		for (int i = 0; i < iNestCount; i++) {
			sNest = _nests[i];
			sarNestedNames[i] = sNest;
			char cFacet = sNest.toCharArray()[0];
			Facet f = getFacet(cFacet);
			f.doNesting(sNest);
		}
		myTree.setNests(_nests);
		iNestCount = _nests.length;
	}

	/**
	 * setter of <code>Scene</code>.
	 *
	 * @param _scene  <code>Scene</code> to be displayed
	 */
	public void setScene(Scene _scene) {
		scene = _scene;
	}

	/**
	 * getter of <code>Scene</code>.
	 *
	 * @return scene  to be displayed
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * setter of primary <code>Stage</code>.
	 *
	 */
	public void setStage() {
		/*
		 * <code>primaryStage</code> display stage of GUI.
		 */
	}

	/**
	 * setter of <code>sFileName</code>.
	 *
	 */
	public void setFileName() {
		/*
		 * <code>sFileName</code> path of control file.
		 */
	}

	/**
	 * getter of <code>bDawdle</code>.
	 *
	 * @return bDawdle flag to stepper
	 */
	public Boolean getDawdle() {
		return bDawdle;
	}

	/**
	 * getter of <code>bVarianceDawdle</code>.
	 *
	 * @return bVarianceDawdle
	 */
	public Boolean getVarianceDawdle() {
		return bVarianceDawdle;
	}

	/**
	 * setter of <code>bDawdle</code>.
	 *
	 * @param _dawdle  flag to stepper
	 */
	public void setDawdle(Boolean _dawdle) {
		bDawdle = _dawdle;
	}

	/**
	 * setter of <code>bVarianceDawdle</code>.
	 *
	 * @param _Dawdle flag to stepper
	 */
	public void setVarianceDawdle(Boolean _Dawdle) {
		bVarianceDawdle = _Dawdle;
	}

	/**
	 * passes <code>_group</code> via <code>myMain</code> to <code>controller</code>, i.e. GUI.
	 *
	 * @param _group display group
	 */
	public void show(Group _group) {
		myMain.show(_group);
	}

	/**
	 * getter of <code>sControlFileName</code>.
	 *
	 * @return sControlFileName
	 */
	public String getControlFileName() {
		return "control.txt";
	}

	/**
	 * getter of <code>sDataFileName</code>.
	 *
	 * @return sDataFileName
	 */
	public String getDataFileName() {
		return "~data.txt";
	}

	/**
	 * setter of <code>dGrandMeans</code>.
	 *
	 * @param _means  value of grand mean
	 */
	public void setGrandMeans(Double _means) {
		dGrandMeans = _means;
	}

	/**
	 * getter of <code>dGrandMeans</code>.
	 *
	 * @return dGrandMeans
	 */
	public Double getGreatMeans() {
		return dGrandMeans;
	}

	/**
	 * adds new <code>VarianceComponent</code> to <code>salVarianceComponents</code> by parsing
	 * ANOVA line from urGENOVA output.
	 *
	 * @param _line  string from ANOVA table
	 */
	public void setVariance(String _line) {
		salVarianceComponents.add(new VarianceComponent(this, _line, logger));
	}

	/**
	 * Essential routine to establish the logic of facet organization. Is
	 * called from AnaGroups and SynthGroups after Nesting step
	 */
	public void setOrder() {
		int iFacetCount = sHDictionary.length();
		for (int i = 0; i < iFacetCount; i++) {
			Facet f = farFacets[i];
			char c = f.getDesignation();
			Integer j = sDictionary.indexOf(c);
			f.setID(j);
			farFacets[j] = f;
		}
		myTree.setDictionary(sDictionary);
		myTree.setFacets(farFacets);
		myTree.setFacetCount(iFacetCount);
		myTree.setNests(sarNestedNames);
	}

	public void fillEffects(){
		for (String s : sarNestedNames)
			this.addEffect(s);
	}

	/**
	 * getter for array of all facets.
	 *
	 * @return farFacets  all facets of the study
	 */
	public Facet[] getFacets() {
		return farFacets;
	}

	/**
	 * getter for number of primary Effects (facet nestings)
	 * @return iNestCount  number of primary Effects
	 */
	public Integer getNestCount() {
		return iNestCount;
	}

	/**
	 * sets up facet types for generalizability calculations
	 */
	public void G_setFacets() {
		if ((sarNestedNames == null) || (sarNestedNames.length ==0)) {
			int L = salNestedNames.size();
			sarNestedNames = new String[L];
			for (int i = 0; i < L; i++)
				sarNestedNames[i] = salNestedNames.get(i);
		}
		iNestCount = sarNestedNames.length;
		
		char cDiff = 'x';

		/*
		 * First, identify facet of differentiation
		 */
		for (Facet f : farFacets) {
			if (f.getOrder() == 0) {
				f.setFacetType('d');
				cDiff = f.getDesignation();
			}
			else
				f.setFacetType('g');
		}

		/*
		 * Then identify facets of stratification
		 */

		String sNest;
		for (int i = 0; i < iNestCount; i++) {
			sNest = sarNestedNames[i];
			if (sNest.indexOf(cDiff) == 0) {
				/*
				 * That's a nest with the stratified facet of differentiation
				 */
				char[] cFacets = sNest.replace(":",  "").toCharArray();
				for (char c : cFacets)
					if (c != cDiff)
						getFacet(c).setFacetType('s');

			}
		}
		
	}

	/**
	 * Setter of Facet array
	 */
	public void createFacets() {
		farFacets = new Facet[iFacetCount];
		farFacets[0] = fSubject;
		for (int i = 1; i < iFacetCount; i++)
			farFacets[i] = new Facet(this);
	}

	/**
	 * Create basic dictionary (in original order of facets)
	 */
	public void createDictionary() {
		StringBuilder sb = new StringBuilder();
		if (sDictionary == null) {
			for (Facet f : farFacets)
				sb.append(f.getDesignation());
			sDictionary = sb.toString();
		}
		sHDictionary = sDictionary;
	}

	/**
	 * Sets facet levels of all facets (for D-Studies
	 */
	public void setLevels() {
		for (Facet f : farFacets)
			f.setFacetLevel();
	}

	/**
	 * Handles integer conversion for the floor and ceiling score values
	 * in the synthesis, and saves them appropriately (Synthesis).
	 *
	 * @param _sTarget  <code>floor</code> or <code>ceiling</code> respectively
	 * @param _sValue  value as text
	 */
	private void saveInteger(String _sTarget, String _sValue) {
		int iValue = 0;
		if (_sValue != null)
			iValue = Integer.parseInt(_sValue);
		switch (_sTarget) {
			case "cFloor" -> iFloor = iValue;
			case "cCeiling" -> iCeiling = iValue;
			case "cRepMin" -> iRepMinimum = iValue;
		}
	}

	/**
	 * saves intended 'score' mean value from text to Double, (Synthesis)
	 * @param _sTarget  usually 'cMean'
	 * @param _sValue  value of intended mean score in text form
	 */
	private void saveDouble(String _sTarget, String _sValue) {
		double dValue = 0.0;
		if (_sValue != null)
			dValue = Double.parseDouble(_sValue);
		switch (_sTarget) {
		case "cMean":
			dMean = dValue;
			break;
		case "cRepRange":
			dRepRange = dValue;
		default:
			break;
		}
	}

	/**
	 * General purpose variable saving method.
	 *
	 * @param _sType  variable type
	 * @param _sTarget  variable name
	 * @param _sValue  variable value
	 */
	public void saveVariable(String _sType, String _sTarget, String _sValue) {
		switch (_sType) {
			case "Integer" -> saveInteger(_sTarget, _sValue);
			case "Double" -> saveDouble(_sTarget, _sValue);
		}
	}

	/**
	 * This method should probably be in org.gs_users.gs_lv.utilities..filer from a logic point of view
	 * but was placed in nests for convenience.
	 * It redacts and formats the prose for G- and D-Studies into the StringBuilder 'sbResult'.
	 *
	 * @param sbResult  StringBuilder, to which the prose has to be added
	 */
	public void formatResults(StringBuilder sbResult) {

		StringBuilder sb = new StringBuilder();
		//double dAbsolute = 0.0; // total sum of absolute values of weighted
								// components
		//double dFactual = 0.0; // total sum of weighted components > 0.0
		double dTemp; // temporary variable;
		double dS2_t = 0.0; // sigma2(tau)
		double dS2_d = 0.0; // sigma2(delta)
		double dS2_D = 0.0; // sigma2(Delta)


		for (Facet f : farFacets) {
			if (f.getFacetType() == 'g'){
					sb.append(f.getDiagDesignation());
			}
		}
		sb.append("\n");

		try {
			sbResult.append("\n\n-----------------------------------\n");
			sbResult.append("Results ").append(sTitle).append("\n\n");
			sbResult.append(sb);
		} catch (Exception e1) {
			logger.log("Nest", 1013, "", e1);
		}
		sTitle = " D-Study.";
		for (VarianceComponent vc : salVarianceComponents) {
			vc.doCoefficient(sbResult);
			dTemp = vc.getVarianceComponent() / vc.getDenominator();
			//dAbsolute += Math.abs(dTemp);
			if (dTemp > 0.0) {
				//dFactual += dTemp;
				if (vc.b_tau())
					dS2_t += dTemp;
				if (vc.b_delta())
					dS2_d += dTemp;
				if (vc.b_Delta())
					dS2_D += dTemp;
			}
		}
		sbResult.append("\n");

		sbResult.append("σ²(τ) = ").append(String.format("%.4f", dS2_t)).append("\n");
		sbResult.append("σ²(δ) = ").append(String.format("%.4f", dS2_d)).append("\n");
		sbResult.append("σ²(Δ) = ").append(String.format("%.4f", dS2_D)).append("\n");
		dRel = dS2_t / (dS2_t + dS2_d);
		dAbs = dS2_t / (dS2_t + dS2_D);
		try {
			sbResult.append("\nGENERALIZABILITY COEFFICIENTS:\n\n");
			sbResult.append("Eρ²      	= ").append(String.format("%.2f", dRel)).append("\n");
			sbResult.append("Φ           = ").append(String.format("%.2f", dAbs)).append("\n");
		} catch (Exception e) {
			logger.log("Nest", 1042, "", e);
		}
	}

	/**
	 * getter for Generalizability Coefficient.
	 *
	 * @return dRel  Double value of Generalizability Coefficient
	 */
	public Double getRho() {
		return dRel;
	}

	/**
	 * getter for Index of Reliability.
	 *
	 * @return dAbs  Double value of Index of Reliability
	 */
	public Double getPhi() {
		return dAbs;
	}

	/**
	 * setter for step used in AnaGroups and SynthGroups respectively.
	 *
	 * @param _iStep  step in data entry sequence
	 */
	public void setStep(Integer _iStep) {
		iProblem = 0;
		iStep = _iStep;
	}

	/**
	 * getter of primary Effect name.
	 *
	 * @param iSAR index to string array
	 * @return sarNestedNames[iSAR]  primary effect name
	 */
	public String getNestedName(Integer iSAR) {
		return sarNestedNames[iSAR];
	}
	
	/**
	 * Getter of nested names array
	 * 
	 * @return nested names array
	 */
	public String[] getNestedNames() {
		int i = 0;
		sarNestedNames = new String[salNestedNames.size()];
		for (String s : salNestedNames)
			sarNestedNames[i++] = s;
		return sarNestedNames;
	}

	/**
	 * getter of a facet's 'Nestor' facet.
	 *
	 * @param cF  char designation of facet
	 * @return char designation of Nestor facet
	 */
	public Character getNestor(char cF) {
		return getFacet(cF).getNestor();
	}

	/**
	 * setter of individual variance coefficient in array.
	 *
	 * @param i  position in array
	 * @param _dVC  value of variance coefficient.
	 */
	public void setVariancecoefficient(int i, Double _dVC) {
		if (_dVC == null)
			return;
		if (darVarianceCoefficients == null) {
			darVarianceCoefficients = new Double[myTree.getConfigurationCount()];
		}
		darVarianceCoefficients[i] = _dVC;
	}

	/**
	 * getter of variance coefficient from array.
	 *
	 * @param i position in array
	 * @return Double value of variance coefficient
	 */
	public Double getVarianceCoefficient(int i) {
		if ((darVarianceCoefficients != null) && (i < darVarianceCoefficients.length))
			return darVarianceCoefficients[i];
		else
			return 0.0;
	}

	/**
	 * getter of score ceiling value
	 *
	 * @return iCeiling
	 */
	public Integer getCeiling() {
		return iCeiling;
	}

	/**
	 * getter of score floor value.
	 *
	 * @return iFloor
	 */
	public Integer getFloor() {
		return iFloor;
	}

	/**
	 * getter of intended score mean
	 *
	 * @return Double value
	 */
	public Double getMean() {
		return dMean;
	}

	/**
	 * parser for reading score anchors from script (SynthGroups).
	 *
	 * @param sValue  text line from script
	 */
	public void addAnchors(String sValue) {
		String[] sAnchors = sValue.split("\\s+");
		int iAnchors = sAnchors.length;
		iFloor = Integer.parseInt(sAnchors[0]);
		dMean = Double.parseDouble(sAnchors[1]);
		iCeiling = Integer.parseInt(sAnchors[2]);
		if (iAnchors > 3) {
			iRepMinimum = Integer.parseInt(sAnchors[3]);
			dRepRange = Double.parseDouble(sAnchors[4]);
		}
	}

	/**
	 * parser for reading variance components from script (SynthGroups).
	 *
	 * @param sValue  text line from script
	 */
	public void addVariances(String sValue) {
		String[] sVariances = sValue.split("\\s+");
		int ivCount = sVariances.length;
		darVarianceCoefficients = new Double[ivCount];
		for (int i = 0; i < ivCount; i++)
			darVarianceCoefficients[i] = Double.parseDouble(sVariances[i]);
	}

	/**
	 * getter of variance coefficient array size.
	 *
	 * @return array size
	 */
	public Integer getVcDim() {
		return darVarianceCoefficients.length;
	}

	/**
	 * getter for SampleSizeTree.
	 *
	 * @return current SampleSizeTree
	 */
	public SampleSizeTree getTree() {
		return myTree;
	}

	/**
	 * Generates facet combinations responsible for variance
	 * components, including their appropriate syntax.
	 * The object is somewhat occult. It gets only called once in
	 * steps.SynthGroups.VarianceComponentsGroup line 887.
	 * It sets up the string array sarNestedNames in Nest and the
	 * 'aNode' array 'nodes' in 'nest'.
	 */
	public void doComponents() {
		myTree.setFacetCount(sDictionary.length());
	}

	/**
	 * Setter for replication flag.
	 * @param _bReplicate replication flag
	 */
	public void setReplicate(Boolean _bReplicate) {
		bReplicate = _bReplicate;
	}

	/**
	 * Getter for replication flag.
	 * 
	 * @return  replication flag.
	 */
	public Boolean getReplicate() {
		return bReplicate;
	}

	/**
	 * Getter for cReplicate
	 * 
	 * @return cReplicate
	 */
	public char get_cRep() {
		return cReplicate;
	}
	
	/**
	 * Setter of termination flag (<code>bAbandon</code>).
	 * 
	 * @param _iProblem  problem identifier in replication
	 */
	public void setProblem(int _iProblem) {
		iProblem = _iProblem;
		bDawdle = false;
	}
	
	/**
	 * Getter of termination flag (<code>iProblem</code>).
	 * 
	 * @return <code>bAbandon</code>
	 */
	public int getProblem() {
		return iProblem;
	}

	/**
	 * Setter for cReplicate
	 * 
	 * @param _cRep  cReplicate
	 */
	public void set_cRep(char _cRep) {
		if(bReplicate)
			cReplicate = _cRep;
	}
	
	/**
	 * Returns designation of replicating facet, or '-' if none.
	 * 
	 * @return  <code>cReplicate</code>, or '-'
	 */
	public char getRepChar() {
		if (bReplicate)
			return cReplicate;
		else
			return '-';
	}
	
	/**
	 * Getter for lowest possible value for number of replications.
	 * 
	 * @return  iRepMinimum
	 */
	public Integer getRepMin() {
		return iRepMinimum;
	}

	/**
	 * Setter for number of index columns in data file.
	 * 
	 * @param  _iHighLight  offset after index columns
	 */
	public void setHighlight(int _iHighLight) {
		iHighLight = _iHighLight;
	}
	
	/**
	 * Getter  for number of index columns in data file.
	 * 
	 * @return  iHighLight  offset after index columns;
	 */
	public int getHighLight() {
		return iHighLight;
	}
	
	/**
	 * Sets step, at which AnaGroups has to resume after a design problem
	 * 
	 * @param _iResume  set number to resume at.
	 */
	public void setResume(int _iResume) {
		iResume = _iResume;
	}

	/**
	 * Setter for bReNest (whether nesting info is reused)
	 * 
	 * @param _bReNest  boolean flag
	 */
	public void setReNest(Boolean _bReNest) {
		if (!_bReNest) {
			iNestCount = 0;
			sarNestedNames = null;

		}
	}

	/**
	 * Dummy switch constant for internal control
	 * This is a VERY IMPORTANT SWITCH! It can only be changed by directly
	 * entering the value here. It controls, whether exceptions get logged, or
	 * cause a stack trace printout in debugging mode.
	 *
	 * @return Boolean switch for trace mode
	 */
	public Boolean getStackTraceMode() {
		return true;
	}

	/**
	 * Test if previous nest setup was complete
	 *
	 * @return true, if complete
	 */
	public Boolean complete(){
		return (sarNestedNames.length != 0);
	}

	/**
	 * increments missing data count
	 */
	public void incrementMissing(){
		iMissing++;
	}

	/**
	 * Returns missing data count
	 *
	 * @return int missing data count
	 */
	public int getMissingDataCount(){
		return iMissing;
	}
}

