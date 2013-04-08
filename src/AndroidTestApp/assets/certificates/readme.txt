Generating a key to sign your plugins:

An example keystore, key-pair and certificate are provided for reference, however you should generate your own.

First generate a keystore:

keytool -genkey -alias kezdet -keyalg RSA -keystore kezdet-store -keysize 2048 -validity 3560

The included build scripts use the password "password" for the keystore and "password2" for the key-pair.
Please choose more secure passwords and alter the build.xml in the relevant directories appropriately.
The entries to change can be found in the <signjar> node under the "storepass" and "keypass" attributes.

Enter the appropriate information, look here for an example: http://docs.oracle.com/javase/tutorial/security/toolsign/step3.html

Next export the public key:

keytool -export -keystore kezdet-store -alias kezdet -file kezdet-public.cer

The public key will be used to check the validity of the plugin by the host system.
