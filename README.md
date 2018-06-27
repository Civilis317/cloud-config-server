# cloud-config-server

An implementation of Spring Cloud Config Server
using a database (here in-memory h2, but could be any sql database) for storage of the property values.

`Sources` followed are:
- https://cloud.spring.io/spring-cloud-config/multi/multi_spring-cloud-config.html
- http://www.baeldung.com/spring-cloud-configuration
- https://github.com/rashidi/jdbc-env-repo-sample



**Master** branch:
A working set of server and client, showing inclusion of default profile properties in the local profile set.

In the **basic-authentication** branch the client needs to authenticate to be able to use the server.
Certain endpoints (eg /h2-console) are exempt from the security constaint.

The **symmetric-encryption** branch shows how to store sensitive configuration entries in an encrypted way.
The client needs the (same) encryption key to decrypt the received values.

Work in progress is an assymetric encryption example