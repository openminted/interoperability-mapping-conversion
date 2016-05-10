package eu.openminted.interop.componentoverview.repo
import org.codehaus.groovy.tools.GrapeUtil;

import groovy.grape.Grape
class demo {

	static void main(String... args){
		System.setProperty("groovy.grape.report.downloads", "true");
		GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
		
		try {
	
			Grape.grab(group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core.umlautnormalizer-asl', version:'1.6.2', classLoader: loader,autoDownload:false);
			
		}
		catch (RuntimeException e) {
			e.printStackTrace();
			println "Unable to grab  -- version -- = }"

		}
	}
}
	