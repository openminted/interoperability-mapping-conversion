package eu.openminted.interop.componentoverview.importer;

import static eu.openminted.interop.componentoverview.Util.*;

import java.io.File;
import java.util.List;

import eu.openminted.interop.componentoverview.model.ComponentMetaData;
import eu.openminted.interop.componentoverview.model.InputOutputMetaData
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
        
        ComponentMetaData meta;
        switch (descriptor.name().localPart) {
            case 'analysisEngineDescription':
                meta = parseUimaAnalysisEngine(collection, descriptor);
                break;
            case 'taeDescription':
                meta = parseTAEDescription(collection, descriptor);
                break;
            case 'collectionReaderDescription':
                meta = parseUimaCollectionReader(collection, descriptor);
                break;
            case 'casConsumerDescription':
                meta = parseUimaCasConsumer(collection, descriptor);
                break;
            case 'analysisEngineDeploymentDescription':
                println "Ignoring analysisEngineDeploymentDescription in ${aFile}"
                return [];
            case 'typeSystemDescription':
                println "Ignoring typeSystemDescription in ${aFile}"
                return [];
            default:
                throw new IllegalStateException("Unknown descriptor type ${descriptor.name().localPart} in ${aFile}");
        }

        meta.source = aFile.path;
        
        meta.parameters = [];
        descriptor.'**'.'configurationParameter'.each { param->
            def p = new ParameterMetaData();
            p.name = param.get('name').text();
            p.description = param.get('description').text();
            p.type = param.get('type').text();
            p.multiValued = param.get('multiValued').text();
            p.mandatory = param.get('mandatory').text();
            meta.parameters.add(p);
        }
        
        meta.inputs=[];
        descriptor.'**'.'capabilities'.'capability'.each { capability->
            def iometa = new InputOutputMetaData();
            iometa.types = [];
            capability.'inputs'.'type'.each {
                iometa.types << it.text();
            }
            meta.inputs << iometa;
        }

        meta.outputs=[];
        descriptor.'**'.'capabilities'.'capability'.each { capability->
            def iometa = new InputOutputMetaData();
            iometa.types = [];
            capability.'outputs'.'type'.each {
                iometa.types << it.text();
            }
            meta.outputs << iometa;
        }

        return [meta];
    }

    public ComponentMetaData parseUimaAnalysisEngine(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.version = resource.'analysisEngineMetaData'.'version'.text();
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = resource.'analysisEngineMetaData'.'description'.text();
        return meta;
    }

    public ComponentMetaData parseTAEDescription(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.version = resource.'analysisEngineMetaData'.'version'.text();
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = resource.'analysisEngineMetaData'.'description'.text();
        return meta;
    }
            
    public ComponentMetaData parseUimaCollectionReader(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.version = resource.'processingResourceMetaData'.'version'.text();
        meta.implementation = resource.'implementationName'.text();
        meta.description = resource.'processingResourceMetaData'.'description'.text();
		return meta;
    }
        
    public ComponentMetaData parseUimaCasConsumer(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.version = resource.'processingResourceMetaData'.'version'.text();
        meta.implementation = resource.'implementationName'.text();
        meta.description = resource.'processingResourceMetaData'.'description'.text();
        return meta;
    }
}
