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

package edu.dar.algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;
import java.util.Random;

import edu.dar.util.AttrAttrConjunct;
import edu.dar.util.AttrValConjunct;
import edu.dar.util.Attribute;
import edu.dar.util.AttributeType;
import edu.dar.util.Config;
import edu.dar.util.Operator;
import edu.dar.util.Pair;
import edu.dar.util.Parser;
import edu.dar.util.Rule;
import edu.dar.util.RulePairComparator;
import edu.dar.util.RuleQualityMetricType;
import edu.dar.util.RuleQualityValue;
import edu.dar.util.Time;
import edu.dar.util.Triple;
import edu.dar.util.UserPermComparator;
import edu.dar.util.ValueType;

public class ABACMiner {
	public static int numElimConjuncts = 0;
	public static int numElimValues = 0;
	public static int numElimElements = 0;
	public static int numElimConstraints = 0;
	public static int numGeneralizations = 0;
	public static int numMerges = 0;
	public static int numMergeRulesCalls = 1;
	public static int numSubsumptionCheck = 0;
	public static int numUnsuccessfulMerges = 0;
	public static int numRemovedPairs = 0;
	public static int numExtraCompares = 0;

	public static long startTime;
	public static long endTime;
	public static long totalTime = 0;
	public static long duration;

	public static boolean debug = false;
	public static boolean newElimConjunct = true;

	public static double underAssignFrac = 0.0;

	public static double upPreFrac = 0.2;
	public static int upPreNumTuples = 1000;

	public static ArrayList<Rule> resultRules;

	public static final HashSet<String> topSet;

	public static HashSet<Triple<String, String, String>> overAssignments;
	public static HashSet<Triple<String, String, String>> underAssignments;
	// top set is a special set that contains "TOP"
	static {
		topSet = new HashSet<String>();
		topSet.add("TOP");
	}

	/**
	 * add noisePercentage * |up0| numbers of over-assignment noise
	 * 
	 * @param config
	 * @param noisePercentage
	 */
	public static void addOverassignmentNoise(Config config,
			double noisePercentage) {
		int numNoise = (int) (config.getCoveredUP().size() * noisePercentage);

		while (config.getOverassignmentUP().size() < numNoise) {
			String user = randomElement(config.getUsers());
			String op = randomElement(config.getOps());
			String res = randomElement(config.getResources());
			Triple<String, String, String> userPerm = new Triple<String, String, String>(
					user, op, res);
			if (!config.getCoveredUP().contains(userPerm)) {
				config.getOverassignmentUP().add(userPerm);
				config.getCoveredUP().add(userPerm);
				config.getUserPerms().get(user)
						.add(new Pair<String, String>(op, res));
				if (!config.getPermUsers().containsKey(
						new Pair<String, String>(op, res))) {
					config.getPermUsers().put(
							new Pair<String, String>(op, res),
							new HashSet<String>());
				}
				config.getPermUsers().get(new Pair<String, String>(op, res))
						.add(user);
			}
		}
	}

	/**
	 * Compute the overlap of two rules
	 * 
	 * @param r1
	 * @param r2
	 * @param config
	 * @return the overlapping tuples
	 */
	public static HashSet<Triple<String, String, String>> computeRuleOverlap(
			Rule r1, Rule r2, Config config) {

		if (r1.getCoveredUPTriple() == null || r1.isChanged()) {
			r1.setCoveredUPTriple(new HashSet<Triple<String, String, String>>(
					computeCoveredUPTriple(r1, config)));
		}
		HashSet<Triple<String, String, String>> overlap = new HashSet<Triple<String, String, String>>(
				r1.getCoveredUPTriple());
		if (r2.getCoveredUPTriple() == null || r2.isChanged()) {
			r2.setCoveredUPTriple(new HashSet<Triple<String, String, String>>(
					computeCoveredUPTriple(r2, config)));
		}
		overlap.retainAll(r2.getCoveredUPTriple());
		return overlap;
	}

	/**
	 * add noisePercentage * |up0| numbers of under-assignment noise
	 * 
	 * @param config
	 * @param noisePercentage
	 */
	public static void addUnderassignmentNoise(Config config,
			double noisePercentage) {
		int numNoise = (int) (config.getCoveredUP().size() * noisePercentage);

		while (config.getUnderassignmentUP().size() < numNoise
				&& !config.getCoveredUP().isEmpty()) {
			Triple<String, String, String> element = randomElement(config
					.getCoveredUP());
			config.getUnderassignmentUP().add(element);
			config.getCoveredUP().remove(element);
			config.getUserPerms()
					.get(element.getFirst())
					.remove(new Pair<String, String>(element.getSecond(),
							element.getThird()));
			config.getPermUsers()
					.get(new Pair<String, String>(element.getSecond(), element
							.getThird())).remove(element.getFirst());
		}
	}

	/**
	 * Pick a randomeElement from a set S
	 * 
	 * @param S
	 * @return
	 */
	public static <T> T randomElement(HashSet<T> S) {
		if (S == null) {
			return null;
		}
		int size = S.size();
		int item = new Random(System.currentTimeMillis()).nextInt(size);
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

	public static void mineABACPolicy(Config config, boolean dbg,
			Integer qualityMetricType, boolean generalizeToSet,
			boolean elimConj, int overAssignThres, double underAssignF,
			boolean newApproach) {
		//System.out.println("Covered UP size:" + config.getCoveredUP().size());
		debug = dbg;
		newElimConjunct = elimConj;
		underAssignFrac = underAssignF;
		// Line 1 Rules is the set of candidate rules
		LinkedList<Rule> Rules = new LinkedList<Rule>();
		// Line 2 and 3
		// uncovUP containts user-permission tuples in UP0 that are not covered
		// by Rules
		HashSet<Triple<String, String, String>> uncovUP = new HashSet<Triple<String, String, String>>(
				config.getCoveredUP());

		RuleQualityMetricType qType = null;
		if (qualityMetricType <= 0 || qualityMetricType > 5) {
			qType = RuleQualityMetricType.QRUL;
		} else if (qualityMetricType == 1) {
			qType = RuleQualityMetricType.QRULANDB;
		} else if (qualityMetricType == 2) {
			qType = RuleQualityMetricType.QRULANDD;
		} else if (qualityMetricType == 3) {
			qType = RuleQualityMetricType.BANDQRUL;
		} else if (qualityMetricType == 4) {
			qType = RuleQualityMetricType.DANDQRUL;
		} else if (qualityMetricType == 5) {
			qType = RuleQualityMetricType.QMEAN;
		}

		// Line 4
		if (debug) {
			System.out.println("Total uncovUP size: " + uncovUP.size());
		}
		startTime = System.currentTimeMillis();
		//int numTupleToProcess = (int) (upPreFrac * config.getCoveredUP().size());
		int numTupleToProcess = upPreNumTuples;
		while (!uncovUP.isEmpty()) {
			if (!Rules.isEmpty()) {
				mergeRules(Rules, config);
				for (Rule r : Rules) {
					//System.out.println(r);
					uncovUP.removeAll(r.getCoveredUPTriple());
				}
				if (uncovUP.isEmpty()) {
					break;
				}
			}
			int numUPTuples = 0;
			int uncovUPSize = uncovUP.size();
			HashSet<Triple<String, String, String>> UPTuplesToProcess = new HashSet<Triple<String, String, String>>();
			if (newApproach) {
				ArrayList<Triple<String, String, String>> uncoveredUPList = new ArrayList<Triple<String, String, String>>(uncovUP);
				Collections.sort(uncoveredUPList, new UserPermComparator(config));
				int numUPTuplesToPick = numTupleToProcess <= uncoveredUPList.size() ? numTupleToProcess : uncoveredUPList.size();
				UPTuplesToProcess.addAll(uncoveredUPList.subList(0, numUPTuplesToPick));
//				while (numUPTuples < numTupleToProcess
//						&& numUPTuples < uncovUPSize) {
//					UPTuplesToProcess.add(randomElement(uncovUP));
//					numUPTuples = UPTuplesToProcess.size();
//				}
			} else {
				UPTuplesToProcess.addAll(uncovUP);
			}
			while (!UPTuplesToProcess.isEmpty()) {
				// select an uncovered user-permission tuple
				ArrayList<Triple<String, String, String>> coveredUPList = new ArrayList<Triple<String, String, String>>(
						UPTuplesToProcess);
				// ArrayList<Triple<String, String, String>> coveredUPList = new
				// ArrayList<Triple<String, String, String>>(
				// uncovUP);
				Collections.sort(coveredUPList, new UserPermComparator(config));
				String userSeed = coveredUPList.get(0).getFirst();
				Pair<String, String> permSeed = new Pair<String, String>(
						coveredUPList.get(0).getSecond(), coveredUPList.get(0)
								.getThird());
				HashSet<String> users = new HashSet<String>(config
						.getPermUsers().get(permSeed));
				ArrayList<String> usersList = new ArrayList<String>(users);

				if (debug) {
					System.out
							.println("==============================================");
					System.out
							.println("Select an uncovered user-permission tuple:");
					System.out.println("<u,p>=" + "<" + userSeed + ","
							+ permSeed + ">");
				}

				// Line 6
				ArrayList<AttrAttrConjunct> cc = candidateConstraint(userSeed,
						permSeed.getSecond(), config);
				Collections.sort(cc);
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Candidate constraint:");
					System.out.println(cc);
				}

				// Line 7-8 s_u contains users for which permSeed is uncovered
				// and
				// that have the same candidate constraint as userSeed
				HashSet<String> s_u = new HashSet<String>();
				s_u.add(userSeed);

				for (int i = 1; i < usersList.size(); i++) {
					ArrayList<AttrAttrConjunct> constraints = candidateConstraint(
							usersList.get(i), permSeed.getSecond(), config);
					Collections.sort(constraints);
					if (constraints.equals(cc)) {
						s_u.add(usersList.get(i));
					}
				}

				if (debug) {
					System.out
							.println("==============================================");
					System.out
							.println("The set of users which "
									+ permSeed
									+ " is uncovered and that have the same candidate constraint as "
									+ userSeed);
					System.out.println(s_u);
				}

				// Line 9 compute UAE
				ArrayList<AttrValConjunct> uae = computeUAE(s_u, config);

				// Line 10 compute RAE
				HashSet<String> rSet = new HashSet<String>();
				rSet.add(permSeed.getSecond());
				ArrayList<AttrValConjunct> pae = computeRAE(rSet, config);

				// Line 10
				Rule r = new Rule();
				r.setUAE(uae);
				r.setRAE(pae);
				HashSet<String> ops = new HashSet<String>();
				ops.add(permSeed.getFirst());
				r.setOps(ops);

				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Result of computeUAE:");
					System.out.println(uae);
					System.out
							.println("==============================================");
					System.out.println("Result of computePAE:");
					System.out.println(pae);
				}

				Rule gr = null;

				// line 12
				gr = generalizeRule(r, cc, config, uncovUP, qType, 0);
				// gr.setUaeChanged(true);
				// gr.setRaeChanged(true);
				numGeneralizations += gr.getCon().size();
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Generalized Rule: \n" + gr);
				}
				// line 13
				Rules.add(gr);
				// update covered UP of gr and uncovUP
				// gr.setCoveredUPTriple(computeCoveredUPTriple(gr, config));
				UPTuplesToProcess.removeAll(gr.getCoveredUPTriple());
				uncovUP.removeAll(gr.getCoveredUPTriple());

				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Remove UP Set: "
							+ gr.getCoveredUPTriple());
					if (debug) {
						System.out
								.println("==============================================");
						System.out.println("Remaining Uncovered UP Size: "
								+ uncovUP.size());
					}
				}

				HashSet<String> opSet = new HashSet<String>();
				opSet.add(permSeed.getFirst());

				HashSet<Pair<String, String>> perms = new HashSet<Pair<String, String>>(
						config.getUserPerms().get(userSeed));
				ArrayList<Pair<String, String>> permList = new ArrayList<Pair<String, String>>(
						perms);
				// line 14
				for (int i = 0; i < permList.size(); i++) {
					if (permList.get(i).getSecond()
							.equals(permSeed.getSecond())) {
						opSet.add(permList.get(i).getFirst());
					}
				}

				if (debug) {
					System.out
							.println("==============================================");
					System.out
							.println("The set of perms which "
									+ userSeed
									+ " is uncovered and that have the same candidate constraint as "
									+ permSeed);
					System.out.println(opSet);
				}

				// Line 15-17
				r = new Rule();
				HashSet<String> uSet = new HashSet<String>();
				uSet.add(userSeed);
				uae = computeUAE(uSet, config);
				r.setUAE(uae);
				r.setRAE(pae);
				r.setOps(opSet);
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Result of computeUAE:");
					System.out.println(uae);
					System.out
							.println("==============================================");
					System.out.println("Result of computePAE:");
					System.out.println(pae);
				}

				// Line 18
				gr = generalizeRule(r, cc, config, uncovUP, qType, 0);
				// gr.setUaeChanged(true);
				// gr.setRaeChanged(true);
				numGeneralizations += gr.getCon().size();
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Generalized Rule: \n" + gr);
				}

				// line 19
				Rules.add(gr);
				UPTuplesToProcess.removeAll(gr.getCoveredUPTriple());
				uncovUP.removeAll(gr.getCoveredUPTriple());

				if ((qType == RuleQualityMetricType.BANDQRUL || qType == RuleQualityMetricType.QRULANDB)
						&& !gr.getCon().isEmpty()) {
					if (!config.constraintsToNumRules
							.containsKey(new HashSet<AttrAttrConjunct>(gr
									.getCon()))) {
						config.constraintsToNumRules.put(
								new HashSet<AttrAttrConjunct>(gr.getCon()), 1);
					} else {
						config.constraintsToNumRules.put(
								new HashSet<AttrAttrConjunct>(gr.getCon()),
								config.constraintsToNumRules
										.get(new HashSet<AttrAttrConjunct>(gr
												.getCon())) + 1);
					}

					if (debug) {
						System.out
								.println("==============================================");
						System.out.println("Remaining Uncovered UP Size: "
								+ uncovUP.size());
					}
				}
			}
		}

		computeDuration();
	    if (debug) {
	    	System.out.println("==============================================");
	    	System.out.println("Time for the main while loop : " + duration
				+ " millisec");
		 }
		// line 15
		for (Rule r : Rules) {
			Collections.sort(r.getUAE());
			Collections.sort(r.getRAE());
			Collections.sort(r.getCon());
		}

		class RuleQualityComparator implements Comparator<Rule> {

			@Override
			public int compare(Rule r1, Rule r2) {
				if (r1 == r2) {
					return 0;
				}
				return r1.getQuality().compareTo(r2.getQuality());
			}
		}

		if (debug) {
			int nRules = 1;
			System.out
					.println("==============================================");
			System.out.println("BEFORE MERGING RULES\n");
			for (Rule r : Rules) {
				System.out.println(nRules++ + ".\n" + r);
			}
			nRules = 1;
		}

		mergeRules(Rules, config);
		if (debug) {
			int nRules = 1;
			System.out
					.println("==============================================");
			System.out.println("Round " + numMergeRulesCalls);
			for (Rule r : Rules) {
				System.out.println(nRules++ + ".\n" + r);
			}
			nRules = 1;
		}
		computeDuration();
		if (debug) {
			System.out
					.println("==============================================");
			System.out.println("Time for the mergeRules: " + duration
					+ " millisec");
		}

		while (simplifyRules(Rules, config, config.getCoveredUP(),
				RuleQualityMetricType.QRUL)) {
			computeDuration();
			if (debug) {
				System.out
						.println("==============================================");
				System.out.println("Time for simplifyRules Round: "
						+ numMergeRulesCalls + " " + duration + " millisec");
			}
			mergeRules(Rules, config);
			if (debug) {
				int nRules = 1;
				System.out
						.println("==============================================");
				System.out.println("Round " + ++numMergeRulesCalls);
				for (Rule r : Rules) {
					System.out.println(nRules++ + ".\n" + r);
				}
				nRules = 1;
			}
			computeDuration();
			if (debug) {
				System.out
						.println("==============================================");
				System.out.println("Time for mergeRules Round: "
						+ numMergeRulesCalls + " " + duration + " millisec");
			}
		}

		resultRules = new ArrayList<Rule>();

		uncovUP = new HashSet<Triple<String, String, String>>(
				config.getCoveredUP());
		for (Rule r : Rules) {
			r.setQuality(computeRuleQuality(r, uncovUP, config,
					RuleQualityMetricType.QRUL));
		}
		overAssignments = new HashSet<Triple<String, String, String>>();
		while (!uncovUP.isEmpty()) {
			Rule r = Collections.max(Rules, new RuleQualityComparator());
			resultRules.add(r);

			if (Double.compare(overAssignThres, 0) > 0) {
				HashSet<Triple<String, String, String>> overUP = new HashSet<Triple<String, String, String>>(
						r.getCoveredUPTriple());
				overUP.retainAll(config.getOverassignmentUP());
				// if (!overUP.isEmpty()) {
				// System.out
				// .println("==============================================");
				// System.out
				// .println("The rule contains overassignment: " + r);
				// System.out.println("Overassignments: " + overUP + " size:"
				// + overUP.size());
				// System.out.println("r's UP size: "
				// + r.getCoveredUPTriple().size());
				// System.out.println("r's rule quality "
				// + r.getQuality().firstComponent);
				// System.out.println("r is the " + resultRules.size()
				// + "th rule");
				// }
				//
				// else {
				// System.out
				// .println("==============================================");
				// System.out
				// .println("The rule does not contain overassignment: "
				// + r);
				// System.out.println("r's UP size: "
				// + r.getCoveredUPTriple().size());
				// System.out.println("r's rule quality "
				// + r.getQuality().firstComponent);
				// System.out.println("r is the " + resultRules.size()
				// + "th rule");
				// }
			}

			if (debug) {
				System.out
						.println("==============================================");
				System.out.println("Adding Rule: \n" + r);
			}
			Rules.remove(r);
			HashSet<Triple<String, String, String>> coveredUP = new HashSet<Triple<String, String, String>>(
					r.getCoveredUPTriple());
			coveredUP.retainAll(uncovUP);
			uncovUP.removeAll(computeCoveredUPTriple(r, config));

			if (coveredUP.size() <= overAssignThres) {
				overAssignments.addAll(coveredUP);
			}
			for (Rule r1 : Rules) {
				r1.setQuality(computeRuleQuality(r1, uncovUP, config,
						RuleQualityMetricType.QRUL));
			}
		}
		computeDuration();
		if (debug) {
			System.out.println("==============================================");
			System.out.println("Time for selection step: " + " " + duration
				+ " millisec");
		}

		int numRules = 1;
		int totalWSC = 0;
		double wsc = 0;

		if (debug) {
			System.out
					.println("==============================================");
			System.out.println("OUTPUT RULES\n");
			for (Rule r : resultRules) {
				System.out.println(numRules++ + ".\n" + r);
				wsc = r.getSize();
				totalWSC += wsc;
				System.out.println("WSC=" + wsc);
			}
			System.out.println("\ntotal WSC=" + totalWSC);

			int[] maxSimilarityArray = new int[resultRules.size()];

			for (int i = 0; i < resultRules.size(); i++) {
				Rule r1 = resultRules.get(i);
				double maxSimilarity = -1;
				double currSimilarity = 0;

				for (int j = 0; j < config.getRuleList().size(); j++) {
					Rule r2 = config.getRuleList().get(j);
					currSimilarity = syntacticSimilarityOfRules(r1, r2, config);
					if (Double.compare(currSimilarity, maxSimilarity) > 0) {
						maxSimilarityArray[i] = j;
						maxSimilarity = currSimilarity;
					}
				}
			}

			System.out
					.println("==============================================");
			System.out.println("INPUT-OUTPUT CORRESPONDENCE");
			for (int i = 0; i < config.getRuleList().size(); i++) {
				System.out
						.println((i + 1) + ". " + config.getRuleList().get(i));
				System.out.println("SIMILAR OUTPUT RULES:");
				for (int j = 0; j < maxSimilarityArray.length; j++) {
					if (maxSimilarityArray[j] == i) {
						System.out.print(resultRules.get(j));
						System.out.format(
								", similarity: %.2f",
								syntacticSimilarityOfRules(config.getRuleList()
										.get(i), resultRules.get(j), config));
						System.out.println();
					}
				}
				System.out.println();
			}
		}
		if (debug) {
			System.out
					.println("==============================================");
			System.out.println("Consistency Check Result:");
			System.out.println(consistencyCheck(resultRules, config));

			System.out
					.println("==============================================");
			System.out.println("Statistics:");
			System.out.println("Number of calls to mergeRules: "
					+ numMergeRulesCalls);
			System.out.println("Number of generalizations: "
					+ numGeneralizations);
			System.out.println("Number of merges: " + numMerges);
			System.out.println("Number of eliminated conjuncts: "
					+ numElimConjuncts);
			System.out.println("Number of eliminated values: " + numElimValues);
			System.out
					.println("Number of eliminated elements for multi-value: "
							+ numElimElements);
			System.out.println("Number of eliminated constraints: "
					+ numElimConstraints);
			System.out.println("Number of subsumption checks: "
					+ numSubsumptionCheck);
			System.out.println("Number of unsuccessful merges: "
					+ numUnsuccessfulMerges);
			System.out.println("Number of removed pairs: " + numRemovedPairs);
			System.out.println("Number of extra compares: " + numExtraCompares);

			computeDuration();
			System.out
					.println("==============================================");
			System.out.println("Remaining time: " + duration + " millisec");

		}
		underAssignments = new HashSet<Triple<String, String, String>>();
		if (Double.compare(underAssignF, 0) > 0) {
			// HashSet<Triple<String, String, String>> underAssignments = new
			// HashSet<Triple<String, String, String>>();
			for (Rule r : resultRules) {
				underAssignments
						.addAll(new HashSet<Triple<String, String, String>>(r
								.getCoveredUPTriple()));
			}
			underAssignments.removeAll(config.getCoveredUP());
			// System.out
			// .println("==============================================");
			// System.out.println("Under Assignment Noise: "
			// + config.getUnderassignmentUP());
			// System.out.println("Under Assignments in Output Policy: "
			// + underAssignments);
			// System.out
			// .println("Jaccard Similary of Under Assignment Noises and Under Assignments in Output Policy is: "
			// + jaccardSimilarity(config.getUnderassignmentUP(),
			// underAssignments));
		}
//		System.out.println("UnderAssignments size:" + underAssignments.size());
//		System.out.println("UnderAssignments:" + underAssignments);
		//
		// if (Double.compare(overAssignThres, 0) > 0) {
		// System.out
		// .println("==============================================");
		// System.out.println("Over Assignment Noise: "
		// + config.getOverassignmentUP());
		// System.out.println("Over Assignments in Output Policy: "
		// + overAssignments);
		// System.out
		// .println("Jaccard Similary of Over Assignment Noises and Over Assignments in Output Policy is: "
		// + jaccardSimilarity(config.getOverassignmentUP(),
		// overAssignments));
		//
		// }
	}

	public static <T> double jaccardSimilarity(HashSet<T> a, HashSet<T> b) {
		HashSet<T> s1 = new HashSet<T>(a);
		s1.retainAll(b);
		HashSet<T> s2 = new HashSet<T>(a);
		s2.addAll(b);
		if (s2.isEmpty()) {
			return 1.0;
		}
		return (double) s1.size() / s2.size();
	}

	public static boolean simplifyRules(LinkedList<Rule> rules, Config config,
			HashSet<Triple<String, String, String>> uncovUP,
			RuleQualityMetricType type) {
		boolean changed = false;
		boolean eliminated = true;
		
//		while (eliminated) {
//			eliminated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule r = rules.get(i);
				elimRedundantSets(r.getUAE(), config, AttributeType.UserAttr);

				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Eliminating Conjuncts for:\n" + r);
				}
				if (elimConjuncts(r, rules, config, uncovUP, i, type)) {
					changed = true;
					eliminated = true;
					r.setChanged(true);
					r.setUaeChanged(true);
					r.setRaeChanged(true);
				}
				//System.out.println(rules.get(i));
			}
	//	}

		// System.out.println("After Eliminating Conjuncts");
		// for (int i = 0; i < rules.size(); i++) {
		// System.out.println(i + "." + rules.get(i) + "\n");
		// }

		while (eliminated) {
			eliminated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule r = rules.get(i);
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Eliminating Overlap Elements for:\n"
							+ r);
				}
				if (elimOverlapBtwnRules(r, rules, config)) {
					changed = true;
					eliminated = true;
					r.setChanged(true);
					r.setUaeChanged(true);
					r.setRaeChanged(true);
					if (rules.size() > i && !r.equals(rules.get(i))) {
						i--;
					}
				}
				//System.out.println(rules.get(i));
			}
		}

//		while (eliminated) {
//			eliminated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule r = rules.get(i);
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Eliminating Elements for:\n" + r);
				}
				if (elimElements(r, rules, config)) {
					eliminated = true;
					changed = true;
					r.setChanged(true);
					r.setUaeChanged(true);
				}
				//System.out.println(rules.get(i));
			}
		//}
//		while (eliminated) {
//			eliminated = false;
			for (int i = 0; i < rules.size(); i++) {
				Rule r = rules.get(i);
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Eliminating Overlap Ops for:\n" + r);
				}
				if (elimOverlapOpsBtwnRules(r, rules, config)) {
					changed = true;
					eliminated = true;
					r.setChanged(true);
				}
				//System.out.println(rules.get(i));
			}
		//}

		for (int i = 0; i < rules.size(); i++) {
			Rule r = rules.get(i);
			if (debug) {
				System.out
						.println("==============================================");
				System.out.println("Eliminating Constraints for:\n" + r);
			}
			Rule bestRule = elimConstraints(r, r.getCon(), config, uncovUP,
					type);
			if (!bestRule.equals(r)) {
				changed = true;
				rules.set(i, bestRule);
				bestRule.setChanged(true);
			}
			//System.out.println(bestRule);
		}

		return changed;
	}

	public static Rule elimConstraints(Rule r,
			List<AttrAttrConjunct> constraints, Config config,
			HashSet<Triple<String, String, String>> uncovUP,
			RuleQualityMetricType type) {
		Rule bestRule = r;
		RuleQualityValue bestQuality = computeRuleQuality(bestRule, uncovUP,
				config, type);
		ArrayList<AttrAttrConjunct> validConstraints = new ArrayList<AttrAttrConjunct>(
				constraints);
		for (int i = 0; i < validConstraints.size(); i++) {
			Rule temp = new Rule(r);
			temp.getCon().remove(validConstraints.get(i));
			temp.setChanged(true);
			boolean isValid = false;
			if (Double.compare(underAssignFrac, 0) == 0) {
				isValid = isValidRule(temp, config);
			} else {
				isValid = isValidRuleWithUnderAssignment(temp, config,
						underAssignFrac);
			}
			if (!isValid) {
				// if (!isValidRule(temp, config)) {
				validConstraints.remove(i);
				i--;
			}
		}

		for (int i = 0; i < validConstraints.size(); i++) {
			Rule r1 = new Rule(r);
			r1.getCon().remove(validConstraints.get(i));
			r1.setChanged(true);
			boolean isValid = false;
			if (Double.compare(underAssignFrac, 0) == 0) {
				isValid = isValidRule(r1, config);
			} else {
				isValid = isValidRuleWithUnderAssignment(r1, config,
						underAssignFrac);
			}
			if (!isValid) {
				continue;
			}
			Rule tempR = elimConstraints(r1,
					validConstraints.subList(i + 1, validConstraints.size()),
					config, uncovUP, type);
			if (tempR != null) {
				RuleQualityValue quality = computeRuleQuality(tempR, uncovUP,
						config, type);
				if (quality.compareTo(bestQuality) > 0) {
					bestRule = tempR;
					bestQuality = quality;
				}
			}
		}
		return bestRule;
	}

	public static boolean elimElements(Rule r, LinkedList<Rule> rules,
			Config config) {
		boolean changed = false;
		HashSet<AttrValConjunct> removeConjunctSet = new HashSet<AttrValConjunct>();
		for (String a : config.getUserAttrSet().keySet()) {
			if (config.getUserAttrSet().get(a).getvType() == ValueType.Single) {
				continue;
			}
			for (AttrValConjunct c : r.getUAE()) {
				if (c.getLHS().equals(a)) {
					for (HashSet<String> s : c.getRHSet()) {
						HashSet<String> temp = new HashSet<String>(s);
						for (String v : temp) {
							s.remove(v);
							r.setUaeChanged(true);
							boolean uaeChanged = r.isUaeChanged();
							boolean paeChanged = r.isRaeChanged();
							boolean rchanged = r.isChanged();
							boolean isValid = false;
							if (Double.compare(underAssignFrac, 0) == 0) {
								isValid = isValidRule(r, config);
							} else {
								isValid = isValidRuleWithUnderAssignment(r,
										config, underAssignFrac);
							}

							if (!isValid) {
								// if (!isValidRule(r, config)) {
								s.add(v);
								r.setUaeChanged(uaeChanged);
								r.setRaeChanged(paeChanged);
								r.setChanged(rchanged);
							} else {
								changed = true;
								numElimElements++;
								if (debug) {
									System.out
											.println("==============================================");
									System.out.println("Removed Element " + v
											+ " from Rule " + r + " Conjunct "
											+ c);
								}
								if (s.isEmpty()) {
									removeConjunctSet.add(c);
									break;
								}
							}
						}
					}
				}
			}
			r.getUAE().removeAll(removeConjunctSet);
		}
		return changed;
	}

	public static boolean elimOverlapOpsBtwnRules(Rule r,
			LinkedList<Rule> rules, Config config) {
		boolean changed = false;
		for (Rule r1 : rules) {
			if (r == r1) {
				continue;
			}
			if (!(new HashSet<AttrAttrConjunct>(r.getCon()))
					.containsAll((new HashSet<AttrAttrConjunct>(r1.getCon())))) {
				continue;
			}
			if (!(r.getUserAttrs().containsAll(r1.getUserAttrs()))) {
				continue;
			}
			if (!(r.getPermAttrs().containsAll(r1.getPermAttrs()))) {
				continue;
			}
			boolean isSubset = true;
			for (AttrValConjunct c1 : r1.getUAE()) {
				String uattr = c1.getLHS();
				for (AttrValConjunct c2 : r.getUAE()) {
					if (c2.getLHS().equals(uattr)) {
						if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Single) {
							if (!c1.getRHS().containsAll(c2.getRHS())) {
								isSubset = false;
								break;
							}
						} else {
							if (!c1.getRHSet().containsAll(c2.getRHSet())) {
								isSubset = false;
								break;
							}
						}
					}
				}
				if (isSubset == false) {
					break;
				}
			}
			if (isSubset == false) {
				continue;
			}
			for (AttrValConjunct c1 : r1.getRAE()) {
				String pattr = c1.getLHS();
				for (AttrValConjunct c2 : r.getRAE()) {
					if (c2.getLHS().equals(pattr)) {
						if (config.getResourceAttrSet().get(pattr).getvType() == ValueType.Single) {
							if (!c1.getRHS().containsAll(c2.getRHS())) {
								isSubset = false;
								break;
							}
						} else {
							if (!c1.getRHSet().containsAll(c2.getRHSet())) {
								isSubset = false;
								break;
							}
						}
					}
				}
				if (isSubset == false) {
					break;
				}
			}
			if (isSubset == false) {
				continue;
			}
			HashSet<String> removeVals = new HashSet<String>();
			for (String op : r.getOps()) {
				if (!r1.getOps().contains(op)) {
					continue;
				} else {
					removeVals.add(op);
				}
			}
			r.getOps().removeAll(removeVals);
			numElimElements += removeVals.size();
			if (!removeVals.isEmpty()) {
				changed = true;
			}
			if (r.getOps().isEmpty()) {
				rules.remove(r);
				return true;
			}
		}
		return changed;
	}

	public static boolean elimOverlapBtwnRules(Rule r, LinkedList<Rule> rules,
			Config config) {
		boolean changed = false;
		for (AttrValConjunct c : r.getUAE()) {
			String attr = c.getLHS();
			if (config.getUserAttrSet().get(attr).getvType() == ValueType.Single) {
				HashSet<String> vals = c.getRHS();
				HashSet<String> removeVals = new HashSet<String>();
				for (String val : vals) {
					for (Rule r1 : rules) {
						if (r1.equals(r)) {
							continue;
						}
						if (!r.getCon().containsAll(r1.getCon())) {
							continue;
						}
						if (!(r.getUserAttrs().containsAll(r1.getUserAttrs()))) {
							continue;
						}
						if (!(r.getPermAttrs().containsAll(r1.getPermAttrs()))) {
							continue;
						}
						if (!r1.getUserAttrs().contains(attr)) {
							continue;
						}
						if (!r1.getOps().containsAll(r.getOps())) {
							continue;
						}
						boolean isSubset = true;
						for (AttrValConjunct c1 : r1.getUAE()) {
							String uattr = c1.getLHS();
							if (uattr.equals(attr)) {
								if (!c1.getRHS().contains(val)) {
									isSubset = false;
									break;
								} else {
									continue;
								}
							}
							for (AttrValConjunct c2 : r.getUAE()) {
								if (c2.getLHS().equals(uattr)) {
									if (config.getUserAttrSet().get(uattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						for (AttrValConjunct c1 : r1.getRAE()) {
							String pattr = c1.getLHS();
							for (AttrValConjunct c2 : r.getRAE()) {
								if (c2.getLHS().equals(pattr)) {
									if (config.getResourceAttrSet().get(pattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						removeVals.add(val);
						changed = true;
						r.setUaeChanged(true);
						if (debug) {
							System.out
									.println("==============================================");
							System.out.println("Removing " + val
									+ " from UAE of \n" + r + "because of \n"
									+ r1);
						}
					}
				}
				c.getRHS().removeAll(removeVals);
				numElimElements += removeVals.size();
				if (c.getRHS().isEmpty()) {
					rules.remove(r);
					if (debug) {
						System.out
								.println("==============================================");
						System.out.println("Removing Rule " + r);
					}
					return true;
				}
			} else if (config.getUserAttrSet().get(attr).getvType() == ValueType.Set) {
				HashSet<HashSet<String>> vals = c.getRHSet();
				HashSet<HashSet<String>> removeVals = new HashSet<HashSet<String>>();
				for (HashSet<String> val : vals) {
					for (Rule r1 : rules) {
						if (r1.equals(r)) {
							continue;
						}
						if (!r.getCon().containsAll(r1.getCon())) {
							continue;
						}
						if (!(r.getUserAttrs().containsAll(r1.getUserAttrs()))) {
							continue;
						}
						if (!(r.getPermAttrs().containsAll(r1.getPermAttrs()))) {
							continue;
						}
						if (!r1.getUserAttrs().contains(attr)) {
							continue;
						}
						if (!r1.getOps().containsAll(r.getOps())) {
							continue;
						}
						boolean isSubset = true;
						for (AttrValConjunct c1 : r1.getUAE()) {
							String uattr = c1.getLHS();
							if (uattr.equals(attr)) {
								if (!c1.getRHSet().contains(val)) {
									isSubset = false;
									break;
								} else {
									continue;
								}
							}
							for (AttrValConjunct c2 : r.getUAE()) {
								if (c2.getLHS().equals(uattr)) {
									if (config.getUserAttrSet().get(uattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						for (AttrValConjunct c1 : r1.getRAE()) {
							String pattr = c1.getLHS();
							for (AttrValConjunct c2 : r.getRAE()) {
								if (c2.getLHS().equals(pattr)) {
									if (config.getResourceAttrSet().get(pattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						removeVals.add(val);
						changed = true;
						r.setUaeChanged(true);
						if (debug) {
							System.out
									.println("==============================================");
							System.out.println("Removing " + val
									+ " from UAE of \n" + r + "because of \n"
									+ r1);
						}
					}
				}
				c.getRHSet().removeAll(removeVals);
				numElimElements += removeVals.size();
				if (c.getRHSet().isEmpty()) {
					rules.remove(r);
					if (debug) {
						System.out
								.println("==============================================");
						System.out.println("Removing Rule " + r);
					}
					return true;
				}
			}
		}

		for (AttrValConjunct c : r.getRAE()) {
			String attr = c.getLHS();
			if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Single) {

				HashSet<String> vals = c.getRHS();
				HashSet<String> removeVals = new HashSet<String>();
				for (String val : vals) {
					for (Rule r1 : rules) {
						if (r1.equals(r)) {
							continue;
						}
						if (!(new HashSet<AttrAttrConjunct>(r.getCon()))
								.containsAll((new HashSet<AttrAttrConjunct>(r1
										.getCon())))) {
							continue;
						}
						if (!(r.getUserAttrs().containsAll(r1.getUserAttrs()))) {
							continue;
						}
						if (!(r.getPermAttrs().containsAll(r1.getPermAttrs()))) {
							continue;
						}
						if (!r1.getPermAttrs().contains(attr)) {
							continue;
						}
						if (!r1.getOps().containsAll(r.getOps())) {
							continue;
						}
						boolean isSubset = true;
						for (AttrValConjunct c1 : r1.getRAE()) {
							String pattr = c1.getLHS();
							if (pattr.equals(attr)) {
								if (!c1.getRHS().contains(val)) {
									isSubset = false;
									break;
								} else {
									continue;
								}
							}
							for (AttrValConjunct c2 : r.getRAE()) {
								if (c2.getLHS().equals(pattr)) {
									if (config.getResourceAttrSet().get(pattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						for (AttrValConjunct c1 : r1.getUAE()) {
							String uattr = c1.getLHS();
							for (AttrValConjunct c2 : r.getUAE()) {
								if (c2.getLHS().equals(uattr)) {
									if (config.getUserAttrSet().get(uattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						removeVals.add(val);
						changed = true;
						r.setRaeChanged(true);
						if (debug) {
							System.out
									.println("==============================================");
							System.out.println("Removing " + val
									+ " from PAE of \n" + r + "because of \n"
									+ r1);
						}
					}
				}
				c.getRHS().removeAll(removeVals);
				numElimElements += removeVals.size();
				if (c.getRHS().isEmpty()) {
					rules.remove(r);
					if (debug) {
						System.out
								.println("==============================================");
						System.out.println("Removing Rule " + r);
					}
					return true;
				}
			} else if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Set) {

				HashSet<HashSet<String>> vals = c.getRHSet();
				HashSet<HashSet<String>> removeVals = new HashSet<HashSet<String>>();
				for (HashSet<String> val : vals) {
					for (Rule r1 : rules) {
						if (r1.equals(r)) {
							continue;
						}
						if (!(new HashSet<AttrAttrConjunct>(r.getCon()))
								.containsAll((new HashSet<AttrAttrConjunct>(r1
										.getCon())))) {
							continue;
						}
						if (!(r.getUserAttrs().containsAll(r1.getUserAttrs()))) {
							continue;
						}
						if (!(r.getPermAttrs().containsAll(r1.getPermAttrs()))) {
							continue;
						}
						if (!r1.getPermAttrs().contains(attr)) {
							continue;
						}
						if (!r1.getOps().containsAll(r.getOps())) {
							continue;
						}
						boolean isSubset = true;
						for (AttrValConjunct c1 : r1.getRAE()) {
							String pattr = c1.getLHS();
							if (pattr.equals(attr)) {
								if (!c1.getRHSet().contains(val)) {
									isSubset = false;
									break;
								} else {
									continue;
								}
							}
							for (AttrValConjunct c2 : r.getRAE()) {
								if (c2.getLHS().equals(pattr)) {
									if (config.getResourceAttrSet().get(pattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						for (AttrValConjunct c1 : r1.getUAE()) {
							String uattr = c1.getLHS();
							for (AttrValConjunct c2 : r.getUAE()) {
								if (c2.getLHS().equals(uattr)) {
									if (config.getUserAttrSet().get(uattr)
											.getvType() == ValueType.Single) {
										if (!c1.getRHS().containsAll(
												c2.getRHS())) {
											isSubset = false;
											break;
										}
									} else {
										if (!c1.getRHSet().containsAll(
												c2.getRHSet())) {
											isSubset = false;
											break;
										}
									}
								}
							}
							if (isSubset == false) {
								break;
							}
						}
						if (isSubset == false) {
							continue;
						}
						removeVals.add(val);
						changed = true;
						r.setRaeChanged(true);
						if (debug) {
							System.out
									.println("==============================================");
							System.out.println("Removing " + val
									+ "from PAE of \n" + r + "because of \n"
									+ r1);
						}
					}
				}
				c.getRHSet().removeAll(removeVals);
				numElimElements += removeVals.size();
				if (c.getRHSet().isEmpty()) {
					rules.remove(r);
					return true;
				}
			}
		}
		return changed;
	}

	public static boolean consistencyCheck(ArrayList<Rule> rules, Config config) {
		HashMap<Pair<String, String>, HashSet<String>> permUsers = new HashMap<Pair<String, String>, HashSet<String>>();
		for (Rule r : rules) {
			HashMap<Pair<String, String>, HashSet<String>> permUser = computeCoveredUP(
					r, config);
			for (Pair<String, String> perm : permUser.keySet()) {
				if (!permUsers.containsKey(perm)) {
					permUsers.put(perm, new HashSet<String>());
				}
				permUsers.get(perm).addAll(permUser.get(perm));
			}
		}
		if (!config.getPermUsers().keySet().equals(permUsers.keySet())) {
			if (debug) {
				System.out
						.println("==============================================");
				System.out.println("Inconsistency");
				HashSet<Pair<String, String>> permUserKeySet = new HashSet<Pair<String, String>>(
						config.getPermUsers().keySet());
				permUserKeySet.removeAll(permUsers.keySet());
				System.out.println("difference:" + permUserKeySet);
			}
			return false;
		}
		for (Pair<String, String> perm : config.getPermUsers().keySet()) {
			if (!config.getPermUsers().get(perm).equals(permUsers.get(perm))) {
				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Inconsistency in Permission " + perm);
					System.out.println("input:"
							+ config.getPermUsers().get(perm));
					System.out.println("output:" + permUsers.get(perm));
				}
				return false;
			}
		}
		return true;
	}

	public static <T> double setSimilarity(HashSet<T> s1, HashSet<T> s2) {
		HashSet<T> cloneSet1 = new HashSet<T>(s1);
		cloneSet1.retainAll(s2);
		HashSet<T> cloneSet2 = new HashSet<T>(s1);
		cloneSet2.addAll(s2);
		return (double) cloneSet1.size() / cloneSet2.size();
	}

	public static double constraintSimilarity(HashSet<AttrAttrConjunct> s1,
			HashSet<AttrAttrConjunct> s2) {
		HashSet<AttrAttrConjunct> cloneSet1 = new HashSet<AttrAttrConjunct>(s1);
		cloneSet1.retainAll(s2);
		HashSet<AttrAttrConjunct> cloneSet2 = new HashSet<AttrAttrConjunct>(s1);
		cloneSet2.addAll(s2);
		if (cloneSet2.size() == 0) {
			return 1.0;
		} else {
			return (double) cloneSet1.size() / cloneSet2.size();
		}
	}

	public static double nonsymSemanticSimilarityOfPolicies(
			ArrayList<Rule> rules1, Config config,
			HashSet<Triple<String, String, String>> upSet,
			HashSet<Triple<String, String, String>> overAssignment,
			HashSet<Triple<String, String, String>> underAssignment) {
		HashSet<Triple<String, String, String>> coveredUP1 = new HashSet<Triple<String, String, String>>();
		// HashSet<Triple<String, String, String>> coveredUP2 =
		// config.getCoveredUP();
		for (Rule r : rules1) {
			coveredUP1.addAll(computeCoveredUPTriple(r, config));
		}
		// for (Rule r : rules2) {
		// coveredUP2.addAll(computeCoveredUPTriple(r, config));
		// }
		coveredUP1.removeAll(overAssignment);
		coveredUP1.addAll(underAssignment);

		HashSet<Triple<String, String, String>> intersection = new HashSet<Triple<String, String, String>>(
				coveredUP1);
		intersection.retainAll(upSet);
		HashSet<Triple<String, String, String>> union = new HashSet<Triple<String, String, String>>(
				coveredUP1);
		union.addAll(upSet);

		return (double) intersection.size() / union.size();
	}

	/**
	 * 
	 * @param rules1
	 *            : the first policy
	 * @param rules2
	 *            : the second policy
	 * @param config
	 * @return the semantic similarity of two policies
	 */
	public static double semanticSimilarityOfPolicies(ArrayList<Rule> rules1,
			ArrayList<Rule> rules2, Config config) {
		HashSet<Triple<String, String, String>> coveredUP1 = new HashSet<Triple<String, String, String>>();
		HashSet<Triple<String, String, String>> coveredUP2 = new HashSet<Triple<String, String, String>>();
		for (Rule r : rules1) {
			coveredUP1.addAll(computeCoveredUPTriple(r, config));
		}
		for (Rule r : rules2) {
			coveredUP2.addAll(computeCoveredUPTriple(r, config));
		}

		HashSet<Triple<String, String, String>> intersection = new HashSet<Triple<String, String, String>>(
				coveredUP1);
		intersection.retainAll(coveredUP2);
		HashSet<Triple<String, String, String>> union = new HashSet<Triple<String, String, String>>(
				coveredUP1);
		union.addAll(coveredUP2);

		return (double) intersection.size() / union.size();
	}

	/**
	 * 
	 * @param rules1
	 *            : the first policy
	 * @param rules2
	 *            : the second policy
	 * @param config
	 * @return the syntactic similarity of two policies
	 */
	public static double nonsymmetricSyntacticSimilarityPolicies(
			ArrayList<Rule> rules1, ArrayList<Rule> rules2, Config config) {

		double similarity = 0.0;

		Rule maxRule = null;

		for (Rule r2 : rules2) {
			double maxSimilarity = 0.0;
			for (Rule r1 : rules1) {
				double currentSimilarity = syntacticSimilarityOfRules(r2, r1,
						config);
				if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
					maxSimilarity = currentSimilarity;
					maxRule = r1;
				}
			}
			similarity += maxSimilarity;

			// System.out.println(r2);
			// System.out.println(maxRule);
			// System.out.println(maxSimilarity);
		}
		similarity /= rules2.size();

		return similarity;
	}

	/**
	 * 
	 * @param rules1
	 *            : the first policy
	 * @param rules2
	 *            : the second policy
	 * @param config
	 * @return the syntactic similarity of two policies
	 */
	public static double symmetricSyntacticSimilarityOfPolicies(
			ArrayList<Rule> rules1, ArrayList<Rule> rules2, Config config) {
		double similarity1 = 0.0;
		double similarity2 = 0.0;

		for (Rule r1 : rules1) {
			double maxSimilarity = 0.0;
			for (Rule r2 : rules2) {
				double currentSimilarity = syntacticSimilarityOfRules(r1, r2,
						config);
				if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
					maxSimilarity = currentSimilarity;
				}
			}
			similarity1 += maxSimilarity;
		}
		similarity1 /= rules1.size();

		for (Rule r2 : rules2) {
			double maxSimilarity = 0.0;
			for (Rule r1 : rules1) {
				double currentSimilarity = syntacticSimilarityOfRules(r2, r1,
						config);
				if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
					maxSimilarity = currentSimilarity;
				}
			}
			similarity2 += maxSimilarity;
		}
		similarity2 /= rules2.size();

		return Math.max(similarity1, similarity2);
	}

	public static double syntacticSimilarityOfRules(Rule r1, Rule r2,
			Config config) {
		double uaeSimilarity = 0.0;
		double paeSimilarity = 0.0;
		double conSimilarity = 0.0;
		double opsSimilarity = 0.0;

		// System.out.println(r1);
		// System.out.println(r2);
		// compute syntactic similarity of UAEs
		int denominator = config.getUserAttrSet().keySet().size();
		for (String uattr : config.getUserAttrSet().keySet()) {
			// System.out.println("User attribute:" + uattr);
			boolean found1 = false;
			// look for uattr in c1
			for (AttrValConjunct c1 : r1.getUAE()) {
				if (c1.getLHS().equals(uattr)) {
					boolean found2 = false;
					for (AttrValConjunct c2 : r2.getUAE()) {
						if (c2.getLHS().equals(uattr)) {
							// uattr appears in both c1 and c2
							if (config.getUserAttrSet().get(c1.getLHS())
									.getvType() == ValueType.Single) {
								uaeSimilarity += setSimilarity(c1.getRHS(),
										c2.getRHS());
							} else {
								uaeSimilarity += setSimilarity(c1.getRHSet(),
										c2.getRHSet());
							}
							// System.out.println("Case 1:");
							// System.out.println(c1);
							// System.out.println(c2);
							// System.out.println(uaeSimilarity);
							found2 = true;
							break;
						}
					}
					if (found2 == false) {
						// System.out.println("Case 2:");
						if (config.getUserAttrSet().get(c1.getLHS()).getvType() == ValueType.Single) {
							uaeSimilarity += (double) c1.getRHS().size()
									/ config.getUserAttrSet().get(uattr)
											.getDomain().size();
							// System.out.println(c1);
							// System.out.println(config.getUserAttrSet().get(uattr).getDomain());
							// System.out.println(uaeSimilarity);
						} else {
							uaeSimilarity += (double) c1.getRHSet().size()
									/ config.getUserAttrSet().get(uattr)
											.getSetDomain().size();
							// System.out.println(c1);
							// System.out.println(config.getUserAttrSet().get(uattr).getSetDomain());
							// System.out.println(uaeSimilarity);
						}
					}
					found1 = true;
					break;
				}
			}
			if (found1 == false) {
				boolean found2 = false;
				for (AttrValConjunct c2 : r2.getUAE()) {
					if (c2.getLHS().equals(uattr)) {
						// System.out.println("Case 3:");
						if (config.getUserAttrSet().get(c2.getLHS()).getvType() == ValueType.Single) {
							uaeSimilarity += (double) c2.getRHS().size()
									/ config.getUserAttrSet().get(uattr)
											.getDomain().size();
							// System.out.println(c2);
							// System.out.println(config.getUserAttrSet().get(uattr).getDomain());
							// System.out.println(uaeSimilarity);
						} else {
							uaeSimilarity += (double) c2.getRHSet().size()
									/ config.getUserAttrSet().get(uattr)
											.getSetDomain().size();
							// System.out.println(c2);
							// System.out.println(config.getUserAttrSet().get(uattr).getSetDomain());
							// System.out.println(uaeSimilarity);
						}
						found2 = true;
						break;
					}
				}
				if (found2 == false) {
					// uaeSimilarity += 1.0;
					denominator -= 1;
					// System.out.println("Case 4:");
					// System.out.println(uaeSimilarity);
				}
			}
		}
		if (denominator > 0) {
			uaeSimilarity = uaeSimilarity / denominator;
		} else {
			uaeSimilarity = 1.0;
		}

		denominator = config.getResourceAttrSet().keySet().size();

		for (String pattr : config.getResourceAttrSet().keySet()) {
			boolean found1 = false;
			for (AttrValConjunct c1 : r1.getRAE()) {
				if (c1.getLHS().equals(pattr)) {
					boolean found2 = false;
					for (AttrValConjunct c2 : r2.getRAE()) {
						if (c2.getLHS().equals(pattr)) {
							if (config.getResourceAttrSet().get(pattr)
									.getvType() == ValueType.Single) {
								paeSimilarity += setSimilarity(c1.getRHS(),
										c2.getRHS());
							} else {
								paeSimilarity += setSimilarity(c1.getRHSet(),
										c2.getRHSet());
							}
							found2 = true;
							break;
						}
					}
					if (found2 == false) {
						if (config.getResourceAttrSet().get(pattr).getvType() == ValueType.Single) {
							paeSimilarity += (double) c1.getRHS().size()
									/ config.getResourceAttrSet().get(pattr)
											.getDomain().size();
						} else {
							paeSimilarity += (double) c1.getRHSet().size()
									/ config.getResourceAttrSet().get(pattr)
											.getSetDomain().size();
						}
					}
					found1 = true;
					break;
				}
			}
			if (found1 == false) {
				boolean found2 = false;
				for (AttrValConjunct c2 : r2.getRAE()) {
					if (c2.getLHS().equals(pattr)) {
						if (config.getResourceAttrSet().get(pattr).getvType() == ValueType.Single) {
							paeSimilarity += (double) c2.getRHS().size()
									/ config.getResourceAttrSet().get(pattr)
											.getDomain().size();
						} else {
							paeSimilarity += (double) c2.getRHSet().size()
									/ config.getResourceAttrSet().get(pattr)
											.getSetDomain().size();
						}
						found2 = true;
						break;
					}
				}
				if (found2 == false) {
					// paeSimilarity += 1.0;
					denominator -= 1;
				}
			}
		}
		if (denominator > 0) {
			paeSimilarity = paeSimilarity / denominator;
		} else {
			paeSimilarity = 1.0;
		}

		conSimilarity = constraintSimilarity(
				new HashSet<AttrAttrConjunct>(r1.getCon()),
				new HashSet<AttrAttrConjunct>(r2.getCon()));

		opsSimilarity = setSimilarity(r1.getOps(), r2.getOps());

		return (uaeSimilarity + paeSimilarity + conSimilarity + opsSimilarity) / 4;
	}

	public static boolean mergeRules(LinkedList<Rule> Rules, Config config) {
		boolean merged = false;
		PriorityQueue<Pair<Rule, Rule>> workSet = new PriorityQueue<Pair<Rule, Rule>>(
				100, new RulePairComparator());
		// remove redundant rules
		for (int i = 0; i < Rules.size(); i++) {
			Rule r1 = Rules.get(i);
			if (r1.getCoveredUPTriple() == null || r1.isRaeChanged()
					|| r1.isUaeChanged()) {
				r1.setCoveredUPTriple(computeCoveredUPTriple(r1, config));
				r1.setChanged(false);
			}
			// for (int j = i + 1; j < Rules.size(); j++) {
			// Rule r2 = Rules.get(j);
			// if (r2.getCoveredUPTriple() == null || r2.isRaeChanged()
			// || r2.isUaeChanged()) {
			// r2.setCoveredUPTriple(computeCoveredUPTriple(r2, config));
			// r2.setChanged(false);
			// }
			// if (r1.getCoveredUPTriple()
			// .containsAll(r2.getCoveredUPTriple())) {
			// Rules.remove(j);
			// if (debug) {
			// System.out
			// .println("==============================================");
			// System.out.println("Removing Rule " + r2
			// + "\nbecause of \n" + r1);
			// }
			// j--;
			// } else if (r2.getCoveredUPTriple().containsAll(
			// r1.getCoveredUPTriple())) {
			// Rules.remove(i);
			// if (debug) {
			// System.out
			// .println("==============================================");
			// System.out.println("Removing Rule " + r1
			// + "\nbecause of \n" + r2);
			// }
			// i--;
			// break;
			// }
			// }
		}
		// set up workSet
		for (int i = 0; i < Rules.size(); i++) {
			Rule r1 = Rules.get(i);
			if (r1.getQuality() == null) {
				r1.setQuality(computeRuleQuality(r1, config.getCoveredUP(),
						config, RuleQualityMetricType.QRUL));
			}
			for (int j = i + 1; j < Rules.size(); j++) {
				Rule r2 = Rules.get(j);
				if (r2.getQuality() == null) {
					r2.setQuality(computeRuleQuality(r2, config.getCoveredUP(),
							config, RuleQualityMetricType.QRUL));
				}
				if ((r1.getCon().equals(r2.getCon()))) {
					// if (r1.getQuality().compareTo(r2.getQuality()) >= 0) {
					workSet.add(new Pair<Rule, Rule>(r1, r2));
					// } else {
					// workSet.add(new Pair<Rule, Rule>(r2, r1));
					// }
				}
			}
		}
		HashSet<Rule> removedRules = new HashSet<Rule>();
		// System.out.println("Size of WorkSet: " + workSet.size());

		while (!workSet.isEmpty()) {
			// Rule r1 = workSet.get(0).getFirst();
			// Rule r2 = workSet.get(0).getSecond();
			// workSet.remove(0);
			Rule r1 = workSet.peek().getFirst();
			Rule r2 = workSet.peek().getSecond();
			workSet.poll();
			if (removedRules.contains(r1) || removedRules.contains(r2)) {
				continue;
			}

			Rule temp2 = r2;
			Rule temp = new Rule(r1);
			for (int n = 0; n < temp.getUAE().size(); n++) {
				AttrValConjunct c1 = temp.getUAE().get(n);
				boolean found = false;
				for (int m = 0; m < temp2.getUAE().size(); m++) {
					AttrValConjunct c2 = temp2.getUAE().get(m);
					if (c2.getLHS().equals(c1.getLHS())) {
						if (config.getUserAttrSet().get(c1.getLHS()).getvType() == ValueType.Single) {
							c1.getRHS().addAll(c2.getRHS());
						} else {
							c1.getRHSet().addAll(c2.getRHSet());
						}
						found = true;
						break;
					}
				}
				if (found == false) {
					temp.getUAE().remove(n);
					n--;
				}
			}
			for (int n = 0; n < temp.getRAE().size(); n++) {
				AttrValConjunct c1 = temp.getRAE().get(n);
				boolean found = false;
				for (int m = 0; m < temp2.getRAE().size(); m++) {
					AttrValConjunct c2 = temp2.getRAE().get(m);
					if (c2.getLHS().equals(c1.getLHS())) {
						if (config.getResourceAttrSet().get(c1.getLHS())
								.getvType() == ValueType.Single) {
							c1.getRHS().addAll(c2.getRHS());
						} else {
							c1.getRHSet().addAll(c2.getRHSet());
						}
						found = true;
						break;
					}
				}
				if (found == false) {
					temp.getRAE().remove(n);
					n--;
				}
			}
			temp.getOps().addAll(temp2.getOps());
			temp.setUaeChanged(true);
			temp.setRaeChanged(true);
			boolean isValid = false;
			if (Double.compare(underAssignFrac, 0) == 0) {
				isValid = isValidMergedRule(temp, r1, r2, config);
			} else {
				isValid = isValidRuleWithUnderAssignment(temp, config,
						underAssignFrac);
			}
			if (isValid) {
				// if (isValidMergedRule(temp, r1, r2, config)) {
				merged = true;
				numMerges++;
				removedRules.add(r1);
				removedRules.add(r2);
				if (debug) {
					System.out.println("Merging two rules:");
					System.out.println("1." + r1);
					System.out.println("2." + r2);
				}
				ListIterator<Rule> itr = Rules.listIterator();

				while (itr.hasNext()) {
					Rule r = itr.next();
					if (r.equals(r1) || r.equals(r2)) {
						itr.remove();
						continue;
					}

					if (r.getCoveredUPTriple() == null || r.isChanged()
							|| r.isRaeChanged() || r.isUaeChanged()) {
						r.setCoveredUPTriple(computeCoveredUPTriple(r, config));
						r.setChanged(false);
					}

					if (temp.getOps().containsAll(r.getOps())
							&& temp.getCoveredUPTriple().size() >= r
									.getCoveredUPTriple().size()) {
						if (temp.getCoveredUPTriple().containsAll(
								r.getCoveredUPTriple())) {
							numSubsumptionCheck++;
							removedRules.add(r);
							itr.remove();
							continue;
						} else {
							numExtraCompares++;
						}
					}
					if (temp.getCon().equals(r.getCon())) {
						// workSet.add(0, new Pair<Rule, Rule>(temp, r));

						temp.setQuality(computeRuleQuality(temp,
								config.getCoveredUP(), config,
								RuleQualityMetricType.QRUL));

						workSet.add(new Pair<Rule, Rule>(temp, r));
					}

				}

				if (debug) {
					System.out
							.println("==============================================");
					System.out.println("Result Rule " + temp);
				}
				Rules.add(temp);

			} else {
				numUnsuccessfulMerges++;
			}
		}
		return merged;
	}

	/**
	 * compute all possible constraints between a user and a
	 * permission(resource)
	 * 
	 * @param user
	 *            : user identity
	 * @param perm
	 *            : permission identity
	 * @param config
	 *            : configuration
	 * @return a list of constraints
	 */
	public static ArrayList<AttrAttrConjunct> candidateConstraint(String user,
			String perm, Config config) {
		ArrayList<AttrAttrConjunct> constraints = new ArrayList<AttrAttrConjunct>();
		for (String uattr : config.getUserAttrInfo().get(user).keySet()) {
			HashSet<String> uvals = config.getUserAttrInfo().get(user)
					.get(uattr);
			if (uvals == null) {
				continue;
			}
			for (String pattr : config.getResourceAttrInfo().get(perm).keySet()) {
				HashSet<String> pvals = config.getResourceAttrInfo().get(perm)
						.get(pattr);
				if (pvals == null) {
					continue;
				}
				if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Single
						&& config.getResourceAttrSet().get(pattr).getvType() == ValueType.Single
						&& uvals.equals(pvals)) {
					// add equal relation
					constraints.add(new AttrAttrConjunct(uattr, pattr,
							Operator.EQUALS));
				} else if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Set
						&& config.getResourceAttrSet().get(pattr).getvType() == ValueType.Set
						&& uvals.containsAll(pvals)) {
					// add supseteq relation
					constraints.add(new AttrAttrConjunct(uattr, pattr,
							Operator.SUPSETEQ));
				} else if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Set
						&& config.getResourceAttrSet().get(pattr).getvType() == ValueType.Set
						&& pvals.containsAll(uvals)) {
					// add sunseteq relation
					constraints.add(new AttrAttrConjunct(uattr, pattr,
							Operator.SUBSETEQ));
				} else if (config.getUserAttrSet().get(uattr).getvType() == ValueType.Set
						&& config.getResourceAttrSet().get(pattr).getvType() == ValueType.Single
						&& uvals.containsAll(pvals)) {
					// add contains relation
					constraints.add(new AttrAttrConjunct(uattr, pattr,
							Operator.IN));
				}
			}
		}
		return constraints;
	}

	public static ArrayList<AttrValConjunct> computeRAE(HashSet<String> s,
			Config config) {
		// construct a set of conjuncts
		ArrayList<AttrValConjunct> rae = new ArrayList<AttrValConjunct>();
		int count = 0;
		for (String perm : s) {
			count++;
			for (String attr : config.getResourceAttrInfo().get(perm).keySet()) {
				if (attr.equals("rid")) {
					continue;
				}
				boolean flag = false;
				for (AttrValConjunct c : rae) {
					if (c.getLHS().equals(attr)) {
						if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Single) {
							if (!c.getRHS().contains("TOP")) {
								c.getRHS().addAll(
										config.getResourceAttrInfo().get(perm)
												.get(attr));
							}

						} else {
							if (!c.getRHSet().contains(topSet)) {
								c.getRHSet().add(
										new HashSet<String>(config
												.getResourceAttrInfo()
												.get(perm).get(attr)));
							}
						}
						flag = true;
						break;
					}
				}
				if (!flag) {
					// This is the first user
					if (count == 1) {
						if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Single) {
							rae.add(new AttrValConjunct(attr,
									new HashSet<String>(config
											.getResourceAttrInfo().get(perm)
											.get(attr)), null, "="));
						} else {
							HashSet<HashSet<String>> valSet = new HashSet<HashSet<String>>();
							valSet.add(new HashSet<String>(config
									.getResourceAttrInfo().get(perm).get(attr)));
							rae.add(new AttrValConjunct(attr, null, valSet, "="));
						}
					} else { // This is not the first user, then the conjunct
								// should be top
						if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Single) {
							HashSet<String> top = new HashSet<String>();
							top.add("TOP");
							rae.add(new AttrValConjunct(attr, top, null, "="));
						} else {
							HashSet<HashSet<String>> top = new HashSet<HashSet<String>>();
							top.add(topSet);
							rae.add(new AttrValConjunct(attr, null, top, "="));
						}
					}
				}
			}

			for (AttrValConjunct c : rae) {
				String attr = c.getLHS();
				if (!config.getResourceAttrInfo().get(perm).keySet()
						.contains(attr)) {
					if (config.getResourceAttrSet().get(attr).getvType() == ValueType.Single) {
						c.getRHS().removeAll(c.getRHS());
						c.getRHS().add("TOP");
					} else {
						c.getRHSet().removeAll(c.getRHSet());
						c.getRHSet().add(topSet);
					}
				}
			}
		}

		// test if there are any outsiders
		boolean noOutsider = true;
		for (int i = 0; i < rae.size(); i++) {
			AttrValConjunct c = rae.get(i);
			if (c.getRHS() != null && c.getRHS().contains("TOP")) {
				rae.remove(i);
				i--;
			}
			if (c.getRHSet() != null && c.getRHSet().contains(topSet)) {
				rae.remove(i);
				i--;
			}
		}

		for (String resource : config.getResources()) {
			if (s.contains(resource)) {
				continue;
			}
			try {
				if (Parser.satisfyingRAE(resource, rae, config)) {
					noOutsider = false;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// add rid expression
		if (noOutsider == false) {
			AttrValConjunct c = new AttrValConjunct("rid", s, null, "=");
			rae.add(c);
		}

		// elimRedundantSets(rae, config, AttributeType.ResourceAttr);
		return rae;
	}

	/**
	 * compute UAE for a set of users s
	 * 
	 * @param s
	 *            : a set of users
	 * @param config
	 *            : configuration
	 * @return UAE
	 */
	public static ArrayList<AttrValConjunct> computeUAE(HashSet<String> s,
			Config config) {
		// construct a set of conjuncts
		ArrayList<AttrValConjunct> uae = new ArrayList<AttrValConjunct>();
		int count = 0;
		for (String user : s) {
			count++;
			for (String attr : config.getUserAttrInfo().get(user).keySet()) {
				if (attr.equals("uid")) {
					continue;
				}
				boolean flag = false;
				for (AttrValConjunct c : uae) {
					if (c.getLHS().equals(attr)) {
						if (config.getUserAttrSet().get(attr).getvType() == ValueType.Single) {
							if (!c.getRHS().contains("TOP")) {
								c.getRHS().addAll(
										config.getUserAttrInfo().get(user)
												.get(attr));
							}

						} else {
							if (!c.getRHSet().contains(topSet)) {
								c.getRHSet().add(
										new HashSet<String>(config
												.getUserAttrInfo().get(user)
												.get(attr)));
							}
						}
						flag = true;
						break;
					}
				}
				if (!flag) {
					if (count == 1) {
						if (config.getUserAttrSet().get(attr).getvType() == ValueType.Single) {
							uae.add(new AttrValConjunct(attr,
									new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.get(attr)), null, "="));
						} else {
							HashSet<HashSet<String>> valSet = new HashSet<HashSet<String>>();
							valSet.add(new HashSet<String>(config
									.getUserAttrInfo().get(user).get(attr)));
							uae.add(new AttrValConjunct(attr, null, valSet, "="));
						}
					} else {
						if (config.getUserAttrSet().get(attr).getvType() == ValueType.Single) {
							HashSet<String> top = new HashSet<String>();
							top.add("TOP");
							uae.add(new AttrValConjunct(attr, top, null, "="));
						} else {
							HashSet<HashSet<String>> top = new HashSet<HashSet<String>>();
							top.add(topSet);
							uae.add(new AttrValConjunct(attr, null, top, "="));
						}
					}
				}
			}

			for (AttrValConjunct c : uae) {
				String attr = c.getLHS();
				if (!config.getUserAttrInfo().get(user).keySet().contains(attr)) {
					if (config.getUserAttrSet().get(attr).getvType() == ValueType.Single) {
						c.getRHS().removeAll(c.getRHS());
						c.getRHS().add("TOP");
					} else {
						c.getRHSet().removeAll(c.getRHSet());
						c.getRHSet().add(topSet);
					}
				}
			}
		}

		// test if there are any outsiders
		boolean noOutsider = true;
		for (int i = 0; i < uae.size(); i++) {
			AttrValConjunct c = uae.get(i);
			if (c.getRHS() != null && c.getRHS().contains("TOP")) {
				uae.remove(i);
				i--;
			}
			if (c.getRHSet() != null && c.getRHSet().contains(topSet)) {
				uae.remove(i);
				i--;
			}
		}

		for (String user : config.getUsers()) {
			if (s.contains(user)) {
				continue;
			}
			try {
				if (Parser.satisfyingUAE(user, uae, config)) {
					noOutsider = false;
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// add uid expression
		if (noOutsider == false) {
			AttrValConjunct c = new AttrValConjunct("uid", s, null, "=");
			uae.add(c);
		}
		elimRedundantSets(uae, config, AttributeType.UserAttr);

		return uae;
	}

	public static boolean elimRedundantSets(ArrayList<AttrValConjunct> ae,
			Config config, AttributeType type) {
		HashSet<AttrValConjunct> removeConjunctSet = new HashSet<AttrValConjunct>();
		switch (type) {
		case UserAttr:
			for (AttrValConjunct c : ae) {
				if (config.getUserAttrSet().get(c.getLHS()).getvType() == ValueType.Set) {
					HashSet<HashSet<String>> removeSet = new HashSet<HashSet<String>>();
					boolean foundEmptySet = false;
					for (HashSet<String> s1 : c.getRHSet()) {
						if (s1.isEmpty()) {
							foundEmptySet = true;
							break;
						}
						boolean found = false;
						for (HashSet<String> s2 : c.getRHSet()) {
							if (s1 != s2 && s1.containsAll(s2)) {
								found = true;
								break;
							}
						}
						if (found) {
							removeSet.add(s1);
						}
					}
					if (foundEmptySet == true) {
						removeConjunctSet.add(c);
						continue;
					}
					if (debug && !removeSet.isEmpty()) {
						System.out
								.println("==============================================");
						System.out.println("Removed Sets for UAE " + c);
						System.out.println(removeSet);
					}
					c.getRHSet().removeAll(removeSet);
				}
			}
			ae.removeAll(removeConjunctSet);
			break;
		case ResourceAttr:
			for (AttrValConjunct c : ae) {
				if (config.getResourceAttrSet().get(c.getLHS()).getvType() == ValueType.Set) {
					HashSet<HashSet<String>> removeSet = new HashSet<HashSet<String>>();
					boolean foundEmptySet = false;
					for (HashSet<String> s1 : c.getRHSet()) {
						if (s1.isEmpty()) {
							foundEmptySet = true;
							break;
						}
						boolean found = false;
						for (HashSet<String> s2 : c.getRHSet()) {
							if (s1 != s2 && s1.containsAll(s2)) {
								found = true;
								break;
							}
						}
						if (found) {
							removeSet.add(s1);
						}
					}
					if (foundEmptySet == true) {
						removeConjunctSet.add(c);
						continue;
					}
					if (debug && !removeSet.isEmpty()) {
						System.out
								.println("==============================================");
						System.out.println("Removed Sets for UAE " + c);
						System.out.println(removeSet);
					}
					c.getRHSet().removeAll(removeSet);
				}
			}
			ae.removeAll(removeConjunctSet);
			break;
		default:
			break;
		}
		return false;
	}

	public static ArrayList<Rule> generalizeRuleToSet(Rule r,
			List<AttrAttrConjunct> constraints, Config config,
			HashSet<Triple<String, String, String>> uncovUP,
			RuleQualityMetricType type) {
		// bestRules is the all generalizations of r with the best quality
		ArrayList<Rule> bestRules = new ArrayList<Rule>();
		bestRules.add(r);
		RuleQualityValue bestQuality = computeRuleQuality(r, uncovUP, config,
				type);
		// cc contains formulas from constraints that lead to valid
		// generalizations of r
		ArrayList<AttrAttrConjunct> cc = new ArrayList<AttrAttrConjunct>();
		// gen[i] is a generalization of r using formula cc[i]
		ArrayList<Rule> gen = new ArrayList<Rule>();

		for (int i = 0; i < constraints.size(); i++) {
			Rule r1 = new Rule(r);
			String uattr = constraints.get(i).getLHS();
			String pattr = constraints.get(i).getRHS();
			// try to generalize r by adding constraints[i] and eliminating both
			// relevant conjuncts
			for (int j = 0; j < r1.getUAE().size(); j++) {
				if (uattr.equals(r1.getUAE().get(j).getLHS())) {
					r1.getUAE().remove(j);
					r1.setUaeChanged(true);
					break;
				}
			}
			for (int j = 0; j < r1.getRAE().size(); j++) {
				if (pattr.equals(r1.getRAE().get(j).getLHS())) {
					r1.getRAE().remove(j);
					r1.setRaeChanged(true);
					break;
				}
			}
			r1.getCon().add(constraints.get(i));
			if (isValidRule(r1, config)) {
				cc.add(constraints.get(i));
				gen.add(r1);
				r1.setChanged(true);
			} else {
				// try to generalize r by adding constraints[i] and eliminating
				// one relevant conjunct
				Rule r2 = new Rule(r);
				pattr = constraints.get(i).getRHS();
				for (int j = 0; j < r2.getRAE().size(); j++) {
					if (pattr.equals(r2.getRAE().get(j).getLHS())) {
						r2.getRAE().remove(j);
						r2.setRaeChanged(true);
						break;
					}
				}
				r2.getCon().add(constraints.get(i));
				r2.setChanged(true);
				if (isValidRule(r2, config)) {
					cc.add(constraints.get(i));
					gen.add(r2);
					r2.setChanged(true);
				} else {
					Rule r3 = new Rule(r);
					uattr = constraints.get(i).getLHS();
					for (int j = 0; j < r3.getUAE().size(); j++) {
						if (uattr.equals(r3.getUAE().get(j).getLHS())) {
							r3.getUAE().remove(j);
							r3.setUaeChanged(true);
							break;
						}
					}
					r3.getCon().add(constraints.get(i));
					r3.setChanged(true);
					if (isValidRule(r3, config)) {
						cc.add(constraints.get(i));
						gen.add(r3);
						r3.setChanged(true);
					}
				}
			}
		}

		for (int i = 0; i < cc.size(); i++) {
			// try to further generalize gen[i]
			ArrayList<Rule> tempR = generalizeRuleToSet(gen.get(i),
					cc.subList(i + 1, cc.size()), config, uncovUP, type);
			if (tempR != null && !tempR.isEmpty()) {
				RuleQualityValue quality = computeRuleQuality(tempR.get(0),
						uncovUP, config, type);
				// if quality is better, replace the current bestRules
				if (quality.compareTo(bestQuality) > 0) {
					bestRules = tempR;
					bestQuality = quality;
				}
				// if quality is equal, add all rules in tempR to bestRules
				if (quality.compareTo(bestQuality) == 0) {
					for (Rule tr : tempR) {
						if (!bestRules.contains(tr)) {
							bestRules.add(tr);
						}
					}

				}
			}
		}

		return bestRules;
	}

	/**
	 * generalize a rule by adding atomic constraints to it
	 * 
	 * @param r
	 * @param constraints
	 * @param config
	 * @param uncovUP
	 * @param type
	 * @return
	 */
	public static Rule generalizeRule(Rule r,
			List<AttrAttrConjunct> constraints, Config config,
			HashSet<Triple<String, String, String>> uncovUP,
			RuleQualityMetricType type, int currentLevel) {
		// System.out.println("Current Level: " + currentLevel);
		if (currentLevel >= 7) {
			return r;
		}
		// bestRule is the generalization of r with the best quality
		Rule bestRule = r;
		RuleQualityValue bestQuality = computeRuleQuality(bestRule, uncovUP,
				config, type);
		// cc contains formulas from constraints that lead to valid
		// generalizations of r
		ArrayList<AttrAttrConjunct> cc = new ArrayList<AttrAttrConjunct>();
		// gen[i] is a generalization of r using formula cc[i]
		ArrayList<Rule> gen = new ArrayList<Rule>();

		for (int i = 0; i < constraints.size(); i++) {
			Rule r1 = new Rule(r);
			String uattr = constraints.get(i).getLHS();
			String pattr = constraints.get(i).getRHS();
			// try to generalize r by adding constraints[i] and eliminating both
			// relevant conjuncts
			for (int j = 0; j < r1.getUAE().size(); j++) {
				if (uattr.equals(r1.getUAE().get(j).getLHS())) {
					r1.getUAE().remove(j);
					r1.setUaeChanged(true);
					break;
				}
			}
			for (int j = 0; j < r1.getRAE().size(); j++) {
				if (pattr.equals(r1.getRAE().get(j).getLHS())) {
					r1.getRAE().remove(j);
					r1.setRaeChanged(true);
					break;
				}
			}
			r1.getCon().add(constraints.get(i));
			boolean isValid = false;
			if (Double.compare(underAssignFrac, 0) == 0) {
				isValid = isValidRule(r1, config);
			} else {
				isValid = isValidRuleWithUnderAssignment(r1, config,
						underAssignFrac);
			}

			if (isValid) {
				cc.add(constraints.get(i));
				gen.add(r1);
				r1.setChanged(true);
			} else {
				// try to generalize r by adding constraints[i] and eliminating
				// one relevant conjunct
				Rule r2 = new Rule(r);
				pattr = constraints.get(i).getRHS();
				for (int j = 0; j < r2.getRAE().size(); j++) {
					if (pattr.equals(r2.getRAE().get(j).getLHS())) {
						r2.getRAE().remove(j);
						r2.setRaeChanged(true);
						break;
					}
				}
				r2.getCon().add(constraints.get(i));
				r2.setChanged(true);
				if (Double.compare(underAssignFrac, 0) == 0) {
					isValid = isValidRule(r1, config);
				} else {
					isValid = isValidRuleWithUnderAssignment(r1, config,
							underAssignFrac);
				}

				if (isValid) {
					cc.add(constraints.get(i));
					gen.add(r2);
					r2.setChanged(true);
				} else {
					Rule r3 = new Rule(r);
					uattr = constraints.get(i).getLHS();
					for (int j = 0; j < r3.getUAE().size(); j++) {
						if (uattr.equals(r3.getUAE().get(j).getLHS())) {
							r3.getUAE().remove(j);
							r3.setUaeChanged(true);
							break;
						}
					}
					r3.getCon().add(constraints.get(i));
					r3.setChanged(true);
					if (Double.compare(underAssignFrac, 0) == 0) {
						isValid = isValidRule(r1, config);
					} else {
						isValid = isValidRuleWithUnderAssignment(r1, config,
								underAssignFrac);
					}

					if (isValid) {
						cc.add(constraints.get(i));
						gen.add(r3);
						r3.setChanged(true);
					}
				}
			}
		}

		for (int i = 0; i < cc.size(); i++) {
			// try to further generalize gen[i]
			Rule tempR = generalizeRule(gen.get(i),
					cc.subList(i + 1, cc.size()), config, uncovUP, type,
					currentLevel + 1);
			if (tempR != null) {
				RuleQualityValue quality = computeRuleQuality(tempR, uncovUP,
						config, type);
				if (quality.compareTo(bestQuality) > 0) {
					bestRule = tempR;
					bestQuality = quality;
				}
			}
		}
		return bestRule;
	}

	public static boolean elimConjuncts(Rule r, LinkedList<Rule> rules,
			Config config, HashSet<Triple<String, String, String>> uncovUP,
			int index, RuleQualityMetricType type) {
		boolean changed = false;
		ArrayList<Pair<AttributeType, String>> A = new ArrayList<Pair<AttributeType, String>>();
		ArrayList<Pair<AttributeType, String>> B = new ArrayList<Pair<AttributeType, String>>();
		for (String uattr : r.getUserAttrs()) {
			A.add(new Pair<AttributeType, String>(AttributeType.UserAttr, uattr));
		}
		for (String rattr : r.getPermAttrs()) {
			B.add(new Pair<AttributeType, String>(AttributeType.ResourceAttr,
					rattr));
			// A.add(new Pair<AttributeType, String>(AttributeType.ResourceAttr,
			// rattr));
		}

		Rule minRule = new Rule(r);

		if (newElimConjunct) {
			if (r.getMaxUAESize() >= r.getMaxRAESize()) {
				minRule = elimConjunctsHelper(minRule, A, config, uncovUP, type);
				minRule = elimConjunctsHelper(minRule, B, config, uncovUP, type);
			} else {
				minRule = elimConjunctsHelper(minRule, B, config, uncovUP, type);
				minRule = elimConjunctsHelper(minRule, A, config, uncovUP, type);
			}
		} else {
			minRule = elimConjunctsHelper(minRule, A, config, uncovUP, type);
			minRule = elimConjunctsHelper(minRule, B, config, uncovUP, type);
			// minRule = elimConjunctsHelper(minRule, A, config, uncovUP, type);
		}

		if (!r.equals(minRule)) {
			rules.set(index, minRule);
			numElimConjuncts += r.getUAE().size() + r.getRAE().size()
					- minRule.getUAE().size() - minRule.getRAE().size();

			changed = true;
		}

		return changed;
	}

	public static Rule elimConjunctsHelper(Rule r,
			List<Pair<AttributeType, String>> A, Config config,
			HashSet<Triple<String, String, String>> uncovUP,
			RuleQualityMetricType qType) {
		if (A.isEmpty()) {
			return r;
		}
		Rule bestRule = r;
		RuleQualityValue bestQuality = computeRuleQuality(bestRule, uncovUP,
				config, qType);

		ArrayList<Pair<AttributeType, String>> validPairs = new ArrayList<Pair<AttributeType, String>>(
				A);

		ArrayList<Rule> potentialRules = new ArrayList<Rule>();
		for (int i = 0; i < validPairs.size(); i++) {
			Rule temp = new Rule(r);
			AttributeType type = validPairs.get(i).getFirst();
			String attr = validPairs.get(i).getSecond();
			boolean isValid = false;
			switch (type) {
			case UserAttr:
				for (int j = 0; j < temp.getUAE().size(); j++) {
					if (attr.equals(temp.getUAE().get(j).getLHS())) {
						temp.getUAE().remove(j);
						temp.setUaeChanged(true);
						break;
					}
				}

				if (Double.compare(underAssignFrac, 0) == 0) {
					isValid = isValidRule(temp, config);
				} else {
					isValid = isValidRuleWithUnderAssignment(temp, config,
							underAssignFrac);
				}

				if (!isValid) {
					// if (!isValidRule(temp, config)) {
					validPairs.remove(i);
					i--;
				} else if (config.getUserAttrSet().get(attr).isUnremovable()) {
					validPairs.remove(i);
					i--;
				} else {
					potentialRules.add(temp);
				}
				break;
			case ResourceAttr:
				for (int j = 0; j < temp.getRAE().size(); j++) {
					if (attr.equals(temp.getRAE().get(j).getLHS())) {
						temp.getRAE().remove(j);
						temp.setRaeChanged(true);
						break;
					}
				}
				// boolean isValid = false;
				if (Double.compare(underAssignFrac, 0) == 0) {
					isValid = isValidRule(temp, config);
				} else {
					isValid = isValidRuleWithUnderAssignment(temp, config,
							underAssignFrac);
				}

				if (!isValid) {
					// if (!isValidRule(temp, config)) {
					validPairs.remove(i);
					i--;
				} else if (config.getResourceAttrSet().get(attr)
						.isUnremovable()) {
					validPairs.remove(i);
					i--;
				} else {
					potentialRules.add(temp);
				}
				break;
			}
		}

		for (int i = 0; i < validPairs.size(); i++) {
			// Rule tempR = potentialRules.get(i);
			Rule tempR = new Rule(r);
			AttributeType type = validPairs.get(i).getFirst();
			String attr = validPairs.get(i).getSecond();
			switch (type) {
			case UserAttr:
				for (int j = 0; j < tempR.getUAE().size(); j++) {
					if (attr.equals(tempR.getUAE().get(j).getLHS())) {
						tempR.getUAE().remove(j);
						tempR.setUaeChanged(true);
						break;
					}
				}
				break;
			case ResourceAttr:
				for (int j = 0; j < tempR.getRAE().size(); j++) {
					if (attr.equals(tempR.getRAE().get(j).getLHS())) {
						tempR.getRAE().remove(j);
						tempR.setRaeChanged(true);
						break;
					}
				}
				break;
			}
			Rule minTemp = elimConjunctsHelper(tempR,
					validPairs.subList(i + 1, validPairs.size()), config,
					uncovUP, qType);
			if (minTemp != null) {
				RuleQualityValue quality = computeRuleQuality(minTemp, uncovUP,
						config, qType);
				if (quality.compareTo(bestQuality) > 0) {
					bestRule = minTemp;
					bestQuality = quality;
				}
			}
		}
		return bestRule;
	}

	public static RuleQualityValue computeRuleQuality(Rule r,
			HashSet<Triple<String, String, String>> uncovUP, Config config,
			RuleQualityMetricType type) {
		RuleQualityValue result = new RuleQualityValue();

		if (r.getCoveredUPTriple() == null || r.isChanged() || r.isRaeChanged()
				|| r.isUaeChanged()) {
			r.setCoveredUPTriple(computeCoveredUPTriple(r, config));
			r.setChanged(false);
		}
		HashSet<Triple<String, String, String>> coveredUP = new HashSet<Triple<String, String, String>>(
				r.getCoveredUPTriple());
		coveredUP.retainAll(uncovUP);

		switch (type) {
		case QRUL:
			result.firstComponent = (double) coveredUP.size() / r.getSize();
			result.secondComponent = r.getCon().size();
			break;
		case QRULANDB:
			result.firstComponent = (double) coveredUP.size() / r.getSize();
			Integer qrulB = config.constraintsToNumRules
					.get(new HashSet<AttrAttrConjunct>(r.getCon()));
			if (qrulB == null) {
				qrulB = 0;
			}
			result.secondComponent = qrulB;
			result.thirdComponent = r.getCon().size();
			break;
		case QRULANDD:
			result.firstComponent = (double) coveredUP.size() / r.getSize();
			Integer qrulD = config.constraintsToNumUP
					.get(new HashSet<AttrAttrConjunct>(r.getCon()));
			if (qrulD == null) {
				qrulD = 0;
			}
			result.secondComponent = qrulD;
			break;
		case BANDQRUL:
			qrulB = config.constraintsToNumRules
					.get(new HashSet<AttrAttrConjunct>(r.getCon()));
			if (qrulB == null) {
				qrulB = 0;
			}
			result.firstComponent = qrulB;
			result.secondComponent = (double) coveredUP.size() / r.getSize();
			break;
		case DANDQRUL:
			qrulD = config.constraintsToNumUP
					.get(new HashSet<AttrAttrConjunct>(r.getCon()));
			if (qrulD == null) {
				qrulD = 0;
			}
			if (r.getCon().isEmpty()) {
				qrulD = 0;
			} else {
				for (AttrAttrConjunct c : r.getCon()) {
					qrulD += config.constraintToNumUP.get(c);
				}
				qrulD /= r.getCon().size();
			}
			result.firstComponent = qrulD;
			result.secondComponent = (double) coveredUP.size() / r.getSize();
			break;
		case QMEAN:
			result.firstComponent = (double) coveredUP.size();
			result.secondComponent = 0.0;
			break;
		default:
			break;
		}

		return result;
	}

	public static HashMap<Pair<String, String>, HashSet<String>> computeCoveredUP(
			Rule r, Config config) {
		HashMap<Pair<String, String>, HashSet<String>> coveredUP = new HashMap<Pair<String, String>, HashSet<String>>();
		HashSet<String> satUsers = new HashSet<String>();
		HashSet<String> satPerms = new HashSet<String>();
		try {
			for (String user : config.getUsers()) {
				if (Parser.satisfyingRule(user, r, config, true)) {
					satUsers.add(user);
				}
			}
			for (String permission : config.getResources()) {
				if (Parser.satisfyingRule(permission, r, config, false)) {
					satPerms.add(permission);
				}
			}
			for (String user : satUsers) {
				for (String permission : satPerms) {
					if (r.getCon().isEmpty()
							|| Parser.satisfyingRuleConstraints(user,
									permission, r, config)) {

						for (String op : r.getOps()) {
							Pair<String, String> perm = new Pair<String, String>(
									op, permission);
							if (!coveredUP.containsKey(perm)) {
								coveredUP.put(perm, new HashSet<String>());
							}
							coveredUP.get(perm).add(user);
						}

					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coveredUP;
	}

	/**
	 * compute covered UP set for a rule
	 * 
	 * @param r
	 *            : rule
	 * @param config
	 *            : config
	 * @return covered UP set
	 */
	public static HashSet<Triple<String, String, String>> computeCoveredUPTriple(
			Rule r, Config config) {
		HashSet<Triple<String, String, String>> coveredUP = new HashSet<Triple<String, String, String>>();
		HashSet<String> satUsers = new HashSet<String>();
		HashSet<String> satResources = new HashSet<String>();

		HashSet<String> oldSatUsers = new HashSet<String>();
		HashSet<String> oldSatResources = new HashSet<String>();

		if (r.isUaeChanged() == false && r.getSatUsers() != null
				&& !r.getSatUsers().isEmpty()) {
			satUsers = r.getSatUsers();
		} else {
			if (r.getSatUsers() != null) {
				oldSatUsers = r.getSatUsers();
			}
			for (String user : config.getUsers()) {
				if (Parser.satisfyingRule(user, r, config, true)) {
					satUsers.add(user);
				}
			}
			r.setSatUsers(satUsers);
			r.setUaeChanged(false);
		}
		if (r.isRaeChanged() == false && r.getSatResources() != null
				&& !r.getSatResources().isEmpty()) {
			satResources = r.getSatResources();
		} else {
			if (r.getSatResources() != null) {
				oldSatResources = r.getSatUsers();
			}
			for (String permission : config.getResources()) {
				if (Parser.satisfyingRule(permission, r, config, false)) {
					satResources.add(permission);
				}
			}
			r.setSatResources(satResources);
			r.setRaeChanged(false);
		}

		if (r.getCoveredUPTriple() != null && !r.getCoveredUPTriple().isEmpty()
				&& oldSatUsers.equals(r.getSatUsers())
				&& oldSatResources.equals(r.getSatResources())) {
			return r.getCoveredUPTriple();
		}

		for (String user : satUsers) {
			for (String permission : satResources) {
				boolean satisfied = false;
				if (r.getCon().isEmpty()) {
					satisfied = true;
				} else {
					satisfied = Parser.satisfyingRuleConstraints(user,
							permission, r, config);
				}
				if (satisfied) {
					for (String op : r.getOps()) {
						Triple<String, String, String> up = new Triple<String, String, String>(
								user, op, permission);
						coveredUP.add(up);
					}
				}
			}
		}
		return coveredUP;
	}

	public static boolean isValidRule(Rule r, Config config) {
		if (r.getCoveredUPTriple().isEmpty() || r.isChanged()
				|| r.isUaeChanged() || r.isRaeChanged()) {
			r.setCoveredUPTriple(computeCoveredUPTriple(r, config));
			r.setChanged(false);
		}
		boolean isValid = config.getCoveredUP().containsAll(
				r.getCoveredUPTriple());

		if (isValid) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isValidRuleWithUnderAssignment(Rule r, Config config,
			double underAssignFrac) {
		if (r.getCoveredUPTriple().isEmpty() || r.isChanged()
				|| r.isUaeChanged() || r.isRaeChanged()) {
			r.setCoveredUPTriple(computeCoveredUPTriple(r, config));
			r.setChanged(false);
		}

		if (Double.compare(underAssignFrac, 0) == 0) {
			boolean isValid = config.getCoveredUP().containsAll(
					r.getCoveredUPTriple());

			if (isValid) {
				return true;
			} else {
				return false;
			}
		} else {
			int numUnderAssign = (int) (r.getCoveredUPTriple().size() * underAssignFrac);
			int underAssign = 0;
			for (Triple<String, String, String> element : r
					.getCoveredUPTriple()) {
				if (!config.getCoveredUP().contains(element)) {
					underAssign++;
					if (underAssign > numUnderAssign) {
						return false;
					}
				}
			}
			return true;
		}
	}

	public static boolean isValidMergedRule(Rule r, Rule r1, Rule r2,
			Config config) {
		HashSet<Triple<String, String, String>> coveredUP = new HashSet<Triple<String, String, String>>();
		coveredUP.addAll(new HashSet<Triple<String, String, String>>(r1
				.getCoveredUPTriple()));
		coveredUP.addAll(new HashSet<Triple<String, String, String>>(r2
				.getCoveredUPTriple()));

		for (String op : r2.getOps()) {
			if (!r1.getOps().contains(op)) {
				for (Triple<String, String, String> up : r1
						.getCoveredUPTriple()) {
					Triple<String, String, String> up1 = new Triple<String, String, String>(
							up.getFirst(), op, up.getThird());
					if (!config.getCoveredUP().contains(up1)) {
						return false;
					} else {
						coveredUP.add(up1);
					}
				}
			}
		}

		for (String op : r1.getOps()) {
			if (!r2.getOps().contains(op)) {
				for (Triple<String, String, String> up : r2
						.getCoveredUPTriple()) {
					Triple<String, String, String> up1 = new Triple<String, String, String>(
							up.getFirst(), op, up.getThird());
					if (!config.getCoveredUP().contains(up1)) {
						return false;
					} else {
						coveredUP.add(up1);
					}
				}
			}
		}

		for (String user : r1.getSatUsers()) {
			for (String permission : r2.getSatResources()) {
				boolean satisfied = false;
				if (r.getCon().isEmpty())
					satisfied = true;
				else
					satisfied = Parser.satisfyingRuleConstraints(user,
							permission, r, config);
				if (satisfied) {
					for (String op : r.getOps()) {
						Triple<String, String, String> up = new Triple<String, String, String>(
								user, op, permission);
						if (!config.getCoveredUP().contains(up)) {
							return false;
						} else {
							coveredUP.add(up);
						}
					}
				}
			}
		}

		for (String user : r2.getSatUsers()) {
			for (String permission : r1.getSatResources()) {
				boolean satisfied = false;
				if (r.getCon().isEmpty())
					satisfied = true;
				else
					satisfied = Parser.satisfyingRuleConstraints(user,
							permission, r, config);
				if (satisfied) {
					for (String op : r.getOps()) {
						Triple<String, String, String> up = new Triple<String, String, String>(
								user, op, permission);
						if (!config.getCoveredUP().contains(up)) {
							return false;
						} else {
							coveredUP.add(up);
						}
					}
				}
			}
		}

		HashSet<String> satUsers = new HashSet<String>();
		satUsers.addAll(new HashSet<String>(r1.getSatUsers()));
		satUsers.addAll(new HashSet<String>(r2.getSatUsers()));
		HashSet<String> satResources = new HashSet<String>();
		satResources.addAll(new HashSet<String>(r1.getSatResources()));
		satResources.addAll(new HashSet<String>(r2.getSatResources()));

		if (r.isUaeChanged() == false && r.getSatUsers() != null
				&& !r.getSatUsers().isEmpty()) {
			satUsers = r.getSatUsers();
		} else {
			for (String user : config.getUsers()) {
				if (!satUsers.contains(user)
						&& Parser.satisfyingRule(user, r, config, true)) {
					satUsers.add(user);
				}
			}
			r.setSatUsers(satUsers);
			r.setUaeChanged(false);
		}
		if (r.isRaeChanged() == false && r.getSatResources() != null
				&& !r.getSatResources().isEmpty()) {
			satResources = r.getSatResources();
		} else {
			for (String permission : config.getResources()) {
				if (!satResources.contains(permission)
						&& Parser.satisfyingRule(permission, r, config, false)) {
					satResources.add(permission);
				}
				r.setSatResources(satResources);
				r.setRaeChanged(false);
			}
		}
		for (String user : satUsers) {
			for (String permission : satResources) {
				boolean satisfied = false;
				if (r.getCon().isEmpty())
					satisfied = true;
				else
					satisfied = Parser.satisfyingRuleConstraints(user,
							permission, r, config);
				if (satisfied) {
					for (String op : r.getOps()) {
						Triple<String, String, String> up = new Triple<String, String, String>(
								user, op, permission);
						if (!config.getCoveredUP().contains(up)) {
							return false;
						}
						coveredUP.add(up);
					}
				}
			}
		}

		r.setCoveredUPTriple(coveredUP);
		return true;
	}

	public static void computeDuration() {
		endTime = System.currentTimeMillis();
		duration = endTime - startTime;
		startTime = endTime;
		totalTime += duration;
	}

	public static void computeAttributeRelevance(Config config) {
		double[] maxRelativeMutualInfo = new double[config.getUserAttrSet()
				.keySet().size()];
		ArrayList<String> userAttrs = new ArrayList<String>(config
				.getUserAttrSet().keySet());
		HashMap<Pair<String, String>, HashSet<String>> userAttributeToUsers = new HashMap<Pair<String, String>, HashSet<String>>();
		for (String userAttr : userAttrs) {
			Attribute uAttr = config.getUserAttrSet().get(userAttr);
			if (uAttr.getvType() == ValueType.Single) {
				for (String uAttrValue : uAttr.getDomain()) {
					Pair<String, String> userAttribute = new Pair<String, String>(
							userAttr, uAttrValue);
					userAttributeToUsers.put(userAttribute,
							new HashSet<String>());
					for (String user : config.getUsers()) {
						if (config.getUserAttrInfo().get(user)
								.containsKey(userAttr)) {
							if (config.getUserAttrInfo().get(user)
									.get(userAttr).contains(uAttrValue)) {
								userAttributeToUsers.get(userAttribute).add(
										user);
							}
						}
					}
				}
			}
		}

		for (Pair<String, String> perm : config.getPermUsers().keySet()) {
			HashSet<String> userSet = config.getPermUsers().get(perm);
			double p = (double) userSet.size() / config.getUsers().size();
			double binaryEntropy = -(p * (Math.log(p) / Math.log(2)) + (1 - p)
					* (Math.log(1 - p) / Math.log(2)));
			for (int i = 0; i < userAttrs.size(); i++) {
				String userAttr = userAttrs.get(i);
				Attribute uAttr = config.getUserAttrSet().get(userAttr);
				double conditionalEntropy = 0.0;
				if (uAttr.getvType() == ValueType.Single) {
					for (String uAttrValue : uAttr.getDomain()) {
						Pair<String, String> userAttribute = new Pair<String, String>(
								userAttr, uAttrValue);
						HashSet<String> userWithPerm = new HashSet<String>(
								config.getPermUsers().get(perm));

						userWithPerm.retainAll(userAttributeToUsers
								.get(userAttribute));
						if (!userWithPerm.isEmpty()) {
							double cp = (double) userWithPerm.size()
									/ userAttributeToUsers.get(userAttribute)
											.size();
							if (Double.compare(cp, 0.0) != 0
									&& Double.compare(cp, 1.0) != 0) {
								conditionalEntropy += -((double) userAttributeToUsers
										.get(userAttribute).size() / config
										.getUsers().size())
										* (cp * (Math.log(cp) / Math.log(2)) + (1 - cp)
												* (Math.log(1 - cp) / Math
														.log(2)));
							}
						}
					}
					if (Double.compare(conditionalEntropy, 0.0) != 0) {
						double mutualInfo = 1 - (conditionalEntropy / binaryEntropy);
						if (mutualInfo > maxRelativeMutualInfo[i]) {
							maxRelativeMutualInfo[i] = mutualInfo;
						}
					}
				}
			}
		}
		System.out.println(userAttrs);
		System.out.println(Arrays.toString(maxRelativeMutualInfo));
	}

	public static void main(String[] args) {
//		if (args.length != 5) {
//			System.err.println("Number of arguments are not correct.");
//			System.exit(1);
//		}

		String inputFile = args[0];

		Parser.parseInputFile(inputFile);
		startTime = System.currentTimeMillis();

		// computeAttributeRelevance(Parser.config);
		// for (double noiseRatio = 0.07; noiseRatio <= 1.00; noiseRatio +=
		// 0.01) {
		Parser.config = new Config();
		Parser.parseInputFile(inputFile);
		// addUnderassignmentNoise(Parser.config, noiseRatio);

		// underAssignFrac = 3 * noiseRatio;
		// System.out.println(noiseRatio);
		// overAssignThres = 12;
		// addOverassignmentNoise(Parser.config, noiseRatio);

		mineABACPolicy(Parser.config, true, 0, false, true, 0, 0.0, true);

		double totalInputWSC = 0;
		for (int i = 0; i < Parser.config.getRuleList().size(); i++) {
			totalInputWSC += Parser.config.getRuleList().get(i).getSize();
		}

		double totalOutputWSC = 0;
		for (int i = 0; i < ABACMiner.resultRules.size(); i++) {
			totalOutputWSC += ABACMiner.resultRules.get(i).getSize();
		}

		double aclSize = 0;
		aclSize += Parser.config.getCoveredUP().size();
		for (String user : Parser.config.getUserAttrInfo().keySet()) {
			for (String attr : Parser.config.getUserAttrInfo().get(user)
					.keySet()) {
				aclSize += Parser.config.getUserAttrInfo().get(user).get(attr)
						.size();
			}
		}
		for (String resource : Parser.config.getResourceAttrInfo().keySet()) {
			for (String attr : Parser.config.getResourceAttrInfo()
					.get(resource).keySet()) {
				aclSize += Parser.config.getResourceAttrInfo().get(resource)
						.get(attr).size();
			}
		}

		computeDuration();

		System.out.println(Parser.config.getRuleList().size() + " "
				+ ABACMiner.resultRules.size() + " " + totalInputWSC + " "
				+ aclSize + " " + Parser.config.getCoveredUP().size() + " "
				+ totalOutputWSC + " " + totalTime);
	}
	// }
}
