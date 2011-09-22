package com.izforge.izpack.compiler.packager.impl;

import static org.junit.Assert.fail;

import java.io.IOException;

import org.dom4j.dom.DOMElement;
import org.junit.Test;
import org.mockito.Mockito;

import com.izforge.izpack.api.adaptator.IXMLElement;
import com.izforge.izpack.api.adaptator.impl.XMLElementImpl;
import com.izforge.izpack.compiler.resource.ResourceFinder;
import com.izforge.izpack.merge.MergeManager;

public class PackagerTest {

	@Test
	public void testWritePacks() throws IOException {
		final ResourceFinder resourceFinder = new ResourceFinder(null, null,
				null, null) {
			@Override
			public IXMLElement getXMLTree() throws IOException {
				final DOMElement rootNode = new DOMElement("installation");
				final DOMElement guiPrefsNode = new DOMElement("guiprefs");
				rootNode.add(guiPrefsNode);
				return new XMLElementImpl(rootNode);
			}
		};
		final MergeManager mockMergeManager = Mockito.mock(MergeManager.class);
		final Packager packager = new Packager(null, null, null, null, null,
				null, null, mockMergeManager, null, null, null, resourceFinder);
		packager.writeManifest();
		fail("Not yet implemented");
	}

}
