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

import java.util.HashSet;
import java.util.Set;



public class Attribute {
	// type of attribute
	private AttributeType aType;
	// type of attribute value type
	private ValueType vType;
	// name of attribtue
	private String name;
	// attribute value can be a single value or a set
	private HashSet<String> domain;
	// attribute value can be a set 
	private HashSet<HashSet<String>> setDomain;
	
	private boolean isUnremovable;

	public Attribute(AttributeType aType, ValueType vType, String name, boolean isMandatory) {
		this.aType = aType;
		this.vType = vType;
		this.name = name;
		this.isUnremovable = isMandatory;
	}

	// getters and setters
	public AttributeType getType() {
		return aType;
	}

	public void setType(AttributeType type) {
		this.aType = type;
	}
	
	public ValueType getvType() {
		return vType;
	}

	public void setvType(ValueType vType) {
		this.vType = vType;
	}

	public HashSet<String> getDomain() {
		return domain;
	}

	public void setValue(HashSet<String> domain) {
		this.domain = domain;
	}
	
	public HashSet<HashSet<String>> getSetDomain() {
		return setDomain;
	}

	public void setSetDomain(HashSet<HashSet<String>> setDomain) {
		this.setDomain = setDomain;
	}

	// add a value to the value set
	public boolean addValue(String val) {
		if (this.domain == null) {
			this.domain = new HashSet<String>();
		}
		return this.domain.add(val);
	}
	
	public boolean addValues(Set<String> vals) {
		if (this.domain == null) {
			this.domain = new HashSet<String>();
		}
		return this.domain.addAll(vals);
	}
	
	public boolean addValue(HashSet<String> set) {
		if (this.setDomain == null) {
			this.setDomain = new HashSet<HashSet<String>>();
		}
		return this.setDomain.add(set);
	}

	// remove a value from the value set
	public boolean removeValue(String val) {
		if (this.domain == null) {
			return false;
		}
		return this.domain.remove(val);
	}
	
	public boolean removeValue(HashSet<String> set) {
		if (this.setDomain == null) {
			return false;
		}
		return this.setDomain.remove(set);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isUnremovable() {
		return isUnremovable;
	}

	public void setUnremovable(boolean isMandatory) {
		this.isUnremovable = isMandatory;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof Attribute)) {
			return false;
		}
		Attribute attr = (Attribute) obj;
		return this.getName().equals(attr.getName())
				&& this.getType().equals(attr.getType());
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		switch (this.aType) {
		case UserAttr:
			str.append("u_");
			break;
		case ResourceAttr:
			str.append("r_");
			break;
		default:
			break;
		}
		switch (this.vType) {
		case Single:
			str.append("single_");
			break;
		case Set:
			str.append("set_");
			break;
		default:
			break;
		}
		str.append(this.name);
		if (this.domain != null) {
			str.append("={").append(domain.toString()).append("}");
		}
		if (this.setDomain != null) {
			str.append("={").append(setDomain.toString()).append("}");
		}
		str.append("_" + isUnremovable);
		return str.toString();
	}

	@Override
	public int hashCode() {
		return this.name.hashCode()*31 + this.getType().hashCode();
	}
}
