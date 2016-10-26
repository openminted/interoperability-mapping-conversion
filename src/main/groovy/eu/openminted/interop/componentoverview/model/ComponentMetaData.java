package eu.openminted.interop.componentoverview.model;

import java.util.List;

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
    String POMUrl;    
    List<Developer> developers;
    List<Contributor> contributors;
    List<License> licenses;    
    List<MailingList> mailingLists;
    IssueManagement issueManagement;
    CiManagement ciManagement;
    Organization org;
    Scm scm;
    String projURL;
    MetaDataRecord meta;
    String componentType;
    String mvnAccessURL;
    String mvnDownloadURL;
    String githubAccessURL;
    String githubDownloadURL;
}