package eu.openminted.interop.componentoverview.repo
import org.codehaus.groovy.tools.GrapeUtil;

import groovy.grape.Grape
class demo {

	static void main(String... args){
		System.setProperty("groovy.grape.report.downloads", "true");
		GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
		
		try {
	
			
			Grape.grab(group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core.arktools-gpl', version:'1.6.2',transitive:false, classLoader: loader);
			
		}
		catch (RuntimeException e) {
			e.printStackTrace();
			println "Unable to grab  -- version -- = }"

		}
	}
}
	