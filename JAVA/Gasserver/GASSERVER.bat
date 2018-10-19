del *.bak
del *.class
set path=.;C:\Program Files (x86)\Java\jdk1.6.0_10\bin;
set classpath=.;C:\Program Files (x86)\Java\jdk1.6.0_10\lib;mysql-connector-java-3.2.0-alpha-bin.jar;activation.jar;mail.jar;additionnal.jar;jackson-annotations-2.4.2.jar;jackson-core-2.4.2.jar;jackson-databind-2.4.2.jar;javase-2.2.jar;core-2.2.jar;
set classpath=.;mysql-connector-java-5.0.4-bin.jar;comm.jar;jSMS.jar;SMSApp;sound.jar;jmf.jar;Jama-1.0.1.jar;mail.jar;SMSApp.jar;

javac *.java
java GasServer

pause

