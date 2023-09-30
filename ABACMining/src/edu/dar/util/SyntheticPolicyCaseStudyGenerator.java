/**
 *  Mining Attribute-Based Access Control Policies
 * Copyright (C) 2014 Zhongyuan Xu
 * Copyright (C) 2014 Scott D. Stoller
 * Copyright (c) 2014 Stony Brook University
 * Copyright (c) 2014 Research Foundation of SUNY
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package edu.dar.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import edu.dar.algo.ABACMiner;
import edu.dar.util.ProbabilityDistribution;

public class SyntheticPolicyCaseStudyGenerator {

	// random number generator
	public static Random randomGen;
	// number of user attributes
	public static int NuserAtt = 8;
	// number of resource attributes
	public static int NresAtt = 8;
	// minimum number of satisfying users per rule
	public static int NuserPerRule = 16;
	// number of operations
	public static int Nop = 8;

	// number of attribute groups, only user and resource attributes from the
	// same group are comparable
	public static int NComparableAttrGroups = 7;
	// array to keep the cardinalities of each group
	public static int[] groupCardinality = new int[NComparableAttrGroups];

	// arrays to keep the cardinalities of each attribute
	public static int[] uCardinality = new int[NuserAtt];
	public static int[] rCardinality = new int[NresAtt];

	// arrays to keep the group number of each attribute
	public static int[] uGroupNumber = new int[NuserAtt];
	public static int[] rGroupNumber = new int[NresAtt];

	// array lists to keep the value distribution of each attribute
	public static ArrayList<ZipfDistrib> userAttrToValueDistrib = new ArrayList<ZipfDistrib>();
	public static ArrayList<ZipfDistrib> resourceAttrToValueDistrib = new ArrayList<ZipfDistrib>();
	public static ArrayList<ZipfDistrib> groupToValueDistrib = new ArrayList<ZipfDistrib>();

	// arrays to keep the shift of each attribute
	public static int[] attributeShift = new int[NComparableAttrGroups];

	// assign values to attributes
	public static int NadditionalAttribWithVal = 2;

	// the overlap percentage between the original and overlap rules
	public static double pOverlapVal = 0.8;

	// arrays to keep whether each attribute is single-valued or multi-valued
	public static boolean[] uattrIsMulti = new boolean[NuserAtt];
	public static boolean[] rattrIsMulti = new boolean[NresAtt];

	// user identity will increment by 1 for each new user
	public static int newUserIdentity = 0;
	// resource identity will increment by 1 for each new resource
	public static int newResrIdentity = 0;
	
	public static double botFrac = 0.1;

	public static Config config = new Config();

	// debug mode switch
	public static boolean debug = true;

	static {
		randomGen = new Random(System.currentTimeMillis());
	}

	public static ArrayList<Rule> generateCaseStudy(String outputFile,
			String outputAddFile, int Nrule, boolean debug,
			boolean checkSatisfy, int minNumConjuncts, int minNumConstraints,
			int minConjunctSize, int minNumOps, double pOverlapRule) {
		// keep users, resources and rules in ArrayLists
		ArrayList<String> users = new ArrayList<String>();
		ArrayList<String> resources = new ArrayList<String>();
		ArrayList<Rule> rules = new ArrayList<Rule>();

		// keep distinguishing users and resources. distinguishing users and
		// resources are used to make the conjuncts and constraints hard to be
		// eliminated
		ArrayList<String> distinguishUsers = new ArrayList<String>();
		ArrayList<String> distinguishResources = new ArrayList<String>();

		// only attributes in the same comparable attribute group are comparable
		ArrayList<Pair<HashSet<Integer>, HashSet<Integer>>> comparableAttrGroups = new ArrayList<Pair<HashSet<Integer>, HashSet<Integer>>>();

		// put attributes into each comparable group
		for (int i = 0; i < NComparableAttrGroups; i++) {
			HashSet<Integer> uattrSet = new HashSet<Integer>();
			uattrSet.add(i);
			HashSet<Integer> rattrSet = new HashSet<Integer>();
			rattrSet.add(i);
			comparableAttrGroups
					.add(new Pair<HashSet<Integer>, HashSet<Integer>>(uattrSet,
							rattrSet));
			uGroupNumber[i] = i;
			rGroupNumber[i] = i;
		}
		// randomly pick a group and put the last user attribute into that group
		for (int i = NComparableAttrGroups; i < NuserAtt; i++) {
			int index = randomGen.nextInt(NComparableAttrGroups);
			comparableAttrGroups.get(index).getFirst().add(i);
			uGroupNumber[i] = index;
		}
		// randomly pick a group and put the last resource attribute into that
		// group
		for (int i = NComparableAttrGroups; i < NresAtt; i++) {
			int index = randomGen.nextInt(NComparableAttrGroups);
			comparableAttrGroups.get(index).getSecond().add(i);
			rGroupNumber[i] = index;
		}

		// generate cardinality and shift for each group
		for (int i = 0; i < NComparableAttrGroups; i++) {
			groupCardinality[i] = DattribCardinality(Nrule);
			attributeShift[i] = randomGen.nextInt(DattribCardinality(Nrule));
		}

		// generate value distribution for each group
		for (int i = 0; i < NComparableAttrGroups; i++) {
			double skew = zipfSkewSelector(groupCardinality[i]);
			ZipfDistrib zipf = new ZipfDistrib(groupCardinality[i], skew,
					randomGen.nextLong());
			groupToValueDistrib.add(zipf);
		}

		// assign value distribution for each user attribute
		for (int i = 0; i < NuserAtt; i++) {
			uCardinality[i] = groupCardinality[uGroupNumber[i]];
			userAttrToValueDistrib
					.add(groupToValueDistrib.get(uGroupNumber[i]));
		}

		// assign value distribution for each user attribute
		for (int i = 0; i < NresAtt; i++) {
			rCardinality[i] = groupCardinality[rGroupNumber[i]];
			resourceAttrToValueDistrib.add(groupToValueDistrib
					.get(rGroupNumber[i]));
		}

		//System.out.println("Assign Multi-Value or Single-Value");
		while (true) {
			// assign value type: multi or single to attributes	
			for (int i = 0; i < uCardinality.length; i++) {
				uattrIsMulti[i] = isMulti();
				if (uattrIsMulti[i]) {
					config.getUserAttrSet().put(
							"uattr" + i,
							new Attribute(AttributeType.UserAttr,
									ValueType.Set, "uattr" + i, false));
				} else {
					config.getUserAttrSet().put(
							"uattr" + i,
							new Attribute(AttributeType.UserAttr,
									ValueType.Single, "uattr" + i, false));
				}
			}
			// check if there is one group that contains no multi-valued user
			// attributes but has multi-valued resource attribute
			boolean unsat = false;
			for (int i = 0; i < rCardinality.length; i++) {
				rattrIsMulti[i] = isMulti();
				if (rattrIsMulti[i]) {
					config.getResourceAttrSet().put(
							"rattr" + i,
							new Attribute(AttributeType.ResourceAttr,
									ValueType.Set, "rattr" + i, false));
					for (Pair<HashSet<Integer>, HashSet<Integer>> attrPair : comparableAttrGroups) {
						if (attrPair.getSecond().contains(i)) {
							boolean hasMultiUserAttr = false;
							for (Integer u : attrPair.getFirst()) {
								if (uattrIsMulti[u]) {
									hasMultiUserAttr = true;
									break;
								}
							}
							if (hasMultiUserAttr == false) {
								unsat = true;
							}
							break;
						}
					}
					if (unsat == true) {
						break;
					}
				} else {
					config.getResourceAttrSet().put(
							"rattr" + i,
							new Attribute(AttributeType.ResourceAttr,
									ValueType.Single, "rattr" + i, false));
				}
			}
			if (unsat == true) {
				continue;
			} else {
				break;
			}
		}

		config.getUserAttrSet().put(
				"uid",
				new Attribute(AttributeType.UserAttr, ValueType.Single, "uid",
						false));
		config.getResourceAttrSet().put(
				"rid",
				new Attribute(AttributeType.ResourceAttr, ValueType.Single,
						"rid", false));

		if (debug) {
			System.out.println("===========================");
			System.out
					.println("User Attributes Cardinality, Multinality and Group Number");
			for (int i = 0; i < uCardinality.length; i++) {
				if (uattrIsMulti[i]) {
					System.out.print("(" + uCardinality[i] + "," + "Multi"
							+ "," + uGroupNumber[i] + ") ");
				} else {
					System.out.print("(" + uCardinality[i] + "," + "Single"
							+ "," + uGroupNumber[i] + ") ");
				}
			}

			System.out.println("===========================");
			System.out
					.println("Resource Attributes Cardinality, Multinality and Group Number");
			for (int i = 0; i < rCardinality.length; i++) {
				if (rattrIsMulti[i]) {
					System.out.print("(" + rCardinality[i] + "," + "Multi"
							+ "," + rGroupNumber[i] + ") ");
				} else {
					System.out.print("(" + rCardinality[i] + "," + "Single"
							+ "," + rGroupNumber[i] + ") ");
				}
			}
			System.out.println();
		}

		// generate Nrule numbers of rules
		for (int i = 1; i <= Nrule; i++) {
			Rule rho = new Rule();
			rules.add(rho);
			int nUConjunct = DnumConjunct(minNumConjuncts);
			int nRConjunct = DnumConjunct(minNumConjuncts);
			int nConstraint = DnumConstraint(minNumConstraints);
			int nOp = DnumOp(minNumOps);

			if (debug) {
				System.out.println("===========================");
				System.out.println("Generating the " + i + "th rule");
				System.out.println("Number of UAE conjuncts: " + nUConjunct);
				System.out.println("Number of RAE conjuncts: " + nRConjunct);
				System.out.println("Number of constraints: " + nConstraint);
				System.out.println("Number of operations: " + nOp);
			}

			// adding operations to the rule
			//System.out.println("Adding Operations");
			while (rho.getOps().size() < nOp) {			
				rho.getOps().add("op" + Integer.toString(Dop()));
			}

			if (debug) {
				System.out.println("Operation Set:" + rho.getOps());
			}

			// generate UAE
			HashSet<String> usedUserAttrs = new HashSet<String>();
			// generate each user attribute conjunct
			for (int j = 0; j < nUConjunct; j++) {
				Integer a = Dattrib();
				//System.out.println("Selecting User Attributes");
				if (usedUserAttrs.size() == NuserAtt) {
					return null;
				}
				while (usedUserAttrs.contains("uattr" + a)) {
					a = Dattrib();
				}
				usedUserAttrs.add("uattr" + a);
				int conjunctSize = DconjunctSize(uCardinality[a],
						minConjunctSize);
				// add values to single user attribute conjunct
				if (!uattrIsMulti[a]) {
					HashSet<String> s = new HashSet<String>();
					//System.out.println("Selecting Single User Attribute Values");
					int iteration = 0;
					while (s.size() < conjunctSize) {
						iteration++;
						if (iteration >= uCardinality[a]) {
							return null;
						}
						int v = DdataVal(a, userAttrToValueDistrib,
								AttributeType.UserAttr);
						s.add(Integer.toString(v));
					}
					rho.adduConjunct(new AttrValConjunct("uattr"
							+ Integer.toString(a), s, null, "="));
				} else {
					// add values to multi user attribute conjunct
					HashSet<HashSet<String>> sSet = new HashSet<HashSet<String>>();
					//System.out.println("Selecting Multi User Attribute Values");
					int iteration = 0;
					while (sSet.size() < conjunctSize) {
						iteration++;
						if (iteration >= uCardinality[a]) {
							return null;
						}
						HashSet<String> vSet = new HashSet<String>();
						int sz = DmultiSz(uCardinality[a]);
						while (vSet.size() < sz) {
							iteration++;
							if (iteration >= uCardinality[a]) {
								return null;
							}
							vSet.add(Integer.toString(DdataVal(a,
									userAttrToValueDistrib,
									AttributeType.UserAttr)));
						}
						sSet.add(vSet);
					}
					rho.adduConjunct(new AttrValConjunct("uattr"
							+ Integer.toString(a), null, sSet, "="));
				}
				if (debug) {
					System.out.println("The " + j + "th uconjunct has "
							+ conjunctSize + " values:" + rho.getUAE().get(j));
				}
			}
			// generate RAE
			HashSet<String> usedResAttrs = new HashSet<String>();
			for (int j = 0; j < nRConjunct; j++) {
				Integer a = Dattrib();
				
				//System.out.println("Selecting Resource Attributes");
				if (usedResAttrs.size() == NresAtt) {
					return null;
				}
				while (usedResAttrs.contains("rattr" + a)) {
					a = Dattrib();
				}
				usedResAttrs.add("rattr" + a);
				int conjunctSize = DconjunctSize(rCardinality[a],
						minConjunctSize);
				
				// add values to single resource attribute conjunct
				if (!rattrIsMulti[a]) {
					HashSet<String> s = new HashSet<String>();
					//System.out.println("Selecting Single Resource Attributes");
					int iteration = 0;
					while (s.size() < conjunctSize) {
						iteration++;
						if (iteration >= rCardinality[a]) {
							return null;
						}
						int v = DdataVal(a, resourceAttrToValueDistrib,
								AttributeType.ResourceAttr);
						s.add(Integer.toString(v));
					}
					rho.addrConjunct(new AttrValConjunct("rattr"
							+ Integer.toString(a), s, null, "="));
				} else { // add values to multi resource attribute conjunct
					HashSet<HashSet<String>> sSet = new HashSet<HashSet<String>>();
					//System.out.println("Selecting Multi Resource Attributes");
					int iteration = 0;
					while (sSet.size() < conjunctSize) {
						iteration++;
						if (iteration >= rCardinality[a]) {
							return null;
						}
						HashSet<String> vSet = new HashSet<String>();
						int sz = DmultiSz(rCardinality[a]);
						while (vSet.size() < sz) {
							iteration++;
							if (iteration >= uCardinality[a]) {
								return null;
							}
							vSet.add(Integer.toString(DdataVal(a,
									resourceAttrToValueDistrib,
									AttributeType.ResourceAttr)));
						}
						sSet.add(vSet);
					}
					rho.addrConjunct(new AttrValConjunct("rattr"
							+ Integer.toString(a), null, sSet, "="));
				}
				if (debug) {
					System.out.println("The " + j + "th rconjunct has "
							+ conjunctSize + " values:" + rho.getRAE().get(j));
				}
			}

			// generate constraints
			HashSet<Pair<String, String>> generatedConstraint = new HashSet<Pair<String, String>>();
			HashSet<String> userAttrsInConstraint = new HashSet<String>();
			HashSet<String> resAttrsInConstraint = new HashSet<String>();
			String lhs = null, rhs = null;
			Operator op = null;
			int iteration = 0;
			while (generatedConstraint.size() < nConstraint) {
				iteration++;
				if (iteration > 100) {
					return null;
				}
				//System.out.println("Generating constraints");
				while (true) {
					int uAttr = DattribCon();
					boolean found = true;
					lhs = "uattr" + Integer.toString(uAttr);
					int rAttr = -1;
					for (int k = 0; k < NComparableAttrGroups; k++) {
						if (comparableAttrGroups.get(k).getFirst()
								.contains(uAttr)) {
							rAttr = randomElement(comparableAttrGroups.get(k)
									.getSecond());
							rhs = "rattr" + Integer.toString(rAttr);
							break;
						}
					}
					if (uattrIsMulti[uAttr] == true
							&& rattrIsMulti[rAttr] == true) {
						op = Operator.SUPSETEQ;
					} else if (uattrIsMulti[uAttr] == false
							&& rattrIsMulti[rAttr] == false) {
						op = Operator.EQUALS;
					} else if (uattrIsMulti[uAttr] == true
							&& rattrIsMulti[rAttr] == false) {
						op = Operator.IN;
					} else {
						found = false;
					}
					if (found) {
						if (debug) {
							System.out.println("Generating constrait with " + lhs
									+ " " + op +  " " + rhs);
						}
						break;
					}				
				}

				// check if the generated constraint is satisfiable
				if (!userAttrsInConstraint.contains(lhs)
						&& !resAttrsInConstraint.contains(rhs)) {
					AttrAttrConjunct c = new AttrAttrConjunct(lhs, rhs, op);
					boolean satisfied = true;
					// if both attributes are used in some conjuncts
					if ((usedUserAttrs.contains(lhs) && usedResAttrs
							.contains(rhs))) {
						AttrValConjunct uConjunct = null, rConjunct = null;
						for (AttrValConjunct v : rho.getUAE()) {
							if (v.getLHS().equals(lhs)) {
								uConjunct = v;
								break;
							}
						}
						for (AttrValConjunct v : rho.getRAE()) {
							if (v.getLHS().equals(rhs)) {
								rConjunct = v;
								break;
							}
						}
						switch (op) {
						case EQUALS:
							if (!uConjunct.getRHS().equals(rConjunct.getRHS())) {
								satisfied = false;
							}
							break;
						default:
							break;
						}
					}
					if (!satisfied) {
						continue;
					}
					rho.getCon().add(c);
					generatedConstraint.add(new Pair<String, String>(lhs, rhs));
					userAttrsInConstraint.add(lhs);
					resAttrsInConstraint.add(rhs);
					if (debug) {
						System.out.println("Add Constraint:" + c);
					}
				}
			}
					
			// generate nUser numbers of users to satisfy the rule
			int nUser = DnumUserPerRule();
			if (debug) {
				System.out.println("Number of satisfying users: " + nUser);
			}

			// let current users satisfy UAE
			for (String u : users) {
				// checkUserSatisfiesRule will
				if (checkUserSatisfiesRule(u, rho.getUAE(), rho.getRAE(),
						rho.getCon(), config)) {
					rho.getSatUsers().add(u);
				}
			}
			//System.out.println("Generating Sat Users");
			while (rho.getSatUsers().size() < nUser) {
				String u = newUser();
				HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
				HashSet<String> uids = new HashSet<String>();
				uids.add(u);
				h.put("uid", uids);
				// assign user attribute values to satisfy UAE
				for (AttrValConjunct c : rho.getUAE()) {
					if (!uattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
						HashSet<String> vals = new HashSet<String>();
						vals.add(randomElement(c.getRHS()));
						h.put(c.getLHS(), vals);
					} else {
						HashSet<String> vals = randomSet(c.getRHSet());
						h.put(c.getLHS(), vals);
					}
				}
				// assign user attribute values to satisfy constraint
				for (AttrAttrConjunct c : rho.getCon()) {
					if (h.keySet().contains(c.getLHS())) {
						boolean found = false;
						AttrValConjunct rConjunct = null;
						for (AttrValConjunct v : rho.getRAE()) {
							if (v.getLHS().equals(c.getRHS())) {
								rConjunct = v;
								found = true;
								break;
							}
						}
						if (found) {
							boolean satisfied = true;
							switch (c.getOperator()) {
							case EQUALS:
								if (!rConjunct.getRHS().containsAll(
										h.get(c.getLHS()))) {
									satisfied = false;
								}
								break;
							case IN:
								HashSet<String> uValSet = new HashSet<String>(
										h.get(c.getLHS()));
								uValSet.retainAll(rConjunct.getRHS());
								if (uValSet.isEmpty()) {
									h.get(c.getLHS()).add(
											randomElement(rConjunct.getRHS()));
								}
								break;
							case SUPSETEQ:
								boolean supsetEq = false;
								for (HashSet<String> rValSet : rConjunct
										.getRHSet()) {
									if (h.get(c.getLHS()).containsAll(rValSet)) {
										supsetEq = true;
										break;
									}
								}
								if (!supsetEq) {
									h.get(c.getLHS()).addAll(
											randomSet(rConjunct.getRHSet()));
								}
								break;
							default:
								break;
							}
							if (!satisfied) {
								System.err
										.println("Generated Unsatisfiable Constraint! :"
												+ c);
								return null;
							}
						}
					}
				}
				users.add(u);
				config.getUserAttrInfo().put(u, h);
				rho.getSatUsers().add(u);
				if (debug) {
					System.out.println("Satisfying user:" + u + " " + config.getUserAttrInfo().get(u));
				}
			}

			if (debug) {
				System.out.println("Satisfying user set:" + rho.getSatUsers());
			}
			//System.out.println("Generating Sat Resources");
			// for each user, find a reource that together satisfy the rule
			HashSet<Pair<String, String>> satisfyingUserResrPairs = new HashSet<Pair<String, String>>();
			for (String u : rho.getSatUsers()) {
				boolean foundResource = false;
				for (String r : resources) {
					if (checkUserAndResourceSatisfiesRule(u, r, rho.getRAE(),
							rho.getCon(), config)) {
						foundResource = true;
						rho.getSatResources().add(r);
						if (debug) {
							System.out.println("Satisfying resource:" + r + " " + config.getResourceAttrInfo().get(r));
						}
						satisfyingUserResrPairs.add(new Pair<String, String>(u,
								r));
						if (debug) {
							System.out
									.println("Satisfying user and resource pair:"
											+ u + "," + r);
						}
						break;
					}
				}
				if (!foundResource) {
					String r = newRes();
					if (debug) {
						System.out.println("Generated new resource:" + r);
					}
					HashMap<String, HashSet<String>> rad1 = new HashMap<String, HashSet<String>>();
					HashMap<String, HashSet<String>> uad1 = new HashMap<String, HashSet<String>>(
							config.getUserAttrInfo().get(u));
					HashSet<String> rids = new HashSet<String>();
					rids.add(r);
					rad1.put("rid", rids);
					// let resource satisfy RAE
					for (AttrValConjunct c : rho.getRAE()) {
						if (!rattrIsMulti[Integer.parseInt(c.getLHS()
								.substring(5))]) {
							HashSet<String> vals = new HashSet<String>();
							vals.add(randomElement(c.getRHS()));
							rad1.put(c.getLHS(), vals);
						} else {
							HashSet<String> vals = randomSet(c.getRHSet());
							rad1.put(c.getLHS(), vals);
						}
					}

					for (AttrAttrConjunct c : rho.getCon()) {
						boolean uaEqualsBot = !uad1.containsKey(c.getLHS());
						boolean raEqualsBot = !rad1.containsKey(c.getRHS());
						// user attribute is not defined
						if (uaEqualsBot) {
							// use resource attribute assignment for user
							// attribute
							if (!raEqualsBot) {
								switch (c.getOperator()) {
								case EQUALS:
									uad1.put(c.getLHS(), new HashSet<String>(
											rad1.get(c.getRHS())));
									break;
								case IN:
									uad1.put(c.getLHS(), new HashSet<String>(
											rad1.get(c.getRHS())));
									break;
								case SUPSETEQ:
									uad1.put(c.getLHS(), new HashSet<String>(
											rad1.get(c.getRHS())));
									break;
								default:
									break;
								}
							} else {
								HashSet<String> v = new HashSet<String>();
								if (!uattrIsMulti[Integer.parseInt(c.getLHS()
										.substring(5))]) {
									v.add(Integer.toString(DdataVal(Integer
											.parseInt(c.getLHS().substring(5)),
											userAttrToValueDistrib,
											AttributeType.UserAttr)));
								} else {
									int sz = DmultiSz(uCardinality[Integer
											.parseInt(c.getLHS().substring(5))]);
									int iter = 0;
									while (v.size() < sz) {
										iter++;
										if (iter > uCardinality[Integer
																.parseInt(c.getLHS().substring(5))]) {
											return null;
										}
										v.add(Integer.toString(DdataVal(Integer
												.parseInt(c.getLHS().substring(
														5)),
												userAttrToValueDistrib,
												AttributeType.UserAttr)));
									}
								}
								uad1.put(c.getLHS(), v);
								switch (c.getOperator()) {
								case EQUALS:
									rad1.put(c.getRHS(), new HashSet<String>(
											uad1.get(c.getLHS())));
									break;
								case IN:
									HashSet<String> vSet = new HashSet<String>();
									vSet.add(randomElement(uad1.get(c.getLHS())));
									rad1.put(c.getRHS(), vSet);
									break;
								case SUPSETEQ:
									rad1.put(c.getRHS(), new HashSet<String>(
											uad1.get(c.getLHS())));
									break;
								default:
									break;
								}
							}
						} else {
							if (raEqualsBot) {
								switch (c.getOperator()) {
								case EQUALS:
									rad1.put(c.getRHS(), new HashSet<String>(
											uad1.get(c.getLHS())));
									break;
								case IN:
									HashSet<String> vSet = new HashSet<String>();
									vSet.add(randomElement(uad1.get(c.getLHS())));
									rad1.put(c.getRHS(), vSet);
									break;
								case SUPSETEQ:
									rad1.put(c.getRHS(), new HashSet<String>(
											uad1.get(c.getLHS())));
									break;
								default:
									break;
								}
							} else {
								boolean satisfied = false;
								if (debug) {
									System.out
											.println("Both user and resource attribute are assigned.");
								}
								switch (c.getOperator()) {
								case EQUALS:
									if (uad1.get(c.getLHS()).equals(
											rad1.get(c.getRHS()))) {
										satisfied = true;
									}
									break;
								case IN:
									if (uad1.get(c.getLHS()).containsAll(
											rad1.get(c.getRHS()))) {
										satisfied = true;
									} else {
										uad1.get(c.getLHS()).addAll(
												rad1.get(c.getRHS()));
										satisfied = true;
									}
									break;
								case SUPSETEQ:
									if (uad1.get(c.getLHS()).containsAll(
											rad1.get(c.getRHS()))) {
										satisfied = true;
									} else {
										uad1.get(c.getLHS()).addAll(
												rad1.get(c.getRHS()));
										satisfied = true;
									}
									break;
								default:
									break;
								}
								if (!satisfied) {
									System.err
											.println("Generated Unsatisfiable Constraint "
													+ c);
									System.err.println(config.getUserAttrInfo()
											.get(u));
									System.err.println(rad1);
									return null;
								}
							}
						}
					}
					resources.add(r);
					rho.getSatResources().add(r);
					config.getUserAttrInfo().put(u, uad1);
					config.getResourceAttrInfo().put(r, rad1);
					if (debug) {
						System.out
								.println("Satisfying user and new resource pair:"
										+ u + "," + r);
					}
				}
			}

			if (checkSatisfy) {
				for (String user : rho.getSatUsers()) {
					if (!Parser.satisfyingUAE(user, rho.getUAE(), config)) {
						System.err.println("user " + user + "does not satisfy "
								+ rho.getUAE());
						System.err.println(config.getUserAttrInfo().get(user));
						return null;
					}
				}
				for (String resr : rho.getSatResources()) {
					if (!Parser.satisfyingRAE(resr, rho.getRAE(), config)) {
						System.err.println("resource " + resr
								+ "does not satisfy " + rho.getUAE());
						System.err.println(config.getResourceAttrInfo().get(
								resr));
						return null;
					}
				}
				for (Pair<String, String> pair : satisfyingUserResrPairs) {
					if (!Parser.satisfyingRuleConstraints(pair.getFirst(),
							pair.getSecond(), rho, config)) {
						System.err.println("pair " + pair + "does not satisfy "
								+ rho.getCon());
						System.err.println(config.getUserAttrInfo().get(
								pair.getFirst()));
						System.err.println(config.getResourceAttrInfo().get(
								pair.getSecond()));
						return null;
					}
				}
			}
			if (debug) {
				System.out.println(rho);
			}

			//System.out.println("Generating companion rule");
			// try to generate a companion rule that has similar structure
			Random rand = new Random(System.currentTimeMillis());
			double p = rand.nextDouble();
			if (Double.compare(p, pOverlapRule) <= 0) {
				if (debug) {
					System.out
							.println("Generating a companion rule********************************.");
				}
				Rule tRho = new Rule();
				tRho.setChanged(true);
				tRho.setUaeChanged(true);
				tRho.setRaeChanged(true);
				for (AttrAttrConjunct c : rho.getCon()) {
					tRho.getCon().add(new AttrAttrConjunct(c));
				}

				for (AttrValConjunct c : rho.getUAE()) {
					tRho.getUAE().add(new AttrValConjunct(c));
				}

				for (AttrValConjunct c : rho.getRAE()) {
					tRho.getRAE().add(new AttrValConjunct(c));
				}
				tRho.setOps(new HashSet<String>(rho.getOps()));

				HashSet<String> conUserAttrs = new HashSet<String>();
				for (AttrAttrConjunct c : rho.getCon()) {
					conUserAttrs.add(c.getLHS());
				}

				int uaeIndex = rand.nextInt(tRho.getUAE().size());
				int iter1 = 0;
				while (conUserAttrs.contains(tRho.getUAE().get(uaeIndex)
						.getLHS())) {
					iter1 ++;
					if (iter1 > tRho.getUAE().size()) {
						return null;
					}
					uaeIndex = rand.nextInt(tRho.getUAE().size());
				}
				tRho.getUAE().remove(uaeIndex);
				int a = Dattrib();
				for (AttrAttrConjunct c : rho.getCon()) {
					usedResAttrs.add(c.getRHS());
				}
				if (usedResAttrs.size() == NresAtt) {
					continue;
				}
				int iter2 = 0;
				while (usedResAttrs.contains("rattr" + a)) {
					iter2 ++;
					if (iter2 > usedResAttrs.size()) {
						return null;
					}
					a = Dattrib();
				}
				usedResAttrs.add("rattr" + a);
				String rattr = "rattr" + a;
				// int conjunctSize = DconjunctSize(rCardinality[a],
				// minConjunctSize);
				if (!rattrIsMulti[a]) {
					HashSet<String> s = new HashSet<String>();
					for (String r : rho.getSatResources()) {
						if (s.isEmpty()
								|| Double.compare(rand.nextDouble(),
										pOverlapVal) <= 0) {
							if (!config.getResourceAttrInfo().get(r)
									.containsKey(rattr)
									|| config.getResourceAttrInfo().get(r)
											.get(rattr).isEmpty()) {
								HashSet<String> valSet = new HashSet<String>();
								valSet.add(Integer.toString(DdataVal(a,
										resourceAttrToValueDistrib,
										AttributeType.ResourceAttr)));
								config.getResourceAttrInfo().get(r)
										.put(rattr, valSet);

							}
							s.addAll(config.getResourceAttrInfo().get(r)
									.get(rattr));
							tRho.getSatResources().add(r);
						}
					}
					tRho.addrConjunct(new AttrValConjunct("rattr"
							+ Integer.toString(a), s, null, "="));
				} else {
					HashSet<HashSet<String>> sSet = new HashSet<HashSet<String>>();
					for (String r : rho.getSatResources()) {
						if (sSet.isEmpty()
								|| Double.compare(rand.nextDouble(),
										pOverlapVal) <= 0) {
							if (!config.getResourceAttrInfo().get(r)
									.containsKey(rattr)
									|| config.getResourceAttrInfo().get(r)
											.get(rattr).isEmpty()) {
								HashSet<String> valSet = new HashSet<String>();
								int sz = DmultiSz(rCardinality[a]);
								int iter = 0;
								while (valSet.size() < sz) {
									iter ++;
									if (iter > rCardinality[a]) {
										return null;
									}
									valSet.add(Integer.toString(DdataVal(a,
											resourceAttrToValueDistrib,
											AttributeType.ResourceAttr)));
								}
								config.getResourceAttrInfo().get(r)
										.put(rattr, valSet);

							}
							sSet.add(config.getResourceAttrInfo().get(r)
									.get(rattr));
							tRho.getSatResources().add(r);
						}
					}
					tRho.addrConjunct(new AttrValConjunct("rattr"
							+ Integer.toString(a), null, sSet, "="));
				}
				tRho.setCompanion(true);
				/**********************************************************/
				int iter3 = 0;
				while (tRho.getSatUsers().size() < 4/* nUser */) {
					//System.out.println("Generating Sat Users For Componion Rule");
					iter3++;
					if (iter3 > 100) {
						return null;
					}
					String u = newUser();
					HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
					HashSet<String> uids = new HashSet<String>();
					uids.add(u);
					h.put("uid", uids);
					// assign user attribute values to satisfy UAE
					for (AttrValConjunct c : tRho.getUAE()) {
						if (!uattrIsMulti[Integer.parseInt(c.getLHS()
								.substring(5))]) {
							HashSet<String> vals = new HashSet<String>();
							vals.add(randomElement(c.getRHS()));
							h.put(c.getLHS(), vals);
						} else {
							HashSet<String> vals = randomSet(c.getRHSet());
							h.put(c.getLHS(), vals);
						}
					}
					// assign user attribute values to satisfy constraint
					for (AttrAttrConjunct c : tRho.getCon()) {
						if (h.keySet().contains(c.getLHS())) {
							boolean found = false;
							AttrValConjunct rConjunct = null;
							for (AttrValConjunct v : tRho.getRAE()) {
								if (v.getLHS().equals(c.getRHS())) {
									rConjunct = v;
									found = true;
									break;
								}
							}
							if (found) {
								boolean satisfied = true;
								switch (c.getOperator()) {
								case EQUALS:
									if (!rConjunct.getRHS().containsAll(
											h.get(c.getLHS()))) {
										satisfied = false;
									}
									break;
								case IN:
									HashSet<String> uValSet = new HashSet<String>(
											h.get(c.getLHS()));
									uValSet.retainAll(rConjunct.getRHS());
									if (uValSet.isEmpty()) {
										h.get(c.getLHS()).add(
												randomElement(rConjunct
														.getRHS()));
									}
									break;
								case SUPSETEQ:
									boolean supsetEq = false;
									for (HashSet<String> rValSet : rConjunct
											.getRHSet()) {
										if (h.get(c.getLHS()).containsAll(
												rValSet)) {
											supsetEq = true;
											break;
										}
									}
									if (!supsetEq) {
										h.get(c.getLHS())
												.addAll(randomSet(rConjunct
														.getRHSet()));
									}
									break;
								default:
									break;
								}
								if (!satisfied) {
									System.err
											.println("Generated Unsatisfiable Constraint! :"
													+ c);
									return null;
//									/System.exit(1);
								}
							}
						}
					}
					// if (checkUserSatisfiesRule(u, rho.getUAE(), rho.getRAE(),
					// rho.getCon(), config)) {
					// continue;
					// }

					users.add(u);
					config.getUserAttrInfo().put(u, h);
					tRho.getSatUsers().add(u);
				}

				if (debug) {
					System.out.println("Satisfying user set:"
							+ rho.getSatUsers());
					System.out.println(config.getUserAttrInfo());
					System.out.println(config.getResourceAttrInfo());
				}

				// for each user, find a reource that together satisfy the rule
				satisfyingUserResrPairs = new HashSet<Pair<String, String>>();
				for (String u : tRho.getSatUsers()) {
					boolean foundResource = false;
					for (String r : resources) {
						if (checkUserAndResourceSatisfiesRule(u, r,
								tRho.getRAE(), tRho.getCon(), config)) {
							foundResource = true;
							tRho.getSatResources().add(r);
							satisfyingUserResrPairs
									.add(new Pair<String, String>(u, r));
							if (debug) {
								System.out
										.println("Satisfying user and resource pair:"
												+ u + "," + r);
							}
							break;
						}
					}
					if (!foundResource) {
						String r = newRes();
						if (debug) {
							System.out.println("Generated new resource:" + r);
						}
						HashMap<String, HashSet<String>> rad1 = new HashMap<String, HashSet<String>>();
						HashMap<String, HashSet<String>> uad1 = new HashMap<String, HashSet<String>>(
								config.getUserAttrInfo().get(u));
						HashSet<String> rids = new HashSet<String>();
						rids.add(r);
						rad1.put("rid", rids);
						// let resource satisfy RAE
						for (AttrValConjunct c : tRho.getRAE()) {
							if (!rattrIsMulti[Integer.parseInt(c.getLHS()
									.substring(5))]) {
								HashSet<String> vals = new HashSet<String>();
								vals.add(randomElement(c.getRHS()));
								rad1.put(c.getLHS(), vals);
							} else {
								HashSet<String> vals = randomSet(c.getRHSet());
								rad1.put(c.getLHS(), vals);
							}
						}

						for (AttrAttrConjunct c : tRho.getCon()) {
							boolean uaEqualsBot = !uad1.containsKey(c.getLHS());
							boolean raEqualsBot = !rad1.containsKey(c.getRHS());
							// user attribute is not defined
							if (uaEqualsBot) {
								// use resource attribute assignment for user
								// attribute
								if (!raEqualsBot) {
									switch (c.getOperator()) {
									case EQUALS:
										uad1.put(
												c.getLHS(),
												new HashSet<String>(rad1.get(c
														.getRHS())));
										break;
									case IN:
										uad1.put(
												c.getLHS(),
												new HashSet<String>(rad1.get(c
														.getRHS())));
										break;
									case SUPSETEQ:
										uad1.put(
												c.getLHS(),
												new HashSet<String>(rad1.get(c
														.getRHS())));
										break;
									default:
										break;
									}
								} else {
									HashSet<String> v = new HashSet<String>();
									if (!uattrIsMulti[Integer.parseInt(c
											.getLHS().substring(5))]) {
										v.add(Integer.toString(DdataVal(Integer
												.parseInt(c.getLHS().substring(
														5)),
												userAttrToValueDistrib,
												AttributeType.UserAttr)));
									} else {
										int sz = DmultiSz(uCardinality[Integer
												.parseInt(c.getLHS().substring(
														5))]);
										int iter = 0;
										while (v.size() < sz) {
											iter ++;
											if (iter > uCardinality[Integer
																	.parseInt(c.getLHS().substring(
																			5))]) {
												return null;
											}
											v.add(Integer.toString(DdataVal(
													Integer.parseInt(c.getLHS()
															.substring(5)),
													userAttrToValueDistrib,
													AttributeType.UserAttr)));
										}
									}
									uad1.put(c.getLHS(), v);
									switch (c.getOperator()) {
									case EQUALS:
										rad1.put(
												c.getRHS(),
												new HashSet<String>(uad1.get(c
														.getLHS())));
										break;
									case IN:
										HashSet<String> vSet = new HashSet<String>();
										vSet.add(randomElement(uad1.get(c
												.getLHS())));
										rad1.put(c.getRHS(), vSet);
										break;
									case SUPSETEQ:
										rad1.put(
												c.getRHS(),
												new HashSet<String>(uad1.get(c
														.getLHS())));
										break;
									default:
										break;
									}
								}
							} else {
								if (raEqualsBot) {
									switch (c.getOperator()) {
									case EQUALS:
										rad1.put(
												c.getRHS(),
												new HashSet<String>(uad1.get(c
														.getLHS())));
										break;
									case IN:
										HashSet<String> vSet = new HashSet<String>();
										vSet.add(randomElement(uad1.get(c
												.getLHS())));
										rad1.put(c.getRHS(), vSet);
										break;
									case SUPSETEQ:
										rad1.put(
												c.getRHS(),
												new HashSet<String>(uad1.get(c
														.getLHS())));
										break;
									default:
										break;
									}
								} else {
									boolean satisfied = false;
									if (debug) {
										System.out
												.println("Both user and resource attribute are assigned.");
									}
									switch (c.getOperator()) {
									case EQUALS:
										if (uad1.get(c.getLHS()).equals(
												rad1.get(c.getRHS()))) {
											satisfied = true;
										}
										break;
									case IN:
										if (uad1.get(c.getLHS()).containsAll(
												rad1.get(c.getRHS()))) {
											satisfied = true;
										} else {
											uad1.get(c.getLHS()).addAll(
													rad1.get(c.getRHS()));
											satisfied = true;
										}
										break;
									case SUPSETEQ:
										if (uad1.get(c.getLHS()).containsAll(
												rad1.get(c.getRHS()))) {
											satisfied = true;
										} else {
											uad1.get(c.getLHS()).addAll(
													rad1.get(c.getRHS()));
											satisfied = true;
										}
										break;
									default:
										break;
									}
									if (!satisfied) {
										System.err
												.println("Generated Unsatisfiable Constraint "
														+ c);
										System.err.println(config
												.getUserAttrInfo().get(u));
										System.err.println(rad1);
										return null;
									}
								}
							}
						}
						resources.add(r);
						tRho.getSatResources().add(r);
						config.getUserAttrInfo().put(u, uad1);
						config.getResourceAttrInfo().put(r, rad1);
						if (debug) {
							System.out
									.println("Satisfying user and new resource pair:"
											+ u + "," + r);
						}
					}
				}
				/*****************************************************************/
				rules.add(tRho);
				if (debug) {
					System.out.println(tRho);
				}
			}
		}

		//System.out.println("Adding distinguishing Users");
		for (Rule rho : rules) {
			// adding users with one attribute value different from a current
			// user, to make conjunct more useful
			if (rho.isCompanion()) {
				continue;
			}
			HashSet<String> uaeConjunctAttrs = new HashSet<String>();
			HashSet<String> constraintUserAttrs = new HashSet<String>();
			for (AttrValConjunct c : rho.getUAE()) {
				uaeConjunctAttrs.add(c.getLHS());
			}
			for (AttrAttrConjunct c : rho.getCon()) {
				constraintUserAttrs.add(c.getLHS());
			}
			uaeConjunctAttrs.removeAll(constraintUserAttrs);
			if (!uaeConjunctAttrs.isEmpty()) {
				for (String userAttr : uaeConjunctAttrs) {
					AttrValConjunct uConjunct = null;
					for (AttrValConjunct c : rho.getUAE()) {
						if (userAttr.equals(c.getLHS())) {
							uConjunct = c;
							break;
						}
					}
					String u = randomElement(rho.getSatUsers());
					String disUser = newUser();
					HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
					HashSet<String> uids = new HashSet<String>();
					uids.add(disUser);
					h.put("uid", uids);
					if (debug) {
						System.out.println("Rule: \n" + rho);
						System.out.println("Conjunct: " + uConjunct);
					}
					for (String uattr : config.getUserAttrInfo().get(u)
							.keySet()) {
						if (uattr.equals(userAttr)) {
							HashSet<String> unsatisfiableValueSet = new HashSet<String>();
							if (!uattrIsMulti[Integer.parseInt(uattr
									.substring(5))]) {
								int v = -1;
								if (uConjunct.getRHS().size() == uCardinality[Integer
										.parseInt(uattr.substring(5))]) {
									v = uCardinality[Integer.parseInt(uattr
											.substring(5))];
								} else {
									v = DdataVal(Integer.parseInt(uattr
											.substring(5)),
											userAttrToValueDistrib,
											AttributeType.UserAttr);
									int iteration = 0;
									while (uConjunct.getRHS().contains(v)) {
										iteration++;
										if (iteration >= uCardinality[Integer
																		.parseInt(uattr.substring(5))]) {
											return null;
										}
										v = DdataVal(Integer.parseInt(uattr
												.substring(5)),
												userAttrToValueDistrib,
												AttributeType.UserAttr);
									}
								}
								unsatisfiableValueSet.add(Integer.toString(v));
							} else {
								if (uConjunct.getRHSet().size() == uCardinality[Integer
										.parseInt(uattr.substring(5))]) {
									int v = uCardinality[Integer.parseInt(uattr
											.substring(5))];
									unsatisfiableValueSet.add(Integer
											.toString(v));
								} else {
									int v = DdataVal(Integer.parseInt(uattr
											.substring(5)),
											userAttrToValueDistrib,
											AttributeType.UserAttr);
									unsatisfiableValueSet.add(Integer
											.toString(v));
									int iteration = 0;
									while (true) {
										iteration++;
										if (iteration >= uCardinality[Integer
																		.parseInt(uattr.substring(5))]) {
											return null;
										}
										boolean contained = false;
										for (HashSet<String> vSet : uConjunct
												.getRHSet()) {
											if (unsatisfiableValueSet
													.containsAll(vSet)) {
												contained = true;
												break;
											}
										}
										if (contained == true) {
											v = DdataVal(Integer.parseInt(uattr
													.substring(5)),
													userAttrToValueDistrib,
													AttributeType.UserAttr);
											unsatisfiableValueSet.clear();
											unsatisfiableValueSet.add(Integer
													.toString(v));
										} else {
											break;
										}
									}
								}
							}
							// unsatisfiableValueSet.add("unsat" + unsatNum++);
							h.put(uattr, unsatisfiableValueSet);
						} else {
							h.put(uattr, new HashSet<String>(config
									.getUserAttrInfo().get(u).get(uattr)));
						}
					}
					distinguishUsers.add(disUser);
					config.getUserAttrInfo().put(disUser, h);
					if (debug) {

						System.out.println(u);
						for (String uattr : config.getUserAttrInfo().get(u)
								.keySet()) {
							System.out.print(config.getUserAttrInfo().get(u)
									.get(uattr));
						}
						System.out.println();
						System.out.println(disUser);
						for (String uattr : config.getUserAttrInfo()
								.get(disUser).keySet()) {
							System.out.print(config.getUserAttrInfo()
									.get(disUser).get(uattr));
						}
						System.out.println();
					}
				}
			}
		}
		//System.out.println("Adding distinguishing Resources");
		for (Rule rho : rules) {
			// adding resourcers with one attribute value different from a
			// current
			// resource, to make conjunct more useful
			if (rho.isCompanion()) {
				continue;
			}
			HashSet<String> raeConjunctAttrs = new HashSet<String>();
			HashSet<String> constraintResAttrs = new HashSet<String>();
			for (AttrValConjunct c : rho.getRAE()) {
				raeConjunctAttrs.add(c.getLHS());
			}
			for (AttrAttrConjunct c : rho.getCon()) {
				constraintResAttrs.add(c.getRHS());
			}
			raeConjunctAttrs.removeAll(constraintResAttrs);
			if (!raeConjunctAttrs.isEmpty()) {
				for (String resAttr : raeConjunctAttrs) {
					AttrValConjunct rConjunct = null;
					for (AttrValConjunct c : rho.getRAE()) {
						if (resAttr.equals(c.getLHS())) {
							rConjunct = c;
							break;
						}
					}
					String r = randomElement(rho.getSatResources());
					String disRes = newRes();
					HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
					HashSet<String> rids = new HashSet<String>();
					rids.add(disRes);
					h.put("rid", rids);
					if (debug) {
						System.out.println("Rule: \n" + rho);
						System.out.println("Conjunct: " + rConjunct);
					}
					for (String rattr : config.getResourceAttrInfo().get(r)
							.keySet()) {
						if (rattr.equals(resAttr)) {
							HashSet<String> unsatisfiableValueSet = new HashSet<String>();
							if (!rattrIsMulti[Integer.parseInt(rattr
									.substring(5))]) {
								int v = -1;
								if (rConjunct.getRHS().size() == rCardinality[Integer
										.parseInt(rattr.substring(5))]) {
									v = rCardinality[Integer.parseInt(rattr
											.substring(5))];
								} else {
									v = DdataVal(Integer.parseInt(rattr
											.substring(5)),
											resourceAttrToValueDistrib,
											AttributeType.ResourceAttr);
									int iteration = 0;
									while (rConjunct.getRHS().contains(v)) {
										iteration++;
										if (iteration >= rCardinality[Integer
																		.parseInt(rattr.substring(5))]) {
											return null;
										}
										v = DdataVal(Integer.parseInt(rattr
												.substring(5)),
												resourceAttrToValueDistrib,
												AttributeType.ResourceAttr);
									}
								}
								unsatisfiableValueSet.add(Integer.toString(v));
							} else {
								if (rConjunct.getRHSet().size() == rCardinality[Integer
										.parseInt(rattr.substring(5))]) {
									int v = rCardinality[Integer.parseInt(rattr
											.substring(5))];
									unsatisfiableValueSet.add(Integer
											.toString(v));
								} else {
									int v = DdataVal(Integer.parseInt(rattr
											.substring(5)),
											resourceAttrToValueDistrib,
											AttributeType.ResourceAttr);
									unsatisfiableValueSet.add(Integer
											.toString(v));
									int iteration = 0;
									while (true) {
										iteration++;
										if (iteration >= rCardinality[Integer
																		.parseInt(rattr.substring(5))]) {
											return null;
										}
										boolean contained = false;
										for (HashSet<String> vSet : rConjunct
												.getRHSet()) {
											if (unsatisfiableValueSet
													.equals(vSet)) {
												contained = true;
												break;
											}
										}
										if (contained == true) {
											v = DdataVal(Integer.parseInt(rattr
													.substring(5)),
													resourceAttrToValueDistrib,
													AttributeType.ResourceAttr);
											unsatisfiableValueSet.clear();
											;
											unsatisfiableValueSet.add(Integer
													.toString(v));
										} else {
											break;
										}
									}
								}
							}
							// unsatisfiableValueSet.add("unsat" + unsatNum++);
							h.put(rattr, unsatisfiableValueSet);

						} else {
							h.put(rattr, new HashSet<String>(config
									.getResourceAttrInfo().get(r).get(rattr)));
						}
					}

					distinguishResources.add(disRes);
					config.getResourceAttrInfo().put(disRes, h);
					if (debug) {
						System.out.println(r);
						for (String uattr : config.getUserAttrInfo().get(r)
								.keySet()) {
							System.out.print(config.getUserAttrInfo().get(r)
									.get(uattr));
						}
						System.out.println();
						System.out.println(disRes);
						for (String uattr : config.getUserAttrInfo()
								.get(disRes).keySet()) {
							System.out.print(config.getUserAttrInfo()
									.get(disRes).get(uattr));
						}
						System.out.println();
					}
				}
			}
		}

		// adding users with one attribute value different from a current
		// user, to make constraints more useful
		for (Rule rho : rules) {
			if (rho.isCompanion()) {
				continue;
			}
			HashSet<String> uaeConjunctAttrs = new HashSet<String>();
			HashSet<String> constraintUserAttrs = new HashSet<String>();
			for (AttrValConjunct c : rho.getUAE()) {
				uaeConjunctAttrs.add(c.getLHS());
			}
			for (AttrAttrConjunct c : rho.getCon()) {
				constraintUserAttrs.add(c.getLHS());
			}
			constraintUserAttrs.removeAll(uaeConjunctAttrs);
			if (!constraintUserAttrs.isEmpty()) {
				for (String userAttr : constraintUserAttrs) {
					String u = randomElement(rho.getSatUsers());
					String disUser = newUser();
					HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
					HashSet<String> uids = new HashSet<String>();
					uids.add(disUser);
					h.put("uid", uids);
					if (debug) {
						System.out.println("Rule: \n" + rho);
						System.out.println("Constraint attribute: " + userAttr);
					}
					for (String uattr : config.getUserAttrInfo().get(u)
							.keySet()) {
						if (uattr.equals(userAttr)) {
							HashSet<String> valSet = new HashSet<String>();
							for (String user : rho.getSatUsers()) {
								valSet.addAll(config.getUserAttrInfo()
										.get(user).get(uattr));
							}

							HashSet<String> unsatisfiableValueSet = new HashSet<String>();
							int v = -1;
							if (valSet.size() == uCardinality[Integer
									.parseInt(uattr.substring(5))]) {
								v = uCardinality[Integer.parseInt(uattr
										.substring(5))];
							} else {
								v = DdataVal(
										Integer.parseInt(uattr.substring(5)),
										userAttrToValueDistrib,
										AttributeType.UserAttr);
								while (valSet.contains(v)) {
									v = DdataVal(Integer.parseInt(uattr
											.substring(5)),
											userAttrToValueDistrib,
											AttributeType.UserAttr);
								}
							}
							unsatisfiableValueSet.add(Integer.toString(v));
							h.put(uattr, unsatisfiableValueSet);

						} else {
							h.put(uattr, new HashSet<String>(config
									.getUserAttrInfo().get(u).get(uattr)));
						}
					}
					distinguishUsers.add(disUser);
					config.getUserAttrInfo().put(disUser, h);
					if (debug) {

						System.out.println(u);
						for (String uattr : config.getUserAttrInfo().get(u)
								.keySet()) {
							System.out.print(config.getUserAttrInfo().get(u)
									.get(uattr));
						}
						System.out.println();
						System.out.println(disUser);
						for (String uattr : config.getUserAttrInfo()
								.get(disUser).keySet()) {
							System.out.print(config.getUserAttrInfo()
									.get(disUser).get(uattr));
						}
						System.out.println();
					}
				}
			}
		}

		for (Rule rho : rules) {
			// adding resources with one attribute value different from a
			// current
			// resource, to make constraint more useful
			if (rho.isCompanion()) {
				continue;
			}
			HashSet<String> raeConjunctAttrs = new HashSet<String>();
			HashSet<String> constraintResAttrs = new HashSet<String>();
			for (AttrValConjunct c : rho.getRAE()) {
				raeConjunctAttrs.add(c.getLHS());
			}
			for (AttrAttrConjunct c : rho.getCon()) {
				constraintResAttrs.add(c.getRHS());
			}
			constraintResAttrs.removeAll(raeConjunctAttrs);
			if (!constraintResAttrs.isEmpty()) {
				for (String resAttr : constraintResAttrs) {
					String r = randomElement(rho.getSatResources());
					String disRes = newRes();
					HashMap<String, HashSet<String>> h = new HashMap<String, HashSet<String>>();
					HashSet<String> rids = new HashSet<String>();
					rids.add(disRes);
					h.put("rid", rids);
					if (debug) {
						System.out.println("Rule: \n" + rho);
						System.out.println("Constraint attribute: " + resAttr);
					}
					for (String rattr : config.getResourceAttrInfo().get(r)
							.keySet()) {
						if (rattr.equals(resAttr)) {
							HashSet<String> valSet = new HashSet<String>();
							for (String res : rho.getSatResources()) {
								valSet.addAll(config.getResourceAttrInfo()
										.get(res).get(rattr));
							}

							HashSet<String> unsatisfiableValueSet = new HashSet<String>();
							int v = -1;
							if (valSet.size() == rCardinality[Integer
									.parseInt(rattr.substring(5))]) {
								v = rCardinality[Integer.parseInt(rattr
										.substring(5))];
							} else {
								v = DdataVal(
										Integer.parseInt(rattr.substring(5)),
										resourceAttrToValueDistrib,
										AttributeType.ResourceAttr);
								while (valSet.contains(v)) {
									v = DdataVal(Integer.parseInt(rattr
											.substring(5)),
											resourceAttrToValueDistrib,
											AttributeType.ResourceAttr);
								}
							}
							unsatisfiableValueSet.add(Integer.toString(v));
							h.put(rattr, unsatisfiableValueSet);
						} else {
							h.put(rattr, new HashSet<String>(config
									.getResourceAttrInfo().get(r).get(rattr)));
						}
					}
					distinguishResources.add(disRes);
					config.getResourceAttrInfo().put(disRes, h);
					if (debug) {

						System.out.println(r);
						for (String rattr : config.getResourceAttrInfo().get(r)
								.keySet()) {
							System.out.print(config.getResourceAttrInfo()
									.get(r).get(rattr));
						}
						System.out.println();
						System.out.println(disRes);
						for (String rattr : config.getResourceAttrInfo()
								.get(disRes).keySet()) {
							System.out.print(config.getResourceAttrInfo()
									.get(disRes).get(rattr));
						}
						System.out.println();
					}
				}
			}
		}

		users.addAll(distinguishUsers);
		resources.addAll(distinguishResources);

		// append each attribute value with the comparable group number
		for (String u : users) {
			for (String uattr : config.getUserAttrInfo().get(u).keySet()) {
				if (uattr.equals("uid")) {
					continue;
				}
				int index = -1;
				for (int i = 0; i < NComparableAttrGroups; i++) {
					if (comparableAttrGroups.get(i).getFirst()
							.contains(Integer.parseInt(uattr.substring(5)))) {
						index = i;
						break;
					}
				}
				HashSet<String> valueSet = new HashSet<String>();
				for (String v : config.getUserAttrInfo().get(u).get(uattr)) {
					valueSet.add(v + "_" + index);
				}
				config.getUserAttrInfo().get(u).put(uattr, valueSet);
			}
		}

		for (String r : resources) {
			for (String rattr : config.getResourceAttrInfo().get(r).keySet()) {
				if (rattr.equals("rid")) {
					continue;
				}
				int index = -1;
				for (int i = 0; i < NComparableAttrGroups; i++) {
					if (comparableAttrGroups.get(i).getSecond()
							.contains(Integer.parseInt(rattr.substring(5)))) {
						index = i;
						break;
					}
				}
				HashSet<String> valueSet = new HashSet<String>();
				for (String v : config.getResourceAttrInfo().get(r).get(rattr)) {
					valueSet.add(v + "_" + index);
				}
				config.getResourceAttrInfo().get(r).put(rattr, valueSet);
			}
		}
		
		int Nava = users.size() * NuserAtt;
		int Nbot = 0;

		for (String user : users) {
			Nbot += NuserAtt + 1 - config.getUserAttrInfo().get(user).size();
		}
		//System.out.println("botFrac for users");
		double currentBotFrac1 = (double) Nbot / Nava;
		if (currentBotFrac1 > botFrac) {
			double NbotToReplace = (currentBotFrac1 - botFrac) / Nava;
			double pReplace = NbotToReplace / Nbot;
			for (String user : users) {
				for (int i = 0; i < NuserAtt; i++) {
					String uAttr = "uattr" + i;
					if (!config.getUserAttrInfo().get(user).keySet().contains(uAttr) && randomGen.nextDouble() <= pReplace) {
						int value = DdataVal(i, userAttrToValueDistrib,
								AttributeType.UserAttr);
						HashSet<String> valueSet = new HashSet<String>();
						valueSet.add(Integer.toString(value));
						config.getUserAttrInfo().get(user).put(uAttr, valueSet);
					}
				}
			}
		}
		
		Nava = resources.size() * NresAtt;
		Nbot = 0;

		for (String res : resources) {
			Nbot += NuserAtt + 1 - config.getResourceAttrInfo().get(res).size();
		}
		double currentBotFrac2 = (double) Nbot / Nava;
		//System.out.println("botFrac for resources");
		if (currentBotFrac2 > botFrac) {
			double NbotToReplace = (currentBotFrac2 - botFrac) / Nava;
			double pReplace = NbotToReplace / Nbot;
			for (String res : resources) {
				for (int i = 0; i < NresAtt; i++) {
					String rAttr = "rattr" + i;
					if (!config.getResourceAttrInfo().get(res).keySet().contains(rAttr) && randomGen.nextDouble() <= pReplace) {
						int value = DdataVal(i, resourceAttrToValueDistrib,
								AttributeType.ResourceAttr);
						HashSet<String> valueSet = new HashSet<String>();
						valueSet.add(Integer.toString(value));
						config.getResourceAttrInfo().get(res).put(rAttr, valueSet);
					}
				}
			}
		}
		
		for (Rule r : rules) {
			for (AttrValConjunct c : r.getUAE()) {
				String uattr = c.getLHS();
				int a = Integer.parseInt(uattr.substring(5));
				int index = -1;
				for (int i = 0; i < NComparableAttrGroups; i++) {
					if (comparableAttrGroups.get(i).getFirst().contains(a)) {
						index = i;
						break;
					}
				}
				if (uattrIsMulti[a] == false) {
					HashSet<String> valueSet = new HashSet<String>();
					for (String v : c.getRHS()) {
						valueSet.add(v + "_" + index);
					}
					c.getRHS().clear();
					c.getRHS().addAll(valueSet);
				} else {
					HashSet<HashSet<String>> valueSets = new HashSet<HashSet<String>>();
					for (HashSet<String> vSet : c.getRHSet()) {
						HashSet<String> valueSet = new HashSet<String>();
						for (String v : vSet) {
							valueSet.add(v + "_" + index);
						}
						valueSets.add(valueSet);
					}
					c.getRHSet().clear();
					c.getRHSet().addAll(valueSets);
				}
			}

			for (AttrValConjunct c : r.getRAE()) {
				String rattr = c.getLHS();
				int a = Integer.parseInt(rattr.substring(5));
				int index = -1;
				for (int i = 0; i < NComparableAttrGroups; i++) {
					if (comparableAttrGroups.get(i).getSecond().contains(a)) {
						index = i;
						break;
					}
				}
				if (rattrIsMulti[a] == false) {
					HashSet<String> valueSet = new HashSet<String>();
					for (String v : c.getRHS()) {
						valueSet.add(v + "_" + index);
					}
					c.getRHS().clear();
					c.getRHS().addAll(valueSet);
				} else {
					HashSet<HashSet<String>> valueSets = new HashSet<HashSet<String>>();
					for (HashSet<String> vSet : c.getRHSet()) {
						HashSet<String> valueSet = new HashSet<String>();
						for (String v : vSet) {
							valueSet.add(v + "_" + index);
						}
						valueSets.add(valueSet);
					}
					c.getRHSet().clear();
					c.getRHSet().addAll(valueSets);
				}
			}
		}

		try {
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			if (debug) {
				System.out.println(config.getUserAttrInfo());
				System.out.println(config.getResourceAttrInfo());
			}

			for (String user : users) {
				out.write("userAttrib(" + user + ",");
				if (debug) {
					System.out.print("userAttrib(" + user + ",");
				}
				int keySetSize = 0;
				String[] keyList = config.getUserAttrInfo().get(user).keySet()
						.toArray(new String[0]);
				Arrays.sort(keyList);
				for (String key : keyList) {
					if (!(config.getUserAttrInfo().get(user).get(key) == null)
							&& !key.equals("uid")) {
						out.write(key + "=");
						if (debug) {
							System.out.print(key + "=");
						}
						if (key.equals("uid")) {
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else if (!uattrIsMulti[Integer.parseInt(key
								.substring(5))]) {
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else {
							out.write("{");
							if (debug) {
								System.out.print("{");
							}
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v + " ");
								if (debug) {
									System.out.print(v + " ");
								}
							}
							out.write("}");
							if (debug) {
								System.out.print("}");
							}
						}
						if (++keySetSize < config.getUserAttrInfo().get(user)
								.keySet().size()) {
							out.write(",");
							if (debug) {
								System.out.print(",");
							}
						}
					}
				}
				out.write(")\n");
				if (debug) {
					System.out.print("\n\n");
				}
			}
			out.write("\n");

			for (String res : resources) {
				out.write("resourceAttrib(" + res + ",");
				if (debug) {
					System.out.print("resourceAttrib(" + res + ",");
				}
				int keySetSize = 0;
				String[] keyList = config.getResourceAttrInfo().get(res)
						.keySet().toArray(new String[0]);
				Arrays.sort(keyList);
				for (String key : keyList) {
					if (!(config.getResourceAttrInfo().get(res).get(key) == null)
							&& !config.getResourceAttrInfo().get(res).get(key)
									.isEmpty() && !key.equals("rid")) {
						out.write(key + "=");
						if (debug) {
							System.out.print(key + "=");
						}
						if (key.equals("rid")) {
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else if (!rattrIsMulti[Integer.parseInt(key
								.substring(5))]) {
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else {
							out.write("{");
							if (debug) {
								System.out.print("{");
							}
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v + " ");
								if (debug) {
									System.out.print(v + " ");
								}
							}
							out.write("}");
							if (debug) {
								System.out.print("}");
							}
						}
						if (++keySetSize < config.getResourceAttrInfo()
								.get(res).keySet().size()) {
							out.write(",");
							if (debug) {
								System.out.print(",");
							}
						}
					}
				}
				out.write(")\n");
				if (debug) {
					System.out.print(")\n");
				}
			}
			for (Rule r : rules) {
				out.write(r + "\n");
				if (debug) {
					System.out.print(r + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		if (outputAddFile != null) {
			// adding additional user attribute
			for (String user : users) {
				int numAddAttrs = 0;
				if (config.getUserAttrInfo().get(user).keySet().size() <= NuserAtt - 2) {
					while (numAddAttrs < NadditionalAttribWithVal) {
						int u = Dattrib();
						String uattr = "uattr" + u;
						if (config.getUserAttrInfo().get(user).keySet()
								.contains(uattr)
								&& config.getUserAttrInfo().get(user)
										.get(uattr) != null) {
							continue;
						} else {
							int value = DdataVal(u, userAttrToValueDistrib,
									AttributeType.UserAttr);
							HashSet<String> valueSet = new HashSet<String>();
							valueSet.add(Integer.toString(value));
							config.getUserAttrInfo().get(user)
									.put(uattr, valueSet);
							numAddAttrs++;
						}
					}
				}
				// should add additional permission attribute too!!!
			}

			try {
				FileWriter fstream = new FileWriter(outputAddFile);
				BufferedWriter out = new BufferedWriter(fstream);
				if (debug) {
					System.out.println(config.getUserAttrInfo());
					System.out.println(config.getResourceAttrInfo());
				}

				for (String user : users) {
					out.write("userAttrib(" + user + ",");
					if (debug) {
						System.out.print("userAttrib(" + user + ",");
					}
					int keySetSize = 0;
					String[] keyList = config.getUserAttrInfo().get(user)
							.keySet().toArray(new String[0]);
					Arrays.sort(keyList);
					for (String key : keyList) {
						if (!(config.getUserAttrInfo().get(user).get(key) == null)
								&& !key.equals("uid")) {
							out.write(key + "=");
							if (debug) {
								System.out.print(key + "=");
							}
							if (key.equals("uid")) {
								for (String v : config.getUserAttrInfo()
										.get(user).get(key)) {
									out.write(v);
									if (debug) {
										System.out.print(v);
									}
								}
							} else if (!uattrIsMulti[Integer.parseInt(key
									.substring(5))]) {
								for (String v : config.getUserAttrInfo()
										.get(user).get(key)) {
									out.write(v);
									if (debug) {
										System.out.print(v);
									}
								}
							} else {
								out.write("{");
								if (debug) {
									System.out.print("{");
								}
								for (String v : config.getUserAttrInfo()
										.get(user).get(key)) {
									out.write(v + " ");
									if (debug) {
										System.out.print(v + " ");
									}
								}
								out.write("}");
								if (debug) {
									System.out.print("}");
								}
							}
							if (++keySetSize < config.getUserAttrInfo()
									.get(user).keySet().size()) {
								out.write(",");
								if (debug) {
									System.out.print(",");
								}
							}
						}
					}
					out.write(")\n");
					if (debug) {
						System.out.print("\n\n");
					}
				}
				out.write("\n");

				for (String res : resources) {
					out.write("resourceAttrib(" + res + ",");
					if (debug) {
						System.out.print("resourceAttrib(" + res + ",");
					}
					int keySetSize = 0;
					String[] keyList = config.getResourceAttrInfo().get(res)
							.keySet().toArray(new String[0]);
					Arrays.sort(keyList);
					for (String key : keyList) {
						if (!(config.getResourceAttrInfo().get(res).get(key) == null)
								&& !config.getResourceAttrInfo().get(res)
										.get(key).isEmpty()
								&& !key.equals("rid")) {
							out.write(key + "=");
							if (debug) {
								System.out.print(key + "=");
							}
							if (key.equals("rid")) {
								for (String v : config.getResourceAttrInfo()
										.get(res).get(key)) {
									out.write(v);
									if (debug) {
										System.out.print(v);
									}
								}
							} else if (!rattrIsMulti[Integer.parseInt(key
									.substring(5))]) {
								for (String v : config.getResourceAttrInfo()
										.get(res).get(key)) {
									out.write(v);
									if (debug) {
										System.out.print(v);
									}
								}
							} else {
								out.write("{");
								if (debug) {
									System.out.print("{");
								}
								for (String v : config.getResourceAttrInfo()
										.get(res).get(key)) {
									out.write(v + " ");
									if (debug) {
										System.out.print(v + " ");
									}
								}
								out.write("}");
								if (debug) {
									System.out.print("}");
								}
							}
							if (++keySetSize < config.getResourceAttrInfo()
									.get(res).keySet().size()) {
								out.write(",");
								if (debug) {
									System.out.print(",");
								}
							}
						}
					}
					out.write(")\n");
					if (debug) {
						System.out.print(")\n");
					}
				}
				for (Rule r : rules) {
					out.write(r + "\n");
					if (debug) {
						System.out.print(r + "\n");
					}
				}
				out.close();
			} catch (Exception e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		return rules;
	}

	public static void outputRuleOverlap(ArrayList<Rule> rules, Config config) {
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				System.out.println(rules.get(i));
				System.out.println(rules.get(j));
				System.out.println(ABACMiner.computeRuleOverlap(rules.get(i),
						rules.get(j), config));
			}
		}

	}

	public static int Dop() {
		return randomGen.nextInt(Nop) + 1;
	}

	public static int DnumOp(int min) {
		float result = randomGen.nextFloat();
		if (Double.compare(result, 0.9) <= 0) {
			return Nop < 1 + min ? Nop : 1 + min;
		} else {
			return Nop < 2 + min ? Nop : 2 + min;
		}
	}

	public static int constraintRes(int uattr, Operator op) {
		if (op.equals(Operator.SUPSETEQ)) {
			int result = randomGen.nextInt(NresAtt);
			while (!rattrIsMulti[result]) {
				result = randomGen.nextInt(NresAtt);
			}
			return result;
		} else {
			int result = randomGen.nextInt(NresAtt);
			while (rattrIsMulti[result]) {
				result = randomGen.nextInt(NresAtt);
			}
			return result;
		}
	}

	public static Operator DrelationalOp(int uAttr) {
		float result = randomGen.nextFloat();
		if (uAttr == -1) {
			return Operator.EQUALS;
		}
		if (!uattrIsMulti[uAttr]) {
			return Operator.EQUALS;
		} else if (Double.compare(result, 0.75) <= 0) {
			return Operator.IN;
		} else {
			return Operator.SUPSETEQ;
		}
	}

	/**
	 * 
	 * @param min
	 *            minimum number of conjuncts
	 * @return number of conjuncts
	 */
	public static int DnumConjunct(int min) {
		float result = randomGen.nextFloat();
		if (Double.compare(result, 0.1) <= 0) {
			return min;
		} else if (Double.compare(result, 0.85) <= 0) {
			return min + 1;
		} else if (Double.compare(result, 0.95) <= 0) {
			return min + 2;
		} else {
			return min + 3;
		}
	}

	/**
	 * 
	 * @param min
	 *            minimum number of constraints
	 * @return number of constraints
	 */
	public static int DnumConstraint(int min) {
		float result = randomGen.nextFloat();
		if (Double.compare(result, 0.05) <= 0) {
			return min;
		} else if (Double.compare(result, 0.90) <= 0) {
			return min + 1;
		} else {
			return min + 2;
		}
	}

	/**
	 * 
	 * @param n
	 *            conjunct size
	 * @param min
	 *            minimum conjunct size
	 * @return conjunct size
	 */
	public static int DconjunctSize(int n, int min) {
		float result = randomGen.nextFloat();
		if (Double.compare(result, 0.85) <= 0) {
			return n < 1 + min ? n : 1 + min;
		} else if (Double.compare(result, 0.95) <= 0) {
			return n < 2 + min ? n : 2 + min;
		} else {
			return n < 3 + min ? n : 3 + min;
		}
	}

	/**
	 * 
	 * @param n
	 *            cardinality
	 * @return size of a conjunct for a multi-valued attribute
	 */
	public static int DmultiSz(int n) {
		float result = randomGen.nextFloat();
		if (Double.compare(result, 0.85) <= 0) {
			return n < 1 ? n : 1;
		} else if (Double.compare(result, 0.95) <= 0) {
			return n < 2 ? n : 2;
		} else {
			return n < 3 ? n : 3;
		}
	}

	public static int Dattrib() {
		return randomGen.nextInt(NuserAtt);
	}

	public static int DattribCon() {
		return randomGen.nextInt(NuserAtt);
	}

	/**
	 * @param Nrule
	 *            : the number of rules in the input policy
	 * @return cardinality for an group
	 */
	public static int DattribCardinality(int Nrule) {
		return Math.max(randomGen.nextInt(10 * Nrule - 1), 2);
	}

	/**
	 * 
	 * @return whether an attribute is a multi-valued attribute or single-valued
	 *         attribute
	 */
	public static boolean isMulti() {
		if (Double.compare(randomGen.nextFloat(), 0.2) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @param n
	 *            attribute
	 * @param attrToValueDistrib
	 *            distribution
	 * @param type
	 *            attribute type
	 * @return an attribute value
	 */
	public static int DdataVal(int n,
			ArrayList<ZipfDistrib> attrToValueDistrib, AttributeType type) {
		switch (type) {
		case UserAttr:
			return (attrToValueDistrib.get(n).getNextDistVal() + attributeShift[uGroupNumber[n]])
					% uCardinality[n];
		case ResourceAttr:
			return (attrToValueDistrib.get(n).getNextDistVal() + attributeShift[rGroupNumber[n]])
					% rCardinality[n];
		}
		return 0;
	}

	/**
	 * 
	 * @return number of users satisfying a rule
	 */
	public static int DnumUserPerRule() {
		final int standardDeviation = 4;
		final int minimum = 4;

		double r0 = randomGen.nextGaussian();
		double r = standardDeviation * r0 + NuserPerRule;
		return (int) (minimum > Math.floor(r) ? minimum : Math.floor(r));
	}
	
	public static boolean checkResourceSatisfiesRule(String r,
			ArrayList<AttrValConjunct> uae, ArrayList<AttrValConjunct> rae,
			ArrayList<AttrAttrConjunct> con, Config config) {
		if (!config.getResourceAttrInfo().containsKey(r)) {
			config.getResourceAttrInfo().put(r,
					new HashMap<String, HashSet<String>>());
		}
		HashMap<String, HashSet<String>> rad1 = new HashMap<String, HashSet<String>>(
				config.getResourceAttrInfo().get(r));
		// check whether resource satisfy rae
		for (AttrValConjunct c : rae) {
			if (!rad1.containsKey(c.getLHS())) {
				if (!rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
					HashSet<String> vals = new HashSet<String>();
					vals.add(randomElement(c.getRHS()));
					rad1.put(c.getLHS(), vals);
				} else {
					HashSet<String> vals = randomSet(c.getRHSet());
					rad1.put(c.getLHS(), vals);
				}
			} else if (!rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]
					&& c.getRHS().containsAll(rad1.get(c.getLHS()))) {
				continue;
			} else if (rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
				boolean satisfied = false;
				for (HashSet<String> s : c.getRHSet()) {
					if (rad1.get(c.getLHS()).containsAll(s)) {
						satisfied = true;
						break;
					}
				}
				if (!satisfied) {
					return false;
				} else {
					continue;
				}
			} else {
				return false;
			}
		}

		for (AttrAttrConjunct c : con) {
			if (rad1.keySet().contains(c.getLHS())) {
				boolean found = false;
				AttrValConjunct uConjunct = null;
				for (AttrValConjunct v : uae) {
					if (v.getLHS().equals(c.getRHS())) {
						uConjunct = v;
						found = true;
						break;
					}
				}
				if (found) {
					boolean satisfied = true;
					switch (c.getOperator()) {
					case EQUALS:
						if (!rad1.get(c.getLHS()).equals(uConjunct.getRHS())) {
							satisfied = false;
						}
						break;
					case IN:
						HashSet<String> rValSet = new HashSet<String>(
								rad1.get(c.getLHS()));
						rValSet.retainAll(uConjunct.getRHS());
						if (rValSet.isEmpty()) {
							rad1.get(c.getLHS()).add(
									randomElement(uConjunct.getRHS()));
						}
						break;
					case SUPSETEQ:
						boolean supsetEq = false;
						for (HashSet<String> uValSet : uConjunct.getRHSet()) {
							if (rad1.get(c.getLHS()).containsAll(uValSet)) {
								supsetEq = true;
								break;
							}
						}
						if (!supsetEq) {
							rad1.get(c.getLHS()).addAll(
									randomSet(uConjunct.getRHSet()));
						}
						break;
					default:
						break;
					}
					if (!satisfied) {
						return false;
					}
				}
			}
		}

		config.getResourceAttrInfo().put(r, rad1);
		return true;
	}

	/**
	 * check if the current user is possible to satify a rule
	 * 
	 * @param u
	 * @param uae
	 * @param rae
	 * @param con
	 * @param config
	 * @return
	 */
	public static boolean checkUserSatisfiesRule(String u,
			ArrayList<AttrValConjunct> uae, ArrayList<AttrValConjunct> rae,
			ArrayList<AttrAttrConjunct> con, Config config) {
		if (!config.getUserAttrInfo().containsKey(u)) {
			config.getUserAttrInfo().put(u,
					new HashMap<String, HashSet<String>>());
		}
		// uad1 is the user attribute info for u
		HashMap<String, HashSet<String>> uad1 = new HashMap<String, HashSet<String>>(
				config.getUserAttrInfo().get(u));
		// check whether user satisfy uae
		for (AttrValConjunct c : uae) {
			if (!uad1.containsKey(c.getLHS())) {
				if (!uattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
					HashSet<String> vals = new HashSet<String>();
					vals.add(randomElement(c.getRHS()));
					uad1.put(c.getLHS(), vals);
				} else {
					HashSet<String> vals = randomSet(c.getRHSet());
					uad1.put(c.getLHS(), vals);
				}
			} else if (!uattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]
					&& c.getRHS().containsAll(uad1.get(c.getLHS()))) {
				continue;
			} else if (uattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
				boolean satisfied = false;
				for (HashSet<String> s : c.getRHSet()) {
					if (uad1.get(c.getLHS()).containsAll(s)) {
						satisfied = true;
						break;
					}
				}
				if (!satisfied) {
					return false;
				} else {
					continue;
				}
			} else {
				return false;
			}
		}

		for (AttrAttrConjunct c : con) {
			if (uad1.keySet().contains(c.getLHS())) {
				boolean found = false;
				AttrValConjunct rConjunct = null;
				for (AttrValConjunct v : rae) {
					if (v.getLHS().equals(c.getRHS())) {
						rConjunct = v;
						found = true;
						break;
					}
				}
				if (found) {
					boolean satisfied = true;
					switch (c.getOperator()) {
					case EQUALS:
						if (!uad1.get(c.getLHS()).equals(rConjunct.getRHS())) {
							satisfied = false;
						}
						break;
					case IN:
						HashSet<String> uValSet = new HashSet<String>(
								uad1.get(c.getLHS()));
						uValSet.retainAll(rConjunct.getRHS());
						if (uValSet.isEmpty()) {
							uad1.get(c.getLHS()).add(
									randomElement(rConjunct.getRHS()));
						}
						break;
					case SUPSETEQ:
						boolean supsetEq = false;
						for (HashSet<String> rValSet : rConjunct.getRHSet()) {
							if (uad1.get(c.getLHS()).containsAll(rValSet)) {
								supsetEq = true;
								break;
							}
						}
						if (!supsetEq) {
							uad1.get(c.getLHS()).addAll(
									randomSet(rConjunct.getRHSet()));
						}
						break;
					default:
						break;
					}
					if (!satisfied) {
						return false;
					}
				}
			}
		}

		config.getUserAttrInfo().put(u, uad1);
		return true;
	}

	public static boolean checkUserAndResourceSatisfiesRule(String u, String r,
			ArrayList<AttrValConjunct> rae,
			ArrayList<AttrAttrConjunct> constraint, Config config) {
		HashMap<String, HashSet<String>> uad1 = new HashMap<String, HashSet<String>>(
				config.getUserAttrInfo().get(u));
		if (!config.getResourceAttrInfo().containsKey(r)) {
			config.getResourceAttrInfo().put(r,
					new HashMap<String, HashSet<String>>());
		}
		HashMap<String, HashSet<String>> rad1 = new HashMap<String, HashSet<String>>(
				config.getResourceAttrInfo().get(r));
		// let resource satisfy RAE
		for (AttrValConjunct c : rae) {
			if (!rad1.containsKey(c.getLHS())) {
				if (!rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
					HashSet<String> vals = new HashSet<String>();
					vals.add(randomElement(c.getRHS()));
					rad1.put(c.getLHS(), vals);
				} else {
					HashSet<String> vals = randomSet(c.getRHSet());
					rad1.put(c.getLHS(), vals);
				}
			} else if (!rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]
					&& c.getRHS().containsAll(rad1.get(c.getLHS()))) {
				continue;
			} else if (rattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
				boolean satisfied = false;
				for (HashSet<String> s : c.getRHSet()) {
					if (rad1.get(c.getLHS()).containsAll(s)) {
						satisfied = true;
						break;
					}
				}
				if (!satisfied) {
					return false;
				}
			} else {
				return false;
			}
		}

		for (AttrAttrConjunct c : constraint) {
			boolean uaEqualsBot = !uad1.containsKey(c.getLHS());
			boolean raEqualsBot = !rad1.containsKey(c.getRHS());
			// user attribute is not defined
			if (uaEqualsBot) {
				// use resource attribute assignment for user attribute
				if (!raEqualsBot) {
					switch (c.getOperator()) {
					case EQUALS:
						uad1.put(c.getLHS(),
								new HashSet<String>(rad1.get(c.getRHS())));
						break;
					case IN:
						uad1.put(c.getLHS(),
								new HashSet<String>(rad1.get(c.getRHS())));
						break;
					case SUPSETEQ:
						uad1.put(c.getLHS(),
								new HashSet<String>(rad1.get(c.getRHS())));
						break;
					default:
						break;
					}
				} else {
					HashSet<String> v = new HashSet<String>();
					if (!uattrIsMulti[Integer.parseInt(c.getLHS().substring(5))]) {
						v.add(Integer.toString(DdataVal(
								Integer.parseInt(c.getLHS().substring(5)),
								userAttrToValueDistrib, AttributeType.UserAttr)));
					} else {
						int sz = DmultiSz(uCardinality[Integer.parseInt(c
								.getLHS().substring(5))]);
						while (v.size() < sz) {
							v.add(Integer.toString(DdataVal(
									Integer.parseInt(c.getLHS().substring(5)),
									userAttrToValueDistrib,
									AttributeType.UserAttr)));
						}
					}
					uad1.put(c.getLHS(), v);
					switch (c.getOperator()) {
					case EQUALS:
						rad1.put(c.getRHS(),
								new HashSet<String>(uad1.get(c.getLHS())));
						break;
					case IN:
						HashSet<String> vSet = new HashSet<String>();
						vSet.add(randomElement(uad1.get(c.getLHS())));
						rad1.put(c.getRHS(), vSet);
						break;
					case SUPSETEQ:
						rad1.put(c.getRHS(),
								new HashSet<String>(uad1.get(c.getLHS())));
						break;
					default:
						break;
					}
				}
			} else {
				if (raEqualsBot) {
					switch (c.getOperator()) {
					case EQUALS:
						rad1.put(c.getRHS(),
								new HashSet<String>(uad1.get(c.getLHS())));
						break;
					case IN:
						HashSet<String> vSet = new HashSet<String>();
						vSet.add(randomElement(uad1.get(c.getLHS())));
						rad1.put(c.getRHS(), vSet);
						break;
					case SUPSETEQ:
						rad1.put(c.getRHS(),
								new HashSet<String>(uad1.get(c.getLHS())));
						break;
					default:
						break;
					}
				} else {
					boolean satisfied = false;
					switch (c.getOperator()) {
					case EQUALS:
						if (uad1.get(c.getLHS()).equals(rad1.get(c.getRHS()))) {
							satisfied = true;
						}
						break;
					case IN:
						if (uad1.get(c.getLHS()).containsAll(
								rad1.get(c.getRHS()))) {
							satisfied = true;
						}
						break;
					case SUPSETEQ:
						if (uad1.get(c.getLHS()).containsAll(
								rad1.get(c.getRHS()))) {
							satisfied = true;
						}
						break;
					default:
						break;
					}
					if (!satisfied) {
						return false;
					}
				}
			}
		}

		config.getUserAttrInfo().put(u, uad1);
		config.getResourceAttrInfo().put(r, rad1);

		return true;
	}

	public static <T> T randomElement(HashSet<T> S) {
		if (S == null || S.isEmpty()) {
			return null;
		}
		int size = S.size();
		int item = new Random().nextInt(size);
		int i = 0;
		T result = null;
		for (T obj : S) {
			if (i == item) {
				result = obj;
				break;
			}

			i = i + 1;
		}
		return result;
	}

	public static HashSet<String> randomSet(HashSet<HashSet<String>> S) {
		if (S == null) {
			return null;
		}
		int size = S.size();
		int item = new Random().nextInt(size);
		int i = 0;
		HashSet<String> result = null;
		for (HashSet<String> obj : S) {
			if (i == item) {
				result = obj;
				break;
			}
			i = i + 1;
		}
		return new HashSet<String>(result);
	}

	public static String newUser() {
		return Integer.toString(newUserIdentity++);
	}

	public static String newRes() {
		return Integer.toString(newResrIdentity++);
	}

	public static double zipfSkewSelector(int cardinality) {
		double probability = randomGen.nextFloat();
		int popRatio = 0;
		if (probability < 0.2) {
			popRatio = 1;
		} else if (0.2 <= probability && probability < 0.9) {
			popRatio = 10;
		} else {
			popRatio = 100;
		}
		return Math.log(popRatio) / Math.log(cardinality);
	}

	public static void randomExperiment() {
		Random rand = new Random();
		String inputPath = "ran-case-studies/synthetic_ran_";
		String outputPath = "output/synthetic_ran_";
		try {
			for (int i = 0; i < 50; i++) {
				int numRules = rand.nextInt(21) + 20;
				// int numRules = 39;
				System.out.println("Number of Rules:" + numRules);
				SyntheticPolicyCaseStudyGenerator.newResrIdentity = 0;
				SyntheticPolicyCaseStudyGenerator.newUserIdentity = 0;
				SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputPath
						+ i + "_" + numRules + ".abac", null, numRules, false,
						false, 0, 0, 0, 0, 0.0);
				Parser.config = new Config();
				Parser.parseInputFile(inputPath + i + "_" + numRules + ".abac");
				double startTime = System.currentTimeMillis();
				ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true,
						0, 0.0, true);
				String outputFile = outputPath + i + "_" + numRules + ".output";

				FileWriter fstream = new FileWriter(outputFile);
				BufferedWriter out = new BufferedWriter(fstream);
				out.write("INPUT RULES \n");
				double wsc;
				int totalInputWSC = 0;
				for (Rule r : Parser.config.getRuleList()) {
					out.write(numRules++ + ". " + r + "\n");
					wsc = r.getSize();
					totalInputWSC += wsc;
					out.write("WSC=" + wsc + "\n");
				}

				out.write("\ntotal Input WSC=" + totalInputWSC + "\n");

				out.write("Total UP size:"
						+ Parser.config.getCoveredUP().size() + "\n");

				numRules = 1;
				int totalOutputWSC = 0;
				out.write("OUTPUT RULES\n");
				for (Rule r : ABACMiner.resultRules) {
					out.write(numRules++ + ".\n" + r + "\n");
					wsc = r.getSize();
					totalOutputWSC += wsc;
					out.write("WSC=" + wsc + "\n");
				}
				out.write("\ntotal Output WSC=" + totalOutputWSC);

				out.close();
				double endTime = System.currentTimeMillis();
				double duration = endTime - startTime;
				System.out.println(totalInputWSC + " " + totalOutputWSC + " "
						+ (duration / 1000));
			}
		} catch (Exception e) {

		}
	}

	public static void addUserAttributes(Config config, String outputAddFile) {
		ArrayList<String> users = new ArrayList<String>(config.getUsers());
		ArrayList<String> resources = new ArrayList<String>(
				config.getResources());

		uattrIsMulti = new boolean[NuserAtt];
		rattrIsMulti = new boolean[NresAtt];

		for (String uattr : config.getUserAttrSet().keySet()) {
			if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Set) {
				uattrIsMulti[Integer.parseInt(uattr.substring(5))] = true;
			}
		}

		for (String rattr : config.getResourceAttrSet().keySet()) {
			if (config.getResourceAttrSet().get(rattr).getvType() == ValueType.Set) {
				rattrIsMulti[Integer.parseInt(rattr.substring(5))] = true;
			}
		}

		// adding additional user attribute
		for (String user : users) {
			int numAddAttrs = 0;
			if (config.getUserAttrInfo().get(user).keySet().size() <= NuserAtt - 2) {
				while (numAddAttrs < NadditionalAttribWithVal) {
					int u = Dattrib();
					String uattr = "uattr" + u;
					if (config.getUserAttrInfo().get(user).keySet()
							.contains(uattr)
							&& config.getUserAttrInfo().get(user).get(uattr) != null) {
						continue;
					} else {
						int value;
						if (config.getUserAttrSet().containsKey(uattr)) {
							if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Single) {
								value = DdataVal(u, userAttrToValueDistrib,
										AttributeType.UserAttr);
							} else {
								value = DdataVal(u, userAttrToValueDistrib,
										AttributeType.UserAttr);
							}
						} else {
							value = DdataVal(u, userAttrToValueDistrib,
									AttributeType.UserAttr);
						}
						HashSet<String> valueSet = new HashSet<String>();
						valueSet.add(Integer.toString(value));
						config.getUserAttrInfo().get(user).put(uattr, valueSet);
						numAddAttrs++;
					}
				}
			}
			// should add additional permission attribute too!!!
		}

		try {
			FileWriter fstream = new FileWriter(outputAddFile);
			BufferedWriter out = new BufferedWriter(fstream);
			if (debug) {
				System.out.println(config.getUserAttrInfo());
				System.out.println(config.getResourceAttrInfo());
			}

			for (String user : users) {
				out.write("userAttrib(" + user + ",");
				if (debug) {
					System.out.print("userAttrib(" + user + ",");
				}
				int keySetSize = 0;
				String[] keyList = config.getUserAttrInfo().get(user).keySet()
						.toArray(new String[0]);
				Arrays.sort(keyList);
				for (String key : keyList) {
					if (!(config.getUserAttrInfo().get(user).get(key) == null)
							&& !key.equals("uid")) {
						out.write(key + "=");
						if (debug) {
							System.out.print(key + "=");
						}
						if (key.equals("uid")) {
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else if (!uattrIsMulti[Integer.parseInt(key
								.substring(5))]) {
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else {
							out.write("{");
							if (debug) {
								System.out.print("{");
							}
							for (String v : config.getUserAttrInfo().get(user)
									.get(key)) {
								out.write(v + " ");
								if (debug) {
									System.out.print(v + " ");
								}
							}
							out.write("}");
							if (debug) {
								System.out.print("}");
							}
						}
						if (++keySetSize < config.getUserAttrInfo().get(user)
								.keySet().size()) {
							out.write(",");
							if (debug) {
								System.out.print(",");
							}
						}
					}
				}
				out.write(")\n");
				if (debug) {
					System.out.print("\n\n");
				}
			}
			out.write("\n");

			for (String res : resources) {
				out.write("resourceAttrib(" + res + ",");
				if (debug) {
					System.out.print("resourceAttrib(" + res + ",");
				}
				int keySetSize = 0;
				String[] keyList = config.getResourceAttrInfo().get(res)
						.keySet().toArray(new String[0]);
				Arrays.sort(keyList);
				for (String key : keyList) {
					if (!(config.getResourceAttrInfo().get(res).get(key) == null)
							&& !config.getResourceAttrInfo().get(res).get(key)
									.isEmpty() && !key.equals("rid")) {
						out.write(key + "=");
						if (debug) {
							System.out.print(key + "=");
						}
						if (key.equals("rid")) {
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else if (!rattrIsMulti[Integer.parseInt(key
								.substring(5))]) {
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v);
								if (debug) {
									System.out.print(v);
								}
							}
						} else {
							out.write("{");
							if (debug) {
								System.out.print("{");
							}
							for (String v : config.getResourceAttrInfo()
									.get(res).get(key)) {
								out.write(v + " ");
								if (debug) {
									System.out.print(v + " ");
								}
							}
							out.write("}");
							if (debug) {
								System.out.print("}");
							}
						}
						if (++keySetSize < config.getResourceAttrInfo()
								.get(res).keySet().size()) {
							out.write(",");
							if (debug) {
								System.out.print(",");
							}
						}
					}
				}
				out.write(")\n");
				if (debug) {
					System.out.print(")\n");
				}
			}
			for (Rule r : config.getRuleList()) {
				out.write(r + "\n");
				if (debug) {
					System.out.print(r + "\n");
				}
			}
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		generateCaseStudy("ran-case-studies/test.abac", null, 50, true, true,
				1, 1, 0, 0, 0);
	}
}
