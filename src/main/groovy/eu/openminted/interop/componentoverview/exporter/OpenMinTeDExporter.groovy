package eu.openminted.interop.componentoverview.exporter

import org.w3c.dom.Element;

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class OpenMinTeDExporter implements Exporter<Writable> {
	@Override
	public Writable process(ComponentMetaData aMetaData) {
		def markupBuilder = new StreamingMarkupBuilder();

		def xml = markupBuilder.bind { builder ->
			mkp.xmlDeclaration()
			namespaces << ['': 'http://www.meta-share.org/OMTD-SHARE_XMLSchema']
			namespaces << ['xsi': 'http://www.w3.org/2001/XMLSchema-instance']
			componentMetadataRecord ('xsi:schemaLocation':'http://www.meta-share.org/OMTD-SHARE_XMLSchema OMTD-SHARE-Component.xsd') {
				componentInfo {
					identificationInfo {
						resourceNames { 
							resourceName(lang:'en') { mkp.yield aMetaData.name }
						}
						descriptions {
							description(lang:'en') { mkp.yield aMetaData.description }
						}
					}
					contactInfo {

					}
				
					distributionInfos {
						componentDistributionInfo {
							rightsInfo {
								licenseInfos {
									aMetaData.license.each{ lic->
										licenceInfo {
											license{
												name {mkp.yield lic.name}
												url {mkp.yield lic.url}
												distribution {mkp.yield lic.distribution}
											}
										}
									}
								}
							}
						}
					}
				}

			}
		}

		return xml;
	}
}
