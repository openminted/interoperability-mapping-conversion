package eu.openminted.interop.componentoverview.repo

import groovy.grape.Grape

import java.io.File

import org.apache.ivy.plugins.repository.ssh.Scp.FileInfo;
import org.apache.maven.index.ArtifactInfo
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.FileCopyUtils
import org.apache.commons.lang3.StringUtils

import eu.openminted.interop.componentoverview.model.ComponentMetaData;;

class FindAndStoreArtifactsPOM {
	
		static File generateArtifactDescriptorAndPOM(ModelRepository repo,String grpID, String version){
			
		HashMap<File,String> componentDirMap = new HashMap<File, String>();
		String dkproGroupId= grpID;
		File dkproDescriptorFolder = new File("target/generated-docs/descriptors/crawled-dkprocore");

		if(!dkproDescriptorFolder.absoluteFile.exists())
			dkproDescriptorFolder.mkdirs();

		componentDirMap.put(dkproDescriptorFolder,dkproGroupId);

		Set<ArtifactInfo> searchResult = repo.getArtifacts(grpID,null,version,null,null).toList().sort { a,b ->
			a.artifactVersion.compareTo(b.artifactVersion)
		};

		HashMap<String , String > filteredResult = new HashMap<String, String>();
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
			//			println "${f}  -- version -- ${filteredResult.get(f)}  " + resolver.getResources("classpath*:de/**/*.xml");
			UrlResource[] files = resolver.getResources("classpath*:META-INF/**/pom.xml");
			UrlResource[] filesDescriptor = resolver.getResources("classpath*:de/**/*.xml");

			files.each {fileinfo->
				String fileName = fileinfo.cleanedUrl.toString();
				String[] pathArr= fileName.split("/jars/");
				String artifactName;
				if(pathArr[1])
				{
					String[] artifactNameArr = pathArr[1].split(".jar!")
					artifactName = artifactNameArr[0];
					//					println(artifactName)
				}
				new File(dkproDescriptorFolder.getPath()+ "/"+artifactName).mkdir();
				File tempFile = new File(dkproDescriptorFolder.getPath()+"/"+artifactName+ "/"+ fileinfo.filename);
				tempFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(tempFile);
				FileCopyUtils.copy(fileinfo.getInputStream(),fos);
				fos.close();
			}
			filesDescriptor.each {fileinfo->
				String fileName = fileinfo.cleanedUrl.toString();
				String[] pathArr= fileName.split("/jars/");
				String artifactName;
				if(pathArr[1])
				{
					String[] artifactNameArr = pathArr[1].split(".jar!")
					artifactName = artifactNameArr[0];
					//					println(artifactName)
				}
				if(!new File(dkproDescriptorFolder.getPath()+ "/"+artifactName).exists())
				{
					new File(dkproDescriptorFolder.getPath()+ "/"+artifactName).mkdir();
				}
				File tempFile = new File(dkproDescriptorFolder.getPath()+"/"+artifactName+ "/"+ fileinfo.filename);
				tempFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(tempFile);
				FileCopyUtils.copy(fileinfo.getInputStream(),fos);
				fos.close();
			}						
		}
		return dkproDescriptorFolder;
	}
	static List<ComponentMetaData> addPOMInfo(List<ComponentMetaData> components){
		File parentDir;
		components.each {component->
			 parentDir = new File(component.source).parentFile;
			 if(new File(parentDir.path+"/"+"pom.xml").exists())
			 {
				 component.POMUrl = StringUtils.substringAfter(parentDir.path +"/"+"pom.xml","target/generated-docs/");				 
			 }			 
		}
		return components;
	}
}
