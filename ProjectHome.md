an open source web application for viewing calibre libraries, see: http://calibre-ebook.com/ for more information about the calibre software

you can also view the announcement thread here: http://www.mobileread.com/forums/showthread.php?t=149095

built with maven, to build this project use the following command(s):
```
mvn clean compile war:war
```

to use this, deploy to your container and configure the following element in your web.xml  this is the location in which it will look for your janus configuration.
```
  <env-entry>
    <env-entry-name>configFile</env-entry-name>
    <env-entry-type>java.lang.String</env-entry-type>
    <env-entry-value>/var/janus.conf</env-entry-value>
  </env-entry>
```

the configuration file should look something like this.
```
basePath=/var/books

email.smtp=smtp.google.com
email.port=465
email.security=ssl
email.from=your.gmail.user@gmail.com
email.user=your.gmail.user@gmail.com
email.password=password
```

the configured base path will serve as the base location for all file and image lookups.  you can use any smtp server you would like to use and it supports no encryption, ssl, and tls.  (email.security can be set to "tls", "ssl", or "none".)

the external config file should make it easier to handle any changes to the application and future updates without having to constantly worry about redeploying the proper config.

the following java options are recommended (tested on tomcat 7):
```
-Djava.awt.headless=true 
-Xms420m 
-Xmx420m 
-XX:+UseConcMarkSweepGC 
-XX:+UseParNewGC 
-XX:+DisableExplicitGC
-XX:+CMSIncrementalPacing 
-XX:+CMSClassUnloadingEnabled 
-XX:+CMSPermGenSweepingEnabled 
-XX:+AggressiveOpts 
-XX:+CMSScavengeBeforeRemark
-XX:+ParallelRefProcEnabled
-Dsun.lang.ClassLoader.allowArraySyntax=true 
-Djava.net.preferIPv4Stack=true
-Dsun.rmi.dgc.client.gcInterval=3600000 
-Dsun.rmi.dgc.server.gcInterval=3600000
```