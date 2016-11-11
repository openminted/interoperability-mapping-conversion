package eu.openminted.interop.componentoverview.importer;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public interface Importer<T extends Object>
{
    default List<T> process(File aFile, T meta) {
	try {
		return process(aFile.toURI().toURL(),meta);
	}
	catch (MalformedURLException e) {
		//this should be impossible
		throw new RuntimeException("How did we get a malformed URL from this file: "+aFile,e);
	}
    }

    List<T> process(URL aURL,T meta);
}
