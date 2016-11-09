package eu.openminted.interop.componentoverview.exporter

import org.w3c.dom.Element;

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import eu.openminted.interop.componentoverview.model.Constants;
import eu.openminted.interop.componentoverview.model.InputOutputMetaData;
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class OpenMinTeDExporter implements Exporter<Node> {
	@Override
	public Node process(ComponentMetaData aMetaData) {
		def markupBuilder = new StreamingMarkupBuilder();

		def xml = markupBuilder.bind { builder ->
			mkp.xmlDeclaration()
			namespaces << ['': 'http://www.meta-share.org/OMTD-SHARE_XMLSchema']
			namespaces << ['xsi': 'http://www.w3.org/2001/XMLSchema-instance']
			componentMetadataRecord ('xsi:schemaLocation':'http://www.meta-share.org/OMTD-SHARE_XMLSchema http://openminted.github.io/openminted-site/releases/omtd-share/1.0.0/xsd/OMTD-SHARE-Component.xsd') {
				metadataHeaderInfo{
					metadataRecordIdentifier(metadataIdentifierSchemeName:'urn'){
						if(aMetaData.meta){
							if(aMetaData.meta.gId && aMetaData.meta.aId){
								mkp.yield "mvn:{grpID:{"+aMetaData.meta.gId+"},artifactID:{"+aMetaData.meta.aId+"}}";
							}else {
								mkp.yield "unknown"
							}
						}
					}
					metadataCreationDate{
						if(aMetaData.meta)
							mkp.yield aMetaData.meta.creationDate
					}
					metadataLastDateUpdated{
						if(aMetaData.meta)
							mkp.yield aMetaData.meta.updatedDate
					}
				}
				componentInfo {
					resourceType{ mkp.yield "component" }
					identificationInfo {
						resourceNames {
							resourceName(lang:'en') { mkp.yield aMetaData.name }
						}
						descriptions {
							description(lang:'en') { mkp.yield aMetaData.description }
						}
						identifiers {
							identifier(resourceIdentifierSchemeName:'urn') {
								if(aMetaData.meta){
									if(aMetaData.meta.aId){
										mkp.yield aMetaData.meta.aId;
									}else{
										mkp.yield "unknown";
									}
								}else{
									mkp.yield "unknown";
								}
							}
						}
					}
					contactInfo{
						landingPage {
							if(aMetaData.projURL){
								mkp.yield aMetaData.projURL
							}else{
								mkp.yield "http://unknown"
							}
						}
						if(aMetaData.developers){
							contactGroups{
								aMetaData.developers.each{ dev->
									contactGroup{
										groupNames{
											groupName{ mkp.yield dev.organization }
										}
										relatedOrganization{
											organizationNames{
												organizationName{ mkp.yield dev.organization }
											}
										}
									}
								}
							}
						}
						if(aMetaData.mailingLists){
							mailingLists{
								aMetaData.mailingLists.each{ ml->
									mailingListInfo{
										mailingListName{ mkp.yield ml.name }
										subscribe{ mkp.yield ml.subscribe }
										unsubscribe{ mkp.yield ml.unsubscribe }
										post{ mkp.yield ml.post }
										archive{ mkp.yield ml.archive }
									}
								}
							}
						}
					}
					if(!aMetaData.version.equals("unknown")){
						versionInfo {
							version { mkp.yield aMetaData.version }
						}
					}
					componentTypes{
						componentType{
							mkp.yield aMetaData.componentType;
						}
					}
					distributionInfos {
						componentDistributionInfo {
							if(aMetaData.githubDownloadURL)
								componentDistributionMedium{ mkp.yield "sourceCode" }
							else
								componentDistributionMedium{ mkp.yield "executableCode" }

							if(aMetaData.scm){
								downloadURLs{
									downloadURL{ mkp.yield aMetaData.githubDownloadURL }
								}
								accessURLs{
									accessURL{ mkp.yield aMetaData.githubAccessURL }
								}
							}
							rightsInfo {
								licenceInfos {
									if(aMetaData.licenses){
										aMetaData.licenses.each{lic->
											licenceInfo {
												licence {
													if(lic.name.replaceAll(" ","").toLowerCase().contains("apachelicenseversion2.0")){
														mkp.yield "ApacheLicence_2.0"
													}else{
														if(lic.name.replaceAll(" ","").toLowerCase().contains("gnu")){
															mkp.yield "GPL"
														}
														else{
															mkp.yield lic.name
														}
													}
												}
											}
										}
									}else{
										licenceInfo {
											licence { mkp.yield "nonStandardLicenceTerms" }
										}
									}
								}
							}
						}
						if(aMetaData.mvnDownloadURL){
							componentDistributionInfo {
								componentDistributionMedium{ mkp.yield "executableCode" }
								if(aMetaData.scm){

									downloadURLs{
										downloadURL{ mkp.yield aMetaData.mvnDownloadURL }
									}

									accessURLs{
										accessURL{ mkp.yield aMetaData.mvnAccessURL }
									}
								}
								rightsInfo {
									licenceInfos {
										aMetaData.licenses.each{ lic->
											licenceInfo {
												licence {
													if(lic.name.replaceAll(" ","").toLowerCase().contains("apachelicenseversion2.0")){
														mkp.yield "ApacheLicence_2.0"
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if(aMetaData.inputs) {
						inputContentResourceInfo{
							resourceTypes{
								resourceType{ mkp.yield "document" }
							}
							mediaType{ mkp.yield "text" }
							if(aMetaData.inputs.size()>0){
								annotationLevels{
									for(InputOutputMetaData iometa:aMetaData.inputs){
										for(String s : iometa.types){
											String type = s.substring(s.lastIndexOf('.') + 1);
											if(!type.empty){
												annotationLevel{
													if(Constants.ANNOTATION_LEVEL[type]) {
														mkp.yield Constants.ANNOTATION_LEVEL[type]
													}else{
														mkp.yield "other"
													}
												}
											}
										}
									}
								}
							}
						}
					}
					if(aMetaData.outputs) {
						outputResourceInfo{
							resourceTypes{
								resourceType{ mkp.yield "document" }
							}
							mediaType{ mkp.yield "text" }
							if(aMetaData.outputs.size()>0){
								annotationLevels{
									for(InputOutputMetaData iometa:aMetaData.outputs){
										for(String s : iometa.types){
											String type = s.substring(s.lastIndexOf('.') + 1);
											if(!type.empty){
												annotationLevel{
													if(Constants.ANNOTATION_LEVEL[type]) {
														mkp.yield Constants.ANNOTATION_LEVEL[type]
													}else{
														mkp.yield "other"
													}
												}
											}
										}
									}
								}
							}
						}
					}

					componentCreationInfo {
						framework {
							if(aMetaData.framework.toLowerCase().contains("uima")){
								mkp.yield "UIMA"
							}
							else if(aMetaData.framework.toLowerCase().contains("gate")){
								mkp.yield "GATE"
							}
							else {
								mkp.yield "other"
							};
						}
					}

					if(aMetaData.documentationUrl){
						componentDocumentationInfo {
							onLineHelpURL { mkp.yield aMetaData.documentationUrl }
							if(aMetaData.issueManagement){
								issueTracker {
									mkp.yield aMetaData.issueManagement.url
								}
							}
						}
					}
				}
			}
		}
		Node root = new XmlParser().parseText( xml.toString() )
		cleanNode(root)
		return root;
	}
	boolean cleanNode( Node node ) {
		node.attributes().with { a ->
			a.findAll { !it.value }.each {
				a.remove( it.key )
			}
		}
		node.children().with { kids ->
			kids.findAll {
				it instanceof Node ? !cleanNode( it ) : false
			}
			.each { kids.remove( it ) }
		}
		node.attributes() || node.children() || node.text()
	}
}
