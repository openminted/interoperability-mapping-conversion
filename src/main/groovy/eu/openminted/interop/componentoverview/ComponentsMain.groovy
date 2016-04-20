package eu.openminted.interop.componentoverview

import static groovy.io.FileType.FILES

import java.text.BreakIterator;

import eu.openminted.interop.componentoverview.exporter.MetaShareExporter
import eu.openminted.interop.componentoverview.importer.AlvisImporter
import eu.openminted.interop.componentoverview.importer.CreoleImporter
import eu.openminted.interop.componentoverview.importer.UimaImporter
import eu.openminted.interop.componentoverview.model.Constants;
import groovy.xml.QName
import groovy.xml.XmlUtil;

import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.AsciiDocDirectoryWalker
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder
import org.asciidoctor.OptionsBuilder
import org.asciidoctor.SafeMode
import org.yaml.snakeyaml.Yaml;

class ComponentsMain
{
    static def products =  Constants.PRODUCTS;
    static def formats = Constants.FORMATS;
    static def categories = Constants.CATEGORIES;
            
    static void main(String... args) {
        
		def components = [];
        AlvisImporter alvisParser = new AlvisImporter();
		CreoleImporter creoleParser = new CreoleImporter();
		UimaImporter uimaParserDkPro = new UimaImporter("DKPro Core");
		UimaImporter uimaParserIlsp = new UimaImporter("ILSP");
		UimaImporter uimaParserNactem = new UimaImporter("NaCTeM");
		
        new File("src/main/resources/components/alvis").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(alvisParser.process(it));
            }
        }

        new File("src/main/resources/components/gate").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(creoleParser.process(it));
            }
        }

        new File("src/main/resources/components/dkprocore").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(uimaParserDkPro.process(it));
            }
        }

        new File("src/main/resources/components/ilsp").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(uimaParserIlsp.process(it));
            }
        }

        new File("src/main/resources/components/nactem").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(uimaParserNactem.process(it));
            }
        }

        components.eachWithIndex { component, idx -> component.id = "$idx"};
        components.each { component ->
            def source = StringUtils.substringAfter(component.source, "src/main/resources/components/");
            source = 'https://github.com/openminted/interoperability-mapping-conversion/blob/master/src/main/resources/components/'+source;
            component.source = source;
        }
        components.each { it.categories = Util.findCategories(categories, it.name) };
        components.each { it.format = Util.findCategories(formats, it.name + " " +it.description.replace('\n', ' '))[0] };
        components.each {
            it.product = Util.findCategories(products, it.name + " " +it.description.replace('\n', ' '))[0] ?: "(original) $it.framework"
            };
        
//        components
//            .groupBy { it.categories as String }
//            .each { cats, comps ->
//                println cats;
//                comps.each {
//                    printf("  %-20s %-30s %s %n", it.categories, it.name, it.description);
//                }
//            }; 

        new File("target/generated-docs/metashare").mkdirs();
        components.each { component ->
            def exporter = new MetaShareExporter();
            new File("target/generated-docs/metashare/${component.id}.xml").withOutputStream { out -> 
                XmlUtil.serialize(exporter.process(component), out);
            }
        };

        println "Applying templates..."
    
        File adocTargetFolder = new File("target/generated-adoc");
    
        def te = new groovy.text.SimpleTemplateEngine(ComponentsMain.class.classLoader);
        new File("src/main/templates/components").eachFile(FILES) { tf ->
            if (!(tf.name.endsWith(".xml") || tf.name.endsWith(".adoc"))) {
                return;
            }
            
            println "Processing template ${tf.name}...";
            
            File mixin = new File(FilenameUtils.removeExtension(tf.path)+'.yaml');
            
            def data = [:];
            if (mixin.exists()) {
                data = mixin.withInputStream { new Yaml().load(it) };
            }
            
            try {
                def template = te.createTemplate(tf.getText("UTF-8"));
                def result = template.make([
                    data: data,
                    components: components]);
                def output = new File(adocTargetFolder, "${tf.name}");
                output.parentFile.mkdirs();
                output.setText(result.toString(), 'UTF-8');
            }
            catch (Exception e) {
                te.setVerbose(true);
                te.createTemplate(tf.getText("UTF-8"));
                throw e;
            }
        }
        
        println "Rendering..."

        Asciidoctor asciidoctor = Asciidoctor.Factory.create();
        
        Map<String, Object> attributes = AttributesBuilder.attributes()
            .linkCss(false)
            .dataUri(true)
            .asMap();
            
        attributes['generated-dir'] = adocTargetFolder.absolutePath+'/';
        attributes['toc'] = 'left';
        attributes['docinfo1'] = true;
        attributes['toclevels'] = 8;
        attributes['sectanchors'] = true;
        
        OptionsBuilder options = OptionsBuilder.options()
            .backend('html5')
            .safe(SafeMode.UNSAFE)
            .mkDirs(true)
            .attributes(attributes)
            .docType("book")
            .toDir(new File("target/generated-docs/"));

        asciidoctor.renderDirectory(new AsciiDocDirectoryWalker("src/main/asciidoc"), options);
        
        println "Done!"
    }
}
