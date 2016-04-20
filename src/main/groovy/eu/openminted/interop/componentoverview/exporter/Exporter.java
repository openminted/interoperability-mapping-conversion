package eu.openminted.interop.componentoverview.exporter;

import eu.openminted.interop.componentoverview.model.ComponentMetaData;

public interface Exporter<T extends Object>
{
    T process(ComponentMetaData aMetaData);
}
