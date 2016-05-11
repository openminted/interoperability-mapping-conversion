package eu.openminted.interop.componentoverview.repo

import groovy.grape.Grape

import org.apache.commons.io.FileUtils
import org.apache.ivy.plugins.repository.ssh.Scp.FileInfo;
import org.apache.ivy.util.DefaultMessageLogger
import org.apache.ivy.util.Message;
import org.apache.maven.index.ArtifactInfo
import org.codehaus.plexus.util.io.URLInputStreamFacade;
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.FileCopyUtils;

class FindComponentDescriptor {

	static File DownloadDescriptorFiles(){
		ModelRepository repo = new ModelRepository();
		HashMap<File,String> componentDirMap = new HashMap<File, String>();
		

		String dkproGroupId= "de.tudarmstadt.ukp.dkpro.core";
		File dkproDescriptorFolder = new File("src/test/test-output/grapes/downloaded-descriptors/dkpro-core");
		
		if(!dkproDescriptorFolder.absoluteFile.exists())
			dkproDescriptorFolder.mkdirs();
			
		componentDirMap.put(dkproDescriptorFolder,dkproGroupId);

		Set<ArtifactInfo> searchResult = repo.getArtifacts("de.tudarmstadt.ukp.dkpro.core",null,null,null,null).toList().sort { a,b ->
			a.artifactVersion.compareTo(b.artifactVersion)
		};

		HashMap<String , String > filteredResult = new HashMap<String, String>();
		//		def ab = searchResult.unique{a,b-> a.artifactId <=> b.artifactId};
		searchResult.each {ai->
			if(!filteredResult.containsKey(ai.artifactId))
			{
				filteredResult.put(ai.artifactId,ai.version);

			} else{
				String ver = filteredResult.get(ai.artifactId);
				if(ver.toString().replace('.','').toInteger() < ai.version.toString().replace('.','').toInteger())
				{
					filteredResult.put(ai.artifactId,ai.version);
				}
			}
		}
		
		for(f in filteredResult.keySet()){
			GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
			try {
				Grape.grab(group:dkproGroupId, module:f, version:filteredResult.get(f),transitive:false, classLoader: loader);
			}
			catch (RuntimeException e) {
//				e.printStackTrace();
				println "Unable to grab ${f} -- version -- ${filteredResult.get(f)} }"

			}
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
//			println "${f}  -- version -- ${filteredResult.get(f)}  " + resolver.getResources(
//					"classpath*:de/**/*.xml");
			UrlResource[] files = resolver.getResources("classpath*:de/**/*.xml");
			files.each {fileinfo->
				File tempFile = new File(dkproDescriptorFolder.getAbsolutePath()+ "/"+ fileinfo.filename);
				tempFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(tempFile);
				FileCopyUtils.copy(fileinfo.getInputStream(),fos);
				fos.close();
				
			}
			
		}
		
		return dkproDescriptorFolder.getAbsoluteFile();

	}
}
