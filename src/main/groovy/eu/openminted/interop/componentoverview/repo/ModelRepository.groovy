package eu.openminted.interop.componentoverview.repo;

import org.apache.lucene.search.BooleanQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.BooleanClause.Occur
import org.apache.maven.index.ArtifactInfo
import org.apache.maven.index.Field
import org.apache.maven.index.Indexer
import org.apache.maven.index.IteratorSearchRequest
import org.apache.maven.index.IteratorSearchResponse
import org.apache.maven.index.MAVEN
import org.apache.maven.index.context.IndexCreator
import org.apache.maven.index.context.IndexingContext
import org.apache.maven.index.expr.SourcedSearchExpression
import org.apache.maven.index.updater.IndexUpdateRequest
import org.apache.maven.index.updater.IndexUpdateResult
import org.apache.maven.index.updater.IndexUpdater
import org.apache.maven.index.updater.ResourceFetcher
import org.apache.maven.index.updater.WagonHelper
import org.apache.maven.wagon.Wagon
import org.codehaus.plexus.DefaultContainerConfiguration
import org.codehaus.plexus.DefaultPlexusContainer
import org.codehaus.plexus.PlexusConstants
import org.sonatype.aether.util.version.GenericVersionScheme
import org.apache.maven.wagon.observers.AbstractTransferListener
import org.apache.maven.wagon.authentication.AuthenticationInfo;
import org.apache.maven.wagon.events.TransferEvent;
import org.apache.maven.wagon.events.TransferListener

public class ModelRepository
{
    def Indexer indexer;

    def IndexingContext centralContext;
    def IndexingContext ukpContext;
    def IndexingContext ukpStagingContext;
    def IndexingContext ukpPrivateContext;
    
    ModelRepository() {
        DefaultContainerConfiguration config = new DefaultContainerConfiguration();
        config.setClassPathScanning( PlexusConstants.SCANNING_INDEX );
        DefaultPlexusContainer plexusContainer = new DefaultPlexusContainer(config);

        // lookup the indexer components from plexus
        indexer = plexusContainer.lookup( Indexer.class );
        IndexUpdater indexUpdater = plexusContainer.lookup( IndexUpdater.class );
        // lookup wagon used to remotely fetch index
        Wagon httpWagon = plexusContainer.lookup( Wagon.class, "http" );
        
        // Creators we want to use (search for fields it defines)
        List<IndexCreator> indexers = new ArrayList<IndexCreator>();
        indexers.add( plexusContainer.lookup( IndexCreator.class, "min" ) );
        indexers.add( plexusContainer.lookup( IndexCreator.class, "jarContent" ) );
        indexers.add( plexusContainer.lookup( IndexCreator.class, "maven-plugin" ) );

        // Create context for central repository index
        centralContext = indexer.createIndexingContext( "central-context", "central ", 
                new File( "target/central-cache" ), new File( "target/central-index" ), 
                "http://repo1.maven.org/maven2", 
                null, true, true, indexers );
            
        TransferListener listener = new AbstractTransferListener() {
            public void transferStarted( TransferEvent transferEvent )
            {
                print "   Downloading ${transferEvent.resource.name} -> ${transferEvent.localFile}";
            }

            public void transferProgress( TransferEvent transferEvent, byte[] buffer, int length )
            {
            }

            public void transferCompleted( TransferEvent transferEvent )
            {
                System.out.println( " - Done" );
            }
        };
    
//         println "Not doing index updates"
//         return
            
        //Date centralContextCurrentTimestamp = centralContext.getTimestamp();
        central: {
            ResourceFetcher fetcher = new WagonHelper.WagonFetcher( httpWagon, listener, null, null );
            IndexUpdateRequest updateRequest = new IndexUpdateRequest( centralContext, fetcher );
            IndexUpdateResult updateResult = indexUpdater.fetchAndUpdateIndex( updateRequest );
        }
    }

    IteratorSearchResponse getJarVersions(String groupId, String artifactId)
    {
        IndexSearcher searcher = centralContext.acquireIndexSearcher();
        
        // construct the query for known GA
        final Query groupIdQ =
            indexer.constructQuery(MAVEN.GROUP_ID, new SourcedSearchExpression(groupId));
        final Query artifactIdQ =
            indexer.constructQuery(MAVEN.ARTIFACT_ID, new SourcedSearchExpression(artifactId));
        final BooleanQuery query = new BooleanQuery();
        query.add( groupIdQ, Occur.MUST );
        query.add( artifactIdQ, Occur.MUST );

        // we want "jar" artifacts only
        query.add(indexer.constructQuery(MAVEN.PACKAGING, new SourcedSearchExpression("jar")),
            Occur.MUST);
        // we want main artifacts only (no classifier)
        // Note: this below is unfinished API, needs fixing
        query.add( indexer.constructQuery( MAVEN.CLASSIFIER,
            new SourcedSearchExpression(Field.NOT_PRESENT)), Occur.MUST_NOT);
        
        final IteratorSearchRequest request =
            new IteratorSearchRequest( query, Collections.singletonList( centralContext ) );
        IteratorSearchResponse response = indexer.searchIterator( request );
        return response;

    }

    IteratorSearchResponse getModels(String group, String tool, String language, String variant) 
    {
        IndexSearcher searcher = centralContext.acquireIndexSearcher();
        
        // construct the query for known GA
        final Query groupIdQ =
            indexer.constructQuery( MAVEN.GROUP_ID, new SourcedSearchExpression(
                "de.tudarmstadt.ukp.dkpro.core" ) );
        final Query artifactIdQ =
            indexer.constructQuery( MAVEN.ARTIFACT_ID, new SourcedSearchExpression(
                "de.tudarmstadt.ukp.dkpro.core.${group}-model-${tool}-${language}-${variant}" ) );
        final BooleanQuery query = new BooleanQuery();
        query.add( groupIdQ, Occur.MUST );
        query.add( artifactIdQ, Occur.MUST );

        // we want "jar" artifacts only
        query.add( indexer.constructQuery(MAVEN.PACKAGING, new SourcedSearchExpression( "jar" ) ), 
            Occur.MUST );
        // we want main artifacts only (no classifier)
        // Note: this below is unfinished API, needs fixing
        query.add( indexer.constructQuery( MAVEN.CLASSIFIER, 
            new SourcedSearchExpression( Field.NOT_PRESENT ) ), Occur.MUST_NOT );
        
        final IteratorSearchRequest request =
            new IteratorSearchRequest( query, [ukpContext, ukpPrivateContext, ukpStagingContext ] );
        IteratorSearchResponse response = indexer.searchIterator( request );
        return response;
    }
}
