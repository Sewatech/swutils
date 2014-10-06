# What's that ?

Sewatech tc-utils is the place where we put all utility stuff that we're using for our 
[Apache Tomcat](http://tomcat.apache.org) deployments. 
 
# Password encryption

According to some discussions within the Tomcat team, it is useless to encrypt datasource passwords. But as stated in 
this [FAQ page](http://wiki.apache.org/tomcat/FAQ/Password), auditors do not like this answer.
 
We have made the choice to implement an ObjectFactory. To be more precise, we've done a sub-class of [Tomcat JDBC]
(http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html)'s DataSourceFactory that handles with encrypted passwords. 

In order to use it, just put the tc-utils.jar file in the ${cataline.home}/lib directory, and change the configuration of 
your datasource by adding (or changing) the factory attribute and replacing the password by the encrypted one :
   
    <Resource name="MyDS" type="javax.sql.DataSource" auth="Container" 
              factory="fr.sewatech.tcutils.jdbc.EncryptedDataSourceFactory"
              username="alexis" password="XiGY7vFU1Nc=" 
              .../>
 
The password is encrypted by running the following command :

    java -cp $CATALINA_HOME/lib:$CATALINA_HOME/bin/tomcat-juli.jar fr.sewatech.tcutils.jdbc.EncryptedDataSourceFactory encode mypwd

TODO :
 
* prompt when no argument after encode

* prompt at Tomcat startup if password attribute is not in configuration

# Sessions

Request comming from monitoring tools are generating a lot of useless sessions. We should invalidate them or put a low 
timeout.

TODO :

* everything

# Server

Default Server implementation is listening on port 8005. Useless in most cases.

TODO :

* everything

