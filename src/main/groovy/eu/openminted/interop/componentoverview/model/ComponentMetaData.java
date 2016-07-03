package eu.openminted.interop.componentoverview.model;

import java.io.File;
import java.util.List;

import org.apache.maven.model.License;

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
    List<License> license;
    String devInfo;
    List<String> mailingLists;
}