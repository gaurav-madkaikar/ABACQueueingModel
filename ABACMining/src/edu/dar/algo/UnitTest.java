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

import java.util.HashSet;
import java.util.Random;

import edu.dar.util.AttrValConjunct;
import edu.dar.util.Config;
import edu.dar.util.Pair;
import edu.dar.util.Parser;
import edu.dar.util.Rule;
import edu.dar.util.SyntheticPolicyCaseStudyGenerator;
import edu.dar.util.Triple;
import edu.dar.util.ValueType;

public class UnitTest {
	public static void main(String[] args) {
		Random rand = new Random(System.currentTimeMillis());
		String inputFile = "ran-case-studies/test.abac";
		SyntheticPolicyCaseStudyGenerator.generateCaseStudy(inputFile,
				null, 10, false, true, 1, 0, 3, 0,
				1.0);
		Parser.config = new Config();
		Parser.parseInputFile(inputFile);

		ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true, 0, 0.0, true);
//		Config config = Parser.config;
//		HashSet<Pair<Rule, Rule>> overlapRulePairs = new HashSet<Pair<Rule, Rule>>();
//
//		for (int j = 0; j < config.getRuleList().size(); j++) {
//			Rule r1 = config.getRuleList().get(j);
//			for (int l = j + 1; l < config.getRuleList().size(); l++) {
//				Rule r2 = config.getRuleList().get(l);
//				if (ABACMiner.computeRuleOverlap(r1, r2, config).size() > 0) {
//					overlapRulePairs.add(new Pair<Rule, Rule>(r1, r2));
//					//System.out.println(r1);
//					//System.out.println(r2);
//					//System.out.println(ABACMiner.computeRuleOverlap(r1, r2, config));
//					System.out.println(ABACMiner.computeRuleOverlap(r1, r2, config).size());
//				}
//			}
//		}
		
//		for (Pair<Rule, Rule> p : overlapRulePairs) {
//			Rule r = p.getSecond();
//			if (rand.nextDouble() <= 0.5 && r.getUAE().size() > 0) {
//				for (int l = 0; l < 10; l++) {
//					int index = rand.nextInt(r.getUAE().size());
//					AttrValConjunct c = r.getUAE().get(index);
//					if (config.getUserAttrSet().get(c.getLHS())
//							.getvType() == ValueType.Single
//							&& c.getRHS().size() > 1) {
//						String v = SyntheticPolicyCaseStudyGenerator
//								.randomElement(c.getRHS());
//						c.getRHS().remove(v);
//						System.out.println("removed");
//						r.setChanged(true);
//						r.setUaeChanged(true);
//						r.setRaeChanged(true);
//						break;
//					} else if (config.getUserAttrSet()
//							.get(c.getLHS()).getvType() == ValueType.Set
//							&& c.getRHSet().size() > 1) {
//						HashSet<String> v = SyntheticPolicyCaseStudyGenerator
//								.randomSet(c.getRHSet());
//						c.getRHSet().remove(v);
//						System.out.println("removed");
//						r.setChanged(true);
//						r.setUaeChanged(true);
//						r.setRaeChanged(true);
//						break;
//					}
//				}
//			} else {
//				if (r.getRAE().size() == 0) {
//					continue;
//				}
//				for (int l = 0; l < 10; l++) {
//					int index = rand.nextInt(r.getRAE().size());
//					AttrValConjunct c = r.getRAE().get(index);
//					if (config.getResourceAttrSet().get(c.getLHS())
//							.getvType() == ValueType.Single
//							&& c.getRHS().size() > 1) {
//						String v = SyntheticPolicyCaseStudyGenerator
//								.randomElement(c.getRHS());
//						c.getRHS().remove(v);
//						System.out.println("removed");
//						r.setChanged(true);
//						r.setUaeChanged(true);
//						r.setRaeChanged(true);
//						break;
//					} else if (config.getResourceAttrSet()
//							.get(c.getLHS()).getvType() == ValueType.Set
//							&& c.getRHSet().size() > 1) {
//						HashSet<String> v = SyntheticPolicyCaseStudyGenerator
//								.randomSet(c.getRHSet());
//						c.getRHSet().remove(v);
//						System.out.println("removed");
//						r.setChanged(true);
//						r.setUaeChanged(true);
//						r.setRaeChanged(true);
//						break;
//					}
//				}
//			}
//		}
//		System.out.println("***************************************");
//		for (int j = 0; j < config.getRuleList().size(); j++) {
//			Rule r1 = config.getRuleList().get(j);
//			for (int l = j + 1; l < config.getRuleList().size(); l++) {
//				Rule r2 = config.getRuleList().get(l);
//				if (ABACMiner.computeRuleOverlap(r1, r2, config).size() > 0) {
//					overlapRulePairs.add(new Pair<Rule, Rule>(r1, r2));
//					//System.out.println(r1);
//					//System.out.println(r2);
//					//System.out.println(ABACMiner.computeRuleOverlap(r1, r2, config));
//					System.out.println(ABACMiner.computeRuleOverlap(r1, r2, config).size());
//				}
//			}
//		}

		int i = 1;
		int totalInputSize = 0;
		for (Rule r : Parser.config.getRuleList()) {
			System.out.println(i++ + " " + r);
			System.out.println(r.getSize());
			System.out.println(r.getRuleSize(Parser.config));
			totalInputSize += r.getRuleSize(Parser.config);
		}

		int totalOutputSize = 0;
		double similarity = 0.0;
		i = 1;
		for (Rule r : ABACMiner.resultRules) {
			System.out.println(i++ + " " + r);
			System.out.println(r.getSize());
			System.out.println(r.getRuleSize(Parser.config));
			totalOutputSize += r.getRuleSize(Parser.config);
			double maxSimilarity = 0.0;
			Rule maxRule = null;
			for (Rule r1 : Parser.config.getRuleList()) {
				double currentSimilarity = ABACMiner
						.syntacticSimilarityOfRules(r, r1, Parser.config);
				if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
					maxSimilarity = currentSimilarity;
					maxRule = r1;
				}

			}
			similarity += maxSimilarity;
			System.out.println("Most similar rule\n:" + maxRule);
			System.out.println(maxSimilarity);
		}
		similarity /= ABACMiner.resultRules.size();
		System.out.println("Similarity: " + similarity);
		System.out.println("Input Policy Size:" + totalInputSize);
		System.out.println("Output Policy Size:" + totalOutputSize);
		
//		Config config = Parser.config;
//		
//		for (i = 1; i <= 9; i++) {
//			System.out.println("i = " + i);
//			HashSet<Pair<Rule, Rule>> overlapRulePairs = new HashSet<Pair<Rule, Rule>>();
//			for (int j = 0; j < config.getRuleList().size(); j++) {
//				Rule r1 = config.getRuleList().get(j);
//				for (int l = j + 1; l < config.getRuleList().size(); l++) {
//					Rule r2 = config.getRuleList().get(l);
//					if (ABACMiner.computeRuleOverlap(r1, r2, config)
//							.size() > 0) {
//						overlapRulePairs.add(new Pair<Rule, Rule>(r1,
//								r2));
//					}
//				}
//			}
//
//			for (Pair<Rule, Rule> p : overlapRulePairs) {
//				Rule r = p.getSecond();
//				if (rand.nextDouble() <= 0.5 && r.getUAE().size() > 0) {
//					for (int l = 0; l < 10; l++) {
//						int index = rand.nextInt(r.getUAE().size());
//						AttrValConjunct c = r.getUAE().get(index);
//						if (config.getUserAttrSet().get(c.getLHS())
//								.getvType() == ValueType.Single
//								&& c.getRHS().size() > 1) {
//							String v = SyntheticPolicyCaseStudyGenerator
//									.randomElement(c.getRHS());
//							c.getRHS().remove(v);
//							r.setChanged(true);
//							r.setUaeChanged(true);
//							r.setRaeChanged(true);
//							break;
//						} else if (config.getUserAttrSet()
//								.get(c.getLHS()).getvType() == ValueType.Set
//								&& c.getRHSet().size() > 1) {
//							HashSet<String> v = SyntheticPolicyCaseStudyGenerator
//									.randomSet(c.getRHSet());
//							c.getRHSet().remove(v);
//							r.setChanged(true);
//							r.setUaeChanged(true);
//							r.setRaeChanged(true);
//							break;
//						}
//					}
//				} else {
//					if (r.getRAE().size() == 0) {
//						continue;
//					}
//					for (int l = 0; l < 10; l++) {
//						int index = rand.nextInt(r.getRAE().size());
//						AttrValConjunct c = r.getRAE().get(index);
//						if (config.getResourceAttrSet().get(c.getLHS())
//								.getvType() == ValueType.Single
//								&& c.getRHS().size() > 1) {
//							String v = SyntheticPolicyCaseStudyGenerator
//									.randomElement(c.getRHS());
//							c.getRHS().remove(v);
//							r.setChanged(true);
//							r.setUaeChanged(true);
//							r.setRaeChanged(true);
//							break;
//						} else if (config.getResourceAttrSet()
//								.get(c.getLHS()).getvType() == ValueType.Set
//								&& c.getRHSet().size() > 1) {
//							HashSet<String> v = SyntheticPolicyCaseStudyGenerator
//									.randomSet(c.getRHSet());
//							c.getRHSet().remove(v);
//							r.setChanged(true);
//							r.setUaeChanged(true);
//							r.setRaeChanged(true);
//							break;
//						}
//					}
//				}
//			}
//
//			// for (Rule rule : config.getRuleList()) {
//			// if (rule.getCon().size() > 0) {
//			// int conIndex = rand.nextInt(rule.getCon().size());
//			// rule.getCon().remove(conIndex);
//			// }
//			// rule.setChanged(true);
//			// rule.setUaeChanged(true);
//			// rule.setRaeChanged(true);
//			// }
//			// Construct UP relations from rules
//			config.getCoveredUP().clear();
//			config.getResourceUsers().clear();
//			config.getUserResources().clear();
//			config.getUserPerms().clear();
//			config.getPermUsers().clear();
//			for (Rule r : config.getRuleList()) {
//				HashSet<String> satUsers = new HashSet<String>();
//				HashSet<String> satPerms = new HashSet<String>();
//				for (String u : config.getUsers()) {
//					if (Parser.satisfyingRule(u, r, config, true)) {
//						satUsers.add(u);
//					}
//				}
//				for (String p : config.getResources()) {
//					if (Parser.satisfyingRule(p, r, config, false)) {
//						satPerms.add(p);
//					}
//				}
//				for (String u : satUsers) {
//					for (String p : satPerms) {
//						if (Parser.satisfyingRuleConstraints(u, p, r,
//								config)) {
//							if (!config.getUserResources().containsKey(
//									u)) {
//								config.getUserResources().put(u,
//										new HashSet<String>());
//							}
//							config.getUserResources().get(u).add(p);
//							if (!config.getResourceUsers().containsKey(
//									p)) {
//								config.getResourceUsers().put(p,
//										new HashSet<String>());
//							}
//							config.getResourceUsers().get(p).add(u);
//							if (!config.getUserPerms().containsKey(u)) {
//								config.getUserPerms()
//										.put(u,
//												new HashSet<Pair<String, String>>());
//							}
//							for (String op : r.getOps()) {
//								config.getUserPerms()
//										.get(u)
//										.add(new Pair<String, String>(
//												op, p));
//								Pair<String, String> perm = new Pair<String, String>(
//										op, p);
//								if (!config.getPermUsers().containsKey(
//										perm)) {
//									config.getPermUsers().put(perm,
//											new HashSet<String>());
//								}
//								config.getPermUsers().get(perm).add(u);
//								config.getCoveredUP()
//										.add(new Triple<String, String, String>(
//												u, op, p));
//							}
//						}
//					}
//				}
//			}
//			ABACMiner.mineABACPolicy(Parser.config, false, 0, false,
//					true);
//			
//			i = 1;
//			totalInputSize = 0;
//			for (Rule r : Parser.config.getRuleList()) {
//				System.out.println(i++ + " " + r);
//				System.out.println(r.getSize());
//				System.out.println(r.getRuleSize(Parser.config));
//				totalInputSize += r.getRuleSize(Parser.config);
//			}
//
//			totalOutputSize = 0;
//			similarity = 0.0;
//			i = 1;
//			for (Rule r : ABACMiner.resultRules) {
//				System.out.println(i++ + " " + r);
//				System.out.println(r.getSize());
//				System.out.println(r.getRuleSize(Parser.config));
//				totalOutputSize += r.getRuleSize(Parser.config);
//				double maxSimilarity = 0.0;
//				Rule maxRule = null;
//				for (Rule r1 : Parser.config.getRuleList()) {
//					double currentSimilarity = ABACMiner
//							.syntacticSimilarityOfRules(r, r1, Parser.config);
//					if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
//						maxSimilarity = currentSimilarity;
//						maxRule = r1;
//					}
//
//				}
//				similarity += maxSimilarity;
//				System.out.println("Most similar rule\n:" + maxRule);
//				System.out.println(maxSimilarity);
//			}
//			similarity /= ABACMiner.resultRules.size();
//			System.out.println("Similarity: " + similarity);
//			System.out.println("Input Policy Size:" + totalInputSize);
//			System.out.println("Output Policy Size:" + totalOutputSize);
//		
////		 for (int j = 1; j >= 0; j--) {
////		 Random rand = new Random(System.currentTimeMillis());
////		 for (Rule rule : config.getRuleList()) {
////		 // if (rule.getUAE().size() > 0) {
////		 // int uaeIndex = rand.nextInt(rule.getUAE().size());
////		 // rule.getUAE().remove(uaeIndex);
////		 // rule.setUaeChanged(true);
////		 // }
////		 // if (rule.getRAE().size() > 0) {
////		 // int raeIndex = rand.nextInt(rule.getRAE().size());
////		 // rule.getRAE().remove(raeIndex);
////		 // rule.setRaeChanged(true);
////		 // }
////		 if (rule.getCon().size() > 0) {
////		 int conIndex = rand.nextInt(rule.getCon().size());
////		 rule.getCon().remove(conIndex);
////		 }
////		 rule.setChanged(true);
////		 rule.setUaeChanged(true);
////		 rule.setRaeChanged(true);
////		 }
////		 // Construct UP relations from rules
////		 config.getCoveredUP().clear();
////		 config.getResourceUsers().clear();
////		 config.getUserResources().clear();
////		 config.getUserPerms().clear();
////		 config.getPermUsers().clear();
////		 for (Rule r : config.getRuleList()) {
////		 HashSet<String> satUsers = new HashSet<String>();
////		 HashSet<String> satPerms = new HashSet<String>();
////		 for (String u : config.getUsers()) {
////		 if (Parser.satisfyingRule(u, r, config, true)) {
////		 satUsers.add(u);
////		 }
////		 }
////		 for (String p : config.getResources()) {
////		 if (Parser.satisfyingRule(p, r, config, false)) {
////		 satPerms.add(p);
////		 }
////		 }
////		 for (String u : satUsers) {
////		 for (String p : satPerms) {
////		 if (Parser.satisfyingRuleConstraints(u, p, r, config)) {
////		 if (!config.getUserResources().containsKey(u)) {
////		 config.getUserResources().put(u,
////		 new HashSet<String>());
////		 }
////		 config.getUserResources().get(u).add(p);
////		 if (!config.getResourceUsers().containsKey(p)) {
////		 config.getResourceUsers().put(p,
////		 new HashSet<String>());
////		 }
////		 config.getResourceUsers().get(p).add(u);
////		 if (!config.getUserPerms().containsKey(u)) {
////		 config.getUserPerms().put(u,
////		 new HashSet<Pair<String, String>>());
////		 }
////		 for (String op : r.getOps()) {
////		 config.getUserPerms().get(u)
////		 .add(new Pair<String, String>(op, p));
////		 Pair<String, String> perm = new Pair<String, String>(
////		 op, p);
////		 if (!config.getPermUsers().containsKey(perm)) {
////		 config.getPermUsers().put(perm,
////		 new HashSet<String>());
////		 }
////		 config.getPermUsers().get(perm).add(u);
////		 config.getCoveredUP().add(
////		 new Triple<String, String, String>(u,
////		 op, p));
////		 }
////		 }
////		 }
////		 }
////		 }
////		 ABACMiner.mineABACPolicy(Parser.config, false, 0, false, true);
////		 i = 1;
////		 totalInputSize = 0;
////		 for (Rule r : Parser.config.getRuleList()) {
////		 System.out.println(i++ + " " + r);
////		 System.out.println(r.getSize());
////		 System.out.println(r.getRuleSize(Parser.config));
////		 totalInputSize += r.getRuleSize(Parser.config);
////		 }
////		
////		 totalOutputSize = 0;
////		 similarity = 0.0;
////		 i = 1;
////		 for (Rule r : ABACMiner.resultRules) {
////		 System.out.println(i++ + " " + r);
////		 System.out.println(r.getSize());
////		 System.out.println(r.getRuleSize(Parser.config));
////		 totalOutputSize += r.getRuleSize(Parser.config);
////		 double maxSimilarity = 0.0;
////		 Rule maxRule = null;
////		 for (Rule r1 : Parser.config.getRuleList()) {
////		 double currentSimilarity = ABACMiner
////		 .syntacticSimilarityOfRules(r, r1, Parser.config);
////		 if (Double.compare(currentSimilarity, maxSimilarity) > 0) {
////		 maxSimilarity = currentSimilarity;
////		 maxRule = r1;
////		 }
////		
////		 }
////		 similarity += maxSimilarity;
////		 System.out.println("Most similar rule\n:" + maxRule);
////		 System.out.println(maxSimilarity);
////		 }
////		 similarity /= ABACMiner.resultRules.size();
////		 System.out.println("Similarity: " + similarity);
////		 System.out.println("Input Policy Size:" + totalInputSize);
////		 System.out.println("Output Policy Size:" + totalOutputSize);
////		 }
//		}
	}
}
