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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Vector;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.dar.util.Config;
import edu.dar.util.DiscreteNormalDistribution;
import edu.dar.util.Pair;
import edu.dar.util.Parser;
import edu.dar.util.RandomUtil;
import edu.dar.util.Rule;
import edu.dar.util.SyntheticPolicyCaseStudyGenerator;
import edu.dar.util.Triple;
import edu.dar.util.ValueType;

public class NoiseExperiment {
	public static final int NUM_CASES = 10;
	public static final int MAXIMUM_THRES = 16;
	public static final int NRULE = 20;

	public static final double[] noiseRatios = new double[] { 
																	 
																	  0.03,
																	  0.06,
																	  0.09,
																	  0.12,
																	  //0.15,
																	  
																	 };

	public static final double[] TAU = new double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1 , 2, 3, 4, 5, 6, 7,
														  8, 9, 10/*, 11, 12, 13,
														  14, 15 /* , 9 , 10 ,
														 * 11 , 12 , 13 , 14 ,
														 * 15
														 */};
	public static final double[] ALPHA = new double[] {0.01, 0.02, 0.03, 0.04,
			0.05, 0.06, 0.07, 0.08, 0.09/*, 0.12, 0.15, 0.18, 0.21/*, 0.3 , 0.4, 0.5/* 0.10, 0.15, 0.18, 0.21 */
	};

	public static void ROCExperiment(double unoiseRatio, double onoiseRatio) {
		StandardDeviation std = new StandardDeviation();

		double[][][] tpr_u = new double[TAU.length][ALPHA.length][NUM_CASES];
		double[][][] fpr_u = new double[TAU.length][ALPHA.length][NUM_CASES];
		double[][][] tpr_o = new double[TAU.length][ALPHA.length][NUM_CASES];
		double[][][] fpr_o = new double[TAU.length][ALPHA.length][NUM_CASES];

		// Vector<Pair<Double, Double>> tausAndAlphas = new Vector<Pair<Double,
		// Double>>(TAU.length);
		// double[] maxSimilarities = new double[NUM_CASES];

		for (int j = 0; j < NUM_CASES; j++) {
			String inputFile = "ran-case-studies/test.abac";
			SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
					null, NRULE, false, true, 0, 0, 0, 0, 0);
			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());

			int numUNoise = (int) (originalUP.size() * unoiseRatio);

			while (config.getUnderassignmentUP().size() < numUNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			int numONoise = (int) (originalUP.size() * onoiseRatio);

			while (config.getOverassignmentUP().size() < numONoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!originalUP.contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			// HashSet<Triple<String, String, String>> injectedNoise = new
			// HashSet<Triple<String, String, String>>(
			// config.getUnderassignmentUP());
			// injectedNoise.addAll(config.getOverassignmentUP());

			for (int k = 0; k < TAU.length; k++) {
				int tau = (int) TAU[k];
				System.out.println(tau);
				for (int l = 0; l < ALPHA.length; l++) {
					double alpha = ALPHA[l];
					System.out.println(alpha);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);

					HashSet<Triple<String, String, String>> tp_u = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					tp_u.retainAll(config.getUnderassignmentUP());

					HashSet<Triple<String, String, String>> tp_o = new HashSet<Triple<String, String, String>>(
							ABACMiner.overAssignments);
					tp_o.retainAll(config.getOverassignmentUP());

					tpr_u[k][l][j] = (double) tp_u.size()
							/ config.getUnderassignmentUP().size();
					tpr_o[k][l][j] = (double) tp_o.size()
							/ config.getOverassignmentUP().size();

					HashSet<Triple<String, String, String>> fp_u = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					fp_u.removeAll(config.getUnderassignmentUP());

					HashSet<Triple<String, String, String>> fp_o = new HashSet<Triple<String, String, String>>(
							ABACMiner.overAssignments);
					fp_o.removeAll(config.getOverassignmentUP());

					fpr_u[k][l][j] = (double) fp_u.size() / originalUP.size();
					fpr_o[k][l][j] = (double) fp_o.size() / originalUP.size();
				}
			}
		}
		for (int k = 0; k < TAU.length; k++) {
			int tau = (int) TAU[k];
			for (int l = 0; l < ALPHA.length; l++) {
				double alpha = ALPHA[l];
				System.out.print(tau + " " + alpha + " ");
				System.out.printf("%7.2f %7.2f ",
						doubleArrayAverage(tpr_u[k][l]),
						std.evaluate(tpr_u[k][l]));
				System.out.printf("%7.2f %7.2f ",
						doubleArrayAverage(fpr_u[k][l]),
						std.evaluate(fpr_u[k][l]));
				System.out.printf("%7.2f %7.2f ",
						doubleArrayAverage(tpr_o[k][l]),
						std.evaluate(tpr_o[k][l]));
				System.out.printf("%7.2f %7.2f ",
						doubleArrayAverage(fpr_o[k][l]),
						std.evaluate(fpr_o[k][l]));
				System.out.println();
			}
		}
	}

	public static void UPAndAttrNoiseOnCaseStudy(double unoiseRatio,
			double onoiseRatio, double anoiseRatio, String inputFile) {
		double[] taus_o = new double[NUM_CASES];
		double[] alphas_o = new double[NUM_CASES];

		double[] taus_u = new double[NUM_CASES];
		double[] alphas_u = new double[NUM_CASES];

		Vector<Pair<Double, Double>> tausAndAlphas_o = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_u = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		double[] maxSimilarities_o = new double[NUM_CASES];
		double[] maxSimilarities_u = new double[NUM_CASES];

		double[] maxPolicySimilarities_o = new double[NUM_CASES];
		double[] maxPolicySimilarities_u = new double[NUM_CASES];
		for (int j = 0; j < NUM_CASES; j++) {
			double bestTau_o = 0.0;
			double bestAlpha_o = 0.0;
			double bestTau_u = 0.0;
			double bestAlpha_u = 0.0;

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());

			int numANoise = (int) (originalUP.size() * anoiseRatio);
			int affectedUPTuples = 0;
			HashSet<Pair<String, String>> changedUserAttrPairs = new HashSet<Pair<String, String>>();
			HashSet<Pair<String, String>> changedResourceAttrPairs = new HashSet<Pair<String, String>>();
			Random rand = new Random(System.currentTimeMillis());
			while (true) {
				if (rand.nextFloat() <= 0.5) {
					// change user attribute
					String user = Experiment.randomElement(config.getUsers());
					if (rand.nextFloat() <= 0.5) {
						// replace a non-bottom value with the value of the
						// same attribute from a randomly selected user
						if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
							continue;
						} else {
							String uAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.keySet()));
							while (uAttr.equals("uid")) {
								uAttr = Experiment
										.randomElement(new HashSet<String>(
												config.getUserAttrInfo()
														.get(user).keySet()));
							}
							Pair<String, String> userAttrPair = new Pair<String, String>(
									user, uAttr);
							if (changedUserAttrPairs.contains(userAttrPair)) {
								continue;
							} else {
								changedUserAttrPairs.add(userAttrPair);
							}
							HashSet<String> uAttrValueSet = new HashSet<String>();
							if (config.getUserAttrSet().get(uAttr).getvType() == ValueType.Single) {
								if (config.getUserAttrSet().get(uAttr)
										.getDomain().size() == 1) {
									continue;
								}
								uAttrValueSet.add(Experiment
										.randomElement(config.getUserAttrSet()
												.get(uAttr).getDomain()));
							} else {
								if (config.getUserAttrSet().get(uAttr)
										.getSetDomain().size() == 1) {
									continue;
								}
								uAttrValueSet = new HashSet<String>(
										Experiment.randomElement(config
												.getUserAttrSet().get(uAttr)
												.getSetDomain()));
							}
							if (uAttrValueSet.equals(config.getUserAttrInfo()
									.get(user).get(uAttr))) {
								continue;
							}
							HashSet<String> uAttrValueSetOld = config
									.getUserAttrInfo().get(user).get(uAttr);
							config.getUserAttrInfo().get(user)
									.put(uAttr, uAttrValueSet);
							// System.out.println(changedAttrs + " Add User: "
							// + user + " uAttr: " + uAttr
							// + "uAttrValue: " + uAttrValueSet);
							affectedUPTuples = computeAffectedUPTriples(config,
									originalUP);
							if (affectedUPTuples > numANoise) {
								config.getUserAttrInfo().get(user)
										.put(uAttr, uAttrValueSetOld);
								changedUserAttrPairs.remove(userAttrPair);
								break;
							}
						}
					} else {
						// replace a non-bottom value with bottom
						if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
							continue;
						}
						String uAttr = Experiment
								.randomElement(new HashSet<String>(config
										.getUserAttrInfo().get(user).keySet()));
						while (uAttr.equals("uid")) {
							uAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.keySet()));
						}
						Pair<String, String> userAttrPair = new Pair<String, String>(
								user, uAttr);
						if (changedUserAttrPairs.contains(userAttrPair)) {
							continue;
						} else {
							changedUserAttrPairs.add(userAttrPair);
						}
						HashSet<String> uAttrValueSetOld = config
								.getUserAttrInfo().get(user).get(uAttr);
						config.getUserAttrInfo().get(user).remove(uAttr);
						// System.out.println(changedAttrs + " Remove User: "
						// + user + " uAttr: " + uAttr);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getUserAttrInfo().get(user)
									.put(uAttr, uAttrValueSetOld);
							changedUserAttrPairs.remove(userAttrPair);
							break;
						}
					}
				} else {
					// change resource attribute
					String resource = Experiment.randomElement(config
							.getResources());
					if (rand.nextFloat() <= 0.5) {
						// replace a non-bottom value with the value of the
						// same attribute from a randomly selected user
						if (config.getResourceAttrInfo().get(resource).keySet()
								.size() == 1) {
							continue;
						} else {
							String rAttr = Experiment
									.randomElement(new HashSet<String>((config
											.getResourceAttrSet().keySet())));
							while (rAttr.equals("rid")) {
								rAttr = Experiment
										.randomElement(new HashSet<String>(
												config.getResourceAttrSet()
														.keySet()));
							}
							Pair<String, String> resourceAttrPair = new Pair<String, String>(
									resource, rAttr);
							if (changedResourceAttrPairs
									.contains(resourceAttrPair)) {
								continue;
							} else {
								changedResourceAttrPairs.add(resourceAttrPair);
							}
							HashSet<String> rAttrValueSet = new HashSet<String>();
							if (config.getResourceAttrSet().get(rAttr)
									.getvType() == ValueType.Single) {
								if (config.getResourceAttrSet().get(rAttr)
										.getDomain().size() == 1) {
									continue;
								}
								rAttrValueSet.add(Experiment
										.randomElement(config
												.getResourceAttrSet()
												.get(rAttr).getDomain()));
							} else {
								if (config.getResourceAttrSet().get(rAttr)
										.getSetDomain().size() == 1) {
									continue;
								}
								rAttrValueSet = new HashSet<String>(
										Experiment.randomElement(config
												.getResourceAttrSet()
												.get(rAttr).getSetDomain()));
							}
							if (rAttrValueSet.equals(config
									.getResourceAttrInfo().get(resource)
									.get(rAttr))) {
								continue;
							}
							HashSet<String> rAttrValueSetOld = config
									.getResourceAttrInfo().get(resource)
									.get(rAttr);
							config.getResourceAttrInfo().get(resource)
									.put(rAttr, rAttrValueSet);
							// System.out.println(changedAttrs +
							// " Add Resource: "
							// + resource + " rAttr: " + rAttr
							// + "rAttrValue: " + rAttrValueSet);
							affectedUPTuples = computeAffectedUPTriples(config,
									originalUP);
							if (affectedUPTuples > numANoise) {
								config.getResourceAttrInfo().get(resource)
										.put(rAttr, rAttrValueSetOld);
								changedResourceAttrPairs
										.remove(resourceAttrPair);
								break;
							}
						}
					} else {
						if (config.getResourceAttrInfo().get(resource).keySet()
								.size() == 1) {
							continue;
						}
						String rAttr = Experiment
								.randomElement(new HashSet<String>(config
										.getResourceAttrInfo().get(resource)
										.keySet()));
						while (rAttr.equals("rid")) {
							rAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getResourceAttrInfo()
											.get(resource).keySet()));
						}
						Pair<String, String> resourceAttrPair = new Pair<String, String>(
								resource, rAttr);
						if (changedResourceAttrPairs.contains(resourceAttrPair)) {
							continue;
						} else {
							changedResourceAttrPairs.add(resourceAttrPair);
						}
						HashSet<String> rAttrValueSetOld = config
								.getResourceAttrInfo().get(resource).get(rAttr);
						config.getResourceAttrInfo().get(resource)
								.remove(rAttr);
						// System.out.println(changedAttrs +
						// " Remove Resource: "
						// + resource + " rAttr: " + rAttr);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getResourceAttrInfo().get(resource)
									.put(rAttr, rAttrValueSetOld);
							changedResourceAttrPairs.remove(resourceAttrPair);
							break;
						}
					}
				}
			}

			// Construct the UP relation entailed by the new attribtue data
			// Construct UP relations from rules
			HashSet<Triple<String, String, String>> newCoveredUP = new HashSet<Triple<String, String, String>>();

			for (Rule r : config.getRuleList()) {
				HashSet<String> satUsers = new HashSet<String>();
				HashSet<String> satPerms = new HashSet<String>();
				for (String u : config.getUsers()) {
					if (Parser.satisfyingRule(u, r, config, true)) {
						satUsers.add(u);
					}
				}
				for (String p : config.getResources()) {
					if (Parser.satisfyingRule(p, r, config, false)) {
						satPerms.add(p);
					}
				}
				for (String u : satUsers) {
					for (String p : satPerms) {
						if (Parser.satisfyingRuleConstraints(u, p, r, config)) {
							for (String op : r.getOps()) {
								config.getUserPerms().get(u)
										.add(new Pair<String, String>(op, p));
								Pair<String, String> perm = new Pair<String, String>(
										op, p);
								if (!config.getPermUsers().containsKey(perm)) {
									config.getPermUsers().put(perm,
											new HashSet<String>());
								}
								config.getPermUsers().get(perm).add(u);
								newCoveredUP
										.add(new Triple<String, String, String>(
												u, op, p));
							}
						}
					}
				}
			}

			HashSet<Triple<String, String, String>> attrOverAssignment = new HashSet<Triple<String, String, String>>(
					originalUP);
			attrOverAssignment.removeAll(newCoveredUP);

			HashSet<Triple<String, String, String>> attrUnderAssignment = new HashSet<Triple<String, String, String>>(
					newCoveredUP);
			attrUnderAssignment.removeAll(originalUP);

			int numUNoise = (int) (originalUP.size() * unoiseRatio);

			int numONoise = (int) (originalUP.size() * onoiseRatio);
			affectedUPTuples = computeAffectedUPTriples(config, originalUP);
			numONoise += numANoise - affectedUPTuples;

			while (config.getUnderassignmentUP().size() < numUNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				// updating UserPerms and PermUsers
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			while (config.getOverassignmentUP().size() < numONoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!originalUP.contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			HashSet<Triple<String, String, String>> actOverAssignment = new HashSet<Triple<String, String, String>>(
					Parser.config.getOverassignmentUP());
			actOverAssignment.addAll(attrOverAssignment);

			HashSet<Triple<String, String, String>> actUnderAssignment = new HashSet<Triple<String, String, String>>(
					Parser.config.getUnderassignmentUP());
			actUnderAssignment.addAll(attrUnderAssignment);

			HashSet<Triple<String, String, String>> injectedNoise = new HashSet<Triple<String, String, String>>(
					config.getUnderassignmentUP());
			injectedNoise.addAll(config.getOverassignmentUP());

			double maximumSimilarity_o = -1;
			double maximumSimilarity_u = -1;
			double maximumPolicySimilarity_o = -1;
			double maximumPolicySimilarity_u = -1;
			for (double tau : TAU) {
				// System.out.println(tau);
				for (double alpha : ALPHA) {
					// System.out.println(alpha);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);
					double currentSimilarity_o = ABACMiner.jaccardSimilarity(
							actOverAssignment, ABACMiner.overAssignments);
					if (Double
							.compare(currentSimilarity_o, maximumSimilarity_o) > 0) {
						bestTau_o = tau;
						bestAlpha_o = alpha;
						maximumSimilarity_o = currentSimilarity_o;
						maximumPolicySimilarity_o = ABACMiner
								.nonsymmetricSyntacticSimilarityPolicies(
										ABACMiner.resultRules,
										config.getRuleList(), config);
					}
					double currentSimilarity_u = ABACMiner.jaccardSimilarity(
							actUnderAssignment, ABACMiner.underAssignments);
					if (Double
							.compare(currentSimilarity_u, maximumSimilarity_u) > 0) {
						bestTau_u = tau;
						bestAlpha_u = alpha;
						maximumSimilarity_u = currentSimilarity_u;
						maximumPolicySimilarity_u = ABACMiner
								.nonsymmetricSyntacticSimilarityPolicies(
										ABACMiner.resultRules,
										config.getRuleList(), config);
					}
					if (Double.compare(maximumSimilarity_o, 1.0) == 0
							&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
						break;
					}
				}
				if (Double.compare(maximumSimilarity_o, 1.0) == 0
						&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
					break;
				}
			}
			tausAndAlphas_o.add(j, new Pair<Double, Double>(bestTau_o,
					bestAlpha_o));
			maxSimilarities_o[j] = maximumSimilarity_o;
			maxPolicySimilarities_o[j] = maximumPolicySimilarity_o;
			tausAndAlphas_u.add(j, new Pair<Double, Double>(bestTau_u,
					bestAlpha_u));
			maxSimilarities_u[j] = maximumSimilarity_u;
			maxPolicySimilarities_u[j] = maximumPolicySimilarity_u;
		}
		for (int i = 0; i < NUM_CASES; i++) {
			taus_o[i] = tausAndAlphas_o.get(i).getFirst();
			alphas_o[i] = tausAndAlphas_o.get(i).getSecond();
			taus_u[i] = tausAndAlphas_u.get(i).getFirst();
			alphas_u[i] = tausAndAlphas_u.get(i).getSecond();
		}
		System.out.printf("Best tau for Over-assignments: %7.2f\n",
				doubleArrayAverage(taus_o));
		System.out.printf("Best alpha for Over-assignments: %7.2f\n",
				doubleArrayAverage(alphas_o));
		System.out
				.printf("Maximum Similarity between Injected Over-assignments and Detected Over-assignments: %7.2f \n",
						doubleArrayAverage(maxSimilarities_o));
		// System.out.printf("%7.2f ",
		// doubleArrayAverage(maxPolicySimilarities_o));
		System.out.printf("Best tau for Under-assignments: %7.2f \n",
				doubleArrayAverage(taus_u));
		System.out.printf("Best alpha for Under-assignments:%7.2f \n",
				doubleArrayAverage(alphas_u));
		System.out
				.printf("Maximum Similarity between Injected Under-assignments and Detected Under-assignments: %7.2f \n",
						doubleArrayAverage(maxSimilarities_u));
		// System.out.printf("%7.2f ",
		// doubleArrayAverage(maxPolicySimilarities_u));
		System.out.println();
	}

	/**
	 * 
	 * @param unoiseRatio
	 *            : underAssignment ratio
	 * @param onoiseRatio
	 *            : overAssignment ratio
	 * @param anoiseRatio
	 *            : attribute noise ratio
	 */
	public static void UPAndAttrNoise(double unoiseRatio, double onoiseRatio,
			double anoiseRatio) {
		StandardDeviation std = new StandardDeviation();
		double[] taus_o = new double[NUM_CASES];
		double[] alphas_o = new double[NUM_CASES];

		double[] taus_u = new double[NUM_CASES];
		double[] alphas_u = new double[NUM_CASES];

		Vector<Pair<Double, Double>> tausAndAlphas_o = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_u = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		double[] maxSimilarities_o = new double[NUM_CASES];
		double[] maxSimilarities_u = new double[NUM_CASES];

		double[] maxPolicySimilarities_o = new double[NUM_CASES];
		double[] maxPolicySimilarities_u = new double[NUM_CASES];

		for (int j = 0; j < NUM_CASES; j++) {
			System.out.println(j);
			double bestTau_o = 0.0;
			double bestAlpha_o = 0.0;
			double bestTau_u = 0.0;
			double bestAlpha_u = 0.0;
			String inputFile = "ran-case-studies/synthetic_" + NRULE + "_"
					+ unoiseRatio + "_" + onoiseRatio + "_" + anoiseRatio + "_"
					+ j + ".abac";
			;
			SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
					null, NRULE, false, true, 0, 0, 0, 0, 0);

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());
			// System.out.println("Original UP:\n" + originalUP);

			int numANoise = (int) (originalUP.size() * anoiseRatio);
			int affectedUPTuples = 0;
			HashSet<Pair<String, String>> changedUserAttrPairs = new HashSet<Pair<String, String>>();
			HashSet<Pair<String, String>> changedResourceAttrPairs = new HashSet<Pair<String, String>>();
			Random rand = new Random(System.currentTimeMillis());
			// System.out.println("Number of attributes to change:" +
			// numANoise);
			while (true) {
				if (rand.nextFloat() <= 0.5) {
					// change user attribute
					String user = Experiment.randomElement(config.getUsers());
					if (rand.nextFloat() <= 0.5) {
						// replace a non-bottom value with the value of the
						// same attribute from a randomly selected user
						if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
							continue;
						} else {
							String uAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.keySet()));
							while (uAttr.equals("uid")) {
								uAttr = Experiment
										.randomElement(new HashSet<String>(
												config.getUserAttrInfo()
														.get(user).keySet()));
							}
							Pair<String, String> userAttrPair = new Pair<String, String>(
									user, uAttr);
							if (changedUserAttrPairs.contains(userAttrPair)) {
								continue;
							} else {
								changedUserAttrPairs.add(userAttrPair);
							}
							HashSet<String> uAttrValueSet = new HashSet<String>();
							if (config.getUserAttrSet().get(uAttr).getvType() == ValueType.Single) {
								if (config.getUserAttrSet().get(uAttr)
										.getDomain().size() == 1) {
									continue;
								}
								uAttrValueSet.add(Experiment
										.randomElement(config.getUserAttrSet()
												.get(uAttr).getDomain()));
							} else {
								if (config.getUserAttrSet().get(uAttr)
										.getSetDomain().size() == 1) {
									continue;
								}
								uAttrValueSet = new HashSet<String>(
										Experiment.randomElement(config
												.getUserAttrSet().get(uAttr)
												.getSetDomain()));
							}
							if (uAttrValueSet.equals(config.getUserAttrInfo()
									.get(user).get(uAttr))) {
								continue;
							}
							HashSet<String> uAttrValueSetOld = config
									.getUserAttrInfo().get(user).get(uAttr);
							config.getUserAttrInfo().get(user)
									.put(uAttr, uAttrValueSet);
							// System.out.println(changedAttrs + " Add User: "
							// + user + " uAttr: " + uAttr
							// + "uAttrValue: " + uAttrValueSet);
							affectedUPTuples = computeAffectedUPTriples(config,
									originalUP);
							if (affectedUPTuples > numANoise) {
								config.getUserAttrInfo().get(user)
										.put(uAttr, uAttrValueSetOld);
								changedUserAttrPairs.remove(userAttrPair);
								break;
							}
						}
					} else {
						// replace a non-bottom value with bottom
						if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
							continue;
						}
						String uAttr = Experiment
								.randomElement(new HashSet<String>(config
										.getUserAttrInfo().get(user).keySet()));
						while (uAttr.equals("uid")) {
							uAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.keySet()));
						}
						Pair<String, String> userAttrPair = new Pair<String, String>(
								user, uAttr);
						if (changedUserAttrPairs.contains(userAttrPair)) {
							continue;
						} else {
							changedUserAttrPairs.add(userAttrPair);
						}
						HashSet<String> uAttrValueSetOld = config
								.getUserAttrInfo().get(user).get(uAttr);
						config.getUserAttrInfo().get(user).remove(uAttr);
						// System.out.println(changedAttrs + " Remove User: "
						// + user + " uAttr: " + uAttr);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getUserAttrInfo().get(user)
									.put(uAttr, uAttrValueSetOld);
							changedUserAttrPairs.remove(userAttrPair);
							break;
						}
					}
				} else {
					// change resource attribute
					String resource = Experiment.randomElement(config
							.getResources());
					if (rand.nextFloat() <= 0.5) {
						// replace a non-bottom value with the value of the
						// same attribute from a randomly selected user
						if (config.getResourceAttrInfo().get(resource).keySet()
								.size() == 1) {
							continue;
						} else {
							String rAttr = Experiment
									.randomElement(new HashSet<String>((config
											.getResourceAttrSet().keySet())));
							while (rAttr.equals("rid")) {
								rAttr = Experiment
										.randomElement(new HashSet<String>(
												config.getResourceAttrSet()
														.keySet()));
							}
							Pair<String, String> resourceAttrPair = new Pair<String, String>(
									resource, rAttr);
							if (changedResourceAttrPairs
									.contains(resourceAttrPair)) {
								continue;
							} else {
								changedResourceAttrPairs.add(resourceAttrPair);
							}
							HashSet<String> rAttrValueSet = new HashSet<String>();
							if (config.getResourceAttrSet().get(rAttr)
									.getvType() == ValueType.Single) {
								if (config.getResourceAttrSet().get(rAttr)
										.getDomain().size() == 1) {
									continue;
								}
								rAttrValueSet.add(Experiment
										.randomElement(config
												.getResourceAttrSet()
												.get(rAttr).getDomain()));
							} else {
								if (config.getResourceAttrSet().get(rAttr)
										.getSetDomain().size() == 1) {
									continue;
								}
								rAttrValueSet = new HashSet<String>(
										Experiment.randomElement(config
												.getResourceAttrSet()
												.get(rAttr).getSetDomain()));
							}
							if (rAttrValueSet.equals(config
									.getResourceAttrInfo().get(resource)
									.get(rAttr))) {
								continue;
							}
							HashSet<String> rAttrValueSetOld = config
									.getResourceAttrInfo().get(resource)
									.get(rAttr);
							config.getResourceAttrInfo().get(resource)
									.put(rAttr, rAttrValueSet);
							// System.out.println(changedAttrs +
							// " Add Resource: "
							// + resource + " rAttr: " + rAttr
							// + "rAttrValue: " + rAttrValueSet);
							affectedUPTuples = computeAffectedUPTriples(config,
									originalUP);
							if (affectedUPTuples > numANoise) {
								config.getResourceAttrInfo().get(resource)
										.put(rAttr, rAttrValueSetOld);
								changedResourceAttrPairs
										.remove(resourceAttrPair);
								break;
							}
						}
					} else {
						if (config.getResourceAttrInfo().get(resource).keySet()
								.size() == 1) {
							continue;
						}
						String rAttr = Experiment
								.randomElement(new HashSet<String>(config
										.getResourceAttrInfo().get(resource)
										.keySet()));
						while (rAttr.equals("rid")) {
							rAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getResourceAttrInfo()
											.get(resource).keySet()));
						}
						Pair<String, String> resourceAttrPair = new Pair<String, String>(
								resource, rAttr);
						if (changedResourceAttrPairs.contains(resourceAttrPair)) {
							continue;
						} else {
							changedResourceAttrPairs.add(resourceAttrPair);
						}
						HashSet<String> rAttrValueSetOld = config
								.getResourceAttrInfo().get(resource).get(rAttr);
						config.getResourceAttrInfo().get(resource)
								.remove(rAttr);
						// System.out.println(changedAttrs +
						// " Remove Resource: "
						// + resource + " rAttr: " + rAttr);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getResourceAttrInfo().get(resource)
									.put(rAttr, rAttrValueSetOld);
							changedResourceAttrPairs.remove(resourceAttrPair);
							break;
						}
					}
				}
			}

			// Construct the UP relation entailed by the new attribtue data
			// Construct UP relations from rules
			HashSet<Triple<String, String, String>> newCoveredUP = new HashSet<Triple<String, String, String>>();

			for (Rule r : config.getRuleList()) {
				HashSet<String> satUsers = new HashSet<String>();
				HashSet<String> satPerms = new HashSet<String>();
				for (String u : config.getUsers()) {
					if (Parser.satisfyingRule(u, r, config, true)) {
						satUsers.add(u);
					}
				}
				for (String p : config.getResources()) {
					if (Parser.satisfyingRule(p, r, config, false)) {
						satPerms.add(p);
					}
				}
				for (String u : satUsers) {
					for (String p : satPerms) {
						if (Parser.satisfyingRuleConstraints(u, p, r, config)) {
							for (String op : r.getOps()) {
								config.getUserPerms().get(u)
										.add(new Pair<String, String>(op, p));
								Pair<String, String> perm = new Pair<String, String>(
										op, p);
								if (!config.getPermUsers().containsKey(perm)) {
									config.getPermUsers().put(perm,
											new HashSet<String>());
								}
								config.getPermUsers().get(perm).add(u);
								newCoveredUP
										.add(new Triple<String, String, String>(
												u, op, p));
							}
						}
					}
				}
			}

			HashSet<Triple<String, String, String>> attrOverAssignment = new HashSet<Triple<String, String, String>>(
					originalUP);
			attrOverAssignment.removeAll(newCoveredUP);

			HashSet<Triple<String, String, String>> attrUnderAssignment = new HashSet<Triple<String, String, String>>(
					newCoveredUP);
			attrUnderAssignment.removeAll(originalUP);

			// System.out.println("Overassignment " +
			// attrOverAssignment.size());

			// System.out.println("Underassignment " +
			// attrUnderAssignment.size());
			// System.out.println(config.getCoveredUP().size());
			// System.out.println("finished injecting attribute noise");

			int numUNoise = (int) (originalUP.size() * unoiseRatio);

			int numONoise = (int) (originalUP.size() * onoiseRatio);
			affectedUPTuples = computeAffectedUPTriples(config, originalUP);
			numONoise += numANoise - affectedUPTuples;

			// System.out.println("Num of underassignment " + numUNoise);
			while (config.getUnderassignmentUP().size() < numUNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				// updating UserPerms and PermUsers
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			// System.out.println("Underassignment:\n" +
			// config.getUnderassignmentUP());

			// System.out.println("Num of overassignment " + numONoise);

			while (config.getOverassignmentUP().size() < numONoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!originalUP.contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			// System.out.println("Overassignment:\n" +
			// config.getOverassignmentUP());
			HashSet<Triple<String, String, String>> actOverAssignment = new HashSet<Triple<String, String, String>>(
					Parser.config.getOverassignmentUP());
			actOverAssignment.addAll(attrOverAssignment);

			HashSet<Triple<String, String, String>> actUnderAssignment = new HashSet<Triple<String, String, String>>(
					Parser.config.getUnderassignmentUP());
			actUnderAssignment.addAll(attrUnderAssignment);

			HashSet<Triple<String, String, String>> injectedNoise = new HashSet<Triple<String, String, String>>(
					config.getUnderassignmentUP());
			injectedNoise.addAll(config.getOverassignmentUP());
			// System.out.println("Num of injected noise: " +
			// injectedNoise.size());
			// System.out.println("InjectedNoise:\n" + injectedNoise);

			double maximumSimilarity_o = -1;
			double maximumSimilarity_u = -1;
			double maximumPolicySimilarity_o = -1;
			double maximumPolicySimilarity_u = -1;
			for (double tau : TAU) {
				// System.out.println(tau);
				for (double alpha : ALPHA) {
					// System.out.println(alpha);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							false, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);
					double currentSimilarity_o = ABACMiner.jaccardSimilarity(
							actOverAssignment, ABACMiner.overAssignments);
					// System.out.println("Num of reported noise: " +
					// reportedNoise.size());
					// System.out.println("ReportedNoise:\n" + reportedNoise);
					// if (Double.compare(currentSimilarity_o, 1.0) == 0) {
					// bestTau_o = tau;
					// bestAlpha_o = alpha;
					// maximumSimilarity_o = 1.0;
					// break;
					// }
					if (Double
							.compare(currentSimilarity_o, maximumSimilarity_o) > 0) {
						bestTau_o = tau;
						bestAlpha_o = alpha;
						maximumSimilarity_o = currentSimilarity_o;
						// maximumPolicySimilarity_o = ABACMiner
						// .nonsymmetricSyntacticSimilarityPolicies(
						// ABACMiner.resultRules,
						// config.getRuleList(), config);

						maximumPolicySimilarity_o = ABACMiner
								.nonsymSemanticSimilarityOfPolicies(
										ABACMiner.resultRules, config,
										originalUP, ABACMiner.overAssignments,
										ABACMiner.underAssignments);
					}
					double currentSimilarity_u = ABACMiner.jaccardSimilarity(
							actUnderAssignment, ABACMiner.underAssignments);
					if (Double
							.compare(currentSimilarity_u, maximumSimilarity_u) > 0) {
						bestTau_u = tau;
						bestAlpha_u = alpha;
						maximumSimilarity_u = currentSimilarity_u;
						// maximumPolicySimilarity_u = ABACMiner
						// .nonsymmetricSyntacticSimilarityPolicies(
						// ABACMiner.resultRules,
						// config.getRuleList(), config);
						maximumPolicySimilarity_u = ABACMiner
								.nonsymSemanticSimilarityOfPolicies(
										ABACMiner.resultRules, config,
										originalUP, ABACMiner.overAssignments,
										ABACMiner.underAssignments);
					}
					if (Double.compare(maximumSimilarity_o, 1.0) == 0
							&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
						break;
					}
				}
				if (Double.compare(maximumSimilarity_o, 1.0) == 0
						&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
					break;
				}
			}
			tausAndAlphas_o.add(j, new Pair<Double, Double>(bestTau_o,
					bestAlpha_o));
			maxSimilarities_o[j] = maximumSimilarity_o;
			maxPolicySimilarities_o[j] = maximumPolicySimilarity_o;
			tausAndAlphas_u.add(j, new Pair<Double, Double>(bestTau_u,
					bestAlpha_u));
			maxSimilarities_u[j] = maximumSimilarity_u;
			maxPolicySimilarities_u[j] = maximumPolicySimilarity_u;
			// System.out.println(maximumSimilarity_o + " "
			// + maximumPolicySimilarity_o + " " + maximumSimilarity_u
			// + " " + maximumPolicySimilarity_u);
		}
		// System.out.println(noiseRatio + " " + bestTau + " " +
		// maximumSimilarity);
		for (int i = 0; i < NUM_CASES; i++) {
			taus_o[i] = tausAndAlphas_o.get(i).getFirst();
			alphas_o[i] = tausAndAlphas_o.get(i).getSecond();
			taus_u[i] = tausAndAlphas_u.get(i).getFirst();
			alphas_u[i] = tausAndAlphas_u.get(i).getSecond();
		}
		System.out.print(unoiseRatio + " " + onoiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_o),
				std.evaluate(taus_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_o),
				std.evaluate(alphas_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_o),
				std.evaluate(maxSimilarities_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxPolicySimilarities_o),
				std.evaluate(maxPolicySimilarities_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_u),
				std.evaluate(taus_u));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_u),
				std.evaluate(alphas_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_u),
				std.evaluate(maxSimilarities_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxPolicySimilarities_u),
				std.evaluate(maxPolicySimilarities_u));
		System.out.println();
	}

	public static int computeAffectedUPTriples(Config config,
			HashSet<Triple<String, String, String>> originalUP) {
		HashSet<Triple<String, String, String>> coveredUP = new HashSet<Triple<String, String, String>>();
		for (Rule r : config.getRuleList()) {
			HashSet<String> satUsers = new HashSet<String>();
			HashSet<String> satPerms = new HashSet<String>();
			for (String u : config.getUsers()) {
				if (Parser.satisfyingRule(u, r, config, true)) {
					satUsers.add(u);
				}
			}
			for (String p : config.getResources()) {
				if (Parser.satisfyingRule(p, r, config, false)) {
					satPerms.add(p);
				}
			}
			for (String u : satUsers) {
				for (String p : satPerms) {
					if (Parser.satisfyingRuleConstraints(u, p, r, config)) {
						for (String op : r.getOps()) {
							config.getUserPerms().get(u)
									.add(new Pair<String, String>(op, p));
							Pair<String, String> perm = new Pair<String, String>(
									op, p);
							if (!config.getPermUsers().containsKey(perm)) {
								config.getPermUsers().put(perm,
										new HashSet<String>());
							}
							config.getPermUsers().get(perm).add(u);
							coveredUP.add(new Triple<String, String, String>(u,
									op, p));
						}
					}
				}
			}
		}

		HashSet<Triple<String, String, String>> differenceSet1 = new HashSet<Triple<String, String, String>>(
				originalUP);
		differenceSet1.removeAll(coveredUP);
		HashSet<Triple<String, String, String>> differenceSet2 = new HashSet<Triple<String, String, String>>(
				coveredUP);
		differenceSet2.removeAll(originalUP);
		return differenceSet1.size() + differenceSet2.size();
	}

	/**
	 * 
	 * @param unoiseRatio
	 *            : underAssignment ratio
	 * @param onoiseRatio
	 *            : overAssignment ratio
	 */
	public static void OverAndUnderassignment(double unoiseRatio,
			double onoiseRatio) {
		StandardDeviation std = new StandardDeviation();
		double[] taus_o = new double[NUM_CASES];
		double[] alphas_o = new double[NUM_CASES];

		double[] taus_u = new double[NUM_CASES];
		double[] alphas_u = new double[NUM_CASES];

		Vector<Pair<Double, Double>> tausAndAlphas_o = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_u = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		double[] maxSimilarities_o = new double[NUM_CASES];
		double[] maxSimilarities_u = new double[NUM_CASES];

		double[] maxSyntacticSimilarities_o = new double[NUM_CASES];
		double[] maxSyntacticSimilarities_u = new double[NUM_CASES];

		for (int j = 0; j < NUM_CASES; j++) {
			System.out.println(j);
			double bestTau_o = 0.0;
			double bestAlpha_o = 0.0;
			double bestTau_u = 0.0;
			double bestAlpha_u = 0.0;
			String inputFile = "ran-case-studies/synthetic_" + NRULE + "_"
					+ unoiseRatio + "_" + onoiseRatio + "_" + j + ".abac";
			SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
					null, NRULE, false, true, 0, 0, 0, 0, 0);

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());
			// System.out.println("Original UP:\n" + originalUP);

			int numUNoise = (int) (originalUP.size() * unoiseRatio);

			// System.out.println("Num of underassignment " + numUNoise);
			while (config.getUnderassignmentUP().size() < numUNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				// updating UserPerms and PermUsers
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			// System.out.println("Underassignment:\n" +
			// config.getUnderassignmentUP());

			int numONoise = (int) (originalUP.size() * onoiseRatio);
			// System.out.println("Num of overassignment " + numONoise);

			while (config.getOverassignmentUP().size() < numONoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!originalUP.contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			HashSet<Triple<String, String, String>> injectedNoise = new HashSet<Triple<String, String, String>>(
					config.getUnderassignmentUP());
			injectedNoise.addAll(config.getOverassignmentUP());

			double maximumSimilarity_o = -1;
			double maximumSimilarity_u = -1;
			double maximumSyntacticSimilarity_o = -1;
			double maximumSyntacticSimilarity_u = -1;
			for (double tau : TAU) {
				for (double alpha : ALPHA) {
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);
					double currentSimilarity_o = ABACMiner.jaccardSimilarity(
							Parser.config.getOverassignmentUP(),
							ABACMiner.overAssignments);
					if (Double
							.compare(currentSimilarity_o, maximumSimilarity_o) > 0) {
						bestTau_o = tau;
						bestAlpha_o = alpha;
						maximumSimilarity_o = currentSimilarity_o;
						maximumSyntacticSimilarity_o = ABACMiner
								.symmetricSyntacticSimilarityOfPolicies(
										Parser.config.getRuleList(),
										ABACMiner.resultRules, Parser.config);
					}
					double currentSimilarity_u = ABACMiner.jaccardSimilarity(
							Parser.config.getUnderassignmentUP(),
							ABACMiner.underAssignments);
					if (Double
							.compare(currentSimilarity_u, maximumSimilarity_u) > 0) {
						bestTau_u = tau;
						bestAlpha_u = alpha;
						maximumSimilarity_u = currentSimilarity_u;
						maximumSyntacticSimilarity_u = ABACMiner
								.symmetricSyntacticSimilarityOfPolicies(
										Parser.config.getRuleList(),
										ABACMiner.resultRules, Parser.config);
					}
					if (Double.compare(maximumSimilarity_o, 1.0) == 0
							&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
						break;
					}
				}
				if (Double.compare(maximumSimilarity_o, 1.0) == 0
						&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
					break;
				}
			}
			tausAndAlphas_o.add(j, new Pair<Double, Double>(bestTau_o,
					bestAlpha_o));
			maxSimilarities_o[j] = maximumSimilarity_o;
			maxSyntacticSimilarities_o[j] = maximumSyntacticSimilarity_o;
			tausAndAlphas_u.add(j, new Pair<Double, Double>(bestTau_u,
					bestAlpha_u));
			maxSimilarities_u[j] = maximumSimilarity_u;
			maxSyntacticSimilarities_u[j] = maximumSyntacticSimilarity_u;
		}
		for (int i = 0; i < NUM_CASES; i++) {
			taus_o[i] = tausAndAlphas_o.get(i).getFirst();
			alphas_o[i] = tausAndAlphas_o.get(i).getSecond();
			taus_u[i] = tausAndAlphas_u.get(i).getFirst();
			alphas_u[i] = tausAndAlphas_u.get(i).getSecond();
		}
		System.out.print(unoiseRatio + " " + onoiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_o),
				std.evaluate(taus_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_o),
				std.evaluate(alphas_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_o),
				std.evaluate(maxSimilarities_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSyntacticSimilarities_o),
				std.evaluate(maxSyntacticSimilarities_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_u),
				std.evaluate(taus_u));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_u),
				std.evaluate(alphas_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_u),
				std.evaluate(maxSimilarities_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSyntacticSimilarities_u),
				std.evaluate(maxSyntacticSimilarities_u));
		System.out.println();
	}

	public static void OverAndUnderassignmentOnCaseStudy(double unoiseRatio,
			double onoiseRatio, String inputFile) {
		System.out.println(inputFile);
		StandardDeviation std = new StandardDeviation();
		double[] taus_o = new double[NUM_CASES];
		double[] alphas_o = new double[NUM_CASES];

		double[] taus_u = new double[NUM_CASES];
		double[] alphas_u = new double[NUM_CASES];

		Vector<Pair<Double, Double>> tausAndAlphas_o = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_u = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		double[] maxSimilarities_o = new double[NUM_CASES];
		double[] maxSimilarities_u = new double[NUM_CASES];

		double[] maxSyntacticSimilarities_o = new double[NUM_CASES];
		double[] maxSyntacticSimilarities_u = new double[NUM_CASES];
		System.out.println();
		for (int j = 0; j < NUM_CASES; j++) {
			System.out.println(j);
			double bestTau_o = 0.0;
			double bestAlpha_o = 0.0;
			double bestTau_u = 0.0;
			double bestAlpha_u = 0.0;
			// inputFile = "ran-case-studies/synthetic_" + NRULE + "_" + j +
			// ".abac";
			// SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
			// null, NRULE, false, true, 0, 0, 0, 0, 0);

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());
			// System.out.println("Original UP:\n" + originalUP);

			int numUNoise = (int) (originalUP.size() * unoiseRatio);

			System.out.println("Num of underassignment " + numUNoise);
			while (config.getUnderassignmentUP().size() < numUNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				// updating UserPerms and PermUsers
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			System.out.println("Underassignment:\n"
					+ config.getUnderassignmentUP());

			int numONoise = (int) (originalUP.size() * onoiseRatio);
			System.out.println("Num of overassignment " + numONoise);
			while (config.getOverassignmentUP().size() < numONoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!originalUP.contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			HashSet<Triple<String, String, String>> injectedNoise = new HashSet<Triple<String, String, String>>(
					config.getUnderassignmentUP());
			injectedNoise.addAll(config.getOverassignmentUP());

			double maximumSimilarity_o = -1;
			double maximumSimilarity_u = -1;
			double maximumSyntacticSimilarity_o = -1;
			double maximumSyntacticSimilarity_u = -1;
			HashSet<Triple<String, String, String>> maxUnderAssignments = new HashSet<Triple<String, String, String>>();
			for (double tau : TAU) {
				for (double alpha : ALPHA) {
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);
					double currentSimilarity_o = ABACMiner.jaccardSimilarity(
							Parser.config.getOverassignmentUP(),
							ABACMiner.overAssignments);
					if (Double
							.compare(currentSimilarity_o, maximumSimilarity_o) > 0) {
						bestTau_o = tau;
						bestAlpha_o = alpha;
						maximumSimilarity_o = currentSimilarity_o;
						maximumSyntacticSimilarity_o = ABACMiner
								.symmetricSyntacticSimilarityOfPolicies(
										Parser.config.getRuleList(),
										ABACMiner.resultRules, Parser.config);
					}
					double currentSimilarity_u = ABACMiner.jaccardSimilarity(
							Parser.config.getUnderassignmentUP(),
							ABACMiner.underAssignments);
					if (Double
							.compare(currentSimilarity_u, maximumSimilarity_u) > 0) {
						bestTau_u = tau;
						bestAlpha_u = alpha;
						maximumSimilarity_u = currentSimilarity_u;
						maximumSyntacticSimilarity_u = ABACMiner
								.symmetricSyntacticSimilarityOfPolicies(
										Parser.config.getRuleList(),
										ABACMiner.resultRules, Parser.config);
						maxUnderAssignments = ABACMiner.underAssignments;
					}
					if (Double.compare(maximumSimilarity_o, 1.0) == 0
							&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
						break;
					}
				}
				if (Double.compare(maximumSimilarity_o, 1.0) == 0
						&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
					break;
				}
			}
			tausAndAlphas_o.add(j, new Pair<Double, Double>(bestTau_o,
					bestAlpha_o));
			maxSimilarities_o[j] = maximumSimilarity_o;
			maxSyntacticSimilarities_o[j] = maximumSyntacticSimilarity_o;
			tausAndAlphas_u.add(j, new Pair<Double, Double>(bestTau_u,
					bestAlpha_u));
			maxSimilarities_u[j] = maximumSimilarity_u;
			maxSyntacticSimilarities_u[j] = maximumSyntacticSimilarity_u;
			System.out.println(maxUnderAssignments);
		}
		for (int i = 0; i < NUM_CASES; i++) {
			taus_o[i] = tausAndAlphas_o.get(i).getFirst();
			alphas_o[i] = tausAndAlphas_o.get(i).getSecond();
			taus_u[i] = tausAndAlphas_u.get(i).getFirst();
			alphas_u[i] = tausAndAlphas_u.get(i).getSecond();
		}
		System.out.print(unoiseRatio + " " + onoiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_o),
				std.evaluate(taus_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_o),
				std.evaluate(alphas_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_o),
				std.evaluate(maxSimilarities_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSyntacticSimilarities_o),
				std.evaluate(maxSyntacticSimilarities_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_u),
				std.evaluate(taus_u));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_u),
				std.evaluate(alphas_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_u),
				std.evaluate(maxSimilarities_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSyntacticSimilarities_u),
				std.evaluate(maxSyntacticSimilarities_u));
		System.out.println();
	}

	public static void underAssignmentExperiment(double noiseRatio) {
		StandardDeviation std = new StandardDeviation();
		double[] taus = new double[NUM_CASES];
		double[] maxSimilarities = new double[NUM_CASES];
		for (int j = 0; j < NUM_CASES; j++) {
			double bestTau = 0;
			String inputFile = "ran-case-studies/test.abac";
			SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
					null, NRULE, false, true, 0, 0, 0, 0, 0);

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			int numNoise = (int) (config.getCoveredUP().size() * noiseRatio);

			while (config.getUnderassignmentUP().size() < numNoise
					&& !config.getCoveredUP().isEmpty()) {
				Triple<String, String, String> element = ABACMiner
						.randomElement(config.getCoveredUP());
				config.getUnderassignmentUP().add(element);
				config.getCoveredUP().remove(element);
				config.getUserPerms()
						.get(element.getFirst())
						.remove(new Pair<String, String>(element.getSecond(),
								element.getThird()));
				config.getPermUsers()
						.get(new Pair<String, String>(element.getSecond(),
								element.getThird())).remove(element.getFirst());
			}

			double maximumSimilarity = -1;
			HashSet<Triple<String, String, String>> injectedNoise = new HashSet<Triple<String, String, String>>(
					config.getUnderassignmentUP());
			injectedNoise.addAll(config.getOverassignmentUP());

			for (double i = 0.10; i <= 0.50; i += 0.10) {
				ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true,
						0, i, true);
				HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
						ABACMiner.underAssignments);
				reportedNoise.addAll(ABACMiner.overAssignments);
				double currentSimilarity = ABACMiner.jaccardSimilarity(
						injectedNoise, reportedNoise);
				if (Double.compare(currentSimilarity, 1.0) == 0) {
					bestTau = i;
					maximumSimilarity = 1.0;
					break;
				}
				if (Double.compare(currentSimilarity, maximumSimilarity) > 0) {
					bestTau = i;
					maximumSimilarity = currentSimilarity;
				}
			}
			taus[j] = bestTau;
			maxSimilarities[j] = maximumSimilarity;
		}
		// System.out.println(noiseRatio + " " + bestTau + " " +
		// maximumSimilarity);
		System.out.print(noiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus),
				std.evaluate(taus));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(maxSimilarities),
				std.evaluate(maxSimilarities));
		System.out.println();
	}

	/**
	 * Return the best \tau that can minimize the jaccard distance of the actual
	 * noise and reported noise
	 * 
	 * @param noiseRatio
	 *            : a double value between 0 and 1
	 * @return
	 */
	public static void overAssignmentExperiment(double noiseRatio) {
		StandardDeviation std = new StandardDeviation();
		double[] taus = new double[NUM_CASES];
		double[] maxSimilarities = new double[NUM_CASES];
		for (int j = 0; j < NUM_CASES; j++) {
			int bestTau = 0;
			String inputFile = "ran-case-studies/test.abac";
			SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
					null, NRULE, false, true, 0, 0, 0, 0, 0);

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			int numNoise = (int) (config.getCoveredUP().size() * noiseRatio);

			while (config.getOverassignmentUP().size() < numNoise) {
				String user = RandomUtil.randomElement(config.getUsers());
				String op = RandomUtil.randomElement(config.getOps());
				String res = RandomUtil.randomElement(config.getResources());
				Triple<String, String, String> userPerm = new Triple<String, String, String>(
						user, op, res);
				if (!config.getCoveredUP().contains(userPerm)) {
					config.getOverassignmentUP().add(userPerm);
					config.getCoveredUP().add(userPerm);
					if (!config.getUserPerms().containsKey(user)) {
						config.getUserPerms().put(user,
								new HashSet<Pair<String, String>>());
					}
					config.getUserPerms().get(user)
							.add(new Pair<String, String>(op, res));
					if (!config.getPermUsers().containsKey(
							new Pair<String, String>(op, res))) {
						config.getPermUsers().put(
								new Pair<String, String>(op, res),
								new HashSet<String>());
					}
					config.getPermUsers()
							.get(new Pair<String, String>(op, res)).add(user);
				}
			}
			double maximumSimilarity = -1;
			for (int i = 1; i <= MAXIMUM_THRES; i++) {
				ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true,
						i, 0.0, true);
				double currentSimilarity = ABACMiner
						.jaccardSimilarity(config.getOverassignmentUP(),
								ABACMiner.overAssignments);
				if (Double.compare(currentSimilarity, 1.0) == 0) {
					bestTau = i;
					maximumSimilarity = 1.0;
					break;
				}
				if (Double.compare(currentSimilarity, maximumSimilarity) > 0) {
					bestTau = i;
					maximumSimilarity = currentSimilarity;
				}
			}
			taus[j] = bestTau;
			maxSimilarities[j] = maximumSimilarity;
		}
		// System.out.println(noiseRatio + " " + bestTau + " " +
		// maximumSimilarity);
		System.out.print(noiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus),
				std.evaluate(taus));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(maxSimilarities),
				std.evaluate(maxSimilarities));
		System.out.println();
	}

	public static double doubleArrayAverage(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum / a.length;
	}

	public static void sanityCheck(double unoiseRatio, double onoiseRatio,
			double anoiseRatio) {
		String inputFile = "ran-case-studies/test.abac";
		SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile, null,
				NRULE, false, true, 0, 0, 0, 0, 0);

		Parser.config = new Config();
		Config config = Parser.config;
		Parser.parseInputFile(inputFile);

		HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
				config.getCoveredUP());
		// System.out.println("Original UP:\n" + originalUP);

		int numANoise = (int) (originalUP.size() * anoiseRatio);
		int affectedUPTuples = 0;
		HashSet<Pair<String, String>> changedUserAttrPairs = new HashSet<Pair<String, String>>();
		HashSet<Pair<String, String>> changedResourceAttrPairs = new HashSet<Pair<String, String>>();
		Random rand = new Random(System.currentTimeMillis());
		// System.out.println("Number of attributes to change:" +
		// numANoise);
		while (true) {
			if (rand.nextFloat() <= 0.5) {
				// change user attribute
				String user = Experiment.randomElement(config.getUsers());
				if (rand.nextFloat() <= 0.5) {
					// replace a non-bottom value with the value of the
					// same attribute from a randomly selected user
					if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
						continue;
					} else {
						String uAttr = Experiment
								.randomElement(new HashSet<String>(config
										.getUserAttrInfo().get(user).keySet()));
						while (uAttr.equals("uid")) {
							uAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getUserAttrInfo().get(user)
											.keySet()));
						}
						Pair<String, String> userAttrPair = new Pair<String, String>(
								user, uAttr);
						if (changedUserAttrPairs.contains(userAttrPair)) {
							continue;
						} else {
							changedUserAttrPairs.add(userAttrPair);
						}
						HashSet<String> uAttrValueSet = new HashSet<String>();
						if (config.getUserAttrSet().get(uAttr).getvType() == ValueType.Single) {
							if (config.getUserAttrSet().get(uAttr).getDomain()
									.size() == 1) {
								continue;
							}
							uAttrValueSet.add(Experiment.randomElement(config
									.getUserAttrSet().get(uAttr).getDomain()));
						} else {
							if (config.getUserAttrSet().get(uAttr)
									.getSetDomain().size() == 1) {
								continue;
							}
							uAttrValueSet = new HashSet<String>(
									Experiment.randomElement(config
											.getUserAttrSet().get(uAttr)
											.getSetDomain()));
						}
						if (uAttrValueSet.equals(config.getUserAttrInfo()
								.get(user).get(uAttr))) {
							continue;
						}
						HashSet<String> uAttrValueSetOld = config
								.getUserAttrInfo().get(user).get(uAttr);
						config.getUserAttrInfo().get(user)
								.put(uAttr, uAttrValueSet);
						// System.out.println(changedAttrs + " Add User: "
						// + user + " uAttr: " + uAttr
						// + "uAttrValue: " + uAttrValueSet);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getUserAttrInfo().get(user)
									.put(uAttr, uAttrValueSetOld);
							changedUserAttrPairs.remove(userAttrPair);
							break;
						}
					}
				} else {
					// replace a non-bottom value with bottom
					if (config.getUserAttrInfo().get(user).keySet().size() == 1) {
						continue;
					}
					String uAttr = Experiment
							.randomElement(new HashSet<String>(config
									.getUserAttrInfo().get(user).keySet()));
					while (uAttr.equals("uid")) {
						uAttr = Experiment.randomElement(new HashSet<String>(
								config.getUserAttrInfo().get(user).keySet()));
					}
					Pair<String, String> userAttrPair = new Pair<String, String>(
							user, uAttr);
					if (changedUserAttrPairs.contains(userAttrPair)) {
						continue;
					} else {
						changedUserAttrPairs.add(userAttrPair);
					}
					HashSet<String> uAttrValueSetOld = config.getUserAttrInfo()
							.get(user).get(uAttr);
					config.getUserAttrInfo().get(user).remove(uAttr);
					// System.out.println(changedAttrs + " Remove User: "
					// + user + " uAttr: " + uAttr);
					affectedUPTuples = computeAffectedUPTriples(config,
							originalUP);
					if (affectedUPTuples > numANoise) {
						config.getUserAttrInfo().get(user)
								.put(uAttr, uAttrValueSetOld);
						changedUserAttrPairs.remove(userAttrPair);
						break;
					}
				}
			} else {
				// change resource attribute
				String resource = Experiment.randomElement(config
						.getResources());
				if (rand.nextFloat() <= 0.5) {
					// replace a non-bottom value with the value of the
					// same attribute from a randomly selected user
					if (config.getResourceAttrInfo().get(resource).keySet()
							.size() == 1) {
						continue;
					} else {
						String rAttr = Experiment
								.randomElement(new HashSet<String>((config
										.getResourceAttrSet().keySet())));
						while (rAttr.equals("rid")) {
							rAttr = Experiment
									.randomElement(new HashSet<String>(config
											.getResourceAttrSet().keySet()));
						}
						Pair<String, String> resourceAttrPair = new Pair<String, String>(
								resource, rAttr);
						if (changedResourceAttrPairs.contains(resourceAttrPair)) {
							continue;
						} else {
							changedResourceAttrPairs.add(resourceAttrPair);
						}
						HashSet<String> rAttrValueSet = new HashSet<String>();
						if (config.getResourceAttrSet().get(rAttr).getvType() == ValueType.Single) {
							if (config.getResourceAttrSet().get(rAttr)
									.getDomain().size() == 1) {
								continue;
							}
							rAttrValueSet.add(Experiment.randomElement(config
									.getResourceAttrSet().get(rAttr)
									.getDomain()));
						} else {
							if (config.getResourceAttrSet().get(rAttr)
									.getSetDomain().size() == 1) {
								continue;
							}
							rAttrValueSet = new HashSet<String>(
									Experiment.randomElement(config
											.getResourceAttrSet().get(rAttr)
											.getSetDomain()));
						}
						if (rAttrValueSet.equals(config.getResourceAttrInfo()
								.get(resource).get(rAttr))) {
							continue;
						}
						HashSet<String> rAttrValueSetOld = config
								.getResourceAttrInfo().get(resource).get(rAttr);
						config.getResourceAttrInfo().get(resource)
								.put(rAttr, rAttrValueSet);
						// System.out.println(changedAttrs +
						// " Add Resource: "
						// + resource + " rAttr: " + rAttr
						// + "rAttrValue: " + rAttrValueSet);
						affectedUPTuples = computeAffectedUPTriples(config,
								originalUP);
						if (affectedUPTuples > numANoise) {
							config.getResourceAttrInfo().get(resource)
									.put(rAttr, rAttrValueSetOld);
							changedResourceAttrPairs.remove(resourceAttrPair);
							break;
						}
					}
				} else {
					if (config.getResourceAttrInfo().get(resource).keySet()
							.size() == 1) {
						continue;
					}
					String rAttr = Experiment
							.randomElement(new HashSet<String>(config
									.getResourceAttrInfo().get(resource)
									.keySet()));
					while (rAttr.equals("rid")) {
						rAttr = Experiment.randomElement(new HashSet<String>(
								config.getResourceAttrInfo().get(resource)
										.keySet()));
					}
					Pair<String, String> resourceAttrPair = new Pair<String, String>(
							resource, rAttr);
					if (changedResourceAttrPairs.contains(resourceAttrPair)) {
						continue;
					} else {
						changedResourceAttrPairs.add(resourceAttrPair);
					}
					HashSet<String> rAttrValueSetOld = config
							.getResourceAttrInfo().get(resource).get(rAttr);
					config.getResourceAttrInfo().get(resource).remove(rAttr);
					// System.out.println(changedAttrs +
					// " Remove Resource: "
					// + resource + " rAttr: " + rAttr);
					affectedUPTuples = computeAffectedUPTriples(config,
							originalUP);
					if (affectedUPTuples > numANoise) {
						config.getResourceAttrInfo().get(resource)
								.put(rAttr, rAttrValueSetOld);
						changedResourceAttrPairs.remove(resourceAttrPair);
						break;
					}
				}
			}
		}

		// Construct the UP relation entailed by the new attribtue data
		// Construct UP relations from rules
		HashSet<Triple<String, String, String>> newCoveredUP = new HashSet<Triple<String, String, String>>();

		for (Rule r : config.getRuleList()) {
			HashSet<String> satUsers = new HashSet<String>();
			HashSet<String> satPerms = new HashSet<String>();
			for (String u : config.getUsers()) {
				if (Parser.satisfyingRule(u, r, config, true)) {
					satUsers.add(u);
				}
			}
			for (String p : config.getResources()) {
				if (Parser.satisfyingRule(p, r, config, false)) {
					satPerms.add(p);
				}
			}
			for (String u : satUsers) {
				for (String p : satPerms) {
					if (Parser.satisfyingRuleConstraints(u, p, r, config)) {
						for (String op : r.getOps()) {
							config.getUserPerms().get(u)
									.add(new Pair<String, String>(op, p));
							Pair<String, String> perm = new Pair<String, String>(
									op, p);
							if (!config.getPermUsers().containsKey(perm)) {
								config.getPermUsers().put(perm,
										new HashSet<String>());
							}
							config.getPermUsers().get(perm).add(u);
							newCoveredUP
									.add(new Triple<String, String, String>(u,
											op, p));
						}
					}
				}
			}
		}

		HashSet<Triple<String, String, String>> attrOverAssignment = new HashSet<Triple<String, String, String>>(
				originalUP);
		attrOverAssignment.removeAll(newCoveredUP);

		HashSet<Triple<String, String, String>> attrUnderAssignment = new HashSet<Triple<String, String, String>>(
				newCoveredUP);
		attrUnderAssignment.removeAll(originalUP);

		for (Rule r : config.getRuleList()) {
			ABACMiner.resultRules.add(r);
		}
	}

	public static void nonUniformNoiseExperiment(double uNoiseRatio,
			double oNoiseRatio, String inputFile) {
		StandardDeviation std = new StandardDeviation();

		double[] taus_o = new double[NUM_CASES];
		double[] alphas_o = new double[NUM_CASES];

		double[] taus_u = new double[NUM_CASES];
		double[] alphas_u = new double[NUM_CASES];
		
		double[] taus_p = new double[NUM_CASES];
		double[] alphas_p = new double[NUM_CASES]; 

		Vector<Pair<Double, Double>> tausAndAlphas_o = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_u = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		Vector<Pair<Double, Double>> tausAndAlphas_p = new Vector<Pair<Double, Double>>(
				NUM_CASES);
		double[] maxSimilarities_o = new double[NUM_CASES];
		double[] maxSimilarities_u = new double[NUM_CASES];

		double[] maxPolicySimilarities_o = new double[NUM_CASES];
		double[] maxPolicySimilarities_u = new double[NUM_CASES];

		double[] maxPolicySimilarities = new double[NUM_CASES];

		Random randomGen = new Random(System.currentTimeMillis());

		for (int j = 0; j < NUM_CASES; j++) {
			System.out.println("j =" + j);
			double bestTau_o = 0.0;
			double bestAlpha_o = 0.0;
			double bestTau_u = 0.0;
			double bestAlpha_u = 0.0;
			double bestTau_p = 0.0;
			double bestAlpha_p = 0.0;

			if (inputFile == null) {
				inputFile = "ran-case-studies/NonuniformNoise_" + j + ".abac";
				File f = new File(inputFile);
				if (!f.exists()) {
					while (true) {
						if (SyntheticPolicyCaseStudyGenerator
								.generateCaseStudy(inputFile, null, NRULE,
										false, true, 1, 1, 0, 0, 0) != null) {
							break;
						} else {
							System.out.println("Regenerate Policy");
						}
					}
				}
			}

			Parser.config = new Config();
			Config config = Parser.config;
			Parser.parseInputFile(inputFile);

			int Nuser = config.getUsers().size();
			int Nres = config.getResources().size();
			int Nop = config.getOps().size();

			// distributions for selecting users, resources, and operations when
			// generating noise. the choice of standard deviation is somewhat
			// arbitrary.

			DiscreteNormalDistribution noiseUserDist = new DiscreteNormalDistribution(
					0, Nuser - 1, (double)Nuser / 2, (double)Nuser / 4);
			noiseUserDist.setSeed(randomGen.nextLong());

			ArrayList<String> users = new ArrayList<String>();
			users.addAll(config.getUsers());

			DiscreteNormalDistribution noiseResDist = new DiscreteNormalDistribution(
					0, Nres - 1, (double)Nres / 2, (double)Nres / 4);

			noiseResDist.setSeed(randomGen.nextLong());

			ArrayList<String> resources = new ArrayList<String>();
			resources.addAll(config.getResources());

			DiscreteNormalDistribution noiseOpDist = new DiscreteNormalDistribution(
					0, Nop - 1, (double)Nop / 2, (double)Nop / 4);
			noiseOpDist.setSeed(randomGen.nextLong());

			ArrayList<String> ops = new ArrayList<String>();
			ops.addAll(config.getOps());

			HashSet<Triple<String, String, String>> originalUP = new HashSet<Triple<String, String, String>>(
					config.getCoveredUP());

			// add over-assignments
			int Noverassignments = 0;
			HashSet<Triple<String, String, String>> overAssignments = new HashSet<Triple<String, String, String>>();
			int originalUPSize = originalUP.size();
			while (Noverassignments < oNoiseRatio * originalUPSize) {
				String user = users.get(noiseUserDist.getNextDistVal());
				String resource = resources.get(noiseResDist.getNextDistVal());
				HashSet<Pair<String, String>> permSet = config.getUserPerms()
						.get(user);
				if (permSet == null || permSet.isEmpty()) {
					String op = ops.get(noiseOpDist.getNextDistVal());
					Triple<String, String, String> upTriple = new Triple<String, String, String>(
							user, op, resource);
					if (!config.getCoveredUP().contains(upTriple)) {
						config.getCoveredUP().add(upTriple);
						// updating UserPerms and PermUsers
						if (!config.getUserPerms().containsKey(
								upTriple.getFirst())) {
							config.getUserPerms().put(upTriple.getFirst(),
									new HashSet<Pair<String, String>>());
						}
						config.getUserPerms()
								.get(upTriple.getFirst())
								.add(new Pair<String, String>(upTriple
										.getSecond(), upTriple.getThird()));
						if (!config.getPermUsers().containsKey(
								new Pair<String, String>(upTriple.getSecond(),
										upTriple.getThird()))) {
							config.getPermUsers().put(
									new Pair<String, String>(
											upTriple.getSecond(),
											upTriple.getThird()),
									new HashSet<String>());
						}
						config.getPermUsers()
								.get(new Pair<String, String>(upTriple
										.getSecond(), upTriple.getThird()))
								.add(upTriple.getFirst());
						Noverassignments++;
						overAssignments.add(upTriple);
					}
				} else {
					HashSet<String> operationSet = new HashSet<String>();
					for (String op : ops) {
						Pair<String, String> pair = new Pair<String, String>(
								op, resource);
						if (!permSet.contains(pair)) {
							operationSet.add(op);
						}
					}
					if (operationSet.isEmpty()) {
						continue;
					}
					ArrayList<String> operationList = new ArrayList<String>();
					operationList.addAll(operationSet);

					ArrayList<Integer> opIndex = new ArrayList<Integer>();
					for (String op : operationSet) {
						for (int i = 0; i < ops.size(); i++) {
							if (op.equals(ops.get(i))) {
								opIndex.add(i);
								break;
							}
						}
					}
					Collections.sort(opIndex);

					String op = ops.get(noiseOpDist.nextValue(opIndex));
					Triple<String, String, String> upTriple = new Triple<String, String, String>(
							user, op, resource);
					if (!config.getCoveredUP().contains(upTriple)) {
						Noverassignments++;
						config.getCoveredUP().add(upTriple);
						overAssignments.add(upTriple);
						config.getUserPerms()
								.get(upTriple.getFirst())
								.add(new Pair<String, String>(upTriple
										.getSecond(), upTriple.getThird()));
						if (!config.getPermUsers().containsKey(
								new Pair<String, String>(upTriple.getSecond(),
										upTriple.getThird()))) {
							config.getPermUsers().put(
									new Pair<String, String>(
											upTriple.getSecond(),
											upTriple.getThird()),
									new HashSet<String>());
						}
						config.getPermUsers()
								.get(new Pair<String, String>(upTriple
										.getSecond(), upTriple.getThird()))
								.add(upTriple.getFirst());
					}
				}
			}

			// add under-assignments

			int Nunderassignments = 0;
			HashSet<Triple<String, String, String>> underAssignments = new HashSet<Triple<String, String, String>>();
			while (Nunderassignments < uNoiseRatio * originalUPSize) {
				String user = users.get(noiseUserDist.getNextDistVal());
				HashSet<Pair<String, String>> userPerms = config.getUserPerms()
						.get(user);
				if (userPerms == null || userPerms.isEmpty()) {
					continue;
				}

				ArrayList<Integer> resourceIndex = new ArrayList<Integer>();
				ArrayList<Integer> opIndex = new ArrayList<Integer>();
				for (Pair<String, String> perm : userPerms) {
					String op = perm.getFirst();
					String resource = perm.getSecond();
					for (int i = 0; i < ops.size(); i++) {
						if (op.equals(ops.get(i))) {
							opIndex.add(i);
							break;
						}
					}
					for (int i = 0; i < resources.size(); i++) {
						if (resource.equals(resources.get(i))) {
							resourceIndex.add(i);
							break;
						}
					}
				}

				Collections.sort(opIndex);
				Collections.sort(resourceIndex);

				String resultOp = ops.get(noiseOpDist.nextValue(opIndex));
				String resultRes = resources.get(noiseResDist
						.nextValue(resourceIndex));
				Triple<String, String, String> upTriple = new Triple<String, String, String>(
						user, resultOp, resultRes);
				if (!overAssignments.contains(upTriple) && config.getCoveredUP().contains(upTriple)) {
					Nunderassignments++;
					config.getCoveredUP().remove(upTriple);
					underAssignments.add(upTriple);
					config.getUserPerms()
							.get(upTriple.getFirst())
							.remove(new Pair<String, String>(upTriple
									.getSecond(), upTriple.getThird()));
					if (config.getPermUsers().get(
							new Pair<String, String>(upTriple.getSecond(),
									upTriple.getThird())) != null)
						config.getPermUsers()
								.get(new Pair<String, String>(upTriple
										.getSecond(), upTriple.getThird()))
								.remove(upTriple.getFirst());
				}
			}

			double maximumSimilarity_o = -1;
			double maximumSimilarity_u = -1;
			double maximumPolicySimilarity_o = -1;
			double maximumPolicySimilarity_u = -1;
			double maximumPolicySimilarity = -1;
			for (double tau : TAU) {
				System.out.println(tau);
				for (double alpha : ALPHA) {
					System.out.println(alpha);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							false, (int) tau, alpha, true);
					HashSet<Triple<String, String, String>> reportedNoise = new HashSet<Triple<String, String, String>>(
							ABACMiner.underAssignments);
					reportedNoise.addAll(ABACMiner.overAssignments);
					
					double currentPolicySimilarity = ABACMiner
							.nonsymSemanticSimilarityOfPolicies(
									ABACMiner.resultRules, config,
									originalUP, ABACMiner.overAssignments,
									ABACMiner.underAssignments);
					if (Double
							.compare(currentPolicySimilarity, maximumPolicySimilarity) > 0) {
						maximumPolicySimilarity = currentPolicySimilarity;
						bestTau_p = tau;
						bestTau_o = alpha;
					}
						
					double currentSimilarity_o = ABACMiner.jaccardSimilarity(
							overAssignments, ABACMiner.overAssignments);
					if (Double
							.compare(currentSimilarity_o, maximumSimilarity_o) > 0) {
						bestTau_o = tau;
						bestAlpha_o = alpha;
						maximumSimilarity_o = currentSimilarity_o;
						maximumPolicySimilarity_o = ABACMiner
								.nonsymSemanticSimilarityOfPolicies(
										ABACMiner.resultRules, config,
										originalUP, ABACMiner.overAssignments,
										ABACMiner.underAssignments);
//						if (maximumPolicySimilarity_o > maximumPolicySimilarity) {
//							maximumPolicySimilarity = maximumPolicySimilarity_o;
//						}
					}
					double currentSimilarity_u = ABACMiner.jaccardSimilarity(
							underAssignments, ABACMiner.underAssignments);
					

					System.out.println("Current underassignment similairy:"
							+ currentSimilarity_u);
//					System.out.println("Current syntactic similairy:"
//							+ ABACMiner
//							.symmetricSyntacticSimilarityOfPolicies(
//									Parser.config.getRuleList(),
//									ABACMiner.resultRules, Parser.config));

					if (Double
							.compare(currentSimilarity_u, maximumSimilarity_u) > 0) {
						bestTau_u = tau;
						bestAlpha_u = alpha;
						maximumSimilarity_u = currentSimilarity_u;
						maximumPolicySimilarity_u = ABACMiner
								.nonsymSemanticSimilarityOfPolicies(
										ABACMiner.resultRules, config,
										originalUP, ABACMiner.overAssignments,
										ABACMiner.underAssignments);
//						if (maximumPolicySimilarity_u > maximumPolicySimilarity) {
//							maximumPolicySimilarity = maximumPolicySimilarity_u;
//							// System.out.println("Current policy similairy:" +
//							// maximumPolicySimilarity);
//						}
					}
					if (Double.compare(maximumSimilarity_o, 1.0) == 0
							&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
						break;
					}
				}
				if (Double.compare(maximumSimilarity_o, 1.0) == 0
						&& Double.compare(maximumSimilarity_u, 1.0) == 0) {
					break;
				}
			}
			tausAndAlphas_o.add(j, new Pair<Double, Double>(bestTau_o,
					bestAlpha_o));
			maxSimilarities_o[j] = maximumSimilarity_o;
			maxPolicySimilarities_o[j] = maximumPolicySimilarity_o;
			tausAndAlphas_u.add(j, new Pair<Double, Double>(bestTau_u,
					bestAlpha_u));
			maxSimilarities_u[j] = maximumSimilarity_u;
			maxPolicySimilarities_u[j] = maximumPolicySimilarity_u;
			tausAndAlphas_p.add(j, new Pair<Double, Double>(bestTau_p,
					bestAlpha_p));
			maxPolicySimilarities[j] = maximumPolicySimilarity;
		}
		for (int i = 0; i < NUM_CASES; i++) {
			taus_o[i] = tausAndAlphas_o.get(i).getFirst();
			alphas_o[i] = tausAndAlphas_o.get(i).getSecond();
			taus_u[i] = tausAndAlphas_u.get(i).getFirst();
			alphas_u[i] = tausAndAlphas_u.get(i).getSecond();
			taus_p[i] = tausAndAlphas_p.get(i).getFirst();
			alphas_p[i] = tausAndAlphas_p.get(i).getSecond();
		}
		System.out.print(uNoiseRatio + " " + oNoiseRatio);
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_o),
				std.evaluate(taus_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_o),
				std.evaluate(alphas_o));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_o),
				std.evaluate(maxSimilarities_o));
		System.out.printf("%7.4f %7.4f ",
				doubleArrayAverage(maxPolicySimilarities_o),
				std.evaluate(maxPolicySimilarities_o));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_u),
				std.evaluate(taus_u));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_u),
				std.evaluate(alphas_u));
		System.out.printf("%7.2f %7.2f ",
				doubleArrayAverage(maxSimilarities_u),
				std.evaluate(maxSimilarities_u));
		System.out.printf("%7.4f %7.4f ",
				doubleArrayAverage(maxPolicySimilarities_u),
				std.evaluate(maxPolicySimilarities_u));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(taus_p),
				std.evaluate(taus_p));
		System.out.printf("%7.2f %7.2f ", doubleArrayAverage(alphas_p),
				std.evaluate(alphas_p));
		System.out.printf("%7.4f %7.4f ",
				doubleArrayAverage(maxPolicySimilarities),
				std.evaluate(maxPolicySimilarities));
		System.out.println();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Too few arguments!");
			System.exit(1);
		} else {
			if (args[0].length() != 2 || args[0].charAt(0) != '-') {
				System.err
						.println("The first argument specifies the excution mode, which should begin with '-' ");
				System.exit(1);
			}
			if (args[0].charAt(1) == 'u') {
				// System.out.println("Two kinds of noise.");
				// for(double noiseRatio : noiseRatios) {
				// OverAndUnderassignment(noiseRatio / 6, noiseRatio * 5 / 6);
				// }
				System.out.println("Three kinds of noise.");
				for (double noiseRatio : noiseRatios) {
					UPAndAttrNoise(noiseRatio / 7, noiseRatio * 5 / 7,
							noiseRatio / 7);
				}
			} else if (args[0].charAt(1) == 'c') {
				for (int i = 1; i < args.length; i++)
					for (double noiseRatio : noiseRatios) {
						OverAndUnderassignmentOnCaseStudy(noiseRatio / 6,
								noiseRatio * 5 / 6, args[i]);
					}
			} else if (args[0].charAt(1) == 'd') {
				for (double noiseRatio : noiseRatios) {
					String inputFile = "sample-policies-non-uniform-synthetic-attribute-data/projectmanagement/Set1/ProjectManagementCaseStudy_10.abac";
					nonUniformNoiseExperiment(noiseRatio / 7 /*0*/,
							noiseRatio * 6 / 7, inputFile);
				}
			}
		}
	}
}
