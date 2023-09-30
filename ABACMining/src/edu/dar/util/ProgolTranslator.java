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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;

public class ProgolTranslator {
	
	public static final String STATIC_SETTING_FILE = "../progol/static/setting-static.pl";
	
	public static final String STATIC_BACKGROUND_FILE = "../progol/static/background-static.pl";
	
	public static final String STATIC_MODE_FILE = "../progol/static/mode-static.pl";
	
	public static String translateUserAttrib(String a) {
		return a + "U";
	}

	public static String translateResAttrib(String a) {
		return a + "R";
	}

	public static String translateConst(String v) {
		return v.toLowerCase().replace('-', '_');
	}

	public static String addType(String a) {
		return a + "Type";
	}
	
	public static String addSetType(String a) {
		return a + "SetType";
	}
	
	public static void translateABACToProgol(Config config, String outputFile, boolean attribVar) {
		try {
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("%Settings\n");
			int nodes =(int) (config.getUsers().size() * config.getResources().size() * config.getOps().size() * 1.6);
			out.write(":- set(nodes," + nodes + ")?\n");
			
			// read and write static setting file
			BufferedReader br = new BufferedReader(new FileReader(STATIC_SETTING_FILE));
			String line = null;
			while ((line = br.readLine()) != null) {
				out.write(line + "\n");
			}
			br.close();
			
			out.write("%Mode Declarations\n");
			br = new BufferedReader(new FileReader(STATIC_MODE_FILE));
			line = null;
			while ((line = br.readLine()) != null) {
				out.write(line + "\n");
			}
			br.close();
			// up is the predicate that we want to learn
			//out.write(":- modeh(1,up(+user,+resource,+operation))?\n");
			out.write(":- modeh(1,up(+user,+resource,#operation))?\n");

			for (String a : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(a);
				if (uAttr.getvType() == ValueType.Single) {
					if (attribVar) {
					out.write(":- modeb(1," + translateUserAttrib(a)
							+ "(+user,-attribValAtomic))?\n");
					}
					out.write(":- modeb(1," + translateUserAttrib(a)
							+ "(+user,#" + addType(translateUserAttrib(a)) + "))?\n");
				}
				if (uAttr.getvType() == ValueType.Set) {
					if (attribVar) {
					out.write(":- modeb(*," + translateUserAttrib(a)			
							+ "(+user,-attribValAtomic))?\n");
					}
					out.write(":- modeb(*," + translateUserAttrib(a)
							+ "(+user,#" + addType(translateUserAttrib(a)) + "))?\n");
				}
			}

			for (String a : config.getResourceAttrSet().keySet()) {
				Attribute rAttr = config.getResourceAttrSet().get(a);
				if (rAttr.getvType() == ValueType.Single) {
					if (attribVar) {
					out.write(":- modeb(1," + translateResAttrib(a)
							+ "(+resource,-attribValAtomic))?\n");	
					}
					
					out.write(":- modeb(1," + translateResAttrib(a)		
							+ "(+resource,#" + addType(translateResAttrib(a)) + "))?\n");
					
				}
				if (rAttr.getvType() == ValueType.Set) {
					if (attribVar) {
					out.write(":- modeb(*," + translateResAttrib(a)
							+ "(+resource,-attribValAtomic))?\n");
					}
					out.write(":- modeb(1," + translateResAttrib(a)
							+ "(+resource,#" + addType(translateResAttrib(a)) + "))?\n");
				}
			}

			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Set) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Set) {
							String p = translateUserAttrib(au) + "_superset_"
									+ translateResAttrib(ar);
							out.write(":- modeb(1," + p
									+ "(+user,+resource))?\n");
						}
					}
				}
			}
			
			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Set) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Single) {
							String p = translateUserAttrib(au) + "_contains_"
									+ translateResAttrib(ar);
							out.write(":- modeb(1," + p
									+ "(+user,+resource))?\n");
						}
					}
				}
			}
			
			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Single) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Single) {
							String p = translateUserAttrib(au) + "_equals_"
									+ translateResAttrib(ar);
							out.write(":- modeb(1," + p
									+ "(+user,+resource))?\n");
						}
					}
				}
			}

			out.write(":- modeb(1,superset(+attribValSet,+attribValSet))?\n");
			out.write(":- modeb(1,element(+attribValAtomic,+attribValSet))?\n");
			out.write(":- modeb(1,element(+attribValAtomic,#attribValSet))?\n");
			out.write(":- modeb(1,element(#attribValAtomic,+attribValSet))?\n");

			out.write("%Types\n");

			for (String user : config.getUsers()) {
				out.write("user(" + translateConst(user) + ").\n");
			}
			for (String resource : config.getResources()) {
				out.write("resource(" + translateConst(resource) + ").\n");
			}
			for (String operation : config.getOps()) {
				out.write("operation(" + translateConst(operation) + ").\n");
			}

			HashSet<String> Val = new HashSet<String>();
			for (String a : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(a);
				if (uAttr.getvType() == ValueType.Single) {
					Val.addAll(uAttr.getDomain());
				}
				if (uAttr.getvType() == ValueType.Set) {
					for (HashSet<String> vSet : uAttr.getSetDomain()) {
						Val.addAll(vSet);
					}
				}
			}

			for (String a : config.getResourceAttrSet().keySet()) {
				Attribute rAttr = config.getResourceAttrSet().get(a);
				if (rAttr.getvType() == ValueType.Single) {
					Val.addAll(rAttr.getDomain());
				}
				if (rAttr.getvType() == ValueType.Set) {
					for (HashSet<String> vSet : rAttr.getSetDomain()) {
						Val.addAll(vSet);
					}
				}
			}

			for (String v : Val) {
				out.write("attribValAtomic(" + translateConst(v) + ").\n");
			}

			out.write("attribValSet([]).\n");
			out.write("attribValSet([V|Vs]) :- attribValAtomic(V), attribValSet(Vs).\n");
			
			for (String a : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(a);
				if (uAttr.getvType() == ValueType.Single) {
					for (String v : uAttr.getDomain()) {
						out.write(addType(translateUserAttrib(a)) + "(" + translateConst(v) + ").\n");
					}
				}
				if (uAttr.getvType() == ValueType.Set) {
					for (HashSet<String> vSet : uAttr.getSetDomain()) {
						for (String v : vSet) {
							out.write(addType(translateUserAttrib(a)) + "(" + translateConst(v) + ").\n");
						}
					}
				}
			}
			
			for (String a : config.getResourceAttrSet().keySet()) {
				Attribute rAttr = config.getResourceAttrSet().get(a);
				if (rAttr.getvType() == ValueType.Single) {
					for (String v : rAttr.getDomain()) {
						out.write(addType(translateResAttrib(a)) + "(" + translateConst(v) + ").\n");
					}
				}
				if (rAttr.getvType() == ValueType.Set) {
					for (HashSet<String> vSet : rAttr.getSetDomain()) {
						for (String v : vSet) {
							out.write(addType(translateResAttrib(a)) + "(" + translateConst(v) + ").\n");
						}
					}
				}
			}

			out.write("%Background Knowledge\n");
			// read and write static background file
			br = new BufferedReader(new FileReader(STATIC_BACKGROUND_FILE));
			line = null;
			while ((line = br.readLine()) != null) {
				out.write(line + "\n");
			}
			br.close();
			
			for (String user : config.getUserAttrInfo().keySet()) {
				for (String au : config.getUserAttrInfo().get(user).keySet()) {
					for (String val : config.getUserAttrInfo().get(user)
							.get(au)) {
						out.write(translateUserAttrib(au) + "("
								+ translateConst(user) + ","
								+ translateConst(val) + ").\n");
					}
				}
			}
			for (String resr : config.getResourceAttrInfo().keySet()) {
				for (String ar : config.getResourceAttrInfo().get(resr)
						.keySet()) {
					for (String val : config.getResourceAttrInfo().get(resr)
							.get(ar)) {
						out.write(translateResAttrib(ar) + "("
								+ translateConst(resr) + ","
								+ translateConst(val) + ").\n");
					}
				}
			}
			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Set) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Set) {
							String p = translateUserAttrib(au) + "_superset_"
									+ translateResAttrib(ar);
							out.write(p + "(U,R) :- setof(X,"
									+ translateUserAttrib(au)
									+ "(U,X),SU), setof(Y,"
									+ translateResAttrib(ar)
									+ "(R,Y),SR), superset(SU,SR), not(SR==[]).\n");
						}
					}
				}
			}
			
			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Set) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Single) {
							String p = translateUserAttrib(au) + "_contains_"
									+ translateResAttrib(ar);
//							out.write(p + "(U,R) :- setof(X,"
//									+ translateUserAttrib(au)
//									+ "(U,X),SU),"
//									+ translateResAttrib(ar)
//									+ "(R,Y), element(Y,SU).\n");
							out.write(p + "(U,R) :-" +
									translateUserAttrib(au)
									+ "(U,X),"
									+ translateResAttrib(ar)
									+ "(R,X).\n");
						}
					}
				}
			}
			
			for (String au : config.getUserAttrSet().keySet()) {
				Attribute uAttr = config.getUserAttrSet().get(au);
				if (uAttr.getvType() == ValueType.Single) {
					for (String ar : config.getResourceAttrSet().keySet()) {
						Attribute rAttr = config.getResourceAttrSet().get(ar);
						if (rAttr.getvType() == ValueType.Single) {
							String p = translateUserAttrib(au) + "_equals_"
									+ translateResAttrib(ar);
							out.write(p + "(U,R) :-" +
									translateUserAttrib(au)
									+ "(U,X),"
									+ translateResAttrib(ar)
									+ "(R,X).\n");
						}
					}
				}
			}

			out.write("superset(Y,[A|X]) :- element(A,Y), superset(Y,X).\n");
			out.write("superset(Y,[]).\n");

			//for (String v : Val) {
				//out.write("element(" + translateConst(v) + ",[" + translateConst(v) + "]).\n");
			//}
			out.write("% Positive and negative examples\n");
			ArrayList<Triple<String, String, String>> upList = new ArrayList<Triple<String, String, String>>();
			for (String user : config.getUsers()) {
				for (String resource : config.getResources()) {
					for (String operation : config.getOps()) {
						Triple<String, String, String> up = new Triple<String, String, String>(
								user, operation, resource);
						upList.add(up);
//						if (config.getCoveredUP().contains(up)) {
//							out.write("up(" + translateConst(user) + "," + translateConst(resource)
//									+ "," + translateConst(operation) + ").\n");
//						} else {
//							out.write(":- up(" + translateConst(user) + "," + translateConst(resource)
//									+ "," + translateConst(operation) + ").\n");
//						}
					}
				}
			}
			Collections.shuffle(upList, new Random(System.currentTimeMillis()));
			for (Triple<String, String, String> up : upList) {
				if (config.getCoveredUP().contains(up)) {
					out.write("up(" + translateConst(up.getFirst()) + ","
							+ translateConst(up.getThird()) + ","
							+ translateConst(up.getSecond()) + ").\n");
				} else {
					out.write(":- up(" + translateConst(up.getFirst()) + ","
							+ translateConst(up.getThird()) + ","
							+ translateConst(up.getSecond()) + ").\n");
				}
			}

			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		Parser.parseInputFile(args[0]);
		translateABACToProgol(Parser.config, args[1], false);
	}

}
