# cloud-config-server

I'm  basically following point 7 here from http://www.baeldung.com/spring-cloud-configuration

This describes a configuration where the client has to decrypt the received values.

_Keystore generation_:
1. **Generate client keypair in keystore**
`keytool -genkeypair -alias client1 -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=acme.com,OU=acme ict,O=acme' -keypass welcome1 -keystore config-client.jks -storepass welcome1
`
2. **Generate empty server keystore**
- `keytool -genkeypair -alias dummy -keyalg RSA -keysize 4096 -sigalg SHA512withRSA -dname 'CN=ConfigServer,OU=Server,O=keystore' -keypass welcome1 -keystore config-server.jks -storepass welcome1`
- `keytool -delete -alias dummy -keystore config-server.jks -storepass welcome1
`

3. **Import client key pair into server keystore**
`keytool -v -noprompt -importkeystore -srckeystore ../../../../client/src/main/resources/config-client.jks -srcstorepass welcome1 -destkeystore config-server.jks -deststorepass welcome1 -srcalias client1 -srckeypass welcome1 -destalias client1 -destkeypass welcome1
`

The server imports the client keypair in it's keystore.
Strictly speaking the server would need only the client public key but sadly this does not work.
Trying to send asymmetrically encrypted values to the client by using only the public key of the client
fails due to the class org.springframework.security.rsa.crypto.KeyStoreKeyFactory.getKeyPair(..) method is using private key properties
to get hold of the public key:
`RSAPrivateCrtKey key = (RSAPrivateCrtKey)this.store.getKey(alias, password);
RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
`If the private key is not present, key will be null and key.getModulus() leads to an NPE.

So the server actually needs the private key in order to be able to get the public key, sigh...
