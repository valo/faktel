package com.faktel;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.faktel.config.ConfigParser;
import com.faktel.features.DummyView;
import com.faktel.filters.FilterArgs;
import com.faktel.filters.FilterInfo;
import com.faktel.filters.RowFilter;
import com.faktel.io.InputReader;
import com.faktel.io.OutputWriter;
import com.faktel.io.invoice.InvoiceRowReader;
import com.faktel.io.invoice.InvoiceRowWriter;
import com.faktel.mvc.Grid;
import com.faktel.mvc.Model;
import com.faktel.mvc.View;
import com.faktel.mvc.commands.Append;
import com.faktel.mvc.commands.Clear;

public class FakEngine {
	private static final Logger logger = Logger.getLogger(FakEngine.class
			.getName());
	private InvoiceRowReader m_reader;
	private Collection<FilterInfo> m_filters;
	@Deprecated
	private Collection<InvoiceRowWriter> m_writers = new LinkedList<InvoiceRowWriter>();

	public FakEngine(InputReader reader, List<FilterInfo> filters,
			Collection<OutputWriter> writers) {
		m_reader = new InvoiceRowReader(reader);
		m_filters = filters;
		for (OutputWriter wr : writers)
			m_writers.add(new InvoiceRowWriter(wr));
	}

	public FakEngine(ConfigParser config) {
		this(config.getInputReader(), config.getRowFilters(), config
				.getOutputWriters());
	}

	private void process() {
		InvoiceRows rowData = null;
		try {
			rowData = m_reader.readAllRowData();
		} catch (IOException e) {
			logger.warning("IOException have been occured while parsing file "
					+ m_reader.getStorageSource() + ". Exception: " + e);
		}

		if (rowData == null) {
			logger.warning("Did not find any rows in "
					+ m_reader.getStorageSource());
			rowData = new InvoiceRows();
		}
		//dumb ass programming ;)
		String resultsFile = "";

		
		
		

		Model model = new Model(rowData);
		View view = new DummyView(model);
		// The working dir for this applicatio run.
		File runDir = Utils.createFolderForCurrentRun();

		// filter given data - iterate through all filters
		int filterNo = 0;
		for (FilterInfo fi : m_filters) {
			filterNo++;

			RowFilter filter = null;
			try {
				filter = Utils.createFilter(fi);
			} catch (Exception e) {
				logger.warning("Could not create filter " + fi
						+ ". \r\n This filter will not be executed.");
				logger.throwing(this.getClass().getCanonicalName(),
						"process(Method, thrown)", e);
			}

			File filterDir = Utils.createFolderForFilter(filter.getClass()
					.getName(), runDir);
			FilterArgs args = fi.getArguments();
			try { // start processing
				logger.info("Preparing filter");
				if (filter.prepare(args)) {
					logger.info("Executing filter");
					model.gridClear();
					Grid resultData = filter.execute(model, filterDir, runDir);
					view.update(model, new Clear());
					view.update(model, new Append(resultData));

					// output transformed data:
					// TODO - refactor - Should be done by the filters
					// themselves.
					String outputDir = runDir.getAbsolutePath()
							+ File.separator;

					// String outputName = outputDir + filterNo + "-"
					// + filter.getClass().getName();
					// Utils.outputFilterResultToCsv(resultData, outputName+
					// ".csv");
					String sheetName = "Filter" + filterNo;
					resultsFile = outputDir + "results.xls";
					Utils.outputFilterResultToXls(resultData, resultsFile, sheetName);
				}
				logger.info("Cleanup after filter");
				filter.cleanup(model, filterDir, runDir);

			} catch (Throwable t) {
				t.printStackTrace();
			}
		}//for filter
		
		Utils.shellExecute("explorer " + resultsFile);
		

	}

	public static void main(String[] args) throws Exception {
		boolean debug = true;
		if (debug && args.length == 0) {
			args = new String[] { "config/settings.xml" };
		}

		if (args.length == 0) {
			System.err.println("Config file is not specified. Exitting..");
			System.exit(1);
		}
		File configFile = new File(args[0]);
		if (!configFile.isFile() || !configFile.exists()) {
			System.err.println("Config file '" + configFile
					+ "' does not exists");
			System.exit(2);
		}

		ConfigParser parser = new ConfigParser(configFile);
		FakEngine engine = new FakEngine(parser);
		engine.process();
		System.out.println("Bye!");
		// Fix dummy bug
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6476706
		System.exit(0);

	}
}
