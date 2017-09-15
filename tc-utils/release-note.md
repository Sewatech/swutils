# Pre-versions

## 0.5.0

- XForwardedFilter to rewrite the context path when using a reverse proxy
- HttpResponseHeaderValve to set an arbitrary header in HTTP response

## 0.4.1

- password encryption for SSL NIO2 connector (EncryptedSslHttp11Nio2Protocol)
- ugrade to Tomcat 8.5 and Java 8
- deprecate old stuff (Tomcat 7 related)

## 0.4

- password encryption for SSL connector (EncryptedSslHttp11NioProtocol)
- choose group owner for JULI log files (PosixAsyncFileHandler)
- choose group owner for access log files (PosixAccessLogValve)

## 0.3

- digest authentication with LDAP (LdapRealm)

## 0.2

- session invalidation for defined users (SingleRequestSessionFilter)

## 0.1

- password encryption for datasources (EncryptedDataSourceFactory)
