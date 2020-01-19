package com.sc.aftds;

import com.sc.aftds.cmd.Command;
import com.sc.aftds.excel.ExcelFileLoader;
import com.sc.aftds.excel.ExcelFileWriter;
import com.sc.aftds.excel.IExcelFileWriter;
import com.sc.aftds.unit.*;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class AftdsApplication {
	private static final Logger logger = LogManager.getLogger(AftdsApplication.class);

	public static void main(String[] args) throws ParseException, IOException {
		var command = new Command(args);
		IUnitEngine<UnitModel> unitEngine = new UnitEngine();
		var fileLoader = new ExcelFileLoader(command, unitEngine);
		fileLoader.loadIntoEngine();
		IUnitService unitService = new UnitService(unitEngine, command);
		unitService.process();
		IExcelFileWriter fileWriter = new ExcelFileWriter(unitEngine.getAllUnitModels());
		fileWriter.write();

		logger.info("Application will close.");
	}
}
