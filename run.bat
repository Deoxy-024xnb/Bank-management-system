@echo off
echo Compiling...
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d src src\DBUtil.java src\AccountService.java src\Main.java
echo Running...
java -cp "lib/mysql-connector-j-8.0.33.jar;src" Main
pause
