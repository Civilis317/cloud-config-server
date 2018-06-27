# cloud-config-server

The `encryption key` shared between server and client is defined in the `bootstrap.yml` file of both projects. 
For the client this is logical since it has no application.yml files anymore. But in the case of the server the fact that the key when placed in the normal application.yml files is not recognized is a known issue.

It is necessary to install the Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files.

It can be downloaded (for java 8) here:
 - http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html

The zip  file contains instruction on how to install.

Also in the server bootstrap.yml is this property:
_`spring:
  cloud:
    config:
      server:
        encrypt:
          enabled: false`_
This makes sure that the client receives encrypted values that it will need to decrypt.          
Any man-in-the-middle will also see only encrypted values.

The default setting is that the values are stored encrypted, but that the server decrypts these values before sending them to the client.
The client in that case will not need a key for decryption.


          
