package org.concordion.ext.excel;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.concordion.api.Resource;
import org.concordion.api.Target;
import org.concordion.internal.FileTarget;

public class ExcelSuffixRenamingTarget implements Target {

	private static final String PROPERTY_OUTPUT_DIR = "concordion.output.dir";

	private FileTarget target = new FileTarget(getBaseOutputDir());

	private File getBaseOutputDir() {
		String outputPath = System.getProperty(PROPERTY_OUTPUT_DIR);
		if (outputPath == null) {
			return new File(System.getProperty("java.io.tmpdir"), "concordion");
		}
		return new File(outputPath);
	}

	@Override
	public void write(org.concordion.api.Resource resource, String s) throws java.io.IOException {
		target.write(rename(resource), s);
	}

	@Override
	public void copyTo(org.concordion.api.Resource resource, java.io.InputStream inputStream) throws java.io.IOException {
		target.copyTo(resource, inputStream);
	}

	@Override
	public void delete(Resource resource) throws IOException {
		target.delete(resource);
	}

	@Override
	public boolean exists(Resource resource) {
		return target.exists(resource);
	}

	@Override
	public OutputStream getOutputStream(Resource resource) throws IOException {
		return target.getOutputStream(resource);
	}

	@Override
	public String resolvedPathFor(Resource resource) {
		return target.resolvedPathFor(rename(resource));
	}

	private Resource rename(Resource resource) {
		return new Resource(resource.getPath().replaceFirst("\\." + ExcelExtension.EXCEL_FILE_EXTENSION + "$", "\\.html"));
	}

}
