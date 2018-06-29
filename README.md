# cloud-config-server

The situation in this branch is a single config-server providing _encrypted properties_ to different clients.
The encryption is _specific_ for the client in question.
Each client has a keystore containing a public/private keypair of which the private key is used to decrypt the values received from the server.
The server has a keystore containing the public/private keypairs from all the clients.

Steps to achieve the setup:\
**Generate first client key pair in a keystore**:\
`keytool -genkeypair -alias client1 -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=acme.com,OU=acme ict,O=client1' -keypass welcome1 -keystore config-client.jks -storepass welcome1`

**Generate a key pair in a keystore for a another client**:\
`keytool -genkeypair -alias client2 -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=acme.com,OU=acme ict,O=client2' -keypass welcome1 -keystore config-client2.jks -storepass welcome1`

**Create server keystore (containing a default key pair (1))**:\
`keytool -genkeypair -alias default -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=ConfigServer,OU=default,O=key' -keypass welcome1 -keystore config-server.jks -storepass welcome1`

**Import client key pairs in server keystore**:\
`keytool -v -noprompt -importkeystore -srckeystore config-client.jks -srcstorepass welcome1 -destkeystore config-server.jks -deststorepass welcome1 -srcalias client1 -srckeypass welcome1 -destalias client1 -destkeypass welcome1`
\
`keytool -v -noprompt -importkeystore -srckeystore config-client2.jks -srcstorepass welcome1 -destkeystore config-server.jks -deststorepass welcome1 -srcalias client2 -srckeypass welcome1 -destalias client2 -destkeypass welcome1`

**Obtain client-specific encrypted values from the /encrypt endpoint at the server**:\
`curl -X POST --data-urlencode "{key:client1}Message for Local Profile of Client #1" --user config:welcome1 http://localhost:8888/configserver/encrypt`
_{key:client1}AgBfFvD7Yyv2pE7H...MORYsRNKQ8ixnFg=_
\
`curl -X POST --data-urlencode "{key:client2}Message for Local Profile of Client #2" --user config:welcome1 http://localhost:8888/configserver/encrypt`
_{key:client2}AgCU3gtR...GHXdQnH3PtHPzo=_
\

\
**These values are stored in the database**:

**Insert statement**:\
`insert into properties (application, profile, label, key, value)` \
`values('configserver-client', 'local', 'master', 'app.message', '{cipher}AgCbkgjwthOWIUZuS...6gTcRv09w==');`

**Update statement**:\
`update properties p`
`set p.value = '{cipher}AgBfFvD7Yy...ORYsRNKQ8ixnFg='`
\
`where p.application = 'configserver-client'`
\
`and p.profile = 'local'`
\
`and p.label = 'master'`
\
`and p.key = 'app.message'`
\
\
**Refresh application context following an update**:\
`curl localhost:9080/config-client/actuator/refresh -d {} -H "Content-Type: application/json"`



