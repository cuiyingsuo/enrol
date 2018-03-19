package com.itcast.enrol.student.service.plugins;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

public class Test {
	public static void main(String[] args) throws IOException, DocumentException {
		PDFParams pdfParams = new PDFParams();
		pdfParams.setPdfSerial("ITCAST-2017-1111-0001");
		pdfParams.setOrgName("江苏传智播客教育科技股份有限公司北京分公司");
		pdfParams.setStudentName("崔英锁");
		pdfParams.setCardNo("371481198611216013");
		pdfParams.setStudentTel("15210173019");
		pdfParams.setNowAddress("北京市昌平区沙河地铁站于辛庄村村委会附近111号");
		
		pdfParams.setGoodsName("测试商品 名字要长一些");
		pdfParams.setStartDate("2018/01/02");
		pdfParams.setEndDate("2018/01/31");
		createContract(pdfParams,"D:/项目文档/电子合同/培训协议（就业班）.pdf","D:/项目文档/电子合同/培训协议（就业班）_new.pdf");
	}

	public static void createContract(PDFParams pdfParams, String modelUrl,
			String contractUrl) throws IOException, DocumentException {
		Map<String, String> paraMap = new HashMap<String, String>();

		paraMap.put("FILL_1", pdfParams.getPdfSerial());
		paraMap.put("FILL_2", pdfParams.getOrgName());
		paraMap.put("FILL_3", pdfParams.getStudentName());
		paraMap.put("FILL_4", pdfParams.getCardNo());
		paraMap.put("FILL_5", pdfParams.getStudentTel());
		paraMap.put("FILL_6", pdfParams.getNowAddress());
		
		paraMap.put("FILL_19", pdfParams.getOrgName());
		paraMap.put("FILL_7", pdfParams.getGoodsName());

		// 读取模板
		PdfReader reader = new PdfReader(modelUrl);
		// 合同输出
		PdfStamper ps = new PdfStamper(reader,
				new FileOutputStream(contractUrl));

		//获取pdf表单
		AcroFields s = ps.getAcroFields();

		//给表单添加中文字体 这里采用系统字体。不设置的话，中文可能无法显示
		BaseFont bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
		s.addSubstitutionFont(bf);

		//遍历pdf表单表格，同时给表格赋值
		Map<String, Item> fieldMap = s.getFields();

		Set<Entry<String, Item>> set = fieldMap.entrySet();

		Iterator<Entry<String, Item>> iterator = set.iterator();

		while (iterator.hasNext()) {

			Entry<String, Item> entry = iterator.next();

			String key = (String) entry.getKey();
			System.out.println(key.toUpperCase() + ","
					+ paraMap.get(key.toUpperCase()));
			if (paraMap.get(key.toUpperCase()) != null) {

				s.setField(key, "" + paraMap.get(key.toUpperCase()));

			}

		}

		ps.setFormFlattening(true);

		ps.close();

		reader.close();
	}
}
