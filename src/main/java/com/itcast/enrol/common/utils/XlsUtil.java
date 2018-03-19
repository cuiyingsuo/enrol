package com.itcast.enrol.common.utils;

import com.itcast.enrol.common.entity.Bill;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuzhongshuai on 2017/11/1.
 */
public class XlsUtil {

    /**
     * 根据查询结果生成xls文件
     *
     * @param mapList 结果
     * @param heads   表头
     */
    public static XSSFWorkbook buildExcelDocument(List<Map<String, Object>> mapList, String... heads) {

        if (null == mapList) {
            throw new NullPointerException("导出的数据为null！");
        }

        if (mapList.get(0).size() != (heads.length)) {
            throw new RuntimeException("表头与数据列数不匹配!");
        }
        //创建一个xls文件
        XSSFWorkbook xls = new XSSFWorkbook();
        //创建sheet
        XSSFSheet hssfSheet = xls.createSheet("第一页");
        //创建样式
        XSSFCellStyle style = xls.createCellStyle();
        //居中格式
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //创建表头行
        XSSFRow row = hssfSheet.createRow(0);

        //创建序号列
        XSSFCell cell = row.createCell(0);
        cell.setCellValue("序号");
        cell.setCellStyle(style);

        //遍历表头
        for (int i = 1; i <= heads.length; i++) {
            // 设置表头
            XSSFCell cell1 = row.createCell(i);
            cell1.setCellValue(heads[i - 1]);
            cell1.setCellStyle(style);
        }
        //遍历查询结果
        for (int i = 1; i <= mapList.size(); i++) {
            XSSFRow currentRow = hssfSheet.createRow(i);
            Map<String, Object> childMap = mapList.get((i - 1));
            Object[] valueObject = childMap.values().toArray();

            XSSFCell currentCell = currentRow.createCell(0);
            currentCell.setCellValue(i);
            currentCell.setCellStyle(style);

            for (int ci = 1; ci <= heads.length; ci++) {
                // 设置cell 值
                hssfSheet.setColumnWidth(ci, 8000);
                currentCell = currentRow.createCell(ci);
                currentCell.setCellValue(String.valueOf(valueObject[ci - 1]));
                currentCell.setCellStyle(style);
            }
        }
        return xls;
    }

    /**
     * 解析 xls
     *
     * @param filePath
     * @param isContainTitle
     * @return
     */
    public static List<Bill> xlsResolver(String filePath, Boolean isContainTitle) {
        if (StringUtils.isEmpty(filePath)) {
            throw new RuntimeException("找不到文件！");
        }
        XSSFWorkbook wb = null;
        try (InputStream is = new FileInputStream(filePath)) {
            wb = new XSSFWorkbook(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取文件异常");
        }
        //默认只读取第一个 sheet 页
        XSSFSheet sheet = wb.getSheetAt(0);
        List<Bill> billList = new ArrayList<>();
        int rowNum = sheet.getLastRowNum();
        for (int i = 1; i <= rowNum; i++) {
            XSSFRow row = sheet.getRow(i);
            if (null == row) {
                break;
            }
            row.getCell(0).setCellType(Cell.CELL_TYPE_STRING);
            if (StringUtils.isEmpty(row.getCell(0).getStringCellValue())) {
                break;
            }
            row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(10).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(11).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(12).setCellType(Cell.CELL_TYPE_STRING);
            row.getCell(13).setCellType(Cell.CELL_TYPE_STRING);

            String subOrder = row.getCell(4).getStringCellValue();
            String payFlowNo = row.getCell(6).getStringCellValue();
            BigDecimal orderAmount = new BigDecimal(row.getCell(7).getStringCellValue());
            BigDecimal fee = new BigDecimal(row.getCell(8).getStringCellValue());
            String payTime = row.getCell(9).getStringCellValue();
            String payMethodCode = row.getCell(10).getStringCellValue();
            String payMethodName = row.getCell(11).getStringCellValue();
            String payTerminal = row.getCell(12).getStringCellValue();
            String channelName = row.getCell(13).getStringCellValue();
            String postNO = row.getCell(15).getStringCellValue();
            String postPayMethod = row.getCell(16).getStringCellValue();

            Bill bill = new Bill();
            bill.setSubOrderNo(subOrder);
            bill.setPayFlowNo(payFlowNo);
            bill.setPayAmount(orderAmount);
            bill.setFee(fee);
            bill.setPayTime(payTime);
            bill.setPayMethodNo(payMethodCode);
            bill.setPayMethodName(payMethodName);
            bill.setPayTerminal(payTerminal);
            bill.setPayChnnel(channelName);
            bill.setPosNo(postNO);
            bill.setPosPayMethod(postPayMethod);
            bill.setCheckStatus(true);

            billList.add(bill);
        }
        return billList;
    }

}
