package eu.openminted.interop.componentoverview.importer;

import java.io.File;
import java.net.URL;
import java.util.List;
import org.apache.maven.index.ArtifactInfo

import eu.openminted.interop.componentoverview.model.ComponentMetaData;
import eu.openminted.interop.componentoverview.model.ParameterMetaData

public class CreoleImporter implements Importer<ComponentMetaData>
{
    @Override
    public List<ComponentMetaData> process(URL aURL, ComponentMetaData metadata)
    {
        def descriptor = new XmlParser().parse(aURL.toURI().toString());
               
        def components=[]
        descriptor.'**'.'RESOURCE'.each { resource ->
            def meta = new ComponentMetaData(metadata);
            meta.source = aURL.path;
            meta.framework = "GATE";
            meta.name = resource.'NAME'.text();
            meta.version = "unknown";
            meta.implementation = resource.'CLASS'.text();
            meta.description = resource.'COMMENT'.text();
            meta.documentationUrl = resource.'HELPURL'.text();
   
            meta.inputs = [];
            meta.outputs = [];
    
			def paraList=[]
			resource.'**'.'PARAMETER'.each { param->
				def paramLocal = new ParameterMetaData();
				paramLocal.name = param.'@NAME'
				paramLocal.type = param.value().text();
				paramLocal.defaultValue = param.'@DEFAULT';
				paramLocal.runTime = param.'@RUNTIME';
				paraList.add(paramLocal);
			}
			meta.parameters = paraList;   
			components << meta   
        }
        
        if (components.isEmpty()) {
            println "No resources found in $aURL";
        }
        
        return components;
    }
}
