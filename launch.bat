@echo off
@color F0

@title KMS 1.2.391

set JAVA_HOME=C:\Program Files\Java\jdk-14
set PATH=%JAVA_HOME%\bin;%PATH%
set CLASSPATH=.;dist\*;lib\*;
if not defined KMS_XMS set KMS_XMS=512M
if not defined KMS_XMX set KMS_XMX=2048M
java -Xms%KMS_XMS% -Xmx%KMS_XMX% -Dnet.sf.odinms.wzpath=wz server.Start
pause
