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
 * 
 * @param <T>
 *            generic type of the first element
 * @param <U>
 *            generic type of the second element
 */
public class Triple<T, U, E> {
	private T first;
	private U second;
	private E third;
	private transient final int hash;

	public Triple(T f, U s, E e) {
		this.first = f;
		this.second = s;
		this.third = e;
		hash = (first == null ? 0 : first.hashCode() * 31)
				+ (second == null ? 0 : second.hashCode() * 17)
				+ (third == null ? 0 : third.hashCode());
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

	public E getThird() {
		return third;
	}

	public void setThird(E third) {
		this.third = third;
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
		Triple<T, U, E> other = getClass().cast(oth);
		return (first == null ? other.first == null : first.equals(other.first))
				&& (second == null ? other.second == null : second
						.equals(other.second)
						&& (third == null ? other.third == null : third
								.equals(other.third)));
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("(");
		s.append(first.toString() + "," + second.toString() + "," + third.toString() + ")");
		return s.toString();
	}
}
