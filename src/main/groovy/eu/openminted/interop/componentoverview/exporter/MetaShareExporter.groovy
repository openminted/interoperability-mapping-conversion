package eu.openminted.interop.componentoverview.exporter

import org.w3c.dom.Element;

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import groovy.xml.StreamingMarkupBuilder
import groovy.xml.XmlUtil

class MetaShareExporter implements Exporter<Writable> {
	@Override
	public Writable process(ComponentMetaData aMetaData) {
		def markupBuilder = new StreamingMarkupBuilder();

		def xml = markupBuilder.bind { builder ->
			mkp.xmlDeclaration()
			namespaces << ['': 'https://inventory.clarin.gr/META-XMLSchema/v3.0.2']
			namespaces << ['xsi': 'http://www.w3.org/2001/XMLSchema-instance']
			resourceInfo ('xsi:schemaLocation':'https://inventory.clarin.gr/META-XMLSchema/v3.0.2 https://inventory.clarin.gr/META-XMLSchema/v3.0.2/META-SHARE-Resource.xsd') {
				identificationInfo {
					resourceShortName(lang:'en') { mkp.yield aMetaData.name }
					resourceName(lang:'en') { mkp.yield aMetaData.name }
					description(lang:'en') { mkp.yield aMetaData.description }
				}
				distributionInfo {
					licenses{
						aMetaData.licenses.each{ lic->
							license{
								name {mkp.yield lic.name}
								url {mkp.yield lic.url}
								distribution {mkp.yield lic.distribution}
							}
						}
					}
				}
				contactPerson {
				}
				metadataInfo {
				}
				versionInfo {
					version { mkp.yield aMetaData.version }
				}
				resourceDocumentationInfo {
					if (aMetaData.documentationUrl) {
						documentation {
							documentInfo {
								url { mkp.yield aMetaData.documentationUrl }
							}
						}
					}
				}
				resourceComponentType {
					toolServiceInfo {
						resourceType { mkp.yield 'toolService' }
						toolServiceType {
							mkp.yield( aMetaData.name.startsWith('(service)') ? 'service' : 'tool' )
						}
						aMetaData.categories.each { category ->
							toolServiceSubtype { mkp.yield( category ) }
						}
						aMetaData.inputs.each { input ->
							inputInfo {
								mediaType { mkp.yield 'text' }
								resourceType { mkp.yield 'corpus' }
								input.types.each { type ->
									annotationType { mkp.yield type }
								}
							}
						}

						aMetaData.outputs.each { output ->
							outputInfo {
								mediaType { mkp.yield 'text' }
								resourceType { mkp.yield 'corpus' }
								output.types.each { type ->
									annotationType { mkp.yield type }
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
