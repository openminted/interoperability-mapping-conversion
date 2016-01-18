package eu.openminted.interop.mappingconversion

import static groovy.io.FileType.FILES;
import groovy.json.JsonOutput;

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.asciidoctor.AsciiDocDirectoryWalker
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.AttributesBuilder;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.SafeMode;

class Main
{
    static Map<String, Map<String, TypeDef>> typeSystems = [:];
    
    static List<Tuple2<TypeDef, TypeDef>> typeMapping = [];
    static List<Tuple2<FeatureDef, FeatureDef>> featureMapping = [];
    
    static void main(String... args) {
        List<String> files = [
            "src/test/resources/alvis.csv", 
            "src/test/resources/argo.csv", 
            "src/test/resources/dkpro.csv", 
            "src/test/resources/gate.csv", 
            "src/test/resources/ilsp.csv"
            ];
        
        files.each { parse it as File };

        Set<TypeDef> types = typeMapping
            .collect{ it.first } as TreeSet<>;
            
//        playground: {
//            return;
//        }
                    
        
        File adocTargetFolder = new File("target/generated-adoc");

        def te = new groovy.text.SimpleTemplateEngine(this.class.classLoader);
        new File("src/main/templates/").eachFile(FILES) { tf ->
            println "Processing template ${tf.name}...";
            try {
                def template = te.createTemplate(tf.getText("UTF-8"));
                def result = template.make([
                    types: types,
                    typeSystems: typeSystems,
                    typeMapping: typeMapping,
                    featureMapping: featureMapping]);
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
    
    static void parse(File mappingFile) {
        println "Processing file ${mappingFile}...";
        
        CSVParser parser = CSVFormat.DEFAULT.parse(new InputStreamReader(
            new FileInputStream(mappingFile), "UTF-8"));

        String sysA;
        String sysB;
        
        TypeDef curA;
        TypeDef curB;
        CSVRecord prevRecord;
        parser.each { record ->
            // println "Processing ${record}";
            
            // Read the type system names from the title line
            if (record.recordNumber == 1) {
                sysA = record.values[0].trim().replaceAll(' ', '_');
                sysB = record.values[3].trim().replaceAll(' ', '_');
                if (!typeSystems[sysA]) {
                    typeSystems[sysA] = [:];
                }
                if (!typeSystems[sysB]) {
                    typeSystems[sysB] = [:];
                }
                return;
            }
            
            switch (record) {
                case { isSeparator(record) }:
                    curA = null;
                    curB = null;
                    break;
                case { isHeading(record) }:
                    curA = null;
                    curB = null;
                    break;
                case { isComments(record) }:
                    curA = null;
                    curB = null;
                    break;
                case { isTypeDef(record, prevRecord) }:
                    TypeDef a = parseTypeDef(record, 0, sysA);
                    if (a) {
                        if (typeSystems[sysA][a.name]) {
                            a = typeSystems[sysA][a.name];
                            println "Using existing [${sysA}] type [${a.name}]";
                        }
                        else {
                            typeSystems[sysA][a.name] = a;
                            println "Found new [${sysA}] type [${a.name}]";
                        }
                        curA = a;
                        typeMapping << new Tuple2(a,a);
                    }
                    TypeDef b = parseTypeDef(record, 3, sysB);
                    if (b) {
                        if (typeSystems[sysB][b.name]) {
                            b = typeSystems[sysB][b.name];
                            println "Using existing [${sysB}] type [${b.name}]";
                        }
                        else {
                            typeSystems[sysB][b.name] = b;
                            println "Found new [${sysB}] type [${b.name}]";
                        }
                        curB = b;
                        typeMapping << new Tuple2(b,b);
                    }
                    if (a && b) {
                        typeMapping << new Tuple2(a,b);
                        typeMapping << new Tuple2(b,a);
                    }
                    break;
                default: // feature record then
                    FeatureDef a = parseFeatureDef(record, 0, curA);
                    if (a) {
                        if (curA.features[a.name]) {
                            a = curA.features[a.name];
                            println "Using existing [${sysA}] feature [${a.name}]";
                        }
                        else {
                            curA.features[a.name] = a;
                            println "Found new [${sysA}] feature [${a.name}]";
                        }
                        featureMapping << new Tuple2(a,a);
                    }
                    
                    FeatureDef b = parseFeatureDef(record, 3, curB);
                    if (b) {
                        if (curB.features[b.name]) {
                            b = curB.features[b.name];
                            println "Using existing [${sysB}] feature [${b.name}]";
                        }
                        else {
                            curB.features[b.name] = b;
                            println "Found new [${sysB}] feature [${b.name}]";
                        }
                        featureMapping << new Tuple2(b,b);
                    }
                    
                    if (a && b) {
                        featureMapping << new Tuple2(a,b);
                        featureMapping << new Tuple2(b,a);
                    }
                    break;
            }
            
            prevRecord = record;
        }
    }
    
    public static TypeDef parseTypeDef(CSVRecord aRecord, int aOffset, String aSystem)
    {
        TypeDef t = new TypeDef();
        t.name = aRecord.values[aOffset].trim();
        t.supertype = aRecord.values[aOffset+1].trim();
        t.description = aRecord.values[aOffset+2].trim();
        t.system = aSystem.trim();
        
        if (StringUtils.startsWith(t.name, "<similar>")) {
            t.name = t.name.substring(9).trim();
        }

        if (!t.name || t.name in ["<no equivalent>", "<no equivalence>"]) {
            return null;
        }
        
        return t;
    }
    
    public static FeatureDef parseFeatureDef(CSVRecord aRecord, int aOffset, TypeDef aType)
    {
        if (!aType) {
            println "Dangling feature at offset ${aOffset}: ${aRecord}";
            return null;
        }
        
        FeatureDef f = new FeatureDef();
        f.name = aRecord.values[aOffset].trim();
        f.type = aRecord.values[aOffset+1].trim();
        f.description = aRecord.values[aOffset+2].trim();
        f.owner = aType;

        if (StringUtils.startsWith(f.name, "<similar>")) {
            f.name = f.name.substring(9).trim();
        }
                
        if (!f.name || f.name in ["<no equivalent>", "<no equivalence>"]) {
            return null;
        }
        
        return f;
    }
    
    public static boolean isTypeDef(CSVRecord aRecord, CSVRecord aPrevRecord)
    {
        aPrevRecord && (isSeparator(aPrevRecord) || isHeading(aPrevRecord));
    }
        
    public static boolean isSeparator(CSVRecord aRecord)
    {
        aRecord.values.every { !it };
    }

    public static boolean isHeading(CSVRecord aRecord)
    {
        aRecord.values[0] && aRecord.values[1..-1].every { !it } && 
            !StringUtils.startsWith(aRecord.values[0], "Comments:");
    }

    public static boolean isComments(CSVRecord aRecord)
    {
        aRecord.values[0] && aRecord.values[1..-1].every { !it } &&
            StringUtils.startsWith(aRecord.values[0], "Comments:");
    }

    static class TypeDef implements Comparable<TypeDef>
    {
        String system;
        String name;
        String supertype;
        String description;
        Map<String, FeatureDef> features = [:];
        
        @Override
        public String toString()
        {
            "${system}.${name}";
        }

        @Override
        public int compareTo(TypeDef aOther)
        {
            (system) <=> (aOther.system) ?:
                (name) <=> (aOther.name);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((system == null) ? 0 : system.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            TypeDef other = (TypeDef) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (system == null) {
                if (other.system != null)
                    return false;
            }
            else if (!system.equals(other.system))
                return false;
            return true;
        }
    }
    
    static class FeatureDef implements Comparable<FeatureDef> {
        TypeDef owner;
        String name;
        String type;
        String description;
        
        @Override
        public int compareTo(FeatureDef aOther)
        {
            (owner) <=> (aOther.owner) ?:
                (name) <=> (aOther.name);
        }

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + ((owner == null) ? 0 : owner.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            FeatureDef other = (FeatureDef) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            }
            else if (!name.equals(other.name))
                return false;
            if (owner == null) {
                if (other.owner != null)
                    return false;
            }
            else if (!owner.equals(other.owner))
                return false;
            return true;
        }
    }
}
