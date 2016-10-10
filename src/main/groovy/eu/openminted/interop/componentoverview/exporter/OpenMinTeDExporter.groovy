package eu.openminted.interop.componentoverview.exporter

import org.w3c.dom.Element;

import eu.openminted.interop.componentoverview.model.ComponentMetaData
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
			componentMetadataRecord ('xsi:schemaLocation':'http://www.meta-share.org/OMTD-SHARE_XMLSchema OMTD-SHARE-Component.xsd') {
				metadataHeaderInfo{
					metadataRecordIdentifier{}
					metadataCreationDate{}
					metadataLastDateUpdated{}
				}
				componentInfo {
					resourceType{}
					identificationInfo {
						resourceNames {
							resourceName(lang:'en') { mkp.yield aMetaData.name }
						}
						descriptions {
							description(lang:'en') { mkp.yield aMetaData.description }
						}
						identifiers {
							identifier(resourceIdentifierSchemeName:'doi') { mkp.yield aMetaData.id }
						}
					}
					contactInfo{
						landingPage { mkp.yield aMetaData.projURL }
						contactGroups{
							aMetaData.developers.each{dev->
								contactGroup{
									groupNames{
										groupName{}
									}
									relatedOrganization{
										organizationNames{
											organizationName{ mkp.yield dev.organization
											} }
									}
								}
							}
						}
						mailingLists{
							aMetaData.mailingLists.each{ml->
								mailingListInfo{
									mailingListName{mkp.yield ml.name}
									subscribe{mkp.yield ml.subscribe}
									unsubscribe{mkp.yield ml.unsubscribe}
									post{mkp.yield ml.post}
									archive{mkp.yield ml.archive}
								}
							}
						}
					}
					versionInfo {
						version { mkp.yield aMetaData.version }
					}
					componentTypes{
						componentType{}
					}					
					distributionInfos {
						componentDistributionInfo {
							componentDistributionMedium{mkp.yield "sourceCode"}
							if(aMetaData.scm){
								downloadURLs{
									downloadURL{mkp.yield aMetaData.scm.connection}
									downloadURL{mkp.yield aMetaData.scm.developerConnection}
								}
								accessURLs{
									accessURL{mkp.yield aMetaData.scm.url}
								}
							}
							rightsInfo {
								licenceInfos {
									aMetaData.licenses.each{ lic->
										licenceInfo {
											licence { mkp.yield lic.name}
											nonStandardLicenceTermsURL {mkp.yield lic.url}
										}
									}
								}
							}
						}
					}
					componentCreationInfo {
						framework { mkp.yield aMetaData.framework }
					}

					componentDocumentationInfo {
						onLineHelpURL { mkp.yield aMetaData.documentationUrl }
						if(aMetaData.issueManagement){
							issueTracker { mkp.yield aMetaData.issueManagement.url }
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
			a.findAll { !it.value }.each { a.remove( it.key ) }
		}
		node.children().with { kids ->
			kids.findAll { it instanceof Node ? !cleanNode( it ) : false }
			.each { kids.remove( it ) }
		}
		node.attributes() || node.children() || node.text()
	}
}
