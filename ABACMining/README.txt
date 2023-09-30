======================================================================
SYSTEM REQUIREMENTS

The program should compile and run on any system with a Java compiler and
Java runtime environment.  Java 6 and higher definitely work; earlier
versions of Java might work.

The shell scripts "compile" and "run", documented below, make it a little
easier to compute and run the program.  The shell scripts work on any
system with a bash shell: Windows/cygwin, linux, solaris, etc.  If your
system does not have a bash shell, you can run the program without the
shell scripts, by copying-and-pasting the commands in the shell scripts
into your command shell (for the run script, replace "$*" with the
appropriate arguments to the shell script, based on the documentation
below).

======================================================================
COMMANDS

To compile the program: ./compile

To run the program, the general format is: ./run [option] arg1 arg2 ...
The options and the corresponding arguments are described below.

To enable verbose output, pass "-verbose" as the last argument to the program.
In some modes, -verbose must be included to see the mined policy;
otherwise, only some statistics about the mining process are shown.

For running modes shown below that include the "outputFile" option, the
output is written to the specified file.

For running modes that do not include the "outputFile" option, the output
is written to standard output (i.e., the terminal).

./run -m inputFile
Meaning: run ABAC mining algorithm on inputFile, which should contain an ABAC
policy in the syntax described below.  note that the path of inputFile is
relative to the bin subdirectory.
Examples: 
1. run ABAC mining algorithm on the university case study with manually
written attribute data: ./run -m ../case-studies/university.abac
2. run ABAC mining algorithm on the university sample policy with the
first synthetic attribute dataset with Ndept=10: 
  ./run -m ../sample-policies-non-uniform-synthetic-attribute-data/university/Set1/UniversityCaseStudy_10.abac
3. run ABAC mining algorithm on the first synthetic policy with Nrules=5: 
  ./run -m ../ran-case-studies/synthetic_05_1_new.abac

./run -r caseStudyType outputFile N
Meaning: generate case study policy with synthetic attribute data of size N.
caseStudyType may be university, healthcare, or projectmanagement.
N is Ndept or Nward, depending on caseStudyType
Example: ./run -r university gen-case-studies/university/university-10.abac 10

./run -s outputFile Nrules NminCnj NminCon pOverlapRule
Meaning: generate synthetic policy with Nrules rules, where each rule has 
at least NminCnj user-attribute conjuncts and resource-attribute conjuncts
(NminCnj is denoted N^{min}_{cnj} in the paper) and at least NminCon
constraints (NminCon is denoted N^{min}_{con} in the paper), and with
a probability pOverlapRule of introducing a companion rule for each
generated rule rho, by randomly removing one conjunct from uae(rho) and
adding one conjunct (generated in the usual way) to rae(rho).
Example: ./run -s ran-case-studies/synthetic.abac 10 2 2 0.5

./run -g inputFile fraction
Meaning: evaluate generalization error on inputFile, which should contain
an ABAC policy, using the specified fraction of the users and resources for
training, and using the remaining users and resources for validation.
Example: ./run -g case-studies/university.abac 0.15

./run -p inputFile outputFile [-attribVar]
Meaning: translate the ABAC policy in inputFile into Progol. 
Progol files should have suffix ".pl".  the -attribVar option
causes attribute predicates to have an additional mode declaration that
allows variables as the second argument to the attribute predicate.
Example: ./run -p case-studies/university.abac progol/university.pl

./run -u inputFile noiseLevel alpha
Meaning: add under-assignments to the ABAC policy in inputFile, and run the
ABAC mining algorithm with extension to detect under-assignments.  Please
refer to the paper for the definitions of noiseLevel (denoted \nu in the
paper) and alpha (denoted \alpha in the paper).
Example: ./run -u case-studies/university.abac 0.05 0.15 

./run -o inputFile noiseLevel tau
Meaning: add over-assignments to the ABAC policy in inputFile, and run the
ABAC mining algorithm with extension to detect over-assignments. Please
refer to the paper for the definition of noiseLevel (denoted \nu in the
paper) and tau (denoted \tau in the paper).
Example: ./run -o case-studies/university.abac 0.05 2 

./run -n underAssignmentNoiseLevel overAssignmentNoiseLevel attributeDataNoiseLevel inputFile
Meaning: add under-assignments, over-assignments, and attribute data noise
with the specified noise levels (measured as described in the paper) to the
ABAC policy in inputFile, and run the ABAC mining algorithm with extensions
to detect all three kinds of noises, for a range of values of alpha and
tau, and report the values of alpha and tau which yield the best noise
detection.
Example: ./run -n 0.05 0.1 0.1 case-studies/university.abac

./run -c "Inputfile1" "Inputfile2" ...
Meaning: parse the input files one after another and get the set
and count of the atomic values of the input files and generate a 
report "atomic_values_count_report.txt" in the current workspace of 
the project which has the set and count of atomic values of each file
Example: ./run -c "./case-studies/university.abac" "./case-studies/project-management.abac" 
Warning: For every new run output file "atomic_values_count_report.txt" will get overwritten.
Also this is an exception for running modes that do not include the "outputFile" option that 
it generates a output file with predefined name and output is not written to standard output
(i.e) the terminal. 

./run -t caseStudyType outputFolder N
Meaning: generate case study sample policy with non uniform synthetic attribute data of size N.
caseStudyType may be university, healthcare, or projectmanagement.
N is Ndept or Nward, depending on caseStudyType.
Policy files will be generated in the outputFolder size Ndept to 1 
with naming convention eg UniversityCaseStudy_1.abac, UniversityCaseStudy_2.abac and so on till Ndept or Nward
Example: ./run -t university "../sample-policies-non-uniform-synthetic-attribute-data/university/Set1/" 10

======================================================================
CONCRETE SYNTAX FOR ABAC POLICIES

an ABAC policy file has extension .abac. it contains lines of the forms:

userAttrib(uid, attribute1=value1, attribute2=value2, ...)
resourceAttrib(rid, attribute1=value1, attribute2=value2, ...)
rule(uae; pae; ops; con)
unremovableUserAttribs(attribute1, attribute2, ...)
unremovableResourceAttribs(attribute1, attribute2, ...)

the first argument of a userAttrib is automatically assigned to an
attribute named "uid".  the first argument of a resourceAttrib is
automatically assigned to an attribute named "rid".  value1, value2,
... are atomic values or sets.  an atomic value is a string that starts
with a character other than a left curly brace.  a set has the form
"{element1 element2 ...}".  note that elements of a set are separated by
spaces, not commas.

uae is a user attribute expression.  it is a conjunction, with the
conjuncts separated by commas.  each conjunct has the form "a in {value1
value2 ...}" where a is a single-valued user attribute, or "a supseteqIn
{set1 set2 ...}"  (meaning that a is a superset of one of the sets in {set1
set2 ...}) where a is a multi-valued user attribute, and set1, set2, ...,
are sets of the form {value1+value2+...}".  note that sets (such as set1,
set2, ...) that are nested inside another set are written with the elements
separated by "+" (instead of " ") to simplify parsing.

rae is a resource attribute expression.  the syntax is analogous to the
syntax for user attribute expressions.

ops is a set of operations.

con is a constraint.  it is a conjunction of atomic constraints, with the
conjuncts separated by commas.  an atomic constraint is a formula of the form
aum > arm, aum ] ars, or aus=ars$,where aus is a single-valued user aum is
a multi-valued user attribute, ars is a single-valued resource attribute,
and arm is a multi-valued resource attribute.  note that > denotes
$\supseteq$ and "]" denotes "\ni".

userAttrib and resourceAttrib statements must precede rule statements.

the sets of users, resources, operations, user attributes, permission
attributes, are not specified explicitly.  they are implicitly defined as
the sets containing the users, permissions, etc., that are mentioned in the
policy.

lines starting with "#" are comments.

the unremovableUserAttribs and unremovableResourceAttribs statements
specify user attributes and resource attributes that \eliminateconjuncts
should not eliminate from the UAE and PAE, respectively.

======================================================================
DIRECTORIES

case-studies/ contains .abac files for case studies with manually written
attribute data

gen-case-studies/ contains .abac files for case studies with synthetic attribute
data.  for example, gen-case-studies/university/university-sets_10_1.abac
is for the university case study with Ndept=10, and is the first such
policy (we generate several policies of each size, because some aspects of
the synthetic attribute data are pseudorandom).

generalization-error/ contains files with output from generalization
experiments.  for example, generalization-error/healthcare_0.15.output is
the result of generalization experiments for healthcare case study with
fraction=0.15.

ir-gen-case-studies/ contains policies for case studies with synthetic
attribute data, extended with data for irrelevant attributes. specifically,
the synthetic user attribute data and synthetic resource attribute data are
each extended with 5 ``irrelevant'' attributes having integer values in
[0..29] assigned uniformly at random.  these policies are not used in the
experiments described in the paper.

noise/ contains files with output from the program when run with the noise
detection extension on case studies with synthetic attribute data with
noise.

ran-case-studies-with-additional-uattrs/ contains synthetic policies for
synthetic policies with Naua = 2.  ("ran-case-studies" stands for "random
case studies", our old terminology for synthetic policies.)  for example,
ran-case-studies-with-additional-uattrs/synthetic_10_1_new.abac contains the
synthetic policy with Nrule = 10, and is the first such policy (we generate
several policies of each size).  Naua is mnemonic for "number of
assignments to undefined attributes".  this parameter is not named
explicitly in the paper, but it corresponds to the "2" in the sentence
"To make the attribute data more realistic, a final step of the algorithm
iterates over all users and, for each user u, assigns random values to
min(n_u, 2) randomly selected attributes whose value was \bot, where n_u is
the number of attributes that were equal to \bot for user u."

ran-case-studies/ contains synthetic policies with Naua = 0.  these
policies are not used in the experiments described in the paper.

progol/ contains Progol (.pl) files generated from .abac files, and .output
files with the output produced by running Progol on those .pl files.

verbose-output/ contains output files from the program when run on the case
studies.

lib/ contains commons-math-2.2.jar a library of lightweight, self-contained 
mathematics and statistics components addressing the most common problems not 
available in the Java programming language or Commons Lang. Obtained from
http://commons.apache.org/proper/commons-math/download_math.cgi 
