package eu.openminted.interop.componentoverview.model;

import java.util.List;

public class ComponentMetaData
{
    String name;
    String implementation;
    String description;
    String framework;
    List<String> categories;
    String product;
    String format; // Only relevant for readers and writers
}