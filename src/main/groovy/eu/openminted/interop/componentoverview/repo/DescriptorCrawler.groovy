package eu.openminted.interop.componentoverview.repo

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import eu.openminted.interop.componentoverview.model.MetaDataRecord;
import groovy.grape.Grape

import static groovy.io.FileType.FILES

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils
import org.apache.maven.index.ArtifactInfo
import org.apache.maven.model.Developer
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License
import org.apache.maven.model.MailingList
import org.apache.maven.model.Organization;
import org.apache.maven.model.Scm
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.util.FileCopyUtils

class DescriptorCrawler {

	static Map pomSourceMap = [:]
	
	//Crawls descriptors with groupID and Version 
	 HashMap<File, ComponentMetaData> crawlDescriptors(String grpID, String version){
		 
		HashMap<File,String> componentDirMap = new HashMap<File, String>()
		ModelRepository repo = new ModelRepository();

		File descriptorFolder = new File("target/generated-docs/descriptors/crawled-dkprocore")
		if(!descriptorFolder.absoluteFile.exists())
			descriptorFolder.mkdirs()

		componentDirMap.put(descriptorFolder,grpID)
		//Sorting on basis of version
		Set<ArtifactInfo> searchResult = repo.getArtifacts(grpID,null,version,null,null).toList().sort { a,b ->
			a.artifactVersion.compareTo(b.artifactVersion)
		}

		HashMap<String , ArtifactInfo > filteredResult = new HashMap<String, ArtifactInfo>()
		HashMap<File, ArtifactInfo> descAIMap = new HashMap<File, ArtifactInfo>();
		
		searchResult.each { ai->
			if(!filteredResult.containsKey(ai.artifactId)) {
				filteredResult.put(ai.artifactId,ai)
			} else{
				String ver = filteredResult.get(ai.artifactId).version.toString();
				if(ver.replace('.','').toInteger() < ai.version.toString().replace('.','').toInteger()) {
					filteredResult.put(ai.artifactId, ai)
				}
			}
		}
		
		//Grabbing for each Artifacts
		for(f in filteredResult.keySet()){
			GroovyClassLoader loader = new GroovyClassLoader(null);
			System.setProperty("grape.root", "target/test-output/grapes");
			System.setProperty("ivy.cache.dir", new File("target/test-output/grapes/grapes").absolutePath);
			System.setProperty("groovy.grape.report.downloads", "true");
			try {
				Grape.addResolver([name:'ukp',root:'http://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-snapshots',m2Compatible: 'true'])
				Grape.grab(group:grpID, module:f, version:filteredResult.get(f).version,transitive:false, classLoader: loader)
			}
			catch (RuntimeException e) {
				//				e.printStackTrace();
				println "Unable to grab ${f} -- version -- ${filteredResult.get(f)} }"

			}
			PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(loader)
			//			println "${f}  -- version -- ${filteredResult.get(f)}  " + resolver.getResources("classpath*:de/**/*.xml");
			Resource[] files = resolver.getResources("classpath*:META-INF/**/pom.xml")
			Resource[] filesDescriptor = resolver.getResources("classpath*:de/**/*.xml")

			//POM files
			files.each {fileinfo->			
//				getURL or clean it properly no internal methods 
				String fileName = fileinfo.cleanedUrl.toString()
				String[] pathArr= fileName.split("/jars/")
				String artifactName
				if(pathArr[1])
				{
					String[] artifactNameArr = pathArr[1].split(".jar!")
					artifactName = artifactNameArr[0]
					//					println(artifactName)
				}
				new File(descriptorFolder.getPath()+ "/"+artifactName).mkdir()
				File tempFile = new File(descriptorFolder.getPath()+"/"+artifactName+ "/"+ fileinfo.filename)
				tempFile.createNewFile()
				FileOutputStream fos = new FileOutputStream(tempFile)
				FileCopyUtils.copy(fileinfo.getInputStream(),fos)
				fos.close()
				descAIMap.put(tempFile,filteredResult.get(f));
			}
			//Descriptor files 
			filesDescriptor.each {fileinfo->
				String fileName = fileinfo.cleanedUrl.toString()
				String[] pathArr= fileName.split("/jars/")
				String artifactName
				if(pathArr[1])
				{
					String[] artifactNameArr = pathArr[1].split(".jar!")
					artifactName = artifactNameArr[0]
					//					println(artifactName)
				}
				if(!new File(descriptorFolder.getPath()+ "/"+artifactName).exists())
				{
					new File(descriptorFolder.getPath()+ "/"+artifactName).mkdir()
				}
				File tempFile = new File(descriptorFolder.getPath()+"/"+artifactName+ "/"+ fileinfo.filename)
				tempFile.createNewFile()
				FileOutputStream fos = new FileOutputStream(tempFile)
				FileCopyUtils.copy(fileinfo.getInputStream(),fos)
				fos.close()
				descAIMap.put(tempFile,filteredResult.get(f));
			}
		}	
		
		HashMap<File,ComponentMetaData> componentMap= new HashMap<File, ComponentMetaData>();
		descAIMap.each{it->
			File f = it.key;
			ArtifactInfo ai = it.value;
			if(f.name.endsWith('.xml') && f.name!="pom.xml"){
				ComponentMetaData meta = new ComponentMetaData();
				meta.meta = new MetaDataRecord();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				meta.meta.creationDate = format.format(new Date(ai.lastModified));
				meta.meta.updatedDate  = format.format(new Date(ai.lastModified));
				meta.meta.aId = ai.artifactId.toString();
				meta.meta.gId = ai.groupId;
				componentMap.put(f,meta);
			}
			
		}
		return componentMap;
	}
	
	// get parent pom if child pom is not able to provide sufficient info
	def getParentPom(def grpId,def artifactId, def version){
		def pomSource =pomSourceMap.get(artifactId+version);
		if(pomSource){
			return pomSource
		}

		grabArtifact(grpId,artifactId,version)
		String path = System.getProperty("ivy.cache.dir")
		String artifactPath = path + "/" + grpId + "/" + artifactId
		def pom = grabPOMFileFromDir(artifactPath + "/poms", artifactId,version)

		if(!pom){
			return null
			//			throw new IllegalStateException("Unable to find pom for the artifact")
		}else{
			pomSourceMap.put(artifactId+version, pom);
			return pom
		}
	}

	//Add pom location info in target to component object
	List<ComponentMetaData> addPOMInfo(List<ComponentMetaData> components){
		
		components.each {component->
			
			File parentDir = new File("target/generated-docs/" + StringUtils.substringAfter(component.source,"target/generated-docs/"));				
			File pom = new File(parentDir.parent +"/"+"pom.xml");			
			if(pom.exists())
			{
				component.POMUrl =  StringUtils.substringAfter(pom.path,"target/generated-docs/")				
			}
		}
		return components
	}
	
	def getVersion(String pomURL){
		String pomSource = null;
		if(pomURL.empty || pomURL == null){
			return null;
		}

		File f = new File(pomURL)
		def descriptor = new XmlParser().parse(f)
		def ver = descriptor.'version'.text()
		def parentVer = descriptor.'parent'.'version'.text()
		if(!ver&&!parentVer){
			//			def pom = getParentProperty(descriptor.'parent'.'groupId',descriptor.'parent'.'artifactId',descriptor.'parent'.'version',"version")
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getVersion(pom)
		}else{
			return ver.isEmpty()?parentVer:ver
		}
	}

	List<License> getLicense(String pomURL){
		String pomSource = null;
		if(pomURL.empty || pomURL == null){
			return null;
		}
		
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		List<License> licenses = new ArrayList<License>()
		descriptor.'licenses'.each{node->
			node.'license'.each{nodeChild->
				if(nodeChild.'name'.text() ||nodeChild.'url'.text() ||nodeChild.'distribution'.text()){
				License l = new License()
				l.setName(nodeChild.'name'.text())
				l.setUrl(nodeChild.'url'.text())
				l.setDistribution(nodeChild.'distribution'.text())
				licenses.add(l)
				}
			}
		}
		if(!licenses || licenses.empty){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getLicense(pom);
		}
		return licenses
	}

	List<MailingList> getMailLists(String pomURL){
		String pomSource = null;
		if(pomURL.empty || pomURL == null){
			return null;
		}
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		List<MailingList> mls = new ArrayList<MailingList>();
		descriptor.'mailingLists'.each{node->
			node.'mailingList'.each{nodeChild->
				if(nodeChild.'name'.text() || nodeChild.'subscribe'.text() || nodeChild.'unsubscribe'.text() || nodeChild.'archive'.text() ||nodeChild.'post'.text()){
					MailingList ml = new MailingList();
					ml.setName(nodeChild.'name'.text());
					ml.setSubscribe(nodeChild.'subscribe'.text());
					ml.setUnsubscribe(nodeChild.'unsubscribe'.text());
					ml.setArchive(nodeChild.'archive'.text());
					ml.setPost(nodeChild.'post'.text());
					mls.add(ml);
				}
			}
		}
		if(!mls || mls.empty){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getMailLists(pom);
		}
		return mls
	}

	List<Developer> getDevelopsers(String pomURL){
		String pomSource = null;
		if(pomURL.empty || pomURL == null){
			return null;
		}
		
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		List<Developer> devs = new ArrayList<Developer>();
		descriptor.'developers'.each{node->
			node.'developer'.each{nodeChild->
				if(nodeChild.'organization'.text() || nodeChild.'organizationUrl'.text()){
					Developer dev = new Developer();
					dev.setOrganization(nodeChild.'organization'.text());
					dev.setOrganizationUrl(nodeChild.'organizationUrl'.text());
					devs.add(dev);
				}
			}
		}
		if(!devs || devs.empty){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getDevelopsers(pom)
		}
		return devs
	}

	IssueManagement getIssueManagement(String pomURL){
		if(pomURL.empty || pomURL == null){
			return null;
		}
		
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		IssueManagement ism;
		if(descriptor.'issueManagement'.'system'.text() || descriptor.'issueManagement'.'url'.text()){
			ism = new IssueManagement();
			ism.setSystem(descriptor.'issueManagement'.'system'.text());
			ism.setUrl(descriptor.'issueManagement'.'url'.text());
		}
		if(!ism){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getIssueManagement(pom); 
		}
		return ism;
	}
	 Scm getScm(String pomURL){
		if(pomURL.empty || pomURL == null){
			return null;
		}
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		Scm scm;
		if(descriptor.'scm'.'connection'.text() || descriptor.'scm'.'developerConnection'.text() ){
			scm = new Scm()
			scm.setConnection(descriptor.'scm'.'connection'.text());
			scm.setDeveloperConnection(descriptor.'scm'.'developerConnection'.text());
			scm.setUrl(descriptor.'scm'.'url'.text())
		}
		if(!scm || scm.getConnection().empty || scm.getDeveloperConnection().empty || scm.getUrl().empty){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getScm(pom);
		}
		return scm;
	}
	Organization getOrg(String pomURL){
		if(pomURL.empty || pomURL == null){
			return null;
		}
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		Organization org;		
		if(descriptor.'organization'.'name'.text() || descriptor.'organization'.'url'.text()){
			org = new Organization()
			org.setName(descriptor.'organization'.'name'.text());
			org.setUrl(descriptor.'organization'.'url'.text());
		}
		if(!org){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getOrg(pom);
		}
		return org;
	}
	String getUrl(String pomURL){
		if(pomURL.empty || pomURL == null){
			return null;
		}
		def File f = new File(pomURL)
		def descriptor = new XmlSlurper().parse(f)
		String url = descriptor.'url'.text()
		if(!url||url.empty){
			def pom = getParentPom(descriptor.'parent'.'groupId'.text(),descriptor.'parent'.'artifactId'.text(),descriptor.'parent'.'version'.text())
			return getUrl(pom);
		}
		return url;
	}

	void grabArtifact(def grpId,def artifactId, def version){
		GroovyClassLoader loader = new GroovyClassLoader(this.class.classLoader);
		System.setProperty("grape.root", "target/test-output/grapes");
		System.setProperty("ivy.cache.dir", new File("target/test-output/grapes/grapes").absolutePath);
		System.setProperty("groovy.grape.report.downloads", "true");
		try {
			//          Grape.grab(group:grpId, module:artifactId, version:version,type:"jar",transitive:false, classLoader: loader);
			Grape.addResolver([name:'ukp',root:'http://zoidberg.ukp.informatik.tu-darmstadt.de/artifactory/public-snapshots',m2Compatible: 'true'])
			Grape.resolve(
					[classLoader: new GroovyClassLoader()],
					[group:grpId, module:artifactId, version:version, type:'pom', transitive:false]);
		}
		catch (RuntimeException e) {
			//          e.printStackTrace();
			printf("Unable to grab with POM for artifact: %s\n",artifactId)
		}
	}
	String grabPOMFileFromDir(String path, def artifactId, def version){
		File f = new File(path +"/" + artifactId +"-" + version + ".pom")
		if(f.exists())
		{
			return f.getAbsolutePath()
		}else
			return null
	}	
}
