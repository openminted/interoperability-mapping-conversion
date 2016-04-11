package eu.openminted.interop.componentoverview.importer

import java.io.File;
import java.util.List;

import eu.openminted.interop.componentoverview.model.ComponentMetaData

class AlvisImporter implements Importer<ComponentMetaData>
{

    @Override
    public List<ComponentMetaData> process(File aFile)
    {
        def descriptor = new XmlParser().parse(aDescriptor);
        
        def meta = new ComponentMetaData();
        meta.framework = "AlvisNLP";
        meta.name = descriptor.'@short-target';
        meta.implementation = descriptor.'@target';
        meta.description = descriptor.'synopsis'.text();
        
        return [meta];
    }
}
