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
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class CaseStudyGenerator {
	public void generateCaseStudy(String outputFile, int inputSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
	}

	public static CaseStudyGenerator createGenerator(String type) {
		if (type.toLowerCase().equals("university")) {
			return new UniversityCaseStudyGenerator();
		}
		if (type.toLowerCase().equals("healthcare")) {
			return new HealthcareCaseStudyGenerator();
		}
		if (type.toLowerCase().equals("projectmanagement")) {
			return new ProjectManagementCaseStudyGenerator();
		}
		return null;
	}
}

class UniversityCaseStudyGenerator extends CaseStudyGenerator {
	public static final int NAPP = 5;
	public static final int NSTU = 20;
	public static final int NFAC = 5;
	public static final int NCRS = 10;
	public static final int NCRS_PER_FAC = 2;
	public static final int NCRS_TAKEN_PER_STU = 4;
	public static final int NCRS_TAUGHT_PER_STU = 1;
	public static final int NSTAFF = 2;

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

	public void printResourceAttrib(BufferedWriter out, Resource g) {
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(BufferedWriter out, User u) {
		try {
			String s = "userAttrib(" + u.uid;
			if (u.dept != null) {
				s += ", department=" + u.dept;
			}
			if (u.position != null) {
				s += ", position=" + u.position;
			}
			if (!u.crsTaken.isEmpty()) {
				s += ", crsTaken={";
				for (String crs : u.crsTaken) {
					s += crs + " ";
				}
				s += "}";
			}
			if (!u.crsTaught.isEmpty()) {
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFile, int inputSize,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			Random rand = new Random();
			for (int d = 0; d < inputSize; d++) {
				for (int c = 1; c < NCRS; c++) {
					Resource g = new Resource();
					g.rid = "gradebook" + d + "-" + c;
					g.type = "gradebook";
					g.dept = "dept" + d;
					g.crs = courseID(Integer.toString(d), Integer.toString(c));
					for (int i = 0; i < irvtAttrSize; i++) {
						g.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, g);

					Resource r = new Resource();
					r.rid = "roster" + d + "-" + c;
					r.type = "roster";
					r.dept = "dept" + d;
					r.crs = courseID(Integer.toString(d), Integer.toString(c));
					for (int i = 0; i < irvtAttrSize; i++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, r);
				}
				for (int a = 0; a < NAPP; a++) {
					User u = new User();
					u.uid = "app" + d + "-" + a;
					u.position = "applicant";
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
					Resource app = new Resource();
					app.rid = "application" + d + "-" + a;
					app.type = "application";
					app.student = u.uid;
					for (int i = 0; i < irvtAttrSize; i++) {
						app.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, app);
				}

				for (int s = 0; s < NSTU; s++) {
					User u = new User();
					u.uid = "stu" + d + "-" + s;
					u.dept = "dept" + d;
					u.position = "student";
					for (int i = 0; i < NCRS_TAKEN_PER_STU; i++) {
						Random ram = new Random();
						int crsnum = ram.nextInt(NCRS);
						u.crsTaken.add(courseID(Integer.toString(d),
								Integer.toString(crsnum)));
					}
					for (int i = 0; i < NCRS_TAUGHT_PER_STU; i++) {
						Random ram = new Random();
						int crsnum = ram.nextInt(NCRS);
						String courseID = courseID(Integer.toString(d),
								Integer.toString(crsnum));
						if (!u.crsTaken.contains(courseID)) {
							u.crsTaught.add(courseID);
						}
					}
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
					Resource t = new Resource();
					t.rid = "transcript" + d + "-" + s;
					t.type = "transcript";
					t.student = u.uid;
					t.dept = "dept" + d;
					for (int i = 0; i < irvtAttrSize; i++) {
						t.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, t);
					Resource app = new Resource();
					app.rid = u.uid + "application" + d + "-" + s;
					app.type = "application";
					app.student = u.uid;
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, app);
				}
				for (int f = 0; f < NFAC; f++) {
					User u = new User();
					u.uid = "fac" + d + "-" + f;
					u.dept = "dept" + d;
					u.position = "faculty";
					for (int c = 0; c < NCRS_PER_FAC; c++) {
						u.crsTaught.add(courseID(Integer.toString(d),
								Integer.toString(f * NCRS_PER_FAC + c)));
					}
					for (int i = 0; i < irvtAttrSize; i++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
				}
				User u = new User();
				u.uid = "chair" + d;
				u.dept = "dept" + d;
				u.isChair = true;
				for (int i = 0; i < irvtAttrSize; i++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(out, u);
				if (d % NSTAFF == 0) {
					for (int i = 0; i < NSTAFF; i++) {
						u = new User();
						u.uid = "registrar" + d + "-" + i;
						u.dept = "registrar";
						u.position = "staff";
						for (int j = 0; j < irvtAttrSize; j++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(out, u);
						u = new User();
						u.uid = "admissions" + d + "-" + i;
						u.dept = "admissions";
						u.position = "staff";
						for (int j = 0; j < irvtAttrSize; j++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(out, u);
					}
				}
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
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		CaseStudyGenerator c = new UniversityCaseStudyGenerator();
		c.generateCaseStudy(args[0], Integer.parseInt(args[1]), 0, 30);
	}
}

class HealthcareCaseStudyGenerator extends CaseStudyGenerator {
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

	public void printResourceAttrib(BufferedWriter out, Resource g) {
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(BufferedWriter out, User u) {
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
				s += ", specialties={";
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFile, int NWARD,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			HashMap<String, HashSet<String>> patientToAgent = new HashMap<String, HashSet<String>>();
			Random rand = new Random();
			for (int w = 0; w < NWARD; w++) {
				for (int i = 0; i < NPAT_PER_WARD; i++) {
					User u = new User();
					u.uid = "patient" + w + "-" + i;
					u.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
				}
				for (int i = 0; i < NAGENT_PER_WARD; i++) {
					User u = new User();
					u.uid = "agent" + w + "-" + i;
					for (int j = 0; j < NPAT_PER_AGENT; j++) {
						Random ram = new Random();
						String patient = "patient" + w + "-"
								+ ram.nextInt(NPAT_PER_WARD);
						u.agentFor.add(patient);
						if (!patientToAgent.keySet().contains(patient)) {
							patientToAgent.put(patient, new HashSet<String>());
						}
						patientToAgent.get(patient).add(u.uid);
					}
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
					u.uid = "agent" + w + "-" + i + "1";
					printUserAttrib(out, u);
				}
				for (int i = 0; i < NDOC_PER_WARD; i++) {
					User u = new User();
					u.uid = "doctor" + w + "-" + i;
					u.position = "doctor";
					for (int j = 0; j < NSPEC_PER_DOC; j++) {
						Random ram = new Random();
						u.specialties.add("area" + ram.nextInt(NAREA));
					}
					Random ram = new Random();
					u.teams.add("team" + w + "-" + ram.nextInt(NTEAM_PER_WARD));
					for (int j = 0; j < NTEAM_PER_DOC; j++) {
						Random r = new Random();
						u.teams.add("team" + r.nextInt(NWARD) + "-"
								+ r.nextInt(NTEAM_PER_WARD));
					}
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
				}
				for (int i = 0; i < NNURSE_PER_WARD; i++) {
					User u = new User();
					u.uid = "nurse" + w + "-" + i;
					u.position = "nurse";
					u.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
				}
				for (int i = 0; i < NPAT_PER_WARD; i++) {
					Resource r = new Resource();
					r.rid = "HR" + w + "-" + i;
					Random ram = new Random();
					r.type = "HR";
					r.patient = "patient" + w + "-" + i;
					String team = "team" + w + "-"
							+ ram.nextInt(NTEAM_PER_WARD);
					r.treatingTeam = team;
					r.ward = "ward" + w;
					for (int j = 0; j < irvtAttrSize; j++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, r);
					r = new Resource();
					r.rid = "nursingItem" + w + "-" + i;
					r.type = "HRitem";
					r.treatingTeam = team;
					r.ward = "ward" + w;
					r.patient = "patient" + w + "-" + i;
					r.topics.add("nursing");
					r.author = "nurse" + w + "-" + ram.nextInt(NNURSE_PER_WARD);
					for (int j = 0; j < irvtAttrSize; j++) {
						r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printResourceAttrib(out, r);
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
					printResourceAttrib(out, r);
					if (patientToAgent.get(r.patient) != null) {
						for (String agent : patientToAgent.get(r.patient)) {
							r.author = agent;
							r.rid += agent;
							printResourceAttrib(out, r);
						}
					}
					HashSet<String> topics = new HashSet<String>();
					for (int j = 0; j < NITEM_PER_PAT; j++) {
						Resource s = new Resource();
						s.rid = "HRitem" + w + "-" + i + "-" + j;
						s.type = "HRitem";
						s.patient = "patient" + w + "-" + i;
						s.treatingTeam = team;
						s.ward = "ward" + w;
						s.author = "doc" + w + "-" + ram.nextInt(NDOC_PER_WARD);
						for (int k = 0; k < NTOPICS_PER_ITEM; k++) {
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
						printResourceAttrib(out, s);
					}
				}
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
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		CaseStudyGenerator c = new HealthcareCaseStudyGenerator();
		c.generateCaseStudy(args[0], Integer.parseInt(args[1]), 0, 30);
	}
}

class ProjectManagementCaseStudyGenerator extends CaseStudyGenerator {
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

	public void printResourceAttrib(BufferedWriter out, Resource g) {
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public void printUserAttrib(BufferedWriter out, User u) {
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
			out.write(s + ")" + "\n");
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void generateCaseStudy(String outputFile, int NDEPT,
			int irvtAttrSize, int irvtAttrDomainSize) {
		try {
			FileWriter fstream = new FileWriter(outputFile);
			BufferedWriter out = new BufferedWriter(fstream);
			Random rand = new Random();
			for (int d = 1; d <= NDEPT; d++) {
				// generate manager
				User u = new User();
				u.uid = "manager" + d;
				u.adminRole.add("manager");
				u.dept = "dept" + d;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(out, u);
				HashSet<String> projs = new HashSet<String>();
				for (int i = 1; i <= NPROJ_PER_DEPT; i++) {
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
				printUserAttrib(out, u);
				// generate auditor
				u = new User();
				u.uid = "aud" + d;
				u.adminRole.add("auditor");
				u.projects = projs;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(out, u);
				// generate planner
				u = new User();
				u.uid = "planner" + d;
				u.adminRole.add("planner");
				u.projects = projs;
				for (int j = 0; j < irvtAttrSize; j++) {
					u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
				}
				printUserAttrib(out, u);
				// generate project leader
				for (int i = 1; i <= NPLD_PER_DEPT; i++) {
					u = new User();
					u.uid = "ldr" + d + i;
					u.projects = projs;
					u.projectsLed.add("proj" + d + i);
					u.dept = "dept" + d;
					for (int j = 0; j < irvtAttrSize; j++) {
						u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
					}
					printUserAttrib(out, u);
				}
				// Random ram = new Random();
				// int noprojID = ram.nextInt(NPROJ_PER_DEPT);
				// int projID = ram.nextInt(NPROJ_PER_DEPT);
				// while (projID == noprojID) {
				// projID = ram.nextInt(NPROJ_PER_DEPT);
				// }
				int noprojID = 2;
				int projID = 1;
				for (int i = 1; i <= NAREA; i++) {
					for (int j = 1; j <= NNEMP_PER_AREA_PER_DEPT; j++) {
						u = new User();
						u.uid = "nonemployee" + d + "_area" + i;
						u.expertise.add("area" + i);
						u.isEmployee = "False";
						u.projects.add("proj" + d + noprojID);
						u.tasks.add("proj" + d + noprojID + "task" + i + "a");
						u.tasks.add("proj" + d + noprojID + "task" + i
								+ "propa");
						for (int k = 0; k < irvtAttrSize; k++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(out, u);
					}
					for (int j = 1; j <= NEMP_PER_AREA_PER_DEPT; j++) {
						u = new User();
						u.uid = "employee" + d + "_area" + i;
						u.expertise.add("area" + i);
						u.isEmployee = "True";
						// int projID = ram.nextInt(NPROJ_PER_DEPT);
						u.projects.add("proj" + d + projID);
						u.tasks.add("proj" + d + projID + "task" + i + "a");
						u.tasks.add("proj" + d + projID + "task" + i + "propa");
						for (int k = 0; k < irvtAttrSize; k++) {
							u.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printUserAttrib(out, u);
					}
				}
				// generate budget
				for (int i = 1; i <= NPROJ_PER_DEPT; i++) {
					for (int j = 1; j <= NBGT_PER_PROJ; j++) {
						Resource r = new Resource();
						r.rid = "proj" + d + i + "budget" + j;
						r.type = "budget";
						r.project = "proj" + d + i;
						r.department = "dept" + d;
						for (int k = 0; k < irvtAttrSize; k++) {
							r.irvtAttrs.add(rand.nextInt(irvtAttrDomainSize));
						}
						printResourceAttrib(out, r);
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
						printResourceAttrib(out, r);
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
								printResourceAttrib(out, r);
							}
						}
					}
				}
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
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		ProjectManagementCaseStudyGenerator c = new ProjectManagementCaseStudyGenerator();
		c.generateCaseStudy(args[0], Integer.parseInt(args[1]), 0, 30);
	}

}
