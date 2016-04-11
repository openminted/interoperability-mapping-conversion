package eu.openminted.interop.componentoverview.importer;

import java.io.File;
import java.util.List;

import eu.openminted.interop.componentoverview.model.ComponentMetaData;

public class CreoleImporter implements Importer<ComponentMetaData>
{
    @Override
    public List<ComponentMetaData> process(File aFile)
    {
        def descriptor = new XmlParser().parse(aFile);
        
        def components = [];
        
        descriptor.'**'.'RESOURCE'.each { resource ->
            def meta = new ComponentMetaData();
            meta.framework = "GATE";
            meta.name = resource.'NAME'.text();
            meta.implementation = resource.'CLASS'.text();
            meta.description = resource.'COMMENT'.text();
            
            components << meta;
        }
        
        if (components.isEmpty()) {
            println "No resources found in $aDescriptor";
        }
        
        return components;
    }
}
