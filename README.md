c.f.) http://db.apache.org/derby/integrate/db_torque.html

generate:

cd torque
ant -f build-torque.xml
ant -f build-torque.xml sql

cd ..
java -classpath lib/derby.jar:lib/derbytools.jar org.apache.derby.tools.ij
connect 'connect 'jdbc:derby:exifdata;create=true';
quit;

java -Dderby.system.home=. -classpath bin:lib/torque-3.3.jar:lib/commons-lang-2.3.jar:lib/commons-configuration-1.4.jar:lib/commons-logging-1.1.jar:lib/commons-collections-3.2.jar:lib/commons-beanutils-core-1.7.0.jar:lib/commons-dbcp-1.2.2.jar:lib/commons-pool-1.6.jar:lib/commons-dbcp-1.4.jar:lib/derby.jar:lib/metadata-extractor-2.6.4.jar:lib/xmpcore.jar:lib/village-3.3.jar com.example.exifer.ExifTest ~/Pictures