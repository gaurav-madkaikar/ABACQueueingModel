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


/**
 * Generic class Pair to represent a pair of elements 
 * @param <T>
 *            generic type of the first element
 * @param <U>
 *            generic type of the second element
 */
public class Pair<T, U> implements Comparable<Pair<T, U>> {
	private T first;
	private U second;
	private transient final int hash;

	public Pair(T f, U s) {
		this.first = f;
		this.second = s;
		hash = (first == null ? 0 : first.hashCode() * 31)
				+ (second == null ? 0 : second.hashCode());
	}

	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public U getSecond() {
		return second;
	}

	public void setSecond(U second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(Object oth) {
		if (this == oth) {
			return true;
		}
		if (oth == null || !(getClass().isInstance(oth))) {
			return false;
		}
		@SuppressWarnings("unchecked")
		Pair<T, U> other = getClass().cast(oth);
		return (first == null ? other.first == null : first.equals(other.first))
				&& (second == null ? other.second == null : second
						.equals(other.second));
	}

	public int compareTo(Pair<T, U> p) {
		if (this.first.equals(p.getFirst())
				&& this.second.equals(p.getSecond()))
			return 0;

		else if ((Integer) this.first < (Integer) p.getFirst()
				|| ((Integer) this.first == (Integer) p.getFirst() && (Integer) this.second < (Integer) p
						.getSecond()))
			return -1;
		else
			return 1;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(first.toString() + "," +  second.toString() + ")");
		return s.toString();
	}
}
