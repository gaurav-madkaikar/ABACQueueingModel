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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

public class NonUniformCaseStudyGenerator {

	public void generateCaseStudy(String outputFolder, int inputSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
	}

	public static NonUniformCaseStudyGenerator createGenerator(String type) {
		if (type.toLowerCase().equals("university")) {
			return new UniversityNonUniformCaseStudyGenerator();
		}
		if (type.toLowerCase().equals("healthcare")) {
			return new HealthcareNonUniformCaseStudyGenerator();
		}
		if (type.toLowerCase().equals("projectmanagement")) {
			return new ProjectManagementNonUniformCaseStudyGenerator();
		}
		return null;
	}

	public static void main(String[] args) {
		// DiscreteNormalDistribution crsPerFacDistrib = new
		// DiscreteNormalDistribution(1,4,2,0.5);
		// for (int i=1; i<= 10 ; i++)
		// {
		// System.out.println(crsPerFacDistrib.nextValue());
		// }
		// UniversityNonUniformCaseStudyGenerator u = new
		// UniversityNonUniformCaseStudyGenerator();
		// u.generateCaseStudy("./non-uniform-gen-case-studies/university/Set1/UniversityCaseStudy",
		// 10, 0, 0);
		// u.generateCaseStudy("./non-uniform-gen-case-studies/university/Set2/UniversityCaseStudy",
		// 10, 0, 0);
		// u.generateCaseStudy("./non-uniform-gen-case-studies/university/Set3/UniversityCaseStudy",
		// 10, 0, 0);
		// u.generateCaseStudy("./non-uniform-gen-case-studies/university/Set4/UniversityCaseStudy",
		// 10, 0, 0);
		// u.generateCaseStudy("./non-uniform-gen-case-studies/university/Set5/UniversityCaseStudy",
		// 10, 0, 0);

		HealthcareCaseStudyGenerator h = new HealthcareCaseStudyGenerator();
		for (int set = 1; set <= 20; set++) {
			for (int size = 1; size <= 20; size++) {
				h.generateCaseStudy("sample-policies-non-uniform-synthetic-attribute-data/healthcare/Set"
				+ set + "/HealthcareCaseStudy_" + size + ".abac", size, 0, 0);
			}
		}
		
		
//		for (int set = 1; set <= 20; set++) {
//			File theDir = new File(
//					"sample-policies-non-uniform-synthetic-attribute-data/projectmanagement/Set"
//							+ set);
//
//			// if the directory does not exist, create it
//			if (!theDir.exists()) {
//				System.out.println("creating directory: " + theDir);
//				boolean result = false;
//
//				try {
//					theDir.mkdir();
//					result = true;
//				} catch (SecurityException se) {
//					// handle it
//				}
//				if (result) {
//					System.out.println("DIR created");
//				}
//			}
//			
//		}

	}
}

class UniversityNonUniformCaseStudyGenerator extends
		NonUniformCaseStudyGenerator {

	public static final int NAPP_PER_DEPT = 5;
	public static final int NSTU_PER_DEPT = 20;
	public static final int NFAC_PER_DEPT = 5;
	public static final int NCRS_PER_DEPT = 10;
	public static final int NCRS_PER_FAC = 2;
	public static final int NCRS_TAKEN_PER_STU = 4;
	public static final int NCRS_TAUGHT_PER_STU = 1;
	public static final int NSTAFF = 3;

	NormalDistribution deptSizeDistrib;

	DiscreteNormalDistribution crsPerFacDistrib;
	DiscreteNormalDistribution crsTakenPerStuDistrib;
	DiscreteNormalDistribution crsTaughtPerStuDistrib;

	Vector<Double> deptSize;
	Vector<Integer> Napp;
	Vector<Integer> Nstu;
	Vector<Integer> Nfac;
	Vector<Integer> Ncrs;

	int Ndept;
	int NcrsTaken;
	int NcrsTaught;
	int NcrsPerFac;

	ZipfDistrib crsDistrib1;

	class Resource {
		public String rid;
		public String type;
		public String dept;
		public String crs;
		public String student;
		public ArrayList<Integer> irvtAttrs;

		public Resource() {
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	class User {
		public String uid;
		public String dept;
		public String position;
		public String crsNum;
		public HashSet<String> crsTaken;
		public HashSet<String> crsTaught;
		public boolean isChair;
		public ArrayList<Integer> irvtAttrs;

		public User() {
			crsTaken = new HashSet<String>();
			crsTaught = new HashSet<String>();
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	public String courseID(String dept, String course) {
		return "crs" + dept + "-" + course;
	}

	public void printResourceAttrib(Resource g, Vector<String> output) {
		try {
			String s = "resourceAttrib(" + g.rid;
			if (g.type != null) {
				s += ", type=" + g.type;
			}
			if (g.dept != null) {
				s += ", department=" + g.dept;
			}
			if (g.crs != null) {
				s += ", crs=" + g.crs;
			}
			if (g.student != null) {
				s += ", student=" + g.student;
			}

			if (!g.irvtAttrs.isEmpty()) {
				for (int i = 0; i < g.irvtAttrs.size(); i++) {
					s += ", irResAttr_" + i + "=" + g.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(User u, Vector<String> output) {
		try {
			String s = "userAttrib(" + u.uid;
			if (u.dept != null) {
				s += ", department=" + u.dept;
			}
			if (u.position != null) {
				s += ", position=" + u.position;
			}

			if (!u.crsTaken.isEmpty() || u.position == "student") {
				s += ", crsTaken={";
				for (String crs : u.crsTaken) {
					s += crs + " ";
				}
				s += "}";
			}

			if (!u.crsTaught.isEmpty() || u.position == "student") {
				s += ", crsTaught={";
				for (String crs : u.crsTaught) {
					s += crs + " ";
				}
				s += "}";
			}

			if (u.isChair) {
				s += ", isChair=" + u.isChair;
			}

			if (!u.irvtAttrs.isEmpty()) {
				for (int i = 0; i < u.irvtAttrs.size(); i++) {
					s += ", irUsrAttr_" + i + "=" + u.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFolder, int inputSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {

			Random rand = new Random();

			double skew;
			long seed;

			deptSizeDistrib = new NormalDistribution(0.5, 5, 1, 1);

			crsPerFacDistrib = new DiscreteNormalDistribution(1, 4, 2.0, 0.5);
			crsTakenPerStuDistrib = new DiscreteNormalDistribution(1, 4, 4.0,
					0.5);
			crsTaughtPerStuDistrib = new DiscreteNormalDistribution(0, 1, 1.0,
					0.5);

			Ndept = inputSize;

			Vector<Vector<String>> output = new Vector<Vector<String>>();
			Vector<String> temp;

			deptSize = new Vector<Double>(Ndept);
			Napp = new Vector<Integer>(Ndept);
			Nstu = new Vector<Integer>(Ndept);
			Nfac = new Vector<Integer>(Ndept);
			Ncrs = new Vector<Integer>();

			for (int d = 0; d < Ndept; d++) {
				deptSize.insertElementAt(deptSizeDistrib.nextValue(), d);
				Napp.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NAPP_PER_DEPT
										* deptSize.elementAt(d))), d);
				Nstu.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NSTU_PER_DEPT
										* deptSize.elementAt(d))), d);
				Nfac.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NFAC_PER_DEPT
										* deptSize.elementAt(d))), d);
				// Ncrs.insertElementAt((int) Math.max(2,
				// Math.round(NCRS_PER_DEPT*deptSize.elementAt(d))), d);
				Ncrs.insertElementAt(0, d);
			}

			for (int d = 0; d < Ndept; d++) {
				// System.out.println("Dept Size:" + d + " " +
				// deptSize.elementAt(d));
				// System.out.println("No Of Applications: " +
				// Napp.elementAt(d));
				// System.out.println("No Of Students: " + Nstu.elementAt(d));
				// System.out.println("No Of Faculty: " + Nfac.elementAt(d));
				// System.out.println("No Of Courses: " + Ncrs.elementAt(d));
				// System.out.println("------------------------------");
			}

			for (int d = 0; d < Ndept; d++) {

				temp = new Vector<String>();

				for (int a = 0; a < Napp.elementAt(d); a++) {
					User u = new User();
					u.uid = "app" + d + "-" + a;
					u.position = "applicant";
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
					Resource app = new Resource();
					app.rid = "application" + d + "-" + a;
					app.type = "application";
					app.student = u.uid;
					for (int i = 0; i < irvtAttrSize; i++) {
						app.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(app, temp);
				}

				for (int f = 0; f < Nfac.elementAt(d); f++) {
					User u = new User();
					u.uid = "fac" + d + "-" + f;
					u.dept = "dept" + d;
					u.position = "faculty";
					NcrsPerFac = crsPerFacDistrib.getNextDistVal();
					for (int c = 0; c < NcrsPerFac; c++) {
						u.crsTaught.add(courseID(Integer.toString(d),
								Integer.toString(Ncrs.elementAt(d))));
						int inc = (Ncrs.elementAt(d)) + 1;
						Ncrs.remove(d);
						Ncrs.insertElementAt(inc, d);

					}
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
				}

				// System.out.println("No Of Courses: " + Ncrs.elementAt(d));

				for (int c = 0; c < Ncrs.elementAt(d); c++) {
					Resource g = new Resource();
					g.rid = "gradebook" + d + "-" + c;
					g.type = "gradebook";
					g.dept = "dept" + d;
					g.crs = courseID(Integer.toString(d), Integer.toString(c));
					for (int i = 0; i < irvtAttrSize; i++) {
						g.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(g, temp);

					Resource r = new Resource();
					r.rid = "roster" + d + "-" + c;
					r.type = "roster";
					r.dept = "dept" + d;
					r.crs = courseID(Integer.toString(d), Integer.toString(c));
					for (int i = 0; i < irvtAttrSize; i++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(r, temp);
				}

				skew = 0.5;
				seed = rand.nextLong();
				crsDistrib1 = new ZipfDistrib(Ncrs.elementAt(d), skew, seed);

				for (int s = 0; s < Nstu.elementAt(d); s++) {
					User u = new User();
					u.uid = "stu" + d + "-" + s;
					u.dept = "dept" + d;
					u.position = "student";

					NcrsTaken = crsTakenPerStuDistrib.getNextDistVal();

					for (int i = 0; i < NcrsTaken; i++) {
						int crsnum = crsDistrib1.getNextDistVal();
						u.crsTaken.add(courseID(Integer.toString(d),
								Integer.toString(crsnum)));
					}

					NcrsTaught = crsTaughtPerStuDistrib.getNextDistVal();

					for (int i = 0; i < NcrsTaught; i++) {
						int crsnum = crsDistrib1.getNextDistVal();
						String courseID = courseID(Integer.toString(d),
								Integer.toString(crsnum));
						if (!u.crsTaken.contains(courseID)) {
							u.crsTaught.add(courseID);
						}
					}
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
					Resource t = new Resource();
					t.rid = "transcript" + d + "-" + s;
					t.type = "transcript";
					t.student = u.uid;
					t.dept = "dept" + d;
					for (int i = 0; i < irvtAttrSize; i++) {
						t.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(t, temp);
					Resource app = new Resource();
					app.rid = "application" + "-" + u.uid;
					app.type = "application";
					app.student = u.uid;
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(app, temp);
				}

				User u = new User();
				u.uid = "chair" + d;
				u.dept = "dept" + d;
				u.isChair = true;
				for (int i = 0; i < irvtAttrSize; i++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(u, temp);
				if (d % NSTAFF == 0) {
					for (int i = 0; i < 1; i++) {
						u = new User();
						u.uid = "registrar" + d + "-" + i;
						u.dept = "registrar";
						u.position = "staff";
						for (int j = 0; j < irvtAttrSize; j++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(u, temp);
						u = new User();
						u.uid = "admissions" + d + "-" + i;
						u.dept = "admissions";
						u.position = "staff";
						for (int j = 0; j < irvtAttrSize; j++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(u, temp);
					}
				}
				output.add(temp);
			}

			FileWriter fstream;
			BufferedWriter out;

			/*
			 * To dump the values in the files each of no of departments NDEPT
			 * to 1
			 */
			for (int i = Ndept - 1; i >= 0; i--) {
				fstream = new FileWriter(outputFolder + "UniversityCaseStudy_"
						+ Integer.toString(i + 1) + ".abac");
				out = new BufferedWriter(fstream);

				for (int j = 0; j <= i; j++) {
					out.write("#---------------------------------------------------\n");
					out.write("#      Department : " + Integer.toString(j + 1)
							+ "\n");
					out.write("#---------------------------------------------------\n");
					out.write("# Dept Size: " + deptSize.elementAt(j) + "\n");
					out.write("# No Of Applicants: " + Napp.elementAt(j) + "\n");
					out.write("# No Of Students: " + Nstu.elementAt(j) + "\n");
					out.write("# No Of Faculty: " + Nfac.elementAt(j) + "\n");
					out.write("# No Of Courses: " + Ncrs.elementAt(j) + "\n");
					out.newLine();
					for (int k = 0; k < output.elementAt(j).size(); k++) {
						out.write(output.elementAt(j).elementAt(k));
					}
					out.newLine();
				}

				out.write("rule(; type in {gradebook}; {readMyScores}; crsTaken ] crs)\n");

				out.write("rule(; type in {gradebook}; {addScore readScore}; crsTaught ] crs;)\n");

				out.write("rule(position in {faculty}; type in {gradebook}; {changeScore assignGrade}; crsTaught ] crs)\n");

				out.write("rule(department in {registrar}; type in {roster}; {read write}; )\n");

				out.write("rule(position in {faculty}; type in {roster}; {read}; crsTaught ] crs)\n");

				out.write("rule(; type in {transcript}; {read}; uid=student)\n");

				out.write("rule(isChair in {true}; type in {transcript}; {read}; department=department)\n");

				out.write("rule(department in {registrar}; type in {transcript}; {read}; )\n");

				out.write("rule(; type in {application}; {checkStatus}; uid=student)\n");

				out.write("rule(department in {admissions}; type in {application}; {read setStatus}; )\n");

				out.write("unremovableResourceAttribs(type)\n");

				out.close();
				fstream.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}

class HealthcareNonUniformCaseStudyGenerator extends
		NonUniformCaseStudyGenerator {
	public static final int NPAT_PER_WARD = 10;
	public static final int NITEM_PER_PAT = 4;
	public static final int NTOPICS_PER_ITEM = 1;
	public static final int NNURSE_PER_WARD = 4;
	public static final int NDOC_PER_WARD = 2;
	public static final int NAGENT_PER_WARD = 2;
	public static final int NPAT_PER_AGENT = 1;
	public static final int NTEAM_PER_WARD = 2;
	public static final int NTEAM_PER_DOC = 2;
	public static final int NAREA = 10;
	public static final int NSPEC_PER_DOC = 1;

	NormalDistribution wardSizeDistrib;

	DiscreteNormalDistribution NitemPerPatDistrib;
	DiscreteNormalDistribution NtopicsPerItemDistrib;
	DiscreteNormalDistribution NpatPerAgentDistrib;
	DiscreteNormalDistribution NspecPerDocDistrib;
	DiscreteNormalDistribution NteamPerDocDistrib;

	ZipfDistrib areaDistrib;

	Vector<Double> wardSize;
	Vector<Integer> Npat;
	Vector<Integer> Nnurse;
	Vector<Integer> Ndoc;
	Vector<Integer> Nagent;
	Vector<Integer> Nteam;
	Vector<User> patients;

	class Resource {
		public String rid;
		public String type;
		public String patient;
		public String treatingTeam;
		public String ward;
		public String author;
		public HashSet<String> topics;
		public ArrayList<Integer> irvtAttrs;

		public Resource() {
			topics = new HashSet<String>();
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	class User {
		public String uid;
		public String ward;
		public String position;
		public HashSet<String> agentFor;
		public HashSet<String> specialties;
		public HashSet<String> teams;
		public ArrayList<Integer> irvtAttrs;

		public User() {
			agentFor = new HashSet<String>();
			specialties = new HashSet<String>();
			teams = new HashSet<String>();
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	public void printResourceAttrib(Resource g, Vector<String> output) {
		try {
			String s = "resourceAttrib(" + g.rid;
			if (g.type != null) {
				s += ", type=" + g.type;
			}
			if (g.author != null) {
				s += ", author=" + g.author;
			}
			if (g.patient != null) {
				s += ", patient=" + g.patient;
			}
			if (g.treatingTeam != null) {
				s += ", treatingTeam=" + g.treatingTeam;
			}
			if (g.ward != null) {
				s += ", ward=" + g.ward;
			}
			if (!g.topics.isEmpty()) {
				s += ", topics={";
				for (String topic : g.topics) {
					s += topic + " ";
				}
				s += "}";
			}
			if (!g.irvtAttrs.isEmpty()) {
				for (int i = 0; i < g.irvtAttrs.size(); i++) {
					s += ", irResAttr_" + i + "=" + g.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(User u, Vector<String> output) {
		try {
			String s = "userAttrib(" + u.uid;
			if (u.ward != null) {
				s += ", ward=" + u.ward;
			}
			if (u.position != null) {
				s += ", position=" + u.position;
			}
			if (!u.agentFor.isEmpty()) {
				s += ", agentFor={";
				for (String ag : u.agentFor) {
					s += ag + " ";
				}
				s += "}";
			}

			if (!u.specialties.isEmpty()) {
				s += ", specialities={";
				for (String sp : u.specialties) {
					s += sp + " ";
				}
				s += "}";
			}
			if (!u.teams.isEmpty()) {
				s += ", teams={";
				for (String team : u.teams) {
					s += team + " ";
				}
				s += "}";
			}
			if (!u.irvtAttrs.isEmpty()) {
				for (int i = 0; i < u.irvtAttrs.size(); i++) {
					s += ", irUsrAttr_" + i + "=" + u.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFolder, int inputFileSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {
			HashMap<String, HashSet<String>> patientToAgent = new HashMap<String, HashSet<String>>();

			Random rand = new Random();

			// similar to deptSizeDistrib for university sample policy
			wardSizeDistrib = new NormalDistribution(0.5, 5, 1, 1);

			// these distributions are the same for all wards
			NitemPerPatDistrib = new DiscreteNormalDistribution(1, 8, 4, 1);
			NtopicsPerItemDistrib = new DiscreteNormalDistribution(1, 2, 1, 0.5);
			NpatPerAgentDistrib = new DiscreteNormalDistribution(1, 2, 1, 0.5);
			NspecPerDocDistrib = new DiscreteNormalDistribution(1, 2, 1, 0.5);
			NteamPerDocDistrib = new DiscreteNormalDistribution(1, 3, 2, 0.5);

			int Nward = inputFileSize;

			wardSize = new Vector<Double>(Nward);
			Npat = new Vector<Integer>(Nward);
			Nnurse = new Vector<Integer>(Nward);
			Ndoc = new Vector<Integer>(Nward);
			Nagent = new Vector<Integer>(Nward);
			Nteam = new Vector<Integer>(Nward);
			patients = new Vector<User>();

			// agents in each ward
			Vector<Set<User>> agents = new Vector<Set<User>>(Nward);
			// TreatingTeam for each patient
			Map<String, String> patTreatingTeam = new HashMap<String, String>();

			double skew = 0.5;
			long seed = rand.nextLong();
			areaDistrib = new ZipfDistrib(NAREA, skew, seed);

			Vector<Vector<String>> patients_data = new Vector<Vector<String>>();
			Vector<Vector<String>> agents_data = new Vector<Vector<String>>();

			Vector<Vector<String>> output = new Vector<Vector<String>>();
			Vector<String> temp;

			for (int w = 0; w < Nward; w++) {
				wardSize.insertElementAt(wardSizeDistrib.nextValue(), w);
				Npat.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NPAT_PER_WARD
										* wardSize.elementAt(w))), w);
				Nnurse.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NNURSE_PER_WARD
										* wardSize.elementAt(w))), w);
				Ndoc.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NDOC_PER_WARD
										* wardSize.elementAt(w))), w);
				Nagent.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NDOC_PER_WARD
										* wardSize.elementAt(w))), w);
				Nteam.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NTEAM_PER_WARD
										* wardSize.elementAt(w))), w);
			}

			for (int w = 0; w < Nward; w++) {

				temp = new Vector<String>();

				for (int i = 0; i < Npat.elementAt(w); i++) {
					User u = new User();
					u.uid = "patient" + w + "-" + i;
					u.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
					patients.add(u);
				}
				patients_data.add(temp);
			}

			for (int w = 0; w < Nward; w++) {
				temp = new Vector<String>();
				Set<User> s = new HashSet<User>();
				for (int i = 0; i < Nagent.elementAt(w); i++) {
					User u = new User();
					u.uid = "agent" + w + "-" + i;
					for (int j = 0; j < NpatPerAgentDistrib.getNextDistVal(); j++) {
						Random ram = new Random();
						int patWard = ram.nextInt(w + 1);
						String patId = "patient" + patWard + "-"
								+ ram.nextInt(Npat.elementAt(patWard));
						u.agentFor.add(patId);
						if (!patientToAgent.keySet().contains(patId)) {
							patientToAgent.put(patId, new HashSet<String>());
						}
						patientToAgent.get(patId).add(u.uid);
					}
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					s.add(u);
					printUserAttrib(u, temp);
				}
				agents.insertElementAt(s, w);
				agents_data.add(temp);
			}

			for (int w = 0; w < Nward; w++) {
				temp = new Vector<String>();
				for (int i = 0; i < Ndoc.elementAt(w); i++) {
					User u = new User();
					u.uid = "doctor" + w + "-" + i;
					u.position = "doctor";
					for (int j = 0; j < NspecPerDocDistrib.getNextDistVal(); j++) {
						String area = "area" + areaDistrib.getNextDistVal();
						while (u.specialties.contains(area)) {
							area = "area" + areaDistrib.getNextDistVal();
						}
						u.specialties.add(area);
					}
					Random ram = new Random();
					u.teams.add("team" + w + "-"
							+ ram.nextInt(Nteam.elementAt(w)));
					for (int j = 0; j < NteamPerDocDistrib.getNextDistVal() - 1; j++) {
						Random r = new Random();
						int w1 = r.nextInt(w + 1);
						u.teams.add("team" + w1 + "-"
								+ r.nextInt(Nteam.elementAt(w1)));
					}
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
				}
				for (int i = 0; i < Nnurse.elementAt(w); i++) {
					User u = new User();
					u.uid = "nurse" + w + "-" + i;
					u.position = "nurse";
					u.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
				}
				for (int i = 0; i < Npat.elementAt(w); i++) {
					Resource r = new Resource();
					r.rid = "HR" + w + "-" + i;
					Random ram = new Random();
					r.type = "HR";
					r.patient = "patient" + w + "-" + i;
					String team = "team" + w + "-"
							+ ram.nextInt(Nteam.elementAt(w));
					patTreatingTeam.put(r.patient, team);
					r.treatingTeam = team;
					r.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(r, temp);
					r = new Resource();
					r.rid = "nursingItem" + w + "-" + i;
					r.type = "HRitem";
					r.treatingTeam = team;
					r.ward = "ward" + w;
					r.patient = "patient" + w + "-" + i;
					r.topics.add("nursing");
					r.author = "nurse" + w + "-"
							+ ram.nextInt(Nnurse.elementAt(w));
					for (int j = 0; j < irvtAttrSize; j++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(r, temp);
					r = new Resource();
					r.rid = "noteItem" + w + "-" + i;
					r.type = "HRitem";
					r.treatingTeam = team;
					r.ward = "ward" + w;
					r.patient = "patient" + w + "-" + i;
					r.topics.add("note");
					r.author = r.patient;
					for (int j = 0; j < irvtAttrSize; j++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(r, temp);
					// if (patientToAgent.get(r.patient) != null) {
					// for (String agent : patientToAgent.get(r.patient)) {
					// r.author = agent;
					// r.rid += agent;
					// printResourceAttrib(r, temp);
					// }
					// }
					HashSet<String> topics = new HashSet<String>();
					for (int j = 0; j < NitemPerPatDistrib.getNextDistVal(); j++) {
						Resource s = new Resource();
						s.rid = "HRitem" + w + "-" + i + "-" + j;
						s.type = "HRitem";
						s.patient = "patient" + w + "-" + i;
						s.treatingTeam = team;
						s.ward = "ward" + w;
						s.author = "doc" + w + "-"
								+ ram.nextInt(Ndoc.elementAt(w));
						for (int k = 0; k < NtopicsPerItemDistrib
								.getNextDistVal(); k++) {
							String topic = "area" + ram.nextInt(NAREA);
							while (topics.contains(topic)) {
								topic = "area" + ram.nextInt(NAREA);
							}
							topics.add(topic);
							s.topics.add(topic);
						}
						for (int k = 0; k < irvtAttrSize; k++) {
							s.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printResourceAttrib(s, temp);
					}
				}
				/* Note Resource Items of agents for patients */
				int i = 0;
				for (User a : agents.elementAt(w)) {
					for (String patId : a.agentFor) {
						Resource r = new Resource();
						r.rid = "noteItem" + w + "-" + i + a.uid + "-" + patId;
						r.type = "HRitem";
						r.treatingTeam = patTreatingTeam.get(patId);
						r.patient = patId;
						r.ward = "ward" + patId.substring(7, 8);
						r.author = a.uid;
						r.topics.add("note");
						i++;
						printResourceAttrib(r, temp);
					}
				}
				output.add(temp);
			}

			FileWriter fstream;
			BufferedWriter out;

			/* To dump the values in the files each of no of wards NWARD to 1 */
			for (int i = Nward - 1; i >= 0; i--) {
				fstream = new FileWriter(outputFolder + "HealthcareCaseStudy_"
						+ Integer.toString(i + 1) + ".abac");
				out = new BufferedWriter(fstream);

				for (int j = 0; j <= i; j++) {
					out.write("#---------------------------------------------------\n");
					out.write("#      Ward : " + Integer.toString(j + 1) + "\n");
					out.write("#---------------------------------------------------\n");
					out.write("# Ward Size: " + wardSize.elementAt(j) + "\n");
					out.write("# No Of Patients: " + Npat.elementAt(j) + "\n");
					out.write("# No Of Agents: " + Nagent.elementAt(j) + "\n");
					out.write("# No Of Nurses: " + Nnurse.elementAt(j) + "\n");
					out.write("# No Of Doctors: " + Ndoc.elementAt(j) + "\n");
					out.write("# No Of Teams: " + Nteam.elementAt(j) + "\n");

					out.newLine();
					for (int k = 0; k < patients_data.elementAt(j).size(); k++) {
						out.write(patients_data.elementAt(j).elementAt(k));
					}

					for (int k = 0; k < agents_data.elementAt(j).size(); k++) {
						out.write(agents_data.elementAt(j).elementAt(k));
					}

					for (int k = 0; k < output.elementAt(j).size(); k++) {
						out.write(output.elementAt(j).elementAt(k));
					}
					out.newLine();
				}

				out.write("rule(position in {nurse}; type in {HR}; {addItem}; ward=ward)\n");

				out.write("rule(; type in {HR}; {addItem}; teams ] treatingTeam))\n");

				out.write("rule(; type in {HR}; {addNote}; uid=patient)\n");

				out.write("rule(; type in {HR}; {addNote}; agentFor ] patient)\n");

				out.write("rule(; type in {HRitem}; {read}; uid=author)\n");

				out.write("rule(position in {nurse}; type in {HRitem}, topics supseteqln {{nursing}}; {read}; ward=ward)\n");

				out.write("rule(; type in {HRitem}; {read}; specialties > topics, teams ] treatingTeam)\n");

				out.write("rule(; type in {HRitem}, topics supseteqln {{note}}; {read}; uid=patient)\n");

				out.write("rule(; type in {HRitem}, topics supseteqln {{note}}; {read}; agentFor ] patient)\n");

				out.write("unremovableResourceAttribs(type)\n");

				out.close();
				fstream.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}

class ProjectManagementNonUniformCaseStudyGenerator extends
		NonUniformCaseStudyGenerator {
	public static final int NPROJ_PER_DEPT = 2;
	public static final int NMGR_PER_DEPT = 1;
	public static final int NACC_PER_DEPT = 1;
	public static final int NAUD_PER_DEPT = 1;
	public static final int NPLA_PER_DEPT = 1;
	public static final int NPLD_PER_DEPT = 2;
	public static final int NAREA = 2;
	public static final int NNEMP_PER_AREA_PER_DEPT = 1;
	public static final int NEMP_PER_AREA_PER_DEPT = 1;

	public static final int NBGT_PER_PROJ = 1;
	public static final int NSCHL_PER_PROJ = 1;

	NormalDistribution deptSizeDistrib;

	Vector<Double> deptSize;
	Vector<Integer> Nproj;
	Vector<Integer> Npled;
	Vector<Integer> Nnemp;
	Vector<Integer> Nemp;

	ZipfDistrib projPopDist;

	class Resource {
		public String rid;
		public String type;
		public String project;
		public String department;
		public HashSet<String> expertise;
		public String proprietary;
		public ArrayList<Integer> irvtAttrs;

		public Resource() {
			expertise = new HashSet<String>();
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	class User {
		public String uid;
		public String dept;
		public String isEmployee;
		public HashSet<String> projectsLed;
		public HashSet<String> adminRole;
		public HashSet<String> projects;
		public HashSet<String> expertise;
		public HashSet<String> tasks;
		public ArrayList<Integer> irvtAttrs;

		public User() {
			adminRole = new HashSet<String>();
			projects = new HashSet<String>();
			projectsLed = new HashSet<String>();
			expertise = new HashSet<String>();
			tasks = new HashSet<String>();
			irvtAttrs = new ArrayList<Integer>();
		}
	}

	public void printResourceAttrib(Resource g, Vector<String> output) {
		try {
			String s = "resourceAttrib(" + g.rid;
			if (g.type != null) {
				s += ", type=" + g.type;
			}
			if (g.project != null) {
				s += ", project=" + g.project;
			}
			if (g.department != null) {
				s += ", department=" + g.department;
			}
			if (!g.expertise.isEmpty()) {
				s += ", expertise={";
				for (String exp : g.expertise) {
					s += exp + " ";
				}
				s += "}";
			}
			if (g.proprietary != null) {
				s += ", proprietary=" + g.proprietary;
			}
			if (!g.irvtAttrs.isEmpty()) {
				for (int i = 0; i < g.irvtAttrs.size(); i++) {
					s += ", irResAttr_" + i + "=" + g.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(User u, Vector<String> output) {
		try {
			String s = "userAttrib(" + u.uid;
			if (!u.adminRole.isEmpty()) {
				s += ", adminRoles={";
				for (String ag : u.adminRole) {
					s += ag + " ";
				}
				s += "}";
			}

			if (!u.expertise.isEmpty()) {
				s += ", expertise={";
				for (String exp : u.expertise) {
					s += exp + " ";
				}
				s += "}";
			}
			if (!u.projects.isEmpty()) {
				s += ", projects={";
				for (String sp : u.projects) {
					s += sp + " ";
				}
				s += "}";
			}
			if (!u.projectsLed.isEmpty()) {
				s += ", projectsLed={";
				for (String sp : u.projectsLed) {
					s += sp + " ";
				}
				s += "}";
			}
			if (u.dept != null) {
				s += ", department=" + u.dept;
			}
			if (u.isEmployee != null) {
				s += ", isEmployee=" + u.isEmployee;
			}
			if (!u.tasks.isEmpty()) {
				s += ", tasks={";
				for (String task : u.tasks) {
					s += task + " ";
				}
				s += "}";
			}
			if (!u.irvtAttrs.isEmpty()) {
				for (int i = 0; i < u.irvtAttrs.size(); i++) {
					s += ", irUsrAttr_" + i + "=" + u.irvtAttrs.get(i);
				}
			}

			output.add(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFolder, int inputSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {

			Random rand = new Random();

			double skew = 0.5;
			long seed;
			ZipfDistrib projPopDist;

			Vector<Vector<String>> output = new Vector<Vector<String>>();
			Vector<String> temp;

			int Ndept = inputSize;

			deptSizeDistrib = new NormalDistribution(0.5, 5, 1, 1);

			deptSize = new Vector<Double>(Ndept);
			Nproj = new Vector<Integer>(Ndept);
			Npled = new Vector<Integer>(Ndept);
			Nnemp = new Vector<Integer>(Ndept);
			Nemp = new Vector<Integer>(Ndept);

			for (int d = 0; d < Ndept; d++) {
				deptSize.insertElementAt(deptSizeDistrib.nextValue(), d);
				Nproj.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NPROJ_PER_DEPT
										* deptSize.elementAt(d))), d);
				Npled.insertElementAt(Nproj.elementAt(d), d);
				Nnemp.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NNEMP_PER_AREA_PER_DEPT
										* deptSize.elementAt(d))), d);
				Nemp.insertElementAt(
						(int) Math.max(
								2,
								Math.round(NEMP_PER_AREA_PER_DEPT
										* deptSize.elementAt(d))), d);
			}

			for (int d = 1; d <= Ndept; d++) {
				// generate manager
				seed = rand.nextLong();
				projPopDist = new ZipfDistrib(Nproj.elementAt(d - 1), skew,
						seed);

				temp = new Vector<String>();

				User u = new User();
				u.uid = "manager" + d;
				u.adminRole.add("manager");
				u.dept = "dept" + d;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(u, temp);
				HashSet<String> projs = new HashSet<String>();
				for (int i = 1; i <= Nproj.elementAt(d - 1); i++) {
					projs.add("proj" + d + i);
				}
				// generate accountant
				u = new User();
				u.uid = "acc" + d;
				u.adminRole.add("accountant");
				u.projects = projs;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(u, temp);
				// generate auditor
				u = new User();
				u.uid = "aud" + d;
				u.adminRole.add("auditor");
				u.projects = projs;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(u, temp);
				// generate planner
				u = new User();
				u.uid = "planner" + d;
				u.adminRole.add("planner");
				u.projects = projs;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(u, temp);
				// generate project leader
				for (int i = 1; i <= Npled.elementAt(d - 1); i++) {
					u = new User();
					u.uid = "ldr" + d + i;
					u.projects = projs;
					u.projectsLed.add("proj" + d + i);
					u.dept = "dept" + d;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(u, temp);
				}

				int noprojID;
				int projID;
				// int noprojID = ram.nextInt(NPROJ_PER_DEPT);
				// int projID = ram.nextInt(NPROJ_PER_DEPT);
				// while (projID == noprojID) {
				// projID = ram.nextInt(NPROJ_PER_DEPT);
				// }
				// int noprojID = 2;
				// int projID = 1;
				for (int i = 1; i <= NAREA; i++) {

					for (int j = 1; j <= Nnemp.elementAt(d - 1); j++) {
						u = new User();
						u.uid = "nonemployee" + d + "_" + j + "_area" + i;
						u.expertise.add("area" + i);
						u.isEmployee = "False";
						noprojID = projPopDist.getNextDistVal() + 1;
						u.projects.add("proj" + d + noprojID);
						u.tasks.add("proj" + d + noprojID + "task" + i + "a");
						u.tasks.add("proj" + d + noprojID + "task" + i
								+ "propa");
						for (int k = 0; k < irvtAttrSize; k++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(u, temp);
					}
					for (int j = 1; j <= Nemp.elementAt(d - 1); j++) {
						u = new User();
						u.uid = "employee" + d + "_" + j + "_area" + i;
						u.expertise.add("area" + i);
						u.isEmployee = "True";
						projID = projPopDist.getNextDistVal() + 1;
						u.projects.add("proj" + d + projID);
						u.tasks.add("proj" + d + projID + "task" + i + "a");
						u.tasks.add("proj" + d + projID + "task" + i + "propa");
						for (int k = 0; k < irvtAttrSize; k++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(u, temp);
					}
				}
				// generate budget
				for (int i = 1; i <= Nproj.elementAt(d - 1); i++) {
					for (int j = 1; j <= NBGT_PER_PROJ; j++) {
						Resource r = new Resource();
						r.rid = "proj" + d + i + "budget" + j;
						r.type = "budget";
						r.project = "proj" + d + i;
						r.department = "dept" + d;
						for (int k = 0; k < irvtAttrSize; k++) {
							r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printResourceAttrib(r, temp);
					}
					for (int j = 1; j <= NSCHL_PER_PROJ; j++) {
						Resource r = new Resource();
						r.rid = "proj" + d + i + "sched" + j;
						r.type = "schedule";
						r.project = "proj" + d + i;
						r.department = "dept" + d;
						for (int k = 0; k < irvtAttrSize; k++) {
							r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printResourceAttrib(r, temp);
					}

					for (int k = 1; k <= 2; k++) {
						for (int j = 1; j <= NAREA; j++) {
							for (int l = 1; l <= 2; l++) {
								Resource r = new Resource();
								r.rid = "proj" + d + i + "task" + j
										+ (k % 2 == 0 ? "prop" : "")
										+ (l % 2 == 0 ? "a" : "");
								r.type = "task";
								r.project = "proj" + d + i;
								r.department = "dept" + d;
								r.expertise.add("area" + j);
								r.proprietary = k % 2 == 0 ? "true" : "false";
								for (int m = 0; m < irvtAttrSize; m++) {
									r.irvtAttrs.add(rand
											.nextInt(irvtAttrDomainSize));
								}
								printResourceAttrib(r, temp);
							}
						}
					}
				}
				output.add(temp);
			}

			FileWriter fstream;
			BufferedWriter out;

			/*
			 * To dump the values in the files each of no of departments NDEPT
			 * to 1
			 */
			for (int i = Ndept - 1; i >= 0; i--) {

				fstream = new FileWriter(outputFolder
						+ "ProjectManagementCaseStudy_"
						+ Integer.toString(i + 1) + ".abac");
				out = new BufferedWriter(fstream);

				for (int j = 0; j <= i; j++) {
					out.write("#---------------------------------------------------\n");
					out.write("#      Department : " + Integer.toString(j + 1)
							+ "\n");
					out.write("#---------------------------------------------------\n");
					out.write("# Dept Size: " + deptSize.elementAt(j) + "\n");
					out.write("# No Of Projects: " + Nproj.elementAt(j) + "\n");
					out.write("# No Of Project Leaders: " + Npled.elementAt(j)
							+ "\n");
					out.write("# No Of Non Employees: " + Nnemp.elementAt(j)
							+ "\n");
					out.write("# No Of Employees: " + Nemp.elementAt(j) + "\n");
					out.newLine();
					for (int k = 0; k < output.elementAt(j).size(); k++) {
						out.write(output.elementAt(j).elementAt(k));
					}
					out.newLine();
				}

				out.write("rule(adminRoles supseteqln {{manager}} ; type in {budget}; {read approve}; department = department)\n");

				out.write("rule( ; type in {schedule budget}; {read write}; projectsLed ] project)\n");

				out.write("rule( ; type in {schedule}; {read}; projects ] project)\n");

				out.write("rule( ; type in {task}; {setStatus}; tasks ] rid)\n");

				out.write("rule( ; type in {task}, proprietary in {false}; {request read}; projects ] project, expertise > expertise)\n");

				out.write("rule(isEmployee in {True} ; type in {task}; {request read}; projects ] project, expertise > expertise)\n");

				out.write("rule(adminRoles supseteqln {{auditor}} ; type in {budget}; {read}; projects ] project)\n");

				out.write("rule(adminRoles supseteqln {{accountant}} ; type in {budget}; {read write}; projects ] project)\n");

				out.write("rule(adminRoles supseteqln {{accountant}} ; type in {task}; {setCost}; projects ] project)\n");

				out.write("rule(adminRoles supseteqln {{planner}} ; type in {schedule}; {write}; projects ] project)\n");

				out.write("rule(adminRoles supseteqln {{planner}} ; type in {task}; {setSchedule}; projects ] project)\n");

				out.write("unremovableResourceAttribs(type)\n");

				out.close();
				fstream.close();
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
