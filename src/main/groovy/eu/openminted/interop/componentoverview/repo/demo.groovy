package eu.openminted.interop.componentoverview.repo
import org.codehaus.groovy.tools.GrapeUtil;

import groovy.grape.Grape
class demo {

    static void main(String... args){
        //ivy debug
        System.setProperty("ivy.message.logger.level", "4")

                
        //http debug
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");

        System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");

        System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire.header", "debug");

        System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.commons.httpclient", "debug");

//        //groovy debug
        System.setProperty("groovy.grape.report.downloads","true");
//
        System.setProperty("grape.root", "target/test-output/grapes");
        System.setProperty("ivy.cache.dir", new File("target/test-output/grapes/grape").absolutePath);
//
//        System.setProperty("groovy.grape.report.downloads", "true");
        GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);

        try {

            //			Grape.addResolver('classifier':'pom.xml');
            //			println Grape.resolve(
            //				[classLoader: new GroovyClassLoader()],
            //				[group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core-gpl', version:'1.7.0', type:'pom', transitive:false]);
            //
            Grape.addResolver([name:'ukp',root:'http://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-snapshots',m2Compatible: 'true'])
            Grape.resolve(
                    [classLoader: new GroovyClassLoader()],
                    [group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core', version:'1.9.0-SNAPSHOT', type:'pom', transitive:false]);
            //			Grape.grab(group:'de.tudarmstadt.ukp.dkpro.core', module:'de.tudarmstadt.ukp.dkpro.core-gpl', version:'1.7.0',type:'pom',transitive:false, classLoader: loader);

        }
        catch (RuntimeException e) {
            printf("Unable to grab with type:%s\n","pom")
        }
    }
}
