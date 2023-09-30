%Settings
:- set(nodes,8512)?
:- set(h,100)?
%Mode Declarations
:- modeh(1,up(+user,+resource,#operation))?
:- modeb(1,uidU(+user,#uidUType))?
:- modeb(*,projectsU(+user,#projectsUType))?
:- modeb(*,expertiseU(+user,#expertiseUType))?
:- modeb(*,adminRolesU(+user,#adminRolesUType))?
:- modeb(1,departmentU(+user,#departmentUType))?
:- modeb(*,tasksU(+user,#tasksUType))?
:- modeb(*,projectsLedU(+user,#projectsLedUType))?
:- modeb(1,isEmployeeU(+user,#isEmployeeUType))?
:- modeb(1,projectR(+resource,#projectRType))?
:- modeb(1,expertiseR(+resource,#expertiseRType))?
:- modeb(1,ridR(+resource,#ridRType))?
:- modeb(1,departmentR(+resource,#departmentRType))?
:- modeb(1,proprietaryR(+resource,#proprietaryRType))?
:- modeb(1,typeR(+resource,#typeRType))?
:- modeb(1,projectsU_superset_expertiseR(+user,+resource))?
:- modeb(1,expertiseU_superset_expertiseR(+user,+resource))?
:- modeb(1,adminRolesU_superset_expertiseR(+user,+resource))?
:- modeb(1,tasksU_superset_expertiseR(+user,+resource))?
:- modeb(1,projectsLedU_superset_expertiseR(+user,+resource))?
:- modeb(1,projectsU_contains_projectR(+user,+resource))?
:- modeb(1,projectsU_contains_ridR(+user,+resource))?
:- modeb(1,projectsU_contains_departmentR(+user,+resource))?
:- modeb(1,projectsU_contains_proprietaryR(+user,+resource))?
:- modeb(1,projectsU_contains_typeR(+user,+resource))?
:- modeb(1,expertiseU_contains_projectR(+user,+resource))?
:- modeb(1,expertiseU_contains_ridR(+user,+resource))?
:- modeb(1,expertiseU_contains_departmentR(+user,+resource))?
:- modeb(1,expertiseU_contains_proprietaryR(+user,+resource))?
:- modeb(1,expertiseU_contains_typeR(+user,+resource))?
:- modeb(1,adminRolesU_contains_projectR(+user,+resource))?
:- modeb(1,adminRolesU_contains_ridR(+user,+resource))?
:- modeb(1,adminRolesU_contains_departmentR(+user,+resource))?
:- modeb(1,adminRolesU_contains_proprietaryR(+user,+resource))?
:- modeb(1,adminRolesU_contains_typeR(+user,+resource))?
:- modeb(1,tasksU_contains_projectR(+user,+resource))?
:- modeb(1,tasksU_contains_ridR(+user,+resource))?
:- modeb(1,tasksU_contains_departmentR(+user,+resource))?
:- modeb(1,tasksU_contains_proprietaryR(+user,+resource))?
:- modeb(1,tasksU_contains_typeR(+user,+resource))?
:- modeb(1,projectsLedU_contains_projectR(+user,+resource))?
:- modeb(1,projectsLedU_contains_ridR(+user,+resource))?
:- modeb(1,projectsLedU_contains_departmentR(+user,+resource))?
:- modeb(1,projectsLedU_contains_proprietaryR(+user,+resource))?
:- modeb(1,projectsLedU_contains_typeR(+user,+resource))?
:- modeb(1,uidU_equals_projectR(+user,+resource))?
:- modeb(1,uidU_equals_ridR(+user,+resource))?
:- modeb(1,uidU_equals_departmentR(+user,+resource))?
:- modeb(1,uidU_equals_proprietaryR(+user,+resource))?
:- modeb(1,uidU_equals_typeR(+user,+resource))?
:- modeb(1,departmentU_equals_projectR(+user,+resource))?
:- modeb(1,departmentU_equals_ridR(+user,+resource))?
:- modeb(1,departmentU_equals_departmentR(+user,+resource))?
:- modeb(1,departmentU_equals_proprietaryR(+user,+resource))?
:- modeb(1,departmentU_equals_typeR(+user,+resource))?
:- modeb(1,isEmployeeU_equals_projectR(+user,+resource))?
:- modeb(1,isEmployeeU_equals_ridR(+user,+resource))?
:- modeb(1,isEmployeeU_equals_departmentR(+user,+resource))?
:- modeb(1,isEmployeeU_equals_proprietaryR(+user,+resource))?
:- modeb(1,isEmployeeU_equals_typeR(+user,+resource))?
:- modeb(1,superset(+attribValSet,+attribValSet))?
:- modeb(1,element(+attribValAtomic,+attribValSet))?
:- modeb(1,element(+attribValAtomic,#attribValSet))?
:- modeb(1,element(#attribValAtomic,+attribValSet))?
%Types
user(code22).
user(des22).
user(code21).
user(code12).
user(code11).
user(des21).
user(plan2).
user(des11).
user(des12).
user(plan1).
user(ldr2).
user(acc2).
user(ldr11).
user(acc1).
user(ldr12).
user(mgr1).
user(mgr2).
user(aud1).
user(aud2).
resource(proj11task2).
resource(proj21task2a).
resource(proj22task2a).
resource(proj12task2).
resource(proj12task1).
resource(proj11task2prop).
resource(proj12task1a).
resource(proj12task1propa).
resource(proj21task1propa).
resource(proj11task2a).
resource(proj22task2prop).
resource(proj22budget).
resource(proj12sched).
resource(proj21task1prop).
resource(proj11budget).
resource(proj11task2propa).
resource(proj21task2prop).
resource(proj21budget).
resource(proj11task1).
resource(proj22task1).
resource(proj22sched).
resource(proj12task2propa).
resource(proj22task2propa).
resource(proj22task1propa).
resource(proj22task1a).
resource(proj12task2prop).
resource(proj11task1propa).
resource(proj11sched).
resource(proj12task2a).
resource(proj21task2).
resource(proj21task1).
resource(proj11task1a).
resource(proj21task2propa).
resource(proj21sched).
resource(proj22task1prop).
resource(proj12task1prop).
resource(proj12budget).
resource(proj11task1prop).
resource(proj21task1a).
resource(proj22task2).
operation(approve).
operation(setschedule).
operation(setcost).
operation(request).
operation(write).
operation(read).
operation(setstatus).
attribValAtomic(proj11task2).
attribValAtomic(manager).
attribValAtomic(auditor).
attribValAtomic(proj22task2a).
attribValAtomic(proj12task2).
attribValAtomic(proj12task1).
attribValAtomic(plan2).
attribValAtomic(plan1).
attribValAtomic(proj12task1propa).
attribValAtomic(ldr2).
attribValAtomic(proj22).
attribValAtomic(proj21).
attribValAtomic(proj22task2prop).
attribValAtomic(false).
attribValAtomic(proj21budget).
attribValAtomic(proj11task1).
attribValAtomic(proj22task2propa).
attribValAtomic(proj12task2prop).
attribValAtomic(des11).
attribValAtomic(des12).
attribValAtomic(proj11task1propa).
attribValAtomic(proj11sched).
attribValAtomic(accountant).
attribValAtomic(ldr11).
attribValAtomic(ldr12).
attribValAtomic(proj21task2).
attribValAtomic(proj21task1).
attribValAtomic(false_).
attribValAtomic(proj12task1prop).
attribValAtomic(proj12budget).
attribValAtomic(proj21task1a).
attribValAtomic(code22).
attribValAtomic(des22).
attribValAtomic(budget).
attribValAtomic(code21).
attribValAtomic(proj21task2a).
attribValAtomic(des21).
attribValAtomic(proj11task2prop).
attribValAtomic(proj12task1a).
attribValAtomic(proj21task1propa).
attribValAtomic(proj11task2a).
attribValAtomic(proj22budget).
attribValAtomic(proj12sched).
attribValAtomic(proj11budget).
attribValAtomic(proj21task1prop).
attribValAtomic(proj11task2propa).
attribValAtomic(proj21task2prop).
attribValAtomic(proj22task1).
attribValAtomic(code12).
attribValAtomic(proj22sched).
attribValAtomic(code11).
attribValAtomic(proj12task2propa).
attribValAtomic(dept2).
attribValAtomic(dept1).
attribValAtomic(task).
attribValAtomic(proj22task1propa).
attribValAtomic(proj22task1a).
attribValAtomic(proj11).
attribValAtomic(true).
attribValAtomic(proj12).
attribValAtomic(proj12task2a).
attribValAtomic(acc2).
attribValAtomic(acc1).
attribValAtomic(schedule).
attribValAtomic(mgr1).
attribValAtomic(proj11task1a).
attribValAtomic(mgr2).
attribValAtomic(proj21task2propa).
attribValAtomic(proj21sched).
attribValAtomic(aud1).
attribValAtomic(proj22task1prop).
attribValAtomic(aud2).
attribValAtomic(planner).
attribValAtomic(proj11task1prop).
attribValAtomic(true_).
attribValAtomic(design).
attribValAtomic(proj22task2).
attribValAtomic(coding).
attribValSet([]).
attribValSet([V|Vs]) :- attribValAtomic(V), attribValSet(Vs).
uidUType(code22).
uidUType(des22).
uidUType(code21).
uidUType(code12).
uidUType(code11).
uidUType(des21).
uidUType(plan2).
uidUType(des11).
uidUType(des12).
uidUType(plan1).
uidUType(ldr2).
uidUType(acc2).
uidUType(ldr11).
uidUType(acc1).
uidUType(ldr12).
uidUType(mgr1).
uidUType(mgr2).
uidUType(aud1).
uidUType(aud2).
projectsUType(proj11).
projectsUType(proj12).
projectsUType(proj11).
projectsUType(proj12).
projectsUType(proj22).
projectsUType(proj22).
projectsUType(proj21).
projectsUType(proj21).
expertiseUType(design).
expertiseUType(coding).
adminRolesUType(manager).
adminRolesUType(auditor).
adminRolesUType(planner).
adminRolesUType(accountant).
departmentUType(dept2).
departmentUType(dept1).
tasksUType(proj12task1a).
tasksUType(proj12task1propa).
tasksUType(proj22task2a).
tasksUType(proj22task2propa).
tasksUType(proj11task2a).
tasksUType(proj11task2propa).
tasksUType(proj21task1a).
tasksUType(proj21task1propa).
tasksUType(proj12task2propa).
tasksUType(proj12task2a).
tasksUType(proj11task1a).
tasksUType(proj11task1propa).
tasksUType(proj22task1propa).
tasksUType(proj22task1a).
tasksUType(proj21task2a).
tasksUType(proj21task2propa).
projectsLedUType(proj11).
projectsLedUType(proj12).
projectsLedUType(proj22).
projectsLedUType(proj21).
isEmployeeUType(false_).
isEmployeeUType(true_).
projectUType(proj11).
projectUType(proj12).
projectUType(proj22).
projectUType(proj21).
expertiseUType(design).
expertiseUType(coding).
ridUType(proj11task2).
ridUType(proj21task2a).
ridUType(proj22task2a).
ridUType(proj12task2).
ridUType(proj12task1).
ridUType(proj11task2prop).
ridUType(proj12task1a).
ridUType(proj12task1propa).
ridUType(proj21task1propa).
ridUType(proj11task2a).
ridUType(proj22task2prop).
ridUType(proj22budget).
ridUType(proj12sched).
ridUType(proj21task1prop).
ridUType(proj11budget).
ridUType(proj11task2propa).
ridUType(proj21task2prop).
ridUType(proj21budget).
ridUType(proj11task1).
ridUType(proj22task1).
ridUType(proj22sched).
ridUType(proj12task2propa).
ridUType(proj22task2propa).
ridUType(proj22task1propa).
ridUType(proj22task1a).
ridUType(proj12task2prop).
ridUType(proj11task1propa).
ridUType(proj11sched).
ridUType(proj12task2a).
ridUType(proj21task2).
ridUType(proj21task1).
ridUType(proj11task1a).
ridUType(proj21task2propa).
ridUType(proj21sched).
ridUType(proj22task1prop).
ridUType(proj12task1prop).
ridUType(proj12budget).
ridUType(proj11task1prop).
ridUType(proj21task1a).
ridUType(proj22task2).
departmentUType(dept2).
departmentUType(dept1).
proprietaryUType(true).
proprietaryUType(false).
typeUType(budget).
typeUType(schedule).
typeUType(task).
%Background Knowledge
uidU(code22,code22).
projectsU(code22,proj22).
expertiseU(code22,coding).
tasksU(code22,proj22task2a).
tasksU(code22,proj22task2propa).
isEmployeeU(code22,false_).
uidU(des22,des22).
projectsU(des22,proj22).
expertiseU(des22,design).
tasksU(des22,proj22task1propa).
tasksU(des22,proj22task1a).
isEmployeeU(des22,false_).
uidU(code21,code21).
projectsU(code21,proj21).
expertiseU(code21,coding).
tasksU(code21,proj21task2a).
tasksU(code21,proj21task2propa).
isEmployeeU(code21,true_).
uidU(code12,code12).
projectsU(code12,proj12).
expertiseU(code12,coding).
tasksU(code12,proj12task2propa).
tasksU(code12,proj12task2a).
isEmployeeU(code12,false_).
uidU(code11,code11).
projectsU(code11,proj11).
expertiseU(code11,coding).
tasksU(code11,proj11task2a).
tasksU(code11,proj11task2propa).
isEmployeeU(code11,true_).
uidU(des21,des21).
projectsU(des21,proj21).
expertiseU(des21,design).
tasksU(des21,proj21task1a).
tasksU(des21,proj21task1propa).
isEmployeeU(des21,true_).
uidU(plan2,plan2).
projectsU(plan2,proj22).
projectsU(plan2,proj21).
adminRolesU(plan2,planner).
uidU(des11,des11).
projectsU(des11,proj11).
expertiseU(des11,design).
tasksU(des11,proj11task1a).
tasksU(des11,proj11task1propa).
isEmployeeU(des11,true_).
uidU(des12,des12).
projectsU(des12,proj12).
expertiseU(des12,design).
tasksU(des12,proj12task1a).
tasksU(des12,proj12task1propa).
isEmployeeU(des12,false_).
uidU(plan1,plan1).
projectsU(plan1,proj11).
projectsU(plan1,proj12).
adminRolesU(plan1,planner).
uidU(ldr2,ldr2).
projectsU(ldr2,proj22).
projectsU(ldr2,proj21).
departmentU(ldr2,dept2).
projectsLedU(ldr2,proj22).
projectsLedU(ldr2,proj21).
uidU(acc2,acc2).
projectsU(acc2,proj22).
projectsU(acc2,proj21).
adminRolesU(acc2,accountant).
uidU(ldr11,ldr11).
projectsU(ldr11,proj11).
projectsU(ldr11,proj12).
departmentU(ldr11,dept1).
projectsLedU(ldr11,proj11).
uidU(acc1,acc1).
projectsU(acc1,proj11).
projectsU(acc1,proj12).
adminRolesU(acc1,accountant).
uidU(ldr12,ldr12).
projectsU(ldr12,proj12).
departmentU(ldr12,dept1).
projectsLedU(ldr12,proj12).
uidU(mgr1,mgr1).
adminRolesU(mgr1,manager).
departmentU(mgr1,dept1).
uidU(mgr2,mgr2).
adminRolesU(mgr2,manager).
departmentU(mgr2,dept2).
uidU(aud1,aud1).
projectsU(aud1,proj11).
projectsU(aud1,proj12).
adminRolesU(aud1,auditor).
uidU(aud2,aud2).
projectsU(aud2,proj22).
projectsU(aud2,proj21).
adminRolesU(aud2,auditor).
projectR(proj11task2,proj11).
expertiseR(proj11task2,coding).
ridR(proj11task2,proj11task2).
departmentR(proj11task2,dept1).
proprietaryR(proj11task2,false).
typeR(proj11task2,task).
projectR(proj21task2a,proj21).
expertiseR(proj21task2a,coding).
ridR(proj21task2a,proj21task2a).
departmentR(proj21task2a,dept2).
proprietaryR(proj21task2a,false).
typeR(proj21task2a,task).
projectR(proj22task2a,proj22).
expertiseR(proj22task2a,coding).
ridR(proj22task2a,proj22task2a).
departmentR(proj22task2a,dept2).
proprietaryR(proj22task2a,false).
typeR(proj22task2a,task).
projectR(proj12task2,proj12).
expertiseR(proj12task2,coding).
ridR(proj12task2,proj12task2).
departmentR(proj12task2,dept1).
proprietaryR(proj12task2,false).
typeR(proj12task2,task).
projectR(proj12task1,proj12).
expertiseR(proj12task1,design).
ridR(proj12task1,proj12task1).
departmentR(proj12task1,dept1).
proprietaryR(proj12task1,false).
typeR(proj12task1,task).
projectR(proj11task2prop,proj11).
expertiseR(proj11task2prop,coding).
ridR(proj11task2prop,proj11task2prop).
departmentR(proj11task2prop,dept1).
proprietaryR(proj11task2prop,true).
typeR(proj11task2prop,task).
projectR(proj12task1a,proj12).
expertiseR(proj12task1a,design).
ridR(proj12task1a,proj12task1a).
departmentR(proj12task1a,dept1).
proprietaryR(proj12task1a,false).
typeR(proj12task1a,task).
projectR(proj12task1propa,proj12).
expertiseR(proj12task1propa,design).
ridR(proj12task1propa,proj12task1propa).
departmentR(proj12task1propa,dept1).
proprietaryR(proj12task1propa,true).
typeR(proj12task1propa,task).
projectR(proj21task1propa,proj21).
expertiseR(proj21task1propa,design).
ridR(proj21task1propa,proj21task1propa).
departmentR(proj21task1propa,dept2).
proprietaryR(proj21task1propa,true).
typeR(proj21task1propa,task).
projectR(proj11task2a,proj11).
expertiseR(proj11task2a,coding).
ridR(proj11task2a,proj11task2a).
departmentR(proj11task2a,dept1).
proprietaryR(proj11task2a,false).
typeR(proj11task2a,task).
projectR(proj22task2prop,proj22).
expertiseR(proj22task2prop,coding).
ridR(proj22task2prop,proj22task2prop).
departmentR(proj22task2prop,dept2).
proprietaryR(proj22task2prop,true).
typeR(proj22task2prop,task).
projectR(proj22budget,proj22).
ridR(proj22budget,proj22budget).
departmentR(proj22budget,dept2).
typeR(proj22budget,budget).
projectR(proj12sched,proj12).
ridR(proj12sched,proj12sched).
departmentR(proj12sched,dept1).
typeR(proj12sched,schedule).
projectR(proj21task1prop,proj21).
expertiseR(proj21task1prop,design).
ridR(proj21task1prop,proj21task1prop).
departmentR(proj21task1prop,dept2).
proprietaryR(proj21task1prop,true).
typeR(proj21task1prop,task).
projectR(proj11budget,proj11).
ridR(proj11budget,proj11budget).
departmentR(proj11budget,dept1).
typeR(proj11budget,budget).
projectR(proj11task2propa,proj11).
expertiseR(proj11task2propa,coding).
ridR(proj11task2propa,proj11task2propa).
departmentR(proj11task2propa,dept1).
proprietaryR(proj11task2propa,true).
typeR(proj11task2propa,task).
projectR(proj21task2prop,proj21).
expertiseR(proj21task2prop,coding).
ridR(proj21task2prop,proj21task2prop).
departmentR(proj21task2prop,dept2).
proprietaryR(proj21task2prop,true).
typeR(proj21task2prop,task).
projectR(proj21budget,proj21).
ridR(proj21budget,proj21budget).
departmentR(proj21budget,dept2).
typeR(proj21budget,budget).
projectR(proj11task1,proj11).
expertiseR(proj11task1,design).
ridR(proj11task1,proj11task1).
departmentR(proj11task1,dept1).
proprietaryR(proj11task1,false).
typeR(proj11task1,task).
projectR(proj22task1,proj22).
expertiseR(proj22task1,design).
ridR(proj22task1,proj22task1).
departmentR(proj22task1,dept2).
proprietaryR(proj22task1,false).
typeR(proj22task1,task).
projectR(proj22sched,proj22).
ridR(proj22sched,proj22sched).
departmentR(proj22sched,dept2).
typeR(proj22sched,schedule).
projectR(proj12task2propa,proj12).
expertiseR(proj12task2propa,coding).
ridR(proj12task2propa,proj12task2propa).
departmentR(proj12task2propa,dept1).
proprietaryR(proj12task2propa,true).
typeR(proj12task2propa,task).
projectR(proj22task2propa,proj22).
expertiseR(proj22task2propa,coding).
ridR(proj22task2propa,proj22task2propa).
departmentR(proj22task2propa,dept2).
proprietaryR(proj22task2propa,true).
typeR(proj22task2propa,task).
projectR(proj22task1propa,proj22).
expertiseR(proj22task1propa,design).
ridR(proj22task1propa,proj22task1propa).
departmentR(proj22task1propa,dept2).
proprietaryR(proj22task1propa,true).
typeR(proj22task1propa,task).
projectR(proj22task1a,proj22).
expertiseR(proj22task1a,design).
ridR(proj22task1a,proj22task1a).
departmentR(proj22task1a,dept2).
proprietaryR(proj22task1a,false).
typeR(proj22task1a,task).
projectR(proj12task2prop,proj12).
expertiseR(proj12task2prop,coding).
ridR(proj12task2prop,proj12task2prop).
departmentR(proj12task2prop,dept1).
proprietaryR(proj12task2prop,true).
typeR(proj12task2prop,task).
projectR(proj11task1propa,proj11).
expertiseR(proj11task1propa,design).
ridR(proj11task1propa,proj11task1propa).
departmentR(proj11task1propa,dept1).
proprietaryR(proj11task1propa,true).
typeR(proj11task1propa,task).
projectR(proj11sched,proj11).
ridR(proj11sched,proj11sched).
departmentR(proj11sched,dept1).
typeR(proj11sched,schedule).
projectR(proj12task2a,proj12).
expertiseR(proj12task2a,coding).
ridR(proj12task2a,proj12task2a).
departmentR(proj12task2a,dept1).
proprietaryR(proj12task2a,false).
typeR(proj12task2a,task).
projectR(proj21task2,proj21).
expertiseR(proj21task2,coding).
ridR(proj21task2,proj21task2).
departmentR(proj21task2,dept2).
proprietaryR(proj21task2,false).
typeR(proj21task2,task).
projectR(proj21task1,proj21).
expertiseR(proj21task1,design).
ridR(proj21task1,proj21task1).
departmentR(proj21task1,dept2).
proprietaryR(proj21task1,false).
typeR(proj21task1,task).
projectR(proj11task1a,proj11).
expertiseR(proj11task1a,design).
ridR(proj11task1a,proj11task1a).
departmentR(proj11task1a,dept1).
proprietaryR(proj11task1a,false).
typeR(proj11task1a,task).
projectR(proj21task2propa,proj21).
expertiseR(proj21task2propa,coding).
ridR(proj21task2propa,proj21task2propa).
departmentR(proj21task2propa,dept2).
proprietaryR(proj21task2propa,true).
typeR(proj21task2propa,task).
projectR(proj21sched,proj21).
ridR(proj21sched,proj21sched).
departmentR(proj21sched,dept2).
typeR(proj21sched,schedule).
projectR(proj22task1prop,proj22).
expertiseR(proj22task1prop,design).
ridR(proj22task1prop,proj22task1prop).
departmentR(proj22task1prop,dept2).
proprietaryR(proj22task1prop,true).
typeR(proj22task1prop,task).
projectR(proj12task1prop,proj12).
expertiseR(proj12task1prop,design).
ridR(proj12task1prop,proj12task1prop).
departmentR(proj12task1prop,dept1).
proprietaryR(proj12task1prop,true).
typeR(proj12task1prop,task).
projectR(proj12budget,proj12).
ridR(proj12budget,proj12budget).
departmentR(proj12budget,dept1).
typeR(proj12budget,budget).
projectR(proj11task1prop,proj11).
expertiseR(proj11task1prop,design).
ridR(proj11task1prop,proj11task1prop).
departmentR(proj11task1prop,dept1).
proprietaryR(proj11task1prop,true).
typeR(proj11task1prop,task).
projectR(proj21task1a,proj21).
expertiseR(proj21task1a,design).
ridR(proj21task1a,proj21task1a).
departmentR(proj21task1a,dept2).
proprietaryR(proj21task1a,false).
typeR(proj21task1a,task).
projectR(proj22task2,proj22).
expertiseR(proj22task2,coding).
ridR(proj22task2,proj22task2).
departmentR(proj22task2,dept2).
proprietaryR(proj22task2,false).
typeR(proj22task2,task).
projectsU_superset_expertiseR(U,R) :- setof(X,projectsU(U,X),SU), setof(Y,expertiseR(R,Y),SR), superset(SU,SR), not(SR==[]).
expertiseU_superset_expertiseR(U,R) :- setof(X,expertiseU(U,X),SU), setof(Y,expertiseR(R,Y),SR), superset(SU,SR), not(SR==[]).
adminRolesU_superset_expertiseR(U,R) :- setof(X,adminRolesU(U,X),SU), setof(Y,expertiseR(R,Y),SR), superset(SU,SR), not(SR==[]).
tasksU_superset_expertiseR(U,R) :- setof(X,tasksU(U,X),SU), setof(Y,expertiseR(R,Y),SR), superset(SU,SR), not(SR==[]).
projectsLedU_superset_expertiseR(U,R) :- setof(X,projectsLedU(U,X),SU), setof(Y,expertiseR(R,Y),SR), superset(SU,SR), not(SR==[]).
projectsU_contains_projectR(U,R) :-projectsU(U,X),projectR(R,X).
projectsU_contains_ridR(U,R) :-projectsU(U,X),ridR(R,X).
projectsU_contains_departmentR(U,R) :-projectsU(U,X),departmentR(R,X).
projectsU_contains_proprietaryR(U,R) :-projectsU(U,X),proprietaryR(R,X).
projectsU_contains_typeR(U,R) :-projectsU(U,X),typeR(R,X).
expertiseU_contains_projectR(U,R) :-expertiseU(U,X),projectR(R,X).
expertiseU_contains_ridR(U,R) :-expertiseU(U,X),ridR(R,X).
expertiseU_contains_departmentR(U,R) :-expertiseU(U,X),departmentR(R,X).
expertiseU_contains_proprietaryR(U,R) :-expertiseU(U,X),proprietaryR(R,X).
expertiseU_contains_typeR(U,R) :-expertiseU(U,X),typeR(R,X).
adminRolesU_contains_projectR(U,R) :-adminRolesU(U,X),projectR(R,X).
adminRolesU_contains_ridR(U,R) :-adminRolesU(U,X),ridR(R,X).
adminRolesU_contains_departmentR(U,R) :-adminRolesU(U,X),departmentR(R,X).
adminRolesU_contains_proprietaryR(U,R) :-adminRolesU(U,X),proprietaryR(R,X).
adminRolesU_contains_typeR(U,R) :-adminRolesU(U,X),typeR(R,X).
tasksU_contains_projectR(U,R) :-tasksU(U,X),projectR(R,X).
tasksU_contains_ridR(U,R) :-tasksU(U,X),ridR(R,X).
tasksU_contains_departmentR(U,R) :-tasksU(U,X),departmentR(R,X).
tasksU_contains_proprietaryR(U,R) :-tasksU(U,X),proprietaryR(R,X).
tasksU_contains_typeR(U,R) :-tasksU(U,X),typeR(R,X).
projectsLedU_contains_projectR(U,R) :-projectsLedU(U,X),projectR(R,X).
projectsLedU_contains_ridR(U,R) :-projectsLedU(U,X),ridR(R,X).
projectsLedU_contains_departmentR(U,R) :-projectsLedU(U,X),departmentR(R,X).
projectsLedU_contains_proprietaryR(U,R) :-projectsLedU(U,X),proprietaryR(R,X).
projectsLedU_contains_typeR(U,R) :-projectsLedU(U,X),typeR(R,X).
uidU_equals_projectR(U,R) :-uidU(U,X),projectR(R,X).
uidU_equals_ridR(U,R) :-uidU(U,X),ridR(R,X).
uidU_equals_departmentR(U,R) :-uidU(U,X),departmentR(R,X).
uidU_equals_proprietaryR(U,R) :-uidU(U,X),proprietaryR(R,X).
uidU_equals_typeR(U,R) :-uidU(U,X),typeR(R,X).
departmentU_equals_projectR(U,R) :-departmentU(U,X),projectR(R,X).
departmentU_equals_ridR(U,R) :-departmentU(U,X),ridR(R,X).
departmentU_equals_departmentR(U,R) :-departmentU(U,X),departmentR(R,X).
departmentU_equals_proprietaryR(U,R) :-departmentU(U,X),proprietaryR(R,X).
departmentU_equals_typeR(U,R) :-departmentU(U,X),typeR(R,X).
isEmployeeU_equals_projectR(U,R) :-isEmployeeU(U,X),projectR(R,X).
isEmployeeU_equals_ridR(U,R) :-isEmployeeU(U,X),ridR(R,X).
isEmployeeU_equals_departmentR(U,R) :-isEmployeeU(U,X),departmentR(R,X).
isEmployeeU_equals_proprietaryR(U,R) :-isEmployeeU(U,X),proprietaryR(R,X).
isEmployeeU_equals_typeR(U,R) :-isEmployeeU(U,X),typeR(R,X).
superset(Y,[A|X]) :- element(A,Y), superset(Y,X).
superset(Y,[]).
% Positive and negative examples
:- up(acc1,proj12task2a,write).
:- up(code12,proj21task2a,approve).
up(des11,proj11task1propa,read).
:- up(aud2,proj22task2a,setschedule).
:- up(des12,proj21task2propa,request).
:- up(code11,proj12task1prop,setstatus).
:- up(des21,proj22task1propa,read).
:- up(mgr2,proj21task2prop,setcost).
:- up(ldr11,proj11task1a,read).
:- up(ldr12,proj12task2,write).
:- up(mgr1,proj12task1a,setcost).
:- up(des21,proj22task1prop,approve).
:- up(code21,proj12task2propa,setstatus).
:- up(des11,proj11task1prop,setschedule).
:- up(plan1,proj12task1propa,read).
:- up(plan2,proj21task1,setstatus).
:- up(aud2,proj11task2propa,setstatus).
:- up(des21,proj22task1prop,setstatus).
:- up(ldr2,proj21task1,approve).
:- up(ldr2,proj11task1prop,request).
:- up(code11,proj12task1,request).
:- up(mgr2,proj11task2propa,setstatus).
:- up(ldr12,proj12task2propa,read).
:- up(acc2,proj22task1,request).
:- up(code21,proj21budget,request).
:- up(code11,proj11task1,write).
:- up(des11,proj22task2propa,read).
:- up(mgr1,proj22sched,setschedule).
:- up(mgr1,proj21task2propa,setschedule).
:- up(des12,proj11task2prop,setschedule).
:- up(aud2,proj21budget,setcost).
up(ldr2,proj21budget,write).
:- up(ldr2,proj12sched,read).
:- up(mgr1,proj11task1a,read).
:- up(code11,proj12task2prop,approve).
:- up(code22,proj21task1a,setcost).
:- up(acc1,proj11budget,setschedule).
:- up(code11,proj12task1prop,request).
:- up(ldr11,proj11sched,setschedule).
:- up(ldr2,proj11task2a,setstatus).
:- up(acc2,proj12task2,write).
:- up(plan2,proj21sched,setcost).
:- up(acc1,proj12sched,approve).
:- up(acc2,proj11sched,request).
:- up(des11,proj11task2prop,read).
:- up(aud2,proj21task2,approve).
:- up(mgr2,proj21budget,setschedule).
:- up(mgr2,proj21task2propa,write).
:- up(des12,proj11sched,write).
:- up(des11,proj21task1,write).
:- up(des12,proj21task2,write).
:- up(plan1,proj11task1,setstatus).
:- up(des11,proj21task1a,write).
:- up(des21,proj21task2,request).
:- up(des22,proj12budget,setschedule).
:- up(code21,proj21task2prop,setschedule).
:- up(code22,proj11task2a,read).
:- up(code21,proj12task2propa,setschedule).
:- up(plan1,proj22task2a,setcost).
:- up(code22,proj11task2,write).
:- up(plan1,proj12task1,write).
:- up(des21,proj11budget,request).
:- up(des22,proj22task1,approve).
:- up(des21,proj12task2,setcost).
:- up(des21,proj11task2a,approve).
:- up(aud1,proj11task2prop,setstatus).
:- up(mgr2,proj11task1a,read).
up(code12,proj12task2a,read).
up(code12,proj12task2,request).
:- up(des21,proj22sched,write).
:- up(acc1,proj22task2propa,request).
:- up(des21,proj12sched,read).
:- up(ldr2,proj11task1prop,read).
:- up(mgr1,proj22task2prop,request).
:- up(code12,proj12task1a,setstatus).
:- up(mgr2,proj21task2a,setschedule).
:- up(ldr11,proj22task1prop,read).
:- up(aud1,proj12task2propa,setcost).
:- up(des11,proj21task1prop,setschedule).
:- up(aud1,proj12task1propa,setcost).
:- up(mgr1,proj21task2prop,read).
:- up(ldr11,proj22task1prop,setschedule).
:- up(des21,proj11task1propa,approve).
:- up(aud2,proj21task1,write).
:- up(des12,proj22sched,setschedule).
:- up(mgr2,proj21task2propa,approve).
:- up(ldr2,proj22task1propa,write).
:- up(plan1,proj11task2prop,read).
:- up(des12,proj11task2prop,setstatus).
:- up(des12,proj22budget,approve).
:- up(plan2,proj11task1propa,read).
:- up(aud2,proj21task1prop,write).
:- up(ldr11,proj11task1prop,request).
:- up(plan2,proj11task2prop,setstatus).
:- up(aud1,proj21task2propa,request).
:- up(des11,proj22task2propa,setstatus).
:- up(mgr1,proj22sched,approve).
:- up(ldr2,proj12task1,setcost).
:- up(ldr2,proj11task1a,request).
:- up(ldr11,proj22budget,setstatus).
up(plan2,proj22task2propa,setschedule).
:- up(mgr1,proj12task2prop,setstatus).
:- up(des22,proj11task1a,setcost).
:- up(aud1,proj11task2prop,request).
:- up(des11,proj12task2propa,request).
:- up(des11,proj22task2prop,approve).
:- up(des22,proj11task1prop,read).
up(plan1,proj12task2propa,setschedule).
:- up(ldr2,proj11task2propa,approve).
:- up(ldr12,proj22task1propa,write).
:- up(des21,proj12task1a,setstatus).
:- up(plan2,proj11task2,read).
:- up(code21,proj11task1a,read).
:- up(acc2,proj21task2prop,approve).
:- up(mgr2,proj11sched,write).
:- up(ldr2,proj11task1propa,setschedule).
:- up(code11,proj21task1a,write).
:- up(mgr2,proj21task2a,approve).
:- up(code22,proj11task2,setcost).
:- up(des11,proj21task1propa,setcost).
:- up(code12,proj12task1,setcost).
:- up(code11,proj21task1,request).
:- up(code12,proj22task1prop,setstatus).
:- up(des12,proj22task1,request).
:- up(mgr2,proj21task1prop,setschedule).
:- up(code21,proj12task1propa,request).
:- up(ldr2,proj21task2propa,setstatus).
:- up(mgr2,proj21task2,write).
:- up(mgr2,proj22task1prop,request).
:- up(ldr12,proj22task2propa,setschedule).
:- up(des12,proj22task1propa,approve).
:- up(plan1,proj11sched,request).
:- up(code11,proj22task2,setstatus).
:- up(ldr12,proj11sched,setschedule).
:- up(ldr12,proj12task2propa,setcost).
:- up(code21,proj11task2a,approve).
:- up(ldr12,proj22task2propa,read).
:- up(plan1,proj11task2propa,approve).
:- up(aud1,proj21task1a,read).
:- up(ldr11,proj11budget,request).
:- up(des12,proj11sched,request).
:- up(ldr12,proj21task1prop,write).
up(plan2,proj22task2,setschedule).
:- up(plan1,proj12task1propa,setcost).
:- up(acc1,proj12task1a,write).
:- up(mgr1,proj21sched,approve).
:- up(acc1,proj12task1a,setschedule).
:- up(aud2,proj22task1propa,setstatus).
:- up(aud2,proj21task1,read).
:- up(des22,proj22task1a,setcost).
:- up(mgr2,proj21task2a,read).
:- up(code21,proj22task2propa,setschedule).
:- up(aud2,proj21task2a,setcost).
:- up(code11,proj22task1prop,approve).
:- up(aud2,proj11task1prop,setstatus).
:- up(ldr2,proj21task2prop,setstatus).
:- up(code22,proj12task1propa,setcost).
:- up(aud1,proj12task2,setstatus).
:- up(mgr1,proj11task1prop,approve).
:- up(ldr2,proj11task1prop,setstatus).
:- up(des11,proj21budget,write).
:- up(code11,proj21task2propa,setschedule).
:- up(mgr2,proj12task2prop,setschedule).
:- up(ldr12,proj21task2prop,setstatus).
:- up(plan2,proj21task1prop,setcost).
:- up(aud1,proj21budget,setcost).
:- up(mgr1,proj22budget,approve).
:- up(mgr1,proj21sched,setschedule).
:- up(plan1,proj11task1propa,request).
:- up(code22,proj11task1a,setstatus).
:- up(des21,proj22sched,request).
:- up(acc1,proj22budget,request).
:- up(des12,proj11task2propa,request).
:- up(acc2,proj22task2a,request).
:- up(mgr1,proj11task1,setschedule).
:- up(aud1,proj22task2a,write).
:- up(des12,proj21task2,setschedule).
:- up(mgr2,proj22sched,setstatus).
:- up(ldr2,proj11task2propa,write).
:- up(des22,proj22task2propa,setcost).
:- up(code21,proj22task1propa,setschedule).
:- up(code11,proj12budget,request).
:- up(aud1,proj21task2propa,setstatus).
:- up(des21,proj22task2propa,request).
:- up(code22,proj22task1,write).
:- up(acc2,proj12task1prop,read).
:- up(plan1,proj22task1propa,request).
:- up(des11,proj21task2a,setstatus).
:- up(ldr2,proj21budget,setschedule).
:- up(acc1,proj22task2propa,setstatus).
:- up(des12,proj21sched,read).
:- up(ldr2,proj11task1a,setschedule).
:- up(plan2,proj21sched,request).
:- up(code21,proj22task1,request).
:- up(acc2,proj21task2,approve).
:- up(code12,proj21task2prop,setstatus).
:- up(aud1,proj21task2prop,request).
:- up(des12,proj22task2prop,write).
:- up(aud2,proj12task1,request).
:- up(mgr1,proj21task1propa,write).
:- up(code11,proj22task1prop,setcost).
:- up(aud1,proj22task1a,setcost).
:- up(acc2,proj21task2propa,read).
:- up(ldr2,proj12sched,setcost).
:- up(code21,proj11sched,setstatus).
:- up(aud2,proj21task1,request).
:- up(plan2,proj21task1a,setcost).
:- up(code12,proj11task2a,read).
up(plan2,proj21task2,setschedule).
:- up(des22,proj11sched,setschedule).
:- up(aud1,proj21task1prop,setschedule).
:- up(aud2,proj11task1propa,setschedule).
:- up(mgr2,proj12task1,setcost).
:- up(acc1,proj21task1propa,setschedule).
:- up(ldr11,proj12task1prop,read).
:- up(ldr2,proj21task1a,setschedule).
:- up(des11,proj21task2prop,setstatus).
:- up(acc2,proj21task2a,write).
:- up(plan1,proj21task1propa,approve).
:- up(des21,proj21task1a,setcost).
:- up(code21,proj21budget,approve).
:- up(plan1,proj12task2propa,setcost).
:- up(des21,proj21budget,read).
:- up(ldr12,proj22task2,write).
:- up(code11,proj12task2a,setcost).
:- up(des22,proj11task2prop,write).
:- up(mgr2,proj22task2propa,read).
:- up(des22,proj22task2propa,setschedule).
:- up(ldr2,proj22task1propa,setstatus).
:- up(acc2,proj21task1propa,setschedule).
:- up(plan1,proj21task1,setstatus).
:- up(ldr12,proj21task1propa,read).
:- up(code11,proj11budget,approve).
up(code22,proj22sched,read).
:- up(code22,proj22task1propa,read).
:- up(mgr1,proj22sched,read).
:- up(plan2,proj11task1prop,setschedule).
:- up(acc1,proj22task2prop,setstatus).
:- up(des11,proj12budget,write).
:- up(aud2,proj11task2a,approve).
:- up(code22,proj21task1a,request).
:- up(des12,proj22task2,setschedule).
:- up(code11,proj12task2propa,request).
:- up(aud1,proj12task2propa,write).
:- up(plan1,proj11budget,write).
:- up(des21,proj22task1,setcost).
:- up(ldr11,proj12task2,request).
:- up(des12,proj11task2propa,read).
:- up(aud2,proj21task1propa,read).
:- up(code21,proj12task2prop,read).
:- up(acc2,proj11task2,approve).
:- up(ldr11,proj22task2propa,read).
:- up(mgr1,proj21task1,request).
:- up(plan2,proj11task2a,setstatus).
:- up(mgr2,proj12task2,setschedule).
:- up(mgr1,proj22budget,setstatus).
:- up(acc1,proj12sched,setstatus).
:- up(ldr2,proj21task1propa,setcost).
:- up(mgr1,proj11task1propa,request).
:- up(ldr12,proj22budget,setschedule).
:- up(aud2,proj11task1prop,write).
:- up(ldr11,proj11task1prop,setschedule).
:- up(aud1,proj22task1prop,approve).
:- up(plan2,proj22task1a,setstatus).
:- up(des12,proj11task1,request).
:- up(des12,proj21task2,approve).
:- up(code22,proj11task2propa,approve).
:- up(plan2,proj21task1a,write).
:- up(aud2,proj11task2,write).
:- up(mgr2,proj22sched,setcost).
:- up(code11,proj11task1,approve).
:- up(plan2,proj22task1,approve).
:- up(ldr2,proj22task1a,read).
:- up(acc1,proj21task1,read).
:- up(code21,proj12sched,write).
:- up(aud1,proj12task1prop,request).
:- up(code12,proj11task2propa,write).
:- up(code11,proj22task1propa,write).
:- up(mgr1,proj22task1prop,request).
:- up(aud1,proj12task2prop,write).
:- up(plan2,proj21task1a,setstatus).
:- up(code11,proj21task1propa,setschedule).
up(plan2,proj22task1,setschedule).
:- up(code22,proj21task2,setstatus).
:- up(des22,proj21task2,setcost).
up(plan1,proj11task2propa,setschedule).
:- up(des11,proj21task1,request).
:- up(code21,proj12sched,read).
:- up(des21,proj22task2prop,request).
:- up(aud1,proj12task1,setstatus).
:- up(code22,proj11task2propa,setstatus).
:- up(ldr11,proj22task2prop,read).
:- up(des12,proj11task2a,setcost).
:- up(ldr11,proj22task1,read).
:- up(mgr1,proj21task1a,request).
:- up(mgr1,proj11task2a,read).
:- up(ldr11,proj22task1prop,approve).
:- up(des11,proj12task1,approve).
:- up(code22,proj12budget,setstatus).
:- up(mgr2,proj22task2a,approve).
:- up(des22,proj22budget,setstatus).
:- up(aud1,proj11task2a,setcost).
:- up(des11,proj22task1,read).
:- up(aud1,proj11task2propa,write).
:- up(code21,proj21budget,write).
:- up(plan1,proj11budget,request).
:- up(plan1,proj12task1a,setcost).
:- up(des22,proj12task2,setstatus).
:- up(code11,proj21task1propa,read).
:- up(des22,proj12task1,setschedule).
:- up(ldr11,proj11task1a,write).
:- up(plan1,proj22task2prop,setcost).
up(code12,proj12task2a,setstatus).
:- up(code22,proj12task1propa,read).
:- up(code12,proj11task2propa,setcost).
:- up(mgr1,proj21task2,setschedule).
:- up(ldr12,proj11sched,setstatus).
:- up(code11,proj11task2,setstatus).
:- up(des22,proj21task1a,setschedule).
:- up(plan1,proj22budget,request).
:- up(mgr2,proj11task2propa,setcost).
:- up(mgr1,proj21task1propa,request).
:- up(code22,proj22budget,read).
:- up(mgr1,proj11task2,write).
:- up(des11,proj22task1prop,read).
:- up(des21,proj21task2propa,approve).
:- up(acc1,proj21task2propa,setschedule).
:- up(mgr1,proj21task1,read).
:- up(mgr2,proj22task2a,write).
:- up(ldr12,proj11task1,setschedule).
:- up(des22,proj22task2prop,request).
:- up(aud2,proj12sched,read).
:- up(des21,proj22task1a,approve).
:- up(des12,proj22task1a,request).
:- up(mgr2,proj12task1prop,read).
:- up(code11,proj21task1prop,setschedule).
:- up(acc2,proj21task1a,setstatus).
:- up(des12,proj12task2propa,setschedule).
:- up(code22,proj22budget,setstatus).
:- up(acc2,proj12task1propa,setstatus).
:- up(des12,proj12task1prop,setcost).
:- up(ldr2,proj21task1prop,request).
:- up(aud1,proj22task2prop,setcost).
:- up(des22,proj22task2propa,read).
:- up(des11,proj11task2prop,setcost).
:- up(ldr11,proj12task2,setstatus).
:- up(des22,proj22task2a,write).
:- up(des11,proj12task1,setcost).
:- up(plan2,proj11budget,setcost).
:- up(plan1,proj11task2a,request).
:- up(aud2,proj21task1,setschedule).
:- up(aud2,proj11task1a,write).
:- up(plan1,proj21task1a,write).
:- up(plan1,proj11task1a,read).
:- up(des22,proj21task2propa,setcost).
:- up(code22,proj11task1propa,request).
:- up(ldr12,proj21task2propa,read).
:- up(acc2,proj22task1prop,setstatus).
:- up(plan1,proj12task2prop,setstatus).
:- up(ldr2,proj21sched,setschedule).
:- up(acc2,proj11task2a,request).
:- up(code21,proj21task2,approve).
:- up(des22,proj22task2propa,write).
:- up(code21,proj12task2,setschedule).
:- up(acc2,proj21task1prop,request).
:- up(mgr1,proj11sched,approve).
:- up(ldr12,proj11task1a,write).
:- up(ldr2,proj12task1propa,write).
:- up(mgr2,proj21sched,approve).
:- up(mgr1,proj21task2propa,setstatus).
:- up(acc1,proj12task2prop,write).
:- up(des21,proj21task2,read).
:- up(ldr2,proj11task2propa,setstatus).
:- up(des11,proj12task1a,setcost).
:- up(mgr2,proj11sched,setcost).
:- up(aud2,proj21task2prop,read).
:- up(acc1,proj12task1a,read).
:- up(ldr2,proj22task1prop,setcost).
:- up(aud1,proj12task2prop,read).
:- up(code12,proj22sched,request).
:- up(ldr12,proj12task1a,setstatus).
:- up(aud2,proj12task2propa,setcost).
:- up(acc1,proj22task1propa,request).
:- up(plan2,proj12task1a,approve).
:- up(code12,proj12task1,setstatus).
:- up(code11,proj22task2prop,write).
:- up(code22,proj12task1,setschedule).
:- up(ldr11,proj12sched,approve).
:- up(mgr1,proj11task2propa,approve).
:- up(ldr11,proj12task2prop,setcost).
:- up(code21,proj12task2,setstatus).
:- up(ldr2,proj11task1a,read).
:- up(mgr2,proj21task2,setcost).
:- up(ldr2,proj21task2propa,write).
:- up(acc1,proj22task1prop,setstatus).
:- up(plan2,proj11task1,approve).
:- up(code12,proj21task1a,read).
:- up(acc1,proj12task1propa,request).
:- up(acc1,proj21task1a,request).
:- up(plan1,proj22task2prop,read).
:- up(ldr2,proj22task1,setcost).
:- up(code11,proj12budget,write).
:- up(des21,proj11budget,setschedule).
:- up(des21,proj11budget,setstatus).
:- up(aud1,proj11task1,setcost).
:- up(plan2,proj21task1prop,request).
up(des11,proj11task1propa,setstatus).
:- up(mgr1,proj11task1a,setstatus).
:- up(ldr12,proj11task2propa,request).
:- up(ldr2,proj21task1a,approve).
:- up(mgr2,proj11task1prop,request).
:- up(aud2,proj11budget,setstatus).
:- up(acc2,proj22task2,read).
:- up(acc1,proj12task2a,request).
:- up(aud2,proj11task1a,setcost).
:- up(ldr11,proj11task2propa,approve).
:- up(aud2,proj11task2prop,setschedule).
:- up(ldr12,proj11task2,approve).
:- up(code21,proj11task1prop,approve).
:- up(des21,proj11task2prop,read).
:- up(aud1,proj11task2,request).
:- up(mgr1,proj21task1propa,setschedule).
:- up(des22,proj22task1prop,setstatus).
up(des11,proj11sched,read).
:- up(ldr2,proj21task1prop,read).
:- up(code22,proj22sched,write).
:- up(des21,proj22task2propa,setschedule).
:- up(code11,proj12task1prop,setschedule).
:- up(des12,proj21task2propa,setschedule).
:- up(plan1,proj22task1prop,write).
:- up(des22,proj22sched,approve).
:- up(mgr2,proj11task1,setstatus).
:- up(code21,proj11task1a,write).
:- up(code22,proj21sched,write).
:- up(acc2,proj21task2,setstatus).
:- up(mgr1,proj11task2propa,request).
:- up(acc2,proj11task1,approve).
:- up(ldr2,proj12task2a,setstatus).
:- up(des11,proj11sched,write).
:- up(acc2,proj22task2prop,request).
:- up(acc1,proj21task2prop,request).
:- up(plan1,proj22task1,setstatus).
:- up(des12,proj22task1,setschedule).
:- up(acc2,proj11task1propa,read).
:- up(code21,proj12budget,setschedule).
:- up(ldr11,proj12sched,setcost).
:- up(des11,proj11task1propa,setcost).
:- up(code21,proj22task2,setcost).
:- up(des21,proj11task2propa,setcost).
:- up(mgr2,proj11task2a,read).
:- up(des21,proj11sched,read).
up(code21,proj21task2a,request).
:- up(des22,proj12task1,approve).
:- up(plan2,proj12task2a,approve).
:- up(code21,proj11task1prop,setcost).
:- up(ldr2,proj22task1prop,setstatus).
:- up(code21,proj22task2prop,request).
:- up(plan2,proj22task2propa,write).
:- up(plan2,proj11budget,request).
up(code21,proj21task2a,setstatus).
:- up(aud1,proj21task1prop,request).
:- up(acc2,proj11task1propa,setcost).
:- up(des12,proj22task2,request).
:- up(mgr2,proj12task2,request).
:- up(ldr11,proj21task2,write).
:- up(plan1,proj22task2a,request).
:- up(code11,proj12task1a,setcost).
:- up(mgr2,proj11task2prop,setcost).
:- up(des12,proj11budget,setcost).
:- up(aud1,proj11budget,setschedule).
up(mgr2,proj22budget,read).
:- up(acc2,proj12task1,setstatus).
:- up(des22,proj12task2propa,setschedule).
:- up(des11,proj12task1,request).
:- up(mgr2,proj11task2prop,read).
:- up(code21,proj22task1a,read).
:- up(acc1,proj11task2a,setschedule).
:- up(des21,proj21budget,setschedule).
:- up(mgr2,proj12task1prop,setstatus).
:- up(code12,proj11task1propa,setcost).
up(des11,proj11task1,request).
:- up(ldr2,proj12budget,setstatus).
:- up(code12,proj22task2prop,read).
:- up(aud2,proj11sched,request).
:- up(ldr12,proj22task1,setstatus).
:- up(aud2,proj22task2a,request).
:- up(des21,proj22task2prop,read).
:- up(aud1,proj12task1propa,setstatus).
:- up(code22,proj12task1a,setschedule).
:- up(acc1,proj22task1a,setschedule).
:- up(aud1,proj11budget,write).
:- up(ldr11,proj22budget,write).
:- up(code22,proj11task1prop,setstatus).
:- up(acc2,proj11task1prop,request).
:- up(ldr2,proj11task2prop,read).
:- up(mgr1,proj11task1prop,setcost).
:- up(des11,proj21task2propa,read).
:- up(ldr11,proj22task1,approve).
:- up(ldr2,proj21task2prop,read).
:- up(mgr2,proj22task1prop,setcost).
:- up(des21,proj22sched,setstatus).
up(des21,proj21sched,read).
:- up(ldr12,proj22task2,read).
:- up(mgr1,proj12task1a,setstatus).
:- up(plan2,proj22task2prop,approve).
:- up(plan1,proj22task1propa,read).
:- up(des21,proj11task2propa,write).
:- up(ldr12,proj22budget,setstatus).
:- up(acc1,proj22task2a,setstatus).
:- up(code21,proj12task2,read).
:- up(des11,proj12sched,setschedule).
:- up(aud1,proj12task2,setschedule).
up(acc2,proj21task2propa,setcost).
:- up(des22,proj12task2,setcost).
:- up(des21,proj12task2a,request).
:- up(des21,proj21task1propa,approve).
:- up(code22,proj12task2prop,write).
:- up(plan1,proj21task1,approve).
:- up(aud1,proj22budget,approve).
:- up(mgr1,proj12task1prop,approve).
:- up(code12,proj11task2prop,write).
:- up(code12,proj21task1a,request).
:- up(aud1,proj21budget,setstatus).
:- up(des21,proj22task2,setschedule).
:- up(acc2,proj22task1prop,write).
:- up(code11,proj12task1a,approve).
:- up(plan2,proj21task2prop,read).
:- up(acc2,proj11task2propa,write).
:- up(code11,proj11task1prop,approve).
:- up(code11,proj11task2prop,approve).
:- up(mgr2,proj11budget,request).
:- up(plan1,proj22task1,read).
:- up(code21,proj12task1propa,setcost).
:- up(aud2,proj11task2a,setcost).
:- up(ldr11,proj22task2propa,write).
:- up(code22,proj22task1propa,setstatus).
:- up(des12,proj11task2prop,setcost).
:- up(ldr12,proj11task2,request).
up(acc1,proj12task2propa,setcost).
:- up(aud2,proj21task2a,setstatus).
:- up(ldr2,proj21task2,setcost).
:- up(ldr2,proj21task2a,setschedule).
:- up(aud1,proj11task2propa,read).
:- up(ldr2,proj12task1a,request).
:- up(ldr2,proj22task2prop,approve).
:- up(ldr12,proj12task2,request).
:- up(des22,proj12sched,request).
up(aud1,proj12budget,read).
:- up(plan2,proj11task1,write).
:- up(plan1,proj11task1a,setstatus).
:- up(ldr11,proj22sched,write).
:- up(des21,proj11task2prop,setstatus).
:- up(plan2,proj22task1a,setcost).
:- up(mgr1,proj21task2,request).
:- up(code12,proj11task2prop,request).
:- up(acc1,proj21task1propa,write).
:- up(des12,proj11task2a,request).
:- up(code11,proj12task2propa,setstatus).
:- up(mgr1,proj12budget,setstatus).
up(plan1,proj12task2,setschedule).
up(des12,proj12sched,read).
:- up(code22,proj22task1prop,setcost).
:- up(code11,proj11task2,write).
:- up(des11,proj22task2,approve).
:- up(ldr2,proj12task1propa,setschedule).
:- up(code12,proj12task2propa,setcost).
:- up(des12,proj22sched,setcost).
:- up(ldr11,proj21task1,read).
:- up(acc1,proj21task2a,approve).
:- up(acc1,proj22task1a,setstatus).
:- up(des22,proj11task2a,request).
:- up(aud1,proj11task2a,setschedule).
:- up(aud1,proj12task1,request).
:- up(ldr2,proj12task1propa,setcost).
:- up(acc1,proj12task1prop,approve).
:- up(code11,proj21task2,request).
:- up(aud1,proj11task1propa,approve).
:- up(des12,proj22budget,setstatus).
:- up(code22,proj21task2prop,setstatus).
:- up(acc1,proj11task2propa,read).
:- up(acc1,proj11task1,setschedule).
:- up(des21,proj22task2a,read).
:- up(acc1,proj22budget,write).
:- up(ldr11,proj11task1a,approve).
:- up(des22,proj22task1a,approve).
:- up(des21,proj12task1a,read).
:- up(plan2,proj11task1,request).
:- up(ldr2,proj22task2a,approve).
:- up(mgr2,proj11task1,setschedule).
:- up(code12,proj11budget,setschedule).
:- up(des12,proj21task1propa,write).
up(mgr2,proj22budget,approve).
:- up(aud1,proj12task1,read).
:- up(plan1,proj21task1a,approve).
:- up(mgr1,proj12sched,read).
:- up(mgr1,proj11budget,setschedule).
:- up(des11,proj12budget,read).
:- up(ldr2,proj12task2propa,setcost).
:- up(acc2,proj12task1propa,request).
:- up(aud1,proj22task2propa,approve).
:- up(des11,proj22task1a,approve).
:- up(plan2,proj12task2propa,setcost).
:- up(acc2,proj22task2,request).
:- up(ldr2,proj12task1a,setstatus).
:- up(code12,proj12budget,setstatus).
:- up(des11,proj21task2,setstatus).
:- up(code11,proj22task1,write).
:- up(code11,proj22task2prop,setstatus).
:- up(code11,proj22task2propa,write).
:- up(plan1,proj22sched,write).
:- up(ldr2,proj11budget,approve).
:- up(aud1,proj22task1,request).
:- up(plan2,proj22budget,setcost).
:- up(code21,proj21task1a,approve).
:- up(code11,proj21task2,read).
:- up(ldr12,proj11task1prop,setschedule).
:- up(des21,proj11task1propa,setschedule).
:- up(acc1,proj12task2,approve).
:- up(code11,proj22task2propa,request).
:- up(plan2,proj21task1propa,write).
:- up(des22,proj12task1,read).
:- up(code22,proj12sched,request).
:- up(code12,proj11task1propa,request).
:- up(ldr12,proj22task2prop,setschedule).
:- up(aud2,proj11sched,setschedule).
:- up(code11,proj11task2a,setcost).
:- up(acc1,proj21task2a,request).
:- up(mgr1,proj11sched,read).
:- up(code11,proj21task2propa,setstatus).
:- up(plan1,proj12sched,request).
:- up(plan1,proj12task1prop,write).
:- up(mgr2,proj12task2prop,setcost).
:- up(ldr12,proj12task2prop,read).
:- up(mgr2,proj22task1prop,approve).
up(code12,proj12task2,read).
:- up(code11,proj12task2propa,setcost).
:- up(plan2,proj12task1,setcost).
up(mgr1,proj11budget,read).
:- up(des12,proj11task2,request).
:- up(plan1,proj22task2,request).
:- up(code21,proj21task1propa,approve).
:- up(plan1,proj21task1a,setschedule).
:- up(des11,proj21budget,setcost).
:- up(mgr1,proj12task1prop,read).
:- up(ldr2,proj12task2a,request).
:- up(ldr2,proj21sched,setcost).
:- up(acc2,proj12task1a,write).
:- up(aud2,proj12task1a,setcost).
:- up(mgr2,proj21task1,setschedule).
:- up(aud2,proj21task1a,request).
:- up(mgr1,proj11sched,setcost).
:- up(mgr2,proj22task1prop,write).
:- up(des12,proj21task1prop,approve).
:- up(ldr11,proj12task2prop,read).
:- up(des12,proj11task1a,setcost).
:- up(plan2,proj12task1propa,approve).
:- up(plan2,proj21task2propa,setcost).
:- up(code11,proj12task2prop,setcost).
:- up(aud2,proj11task2prop,request).
:- up(ldr11,proj22sched,approve).
:- up(code22,proj21budget,setschedule).
:- up(plan1,proj21task2propa,read).
:- up(mgr1,proj11task1a,request).
:- up(ldr12,proj12task1a,setcost).
:- up(code21,proj11task2a,setcost).
:- up(des12,proj11budget,request).
:- up(acc2,proj12budget,setcost).
:- up(mgr2,proj11task1propa,setstatus).
:- up(des12,proj21task2,read).
:- up(mgr2,proj12task1prop,setcost).
:- up(mgr1,proj21task2a,setschedule).
:- up(code11,proj21task2propa,read).
:- up(acc1,proj12task2propa,write).
:- up(code11,proj12budget,setschedule).
:- up(code21,proj11task2,setschedule).
:- up(mgr2,proj22task1,setstatus).
:- up(des11,proj11task2,setstatus).
:- up(des12,proj22task1propa,write).
:- up(des22,proj22budget,write).
:- up(ldr11,proj11task2prop,setcost).
:- up(code12,proj22sched,write).
:- up(des11,proj11task1a,write).
:- up(code21,proj12task1prop,approve).
:- up(mgr2,proj21task2propa,setstatus).
:- up(aud1,proj11budget,setstatus).
:- up(aud1,proj11task1,write).
:- up(code12,proj22sched,read).
:- up(ldr2,proj22task1propa,request).
:- up(des22,proj21task2,setschedule).
:- up(ldr2,proj12task2prop,request).
:- up(mgr2,proj21task1prop,setcost).
:- up(ldr11,proj22sched,setstatus).
:- up(aud1,proj21task1,write).
:- up(acc2,proj21task2prop,write).
:- up(ldr2,proj11task1,write).
:- up(acc2,proj21sched,setschedule).
:- up(ldr2,proj22task2prop,setcost).
:- up(des22,proj12task1propa,approve).
:- up(des12,proj11task2prop,approve).
:- up(mgr2,proj21budget,setcost).
:- up(des21,proj21task1propa,write).
:- up(ldr12,proj22task1propa,read).
:- up(aud1,proj21task1a,setcost).
:- up(aud2,proj22sched,approve).
:- up(des21,proj21task2prop,read).
:- up(code21,proj11task1a,setschedule).
:- up(plan1,proj12task2a,setstatus).
:- up(des21,proj11task1,setcost).
:- up(code22,proj12task1prop,read).
:- up(code21,proj11task1prop,request).
:- up(code21,proj21task1,approve).
:- up(code12,proj22task1propa,read).
:- up(mgr1,proj11task2a,setstatus).
:- up(code11,proj22task1a,setstatus).
:- up(code22,proj11task2propa,write).
:- up(des21,proj21sched,write).
up(plan2,proj21sched,read).
:- up(des22,proj11task2,setschedule).
:- up(des11,proj22task2a,approve).
:- up(code11,proj22task2propa,approve).
:- up(ldr2,proj11task2propa,read).
:- up(mgr1,proj21task1prop,read).
:- up(acc2,proj12task2prop,approve).
:- up(code22,proj11sched,setcost).
:- up(mgr2,proj22task2,setstatus).
:- up(des22,proj12budget,approve).
:- up(acc1,proj11task2,request).
:- up(plan1,proj21task2prop,setstatus).
:- up(des11,proj21task1prop,request).
:- up(plan2,proj11budget,write).
:- up(ldr2,proj21task2,request).
:- up(des12,proj21task1,write).
:- up(code12,proj11task2,approve).
:- up(code12,proj22task2,request).
:- up(aud2,proj21task2propa,setstatus).
:- up(des12,proj12budget,setcost).
up(plan1,proj12sched,read).
:- up(ldr11,proj21task2prop,read).
:- up(mgr1,proj21sched,request).
:- up(code12,proj21sched,approve).
:- up(code22,proj11task2prop,setstatus).
:- up(acc2,proj12task1,read).
:- up(plan2,proj22task2,write).
:- up(ldr11,proj22task1,setschedule).
up(ldr11,proj11sched,write).
:- up(code22,proj21task1propa,write).
:- up(des22,proj22task2prop,setcost).
:- up(aud2,proj22task1prop,setstatus).
:- up(des21,proj21task2propa,setcost).
:- up(plan2,proj12task1prop,approve).
:- up(mgr1,proj11task1propa,setschedule).
:- up(acc1,proj21task2,setstatus).
:- up(des22,proj21task1,write).
:- up(ldr12,proj22budget,read).
:- up(code12,proj21task2prop,write).
:- up(des21,proj22task2a,setstatus).
:- up(plan2,proj21task2prop,write).
:- up(plan2,proj12task2a,request).
:- up(des12,proj22task2prop,read).
:- up(acc2,proj11task1propa,setstatus).
up(des12,proj12task1propa,setstatus).
:- up(acc1,proj11task1a,setschedule).
:- up(acc1,proj11task1propa,read).
:- up(des11,proj22task2a,setcost).
:- up(acc1,proj12task2prop,setschedule).
up(code11,proj11sched,read).
:- up(aud1,proj11task2propa,setstatus).
:- up(code21,proj21task2a,setcost).
:- up(acc1,proj21task1a,setcost).
:- up(aud1,proj12task2a,request).
:- up(code12,proj22task1a,setcost).
:- up(mgr1,proj12task1,setcost).
:- up(ldr2,proj21task1a,read).
:- up(code22,proj22budget,write).
:- up(aud2,proj22budget,request).
:- up(des21,proj22task2,approve).
:- up(ldr12,proj22task2,approve).
:- up(ldr11,proj21task1prop,read).
:- up(code21,proj12task2a,setcost).
:- up(des22,proj12task2,setschedule).
:- up(code21,proj21task2propa,approve).
:- up(acc1,proj12task1a,approve).
:- up(acc2,proj21task1,setstatus).
:- up(des21,proj12task2a,approve).
:- up(ldr12,proj11task1propa,approve).
:- up(code11,proj12task2,read).
:- up(aud1,proj22task1propa,read).
:- up(code22,proj12task2,read).
:- up(des22,proj21task2propa,read).
:- up(code22,proj22task2propa,read).
:- up(aud1,proj21task2prop,setstatus).
:- up(mgr1,proj11sched,setschedule).
:- up(des11,proj11sched,request).
:- up(des11,proj11task2,request).
:- up(code22,proj22sched,setstatus).
:- up(des11,proj21task1,read).
:- up(code12,proj22task1a,approve).
:- up(aud1,proj11task2a,approve).
:- up(code22,proj11task1prop,request).
:- up(code22,proj22task2a,write).
:- up(aud2,proj22task2a,setstatus).
:- up(mgr2,proj12sched,setschedule).
:- up(des21,proj21task1prop,setcost).
:- up(ldr11,proj12task2prop,write).
:- up(acc2,proj21task2prop,request).
:- up(code22,proj12task1propa,setstatus).
:- up(des22,proj22task2prop,approve).
:- up(plan2,proj22task2propa,approve).
:- up(des22,proj12task1,request).
:- up(code21,proj21task2prop,write).
:- up(aud1,proj12task1propa,setschedule).
:- up(code12,proj11task1a,setcost).
:- up(mgr2,proj22task1propa,write).
:- up(plan2,proj11task2prop,request).
:- up(acc1,proj22task2,read).
:- up(code12,proj12task2,write).
:- up(acc1,proj22task1prop,approve).
:- up(plan1,proj22task1,request).
:- up(mgr1,proj21task1prop,setcost).
:- up(code12,proj12budget,write).
:- up(ldr2,proj11task1,setstatus).
:- up(ldr11,proj21budget,write).
:- up(code22,proj21sched,setcost).
:- up(des21,proj12task2propa,request).
:- up(mgr1,proj12budget,setcost).
up(ldr2,proj22budget,write).
:- up(code11,proj21task1a,request).
:- up(des11,proj21task1propa,setstatus).
:- up(plan1,proj12task1prop,request).
:- up(des11,proj11task2,read).
:- up(ldr11,proj11task2a,setstatus).
:- up(acc1,proj11task2propa,setschedule).
:- up(des21,proj12task1,write).
:- up(ldr2,proj21task2prop,approve).
:- up(ldr2,proj12task1a,setschedule).
:- up(des21,proj22task1a,setcost).
:- up(code11,proj12task1a,read).
up(code11,proj11task2propa,setstatus).
:- up(plan2,proj12task2a,setstatus).
:- up(acc1,proj11task2a,write).
:- up(des22,proj12task1propa,setstatus).
:- up(des22,proj21sched,request).
:- up(plan2,proj21task2,setcost).
:- up(des22,proj12task2a,setschedule).
:- up(ldr11,proj22task2propa,request).
:- up(ldr12,proj12budget,setcost).
:- up(aud1,proj22task1,approve).
:- up(mgr1,proj22task1a,write).
:- up(mgr1,proj22task2,write).
:- up(des21,proj21sched,approve).
:- up(ldr12,proj12task2a,request).
:- up(code11,proj12budget,setstatus).
:- up(acc1,proj12budget,request).
:- up(des21,proj11task2prop,write).
:- up(aud1,proj21task1,read).
:- up(ldr12,proj21task2a,setschedule).
:- up(plan1,proj12sched,setschedule).
:- up(code12,proj11task2,setcost).
:- up(ldr12,proj21budget,setschedule).
:- up(code21,proj12budget,setcost).
:- up(ldr12,proj22task2a,setcost).
:- up(code21,proj21task2prop,approve).
:- up(aud2,proj21budget,write).
:- up(des12,proj21task2prop,setschedule).
:- up(plan1,proj22sched,read).
:- up(mgr1,proj12task2prop,write).
:- up(des22,proj22budget,approve).
:- up(ldr12,proj12task2,read).
:- up(des11,proj22budget,setcost).
:- up(code11,proj21task2prop,write).
:- up(mgr2,proj11task2,setschedule).
up(des22,proj22task1,read).
up(code11,proj11task2prop,request).
:- up(des22,proj22task2,request).
:- up(des22,proj21task1a,read).
:- up(des21,proj22task2a,setcost).
:- up(aud1,proj22task2prop,write).
:- up(acc1,proj12task2,setschedule).
:- up(aud1,proj11budget,setcost).
:- up(des22,proj12sched,read).
:- up(acc2,proj12task1,setschedule).
:- up(code22,proj12task2,write).
:- up(code22,proj11task2propa,setschedule).
:- up(code12,proj11task2propa,setschedule).
:- up(aud1,proj12task2propa,setschedule).
:- up(mgr1,proj12task1propa,read).
:- up(des22,proj22task1prop,request).
:- up(des11,proj22task1a,request).
:- up(plan1,proj12task2a,read).
:- up(code11,proj21budget,setcost).
:- up(mgr2,proj11task1a,write).
:- up(des12,proj21task1propa,setstatus).
:- up(mgr1,proj22task2a,write).
:- up(plan1,proj22task1a,approve).
:- up(ldr12,proj22task1,write).
:- up(ldr12,proj21task2prop,write).
:- up(acc1,proj11task1prop,request).
:- up(des11,proj11task2propa,write).
:- up(ldr11,proj11task1,write).
:- up(ldr11,proj12budget,setcost).
:- up(ldr12,proj21task1prop,approve).
:- up(aud2,proj11sched,setstatus).
:- up(des12,proj21budget,write).
:- up(code12,proj11task1,read).
:- up(ldr11,proj12task1a,setschedule).
:- up(acc1,proj22task2a,approve).
:- up(acc2,proj21task1a,read).
:- up(acc1,proj21budget,setschedule).
:- up(mgr1,proj11task1,setcost).
:- up(plan1,proj12task2,setcost).
:- up(ldr2,proj21task2prop,setschedule).
:- up(plan2,proj11task2a,request).
:- up(des22,proj11task1prop,setstatus).
:- up(ldr11,proj12task1propa,write).
:- up(ldr11,proj11task2prop,setstatus).
:- up(mgr1,proj12task2a,setstatus).
:- up(aud1,proj11task2propa,approve).
:- up(code11,proj12task2propa,write).
:- up(plan2,proj11task1propa,setcost).
up(mgr1,proj12budget,read).
:- up(des22,proj22sched,write).
:- up(ldr2,proj11task2propa,setcost).
:- up(code21,proj21task1,read).
:- up(aud2,proj11task2propa,request).
up(des11,proj11task1a,request).
:- up(mgr2,proj11task1,setcost).
:- up(des11,proj12task2a,read).
:- up(des22,proj21task2a,setschedule).
:- up(acc2,proj12task2a,setstatus).
:- up(ldr12,proj12task1,setschedule).
:- up(code12,proj12task1prop,read).
:- up(aud1,proj21task1propa,setstatus).
:- up(acc2,proj12task2prop,setstatus).
:- up(des22,proj11task1,setschedule).
:- up(des22,proj11task1prop,approve).
:- up(aud2,proj11budget,read).
up(des21,proj21task1propa,read).
:- up(ldr12,proj11task1a,setschedule).
:- up(ldr2,proj12task2a,setcost).
:- up(plan2,proj21task2a,setcost).
:- up(code22,proj22budget,request).
:- up(des21,proj11task1prop,setstatus).
:- up(des22,proj21task2a,request).
:- up(des12,proj22task2propa,setstatus).
:- up(plan2,proj11task1a,setstatus).
:- up(des22,proj12task2,request).
:- up(ldr11,proj21task1,approve).
:- up(plan2,proj11task2a,read).
:- up(plan1,proj22task1propa,setschedule).
up(aud2,proj22budget,read).
:- up(mgr2,proj12budget,setschedule).
:- up(des21,proj21budget,write).
:- up(plan1,proj22task1prop,setcost).
:- up(code11,proj11task1a,approve).
:- up(plan1,proj11sched,setschedule).
:- up(code12,proj21task1propa,setcost).
:- up(code11,proj12budget,read).
:- up(code12,proj21task2a,request).
:- up(code12,proj22task2propa,read).
:- up(code12,proj11task2a,request).
:- up(plan1,proj21task1a,setcost).
:- up(des11,proj11task2prop,setschedule).
:- up(aud1,proj21budget,setschedule).
:- up(ldr12,proj12task1a,request).
:- up(mgr1,proj11task2,setschedule).
:- up(code12,proj21task1,request).
:- up(code12,proj21task1propa,read).
:- up(des22,proj22task1propa,setschedule).
:- up(acc2,proj22task2propa,read).
:- up(plan2,proj11task2a,setcost).
:- up(aud1,proj12task1propa,request).
:- up(acc1,proj21sched,setschedule).
:- up(aud1,proj21task2a,write).
:- up(code21,proj11task2prop,setschedule).
:- up(mgr1,proj12task2propa,request).
:- up(des21,proj21budget,setcost).
:- up(ldr12,proj21budget,request).
:- up(des12,proj21task1prop,request).
:- up(ldr11,proj12task1,setcost).
:- up(code11,proj12task2a,approve).
:- up(des22,proj11task1,request).
:- up(mgr1,proj21task1propa,setstatus).
:- up(des12,proj12task2propa,read).
:- up(code11,proj21task2a,setstatus).
:- up(mgr1,proj22task2a,request).
:- up(des22,proj11task2propa,setstatus).
:- up(des12,proj12budget,setschedule).
:- up(plan2,proj22task1,request).
:- up(mgr1,proj21task2a,write).
:- up(des11,proj22task1prop,request).
:- up(ldr11,proj11sched,setcost).
:- up(code21,proj12task2prop,setschedule).
:- up(des12,proj12task1propa,request).
:- up(plan1,proj11sched,approve).
:- up(acc1,proj22task1propa,write).
:- up(code12,proj11task1prop,setcost).
:- up(acc1,proj21task1prop,setstatus).
:- up(aud2,proj22task2propa,write).
:- up(aud1,proj12task2a,read).
:- up(plan2,proj22task1propa,request).
:- up(des21,proj12task2prop,write).
:- up(mgr2,proj12task2,write).
:- up(ldr12,proj22task2prop,read).
up(code12,proj12sched,read).
:- up(mgr2,proj12task2a,approve).
:- up(acc1,proj11task2,setschedule).
:- up(ldr12,proj21task2,write).
:- up(code12,proj21task2,write).
:- up(ldr12,proj12task2a,approve).
:- up(plan2,proj22task1a,read).
:- up(des22,proj22task1a,write).
:- up(des11,proj21sched,request).
:- up(des22,proj21task2prop,setschedule).
:- up(des12,proj22task2a,setcost).
:- up(aud2,proj12task2a,setschedule).
:- up(mgr2,proj22task1a,request).
:- up(ldr12,proj21task1,approve).
:- up(acc1,proj12task1,setschedule).
:- up(ldr12,proj11task1propa,setstatus).
:- up(code12,proj12task2a,setcost).
:- up(des11,proj12task2prop,request).
:- up(ldr2,proj21budget,setcost).
:- up(code12,proj11task2propa,read).
:- up(acc2,proj21task2prop,read).
:- up(des12,proj21task2a,setcost).
:- up(code21,proj11task2,request).
:- up(ldr12,proj21task1prop,setcost).
:- up(des21,proj21task1prop,setstatus).
:- up(ldr2,proj22task1a,approve).
:- up(ldr12,proj21task1a,read).
:- up(ldr2,proj11task2prop,setstatus).
:- up(code11,proj21sched,write).
:- up(plan2,proj22task1,read).
up(des21,proj21task1propa,setstatus).
:- up(aud2,proj22task1propa,setcost).
:- up(plan2,proj11task2a,approve).
:- up(code21,proj21sched,setcost).
:- up(ldr11,proj21task1a,write).
:- up(des12,proj21sched,setstatus).
:- up(acc1,proj21sched,write).
:- up(code22,proj11task1,write).
:- up(ldr11,proj21budget,read).
:- up(code22,proj22budget,setschedule).
:- up(ldr12,proj21task1propa,setstatus).
:- up(des21,proj12task1propa,setstatus).
:- up(aud2,proj21task2,setschedule).
:- up(code22,proj11task1a,approve).
:- up(plan1,proj22task2,setstatus).
:- up(des21,proj11task1propa,setcost).
:- up(mgr1,proj11task1prop,setschedule).
:- up(code11,proj21task1a,setcost).
:- up(des21,proj12task1prop,approve).
:- up(code11,proj11task1propa,approve).
:- up(code11,proj11task2prop,write).
:- up(mgr1,proj21task1,setcost).
:- up(des11,proj22task2propa,request).
:- up(plan1,proj22task1propa,approve).
:- up(code12,proj12task2propa,setschedule).
:- up(mgr2,proj22sched,approve).
:- up(des12,proj22task1prop,setschedule).
:- up(ldr12,proj21sched,request).
:- up(plan1,proj21task2,setcost).
:- up(code11,proj22sched,setstatus).
:- up(acc2,proj22task1propa,request).
up(acc2,proj22budget,read).
:- up(aud1,proj22task1propa,request).
:- up(code11,proj21budget,setschedule).
:- up(acc1,proj22task2a,setcost).
:- up(ldr2,proj22task2propa,setschedule).
:- up(plan2,proj11sched,read).
:- up(acc1,proj22task2prop,read).
:- up(acc2,proj11task2propa,approve).
:- up(aud1,proj21task1a,approve).
:- up(code21,proj22task1,setcost).
:- up(ldr2,proj11sched,read).
:- up(des22,proj12task2propa,request).
:- up(ldr12,proj21task2,read).
:- up(ldr2,proj22task2a,request).
:- up(aud1,proj22task2,setcost).
:- up(code11,proj21sched,setcost).
:- up(des12,proj11task1a,write).
:- up(mgr1,proj21task1propa,setcost).
:- up(mgr2,proj11budget,write).
:- up(code11,proj11sched,setstatus).
:- up(acc2,proj22sched,setstatus).
:- up(plan2,proj11task2,setschedule).
:- up(des21,proj11task2a,setcost).
:- up(aud1,proj21task1prop,setcost).
:- up(des22,proj11task2propa,setcost).
:- up(des11,proj22task1,approve).
:- up(des12,proj22task2a,read).
:- up(ldr2,proj21task1a,write).
:- up(acc2,proj12task2a,approve).
:- up(ldr11,proj21sched,setcost).
:- up(aud2,proj11task1prop,setcost).
:- up(ldr12,proj11task1a,read).
:- up(plan2,proj22task2a,setstatus).
:- up(aud1,proj21budget,read).
:- up(acc2,proj12task1propa,write).
:- up(des12,proj12task1,setcost).
:- up(acc1,proj12task1propa,write).
:- up(des21,proj11task1propa,request).
:- up(code22,proj21sched,approve).
:- up(plan1,proj11task2propa,read).
:- up(plan1,proj21task1propa,setcost).
:- up(aud2,proj22task1propa,request).
up(code21,proj21task2,read).
:- up(aud1,proj21task1a,setstatus).
:- up(code12,proj21task2a,write).
:- up(acc1,proj22budget,setstatus).
:- up(aud1,proj21task1,setschedule).
:- up(acc1,proj21budget,setcost).
:- up(code11,proj22task2a,approve).
:- up(des22,proj21task1,request).
:- up(ldr2,proj21task2propa,request).
:- up(code21,proj11task2propa,request).
:- up(acc2,proj12task2propa,setstatus).
up(acc1,proj11budget,write).
:- up(acc2,proj12task1a,approve).
:- up(acc1,proj12task2propa,setschedule).
:- up(code11,proj12task2a,request).
:- up(aud2,proj21task2,setstatus).
:- up(aud2,proj21task2a,read).
:- up(des21,proj11task1,request).
:- up(ldr12,proj22sched,approve).
:- up(code22,proj21task2,request).
:- up(code22,proj12task1a,request).
:- up(ldr12,proj21task1propa,setcost).
:- up(acc1,proj21task2prop,setcost).
:- up(des12,proj11task1propa,setschedule).
:- up(ldr12,proj21task1,setstatus).
:- up(des11,proj12task2prop,write).
:- up(des12,proj12task1prop,setschedule).
:- up(code21,proj21task1a,setstatus).
:- up(ldr2,proj22task1propa,setcost).
:- up(code12,proj12task1propa,write).
:- up(code22,proj22task2propa,setcost).
:- up(code12,proj12task1prop,setcost).
up(code11,proj11task2prop,read).
:- up(code22,proj11sched,setstatus).
:- up(des22,proj11task2propa,setschedule).
:- up(ldr2,proj12sched,write).
:- up(aud1,proj12task2,setcost).
:- up(mgr1,proj12task1a,setschedule).
:- up(aud2,proj22task2a,setcost).
:- up(des22,proj12task1propa,read).
:- up(code12,proj22task1prop,approve).
up(acc1,proj12task1,setcost).
:- up(des12,proj12task1propa,setschedule).
:- up(des12,proj11task2a,setstatus).
:- up(code12,proj12task2prop,setschedule).
:- up(ldr11,proj11budget,setstatus).
:- up(aud1,proj21task1,setcost).
:- up(plan2,proj22task2,approve).
:- up(code11,proj22task1a,request).
:- up(des12,proj12task2a,approve).
:- up(acc1,proj22task1propa,setcost).
:- up(des12,proj22task1,read).
:- up(aud1,proj12task1,write).
:- up(aud2,proj21task1prop,approve).
:- up(des22,proj12task2propa,approve).
:- up(code22,proj12task2prop,approve).
:- up(des11,proj12task2prop,setstatus).
:- up(plan1,proj22task1propa,setcost).
:- up(aud2,proj12task2a,setstatus).
:- up(ldr11,proj12task1,write).
:- up(ldr12,proj12task2propa,setstatus).
:- up(acc2,proj21task1a,approve).
:- up(des12,proj21task1,setcost).
:- up(des22,proj21sched,setcost).
:- up(ldr12,proj21task1propa,approve).
:- up(code12,proj21task1propa,setstatus).
:- up(plan1,proj22budget,write).
:- up(ldr11,proj22task2prop,request).
:- up(des22,proj22sched,setcost).
:- up(plan2,proj11task1prop,request).
:- up(des12,proj22task1prop,request).
:- up(des21,proj21budget,request).
:- up(plan1,proj12task2,write).
:- up(des21,proj21task2a,write).
:- up(ldr11,proj22task2,write).
:- up(mgr2,proj21task1prop,write).
:- up(aud2,proj22task2,write).
:- up(des12,proj22budget,write).
:- up(aud1,proj21task2a,setstatus).
:- up(acc2,proj12task1prop,approve).
:- up(acc1,proj12task1prop,write).
:- up(des22,proj11task2prop,setschedule).
:- up(ldr12,proj21task2prop,setcost).
:- up(plan2,proj21task1,approve).
:- up(code11,proj22task1prop,request).
:- up(des21,proj12sched,approve).
:- up(ldr11,proj12task2prop,approve).
:- up(des11,proj12task1propa,setschedule).
:- up(code22,proj22task2propa,request).
:- up(mgr1,proj11task1propa,setcost).
:- up(ldr12,proj12task1a,setschedule).
:- up(acc2,proj21task1,approve).
:- up(des22,proj12task1prop,approve).
:- up(aud1,proj21budget,approve).
:- up(des21,proj12task1,approve).
:- up(code12,proj12sched,approve).
:- up(plan1,proj21task2prop,write).
:- up(mgr2,proj22task2a,setcost).
:- up(ldr11,proj22task1prop,setcost).
:- up(code22,proj21task2,read).
:- up(code22,proj21task1prop,setstatus).
:- up(plan2,proj12task1,approve).
:- up(code12,proj22task2a,setcost).
:- up(ldr2,proj21task1,setcost).
:- up(code22,proj22task1propa,setcost).
:- up(aud1,proj11sched,setschedule).
:- up(ldr11,proj21task2propa,write).
:- up(aud2,proj11budget,setcost).
:- up(mgr2,proj22task2prop,setcost).
:- up(des22,proj12task1prop,read).
:- up(acc1,proj21task2,setschedule).
:- up(ldr12,proj12task1prop,setcost).
:- up(code21,proj22task1prop,read).
:- up(code22,proj22task2a,setschedule).
:- up(acc1,proj21task1propa,setstatus).
:- up(mgr1,proj22task1propa,request).
up(acc2,proj22task2a,setcost).
:- up(mgr2,proj22task1propa,setstatus).
:- up(ldr12,proj11task1a,setstatus).
:- up(des11,proj21task2prop,read).
:- up(plan2,proj22task1a,approve).
:- up(mgr2,proj12task2prop,write).
:- up(acc2,proj11task1prop,setschedule).
:- up(des11,proj12task2a,setschedule).
:- up(code22,proj21task2prop,write).
:- up(acc2,proj12task1,write).
:- up(des11,proj22budget,setstatus).
:- up(mgr1,proj11task2a,write).
:- up(des21,proj11task1propa,setstatus).
:- up(aud2,proj21task2a,approve).
:- up(code21,proj22task1a,setstatus).
:- up(ldr2,proj21task1,request).
:- up(ldr12,proj21task2,setcost).
:- up(code11,proj12task1prop,write).
:- up(plan1,proj21sched,read).
:- up(code22,proj21task2,write).
:- up(aud2,proj21task1,setstatus).
:- up(mgr1,proj21task2propa,approve).
:- up(aud1,proj21task2propa,setschedule).
:- up(code22,proj12task2propa,setcost).
up(des21,proj21task1propa,request).
:- up(code22,proj21task1a,write).
up(des11,proj11task1propa,request).
:- up(ldr11,proj21task2,approve).
:- up(plan2,proj22task1prop,request).
:- up(des12,proj21sched,approve).
:- up(acc1,proj11task1a,approve).
:- up(code12,proj12task1prop,write).
:- up(code21,proj21task1prop,write).
up(acc2,proj21task2a,setcost).
:- up(code22,proj22budget,setcost).
:- up(des22,proj11task1a,read).
:- up(code12,proj21task2,setcost).
:- up(code11,proj21task2prop,read).
:- up(des12,proj21task1a,setstatus).
:- up(des21,proj11task1,read).
:- up(plan1,proj12task2prop,read).
:- up(mgr1,proj11task1a,setcost).
:- up(ldr12,proj12task2prop,approve).
:- up(des12,proj11task2prop,write).
:- up(aud1,proj12task1prop,approve).
up(plan1,proj11sched,write).
:- up(ldr12,proj22sched,read).
:- up(ldr2,proj22task2a,read).
:- up(ldr11,proj21sched,read).
:- up(plan1,proj22task2,setcost).
:- up(des11,proj21task1prop,approve).
:- up(ldr11,proj21task1a,setcost).
:- up(aud1,proj21task1propa,request).
:- up(ldr12,proj12budget,approve).
:- up(mgr2,proj12task1propa,write).
:- up(code21,proj12task1a,approve).
:- up(des12,proj11task1propa,write).
:- up(plan1,proj22task2prop,setstatus).
:- up(plan1,proj22task2propa,setstatus).
:- up(des12,proj22task1a,setstatus).
:- up(des21,proj12task2,request).
:- up(aud2,proj12task2,setschedule).
:- up(mgr1,proj21budget,setschedule).
:- up(mgr2,proj22task1propa,setcost).
:- up(des12,proj12task2,setcost).
:- up(plan1,proj22task2a,write).
:- up(acc1,proj12task1prop,read).
:- up(des12,proj12sched,setschedule).
:- up(mgr2,proj21task1a,setcost).
:- up(des12,proj21task1,approve).
up(plan2,proj22task2a,setschedule).
:- up(des11,proj21sched,write).
:- up(code11,proj22task2a,write).
:- up(plan1,proj11task2propa,write).
:- up(plan2,proj22task2prop,setstatus).
:- up(des21,proj22task1a,setschedule).
:- up(mgr2,proj11task2propa,write).
:- up(plan1,proj22sched,request).
:- up(des11,proj22task2prop,setstatus).
:- up(des21,proj12task2propa,setschedule).
:- up(code11,proj11task2propa,approve).
:- up(plan2,proj12budget,read).
:- up(ldr11,proj12task2a,request).
:- up(mgr1,proj22task1,approve).
:- up(des22,proj22task2a,setstatus).
up(plan1,proj11task1,setschedule).
:- up(des21,proj22task1propa,setstatus).
:- up(ldr12,proj22task1,read).
:- up(code21,proj22task1propa,read).
:- up(code21,proj11task2,read).
:- up(plan1,proj12budget,setcost).
:- up(des11,proj11task2propa,setcost).
:- up(ldr12,proj22task1propa,request).
:- up(code21,proj12task1a,setcost).
:- up(des11,proj12sched,setcost).
:- up(mgr2,proj22task2,setcost).
:- up(des11,proj12task1a,setstatus).
:- up(code11,proj11task1propa,setcost).
:- up(plan2,proj11task1a,setcost).
:- up(mgr2,proj21task2prop,setschedule).
:- up(acc2,proj22sched,approve).
:- up(code22,proj21task1a,setschedule).
:- up(aud1,proj12task1prop,read).
:- up(plan1,proj22task2propa,approve).
:- up(code22,proj21task2,setschedule).
:- up(code21,proj22budget,request).
up(des12,proj12task1a,setstatus).
:- up(acc2,proj21task1prop,write).
:- up(ldr2,proj22task1propa,read).
:- up(des11,proj12task2,read).
:- up(mgr2,proj11sched,read).
:- up(plan2,proj21task2,approve).
:- up(code12,proj22task2propa,setcost).
:- up(ldr12,proj12task1propa,request).
:- up(plan1,proj21task2propa,write).
:- up(mgr2,proj11task1propa,write).
:- up(code11,proj22task1,setcost).
:- up(des12,proj11budget,read).
:- up(code11,proj21budget,read).
:- up(ldr12,proj21budget,read).
:- up(code12,proj12task1,read).
:- up(aud2,proj21task1propa,request).
:- up(aud1,proj22task2propa,request).
:- up(acc1,proj22task2propa,setcost).
:- up(code12,proj11task1prop,read).
:- up(code22,proj22budget,approve).
:- up(des11,proj11sched,setstatus).
:- up(des21,proj12task1a,approve).
up(des22,proj22task1a,read).
:- up(aud2,proj22sched,setstatus).
:- up(des22,proj22task2propa,setstatus).
:- up(plan1,proj21task2propa,setschedule).
:- up(mgr2,proj21budget,setstatus).
:- up(ldr2,proj11task1,setschedule).
:- up(aud1,proj11task2a,request).
:- up(acc2,proj11task1,write).
:- up(aud2,proj21budget,setschedule).
:- up(ldr2,proj11task1a,setcost).
:- up(code12,proj12task2propa,approve).
:- up(code11,proj21task1prop,setcost).
:- up(code12,proj12task2,setstatus).
:- up(acc2,proj22task1,approve).
:- up(des21,proj22task1a,read).
:- up(ldr12,proj22task2propa,setcost).
:- up(des21,proj11task1prop,setschedule).
:- up(des11,proj21task2,request).
:- up(aud1,proj22task2propa,setschedule).
:- up(aud1,proj22task1propa,setstatus).
:- up(ldr11,proj12task2,approve).
:- up(des21,proj12task2propa,write).
:- up(acc1,proj22budget,setcost).
:- up(acc2,proj11task2prop,setstatus).
:- up(des21,proj11sched,approve).
:- up(des11,proj22task1propa,request).
:- up(ldr11,proj22task1prop,setstatus).
:- up(mgr2,proj12task2propa,setschedule).
:- up(code12,proj12task1a,setschedule).
:- up(mgr2,proj21task2prop,approve).
:- up(aud1,proj11task1prop,request).
:- up(code11,proj21task2a,setcost).
:- up(ldr11,proj22task2,setstatus).
:- up(code22,proj21sched,setschedule).
:- up(plan2,proj12task1propa,setschedule).
:- up(ldr12,proj12task2a,setstatus).
:- up(code12,proj11task1,setcost).
:- up(mgr2,proj11task1prop,setcost).
:- up(aud1,proj21task2,write).
:- up(des11,proj22sched,write).
:- up(des21,proj21task2prop,setcost).
:- up(des22,proj12sched,approve).
:- up(des21,proj22budget,read).
:- up(mgr1,proj21task2,write).
up(plan2,proj22task2prop,setschedule).
:- up(mgr1,proj11task2,read).
:- up(code21,proj11task1,read).
:- up(acc2,proj11task1a,setcost).
:- up(ldr2,proj11task2prop,setcost).
:- up(code12,proj11task1prop,write).
:- up(ldr11,proj11task1,approve).
:- up(plan2,proj12task2propa,setschedule).
:- up(code11,proj21task2propa,write).
:- up(acc1,proj21task2propa,approve).
:- up(mgr2,proj12budget,approve).
:- up(des22,proj21task1a,write).
:- up(des21,proj22task1,setschedule).
:- up(plan2,proj12task1propa,setstatus).
:- up(des11,proj12task2a,write).
:- up(des11,proj22task2,read).
:- up(ldr11,proj12budget,setschedule).
:- up(plan2,proj12task1,read).
:- up(ldr2,proj22task2propa,setcost).
:- up(mgr2,proj12task2propa,setstatus).
:- up(code22,proj21task1propa,setschedule).
:- up(mgr1,proj12budget,setschedule).
:- up(plan2,proj12task2prop,setstatus).
:- up(ldr12,proj11task1,setstatus).
:- up(code22,proj22sched,setschedule).
:- up(code11,proj11task1,setschedule).
:- up(des11,proj12task1prop,write).
:- up(acc1,proj22task1,read).
:- up(plan1,proj21task1propa,setschedule).
:- up(ldr2,proj12sched,approve).
:- up(acc2,proj12task2propa,setschedule).
:- up(des12,proj21task2propa,write).
:- up(code12,proj22task1,request).
:- up(aud2,proj21task2propa,write).
:- up(ldr2,proj22budget,setcost).
:- up(code21,proj21sched,request).
:- up(acc1,proj22task2a,write).
:- up(code21,proj21sched,approve).
:- up(aud1,proj11task2propa,request).
:- up(mgr2,proj21task2propa,setcost).
:- up(code12,proj22task1propa,request).
:- up(acc2,proj12budget,approve).
:- up(des22,proj21task1prop,setschedule).
:- up(code11,proj22task1,setstatus).
:- up(ldr11,proj11task1propa,read).
:- up(des21,proj12task1,setschedule).
:- up(code11,proj11task2propa,write).
:- up(des12,proj12task1a,write).
:- up(code22,proj22task2a,setcost).
:- up(mgr2,proj12task2propa,read).
:- up(des22,proj22task2a,request).
up(code22,proj22task2a,request).
:- up(des11,proj22task1prop,write).
:- up(ldr2,proj11task2a,read).
:- up(des12,proj11task1,setcost).
:- up(ldr12,proj21task2a,setstatus).
:- up(acc1,proj21task1a,read).
:- up(plan2,proj11task1a,read).
:- up(mgr1,proj12task1prop,setschedule).
:- up(ldr2,proj21budget,approve).
:- up(aud2,proj22task2prop,setstatus).
:- up(des11,proj22task2prop,request).
:- up(des12,proj22budget,read).
:- up(des22,proj11task2prop,request).
:- up(acc1,proj21task2a,write).
:- up(des22,proj12task2propa,setstatus).
up(ldr12,proj12sched,write).
:- up(code22,proj11budget,setschedule).
:- up(plan1,proj11task1,request).
:- up(plan2,proj11budget,setschedule).
:- up(code11,proj21task1a,setschedule).
:- up(aud2,proj11budget,write).
:- up(code21,proj21task1prop,read).
:- up(code22,proj21task1propa,setstatus).
:- up(code21,proj12task2propa,request).
:- up(code12,proj21task1prop,request).
:- up(ldr2,proj22task2propa,approve).
:- up(ldr12,proj21task2propa,write).
:- up(mgr1,proj11task1a,approve).
:- up(mgr2,proj22task2,approve).
:- up(acc2,proj22task2a,setstatus).
:- up(acc1,proj22task1,write).
:- up(ldr2,proj12task2prop,approve).
:- up(code22,proj12sched,approve).
:- up(code21,proj12task2a,read).
:- up(plan1,proj11task2a,approve).
:- up(ldr11,proj11budget,setschedule).
:- up(acc1,proj12task1propa,setschedule).
:- up(ldr12,proj22task2prop,write).
:- up(acc1,proj22task1prop,request).
:- up(acc2,proj12task2propa,request).
:- up(des11,proj11task2propa,request).
:- up(des12,proj22task1a,setschedule).
:- up(des12,proj22task2a,setschedule).
up(aud2,proj21sched,read).
:- up(code21,proj22task2prop,read).
:- up(aud1,proj22task1,setschedule).
:- up(mgr1,proj22sched,setcost).
:- up(plan2,proj12task2prop,request).
:- up(ldr11,proj11task1propa,approve).
:- up(des12,proj22task1,write).
:- up(plan2,proj21task2a,read).
:- up(code11,proj22task1prop,setschedule).
:- up(acc1,proj22task2propa,write).
:- up(des12,proj11task2propa,setstatus).
:- up(ldr11,proj12task1,request).
:- up(des21,proj21task2,setschedule).
up(code12,proj12task2propa,setstatus).
:- up(ldr2,proj12task2prop,setcost).
:- up(ldr2,proj12task2,approve).
:- up(ldr11,proj12task1,read).
:- up(mgr2,proj11task1propa,read).
:- up(acc1,proj22sched,approve).
:- up(plan2,proj11task1,setcost).
:- up(code21,proj22task1propa,request).
:- up(plan1,proj21task2prop,setschedule).
:- up(plan2,proj22task2propa,request).
:- up(mgr1,proj22task2prop,write).
:- up(mgr1,proj21task2prop,setcost).
:- up(mgr1,proj12sched,approve).
:- up(aud2,proj21task2prop,write).
:- up(ldr2,proj21budget,setstatus).
:- up(ldr11,proj21task1propa,setschedule).
:- up(ldr11,proj11task2prop,approve).
:- up(mgr1,proj21task1prop,approve).
:- up(aud1,proj22task1a,setschedule).
:- up(ldr11,proj12task2a,setcost).
:- up(code11,proj12task2prop,read).
:- up(code22,proj12task2a,approve).
:- up(ldr12,proj11budget,approve).
:- up(code22,proj21task1,read).
:- up(ldr2,proj11task1,request).
:- up(des12,proj21task1a,setschedule).
:- up(code12,proj22task2,write).
:- up(des22,proj11task1propa,setcost).
:- up(aud1,proj11task2,setcost).
:- up(code11,proj11task2a,write).
:- up(ldr12,proj12task1prop,read).
:- up(des11,proj12task1propa,setstatus).
:- up(des21,proj21task1a,setschedule).
:- up(code21,proj22task1a,setschedule).
:- up(ldr2,proj21task1a,setcost).
:- up(ldr12,proj21task1propa,write).
:- up(aud1,proj22task1propa,approve).
:- up(des21,proj22task2a,approve).
:- up(ldr12,proj12task1,setstatus).
:- up(acc1,proj21task2prop,read).
:- up(plan2,proj22task1a,request).
:- up(des12,proj22task1propa,setstatus).
:- up(des22,proj12task2a,setstatus).
:- up(aud2,proj12task1,approve).
:- up(aud2,proj12task1a,request).
up(plan1,proj12task1,setschedule).
:- up(code11,proj11task1a,setschedule).
:- up(aud2,proj21task2,request).
up(aud1,proj11sched,read).
:- up(des22,proj21task1propa,request).
:- up(code22,proj11task1propa,setcost).
:- up(aud2,proj12task2prop,setstatus).
:- up(plan1,proj11task2,setstatus).
:- up(ldr12,proj11task1propa,setcost).
:- up(code22,proj12task2,setstatus).
:- up(acc2,proj22task1prop,approve).
:- up(acc1,proj21budget,read).
:- up(acc2,proj21task2propa,setstatus).
up(acc1,proj11task2a,setcost).
:- up(code21,proj22task2,setschedule).
:- up(ldr12,proj22task1,request).
:- up(code12,proj22task2propa,request).
:- up(mgr2,proj12task1,write).
:- up(code11,proj11task2propa,setschedule).
:- up(plan2,proj22task1prop,read).
:- up(des22,proj21task1a,request).
:- up(code12,proj21budget,request).
:- up(mgr1,proj11task2prop,setstatus).
:- up(ldr11,proj21task2prop,setstatus).
:- up(code21,proj11sched,request).
:- up(code22,proj12task2a,write).
:- up(acc2,proj12task1propa,setschedule).
:- up(des12,proj12task1prop,write).
:- up(acc1,proj21task1prop,read).
:- up(mgr2,proj12task1,approve).
:- up(acc1,proj11task2,write).
:- up(des12,proj11task2,approve).
:- up(plan2,proj12task2,setstatus).
:- up(code11,proj22budget,setschedule).
:- up(acc1,proj21task1a,setstatus).
:- up(des21,proj12task2propa,setstatus).
:- up(acc2,proj12sched,write).
:- up(plan2,proj21task2a,setstatus).
:- up(mgr1,proj21task2prop,approve).
:- up(code11,proj11task1,read).
:- up(code22,proj11task2a,write).
:- up(code22,proj12task2propa,setstatus).
:- up(plan2,proj21budget,read).
:- up(des22,proj21task1prop,write).
:- up(plan1,proj21task1a,request).
:- up(mgr1,proj22task2a,setcost).
:- up(des11,proj11task1prop,setcost).
:- up(des12,proj11task2a,approve).
:- up(code11,proj22task2a,request).
:- up(des22,proj22task1prop,approve).
:- up(code11,proj21budget,setstatus).
:- up(ldr2,proj22task2,setcost).
:- up(des12,proj11task2propa,write).
:- up(des21,proj22task1prop,write).
:- up(des22,proj12sched,write).
:- up(code22,proj22sched,request).
:- up(mgr1,proj22task1a,setstatus).
:- up(plan1,proj12sched,approve).
:- up(plan1,proj11task1prop,setstatus).
:- up(mgr2,proj12task1a,read).
:- up(acc2,proj11task2a,setcost).
:- up(aud2,proj12task2prop,setschedule).
:- up(code11,proj22sched,write).
:- up(code11,proj11task1a,setstatus).
:- up(des22,proj12task2,write).
:- up(plan1,proj12sched,setstatus).
:- up(ldr11,proj12task2a,approve).
:- up(mgr2,proj12task1propa,setcost).
:- up(mgr1,proj12sched,request).
:- up(acc1,proj11task1propa,write).
:- up(code11,proj12task1a,write).
:- up(mgr1,proj22task1a,setschedule).
:- up(ldr11,proj21task1prop,setschedule).
:- up(des11,proj22task2,write).
:- up(plan2,proj12task1,request).
:- up(code11,proj12task2,write).
:- up(plan2,proj11task2propa,setcost).
:- up(code12,proj21task2propa,read).
:- up(des12,proj11task2a,write).
:- up(plan2,proj12task2a,write).
:- up(code22,proj12task2a,setschedule).
:- up(des21,proj21task1prop,write).
:- up(plan1,proj22sched,approve).
:- up(des11,proj11task1prop,approve).
:- up(ldr12,proj11task1,read).
:- up(des12,proj21task1a,read).
:- up(mgr1,proj12task1prop,setstatus).
:- up(mgr2,proj11task1prop,read).
:- up(code11,proj22task1propa,approve).
:- up(aud2,proj21task2propa,read).
:- up(mgr1,proj11task1,setstatus).
:- up(mgr2,proj12task2prop,read).
:- up(code22,proj22task2prop,read).
:- up(ldr2,proj21task1prop,approve).
:- up(code21,proj22task1prop,setcost).
:- up(code12,proj22budget,write).
:- up(des12,proj12task1propa,setcost).
:- up(ldr2,proj21task1propa,setschedule).
:- up(mgr2,proj12sched,setstatus).
:- up(des21,proj22task2prop,setschedule).
:- up(code12,proj21task2prop,setcost).
:- up(ldr12,proj11task2prop,approve).
:- up(acc2,proj22task1,setstatus).
:- up(acc1,proj22task1propa,setstatus).
up(aud2,proj22sched,read).
:- up(code11,proj11task1propa,setschedule).
:- up(code22,proj12task1a,write).
:- up(code11,proj21task1,read).
:- up(des11,proj22budget,read).
:- up(ldr2,proj22sched,setcost).
:- up(acc2,proj12sched,setstatus).
:- up(des12,proj21task2a,approve).
:- up(des12,proj21task1propa,setcost).
:- up(des21,proj12task1propa,setschedule).
:- up(des22,proj11sched,approve).
up(ldr2,proj22sched,read).
:- up(mgr1,proj22task2propa,approve).
:- up(code11,proj21task2,setcost).
:- up(aud1,proj21task2,read).
:- up(des12,proj22task2propa,approve).
:- up(aud2,proj12task1a,setschedule).
:- up(ldr11,proj22task2a,read).
:- up(code21,proj12budget,approve).
:- up(acc2,proj11task2prop,setcost).
:- up(code11,proj21budget,request).
:- up(ldr2,proj22task1prop,read).
:- up(des21,proj21task1prop,approve).
:- up(code11,proj11task1propa,setstatus).
:- up(code11,proj12sched,approve).
:- up(code12,proj12task1a,request).
:- up(acc1,proj11task1,read).
:- up(ldr11,proj22sched,setcost).
:- up(des21,proj11task1propa,read).
:- up(acc2,proj11task1prop,approve).
:- up(code12,proj21budget,approve).
:- up(ldr12,proj22task1,setcost).
:- up(aud1,proj21sched,approve).
:- up(acc1,proj12task2,write).
:- up(ldr2,proj22task2a,setstatus).
up(des11,proj11task1a,setstatus).
:- up(aud1,proj11task1propa,read).
:- up(des11,proj21task2,setcost).
:- up(plan1,proj21task2prop,read).
:- up(code11,proj22task2,approve).
:- up(acc1,proj22task1,setcost).
:- up(des21,proj11task2propa,approve).
:- up(ldr11,proj21task1a,request).
:- up(acc1,proj22task2a,request).
:- up(des11,proj12budget,approve).
:- up(ldr11,proj21task2a,write).
:- up(aud2,proj12task2propa,request).
:- up(acc1,proj22task1propa,approve).
:- up(des11,proj22task1,setstatus).
:- up(aud2,proj11task2a,request).
:- up(mgr2,proj21task1a,request).
:- up(acc2,proj21sched,setstatus).
:- up(mgr1,proj11sched,request).
:- up(plan1,proj12task1prop,setstatus).
up(ldr12,proj12sched,read).
:- up(acc1,proj11task2a,read).
:- up(des12,proj22task2a,write).
:- up(des11,proj11task1propa,approve).
:- up(code12,proj21task2,approve).
:- up(code12,proj11budget,setcost).
:- up(acc2,proj12task1a,setschedule).
:- up(des11,proj11task2propa,read).
:- up(code21,proj11task1,request).
:- up(ldr11,proj21sched,write).
:- up(ldr11,proj21sched,setschedule).
:- up(code11,proj11budget,write).
:- up(plan2,proj12task2prop,read).
:- up(des11,proj12task2propa,approve).
:- up(des12,proj22task2propa,write).
:- up(code12,proj11task2prop,setschedule).
:- up(mgr1,proj22task1prop,setstatus).
:- up(code12,proj11task1propa,write).
:- up(des12,proj12task2prop,read).
:- up(code11,proj22sched,request).
:- up(des22,proj12task2propa,setcost).
:- up(ldr12,proj12task2propa,setschedule).
:- up(ldr11,proj21task2prop,setschedule).
:- up(mgr2,proj21task2prop,write).
:- up(mgr1,proj22task1,write).
:- up(ldr12,proj22task1a,request).
:- up(code21,proj22task1prop,write).
:- up(des21,proj22task2a,write).
:- up(code21,proj21task1prop,request).
:- up(mgr2,proj21task2a,request).
:- up(mgr1,proj11task2,setstatus).
:- up(plan1,proj11task1a,request).
:- up(acc1,proj21task2prop,setstatus).
:- up(mgr2,proj11task1a,approve).
:- up(plan1,proj21task2,read).
:- up(des11,proj21task2,write).
:- up(ldr12,proj22task2a,setschedule).
:- up(des22,proj22task2prop,write).
:- up(plan2,proj21task2a,request).
:- up(aud2,proj11task1propa,approve).
:- up(plan2,proj11task2prop,setcost).
:- up(mgr1,proj12task2a,write).
:- up(ldr2,proj21task1,setstatus).
:- up(acc1,proj21task2,request).
:- up(acc2,proj22task1prop,read).
:- up(des12,proj21task1,setschedule).
:- up(des21,proj11task1,approve).
:- up(aud2,proj12task1propa,approve).
:- up(code21,proj11task1a,request).
:- up(des12,proj11task1prop,setcost).
:- up(ldr12,proj21task2propa,setstatus).
:- up(des12,proj22task2propa,setschedule).
:- up(plan1,proj22task2a,approve).
:- up(code21,proj22task1prop,request).
:- up(code12,proj11task1a,write).
:- up(acc2,proj12budget,request).
up(acc1,proj11task1,setcost).
:- up(des12,proj12task2prop,request).
:- up(des21,proj11task2,setcost).
:- up(code11,proj11task1prop,setstatus).
:- up(ldr11,proj21task1,setschedule).
:- up(code22,proj12task2prop,request).
:- up(code11,proj22task2prop,approve).
up(code11,proj11task2,request).
:- up(ldr2,proj12task1a,setcost).
:- up(mgr1,proj21task1a,setcost).
:- up(aud1,proj11task1a,setschedule).
:- up(des12,proj22task1propa,read).
:- up(ldr12,proj11task1propa,write).
:- up(des21,proj12task1prop,request).
:- up(ldr11,proj12task1prop,setstatus).
:- up(plan1,proj11task2,write).
:- up(code22,proj12task2propa,write).
:- up(plan2,proj22budget,read).
up(acc1,proj12budget,write).
:- up(plan1,proj22budget,approve).
:- up(aud1,proj12task1a,setschedule).
:- up(des12,proj22task1prop,write).
:- up(acc1,proj21task2prop,write).
:- up(des21,proj12budget,request).
:- up(code12,proj21task2prop,read).
:- up(aud2,proj22budget,write).
:- up(ldr2,proj11task1prop,setschedule).
:- up(aud1,proj12budget,setstatus).
:- up(ldr12,proj11task2,setschedule).
:- up(aud1,proj12task2propa,request).
:- up(acc2,proj12task2a,request).
:- up(ldr12,proj21task1,setschedule).
:- up(aud2,proj21task2propa,request).
:- up(plan2,proj22task1propa,read).
:- up(aud1,proj11task1,read).
:- up(des11,proj11task1a,setcost).
:- up(des11,proj12task2,write).
:- up(code12,proj12budget,approve).
:- up(code11,proj21task1a,approve).
:- up(des21,proj21task2,write).
:- up(aud2,proj11task1,request).
:- up(des12,proj12task2a,setschedule).
:- up(code21,proj21task1a,setschedule).
:- up(ldr2,proj11task2a,approve).
:- up(ldr12,proj12task2a,setschedule).
:- up(ldr11,proj12sched,setstatus).
:- up(des21,proj12task2,setschedule).
up(des12,proj12task1,read).
:- up(code11,proj11task1,setstatus).
:- up(code21,proj21task1prop,approve).
:- up(mgr1,proj22task1,request).
:- up(ldr11,proj12task2a,setschedule).
:- up(code12,proj12task2prop,request).
:- up(des12,proj21task1prop,setschedule).
:- up(code21,proj21task1propa,setschedule).
:- up(aud2,proj12task1a,read).
:- up(code22,proj12sched,setschedule).
:- up(code21,proj12task1prop,request).
:- up(des12,proj11sched,approve).
:- up(acc2,proj21task1,request).
:- up(aud2,proj12task2,write).
:- up(mgr1,proj21task1a,approve).
:- up(aud1,proj22task2prop,setschedule).
:- up(code11,proj21task1prop,setstatus).
:- up(code12,proj11task1,setstatus).
:- up(code12,proj11task1a,setschedule).
:- up(des21,proj11task2prop,approve).
:- up(aud2,proj22task2propa,setschedule).
:- up(mgr1,proj21task2a,request).
:- up(aud1,proj21task1a,write).
:- up(aud2,proj12task2,setstatus).
:- up(ldr12,proj22sched,setschedule).
:- up(des22,proj12budget,setcost).
:- up(code21,proj22task1a,setcost).
:- up(code11,proj11budget,setcost).
:- up(code12,proj21task1,read).
:- up(ldr11,proj22task2prop,write).
:- up(code22,proj22task2prop,setstatus).
:- up(code12,proj22task2prop,request).
:- up(des11,proj12task1,setstatus).
:- up(code11,proj12task1propa,setcost).
:- up(plan1,proj11task1,write).
:- up(aud2,proj11task2prop,setstatus).
:- up(plan2,proj21task2prop,approve).
:- up(acc2,proj22sched,request).
:- up(aud2,proj12task2propa,write).
:- up(des11,proj21task2propa,request).
:- up(ldr11,proj22budget,request).
:- up(code21,proj22task2,read).
:- up(acc2,proj12task1prop,setschedule).
:- up(aud1,proj11task2prop,approve).
:- up(acc1,proj12task2a,approve).
:- up(mgr1,proj21task2propa,write).
:- up(des22,proj12task2propa,read).
:- up(des21,proj12task2prop,setcost).
:- up(acc2,proj22task2propa,approve).
:- up(acc1,proj21task1a,setschedule).
up(code21,proj21sched,read).
:- up(code11,proj11budget,read).
:- up(ldr11,proj22task1a,approve).
:- up(code11,proj12task1propa,request).
:- up(plan1,proj12budget,approve).
:- up(ldr12,proj12sched,setstatus).
:- up(des21,proj11sched,request).
:- up(code21,proj11task1prop,read).
:- up(code21,proj12task1a,write).
:- up(aud1,proj22task2propa,read).
:- up(code11,proj12task1propa,setschedule).
:- up(ldr2,proj22task2propa,request).
up(mgr1,proj12budget,approve).
:- up(mgr1,proj21task1,setstatus).
:- up(des22,proj12task1a,setstatus).
:- up(ldr12,proj12task1a,read).
:- up(code11,proj12task1,read).
:- up(ldr2,proj21task1prop,setcost).
:- up(des21,proj12task2,approve).
:- up(code21,proj22sched,setcost).
up(plan1,proj11task2,setschedule).
:- up(code12,proj12task2a,setschedule).
:- up(acc1,proj11sched,write).
:- up(code12,proj11task2,setschedule).
up(ldr2,proj21sched,read).
:- up(plan1,proj22task1,setschedule).
:- up(mgr2,proj12budget,setcost).
:- up(des11,proj11task2propa,approve).
:- up(plan2,proj22budget,approve).
:- up(code22,proj11task1,setschedule).
:- up(plan2,proj22task1a,write).
:- up(ldr2,proj11task2a,setcost).
:- up(plan2,proj12budget,setcost).
:- up(code22,proj11task2,approve).
:- up(mgr1,proj22task2prop,read).
:- up(code11,proj11task1a,write).
:- up(aud2,proj22task1a,setcost).
:- up(aud1,proj21task1,request).
:- up(mgr1,proj21task2propa,setcost).
:- up(des12,proj11task2,setstatus).
:- up(code21,proj21task1prop,setschedule).
:- up(plan1,proj11task2,approve).
:- up(mgr1,proj22task1,setcost).
:- up(des21,proj22task1propa,setcost).
:- up(ldr11,proj11task2prop,setschedule).
:- up(code12,proj11task1propa,approve).
:- up(acc1,proj22task1a,read).
up(code21,proj21task2a,read).
:- up(aud2,proj11task1prop,read).
:- up(code11,proj22task2,setcost).
:- up(mgr1,proj12task1prop,write).
:- up(code22,proj12task1propa,write).
:- up(acc1,proj12task1propa,approve).
:- up(aud2,proj12task1propa,read).
:- up(aud2,proj11task2,request).
:- up(des11,proj21task2propa,setstatus).
:- up(des11,proj11task2prop,write).
up(ldr12,proj12budget,write).
:- up(mgr1,proj11task1prop,read).
:- up(ldr2,proj11task2propa,setschedule).
:- up(des11,proj21task2propa,setcost).
:- up(ldr12,proj12budget,setschedule).
:- up(des21,proj21task2propa,setstatus).
:- up(aud1,proj12task1propa,approve).
:- up(plan1,proj22task2propa,request).
:- up(plan2,proj12sched,request).
:- up(plan1,proj21task2,write).
:- up(aud2,proj21task2a,write).
:- up(aud2,proj11task1propa,setstatus).
:- up(code11,proj22budget,read).
:- up(acc2,proj21task1prop,setstatus).
:- up(mgr2,proj21sched,request).
:- up(des21,proj11task2,setschedule).
:- up(ldr11,proj12task1propa,setcost).
:- up(acc1,proj11task2prop,approve).
up(code21,proj21task2propa,request).
:- up(mgr1,proj11task1propa,approve).
up(acc2,proj22budget,write).
:- up(mgr2,proj22budget,setschedule).
:- up(aud2,proj21task1propa,setcost).
:- up(ldr2,proj11task1a,setstatus).
:- up(aud1,proj21task2prop,approve).
:- up(des22,proj21task2propa,approve).
:- up(des11,proj11budget,write).
:- up(code22,proj21task1,setcost).
:- up(aud2,proj21task1prop,setcost).
:- up(des22,proj11task1prop,write).
:- up(aud2,proj11task1,setschedule).
:- up(plan2,proj11sched,approve).
:- up(plan1,proj21task1,write).
:- up(aud1,proj12sched,setschedule).
:- up(acc1,proj22task2,setcost).
:- up(plan1,proj21sched,setstatus).
:- up(code11,proj12task2a,read).
:- up(plan1,proj11task2prop,request).
:- up(des22,proj11budget,approve).
:- up(des22,proj12task1a,read).
:- up(ldr11,proj22task1a,setcost).
:- up(plan2,proj11task2propa,request).
:- up(code11,proj22task1a,setschedule).
:- up(ldr2,proj22task2propa,write).
:- up(plan2,proj12task1prop,request).
:- up(mgr1,proj12task2propa,read).
:- up(code21,proj11task1a,setcost).
:- up(acc1,proj11task1prop,approve).
:- up(aud2,proj22task2a,approve).
:- up(des12,proj12sched,write).
:- up(des22,proj12task1propa,write).
:- up(plan2,proj11task2,setstatus).
:- up(ldr11,proj22task2a,approve).
:- up(code21,proj21task2a,approve).
:- up(code22,proj11sched,read).
:- up(des22,proj12task1prop,setcost).
up(plan2,proj22task1propa,setschedule).
:- up(plan2,proj11task2propa,setschedule).
:- up(des22,proj21task1a,approve).
:- up(code22,proj22task2prop,setschedule).
:- up(des12,proj12budget,setstatus).
:- up(acc1,proj21task1,approve).
:- up(aud1,proj21task2prop,write).
up(plan1,proj12sched,write).
:- up(mgr2,proj12task2a,read).
:- up(code11,proj12sched,setcost).
:- up(des11,proj21task2,approve).
:- up(code21,proj22task2prop,write).
:- up(des22,proj11task2prop,setcost).
:- up(des11,proj12budget,setstatus).
:- up(code22,proj11task1prop,write).
up(acc2,proj21task1propa,setcost).
:- up(ldr2,proj22task1,write).
up(des21,proj21task1,read).
:- up(ldr11,proj12task1propa,approve).
:- up(plan1,proj11budget,setstatus).
:- up(des21,proj21task1a,approve).
:- up(mgr1,proj21task1a,setschedule).
:- up(plan1,proj11task2a,setcost).
:- up(acc1,proj21task1prop,approve).
:- up(acc1,proj11sched,setschedule).
:- up(acc1,proj22task1prop,setschedule).
:- up(code22,proj21task2prop,read).
:- up(des22,proj12task1a,request).
:- up(code12,proj22task1a,read).
:- up(code11,proj11task2a,setschedule).
:- up(aud2,proj11task2propa,setcost).
:- up(acc1,proj22task2prop,setschedule).
:- up(code12,proj11task1a,read).
:- up(mgr1,proj12task2prop,setcost).
:- up(code12,proj21task2propa,request).
:- up(des11,proj22task2propa,write).
up(ldr12,proj12budget,read).
:- up(plan2,proj21task1prop,approve).
:- up(des21,proj21task2prop,approve).
:- up(des11,proj21task1a,read).
:- up(ldr11,proj22task2propa,setstatus).
:- up(code22,proj12task1a,approve).
:- up(aud2,proj21task1prop,request).
:- up(acc1,proj12task1a,setstatus).
:- up(ldr12,proj22task2a,setstatus).
:- up(aud2,proj12budget,read).
:- up(aud2,proj21sched,request).
:- up(aud1,proj22task2prop,setstatus).
:- up(mgr1,proj11task1propa,setstatus).
:- up(ldr2,proj21task2,read).
:- up(code21,proj21task1,setstatus).
:- up(des22,proj21task2prop,request).
:- up(ldr11,proj21task2prop,setcost).
:- up(des11,proj21task2prop,setschedule).
:- up(mgr1,proj22task2propa,write).
:- up(aud1,proj21task1,setstatus).
:- up(code11,proj11task2a,approve).
:- up(code21,proj11task2prop,request).
:- up(mgr1,proj22task1prop,approve).
:- up(code12,proj11task1prop,setstatus).
:- up(acc1,proj21task2a,setcost).
:- up(ldr11,proj12task1a,request).
:- up(des12,proj12budget,read).
:- up(des12,proj22task1,approve).
:- up(ldr2,proj12task1a,approve).
:- up(plan1,proj21task2prop,request).
:- up(des11,proj12task2,setstatus).
:- up(des21,proj21task1,setstatus).
:- up(code11,proj12task1a,request).
:- up(ldr11,proj21sched,approve).
:- up(acc2,proj22task1prop,request).
:- up(acc2,proj21task1propa,write).
:- up(code22,proj22task1a,setschedule).
:- up(des22,proj12task2prop,setcost).
:- up(des11,proj21task2prop,setcost).
:- up(code11,proj11budget,request).
:- up(aud1,proj11sched,request).
:- up(aud2,proj21task2propa,approve).
:- up(plan1,proj21task1propa,request).
:- up(mgr1,proj12task1prop,setcost).
:- up(ldr2,proj11task2propa,request).
:- up(acc2,proj11task1propa,setschedule).
:- up(code22,proj11budget,write).
:- up(mgr1,proj11task1a,setschedule).
:- up(code21,proj22budget,approve).
:- up(aud1,proj22task2prop,read).
:- up(des11,proj12task2,setschedule).
:- up(des22,proj12task1prop,setschedule).
:- up(des21,proj21task2prop,setstatus).
:- up(code12,proj21task2,setstatus).
:- up(code21,proj12task2,setcost).
:- up(aud2,proj12task2prop,approve).
:- up(acc2,proj22task2prop,write).
:- up(mgr1,proj21task2prop,write).
:- up(code12,proj11sched,approve).
:- up(aud2,proj21task2,read).
:- up(code12,proj21sched,read).
:- up(acc2,proj22task1propa,setschedule).
:- up(aud1,proj12task2prop,setcost).
:- up(mgr1,proj21task1,setschedule).
:- up(mgr2,proj22task2a,setschedule).
:- up(plan2,proj12task2,setschedule).
:- up(ldr11,proj11task2,request).
:- up(plan1,proj11task1,setcost).
:- up(ldr11,proj22task2,setcost).
:- up(code11,proj21sched,approve).
:- up(ldr12,proj12task1,write).
:- up(ldr12,proj22task1a,approve).
:- up(code22,proj11task1,setcost).
:- up(mgr2,proj11task1,request).
:- up(ldr2,proj12task2propa,read).
:- up(des22,proj11sched,write).
:- up(des22,proj21task2propa,request).
:- up(acc2,proj22task1a,read).
:- up(code12,proj11task2a,setschedule).
:- up(code12,proj12task2prop,write).
:- up(mgr2,proj22task2propa,write).
:- up(aud1,proj22budget,write).
:- up(acc1,proj12task2,setstatus).
:- up(code11,proj11task2propa,setcost).
:- up(mgr1,proj21task2propa,request).
:- up(des21,proj21task2a,setcost).
:- up(plan2,proj12task1propa,read).
:- up(plan1,proj11task1prop,setcost).
:- up(acc2,proj12task2a,read).
:- up(acc1,proj22task1a,approve).
:- up(code22,proj11task2prop,approve).
:- up(ldr12,proj12task1prop,request).
:- up(code12,proj11task1prop,setschedule).
:- up(mgr1,proj11task2prop,read).
:- up(plan2,proj22sched,approve).
:- up(mgr1,proj22task1,setschedule).
:- up(mgr2,proj21task2propa,setschedule).
up(acc1,proj12task1a,setcost).
up(acc2,proj21sched,read).
:- up(aud1,proj11task1,setstatus).
:- up(des12,proj21task1prop,write).
:- up(aud2,proj11task1,setstatus).
:- up(code22,proj21task1prop,approve).
:- up(ldr12,proj22task1prop,request).
:- up(ldr11,proj11task1,setschedule).
:- up(plan2,proj12task2propa,read).
:- up(acc2,proj21task1a,request).
:- up(mgr2,proj12task1prop,request).
up(acc2,proj22task2,setcost).
:- up(des22,proj12task1propa,request).
up(plan1,proj11task1a,setschedule).
:- up(plan1,proj12task2,read).
:- up(code12,proj21task1prop,write).
:- up(mgr1,proj12task2a,setcost).
:- up(ldr12,proj11task2,read).
:- up(code22,proj21task2prop,setschedule).
:- up(ldr12,proj22task1a,setstatus).
:- up(mgr2,proj22budget,setcost).
:- up(plan2,proj12task1prop,write).
:- up(aud2,proj11task2a,read).
:- up(ldr12,proj12task1a,write).
:- up(aud2,proj12task2propa,setstatus).
:- up(des11,proj11task1,setcost).
:- up(plan2,proj21task1prop,read).
:- up(des11,proj22budget,setschedule).
:- up(aud2,proj12task1propa,setschedule).
:- up(des11,proj21budget,setschedule).
:- up(des11,proj11task1a,approve).
:- up(mgr1,proj12task1propa,setschedule).
:- up(plan2,proj12task2a,read).
:- up(des12,proj21task2prop,read).
:- up(des11,proj11task2,setschedule).
:- up(acc2,proj11task2prop,read).
:- up(ldr12,proj11task1prop,read).
:- up(ldr2,proj12task2,read).
:- up(aud2,proj21task1prop,setstatus).
:- up(aud2,proj12budget,setschedule).
:- up(ldr2,proj22task1,request).
:- up(des21,proj22task2propa,read).
:- up(des21,proj21task1a,write).
:- up(code22,proj12budget,setschedule).
:- up(code21,proj21task2prop,setstatus).
:- up(code21,proj21task1a,write).
:- up(des12,proj21budget,request).
:- up(ldr12,proj22task2prop,setstatus).
up(des21,proj21task1a,read).
:- up(ldr12,proj11task1,approve).
:- up(code11,proj21task2,write).
:- up(ldr2,proj22task2propa,read).
:- up(ldr11,proj22task2a,request).
:- up(ldr2,proj22task1,setstatus).
:- up(ldr2,proj11sched,request).
:- up(des21,proj22task2prop,write).
:- up(aud2,proj12task1a,write).
:- up(code22,proj12task1prop,setcost).
:- up(mgr2,proj12task2prop,request).
:- up(ldr12,proj21task1propa,setschedule).
:- up(code21,proj11task1propa,setschedule).
:- up(ldr11,proj12task1prop,setcost).
:- up(aud1,proj11task2propa,setschedule).
:- up(acc2,proj12task1a,request).
:- up(code21,proj11task2a,setschedule).
:- up(aud1,proj12task1prop,write).
:- up(plan2,proj22task1propa,write).
:- up(acc2,proj12task2a,setschedule).
:- up(des11,proj21sched,setcost).
:- up(code12,proj21task1prop,setcost).
:- up(aud1,proj22task1,setcost).
:- up(ldr11,proj21task2propa,setschedule).
:- up(ldr11,proj21task2,setschedule).
:- up(ldr11,proj22task2,request).
:- up(mgr1,proj21task2prop,setstatus).
:- up(ldr11,proj21task1,setcost).
:- up(ldr2,proj11task1,read).
:- up(mgr2,proj22task2prop,request).
:- up(code22,proj12task2propa,setschedule).
:- up(code11,proj22task2prop,setschedule).
:- up(des12,proj11task1,setstatus).
:- up(aud2,proj11task2prop,approve).
:- up(mgr2,proj11task1,write).
:- up(des11,proj22task2,setcost).
:- up(aud2,proj22task2prop,request).
:- up(plan2,proj21task1propa,setstatus).
up(acc2,proj21budget,write).
:- up(code22,proj21task1a,read).
:- up(aud2,proj11task2prop,read).
:- up(plan1,proj21budget,approve).
:- up(code21,proj22sched,setschedule).
:- up(ldr12,proj12task2a,setcost).
:- up(plan2,proj22task2prop,read).
:- up(des12,proj21task1,read).
:- up(aud1,proj21task1propa,approve).
:- up(des12,proj12task2a,write).
:- up(mgr2,proj22task1,request).
:- up(code11,proj22task2a,setcost).
:- up(code21,proj21task1propa,setstatus).
:- up(des12,proj12task2prop,setstatus).
:- up(mgr1,proj11task2a,request).
:- up(mgr1,proj11task1propa,write).
:- up(acc1,proj11task1a,read).
:- up(aud1,proj22budget,request).
up(ldr2,proj21budget,read).
:- up(ldr11,proj22sched,request).
:- up(aud2,proj21task2propa,setcost).
:- up(code12,proj12sched,setcost).
:- up(plan1,proj21budget,read).
:- up(ldr2,proj12task1propa,request).
:- up(des22,proj21task2a,approve).
:- up(aud2,proj21task2,setcost).
up(acc1,proj11task1a,setcost).
:- up(aud1,proj22task1prop,setstatus).
up(des11,proj11task1a,read).
:- up(des21,proj11task1a,read).
:- up(code22,proj21task1a,setstatus).
:- up(code21,proj12task1a,request).
:- up(acc1,proj22task1propa,setschedule).
up(plan2,proj22sched,read).
:- up(mgr2,proj11task2prop,request).
:- up(code12,proj11task1,write).
up(ldr2,proj21sched,write).
:- up(ldr11,proj11task2,setschedule).
:- up(ldr2,proj12task1,request).
:- up(code12,proj12task1,setschedule).
:- up(ldr11,proj22task1prop,request).
:- up(code11,proj21task1propa,request).
:- up(code22,proj21task1,request).
:- up(code11,proj11task1prop,write).
:- up(ldr11,proj12task1propa,setstatus).
:- up(code12,proj11task2,write).
:- up(ldr12,proj22task2a,request).
:- up(ldr11,proj22task1propa,approve).
:- up(ldr11,proj12task1,approve).
:- up(des12,proj11sched,setcost).
:- up(mgr1,proj12task2,setcost).
:- up(code12,proj22sched,setstatus).
up(plan1,proj11task2a,setschedule).
:- up(ldr2,proj11sched,approve).
:- up(mgr1,proj22task1propa,setstatus).
:- up(plan2,proj21task2,request).
:- up(ldr11,proj21task2prop,request).
:- up(des22,proj11task2,approve).
:- up(code21,proj21task1,request).
:- up(des11,proj22task1,setcost).
:- up(des21,proj22task2,read).
:- up(mgr1,proj11task2prop,write).
:- up(code11,proj11task2,setcost).
:- up(code12,proj12task1propa,request).
:- up(ldr12,proj22task1propa,approve).
:- up(ldr2,proj12task1prop,setschedule).
:- up(acc2,proj11budget,approve).
:- up(plan1,proj21sched,setcost).
:- up(ldr2,proj22task2a,setschedule).
:- up(des11,proj12task2propa,setschedule).
:- up(ldr12,proj22task2,setstatus).
:- up(des21,proj11task2propa,request).
:- up(des12,proj21task2,setcost).
:- up(aud2,proj22task1prop,request).
:- up(acc1,proj12task1prop,setschedule).
:- up(acc1,proj21task1propa,approve).
:- up(plan1,proj21sched,setschedule).
:- up(code22,proj11budget,request).
:- up(aud2,proj12task2propa,setschedule).
:- up(aud1,proj12task1a,setcost).
:- up(acc2,proj11task2propa,setschedule).
:- up(des11,proj11sched,approve).
:- up(code22,proj11task1propa,read).
up(des22,proj22task1,request).
:- up(plan2,proj12task2,request).
:- up(aud1,proj12sched,setcost).
:- up(ldr12,proj11task2a,setstatus).
:- up(ldr2,proj12task2,request).
:- up(des22,proj11task2a,setstatus).
:- up(mgr1,proj11task2prop,setschedule).
:- up(aud1,proj21task2,setschedule).
:- up(aud2,proj12task2,approve).
:- up(ldr12,proj21task2prop,request).
:- up(des21,proj21task2prop,request).
:- up(mgr2,proj21task1prop,approve).
:- up(code21,proj21budget,setschedule).
:- up(code12,proj21sched,write).
:- up(code21,proj21task2prop,setcost).
:- up(code11,proj11task1,setcost).
:- up(plan2,proj12task1a,write).
:- up(mgr2,proj21task2,setschedule).
:- up(code11,proj11task2prop,setcost).
:- up(des21,proj12task1propa,request).
:- up(aud2,proj12sched,setcost).
:- up(ldr2,proj21task1propa,setstatus).
:- up(ldr11,proj12task2,write).
:- up(acc2,proj12task2,request).
:- up(aud1,proj11task1a,setstatus).
:- up(aud1,proj11task2,read).
:- up(code22,proj12task1prop,setstatus).
:- up(code21,proj12budget,setstatus).
:- up(mgr2,proj12task1a,approve).
:- up(des21,proj11task2,setstatus).
:- up(des21,proj21task2,approve).
:- up(des21,proj11sched,setschedule).
:- up(aud1,proj12budget,request).
:- up(des22,proj11task2a,setcost).
:- up(acc1,proj22task1propa,read).
:- up(mgr2,proj11budget,setschedule).
:- up(des21,proj11task2,read).
:- up(des11,proj22task2a,request).
:- up(acc1,proj22task1,setstatus).
:- up(des22,proj22task1,setcost).
:- up(des12,proj12task2,request).
:- up(des12,proj11task1prop,setstatus).
:- up(des21,proj21task1prop,setschedule).
:- up(code12,proj22budget,request).
:- up(code12,proj22task2,setschedule).
:- up(acc2,proj12task1prop,write).
:- up(aud1,proj12task2a,write).
:- up(code12,proj11task2prop,setcost).
:- up(acc1,proj12sched,write).
:- up(aud1,proj12sched,request).
:- up(des11,proj22task1a,write).
:- up(des21,proj21task2,setstatus).
:- up(ldr11,proj12task1prop,setschedule).
:- up(des21,proj21task2propa,write).
:- up(des11,proj12task1propa,write).
:- up(mgr1,proj12task2prop,approve).
:- up(code11,proj22task1,request).
:- up(plan1,proj21task2propa,request).
:- up(des22,proj22budget,setcost).
:- up(ldr2,proj22task1propa,approve).
:- up(code22,proj12task1prop,request).
:- up(ldr12,proj22task2,setcost).
:- up(acc1,proj22task1prop,setcost).
:- up(code21,proj11budget,setcost).
:- up(ldr12,proj12sched,setcost).
:- up(aud1,proj12task1a,request).
:- up(plan1,proj11task1propa,setcost).
:- up(mgr1,proj12budget,request).
:- up(aud1,proj12sched,write).
:- up(des21,proj12task1,setcost).
:- up(des21,proj21task2,setcost).
:- up(plan1,proj11task2,setcost).
:- up(des21,proj12task1a,setschedule).
:- up(acc2,proj21task2a,request).
:- up(des22,proj12task2prop,approve).
:- up(mgr1,proj21task1prop,request).
:- up(code22,proj21task1propa,setcost).
:- up(des12,proj12task2,setschedule).
:- up(des11,proj22sched,approve).
:- up(acc1,proj12task1prop,setstatus).
:- up(des11,proj21task1a,setcost).
:- up(des22,proj11sched,setstatus).
:- up(code11,proj11sched,write).
:- up(code11,proj22task2prop,read).
:- up(des11,proj12task1,setschedule).
:- up(mgr1,proj22task2prop,setstatus).
:- up(plan2,proj11task1,setstatus).
:- up(aud1,proj21sched,read).
:- up(aud1,proj22task2,write).
:- up(mgr1,proj21task2,setcost).
:- up(aud2,proj11task1prop,approve).
:- up(des11,proj11task1a,setschedule).
:- up(ldr2,proj12task2,setcost).
:- up(acc1,proj22task2propa,setschedule).
up(code12,proj12task2a,request).
:- up(ldr11,proj12budget,read).
:- up(code12,proj21task2propa,write).
:- up(ldr2,proj21task2a,approve).
:- up(aud2,proj21task1propa,write).
:- up(aud1,proj12task1prop,setschedule).
:- up(des21,proj22task1propa,approve).
:- up(code11,proj21budget,write).
:- up(des12,proj22task1prop,read).
:- up(code22,proj21task2propa,setcost).
:- up(ldr2,proj22task1propa,setschedule).
:- up(des12,proj12task2a,read).
:- up(plan1,proj21task1,setschedule).
:- up(code12,proj22task1propa,setschedule).
:- up(des11,proj12sched,setstatus).
:- up(acc1,proj11sched,setcost).
:- up(des22,proj11task1prop,setcost).
:- up(ldr11,proj21task2prop,write).
:- up(des22,proj12task1a,approve).
:- up(code12,proj21budget,setcost).
:- up(aud2,proj22task1,read).
up(code11,proj11task2propa,request).
:- up(ldr2,proj22task1prop,approve).
:- up(aud1,proj22task1propa,setcost).
:- up(ldr11,proj21task2propa,request).
:- up(ldr12,proj21task2a,write).
:- up(ldr12,proj21budget,approve).
:- up(aud2,proj22task1propa,setschedule).
:- up(ldr2,proj11task1a,approve).
:- up(des22,proj12task2,approve).
:- up(mgr2,proj12task1propa,setschedule).
:- up(ldr11,proj22task1a,setschedule).
:- up(plan1,proj11task1a,setcost).
:- up(code11,proj11task2prop,setstatus).
:- up(plan1,proj12task2propa,request).
:- up(des12,proj21task1a,request).
:- up(code11,proj11task1a,setcost).
:- up(code11,proj22task2propa,setstatus).
up(acc1,proj11task2propa,setcost).
:- up(mgr2,proj12task1,setschedule).
:- up(des22,proj21task2a,read).
:- up(code21,proj21task2a,write).
up(acc1,proj12task2a,setcost).
:- up(plan2,proj12task1,setschedule).
:- up(des12,proj11task2a,setschedule).
:- up(des21,proj22task1prop,setschedule).
:- up(des12,proj21budget,read).
:- up(mgr2,proj11task2a,setcost).
:- up(acc2,proj11budget,setschedule).
:- up(aud2,proj22task2prop,approve).
up(acc1,proj11task2,setcost).
:- up(acc2,proj22task1prop,setschedule).
:- up(code22,proj22task2a,approve).
:- up(code12,proj11sched,read).
:- up(code11,proj12sched,setstatus).
:- up(mgr1,proj21task1prop,setstatus).
:- up(plan2,proj21task1,read).
:- up(plan1,proj12budget,setschedule).
:- up(code22,proj22sched,setcost).
:- up(aud2,proj12task2,read).
:- up(des12,proj22task2,approve).
:- up(mgr2,proj22task2prop,read).
:- up(code21,proj11task2prop,setcost).
:- up(code21,proj12task2a,approve).
up(acc2,proj21task1,setcost).
:- up(code21,proj11task2prop,write).
:- up(aud2,proj11task2,setcost).
:- up(ldr12,proj11task2prop,setschedule).
:- up(plan2,proj22task1prop,setstatus).
:- up(ldr2,proj21task2,setstatus).
:- up(ldr2,proj11task1propa,request).
:- up(ldr11,proj12task1prop,approve).
:- up(plan2,proj21task2a,approve).
:- up(code11,proj21task2a,write).
:- up(plan1,proj12task2propa,read).
:- up(acc2,proj11task1prop,write).
:- up(ldr2,proj12task1prop,read).
:- up(code21,proj11task2a,request).
:- up(ldr12,proj21task2a,read).
:- up(code22,proj12task1prop,setschedule).
:- up(ldr11,proj22task1,setcost).
:- up(ldr2,proj11task2prop,request).
:- up(des11,proj21task1propa,request).
:- up(code12,proj21task1a,setcost).
:- up(des11,proj11task2a,setcost).
:- up(aud1,proj21task2prop,setcost).
:- up(des11,proj22budget,approve).
:- up(des21,proj22budget,write).
:- up(plan1,proj11task1prop,read).
:- up(ldr12,proj21task2prop,read).
:- up(ldr12,proj12task2propa,approve).
:- up(des21,proj22task2prop,setstatus).
:- up(plan2,proj12budget,setstatus).
:- up(code22,proj21task2a,setcost).
:- up(aud2,proj11task2propa,setschedule).
:- up(code21,proj12task1,write).
:- up(aud1,proj12task1propa,read).
:- up(ldr2,proj21task2propa,setschedule).
:- up(plan2,proj21task1a,approve).
:- up(aud1,proj11task2propa,setcost).
:- up(code21,proj22task2a,setstatus).
:- up(plan1,proj12task2propa,approve).
:- up(des22,proj11task1propa,write).
:- up(plan1,proj12task2,request).
:- up(plan2,proj21sched,approve).
:- up(ldr11,proj22task2prop,setstatus).
:- up(code22,proj12task1propa,approve).
:- up(des21,proj12task2prop,request).
:- up(ldr12,proj22task1propa,setstatus).
:- up(plan1,proj21task2a,setcost).
:- up(plan2,proj12budget,request).
:- up(ldr2,proj11task2prop,approve).
:- up(mgr1,proj21task1a,setstatus).
:- up(des11,proj21task2prop,request).
:- up(mgr2,proj12task2a,setschedule).
:- up(acc1,proj22task2propa,approve).
:- up(des11,proj12task2a,setcost).
:- up(ldr12,proj21task1prop,setschedule).
up(des21,proj21task1a,request).
:- up(plan1,proj11task2propa,setstatus).
:- up(aud2,proj11task2a,write).
:- up(des22,proj21task2prop,approve).
:- up(des21,proj12budget,write).
:- up(ldr2,proj11task1prop,write).
:- up(mgr1,proj22task1prop,write).
:- up(code22,proj12task2a,setcost).
:- up(plan2,proj21task1,write).
:- up(des12,proj21task1propa,approve).
:- up(ldr2,proj21task2,write).
:- up(des21,proj22task1prop,request).
:- up(des11,proj22sched,read).
:- up(mgr2,proj21task1prop,setstatus).
:- up(acc1,proj12task1,write).
:- up(ldr12,proj21task1a,setstatus).
:- up(des12,proj12task1,setschedule).
:- up(code21,proj22task1propa,write).
:- up(des11,proj21task1propa,write).
:- up(code12,proj11task1prop,approve).
:- up(acc1,proj12task1,setstatus).
:- up(aud1,proj22task1a,read).
:- up(plan2,proj22task2a,write).
:- up(code12,proj11task1propa,read).
:- up(des12,proj11task2propa,setschedule).
:- up(aud2,proj22budget,setschedule).
:- up(mgr2,proj21task1,write).
:- up(des22,proj21budget,setcost).
:- up(mgr1,proj21task2,setstatus).
:- up(des22,proj11task1a,approve).
:- up(ldr11,proj21task2,read).
:- up(aud2,proj12task2a,request).
:- up(des12,proj11task1a,read).
:- up(aud2,proj11task2,read).
:- up(aud1,proj11task2a,setstatus).
:- up(aud2,proj11task1a,approve).
:- up(plan2,proj11task1prop,setcost).
:- up(des21,proj11sched,setcost).
:- up(des22,proj22task2propa,approve).
:- up(des22,proj12budget,setstatus).
:- up(ldr12,proj22task2,setschedule).
:- up(plan2,proj12sched,approve).
:- up(aud1,proj22task1a,setstatus).
:- up(code21,proj21task1,setschedule).
:- up(aud2,proj22budget,approve).
:- up(code22,proj12sched,setstatus).
:- up(des11,proj22task2a,setstatus).
:- up(aud2,proj12task1propa,write).
:- up(code21,proj11budget,request).
:- up(des12,proj11task1,approve).
:- up(plan1,proj11task2a,write).
:- up(ldr11,proj11task1a,setcost).
:- up(aud1,proj11task2,write).
:- up(code22,proj22task1a,read).
:- up(code11,proj22task2a,setschedule).
:- up(des12,proj21task2a,setstatus).
:- up(des12,proj21sched,request).
:- up(aud2,proj11task2prop,write).
:- up(aud2,proj22task2,read).
:- up(des22,proj21sched,approve).
:- up(des21,proj22task1a,setstatus).
:- up(ldr2,proj12task1,setstatus).
:- up(des11,proj12task2,approve).
:- up(ldr11,proj21task1propa,write).
:- up(acc1,proj11task2prop,request).
:- up(code22,proj22task1,setstatus).
:- up(ldr11,proj12task2,setschedule).
:- up(plan2,proj22task2prop,request).
:- up(mgr2,proj21task1,setcost).
:- up(code21,proj22sched,write).
:- up(plan2,proj22task2,read).
:- up(ldr2,proj22budget,approve).
:- up(plan2,proj11task1,setschedule).
:- up(plan1,proj22task2prop,request).
:- up(code11,proj21task2prop,request).
:- up(des21,proj12task1a,request).
:- up(des11,proj22task1propa,read).
:- up(des12,proj22task2prop,setschedule).
:- up(des21,proj22budget,setschedule).
:- up(code21,proj11task1propa,read).
up(des12,proj12task1a,read).
:- up(aud1,proj12task2a,setcost).
:- up(mgr2,proj22sched,setschedule).
:- up(acc2,proj11task1,setcost).
:- up(code12,proj12task2propa,request).
:- up(plan1,proj22task1a,setcost).
:- up(mgr2,proj12budget,request).
:- up(ldr2,proj12budget,read).
:- up(acc1,proj11sched,setstatus).
:- up(mgr2,proj21task1propa,request).
:- up(code22,proj21task1prop,request).
up(plan1,proj12task1a,setschedule).
:- up(aud1,proj22sched,read).
:- up(acc2,proj22sched,write).
:- up(aud1,proj12task2,approve).
:- up(des22,proj21sched,setschedule).
:- up(aud1,proj21task2propa,read).
:- up(des12,proj22task2,setstatus).
:- up(aud1,proj11task1a,setcost).
:- up(des11,proj22task1,write).
:- up(mgr1,proj12task1propa,approve).
:- up(des22,proj21task2a,write).
:- up(aud1,proj22task1propa,setschedule).
:- up(aud2,proj22task1propa,write).
:- up(des22,proj21task2a,setstatus).
:- up(acc1,proj22task2,approve).
:- up(plan1,proj21task2propa,setcost).
:- up(ldr11,proj22task2,approve).
:- up(mgr2,proj12task1propa,request).
:- up(plan1,proj21task2,request).
:- up(des21,proj22task1prop,setcost).
:- up(ldr11,proj21sched,setstatus).
:- up(aud2,proj11sched,read).
:- up(code22,proj11task2prop,request).
:- up(aud2,proj22task2,setstatus).
up(des11,proj11task1,read).
:- up(code22,proj11task2,setschedule).
:- up(aud2,proj11sched,write).
:- up(des21,proj11budget,read).
:- up(des12,proj12task2,approve).
:- up(des22,proj12task2prop,write).
:- up(plan2,proj22sched,request).
:- up(ldr11,proj22task1prop,write).
:- up(des21,proj12task1,request).
:- up(mgr1,proj21budget,setstatus).
:- up(code11,proj22task2propa,setschedule).
:- up(ldr11,proj21task2propa,approve).
:- up(des11,proj12task1prop,setcost).
:- up(des22,proj11task1a,setschedule).
:- up(aud2,proj12task1prop,request).
:- up(des12,proj22task1propa,setcost).
:- up(plan1,proj21task1prop,setstatus).
:- up(plan1,proj22task2prop,approve).
:- up(aud1,proj11task1a,approve).
:- up(ldr12,proj12task2prop,setschedule).
:- up(acc1,proj11task1propa,setschedule).
:- up(code12,proj11sched,request).
:- up(mgr1,proj21task2,approve).
:- up(mgr2,proj11budget,read).
:- up(mgr2,proj21task2prop,request).
:- up(ldr12,proj12task2prop,request).
:- up(des21,proj11task2a,setschedule).
:- up(des22,proj21task1,setcost).
:- up(plan1,proj12task1propa,write).
:- up(ldr12,proj22budget,approve).
:- up(code21,proj22task1,setstatus).
:- up(acc2,proj12task2prop,setschedule).
:- up(des22,proj12task1prop,write).
:- up(des22,proj21sched,setstatus).
:- up(aud2,proj22task1prop,write).
:- up(aud1,proj11sched,write).
:- up(mgr1,proj22task1prop,setcost).
up(des12,proj12task1,request).
:- up(aud1,proj21task1a,setschedule).
:- up(code22,proj21task2,approve).
:- up(plan2,proj22task2prop,setcost).
:- up(des21,proj22task2,write).
:- up(des21,proj22task2prop,setcost).
:- up(code11,proj12task2,request).
:- up(mgr1,proj22task2,read).
:- up(des21,proj12sched,setschedule).
up(code22,proj22task2a,setstatus).
:- up(code22,proj12task1,setcost).
:- up(des22,proj11task1,setstatus).
:- up(plan2,proj22task2,request).
:- up(mgr1,proj12task1prop,request).
:- up(aud1,proj22task2prop,request).
:- up(aud1,proj12task2prop,request).
:- up(mgr2,proj11task2propa,read).
:- up(plan2,proj11task1prop,setstatus).
:- up(ldr2,proj11task2,setschedule).
:- up(des22,proj22task1prop,write).
:- up(mgr2,proj11task2a,request).
:- up(mgr2,proj22task1,setschedule).
:- up(code12,proj12task1a,approve).
:- up(code12,proj12task1,approve).
:- up(mgr1,proj22budget,write).
:- up(aud2,proj12task1,setstatus).
:- up(des12,proj12task2propa,request).
:- up(des21,proj12budget,read).
:- up(code22,proj11task2prop,setcost).
:- up(acc2,proj11sched,approve).
:- up(des11,proj22sched,setschedule).
:- up(ldr2,proj12task1propa,approve).
:- up(code21,proj22budget,read).
:- up(code12,proj12sched,request).
:- up(ldr11,proj21task1prop,request).
:- up(ldr11,proj12task2,read).
:- up(acc2,proj12budget,setstatus).
:- up(des12,proj11task1prop,setschedule).
:- up(des22,proj22task1prop,setcost).
:- up(code21,proj12task2propa,approve).
:- up(code21,proj11task2a,write).
:- up(code21,proj22task2propa,request).
:- up(mgr2,proj12task2prop,setstatus).
:- up(aud2,proj12task1propa,setcost).
:- up(acc1,proj11task1propa,request).
:- up(des22,proj22task1propa,request).
:- up(code22,proj12task1a,setcost).
:- up(des22,proj11task2,write).
:- up(des11,proj12task1prop,request).
:- up(ldr2,proj11task1propa,write).
:- up(des12,proj21budget,setcost).
:- up(plan1,proj12task2prop,setcost).
:- up(ldr11,proj11task1propa,setcost).
:- up(ldr12,proj11sched,setcost).
:- up(mgr1,proj12task2propa,write).
:- up(ldr11,proj12task2prop,setstatus).
:- up(ldr12,proj12task2propa,request).
:- up(ldr11,proj12task2propa,read).
:- up(acc2,proj11sched,write).
:- up(code22,proj12task2a,request).
:- up(code11,proj11task1a,read).
:- up(code21,proj22task1prop,setschedule).
:- up(ldr12,proj21task1a,write).
:- up(ldr11,proj22task2,setschedule).
:- up(code21,proj22task2a,read).
:- up(code21,proj21task2,setcost).
:- up(code12,proj22task2a,write).
:- up(ldr12,proj21task2propa,setschedule).
:- up(ldr11,proj11task2a,read).
:- up(mgr1,proj21task2a,setstatus).
:- up(code11,proj12task1,setstatus).
:- up(acc2,proj22task2a,setschedule).
:- up(ldr12,proj11task1propa,read).
:- up(des11,proj22task2propa,setcost).
up(code22,proj22task2a,read).
:- up(aud1,proj11task2,setstatus).
up(code21,proj21task2prop,read).
:- up(plan1,proj11task1prop,request).
:- up(des11,proj12sched,read).
:- up(mgr1,proj21task1a,read).
:- up(mgr1,proj11task2a,setcost).
:- up(aud1,proj21task2a,setschedule).
:- up(mgr1,proj22budget,setcost).
:- up(mgr1,proj22task1,read).
:- up(aud2,proj11task1,setcost).
:- up(plan1,proj11budget,approve).
:- up(plan1,proj12task1a,request).
:- up(des11,proj11budget,read).
:- up(des22,proj21task1,approve).
:- up(aud1,proj21task2prop,read).
:- up(plan1,proj11budget,read).
:- up(ldr2,proj12task2a,approve).
:- up(code21,proj11task1,write).
:- up(mgr2,proj22task2,request).
:- up(ldr11,proj21budget,approve).
:- up(aud2,proj12task1,setschedule).
:- up(des22,proj21task2,setstatus).
:- up(code22,proj11task2,setstatus).
:- up(ldr11,proj12task2propa,request).
:- up(aud2,proj11task1propa,setcost).
:- up(mgr2,proj12task1,setstatus).
:- up(code11,proj12task1a,setschedule).
:- up(code21,proj12task2,request).
:- up(des22,proj11sched,request).
:- up(acc2,proj21task2propa,request).
:- up(plan1,proj22task2propa,write).
:- up(aud2,proj12task1propa,setstatus).
:- up(des11,proj12task1propa,read).
:- up(code11,proj21task2,setstatus).
:- up(mgr2,proj22task2propa,setcost).
:- up(aud2,proj11task2,setschedule).
:- up(plan2,proj12task1prop,setcost).
:- up(code22,proj12task2prop,setcost).
:- up(acc1,proj21task1,setschedule).
:- up(code12,proj12task1prop,setstatus).
:- up(des22,proj11task2,read).
:- up(des22,proj11task2a,approve).
:- up(ldr12,proj22task2prop,approve).
:- up(ldr2,proj22task2prop,setstatus).
:- up(des22,proj22task2,approve).
:- up(code12,proj22budget,setcost).
:- up(mgr2,proj21task1propa,approve).
:- up(code12,proj11task1prop,request).
:- up(aud2,proj12task1prop,setcost).
:- up(plan1,proj12budget,write).
:- up(ldr12,proj22task1prop,setschedule).
:- up(des12,proj22sched,approve).
:- up(des11,proj11budget,request).
:- up(des11,proj21budget,request).
:- up(code22,proj11task2a,approve).
:- up(des11,proj12sched,approve).
:- up(des11,proj21task2prop,write).
:- up(mgr1,proj11task2propa,setcost).
:- up(code22,proj21task2a,write).
:- up(des22,proj22task1prop,setschedule).
:- up(des22,proj22task1propa,approve).
:- up(acc2,proj11sched,setcost).
:- up(mgr2,proj21sched,read).
:- up(aud2,proj12task2prop,write).
:- up(mgr1,proj22task2prop,setschedule).
:- up(mgr2,proj12budget,setstatus).
:- up(mgr2,proj22task1prop,setschedule).
:- up(ldr12,proj11task1prop,setstatus).
:- up(des11,proj11sched,setschedule).
:- up(acc2,proj11task2a,setschedule).
:- up(acc2,proj11budget,setstatus).
:- up(code11,proj12task1propa,write).
:- up(ldr12,proj12task2prop,setcost).
:- up(des21,proj22task1prop,read).
:- up(ldr2,proj12task1prop,request).
:- up(code12,proj12sched,setstatus).
up(ldr11,proj11budget,write).
:- up(mgr1,proj22task1a,setcost).
:- up(code21,proj11budget,approve).
:- up(aud2,proj12task2prop,request).
:- up(acc2,proj22task2a,write).
:- up(ldr12,proj12sched,request).
:- up(code22,proj21task2a,setstatus).
:- up(acc1,proj22sched,setcost).
:- up(des22,proj11budget,request).
:- up(code11,proj22task1a,write).
:- up(des11,proj21task1a,setstatus).
:- up(mgr2,proj11task2a,write).
:- up(code21,proj12task1,read).
:- up(des11,proj22task1propa,write).
:- up(des11,proj11task1,setschedule).
:- up(acc2,proj22sched,setcost).
:- up(des12,proj12sched,setstatus).
:- up(code21,proj22sched,request).
:- up(des11,proj11task2,write).
:- up(code21,proj12budget,request).
:- up(plan1,proj21task1prop,approve).
:- up(code21,proj12task2prop,setstatus).
:- up(des11,proj12task1,write).
:- up(code11,proj11task1a,request).
:- up(code12,proj22task1a,request).
:- up(des22,proj11task2propa,approve).
:- up(mgr1,proj12task2a,read).
:- up(acc2,proj21task2prop,setschedule).
:- up(ldr12,proj11task2a,setschedule).
:- up(code21,proj12task1propa,setstatus).
:- up(ldr2,proj21task1propa,request).
:- up(des12,proj11task1a,setstatus).
:- up(plan2,proj11sched,setschedule).
:- up(aud1,proj22task2a,setstatus).
:- up(mgr1,proj21task2a,approve).
:- up(code22,proj22task1prop,setschedule).
:- up(code22,proj12sched,write).
:- up(acc2,proj12task2,approve).
:- up(des21,proj12task2,write).
:- up(code21,proj21sched,setstatus).
:- up(des21,proj11task1prop,approve).
:- up(plan1,proj21task2a,setstatus).
:- up(des22,proj11budget,setschedule).
:- up(des22,proj12task2prop,request).
:- up(code21,proj21task1propa,request).
:- up(acc2,proj12task2prop,write).
:- up(mgr1,proj22task2,request).
:- up(des12,proj22task2,write).
:- up(mgr2,proj11task1propa,setcost).
:- up(ldr12,proj12task1,approve).
:- up(code22,proj21budget,setstatus).
:- up(des22,proj21task1propa,setcost).
:- up(code12,proj11sched,write).
:- up(ldr11,proj21task1a,setstatus).
:- up(des22,proj12task1a,write).
:- up(acc1,proj12task2prop,request).
:- up(plan1,proj21task2prop,approve).
:- up(ldr11,proj21task2propa,setstatus).
:- up(acc1,proj21task2,read).
:- up(plan2,proj12task1a,request).
:- up(ldr2,proj12task1,write).
:- up(acc2,proj12task2,setcost).
:- up(des12,proj21task2a,write).
:- up(code21,proj21task2,write).
:- up(code21,proj12task1,setstatus).
:- up(aud2,proj22budget,setcost).
:- up(plan1,proj11task2propa,request).
:- up(ldr12,proj22sched,write).
:- up(mgr2,proj22sched,write).
:- up(aud1,proj22task1a,approve).
:- up(acc1,proj11budget,request).
:- up(code12,proj22sched,approve).
:- up(ldr12,proj22task1propa,setcost).
:- up(ldr11,proj12task2,setcost).
:- up(ldr2,proj21task2prop,setcost).
:- up(plan2,proj11task2,request).
:- up(aud2,proj22task1,setcost).
:- up(code12,proj11task1,approve).
:- up(aud1,proj11task1,request).
:- up(code12,proj12task2prop,setstatus).
:- up(aud1,proj21task2a,approve).
up(des21,proj21task1prop,read).
:- up(aud2,proj11budget,setschedule).
:- up(aud2,proj22task1a,setschedule).
:- up(ldr2,proj22task2prop,setschedule).
:- up(ldr2,proj22budget,setstatus).
:- up(des21,proj21task2a,read).
:- up(des21,proj22budget,request).
:- up(ldr2,proj12task2propa,approve).
:- up(des11,proj22budget,write).
:- up(acc2,proj21task2,request).
:- up(acc1,proj12task2a,setstatus).
:- up(des12,proj11task1propa,request).
:- up(code11,proj21task1,write).
:- up(aud1,proj12task2propa,approve).
:- up(code22,proj22task2,write).
:- up(code11,proj12sched,read).
:- up(des22,proj22task2a,read).
:- up(plan1,proj11task2prop,setcost).
:- up(code11,proj21sched,read).
:- up(acc2,proj22task2,setstatus).
:- up(acc1,proj22sched,setschedule).
:- up(des22,proj12task2a,approve).
:- up(code21,proj22budget,setschedule).
:- up(ldr2,proj12task2prop,write).
:- up(code12,proj21task1propa,write).
:- up(code22,proj21task2propa,request).
:- up(code12,proj22task2,approve).
:- up(ldr12,proj22task1a,write).
:- up(des12,proj12task1a,setschedule).
:- up(des22,proj11task2,request).
:- up(acc2,proj12task1,request).
:- up(code11,proj22task2a,setstatus).
:- up(des12,proj21task2prop,approve).
:- up(ldr2,proj22task1prop,write).
:- up(code12,proj21sched,request).
:- up(ldr2,proj12task2a,read).
:- up(des21,proj22task1propa,write).
:- up(aud1,proj11task1prop,write).
:- up(aud1,proj12sched,setstatus).
:- up(ldr12,proj11budget,setstatus).
:- up(mgr2,proj11task2,setstatus).
:- up(ldr2,proj12task2prop,setstatus).
:- up(mgr2,proj22task1prop,read).
:- up(code12,proj12task1prop,approve).
:- up(aud1,proj12task2prop,setstatus).
:- up(code12,proj12task2prop,approve).
:- up(code11,proj21task2a,request).
:- up(des21,proj12task2propa,setcost).
:- up(code12,proj12task2a,write).
:- up(mgr2,proj12task1,read).
:- up(des21,proj12sched,setstatus).
:- up(acc1,proj12task2propa,setstatus).
:- up(des12,proj22task1prop,setstatus).
:- up(code21,proj22task1propa,setcost).
:- up(aud1,proj22task2,setstatus).
:- up(des12,proj22budget,request).
:- up(acc2,proj11task2prop,approve).
:- up(ldr2,proj11sched,write).
:- up(acc2,proj11task2,write).
:- up(mgr1,proj21task2,read).
:- up(acc2,proj12sched,read).
:- up(des22,proj22task2prop,setschedule).
:- up(ldr12,proj21task2propa,approve).
:- up(des11,proj11task2,setcost).
:- up(mgr2,proj21task1a,setschedule).
:- up(aud1,proj12budget,approve).
:- up(ldr2,proj11task1propa,setcost).
:- up(acc1,proj22task1prop,read).
:- up(code12,proj22task1prop,write).
:- up(code22,proj12budget,read).
:- up(acc1,proj22budget,setschedule).
:- up(mgr2,proj21task1propa,read).
:- up(ldr2,proj12budget,write).
:- up(ldr12,proj11task2,setcost).
up(code22,proj22task2propa,setstatus).
:- up(aud2,proj22task1,setschedule).
:- up(des11,proj12budget,request).
:- up(code21,proj11task2propa,setstatus).
:- up(des11,proj22task2,setschedule).
:- up(aud1,proj22task1,read).
:- up(mgr2,proj11task1propa,request).
:- up(code22,proj21budget,read).
up(plan1,proj11sched,read).
:- up(code22,proj11sched,approve).
:- up(code11,proj21task2prop,setschedule).
:- up(plan1,proj12task2prop,approve).
:- up(des11,proj11task1,approve).
:- up(ldr2,proj22task1a,setstatus).
:- up(code21,proj12task2prop,approve).
:- up(des22,proj11task1a,setstatus).
:- up(code22,proj22task1propa,request).
:- up(code22,proj22task1propa,approve).
:- up(code22,proj21budget,request).
:- up(aud1,proj22budget,setcost).
:- up(aud2,proj22task2,approve).
:- up(code22,proj12task2prop,setstatus).
:- up(des11,proj22task2prop,read).
:- up(mgr2,proj11budget,setstatus).
:- up(acc1,proj11task1a,setstatus).
:- up(mgr1,proj12task1,read).
:- up(mgr1,proj21sched,read).
:- up(plan2,proj22task1propa,setcost).
:- up(ldr2,proj11task1prop,approve).
:- up(des11,proj12task2propa,setcost).
:- up(code22,proj11task1a,read).
:- up(aud1,proj12task2propa,setstatus).
:- up(aud1,proj22sched,setschedule).
:- up(code12,proj22task2propa,write).
:- up(mgr1,proj22task2,setcost).
:- up(des21,proj11task2propa,setschedule).
:- up(des12,proj12task2propa,write).
:- up(code11,proj22task2,read).
:- up(acc2,proj22budget,setschedule).
:- up(code12,proj22task2prop,approve).
:- up(plan1,proj11task2a,setstatus).
:- up(aud1,proj12budget,setschedule).
:- up(mgr1,proj12task2propa,approve).
:- up(des21,proj12task2prop,approve).
:- up(plan1,proj22task1propa,setstatus).
:- up(des21,proj22task1a,request).
:- up(des11,proj11task2propa,setschedule).
:- up(aud2,proj21task2prop,setschedule).
:- up(acc1,proj11task2propa,request).
:- up(plan2,proj12task2propa,write).
:- up(ldr11,proj12budget,request).
:- up(des12,proj22task1propa,setschedule).
up(plan2,proj21task1propa,setschedule).
:- up(ldr11,proj11task1a,request).
:- up(plan1,proj12task2a,setcost).
:- up(des11,proj11task1propa,setschedule).
:- up(acc1,proj12task2prop,read).
:- up(mgr1,proj12sched,setcost).
:- up(aud1,proj11task1prop,setstatus).
up(acc2,proj21task2prop,setcost).
:- up(code22,proj12sched,read).
:- up(ldr2,proj21budget,request).
:- up(acc2,proj12task2,setstatus).
:- up(aud1,proj12task1,setschedule).
:- up(aud1,proj21task2,approve).
:- up(code21,proj22budget,setstatus).
:- up(code22,proj11budget,setcost).
:- up(code21,proj22sched,setstatus).
:- up(plan1,proj12task2,approve).
:- up(mgr1,proj12task1a,request).
:- up(ldr12,proj11budget,setcost).
:- up(code12,proj11task1propa,setschedule).
:- up(mgr2,proj22budget,write).
:- up(mgr1,proj22budget,read).
:- up(aud2,proj11task1propa,write).
:- up(aud2,proj12task1prop,approve).
:- up(acc1,proj21task1,write).
:- up(code12,proj22task1,write).
:- up(acc1,proj12task1prop,request).
:- up(code12,proj22task1prop,request).
:- up(des12,proj21sched,write).
:- up(aud1,proj21task1propa,setcost).
:- up(code11,proj12sched,setschedule).
up(acc2,proj21budget,read).
:- up(des21,proj22sched,read).
:- up(code12,proj11sched,setstatus).
:- up(ldr2,proj21task2,approve).
:- up(ldr2,proj12budget,approve).
:- up(des11,proj12task2prop,approve).
:- up(code21,proj12task1,setcost).
:- up(des11,proj21task1propa,read).
:- up(plan2,proj12budget,write).
up(des22,proj22task1propa,setstatus).
:- up(code12,proj11task2prop,approve).
:- up(des22,proj11task2,setcost).
:- up(aud2,proj11task1prop,setschedule).
:- up(des12,proj21task2prop,setcost).
:- up(ldr11,proj21task2a,setschedule).
:- up(des22,proj22task1,setschedule).
:- up(des12,proj21task2prop,write).
:- up(ldr12,proj21task1prop,request).
:- up(plan1,proj12task2propa,write).
:- up(code21,proj21task2,setschedule).
:- up(mgr2,proj11task1a,setschedule).
:- up(code21,proj22task2propa,read).
:- up(acc1,proj21budget,request).
:- up(mgr1,proj22task2a,setstatus).
:- up(aud1,proj11task2a,read).
:- up(acc2,proj22task2propa,request).
:- up(code22,proj21task2a,setschedule).
:- up(code22,proj21task1propa,approve).
:- up(ldr2,proj12task1,approve).
:- up(ldr2,proj12task2propa,write).
:- up(code21,proj11task2,write).
:- up(des12,proj11budget,write).
:- up(code11,proj21task1propa,write).
:- up(mgr1,proj11sched,write).
:- up(mgr1,proj22task1propa,setschedule).
:- up(ldr11,proj11task1prop,setstatus).
:- up(acc1,proj11task2propa,setstatus).
:- up(code21,proj22task1a,request).
:- up(code12,proj22task1prop,setschedule).
:- up(acc1,proj12task2a,setschedule).
:- up(code21,proj12task2,write).
:- up(plan2,proj21budget,setschedule).
:- up(des22,proj21task2,approve).
:- up(acc2,proj11task2propa,setstatus).
:- up(des11,proj21task1,approve).
:- up(ldr11,proj21task1prop,approve).
up(plan2,proj21task2a,setschedule).
:- up(des12,proj12task2propa,approve).
:- up(des12,proj11task2,setcost).
:- up(code22,proj12task2prop,setschedule).
:- up(des21,proj11task2a,write).
:- up(ldr2,proj12task2a,write).
:- up(plan1,proj21task1prop,read).
:- up(ldr12,proj12task2propa,write).
:- up(des11,proj12task2,setcost).
:- up(aud2,proj21task1a,read).
:- up(plan2,proj12task2prop,setcost).
:- up(code21,proj21task2propa,setcost).
:- up(code22,proj12task2,request).
:- up(des11,proj11sched,setcost).
:- up(des11,proj21task2a,setschedule).
:- up(acc2,proj22task2a,read).
:- up(mgr2,proj12task1a,setstatus).
:- up(mgr1,proj22task2propa,setcost).
:- up(ldr12,proj11budget,setschedule).
:- up(aud1,proj22task1,write).
:- up(des22,proj21task1prop,request).
:- up(mgr1,proj12task2propa,setcost).
:- up(ldr12,proj12sched,setschedule).
:- up(code22,proj21task2propa,write).
:- up(plan1,proj11task2propa,setcost).
:- up(acc1,proj12task2prop,setstatus).
:- up(aud2,proj11task1,write).
:- up(acc2,proj11task2propa,setcost).
:- up(des21,proj22task2propa,write).
:- up(des21,proj21task1propa,setschedule).
:- up(acc1,proj22task2prop,approve).
:- up(mgr2,proj21task1,setstatus).
:- up(des11,proj21task1prop,setstatus).
:- up(code12,proj21sched,setcost).
:- up(mgr1,proj22sched,setstatus).
:- up(des21,proj12task2propa,approve).
:- up(des21,proj12task2,read).
:- up(plan2,proj22task2a,read).
:- up(mgr1,proj12task2,request).
:- up(acc2,proj12task2prop,setcost).
:- up(aud1,proj22task2propa,write).
:- up(code12,proj11task1,setschedule).
:- up(mgr2,proj12task2propa,setcost).
:- up(des22,proj12task2propa,write).
:- up(code22,proj21task1prop,setcost).
:- up(des22,proj21task2propa,setstatus).
:- up(ldr12,proj21sched,approve).
:- up(code21,proj21task1prop,setstatus).
:- up(des21,proj21budget,setstatus).
:- up(des22,proj21task1a,setstatus).
:- up(ldr11,proj21task2,setcost).
:- up(aud1,proj12task1prop,setstatus).
:- up(acc1,proj21budget,write).
:- up(des22,proj11task1prop,setschedule).
:- up(ldr2,proj21task2a,setcost).
:- up(des11,proj22task2a,setschedule).
:- up(ldr11,proj22task1propa,setcost).
:- up(aud2,proj11task1,read).
:- up(code22,proj11budget,read).
:- up(code21,proj12task2prop,request).
:- up(acc2,proj11sched,setschedule).
:- up(code21,proj12task1prop,read).
:- up(ldr11,proj11task2a,setcost).
:- up(mgr1,proj22task2a,setschedule).
:- up(ldr11,proj12budget,setstatus).
:- up(acc1,proj21budget,setstatus).
:- up(ldr12,proj21sched,setcost).
:- up(mgr1,proj11task2,approve).
:- up(des11,proj11task2a,request).
:- up(des11,proj11task2propa,setstatus).
:- up(plan1,proj22budget,setstatus).
:- up(acc2,proj21task1prop,approve).
:- up(code21,proj11budget,write).
:- up(acc2,proj11task1prop,read).
:- up(plan1,proj12task1prop,approve).
:- up(ldr12,proj21sched,setstatus).
:- up(des21,proj21task1,setcost).
:- up(mgr2,proj22task2,setschedule).
:- up(aud1,proj21task1prop,write).
:- up(des12,proj21task2prop,request).
:- up(code11,proj22task1prop,write).
:- up(ldr11,proj12task2propa,write).
:- up(ldr11,proj22task1propa,write).
:- up(aud2,proj22sched,request).
:- up(plan2,proj11budget,approve).
:- up(aud2,proj21sched,setstatus).
:- up(code21,proj11task1propa,setcost).
:- up(mgr2,proj21task1,read).
:- up(ldr11,proj12task2propa,approve).
:- up(ldr2,proj22task2a,setcost).
up(ldr11,proj11budget,read).
:- up(plan2,proj21task2propa,approve).
:- up(des11,proj22task1prop,approve).
up(code22,proj22task2,request).
:- up(aud1,proj11task2,setschedule).
:- up(ldr12,proj11task1prop,request).
:- up(code12,proj21budget,write).
:- up(aud1,proj21task1propa,read).
:- up(code11,proj21task2propa,setcost).
:- up(ldr11,proj12task2propa,setstatus).
:- up(plan2,proj12task2propa,setstatus).
:- up(des22,proj21task1prop,setstatus).
:- up(des22,proj22task1propa,setcost).
:- up(code22,proj22task1a,setcost).
:- up(acc2,proj11task1propa,write).
:- up(ldr12,proj21task1a,setcost).
:- up(acc2,proj22task2propa,setschedule).
:- up(code11,proj21task1propa,approve).
:- up(aud2,proj12task2a,write).
up(acc1,proj12task1propa,setcost).
:- up(code21,proj12sched,setschedule).
:- up(plan1,proj11task2,request).
:- up(mgr1,proj21task1prop,write).
:- up(ldr12,proj11sched,request).
:- up(code11,proj22task1a,setcost).
:- up(code21,proj11task2,setstatus).
:- up(des11,proj21task1,setschedule).
:- up(mgr1,proj11task2propa,read).
:- up(des22,proj11task1propa,approve).
:- up(des12,proj21budget,setschedule).
:- up(mgr1,proj22task1prop,read).
:- up(mgr1,proj11task1a,write).
:- up(acc1,proj11task2a,request).
:- up(plan2,proj22task1,write).
:- up(code22,proj11task2propa,setcost).
:- up(mgr1,proj12task2,setstatus).
:- up(code21,proj12task2a,request).
:- up(des21,proj22sched,setschedule).
:- up(des12,proj11task2,write).
:- up(aud2,proj21sched,setcost).
up(acc1,proj11task2prop,setcost).
:- up(ldr2,proj11task2,request).
:- up(aud1,proj21sched,setcost).
:- up(code11,proj11task1propa,request).
:- up(acc1,proj12task2propa,approve).
up(code11,proj11task2propa,read).
:- up(des11,proj11task2a,setstatus).
:- up(code22,proj21budget,write).
:- up(des21,proj12task1a,write).
:- up(ldr2,proj11task1,approve).
up(acc1,proj12budget,read).
:- up(des12,proj11task1,write).
:- up(code21,proj11task2a,setstatus).
:- up(code22,proj11task2a,setcost).
:- up(mgr2,proj21sched,write).
:- up(des22,proj22task2a,setcost).
:- up(des11,proj22task2prop,write).
:- up(des11,proj11task1prop,setstatus).
:- up(aud2,proj12task2,request).
:- up(acc1,proj22sched,setstatus).
:- up(des12,proj22task1a,write).
up(plan2,proj22sched,write).
:- up(code11,proj21task2propa,request).
:- up(ldr11,proj11task2a,write).
:- up(plan1,proj22task1prop,approve).
:- up(plan2,proj11task1a,approve).
:- up(des22,proj21task2propa,write).
:- up(mgr2,proj22task1propa,request).
:- up(des11,proj11task1prop,write).
:- up(des12,proj11task2a,read).
:- up(code12,proj22task1prop,setcost).
:- up(plan2,proj22budget,write).
:- up(acc2,proj11task2,request).
:- up(plan1,proj21task2propa,approve).
:- up(acc1,proj21task2propa,setcost).
:- up(code21,proj22sched,approve).
:- up(code21,proj21sched,write).
:- up(plan1,proj22budget,read).
:- up(mgr1,proj12task2,read).
:- up(mgr2,proj12task1propa,setstatus).
:- up(aud1,proj12task1propa,write).
:- up(ldr11,proj22budget,read).
:- up(ldr12,proj21task2a,setcost).
:- up(code21,proj21budget,setcost).
:- up(code11,proj12task1a,setstatus).
:- up(aud1,proj22sched,setcost).
:- up(ldr2,proj22task2propa,setstatus).
:- up(code12,proj22sched,setcost).
:- up(code21,proj11task1propa,write).
:- up(aud2,proj22task2propa,setcost).
:- up(des12,proj21task2propa,read).
:- up(ldr2,proj22task2prop,write).
:- up(code21,proj12task1,approve).
:- up(plan2,proj12task2prop,setschedule).
:- up(plan2,proj22task2a,approve).
:- up(mgr1,proj12task1,request).
:- up(ldr11,proj22task1propa,read).
:- up(des12,proj22task2propa,request).
:- up(mgr2,proj22task1a,setcost).
:- up(des21,proj21task2a,request).
:- up(aud2,proj21task2prop,approve).
:- up(code12,proj22task2prop,setstatus).
:- up(mgr1,proj21budget,request).
:- up(des22,proj21task1propa,read).
:- up(ldr12,proj11task1prop,write).
:- up(acc2,proj11budget,write).
:- up(ldr2,proj12task2prop,setschedule).
:- up(ldr11,proj21task1propa,read).
:- up(des11,proj22task1,setschedule).
:- up(des11,proj21task1,setstatus).
:- up(plan2,proj22task1,setstatus).
:- up(plan1,proj22budget,setcost).
:- up(ldr12,proj11task1a,request).
:- up(des21,proj11sched,setstatus).
:- up(plan2,proj11task2propa,setstatus).
:- up(plan1,proj21task1,request).
:- up(acc2,proj11task1prop,setstatus).
:- up(ldr12,proj21task2,approve).
:- up(code12,proj21task1a,setstatus).
:- up(ldr11,proj12task1prop,request).
:- up(code21,proj22task2,write).
:- up(plan1,proj22task1a,request).
:- up(des21,proj11task1a,setschedule).
:- up(des12,proj12task1a,approve).
:- up(aud2,proj21task1,setcost).
:- up(mgr2,proj21task2,read).
:- up(aud1,proj11task1propa,setcost).
:- up(mgr1,proj22budget,request).
:- up(des21,proj21task1,approve).
:- up(code21,proj12task1,request).
:- up(mgr2,proj21task1prop,request).
:- up(acc1,proj21task1prop,setschedule).
:- up(ldr2,proj11task2prop,write).
:- up(code21,proj12budget,read).
up(des21,proj21task1a,setstatus).
:- up(aud2,proj22task1a,request).
:- up(des21,proj11task2prop,setcost).
:- up(code11,proj22budget,setcost).
:- up(plan2,proj21task1,setcost).
:- up(aud2,proj21task1a,write).
:- up(mgr2,proj21task1a,setstatus).
:- up(des22,proj21task2a,setcost).
:- up(ldr2,proj12task1propa,read).
:- up(code11,proj21task2,setschedule).
:- up(code12,proj22task1propa,write).
:- up(aud2,proj12task2a,setcost).
:- up(des11,proj22task1prop,setstatus).
:- up(acc1,proj11task1propa,approve).
:- up(code11,proj22sched,read).
:- up(code21,proj11task2a,read).
:- up(ldr2,proj22task1a,setcost).
:- up(code22,proj21task2a,request).
up(des11,proj11task1prop,read).
:- up(aud2,proj11task2propa,approve).
:- up(des12,proj22task1a,approve).
:- up(plan2,proj22task2,setcost).
:- up(code11,proj21task2,approve).
:- up(ldr11,proj21task1a,approve).
:- up(des21,proj12task2prop,setstatus).
:- up(ldr12,proj12task2prop,write).
:- up(ldr12,proj12task2,approve).
:- up(plan2,proj12task2,approve).
:- up(ldr12,proj12task1a,approve).
:- up(code21,proj11budget,read).
:- up(code22,proj12task1a,setstatus).
:- up(plan1,proj21task1propa,read).
:- up(aud2,proj22task1a,setstatus).
:- up(des21,proj12task1prop,read).
:- up(acc2,proj21task1prop,read).
:- up(code12,proj21task1propa,request).
:- up(plan1,proj21task2prop,setcost).
:- up(ldr12,proj12sched,approve).
:- up(mgr1,proj11budget,write).
:- up(des11,proj11budget,setstatus).
:- up(des21,proj21task2a,setschedule).
:- up(aud1,proj21sched,write).
:- up(aud1,proj22task2,setschedule).
:- up(code12,proj11task2a,approve).
:- up(code12,proj22task1,read).
:- up(acc2,proj12task2propa,approve).
:- up(code22,proj11task1,approve).
:- up(code11,proj22task2propa,read).
:- up(des12,proj21task1prop,read).
:- up(aud2,proj22task1prop,setcost).
:- up(code12,proj12task1a,setcost).
:- up(des21,proj21task1propa,setcost).
up(acc2,proj22task1a,setcost).
:- up(acc2,proj12task1prop,setcost).
:- up(code21,proj12task2,approve).
:- up(ldr12,proj12task1prop,setschedule).
:- up(ldr2,proj22sched,request).
:- up(des21,proj12task2prop,read).
:- up(acc1,proj12budget,setstatus).
:- up(aud2,proj12budget,setstatus).
:- up(code22,proj11sched,setschedule).
up(acc2,proj21task1prop,setcost).
:- up(ldr2,proj21sched,setstatus).
:- up(mgr2,proj11task1a,setstatus).
:- up(plan1,proj21budget,write).
:- up(code21,proj21budget,read).
:- up(aud1,proj21task1prop,approve).
:- up(des11,proj21sched,read).
:- up(aud2,proj21budget,request).
:- up(acc1,proj12task1propa,setstatus).
:- up(code11,proj21task1propa,setcost).
:- up(des22,proj11task2prop,approve).
:- up(plan1,proj11task1prop,approve).
:- up(aud1,proj21task1propa,write).
:- up(ldr2,proj11budget,write).
:- up(des11,proj21task2a,read).
:- up(aud1,proj11task1prop,setcost).
:- up(code21,proj22task1,setschedule).
:- up(mgr2,proj21task1,approve).
:- up(ldr11,proj22task2,read).
:- up(code22,proj22task1,setcost).
:- up(acc2,proj11task2,setstatus).
:- up(code11,proj21task1propa,setstatus).
:- up(mgr1,proj12task1a,write).
:- up(plan2,proj12sched,write).
:- up(ldr11,proj22task1a,read).
:- up(ldr2,proj12sched,request).
:- up(acc1,proj11sched,approve).
:- up(code12,proj22budget,read).
:- up(des21,proj12sched,write).
:- up(ldr2,proj22task1a,setschedule).
:- up(des22,proj12task2,read).
:- up(acc2,proj11task2,setcost).
:- up(mgr2,proj21budget,request).
:- up(ldr11,proj12task1a,write).
:- up(des21,proj11budget,write).
:- up(code12,proj22task1propa,approve).
:- up(ldr2,proj11sched,setschedule).
:- up(plan2,proj11task2propa,write).
:- up(code22,proj11task1prop,setschedule).
:- up(code21,proj12task1propa,setschedule).
:- up(mgr2,proj12sched,setcost).
:- up(code12,proj22task2prop,setschedule).
:- up(acc1,proj21task1,setcost).
:- up(code22,proj11task1a,request).
:- up(aud2,proj22task2,setschedule).
:- up(acc1,proj12task2prop,approve).
:- up(des21,proj21task2prop,setschedule).
:- up(acc1,proj21sched,approve).
:- up(mgr2,proj12task1,request).
:- up(plan2,proj11task2a,write).
:- up(acc1,proj22budget,approve).
:- up(ldr12,proj22task1prop,write).
:- up(ldr12,proj11task2a,approve).
:- up(plan1,proj21budget,setstatus).
:- up(acc1,proj11task1,approve).
:- up(aud2,proj22task1,request).
:- up(ldr11,proj21task2,setstatus).
:- up(ldr2,proj21sched,request).
up(ldr2,proj22sched,write).
:- up(code22,proj21task1,setstatus).
:- up(ldr2,proj21task1propa,read).
:- up(code21,proj12sched,setstatus).
:- up(code11,proj11task1propa,read).
:- up(ldr11,proj11task1propa,request).
:- up(mgr1,proj12task1,setstatus).
:- up(code22,proj21task1,setschedule).
:- up(ldr11,proj11task1propa,write).
:- up(acc2,proj22task2,setschedule).
:- up(mgr1,proj21task1,approve).
:- up(code11,proj22task2,write).
:- up(code21,proj21task1a,request).
:- up(code21,proj11task1,setschedule).
:- up(acc1,proj12task2propa,request).
:- up(code12,proj12budget,setcost).
:- up(des12,proj22task2propa,setcost).
:- up(plan2,proj12task2,setcost).
:- up(acc1,proj11task2propa,write).
:- up(code22,proj11sched,write).
:- up(code22,proj22task2,setstatus).
:- up(des21,proj21sched,setstatus).
:- up(code11,proj21task1,setcost).
:- up(plan1,proj12task2prop,write).
:- up(ldr11,proj21task2prop,approve).
:- up(ldr12,proj11budget,read).
:- up(ldr11,proj22task1propa,request).
:- up(acc1,proj11task2prop,read).
:- up(des22,proj22task2,write).
:- up(code12,proj21task2,request).
:- up(ldr11,proj12task2a,setstatus).
:- up(des12,proj22task2a,request).
:- up(des12,proj21task1propa,setschedule).
:- up(mgr2,proj21task1,request).
:- up(acc2,proj21task2a,setstatus).
:- up(mgr1,proj22task2,setstatus).
:- up(acc2,proj21sched,write).
:- up(code12,proj21task2propa,setstatus).
:- up(des22,proj22task2propa,request).
:- up(des22,proj11task2a,setschedule).
:- up(code12,proj21task2propa,setcost).
:- up(plan2,proj22task2a,request).
:- up(des22,proj11task1a,write).
:- up(acc1,proj11task1,setstatus).
:- up(ldr2,proj22task1a,write).
:- up(code12,proj22task1,setstatus).
:- up(des21,proj21sched,request).
:- up(acc2,proj12budget,setschedule).
:- up(ldr12,proj12task1propa,setschedule).
:- up(plan1,proj11task2prop,write).
:- up(code22,proj21task1prop,read).
:- up(des22,proj21budget,request).
:- up(acc2,proj12sched,setcost).
:- up(acc2,proj12task2prop,read).
:- up(des21,proj22task2,request).
:- up(aud2,proj22task2prop,write).
:- up(ldr12,proj21task2a,approve).
:- up(mgr1,proj11task1prop,request).
:- up(des11,proj21sched,setschedule).
:- up(acc1,proj11task2prop,setstatus).
:- up(acc1,proj21task1prop,write).
:- up(mgr1,proj22task2a,read).
:- up(des12,proj12task1prop,setstatus).
:- up(acc2,proj22task1a,setstatus).
:- up(aud2,proj12sched,approve).
:- up(acc2,proj21task2propa,approve).
:- up(plan1,proj21task1prop,write).
:- up(code22,proj22task1,setschedule).
:- up(plan2,proj11task1propa,setschedule).
:- up(des12,proj12task1,approve).
:- up(mgr1,proj12task2a,request).
:- up(code11,proj22task1propa,setcost).
:- up(des22,proj21task1,setstatus).
:- up(des21,proj22task1propa,request).
:- up(code12,proj21task2a,read).
:- up(code12,proj21budget,setstatus).
:- up(acc1,proj12task2a,read).
:- up(des22,proj12task2a,write).
:- up(acc2,proj11task2prop,request).
up(acc2,proj22task2propa,setcost).
:- up(aud1,proj22task1prop,read).
:- up(ldr12,proj22budget,setcost).
:- up(plan1,proj12task1,request).
:- up(plan1,proj12task2a,approve).
:- up(code21,proj21task1,setcost).
up(des22,proj22sched,read).
:- up(code12,proj12task2,approve).
:- up(plan1,proj12task1,setcost).
:- up(plan2,proj21sched,setschedule).
:- up(ldr2,proj22task1a,request).
:- up(ldr12,proj11task1prop,setcost).
:- up(aud1,proj22task2a,setcost).
:- up(des11,proj22task1propa,approve).
:- up(des12,proj22task2prop,setcost).
:- up(acc2,proj11task1a,setstatus).
:- up(des22,proj11task2propa,write).
:- up(aud1,proj22sched,request).
:- up(ldr12,proj21task1prop,read).
:- up(aud2,proj22task1propa,read).
:- up(code12,proj22task1propa,setcost).
up(des22,proj22task1a,request).
:- up(ldr12,proj12task1,request).
:- up(ldr12,proj22task1,approve).
:- up(ldr2,proj22sched,approve).
:- up(ldr12,proj11task2propa,setschedule).
:- up(plan2,proj12sched,setschedule).
:- up(plan1,proj21task1a,read).
:- up(plan1,proj12task2a,write).
:- up(ldr11,proj21task1propa,setcost).
:- up(des11,proj21task2,read).
:- up(ldr12,proj21task1,write).
:- up(acc2,proj21task2,write).
:- up(des22,proj12task2a,read).
:- up(code21,proj21task2,setstatus).
up(acc2,proj22sched,read).
:- up(ldr2,proj22task2,read).
:- up(aud1,proj22task2a,request).
:- up(ldr11,proj11task2a,approve).
:- up(acc2,proj21task2a,read).
:- up(des12,proj11task2prop,read).
:- up(aud1,proj22task1prop,setschedule).
:- up(plan1,proj12budget,read).
:- up(code21,proj12task1,setschedule).
:- up(des12,proj21task1,request).
:- up(code12,proj11task2propa,approve).
:- up(acc1,proj11budget,approve).
:- up(ldr12,proj22task1a,setcost).
:- up(code21,proj12task1a,setstatus).
:- up(plan2,proj12task2prop,write).
:- up(des22,proj11sched,setcost).
:- up(acc2,proj12task2propa,setcost).
:- up(acc1,proj21task2,approve).
:- up(aud2,proj21budget,approve).
:- up(ldr11,proj11task1propa,setschedule).
:- up(code12,proj22task2a,setschedule).
:- up(code22,proj12task1propa,request).
:- up(des12,proj21task1,setstatus).
:- up(code21,proj11budget,setstatus).
:- up(ldr12,proj11task2,write).
:- up(ldr12,proj22task2propa,setstatus).
:- up(acc1,proj21task2a,read).
:- up(des21,proj11task1prop,write).
:- up(code11,proj21task1prop,request).
:- up(plan2,proj22sched,setcost).
up(plan1,proj12task1propa,setschedule).
:- up(plan1,proj11budget,setschedule).
:- up(mgr2,proj21task2a,setcost).
:- up(aud2,proj22task2a,read).
:- up(plan1,proj22task2,setschedule).
:- up(des22,proj12task2a,setcost).
:- up(code21,proj22task2a,write).
:- up(code11,proj12task1prop,setcost).
:- up(acc1,proj21sched,setcost).
:- up(mgr2,proj11task1prop,approve).
:- up(ldr2,proj11task2,read).
:- up(ldr11,proj11task2propa,request).
:- up(des12,proj11task2,read).
:- up(des22,proj11task2propa,request).
:- up(aud1,proj22task2a,setschedule).
:- up(aud1,proj12task1a,setstatus).
:- up(code11,proj22task2,request).
:- up(acc2,proj22task1propa,write).
:- up(code22,proj21task2prop,approve).
:- up(aud2,proj22sched,write).
:- up(plan1,proj21task2propa,setstatus).
:- up(code11,proj11task1prop,setschedule).
:- up(ldr11,proj22task1propa,setschedule).
:- up(aud2,proj11task2prop,setcost).
:- up(des21,proj11task1,setstatus).
:- up(code21,proj11task2prop,approve).
:- up(plan1,proj22task1prop,setstatus).
:- up(plan1,proj22task1prop,request).
:- up(aud1,proj11sched,approve).
:- up(code12,proj22task2,setcost).
:- up(ldr12,proj22task2propa,approve).
:- up(aud2,proj21task1prop,setschedule).
:- up(code11,proj11task1prop,read).
:- up(des11,proj21task1a,setschedule).
:- up(plan1,proj22task1propa,write).
up(acc1,proj11task1prop,setcost).
:- up(mgr1,proj11task2a,setschedule).
:- up(acc2,proj22task1propa,setstatus).
:- up(des11,proj12task2propa,read).
:- up(mgr2,proj22task2,read).
:- up(plan1,proj12task2,setstatus).
:- up(mgr1,proj12task1,approve).
:- up(aud2,proj11budget,approve).
:- up(acc1,proj11task2,approve).
:- up(mgr1,proj21task2prop,setschedule).
:- up(des11,proj12task2prop,setschedule).
:- up(code12,proj11task2a,setcost).
:- up(acc2,proj12task2propa,read).
:- up(aud1,proj12budget,setcost).
:- up(ldr2,proj21task1,write).
:- up(code22,proj11task2propa,read).
:- up(acc2,proj21task1a,setschedule).
:- up(acc1,proj22task2prop,request).
:- up(code11,proj22task1prop,read).
:- up(code21,proj11sched,setcost).
:- up(des11,proj12task1prop,setschedule).
:- up(des12,proj12task1,setstatus).
:- up(des22,proj12task1prop,request).
:- up(plan2,proj12task2,read).
:- up(ldr11,proj11task1,setstatus).
:- up(plan2,proj11task2prop,read).
:- up(code12,proj12task1prop,setschedule).
:- up(mgr2,proj12task2,read).
:- up(ldr2,proj11budget,setcost).
up(aud1,proj11budget,read).
:- up(des21,proj11task2,write).
:- up(plan1,proj11task2prop,approve).
:- up(plan1,proj12task1,read).
:- up(des22,proj11task2propa,read).
up(acc1,proj11sched,read).
:- up(acc2,proj21task2a,approve).
:- up(plan1,proj21task1,read).
:- up(code21,proj12task2propa,read).
:- up(des11,proj22task2a,read).
:- up(code11,proj12task1prop,approve).
:- up(plan1,proj21task1,setcost).
:- up(acc1,proj11task2,read).
:- up(ldr12,proj12task2a,read).
:- up(mgr2,proj22task1,setcost).
:- up(des11,proj12task1prop,setstatus).
:- up(ldr12,proj12task1,setcost).
:- up(des21,proj11task2prop,request).
:- up(mgr2,proj12budget,read).
:- up(code11,proj12budget,setcost).
:- up(ldr12,proj11task1a,setcost).
:- up(acc2,proj21task1propa,setstatus).
:- up(mgr2,proj12task1a,setcost).
:- up(code12,proj12task1a,read).
:- up(code21,proj22task1prop,setstatus).
:- up(plan2,proj12budget,approve).
:- up(code22,proj22task1,read).
:- up(mgr2,proj22task2a,read).
:- up(plan1,proj11task1,read).
:- up(code22,proj11task1propa,setschedule).
:- up(code21,proj21task1propa,read).
:- up(ldr12,proj12task2a,write).
:- up(code12,proj11task1a,setstatus).
:- up(plan2,proj11task2,write).
up(acc1,proj11task1propa,setcost).
:- up(ldr12,proj11task1,write).
:- up(des12,proj12task2propa,setcost).
:- up(code11,proj11sched,request).
up(acc1,proj12task2,setcost).
:- up(ldr11,proj11task1prop,setcost).
:- up(mgr2,proj22task2prop,setstatus).
:- up(ldr12,proj21task1prop,setstatus).
:- up(des12,proj22task1prop,approve).
:- up(des11,proj11task2,approve).
:- up(aud1,proj21task2propa,setcost).
:- up(des22,proj21task2prop,setcost).
:- up(ldr11,proj21budget,setschedule).
:- up(code12,proj22task2,setstatus).
:- up(code22,proj11task1,read).
:- up(code22,proj22task2propa,setschedule).
:- up(code11,proj12task2a,setstatus).
:- up(aud1,proj21budget,write).
:- up(des12,proj11task2,setschedule).
:- up(des21,proj22task1,write).
:- up(plan2,proj21budget,setstatus).
:- up(acc1,proj21sched,setstatus).
:- up(code22,proj11task2propa,request).
:- up(code22,proj11task2,request).
:- up(code12,proj22task2propa,approve).
:- up(des21,proj21sched,setschedule).
:- up(plan2,proj11task1propa,setstatus).
:- up(des12,proj11task1,read).
:- up(ldr12,proj22task1prop,setstatus).
:- up(code21,proj22task2propa,setcost).
:- up(plan2,proj22task1propa,setstatus).
:- up(code12,proj11task1a,request).
:- up(mgr2,proj12sched,read).
:- up(code12,proj11task2propa,request).
:- up(des12,proj21task1propa,request).
:- up(acc1,proj22task1,setschedule).
:- up(code21,proj12task1a,read).
:- up(mgr2,proj21task2propa,request).
:- up(ldr2,proj12task1prop,approve).
:- up(mgr2,proj22sched,request).
:- up(code12,proj22task1a,write).
:- up(code12,proj12budget,request).
:- up(code12,proj12task2a,approve).
:- up(code21,proj12task1a,setschedule).
:- up(acc2,proj12task2a,write).
:- up(acc2,proj21task1,read).
:- up(code22,proj22sched,approve).
:- up(aud1,proj12task2prop,approve).
:- up(des21,proj12task1propa,write).
:- up(des11,proj12task1,read).
:- up(code21,proj11budget,setschedule).
:- up(mgr2,proj11task2a,setschedule).
:- up(ldr11,proj11task1propa,setstatus).
:- up(acc1,proj12budget,setcost).
:- up(ldr2,proj11task2,setstatus).
:- up(plan2,proj22task1propa,approve).
:- up(acc1,proj12sched,request).
:- up(plan2,proj22task2propa,read).
:- up(code21,proj11task2,setcost).
:- up(code12,proj11task1,request).
:- up(plan2,proj11budget,read).
:- up(mgr1,proj12task1a,read).
:- up(plan2,proj11task2prop,write).
:- up(plan1,proj21sched,write).
:- up(mgr2,proj11sched,setschedule).
:- up(plan1,proj22task2,write).
:- up(acc2,proj21task1,write).
:- up(code21,proj11task2propa,read).
:- up(plan1,proj12task1a,approve).
:- up(ldr2,proj11task1propa,approve).
:- up(des21,proj11task2propa,read).
:- up(code22,proj12task1a,read).
:- up(acc2,proj21sched,approve).
:- up(ldr2,proj12task1prop,write).
:- up(code11,proj11task2,setschedule).
:- up(des22,proj12task2prop,read).
:- up(des11,proj12task2prop,read).
:- up(des11,proj12task2propa,setstatus).
:- up(ldr11,proj22task1a,setstatus).
:- up(plan1,proj21task2a,setschedule).
:- up(code11,proj21budget,approve).
:- up(ldr11,proj11task2,read).
:- up(aud1,proj21task1prop,setstatus).
:- up(des11,proj11budget,setschedule).
:- up(code21,proj22task2a,approve).
:- up(ldr12,proj11task1propa,setschedule).
:- up(code21,proj12task2prop,write).
up(acc1,proj12sched,read).
:- up(ldr11,proj11task2,approve).
:- up(ldr2,proj12task1,read).
:- up(code21,proj11task1a,approve).
:- up(acc2,proj22sched,setschedule).
:- up(ldr12,proj21task2propa,setcost).
:- up(des22,proj21budget,setstatus).
:- up(ldr2,proj22task1prop,request).
:- up(des21,proj11task1a,request).
:- up(acc1,proj22budget,read).
:- up(plan2,proj21task1propa,approve).
:- up(acc1,proj12task2,read).
up(acc1,proj12task1prop,setcost).
:- up(aud2,proj11sched,approve).
:- up(ldr11,proj22task1,request).
:- up(mgr2,proj12task2propa,request).
:- up(ldr11,proj21task2a,approve).
:- up(des11,proj11task2a,setschedule).
:- up(ldr12,proj12task2,setschedule).
:- up(ldr12,proj11task2propa,setcost).
:- up(plan2,proj22task1prop,write).
:- up(acc2,proj22budget,setcost).
:- up(des12,proj22task2,read).
:- up(code11,proj22task1,read).
:- up(plan1,proj22task2a,setschedule).
:- up(mgr2,proj22task1a,read).
:- up(acc2,proj11task2a,read).
:- up(ldr11,proj21task2,request).
:- up(acc2,proj21budget,approve).
:- up(plan2,proj11task1a,write).
:- up(aud1,proj21budget,request).
:- up(aud1,proj11task1propa,setstatus).
:- up(plan2,proj21task2,setstatus).
:- up(plan1,proj21task1propa,write).
:- up(code12,proj21sched,setschedule).
:- up(plan1,proj22task2propa,setcost).
:- up(ldr12,proj21task1a,setschedule).
:- up(acc2,proj12task2a,setcost).
:- up(acc2,proj11task2propa,request).
:- up(mgr1,proj12task1propa,request).
:- up(plan2,proj21budget,request).
:- up(des11,proj11task1propa,write).
:- up(acc1,proj12task1,approve).
:- up(acc1,proj22task2propa,read).
:- up(code12,proj22task1,approve).
:- up(code11,proj12task1,write).
:- up(mgr2,proj11task2,approve).
:- up(aud2,proj22task1,approve).
:- up(plan2,proj21task1propa,read).
:- up(des21,proj22task2,setstatus).
:- up(des12,proj11task2propa,approve).
:- up(plan1,proj12task1a,read).
:- up(ldr12,proj11task2propa,approve).
:- up(mgr1,proj21task2a,setcost).
:- up(des12,proj21task2a,read).
:- up(des22,proj11task1,read).
:- up(acc1,proj22task2a,read).
:- up(code11,proj22budget,approve).
:- up(code12,proj11task2prop,read).
:- up(ldr11,proj11budget,setcost).
:- up(mgr1,proj21task2prop,request).
:- up(des21,proj11task2a,setstatus).
:- up(aud2,proj21sched,write).
:- up(mgr2,proj12task2prop,approve).
:- up(code12,proj22task1prop,read).
:- up(des12,proj12task1propa,approve).
:- up(mgr2,proj11task1prop,write).
:- up(ldr12,proj12task1propa,read).
:- up(ldr12,proj21budget,setstatus).
:- up(ldr11,proj21task1propa,request).
:- up(acc2,proj11sched,setstatus).
:- up(aud2,proj11task1prop,request).
:- up(acc1,proj11task1a,request).
:- up(code12,proj21task2a,setschedule).
:- up(code12,proj11budget,write).
:- up(acc1,proj22task1,approve).
:- up(mgr1,proj11task2propa,setschedule).
:- up(plan1,proj12budget,request).
:- up(code22,proj11task1propa,setstatus).
up(aud2,proj21budget,read).
:- up(plan1,proj12task2prop,request).
:- up(code21,proj21task1,write).
:- up(ldr11,proj22task1a,write).
:- up(ldr12,proj22sched,setcost).
:- up(ldr11,proj22task2a,write).
:- up(ldr2,proj12task1propa,setstatus).
:- up(des11,proj21task2propa,approve).
:- up(mgr2,proj12task1propa,read).
:- up(des11,proj22task2,setstatus).
:- up(ldr12,proj11task2a,setcost).
:- up(code22,proj11task1a,setschedule).
:- up(ldr12,proj22budget,request).
:- up(aud2,proj11task1a,request).
:- up(code21,proj21sched,setschedule).
:- up(ldr2,proj22task1prop,setschedule).
:- up(des11,proj21budget,read).
:- up(ldr12,proj11task2a,request).
:- up(ldr2,proj21task1propa,write).
:- up(aud1,proj12task1a,write).
:- up(des12,proj11task1prop,read).
:- up(mgr1,proj11task2propa,write).
:- up(code12,proj22budget,approve).
:- up(des21,proj12task2a,read).
:- up(acc2,proj11task2,read).
:- up(code12,proj21task1prop,read).
:- up(ldr2,proj12task1,setschedule).
:- up(ldr11,proj11budget,approve).
:- up(acc2,proj11sched,read).
:- up(des21,proj22task1propa,setschedule).
:- up(code12,proj12task1propa,setcost).
:- up(code12,proj21task1,setcost).
:- up(ldr11,proj21task2propa,read).
:- up(ldr11,proj21task2a,setstatus).
:- up(code12,proj22task2,read).
:- up(ldr2,proj12budget,request).
:- up(ldr2,proj21task2propa,read).
:- up(mgr2,proj11task1,read).
:- up(plan2,proj11task2,setcost).
:- up(mgr1,proj12task2,setschedule).
:- up(ldr2,proj21task2a,setstatus).
:- up(des22,proj21task1propa,setstatus).
:- up(code22,proj22task2prop,write).
:- up(des22,proj21task1propa,write).
:- up(ldr2,proj21task2propa,setcost).
:- up(des21,proj21task2a,approve).
:- up(des22,proj22budget,request).
:- up(code11,proj22budget,write).
:- up(code12,proj21task2a,setcost).
:- up(des12,proj12budget,request).
:- up(ldr11,proj22sched,setschedule).
:- up(acc2,proj12task1a,read).
:- up(des12,proj11sched,setschedule).
:- up(acc1,proj22task2,request).
:- up(code21,proj22task1propa,approve).
:- up(aud1,proj11budget,request).
:- up(des11,proj12task2a,approve).
:- up(code12,proj12task1prop,request).
:- up(des11,proj12task1a,write).
:- up(code22,proj22task1a,setstatus).
:- up(mgr2,proj22task1,approve).
:- up(plan2,proj11task1propa,write).
:- up(ldr2,proj12task1prop,setstatus).
:- up(code11,proj11task2,approve).
:- up(code22,proj11task1prop,approve).
:- up(acc2,proj22task1,write).
:- up(ldr12,proj22task1prop,read).
:- up(des11,proj21budget,setstatus).
:- up(des11,proj12task2,request).
:- up(des21,proj22budget,setstatus).
:- up(mgr1,proj21task1a,write).
:- up(plan2,proj21task2propa,read).
:- up(plan1,proj21task2a,read).
:- up(code21,proj12task2a,setschedule).
:- up(code11,proj11sched,setschedule).
:- up(des22,proj12sched,setschedule).
:- up(ldr2,proj11sched,setstatus).
:- up(code21,proj12budget,write).
:- up(acc1,proj11task1prop,setschedule).
:- up(acc1,proj22task2,write).
:- up(des22,proj22task2a,approve).
:- up(mgr1,proj22task1propa,setcost).
:- up(aud2,proj22task1,setstatus).
:- up(code11,proj12task2prop,write).
:- up(ldr12,proj11task2a,write).
:- up(plan2,proj21task1a,read).
:- up(ldr11,proj21task1a,setschedule).
:- up(code11,proj12task1,approve).
:- up(ldr12,proj12task2,setcost).
:- up(ldr11,proj12task2propa,setcost).
:- up(mgr2,proj22budget,setstatus).
:- up(plan2,proj21task1prop,write).
:- up(ldr12,proj21sched,read).
:- up(acc2,proj22task2prop,setschedule).
:- up(code11,proj22sched,approve).
:- up(des11,proj12sched,write).
:- up(code11,proj22task2,setschedule).
:- up(acc2,proj11task1propa,request).
:- up(des22,proj11task2,setstatus).
:- up(aud1,proj22task1,setstatus).
:- up(plan2,proj12task1a,setschedule).
:- up(code11,proj22task1propa,read).
:- up(des12,proj12budget,write).
:- up(aud2,proj12task1propa,request).
:- up(plan2,proj11task1propa,approve).
:- up(code11,proj22task1a,approve).
:- up(ldr12,proj22budget,write).
:- up(acc2,proj21task2propa,write).
:- up(code22,proj12task1,approve).
:- up(mgr2,proj11task1a,request).
:- up(acc1,proj21task1,setstatus).
:- up(ldr11,proj11sched,request).
:- up(code21,proj22task2propa,approve).
:- up(acc2,proj12task2propa,write).
:- up(ldr12,proj21task1,setcost).
:- up(aud2,proj11task2,approve).
:- up(des11,proj21task2a,write).
:- up(ldr2,proj12budget,setschedule).
:- up(des12,proj11task1propa,approve).
:- up(des12,proj22task1a,read).
:- up(code12,proj11budget,approve).
:- up(code21,proj22task1a,write).
:- up(des22,proj12task2prop,setstatus).
:- up(ldr12,proj11task2prop,setcost).
:- up(des22,proj11task1propa,setschedule).
:- up(code21,proj12sched,setcost).
:- up(mgr2,proj22task1,write).
:- up(mgr1,proj22task2,setschedule).
:- up(des21,proj11task1,write).
:- up(des22,proj12task1,setstatus).
:- up(des11,proj22task1propa,setschedule).
:- up(ldr12,proj21task2,request).
:- up(mgr2,proj11task2,setcost).
:- up(ldr2,proj22sched,setstatus).
up(code22,proj22task2,read).
:- up(des22,proj12budget,write).
:- up(code22,proj12task2propa,request).
up(ldr2,proj22budget,read).
:- up(acc2,proj22task1propa,read).
:- up(code12,proj11budget,setstatus).
:- up(code22,proj21task2propa,approve).
:- up(code21,proj22task2,approve).
:- up(acc2,proj12budget,write).
:- up(code12,proj21sched,setstatus).
:- up(des11,proj11budget,approve).
:- up(acc1,proj21task1propa,setcost).
:- up(code22,proj22task2prop,setcost).
:- up(code11,proj21task1,setstatus).
:- up(acc1,proj22task2a,setschedule).
:- up(ldr2,proj22task2,approve).
:- up(ldr11,proj12task1,setschedule).
:- up(mgr2,proj12task2propa,approve).
:- up(des22,proj21budget,approve).
:- up(ldr12,proj12task1propa,approve).
:- up(code12,proj21task1a,write).
up(ldr11,proj11sched,read).
:- up(plan2,proj22task2a,setcost).
:- up(acc2,proj11task2propa,read).
:- up(ldr12,proj11sched,write).
:- up(ldr2,proj21task2propa,approve).
:- up(des21,proj12task2a,setstatus).
:- up(des22,proj11task1,setcost).
:- up(aud1,proj11task2prop,write).
:- up(des12,proj12task2prop,approve).
:- up(code22,proj21task1propa,request).
:- up(acc2,proj12budget,read).
:- up(plan2,proj12task2a,setcost).
:- up(acc2,proj21task1propa,approve).
:- up(des12,proj21task2propa,approve).
:- up(code12,proj12task2prop,setcost).
:- up(mgr1,proj11task2propa,setstatus).
:- up(ldr12,proj21task1,read).
:- up(mgr2,proj22task1prop,setstatus).
:- up(code12,proj22task2prop,setcost).
:- up(aud2,proj21task2prop,setstatus).
up(code11,proj11task2,read).
:- up(aud2,proj11task1a,read).
:- up(aud2,proj22task1prop,read).
:- up(code11,proj12task1,setschedule).
:- up(ldr2,proj12task1a,write).
:- up(ldr11,proj12budget,approve).
:- up(plan2,proj12task2prop,approve).
up(des11,proj11task1prop,request).
:- up(des12,proj11task1propa,read).
:- up(des11,proj22task2propa,setschedule).
:- up(des11,proj22task1,request).
:- up(aud2,proj21task1propa,setschedule).
:- up(code11,proj21task2prop,setstatus).
:- up(ldr12,proj21task1a,approve).
:- up(des22,proj11task1propa,read).
up(plan2,proj21task1a,setschedule).
:- up(plan2,proj12sched,setstatus).
:- up(acc2,proj11task1a,request).
:- up(plan1,proj21task1propa,setstatus).
:- up(code21,proj11sched,read).
:- up(ldr12,proj11task1,request).
:- up(plan2,proj11task1a,request).
:- up(plan2,proj22sched,setschedule).
:- up(ldr12,proj11budget,write).
:- up(code21,proj22budget,setcost).
:- up(plan1,proj21budget,request).
:- up(code22,proj21task2a,approve).
:- up(des22,proj21task1prop,read).
:- up(des12,proj22task1propa,request).
:- up(plan2,proj21task2propa,write).
:- up(acc2,proj22task2a,approve).
:- up(mgr2,proj11task2propa,approve).
:- up(plan1,proj12task1propa,approve).
:- up(des21,proj11task1,setschedule).
:- up(des21,proj12sched,request).
:- up(mgr1,proj22task1propa,read).
:- up(code22,proj11task1propa,approve).
:- up(code11,proj12task2,approve).
up(acc2,proj22task1prop,setcost).
:- up(aud1,proj22task1prop,request).
:- up(aud2,proj11sched,setcost).
:- up(code11,proj21task1,setschedule).
:- up(mgr2,proj12task1prop,approve).
:- up(code12,proj22task2prop,write).
:- up(des21,proj12task1,setstatus).
:- up(code11,proj12task2propa,approve).
:- up(acc2,proj21task2propa,setschedule).
:- up(des11,proj22task2prop,setcost).
:- up(mgr1,proj11task1propa,read).
:- up(des21,proj22task2a,request).
:- up(acc1,proj21sched,request).
:- up(acc2,proj21sched,setcost).
:- up(mgr1,proj11task1prop,write).
:- up(ldr2,proj21task1prop,setschedule).
:- up(ldr11,proj22task1,write).
:- up(code12,proj21task1,approve).
:- up(aud2,proj12task2prop,setcost).
:- up(code21,proj21task1a,setcost).
:- up(ldr12,proj21budget,setcost).
:- up(plan1,proj22task1prop,read).
:- up(ldr2,proj21task2prop,write).
:- up(ldr2,proj12task2prop,read).
:- up(des12,proj22task2a,setstatus).
:- up(plan1,proj11task1,approve).
:- up(plan1,proj11task2a,read).
:- up(code11,proj22task2propa,setcost).
:- up(des21,proj12task1prop,setcost).
:- up(des12,proj12task1prop,approve).
:- up(plan2,proj12task2,write).
:- up(aud1,proj12budget,write).
:- up(code22,proj12task2,setschedule).
:- up(ldr2,proj21task1propa,approve).
:- up(des11,proj22task1a,setschedule).
:- up(ldr2,proj12task2propa,setschedule).
:- up(acc2,proj22budget,setstatus).
:- up(aud2,proj11task1,approve).
:- up(aud2,proj22task1a,read).
:- up(ldr12,proj22task1a,setschedule).
:- up(aud1,proj11task1propa,request).
:- up(des21,proj12sched,setcost).
:- up(mgr2,proj11task1propa,setschedule).
:- up(code21,proj22task2a,setcost).
:- up(code21,proj22task2prop,approve).
:- up(acc2,proj11task1prop,setcost).
:- up(mgr2,proj21task2a,write).
:- up(aud2,proj12budget,approve).
:- up(des21,proj21task1,write).
:- up(mgr2,proj12task1a,write).
:- up(ldr11,proj11task2a,request).
:- up(plan2,proj21task2propa,request).
:- up(mgr1,proj22task1propa,write).
:- up(des21,proj21task2a,setstatus).
:- up(mgr2,proj11task2propa,request).
:- up(des12,proj12task2a,setstatus).
up(ldr11,proj12sched,read).
:- up(ldr11,proj12task1propa,request).
:- up(code11,proj12task2propa,read).
:- up(plan2,proj12task1,setstatus).
:- up(plan2,proj11sched,write).
:- up(code22,proj12task1,read).
:- up(code22,proj12budget,setcost).
:- up(aud1,proj11task1propa,write).
:- up(ldr2,proj12task2,setstatus).
:- up(mgr2,proj11task2propa,setschedule).
:- up(ldr11,proj22budget,setcost).
:- up(des11,proj11task2prop,setstatus).
:- up(des11,proj11task2a,approve).
:- up(code22,proj12budget,approve).
:- up(plan2,proj12task2propa,approve).
:- up(aud1,proj11task1a,request).
:- up(ldr11,proj21task2a,request).
:- up(code21,proj22sched,read).
:- up(des21,proj22task2propa,setstatus).
:- up(des12,proj12task2,read).
:- up(des12,proj22sched,request).
:- up(aud1,proj22task2,request).
:- up(aud1,proj22task1prop,setcost).
:- up(code12,proj11task2a,write).
:- up(ldr12,proj11task2prop,write).
:- up(aud2,proj22task2propa,approve).
:- up(ldr11,proj11task2prop,request).
:- up(ldr2,proj12task2,setschedule).
:- up(mgr2,proj21task1propa,setschedule).
:- up(mgr2,proj12task2a,request).
:- up(ldr11,proj12task2a,read).
:- up(code21,proj11task2,approve).
:- up(mgr2,proj12task1a,setschedule).
:- up(ldr11,proj22task2a,setschedule).
:- up(ldr11,proj22task2a,setcost).
:- up(ldr11,proj22task2propa,approve).
:- up(acc1,proj22task2prop,setcost).
:- up(code22,proj12budget,write).
:- up(plan2,proj22task2prop,write).
:- up(ldr2,proj22task2prop,read).
:- up(code11,proj21task1a,read).
:- up(mgr1,proj21sched,setcost).
:- up(des11,proj21task2propa,write).
:- up(aud2,proj21task2prop,request).
:- up(aud1,proj12task2a,setstatus).
:- up(code21,proj22task1,approve).
:- up(des12,proj12task1propa,read).
:- up(aud2,proj22task2propa,read).
:- up(des22,proj11task2prop,read).
:- up(ldr11,proj21task2propa,setcost).
:- up(mgr2,proj11task2prop,setstatus).
:- up(code22,proj12task1propa,setschedule).
:- up(mgr1,proj11task1,read).
:- up(aud2,proj12task1prop,setstatus).
:- up(aud2,proj11task2a,setschedule).
:- up(ldr11,proj21task1,request).
:- up(des11,proj11task2a,read).
:- up(des22,proj21task2prop,write).
:- up(ldr11,proj21task1prop,setcost).
:- up(des21,proj11budget,setcost).
:- up(des21,proj11task1a,write).
:- up(ldr11,proj22task2a,setstatus).
:- up(code21,proj22task1prop,approve).
:- up(mgr2,proj22task1a,approve).
:- up(des11,proj11task1,setstatus).
:- up(aud1,proj21task1,approve).
:- up(des11,proj22task1propa,setstatus).
:- up(des12,proj22task1,setcost).
:- up(code22,proj22task1a,approve).
:- up(plan1,proj21task2a,write).
:- up(code12,proj11budget,request).
:- up(acc2,proj21task1,setschedule).
:- up(plan2,proj21task1,request).
:- up(ldr2,proj11task1propa,setstatus).
:- up(ldr2,proj11task2a,setschedule).
:- up(des11,proj21budget,approve).
:- up(des22,proj21task2propa,setschedule).
:- up(mgr1,proj21task2propa,read).
:- up(des11,proj21task1,setcost).
:- up(acc2,proj22task1propa,approve).
:- up(code11,proj22task1prop,setstatus).
:- up(code11,proj12sched,write).
:- up(des21,proj22task1,setstatus).
:- up(code12,proj21task1a,setschedule).
:- up(acc2,proj12sched,request).
:- up(code22,proj21task1propa,read).
:- up(acc2,proj12task2prop,request).
:- up(des22,proj21task2,read).
:- up(code12,proj11task2prop,setstatus).
:- up(acc1,proj22task2,setschedule).
:- up(acc2,proj22task2,approve).
:- up(code11,proj11task1prop,request).
:- up(mgr2,proj12task1prop,write).
:- up(des12,proj11task1,setschedule).
:- up(acc2,proj11task1a,read).
:- up(ldr2,proj12task2,write).
:- up(des21,proj11task2propa,setstatus).
:- up(mgr2,proj11budget,approve).
:- up(code12,proj22task1propa,setstatus).
:- up(plan2,proj21task2propa,setstatus).
:- up(des11,proj11budget,setcost).
:- up(plan2,proj22budget,setschedule).
:- up(des22,proj11task2a,write).
:- up(aud1,proj11task1prop,approve).
:- up(code22,proj22task1,request).
:- up(mgr2,proj12task2,approve).
:- up(code22,proj21task2propa,setschedule).
:- up(code11,proj22budget,request).
:- up(ldr11,proj22budget,setschedule).
:- up(mgr2,proj22task2propa,request).
:- up(des11,proj12budget,setschedule).
:- up(aud2,proj21sched,setschedule).
:- up(acc2,proj22task2,write).
:- up(des22,proj12task1a,setschedule).
:- up(ldr12,proj12task1prop,approve).
:- up(mgr2,proj22task1a,write).
:- up(code12,proj11task2,setstatus).
:- up(des22,proj21task2prop,read).
:- up(aud2,proj21task1,approve).
:- up(code22,proj12task2propa,read).
:- up(plan2,proj11task2prop,approve).
:- up(code11,proj21task2prop,setcost).
:- up(des12,proj12task1prop,read).
:- up(des11,proj22task1a,read).
:- up(code11,proj21task2a,approve).
:- up(code22,proj22task2,setcost).
:- up(aud2,proj22task2a,write).
:- up(mgr1,proj11task2a,approve).
:- up(plan1,proj21task2,setstatus).
:- up(code11,proj21task2a,setschedule).
:- up(aud2,proj12budget,request).
:- up(des11,proj22budget,request).
:- up(mgr1,proj11task1,request).
:- up(acc1,proj21task1a,write).
:- up(mgr2,proj21task2a,setstatus).
:- up(code22,proj21budget,setcost).
:- up(mgr1,proj22budget,setschedule).
:- up(code21,proj11sched,setschedule).
:- up(mgr2,proj11task2prop,setschedule).
:- up(plan2,proj11task1,read).
:- up(code12,proj21task1prop,setstatus).
:- up(ldr2,proj22budget,setschedule).
:- up(plan2,proj22budget,request).
:- up(code11,proj21task2propa,approve).
:- up(ldr12,proj21task1propa,request).
:- up(ldr12,proj11task2prop,read).
:- up(mgr2,proj21task1a,read).
:- up(des12,proj11task1prop,approve).
:- up(code11,proj12task2prop,request).
:- up(plan1,proj21task1prop,request).
:- up(plan2,proj11task2a,setschedule).
:- up(ldr11,proj22sched,read).
:- up(code22,proj11task1prop,read).
:- up(code21,proj11task2propa,setcost).
:- up(code22,proj12task1prop,approve).
:- up(des11,proj12task1propa,request).
:- up(acc2,proj11task1,setstatus).
:- up(code11,proj12task1propa,setstatus).
:- up(code21,proj21budget,setstatus).
:- up(code21,proj12task1prop,setstatus).
:- up(acc2,proj11task2a,approve).
:- up(mgr2,proj21task2,approve).
:- up(des11,proj12task1propa,approve).
:- up(ldr11,proj12task1a,read).
:- up(code11,proj22task2a,read).
:- up(mgr2,proj21task2propa,read).
:- up(aud2,proj22task2,setcost).
:- up(code22,proj22task1a,write).
:- up(plan2,proj12task1propa,setcost).
:- up(mgr2,proj11task1prop,setstatus).
:- up(des22,proj11task1a,request).
:- up(ldr2,proj22sched,setschedule).
:- up(code21,proj22task2propa,write).
:- up(ldr11,proj12sched,write).
:- up(mgr1,proj22task1propa,approve).
:- up(code21,proj22task2a,request).
:- up(ldr12,proj12budget,request).
:- up(ldr12,proj21task2,setschedule).
:- up(acc2,proj11task1a,setschedule).
:- up(des21,proj12task2,setstatus).
:- up(ldr12,proj12task1propa,setstatus).
:- up(plan1,proj11sched,setcost).
:- up(des21,proj12task1prop,write).
:- up(ldr12,proj11task2propa,read).
:- up(ldr12,proj12task2,setstatus).
up(code11,proj11task2a,setstatus).
:- up(code12,proj11sched,setschedule).
:- up(plan1,proj11task1prop,write).
:- up(plan2,proj12task1a,setstatus).
:- up(des12,proj11task2prop,request).
:- up(ldr2,proj22task2,setschedule).
:- up(code12,proj22budget,setschedule).
:- up(acc2,proj11task2prop,write).
:- up(code12,proj12task1propa,setschedule).
:- up(code12,proj11task2,read).
:- up(plan1,proj22sched,setschedule).
:- up(des11,proj12task2a,request).
:- up(code12,proj12budget,read).
:- up(ldr12,proj11sched,read).
:- up(ldr11,proj22task2prop,setcost).
:- up(mgr1,proj22sched,write).
:- up(code12,proj12task2propa,write).
:- up(code12,proj12task1,request).
:- up(ldr11,proj21task2a,setcost).
:- up(code21,proj11task2prop,setstatus).
:- up(code12,proj22budget,setstatus).
:- up(acc2,proj12task1,setcost).
:- up(des22,proj22sched,setschedule).
:- up(plan1,proj22task2prop,setschedule).
up(plan1,proj12task2a,setschedule).
:- up(des11,proj21task2,setschedule).
:- up(des21,proj21task2prop,write).
:- up(code21,proj22task1,write).
:- up(des22,proj11task2a,read).
:- up(acc2,proj12task1propa,setcost).
:- up(des22,proj22task1,setstatus).
:- up(des21,proj21task2propa,read).
:- up(mgr1,proj22task1,setstatus).
:- up(code21,proj12task1prop,setschedule).
:- up(des11,proj21task2prop,approve).
:- up(des12,proj22task2prop,request).
:- up(code12,proj21task2propa,setschedule).
:- up(code11,proj12task2a,write).
up(plan1,proj12task1prop,setschedule).
:- up(ldr2,proj11sched,setcost).
:- up(aud1,proj11task2prop,setschedule).
:- up(des12,proj12task2a,request).
:- up(ldr11,proj21task1prop,write).
up(acc2,proj21task2,setcost).
:- up(code22,proj22task2prop,request).
:- up(aud2,proj22task1,write).
:- up(des21,proj21task2propa,request).
:- up(aud2,proj12task1,write).
:- up(code21,proj21task1propa,setcost).
:- up(code22,proj22task1prop,write).
:- up(acc2,proj21task1propa,request).
:- up(mgr1,proj22task2propa,setstatus).
:- up(ldr11,proj11task2propa,setcost).
:- up(des22,proj21task2,request).
:- up(ldr2,proj22task1,setschedule).
:- up(des21,proj12task1,read).
:- up(mgr1,proj22task1a,read).
up(plan2,proj22task1prop,setschedule).
:- up(des22,proj21task1,read).
:- up(des21,proj12task1prop,setstatus).
:- up(code22,proj21task1prop,setschedule).
:- up(code21,proj12task1propa,approve).
:- up(des12,proj12task2a,setcost).
:- up(code11,proj21task1a,setstatus).
:- up(acc2,proj11task1,read).
:- up(plan1,proj22task2,approve).
:- up(des11,proj21task2a,approve).
:- up(acc1,proj21task2prop,setschedule).
:- up(code22,proj22task1prop,approve).
:- up(mgr1,proj12task2a,setschedule).
up(acc1,proj11budget,read).
:- up(plan1,proj12task1,setstatus).
:- up(code21,proj12task1prop,write).
:- up(des21,proj22task2propa,approve).
:- up(code11,proj12task1prop,read).
:- up(ldr11,proj11task1prop,approve).
:- up(aud1,proj12task2prop,setschedule).
:- up(des21,proj22task1,read).
:- up(code12,proj12task2propa,read).
:- up(code22,proj12task2propa,approve).
:- up(ldr2,proj21task1a,request).
:- up(des21,proj22sched,setcost).
:- up(ldr2,proj11budget,setschedule).
:- up(code11,proj21task1prop,read).
:- up(code12,proj22task2a,approve).
:- up(code11,proj21task1prop,write).
:- up(des22,proj12task1propa,setschedule).
:- up(code21,proj12task2prop,setcost).
:- up(acc1,proj11task2propa,approve).
:- up(aud2,proj12task2a,read).
:- up(plan1,proj22task1,setcost).
:- up(des11,proj21task2a,request).
:- up(code22,proj11task2a,setschedule).
:- up(aud2,proj11task1a,setschedule).
:- up(mgr2,proj11task1,approve).
:- up(mgr2,proj11task2,write).
:- up(des11,proj21task1a,request).
:- up(des22,proj21sched,read).
:- up(aud1,proj22sched,write).
:- up(ldr12,proj22task2,request).
:- up(ldr2,proj12sched,setstatus).
:- up(ldr11,proj11task1,read).
up(des21,proj21task1prop,request).
:- up(plan2,proj11task1propa,request).
:- up(ldr2,proj11task1prop,setcost).
:- up(mgr1,proj12task2a,approve).
:- up(acc2,proj22task2propa,setstatus).
:- up(aud1,proj21task2a,setcost).
:- up(des12,proj21task1a,approve).
:- up(aud2,proj11task2propa,read).
:- up(mgr1,proj12task1,write).
:- up(mgr2,proj22sched,read).
:- up(des11,proj21sched,approve).
:- up(plan1,proj21task1a,setstatus).
:- up(aud1,proj21task2,request).
:- up(aud2,proj12task2propa,read).
:- up(plan1,proj22task1,approve).
:- up(ldr12,proj11task1prop,approve).
:- up(mgr1,proj22task2propa,setschedule).
:- up(code22,proj21task2propa,read).
:- up(code21,proj22task1,read).
:- up(ldr11,proj22task2propa,setcost).
:- up(ldr11,proj12task1propa,read).
:- up(ldr12,proj22task2a,write).
:- up(code22,proj21task1prop,write).
:- up(des12,proj21sched,setcost).
up(plan1,proj11task1prop,setschedule).
:- up(mgr1,proj22task2prop,approve).
:- up(des22,proj21task1,setschedule).
:- up(plan1,proj22task1a,setschedule).
:- up(plan1,proj12task1a,write).
:- up(aud1,proj11task1a,read).
:- up(des21,proj11task2a,read).
:- up(aud1,proj22sched,setstatus).
:- up(ldr2,proj22task2a,write).
:- up(des22,proj11task1,approve).
:- up(aud2,proj21task2,write).
:- up(aud2,proj21task2prop,setcost).
:- up(code22,proj12task2a,read).
:- up(des11,proj21task1propa,setschedule).
:- up(aud2,proj11task1a,setstatus).
:- up(mgr1,proj22task2prop,setcost).
up(plan2,proj21task2propa,setschedule).
:- up(des21,proj22task2propa,setcost).
:- up(ldr2,proj21task1prop,setstatus).
:- up(plan1,proj22task2prop,write).
:- up(aud1,proj11task2prop,read).
:- up(des22,proj12task1,setcost).
:- up(mgr1,proj22task2propa,read).
:- up(plan1,proj12task2propa,setstatus).
:- up(ldr2,proj21task1,read).
:- up(mgr1,proj11sched,setstatus).
:- up(ldr2,proj11budget,request).
:- up(mgr1,proj11task2prop,request).
:- up(plan1,proj21sched,approve).
:- up(code22,proj12task1,request).
:- up(code22,proj11task1a,write).
:- up(acc1,proj22task2,setstatus).
:- up(ldr12,proj22sched,setstatus).
:- up(ldr12,proj11task2prop,request).
:- up(acc1,proj21task2propa,setstatus).
:- up(aud2,proj21task2a,setschedule).
:- up(mgr1,proj21sched,setstatus).
:- up(mgr1,proj22task2,approve).
:- up(aud2,proj12sched,write).
:- up(aud2,proj22task2,request).
:- up(acc2,proj12task1prop,setstatus).
:- up(code22,proj12task2,setcost).
:- up(code12,proj22task2a,read).
:- up(code21,proj22task2propa,setstatus).
:- up(code11,proj21task2a,read).
:- up(mgr1,proj11task2,request).
:- up(plan2,proj22task1,setcost).
:- up(code22,proj11task1propa,write).
:- up(aud1,proj12sched,approve).
:- up(plan1,proj12task1prop,read).
:- up(ldr11,proj21task1a,read).
:- up(des21,proj21sched,setcost).
:- up(des11,proj22task1propa,setcost).
:- up(mgr2,proj21task2prop,setstatus).
:- up(plan1,proj11task2,read).
:- up(code12,proj12task2,setschedule).
:- up(des12,proj22sched,read).
:- up(code21,proj12task2a,setstatus).
:- up(des22,proj11budget,setstatus).
:- up(code12,proj11task1a,approve).
:- up(des22,proj12task1,write).
:- up(ldr2,proj22task2,write).
:- up(code11,proj22task2prop,request).
:- up(ldr2,proj21task2prop,request).
:- up(acc1,proj21task2propa,write).
:- up(ldr12,proj12budget,setstatus).
:- up(mgr2,proj22budget,request).
:- up(des12,proj11budget,setschedule).
:- up(des12,proj11task1a,approve).
:- up(acc2,proj12task2,setschedule).
:- up(code11,proj12task2prop,setstatus).
:- up(ldr2,proj22task2,setstatus).
:- up(des12,proj12task1prop,request).
:- up(plan2,proj22task1prop,setcost).
:- up(aud1,proj11task2a,write).
:- up(plan1,proj22task1a,write).
:- up(plan2,proj11sched,setstatus).
:- up(plan1,proj12task1propa,request).
:- up(code22,proj22task2propa,write).
:- up(plan1,proj11sched,setstatus).
:- up(mgr1,proj12task2,write).
:- up(des21,proj22task2,setcost).
:- up(code12,proj21task2prop,request).
:- up(des12,proj21task2,request).
:- up(code21,proj22task1propa,setstatus).
:- up(mgr2,proj11task2,request).
:- up(code22,proj12sched,setcost).
:- up(code21,proj12task2propa,setcost).
:- up(code21,proj11task1a,setstatus).
:- up(des22,proj21task1prop,approve).
:- up(mgr1,proj21task1,write).
:- up(acc1,proj21task2propa,request).
:- up(mgr2,proj12task2a,write).
:- up(plan2,proj12task1a,read).
:- up(ldr11,proj22task1propa,setstatus).
:- up(acc2,proj11task2prop,setschedule).
:- up(acc2,proj21budget,setschedule).
:- up(mgr2,proj21budget,write).
:- up(aud2,proj21task1a,approve).
:- up(aud1,proj21task1propa,setschedule).
:- up(mgr2,proj22task2propa,setstatus).
:- up(plan1,proj22task1,write).
:- up(aud1,proj12task2,write).
:- up(acc2,proj12task1a,setcost).
:- up(acc2,proj21task2,read).
:- up(mgr2,proj12budget,write).
:- up(des12,proj12sched,setcost).
:- up(ldr11,proj12sched,request).
:- up(des21,proj12task1propa,setcost).
:- up(plan1,proj12task2a,request).
:- up(des21,proj12budget,setcost).
:- up(acc1,proj11task1prop,setstatus).
:- up(ldr2,proj21task2a,request).
:- up(des22,proj22task1propa,read).
:- up(acc2,proj11task2a,write).
:- up(ldr11,proj11task2propa,setstatus).
:- up(des21,proj11task2,approve).
:- up(aud2,proj21task1a,setcost).
:- up(acc1,proj11task2prop,setschedule).
up(aud1,proj12sched,read).
:- up(aud2,proj22task2prop,read).
:- up(acc2,proj11task1,request).
:- up(code21,proj11sched,write).
:- up(mgr1,proj11task2,setcost).
:- up(ldr12,proj22sched,request).
:- up(des22,proj11task1,write).
:- up(code11,proj22task1propa,request).
:- up(aud1,proj22budget,read).
:- up(aud1,proj22task2propa,setcost).
:- up(mgr2,proj11task1propa,approve).
:- up(des22,proj22task2,setschedule).
:- up(aud1,proj22budget,setschedule).
:- up(des21,proj11sched,write).
:- up(ldr2,proj22task2,request).
:- up(acc1,proj21task2,write).
:- up(ldr2,proj11task1,setcost).
:- up(code12,proj11sched,setcost).
:- up(code12,proj22task1a,setstatus).
:- up(des12,proj11task2propa,setcost).
:- up(mgr2,proj12task1propa,approve).
:- up(code12,proj21task1prop,approve).
up(plan2,proj21task2prop,setschedule).
:- up(des22,proj12sched,setcost).
:- up(mgr2,proj21task2,request).
:- up(ldr2,proj12sched,setschedule).
:- up(code12,proj11task2,request).
:- up(mgr2,proj21sched,setschedule).
:- up(code21,proj22task2prop,setstatus).
:- up(ldr2,proj22task1,read).
:- up(code12,proj21task1,setstatus).
:- up(code22,proj11task2prop,setschedule).
:- up(acc1,proj11task2a,setstatus).
:- up(aud2,proj22task2prop,setschedule).
:- up(code21,proj21task2a,setschedule).
:- up(code11,proj11sched,approve).
:- up(ldr2,proj21task2a,write).
:- up(plan1,proj12task1propa,setstatus).
:- up(acc1,proj22task1a,setcost).
:- up(acc1,proj12sched,setcost).
:- up(mgr1,proj22sched,request).
:- up(acc2,proj22task1a,approve).
:- up(code11,proj11sched,setcost).
:- up(des12,proj21task2a,request).
:- up(ldr2,proj22task1,approve).
:- up(aud1,proj11task1prop,read).
:- up(des12,proj11task1a,setschedule).
:- up(aud2,proj22task1prop,setschedule).
:- up(plan1,proj11task1propa,setstatus).
:- up(des12,proj22task2a,approve).
:- up(acc2,proj21task2,setschedule).
:- up(aud2,proj22task1a,approve).
:- up(mgr1,proj22task1prop,setschedule).
:- up(aud2,proj22task2propa,request).
:- up(des21,proj12task1propa,read).
:- up(mgr1,proj12task2propa,setschedule).
:- up(code12,proj22task1a,setschedule).
:- up(aud2,proj12task1prop,write).
:- up(des11,proj12budget,setcost).
:- up(code22,proj11task2a,request).
:- up(mgr1,proj11task2prop,setcost).
up(acc2,proj22task1,setcost).
:- up(acc2,proj22task1a,request).
:- up(mgr1,proj12sched,write).
:- up(mgr2,proj12sched,write).
:- up(plan1,proj11task1a,write).
:- up(code12,proj12task1,write).
:- up(aud1,proj22task2,approve).
:- up(ldr11,proj11sched,setstatus).
:- up(ldr12,proj11task2prop,setstatus).
:- up(des12,proj12task2prop,write).
:- up(mgr1,proj12sched,setstatus).
:- up(acc1,proj11task2a,approve).
:- up(ldr11,proj22task2prop,approve).
:- up(code21,proj21task1propa,write).
:- up(ldr11,proj21budget,request).
up(acc1,proj12task2prop,setcost).
:- up(aud2,proj12task2a,approve).
:- up(ldr12,proj12task1prop,write).
:- up(plan1,proj22task1a,setstatus).
:- up(aud2,proj21task1propa,approve).
up(code11,proj11task2a,read).
:- up(aud2,proj11task1propa,read).
:- up(des12,proj22sched,setstatus).
:- up(plan1,proj22task2propa,setschedule).
:- up(ldr11,proj21task1prop,setstatus).
:- up(ldr12,proj21task2,setstatus).
:- up(code22,proj22task1propa,write).
:- up(code11,proj21task1,approve).
:- up(plan2,proj21task1prop,setstatus).
:- up(code21,proj12sched,approve).
:- up(aud1,proj22task2propa,setstatus).
up(mgr2,proj21budget,read).
:- up(code21,proj11task1,approve).
:- up(plan2,proj12task1prop,setstatus).
:- up(plan2,proj21task1propa,setcost).
:- up(des21,proj22task1a,write).
:- up(code11,proj22task2prop,setcost).
:- up(ldr12,proj11task1propa,request).
:- up(plan2,proj11task1a,setschedule).
:- up(ldr11,proj12task2propa,setschedule).
:- up(plan2,proj11sched,setcost).
:- up(aud1,proj22budget,setstatus).
:- up(des11,proj22task1a,setstatus).
:- up(code12,proj21task2prop,approve).
up(plan2,proj21sched,write).
:- up(acc2,proj21task1propa,read).
:- up(mgr2,proj12sched,approve).
:- up(plan1,proj12task1a,setstatus).
:- up(des11,proj12task2a,setstatus).
:- up(ldr2,proj11task1propa,read).
:- up(des21,proj21budget,approve).
:- up(acc2,proj21task2a,setschedule).
:- up(ldr11,proj11sched,approve).
:- up(mgr2,proj22task1a,setschedule).
:- up(ldr2,proj21task1a,setstatus).
:- up(mgr1,proj21budget,write).
:- up(des21,proj22task1,approve).
:- up(code11,proj12sched,request).
:- up(mgr2,proj22task2propa,setschedule).
:- up(acc2,proj22budget,approve).
:- up(acc1,proj22sched,request).
:- up(ldr12,proj21task2propa,request).
:- up(des21,proj12task1prop,setschedule).
:- up(code21,proj22task2,setstatus).
:- up(des21,proj12budget,setschedule).
:- up(aud1,proj21task2propa,write).
:- up(aud1,proj21task2propa,approve).
:- up(ldr11,proj12task1propa,setschedule).
:- up(plan2,proj22budget,setstatus).
:- up(mgr1,proj11budget,setcost).
:- up(des21,proj22budget,approve).
:- up(plan1,proj21task1prop,setcost).
:- up(plan1,proj11budget,setcost).
:- up(acc2,proj12task1propa,read).
:- up(acc2,proj22task1,setschedule).
:- up(des12,proj11task1prop,write).
:- up(mgr1,proj12task2prop,request).
:- up(aud2,proj12task2prop,read).
:- up(des22,proj11budget,read).
:- up(ldr12,proj11sched,approve).
:- up(aud1,proj22task2a,approve).
:- up(mgr1,proj21task2a,read).
:- up(ldr11,proj11task2,setcost).
:- up(mgr2,proj22task2a,request).
:- up(acc1,proj11task1a,write).
:- up(acc1,proj22task2prop,write).
:- up(des12,proj22task1a,setcost).
:- up(des12,proj21task1prop,setcost).
:- up(mgr2,proj12sched,request).
:- up(acc2,proj11task2a,setstatus).
:- up(ldr12,proj21task2prop,setschedule).
:- up(acc2,proj11task1a,approve).
:- up(code21,proj11task2propa,write).
:- up(des11,proj11task2prop,request).
:- up(des11,proj11task2a,write).
:- up(plan1,proj11task1propa,approve).
:- up(des21,proj12task2propa,read).
:- up(ldr12,proj22task2a,approve).
:- up(acc2,proj22task2prop,read).
:- up(ldr2,proj21task2a,read).
:- up(ldr2,proj11task1a,write).
:- up(des12,proj22task2prop,setstatus).
:- up(des21,proj11task1a,approve).
:- up(aud1,proj22task1propa,write).
:- up(code22,proj11task1prop,setcost).
:- up(des12,proj21task2,setstatus).
:- up(acc2,proj22task1a,setschedule).
:- up(mgr1,proj12task2prop,setschedule).
:- up(ldr12,proj11task2a,read).
:- up(code12,proj21budget,setschedule).
:- up(ldr12,proj12task1prop,setstatus).
:- up(aud2,proj22sched,setcost).
:- up(code21,proj12task2propa,write).
:- up(des11,proj22sched,setstatus).
:- up(code22,proj12task2prop,read).
:- up(code21,proj22task2prop,setcost).
:- up(plan1,proj21budget,setcost).
:- up(ldr11,proj12task1a,approve).
:- up(code22,proj11task1,request).
:- up(acc1,proj11budget,setcost).
:- up(code22,proj22task2,approve).
:- up(acc2,proj12sched,approve).
:- up(code22,proj22task2propa,approve).
:- up(code12,proj11budget,read).
up(plan2,proj21task1,setschedule).
:- up(des11,proj22sched,setcost).
:- up(plan2,proj12task1a,setcost).
:- up(ldr12,proj22task1,setschedule).
:- up(aud1,proj12task2propa,read).
:- up(ldr12,proj22task2prop,setcost).
:- up(plan2,proj21task2a,write).
up(des12,proj12task1a,request).
:- up(code12,proj21task1propa,setschedule).
:- up(ldr11,proj21task1,write).
:- up(code22,proj11task2prop,write).
:- up(plan1,proj22budget,setschedule).
:- up(aud2,proj21task1prop,read).
:- up(mgr1,proj11budget,request).
:- up(code11,proj22budget,setstatus).
:- up(plan1,proj21task2a,request).
:- up(mgr2,proj21task2prop,read).
:- up(code22,proj21task1,approve).
:- up(code12,proj21budget,read).
:- up(ldr11,proj11task2propa,write).
:- up(des12,proj21budget,approve).
:- up(des22,proj21task2,write).
up(plan1,proj11task2prop,setschedule).
:- up(plan2,proj11task2propa,read).
:- up(ldr12,proj12task2prop,setstatus).
:- up(code11,proj21task2prop,approve).
:- up(des22,proj21budget,setschedule).
:- up(code22,proj21task2prop,setcost).
:- up(acc2,proj21budget,request).
:- up(aud1,proj21sched,setstatus).
:- up(aud2,proj21budget,setstatus).
:- up(acc2,proj12task1prop,request).
:- up(aud2,proj22task1propa,approve).
:- up(des12,proj21task1prop,setstatus).
:- up(code12,proj12sched,write).
:- up(plan1,proj11task1propa,write).
:- up(ldr2,proj21task2,setschedule).
:- up(mgr2,proj21task1prop,read).
:- up(ldr11,proj21task2a,read).
:- up(plan1,proj22task1a,read).
:- up(ldr11,proj11task1prop,write).
:- up(code11,proj12task2prop,setschedule).
:- up(des22,proj11task1prop,request).
:- up(aud2,proj12task1,read).
:- up(acc1,proj12budget,setschedule).
:- up(ldr12,proj22task2propa,write).
up(acc2,proj21task1a,setcost).
:- up(code12,proj11task2a,setstatus).
:- up(des12,proj12task2propa,setstatus).
:- up(acc1,proj22sched,read).
:- up(aud1,proj11task1,approve).
:- up(des12,proj11budget,approve).
:- up(acc1,proj11task1prop,write).
:- up(des21,proj12task2prop,setschedule).
:- up(aud1,proj21task2,setstatus).
:- up(plan1,proj21budget,setschedule).
:- up(aud2,proj21task2propa,setschedule).
:- up(plan1,proj22task2,read).
:- up(ldr12,proj21task1a,request).
:- up(plan2,proj12task1prop,setschedule).
:- up(plan1,proj21task2,approve).
:- up(aud1,proj11task2prop,setcost).
:- up(aud1,proj12task2,request).
:- up(des11,proj12task1a,setschedule).
:- up(mgr2,proj11task2a,setstatus).
:- up(des12,proj22task2prop,approve).
:- up(mgr1,proj11task1prop,setstatus).
:- up(acc2,proj11task1propa,approve).
:- up(plan2,proj12task2propa,request).
:- up(code21,proj11task1propa,request).
:- up(mgr2,proj12task2a,setcost).
:- up(ldr12,proj21sched,write).
:- up(mgr2,proj11sched,request).
:- up(des22,proj12task1a,setcost).
:- up(plan1,proj12task1prop,setcost).
:- up(acc2,proj12task1a,setstatus).
:- up(aud1,proj11task1prop,setschedule).
:- up(des22,proj22task2,setstatus).
:- up(code11,proj12task2,setschedule).
:- up(des12,proj12task1,write).
:- up(mgr2,proj12task1prop,setschedule).
:- up(mgr2,proj11task1prop,setschedule).
:- up(plan1,proj22sched,setstatus).
:- up(code12,proj12task2prop,read).
:- up(code12,proj12task2,setcost).
:- up(des12,proj12task2,write).
:- up(plan1,proj21task2,setschedule).
:- up(aud1,proj22task2prop,approve).
:- up(acc1,proj22task1,request).
:- up(ldr2,proj21task1prop,write).
:- up(ldr12,proj21task2prop,approve).
:- up(acc1,proj21task2propa,read).
:- up(aud1,proj11task1propa,setschedule).
:- up(plan2,proj12task1propa,write).
:- up(ldr2,proj11task2,approve).
:- up(mgr1,proj12task1propa,setcost).
:- up(plan1,proj11task1a,approve).
:- up(mgr2,proj22task2,write).
:- up(acc1,proj11task1prop,read).
:- up(des11,proj21task2a,setcost).
:- up(acc2,proj12sched,setschedule).
:- up(plan2,proj12task1,write).
:- up(des22,proj11task2prop,setstatus).
:- up(ldr12,proj22task1a,read).
:- up(ldr11,proj11task1a,setschedule).
up(mgr1,proj11budget,approve).
:- up(plan2,proj21task1propa,request).
:- up(aud2,proj12budget,write).
:- up(code21,proj12sched,request).
:- up(des12,proj21task1propa,read).
:- up(des21,proj11task1a,setstatus).
:- up(ldr12,proj22task2prop,request).
:- up(plan2,proj21budget,setcost).
:- up(des21,proj12budget,setstatus).
:- up(des22,proj22sched,setstatus).
:- up(des12,proj12task2prop,setcost).
:- up(plan2,proj11task1prop,write).
:- up(code12,proj21task1,write).
:- up(des21,proj11task1prop,read).
:- up(ldr11,proj21task1,setstatus).
:- up(mgr2,proj11sched,setstatus).
:- up(mgr1,proj21task1prop,setschedule).
:- up(code11,proj12task1propa,approve).
:- up(des12,proj22task2propa,read).
:- up(acc1,proj11task1propa,setstatus).
:- up(aud1,proj11task2,approve).
:- up(des21,proj22task2a,setschedule).
:- up(acc2,proj11budget,request).
:- up(ldr2,proj11budget,read).
:- up(ldr2,proj21sched,approve).
:- up(acc1,proj21task2a,setstatus).
:- up(aud1,proj11task1,setschedule).
:- up(ldr11,proj12task1prop,write).
:- up(aud1,proj12task1a,approve).
:- up(des21,proj21task2propa,setschedule).
:- up(des21,proj11task1propa,write).
:- up(ldr2,proj12task2propa,setstatus).
:- up(code12,proj21task2,read).
:- up(aud2,proj22task1a,write).
:- up(code22,proj11budget,approve).
:- up(aud2,proj22task2propa,setstatus).
:- up(ldr2,proj11task2prop,setschedule).
:- up(plan2,proj12task1prop,read).
:- up(code12,proj22task2a,request).
:- up(des22,proj22task2,read).
:- up(mgr2,proj22task2prop,write).
:- up(des21,proj11task1a,setcost).
:- up(plan2,proj21task2,read).
:- up(acc1,proj22task1a,request).
:- up(acc1,proj21task1a,approve).
:- up(ldr11,proj11task2prop,read).
:- up(aud2,proj12task1a,setstatus).
:- up(aud2,proj12sched,setstatus).
:- up(ldr11,proj22task2prop,setschedule).
:- up(code12,proj12task1a,write).
:- up(ldr2,proj22budget,request).
:- up(code11,proj22task1,setschedule).
:- up(ldr11,proj12task2prop,request).
:- up(code22,proj21task1a,approve).
:- up(des12,proj12task1propa,write).
:- up(code22,proj21task2propa,setstatus).
:- up(des22,proj21sched,write).
:- up(des22,proj11sched,read).
:- up(mgr1,proj12task1,setschedule).
:- up(plan1,proj22task1prop,setschedule).
:- up(code21,proj22task1a,approve).
:- up(aud1,proj11sched,setstatus).
:- up(ldr12,proj21sched,setschedule).
:- up(des11,proj21task1prop,read).
:- up(acc2,proj21task1prop,setschedule).
:- up(mgr2,proj12task1a,request).
:- up(code22,proj22task1,approve).
up(acc2,proj22task2prop,setcost).
:- up(des22,proj21task1propa,approve).
:- up(aud1,proj11budget,approve).
:- up(code21,proj21task1a,read).
:- up(ldr11,proj12task1,setstatus).
:- up(code21,proj12task1propa,write).
:- up(plan2,proj22task1prop,approve).
:- up(mgr1,proj12sched,setschedule).
:- up(acc1,proj11sched,request).
:- up(code22,proj22task1prop,read).
up(code21,proj21task2,request).
:- up(code21,proj12task2a,write).
:- up(mgr2,proj21sched,setstatus).
:- up(aud1,proj21task2a,request).
:- up(aud2,proj12task1,setcost).
:- up(ldr11,proj11task2,write).
:- up(des22,proj11task1propa,setstatus).
:- up(des22,proj21task2prop,setstatus).
:- up(code12,proj12sched,setschedule).
:- up(code21,proj21task2propa,write).
:- up(des21,proj12task1a,setcost).
:- up(mgr1,proj12task2,approve).
up(code21,proj21task2propa,read).
:- up(ldr12,proj12task1propa,write).
:- up(code11,proj11task1,request).
:- up(acc2,proj21budget,setcost).
:- up(aud2,proj12task2propa,approve).
:- up(aud1,proj21sched,request).
:- up(code21,proj22task2a,setschedule).
:- up(mgr1,proj12task1propa,write).
:- up(code21,proj22task2prop,setschedule).
:- up(des12,proj22sched,write).
:- up(mgr1,proj22task2a,approve).
:- up(des12,proj12task1a,setcost).
:- up(code22,proj12task1,setstatus).
:- up(code12,proj22task2a,setstatus).
:- up(plan2,proj12budget,setschedule).
:- up(des22,proj11task1propa,request).
:- up(ldr11,proj11task2propa,read).
:- up(code11,proj21sched,setstatus).
:- up(plan1,proj12task1,approve).
:- up(mgr1,proj22task1a,request).
:- up(aud2,proj12budget,setcost).
:- up(code22,proj22task1propa,setschedule).
:- up(plan2,proj11sched,request).
:- up(acc1,proj21sched,read).
:- up(code22,proj21task1,write).
:- up(plan1,proj22task2a,read).
:- up(aud2,proj21task1propa,setstatus).
:- up(acc1,proj22task1prop,write).
:- up(code12,proj22task1,setschedule).
:- up(code11,proj12task1,setcost).
:- up(mgr1,proj12task1a,approve).
:- up(ldr11,proj21budget,setstatus).
:- up(code22,proj12budget,request).
:- up(code12,proj22task2propa,setstatus).
:- up(des12,proj12sched,request).
:- up(acc1,proj11task2prop,write).
:- up(acc2,proj21sched,request).
:- up(plan2,proj21sched,setstatus).
:- up(mgr2,proj22task1,read).
:- up(code12,proj21task1,setschedule).
:- up(code11,proj22task1propa,setschedule).
:- up(des12,proj21budget,setstatus).
:- up(des12,proj21sched,setschedule).
:- up(acc2,proj21task1a,write).
:- up(acc2,proj22task2prop,approve).
:- up(ldr11,proj22budget,approve).
:- up(code21,proj11task1propa,setstatus).
:- up(code11,proj22task1a,read).
:- up(plan2,proj22task2propa,setcost).
:- up(mgr2,proj21task1a,approve).
:- up(aud1,proj12task1,setcost).
:- up(mgr2,proj12task2propa,write).
:- up(des11,proj11task1,write).
:- up(des22,proj12budget,read).
:- up(code21,proj11task1prop,write).
:- up(acc2,proj22task2propa,write).
:- up(mgr2,proj11task2,read).
:- up(mgr1,proj21sched,write).
:- up(aud1,proj22sched,approve).
:- up(des21,proj22task1,request).
:- up(aud1,proj22task1prop,write).
:- up(acc1,proj12task2propa,read).
:- up(aud2,proj12task2,setcost).
:- up(mgr2,proj22task2a,setstatus).
:- up(ldr12,proj11task2propa,setstatus).
:- up(mgr2,proj21task1a,write).
:- up(mgr1,proj21budget,approve).
:- up(des11,proj22task1prop,setcost).
:- up(plan1,proj12sched,setcost).
:- up(acc2,proj12task1,approve).
:- up(mgr2,proj11budget,setcost).
:- up(ldr11,proj22task1a,request).
:- up(aud2,proj21task1a,setschedule).
:- up(des11,proj22task2,request).
:- up(des11,proj22task2prop,setschedule).
:- up(code22,proj11task2prop,read).
:- up(acc1,proj12task1,read).
:- up(plan1,proj11task2prop,setstatus).
:- up(des12,proj11task1propa,setcost).
:- up(acc2,proj11task1a,write).
:- up(des22,proj21task1propa,setschedule).
:- up(ldr2,proj11task2,setcost).
:- up(code22,proj11sched,request).
:- up(plan1,proj12budget,setstatus).
:- up(plan2,proj21budget,approve).
:- up(mgr2,proj21task1propa,write).
:- up(des22,proj22task1a,setschedule).
:- up(des11,proj12task2propa,write).
:- up(code12,proj12task1propa,read).
:- up(mgr2,proj21task1propa,setstatus).
:- up(des11,proj22sched,request).
:- up(ldr12,proj22task2propa,request).
:- up(ldr11,proj21budget,setcost).
:- up(des11,proj22task1a,setcost).
:- up(ldr11,proj11task2,setstatus).
:- up(code22,proj21task2,setcost).
:- up(mgr1,proj11task2prop,approve).
:- up(ldr12,proj22task2a,read).
:- up(aud1,proj21sched,setschedule).
:- up(code21,proj11task1propa,approve).
:- up(des11,proj12sched,request).
:- up(des12,proj21task2propa,setcost).
:- up(ldr12,proj22task1prop,setcost).
:- up(mgr1,proj11budget,setstatus).
:- up(acc2,proj12task2,read).
:- up(des11,proj11task2prop,approve).
:- up(code11,proj12budget,approve).
:- up(acc1,proj11task2,setstatus).
:- up(des22,proj11budget,setcost).
:- up(aud1,proj11sched,setcost).
:- up(des21,proj11budget,approve).
:- up(acc1,proj12task1propa,read).
up(code21,proj21task2propa,setstatus).
:- up(des12,proj22task2,setcost).
:- up(plan2,proj21task2prop,request).
:- up(code22,proj12task2a,setstatus).
:- up(mgr1,proj22task2propa,request).
:- up(des12,proj11sched,setstatus).
:- up(acc1,proj21task2prop,approve).
up(acc2,proj22task1propa,setcost).
:- up(plan2,proj21task2,write).
:- up(ldr11,proj12budget,write).
:- up(code22,proj22task1a,request).
:- up(code22,proj21task2a,read).
:- up(des12,proj21task2prop,setstatus).
:- up(acc1,proj11task1,request).
:- up(acc2,proj21budget,setstatus).
:- up(acc2,proj22task1a,write).
:- up(code21,proj11sched,approve).
:- up(code22,proj12task1prop,write).
:- up(code22,proj21sched,setstatus).
:- up(aud2,proj22task2prop,setcost).
:- up(ldr11,proj12task2prop,setschedule).
:- up(mgr1,proj21budget,read).
:- up(acc1,proj11budget,setstatus).
:- up(code11,proj21sched,request).
:- up(des22,proj21budget,write).
:- up(des12,proj12sched,approve).
:- up(mgr1,proj12budget,write).
:- up(plan2,proj21task2prop,setstatus).
:- up(mgr2,proj22task2prop,approve).
:- up(des12,proj21task2a,setschedule).
:- up(mgr2,proj11task1a,setcost).
:- up(aud2,proj11budget,request).
:- up(des12,proj11task1prop,request).
:- up(ldr11,proj12sched,setschedule).
:- up(code11,proj11task1prop,setcost).
:- up(plan2,proj11task2propa,approve).
:- up(plan1,proj22task2a,setstatus).
:- up(des11,proj21task1prop,write).
:- up(code12,proj21task2a,setstatus).
:- up(des12,proj11sched,read).
:- up(code12,proj22sched,setschedule).
:- up(ldr2,proj11task2a,write).
:- up(code22,proj22task2prop,approve).
:- up(des21,proj11task2,request).
:- up(mgr2,proj22task1propa,setschedule).
:- up(aud1,proj21task1a,request).
:- up(des21,proj22sched,approve).
:- up(des22,proj11budget,write).
:- up(mgr2,proj12task2,setcost).
:- up(plan2,proj22task2propa,setstatus).
:- up(code22,proj21sched,request).
:- up(aud1,proj22task1a,request).
:- up(aud2,proj22sched,setschedule).
:- up(des22,proj12task2a,request).
:- up(des12,proj21task1a,write).
:- up(mgr2,proj12task2a,setstatus).
:- up(aud2,proj21task1a,setstatus).
:- up(code21,proj22budget,write).
:- up(ldr11,proj11task1,request).
:- up(code11,proj11task1propa,write).
:- up(ldr12,proj21budget,write).
:- up(plan1,proj11task1propa,read).
:- up(acc2,proj12task1propa,approve).
:- up(code12,proj21task1prop,setschedule).
:- up(code22,proj21sched,read).
:- up(mgr2,proj22task2propa,approve).
:- up(des11,proj12task1prop,approve).
:- up(ldr11,proj12task1a,setcost).
:- up(ldr11,proj21task1propa,setstatus).
:- up(code22,proj11budget,setstatus).
:- up(aud2,proj12task1prop,read).
:- up(plan2,proj21task1a,request).
:- up(code12,proj21task1propa,approve).
:- up(acc1,proj21task2,setcost).
:- up(code12,proj22task2propa,setschedule).
:- up(acc1,proj12budget,approve).
:- up(aud2,proj21sched,approve).
:- up(des22,proj22task1propa,write).
:- up(des11,proj21sched,setstatus).
:- up(acc1,proj11task1,write).
:- up(ldr11,proj11task2prop,write).
:- up(mgr2,proj21task1propa,setcost).
:- up(mgr2,proj11sched,approve).
:- up(code12,proj21task1a,approve).
:- up(acc1,proj12task2,request).
up(mgr2,proj21budget,approve).
:- up(des21,proj12task1propa,approve).
:- up(acc2,proj11budget,read).
:- up(des11,proj21task1prop,setcost).
:- up(des11,proj22task2propa,approve).
:- up(code12,proj11task1propa,setstatus).
:- up(code11,proj22task1propa,setstatus).
:- up(code21,proj11task1,setcost).
up(plan2,proj22task1a,setschedule).
:- up(plan2,proj12task1propa,request).
:- up(des21,proj12task2a,setcost).
:- up(mgr1,proj12task1propa,setstatus).
:- up(ldr12,proj22task1prop,approve).
:- up(ldr11,proj11task1prop,read).
:- up(acc1,proj21task1prop,request).
:- up(aud1,proj12task2a,setschedule).
:- up(mgr2,proj21task2,setstatus).
:- up(des11,proj22task2a,write).
:- up(des12,proj22budget,setcost).
:- up(acc1,proj21task1propa,read).
:- up(des21,proj11task2a,request).
:- up(code12,proj21task2prop,setschedule).
:- up(aud2,proj22budget,setstatus).
up(code11,proj11task2a,request).
:- up(des21,proj22budget,setcost).
:- up(acc2,proj22task1,read).
:- up(plan1,proj21task1prop,setschedule).
:- up(code11,proj21sched,setschedule).
:- up(ldr12,proj11task2propa,write).
:- up(plan1,proj21sched,request).
:- up(code22,proj21budget,approve).
:- up(des12,proj22task1,setstatus).
:- up(code22,proj11task2,read).
:- up(code11,proj22sched,setschedule).
:- up(aud1,proj21task2,setcost).
:- up(des21,proj21task1,setschedule).
:- up(code22,proj22task1prop,request).
:- up(ldr12,proj11task1,setcost).
:- up(aud2,proj11task2propa,write).
:- up(mgr2,proj22task2prop,setschedule).
:- up(aud1,proj12task1prop,setcost).
:- up(code11,proj11budget,setschedule).
:- up(acc1,proj12task1,request).
:- up(ldr2,proj12task2a,setschedule).
:- up(code22,proj21task2prop,request).
:- up(aud2,proj12task1a,approve).
:- up(code12,proj12budget,setschedule).
:- up(code12,proj21task2propa,approve).
up(des21,proj21task1,request).
:- up(acc1,proj21budget,approve).
:- up(acc2,proj22task2prop,setstatus).
:- up(des11,proj12task1a,approve).
:- up(des11,proj21task2propa,setschedule).
:- up(code22,proj11task1,setstatus).
:- up(plan1,proj22task2propa,read).
:- up(plan2,proj21task2prop,setcost).
:- up(des21,proj22task2prop,approve).
:- up(aud1,proj11task1a,write).
:- up(ldr11,proj11task2a,setschedule).
:- up(mgr1,proj22task1a,approve).
:- up(ldr12,proj12task1propa,setcost).
:- up(ldr12,proj11budget,request).
:- up(mgr2,proj22task1propa,read).
:- up(code21,proj11task2propa,setschedule).
:- up(mgr1,proj21task1propa,read).
:- up(mgr1,proj11task1,approve).
:- up(aud2,proj12task1prop,setschedule).
:- up(code12,proj11task2propa,setstatus).
:- up(des11,proj12task1propa,setcost).
:- up(des22,proj22task2prop,setstatus).
:- up(des12,proj11task1a,request).
:- up(plan2,proj11task1prop,approve).
:- up(des11,proj22task1prop,setschedule).
:- up(mgr1,proj12task2propa,setstatus).
:- up(aud2,proj12sched,setschedule).
:- up(code11,proj11task2prop,setschedule).
:- up(des11,proj12task1a,request).
:- up(ldr11,proj12task1a,setstatus).
:- up(ldr2,proj21task1,setschedule).
:- up(des22,proj22task2a,setschedule).
:- up(acc1,proj21task1,request).
:- up(code11,proj11budget,setstatus).
:- up(plan2,proj11budget,setstatus).
:- up(des21,proj11task2prop,setschedule).
:- up(des12,proj11task1propa,setstatus).
:- up(ldr12,proj11task2,setstatus).
:- up(code12,proj12task1propa,approve).
:- up(plan2,proj12sched,read).
:- up(ldr12,proj12task1,read).
:- up(aud2,proj22task1prop,approve).
:- up(ldr2,proj12task1prop,setcost).
:- up(des11,proj12task2prop,setcost).
:- up(plan2,proj11task1prop,read).
:- up(code21,proj22task2,request).
:- up(code11,proj12task2,setstatus).
up(plan2,proj21task1prop,setschedule).
:- up(code11,proj12task1propa,read).
:- up(ldr11,proj22task1,setstatus).
:- up(des22,proj22task1,write).
:- up(mgr2,proj22task1propa,approve).
:- up(ldr2,proj12task2propa,request).
:- up(ldr11,proj22task2propa,setschedule).
:- up(mgr1,proj12task2prop,read).
:- up(ldr12,proj22task1propa,setschedule).
:- up(des21,proj11task1prop,request).
up(des22,proj22task1a,setstatus).
:- up(plan2,proj11task2prop,setschedule).
:- up(mgr2,proj11task2a,approve).
:- up(code12,proj21task2,setschedule).
:- up(des21,proj11task1prop,setcost).
:- up(aud2,proj11task2a,setstatus).
:- up(plan2,proj21budget,write).
:- up(acc1,proj22task1a,write).
:- up(aud2,proj12sched,request).
:- up(des12,proj12budget,approve).
:- up(des22,proj21task1a,setcost).
:- up(aud1,proj12task2a,approve).
:- up(ldr12,proj21task2a,request).
:- up(plan2,proj12task2a,setschedule).
:- up(des11,proj21task1propa,approve).
:- up(code11,proj12task2a,setschedule).
:- up(ldr2,proj12budget,setcost).
:- up(code21,proj11task1prop,setschedule).
:- up(mgr2,proj12task2,setstatus).
:- up(ldr2,proj12task1a,read).
:- up(acc1,proj21task1propa,request).
:- up(aud1,proj12task2,read).
:- up(aud2,proj11task1propa,request).
:- up(des12,proj22task1prop,setcost).
:- up(acc1,proj21task1prop,setcost).
:- up(plan1,proj22sched,setcost).
:- up(des11,proj12task1a,read).
:- up(code22,proj11task2a,setstatus).
:- up(plan1,proj21task2a,approve).
:- up(aud1,proj22task1a,write).
:- up(ldr2,proj11budget,setstatus).
:- up(code21,proj12task1propa,read).
:- up(des21,proj12task2a,write).
:- up(aud2,proj21task2a,request).
:- up(des11,proj21task1a,approve).
:- up(des22,proj22budget,read).
:- up(des22,proj22task1prop,read).
:- up(acc2,proj11task1,setschedule).
:- up(code22,proj12task1,write).
:- up(des22,proj12task1prop,setstatus).
:- up(plan2,proj22sched,setstatus).
:- up(code11,proj12task2,setcost).
:- up(code22,proj12task2,approve).
up(code21,proj21task2prop,request).
:- up(des12,proj21task1a,setcost).
:- up(des12,proj11budget,setstatus).
:- up(code21,proj11task1,setstatus).
:- up(acc1,proj12sched,setschedule).
:- up(des21,proj12task2a,setschedule).
:- up(aud1,proj12task1a,read).
:- up(des22,proj12budget,request).
:- up(code22,proj22task1prop,setstatus).
:- up(des12,proj22budget,setschedule).
:- up(aud2,proj11task2,setstatus).
:- up(ldr11,proj11task1,setcost).
:- up(aud1,proj21task2a,read).
:- up(des22,proj12task2prop,setschedule).
:- up(acc2,proj21task2prop,setstatus).
:- up(code21,proj11task2propa,approve).
:- up(des22,proj12sched,setstatus).
:- up(ldr11,proj21sched,request).
:- up(code21,proj21task2propa,setschedule).
:- up(ldr12,proj11task1a,approve).
:- up(aud1,proj12task1,approve).
:- up(code22,proj11task1a,setcost).
:- up(acc2,proj11task2,setschedule).
:- up(des22,proj12task1propa,setcost).
:- up(code21,proj11task2prop,read).
:- up(code12,proj22task1,setcost).
:- up(acc1,proj12task1a,request).
:- up(des12,proj21task2propa,setstatus).
:- up(aud1,proj21task2prop,setschedule).
:- up(ldr2,proj11task2,write).
:- up(code22,proj22task2,setschedule).
:- up(des12,proj12task2prop,setschedule).
:- up(code11,proj22sched,setcost).
:- up(des22,proj22sched,request).
:- up(mgr2,proj22task1a,setstatus).
:- up(code11,proj12task2propa,setschedule).
:- up(des11,proj12task1prop,read).
:- up(code11,proj21task1prop,approve).
:- up(code21,proj11task1prop,setstatus).
:- up(des21,proj12budget,approve).
:- up(plan2,proj12sched,setcost).
:- up(acc1,proj21task2a,setschedule).
:- up(des22,proj21budget,read).
:- up(code11,proj22task1,approve).
up(plan1,proj12task2prop,setschedule).
:- up(mgr1,proj21budget,setcost).
:- up(aud1,proj22task2,read).
up(plan1,proj11task1propa,setschedule).
:- up(ldr11,proj21task1propa,approve).
:- up(ldr11,proj11task1a,setstatus).
:- up(code12,proj12task1propa,setstatus).
:- up(ldr2,proj22task2prop,request).
:- up(ldr11,proj12task2a,write).
:- up(aud1,proj22task2a,read).
:- up(plan2,proj11task2,approve).
:- up(mgr2,proj11task2prop,write).
:- up(aud1,proj21task1prop,read).
:- up(acc2,proj11budget,setcost).
:- up(mgr1,proj11task1,write).
:- up(des22,proj22budget,setschedule).
:- up(mgr2,proj21sched,setcost).
:- up(ldr2,proj11task2a,request).
:- up(des12,proj12task2,setstatus).
:- up(plan2,proj22task2,setstatus).
:- up(code21,proj21task1prop,setcost).
:- up(mgr2,proj11task2prop,approve).
:- up(des22,proj22task2prop,read).
:- up(des22,proj22task2,setcost).
:- up(ldr12,proj21task1,request).
:- up(ldr11,proj11task2propa,setschedule).
:- up(acc1,proj22sched,write).
:- up(code21,proj12task1prop,setcost).
:- up(des22,proj21task1prop,setcost).
:- up(mgr1,proj21task1propa,approve).
:- up(acc2,proj22budget,request).
