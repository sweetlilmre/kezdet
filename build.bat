@ECHO OFF
SETLOCAL
SET BASEDIR=%~dp0
SET NAME=%~n0
PUSHD %BASEDIR%

SET ANT_OPTS=-Xms768m -Xmx1024m -XX:MaxPermSize=512m
SET ANT_ARGS=-logger org.apache.tools.ant.listener.BigProjectLogger
:: -logfile build.log -verbose
SET ANT="%ANT_HOME%\bin\ant.bat"

CALL %ANT% %*

:exit
POPD
ENDLOCAL