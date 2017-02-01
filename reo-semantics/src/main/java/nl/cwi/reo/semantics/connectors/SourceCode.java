package nl.cwi.reo.semantics.connectors;

import java.io.File;

public final class SourceCode {

	private final File file;
	
	private final String call;
	
	public SourceCode() {
		this.file = null;
		this.call = "";
	}
	
	public SourceCode(String file) {
		this.file = new File(file);
		this.call = "";
	}

	public File getFile() {
		return file;
	}

	public String getCall() {
		return call;
	}

	@Override
	public String toString() {
		return call + (file == null ? "" : " at " + file.getPath());
	}
}
