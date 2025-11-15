* [33m5031782[m[33m ([m[1;31morigin/feature.SLOT-106.modifyUpdateStudentRequest[m[33m)[m [FTR][SLOT-106]:Modifiqué el updateStudentRequest para no registrar classPrice y extraClasses. También desglosé el método validaPlanDetail
*   [33m90f64ed[m[33m ([m[1;36mHEAD -> [m[1;32mfeature.SLOT-107.modify-StudentDetailsResponse[m[33m, [m[1;31morigin/feature.SLOT-107.modify-StudentDetailsResponse[m[33m, [m[1;31morigin/dev[m[33m, [m[1;31morigin/HEAD[m[33m, [m[1;32mdev[m[33m)[m Merge pull request #38 from ThreeTechSoluciones/enhacement.SLOT-97.modify-edit-student
[32m|[m[33m\[m  
[32m|[m * [33m20c1c6b[m[33m ([m[1;31morigin/enhacement.SLOT-97.modify-edit-student[m[33m, [m[1;32menhacement.SLOT-97.modify-edit-student[m[33m)[m [ENH][SLOT-97]: Implementando code review
[32m|[m * [33mad16d9a[m [ENH][SLOT-97]: Se modificó la edición del estudiante. Al querer modificar el plan de DiaEspecifico a PrincipioDeMes el diaDePago se setea a null
[32m|[m[32m/[m  
[32m|[m * [33m20ede9c[m[33m ([m[1;31morigin/feature.SLOT-83.obtain-payment-information-endpoint[m[33m)[m [REF][SLOT-83]: Implementación de cambios sugeridos
[32m|[m *   [33m913bfcd[m Merge remote-tracking branch 'origin/dev' into feature.SLOT-83.obtain-payment-information-endpoint
[32m|[m [35m|[m[32m\[m  
[32m|[m [35m|[m[32m/[m  
[32m|[m[32m/[m[35m|[m   
[32m|[m * [33me96d65c[m [REF][SLOT-83]: Implementación de cambios para no tener in ciclo
[32m|[m * [33m7c95937[m [FTR][SLOT-83]:Implementación de lógica para endpoint que devuelva el detalle del pago de una cuota
[32m|[m [36m|[m * [33m58aa271[m[33m ([m[1;31morigin/feature.SLOT-94.check-if-dni-exists-endpoint[m[33m)[m [FTR][SLOT-94]: Implementación de lógica para verificar si el dni ya existe
[32m|[m [36m|[m[32m/[m  
[32m|[m[32m/[m[36m|[m   
* [36m|[m   [33m8f614b4[m Merge pull request #35 from ThreeTechSoluciones/fix.SLOT-96.modify-response-studentDetail
[1;32m|[m[1;33m\[m [36m\[m  
[1;32m|[m * [36m|[m [33m11df41f[m[33m ([m[1;31morigin/fix.SLOT-96.modify-response-studentDetail[m[33m)[m [FIX][SLOT-96]:Se añadió el atributo planId a la response de getStudentById
* [1;33m|[m [36m|[m [33m2c4b4c7[m Feature.slot 95.modify payment status (#37)
* [1;33m|[m [36m|[m [33m5c6cf19[m [FTR][SLOT-68]: add register monthly fee manually (#36)
[36m|[m [1;33m|[m[36m/[m  
[36m|[m[36m/[m[1;33m|[m   
[36m|[m [1;33m|[m * [33mdf1acda[m[33m ([m[1;31morigin/feature.SLOT-95.modify-payment-status[m[33m)[m [REF][SLOT-95]: Se agrego @JsonValue para que la salida sea en español
[36m|[m [1;33m|[m * [33ma70ef74[m [FTR][SLOT-95]: Cambio de status de on time a pending
[36m|[m [1;33m|[m * [33mdd8f7d3[m [FTR][SLOT-95]: Implementación de lógica para devolver el valor en español de los estados de las cuotas mensuales
[36m|[m [1;33m|[m [1;34m|[m *   [33m24440ae[m[33m ([m[1;31morigin/feature.SLOT-68.register-monthly-fees-manually[m[33m)[m [REF][SLOT-68]: fix conflicts with dev
[36m|[m [1;33m|[m [1;34m|[m [1;35m|[m[36m\[m  
[36m|[m [1;33m|[m[36m_[m[1;34m|[m[36m_[m[1;35m|[m[36m/[m  
[36m|[m[36m/[m[1;33m|[m [1;34m|[m [1;35m|[m   
* [1;33m|[m [1;34m|[m [1;35m|[m [33mdfc6ac4[m [FTR][SLOT-65]: Actualizar cronjob para la creación de cuotas (#34)
[1;36m|[m [1;33m|[m [1;34m|[m * [33md946537[m [REF][SLOT-68]: change status error
[1;36m|[m [1;33m|[m [1;34m|[m * [33med070a2[m [FTR][SLOT-68]: add register monthly fee manually
[1;36m|[m [1;33m|[m [1;34m|[m *   [33mb732e52[m[33m ([m[1;31morigin/feature.SLOT-65.update-student-monthly-fee-creation-cron-job[m[33m)[m [REF][SLOT-65]: resolve conflicts
[1;36m|[m [1;33m|[m [1;34m|[m [31m|[m[1;36m\[m  
[1;36m|[m [1;33m|[m[1;36m_[m[1;34m|[m[1;36m_[m[31m|[m[1;36m/[m  
[1;36m|[m[1;36m/[m[1;33m|[m [1;34m|[m [31m|[m   
* [1;33m|[m [1;34m|[m [31m|[m [33m5e77859[m [ENH][SLOT-84]: add number of days to plan response (#32)
* [1;33m|[m [1;34m|[m [31m|[m [33m9c985ee[m [FIX][SLOT-80]: change student filter by dni (#33)
[1;34m|[m [1;33m|[m[1;34m/[m [31m/[m  
[1;34m|[m[1;34m/[m[1;33m|[m [31m|[m   
* [1;33m|[m [31m|[m [33mc7d348f[m Feature.slot 81.return a student's fees (#31)
[1;33m|[m[1;33m/[m [31m/[m  
[1;33m|[m * [33mbdaf25f[m [REF][SLOT-65]: change cronjob execution time
[1;33m|[m * [33mb00cba7[m [REF][SLOT-65]: fix error at logging student
[1;33m|[m * [33m1c1bda8[m [FTR][SLOT-65]: change logic to get current price
[1;33m|[m * [33m3940b4b[m [FTR][SLOT-65]: remove innecesary condition
[1;33m|[m * [33m0fc6da1[m [FTR][SLOT-65]: add validation to specific day processor
[1;33m|[m * [33me3aac37[m [FTR][SLOT-65]: add validation to beginning of month processor
[1;33m|[m * [33m6d1c300[m [FTR][SLOT-65]: wip
[1;33m|[m[1;33m/[m  
[1;33m|[m * [33mb11f695[m[33m ([m[1;31morigin/feature.SLOT-81.return-a-student's-fees[m[33m)[m [REF][SLOT-81]: Implementación de cambios sugeridos
[1;33m|[m * [33mfcf88fe[m[33m ([m[1;32mfeature.SLOT-81.return-a-student's-fees[m[33m)[m [REF][SLOT-81]: Implementación de cambios sugeridos
[1;33m|[m * [33m7646263[m [REF][SLOT-81]: Corrección por resolver conflictos
[1;33m|[m *   [33m72d1aac[m Merge branch 'dev' into feature.SLOT-81.return-a-student's-fees
[1;33m|[m [34m|[m[1;33m\[m  
[1;33m|[m [34m|[m[1;33m/[m  
[1;33m|[m[1;33m/[m[34m|[m   
[1;33m|[m * [33m8b737d6[m [FIX][SLOT-81]: Corregir errores
[1;33m|[m * [33m4df2870[m [FTR][SLOT-81]: Implementación de lógica para las cuotas de un estudiante.
[1;33m|[m * [33mc38a957[m[33m ([m[1;31morigin/feature.SLOT-59.register-fee-as-paid[m[33m)[m [REF][SLOT-59]: Eliminación de la clase PlanType
[1;33m|[m * [33m2519b55[m [REF][SLOT-59]: Ajustes por cambios de nombres
[1;33m|[m *   [33ma37313f[m Merge remote-tracking branch 'origin/dev' into feature.SLOT-59.register-fee-as-paid
[1;33m|[m [36m|[m[1;31m\[m  
[1;33m|[m * [1;31m|[m [33m224edf2[m [FIX][SLOT-59]: Eliminación de imports
[1;33m|[m * [1;31m|[m [33mf81d347[m [REF][SLOT-59]: Implementacion de cambios sugeridos
[1;33m|[m * [1;31m|[m [33m90799cb[m [FTR][SLOT-59]: Implementación de lógica para registrar una cuota como pagada
[1;33m|[m [1;31m|[m [1;31m|[m * [33mba2321b[m[33m ([m[1;31morigin/fix.SLOT-80.fix-student-filter-by-dni[m[33m)[m [FIX][SLOT-80]: change student filter by dni
[1;33m|[m [1;31m|[m[1;33m_[m[1;31m|[m[1;33m/[m  
[1;33m|[m[1;33m/[m[1;31m|[m [1;31m|[m   
[1;33m|[m [1;31m|[m [1;31m|[m * [33mb23e232[m[33m ([m[1;31morigin/feature.SLOT-85.return-plan-number-of-days[m[33m)[m [ENH][SLOT-84]: add number of days to plan response
[1;33m|[m [1;31m|[m[1;33m_[m[1;31m|[m[1;33m/[m  
[1;33m|[m[1;33m/[m[1;31m|[m [1;31m|[m   
* [1;31m|[m [1;31m|[m [33mf0bbc37[m [FTR][SLOT-59]: Implementación de lógica para registrar una cuota como pagada (#30)
[1;31m|[m [1;31m|[m[1;31m/[m  
[1;31m|[m[1;31m/[m[1;31m|[m   
* [1;31m|[m [33m3842b48[m [REF][SLOT-64]: mucho refactor (#29)
[1;33m|[m [1;31m|[m * [33m9ef6e77[m[33m ([m[1;31morigin/refactor.SLOT-64.change-model-architecture[m[33m, [m[1;32mrefactor.SLOT-64.change-model-architecture[m[33m)[m [REF][SLOT-64]: refactur due to code review, remove payment from first monthly-amount
[1;33m|[m [1;31m|[m * [33m99c7087[m [REF][SLOT-64]: changes due to code review
[1;33m|[m [1;31m|[m[1;31m/[m  
[1;33m|[m [1;31m|[m * [33me765663[m[33m ([m[1;31morigin/feature.SLOT-68.register-monthly-fee-manually[m[33m)[m [REF][SLOT-68]: wip
[1;33m|[m [1;31m|[m[1;31m/[m  
[1;33m|[m * [33m0fdaddb[m [REF][SLOT-64]: change expiration date
[1;33m|[m * [33mfabc99d[m [REF][SLOT-64]: set user to payment and finish TO DO list
[1;33m|[m * [33m4f2be27[m [REF][SLOT-64]: get student situation
[1;33m|[m * [33m58820c4[m [REF][SLOT-64]: delete price controller, service and repository
[1;33m|[m * [33mc4944fb[m [REF][SLOT-64]: creat