package com.em.janus.config;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class JanusJSONTest {

	public static final String WEBAPP_SRC = "src/main/webapp";
	
	@Deployment
	public static WebArchive createDeployment() {
		//create web archive
		WebArchive archive = ShrinkWrap.create(WebArchive.class, "janus-test.war");
		
		//set web.xml file
		archive.setWebXML(new File(WEBAPP_SRC, "WEB-INF/web.xml"));
		
		//set servlets
		archive.addPackages(true, "com.em.janus");
		archive.addPackages(true, "org.json");
		
		//add dependencies from maven (libraries)
		MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class);
		resolver.loadMetadataFromPom("pom.xml");
		resolver.includeDependenciesFromPom("pom.xml");
		archive.addAsLibraries(resolver.resolveAsFiles());

		//return archive for deployment
		return archive;				
	}
	
	@Test
	@RunAsClient
	public void testIndexJSON() throws IOException {
		String urlString = "http://localhost:8080/janus-test/index?mode=json";
		
		URL url = new URL(urlString);
		
		//create streams
		InputStream urlInputStream = url.openStream();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		IOUtils.copy(urlInputStream, outputStream);
		
		String outputString = new String(outputStream.toByteArray());
		
		//close streams
		urlInputStream.close();
		outputStream.close();
		
		//easy assert
		Assert.assertTrue(outputString.length() > 0);
	}
}
