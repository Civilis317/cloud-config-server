# cloud-config-server

The situation in this branch is a single config-server providing _encrypted properties_ to different clients.
The encryption is _specific_ for the client in question.
Each client has a keystore containing a public/private keypair of which the private key is used to decrypt the values received from the server.
The server has a keystore containing the public/private (1) keypairs from all the clients.

Steps to achieve the setup:\
**Generate first client key pair in a keystore**:\
`keytool -genkeypair -alias client1 -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=acme.com,OU=acme ict,O=client1' -keypass welcome1 -keystore config-client.jks -storepass welcome1`

**Generate a key pair in a keystore for another client**:\
`keytool -genkeypair -alias client2 -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=acme.com,OU=acme ict,O=client2' -keypass welcome1 -keystore config-client2.jks -storepass welcome1`

**Create server keystore (containing a default key pair (2))**:\
`keytool -genkeypair -alias default -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=ConfigServer,OU=default,O=key' -keypass welcome1 -keystore config-server.jks -storepass welcome1`

**Import client key pairs in server keystore**:\
`keytool -v -noprompt -importkeystore -srckeystore config-client.jks -srcstorepass welcome1 -destkeystore config-server.jks -deststorepass welcome1 -srcalias client1 -srckeypass welcome1 -destalias client1 -destkeypass welcome1`
\
`keytool -v -noprompt -importkeystore -srckeystore config-client2.jks -srcstorepass welcome1 -destkeystore config-server.jks -deststorepass welcome1 -srcalias client2 -srckeypass welcome1 -destalias client2 -destkeypass welcome1`
\
\
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
**Refresh client application context following an update**:\
`curl localhost:9080/config-client/actuator/refresh -d {} -H "Content-Type: application/json"`

\
**Things yet to solve**:\
***1**: _Private keys in server keystore_:  
In this setup it is not necessary and in fact undesirable to have the client's private key at the server.  
The server will not be decrypting and therefore would have no need for the private key.
Furthermore having the private key available at the server is a potential security risk. 
Ideally the server can encrypt and serve encrypted values only the specific client is able to decrypt.
But with the private key the server (and anyone gaining acces to it) is able to decrypt the values.
\
I have tried to use a server keystore containing only the trusted certificate and public key of the client.  
However the standard `/encrypt` endpoint uses the `getKeyPair(..)` method of `org.springframework.security.rsa.crypto.KeyStoreKeyFactory`.  
In this method **the private key is used to get at the public key**:  
`RSAPrivateCrtKey key = (RSAPrivateCrtKey)this.store.getKey(alias, password); `   
`RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent()); `   
If key == null, as is the case when no private key is present this ends in disaster (an NPE).  
A possible solution to implement a custom /encrypt endpoint.
\
\
**2**: _The server keystore contains a default key pair, not related to any client_.  
This too is necessary because of a quirk (imho) of the `/encrypt` endpoint implementation.  
When requesting an encrypted value the key to use is indicated, eg {key:client1}.  
However prior to actually encrypting the Spring code checks if a keystore and key pair are available 
by retrieving the key that is configured in the bootstrap.yml configuration file.  
Check out `org.springframework.cloud.config.server.encryption.EncryptionController.encrypt(..)`  
The line `this.checkEncryptorInstalled(name, profiles);` **uses the default configured alias to check if a key pair can be retrieved** from the keystore.  
If this succeeds, the message is parsed for an indicated key and then that key is retrieved from the keystore and used.  
Why not try to get the indicated key right away?






