package eu.openminted.interop.componentoverview

import static groovy.io.FileType.FILES

import java.text.BreakIterator;

import groovy.xml.QName;

import org.apache.commons.io.FilenameUtils;
import org.asciidoctor.AsciiDocDirectoryWalker
import org.asciidoctor.Asciidoctor
import org.asciidoctor.AttributesBuilder
import org.asciidoctor.OptionsBuilder
import org.asciidoctor.SafeMode
import org.yaml.snakeyaml.Yaml;

class ComponentsMain
{
    static def products = [
        'Mallet': [ /.*Mallet.*/],
        'OpenNLP': [ /.*OpenNLP.*/ ],
        'Cjf': [ /.*Cjf.*/ ],
        'LingPipe': [ /.*LingPipe.*/ ],
        'Stanford': [ /.*Stanford.*/ ],
        'ClearNLP': [ /.*ClearNLP.*/ ],
        'TreeTagger': [ /.*TreeTagger.*/ ],
        'EnjuParser': [ /.*EnjuParser.*/, /.*Enju Parser.*/ ],
        'GENIA': [ /.*GENIA.*/ ],
        'GATE Hepple': [ /.*Hepple.*/ ],
//        'GATE Jape': [ /.*Jape.*/ ],
//        'GATE ANNIE': [ /.*ANNIE.*/ ],
//        'GATE RussIE': [ /.*RussIE.*/ ],
//        'GATE TwitIE': [ /.*TwitIE.*/ ],
        'HunPos': [ /.*HunPos.*/ ],
        'Ogmios': [ /.*Ogmios.*/ ],
        'Jazzy': [ /.*Jazzy.*/ ],
        'Web1T': [ /.*Web1T.*/ ],
        'LBJ': [ /.*LBJ.*/ ],
        'KEA': [ /.*KEA.*/ ],
        'TermRaider': [ /.*TermRaider.*/ ],
        'MutationFinder': [ /.*MutationFinder.*/ ],
        '(service) Lupedia': [ /.*Lupedia.*/ ],
        'SPECIES': [ /.*Species.*/ ],
        '(service) TextRazor': [ /.*TextRazor.*/ ],
        'RfTagger': [ /.*RfTagger.*/ ],
        '(service) AlchemyAPI': [ /.*AlchemyAPI.*/],
        'Arktweet': [ /.*Arktweet.*/],
        'BulStem': [ /.*BulStem.*/],
        'CCG': [ /.*CCG.*/],
        'Cogroo': [ /.*Cogroo.*/],
//        'ILSP': [ /.*ILSP.*/],
        'Langdetect': [ /.*Langdetect.*/],
        'LanguageTool': [ /.*LanguageTool.*/],
        'Lucene/Solr': [ /.*Lucene.*/, /.*Solr.*/ ],
        'MaltParser': [ /.*MaltParser.*/],
        'Mate Tools': [ /.*Mate.*/],
        'MeCab': [ /.*MeCab.*/],
        'Minipar': [ /.*Minipar.*/],
        'Morpha': [ /.*Morpha.*/],
        'MstParser': [ /.*MstParser.*/],
        'NormaGene': [ /.*NormaGene.*/],
        'OpenCalais': [ /.*OpenCalais.*/],
        'Porter Stemmer': [ /.*PorterStemmer.*/],
        'RASP': [ /.*RASP.*/],
        'Sfst': [ /.*Sfst.*/],
        'Snowball': [ /.*Snowball.*/],
        'TextCat': [ /.*TextCat.*/],
        '(service) Textalytics': [ /.*Textalytics.*/],
        'JTok': [ /.*JTok.*/],
        'WordNet': [ /.*WordNet.*/],
        'Penn Bio-Tools': [ /.*Penn BioTagger.*/, /.*Penn BioTokenizer.*/ ],
        'Yatea': [ /.*Yatea.*/],
        'Zemanta': [ /.*Zemanta.*/],
        'ABNER': [ /.*ABNER.*/],
        'BioLG': [ /.*BioLG.*/],
        'Apache Commons Codec': [ /.*Apache Commons Codec.*/],
        '(service) CrowdFlower': [ /.*CrowdFlower.*/],
    ];

    static def formats = [
        'AclAnthology': [ /.*AclAnthology.*/ ],
        'BioNLP-ST 2013 a1/a2': [ /.*BioNLPST.*/ ],
        'BNC': [ /.*BNC.*/ ],
        'Brat': [ /.*Brat.*/ ],
        'CoNLL 2000': [ /.*Conll2000.*/ ],
        'CoNLL 2002': [ /.*Conll2002.*/ ],
        'CoNLL 2006': [ /.*Conll2006.*/ ],
        'CoNLL 2007': [ /.*Conll2007.*/ ],
        'CoNLL 2009': [ /.*Conll2009.*/ ],
        'CoNLL 2012': [ /.*Conll2012.*/ ],
        'CoNLL 2000': [ /.*Conll2000.*/ ],
        'CoNLL 2000': [ /.*Conll2000.*/ ],
        'DiTop': [ /.*DiTop.*/ ],
        'CadixeJSON': [ /.*CadixeJSON.*/ ],
        'Fast Infoset': [ /.*Fast Infoset.*/ ],
        'Cochrane': [ /.*cochrane.*/ ],
        'DataSift JSON': [ /.*DataSift JSON.*/ ],
        'Twitter JSON': [ /.*JSON Tweet.*/ ],
        'GATE JSON': [ /.*GATE JSON.*/ ],
        'PubMed': [ /.*pubMed.*/ ],
        'GATE XML': [ /.*GATE XML.*/, /.*GateXML.*/ ],
        'Genia JSON': [ /.*GeniaJSON.*/ ],
        'BioNLP Shared Task ': [ /.*Genia(Reader|Writer).*/ ],
        'HTML5 Microdata': [ /.*HTML5 Microdata.*/ ],
        'UIMA JSON': [ /.*UIMA JSON.*/ ],
        'KEA Corpus': [ /.*KEA Corpus.*/ ],
        'LLL': [ /.*LLL.*/ ],
        'MediaWiki markup': [ /.*MediaWiki markup.*/ ],
        'Factored Tag Lem ': [ /.*Factored Tag Lem .*/ ],
        'Alvis Enriched Document': [ /.*Alvis Enriched Document.*/ ],
        'HTML': [ /.*Html.*/ ],
        'I2B2': [ /.*I2B2.*/ ],
        'GrAF': [ /.*GrAF.*/ ],
        'PDF': [ /.*PDF.*/ ],
        'Prague Markup Language': [ /.*Prague Markup Language.*/ ],
        'XCES': [ /.*XCES.*/ ],
        'ImsCwb': [ /.*ImsCwb.*/ ],
        'JDBC': [ /.*Jdbc.*/ ],
        'NEGRA Export': [ /.*Negra.*/ ],
        'OBO': [ /.*OBO.*/ ],
        'Penn Treebank Chunked': [ /.*PennTreebankChunked.*/ ],
        'Penn Treebank Combined': [ /.*PennTreebankCombined.*/ ],
        'RDF': [ /.*RDF.*/ ],
        'TIGER-XML': [ /.*TigerXml.*/ ],
        'TüPP-D/Z': [ /.*Tuepp.*/ ],
        'Web1T': [ /.*Web1T.*/ ],
        'PDF': [ /.*PDF.*/ ],
        'RTF': [ /.*RTF.*/ ],
        'RelAnnis': [ /.*RelAnnis.*/ ],
        'Relp': [ /.*Relp.*/ ],
        'Reuters-21578': [ /.*Reuters21578.*/ ],
        'Solr': [ /.*Solr.*/ ],
        'CLARIN TCF': [ /.*Tcf.*/ ],
        'TEI-XML': [ /.*Tei.*/ ],
        'UIMA Binary CAS': [ /.*BinaryCas.*/, /.*SerializedCas.*/ ],
        'UIMA CAS Dump': [ /.*CasDump.*/ ],
        'XMI': [ /.*XMI.*/ ],
        'XML': [ /.*XML.*/ ],
        'Text': [ /.*Text.*/, /.*StringReader.*/ ], // This needs to come late because "text" appears often in descriptions
    ];

    static def categories = [
        'Flow': [ '.* Members? PR', 'Scriptable Controller', 'Segment Processing PR',
            'Annotation Merging PR', 'Annotation Set Transfer',
            'Document Reset PR' ],
        'Language Identifier': [ 'TextCat .*', '.*Language Identification.*', 
            '.*Language Identifier PR', '.*LanguageIdentifier', 'LanguageDetector.*' ],
        'Parser': [ '.*Parser', '.*Parser2', '.*Minipar .*', 'StanfordDependencyConverter',
            'Textalytics Lemmatization, PoS and Parsing' ],
        'Spelling/Grammar': [ '.*Checker', '.*SpellingCorrector', 'CorrectionsContextualizer', 
            'Textalytics Spell, Grammar and Style Proofreading' ],
        'Machine Learning': [ 'Batch Learning PR', 'Machine Learning PR' ],
        'Chunker': [ '.*Chunker' ],
        'Stemmer': [ '.*Stemmer PR', '.*BulStem.*', '.*Stemmer' ],
        'Lemmatizer': [ '.*Lemmatizer' ],
        'Viewer/Editor': [ '.*Viewer', '.*Editor', '.*Shell.*', 'GAZE', 'OAT', 'RAT-C', 'RAT-I' ],
        'Developers/Debugging': [ 'JCasHolder', 'Java Heap Dumper', 'Log4J Level: ALL',
             'Stopwatch','TagsetDescriptionStripper', 'DependencyDumper', 
             'DocumentMetaDataStripper', 'EDT Monitor', 'Unload Unused Plugins' ],
        'Scripted analytics': [ 'RunProlog', 'Script', 'Groovy scripting PR', 'UIMA Analysis Engine',
            'JAPE Transducer', 'JAPE-Plus Transducer' ],
        'Coreference': [ '.*Coreferencer', '.*CoreferenceResolver' ],
        'Sentiment': [ '.*SentimentAnalyzer', 'Textalytics Sentiment Analysis' ],
        'Classifier': [ '.*Classifier', '.*Categorization.*', '.*Classification.*' ],
        'Gazetteer': [ '.*Gazetteer', 'Sharable Gazettee', 'DictionaryAnnotator' ],
        'Tagger': [ '.*Tagger', '.*Tagger: .*', '.*POS Tagger.*', 'PosMapper', 'RASP POS Converter',
            'POS Mapper' ],
        'MorphTagger': [ '.*Morphological analyser', 'SfstAnnotator', 'CogrooFeaturizer' ],
        'Segmenter': [ '.*Tokenizer.*', '.* Splitter PR', '.*Splitter', '.* Tokeniser', 
            '.* Segmenter PR', '.*Segmenter', 'CompoundAnnotator', 'TokenMerger', 'TokenTrimmer',
            'TrailingCharacterRemover', 'ILSP Paragraph, Sentence and Token Segmentor' ],
        'Normalizer': [ '.*Normaliser', '.*Normalizer', 'ApplyChangesAnnotator', 'Backmapper',
            '.*Transformer', 'HyphenationRemover' ],
        'Reader' : [ '.*Reader', '.*Reader2', '.*Document Format', 'XcesReaderDescriptor',
            '.* Importer', 'GateXMLReaderDescriptor', 'MediaWiki Corpus Populater',
            'Twitter Corpus Populator' ],
        'Writer' : [ '.*Writer', '.*Writer2.*', '.*Exporter', '.*Export', '.*Consumer', '.*Indexer',
            'ExpressionExtract', 'ExportCadixeJSON', 'ExportAlignmentPR', 'FillDB' ],
        'Named Entity Recognizer': [ '.*NamedEntityRecognizer', '.*NER', '.* NER PR', 'ILSP NERC' ],
        'Semantics': [ 'Semantic Enrichment PR', 'SemanticFieldAnnotator' ],
        'SRL': [ '.*SemanticRoleLabeler' ],
        'Pre-built Workflows': [ 'TwitIE.*', 'RussIE.*', '.* IE System', 'Measurements', 
            'ilsp-nlp-depparser-aggregate' ],
        'Validation': [ 'Schema Enforcer' ],
        'Filtering': [ '.*Filter', 'Boilerpipe Content Detection', 'StopWordRemover' ],
        'CrowdSourcing': [ 'Entity Annotation Job Builder', /Majority-vote consensus builder (annotation)/ ],
        'Irrelevant': [ 'The Duplicator' ],
        'Keywords/Terms': [ '.* Keyphrase Extractor', 'KeywordsSelector', 'YateaExtractor' ],
        'Topics': [ 'MalletTopicModel.*', 'Textalytics Topics Extraction' ],
        'Readability': [ 'ReadabilityAnnotator' ],
        'Evaluation': [ 'IAA Computation PR', 'CompareElements' ]
    ];
    
    static def findCategories(catalog, item) {
        for (e in catalog) {
            if (e.value instanceof Map) {
                def result = findCategories(e.value, item);
                if (result) {
                    result.add(0, e.key);
                    return result;
                }
            }
            else if (e.value instanceof List) {
                for (regex in e.value) {
                    if (item ==~ '(?i)'+regex) {
                        return [e.key];
                    }
                }
            }
            else if (e.value instanceof String) {
                if (item ==~ '(?i)'+e.value) {
                    return [e.key];
                }
            }
            else {
                throw new IllegalStateException("Unknown entry: ${e}");
            }
        }
        
        return [];
    }
    
    static def parseAlvis(File aDescriptor) {
        def descriptor = new XmlParser().parse(aDescriptor);
        
        def meta = new ComponentMetaData();
        meta.framework = "AlvisNLP";
        meta.name = descriptor.'@short-target';
        meta.implementation = descriptor.'@target';
        meta.description = descriptor.'synopsis'.text();
        
        return [meta];
    }
    
    static def parseCreole(File aDescriptor) {
        def descriptor = new XmlParser().parse(aDescriptor);
        
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
    
    static def parseUimaAnalysisEngine(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'analysisEngineMetaData'.'name'.text());
        meta.implementation = resource.'annotatorImplementationName'.text();
        meta.description = shortDesc(resource.'analysisEngineMetaData'.'description'.text());

        return [meta];
    }
        
    static def parseUimaCollectionReader(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());

        return [meta];
    }
        
    static def parseUimaCasConsumer(collection, resource) {
        def meta = new ComponentMetaData();
        meta.framework = "$collection (UIMA)";
        meta.name = shortName(resource.'processingResourceMetaData'.'name'.text());
        meta.implementation = resource.'implementationName'.text();
        meta.description = shortDesc(resource.'processingResourceMetaData'.'description'.text());

        return [meta];
    }
        
    static def parseUima(String aCollection, File aDescriptor) {
        Node descriptor = new XmlParser().parse(aDescriptor);
        
        assert descriptor.name() instanceof QName;
        
        switch (descriptor.name().localPart) {
            case 'analysisEngineDescription':
                return parseUimaAnalysisEngine(aCollection, descriptor);
            case 'collectionReaderDescription':
                return parseUimaCollectionReader(aCollection, descriptor);
            case 'casConsumerDescription':
                return parseUimaCasConsumer(aCollection, descriptor);
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

    static def shortName(name) {
        if (name.contains('.')) {
            return name.tokenize('.')[-1];
        }
        return name;
    }
        
    static def shortDesc(description) {
        if (description) {
            BreakIterator tokenizer = BreakIterator.getSentenceInstance(Locale.US);
            tokenizer.setText(description);
            def start = tokenizer.first();
            def end = tokenizer.next();
            if (start > -1 && end > -1) {
                description = description.substring(start, end);
            }
            description = description
                // Remove HTML tags in tables
                .replaceAll(/<.+?>/, '')
                // Make sure the text doesn't cluse the passthrough block
                .replaceAll(']', '{endsb}')
                .trim();
        }
        return description ? "pass:[${description}]" : '__No description__';
    }

    static class ComponentMetaData {
        String name;
        String implementation;
        String description;
        String framework;
        List<String> categories;
        String product;
        String format; // Only relevant for readers and writers
    }
    
    static void main(String... args) {
        def components = [];
        
        new File("src/main/resources/components/alvis").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(parseAlvis(it));
            }
        }

        new File("src/main/resources/components/gate").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(parseCreole(it));
            }
        }

        new File("src/main/resources/components/dkprocore").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(parseUima("DKPro Core", it));
            }
        }

        new File("src/main/resources/components/ilsp").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(parseUima("ILSP", it));
            }
        }

        new File("src/main/resources/components/nactem").eachFileRecurse(FILES) {
            if (it.name.endsWith('.xml')) {
                components.addAll(parseUima("NaCTeM", it));
            }
        }

        components.each { it.categories = findCategories(categories, it.name) };
        components.each { it.format = findCategories(formats, it.name + " " +it.description.replace('\n', ' '))[0] };
        components.each {
            it.product = findCategories(products, it.name + " " +it.description.replace('\n', ' '))[0] ?: "(original) $it.framework"
            };
        
//        components
//            .groupBy { it.categories as String }
//            .each { cats, comps ->
//                println cats;
//                comps.each {
//                    printf("  %-20s %-30s %s %n", it.categories, it.name, it.description);
//                }
//            }; 
    
        println "Applying templates..."
    
        File adocTargetFolder = new File("target/generated-adoc");
    
        def te = new groovy.text.SimpleTemplateEngine(this.class.classLoader);
        new File("src/main/templates/components").eachFile(FILES) { tf ->
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
