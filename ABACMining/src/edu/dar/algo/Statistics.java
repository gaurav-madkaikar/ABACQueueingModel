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

import java.util.ArrayList;

import edu.dar.util.Config;
import edu.dar.util.Parser;
import edu.dar.util.Rule;

public class Statistics {
	public static void main(String[] args) {

		Parser.parseInputFile(args[0]);
		System.out.println(Parser.config.getUsers().size() + " & "
				+ Parser.config.getResources().size() + " & "
				+ Parser.config.getOps().size() + " & "
				+ Parser.config.getCoveredUP().size() + " & "
				+ Parser.config.getUserAttrSet().keySet().size() + " & "
				+ Parser.config.getResourceAttrSet().keySet().size() + " & "
				+ Parser.config.getRuleList().size() + " & " +  computeAverageOverlap(Parser.config.getRuleList(), Parser.config));
	}
	
	public static double computeAverageOverlap(ArrayList<Rule> rules, Config config) {
		if (rules.size() <= 1) {
			return 0.0;
		}
		int totalOverlap = 0;
		for (int i = 0; i < rules.size(); i++) {
			for (int j = i + 1; j < rules.size(); j++) {
				totalOverlap += ABACMiner.computeRuleOverlap(rules.get(i), rules.get(j), config).size();
			}
		}
		return (double) totalOverlap / (rules.size() * (rules.size() - 1)) ;
	}
}
