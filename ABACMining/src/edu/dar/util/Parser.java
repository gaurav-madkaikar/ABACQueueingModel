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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Parser class is used for parsing .rbac dataset.
 * 
 * @author Zhongyuan
 */
public class Parser {
	final public class RulePattern {
		public static final String commentPattern = "#.*";
		public static final String userAttrPattern = "userAttrib\\(.*\\)";
		public static final String permAttrPattern = "resourceAttrib\\(.*\\)";
		public static final String rulePattern = "rule\\(.*\\)";
		public static final String unrevUserAttrPattern = "unremovableUserAttribs\\(.*\\)";
		public static final String unrevResourceAttrPattern = "unremovableResourceAttribs\\(.*\\)";
	}

	public static Config config = new Config();
	public static boolean debug = false;
	public static void parseInputFile(String inputFile) {
		FileReader fileReader;
		try {
			fileReader = new FileReader(inputFile);
			BufferedReader inputReader = new BufferedReader(fileReader);
			String line;
			// sequentially parse each line
			while ((line = inputReader.readLine()) != null) {
				if (line.matches(RulePattern.commentPattern)) {
					// match comment
					continue;
				} else if (line.matches(RulePattern.userAttrPattern)) {
					// match user attributes
					Parser.processAttrRule(line, AttributeType.UserAttr, config);
				} else if (line.matches(RulePattern.permAttrPattern)) {
					// match resource attributes
					Parser.processAttrRule(line, AttributeType.ResourceAttr, config);
				} else if (line.matches(RulePattern.rulePattern)) {
					// match rules
					Parser.processAccessRule(line, config);
				} else if (line.matches(RulePattern.unrevUserAttrPattern)) {
					Parser.processUnrevAttrRule(line, AttributeType.UserAttr,
							config);
				} else if (line.matches(RulePattern.unrevResourceAttrPattern)) {
					Parser.processUnrevAttrRule(line, AttributeType.ResourceAttr,
							config);
				}
			}

			// Construct UP relations from rules
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
							if (!config.getUserResources().containsKey(u)) {
								config.getUserResources().put(u,
										new HashSet<String>());
							}
							config.getUserResources().get(u).add(p);
							if (!config.getResourceUsers().containsKey(p)) {
								config.getResourceUsers().put(p,
										new HashSet<String>());
							}
							config.getResourceUsers().get(p).add(u);
							if (!config.getUserPerms().containsKey(u)) {
								config.getUserPerms().put(u,
										new HashSet<Pair<String, String>>());
							}
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
								config.getCoveredUP().add(
										new Triple<String, String, String>(u,
												op, p));
							}
						}
					}
				}
			}

//			int numRules = 1;
//			int totalWSC = 0;
//			double wsc = 0;
//			if (false) {
//				System.out.println("INPUT RULES");
//				for (Rule r : config.getRuleList()) {
//					System.out.println(numRules++ + ". " + r);
//					wsc = r.getSize();
//					totalWSC += wsc;
//					System.out.println("WSC=" + wsc);
//				}
//				System.out.println("\ntotal WSC=" + totalWSC);
//				System.out.print("unremovableResourceAttribs(");
//				for (String resAttrib : config.getResourceAttrSet().keySet()) {
//					if (config.getResourceAttrSet().get(resAttrib).isUnremovable()) {
//						System.out.print(resAttrib + ",");
//					}
//				}
//				System.out.println(")");
//				System.out.print("unremovableUserAttribs(");
//				for (String userAttrib : config.getUserAttrSet().keySet()) {
//					if (config.getUserAttrSet().get(userAttrib).isUnremovable()) {
//						System.out.print(userAttrib + ",");
//					}
//				}
//				System.out.println(")");
//				System.out.println("Total UP size:"
//						+ config.getCoveredUP().size());
//			}
			inputReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	public static boolean satisfyingRuleConstraints(String user, String perm,
			Rule r, Config config) {
		if (r.getCon().isEmpty()) {
			return true;
		}
		for (AttrAttrConjunct c : r.getCon()) {
			if (c instanceof AttrAttrConjunct) {
				if (!config.getUserAttrInfo().get(user).containsKey(c.getLHS())) {
					return false;
				}
				if (!config.getResourceAttrInfo().get(perm).containsKey(c.getRHS())) {
					return false;
				}

				String uAttr = c.getLHS();
				String pAttr = c.getRHS();

				HashSet<String> userAttrVals = config.getUserAttrInfo()
						.get(user).get(c.getLHS());
				HashSet<String> permAttrVals = config.getResourceAttrInfo()
						.get(perm).get(c.getRHS());
				switch (c.getOperator()) {
				case EQUALS:
					if (!(config.getUserAttrSet().get(uAttr).getvType() == ValueType.Single)
							|| !(config.getResourceAttrSet().get(pAttr).getvType() == ValueType.Single)
							|| !userAttrVals.equals(permAttrVals)) {
						return false;
					}
					break;
				case SUBSETEQ:
					if (!permAttrVals.containsAll(userAttrVals)) {
						return false;
					}
					break;
				case SUPSETEQ:
					if (!(config.getUserAttrSet().get(uAttr).getvType() == ValueType.Set)
							|| !(config.getResourceAttrSet().get(pAttr).getvType() == ValueType.Set)
							|| !userAttrVals.containsAll(permAttrVals)) {
						return false;
					}
					break;
				case IN:
					if (!(config.getUserAttrSet().get(uAttr).getvType() == ValueType.Set)
							|| !(config.getResourceAttrSet().get(pAttr).getvType() == ValueType.Single)
							|| userAttrVals == null || permAttrVals == null || !userAttrVals.containsAll(permAttrVals)) {
						return false;
					}
					break;
				default:
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * satisfyingUAE checks whether user satisfies user-attribute expression
	 * uae.
	 * 
	 * @param user
	 * @param uae
	 * @param config
	 * @return true if user satisfies uae, otherwise false.
	 */
	public static boolean satisfyingUAE(String user,
			ArrayList<AttrValConjunct> uae, Config config) {
		for (AttrValConjunct c : uae) {
			if (c.getLHS().equals("uid")) {
				if (!c.getRHS().contains(user)) {
					return false;
				} else {
					continue;
				}
			}
			if (!config.getUserAttrInfo().get(user).containsKey(c.getLHS())) {
				return false;
			}
			HashSet<String> userAttrVals = config.getUserAttrInfo().get(user)
					.get(c.getLHS());
			if (c.getRHS() != null && !c.getRHS().containsAll(userAttrVals)) {
				return false;
			}

			if (c.getRHSet() != null) {
				boolean satisfied = false;
				for (HashSet<String> s : c.getRHSet()) {
					if (userAttrVals.containsAll(s)) {
						satisfied = true;
						break;
					}
				}
				if (satisfied == false) {
					return false;
				}
				// else
				// return true;
			}
		}
		return true;
	}

	/**
	 * satisfyingPAE checks whether perm satisfies permission-attribute
	 * expression pae.
	 * 
	 * @param perm
	 * @param pae
	 * @param config
	 * @return true if perm satisfies pae, otherwise false.
	 */
	public static boolean satisfyingRAE(String perm,
			ArrayList<AttrValConjunct> pae, Config config) {
		for (AttrValConjunct c : pae) {
			if (c.getLHS().equals("rid")) {
				if (!c.getRHS().contains(perm)) {
					return false;
				} else {
					continue;
				}
			}
			if (!config.getResourceAttrSet().containsKey(c.getLHS())) {
				continue;
			}
			if (!config.getResourceAttrInfo().get(perm).containsKey(c.getLHS())) {
				return false;
			}
			HashSet<String> permAttrVals = config.getResourceAttrInfo().get(perm)
					.get(c.getLHS());
			if (c.getRHS() != null && !c.getRHS().containsAll(permAttrVals)) {
				return false;
			}
			if (c.getRHSet() != null) {
				boolean satisfied = false;
				for (HashSet<String> s : c.getRHSet()) {
					if (permAttrVals.containsAll(s)) {
						satisfied = true;
						break;
					}
				}
				if (satisfied == false) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean satisfyingRule(String subject, Rule r, Config config,
			Boolean isUser) {
		if (isUser) {
			for (AttrValConjunct c : r.getUAE()) {
				if (((AttrValConjunct) c).getLHS().equals("uid")) {
					if (!((AttrValConjunct) c).getRHS().contains(subject)) {
						return false;
					} else {
						continue;
					}
				}
				if (!config.getUserAttrSet().containsKey(c.getLHS())) {
					continue;
				}
				if (config.getUserAttrSet().get(c.getLHS()).getvType() == ValueType.Single
						&& !config.getUserAttrInfo().get(subject)
								.containsKey(c.getLHS())) {
					return false;
				}
				// single value attribute
				if (config.getUserAttrSet().get(c.getLHS()).getvType() == ValueType.Single) {
					HashSet<String> userAttrVals = config.getUserAttrInfo()
							.get(subject).get(c.getLHS());
					if (!c.getRHS().containsAll(userAttrVals)) {
						return false;
					}
				} else {
					// multi value attribute
					boolean satisfy = false;
					if (c.getRHSet().size() == 1) {
						for (HashSet<String> s : c.getRHSet()) {
							if (s.isEmpty()) {
								satisfy = true;
								break;
							}
						}
					}
					if (satisfy == true) {
						continue;
					}
					
					HashSet<String> userAttrVals = config.getUserAttrInfo()
							.get(subject).get(c.getLHS());
					if (userAttrVals == null) {
						return false;
					}
					
					HashSet<HashSet<String>> valueSet = c.getRHSet();
					for (HashSet<String> s : valueSet) {
						if (userAttrVals.containsAll(s)) {
							satisfy = true;
							break;
						}
					}
					if (satisfy == false) {
						return false;
					}
				}
			}
		} else {
			for (AttrValConjunct c : r.getRAE()) {
				if (((AttrValConjunct) c).getLHS().equals("rid")) {
					if (!((AttrValConjunct) c).getRHS().contains(subject)) {
						return false;
					} else {
						continue;
					}
				}
				if (!config.getResourceAttrSet().containsKey(c.getLHS())) {
					continue;
				}
				if (!config.getResourceAttrInfo().get(subject)
						.containsKey(c.getLHS())) {
					return false;
				}
				// single value attribute
				if (config.getResourceAttrSet().get(c.getLHS()).getvType() == ValueType.Single) {
					HashSet<String> permAttrVals = config.getResourceAttrInfo()
							.get(subject).get(c.getLHS());
					if (c.getRHS() == null) {
						System.out.println("Here");
					}
					if (c.getRHS() == null || permAttrVals == null || !c.getRHS().containsAll(permAttrVals)) {
						return false;
					}
				} else {
					// multi value attribute
					boolean satisfy = false;
					if (c.getRHSet().size() == 1) {
						for (HashSet<String> s : c.getRHSet()) {
							if (s.isEmpty()) {
								satisfy = true;
								break;
							}
						}
					}
					if (satisfy == true) {
						continue;
					}
					
					HashSet<String> permAttrVals = config.getResourceAttrInfo()
							.get(subject).get(c.getLHS());
					if (permAttrVals == null) {
						return false;
					}
					
					HashSet<HashSet<String>> valueSet = c.getRHSet();
					for (HashSet<String> s : valueSet) {
						if (permAttrVals.containsAll(s)) {
							satisfy = true;
							break;
						}
					}
					if (satisfy == false) {
						return false;
					}
				}
			}
		} 
		return true;
	}

	public static void processUnrevAttrRule(String rule, AttributeType type,
			Config config) throws Exception {
		String content = rule.substring(rule.indexOf('(') + 1,
				rule.indexOf(')'));
		String[] attributes = content.split(",");
		if (attributes.length >= 1) {
			for (int i = 0; i < attributes.length; i++) {
				String attribute = attributes[i].trim();
				if (!attribute.isEmpty()) {
					switch (type) {
					case UserAttr:
						config.getUserAttrSet().get(attribute)
								.setUnremovable(true);
						break;
					case ResourceAttr:
						config.getResourceAttrSet().get(attribute)
								.setUnremovable(true);
						break;
					}
				}
			}
		}
	}

	public static void processAccessRule(String rule, Config config)
			throws Exception {
		String content = rule.substring(rule.indexOf('(') + 1,
				rule.indexOf(')'));
		String[] components = content.split(";");
		Rule r = new Rule();

		if (components[0].contains(" in ")
				|| components[0].contains(" supseteqln ")) {
			String[] uConjuncts = components[0].split(",");
			for (int i = 0; i < uConjuncts.length; i++) {
				String conjunct = uConjuncts[i].trim();
				String[] pair;
				if (conjunct.contains(" in ")) {
					pair = conjunct.split(" in ");
				} else {
					pair = conjunct.split(" supseteqln ");
				}
				String lhs = pair[0].trim();
				String rhs = pair[1].trim();
				if (rhs.indexOf('{') >= 0) {
					if (!config.getUserAttrSet().containsKey(lhs)) {
						throw new Exception("User Attributes " + lhs
								+ " are not defined.");
					}
					if (conjunct.contains(" in ")) {
						HashSet<String> vals = new HashSet<String>(
								Arrays.asList(rhs.substring(
										rhs.indexOf('{') + 1, rhs.indexOf('}'))
										.split("\\s+")));
						AttrValConjunct c = new AttrValConjunct(lhs, vals,
								null, "=");
						r.adduConjunct(c);
					} else {
						// the case for supseteqIn
						/*
						 * String[] sets = rhs.substring(1, rhs.length() - 1)
						 * .split("\\s+"); HashSet<HashSet<String>> valSets =
						 * new HashSet<HashSet<String>>(); for (String set :
						 * sets) { HashSet<String> valSet = new HashSet<String>(
						 * Arrays.asList(set .substring(1, set.length() - 1)
						 * .trim().split("\\+"))); valSets.add(valSet); }
						 */

						HashSet<HashSet<String>> valSets = new HashSet<HashSet<String>>();
						String valSetsString = rhs.substring(1,
								rhs.length() - 1);
						while (valSetsString.indexOf('}') >= 0) {
							String subString = valSetsString.substring(
									valSetsString.indexOf('{') + 1,
									valSetsString.indexOf('}'));
							HashSet<String> valSet = new HashSet<String>(
									Arrays.asList(subString.trim()
											.split("\\s+")));
							valSets.add(valSet);
							if (valSetsString.indexOf('}') == valSetsString
									.length() - 1) {
								break;
							} else {
								valSetsString = valSetsString
										.substring(valSetsString.indexOf('}') + 1);
							}
						}

						AttrValConjunct c = new AttrValConjunct(lhs, null,
								valSets, "=");
						r.adduConjunct(c);
					}
				}
			}
		}

		if (components[1].contains(" in ")
				|| components[1].contains(" supseteqln ")) {
			String[] pConjuncts = components[1].split(",");
			for (int i = 0; i < pConjuncts.length; i++) {
				String conjunct = pConjuncts[i].trim();
				String[] pair;
				if (conjunct.contains(" in ")) {
					pair = conjunct.split(" in ");
				} else {
					pair = conjunct.split(" supseteqln ");
				}
				String lhs = pair[0].trim();
				String rhs = pair[1].trim();
				if (rhs.indexOf('{') >= 0) {
					if (!config.getResourceAttrSet().containsKey(lhs)) {
						throw new Exception("Perm Attributes " + lhs
								+ " are not defined.");
					}
					if (conjunct.contains(" in ")) {
						HashSet<String> vals = new HashSet<String>(
								Arrays.asList(rhs.substring(
										rhs.indexOf('{') + 1, rhs.indexOf('}'))
										.split("\\s+")));
						AttrValConjunct c = new AttrValConjunct(lhs, vals,
								null, "=");
						r.addrConjunct(c);
					} else {
						/*
						 * String[] sets = rhs.substring(1, rhs.length() - 1)
						 * .split("\\s+"); HashSet<HashSet<String>> valSets =
						 * new HashSet<HashSet<String>>(); for (String set :
						 * sets) { HashSet<String> valSet = new HashSet<String>(
						 * Arrays.asList(set .substring(1, set.length() - 1)
						 * .trim().split("\\+"))); valSets.add(valSet); }
						 * AttrValConjunct c = new AttrValConjunct(lhs, null,
						 * valSets, "="); r.addpConjunct(c);
						 */

						HashSet<HashSet<String>> valSets = new HashSet<HashSet<String>>();
						String valSetsString = rhs.substring(1,
								rhs.length() - 1);
						while (valSetsString.indexOf('}') >= 0) {
							String subString = valSetsString.substring(
									valSetsString.indexOf('{') + 1,
									valSetsString.indexOf('}'));
							HashSet<String> valSet = new HashSet<String>(
									Arrays.asList(subString.trim()
											.split("\\s+")));
							valSets.add(valSet);
							if (valSetsString.indexOf('}') == valSetsString
									.length() - 1) {
								break;
							} else {
								valSetsString = valSetsString
										.substring(valSetsString.indexOf('}') + 1);
							}
						}

						AttrValConjunct c = new AttrValConjunct(lhs, null,
								valSets, "=");
						r.addrConjunct(c);
					}
				}
			}
		}

		HashSet<String> ops = new HashSet<String>(Arrays.asList(components[2]
				.substring(components[2].indexOf('{') + 1,
						components[2].indexOf('}')).split("\\s+")));
		r.setOps(ops);
		config.getOps().addAll(ops);

		if (components.length >= 4) {
			if (components[3].indexOf('=') > 0
					|| components[3].indexOf('>') > 0
					|| components[3].indexOf('<') > 0
					|| components[3].indexOf(']') > 0) {
				String[] constraints = components[3].split(",");
				for (int i = 0; i < constraints.length; i++) {
					String constraint = constraints[i].trim();
					if (constraint.indexOf('=') > 0) {
						String[] pair = constraint.split("=");
						String lhs = pair[0].trim();
						String rhs = pair[1].trim();
						AttrAttrConjunct c = new AttrAttrConjunct(lhs, rhs,
								Operator.EQUALS);
						r.addConstraint(c);
					} else if (constraint.indexOf('>') > 0) {
						String[] pair = constraint.split(">");
						String lhs = pair[0].trim();
						String rhs = pair[1].trim();
						AttrAttrConjunct c = new AttrAttrConjunct(lhs, rhs,
								Operator.SUPSETEQ);
						r.addConstraint(c);
					} else if (constraint.indexOf('<') > 0) {
						String[] pair = constraint.split("<");
						String lhs = pair[0].trim();
						String rhs = pair[1].trim();
						AttrAttrConjunct c = new AttrAttrConjunct(lhs, rhs,
								Operator.SUBSETEQ);
						r.addConstraint(c);
					} else if (constraint.indexOf(']') > 0) {
						String[] pair = constraint.split("]");
						String lhs = pair[0].trim();
						String rhs = pair[1].trim();
						AttrAttrConjunct c = new AttrAttrConjunct(lhs, rhs,
								Operator.IN);
						r.addConstraint(c);
					} else {
						throw new Exception(
								"Constraint should contain one of the following operators: 1.=, 2.<, 3.>");
					}
				}
			}
		}

		config.getRuleList().add(r);
	}

	/**
	 * Process attribute data
	 * 
	 * @param rule
	 *            : the rule to be parsed
	 * @param type
	 *            : user attribute or permission attribute
	 * @param config
	 *            : configuration to be updated
	 * @throws Exception
	 */

	public static void processAttrRule(String rule, AttributeType type,
			Config config) throws Exception {
		String content = rule.substring(rule.indexOf('(') + 1,
				rule.indexOf(')'));
		String[] attributes = content.split(",");
		String name = attributes[0].trim();
		switch (type) {
		case UserAttr:
			if (config.getUsers().contains(name)) {
				throw new Exception("This user " + name
						+ " has been seen before.");
			} else {
				config.addUser(name);
				if (!config.getUserAttrSet().containsKey("uid")) {
					config.getUserAttrSet().put(
							"uid",
							new Attribute(AttributeType.UserAttr,
									ValueType.Single, "uid", false));
				}
				HashSet<String> nameVals = new HashSet<String>();
				nameVals.add(name);
				config.getUserAttrSet().get("uid").addValues(nameVals);
				config.getUserAttrInfo().put(name,
						new HashMap<String, HashSet<String>>());
				config.getUserAttrInfo().get(name).put("uid", nameVals);

				if (attributes.length > 1) {
					for (int i = 1; i < attributes.length; i++) {
						String attribute = attributes[i].trim();
						String[] pair = attribute.split("=");
						String key = pair[0];
						boolean isSet = false;
						HashSet<String> vals = new HashSet<String>();
						String value = pair[1];
						if (value.indexOf('{') >= 0) {
							vals = new HashSet<String>(Arrays.asList(value
									.substring(value.indexOf('{') + 1,
											value.indexOf('}')).split("\\s+")));
							isSet = true;
						} else {
							vals.add(value);
						}
						if (!config.getUserAttrSet().containsKey(key)) {
							Attribute attr;
							if (!isSet) {
								attr = new Attribute(AttributeType.UserAttr,
										ValueType.Single, key, false);
							} else {
								attr = new Attribute(AttributeType.UserAttr,
										ValueType.Set, key, false);
							}
							config.getUserAttrSet().put(key, attr);
						}
						if (!isSet) {
							config.getUserAttrSet().get(key).addValues(vals);
						} else {
							config.getUserAttrSet().get(key).addValue(vals);
						}

						config.getUserAttrInfo().get(name).put(key, vals);
					}
				}
			}
			break;
		case ResourceAttr:
			if (config.getResources().contains(name)) {
				throw new Exception("This perm " + name
						+ " has been seen before.");
			} else {
				config.addResource(name);
				if (!config.getResourceAttrSet().containsKey("rid")) {
					config.getResourceAttrSet().put(
							"rid",
							new Attribute(AttributeType.ResourceAttr,
									ValueType.Single, "rid", false));
				}
				HashSet<String> nameVals = new HashSet<String>();
				nameVals.add(name);
				config.getResourceAttrSet().get("rid").addValues(nameVals);
				config.getResourceAttrInfo().put(name,
						new HashMap<String, HashSet<String>>());
				config.getResourceAttrInfo().get(name).put("rid", nameVals);

				if (attributes.length > 1) {
					for (int i = 1; i < attributes.length; i++) {
						String attribute = attributes[i].trim();
						String[] pair = attribute.split("=");
						String key = pair[0];
						boolean isSet = false;
						HashSet<String> vals = new HashSet<String>();
						String value = pair[1];
						if (value.indexOf('{') >= 0) {
							vals = new HashSet<String>(Arrays.asList(value
									.substring(value.indexOf('{') + 1,
											value.indexOf('}')).split("\\s+")));
							isSet = true;
						} else {
							vals.add(value);
						}

						if (!config.getResourceAttrSet().containsKey(key)) {
							Attribute attr;
							if (!isSet) {
								attr = new Attribute(AttributeType.ResourceAttr,
										ValueType.Single, key, false);
							} else {
								attr = new Attribute(AttributeType.ResourceAttr,
										ValueType.Set, key, false);
							}
							config.getResourceAttrSet().put(key, attr);
						}
						if (!isSet) {
							config.getResourceAttrSet().get(key).addValues(vals);
						} else {
							config.getResourceAttrSet().get(key).addValue(vals);
						}
						config.getResourceAttrInfo().get(name).put(key, vals);
					}
				}
			}
			break;
		default:
			throw new Exception("Undefined attribute type.");
		}
	}
	
	

	public static void main(String[] args) {
		Parser.parseInputFile(args[0]);
		HashSet<Triple<String, String, String>> coveredUP1 = new HashSet<Triple<String, String, String>>(
				Parser.config.getCoveredUP());
		Parser.config = new Config();
		Parser.parseInputFile(args[1]);
		HashSet<Triple<String, String, String>> coveredUP2 = new HashSet<Triple<String, String, String>>(
				Parser.config.getCoveredUP());
		coveredUP2.removeAll(coveredUP1);
		System.out.println(coveredUP2);
	}
}
