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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Configuration class is used for maintaining the configuration of various
 * input parameters
 * 
 * @author Zhongyuan Xu
 * 
 */

public class Config {
	/* the following fileds are created for new parser */
	private HashSet<String> users;
	private HashSet<String> resources;
	// a permission is a pair of <resource, operation>
	private HashSet<Pair<String, String>> perms;
	private HashSet<String> ops;

	private HashMap<String, Attribute> userAttrSet;
	private HashMap<String, Attribute> resourceAttrSet;

	private HashMap<String, HashMap<String, HashSet<String>>> userAttrInfo;
	private HashMap<String, HashMap<String, HashSet<String>>> resourceAttrInfo;

	private ArrayList<Rule> ruleList;

	private HashMap<String, HashSet<Pair<String, String>>> userPerms;
	private HashMap<Pair<String, String>, HashSet<String>> permUsers;

	private HashMap<String, HashSet<String>> resourceUsers;
	private HashMap<String, HashSet<String>> userResources;

	private HashSet<Triple<String, String, String>> coveredUP;
	
	private HashSet<Triple<String, String, String>> overassignmentUP;
	
	private HashSet<Triple<String, String, String>> detectedOverassignmentUP;
	
	private HashSet<Triple<String, String, String>> underassignmentUP;
	
	private HashSet<Triple<String, String, String>> detectedUnderassignmentUP;
	// metric 1.b
	public HashMap<HashSet<AttrAttrConjunct>, Integer> constraintsToNumRules;
	// metric 1.d
	public HashMap<HashSet<AttrAttrConjunct>, Integer> constraintsToNumUP;
	
	public HashMap<AttrAttrConjunct, Integer> constraintToNumUP;
	
	public Config() {
		users = new HashSet<String>();
		resources = new HashSet<String>();
		ops = new HashSet<String>();
		perms = new HashSet<Pair<String, String>>();

		userAttrInfo = new HashMap<String, HashMap<String, HashSet<String>>>();
		resourceAttrInfo = new HashMap<String, HashMap<String, HashSet<String>>>();

		userAttrSet = new HashMap<String, Attribute>();
		resourceAttrSet = new HashMap<String, Attribute>();

		ruleList = new ArrayList<Rule>();

		userPerms = new HashMap<String, HashSet<Pair<String, String>>>();
		permUsers = new HashMap<Pair<String, String>, HashSet<String>>();

		resourceUsers = new HashMap<String, HashSet<String>>();
		userResources = new HashMap<String, HashSet<String>>();

		coveredUP = new HashSet<Triple<String, String, String>>();
		overassignmentUP = new HashSet<Triple<String, String, String>>();
		underassignmentUP = new HashSet<Triple<String, String, String>>();
		
		detectedOverassignmentUP = new HashSet<Triple<String, String, String>>();
		detectedUnderassignmentUP = new HashSet<Triple<String, String, String>>();
		
		constraintsToNumUP = new HashMap<HashSet<AttrAttrConjunct>, Integer>();
		constraintToNumUP = new HashMap<AttrAttrConjunct, Integer>();
		constraintsToNumRules = new HashMap<HashSet<AttrAttrConjunct>, Integer>();
	}

	public int getNumUsers() {
		return users.size();
	}

	public int getNumPerms() {
		return perms.size();
	}

	public int getNumUserAttribs() {
		return userAttrSet.size();
	}

	public int getNumPermAttribs() {
		return resourceAttrSet.size();
	}

	public void addUser(String user) {
		this.users.add(user);
	}

	public void addPerm(Pair<String, String> perm) {
		this.perms.add(perm);
	}

	public void addResource(String resource) {
		this.resources.add(resource);
	}

	public HashSet<String> getUsers() {
		return users;
	}

	public HashSet<Pair<String, String>> getPerms() {
		return perms;
	}

	public HashSet<String> getResources() {
		return resources;
	}

	public void setResources(HashSet<String> resources) {
		this.resources = resources;
	}

	public HashSet<String> getOps() {
		return ops;
	}

	public void setOps(HashSet<String> ops) {
		this.ops = ops;
	}

	public HashMap<String, HashMap<String, HashSet<String>>> getUserAttrInfo() {
		return userAttrInfo;
	}

	public HashMap<String, HashMap<String, HashSet<String>>> getResourceAttrInfo() {
		return resourceAttrInfo;
	}

	public HashMap<String, Attribute> getUserAttrSet() {
		return userAttrSet;
	}

	public HashMap<String, Attribute> getResourceAttrSet() {
		return resourceAttrSet;
	}

	public ArrayList<Rule> getRuleList() {
		return ruleList;
	}

	public void setRuleList(ArrayList<Rule> ruleList) {
		this.ruleList = ruleList;
	}

	public HashMap<String, HashSet<Pair<String, String>>> getUserPerms() {
		return userPerms;
	}

	public void setUserPerms(
			HashMap<String, HashSet<Pair<String, String>>> userPerms) {
		this.userPerms = userPerms;
	}

	public HashMap<Pair<String, String>, HashSet<String>> getPermUsers() {
		return permUsers;
	}

	public void setPermUsers(
			HashMap<Pair<String, String>, HashSet<String>> permUsers) {
		this.permUsers = permUsers;
	}

	public HashSet<Triple<String, String, String>> getCoveredUP() {
		return coveredUP;
	}

	public void setCoveredUP(HashSet<Triple<String, String, String>> coveredUP) {
		this.coveredUP = coveredUP;
	}

	public HashMap<String, HashSet<String>> getResourceUsers() {
		return resourceUsers;
	}

	public void setResourceUsers(HashMap<String, HashSet<String>> resourceUsers) {
		this.resourceUsers = resourceUsers;
	}

	public HashMap<String, HashSet<String>> getUserResources() {
		return userResources;
	}

	public void setUserResources(HashMap<String, HashSet<String>> userResources) {
		this.userResources = userResources;
	}

	public HashSet<Triple<String, String, String>> getOverassignmentUP() {
		return overassignmentUP;
	}

	public void setOverassignmentUP(
			HashSet<Triple<String, String, String>> overassignmentUP) {
		this.overassignmentUP = overassignmentUP;
	}

	public HashSet<Triple<String, String, String>> getUnderassignmentUP() {
		return underassignmentUP;
	}

	public void setUnderassignmentUP(
			HashSet<Triple<String, String, String>> underassignmentUP) {
		this.underassignmentUP = underassignmentUP;
	}

	public HashSet<Triple<String, String, String>> getDetectedOverassignmentUP() {
		return detectedOverassignmentUP;
	}

	public void setDetectedOverassignmentUP(
			HashSet<Triple<String, String, String>> detectedOverassignmentUP) {
		this.detectedOverassignmentUP = detectedOverassignmentUP;
	}

	public HashSet<Triple<String, String, String>> getDetectedUnderassignmentUP() {
		return detectedUnderassignmentUP;
	}

	public void setDetectedUnderassignmentUP(
			HashSet<Triple<String, String, String>> detectedUnderassignmentUP) {
		this.detectedUnderassignmentUP = detectedUnderassignmentUP;
	}
	
	public int numDistintPerms() {
		HashSet<Pair<String, String>> permSet = new HashSet<Pair<String, String>>();
		for (Triple<String, String, String> perm : this.getCoveredUP()) {
			permSet.add(new Pair<String, String>(perm.getSecond(), perm.getThird()));
		}
		return permSet.size(); 
	}
}
