package com.itcast.enrol.common.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.pdf.PdfReader;

public class PDFBoxUtil {
	// 统一记录日志类
	private static Logger logger = LoggerFactory.getLogger("enrol");
	/***
	 * PDF转PNG图片，按照页数
	 * 
	 * @param PdfFilePath
	 * @param page
	 * @param dpi
	 * @return
	 */
	public static Map<String, Object> pdf2Image(String PdfFilePath, String dstImgFolder, String page, int dpi) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		File file = new File(PdfFilePath);
		StringBuilder msg = null;
		PDDocument pdDocument;
		String imgCount = new String();
		try {
			int dot = file.getName().lastIndexOf('.');
			String pdfFileName = file.getName().substring(0, dot); // PDF文件名
			pdDocument = PDDocument.load(file);
			PDFRenderer renderer = new PDFRenderer(pdDocument);
			PdfReader reader = new PdfReader(PdfFilePath);
			int pdfPage = reader.getNumberOfPages();
			imgCount = String.valueOf(pdfPage);
			// 设置数字的补位符
			StringBuilder format = new StringBuilder();
			for (int i = 0; i < String.valueOf(pdfPage).length(); i++) {
				format.append("0");
			}
			DecimalFormat df = new DecimalFormat(format.toString());

			/* 0表示第一页，300表示转换dpi，dpi越大转换后越清晰，相对转换速度越慢 */
			if ("".equals(page)) {
				StringBuilder imgFilePath = null;
				for (int i = 0; i < pdfPage; i++) {
					imgFilePath = new StringBuilder();
					imgFilePath.append(dstImgFolder);
					imgFilePath.append(File.separator);
					imgFilePath.append(pdfFileName);
					imgFilePath.append("_");
					imgFilePath.append(df.format(i + 1));
					imgFilePath.append(".png");
					File dstImgFile = new File(imgFilePath.toString());
					BufferedImage image = renderer.renderImageWithDPI(i, dpi);
					ImageIO.write(image, "png", dstImgFile);
				}
				result.put("errCode", "0");
				result.put("msg", "PDF转PNG图片成功！");
				result.put("dstImgFolder", dstImgFolder);
				result.put("imgPage", imgCount);
			} else {
				int count = 0;
				if (page.indexOf("-") != -1) {
					String[] pageStr = page.split("-");
					int startPage = Integer.parseInt(pageStr[0]);
					int endPage = Integer.parseInt(pageStr[1]);
					StringBuilder imgFilePath = null;

					for (int i = startPage - 1; i < endPage; i++) {
						imgFilePath = new StringBuilder();
						imgFilePath.append(dstImgFolder);
						imgFilePath.append(File.separator);
						imgFilePath.append(pdfFileName);
						imgFilePath.append("_");
						imgFilePath.append(df.format(i + 1));
						imgFilePath.append(".png");
						File dstFile = new File(imgFilePath.toString());
						BufferedImage image = renderer.renderImageWithDPI(i, dpi);
						ImageIO.write(image, "png", dstFile);
						count++;
					}
					imgCount = String.valueOf(count);
					result.put("errCode", "0");
					result.put("msg", "PDF转PNG图片成功！");
					result.put("dstImgFolder", dstImgFolder);
					result.put("imgPage", imgCount);
				} else {
					StringBuilder imgFilePath = null;
					int startPage = Integer.parseInt(page);
					if (startPage > pdfPage) {
						msg = new StringBuilder();
						msg.append("失败,指定的页数超出PDF文件总页数：");
						msg.append("指定页数 ");
						msg.append(page);
						msg.append("，PDF总页数 ");
						msg.append(pdfPage);
						result.put("errCode", "1");
						result.put("msg", msg.toString());
					} else {
						imgFilePath = new StringBuilder();
						imgFilePath.append(dstImgFolder);
						imgFilePath.append(File.separator);
						imgFilePath.append(pdfFileName);
						imgFilePath.append("_");
						imgFilePath.append(df.format(Integer.valueOf(page)));
						imgFilePath.append(".png");
						File dstFile = new File(imgFilePath.toString());
						BufferedImage image = renderer.renderImageWithDPI(startPage - 1, dpi);
						ImageIO.write(image, "png", dstFile);
						imgCount = "1";

						result.put("errCode", "0");
						result.put("msg", "PDF转PNG图片成功！");
						result.put("imgFilePath", imgFilePath.toString());
						result.put("imgPage", imgCount);
					}
				}
			}
			pdDocument.close();

		} catch (Exception e) {
			msg = new StringBuilder();
			msg.append("失败，");
			msg.append(" 出现异常：");
			msg.append(e.getMessage());
			result.put("errCode", "1");
			result.put("msg", msg.toString());
			e.printStackTrace();
			logger.error(e.getMessage(), e);
		}
		return result;
	}
	
	public static void main(String[] args){
		for(int i=1;i<=5;i++){
			Map<String,Object> pdfImage = pdf2Image("D:/项目文档/培训协议/class_detail_base.pdf", "D:/项目文档/培训协议", String.valueOf(i), 300);
			System.out.println("第"+i+"页："+pdfImage.get("imgFilePath"));
		}
	}
}
