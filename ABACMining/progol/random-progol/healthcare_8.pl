%Settings
:- set(nodes,1612)?
:- set(h,100)?
%Mode Declarations
:- modeh(1,up(+user,+resource,#operation))?
:- modeb(1,positionU(+user,#positionUType))?
:- modeb(1,uidU(+user,#uidUType))?
:- modeb(*,teamsU(+user,#teamsUType))?
:- modeb(*,specialtiesU(+user,#specialtiesUType))?
:- modeb(1,wardU(+user,#wardUType))?
:- modeb(*,agentForU(+user,#agentForUType))?
:- modeb(1,treatingTeamR(+resource,#treatingTeamRType))?
:- modeb(1,authorR(+resource,#authorRType))?
:- modeb(1,topicsR(+resource,#topicsRType))?
:- modeb(1,patientR(+resource,#patientRType))?
:- modeb(1,wardR(+resource,#wardRType))?
:- modeb(1,ridR(+resource,#ridRType))?
:- modeb(1,typeR(+resource,#typeRType))?
:- modeb(1,teamsU_superset_topicsR(+user,+resource))?
:- modeb(1,specialtiesU_superset_topicsR(+user,+resource))?
:- modeb(1,agentForU_superset_topicsR(+user,+resource))?
:- modeb(1,teamsU_contains_treatingTeamR(+user,+resource))?
:- modeb(1,teamsU_contains_authorR(+user,+resource))?
:- modeb(1,teamsU_contains_patientR(+user,+resource))?
:- modeb(1,teamsU_contains_wardR(+user,+resource))?
:- modeb(1,teamsU_contains_ridR(+user,+resource))?
:- modeb(1,teamsU_contains_typeR(+user,+resource))?
:- modeb(1,specialtiesU_contains_treatingTeamR(+user,+resource))?
:- modeb(1,specialtiesU_contains_authorR(+user,+resource))?
:- modeb(1,specialtiesU_contains_patientR(+user,+resource))?
:- modeb(1,specialtiesU_contains_wardR(+user,+resource))?
:- modeb(1,specialtiesU_contains_ridR(+user,+resource))?
:- modeb(1,specialtiesU_contains_typeR(+user,+resource))?
:- modeb(1,agentForU_contains_treatingTeamR(+user,+resource))?
:- modeb(1,agentForU_contains_authorR(+user,+resource))?
:- modeb(1,agentForU_contains_patientR(+user,+resource))?
:- modeb(1,agentForU_contains_wardR(+user,+resource))?
:- modeb(1,agentForU_contains_ridR(+user,+resource))?
:- modeb(1,agentForU_contains_typeR(+user,+resource))?
:- modeb(1,positionU_equals_treatingTeamR(+user,+resource))?
:- modeb(1,positionU_equals_authorR(+user,+resource))?
:- modeb(1,positionU_equals_patientR(+user,+resource))?
:- modeb(1,positionU_equals_wardR(+user,+resource))?
:- modeb(1,positionU_equals_ridR(+user,+resource))?
:- modeb(1,positionU_equals_typeR(+user,+resource))?
:- modeb(1,uidU_equals_treatingTeamR(+user,+resource))?
:- modeb(1,uidU_equals_authorR(+user,+resource))?
:- modeb(1,uidU_equals_patientR(+user,+resource))?
:- modeb(1,uidU_equals_wardR(+user,+resource))?
:- modeb(1,uidU_equals_ridR(+user,+resource))?
:- modeb(1,uidU_equals_typeR(+user,+resource))?
:- modeb(1,wardU_equals_treatingTeamR(+user,+resource))?
:- modeb(1,wardU_equals_authorR(+user,+resource))?
:- modeb(1,wardU_equals_patientR(+user,+resource))?
:- modeb(1,wardU_equals_wardR(+user,+resource))?
:- modeb(1,wardU_equals_ridR(+user,+resource))?
:- modeb(1,wardU_equals_typeR(+user,+resource))?
:- modeb(1,superset(+attribValSet,+attribValSet))?
:- modeb(1,element(+attribValAtomic,+attribValSet))?
:- modeb(1,element(+attribValAtomic,#attribValSet))?
:- modeb(1,element(#attribValAtomic,+attribValSet))?
%Types
user(oncnurse1).
user(oncnurse2).
user(oncdoc4).
user(cardoc2).
user(cardoc1).
user(oncagent1).
user(anesdoc1).
user(oncagent2).
user(oncdoc1).
user(oncdoc3).
user(oncdoc2).
user(oncpat1).
user(oncpat2).
user(caragent1).
user(carnurse1).
user(caragent2).
user(carnurse2).
user(carpat1).
user(doc2).
user(carpat2).
user(doc1).
resource(oncpat2oncitem).
resource(oncpat2noteitem).
resource(carpat1hr).
resource(oncpat1noteitem).
resource(oncpat2nursingitem).
resource(carpat2nursingitem).
resource(carpat1noteitem).
resource(carpat2caritem).
resource(oncpat1nursingitem).
resource(oncpat1oncitem).
resource(carpat1caritem).
resource(oncpat2hr).
resource(carpat1nursingitem).
resource(oncpat1hr).
resource(carpat2hr).
resource(carpat2noteitem).
operation(read).
operation(additem).
operation(addnote).
attribValAtomic(oncdoc4).
attribValAtomic(hr).
attribValAtomic(anesthesiology).
attribValAtomic(oncward).
attribValAtomic(carpat2nursingitem).
attribValAtomic(doctor).
attribValAtomic(carpat1noteitem).
attribValAtomic(carpat2caritem).
attribValAtomic(oncpat1nursingitem).
attribValAtomic(nurse).
attribValAtomic(carpat1nursingitem).
attribValAtomic(carnurse1).
attribValAtomic(caragent1).
attribValAtomic(carnurse2).
attribValAtomic(caragent2).
attribValAtomic(carpat2noteitem).
attribValAtomic(pediatrics).
attribValAtomic(oncnurse1).
attribValAtomic(oncnurse2).
attribValAtomic(cardiology).
attribValAtomic(carpat1hr).
attribValAtomic(oncagent1).
attribValAtomic(carteam2).
attribValAtomic(oncagent2).
attribValAtomic(carteam1).
attribValAtomic(oncpat2hr).
attribValAtomic(oncpat1hr).
attribValAtomic(carpat2hr).
attribValAtomic(oncpat2oncitem).
attribValAtomic(hritem).
attribValAtomic(neurology).
attribValAtomic(oncpat2noteitem).
attribValAtomic(oncpat2nursingitem).
attribValAtomic(oncpat1oncitem).
attribValAtomic(carpat1caritem).
attribValAtomic(carpat1).
attribValAtomic(carpat2).
attribValAtomic(note).
attribValAtomic(carward).
attribValAtomic(cardoc2).
attribValAtomic(cardoc1).
attribValAtomic(oncpat1noteitem).
attribValAtomic(oncology).
attribValAtomic(anesdoc1).
attribValAtomic(oncdoc1).
attribValAtomic(oncdoc3).
attribValAtomic(oncdoc2).
attribValAtomic(oncpat1).
attribValAtomic(oncteam2).
attribValAtomic(oncteam1).
attribValAtomic(oncpat2).
attribValAtomic(doc2).
attribValAtomic(nursing).
attribValAtomic(doc1).
attribValSet([]).
attribValSet([V|Vs]) :- attribValAtomic(V), attribValSet(Vs).
positionUType(nurse).
positionUType(doctor).
uidUType(oncnurse1).
uidUType(oncnurse2).
uidUType(oncdoc4).
uidUType(cardoc2).
uidUType(cardoc1).
uidUType(oncagent1).
uidUType(anesdoc1).
uidUType(oncagent2).
uidUType(oncdoc1).
uidUType(oncdoc3).
uidUType(oncdoc2).
uidUType(oncpat1).
uidUType(oncpat2).
uidUType(caragent1).
uidUType(carnurse1).
uidUType(caragent2).
uidUType(carnurse2).
uidUType(carpat1).
uidUType(doc2).
uidUType(carpat2).
uidUType(doc1).
teamsUType(oncteam1).
teamsUType(carteam1).
teamsUType(oncteam2).
teamsUType(oncteam1).
teamsUType(carteam2).
teamsUType(oncteam2).
teamsUType(oncteam1).
teamsUType(carteam1).
specialtiesUType(cardiology).
specialtiesUType(oncology).
specialtiesUType(anesthesiology).
specialtiesUType(pediatrics).
specialtiesUType(oncology).
specialtiesUType(cardiology).
specialtiesUType(neurology).
wardUType(carward).
wardUType(oncward).
agentForUType(oncpat2).
agentForUType(carpat2).
treatingTeamUType(oncteam2).
treatingTeamUType(oncteam1).
treatingTeamUType(carteam2).
treatingTeamUType(carteam1).
authorUType(oncdoc1).
authorUType(oncnurse1).
authorUType(oncnurse2).
authorUType(cardoc2).
authorUType(oncpat1).
authorUType(caragent1).
authorUType(carnurse1).
authorUType(carnurse2).
authorUType(doc2).
authorUType(carpat1).
authorUType(oncagent1).
authorUType(doc1).
topicsUType(cardiology).
topicsUType(oncology).
topicsUType(nursing).
topicsUType(note).
patientUType(oncpat1).
patientUType(oncpat2).
patientUType(carpat1).
patientUType(carpat2).
wardUType(carward).
wardUType(oncward).
ridUType(oncpat2oncitem).
ridUType(oncpat2noteitem).
ridUType(carpat1hr).
ridUType(oncpat1noteitem).
ridUType(oncpat2nursingitem).
ridUType(carpat2nursingitem).
ridUType(carpat1noteitem).
ridUType(carpat2caritem).
ridUType(oncpat1nursingitem).
ridUType(oncpat1oncitem).
ridUType(carpat1caritem).
ridUType(oncpat2hr).
ridUType(carpat1nursingitem).
ridUType(oncpat1hr).
ridUType(carpat2hr).
ridUType(carpat2noteitem).
typeUType(hritem).
typeUType(hr).
%Background Knowledge
positionU(oncnurse1,nurse).
uidU(oncnurse1,oncnurse1).
wardU(oncnurse1,oncward).
positionU(oncnurse2,nurse).
uidU(oncnurse2,oncnurse2).
wardU(oncnurse2,oncward).
positionU(oncdoc4,doctor).
uidU(oncdoc4,oncdoc4).
teamsU(oncdoc4,oncteam2).
specialtiesU(oncdoc4,oncology).
positionU(cardoc2,doctor).
uidU(cardoc2,cardoc2).
teamsU(cardoc2,carteam2).
specialtiesU(cardoc2,cardiology).
positionU(cardoc1,doctor).
uidU(cardoc1,cardoc1).
teamsU(cardoc1,carteam1).
specialtiesU(cardoc1,cardiology).
uidU(oncagent1,oncagent1).
agentForU(oncagent1,oncpat2).
positionU(anesdoc1,doctor).
uidU(anesdoc1,anesdoc1).
teamsU(anesdoc1,oncteam1).
teamsU(anesdoc1,carteam1).
specialtiesU(anesdoc1,anesthesiology).
uidU(oncagent2,oncagent2).
agentForU(oncagent2,oncpat2).
positionU(oncdoc1,doctor).
uidU(oncdoc1,oncdoc1).
teamsU(oncdoc1,oncteam2).
teamsU(oncdoc1,oncteam1).
specialtiesU(oncdoc1,oncology).
positionU(oncdoc3,doctor).
uidU(oncdoc3,oncdoc3).
teamsU(oncdoc3,oncteam2).
specialtiesU(oncdoc3,oncology).
positionU(oncdoc2,doctor).
uidU(oncdoc2,oncdoc2).
teamsU(oncdoc2,oncteam1).
specialtiesU(oncdoc2,oncology).
uidU(oncpat1,oncpat1).
wardU(oncpat1,oncward).
uidU(oncpat2,oncpat2).
wardU(oncpat2,oncward).
uidU(caragent1,caragent1).
agentForU(caragent1,carpat2).
positionU(carnurse1,nurse).
uidU(carnurse1,carnurse1).
wardU(carnurse1,carward).
uidU(caragent2,caragent2).
agentForU(caragent2,carpat2).
positionU(carnurse2,nurse).
uidU(carnurse2,carnurse2).
wardU(carnurse2,carward).
uidU(carpat1,carpat1).
wardU(carpat1,carward).
positionU(doc2,doctor).
uidU(doc2,doc2).
specialtiesU(doc2,cardiology).
specialtiesU(doc2,neurology).
uidU(carpat2,carpat2).
wardU(carpat2,carward).
positionU(doc1,doctor).
uidU(doc1,doc1).
specialtiesU(doc1,pediatrics).
specialtiesU(doc1,oncology).
treatingTeamR(oncpat2oncitem,oncteam2).
authorR(oncpat2oncitem,doc1).
topicsR(oncpat2oncitem,oncology).
patientR(oncpat2oncitem,oncpat2).
wardR(oncpat2oncitem,oncward).
ridR(oncpat2oncitem,oncpat2oncitem).
typeR(oncpat2oncitem,hritem).
treatingTeamR(oncpat2noteitem,oncteam2).
authorR(oncpat2noteitem,oncagent1).
topicsR(oncpat2noteitem,note).
patientR(oncpat2noteitem,oncpat2).
wardR(oncpat2noteitem,oncward).
ridR(oncpat2noteitem,oncpat2noteitem).
typeR(oncpat2noteitem,hritem).
treatingTeamR(carpat1hr,carteam1).
patientR(carpat1hr,carpat1).
wardR(carpat1hr,carward).
ridR(carpat1hr,carpat1hr).
typeR(carpat1hr,hr).
treatingTeamR(oncpat1noteitem,oncteam1).
authorR(oncpat1noteitem,oncpat1).
topicsR(oncpat1noteitem,note).
patientR(oncpat1noteitem,oncpat1).
wardR(oncpat1noteitem,oncward).
ridR(oncpat1noteitem,oncpat1noteitem).
typeR(oncpat1noteitem,hritem).
treatingTeamR(oncpat2nursingitem,oncteam2).
authorR(oncpat2nursingitem,oncnurse1).
topicsR(oncpat2nursingitem,nursing).
patientR(oncpat2nursingitem,oncpat2).
wardR(oncpat2nursingitem,oncward).
ridR(oncpat2nursingitem,oncpat2nursingitem).
typeR(oncpat2nursingitem,hritem).
treatingTeamR(carpat2nursingitem,carteam2).
authorR(carpat2nursingitem,carnurse2).
topicsR(carpat2nursingitem,nursing).
patientR(carpat2nursingitem,carpat2).
wardR(carpat2nursingitem,carward).
ridR(carpat2nursingitem,carpat2nursingitem).
typeR(carpat2nursingitem,hritem).
treatingTeamR(carpat1noteitem,carteam1).
authorR(carpat1noteitem,carpat1).
topicsR(carpat1noteitem,note).
patientR(carpat1noteitem,carpat1).
wardR(carpat1noteitem,carward).
ridR(carpat1noteitem,carpat1noteitem).
typeR(carpat1noteitem,hritem).
treatingTeamR(carpat2caritem,carteam2).
authorR(carpat2caritem,doc2).
topicsR(carpat2caritem,cardiology).
patientR(carpat2caritem,carpat2).
wardR(carpat2caritem,carward).
ridR(carpat2caritem,carpat2caritem).
typeR(carpat2caritem,hritem).
treatingTeamR(oncpat1nursingitem,oncteam1).
authorR(oncpat1nursingitem,oncnurse2).
topicsR(oncpat1nursingitem,nursing).
patientR(oncpat1nursingitem,oncpat1).
wardR(oncpat1nursingitem,oncward).
ridR(oncpat1nursingitem,oncpat1nursingitem).
typeR(oncpat1nursingitem,hritem).
treatingTeamR(oncpat1oncitem,oncteam1).
authorR(oncpat1oncitem,oncdoc1).
topicsR(oncpat1oncitem,oncology).
patientR(oncpat1oncitem,oncpat1).
wardR(oncpat1oncitem,oncward).
ridR(oncpat1oncitem,oncpat1oncitem).
typeR(oncpat1oncitem,hritem).
treatingTeamR(carpat1caritem,carteam1).
authorR(carpat1caritem,cardoc2).
topicsR(carpat1caritem,cardiology).
patientR(carpat1caritem,carpat1).
wardR(carpat1caritem,carward).
ridR(carpat1caritem,carpat1caritem).
typeR(carpat1caritem,hritem).
treatingTeamR(oncpat2hr,oncteam2).
patientR(oncpat2hr,oncpat2).
wardR(oncpat2hr,oncward).
ridR(oncpat2hr,oncpat2hr).
typeR(oncpat2hr,hr).
treatingTeamR(carpat1nursingitem,carteam1).
authorR(carpat1nursingitem,carnurse1).
topicsR(carpat1nursingitem,nursing).
patientR(carpat1nursingitem,carpat1).
wardR(carpat1nursingitem,carward).
ridR(carpat1nursingitem,carpat1nursingitem).
typeR(carpat1nursingitem,hritem).
treatingTeamR(oncpat1hr,oncteam1).
patientR(oncpat1hr,oncpat1).
wardR(oncpat1hr,oncward).
ridR(oncpat1hr,oncpat1hr).
typeR(oncpat1hr,hr).
treatingTeamR(carpat2hr,carteam2).
patientR(carpat2hr,carpat2).
wardR(carpat2hr,carward).
ridR(carpat2hr,carpat2hr).
typeR(carpat2hr,hr).
treatingTeamR(carpat2noteitem,carteam2).
authorR(carpat2noteitem,caragent1).
topicsR(carpat2noteitem,note).
patientR(carpat2noteitem,carpat2).
wardR(carpat2noteitem,carward).
ridR(carpat2noteitem,carpat2noteitem).
typeR(carpat2noteitem,hritem).
teamsU_superset_topicsR(U,R) :- setof(X,teamsU(U,X),SU), setof(Y,topicsR(R,Y),SR), superset(SU,SR), not(SR==[]).
specialtiesU_superset_topicsR(U,R) :- setof(X,specialtiesU(U,X),SU), setof(Y,topicsR(R,Y),SR), superset(SU,SR), not(SR==[]).
agentForU_superset_topicsR(U,R) :- setof(X,agentForU(U,X),SU), setof(Y,topicsR(R,Y),SR), superset(SU,SR), not(SR==[]).
teamsU_contains_treatingTeamR(U,R) :-teamsU(U,X),treatingTeamR(R,X).
teamsU_contains_authorR(U,R) :-teamsU(U,X),authorR(R,X).
teamsU_contains_patientR(U,R) :-teamsU(U,X),patientR(R,X).
teamsU_contains_wardR(U,R) :-teamsU(U,X),wardR(R,X).
teamsU_contains_ridR(U,R) :-teamsU(U,X),ridR(R,X).
teamsU_contains_typeR(U,R) :-teamsU(U,X),typeR(R,X).
specialtiesU_contains_treatingTeamR(U,R) :-specialtiesU(U,X),treatingTeamR(R,X).
specialtiesU_contains_authorR(U,R) :-specialtiesU(U,X),authorR(R,X).
specialtiesU_contains_patientR(U,R) :-specialtiesU(U,X),patientR(R,X).
specialtiesU_contains_wardR(U,R) :-specialtiesU(U,X),wardR(R,X).
specialtiesU_contains_ridR(U,R) :-specialtiesU(U,X),ridR(R,X).
specialtiesU_contains_typeR(U,R) :-specialtiesU(U,X),typeR(R,X).
agentForU_contains_treatingTeamR(U,R) :-agentForU(U,X),treatingTeamR(R,X).
agentForU_contains_authorR(U,R) :-agentForU(U,X),authorR(R,X).
agentForU_contains_patientR(U,R) :-agentForU(U,X),patientR(R,X).
agentForU_contains_wardR(U,R) :-agentForU(U,X),wardR(R,X).
agentForU_contains_ridR(U,R) :-agentForU(U,X),ridR(R,X).
agentForU_contains_typeR(U,R) :-agentForU(U,X),typeR(R,X).
positionU_equals_treatingTeamR(U,R) :-positionU(U,X),treatingTeamR(R,X).
positionU_equals_authorR(U,R) :-positionU(U,X),authorR(R,X).
positionU_equals_patientR(U,R) :-positionU(U,X),patientR(R,X).
positionU_equals_wardR(U,R) :-positionU(U,X),wardR(R,X).
positionU_equals_ridR(U,R) :-positionU(U,X),ridR(R,X).
positionU_equals_typeR(U,R) :-positionU(U,X),typeR(R,X).
uidU_equals_treatingTeamR(U,R) :-uidU(U,X),treatingTeamR(R,X).
uidU_equals_authorR(U,R) :-uidU(U,X),authorR(R,X).
uidU_equals_patientR(U,R) :-uidU(U,X),patientR(R,X).
uidU_equals_wardR(U,R) :-uidU(U,X),wardR(R,X).
uidU_equals_ridR(U,R) :-uidU(U,X),ridR(R,X).
uidU_equals_typeR(U,R) :-uidU(U,X),typeR(R,X).
wardU_equals_treatingTeamR(U,R) :-wardU(U,X),treatingTeamR(R,X).
wardU_equals_authorR(U,R) :-wardU(U,X),authorR(R,X).
wardU_equals_patientR(U,R) :-wardU(U,X),patientR(R,X).
wardU_equals_wardR(U,R) :-wardU(U,X),wardR(R,X).
wardU_equals_ridR(U,R) :-wardU(U,X),ridR(R,X).
wardU_equals_typeR(U,R) :-wardU(U,X),typeR(R,X).
superset(Y,[A|X]) :- element(A,Y), superset(Y,X).
superset(Y,[]).
% Positive and negative examples
:- up(carpat2,oncpat2noteitem,addnote).
:- up(oncdoc3,carpat1nursingitem,addnote).
up(oncpat1,oncpat1hr,addnote).
:- up(carpat1,oncpat2nursingitem,addnote).
up(oncnurse1,oncpat2nursingitem,read).
:- up(carpat2,carpat1hr,addnote).
:- up(carpat1,carpat2noteitem,read).
:- up(carpat1,carpat2hr,addnote).
:- up(oncdoc1,oncpat2nursingitem,additem).
:- up(oncnurse2,oncpat1noteitem,addnote).
:- up(oncpat1,oncpat2hr,addnote).
:- up(carpat1,oncpat2noteitem,additem).
:- up(cardoc1,carpat1hr,read).
:- up(cardoc2,carpat1nursingitem,read).
:- up(oncpat1,oncpat1nursingitem,read).
:- up(doc1,oncpat2hr,addnote).
:- up(oncdoc1,oncpat1hr,addnote).
:- up(oncnurse1,carpat2caritem,addnote).
:- up(carpat2,oncpat2nursingitem,read).
:- up(anesdoc1,carpat2nursingitem,read).
up(oncdoc3,oncpat2oncitem,read).
:- up(oncpat2,oncpat1nursingitem,additem).
:- up(oncpat2,carpat2caritem,read).
:- up(oncdoc4,carpat1hr,read).
:- up(oncagent1,carpat2caritem,addnote).
up(cardoc2,carpat2hr,additem).
:- up(oncdoc1,oncpat2noteitem,additem).
:- up(oncagent2,oncpat1noteitem,addnote).
:- up(carnurse2,oncpat2nursingitem,addnote).
:- up(oncagent2,carpat2noteitem,additem).
:- up(doc2,carpat2nursingitem,addnote).
:- up(oncagent2,carpat2hr,additem).
:- up(oncdoc4,oncpat1noteitem,addnote).
:- up(caragent2,oncpat1noteitem,addnote).
:- up(anesdoc1,oncpat1hr,addnote).
:- up(oncdoc2,carpat1noteitem,additem).
:- up(oncdoc1,oncpat1nursingitem,addnote).
:- up(doc2,oncpat1noteitem,addnote).
:- up(carpat1,oncpat1hr,additem).
:- up(carpat2,carpat2nursingitem,addnote).
:- up(carpat1,carpat2caritem,additem).
:- up(oncagent1,oncpat1nursingitem,additem).
:- up(oncpat2,carpat2noteitem,additem).
:- up(oncdoc2,oncpat2noteitem,addnote).
:- up(doc1,oncpat1nursingitem,addnote).
:- up(carpat1,carpat1noteitem,addnote).
:- up(oncpat1,oncpat1noteitem,addnote).
:- up(cardoc2,oncpat1noteitem,addnote).
:- up(doc1,carpat1caritem,additem).
up(cardoc1,carpat1caritem,read).
:- up(anesdoc1,oncpat2noteitem,additem).
:- up(oncdoc3,oncpat1noteitem,read).
:- up(doc1,carpat2caritem,additem).
:- up(oncpat1,oncpat1hr,additem).
:- up(caragent2,carpat1caritem,addnote).
:- up(carnurse1,carpat2caritem,addnote).
:- up(oncagent1,oncpat1hr,read).
:- up(carnurse2,oncpat2noteitem,addnote).
:- up(caragent2,oncpat1oncitem,addnote).
:- up(oncdoc3,oncpat2noteitem,additem).
:- up(oncpat2,oncpat1hr,additem).
:- up(oncnurse2,oncpat2noteitem,additem).
:- up(anesdoc1,oncpat2oncitem,addnote).
:- up(carnurse2,oncpat2oncitem,additem).
:- up(oncdoc3,carpat1nursingitem,read).
:- up(oncnurse1,carpat1nursingitem,additem).
:- up(oncagent1,oncpat2nursingitem,addnote).
:- up(carnurse2,carpat2nursingitem,additem).
:- up(carpat2,carpat2nursingitem,read).
:- up(caragent1,oncpat1hr,additem).
:- up(oncnurse2,carpat1caritem,additem).
:- up(oncagent2,oncpat2nursingitem,additem).
:- up(carnurse1,oncpat2nursingitem,read).
:- up(doc2,carpat1caritem,read).
:- up(oncpat2,carpat2hr,read).
:- up(oncnurse1,carpat2hr,additem).
:- up(carpat1,oncpat2hr,read).
:- up(carnurse1,carpat2hr,addnote).
:- up(carpat2,oncpat1nursingitem,addnote).
:- up(oncpat2,oncpat1nursingitem,read).
:- up(oncdoc1,carpat1nursingitem,additem).
:- up(cardoc1,carpat2hr,addnote).
:- up(oncpat2,oncpat2oncitem,addnote).
:- up(carnurse2,oncpat1oncitem,additem).
:- up(oncpat2,carpat1nursingitem,addnote).
:- up(caragent1,carpat1caritem,additem).
:- up(oncnurse1,oncpat1oncitem,addnote).
:- up(oncnurse1,carpat2nursingitem,read).
:- up(oncpat1,oncpat1noteitem,additem).
:- up(doc1,oncpat2nursingitem,addnote).
:- up(anesdoc1,oncpat2hr,addnote).
:- up(caragent1,oncpat2oncitem,additem).
:- up(oncdoc2,oncpat2nursingitem,read).
:- up(carpat1,carpat2caritem,addnote).
:- up(cardoc1,oncpat2oncitem,addnote).
:- up(oncdoc4,oncpat2hr,addnote).
:- up(carpat1,carpat2nursingitem,additem).
:- up(doc2,carpat2noteitem,additem).
:- up(oncdoc4,carpat1nursingitem,addnote).
:- up(oncagent1,carpat2caritem,read).
:- up(oncdoc4,carpat1hr,addnote).
:- up(oncagent2,oncpat2nursingitem,addnote).
:- up(cardoc2,oncpat2nursingitem,addnote).
:- up(caragent2,oncpat1oncitem,additem).
:- up(carnurse1,oncpat1noteitem,additem).
up(carpat1,carpat1hr,addnote).
:- up(oncdoc4,oncpat1oncitem,additem).
:- up(caragent1,oncpat2noteitem,additem).
:- up(oncdoc4,carpat2nursingitem,read).
:- up(doc2,oncpat1nursingitem,addnote).
:- up(anesdoc1,oncpat1nursingitem,additem).
:- up(caragent2,carpat2hr,read).
:- up(oncagent1,oncpat1noteitem,read).
:- up(carpat2,carpat2hr,additem).
:- up(anesdoc1,carpat1caritem,read).
:- up(oncnurse1,oncpat2hr,addnote).
:- up(oncnurse2,oncpat2oncitem,addnote).
:- up(carnurse2,oncpat1nursingitem,read).
:- up(doc2,oncpat2hr,additem).
up(oncagent1,oncpat2hr,addnote).
:- up(oncdoc2,oncpat1nursingitem,read).
:- up(oncdoc3,oncpat2hr,addnote).
:- up(caragent1,carpat1caritem,addnote).
:- up(oncpat1,carpat1caritem,read).
:- up(oncagent1,oncpat1hr,additem).
:- up(oncagent2,carpat1hr,addnote).
:- up(cardoc2,oncpat1nursingitem,addnote).
:- up(caragent2,oncpat2hr,read).
:- up(carnurse2,carpat2hr,read).
:- up(oncnurse1,carpat2noteitem,addnote).
:- up(oncagent1,carpat1nursingitem,additem).
:- up(doc2,carpat1hr,read).
:- up(oncnurse2,carpat1noteitem,additem).
:- up(oncagent1,carpat1hr,addnote).
:- up(carnurse2,oncpat2nursingitem,additem).
:- up(doc2,oncpat1noteitem,additem).
:- up(caragent1,carpat1caritem,read).
:- up(oncdoc1,oncpat1nursingitem,additem).
:- up(carnurse1,oncpat1noteitem,read).
:- up(oncdoc3,carpat2noteitem,read).
:- up(oncagent1,carpat1nursingitem,addnote).
:- up(carnurse2,oncpat2hr,read).
:- up(oncdoc2,carpat2noteitem,additem).
:- up(carnurse2,oncpat1noteitem,additem).
:- up(oncagent2,oncpat1noteitem,additem).
:- up(oncagent1,oncpat1oncitem,read).
:- up(carpat2,carpat1caritem,read).
:- up(oncnurse2,carpat2hr,addnote).
:- up(oncagent1,oncpat1nursingitem,read).
:- up(oncagent1,carpat2noteitem,read).
:- up(oncdoc2,carpat1noteitem,addnote).
:- up(oncpat2,oncpat1hr,addnote).
:- up(anesdoc1,oncpat2hr,additem).
:- up(doc1,carpat1noteitem,addnote).
:- up(carpat1,oncpat1nursingitem,addnote).
:- up(cardoc2,carpat2caritem,additem).
:- up(doc2,carpat2nursingitem,additem).
:- up(oncpat1,carpat1caritem,additem).
:- up(oncdoc4,oncpat2noteitem,addnote).
:- up(anesdoc1,carpat1caritem,additem).
:- up(cardoc1,carpat1hr,addnote).
:- up(caragent1,oncpat1noteitem,read).
:- up(caragent2,oncpat1oncitem,read).
:- up(anesdoc1,oncpat1oncitem,additem).
:- up(carpat2,carpat2hr,read).
:- up(carpat1,carpat1hr,read).
up(carnurse1,carpat2hr,additem).
:- up(oncdoc4,carpat2nursingitem,addnote).
:- up(carnurse1,oncpat2hr,additem).
:- up(oncdoc1,carpat1caritem,addnote).
up(oncnurse1,oncpat1hr,additem).
up(oncdoc1,oncpat2hr,additem).
:- up(caragent2,carpat1caritem,read).
:- up(oncdoc4,carpat2caritem,additem).
:- up(oncnurse1,oncpat2noteitem,read).
:- up(oncpat1,oncpat1oncitem,additem).
:- up(caragent2,oncpat2noteitem,additem).
up(cardoc2,carpat2caritem,read).
:- up(carnurse2,carpat1caritem,read).
:- up(carnurse1,carpat1hr,addnote).
:- up(carnurse1,oncpat2hr,addnote).
:- up(carnurse2,oncpat1hr,read).
:- up(carnurse1,carpat1nursingitem,additem).
:- up(oncagent2,carpat2caritem,read).
:- up(oncnurse2,carpat1caritem,read).
:- up(oncdoc2,oncpat2oncitem,addnote).
:- up(carnurse1,oncpat2oncitem,addnote).
:- up(oncdoc1,carpat1noteitem,read).
:- up(carpat1,oncpat1noteitem,additem).
:- up(carnurse2,carpat1nursingitem,additem).
:- up(carpat1,carpat2hr,additem).
:- up(anesdoc1,carpat2noteitem,addnote).
:- up(oncpat1,oncpat2nursingitem,read).
:- up(caragent2,oncpat1hr,read).
:- up(oncnurse2,carpat2nursingitem,additem).
:- up(oncdoc3,carpat1noteitem,read).
:- up(oncnurse1,carpat1hr,read).
:- up(carnurse1,oncpat1nursingitem,additem).
:- up(oncdoc2,carpat1hr,additem).
:- up(carnurse2,oncpat2noteitem,additem).
:- up(carpat1,oncpat2noteitem,read).
:- up(oncpat2,carpat2caritem,addnote).
:- up(cardoc2,oncpat1oncitem,addnote).
:- up(doc1,carpat2hr,read).
:- up(caragent1,oncpat1nursingitem,addnote).
:- up(cardoc2,oncpat1noteitem,read).
:- up(doc1,oncpat1noteitem,addnote).
:- up(caragent1,oncpat1hr,addnote).
:- up(oncpat2,carpat1noteitem,additem).
:- up(carpat1,oncpat1hr,read).
:- up(oncnurse2,oncpat2oncitem,read).
:- up(oncdoc4,oncpat1nursingitem,additem).
:- up(carpat1,oncpat1oncitem,addnote).
:- up(oncdoc1,carpat2caritem,additem).
:- up(oncdoc4,oncpat1oncitem,addnote).
:- up(anesdoc1,oncpat2hr,read).
:- up(carpat1,oncpat1noteitem,addnote).
up(doc1,oncpat2oncitem,read).
:- up(carpat2,carpat1nursingitem,addnote).
:- up(oncnurse2,oncpat2hr,read).
:- up(oncagent2,carpat1caritem,read).
:- up(oncagent1,carpat1caritem,additem).
:- up(oncagent2,oncpat1oncitem,additem).
:- up(cardoc1,carpat2noteitem,addnote).
:- up(carpat2,carpat2nursingitem,additem).
:- up(oncpat1,carpat1caritem,addnote).
:- up(oncpat1,carpat2nursingitem,addnote).
:- up(carpat2,oncpat2nursingitem,additem).
:- up(oncagent2,carpat2caritem,addnote).
:- up(carnurse2,carpat2noteitem,additem).
:- up(oncnurse2,oncpat1nursingitem,additem).
:- up(oncnurse1,oncpat2oncitem,additem).
:- up(oncpat2,carpat1caritem,additem).
:- up(oncnurse2,carpat1hr,addnote).
:- up(oncnurse2,oncpat2nursingitem,additem).
:- up(caragent1,oncpat2oncitem,read).
up(oncnurse2,oncpat1hr,additem).
:- up(oncpat1,carpat1nursingitem,read).
:- up(oncdoc1,carpat1nursingitem,read).
:- up(doc1,carpat2hr,additem).
:- up(cardoc2,oncpat2hr,read).
:- up(oncdoc2,carpat1nursingitem,read).
:- up(oncpat1,oncpat2noteitem,additem).
:- up(oncdoc2,oncpat2noteitem,read).
:- up(carpat2,carpat2noteitem,addnote).
up(carnurse1,carpat1hr,additem).
:- up(carpat1,carpat1nursingitem,read).
:- up(cardoc1,carpat1nursingitem,addnote).
:- up(cardoc1,oncpat2hr,read).
:- up(oncdoc3,carpat2nursingitem,read).
:- up(oncdoc3,carpat2nursingitem,additem).
:- up(cardoc2,oncpat1hr,additem).
:- up(carnurse1,oncpat2noteitem,addnote).
:- up(anesdoc1,oncpat1noteitem,read).
:- up(oncdoc2,carpat2nursingitem,read).
:- up(oncagent1,oncpat2oncitem,additem).
:- up(oncpat2,oncpat2oncitem,read).
:- up(oncdoc4,oncpat2nursingitem,addnote).
:- up(oncagent1,oncpat2oncitem,addnote).
:- up(caragent1,oncpat2nursingitem,addnote).
:- up(anesdoc1,carpat2hr,additem).
:- up(carnurse2,oncpat1hr,addnote).
:- up(caragent2,carpat1nursingitem,read).
:- up(carnurse2,carpat2caritem,read).
:- up(oncdoc1,oncpat1hr,read).
:- up(oncnurse1,carpat1hr,addnote).
:- up(cardoc2,oncpat1hr,read).
:- up(cardoc2,oncpat2oncitem,read).
:- up(cardoc1,oncpat2hr,addnote).
:- up(cardoc2,carpat2noteitem,additem).
:- up(caragent2,carpat2noteitem,addnote).
:- up(oncagent2,oncpat2oncitem,read).
:- up(oncdoc4,carpat2hr,addnote).
:- up(oncagent1,carpat1caritem,addnote).
:- up(oncpat2,oncpat2nursingitem,read).
:- up(oncnurse2,oncpat2noteitem,read).
:- up(oncpat2,carpat1nursingitem,additem).
:- up(carpat2,oncpat1oncitem,additem).
:- up(oncpat1,carpat2hr,additem).
:- up(doc1,carpat2nursingitem,addnote).
:- up(anesdoc1,oncpat1nursingitem,addnote).
:- up(carnurse2,oncpat2oncitem,read).
up(carpat2,carpat2noteitem,read).
:- up(carpat1,oncpat2oncitem,additem).
:- up(doc2,carpat1noteitem,read).
:- up(doc2,oncpat1hr,read).
:- up(carnurse2,oncpat2noteitem,read).
:- up(cardoc1,carpat2caritem,read).
:- up(carnurse2,carpat2noteitem,read).
:- up(carnurse2,oncpat1oncitem,addnote).
:- up(oncdoc3,carpat2nursingitem,addnote).
:- up(caragent1,carpat2nursingitem,addnote).
up(oncagent2,oncpat2noteitem,read).
:- up(anesdoc1,carpat1noteitem,read).
:- up(oncdoc2,carpat2nursingitem,addnote).
:- up(oncdoc4,carpat1noteitem,additem).
:- up(doc2,carpat2nursingitem,read).
:- up(carpat1,carpat2hr,read).
:- up(carnurse1,carpat2caritem,read).
:- up(caragent1,oncpat1oncitem,addnote).
:- up(cardoc2,carpat2noteitem,read).
:- up(cardoc1,oncpat1noteitem,read).
:- up(oncdoc4,oncpat2oncitem,addnote).
:- up(oncpat2,oncpat1noteitem,addnote).
:- up(oncdoc2,oncpat1hr,addnote).
:- up(oncpat2,oncpat1noteitem,additem).
:- up(caragent1,carpat2hr,additem).
:- up(oncdoc3,oncpat1nursingitem,addnote).
:- up(carpat1,oncpat1oncitem,read).
:- up(oncpat1,carpat1noteitem,additem).
:- up(carpat1,oncpat2hr,addnote).
:- up(cardoc2,oncpat2nursingitem,read).
up(cardoc1,carpat1hr,additem).
:- up(cardoc1,carpat2hr,additem).
:- up(oncagent1,oncpat1oncitem,additem).
:- up(oncnurse1,carpat2nursingitem,additem).
:- up(carpat2,oncpat1noteitem,read).
:- up(carpat2,oncpat2noteitem,additem).
:- up(caragent2,oncpat2oncitem,addnote).
:- up(oncdoc3,carpat2hr,additem).
:- up(carpat1,oncpat1noteitem,read).
:- up(oncdoc1,carpat2nursingitem,read).
:- up(carpat1,carpat1hr,additem).
:- up(carnurse2,carpat1hr,read).
:- up(caragent2,oncpat1nursingitem,additem).
:- up(cardoc1,oncpat2oncitem,additem).
:- up(caragent2,oncpat2nursingitem,addnote).
:- up(caragent1,carpat2hr,read).
:- up(carnurse1,oncpat2oncitem,additem).
:- up(cardoc1,oncpat2oncitem,read).
:- up(oncdoc3,carpat1hr,addnote).
:- up(oncagent1,oncpat2noteitem,addnote).
:- up(carpat1,carpat2noteitem,addnote).
:- up(oncdoc3,oncpat1hr,additem).
:- up(oncdoc1,carpat1hr,addnote).
:- up(oncdoc3,oncpat1noteitem,addnote).
up(caragent2,carpat2noteitem,read).
up(oncnurse1,oncpat2hr,additem).
:- up(oncnurse2,oncpat2nursingitem,addnote).
:- up(anesdoc1,oncpat1noteitem,additem).
:- up(carnurse2,carpat2hr,addnote).
:- up(oncpat1,oncpat1nursingitem,additem).
:- up(oncnurse1,carpat1nursingitem,addnote).
:- up(carnurse1,oncpat1oncitem,addnote).
:- up(carnurse1,carpat1hr,read).
:- up(carpat1,carpat1nursingitem,additem).
:- up(caragent1,oncpat1hr,read).
:- up(oncpat2,oncpat1oncitem,addnote).
up(anesdoc1,carpat1hr,additem).
up(oncnurse2,oncpat2hr,additem).
:- up(carnurse1,oncpat1nursingitem,addnote).
:- up(caragent2,oncpat1nursingitem,addnote).
:- up(oncpat2,carpat2nursingitem,additem).
:- up(doc1,carpat1noteitem,read).
:- up(carpat2,carpat1caritem,additem).
:- up(cardoc2,oncpat2oncitem,addnote).
:- up(oncagent1,carpat1noteitem,additem).
:- up(carnurse2,oncpat1nursingitem,addnote).
:- up(oncnurse1,carpat1caritem,read).
:- up(doc2,oncpat2hr,addnote).
:- up(oncdoc1,carpat1hr,additem).
:- up(oncdoc4,carpat2noteitem,read).
up(oncdoc3,oncpat2hr,additem).
:- up(oncdoc3,carpat2caritem,read).
:- up(oncdoc2,carpat1nursingitem,additem).
:- up(oncagent2,oncpat1hr,read).
:- up(oncdoc2,oncpat1nursingitem,additem).
:- up(carpat2,carpat2caritem,read).
:- up(caragent1,carpat1hr,additem).
:- up(doc2,carpat2noteitem,addnote).
:- up(doc1,carpat2hr,addnote).
:- up(oncpat2,carpat1caritem,read).
:- up(carpat2,carpat1caritem,addnote).
:- up(oncdoc3,oncpat1oncitem,read).
:- up(oncdoc3,oncpat2nursingitem,addnote).
:- up(cardoc1,oncpat2nursingitem,addnote).
up(oncdoc2,oncpat1oncitem,read).
:- up(cardoc2,oncpat2noteitem,addnote).
:- up(oncdoc3,oncpat2noteitem,addnote).
up(oncdoc1,oncpat1hr,additem).
:- up(anesdoc1,oncpat1oncitem,read).
:- up(oncdoc3,oncpat1hr,addnote).
:- up(oncpat1,carpat1hr,read).
:- up(anesdoc1,carpat2noteitem,additem).
:- up(caragent2,carpat1hr,addnote).
:- up(oncnurse2,carpat1hr,read).
:- up(carpat2,carpat1noteitem,read).
:- up(oncagent1,carpat2hr,addnote).
:- up(doc2,carpat1nursingitem,read).
:- up(doc1,carpat2nursingitem,read).
:- up(oncpat2,oncpat1noteitem,read).
:- up(oncdoc3,oncpat2hr,read).
:- up(cardoc2,oncpat1nursingitem,read).
:- up(anesdoc1,carpat2nursingitem,additem).
:- up(caragent1,carpat1noteitem,additem).
:- up(carnurse2,oncpat1nursingitem,additem).
:- up(carpat1,oncpat2oncitem,addnote).
up(oncnurse1,oncpat1nursingitem,read).
:- up(doc1,carpat2caritem,addnote).
:- up(oncdoc4,carpat2caritem,addnote).
:- up(oncdoc2,oncpat1hr,read).
:- up(oncagent1,oncpat2noteitem,additem).
:- up(doc1,oncpat1hr,read).
:- up(doc2,oncpat1noteitem,read).
:- up(cardoc1,carpat1caritem,additem).
:- up(carpat2,oncpat1nursingitem,read).
:- up(doc1,carpat1caritem,read).
:- up(oncpat2,carpat2noteitem,addnote).
:- up(doc2,oncpat1oncitem,addnote).
:- up(oncnurse1,oncpat1hr,read).
:- up(oncdoc4,oncpat1oncitem,read).
:- up(oncnurse2,carpat2noteitem,addnote).
:- up(carpat1,carpat2caritem,read).
:- up(oncagent2,carpat2noteitem,read).
:- up(oncagent2,carpat1hr,read).
:- up(cardoc1,oncpat2hr,additem).
:- up(oncdoc2,oncpat1noteitem,read).
:- up(oncdoc2,carpat1caritem,addnote).
:- up(cardoc2,carpat1nursingitem,additem).
:- up(oncdoc1,carpat2nursingitem,additem).
:- up(cardoc1,carpat2caritem,additem).
:- up(oncagent2,carpat2hr,addnote).
:- up(oncpat1,oncpat2oncitem,addnote).
:- up(doc1,oncpat1oncitem,addnote).
:- up(carpat1,oncpat2nursingitem,read).
:- up(oncdoc1,carpat1caritem,read).
:- up(doc1,carpat1hr,additem).
:- up(caragent1,carpat1nursingitem,additem).
up(oncnurse2,oncpat1nursingitem,read).
:- up(caragent2,oncpat2hr,addnote).
up(carnurse2,carpat1nursingitem,read).
:- up(carpat2,carpat1noteitem,additem).
:- up(carpat1,oncpat1hr,addnote).
:- up(doc2,carpat2noteitem,read).
:- up(oncpat2,oncpat1oncitem,read).
:- up(carnurse2,carpat1noteitem,addnote).
:- up(oncagent2,oncpat1hr,addnote).
:- up(oncpat2,oncpat2nursingitem,additem).
:- up(oncagent1,oncpat2hr,additem).
:- up(doc2,carpat1caritem,additem).
:- up(carpat2,carpat1hr,additem).
:- up(carnurse1,oncpat1hr,additem).
:- up(anesdoc1,carpat2caritem,read).
:- up(oncagent1,carpat2hr,additem).
:- up(cardoc1,oncpat2noteitem,additem).
:- up(oncdoc2,carpat1nursingitem,addnote).
up(oncdoc2,oncpat1hr,additem).
:- up(oncpat1,oncpat2hr,read).
:- up(doc2,oncpat2oncitem,addnote).
:- up(caragent1,oncpat2oncitem,addnote).
:- up(carpat2,carpat2noteitem,additem).
:- up(oncagent2,oncpat1nursingitem,additem).
:- up(oncdoc2,oncpat2hr,addnote).
:- up(oncagent2,carpat2noteitem,addnote).
:- up(oncdoc4,oncpat1hr,addnote).
:- up(oncpat2,carpat1nursingitem,read).
:- up(oncdoc2,oncpat1oncitem,additem).
:- up(oncnurse1,carpat1caritem,addnote).
:- up(carnurse1,carpat1nursingitem,addnote).
:- up(oncagent2,carpat2nursingitem,read).
:- up(oncdoc2,oncpat2oncitem,additem).
:- up(oncnurse1,carpat2nursingitem,addnote).
:- up(oncagent1,carpat2noteitem,addnote).
:- up(oncagent2,carpat2nursingitem,addnote).
:- up(oncpat2,carpat2nursingitem,addnote).
:- up(oncdoc4,carpat2hr,additem).
:- up(caragent2,oncpat1hr,addnote).
:- up(caragent1,carpat2caritem,read).
:- up(oncpat1,carpat2caritem,addnote).
:- up(caragent2,oncpat2oncitem,additem).
:- up(oncagent1,carpat2hr,read).
:- up(oncnurse1,carpat2noteitem,read).
:- up(oncnurse1,carpat1noteitem,addnote).
:- up(caragent2,oncpat1noteitem,read).
:- up(doc1,carpat2caritem,read).
:- up(doc1,oncpat1oncitem,additem).
:- up(anesdoc1,oncpat2oncitem,additem).
:- up(oncnurse2,carpat2noteitem,read).
:- up(cardoc1,carpat1caritem,addnote).
:- up(oncdoc1,oncpat2noteitem,read).
:- up(cardoc1,oncpat1hr,read).
:- up(doc2,carpat1noteitem,addnote).
:- up(oncdoc4,carpat1nursingitem,additem).
:- up(carnurse2,carpat1caritem,addnote).
:- up(oncdoc4,oncpat1noteitem,additem).
:- up(oncnurse1,oncpat1noteitem,read).
:- up(oncagent1,carpat1nursingitem,read).
:- up(cardoc1,carpat1nursingitem,additem).
:- up(oncpat2,carpat2hr,addnote).
:- up(carnurse1,oncpat1hr,read).
:- up(carnurse1,carpat2noteitem,additem).
:- up(anesdoc1,oncpat1oncitem,addnote).
:- up(anesdoc1,carpat1nursingitem,addnote).
:- up(cardoc1,carpat1noteitem,addnote).
:- up(oncnurse1,carpat2hr,read).
:- up(oncdoc2,carpat1hr,read).
:- up(doc2,carpat2hr,read).
:- up(oncnurse2,carpat1caritem,addnote).
:- up(anesdoc1,oncpat2nursingitem,addnote).
:- up(oncpat1,carpat2caritem,read).
:- up(cardoc1,carpat1noteitem,additem).
:- up(cardoc2,carpat1hr,addnote).
:- up(cardoc1,carpat2noteitem,additem).
:- up(oncdoc2,carpat1noteitem,read).
:- up(oncnurse1,oncpat2oncitem,addnote).
up(oncpat1,oncpat1noteitem,read).
:- up(oncpat1,oncpat2noteitem,addnote).
:- up(oncnurse2,oncpat1nursingitem,addnote).
:- up(oncagent2,oncpat1oncitem,addnote).
:- up(oncdoc3,carpat2caritem,addnote).
:- up(oncnurse2,oncpat1hr,read).
:- up(carpat1,carpat2nursingitem,addnote).
:- up(oncdoc1,oncpat2nursingitem,addnote).
:- up(carnurse1,oncpat2nursingitem,additem).
:- up(oncdoc3,oncpat1nursingitem,read).
:- up(anesdoc1,carpat2caritem,addnote).
:- up(oncagent1,carpat2caritem,additem).
:- up(cardoc1,oncpat2nursingitem,read).
:- up(carnurse2,carpat1noteitem,read).
:- up(oncagent2,oncpat2hr,additem).
:- up(oncdoc2,oncpat2noteitem,additem).
:- up(doc2,carpat2caritem,additem).
:- up(caragent2,carpat1nursingitem,additem).
:- up(cardoc2,carpat1hr,additem).
:- up(carnurse2,oncpat2hr,additem).
:- up(oncdoc1,oncpat1noteitem,addnote).
:- up(cardoc2,oncpat2nursingitem,additem).
:- up(carnurse2,oncpat2hr,addnote).
:- up(anesdoc1,carpat2noteitem,read).
:- up(doc2,oncpat2oncitem,read).
:- up(oncdoc4,carpat2nursingitem,additem).
:- up(oncagent2,carpat2caritem,additem).
:- up(oncdoc1,carpat2noteitem,additem).
:- up(cardoc2,carpat2nursingitem,additem).
:- up(carpat1,carpat2nursingitem,read).
:- up(caragent1,oncpat2noteitem,addnote).
:- up(oncpat2,carpat2nursingitem,read).
:- up(oncdoc3,carpat1hr,read).
:- up(carpat2,oncpat2oncitem,read).
:- up(oncdoc4,oncpat1noteitem,read).
:- up(oncpat1,oncpat1nursingitem,addnote).
:- up(cardoc1,carpat2caritem,addnote).
:- up(oncagent1,carpat1hr,read).
:- up(doc2,oncpat2noteitem,additem).
:- up(cardoc1,oncpat1nursingitem,addnote).
:- up(carpat1,oncpat1oncitem,additem).
:- up(oncpat1,oncpat2oncitem,additem).
:- up(doc1,carpat2noteitem,read).
:- up(oncdoc3,oncpat1nursingitem,additem).
up(caragent1,carpat2hr,addnote).
:- up(oncpat1,carpat1nursingitem,addnote).
:- up(doc1,oncpat1noteitem,read).
:- up(doc1,carpat2noteitem,additem).
:- up(doc2,carpat1hr,additem).
:- up(oncdoc4,carpat1caritem,read).
:- up(oncagent1,oncpat1nursingitem,addnote).
:- up(oncpat2,oncpat2hr,read).
:- up(oncagent2,carpat2nursingitem,additem).
:- up(oncagent2,oncpat1nursingitem,addnote).
:- up(carpat1,oncpat1nursingitem,additem).
:- up(caragent1,carpat2caritem,addnote).
:- up(carpat2,carpat1nursingitem,additem).
:- up(oncdoc4,carpat2caritem,read).
:- up(oncdoc4,carpat2noteitem,additem).
:- up(caragent1,oncpat1noteitem,additem).
:- up(doc1,oncpat2hr,additem).
:- up(carnurse1,oncpat2nursingitem,addnote).
:- up(oncagent2,carpat1nursingitem,additem).
:- up(oncpat2,carpat2noteitem,read).
:- up(oncagent2,oncpat2noteitem,addnote).
:- up(anesdoc1,carpat1nursingitem,read).
:- up(carnurse1,carpat1caritem,additem).
:- up(caragent2,carpat2nursingitem,read).
up(oncpat2,oncpat2noteitem,read).
:- up(carpat2,oncpat1nursingitem,additem).
:- up(oncdoc2,carpat1hr,addnote).
:- up(oncdoc1,carpat1noteitem,addnote).
:- up(caragent2,oncpat2hr,additem).
:- up(oncpat1,carpat2noteitem,additem).
:- up(carpat1,carpat1noteitem,additem).
:- up(caragent1,oncpat1oncitem,read).
:- up(caragent2,oncpat2noteitem,addnote).
:- up(caragent1,carpat1noteitem,addnote).
:- up(oncdoc3,oncpat1hr,read).
:- up(oncdoc1,carpat2caritem,read).
:- up(oncpat1,oncpat2nursingitem,addnote).
:- up(carnurse1,carpat2noteitem,read).
:- up(oncagent1,oncpat2hr,read).
:- up(carpat2,oncpat2hr,addnote).
:- up(caragent2,carpat1noteitem,addnote).
:- up(cardoc2,carpat1caritem,addnote).
:- up(carnurse2,carpat1hr,addnote).
:- up(anesdoc1,oncpat2oncitem,read).
:- up(oncagent2,oncpat1hr,additem).
:- up(oncdoc3,oncpat2oncitem,addnote).
:- up(oncpat2,carpat1noteitem,read).
:- up(oncdoc4,carpat2hr,read).
:- up(caragent1,oncpat2noteitem,read).
:- up(oncnurse2,carpat1nursingitem,additem).
:- up(caragent1,carpat1noteitem,read).
:- up(cardoc2,carpat2nursingitem,addnote).
:- up(oncpat1,carpat2noteitem,read).
:- up(oncdoc1,oncpat2oncitem,additem).
:- up(oncdoc4,oncpat1nursingitem,addnote).
:- up(oncagent1,carpat2nursingitem,additem).
:- up(oncdoc4,oncpat2nursingitem,additem).
:- up(anesdoc1,oncpat1hr,read).
up(oncdoc1,oncpat1oncitem,read).
:- up(doc2,oncpat2noteitem,addnote).
:- up(oncagent1,oncpat1oncitem,addnote).
:- up(oncpat1,oncpat2hr,additem).
:- up(carnurse1,carpat2nursingitem,additem).
:- up(oncdoc3,oncpat1oncitem,addnote).
:- up(oncpat1,oncpat2oncitem,read).
:- up(oncdoc4,carpat1hr,additem).
:- up(oncagent2,carpat1nursingitem,addnote).
:- up(oncdoc2,oncpat2hr,read).
:- up(cardoc2,carpat2hr,read).
:- up(doc1,oncpat1hr,addnote).
:- up(oncpat1,carpat2nursingitem,additem).
:- up(oncdoc2,carpat1caritem,additem).
:- up(oncnurse2,carpat2caritem,additem).
:- up(cardoc2,oncpat2noteitem,additem).
:- up(cardoc2,carpat2noteitem,addnote).
:- up(oncpat2,carpat1hr,read).
:- up(oncdoc2,carpat2hr,additem).
:- up(oncdoc3,oncpat2nursingitem,additem).
:- up(anesdoc1,carpat1hr,addnote).
:- up(cardoc2,oncpat1noteitem,additem).
:- up(oncdoc3,carpat2noteitem,additem).
:- up(doc2,oncpat1oncitem,additem).
:- up(caragent1,oncpat2nursingitem,additem).
:- up(oncnurse2,oncpat2oncitem,additem).
:- up(caragent2,carpat1noteitem,additem).
:- up(doc1,oncpat2hr,read).
:- up(caragent2,oncpat2noteitem,read).
:- up(caragent1,oncpat1noteitem,addnote).
:- up(oncagent2,oncpat1oncitem,read).
:- up(oncpat2,oncpat1nursingitem,addnote).
:- up(oncdoc1,carpat1caritem,additem).
:- up(doc1,carpat2noteitem,addnote).
:- up(caragent2,carpat1nursingitem,addnote).
:- up(anesdoc1,carpat2hr,read).
:- up(carnurse1,oncpat1hr,addnote).
:- up(oncdoc1,carpat2noteitem,read).
:- up(doc1,oncpat2noteitem,addnote).
:- up(carpat1,oncpat2oncitem,read).
:- up(cardoc2,carpat1caritem,additem).
:- up(doc1,carpat1noteitem,additem).
:- up(oncdoc2,carpat2noteitem,read).
:- up(doc2,carpat2caritem,addnote).
:- up(caragent2,carpat2noteitem,additem).
:- up(oncagent2,carpat1noteitem,additem).
:- up(cardoc1,oncpat1hr,additem).
:- up(oncpat2,carpat1hr,addnote).
:- up(cardoc1,oncpat1oncitem,read).
:- up(caragent2,oncpat2oncitem,read).
:- up(anesdoc1,carpat1noteitem,additem).
up(carnurse2,carpat2hr,additem).
:- up(oncnurse2,carpat1noteitem,read).
:- up(oncnurse1,oncpat1oncitem,additem).
:- up(doc2,oncpat2nursingitem,addnote).
:- up(oncagent2,carpat1nursingitem,read).
:- up(oncdoc2,oncpat1noteitem,additem).
:- up(doc1,oncpat2noteitem,additem).
:- up(caragent1,oncpat2hr,addnote).
:- up(carnurse1,oncpat2noteitem,additem).
:- up(doc2,oncpat2nursingitem,read).
:- up(carnurse1,carpat1caritem,read).
up(anesdoc1,oncpat1hr,additem).
:- up(oncdoc4,oncpat1hr,read).
:- up(cardoc2,oncpat2hr,additem).
:- up(oncnurse2,oncpat1oncitem,additem).
:- up(oncnurse2,oncpat1oncitem,addnote).
:- up(cardoc1,oncpat1noteitem,addnote).
:- up(oncdoc1,oncpat2hr,read).
:- up(carpat2,oncpat2oncitem,addnote).
:- up(oncpat2,oncpat2noteitem,addnote).
:- up(doc2,oncpat1oncitem,read).
:- up(oncdoc4,oncpat2noteitem,additem).
:- up(carpat1,carpat1caritem,read).
:- up(oncdoc3,carpat2noteitem,addnote).
up(caragent2,carpat2hr,addnote).
:- up(oncdoc1,oncpat1oncitem,additem).
:- up(oncdoc1,carpat2caritem,addnote).
:- up(carpat1,oncpat2nursingitem,additem).
:- up(oncpat2,oncpat2oncitem,additem).
:- up(doc1,oncpat1hr,additem).
:- up(oncagent1,oncpat1noteitem,additem).
:- up(carnurse2,carpat1nursingitem,addnote).
:- up(doc2,oncpat2noteitem,read).
:- up(oncpat1,carpat1hr,addnote).
:- up(oncnurse2,carpat2caritem,read).
:- up(cardoc2,oncpat2noteitem,read).
:- up(anesdoc1,oncpat2noteitem,addnote).
:- up(oncdoc4,oncpat2nursingitem,read).
:- up(anesdoc1,oncpat1nursingitem,read).
:- up(oncpat2,carpat1hr,additem).
:- up(oncagent1,carpat1noteitem,read).
:- up(doc1,oncpat2oncitem,additem).
:- up(cardoc2,oncpat1nursingitem,additem).
up(carpat1,carpat1noteitem,read).
:- up(oncnurse2,oncpat1noteitem,read).
:- up(oncagent1,carpat1caritem,read).
:- up(caragent1,carpat1nursingitem,addnote).
:- up(carnurse2,oncpat1hr,additem).
:- up(oncnurse2,carpat1noteitem,addnote).
:- up(carpat1,carpat2noteitem,additem).
:- up(carnurse1,oncpat2oncitem,read).
up(carnurse2,carpat2nursingitem,read).
:- up(oncnurse1,carpat2caritem,read).
:- up(caragent1,oncpat2hr,read).
:- up(oncdoc2,oncpat1nursingitem,addnote).
:- up(cardoc1,carpat2nursingitem,read).
:- up(oncdoc3,carpat1noteitem,additem).
:- up(oncagent2,oncpat2noteitem,additem).
:- up(oncnurse2,carpat2noteitem,additem).
:- up(caragent2,oncpat2nursingitem,additem).
:- up(oncpat1,carpat1nursingitem,additem).
up(oncdoc4,oncpat2hr,additem).
:- up(carpat2,carpat2caritem,additem).
:- up(oncdoc3,carpat1caritem,addnote).
:- up(doc1,oncpat2oncitem,addnote).
:- up(caragent2,carpat1noteitem,read).
:- up(oncagent2,oncpat2nursingitem,read).
:- up(oncagent1,carpat2nursingitem,read).
:- up(oncdoc2,carpat2caritem,additem).
:- up(doc1,oncpat1nursingitem,additem).
:- up(caragent1,carpat2nursingitem,read).
:- up(doc2,carpat1nursingitem,additem).
:- up(oncagent2,oncpat2hr,read).
:- up(oncdoc4,oncpat2noteitem,read).
:- up(doc1,oncpat1oncitem,read).
:- up(carnurse2,carpat1caritem,additem).
:- up(caragent1,carpat2caritem,additem).
:- up(oncdoc4,carpat2noteitem,addnote).
:- up(caragent1,carpat1nursingitem,read).
:- up(oncdoc1,oncpat1oncitem,addnote).
:- up(doc2,oncpat1hr,additem).
:- up(oncnurse1,oncpat2noteitem,addnote).
:- up(carnurse1,carpat2caritem,additem).
:- up(carpat1,carpat1caritem,addnote).
:- up(oncpat2,oncpat2nursingitem,addnote).
:- up(carnurse2,oncpat1oncitem,read).
:- up(doc2,oncpat1nursingitem,read).
:- up(cardoc2,oncpat1hr,addnote).
:- up(carpat1,carpat1caritem,additem).
up(oncnurse2,oncpat2nursingitem,read).
:- up(caragent1,oncpat1oncitem,additem).
:- up(caragent1,carpat2noteitem,addnote).
:- up(doc1,oncpat2nursingitem,additem).
:- up(oncnurse2,oncpat2noteitem,addnote).
:- up(oncpat1,carpat1noteitem,read).
:- up(carpat1,oncpat1nursingitem,read).
:- up(cardoc1,oncpat1nursingitem,additem).
:- up(cardoc1,carpat2nursingitem,additem).
:- up(carnurse1,oncpat1oncitem,read).
:- up(oncpat1,oncpat1oncitem,addnote).
:- up(doc2,oncpat1nursingitem,additem).
:- up(oncagent2,carpat1caritem,addnote).
:- up(oncagent1,carpat2nursingitem,addnote).
:- up(oncdoc1,carpat2hr,additem).
:- up(oncpat2,carpat2caritem,additem).
:- up(oncagent1,carpat2noteitem,additem).
:- up(cardoc1,carpat2hr,read).
:- up(oncdoc1,oncpat2oncitem,addnote).
:- up(carpat1,carpat1nursingitem,addnote).
:- up(oncpat1,oncpat2noteitem,read).
:- up(cardoc1,carpat1nursingitem,read).
:- up(caragent2,carpat2nursingitem,additem).
:- up(oncnurse2,carpat2hr,read).
:- up(doc2,oncpat1hr,addnote).
:- up(cardoc2,oncpat1oncitem,read).
:- up(cardoc2,carpat1nursingitem,addnote).
:- up(oncdoc1,oncpat1noteitem,read).
:- up(carpat2,oncpat1hr,read).
:- up(carnurse1,carpat2hr,read).
:- up(oncdoc3,carpat1caritem,read).
:- up(anesdoc1,carpat2hr,addnote).
:- up(anesdoc1,oncpat2nursingitem,additem).
:- up(oncpat2,oncpat1oncitem,additem).
:- up(oncnurse2,carpat1nursingitem,read).
:- up(oncnurse1,oncpat2nursingitem,addnote).
:- up(caragent1,carpat1hr,addnote).
:- up(doc2,carpat2hr,additem).
:- up(doc1,carpat1hr,addnote).
:- up(oncdoc2,oncpat1noteitem,addnote).
:- up(oncagent2,carpat1caritem,additem).
:- up(oncpat2,oncpat2hr,additem).
:- up(oncdoc2,oncpat2oncitem,read).
:- up(oncpat1,carpat2noteitem,addnote).
:- up(oncdoc2,oncpat2hr,additem).
:- up(oncdoc3,carpat1hr,additem).
:- up(oncdoc4,carpat1noteitem,addnote).
:- up(oncagent2,oncpat1nursingitem,read).
:- up(oncdoc3,carpat2hr,addnote).
:- up(oncpat2,carpat1caritem,addnote).
:- up(doc1,carpat1hr,read).
:- up(oncnurse2,carpat2nursingitem,addnote).
:- up(oncpat2,carpat1noteitem,addnote).
:- up(oncdoc4,oncpat1hr,additem).
:- up(caragent2,oncpat1nursingitem,read).
up(oncdoc4,oncpat2oncitem,read).
:- up(oncnurse2,carpat2hr,additem).
:- up(oncdoc4,carpat1noteitem,read).
:- up(oncpat1,oncpat2nursingitem,additem).
:- up(carpat2,carpat2caritem,addnote).
:- up(caragent2,carpat1hr,additem).
:- up(carnurse1,oncpat1oncitem,additem).
:- up(doc1,oncpat2nursingitem,read).
:- up(carnurse2,carpat1noteitem,additem).
:- up(doc1,oncpat2noteitem,read).
:- up(doc2,carpat1nursingitem,addnote).
:- up(cardoc1,oncpat1hr,addnote).
:- up(oncpat2,carpat2hr,additem).
:- up(carnurse2,carpat2caritem,addnote).
:- up(cardoc2,carpat2nursingitem,read).
:- up(cardoc2,carpat1noteitem,addnote).
:- up(oncnurse1,oncpat2hr,read).
:- up(oncagent2,oncpat2oncitem,additem).
:- up(doc2,carpat1caritem,addnote).
:- up(oncdoc3,oncpat1oncitem,additem).
:- up(carnurse1,carpat1noteitem,addnote).
:- up(caragent2,carpat1caritem,additem).
:- up(caragent2,carpat2caritem,addnote).
:- up(anesdoc1,carpat2nursingitem,addnote).
:- up(carnurse2,oncpat1noteitem,addnote).
:- up(oncnurse1,oncpat1noteitem,addnote).
:- up(cardoc1,oncpat2noteitem,addnote).
:- up(oncdoc3,oncpat1noteitem,additem).
:- up(caragent1,oncpat1nursingitem,read).
:- up(oncdoc3,carpat1caritem,additem).
:- up(anesdoc1,carpat1noteitem,addnote).
:- up(oncdoc1,carpat2noteitem,addnote).
:- up(oncagent2,oncpat1noteitem,read).
up(carnurse1,carpat2nursingitem,read).
:- up(oncdoc3,oncpat2noteitem,read).
:- up(oncdoc3,oncpat2nursingitem,read).
:- up(carnurse1,carpat2nursingitem,addnote).
:- up(carpat2,oncpat2hr,read).
:- up(oncpat2,oncpat1hr,read).
:- up(doc2,oncpat2hr,read).
:- up(caragent1,carpat2nursingitem,additem).
:- up(anesdoc1,oncpat2noteitem,read).
:- up(oncagent1,carpat1noteitem,addnote).
:- up(cardoc1,oncpat2nursingitem,additem).
:- up(carnurse1,carpat1noteitem,read).
up(caragent1,carpat2noteitem,read).
:- up(oncdoc2,carpat2caritem,addnote).
:- up(oncnurse2,oncpat1oncitem,read).
:- up(oncnurse2,carpat1nursingitem,addnote).
:- up(oncnurse2,carpat1hr,additem).
:- up(cardoc2,carpat1hr,read).
:- up(oncpat1,carpat2nursingitem,read).
:- up(oncdoc3,carpat1noteitem,addnote).
:- up(oncagent1,oncpat1noteitem,addnote).
:- up(carpat1,oncpat2hr,additem).
:- up(oncdoc4,carpat1caritem,additem).
:- up(oncnurse1,carpat2noteitem,additem).
:- up(cardoc1,oncpat1oncitem,addnote).
:- up(oncdoc4,oncpat2hr,read).
:- up(doc1,carpat1nursingitem,additem).
:- up(oncdoc2,carpat2caritem,read).
up(carpat2,carpat2hr,addnote).
:- up(oncdoc3,carpat2hr,read).
:- up(carnurse1,oncpat2noteitem,read).
:- up(oncnurse2,oncpat2hr,addnote).
:- up(doc1,carpat2nursingitem,additem).
:- up(anesdoc1,oncpat2nursingitem,read).
:- up(oncdoc2,oncpat1oncitem,addnote).
:- up(carpat2,oncpat1oncitem,read).
:- up(oncdoc2,carpat1caritem,read).
:- up(oncdoc1,oncpat2noteitem,addnote).
:- up(anesdoc1,carpat1hr,read).
:- up(oncdoc4,oncpat2oncitem,additem).
:- up(oncagent1,oncpat2nursingitem,additem).
:- up(oncagent1,oncpat2oncitem,read).
:- up(oncnurse1,carpat1nursingitem,read).
:- up(caragent2,carpat2nursingitem,addnote).
:- up(oncagent1,carpat1hr,additem).
:- up(oncdoc2,oncpat2nursingitem,addnote).
:- up(oncpat1,carpat2hr,read).
:- up(caragent2,oncpat2nursingitem,read).
:- up(oncagent2,carpat1noteitem,addnote).
:- up(caragent2,carpat1hr,read).
:- up(carnurse2,carpat2nursingitem,addnote).
:- up(anesdoc1,carpat2caritem,additem).
:- up(carpat2,oncpat1hr,addnote).
:- up(oncdoc4,carpat1caritem,addnote).
:- up(oncnurse1,oncpat2noteitem,additem).
:- up(caragent1,carpat2noteitem,additem).
:- up(oncagent1,oncpat1hr,addnote).
:- up(oncdoc1,carpat2nursingitem,addnote).
:- up(oncnurse1,carpat1caritem,additem).
up(carnurse2,carpat1hr,additem).
up(oncpat2,oncpat2hr,addnote).
up(cardoc2,carpat1caritem,read).
:- up(oncnurse1,oncpat1oncitem,read).
:- up(oncnurse1,carpat1noteitem,read).
:- up(oncdoc1,oncpat2nursingitem,read).
:- up(carpat2,oncpat2nursingitem,addnote).
:- up(oncdoc2,carpat2nursingitem,additem).
:- up(oncnurse2,carpat2caritem,addnote).
:- up(carpat2,oncpat2noteitem,read).
:- up(oncdoc4,carpat1nursingitem,read).
:- up(oncpat1,carpat1hr,additem).
:- up(oncdoc2,carpat2noteitem,addnote).
:- up(oncdoc2,carpat2hr,read).
:- up(oncnurse2,oncpat1noteitem,additem).
:- up(oncagent2,carpat1noteitem,read).
:- up(carnurse2,oncpat2oncitem,addnote).
:- up(cardoc2,oncpat2oncitem,additem).
:- up(oncdoc1,oncpat2hr,addnote).
:- up(cardoc1,oncpat1nursingitem,read).
:- up(oncdoc1,oncpat1nursingitem,read).
:- up(oncagent1,oncpat2nursingitem,read).
:- up(oncdoc3,oncpat2oncitem,additem).
:- up(carnurse2,oncpat2nursingitem,read).
:- up(oncnurse1,oncpat1noteitem,additem).
:- up(carnurse2,carpat2noteitem,addnote).
:- up(oncagent2,oncpat2oncitem,addnote).
:- up(oncdoc3,carpat2caritem,additem).
:- up(oncdoc3,carpat1nursingitem,additem).
:- up(carnurse1,carpat1caritem,addnote).
:- up(oncdoc1,carpat1noteitem,additem).
:- up(oncdoc4,oncpat1nursingitem,read).
:- up(cardoc2,carpat1noteitem,read).
up(oncagent2,oncpat2hr,addnote).
:- up(doc2,carpat2hr,addnote).
:- up(oncpat1,carpat2hr,addnote).
:- up(oncnurse2,carpat2nursingitem,read).
:- up(anesdoc1,carpat1caritem,addnote).
:- up(carpat2,oncpat2hr,additem).
:- up(carnurse1,carpat1noteitem,additem).
:- up(doc2,carpat1noteitem,additem).
:- up(doc2,carpat1hr,addnote).
:- up(cardoc1,oncpat2noteitem,read).
:- up(doc1,oncpat1noteitem,additem).
:- up(cardoc1,oncpat1noteitem,additem).
:- up(doc1,oncpat1nursingitem,read).
:- up(oncnurse1,oncpat2oncitem,read).
:- up(carnurse1,carpat2noteitem,addnote).
:- up(oncnurse1,oncpat2nursingitem,additem).
:- up(oncpat1,oncpat1hr,read).
:- up(caragent2,oncpat1hr,additem).
:- up(carnurse1,oncpat1nursingitem,read).
:- up(carnurse2,oncpat1noteitem,read).
:- up(oncdoc1,carpat2hr,addnote).
:- up(cardoc1,carpat2nursingitem,addnote).
:- up(oncpat1,carpat1noteitem,addnote).
:- up(oncdoc1,carpat1hr,read).
:- up(cardoc2,carpat2caritem,addnote).
:- up(oncnurse1,oncpat1hr,addnote).
:- up(carpat2,carpat1nursingitem,read).
:- up(caragent2,carpat2hr,additem).
:- up(carpat2,carpat1noteitem,addnote).
up(oncdoc1,oncpat2oncitem,read).
up(carnurse1,carpat1nursingitem,read).
:- up(cardoc2,oncpat1oncitem,additem).
:- up(doc2,oncpat2nursingitem,additem).
:- up(oncnurse1,carpat2hr,addnote).
up(doc2,carpat2caritem,read).
:- up(anesdoc1,oncpat1noteitem,addnote).
:- up(oncpat1,carpat2caritem,additem).
:- up(carpat2,oncpat1oncitem,addnote).
:- up(carpat2,oncpat1noteitem,additem).
:- up(oncdoc1,carpat2hr,read).
:- up(caragent1,oncpat2nursingitem,read).
:- up(doc1,carpat1nursingitem,read).
:- up(oncpat2,oncpat2noteitem,additem).
:- up(carpat2,carpat1hr,read).
:- up(cardoc2,carpat1noteitem,additem).
:- up(caragent2,carpat2caritem,additem).
:- up(oncagent2,carpat1hr,additem).
:- up(anesdoc1,carpat1nursingitem,additem).
:- up(caragent2,carpat2caritem,read).
:- up(carpat2,oncpat1hr,additem).
:- up(doc1,carpat1nursingitem,addnote).
:- up(cardoc1,carpat1noteitem,read).
:- up(oncnurse1,carpat1noteitem,additem).
:- up(cardoc1,oncpat1oncitem,additem).
:- up(carpat1,oncpat2noteitem,addnote).
:- up(oncdoc1,oncpat1noteitem,additem).
:- up(carnurse2,carpat2caritem,additem).
:- up(doc1,carpat1caritem,addnote).
:- up(oncdoc2,carpat2hr,addnote).
:- up(oncagent2,carpat2hr,read).
:- up(oncnurse1,carpat1hr,additem).
:- up(caragent1,carpat1hr,read).
:- up(doc2,oncpat2oncitem,additem).
:- up(caragent1,oncpat2hr,additem).
:- up(cardoc1,carpat2noteitem,read).
:- up(cardoc2,carpat2hr,addnote).
:- up(oncnurse1,oncpat1nursingitem,additem).
:- up(oncnurse1,oncpat1nursingitem,addnote).
:- up(oncnurse2,oncpat1hr,addnote).
up(oncagent1,oncpat2noteitem,read).
:- up(oncpat1,oncpat1oncitem,read).
:- up(caragent1,oncpat1nursingitem,additem).
:- up(oncdoc2,oncpat2nursingitem,additem).
:- up(cardoc2,oncpat2hr,addnote).
:- up(caragent2,oncpat1noteitem,additem).
:- up(carnurse1,oncpat1noteitem,addnote).
:- up(carnurse1,oncpat2hr,read).
:- up(carpat2,oncpat1noteitem,addnote).
:- up(carpat2,oncpat2oncitem,additem).
:- up(oncdoc1,carpat1nursingitem,addnote).
:- up(oncnurse1,carpat2caritem,additem).
