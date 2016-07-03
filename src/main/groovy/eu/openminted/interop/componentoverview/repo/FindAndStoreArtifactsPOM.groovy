package eu.openminted.interop.componentoverview.repo

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import groovy.grape.Grape

import org.apache.commons.lang3.StringUtils
import org.apache.maven.index.ArtifactInfo
import org.apache.maven.model.License
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.FileCopyUtils

class FindAndStoreArtifactsPOM {

	static File generateArtifactDescriptorAndPOM(ModelRepository repo,String grpID, String version){

		HashMap<File,String> componentDirMap = new HashMap<File, String>();
		String dkproGroupId= grpID;
		File dkproDescriptorFolder = new File("target/generated-docs/descriptors/crawled-dkprocore");
		println "anshul"
		if(!dkproDescriptorFolder.absoluteFile.exists())
			dkproDescriptorFolder.mkdirs();

		componentDirMap.put(dkproDescriptorFolder,dkproGroupId);

		Set<ArtifactInfo> searchResult = repo.getArtifacts(grpID,null,version,null,null).toList().sort { a,b ->
			a.artifactVersion.compareTo(b.artifactVersion)
		};

		HashMap<String , String > filteredResult = new HashMap<String, String>();
		searchResult.each {ai->
			if(!filteredResult.containsKey(ai.artifactId)) {
				filteredResult.put(ai.artifactId,ai.version);
			} else{
				String ver = filteredResult.get(ai.artifactId);
				if(ver.toString().replace('.','').toInteger() < ai.version.toString().replace('.','').toInteger()) {
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
	static def getParentProperty(def grpId,def artifactId,def version, def type){

		grabArtifact(grpId,artifactId,version)
		String path = System.getProperty("ivy.cache.dir");
		String artifactPath = path + "/" + grpId + "/" + artifactId ;
		def pom = grabPOMFileFromDir(artifactPath + "/poms", artifactId,version)
		if(!pom)
		{
			pom = grabPOMFileFromJAR(artifactPath + "/jars", artifactId,version)
		}
		if(!pom){
			return null
			//			throw new IllegalStateException("Unable to find pom for the artifact")
		}
		if(type.equals("version")){
			return getVersion(pom)
		}else if(type.equals("license")){
			return getLicense(pom)
		}else if(type.equals("mailLists")){
			return getMailLists(pom)
		}
		else {
			throw new IllegalArgumentException("Type: Unable to recognise");
		}
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

	static def getVersion(String pomURL){
		File f = new File(pomURL);
		def descriptor = new XmlParser().parse(f);
		def ver = descriptor.'version'.text();
		def parentVer = descriptor.'parent'.'version'.text();
		if(!ver&&!parentVer){
			return getParentProperty(descriptor.'parent'.'groupId',descriptor.'parent'.'artifactId',descriptor.'parent'.'version',"version");
		}else{
			return ver.isEmpty()?parentVer:ver;
		}
	}

	static List<License> getLicense(String pomURL){
		def File f = new File(pomURL);
		def descriptor = new XmlSlurper().parse(f);
		List<License> licenses = new ArrayList<License>()
		descriptor.'licenses'.each{node->
			node.'license'.each{nodeChild->
				License l = new License()
				l.setName(nodeChild.'name'.text())
				l.setUrl(nodeChild.'url'.text())
				l.setDistribution(nodeChild.'distribution'.text())
				licenses.add(l)
			}
		}
		if(!licenses || licenses.empty){
			return getParentProperty(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text(),"license");
		}
		return licenses;
	}

	static def getMailLists(String pomURL){
		return null;
	}

	static void grabArtifact(def grpId,def artifactId, def version){
		System.setProperty("grape.root", "src/test/test-output/grapes");
		System.setProperty("ivy.cache.dir", new File("src/test/test-output/grapes/grapes").absolutePath);
		System.setProperty("groovy.grape.report.downloads", "true");

		GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
		try {
			Grape.grab(group:grpId, module:artifactId, version:version,type:"jar",transitive:false, classLoader: loader);
		}
		catch (RuntimeException e) {
			//			e.printStackTrace();
			println("Unable to grab with type: jar")
			println("Trying to grab with pom type")
			try{
				Grape.grab(group:grpId, module:artifactId, version:version,type:"pom",transitive:false, classLoader: loader);
			}catch(RuntimeException e2){
				println("Unable to get artifact: Check the resource repo");
			}

		}
	}
	static String grabPOMFileFromDir(String path, def artifactId, def version){
		File f = new File(path +"/" + artifactId +"-" + version + ".pom");
		if(f.exists())
		{
			return f.getAbsolutePath();
		}else
			return null;
	}
	static File grabPOMFileFromJAR(String pathOfArtifactJar,def artifactId,def version){
		File f = new File(pathOfArtifactJar +"/" + artifactId +"-" + version + ".jar");
		if(f.exists())
		{
			GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader);
			//			println "${f}  -- version -- ${filteredResult.get(f)}  " + resolver.getResources("classpath*:de/**/*.xml");
			UrlResource[] files = resolver.getResources("classpath*:"+pathOfArtifactJar+"/**/pom.xml");
			files.each { pom->
				if(pom.getURL().toString().contains(version))
				{
					return null;
				}

			}
		}else
			return null;
	}
}
