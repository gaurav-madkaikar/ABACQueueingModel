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

public class AttrAttrConjunct extends Conjunct implements Comparable<AttrAttrConjunct> {
	private String lattr;
	private String rattr;
	private Operator operator;
	
	public AttrAttrConjunct(String lattr, String rattr, Operator operator) {
		this.lattr = lattr;
		this.rattr = rattr;
		this.operator = operator;
		
	}
	
	public AttrAttrConjunct(AttrAttrConjunct c) {
		this.lattr = c.getLHS();
		this.rattr = c.getRHS();
		this.operator = c.getOperator();
		
	}

	@Override
	public String getLHS() {
		return this.lattr;
	}

	@Override
	public String getRHS() {
		return this.rattr;
	}
	

	@Override
	public Operator getOperator() {
		return this.operator;
	}
	
	public void setOperator(Operator op) {
		this.operator = op;
	}

	@Override
	public String toString() {
		String op;
		switch (this.operator) {
		case EQUALS:
			op = "=";
			break;
		case SUBSETEQ:
			op = " < ";
			break;
		case SUPSETEQ:
			op = " > ";
			break;
		case IN:
			op = " ] ";
			break;
		default:
			op = "";
		}
		return this.lattr + op + this.rattr;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AttrAttrConjunct)) {
			return false;
		}
		AttrAttrConjunct c = (AttrAttrConjunct) obj;
		if (!this.getLHS().equals(c.getLHS())) {
			return false;
		}
		if (!this.getRHS().equals(c.getRHS())) {
			return false;
		}
		if (!this.getOperator().equals(c.getOperator())) {
			return false;
		}
		return true;
	}
	
	public boolean compatible(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AttrAttrConjunct)) {
			return false;
		}
		AttrAttrConjunct c = (AttrAttrConjunct) obj;
		if (!this.getLHS().equals(c.getLHS())) {
			return false;
		}
		if (!this.getRHS().equals(c.getRHS())) {
			return false;
		}
		if (!(this.getOperator() == Operator.EQUALS || this.getOperator() == Operator.EQUALS)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(AttrAttrConjunct obj) {
		if (!this.getLHS().equals(obj)) {
			return this.getLHS().compareTo(obj.getLHS());
		} else {
			return this.getRHS().compareTo(obj.getRHS());
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		int result = 17;
		result += this.lattr.hashCode() * 31;
		result += this.operator.hashCode() * 31;
		result += this.rattr.hashCode() * 31;
		return result;
	}
}
