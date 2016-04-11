package eu.openminted.interop.componentoverview.importer;

import static eu.openminted.interop.componentoverview.Util.*;

import java.io.File;
import java.util.List;

import eu.openminted.interop.componentoverview.model.ComponentMetaData;
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
        Node descriptor = new XmlParser().parse(aDescriptor);
        
        assert descriptor.name() instanceof QName;
        
        switch (descriptor.name().localPart) {
            case 'analysisEngineDescription':
                return parseUimaAnalysisEngine(collection, descriptor);
            case 'taeDescription':
                return parseTAEDescription(collection, descriptor);
            case 'collectionReaderDescription':
                return parseUimaCollectionReader(collection, descriptor);
            case 'casConsumerDescription':
                return parseUimaCasConsumer(collection, descriptor);
            case 'analysisEngineDeploymentDescription':
                println "Ignoring analysisEngineDeploymentDescription in ${aDescriptor}"
                return [];
            case 'typeSystemDescription':
                println "Ignoring typeSystemDescription in ${aDescriptor}"
                return [];
            default:
                throw new IllegalStateException("Unknown descriptor type ${descriptor.name().localPart} in ${aDescriptor}");
        }
    }

    public List<ComponentMetaData> parseUimaAnalysisEngine(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = shortDesc(resource.'analysisEngineMetaData'.'description'.text());

        return [meta];
    }

    public List<ComponentMetaData> parseTAEDescription(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = shortDesc(resource.'analysisEngineMetaData'.'description'.text());

        return [meta];
    }
            
    public List<ComponentMetaData> parseUimaCollectionReader(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());

        return [meta];
    }
        
    public List<ComponentMetaData> parseUimaCasConsumer(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());

        return [meta];
    }
}
