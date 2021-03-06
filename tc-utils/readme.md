# What's that ?

tc-utils by [Sewatech](http://www.sewatech.fr) is the place where we put all utility stuff that we're using for our [Apache Tomcat](http://tomcat.apache.org) deployments. 

In order to use it, just put the [tc-utils-0.5.0.jar](https://repo1.maven.org/maven2/fr/sewatech/utils/tc-utils/0.5.0/tc-utils-0.5.0.jar) file in the ${cataline.home}/lib/ directory and change your configuration depending on which feature you need.

# Password encryption

According to some discussions within the Tomcat team, it is useless to encrypt datasource passwords. 
But as stated in this [FAQ page](http://wiki.apache.org/tomcat/FAQ/Password), auditors do not like this answer. 
In order to make them happy, we have implemented some encryption features for DataSource and SSL.

Both are using the same encryption algorithm and key. Encrypting password for either of them work as follow :
 
    java -cp $CATALINA_HOME/lib/*:$CATALINA_HOME/bin/tomcat-juli.jar fr.sewatech.tcutils.commons.Encryption encode mypwd 

## DataSource
 
We have made the choice to implement an ObjectFactory. 
To be more precise, we've done a sub-class of [Tomcat JDBC](http://tomcat.apache.org/tomcat-7.0-doc/jdbc-pool.html)'s DataSourceFactory that handles with encrypted passwords. 

Now you can change the configuration of your datasource by adding (or changing) the factory attribute and replacing the password by the encrypted one :
   
    <Resource name="MyDS" type="javax.sql.DataSource" auth="Container" 
              factory="fr.sewatech.tcutils.jdbc.EncryptedDataSourceFactory"
              username="alexis" password="XiGY7vFU1Nc=" 
              .../> 

## SSL Connector

Generate the keystore with the clear text passwords :

    keytool -genkeypair -keystore conf/my.jks -storepass mystorepwd -alias mykey -keypass mykeypwd    \
                        -keyAlg RSA -dname "cn=www.sewatech.fr, o=Sewatech, c=FR"    

Now you can change the configuration of the SSL connector, using our protocol class and the encrypted passwords (Tomcat 7-style) :

    <Connector port="8443" protocol="fr.sewatech.tcutils.connector.EncryptedSslHttp11NioProtocol"
               SSLEnabled="true" scheme="https" secure="true" clientAuth="false" sslProtocol="TLS" 
               keystoreFile="${catalina.home}/conf/my.jks" keystorePass="sFXj6LJVeHaSzEWkXy+myg=="
               keyAlias="mykey" keyPass="K2wXOlwGHpiu/RdNAO1rJQ=="/>

This kind of configuration was designed for old Tomcats. 
It is supported up to Tomcat 9. 
With Tomcat 8.5, a new way to configure the SSL connector has been introduced.

    <Connector port="8443" protocol="org.apache.coyote.http11.Http11NioProtocol"
               SSLEnabled="true"
               sslImplementationName="fr.sewatech.tcutils.connector.JSSEEncryptedPasswordImplementation">
        <SSLHostConfig>
            <Certificate certificateKeystoreFile="conf/localhost-rsa.jks" certificateKeyAlias="tomcat"
                         type="RSA" certificateKeystorePassword="q55EZMUHS8k=" />
        </SSLHostConfig>
    </Connector>

# Sessions

Request coming from monitoring tools are generating a lot of useless sessions. 
The SingleRequestSessionFilter can invalidate them at the end of each request. 
The filter invalidates the session of some defined users, and keep unchanged the other sessions.

You can configure the filter in the web.xml of an application or in the global web.xml.

Configuration for a single user :

    <filter>
        <filter-name>singleRequestSessionFilter</filter-name>
        <filter-class>fr.sewatech.tcutils.session.SingleRequestSessionFilter</filter-class>
        <init-param>
            <param-name>userName</param-name>
            <param-value>nagios0</param-value>
        </init-param>
    </filter>
    
Configuration for a list of users (coma separated) :

    <filter>
        <filter-name>singleRequestSessionFilter</filter-name>
        <filter-class>fr.sewatech.tcutils.session.SingleRequestSessionFilter</filter-class>
        <init-param>
            <param-name>userName</param-name>
            <param-value>nagios1,nagios2,nagios3</param-value>
        </init-param>
    </filter>

Do not forget to map the filter to the whole application :
    
    <filter-mapping>
        <filter-name>singleRequestSessionFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

TODO :

* Support of Session based authentication (not only principal)

# LDAP Realm

Tomcat comes with a realm that validate credentials toward a LDAP registry. 
This realm is names JNDIRealm. 
It's working fine except when it comes with Digest authentication.

The goal of our LdapRealm is to permit Digest authentication on a LDAP, with some conditions.
 
Indeed, there are two ways to validate credentials with LDAP : bind the user or compare provided information with some fields. 
This one is the only solution for Digest. So you'll have to configure the realm with a ''userPassword'' attribute.  

    <Realm className="fr.sewatech.tcutils.realm.LdapRealm"

           connectionURL="ldap://127.0.0.1:1389/"
           connectionName="cn=Directory Manager"
           connectionPassword="swpwd"

           userBase="ou=people,dc=sewatech,dc=fr"
           userPassword="password"
           userSearch="(uid={0})"

           roleBase="ou=groups,dc=sewatech,dc=fr"
           roleSearch="(uniqueMember={0})"
           roleName="cn" />

Note : 
This realm is deprecated because it is only useful in old versions of Tomcat. 
The feature has been integrated in Tomcat's JNDIRealm, in Tomcat 9, and has been ported to previous versions, in minor releases (8.0.29 and 7.0.66).

# Enhanced logs

Wanna change the group owner of the log files ? 
You can find fine workaround with bash scripts. 
Here are some integrated solutions, for the access logs and the JULI logs. 

Their are a few constraints. Of course it works only on posix file systems. 
And the current user should be a member of the group.

## JULI for Posix

In order to use our JULI file handler, you need to download an additional [file](https://repo1.maven.org/maven2/fr/sewatech/utils/tc-utils/0.4.0/tc-utils-0.4.0-juli.jar) 
and put it in the ${catalina.home}/bin/ directory.

Then, you change the classpath environment variable, in the standalone.conf file :

    CLASSPATH="$CLASSPATH:$CATALINA_BASE/bin/tc-utils-0.4.0-juli.jar"

After that, you may configure the file handler in the logging.properties file :

    9custom.fr.sewatech.tcutils.juli.PosixAsyncFileHandler.level = INFO
    9custom.fr.sewatech.tcutils.juli.PosixAsyncFileHandler.directory = ${catalina.base}/logs
    9custom.fr.sewatech.tcutils.juli.PosixAsyncFileHandler.prefix = sewatech.
    9custom.fr.sewatech.tcutils.juli.PosixAsyncFileHandler.group = everyone

The PosixAsyncFileHandler supports the same options as the regular AsyncFileHandler, and adds the "group" option. 
This option is just ignored on non-posix file systems. 

## AccessLogValve for Posix

Configure the Valve in the server.xml file :

    <Valve className="fr.sewatech.tcutils.log.PosixAccessLogValve"
           ...
           posixGroupName="logs" />

On non-posix file systems, the posixGroupName is just ignored.

# Reverse proxy

## Context path

This XForwardedFilter servlet filter can change the result of request.getContextPath(). 
Instead of given the original value, it checks if it can find the X-Forwarded-Context header and returns this value if exists.

The first part of the setup is in the web server. 
It has to put the context path in the X-Forwarded-Context header. 
For example, with httpd mod_proxy / mod_headers it will look like this :
 
    <Location /hi>
        ProxyPass http://localhost:8080/hell
        RequestHeader set X-Forwarded-Context /hi
    </Location>

The java part of the configuration takes place in the web.xml file :

    <filter>
        <filter-name>XForwardedFilter</filter-name>
        <filter-class>fr.sewatech.tcutils.proxy.XForwardedFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>XForwardedFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

# HTTP Headers

## HTTP Response 

This valve adds a header to the HTTP responses with a statically defined value.

    <Valve className="fr.sewatech.tcutils.headers.HttpResponseHeaderValve"
           headerName="X-Made-By"
           headerValue="Sewatech"
           force="true" />

The `headerName` and `headerValue` parameters are required.

The `force` parameter is optional.
It's default value is `false`, which means that if the header is set by the application the valve won't modify it. 
In the _force_ mode, the header is always set even if has already be added by the application.

## CORS 

CORS is already supported by Tomcat with its [CorsFilter](http://tomcat.apache.org/tomcat-8.5-doc/config/filter.html#CORS_Filter).
But sometime we need a valve instead of a filter. 

CorsValve is a wrapper around the built-in CorsFilter.
The simplest way to enable it is: 

    <Valve className="fr.sewatech.tcutils.headers.CorsValve" />

All the parameters has been adapted as well:

* `allowedOrigins` for `cors.allowed.origins`
* `allowedMethods` for `cors.allowed.methods`
* `allowedHeaders` for `cors.allowed.headers`
* `exposedHeaders` for `cors.exposed.headers`
* `preflightMaxAge` for `cors.preflight.maxage`
* `supportCredentials` for `cors.support.credentials`
* `requestDecorate` for `cors.request.decorate`
