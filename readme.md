# What's that ?

This is the place where we put all utility stuff that we're using for our deployments. 

# [tc-utils](../../tree/master/tc-utils)

This one is dedicated to Tomcat.  

# Build

## License

Header is in the LICENSE-header file. Update it and run the maven goal `license:format` from the root of te project :

 mvn license:format

## Release

A gpg program is required, and the default secret key has to be published (see http://central.sonatype.org/pages/working-with-pgp-signatures.html).
On MacOS :

    brew install gpg

Prepare for the release (sign, javadoc,...)

    mvn install -Prelease -Dgpg.passphrase=xxx

Upload the artifacts to Sonatype OSSRH :

    mvn deploy -Prelease -Dgpg.passphrase=xxx

Check on https://oss.sonatype.org/#stagingRepositories. 
If correct close the bundle, and then release it (see http://central.sonatype.org/pages/releasing-the-deployment.html#close-and-drop-or-release-your-staging-repository).
