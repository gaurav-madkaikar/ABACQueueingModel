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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.dar.util.AttrValConjunct;
import edu.dar.util.Config;
import edu.dar.util.Pair;
import edu.dar.util.Parser;
import edu.dar.util.Rule;
import edu.dar.util.SyntheticPolicyCaseStudyGenerator;
import edu.dar.util.Time;
import edu.dar.util.Triple;
import edu.dar.util.ValueType;

public class DecreasingOverlap {
	public static int NUM_POVER = 5;

	public static final int NUMCASES = 50;
	public static final int MIN_NRULE = 10;
	public static final int MAX_NRULE = 50;
	public static final int STEP_LEN = 20;

	public static long startTime;
	public static long endTime;
	public static long totalTime;
	public static long duration;

	public static void main(String[] args) {
		System.out.println("Decreasing Overlap");
		String outputFile = "ran-case-studies/decreasing_overlap/output_withoutopt.csv";
		try {
			File file = new File(outputFile);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter fw = null;
			BufferedWriter writer = null;
			try {
				fw = new FileWriter(file);
				writer = new BufferedWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}
			writer.write("NRules, Noverlap,|U|mean,|U|stdv,|R|mean,|R|stdv,|O|mean,|O|stdv,"
					+ "|UP|mean,|UP|stdv,Compres.mean,Compres.stdv,Compres.95%,"
					+ "Syn.Sim.mean,Syn.Sim.stdv,Syn.Sim.95%,timemean,timestdv,time95%,"
					+ "|GenRules|mean,|GenRules|stdv,UPperRulemean,UPperRulestdv,densitymean,densitystdv");
			writer.write("\n");
		for (int numRules = MIN_NRULE; numRules <= MAX_NRULE; numRules += STEP_LEN) {
			System.out.println("Number of rules: " + numRules);
			StandardDeviation std = new StandardDeviation();
			double[][] numUsers = new double[NUM_POVER][NUMCASES];
			double[][] numResources = new double[NUM_POVER][NUMCASES];
			double[][] numOps = new double[NUM_POVER][NUMCASES];
			double[][] numCoveredUP = new double[NUM_POVER][NUMCASES];
			double[][] inputOverlap = new double[NUM_POVER][NUMCASES];
			double[][] outputOverlap = new double[NUM_POVER][NUMCASES];
			double[][] inputOverlapBetweenOverlappingRules = new double[NUM_POVER][NUMCASES];
			double[][] outputOverlapBetweenOverlappingRules = new double[NUM_POVER][NUMCASES];
			int[][] inputOverlapRulePairs = new int[NUM_POVER][NUMCASES];
			int[][] outputOverlapRulePairs = new int[NUM_POVER][NUMCASES];
			double[][] compressionFactor = new double[NUM_POVER][NUMCASES];
			double[][] syntacticSimilarities = new double[NUM_POVER][NUMCASES];
			double[][] avgInputConjuncts = new double[NUM_POVER][NUMCASES];
			double[][] avgOutputConjuncts = new double[NUM_POVER][NUMCASES];
			double[][] avgInputConstraints = new double[NUM_POVER][NUMCASES];
			double[][] avgOutputConstraints = new double[NUM_POVER][NUMCASES];
			double[][] avgRunningTime = new double[NUM_POVER][NUMCASES];
			double[][] avgNumSatUPTriples = new double[NUM_POVER][NUMCASES];
			double[][] numOutputRules = new double[NUM_POVER][NUMCASES];
			double[][] densities = new double[NUM_POVER][NUMCASES];
			for (int k = 0; k < NUMCASES; k++) {
				System.out.println("k = " + k);
				String inputFile = "ran-case-studies/overlap_" + numRules + "_"
						+ k + ".abac";
				Time.setStartTime();
				File f = new File(inputFile);
				if (!f.exists()) {
					while (true) {
						if (SyntheticPolicyCaseStudyGenerator
								.generateCaseStudy(inputFile, null, numRules,
										false, true, 2, 1, 0, 0, 0) != null) {
							break;
						} else {
							System.out.println("Regenerate Policy");
						}
					}
				}
				Parser.config = new Config();
				Parser.parseInputFile(inputFile);
				 ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
				 true, 0, 0.0, false);
//				ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true,
//						0, 0.0, false);
				Time.setElapsedTime();
				;
				avgRunningTime[0][k] = Time.getElapsedCPUTime();
				numUsers[0][k] = Parser.config.getUsers().size();
				numResources[0][k] = Parser.config.getResources().size();
				numOps[0][k] = Parser.config.getOps().size();

				numCoveredUP[0][k] = Parser.config.getCoveredUP().size();
				inputOverlap[0][k] = computeAverageOverlap(
						Parser.config.getRuleList(), Parser.config);
				outputOverlap[0][k] = computeAverageOverlap(
						ABACMiner.resultRules, Parser.config);

				inputOverlapBetweenOverlappingRules[0][k] = computeAverageOverlapBetweenOverlappingRules(
						Parser.config.getRuleList(), Parser.config);
				outputOverlapBetweenOverlappingRules[0][k] = computeAverageOverlapBetweenOverlappingRules(
						ABACMiner.resultRules, Parser.config);

				inputOverlapRulePairs[0][k] = computeNumOfOverlappingRulPairs(
						Parser.config.getRuleList(), Parser.config);
				outputOverlapRulePairs[0][k] = computeNumOfOverlappingRulPairs(
						ABACMiner.resultRules, Parser.config);

				numOutputRules[0][k] = ABACMiner.resultRules.size();
				int totalInputConjuncts = 0;

				for (Rule r : Parser.config.getRuleList()) {
					totalInputConjuncts += r.getUAE().size()
							+ r.getRAE().size();
				}
				avgInputConjuncts[0][k] = (double) totalInputConjuncts
						/ Parser.config.getRuleList().size();

				int totalOutputConjuncts = 0;
				for (Rule r : ABACMiner.resultRules) {
					totalOutputConjuncts += r.getUAE().size()
							+ r.getRAE().size();
				}
				avgOutputConjuncts[0][k] = (double) totalOutputConjuncts
						/ ABACMiner.resultRules.size();

				int totalInputConstraints = 0;
				for (Rule r : Parser.config.getRuleList()) {
					totalInputConstraints += r.getCon().size();
				}
				avgInputConstraints[0][k] = (double) totalInputConstraints
						/ Parser.config.getRuleList().size();

				int totalOutputConstraints = 0;
				for (Rule r : ABACMiner.resultRules) {
					totalOutputConstraints += r.getCon().size();
				}
				avgOutputConstraints[0][k] = (double) totalOutputConstraints
						/ ABACMiner.resultRules.size();

				int inputPolicySize = 0;
				for (Rule r : Parser.config.getRuleList()) {
					inputPolicySize += r.getRuleSize(Parser.config);
				}
				int outputPolicySize = 0;
				for (Rule r : ABACMiner.resultRules) {
					outputPolicySize += r.getRuleSize(Parser.config);
				}
				compressionFactor[0][k] = (double) inputPolicySize
						/ outputPolicySize;

				syntacticSimilarities[0][k] = ABACMiner
						.nonsymmetricSyntacticSimilarityPolicies(
								Parser.config.getRuleList(),
								ABACMiner.resultRules, Parser.config);
				int totalNumUPTriples = 0;
				for (Rule r : ABACMiner.resultRules) {
					totalNumUPTriples += ABACMiner.computeCoveredUPTriple(r,
							Parser.config).size();
				}
				double avgNumUPTriples = (double) totalNumUPTriples
						/ ABACMiner.resultRules.size();
				avgNumSatUPTriples[0][k] = avgNumUPTriples;
				densities[0][k] = (double) Parser.config.getCoveredUP().size()
						/ (Parser.config.getUsers().size() * Parser.config
								.numDistintPerms());
				Random rand = new Random(System.currentTimeMillis());

				for (int i = 1; i <= NUM_POVER - 1; i++) {

					System.out.println("i = " + i);
					// System.out.println("i = " + i);
					// HashSet<Pair<Rule, Rule>> overlapRulePairs = new
					// HashSet<Pair<Rule, Rule>>();
					// for (int j = 0; j < config.getRuleList().size(); j++) {
					// Rule r1 = config.getRuleList().get(j);
					// for (int l = j + 1; l < config.getRuleList().size(); l++)
					// {
					// Rule r2 = config.getRuleList().get(l);
					// if (ABACMiner.computeRuleOverlap(r1, r2, config)
					// .size() > 0) {
					// overlapRulePairs.add(new Pair<Rule, Rule>(r1,
					// r2));
					// }
					// }
					// }
					//
					// for (Pair<Rule, Rule> p : overlapRulePairs) {
					// Rule r = p.getSecond();
					// if (rand.nextDouble() <= 0.5) {
					// r = p.getFirst();
					// }
					//
					// if (rand.nextDouble() <= 0.5 && r.getUAE().size() > 0) {
					// for (int l = 0; l < 10; l++) {
					// int index = rand.nextInt(r.getUAE().size());
					// AttrValConjunct c = r.getUAE().get(index);
					// if (config.getUserAttrSet().get(c.getLHS())
					// .getvType() == ValueType.Single
					// && c.getRHS().size() > 1) {
					// String v = SyntheticPolicyCaseStudyGenerator
					// .randomElement(c.getRHS());
					// c.getRHS().remove(v);
					// r.setChanged(true);
					// r.setUaeChanged(true);
					// r.setRaeChanged(true);
					// break;
					// } else if (config.getUserAttrSet()
					// .get(c.getLHS()).getvType() == ValueType.Set
					// && c.getRHSet().size() > 1) {
					// HashSet<String> v = SyntheticPolicyCaseStudyGenerator
					// .randomSet(c.getRHSet());
					// c.getRHSet().remove(v);
					// r.setChanged(true);
					// r.setUaeChanged(true);
					// r.setRaeChanged(true);
					// break;
					// }
					// }
					// } else {
					// if (r.getRAE().size() == 0) {
					// continue;
					// }
					// for (int l = 0; l < 10; l++) {
					// int index = rand.nextInt(r.getRAE().size());
					// AttrValConjunct c = r.getRAE().get(index);
					// if (config.getResourceAttrSet().get(c.getLHS())
					// .getvType() == ValueType.Single
					// && c.getRHS().size() > 1) {
					// String v = SyntheticPolicyCaseStudyGenerator
					// .randomElement(c.getRHS());
					// c.getRHS().remove(v);
					// r.setChanged(true);
					// r.setUaeChanged(true);
					// r.setRaeChanged(true);
					// break;
					// } else if (config.getResourceAttrSet()
					// .get(c.getLHS()).getvType() == ValueType.Set
					// && c.getRHSet().size() > 1) {
					// HashSet<String> v = SyntheticPolicyCaseStudyGenerator
					// .randomSet(c.getRHSet());
					// c.getRHSet().remove(v);
					// r.setChanged(true);
					// r.setUaeChanged(true);
					// r.setRaeChanged(true);
					// break;
					// }
					// }
					// }
					// }

					// for (Rule rule : config.getRuleList()) {
					// if (rule.getCon().size() > 0) {
					// int conIndex = rand.nextInt(rule.getCon().size());
					// rule.getCon().remove(conIndex);
					// }
					// rule.setChanged(true);
					// rule.setUaeChanged(true);
					// rule.setRaeChanged(true);
					// }
					// Construct UP relations from rules
					// config.getCoveredUP().clear();
					// config.getResourceUsers().clear();
					// config.getUserResources().clear();
					// config.getUserPerms().clear();
					// config.getPermUsers().clear();
					// for (Rule r : config.getRuleList()) {
					// HashSet<String> satUsers = new HashSet<String>();
					// HashSet<String> satPerms = new HashSet<String>();
					// for (String u : config.getUsers()) {
					// if (Parser.satisfyingRule(u, r, config, true)) {
					// satUsers.add(u);
					// }
					// }
					// for (String p : config.getResources()) {
					// if (Parser.satisfyingRule(p, r, config, false)) {
					// satPerms.add(p);
					// }
					// }
					// for (String u : satUsers) {
					// for (String p : satPerms) {
					// if (Parser.satisfyingRuleConstraints(u, p, r,
					// config)) {
					// if (!config.getUserResources().containsKey(
					// u)) {
					// config.getUserResources().put(u,
					// new HashSet<String>());
					// }
					// config.getUserResources().get(u).add(p);
					// if (!config.getResourceUsers().containsKey(
					// p)) {
					// config.getResourceUsers().put(p,
					// new HashSet<String>());
					// }
					// config.getResourceUsers().get(p).add(u);
					// if (!config.getUserPerms().containsKey(u)) {
					// config.getUserPerms()
					// .put(u,
					// new HashSet<Pair<String, String>>());
					// }
					// for (String op : r.getOps()) {
					// config.getUserPerms()
					// .get(u)
					// .add(new Pair<String, String>(
					// op, p));
					// Pair<String, String> perm = new Pair<String, String>(
					// op, p);
					// if (!config.getPermUsers().containsKey(
					// perm)) {
					// config.getPermUsers().put(perm,
					// new HashSet<String>());
					// }
					// config.getPermUsers().get(perm).add(u);
					// config.getCoveredUP()
					// .add(new Triple<String, String, String>(
					// u, op, p));
					// }
					// }
					// }
					// }
					// }
					Time.setStartTime();
					System.out.println("Generating");
					SyntheticPolicyCaseStudyGenerator.generateCaseStudy(
							inputFile, null, numRules, false, true, 2, 1, 0, 0,
							0.25 * i);
					Parser.config = new Config();
					System.out.println("Parsing");
					Parser.parseInputFile(inputFile);
					System.out.println("Mining...");
					System.out.println("k=" + k);
					 ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
					 true, 0, 0.0, false);
//					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
//							true, 0, 0.0, false);
					Time.setElapsedTime();
					avgRunningTime[i][k] = Time.getElapsedCPUTime();
					Config config = Parser.config;
					numUsers[i][k] = Parser.config.getUsers().size();
					numResources[i][k] = Parser.config.getResources().size();
					numOps[i][k] = Parser.config.getOps().size();

					numCoveredUP[i][k] = Parser.config.getCoveredUP().size();
					inputOverlap[i][k] = computeAverageOverlap(
							Parser.config.getRuleList(), Parser.config);
					outputOverlap[i][k] = computeAverageOverlap(
							ABACMiner.resultRules, Parser.config);

					totalInputConjuncts = 0;

					inputOverlapBetweenOverlappingRules[i][k] = computeAverageOverlapBetweenOverlappingRules(
							Parser.config.getRuleList(), Parser.config);
					outputOverlapBetweenOverlappingRules[i][k] = computeAverageOverlapBetweenOverlappingRules(
							ABACMiner.resultRules, Parser.config);

					inputOverlapRulePairs[i][k] = computeNumOfOverlappingRulPairs(
							Parser.config.getRuleList(), Parser.config);
					outputOverlapRulePairs[i][k] = computeNumOfOverlappingRulPairs(
							ABACMiner.resultRules, Parser.config);

					numOutputRules[i][k] = ABACMiner.resultRules.size();

					for (Rule r : Parser.config.getRuleList()) {
						totalInputConjuncts += r.getUAE().size()
								+ r.getRAE().size();
					}
					avgInputConjuncts[i][k] = (double) totalInputConjuncts
							/ Parser.config.getRuleList().size();

					totalOutputConjuncts = 0;
					for (Rule r : ABACMiner.resultRules) {
						totalOutputConjuncts += r.getUAE().size()
								+ r.getRAE().size();
					}
					avgOutputConjuncts[i][k] = (double) totalOutputConjuncts
							/ ABACMiner.resultRules.size();

					totalInputConstraints = 0;
					for (Rule r : Parser.config.getRuleList()) {
						totalInputConstraints += r.getCon().size();
					}
					avgInputConstraints[i][k] = (double) totalInputConstraints
							/ Parser.config.getRuleList().size();

					totalOutputConstraints = 0;
					for (Rule r : ABACMiner.resultRules) {
						totalOutputConstraints += r.getCon().size();
					}
					avgOutputConstraints[i][k] = (double) totalOutputConstraints
							/ ABACMiner.resultRules.size();

					inputPolicySize = 0;
					for (Rule r : Parser.config.getRuleList()) {
						inputPolicySize += r.getRuleSize(config);
					}
					outputPolicySize = 0;
					for (Rule r : ABACMiner.resultRules) {
						outputPolicySize += r.getRuleSize(config);
					}
					compressionFactor[i][k] = (double) inputPolicySize
							/ outputPolicySize;
					syntacticSimilarities[i][k] = ABACMiner
							.nonsymmetricSyntacticSimilarityPolicies(
									Parser.config.getRuleList(),
									ABACMiner.resultRules, Parser.config);
					for (Rule r : ABACMiner.resultRules) {
						totalNumUPTriples += ABACMiner.computeCoveredUPTriple(
								r, Parser.config).size();
					}
					avgNumUPTriples = (double) totalNumUPTriples
							/ ABACMiner.resultRules.size();
					avgNumSatUPTriples[i][k] = avgNumUPTriples;
					densities[i][k] = (double) Parser.config.getCoveredUP()
							.size()
							/ (Parser.config.getUsers().size() * Parser.config
									.numDistintPerms());
				}
			}

			for (int k = NUM_POVER - 1; k >= 0; k--) {
				System.out.printf("%7.1f %7.1f ", doubleArrayAverage(numUsers[k]),
						std.evaluate(numUsers[k]));
				writer.write(numRules + "," + 25 * k + ",");
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numUsers[k]), std.evaluate(numUsers[k])));
				System.out.printf("%7.1f %7.1f ",
						doubleArrayAverage(numResources[k]),
						std.evaluate(numResources[k]));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numResources[k]),
						std.evaluate(numResources[k])));
				System.out.printf("%7.1f %7.1f ", doubleArrayAverage(numOps[k]),
						std.evaluate(numOps[k]));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numOps[k]), std.evaluate(numOps[k])));
				System.out.printf("%7.1f %7.1f ",
						doubleArrayAverage(numCoveredUP[k]),
						std.evaluate(numCoveredUP[k]));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numCoveredUP[k]),
						std.evaluate(numCoveredUP[k])));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(compressionFactor[k]),
						std.evaluate(compressionFactor[k]));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(compressionFactor[k]),
						std.evaluate(compressionFactor[k]), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(syntacticSimilarities[k]),
						std.evaluate(syntacticSimilarities[k]));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(syntacticSimilarities[k]),
						std.evaluate(syntacticSimilarities[k]), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(avgRunningTime[k]),
						std.evaluate(avgRunningTime[k]));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(avgRunningTime[k]),
						std.evaluate(avgRunningTime[k]), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(numOutputRules[k]),
						std.evaluate(numOutputRules[k]));
				writer.write(String.format("%.2f,%.2f,",
						doubleArrayAverage(numOutputRules[k]),
						std.evaluate(numOutputRules[k])));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(avgNumSatUPTriples[k]),
						std.evaluate(avgNumSatUPTriples[k]));
				writer.write(String.format("%.2f,%.2f,",
						doubleArrayAverage(avgNumSatUPTriples[k]),
						std.evaluate(avgNumSatUPTriples[k])));
				System.out.printf("%.5f %.5f ", doubleArrayAverage(densities[k]),
						std.evaluate(densities[k]));
				writer.write(String.format("%.5f,%.5f\n",
						doubleArrayAverage(densities[k]), std.evaluate(densities[k])));
				System.out.println();
			}
		}
		writer.flush();
		writer.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	}

	public static double intArrayAverage(int[] a) {
		int sum = 0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return (double) sum / a.length;
	}

	public static double doubleArrayAverage(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum / a.length;
	}

	public static double computeAverageOverlap(ArrayList<Rule> rules,
			Config config) {
		if (rules.size() <= 1) {
			return 0.0;
		}
		int totalOverlap = 0;
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				totalOverlap += ABACMiner.computeRuleOverlap(rules.get(i),
						rules.get(j), config).size();
			}
		}
		return (double) totalOverlap / (rules.size() * (rules.size() - 1));
	}

	public static double computeAverageOverlapBetweenOverlappingRules(
			ArrayList<Rule> rules, Config config) {
		int totalNumOverlappingRules = 0;
		if (rules.size() <= 1) {
			return 0.0;
		}
		int totalOverlap = 0;
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				int overlap = ABACMiner.computeRuleOverlap(rules.get(i),
						rules.get(j), config).size();
				if (overlap != 0) {
					totalNumOverlappingRules++;
				}
				totalOverlap += overlap;
			}
		}

		if (totalNumOverlappingRules == 0) {
			return 0.0;
		}
		return (double) totalOverlap / totalNumOverlappingRules;
	}

	public static int computeNumOfOverlappingRulPairs(ArrayList<Rule> rules,
			Config config) {
		int totalNumOverlappingRules = 0;
		if (rules.size() <= 1) {
			return 0;
		}
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				if (ABACMiner.computeRuleOverlap(rules.get(i), rules.get(j),
						config).size() > 0) {
					totalNumOverlappingRules++;
				}
			}
		}
		return totalNumOverlappingRules;
	}
}
