@echo off
@title test
set CLASSPATH=.;dist\*;dist\lib\*;lib\*
java -Dnet.sf.odinms.wzpath=wz database.DatabaseBackup
pause