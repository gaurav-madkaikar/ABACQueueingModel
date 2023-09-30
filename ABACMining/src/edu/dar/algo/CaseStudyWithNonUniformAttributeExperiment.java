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

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.dar.util.Config;
import edu.dar.util.Parser;
import edu.dar.util.Rule;
import edu.dar.util.Time;

public class CaseStudyWithNonUniformAttributeExperiment {
	public static double doubleArrayAverage(double[] a) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum += a[i];
		}
		return sum / a.length;
	}

	public static void main(String[] args) {
		final int NUMCASES = 20;
		int inputSize = Integer.parseInt(args[0]);
		String caseStudyType = args[1];
		boolean useOptimization = Boolean.parseBoolean(args[2]);
		String outputFile = null;
		if (caseStudyType.equals("university")) {
			outputFile = "sample-policies-non-uniform-synthetic-attribute-data/university/university_new.csv";
		} else if (caseStudyType.equals("healthcare")) {
			outputFile = "sample-policies-non-uniform-synthetic-attribute-data/healthcare/healthcare_new.csv";
		} else {
			outputFile = "sample-policies-non-uniform-synthetic-attribute-data/projectmanagement/projectmanagement_new.csv";
		}
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
			writer.write("N,|U|mean,|U|stdv,|R|mean,|R|stdv,|O|mean,|O|stdv,"
					+ "|UP|mean,|UP|stdv,Compres.mean,Compres.stdv,Compres.95%,"
					+ "Syn.Sim.mean,Syn.Sim.stdv,Syn.Sim.95%,timemean,timestdv,time95%,"
					+ "|GenRules|mean,|GenRules|stdv,UPperRulemean,UPperRulestdv,densitymean,densitystdv");
			writer.write("\n");
			for (int dept = 1; dept <= inputSize; dept++) {
				System.out.println("input size:" + dept);
				StandardDeviation std = new StandardDeviation();
				double[] numUsers = new double[NUMCASES];
				double[] numResources = new double[NUMCASES];
				double[] numOps = new double[NUMCASES];
				double[] numCoveredUP = new double[NUMCASES];
				double[] compressionFactor = new double[NUMCASES];
				double[] syntacticSimilarities = new double[NUMCASES];
				double[] avgRunningTime = new double[NUMCASES];
				double[] avgNumSatUPTriples = new double[NUMCASES];
				double[] numOutputRules = new double[NUMCASES];
				double[] densities = new double[NUMCASES];

				for (int set = 1; set <= NUMCASES; set++) {
					String inputFile = null;
					if (caseStudyType.equals("university")) {
						inputFile = "sample-policies-non-uniform-synthetic-attribute-data/university/Set"
								+ set
								+ "/UniversityCaseStudy_"
								+ dept
								+ ".abac";
					} else if (caseStudyType.equals("healthcare")) {
						inputFile = "sample-policies-non-uniform-synthetic-attribute-data/healthcare/Set"
								+ set
								+ "/HealthcareCaseStudy_"
								+ dept
								+ ".abac";
					} else {
						inputFile = "sample-policies-non-uniform-synthetic-attribute-data/projectmanagement/Set"
								+ set
								+ "/ProjectManagementCaseStudy_"
								+ dept
								+ ".abac";
					}
					System.out.println(inputFile);
					Time.setStartTime();
					Parser.config = new Config();
					Parser.parseInputFile(inputFile);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, 0, 0.0, useOptimization);
					Time.setElapsedTime();
					avgRunningTime[set - 1] = Time.getElapsedCPUTime();
					numUsers[set - 1] = Parser.config.getUsers().size();
					numResources[set - 1] = Parser.config.getResources().size();
					numOps[set - 1] = Parser.config.getOps().size();

					numCoveredUP[set - 1] = Parser.config.getCoveredUP().size();
					numOutputRules[set - 1] = ABACMiner.resultRules.size();
					densities[set - 1] = (double) Parser.config.getCoveredUP()
							.size()
							/ (Parser.config.getUsers().size() * Parser.config
									.numDistintPerms());
					int inputPolicySize = 0;
					for (Rule r : Parser.config.getRuleList()) {
						inputPolicySize += r.getSize();
					}
					int outputPolicySize = 0;
					for (Rule r : ABACMiner.resultRules) {
						outputPolicySize += r.getSize();
					}
					compressionFactor[set - 1] = (double) inputPolicySize
							/ outputPolicySize;

					syntacticSimilarities[set - 1] = ABACMiner
							.symmetricSyntacticSimilarityOfPolicies(
									Parser.config.getRuleList(),
									ABACMiner.resultRules, Parser.config);
					int totalNumUPTriples = 0;
					for (Rule r : ABACMiner.resultRules) {
						totalNumUPTriples += ABACMiner.computeCoveredUPTriple(
								r, Parser.config).size();
					}
					double avgNumUPTriples = (double) totalNumUPTriples
							/ ABACMiner.resultRules.size();
					avgNumSatUPTriples[set - 1] = avgNumUPTriples;
				}
				System.out.printf("%7.1f %7.1f ", doubleArrayAverage(numUsers),
						std.evaluate(numUsers));
				writer.write(dept + ",");
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numUsers), std.evaluate(numUsers)));
				System.out.printf("%7.1f %7.1f ",
						doubleArrayAverage(numResources),
						std.evaluate(numResources));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numResources),
						std.evaluate(numResources)));
				System.out.printf("%7.1f %7.1f ", doubleArrayAverage(numOps),
						std.evaluate(numOps));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numOps), std.evaluate(numOps)));

				System.out.printf("%7.1f %7.1f ",
						doubleArrayAverage(numCoveredUP),
						std.evaluate(numCoveredUP));
				writer.write(String.format("%7.1f,%7.1f,",
						doubleArrayAverage(numCoveredUP),
						std.evaluate(numCoveredUP)));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(compressionFactor),
						std.evaluate(compressionFactor));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(compressionFactor),
						std.evaluate(compressionFactor), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(syntacticSimilarities),
						std.evaluate(syntacticSimilarities));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(syntacticSimilarities),
						std.evaluate(syntacticSimilarities), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(avgRunningTime),
						std.evaluate(avgRunningTime));
				writer.write(String.format("%.2f,%.2f,%.2f,",
						doubleArrayAverage(avgRunningTime),
						std.evaluate(avgRunningTime), 0.0));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(numOutputRules),
						std.evaluate(numOutputRules));
				writer.write(String.format("%.2f,%.2f,",
						doubleArrayAverage(numOutputRules),
						std.evaluate(numOutputRules)));
				System.out.printf("%.2f %.2f ",
						doubleArrayAverage(avgNumSatUPTriples),
						std.evaluate(avgNumSatUPTriples));
				writer.write(String.format("%.2f,%.2f,",
						doubleArrayAverage(avgNumSatUPTriples),
						std.evaluate(avgNumSatUPTriples)));
				System.out.printf("%.5f %.5f ", doubleArrayAverage(densities),
						std.evaluate(densities));
				writer.write(String.format("%.5f,%.5f,",
						doubleArrayAverage(densities), std.evaluate(densities)));
				System.out.println();
				writer.write("\n");

			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
