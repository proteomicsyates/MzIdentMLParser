package edu.scripps.yates.mzidentmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.proteored.miapeapi.exceptions.WrongXMLFormatException;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.proteomicsmodel.MiapeMSIIdentificationsParser;
import org.proteored.miapeapi.xml.mzidentml.MiapeMzIdentMLFile;

import edu.scripps.yates.utilities.remote.RemoteSSHFileReference;

public class MzIdentMLIdentificationsParser extends MiapeMSIIdentificationsParser {
	private final static Logger log = Logger.getLogger(MzIdentMLIdentificationsParser.class);

	public MzIdentMLIdentificationsParser(URL u) throws IOException {
		super(u);
	}

	public MzIdentMLIdentificationsParser(String runid, RemoteSSHFileReference s) throws FileNotFoundException {
		super(runid, s);
	}

	public MzIdentMLIdentificationsParser(Map<String, RemoteSSHFileReference> s) throws FileNotFoundException {
		super(s);
	}

	public MzIdentMLIdentificationsParser(Collection<File> s) throws FileNotFoundException {
		super(s);
	}

	public MzIdentMLIdentificationsParser(File file) throws FileNotFoundException {
		super(file);
	}

	public MzIdentMLIdentificationsParser(String runId, InputStream f) {
		super(runId, f);

	}

	@Override
	public MiapeMSIDocument parseMiapeMSIFromInputStream(InputStream mzIdentMLInputStream) {
		MiapeMSIDocument msiDocument = null;
		try {
			final byte[] bytes = IOUtils.toByteArray(mzIdentMLInputStream);

			log.info("create Dataset MSI from mzIdentML");

			final String projectName = "project";
			try {

				final org.proteored.miapeapi.xml.mzidentml_1_1.MiapeMzIdentMLFile xmlFile = new org.proteored.miapeapi.xml.mzidentml_1_1.MiapeMzIdentMLFile(
						bytes);
				msiDocument = org.proteored.miapeapi.xml.mzidentml_1_1.MSIMiapeFactory.getFactory().toDocument(xmlFile,
						null, getControlVocabularyManager(), null, null, projectName, isProcessInParallel());

			} catch (final WrongXMLFormatException ex) {
				log.info("Error trying to read as mzIdentML 1.2 or 1.1. Trying now as mzIdentML 1.0...");

				final MiapeMzIdentMLFile xmlFile = new MiapeMzIdentMLFile(bytes);
				msiDocument = org.proteored.miapeapi.xml.mzidentml.MSIMiapeFactory.getFactory().toDocument(xmlFile,
						null, getControlVocabularyManager(), null, null, projectName, isProcessInParallel());

			}

			log.info("Dataset created");
			log.info("Storing document MSI with " + msiDocument.getIdentifiedPeptides().size() + " PSMs");

			return msiDocument;

		} catch (final Exception e) {
			log.error(e);
			e.printStackTrace();
		}
		return null;
	}

}
