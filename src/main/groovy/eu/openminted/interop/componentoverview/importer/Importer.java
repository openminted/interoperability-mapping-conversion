package eu.openminted.interop.componentoverview.importer;

import java.io.File;
import java.util.List;

public interface Importer<T extends Object>
{
    List<T> process(File aFile);
}
