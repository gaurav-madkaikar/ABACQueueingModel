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

import java.util.Comparator;

public class RulePairComparator implements Comparator<Pair<Rule, Rule>> {

	@Override
	public int compare(Pair<Rule, Rule> p1, Pair<Rule, Rule> p2) {
		if (p1 == p2) {
			return 0;
		}

		if (p1.getFirst().getQuality() == null || p1.getSecond().getQuality() == null || p2.getFirst().getQuality() == null || p2.getSecond().getQuality() == null) {
			return 0;
		}
//		int maxUPSize1 = p1.getFirst().getCoveredUPTriple().size() > p1
//				.getSecond().getCoveredUPTriple().size() ? p1.getFirst()
//				.getCoveredUPTriple().size() : p1.getSecond().getCoveredUPTriple()
//				.size();
//		int minUPSize1 = p1.getFirst().getCoveredUPTriple().size() < p1
//				.getSecond().getCoveredUPTriple().size() ? p1.getFirst()
//				.getCoveredUPTriple().size() : p1.getSecond().getCoveredUPTriple()
//				.size();
//
//		int maxUPSize2 = p2.getFirst().getCoveredUPTriple().size() > p2
//				.getSecond().getCoveredUPTriple().size() ? p2.getFirst()
//				.getCoveredUPTriple().size() : p2.getSecond().getCoveredUPTriple()
//				.size();
//		int minUPSize2 = p2.getFirst().getCoveredUPTriple().size() < p2
//				.getSecond().getCoveredUPTriple().size() ? p2.getFirst()
//				.getCoveredUPTriple().size() : p2.getSecond().getCoveredUPTriple()
//				.size();
//
//		if (maxUPSize1 > maxUPSize2 || (maxUPSize1 == maxUPSize2 && minUPSize1 > minUPSize2)) {
//			return -1;
//		} else if (maxUPSize1 < maxUPSize2 || (maxUPSize1 == maxUPSize2 && minUPSize1 < minUPSize2)) {
//			return 1;
//		}
		
		if (p1.getFirst().getQuality().compareTo(p2.getFirst().getQuality()) > 0 || (p1.getFirst().getQuality().compareTo(p2.getFirst().getQuality())==0
				&& p1.getSecond().getQuality().compareTo(p2.getSecond().getQuality()) > 0)) {
			return -1;
		}
		
		if (p1.getFirst().getQuality().compareTo(p2.getFirst().getQuality()) < 0 || (p1.getFirst().getQuality().compareTo(p2.getFirst().getQuality())==0
				&& p1.getSecond().getQuality().compareTo(p2.getSecond().getQuality()) < 0)) {
			return 1;
		}
		/*
		 * if (p1.getFirst().getCon().size() > p2.getFirst().getCon().size()) {
		 * return 1; } else if (p1.getFirst().getCon().size() <
		 * p2.getFirst().getCon().size()) { return -1; }
		 */

		return 0;
	}
}
