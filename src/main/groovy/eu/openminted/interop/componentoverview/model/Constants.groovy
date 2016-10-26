package eu.openminted.interop.componentoverview.model

class Constants {

	static final def PRODUCTS = [
		'Mallet': [/.*Mallet.*/],
		'OpenNLP': [/.*OpenNLP.*/],
		'Cjf': [/.*Cjf.*/],
		'IULA': [/.*IULA.*/],
		'MLRS': [/.*MLRS.*/],
		'CRF++': [/.*CRF[+]{2}.*/],
		'SVMLight': [/.*SVMLight.*/],
		'STePP': [/.*STePP.*/],
		'(service) UAIC': [/.*UAIC.*/],
		'GATE Hepple': [/.*Hepple.*/],
		//        'GATE Jape': [ /.*Jape.*/ ],
		//        'GATE ANNIE': [ /.*ANNIE.*/ ],
		//        'GATE RussIE': [ /.*RussIE.*/ ],
		//        'GATE TwitIE': [ /.*TwitIE.*/ ],
		'Java BreakIterator': [/.*BreakIterator.*/],
		'BANNER': [/.*Banner.*/],
		'BioCreative': [/.*BioCreative.*/],
		'FreeLing': [/.*FreeLing.*/],
		'LingPipe': [/.*LingPipe.*/],
		'Stanford': [/.*Stanford.*/],
		'ClearNLP': [/.*ClearNLP.*/],
		'TreeTagger': [/.*TreeTagger.*/],
		'EnjuParser': [
			/.*EnjuParser.*/,
			/.*Enju Parser.*/
		],
		'GENIA': [/.*GENIA.*/],
		'HunPos': [/.*HunPos.*/],
		'Ogmios': [/.*Ogmios.*/],
		'Jazzy': [/.*Jazzy.*/],
		'Web1T': [/.*Web1T.*/],
		'LBJ': [/.*LBJ.*/],
		'KEA': [/.*KEA.*/],
		'TermRaider': [/.*TermRaider.*/],
		'MutationFinder': [/.*MutationFinder.*/],
		'(service) Lupedia': [/.*Lupedia.*/],
		'SPECIES': [/.*Species.*/],
		'(service) TextRazor': [/.*TextRazor.*/],
		'RfTagger': [/.*RfTagger.*/],
		'(service) AlchemyAPI': [/.*AlchemyAPI.*/],
		'Arktweet': [/.*Arktweet.*/],
		'BulStem': [/.*BulStem.*/],
		'CCG': [/.*CCG.*/],
		'Cogroo': [/.*Cogroo.*/],
		//        'ILSP': [ /.*ILSP.*/],
		'Langdetect': [/.*Langdetect.*/],
		'LanguageTool': [/.*LanguageTool.*/],
		'Lucene/Solr': [/.*Lucene.*/, /.*Solr.*/],
		'MaltParser': [/.*MaltParser.*/],
		'Mate Tools': [/.*Mate.*/],
		'MeCab': [/.*MeCab.*/],
		'Minipar': [/.*Minipar.*/],
		'Morpha': [/.*Morpha.*/],
		'MstParser': [/.*MstParser.*/],
		'NormaGene': [/.*NormaGene.*/],
		'OpenCalais': [/.*OpenCalais.*/],
		'Porter Stemmer': [/.*PorterStemmer.*/],
		'RASP': [/.*RASP.*/],
		'Sfst': [/.*Sfst.*/],
		'Snowball': [/.*Snowball.*/],
		'TextCat': [/.*TextCat.*/],
		'(service) Textalytics': [/.*Textalytics.*/],
		'JTok': [/.*JTok.*/],
		'WordNet': [/.*WordNet.*/],
		'Penn Bio-Tools': [
			/.*Penn BioTagger.*/,
			/.*Penn BioTokenizer.*/
		],
		'Yatea': [/.*Yatea.*/],
		'Zemanta': [/.*Zemanta.*/],
		'ABNER': [/.*ABNER.*/],
		'BioLG': [/.*BioLG.*/],
		'Apache Commons Codec': [/.*Apache Commons Codec.*/],
		'(service) CrowdFlower': [/.*CrowdFlower.*/],
	];

	static final def FORMATS = [
		'AclAnthology': [/.*AclAnthology.*/],
		'BioNLP-ST 2013 a1/a2': [/.*BioNLPST.*/],
		'BNC': [/.*BNC.*/],
		'Brat': [/.*Brat.*/],
		'CoNLL 2000': [/.*Conll2000.*/],
		'CoNLL 2002': [/.*Conll2002.*/],
		'CoNLL 2006': [/.*Conll2006.*/],
		'CoNLL 2007': [/.*Conll2007.*/],
		'CoNLL 2009': [/.*Conll2009.*/],
		'CoNLL 2012': [/.*Conll2012.*/],
		'CoNLL 2000': [/.*Conll2000.*/],
		'CoNLL 2000': [/.*Conll2000.*/],
		'DiTop': [/.*DiTop.*/],
		'CadixeJSON': [/.*CadixeJSON.*/],
		'Fast Infoset': [/.*Fast Infoset.*/],
		'Cochrane': [/.*cochrane.*/],
		'DataSift JSON': [/.*DataSift JSON.*/],
		'Twitter JSON': [/.*JSON Tweet.*/],
		'GATE JSON': [/.*GATE JSON.*/],
		'PubMed': [/.*pubMed.*/],
		'GATE XML': [
			/.*GATE XML.*/,
			/.*GateXML.*/
		],
		'Genia JSON': [/.*GeniaJSON.*/],
		'BioNLP Shared Task ': [/.*Genia(Reader|Writer).*/],
		'HTML5 Microdata': [/.*HTML5 Microdata.*/],
		'UIMA JSON': [/.*UIMA JSON.*/],
		'KEA Corpus': [/.*KEA Corpus.*/],
		'LLL': [/.*LLL.*/],
		'MediaWiki markup': [/.*MediaWiki markup.*/],
		'Factored Tag Lem ': [/.*Factored Tag Lem .*/],
		'Alvis Enriched Document': [
			/.*Alvis Enriched Document.*/
		],
		'HTML': [/.*Html.*/],
		'I2B2': [/.*I2B2.*/],
		'GrAF': [/.*GrAF.*/],
		'PDF': [/.*PDF.*/],
		'Prague Markup Language': [
			/.*Prague Markup Language.*/
		],
		'XCES': [/.*XCES.*/],
		'ImsCwb': [/.*ImsCwb.*/],
		'JDBC': [/.*Jdbc.*/],
		'NEGRA Export': [/.*Negra.*/],
		'OBO': [/.*OBO.*/],
		'Penn Treebank Chunked': [/.*PennTreebankChunked.*/],
		'Penn Treebank Combined': [/.*PennTreebankCombined.*/],
		'RDF': [/.*RDF.*/],
		'TIGER-XML': [/.*TigerXml.*/],
		'TüPP-D/Z': [/.*Tuepp.*/],
		'Web1T': [/.*Web1T.*/],
		'PDF': [/.*PDF.*/],
		'RTF': [/.*RTF.*/],
		'RelAnnis': [/.*RelAnnis.*/],
		'Relp': [/.*Relp.*/],
		'Reuters-21578': [/.*Reuters21578.*/],
		'Solr': [/.*Solr.*/],
		'CLARIN TCF': [/.*Tcf.*/],
		'TEI-XML': [/.*Tei.*/],
		'UIMA Binary CAS': [
			/.*BinaryCas.*/,
			/.*SerializedCas.*/
		],
		'UIMA CAS Dump': [/.*CasDump.*/],
		'XMI': [/.*XMI.*/],
		'XML': [/.*XML.*/],
		'Text': [
			/.*Text.*/,
			/.*StringReader.*/
		], // This needs to come late because "text" appears often in descriptions
	];

	static def CATEGORIES = [
		'Flow': [
			'.* Members? PR',
			'Scriptable Controller',
			'Segment Processing PR',
			'Annotation Merging PR',
			'Annotation Set Transfer',
			'Document Reset PR'
		],
		'Language Identifier': [
			'TextCat .*',
			'.*Language Identification.*',
			'.*Language Identifier PR',
			'.*LanguageIdentifier',
			'LanguageDetector.*'
		],
		'Parser': [
			'.*Parser',
			'.*Parser2',
			'.*Minipar .*',
			'StanfordDependencyConverter',
			'Textalytics Lemmatization, PoS and Parsing'
		],
		'Spelling/Grammar': [
			'.*Checker',
			'.*SpellingCorrector',
			'CorrectionsContextualizer',
			'Textalytics Spell, Grammar and Style Proofreading'
		],
		'Machine Learning': [
			'Batch Learning PR',
			'Machine Learning PR'
		],
		'Chunker': ['.*Chunker'],
		'Stemmer': [
			'.*Stemmer PR',
			'.*BulStem.*',
			'.*Stemmer'
		],
		'Lemmatizer': ['.*Lemmatizer'],
		'Viewer/Editor': [
			'.*Viewer',
			'.*Editor',
			'.*Shell.*',
			'GAZE',
			'OAT',
			'RAT-C',
			'RAT-I'
		],
		'Developers/Debugging': [
			'JCasHolder',
			'Java Heap Dumper',
			'Log4J Level: ALL',
			'Stopwatch',
			'TagsetDescriptionStripper',
			'DependencyDumper',
			'DocumentMetaDataStripper',
			'EDT Monitor',
			'Unload Unused Plugins'
		],
		'Scripted analytics': [
			'RunProlog',
			'Script',
			'Groovy scripting PR',
			'UIMA Analysis Engine',
			'JAPE Transducer',
			'JAPE-Plus Transducer'
		],
		'Coreference': [
			'.*Coreferencer',
			'.*CoreferenceResolver'
		],
		'Sentiment': [
			'.*SentimentAnalyzer',
			'Textalytics Sentiment Analysis'
		],
		'Classifier': [
			'.*Classifier',
			'.*Categorization.*',
			'.*Classification.*'
		],
		'Gazetteer': [
			'.*Gazetteer',
			'Sharable Gazettee',
			'DictionaryAnnotator'
		],
		'Tagger': [
			'.*Tagger',
			'.*Tagger: .*',
			'.*POS Tagger.*',
			'PosMapper',
			'RASP POS Converter',
			'POS Mapper'
		],
		'MorphTagger': [
			'.*Morphological analyser',
			'SfstAnnotator',
			'CogrooFeaturizer'
		],
		'Segmenter': [
			'.*Tokenizer.*',
			'.* Splitter PR',
			'.*Splitter',
			'.* Tokeniser',
			'.* Segmenter PR',
			'.*Segmenter',
			'CompoundAnnotator',
			'TokenMerger',
			'TokenTrimmer',
			'TrailingCharacterRemover',
			'ILSP Paragraph, Sentence and Token Segmentor'
		],
		'Normalizer': [
			'.*Normaliser',
			'.*Normalizer',
			'ApplyChangesAnnotator',
			'Backmapper',
			'.*Transformer',
			'HyphenationRemover'
		],
		'Reader' : [
			'.*Reader',
			'.*Reader2',
			'.*Document Format',
			'XcesReaderDescriptor',
			'.* Importer',
			'GateXMLReaderDescriptor',
			'MediaWiki Corpus Populater',
			'Twitter Corpus Populator'
		],
		'Writer' : [
			'.*Writer',
			'.*Writer2.*',
			'.*Exporter',
			'.*Export',
			'.*Consumer',
			'.*Indexer',
			'ExpressionExtract',
			'ExportCadixeJSON',
			'ExportAlignmentPR',
			'FillDB'
		],
		'Named Entity Recognizer': [
			'.*NamedEntityRecognizer',
			'.*NER',
			'.* NER PR',
			'ILSP NERC'
		],
		'Semantics': [
			'Semantic Enrichment PR',
			'SemanticFieldAnnotator'
		],
		'SRL': ['.*SemanticRoleLabeler'],
		'Pre-built Workflows': [
			'TwitIE.*',
			'RussIE.*',
			'.* IE System',
			'Measurements',
			'ilsp-nlp-depparser-aggregate'
		],
		'Validation': ['Schema Enforcer'],
		'Filtering': [
			'.*Filter',
			'Boilerpipe Content Detection',
			'StopWordRemover'
		],
		'CrowdSourcing': [
			'Entity Annotation Job Builder',
			/Majority-vote consensus builder (annotation)/
		],
		'Irrelevant': ['The Duplicator'],
		'Keywords/Terms': [
			'.* Keyphrase Extractor',
			'KeywordsSelector',
			'YateaExtractor'
		],
		'Topics': [
			'MalletTopicModel.*',
			'Textalytics Topics Extraction'
		],
		'Readability': ['ReadabilityAnnotator'],
		'Evaluation': [
			'IAA Computation PR',
			'CompareElements' ]
	];
	static def ANNOTATION_LEVEL = [
		'CoreferenceChain':'discourseAnnotation-dialogueActs',
		'CoreferenceLink':'discourseAnnotation-dialogueActs',
		'Lemma':'morphosyntacticAnnotation-bPosTagging',
		'MorphologicalFeature':'morphosyntacticAnnotation-posTagging',
		'POS':'segmentation',
		'NamedEntity':'semanticAnnotation-polarity',
		'SemArg':'semanticAnnotation-speechActs',
		'SemPred':'semanticAnnotation-speechActs',
		'PhoneticTranscription':'speechAnnotation-prosodicAnnotation',
		'Stem':'structuralAnnotation',
		'Div':'structuralAnnotation-sentences',
		'Paragraph':'structuralAnnotation-sentences',
		'Heading':'structuralAnnotation-sentences',
		'Sentence':'structuralAnnotation-clauses',
		'Token':'syntacticAnnotation-subcategorizationFrames',
		'Dependency':'syntacticAnnotation-constituencyTrees',
		'Constituent':'syntacticAnnotation-chunks',
		'Chunk':'syntacticosemanticAnnotation-links'];
	
	static def COMPONENT_TYPE = [
		'accessComponent':'accesscomponent',
		'reader':'reader',
		'writer':'writer',
		'supportComponent':'supportcomponent',
		'visualiser':'visualiser',
		'debugger':'debugger',
		'validator':'validator',
		'viewer':'viewer',
		'corpusViewer':'corpusviewer',
		'lexiconViewer':'lexiconviewer',
		'editor':'editor',
		'mlTrainer':'mltrainer',
		'mlPredictor':'mlpredictor',
		'featureExtractor':'featureextractor',
		'dataSplitter':'datasplitter',
		'dataMerger':'datamerger',
		'converter':'converter',
		'evaluator':'evaluator',
		'flowController':'flowcontroller',
		'scriptBasedAnalyser':'scriptbasedanalyser',
		'matcher':'matcher',
		'gazeteerBasedMatcher':'gazeteerbasedmatcher',
		'crowdSourcingComponent':'crowdsourcingcomponent',
		'dataCollector':'datacollector',
		'crawler':'crawler',
		'processor':'processor',
		'annotator':'annotator',
		'segmenter':'segmenter',
		'stemmer':'stemmer',
		'lemmatizer':'lemmatizer',
		'morphologicalTagger':'morphologicaltagger',
		'chunker':'chunker',
		'parser':'parser',
		'coreferenceAnnotator':'coreferenceannotator',
		'namedEntityRecognizer':'namedentityrecognizer',
		'semanticsAnnotator':'semanticsannotator',
		'srlAnnotator':'srlannotator',
		'readabilityAnnotator':'readabilityannotator',
		'aligner':'aligner',
		'generator':'generator',
		'summarizer':'summarizer',
		'simplifier':'simplifier',
		'preOrPostProcessingComponent':'preorpostprocessingcomponent',
		'spellingChecker':'spellingchecker',
		'grammarChecker':'grammarchecker',
		'normalizer':'normalizer',
		'filters':'filters',
		'analyzer':'analyzer',
		'topicExtractor':'topicextractor',
		'documentClassifier':'documentclassifier',
		'languageIdentifier':'languageidentifier',
		'sentimentAnalyzer':'sentimentanalyzer',
		'keywordsExtractor':'keywordsextractor',
		'termExtractor':'termextractor',
		'contradictionDetector':'contradictiondetector',
		'eventExtractor':'eventextractor',
		'persuasiveExpressionMiner':'persuasiveexpressionminer',
		'informationExtractor':'informationextractor',
		'lexiconExtractorFromCorpora':'lexiconextractorfromcorpora',
		'lexiconExtractorFromLexica':'lexiconextractorfromlexica',
		'wordSenseDisambiguator':'wordsensedisambiguator',
		'qualitativeAnalyzer':'qualitativeanalyzer',
		'platform':'platform',
		'infrastructure':'infrastructure',
		'architecture':'architecture',
		'nlpDevelopmentEnvironment':'nlpdevelopmentenvironment',
		'other':'other'];
}
