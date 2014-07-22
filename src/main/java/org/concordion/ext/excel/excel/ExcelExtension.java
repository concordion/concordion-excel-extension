package org.concordion.ext.excel;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.concordion.api.Source;
import org.concordion.api.SpecificationLocator;
import org.concordion.api.Target;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.ext.excel.conversion.ConversionStrategy;
import org.concordion.ext.excel.conversion.cell.BasicCellConversionStrategy;
import org.concordion.ext.excel.conversion.cell.TableCellConversionStrategy;
import org.concordion.ext.excel.conversion.cellcontent.DefaultCommentConverter;
import org.concordion.ext.excel.conversion.cellcontent.DefaultStyleConverter;
import org.concordion.ext.excel.conversion.cellcontent.MergedCellConversionStrategy;
import org.concordion.ext.excel.conversion.row.BasicTableRowStrategy;
import org.concordion.ext.excel.conversion.row.HeuristicTablesRowStrategy;
import org.concordion.ext.excel.conversion.row.ParagraphRowStrategy;
import org.concordion.ext.excel.conversion.sheet.BasicSheetConversionStrategy;
import org.concordion.ext.excel.conversion.workbook.BasicWorkbookConversionStrategy;
import org.concordion.ext.excel.conversion.workbook.WorkbookConversionStrategy;
import org.concordion.internal.ClassPathSource;
import org.concordion.internal.FileTargetWithSuffix;

/**
 * Provides functionality to concordion to allow test specifications to be written in Excel format.
 * 
 * @author robmoffat
 *
 */
public class ExcelExtension implements ConcordionExtension {
	
	private SpecificationLocator locator = getLocator();
	private Source source = getSource();
    private Target target = getTarget();
    
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(locator).withSource(source).withTarget(target);
    }

	protected Source getSource() {
		return new ExcelClassPathSource(getDecoratedSource(), getWorkbookConversionStrategy());
	}

	protected WorkbookConversionStrategy getWorkbookConversionStrategy() {
		List<ConversionStrategy<Cell>> cellPartConverters = new LinkedList<ConversionStrategy<Cell>>();
		cellPartConverters.add(new MergedCellConversionStrategy());
		cellPartConverters.add(new DefaultStyleConverter());
		cellPartConverters.add(new DefaultCommentConverter());
	
		ConversionStrategy<Cell> cellBodyStrategy = new TableCellConversionStrategy(cellPartConverters, "td");
		ConversionStrategy<Cell> cellHeaderStrategy = new TableCellConversionStrategy(cellPartConverters, "th");
		ConversionStrategy<Cell> paragraphCellStrategy = new BasicCellConversionStrategy(cellPartConverters, "span", false);
		
		ParagraphRowStrategy paragraphStrategy = new ParagraphRowStrategy(paragraphCellStrategy);
		BasicTableRowStrategy tableStrategy = new BasicTableRowStrategy(cellBodyStrategy, cellHeaderStrategy);
		
		return new BasicWorkbookConversionStrategy(
				new BasicSheetConversionStrategy(
				 new HeuristicTablesRowStrategy(paragraphStrategy, tableStrategy)));
		
	}

	protected ClassPathSource getDecoratedSource() {
		return new ClassPathSource();
	}
	
	protected Target getTarget() {
		return new FileTargetWithSuffix("html");
	}

	protected ExcelLocator getLocator() {
		return new ExcelLocator();
	}

}
