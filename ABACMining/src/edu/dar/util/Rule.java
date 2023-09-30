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
import java.util.HashSet;

public class Rule {
	private ArrayList<AttrValConjunct> uae;
	private ArrayList<AttrValConjunct> rae;
	private ArrayList<AttrAttrConjunct> con;
	private HashSet<String> satUsers;
	private HashSet<String> satResources;
	private HashSet<String> ops;
	private RuleQualityValue quality;
	private HashSet<Triple<String, String, String>> coveredUPTriple;
	private boolean changed;
	private boolean uaeChanged;
	private boolean raeChanged;
	
	private boolean isCompanion;
	
	private HashSet<String> usedUserAttrs;
	private HashSet<String> usedResrAttrs;
	
	public Rule() {
		uae = new ArrayList<AttrValConjunct>();
		rae = new ArrayList<AttrValConjunct>();
		con = new ArrayList<AttrAttrConjunct>();
		ops = new HashSet<String>();
		changed = false;
		uaeChanged = false;
		raeChanged = false;
		satUsers = new HashSet<String>();
		satResources = new HashSet<String>();
		usedUserAttrs = new HashSet<String>();
		usedResrAttrs = new HashSet<String>();
		isCompanion = false;
	}

	public Rule(Rule r) {
		this.con = new ArrayList<AttrAttrConjunct>();
		for (AttrAttrConjunct c : r.getCon()) {
			this.con.add(new AttrAttrConjunct(c));
		}
		this.uae = new ArrayList<AttrValConjunct>();
		for (AttrValConjunct c : r.getUAE()) {
			this.uae.add(new AttrValConjunct(c));
		}
		this.rae = new ArrayList<AttrValConjunct>();
		for (AttrValConjunct c : r.getRAE()) {
			this.rae.add(new AttrValConjunct(c));
		}
		this.ops = new HashSet<String>(r.getOps());
		this.changed = r.isChanged();
		this.uaeChanged = r.isUaeChanged();
		this.raeChanged = r.isRaeChanged();
		this.satUsers = new HashSet<String>(r.getSatUsers());
		this.satResources = new HashSet<String>(r.getSatResources());
		this.coveredUPTriple = new HashSet<Triple<String, String, String>>(r.getCoveredUPTriple());
		this.usedUserAttrs = new HashSet<String>(r.getUsedUserAttrs());
		this.usedResrAttrs = new HashSet<String>(r.getUsedResrAttrs());
		this.isCompanion = r.isCompanion();
	}

	public ArrayList<AttrValConjunct> getUAE() {
		return uae;
	}

	public void setUAE(ArrayList<AttrValConjunct> uConjunctList) {
		this.uae = uConjunctList;
	}

	public ArrayList<AttrValConjunct> getRAE() {
		return rae;
	}

	public void setRAE(ArrayList<AttrValConjunct> pConjunctList) {
		this.rae = pConjunctList;
	}

	public ArrayList<AttrAttrConjunct> getCon() {
		return con;
	}

	public void setCon(ArrayList<AttrAttrConjunct> constraintList) {
		this.con = constraintList;
	}
	
	public HashSet<String> getOps() {
		return ops;
	}

	public void setOps(HashSet<String> ops) {
		this.ops = ops;
	}

	public void adduConjunct(AttrValConjunct c) {
		this.uae.add(c);
	}

	public void addrConjunct(AttrValConjunct c) {
		this.rae.add(c);
	}

	public void addConstraint(AttrAttrConjunct c) {
		this.con.add(c);
	}

	public void addConstraints(ArrayList<AttrAttrConjunct> constraints) {
		this.con.addAll(constraints);
	}

	@Override
	public String toString() {
		StringBuilder opString = new StringBuilder();
		for (String op : ops) {
			opString.append(op + " ");
		}
		
		return "rule(" + removeDelimiters(uae.toString()) + "; "
				+ removeDelimiters(rae.toString()) + "; "
				+ "{" + opString.toString() + "}" + "; "
				+ removeDelimiters(con.toString()) + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Rule)) {
			return false;
		}

		Rule r = (Rule) obj;
		if (!this.getUAE().equals(r.getUAE())) {
			return false;
		}
		if (!this.getRAE().equals(r.getRAE())) {
			return false;
		}
		if (!this.getOps().equals(r.getOps())) {
			return false;
		}	
		if (!this.getCon().equals(r.getCon())) {
			return false;
		}
		return true;
	}

	public String removeDelimiters(String s) {
		return s.substring(1, s.length() - 1);
	}
	
	public int getMaxUAESize() {
		int maxSize = 0;
		for (AttrValConjunct c : this.getUAE()) {
			if (c.getRHS() != null) {
				maxSize = maxSize >= c.getRHS().size() ? maxSize : c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				int size = 0;
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
				maxSize = maxSize >= size ? maxSize : size;
			}
		}
		return maxSize;
	}
	
	public int getMaxRAESize() {
		int maxSize = 0;
		for (AttrValConjunct c : this.getRAE()) {
			if (c.getRHS() != null) {
				maxSize = maxSize >= c.getRHS().size() ? maxSize : c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				int size = 0;
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
				maxSize = maxSize >= size ? maxSize : size;
			}
		}
		return maxSize;
	}
	
	public int getUAESize() {
		int size = 0;
		for (AttrValConjunct c : this.getUAE()) {
			if (c.getRHS() != null) {
				size += c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
		}
		return size;
	}
	
	public int getRAESize() {
		int size = 0;
		for (AttrValConjunct c : this.getRAE()) {
			if (c.getRHS() != null) {
				size += c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
		}
		return size;
	}

	public int getConjunctSize() {
		int size = 0;
		for (AttrValConjunct c : this.getUAE()) {
			if (c.getRHS() != null) {
				size += c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
		}
		for (AttrValConjunct c : this.getRAE()) {
			if (c.getRHS() != null) {
				size += c.getRHS().size();
			}
			if (c.getRHSet() != null) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
		}
		return size;
	}

	public double getSize() {
		return this.getConjunctSize() + this.getCon().size() + this.getOps().size();
	}
	
	public int getConjunctSize(Config config) {
		int size = 0;
		for (AttrValConjunct c : this.getUAE()) {
			if (c.getRHS() != null && !config.getUserAttrSet().get(c.getLHS()).getDomain().equals(c.getRHS())) {
				size += c.getRHS().size();
			}
//			if (c.getRHS() != null && config.getUserAttrSet().get(c.getLHS()).getDomain().equals(c.getRHS())) {
//				System.out.println(config.getUserAttrSet().get(c.getLHS()).getDomain());
//			}
			
			if (c.getRHSet() != null && !config.getUserAttrSet().get(c.getLHS()).getSetDomain().equals(c.getRHSet())) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
//			if (c.getRHSet() != null && config.getUserAttrSet().get(c.getLHS()).getSetDomain().equals(c.getRHSet())) {
//				System.out.println(config.getUserAttrSet().get(c.getLHS()).getSetDomain());
//			}
		}
		for (AttrValConjunct c : this.getRAE()) {
			if (c.getRHS() != null && !config.getResourceAttrSet().get(c.getLHS()).getDomain().equals(c.getRHS())) {
				size += c.getRHS().size();
			}
//			if (c.getRHS() != null && config.getResourceAttrSet().get(c.getLHS()).getDomain().equals(c.getRHS())) {
//				System.out.println(config.getResourceAttrSet().get(c.getLHS()).getDomain());
//			}
			if (c.getRHSet() != null && !config.getResourceAttrSet().get(c.getLHS()).getSetDomain().equals(c.getRHSet())) {
				for (HashSet<String> s : c.getRHSet()) {
					size += s.size();
				}
			}
//			if (c.getRHSet() != null && config.getResourceAttrSet().get(c.getLHS()).getSetDomain().equals(c.getRHSet())) {
//				System.out.println(config.getResourceAttrSet().get(c.getLHS()).getSetDomain());
//			}
		}
		return size;
	}
	
	public double getRuleSize(Config config) {
		return this.getConjunctSize(config) + this.getCon().size() + this.getOps().size();
	}

	public HashSet<String> getUserAttrs() {
		HashSet<String> userAttrs = new HashSet<String>();
		for (AttrValConjunct c : uae) {
			userAttrs.add(c.getLHS());
		}
		return userAttrs;
	}

	public HashSet<String> getPermAttrs() {
		HashSet<String> permAttrs = new HashSet<String>();
		for (AttrValConjunct c : rae) {
			permAttrs.add(c.getLHS());
		}
		return permAttrs;
	}

	public RuleQualityValue getQuality() {
		return quality;
	}

	public void setQuality(RuleQualityValue quality) {
		this.quality = quality;
	}

	public HashSet<Triple<String, String, String>> getCoveredUPTriple() {
		return coveredUPTriple;
	}

	public void setCoveredUPTriple(
			HashSet<Triple<String, String, String>> coveredUPTriple) {
		this.coveredUPTriple = coveredUPTriple;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public HashSet<String> getSatUsers() {
		return satUsers;
	}

	public void setSatUsers(HashSet<String> satUsers) {
		this.satUsers = satUsers;
	}

	public HashSet<String> getSatResources() {
		return satResources;
	}

	public void setSatResources(HashSet<String> satResources) {
		this.satResources = satResources;
	}

	public boolean isUaeChanged() {
		return uaeChanged;
	}

	public void setUaeChanged(boolean uaeChanged) {
		this.uaeChanged = uaeChanged;
	}

	public boolean isCompanion() {
		return isCompanion;
	}

	public void setCompanion(boolean isCompanion) {
		this.isCompanion = isCompanion;
	}

	public boolean isRaeChanged() {
		return raeChanged;
	}

	public void setRaeChanged(boolean paeChanged) {
		this.raeChanged = paeChanged;
	}
	
	public HashSet<String> getUsedUserAttrs() {
		return usedUserAttrs;
	}

	public void setUsedUserAttrs(HashSet<String> usedUserAttrs) {
		this.usedUserAttrs = usedUserAttrs;
	}

	public HashSet<String> getUsedResrAttrs() {
		return usedResrAttrs;
	}

	public void setUsedResrAttrs(HashSet<String> usedResrAttrs) {
		this.usedResrAttrs = usedResrAttrs;
	}
}
