%Settings
:- set(nodes,0)?
:- set(h,100)?
%Mode Declarations
:- modeh(1,up(+user,+resource,#operation))?
:- modeb(1,superset(+attribValSet,+attribValSet))?
:- modeb(1,element(+attribValAtomic,+attribValSet))?
:- modeb(1,element(+attribValAtomic,#attribValSet))?
:- modeb(1,element(#attribValAtomic,+attribValSet))?
%Types
attribValSet([]).
attribValSet([V|Vs]) :- attribValAtomic(V), attribValSet(Vs).
%Background Knowledge
superset(Y,[A|X]) :- element(A,Y), superset(Y,X).
superset(Y,[]).
% Positive and negative examples
