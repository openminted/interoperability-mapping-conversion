package eu.openminted.interop.componentoverview.repo
import org.codehaus.groovy.tools.GrapeUtil;

import groovy.grape.Grape
class demo {

	static void main(String... args){
		System.setProperty("grape.root", "src/test/test-output/grapes");
		System.setProperty("ivy.cache.dir", new File("src/test/test-output/grapes/grapes").absolutePath);

		System.setProperty("groovy.grape.report.downloads", "true");
		GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
		
		try {
	
//			Grape.addResolver('classifier':'pom.xml');
			Grape.grab(group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core-gpl', version:'1.7.0',type:'pom',transitive:false, classLoader: loader);
			
		}
		catch (RuntimeException e) {
			printf("Unable to grab with type:%s\n","pom")
			println("Trying to grab with type: jar")
			try{
				Grape.grab(group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core-gpl', version:'1.7.0',type:"jar",transitive:false, classLoader: loader);
			}catch(RuntimeException e2){
				println("Unable to get artifact: Check the resource repo");
			}

		}
	}
}
	