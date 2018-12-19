package edu.scripps.yates.mzidentmlparser;

import java.io.File;
import java.io.IOException;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class MzIdentMLPaserTest {
	@Test
	public void test1() {
		try {
			final File mzidFile = new ClassPathResource("DTASelect-filter.mzid").getFile();
			final MzIdentMLIdentificationsParser parser = new MzIdentMLIdentificationsParser(mzidFile);
			System.out.println(parser.getProteinMap().size() + " proteins");
			System.out.println(parser.getPSMsByPSMID().size() + " PSMs");

		} catch (final IOException e) {
			e.printStackTrace();
		}
	}
}
