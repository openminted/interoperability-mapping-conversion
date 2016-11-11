package eu.openminted.interop.componentoverview.model;

import java.util.List;
import java.util.ArrayList;

import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Developer;
import org.apache.maven.model.IssueManagement;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Organization;
import org.apache.maven.model.Scm;

public class ComponentMetaData
{
    String id;
    String source;
    String name;
    String version;
    String implementation;
    String description;
    String framework;
    List<String> categories;
    String product;
    String format; // Only relevant for readers and writers
    List<ParameterMetaData> parameters;
    List<InputOutputMetaData> inputs;
    List<InputOutputMetaData> outputs;
    String documentationUrl;
    String projURL;
    MetaDataRecord meta;
    String componentType;
    
    //these seem dubious to me. We should have generic scm details
    //and a download URL, but I don't think we should tie the description
    //to Maven and GitHub. Also if you are storing a URL then store a URL
    //(or possibly a URI) don't use a string that needs conversion to use
    String mvnAccessURL;
    String mvnDownloadURL;
    String githubAccessURL;
    String githubDownloadURL;


    // why are we saving this as there is no requirement to have a POM
    String POMUrl;
    
    //these worry me as I don't think we should be insisting on maven
    //specific classes inside the OpenMinTeD description
    List<Developer> developers;
    List<Contributor> contributors;
    List<License> licenses;    
    List<MailingList> mailingLists;
    IssueManagement issueManagement;
    CiManagement ciManagement;
    Organization org;
    Scm scm;

    public ComponentMetaData() {
        //empty constructor to allow for creation of empty instances
    }

    public ComponentMetaData(ComponentMetaData existing) {

        if (existing == null) return;

        id = existing.id;
        source = existing.source;
        name = existing.name;
        version = existing.version;
        implementation = existing.implementation;
        description = existing.description;
        framework = existing.framework;
        if (existing.parameters != null) categories = new ArrayList<String>(existing.categories);
        product = existing.product;
        format = existing.format;
        if (existing.parameters != null) parameters = new ArrayList<ParameterMetaData>(existing.parameters);
        if (existing.inputs != null) inputs = new ArrayList<InputOutputMetaData>(inputs);
        if (existing.outputs != null) outputs = new ArrayList<InputOutputMetaData>(outputs);
        documentationUrl = existing.documentationUrl;
        POMUrl = existing.POMUrl;
        if (existing.developers != null) developers = new ArrayList<Developer>(existing.developers);
        if (existing.contributors != null) contributors = new ArrayList<Contributor>(existing.contributors);
        if (existing.licenses != null) licenses = new ArrayList<License>(existing.licenses);    
        if (existing.mailingLists != null) mailingLists = new ArrayList<MailingList>(existing.mailingLists);
        issueManagement = existing.issueManagement;
        ciManagement = existing.ciManagement;
        org = existing.org;
        scm = existing.scm;
        projURL = existing.projURL;
        meta = existing.meta;
        componentType = existing.componentType;
        mvnAccessURL = existing.mvnAccessURL;
        mvnDownloadURL = existing.mvnDownloadURL;
        githubAccessURL = existing.githubAccessURL;
        githubDownloadURL = existing.githubDownloadURL;
    }
}
