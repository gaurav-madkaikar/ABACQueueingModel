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

public class RuleQualityValue implements Comparable<RuleQualityValue>{
	public double firstComponent;
	public double secondComponent;
	public double thirdComponent;
	public RuleQualityValue(double v1, double v2, double v3) {
		firstComponent = v1;
		secondComponent = v2;
		thirdComponent = v3;
	}
	public RuleQualityValue() {
		firstComponent = 0.0;
		secondComponent = 0.0;
	}
	
	public RuleQualityValue(RuleQualityValue v) {
		firstComponent = v.firstComponent;
		secondComponent = v.secondComponent;
		thirdComponent = v.thirdComponent;
	}
	@Override
	public int compareTo(RuleQualityValue v) {
		if (this == v) {
			return 0;
		}
		
		if (v == null) {
			return 1;
		}
		
		if (Double.compare(this.firstComponent, v.firstComponent) > 0) {
			return 1;
		}
		if (Double.compare(this.firstComponent, v.firstComponent) < 0) {
			return -1;
		}
		if (Double.compare(this.secondComponent, v.secondComponent) > 0) {
			return 1;
		}
		if (Double.compare(this.secondComponent, v.secondComponent) < 0) {
			return -1;
		}	
		if (Double.compare(this.thirdComponent, v.thirdComponent) > 0) {
			return 1;
		}
		if (Double.compare(this.thirdComponent, v.thirdComponent) < 0) {
			return -1;
		}	
		return 0;
	}
}
