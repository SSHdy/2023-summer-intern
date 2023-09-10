package com.ryzw.housemanager.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


//excel工具类
@Slf4j
public class ExcelUtil {

    /**
     * 导出Excel
     *
     * @param sheetName sheet名称
     * @param title     标题
     * @param values    内容
     * @param wb        HSSFWorkbook对象
     * @return
     */
    public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] title, String[][] values, HSSFWorkbook wb) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
        HSSFRow row = sheet.createRow(0);
        // 第四步，创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle style = wb.createCellStyle();
        // 创建一个居中格式
        style.setAlignment(HorizontalAlignment.CENTER);
        //声明列对象
        HSSFCell cell = null;
        //创建标题
        for (int i = 0; i < title.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(title[i]);
            cell.setCellStyle(style);
        }
        //创建内容
        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 1);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象
                row.createCell(j).setCellValue(values[i][j]);
            }
        }
        return wb;
    }


    /**
     * 导出Excel
     *
     * @param sheet
     * @param rowNum
     * @param contentCellList
     */
    public static void reportHSSFWorkbook(HSSFSheet sheet, int rowNum, List contentCellList, HSSFCellStyle style) {

        HSSFRow row;
        HSSFCell hssfCell;
        //创建内容
        row = sheet.createRow(rowNum + 2);
        for (int j = 0; j < contentCellList.size(); j++) {
            //将内容按顺序赋给对应的列对象
            hssfCell = row.createCell(j);
            if (contentCellList.get(j) instanceof Integer || contentCellList.get(j) instanceof Float ||
                    contentCellList.get(j) instanceof BigDecimal) {
                hssfCell.setCellValue(Double.valueOf(String.valueOf(contentCellList.get(j))));
            } else {
                hssfCell.setCellValue(String.valueOf(contentCellList.get(j)));
            }
            hssfCell.setCellStyle(style);
            row.setHeightInPoints(49.25f);
        }
    }

    /**
     * 导出Excel
     *
     * @param sheet
     * @param rowNum
     * @param contentCellList
     */
    public static void reportHSSFWorkbooks(HSSFSheet sheet, int rowNum, List contentCellList, HSSFCellStyle style) {
        HSSFRow row;
        HSSFCell hssfCell;
        //创建内容
        row = sheet.createRow(rowNum + 1);
        for (int j = 0; j < contentCellList.size(); j++) {
            //将内容按顺序赋给对应的列对象
            hssfCell = row.createCell(j);
            if (contentCellList.get(j) instanceof Integer || contentCellList.get(j) instanceof Float ||
                    contentCellList.get(j) instanceof Double || contentCellList.get(j) instanceof BigDecimal) {
                hssfCell.setCellValue(Double.valueOf(String.valueOf(contentCellList.get(j))));
            } else {
                hssfCell.setCellValue(String.valueOf(contentCellList.get(j)));
            }
            hssfCell.setCellStyle(style);
            row.setHeightInPoints(44.25F);
        }

    }


    /**
     * 获取表格样式
     */
    public static HSSFCellStyle getHSSFCellStyle(HSSFWorkbook hssfWorkbook) {
//        HSSFFont fontStyle = hssfWorkbook.createFont(); // 字体样式
//        fontStyle.setFontName("宋体"); // 字体
//        fontStyle.setFontHeightInPoints((short) 12); // 大小

        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        //下边框
        style.setBorderBottom(BorderStyle.THIN);
        //左边框
        style.setBorderLeft(BorderStyle.THIN);
        //上边框
        style.setBorderTop(BorderStyle.THIN);
        //右边框
        style.setBorderRight(BorderStyle.THIN);
//        style.setFont(fontStyle);
        return style;
    }

    @SuppressWarnings("deprecation")
    public static HSSFWorkbook getHBHSSFWorkbook(String sheetName, String[] head0, String[] headnum0, String[] head1, String[] headnum1, String[][] values, Integer number, HSSFWorkbook wb) {
        // 第一步，创建一个HSSFWorkbook，对应一个Excel文件
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        // 创建一个表
        HSSFSheet sheet = wb.createSheet(sheetName);
        // 表头标题样式
        HSSFFont headfont = wb.createFont();
        headfont.setFontName("宋体");
        // 字体大小
        headfont.setFontHeightInPoints((short) 22);
        //创建单元格，并设置值表头 设置表头居中
        HSSFCellStyle headstyle = wb.createCellStyle();
        headstyle.setFont(headfont);
        // 左右居中
        headstyle.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        headstyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headstyle.setLocked(true);
        // 表头时间样式
        HSSFFont datefont = wb.createFont();
        datefont.setFontName("宋体");
        // 字体大小
        datefont.setFontHeightInPoints((short) 12);
        HSSFCellStyle datestyle = wb.createCellStyle();
        datestyle.setFont(datefont);
        // 左右居中
        datestyle.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        datestyle.setVerticalAlignment(VerticalAlignment.CENTER);
        datestyle.setLocked(true);
        // 列名样式
        HSSFFont font = wb.createFont();
        font.setFontName("宋体");
        // 字体大小
        font.setFontHeightInPoints((short) 12);
        HSSFCellStyle style = wb.createCellStyle();
        //下边框
        style.setBorderBottom(BorderStyle.THIN);
        //左边框
        style.setBorderLeft(BorderStyle.THIN);
        //上边框
        style.setBorderTop(BorderStyle.THIN);
        //右边框
        style.setBorderRight(BorderStyle.THIN);
        style.setFont(font);
        // 左右居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setLocked(true);
        // 普通单元格样式（中文）
        HSSFFont font2 = wb.createFont();
        font2.setFontName("宋体");
        font2.setFontHeightInPoints((short) 12);
        HSSFCellStyle style2 = wb.createCellStyle();
        //下边框
        style2.setBorderBottom(BorderStyle.THIN);
        //左边框
        style2.setBorderLeft(BorderStyle.THIN);
        //上边框
        style2.setBorderTop(BorderStyle.THIN);
        //右边框
        style2.setBorderRight(BorderStyle.THIN);
        style2.setFont(font2);
        // 左右居中
        style2.setAlignment(HorizontalAlignment.CENTER);
        // 换行
        style2.setWrapText(true);
        // 上下居中
        style2.setVerticalAlignment(VerticalAlignment.CENTER);
        // 设置列宽  （第几列，宽度）
        sheet.setColumnWidth(0, 1600);
        sheet.setColumnWidth(1, 3600);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 4000);
        sheet.setColumnWidth(6, 4000);
        sheet.setColumnWidth(7, 4000);
        sheet.setColumnWidth(8, 4000);
        sheet.setColumnWidth(9, 4000);
        sheet.setColumnWidth(10, 4000);
        sheet.setColumnWidth(11, 4000);
        sheet.setColumnWidth(12, 4000);
        sheet.setColumnWidth(13, 4000);
        sheet.setColumnWidth(14, 4000);
        sheet.setColumnWidth(15, 4000);
        sheet.setColumnWidth(16, 4000);
        sheet.setColumnWidth(17, 4000);
        sheet.setColumnWidth(18, 4000);
        sheet.setColumnWidth(19, 4000);
        //设置行高
        sheet.setDefaultRowHeight((short) 360);
        // 第一行表头标题
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, head0.length - 1));
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short) 0x200);
        HSSFCell cell = row.createCell(0);
        cell.setCellStyle(datestyle);
        //第一行标题
        cell.setCellValue("");
        // 第二行时间
        /*sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, head0.length-1));
        HSSFRow row1 = sheet.createRow(1);
        row.setHeight((short) 0x349);
        HSSFCell cell1 = row1.createCell(0);
        cell1.setCellStyle(datestyle);
        CellUtil.setCellValue(cell1, date); */
        // 第二行表头列名
        row = sheet.createRow(1);
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(head0[i]);
            cell.setCellStyle(style);
        }
        //动态合并单元格
        for (int i = 0; i < headnum0.length; i++) {
            String[] temp = headnum0[i].split(",");
            Integer startrow = Integer.parseInt(temp[0]);
            Integer overrow = Integer.parseInt(temp[1]);
            Integer startcol = Integer.parseInt(temp[2]);
            Integer overcol = Integer.parseInt(temp[3]);
            sheet.addMergedRegion(new CellRangeAddress(startrow, overrow,
                    startcol, overcol));
        }
        //设置合并单元格的参数并初始化带边框的表头（这样做可以避免因为合并单元格后有的单元格的边框显示不出来）
        //因为下标从0开始，所以这里表示的是excel中的第3行
        row = sheet.createRow(2);
        for (int i = 0; i < head0.length; i++) {
            cell = row.createCell(i);
            //设置excel中第四行的1、2、7、8列的边框
            cell.setCellStyle(style);
            if (i > 1 && i < head0.length) {
                for (int j = 0; j < head1.length; j++) {
                    cell = row.createCell(j + number);
                    //给excel中第四行的3、4、5、6列赋值（"温度℃", "湿度%", "温度℃", "湿度%"）
                    cell.setCellValue(head1[j]);
                    //设置excel中第四行的3、4、5、6列的边框
                    cell.setCellStyle(style);
                }
            }
        }

        for (int i = 0; i < values.length; i++) {
            row = sheet.createRow(i + 3);
            for (int j = 0; j < values[i].length; j++) {
                //将内容按顺序赋给对应的列对象 
                cell = row.createCell(j);
                cell.setCellValue(values[i][j]);
                cell.setCellStyle(style);

            }
        }

        return wb;
    }

    public static String dateFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dt = format.format(date);
        return dt;
    }

    /**
     * 读取Excel文件的内容,以ArrayList的方式返回每一行，具体 每一行的内容也是一个ArrayList
     *
     * @param file Excel文件
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ArrayList readExcelToList(File file) {
        InputStream inputStream = null;
        try {
            // 创建对Excel工作簿文件的引用
            inputStream = new FileInputStream(file);
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);

            // int numOfSheet = workbook.getNumberOfSheets();
            HSSFSheet sheet = workbook.getSheetAt(0);
            int start = sheet.getFirstRowNum();
            int end = sheet.getLastRowNum();
            short startCell = sheet.getRow(0).getFirstCellNum();
            short endCell = sheet.getRow(0).getLastCellNum();
            ArrayList sheetList = new ArrayList();
            for (int i = start; i <= end; i++) {
                HSSFRow row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }

                ArrayList rowList = new ArrayList();
                for (int j = startCell; j < endCell; j++) {
                    HSSFCell cell = row.getCell(j);
                    rowList.add(getCellValue(cell));
                }
                sheetList.add(rowList);
            }
            removeEmptyLine(sheetList);
            return sheetList;
        } catch (Exception ex) {
            log.error("", ex);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                    inputStream = null;
                } catch (Exception ex) {
                    log.error("error:" + ex);
                }
            }
        }
        return null;
    }


    /**
     * 描述：获取单元格中的值 作者： 时间：2016-1-25
     *
     * @param cell EXCEL 单元格
     * @return
     */
    public static String getCellValue(HSSFCell cell) {
        String value = null;
        if (cell != null) {
            CellType cellType = cell.getCellTypeEnum();
            if (CellType.NUMERIC.equals(cellType)) {
                value = new DataFormatter().formatCellValue(cell);
            } else if (CellType.STRING.equals(cellType)) {
                value = cell.getRichStringCellValue().getString();
            }
        }
        return value;
    }


    /**
     * 删除最后的空行数据
     *
     * @param sheetList
     * @return
     */
    public static void removeEmptyLine(ArrayList sheetList) {
        int lastIndOfNotEmpty = sheetList.size();
        for (int i = sheetList.size() - 1; i >= 0; i--) {
            if (!isEmptyLine((ArrayList) sheetList.get(i))) {
                lastIndOfNotEmpty = i;
                break;
            }
        }
        for (int i = sheetList.size() - 1; i > lastIndOfNotEmpty; i--) {
            sheetList.remove(i);
        }
    }


    /**
     * 判断excel的一行数据是否全部为空。
     *
     * @param line
     * @return
     */
    public static boolean isEmptyLine(ArrayList line) {
        if (CollectionUtils.isEmpty(line)) {
            return true;
        }
        for (Object obj : line) {
            if (obj instanceof String) {
                if (StringUtils.isNotEmpty((String) obj)) {
                    return false;
                }
            } else {
                if (obj != null) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * 校验excel里的首行是否是指定的标题
     *
     * @param titles
     * @param excelTitles
     * @return
     */
    public static boolean checkImportExcleFileTitle(String[] titles, ArrayList<String> excelTitles) {
        if (titles == null || titles.length == 0 || CollectionUtils.isEmpty(excelTitles)) {
            return false;
        }
        if (titles.length != excelTitles.size()) {
            return false;
        }
        for (int i = 0; i < excelTitles.size(); i++) {
            if (!StringUtils.equals(titles[i], excelTitles.get(i))) {
                return false;
            }
        }
        return true;
    }


}
