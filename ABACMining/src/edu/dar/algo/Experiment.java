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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

import edu.dar.util.CaseStudyGenerator;
import edu.dar.util.Config;
import edu.dar.util.NonUniformCaseStudyGenerator;
import edu.dar.util.Pair;
import edu.dar.util.Parser;
import edu.dar.util.ProgolTranslator;
import edu.dar.util.Rule;
import edu.dar.util.SyntheticPolicyCaseStudyGenerator;
import edu.dar.util.Triple;

public class Experiment {

	public static final String VERBOSE_OPTION = "-verbose";

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

	public static HashSet<Pair<String, String>> permissionDifference(
			ArrayList<Rule> rules, Config config, String user) {
		HashSet<Pair<String, String>> oriPerms = new HashSet<Pair<String, String>>(
				config.getUserPerms().get(user));
		HashSet<Pair<String, String>> newPerms = new HashSet<Pair<String, String>>();
		HashSet<Pair<String, String>> difference = new HashSet<Pair<String, String>>();

		for (Rule r : rules) {
			if (!Parser.satisfyingRule(user, r, config, true)) {
				continue;
			}
			HashSet<String> satResources = new HashSet<String>();
			for (String resource : config.getResources()) {
				if (Parser.satisfyingRule(resource, r, config, false)) {
					satResources.add(resource);
				}
			}
			for (String resource : satResources) {
				if (r.getCon().isEmpty()
						|| Parser.satisfyingRuleConstraints(user, resource, r,
								config)) {

					for (String op : r.getOps()) {
						Pair<String, String> perm = new Pair<String, String>(
								op, resource);
						newPerms.add(perm);
					}

				}
			}
		}

		oriPerms.removeAll(newPerms);
		newPerms.removeAll(config.getUserPerms().get(user));

		difference = newPerms;
		difference.addAll(oriPerms);
		return difference;
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
			if (args[0].charAt(1) == 'c') {
				if (args.length < 2) {
					System.err
							.println("One or more arguments needed for -c mode to calculate atomic values count of input files");
					System.exit(1);
				}
		
				File outputFile = new File("atomic_values_count_report.txt");
				
				/* Done to Truncate the File*/
				try {
					BufferedWriter output = new BufferedWriter(new FileWriter(outputFile));
					output.write("");
					output.close();
				} catch ( Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			
				/* Loops over the inputFileList, parses the atomic values and dumps them into the report file*/
				for ( int i = 1 ; i < args.length; i++ ) {
					/* Parses the input ABAC file and add the atomic values to the atomicValues Hashset*/
					CountConst.parseAtomicValues(args[i]);
					
					/*Dumps the atomicValues in the output report file*/
					CountConst.dumpAtomicValues(args[i], outputFile);
					
					/*Clears the Hash set of atomic values for new file to be processed*/
					CountConst.clearAtomicValues();
				}
			}
			if (args[0].charAt(1) == 'w') {
				if (args.length < 2) {
					System.err
							.println("One more argument needed for -w mode. 1. inputFile(String)");
					System.exit(1);
				}
				try {
					BufferedReader br = new BufferedReader(new FileReader("../"
							+ args[1]));
					String line = null;
					ArrayList<String> progolOutput = new ArrayList<String>();
					while ((line = br.readLine()) != null) {
						if (!line.trim().isEmpty()) {
							progolOutput.add(line);
						}
					}
					br.close();

					int start = -1, end = -1;

					for (int i = progolOutput.size() - 1; i >= 0; i--) {
						line = progolOutput.get(i);
						if (!line.startsWith("[") && !line.startsWith("Mem")
								&& end == -1) {
							end = i;
							continue;
						}
						if (line.startsWith("[") && start == -1 && end != -1) {
							start = i + 1;
							break;
						}
					}
					// System.out.println(progolOutput.get(start));
					// System.out.println(progolOutput.get(end));
					ArrayList<String> rules = new ArrayList<String>();
					for (int i = start; i <= end; i++) {
						String rule = progolOutput.get(i);
						while (i + 1 <= end
								&& !progolOutput.get(i + 1).startsWith("up")) {
							rule += progolOutput.get(i + 1).trim();
							i++;
						}
						rules.add(rule);
					}
					// for (String rule : rules) {
					// System.out.println(rule);
					// }
					int w1 = 1, w2 = 1, w3 = 1, w4 = 1;
					int totalWSC = 0;
					for (String rule : rules) {
						if (!rule.contains(":-")) {
							// it is a fact
							totalWSC = w1 + w2 + w3;
						} else {
							// it is a rule, with 2 variables and 1 constant in
							// the head
							totalWSC += w3;
							String premises = rule
									.substring(rule.indexOf(":-") + 2);
							String[] premiseArray = premises.split(", ");
							for (String premise : premiseArray) {
								// System.out.println(premise);
								if (premise.contains("_equals_")
										|| premise.contains("_superset_")
										|| premise.contains("_contains_")) {
									totalWSC += w4;
								} else if (premise.contains("R")) {
									totalWSC += w1;
								} else {
									totalWSC += w2;
								}
							}
						}
					}

					System.out.println(totalWSC);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (args[0].charAt(1) == 'g') {
				// Generalization Experiment
				if (args.length < 2) {
					System.err
							.println("Two more arguments needed for -g mode. 1. inputFile(String) 2. outputFile(String) 3. fraction(Double)");
					System.exit(1);
				}
				try {
					StandardDeviation std = new StandardDeviation();
					for (int k = 1; k < args.length; k++) {
						String inputFile = args[k];
						String outputFile = args[k] + ".output";
						File file = new File(outputFile);
						// if file doesnt exists, then create it
						if (!file.exists()) {
							file.createNewFile();
						}
						FileWriter fw = new FileWriter(
								file.getAbsoluteFile());
						BufferedWriter bw = new BufferedWriter(fw);
						for (double fraction = 0.05; fraction <= 0.50; fraction += 0.05) {	
							bw.write("\n Fraction " + fraction + "\n");
							// System.out.println(inputFile);
							double userFraction = fraction;
							double resourceFraction = fraction;
							System.out.println(fraction);
							double[] generalizationErrors = new double[10];
							for (int i = 1; i <= 10; i++) {
								Parser.config = new Config();
								Parser.parseInputFile(inputFile);
								int numUsers = (int) (Parser.config.getUsers()
										.size() * userFraction);
								int numResources = (int) (Parser.config
										.getResources().size() * resourceFraction);
								
								//System.out.println("Total Users: " + Parser.config.getUsers().size());
								//System.out.println("Total Resources: " + Parser.config.getResources().size());								
								//System.out.println(Parser.config.getCoveredUP().size());

								HashSet<String> userSubset = new HashSet<String>();
								HashSet<String> removedUsers = new HashSet<String>();
								// userSubset contains users that will be
								// considered
								while (userSubset.size() < numUsers) {
									userSubset.add(randomElement(Parser.config
											.getUsers()));
								}
								removedUsers = new HashSet<String>(
										Parser.config.getUsers());
								removedUsers.removeAll(userSubset);

								HashSet<String> resourceSubset = new HashSet<String>();
								HashSet<String> removedResources = new HashSet<String>();
//								for (Triple<String, String, String> up : Parser.config
//										.getCoveredUP()) {
//									if (userSubset.contains(up.getFirst())) {
//										resourceSubset.add(up.getThird());
//									}
//								}
								for (String user : userSubset) {
									resourceSubset.addAll(Parser.config
											.getUserResources().get(user));
								}

								// resourceSubset contains resources that will
								// be
								// considered
								while (resourceSubset.size() > numResources) {
									resourceSubset
											.remove(randomElement(resourceSubset));
								}
								removedResources = new HashSet<String>(
										Parser.config.getResources());
								removedResources.removeAll(resourceSubset);
								
								
								//System.out.println((double) resourceSubset
								//		.size()
								//		/ Parser.config.getResources().size());
								HashSet<Triple<String, String, String>> removedUPSet = new HashSet<Triple<String, String, String>>();

								for (Triple<String, String, String> up : Parser.config
										.getCoveredUP()) {
									if (!userSubset.contains(up.getFirst())
											|| !resourceSubset.contains(up
													.getThird())) {
										removedUPSet.add(up);
									}
								}

								Parser.config.getUsers().retainAll(userSubset);
								Parser.config.getResources().retainAll(
										resourceSubset);
								Parser.config.getCoveredUP().removeAll(
										removedUPSet);
								//System.out.println("Used Users: " + Parser.config.getUsers().size());
								//System.out.println("Used Resources: " + Parser.config.getResources().size());
								//System.out.println("UP fraction: " + Parser.config.getCoveredUP());
								ABACMiner.mineABACPolicy(Parser.config, false,
										0, false, true, 0, 0.0, false);

								double generalizationError = 0.0;
								Parser.config.getResources().addAll(
										removedResources);
								Parser.config.getUsers().addAll(removedUsers);

								HashSet<Triple<String, String, String>> S1 = new HashSet<Triple<String, String, String>>();
								HashSet<Triple<String, String, String>> S2 = new HashSet<Triple<String, String, String>>();
								
								
								System.out.println("Original Rules.");
								int count = 1;
								for (Rule r : Parser.config.getRuleList()) {
									System.out.println(count++ + "." + r);
									for (String user : removedUsers) {
										if (!Parser.satisfyingRule(user, r,
												Parser.config, true)) {
											continue;
										}
										HashSet<String> satResources = new HashSet<String>();
										for (String resource : removedResources) {
											if (Parser.satisfyingRule(resource,
													r, Parser.config, false)) {
												satResources.add(resource);
											}
										}
										for (String resource : satResources) {
											if (r.getCon().isEmpty()
													|| Parser
															.satisfyingRuleConstraints(
																	user,
																	resource,
																	r,
																	Parser.config)) {

												for (String op : r.getOps()) {
													Triple<String, String, String> upTriple = new Triple<String, String, String>(
															user, op, resource);
													S1.add(upTriple);
												}

											}
										}
									}
								}

								System.out.println("Mined Rules.");
								count = 1;
								for (Rule r : ABACMiner.resultRules) {
									System.out.println(count++ + "." + r);
									for (String user : removedUsers) {
										if (!Parser.satisfyingRule(user, r,
												Parser.config, true)) {
											continue;
										}
										HashSet<String> satResources = new HashSet<String>();
										for (String resource : removedResources) {
											if (Parser.satisfyingRule(resource,
													r, Parser.config, false)) {
												satResources.add(resource);
											}
										}
										for (String resource : satResources) {
											if (r.getCon().isEmpty()
													|| Parser
															.satisfyingRuleConstraints(
																	user,
																	resource,
																	r,
																	Parser.config)) {

												for (String op : r.getOps()) {
													Triple<String, String, String> upTriple = new Triple<String, String, String>(
															user, op, resource);
													S2.add(upTriple);
												}
											}
										}
									}
								}
								//System.out.println("S1:" + S1.size() + " " + S1);
								//System.out.println("S2:" + S2.size() + " " + S2);
								HashSet<Triple<String, String, String>> D1 = new HashSet<Triple<String, String, String>>(
										S1);
								D1.removeAll(S2);
								//System.out.println("D1:" + D1.size() + " " + D1);
								
								HashSet<Triple<String, String, String>> D2 = new HashSet<Triple<String, String, String>>(
										S2);
								D2.removeAll(S1);
								//System.out.println("D2:" + D2.size() + " " + D2);
								D1.addAll(D2);
								//System.out.println("D1:" + D1.size() + " " + D1);
								generalizationError = (double) D1.size() / S1.size();
								

								// for (String user : removedUsers) {
								// HashSet<Pair<String, String>> difference =
								// permissionDifference(
								// ABACMiner.resultRules, Parser.config, user);
								// if (difference.isEmpty()) {
								// generalizationError += 0;
								// } else {
								// generalizationError += difference.size()
								// / Parser.config.getUserPerms()
								// .get(user).size();
								// }
								// }
								// generalizationError /= removedUsers.size();
								System.out.println("The " + i + "th run \n");
								System.out.println("Average generalization error is: "
										+ generalizationError + "\n");
								bw.write(generalizationError + "\n");
								generalizationErrors[i - 1] = generalizationError;
							}
							bw.write("\n Overall Average Error is: " + NoiseExperiment
									.doubleArrayAverage(generalizationErrors));
							bw.write("\n Standard Deviation is: " + std.evaluate(generalizationErrors) + "\n");
							System.out.print(NoiseExperiment
									.doubleArrayAverage(generalizationErrors) + " ");
							System.out.println(std.evaluate(generalizationErrors));
							if (Double.compare(0.0, NoiseExperiment
									.doubleArrayAverage(generalizationErrors)) == 0) {
										break;
									}
						}
						bw.close();
					}
				} catch (NumberFormatException nfe) {
					System.err
							.println("The second argument is a not a valid double value.");
					System.exit(1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (args[0].charAt(1) == 'p') {
				// translate .abac file to .pl file
				if (args.length < 3) {
					System.err
							.println("Two more arguments needed for -p mode. 1. inputFile 2. outputFile");
					System.exit(1);
				}
				Parser.parseInputFile("../" + args[1]);
				boolean attribVar = false;
				if (args.length >= 4) {
					if (args[3].equals("-attribVar")) {
						attribVar = true;
					}
				}
				ProgolTranslator.translateABACToProgol(Parser.config, "../"
						+ args[2], attribVar);
			}
			if (args[0].charAt(1) == 'r') {
				// Generate case studies with synthetic attribtue data
				if (args.length < 3) {
					System.err
							.println("Two arguments are required for -r mode. 1. case-study type(university, healthcare, projectmanagement) 2. outputFile");
					System.exit(1);
				}
				CaseStudyGenerator c = CaseStudyGenerator
						.createGenerator(args[1]);

				String outputFile = "../" + args[2];

				int N = 2;
				if (args.length >= 4) {
					N = Integer.parseInt(args[3]);
				}

				int Nir = 5;
				if (args.length >= 5) {
					Nir = Integer.parseInt(args[4]);
				}

				int NdomainSize = 30;
				if (args.length >= 6) {
					NdomainSize = Integer.parseInt(args[5]);
				}

				c.generateCaseStudy(outputFile, N, Nir, NdomainSize);
			}
			if (args[0].charAt(1) == 't') {
				// Generate case studies with synthetic attribtue data
				if (args.length < 3) {
					System.err
							.println("Two arguments are required for -r mode. 1. case-study type(university, healthcare, projectmanagement) 2. outputFolder");
					System.exit(1);
				}
				NonUniformCaseStudyGenerator c = NonUniformCaseStudyGenerator
						.createGenerator(args[1]);

				//String outputFile = "../" + args[2];
				String outputFolder = args[2];

				int N = 2;
				if (args.length >= 4) {
					N = Integer.parseInt(args[3]);
				}

				int Nir = 0;
				if (args.length >= 5) {
					Nir = Integer.parseInt(args[4]);
				}

				int NdomainSize = 0;
				if (args.length >= 6) {
					NdomainSize = Integer.parseInt(args[5]);
				}

				c.generateCaseStudy(outputFolder, N, Nir, NdomainSize);
			}
			if (args[0].charAt(1) == 's') {
				// generate synthetic policy
				if (args.length < 3) {
					System.err
							.println("Two arguments are required for -s mode. 1. outputFile 2. Nrule");
					System.exit(1);
				}

				String outputFile = "../" + args[1];
				int Nrule = Integer.parseInt(args[2]);
				boolean debugMode = false;
				
				int minNumConjuncts = 0;				
				if (args.length >= 4) {
					minNumConjuncts = Integer.parseInt(args[3]);
				}

				int minNumConstraints = 0;
				if (args.length >= 5) {
					minNumConstraints = Integer.parseInt(args[4]);
				}

				double pOverlapRule = 0.0;
				if (args.length >= 6) {
					pOverlapRule = Double.parseDouble(args[5]);
				}
				
				if (args.length >= 7) {
					if (args[6].equals(VERBOSE_OPTION)) {
						debugMode = true;
					}
				}
				SyntheticPolicyCaseStudyGenerator.generateCaseStudy(outputFile,
						null, Nrule, debugMode, true, minNumConjuncts, minNumConstraints, 0, 0, pOverlapRule);		
			}
			if (args[0].charAt(1) == 'o') {
				// add over-assignment
				if (args.length < 4) {
					System.err
							.println("Three more arguments needed for -o mode. 1. inputFile 2. noise level 3. tau");
					System.exit(1);
				}
				String inputFile = "../" + args[1];
				double noiseRatio = Double.parseDouble(args[2]);
				Parser.config = new Config();
				Parser.parseInputFile(inputFile);
				// ABACMiner.overAssignThres = Integer.parseInt(args[3]);
				ABACMiner.addOverassignmentNoise(Parser.config, noiseRatio);
				boolean debugMode = false;
				if (args.length >= 5) {
					if (args[4].equals(VERBOSE_OPTION)) {
						debugMode = true;
					}
				}
				ABACMiner.mineABACPolicy(Parser.config, debugMode, 0, false,
						true, 0, 0.0, false);
			}
			if(args[0].charAt(1) == 'n') {
				if (args.length < 5) {
					System.err
					.println("Four arguments are needed for -n mode. 1. underassignments noise level 2.  overassignment noise level 3. attribute data noise level 4. inputFile");
					System.exit(1);
				}
				String inputFile = "../" + args[4];
				double unoiseRatio = Double.parseDouble(args[1]);
				double onoiseRatio = Double.parseDouble(args[2]);
				double anoiseRatio = Double.parseDouble(args[3]);
				NoiseExperiment.UPAndAttrNoiseOnCaseStudy(unoiseRatio, onoiseRatio, anoiseRatio, inputFile);
			}
			if (args[0].charAt(1) == 'u') {
				// add under-assignment
				if (args.length < 4) {
					System.err
							.println("Three more arguments needed for -u mode. 1. inputFile 2. noise level 3. alpha");
					System.exit(1);
				}
				String inputFile = "../" + args[1];
				double noiseRatio = Double.parseDouble(args[2]);
				Parser.config = new Config();
				Parser.parseInputFile(inputFile);
				ABACMiner.addUnderassignmentNoise(Parser.config, noiseRatio);
				ABACMiner.underAssignFrac = Double.parseDouble(args[3]);
				boolean debugMode = false;
				if (args.length >= 5) {
					if (args[4].equals(VERBOSE_OPTION)) {
						debugMode = true;
					}
				}
				ABACMiner.mineABACPolicy(Parser.config, debugMode, 0, false,
						true, 0, 0.0, false);
			}
			if (args[0].charAt(1) == 'z') {
				if (args.length < 2) {
					System.err
							.println("One more arguments needed for -z mode. 1. inputFile");
					System.exit(1);
				}
				double avgNumUsers = 0;
				double avgNumResources = 0;
				double avgUP = 0;
				double avgUPByRules = 0;
				for (int i = 1; i <= 5; i++) {
					System.out.println(i);
					String inputFile = args[1] + "_" + i + ".abac";
					Parser.parseInputFile(inputFile);
					ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
							true, 0, 0.0, false);
					int totalNumUPTriples = 0;
					for (Rule r : ABACMiner.resultRules) {
						totalNumUPTriples += ABACMiner.computeCoveredUPTriple(
								r, Parser.config).size();
					}
					double avgNumUPTriples = (double) totalNumUPTriples
							/ ABACMiner.resultRules.size();
					avgNumUsers += Parser.config.getUsers().size();
					avgNumResources += Parser.config.getResources().size();
					avgUP += Parser.config.getCoveredUP().size();
					avgUPByRules += avgNumUPTriples;
					Parser.config = new Config();
				}
				System.out.println("Average number of users: " + avgNumUsers
						/ 5);
				System.out.println("Average number of resources: "
						+ avgNumResources / 5);
				System.out.println("Average number of UP: " + avgUP / 5);
				System.out.println("Average number of UP by rule: "
						+ avgUPByRules / 5);
			}

			if (args[0].charAt(1) == 'm') {
				// main ABAC mining algorithm
				if (args.length < 2) {
					System.err
							.println("One argument is needed for -m mode. 1. inputFile");
					System.exit(1);
				}
				String inputFile =  /*"../" +*/ args[1];
				Parser.parseInputFile(inputFile);
				ABACMiner.startTime = System.currentTimeMillis();

				boolean debugMode = false;

				if (args.length >= 3) {
					if (args[2].equals(VERBOSE_OPTION)) {
						debugMode = true;
					}
				}
				ABACMiner.mineABACPolicy(Parser.config, debugMode, 0, false,
						true, 0, 0.0, false);
				double totalInputWSC = 0;
				for (int i = 0; i < Parser.config.getRuleList().size(); i++) {
					totalInputWSC += Parser.config.getRuleList().get(i)
							.getSize();
				}

				double totalOutputWSC = 0;
				for (int i = 0; i < ABACMiner.resultRules.size(); i++) {
					totalOutputWSC += ABACMiner.resultRules.get(i).getSize();
				}

				double aclSize = 0;
				aclSize += Parser.config.getCoveredUP().size();
				// for (String user : Parser.config.getUserAttrInfo().keySet())
				// {
				// for (String attr : Parser.config.getUserAttrInfo()
				// .get(user).keySet()) {
				// aclSize += Parser.config.getUserAttrInfo().get(user)
				// .get(attr).size();
				// }
				// }
				// for (String resource : Parser.config.getResourceAttrInfo()
				// .keySet()) {
				// for (String attr : Parser.config.getResourceAttrInfo()
				// .get(resource).keySet()) {
				// aclSize += Parser.config.getResourceAttrInfo()
				// .get(resource).get(attr).size();
				// }
				// }

				ABACMiner.computeDuration();

				System.out.println("Number of input rules: "
						+ Parser.config.getRuleList().size());
				System.out.println("Number of output rules: "
						+ ABACMiner.resultRules.size());
				System.out.println("Total ACL size: " + aclSize);
				System.out.println("Total input WSC: " + totalInputWSC);
				System.out.println("Total output WSC: " + totalOutputWSC);
				System.out.println("Total Running Time: " + ABACMiner.totalTime
						+ " ms");
				int totalNumUPTriples = 0;
				for (Rule r : ABACMiner.resultRules) {
					totalNumUPTriples += ABACMiner.computeCoveredUPTriple(r,
							Parser.config).size();
				}
				double avgNumUPTriples = (double) totalNumUPTriples
						/ ABACMiner.resultRules.size();
				System.out.println("Average number of covered UP triples:"
						+ avgNumUPTriples);
			}
		}
	}
}
