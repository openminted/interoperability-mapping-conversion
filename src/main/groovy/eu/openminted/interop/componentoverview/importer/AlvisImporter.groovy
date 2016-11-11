package eu.openminted.interop.componentoverview.importer

import java.io.File;
import java.net.URL;
import java.util.List;
import org.apache.maven.index.ArtifactInfo

import eu.openminted.interop.componentoverview.model.ComponentMetaData
import eu.openminted.interop.componentoverview.model.ParameterMetaData

class AlvisImporter implements Importer<ComponentMetaData> {

	@Override
	public List<ComponentMetaData> process(URL aURL,ComponentMetaData metadata) {
		def descriptor = new XmlParser().parse(aURL.toURI().toString());


		def meta = new ComponentMetaData();
		meta.source = aURL.path;
		meta.framework = "AlvisNLP";
		meta.name = descriptor.'@short-target';
		meta.version = descriptor.'@date';
		meta.implementation = descriptor.'@target';
		meta.description = descriptor.'synopsis'.text();

		meta.inputs = [];
		meta.outputs = [];

		def paraList=[]
		for(def ele in descriptor.'module-doc'.'param-doc') {
			def p = new ParameterMetaData();
			p.name = ele.'@name'
			p.mandatory = ele."@mandatory"
			p.type = ele.'@type'
			paraList.add(p)
		}
		meta.parameters = paraList;

		return [meta];
	}
}
