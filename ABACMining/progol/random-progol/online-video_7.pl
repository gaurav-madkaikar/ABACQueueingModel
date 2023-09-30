%Settings
:- set(nodes,249)?
:- set(h,100)?
%Mode Declarations
:- modeh(1,up(+user,+resource,#operation))?
:- modeb(1,uidU(+user,#uidUType))?
:- modeb(1,memberTypeU(+user,#memberTypeUType))?
:- modeb(1,ageGroupU(+user,#ageGroupUType))?
:- modeb(1,ridR(+resource,#ridRType))?
:- modeb(1,ratingR(+resource,#ratingRType))?
:- modeb(1,videoTypeR(+resource,#videoTypeRType))?
:- modeb(1,uidU_equals_ridR(+user,+resource))?
:- modeb(1,uidU_equals_ratingR(+user,+resource))?
:- modeb(1,uidU_equals_videoTypeR(+user,+resource))?
:- modeb(1,memberTypeU_equals_ridR(+user,+resource))?
:- modeb(1,memberTypeU_equals_ratingR(+user,+resource))?
:- modeb(1,memberTypeU_equals_videoTypeR(+user,+resource))?
:- modeb(1,ageGroupU_equals_ridR(+user,+resource))?
:- modeb(1,ageGroupU_equals_ratingR(+user,+resource))?
:- modeb(1,ageGroupU_equals_videoTypeR(+user,+resource))?
:- modeb(1,superset(+attribValSet,+attribValSet))?
:- modeb(1,element(+attribValAtomic,+attribValSet))?
:- modeb(1,element(+attribValAtomic,#attribValSet))?
:- modeb(1,element(#attribValAtomic,+attribValSet))?
%Types
user(teen2p).
user(child2p).
user(teen2r).
user(child2r).
user(teen1r).
user(teen1p).
user(child1r).
user(child1p).
user(adult1r).
user(adult2p).
user(adult1p).
user(adult2r).
resource(brave).
resource(my_cousin_vinny).
resource(flight).
resource(happy_feet).
resource(brave_heart).
resource(cars).
resource(star_wars).
resource(toy_story).
resource(life_of_pi).
resource(jaws).
resource(happy_feet_2).
resource(the_lion_king).
resource(cloud_atlas).
operation(view).
attribValAtomic(brave).
attribValAtomic(my_cousin_vinny).
attribValAtomic(happy_feet).
attribValAtomic(old).
attribValAtomic(g).
attribValAtomic(teen1r).
attribValAtomic(teen1p).
attribValAtomic(jaws).
attribValAtomic(happy_feet_2).
attribValAtomic(child).
attribValAtomic(toy_story).
attribValAtomic(cars).
attribValAtomic(star_wars).
attribValAtomic(r).
attribValAtomic(child1r).
attribValAtomic(child1p).
attribValAtomic(adult2p).
attribValAtomic(adult2r).
attribValAtomic(teen2p).
attribValAtomic(teen2r).
attribValAtomic(regular).
attribValAtomic(teen).
attribValAtomic(premium).
attribValAtomic(life_of_pi).
attribValAtomic(adult).
attribValAtomic(the_lion_king).
attribValAtomic(cloud_atlas).
attribValAtomic(flight).
attribValAtomic(brave_heart).
attribValAtomic(new).
attribValAtomic(child2p).
attribValAtomic(child2r).
attribValAtomic(pg).
attribValAtomic(adult1r).
attribValAtomic(adult1p).
attribValSet([]).
attribValSet([V|Vs]) :- attribValAtomic(V), attribValSet(Vs).
uidUType(teen2p).
uidUType(child2p).
uidUType(teen2r).
uidUType(child2r).
uidUType(teen1r).
uidUType(teen1p).
uidUType(child1r).
uidUType(child1p).
uidUType(adult1r).
uidUType(adult2p).
uidUType(adult1p).
uidUType(adult2r).
memberTypeUType(regular).
memberTypeUType(premium).
ageGroupUType(child).
ageGroupUType(teen).
ageGroupUType(adult).
ridUType(brave).
ridUType(my_cousin_vinny).
ridUType(flight).
ridUType(happy_feet).
ridUType(brave_heart).
ridUType(cars).
ridUType(star_wars).
ridUType(toy_story).
ridUType(life_of_pi).
ridUType(jaws).
ridUType(happy_feet_2).
ridUType(the_lion_king).
ridUType(cloud_atlas).
ratingUType(g).
ratingUType(r).
ratingUType(pg).
videoTypeUType(new).
videoTypeUType(old).
%Background Knowledge
uidU(teen2p,teen2p).
memberTypeU(teen2p,premium).
ageGroupU(teen2p,teen).
uidU(child2p,child2p).
memberTypeU(child2p,premium).
ageGroupU(child2p,child).
uidU(teen2r,teen2r).
memberTypeU(teen2r,regular).
ageGroupU(teen2r,teen).
uidU(child2r,child2r).
memberTypeU(child2r,regular).
ageGroupU(child2r,child).
uidU(teen1r,teen1r).
memberTypeU(teen1r,regular).
ageGroupU(teen1r,teen).
uidU(teen1p,teen1p).
memberTypeU(teen1p,premium).
ageGroupU(teen1p,teen).
uidU(child1r,child1r).
memberTypeU(child1r,regular).
ageGroupU(child1r,child).
uidU(child1p,child1p).
memberTypeU(child1p,premium).
ageGroupU(child1p,child).
uidU(adult1r,adult1r).
memberTypeU(adult1r,regular).
ageGroupU(adult1r,adult).
uidU(adult2p,adult2p).
memberTypeU(adult2p,premium).
ageGroupU(adult2p,adult).
uidU(adult1p,adult1p).
memberTypeU(adult1p,premium).
ageGroupU(adult1p,adult).
uidU(adult2r,adult2r).
memberTypeU(adult2r,regular).
ageGroupU(adult2r,adult).
ridR(brave,brave).
ratingR(brave,pg).
videoTypeR(brave,new).
ridR(my_cousin_vinny,my_cousin_vinny).
ratingR(my_cousin_vinny,r).
videoTypeR(my_cousin_vinny,old).
ridR(flight,flight).
ratingR(flight,r).
videoTypeR(flight,new).
ridR(happy_feet,happy_feet).
ratingR(happy_feet,g).
videoTypeR(happy_feet,new).
ridR(brave_heart,brave_heart).
ratingR(brave_heart,r).
videoTypeR(brave_heart,old).
ridR(cars,cars).
ratingR(cars,g).
videoTypeR(cars,new).
ridR(star_wars,star_wars).
ratingR(star_wars,pg).
videoTypeR(star_wars,old).
ridR(toy_story,toy_story).
ratingR(toy_story,g).
videoTypeR(toy_story,old).
ridR(life_of_pi,life_of_pi).
ratingR(life_of_pi,pg).
videoTypeR(life_of_pi,new).
ridR(jaws,jaws).
ratingR(jaws,pg).
videoTypeR(jaws,old).
ridR(happy_feet_2,happy_feet_2).
ratingR(happy_feet_2,g).
videoTypeR(happy_feet_2,new).
ridR(the_lion_king,the_lion_king).
ratingR(the_lion_king,g).
videoTypeR(the_lion_king,old).
ridR(cloud_atlas,cloud_atlas).
ratingR(cloud_atlas,r).
videoTypeR(cloud_atlas,new).
uidU_equals_ridR(U,R) :-uidU(U,X),ridR(R,X).
uidU_equals_ratingR(U,R) :-uidU(U,X),ratingR(R,X).
uidU_equals_videoTypeR(U,R) :-uidU(U,X),videoTypeR(R,X).
memberTypeU_equals_ridR(U,R) :-memberTypeU(U,X),ridR(R,X).
memberTypeU_equals_ratingR(U,R) :-memberTypeU(U,X),ratingR(R,X).
memberTypeU_equals_videoTypeR(U,R) :-memberTypeU(U,X),videoTypeR(R,X).
ageGroupU_equals_ridR(U,R) :-ageGroupU(U,X),ridR(R,X).
ageGroupU_equals_ratingR(U,R) :-ageGroupU(U,X),ratingR(R,X).
ageGroupU_equals_videoTypeR(U,R) :-ageGroupU(U,X),videoTypeR(R,X).
superset(Y,[A|X]) :- element(A,Y), superset(Y,X).
superset(Y,[]).
% Positive and negative examples
up(teen1p,toy_story,view).
:- up(adult1r,cloud_atlas,view).
:- up(child1p,life_of_pi,view).
:- up(teen2r,brave,view).
up(teen2p,cars,view).
up(teen2p,the_lion_king,view).
:- up(child2p,my_cousin_vinny,view).
up(child2r,toy_story,view).
:- up(child2r,flight,view).
:- up(child1r,life_of_pi,view).
:- up(child2r,life_of_pi,view).
:- up(child1p,brave,view).
:- up(child1r,my_cousin_vinny,view).
up(teen1p,brave,view).
up(teen1r,the_lion_king,view).
:- up(child2r,cloud_atlas,view).
up(adult1p,jaws,view).
up(child2p,the_lion_king,view).
up(adult1p,happy_feet,view).
:- up(child2r,happy_feet_2,view).
up(adult2p,brave,view).
up(adult1p,cars,view).
up(teen2p,jaws,view).
:- up(adult2r,flight,view).
up(teen1r,toy_story,view).
up(adult1r,jaws,view).
:- up(teen1r,my_cousin_vinny,view).
:- up(adult1r,happy_feet,view).
:- up(child2p,star_wars,view).
:- up(adult1r,cars,view).
:- up(teen1r,flight,view).
up(teen1p,happy_feet_2,view).
up(adult2p,brave_heart,view).
:- up(teen2p,cloud_atlas,view).
up(adult2p,jaws,view).
up(adult2p,happy_feet_2,view).
up(adult2p,toy_story,view).
:- up(adult2r,happy_feet_2,view).
:- up(teen1p,cloud_atlas,view).
up(child1p,happy_feet,view).
up(teen1r,jaws,view).
:- up(teen2r,cloud_atlas,view).
up(teen2p,toy_story,view).
:- up(child2r,my_cousin_vinny,view).
:- up(child1r,cloud_atlas,view).
up(teen2r,star_wars,view).
up(adult1r,star_wars,view).
:- up(child1r,cars,view).
:- up(child1r,happy_feet_2,view).
:- up(teen2r,happy_feet,view).
:- up(teen2r,flight,view).
up(adult2r,my_cousin_vinny,view).
up(adult2r,star_wars,view).
:- up(adult1r,happy_feet_2,view).
up(child1p,happy_feet_2,view).
up(teen2p,star_wars,view).
up(adult2p,the_lion_king,view).
:- up(child2p,flight,view).
up(adult1p,my_cousin_vinny,view).
up(adult2p,life_of_pi,view).
up(adult1r,brave_heart,view).
:- up(adult2r,life_of_pi,view).
:- up(child2p,cloud_atlas,view).
up(teen1r,star_wars,view).
:- up(teen2p,brave_heart,view).
:- up(teen2p,flight,view).
up(teen1p,cars,view).
:- up(child2r,jaws,view).
:- up(child2r,star_wars,view).
up(adult2p,flight,view).
:- up(teen2r,happy_feet_2,view).
up(adult2p,my_cousin_vinny,view).
:- up(adult2r,happy_feet,view).
:- up(teen1r,cloud_atlas,view).
:- up(child1r,jaws,view).
:- up(teen1p,brave_heart,view).
up(adult1p,brave_heart,view).
up(adult1r,the_lion_king,view).
:- up(adult1r,brave,view).
:- up(adult2r,cloud_atlas,view).
up(teen1p,life_of_pi,view).
up(child2r,the_lion_king,view).
up(adult2r,jaws,view).
:- up(teen2p,my_cousin_vinny,view).
up(teen1p,the_lion_king,view).
:- up(child1p,jaws,view).
:- up(child1r,brave_heart,view).
:- up(child2r,brave_heart,view).
up(teen1p,happy_feet,view).
up(adult1p,toy_story,view).
up(teen2p,life_of_pi,view).
up(adult2p,cloud_atlas,view).
:- up(child2p,brave_heart,view).
:- up(child2p,life_of_pi,view).
up(child1p,the_lion_king,view).
:- up(teen1p,my_cousin_vinny,view).
up(adult2r,brave_heart,view).
:- up(teen1r,brave_heart,view).
up(adult2p,happy_feet,view).
up(adult1p,cloud_atlas,view).
up(adult1p,life_of_pi,view).
up(child2p,toy_story,view).
:- up(child2r,cars,view).
:- up(child1p,star_wars,view).
up(adult1p,the_lion_king,view).
:- up(child1r,star_wars,view).
:- up(child1r,flight,view).
up(adult1r,toy_story,view).
:- up(child1p,brave_heart,view).
up(teen2p,brave,view).
up(child1p,cars,view).
:- up(child2p,jaws,view).
up(adult1r,my_cousin_vinny,view).
up(child1p,toy_story,view).
up(adult1p,flight,view).
up(adult2p,cars,view).
:- up(adult2r,cars,view).
:- up(teen1r,cars,view).
:- up(child2p,brave,view).
up(adult2r,the_lion_king,view).
up(adult2p,star_wars,view).
:- up(child1r,happy_feet,view).
:- up(teen1r,life_of_pi,view).
up(child2p,happy_feet_2,view).
:- up(child1p,cloud_atlas,view).
up(adult2r,toy_story,view).
:- up(teen1r,happy_feet,view).
up(child2p,cars,view).
up(teen1p,jaws,view).
:- up(child2r,happy_feet,view).
up(child1r,toy_story,view).
up(child2p,happy_feet,view).
:- up(child1p,my_cousin_vinny,view).
up(teen2r,toy_story,view).
:- up(child1r,brave,view).
:- up(teen1p,flight,view).
:- up(child2r,brave,view).
up(teen2r,the_lion_king,view).
:- up(adult2r,brave,view).
:- up(teen2r,brave_heart,view).
:- up(teen1r,happy_feet_2,view).
:- up(teen2r,my_cousin_vinny,view).
up(teen2p,happy_feet_2,view).
:- up(teen1r,brave,view).
up(child1r,the_lion_king,view).
up(teen2p,happy_feet,view).
:- up(adult1r,life_of_pi,view).
:- up(teen2r,life_of_pi,view).
:- up(adult1r,flight,view).
:- up(child1p,flight,view).
up(adult1p,star_wars,view).
up(adult1p,happy_feet_2,view).
up(teen2r,jaws,view).
:- up(teen2r,cars,view).
up(teen1p,star_wars,view).
up(adult1p,brave,view).
