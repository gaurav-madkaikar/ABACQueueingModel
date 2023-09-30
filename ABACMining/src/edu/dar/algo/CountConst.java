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

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import edu.dar.util.*;

public class CountConst {
	
	private static HashSet<String> atomicValues = new HashSet<String>();
	
	public static void clearAtomicValues() {
		atomicValues.clear();
	}
	
	/**
	 * Get atomic values from attribute data
	 * 
	 * @param rule
	 *            : the rule to be parsed
	 */
	public static void getAtomicValuesAttr(String rule) {
	
		String content = rule.substring(rule.indexOf('(') + 1,
				rule.indexOf(')'));
		String[] attributes = content.split(",");
		String name = attributes[0].trim();
		
		atomicValues.add(name);
				
		if (attributes.length > 1) {
			for (int i = 1; i < attributes.length; i++) {
				String attribute = attributes[i].trim();
				String[] pair = attribute.split("=");
				HashSet<String> vals = new HashSet<String>();
				String value = pair[1];
				if (value.indexOf('{') >= 0) {
						vals = new HashSet<String>(Arrays.asList(value
								.substring(value.indexOf('{') + 1,
										value.indexOf('}')).split("\\s+")));
						atomicValues.addAll(vals);
				} else {
					atomicValues.add(value);
				}
			}
		}
	}

	/**
	 * dumps the atomic values to output report file atomic_values_count_report.txt
	 * 
	 * @param inputFileName
	 *            : The name of the inputFileName
	 * @param file
	 * 	          : Output file in which the values needs to be written
	 */
	public static void dumpAtomicValues(String inputFileName, File file) {
		ArrayList<String> atomicVals = new ArrayList<String>();
		atomicVals.addAll(atomicValues);
		Collections.sort(atomicVals);
		
		try {	
			BufferedWriter output = new BufferedWriter(new FileWriter(file,true));
		
			output.write("File: " + inputFileName);
			output.newLine();
			
			output.write("Count : " + atomicVals.size());
			output.newLine();
		
			output.write("Atomic Values :");
			output.newLine();
			
			for(String temp: atomicVals) {
				 output.write(temp);
				 output.newLine();
			}
			
			output.write("---------------------------------------------------------");
			output.newLine();
			output.close();
		} catch ( IOException e ) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch ( Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} 
	}
	
	/**
	 * Get atomic values from rule
	 * 
	 * @param rule
	 *            : the rule to be parsed
	 */
	public static void getAtomicValuesRule(String rule) {
		
		String content = rule.substring(rule.indexOf('(') + 1,
				rule.indexOf(')'));
		String[] components = content.split(";");

		if (components[0].contains(" in ") || components[0].contains(" supseteqln ")) {
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
					if (conjunct.contains(" in ")) {
						HashSet<String> vals = new HashSet<String>(
								Arrays.asList(rhs.substring(
										rhs.indexOf('{') + 1, rhs.indexOf('}'))
										.split("\\s+")));
						atomicValues.addAll(vals);
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
							atomicValues.addAll(valSet);
							if (valSetsString.indexOf('}') == valSetsString
									.length() - 1) {
								break;
							} else {
								valSetsString = valSetsString
										.substring(valSetsString.indexOf('}') + 1);
							}
						}
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
				String rhs = pair[1].trim();
				if (rhs.indexOf('{') >= 0) {
					
					if (conjunct.contains(" in ")) {
						HashSet<String> vals = new HashSet<String>(
								Arrays.asList(rhs.substring(
										rhs.indexOf('{') + 1, rhs.indexOf('}'))
										.split("\\s+")));
						atomicValues.addAll(vals);
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

						String valSetsString = rhs.substring(1,
								rhs.length() - 1);
						while (valSetsString.indexOf('}') >= 0) {
							String subString = valSetsString.substring(
									valSetsString.indexOf('{') + 1,
									valSetsString.indexOf('}'));
							HashSet<String> valSet = new HashSet<String>(
									Arrays.asList(subString.trim()
											.split("\\s+")));
							atomicValues.addAll(valSet);
							if (valSetsString.indexOf('}') == valSetsString
									.length() - 1) {
								break;
							} else {
								valSetsString = valSetsString
										.substring(valSetsString.indexOf('}') + 1);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Parses atomic values from the input file
	 * 
	 * @param inputFile
	 *            : name of the input file to be processed
	 */
	public static void parseAtomicValues(String inputFile) {
		
		FileReader fileReader;
		BufferedReader inputReader;
		try {		
			fileReader = new FileReader(inputFile);
			inputReader = new BufferedReader(fileReader);
			String line;
			// sequentially parse each line
			while ((line = inputReader.readLine()) != null) {
				if (line.matches(Parser.RulePattern.commentPattern)) {
					// match comment
					continue;
				} else if (line.matches(Parser.RulePattern.userAttrPattern)) {
					// gets atomic values from user attributes
					getAtomicValuesAttr(line);
				} else if (line.matches(Parser.RulePattern.permAttrPattern)) {
					// gets atomic values from resource attributes
					getAtomicValuesAttr(line);
				} else if (line.matches(Parser.RulePattern.rulePattern)) {
					// gets atomic values from rules
					getAtomicValuesRule(line);
				} else if (line.matches(Parser.RulePattern.unrevUserAttrPattern)) {
					continue;
				} else if (line.matches(Parser.RulePattern.unrevResourceAttrPattern)) {
					continue;
				}
			}
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
}