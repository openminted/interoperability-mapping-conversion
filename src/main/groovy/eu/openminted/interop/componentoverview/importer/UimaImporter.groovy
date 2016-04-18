package eu.openminted.interop.componentoverview.importer;

import static eu.openminted.interop.componentoverview.Util.*;

import java.io.File;
import java.util.List;

import eu.openminted.interop.componentoverview.model.ComponentMetaData;
import eu.openminted.interop.componentoverview.model.ParameterMetaData
import groovy.xml.QName;

public class UimaImporter implements Importer<ComponentMetaData>
{
    private String collection;
    
    public UimaImporter(String aCollection)
    {
        collection = aCollection;
    }
    
    @Override
    public List<ComponentMetaData> process(File aFile)
    {
        Node descriptor = new XmlParser().parse(aFile);
        
        assert descriptor.name() instanceof QName;
		def paraList=[]
		descriptor.'**'.'configurationParameter'.each { param->
			def p = new ParameterMetaData();
			p.name = param.get('name').text();
			p.description = param.get('description').text();
			p.type = param.get('type').text();
			p.multiValued = param.get('multiValued').text();
			p.mandatory = param.get('mandatory').text();
			paraList.add(p);
//			println p.name
		}
        switch (descriptor.name().localPart) {
            case 'analysisEngineDescription':
                return parseUimaAnalysisEngine(collection, descriptor,paraList);
            case 'taeDescription':
                return parseTAEDescription(collection, descriptor,paraList);
            case 'collectionReaderDescription':
                return parseUimaCollectionReader(collection, descriptor,paraList);
            case 'casConsumerDescription':
                return parseUimaCasConsumer(collection, descriptor,paraList);
            case 'analysisEngineDeploymentDescription':
                println "Ignoring analysisEngineDeploymentDescription in ${aFile}"
                return [];
            case 'typeSystemDescription':
                println "Ignoring typeSystemDescription in ${aFile}"
                return [];
            default:
                throw new IllegalStateException("Unknown descriptor type ${descriptor.name().localPart} in ${aFile}");
        }
    }

    public List<ComponentMetaData> parseUimaAnalysisEngine(collection, resource, paraList) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = shortDesc(resource.'analysisEngineMetaData'.'description'.text());
		meta.parameters = paraList;
        return [meta];
    }

    public List<ComponentMetaData> parseTAEDescription(collection, resource,paraList) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = shortDesc(resource.'analysisEngineMetaData'.'description'.text());
		meta.parameters = paraList;
        return [meta];
    }
            
    public List<ComponentMetaData> parseUimaCollectionReader(collection, resource,paraList) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());
		meta.parameters = paraList;
		return [meta]
    }
        
    public List<ComponentMetaData> parseUimaCasConsumer(collection, resource,paraList) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());
		meta.parameters = paraList;
        return [meta];
    }
}
