package cn.mopon.cec.site.actions.stat;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.joda.time.DateTime;

import cn.mopon.cec.core.model.StatSearchModel;
import coo.base.util.DateUtils;
import coo.core.jxls.Excel;

/**
 * 报表基类。
 */
public class BaseStatAction {

	/**
	 * 日期为空时，默认为当月的日期段。
	 * 
	 * @param model
	 *            查询对象
	 */
	protected void setCurrentMonthIfNull(StatSearchModel model) {
		DateTime date = DateTime.now();
		if (model.getStartDate() == null && model.getEndDate() == null) {
			model.setStartDate(date.plusDays(-(date.getDayOfMonth() - 1))
					.toLocalDate().toDate());
			model.setEndDate(DateUtils.getToday());
		}
	}

	/**
	 * 日期为空时，默认为上一月的时间段。
	 * 
	 * @param model
	 *            查询对象
	 */
	protected void setBeforeMonthIfNull(StatSearchModel model) {
		DateTime date = DateTime.now();
		if (model.getStartDate() == null && model.getEndDate() == null) {
			model.setStartDate(date.plusDays(-(date.getDayOfMonth() - 1))
					.plusMonths(-1).toLocalDate().toDate());
			model.setEndDate(date.plusDays(-(date.getDayOfMonth() - 1))
					.plusDays(-1).toLocalDate().toDate());
		}
	}

	/**
	 * 获取报表查询时间段的日期。
	 * 
	 * @param date
	 *            日期对象
	 * @return yyyy-MM-dd 格式字符串。
	 */
	protected String getDateStr(Date date) {
		return DateUtils.format(date, DateUtils.DAY);
	}

	/**
	 * 合并 影院、渠道 统计报表的表头单元格。
	 * 
	 * @param excel
	 *            excel 对象
	 */
	protected void mergeCinemaAndChannelHeader(Excel excel) {
		for (int i = 0; i < excel.getWorkbook().getNumberOfSheets(); i++) {
			Sheet sheet = excel.getWorkbook().getSheetAt(i);
			for (int j = 0; j <= 7; j++) {
				if (i == 0) {
					sheet.addMergedRegion(new CellRangeAddress(2, 3, j, j));
				} else {
					sheet.addMergedRegion(new CellRangeAddress(3, 4, j, j));
				}
			}
		}
	}

	/**
	 * 过滤导出报表的excel sheet 关键字。
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @return 过滤关键字之后的名称。
	 */
	protected String filterExcelKeyWords(String sheetName) {
		sheetName = sheetName.replace(":", "");
		sheetName = sheetName.replace("\\\\", "");
		sheetName = sheetName.replace("/", "");
		sheetName = sheetName.replace("?", "");
		sheetName = sheetName.replace("*", "");
		sheetName = sheetName.replace("[", "");
		sheetName = sheetName.replace("]", "");
		return sheetName;
	}

	/**
	 * 合并影院结算报表的表头单元格。
	 * 
	 * @param excel
	 *            excel 对象
	 */
	protected void mergeCinemaSettHeader(Excel excel) {
		Sheet sheet = excel.getWorkbook().getSheetAt(0);
		for (int j = 0; j <= 3; j++) {
			sheet.addMergedRegion(new CellRangeAddress(2, 3, j, j));
		}
	}

	/**
	 * 设置响应头。
	 * 
	 * @param response
	 *            response对象
	 * @param fileName
	 *            文件名
	 */
	protected void setResponseHeader(HttpServletResponse response,
			String fileName) {
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="
				+ fileName);
	}

	/**
	 * 获取当前日期的yyyy-MM-dd字符串格式。
	 * 
	 * @return 当前日期的yyyy-MM-dd字符串格式。
	 */
	protected String getCurrentDate() {
		return DateUtils.format(DateUtils.getToday(), DateUtils.DAY);
	}

}
