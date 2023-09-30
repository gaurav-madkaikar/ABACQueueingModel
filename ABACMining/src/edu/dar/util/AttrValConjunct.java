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

public class AttrValConjunct extends Conjunct implements
		Comparable<AttrValConjunct> {
	private String attr;
	private HashSet<String> val;
	private HashSet<HashSet<String>> setVal;
	private String operator;

	public AttrValConjunct(String attr, HashSet<String> val,
			HashSet<HashSet<String>> setVal, String operator) {
		this.attr = attr;
		this.val = val;
		this.setVal = setVal;
		this.operator = operator;
	}

	public AttrValConjunct(AttrValConjunct c) {
		this.attr = c.getLHS();
		if (c.getRHS() != null) {
			this.val = new HashSet<String>(c.getRHS());
		}
		if (c.getRHSet() != null) {
			this.setVal = new HashSet<HashSet<String>>(c.getRHSet());
		}
		this.operator = c.operator;
	}

	@Override
	public String getLHS() {
		return attr;
	}

	@Override
	public HashSet<String> getRHS() {
		return val;
	}

	public HashSet<HashSet<String>> getRHSet() {
		return setVal;
	}

	@Override
	public Object getOperator() {
		return this.operator;
	}

	@Override
	public String toString() {
		if (this.val != null) {
			// if (this.val.size() == 1) {
			// return this.attr + " = "
			// + removeDelimiters(this.val.toString());
			// } else {
			StringBuilder result = new StringBuilder(this.attr + " in {");
			int valSize = 0;
			for (String v : this.val) {
				result.append(v);
				if (++valSize < this.val.size()) {
					result.append(" ");
				}
			}
			result.append("}");
			return result.toString();
			// return this.attr
			// + " in "
			// + this.val.toString().replace('[', '{')
			// .replace(']', '}');
			// }
		} else {
			StringBuilder result = new StringBuilder(this.attr
					+ " supseteqln {");
			int setSize = 0;
			for (HashSet<String> s : this.setVal) {
				int valSize = 0;
				result.append("{");
				for (String v : s) {
					result.append(v);
					if (++valSize < s.size()) {
						result.append(" ");
					}
				}
				result.append("}");
				if (++setSize < this.setVal.size()) {
					result.append(" ");
				}
			}
			result.append("}");
			return result.toString();

			// return this.attr
			// + " supseteqln "
			// + this.setVal.toString().replace('[', '{')
			// .replace(']', '}');
		}
	}

	static String removeDelimiters(String s) {
		return s.substring(1, s.length() - 1);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof AttrValConjunct)) {
			return false;
		}
		AttrValConjunct c = (AttrValConjunct) obj;
		if (!this.getLHS().equals(c.getLHS())) {
			return false;
		}
		if (this.getRHS() != null
				&& (c.getRHS() == null || !this.getRHS().equals(c.getRHS()))) {
			return false;
		}
		if (this.getRHSet() != null
				&& (c.getRHSet() == null || !this.getRHSet().equals(
						c.getRHSet()))) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(AttrValConjunct obj) {
		if (obj == this) {
			return 0;
		}
		return this.getLHS().compareTo(obj.getLHS());
	}
}
