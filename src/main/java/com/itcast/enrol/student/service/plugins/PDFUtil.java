package com.itcast.enrol.student.service.plugins;

import java.io.File;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HeaderFooter;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

//http://blog.csdn.net/justinytsoft/article/details/53320225
//http://www.cnblogs.com/h--d/p/6150320.html
//http://www.cnblogs.com/shuilangyizu/p/5760928.html

class TextInfo {
	private String text = "";
	private int disp = 0; // 0:正常显示；1:下划线；

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getDisp() {
		return disp;
	}

	public void setDisp(int disp) {
		this.disp = disp;
	}
}

public class PDFUtil {

	// 设置纸张；
	private Rectangle rect = null;

	// 创建文档实例；
	private Document doc = null;

	// 添加中文支持；
	private BaseFont bfChinese = null;

	// 生成多少个空格字符；
	private String CreateSP(int spaceCount) {
		String retStr = "";
		for (int i = 0; i < spaceCount; i++)
			retStr += " ";
		return retStr;
	}

	// 生成多少个回车换行符；
	private String CreateBR(int spaceCount) {
		String retStr = "";
		for (int i = 0; i < spaceCount; i++)
			retStr += "\r\n";
		return retStr;
	}

	// 设置PDF文件基本信息；
	private void InitPDFVar() {
		try {
			rect = new Rectangle(PageSize.A4);
			doc = new Document(rect);
			bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
					BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 设置PDF顶部文件内容；
	// String pdfSerial: pdf文档序列号；
	// String subSchool: 分校名称；
	// String studentName: 学员名称；
	// String nextContent: 后续内容；
	private void SetPDFTop(String pdfSerial, String subOrg,
			String studentName, String nextContent1, String nextContent2,
			String cardNo, String tel, String address) throws Exception {

		if (true) {
			Font font = new Font(bfChinese, 14, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			Phrase phrase = new Phrase();
			Chunk chunk1 = new Chunk("协议编号：", font);
			Chunk chunk2 = new Chunk(pdfSerial, font);
			phrase.add(chunk1);
			phrase.add(chunk2);
			paragraph.add(phrase);
			doc.add(paragraph);
		}

		if (true) {
			Font font = new Font(bfChinese, 16, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_CENTER);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateBR(2) + "黑马程序员培训协议" + CreateBR(2),
					font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}

		if (true) {
			Font font = new Font(bfChinese, 12, Font.BOLD);
			Font font2 = new Font(bfChinese, 12, Font.UNDERLINE);
			Font font3 = new Font(bfChinese, 12);

			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(CreateBR(2) + CreateSP(16) + "甲方：",
						font);
				phrase.add(chunk1);
				Chunk chunk2 = new Chunk("江苏传智播客教育科技股份有限公司", font2);
				phrase.add(chunk2);
				paragraph.add(phrase);
			}
			doc.add(paragraph);

			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(CreateBR(1) + CreateSP(16) + "", font);
				phrase.add(chunk1);
				Chunk chunk3 = new Chunk(subOrg, font2);
				phrase.add(chunk3);
				paragraph.add(phrase);
			}
			doc.add(paragraph);

			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(
						CreateBR(1) + CreateSP(16) + "乙方(学员)：", font);
				phrase.add(chunk1);
				Chunk chunk2 = new Chunk(studentName, font2);
				phrase.add(chunk2);
				paragraph.add(phrase);
			}
			doc.add(paragraph);

			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(CreateBR(1) + CreateSP(16) + "身份证号码：",
						font3);
				phrase.add(chunk1);
				Chunk chunk2 = new Chunk(cardNo, font2);
				phrase.add(chunk2);
				paragraph.add(phrase);
			}
			doc.add(paragraph);

			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(CreateBR(1) + CreateSP(16) + "电话：",
						font3);
				phrase.add(chunk1);
				Chunk chunk2 = new Chunk(tel, font2);
				phrase.add(chunk2);
				paragraph.add(phrase);
			}
			doc.add(paragraph);

			paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			if (true) {
				Phrase phrase = new Phrase();
				Chunk chunk1 = new Chunk(CreateBR(1) + CreateSP(16) + "通讯地址：",
						font3);
				phrase.add(chunk1);
				Chunk chunk2 = new Chunk(address, font2);
				phrase.add(chunk2);
				paragraph.add(phrase);
			}
			doc.add(paragraph);
		}

		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Font font2 = new Font(bfChinese, 12, Font.UNDERLINE);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk1 = new Chunk(CreateBR(1) + CreateSP(16) + nextContent1,
					font);
			phrase.add(chunk1);
			Chunk chunk2 = new Chunk(subOrg, font2);
			phrase.add(chunk2);
			Chunk chunk3 = new Chunk(nextContent2, font);
			phrase.add(chunk3);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
	}

	// 设置PDF段落内容；
	// String ParagraphTitle: 段落标题；
	// String ParagraphContent: 段落内容；
	private void SetPDFParagraph(String ParagraphTitle, String ParagraphContent)
			throws Exception {

		if (true) {
			Font font = new Font(bfChinese, 12, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(ParagraphTitle, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}

		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(ParagraphContent, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
	}

	// 设置PDF段落内容；
	// String ParagraphTitle: 段落标题；
	// List<TextInfo> listTextInfo: 段落内容；
	private void SetPDFParagraph(String ParagraphTitle,
			List<TextInfo> listTextInfo) throws Exception {

		if (true) {
			Font font = new Font(bfChinese, 12, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(ParagraphTitle, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}

		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Font font2 = new Font(bfChinese, 12, Font.UNDERLINE);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			for (int i = 0; i < listTextInfo.size(); ++i) {
				if (listTextInfo.get(i).getDisp() == 0) {
					Chunk chunk = new Chunk(listTextInfo.get(i).getText(), font);
					phrase.add(chunk);
				} else {
					Chunk chunk = new Chunk(listTextInfo.get(i).getText(),
							font2);
					phrase.add(chunk);
				}
			}
			paragraph.add(phrase);
			doc.add(paragraph);
		}
	}

	// 设置PDF段落内容；
	// String signLocation: 合同签订地点;
	// String studentName: 乙方（签字）;
	// String studentTel:电 话;
	// String studentIdentify: 身份证号码;
	// String studentAddress: 家庭地址;
	// String nowAddress:现居住地址;
	private void SetPDFBottom(String signLocation, String studentName,
			String studentTel, String studentIdentify, String studentAddress,
			String nowAddress, String signDate) throws Exception {

		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateBR(2) + CreateSP(4) + "本合同签订地为： "
					+ signLocation + CreateBR(2), font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "附件：《传智播客.黑马程序员入学协议》" + CreateBR(2), font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "甲方（盖章）：" + CreateBR(9), font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "乙方（签字）：" + studentName + CreateBR(2), font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "电  话：  " + studentTel, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "身份证号码：" + studentIdentify,
					font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		/*if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "家庭地址：" + studentAddress,
					font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}*/
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "现居住地址：" + nowAddress, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_RIGHT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "签署日期：" + signDate, font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
	}

	/**
	 * 创建就业班PDF文档
	 * 
	 * @return
	 * @throws Exception
	 * @throws docException
	 */
	public void createPDFEmployment(String pdfFileName,PDFParams pdfParams) throws Exception {
		
		String pdfSerial = pdfParams.getPdfSerial();
		String subOrg = pdfParams.getOrgName();
		String studentName = pdfParams.getStudentName();
		String cardNo = pdfParams.getCardNo();
		String tel = pdfParams.getTel();
		String address = pdfParams.getAddress();
		String goodsName = pdfParams.getGoodsName();
		String startDate = pdfParams.getStartDate();
		String endDate = pdfParams.getEndDate();
		String price = pdfParams.getPrice();
		String priceCN = pdfParams.getPriceCN();
		String orderPrice = pdfParams.getOrderPrice();
		String orderPriceCN = pdfParams.getOrderPriceCN();
		String studyAddr = pdfParams.getStudyAddress();
		String signLocation = pdfParams.getSignLocation();
		String studentTel = pdfParams.getStudentTel();
		String studentIdentify = pdfParams.getStudentIdentify();
		String studentAddress = pdfParams.getStudentAddress();
		String nowAddress = pdfParams.getNowAddress();
		String signDate = pdfParams.getSignDate();

		PdfWriter.getInstance(doc, new FileOutputStream(new File(pdfFileName)));
		// 添加页眉
		Font font = new Font(bfChinese, 10, Font.NORMAL);
		Phrase phrase = new Phrase();
		Chunk chunk = new Chunk("重要文件，请认真阅读", font);
		phrase.add(chunk);
		/*HeaderFooter header = new HeaderFooter(phrase, false);
		header.setAlignment(Rectangle.ALIGN_RIGHT);
		header.setBorder(Rectangle.NO_BORDER);
		header.setBorder(Rectangle.NO_BORDER);

		doc.setHeader(header);*/

		doc.open();
		doc.newPage();

		if (true) {
			
			String nextContent1 = "乙方自愿报名参加江苏传智播客教育科技股份有限公司（以下简称“传智播客公司”）运营、";
			String nextContent2 = "（以下简称“传智播客分（子）公司”）辅助教学的传智播客.黑马程序员培训班，为明确双方的权利与义务，经双方友好协商，就培训相关事宜达成如下协议：";
			SetPDFTop(pdfSerial, subOrg, studentName, nextContent1,
					nextContent2, cardNo, tel, address);
		}

		if (true) {
			if (true) {
				
				String ParagraphTitle = CreateBR(2) + "第一条" + CreateSP(4)
						+ "培训条款";
				/*
				 * String ParagraphContent = ""; ParagraphContent += CreateSP(4)
				 * + "1.1" + CreateSP(4) +
				 * "乙方参加学习"+goodsName+"课程 ，培训时间为："+startDate
				 * +"至 "+endDate+"（具体培训时间以实际上课时间为准） 。"; ParagraphContent +=
				 * CreateBR(1); ParagraphContent += CreateSP(4) + "1.2" +
				 * CreateSP(4) + "本培训课程标准费用为 "+price+"元整(￥"+priceCN+
				 * ")，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为"
				 * +orderPrice+"元整(￥"+orderPriceCN+
				 * ")。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客分（子）公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客分（子）公司内部协商代退费事宜，与乙方无关。"
				 * ; ParagraphContent += CreateBR(1); ParagraphContent +=
				 * CreateSP(4) + "1.3" + CreateSP(4) +
				 * "参加培训期间，食宿均由乙方自理，甲方不予负责。"; ParagraphContent += CreateBR(1);
				 * ParagraphContent += CreateSP(4) + "1.4" + CreateSP(4) +
				 * " 培训地点："+campusAddr; ParagraphContent += CreateBR(1);
				 * SetPDFParagraph(ParagraphTitle,ParagraphContent);
				 */

				String TextContent = "";
				List<TextInfo> listTextInfo = new LinkedList<TextInfo>();
				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(1) + CreateSP(4) + "1.1"
								+ CreateSP(4) + "乙方参加学习";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = goodsName;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "课程 ，培训时间为：";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = startDate;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "至 ";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = endDate;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "（具体培训时间以实际上课时间为准） 。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.2"
								+ CreateSP(4) + "本培训课程标准费用为 ";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = price;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "元整(￥";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = priceCN;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = ")，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = orderPrice;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "元整(￥";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = orderPriceCN;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = ")。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客分（子）公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客分（子）公司内部协商代退费事宜，与乙方无关。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.3"
								+ CreateSP(4) + "参加培训期间，食宿均由乙方自理，甲方不予负责。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.4"
								+ CreateSP(4) + " 培训地点：";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = price;
						ti.setDisp(1);
						ti.setText(studyAddr);
						listTextInfo.add(ti);
					}
				}
				SetPDFParagraph(ParagraphTitle, listTextInfo);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第二条" + CreateSP(4)
						+ "培训费支付条款";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "2.1" + CreateSP(4)
						+ "乙方必须在开课前向甲方一次性交清培训费。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "2.2"
						+ CreateSP(4)
						+ "培训费支付方式：现金、刷卡、转账。上述培训费用必须支付到甲方公司账户，如乙方将上述费用支付给个人（包括甲方员工）的，视为乙方未缴费。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "2.3" + CreateSP(4)
						+ "培训课程结束后对乙方已经支付的学费不予退还。";
				ParagraphContent += CreateBR(1);
				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第三条" + CreateSP(4)
						+ "培训费逾期规定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "3.1" + CreateSP(4)
						+ "对于开课前未交清培训费的学员，甲方有权禁止其参加培训课程。";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第四条" + CreateSP(4)
						+ "就业";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "4.1" + CreateSP(4)
						+ "乙方自培训结束之日起，须全力完成就业工作，并积极配合甲方的指导，不得怠于就业。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "4.2" + CreateSP(4)
						+ "甲方负责对乙方进行就业方面的指导，但不为乙方提供就业保障。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.3"
						+ CreateSP(4)
						+ "乙方自培训结束的连续88个工作日（4个月）为就业期，在就业期内，乙方在未就业的情况下，须按时到甲方报到，并接受甲方的就业指导与安排，以协助乙方早日就业。报到周期为每星期1次，不得选择节假日、甲方非工作时间报到。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "4.4" + CreateSP(4)
						+ "乙方就业后，必须在三个工作日内向甲方提供所就业的时间、单位名称、薪资福利待遇及地址等信息。";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第五条" + CreateSP(4)
						+ "其他";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ "5.1"
						+ CreateSP(4)
						+ "乙方须如实提供相应的照片、身份证、个人居住地址、手机号码、紧急联系人及联系方式等信息，同时保证所提供信息完全属实。上述信息如有变更，须在三日内提供其更新的信息给甲方，如未及时提供，甲方有权联系乙方亲属。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "5.2"
						+ CreateSP(4)
						+ "乙方自参加培训班时起，有义务遵守甲方的教学管理规章制度，不得有影响班级教学和影响他人学习的不良行为，如出现严重违反规章制度情形，甲方有权利按照规章制度的具体规定停止对乙方提供培训服务,乙方已缴纳费用将不予退还。相关教学管理规章制度详情见附件《传智播客.黑马程序员入学协议》，该附件为本协议的有效组成部分。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "5.3"
						+ CreateSP(4)
						+ "乙方有义务主动配合老师完成教学或其他教育教学工作，按照甲方的规章制度、教学大纲全力认真完成培训期间的学习。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "5.4" + CreateSP(4)
						+ "乙方不可自行将学习名额转让给他人。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "5.5"
						+ CreateSP(4)
						+ "如甲方发现乙方为与甲方有竞争关系公司的员工，或乙方与上述公司具有特殊关系可能导致商业秘密泄露的，甲方有权随时终止本培训协议。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "5.6"
						+ CreateSP(4)
						+ "乙方参加培训所需的各种软件，由乙方自行购买、安装、使用，如果因相关软件侵犯第三方知识产权的，由乙方自行负责，甲方不承担责任。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "5.7"
						+ CreateSP(4)
						+ "甲方提供的培训为高强度培训，不适合患有或曾经患有高血压、心脏病、抑郁症或其他精神类疾病、传染性疾病及其他相关病症的人群学习。乙方未向甲方提前作书面说明的，则签署本协议即视为乙方确认无上述疾病或病史并且能适应培训强度。乙方在培训期间发病或因此产生其他后果的，因此产生的责任由乙方自行承担，甲方不承担任何责任。 ";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}

			if (true) {
				String ParagraphTitle = CreateBR(2) + "第六条" + CreateSP(4)
						+ "转班、重读、退学规定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "6.1" + CreateSP(4)
						+ "转班、重读 ";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "对于遵守甲方相关规定，认真学习，但由于不可抗力因素造成短期内不能来校学习的，乙方有课程续读的权利，但需提前办理转班或重读手续。且需要注意以下内容：";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "（1）乙方在课程学习过程中出现下列情形之一的：累计旷课2天；累计迟到、早退合计5次；累计请事假3次，甲方有权取消乙方转班或重读资格。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（2）乙方重读需额外缴纳500元费用，且该费用缴纳后不予退还。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "（3）乙方申请重读或转班时，如遇目标班级人数（新生及升级学员人数）达到教室容量上限，甲方有权将乙方的重读或转班时间顺延到下期开班时间（最多顺延一期）。乙方进入目标班级的学时为其原就读班级的剩余学时。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（4）乙方在甲方安排的课程学习期间仅拥有一次转班或重读机会。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（5）如学员参加基础班，毕业后参加其他课程培训的，则转班重读以甲方相应管理规定为准。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（6）乙方重读应在乙方开课之日起两年内向甲方提出申请，逾期无效。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（7）甲方收到申请后，有义务告知乙方重读时间，如果乙方未按要求的时间报到进班，则视为自动放弃重读资格。";
				ParagraphContent += CreateBR(1);

				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "6.2" + CreateSP(4) + "退学";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（1）开课后，无特殊情况，不允许退学。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（2）开课7天内遇以下特殊情况，可申请退学。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(6)
						+ "经三级甲等医院确诊，患有精神病、癫痫、麻疯、传染性疾病或其他严重疾病，不能坚持学习或遇不可抗力因素造成无法继续学习。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(6)
						+ "对于校方批准退学的、符合退学条件的学员，乙方须立即支付已学习课程的实际培训费并承担退费手续费后，方可办理退学。实际培训费数额参照以下公式：实际培训费=(应交学费 / 标准总课时) * 已学课时 * 130%；退费手续费数额参照以下公式：（已收学费-实际培训费）*0.8%。例如：某学员应交学费2000元，开学前一次性付清（即已收学费2000元），标准总课时27天，学习2天课时后因病退学， 则实际培训费为（2000/ 27 )* 2 * 130%=193元；退费手续费为（2000-193）*0.8%=14.46元，实际将退学费为2000-193-14.46=1792.54元。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(6)
						+ "对于不符合退学条件的学员，乙方已缴纳费用不予退还。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（3）开课7天后，乙方不允许以任何理由退学、退费，遇以上特殊情况只可做转班处理。 ";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}

			if (true) {
				String ParagraphTitle = CreateBR(2) + "第七条" + CreateSP(4)
						+ "协议的终止";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "7.1" + CreateSP(4)
						+ "自双方履行完毕各自义务之日，本协议自动失效。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "7.2"
						+ CreateSP(4)
						+ "双方确认，本协议的签署、生效和履行以不违反中华人民共和国的法律法规为前提。如本协议中任意一条款违反相关法律法规，则该条款将被视为无效，但并不影响本协议其他条款的效力。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "7.3"
						+ CreateSP(4)
						+ "由于无法预见的不可抗力事件，如战争、地震、罢工、动乱、网络黑客的侵犯致使网络不能正常运行等事件发生，导致任何一方不能执行协议中的义务时，应及时通知对方，对方可根据实际情况部分或全部免除其应承担责任和义务。";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第八条" + CreateSP(4)
						+ "保密条款";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ "8.1"
						+ CreateSP(4)
						+ "乙方有义务对在培训过程中获悉的甲方教材、教案、讲义、习题、案例、教学视频、PPT等教学产品或其他作品进行保密。除个人学习使用外，不得以任何方式透露给任何第三方。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "8.2"
						+ CreateSP(4)
						+ "乙方有义务对获悉的甲方（包括甲方关联公司）的其他商业机密、人事信息、薪资标准、培训信息、教学模式等保密，不得任何方式透露给任何第三方。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "8.3" + CreateSP(4)
						+ "乙方违反上述保密义务给甲方造成损失的，由乙方负责赔偿，甲方保留追究乙方法律责任的权利。 ";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第九条" + CreateSP(4)
						+ "双方共同商定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "9.1" + CreateSP(4)
						+ "本协议自甲方盖章、乙方签字之日起生效。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "9.2" + CreateSP(4)
						+ "本协议一式二份，双方各执一份。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "9.3"
						+ CreateSP(4)
						+ "对本协议的所有条款，乙方均有权要求甲方进行解释说明，甲方有义务根据乙方要求对涉及到乙方利益的条款进行解释，但本协议一经乙方签署，即视为乙方对本协议条款已充分理解，甲方已履行解释说明义务。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "9.4"
						+ CreateSP(4)
						+ "本协议条款履行中如发生纠纷，各方应协商解决，协商不成，应当将争议提交到合同签订地人民法院通过诉讼方式解决。 ";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			/*
			 * if(true) { String ParagraphTitle = CreateBR(2) + "第三条" +
			 * CreateSP(4) + "培训条款"; String TextContent = ""; List<TextInfo>
			 * listTextInfo = new LinkedList<TextInfo>(); if(true){ TextInfo ti
			 * = new TextInfo(); TextContent = CreateBR(1) + CreateSP(4) + "1.1"
			 * + CreateSP(4) + "对于开课前未交清培训费的学员，甲方有权禁止其参加培训课程。"; ti.setDisp(1);
			 * ti.setText(TextContent); listTextInfo.add(ti); } if(true){
			 * TextInfo ti = new TextInfo(); TextContent = CreateBR(1) +
			 * CreateSP(4) + "1.2" + CreateSP(4) +
			 * "本培训课程标准费用为                  元整(￥             )，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为                  元整(￥          )。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客XX分公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客XX分公司内部协商代退费事宜，与乙方无关。"
			 * ; ti.setDisp(1); ti.setText(TextContent); listTextInfo.add(ti); }
			 * if(true){ TextInfo ti = new TextInfo(); TextContent = CreateBR(1)
			 * + CreateSP(4) + "1.3" + CreateSP(4) + "参加培训期间，食宿均由乙方自理，甲方不予负责。";
			 * ti.setDisp(1); ti.setDisp(0); ti.setText(TextContent);
			 * listTextInfo.add(ti); } if(true){ TextInfo ti = new TextInfo();
			 * TextContent = CreateBR(1) + CreateSP(4) + "1.4" + CreateSP(4) +
			 * " 培训地点：传智播客XX分公司所在地。"; TextContent += CreateBR(1); ti.setDisp(0);
			 * ti.setText(TextContent); listTextInfo.add(ti); }
			 * 
			 * SetPDFParagraph(ParagraphTitle,listTextInfo); }
			 */
		}

		if (true) {
			
			SetPDFBottom(signLocation, studentName, studentTel,
					studentIdentify, studentAddress, nowAddress, signDate);
		}
		CreateAE();
		doc.close();
	}

	/**
	 * 创建基础班PDF文档
	 * 
	 * @return
	 * @throws Exception
	 * @throws docException
	 */
	public void createPDFBase(String pdfFileName,PDFParams pdfParams) throws Exception {

		String pdfSerial = pdfParams.getPdfSerial();
		String subOrg = pdfParams.getOrgName();
		String studentName = pdfParams.getStudentName();
		String cardNo = pdfParams.getCardNo();
		String tel = pdfParams.getTel();
		String address = pdfParams.getAddress();
		String goodsName = pdfParams.getGoodsName();
		String startDate = pdfParams.getStartDate();
		String endDate = pdfParams.getEndDate();
		String price = pdfParams.getPrice();
		String priceCN = pdfParams.getPriceCN();
		String orderPrice = pdfParams.getOrderPrice();
		String orderPriceCN = pdfParams.getOrderPriceCN();
		String signLocation = pdfParams.getSignLocation();
		String studentTel = pdfParams.getStudentTel();
		String studentIdentify = pdfParams.getStudentIdentify();
		String studentAddress = pdfParams.getStudentAddress();
		String nowAddress = pdfParams.getNowAddress();
		String signDate = pdfParams.getSignDate();
		
		String studyAddr = pdfParams.getStudyAddress();

		PdfWriter.getInstance(doc, new FileOutputStream(new File(pdfFileName)));
		// 添加页眉
		Font font = new Font(bfChinese, 10, Font.NORMAL);
		Phrase phrase = new Phrase();
		Chunk chunk = new Chunk("重要文件，请认真阅读", font);
		phrase.add(chunk);

		/*HeaderFooter header = new HeaderFooter(phrase, false);
		header.setAlignment(Rectangle.ALIGN_RIGHT);
		header.setBorder(Rectangle.NO_BORDER);
		header.setBorder(Rectangle.NO_BORDER);

		doc.setHeader(header);*/
		doc.open();

		doc.newPage();

		if (true) {
			String nextContent1 = "乙方自愿报名参加江苏传智播客教育科技股份有限公司（以下简称“传智播客公司”）运营、";
			String nextContent2 = "（以下简称“传智播客分（子）公司”）辅助教学的传智播客.黑马程序员培训班，为明确双方的权利与义务，经双方友好协商，就培训相关事宜达成如下协议：";
			SetPDFTop(pdfSerial, subOrg, studentName, nextContent1,
					nextContent2, cardNo, tel, address);
		}

		if (true) {
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第一条" + CreateSP(4)
						+ "培训条款";
				/*
				 * String ParagraphContent = ""; ParagraphContent += CreateSP(4)
				 * + "1.1" + CreateSP(4) +
				 * "乙方参加学习"+goodsName+"课程 ，培训时间为："+startDate
				 * +"至 "+endDate+"（具体培训时间以实际上课时间为准） 。"; ParagraphContent +=
				 * CreateBR(1); ParagraphContent += CreateSP(4) + "1.2" +
				 * CreateSP(4) + "本培训课程标准费用为 "+price+"元整(￥"+priceCN+
				 * ")，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为"
				 * +orderPrice+"元整(￥"+orderPriceCN+
				 * ")。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客分（子）公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客分（子）公司内部协商代退费事宜，与乙方无关。"
				 * ; ParagraphContent += CreateBR(1); ParagraphContent +=
				 * CreateSP(4) + "1.3" + CreateSP(4) +
				 * "参加培训期间，食宿均由乙方自理，甲方不予负责。"; ParagraphContent += CreateBR(1);
				 * ParagraphContent += CreateSP(4) + "1.4" + CreateSP(4) +
				 * " 培训地点："+campusAddr; ParagraphContent += CreateBR(1);
				 * SetPDFParagraph(ParagraphTitle,ParagraphContent);
				 */

				String TextContent = "";
				List<TextInfo> listTextInfo = new LinkedList<TextInfo>();
				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(1) + CreateSP(4) + "1.1"
								+ CreateSP(4) + "乙方参加学习";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = goodsName;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "课程 ，培训时间为：";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = startDate;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "至 ";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = endDate;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "（具体培训时间以实际上课时间为准） 。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.2"
								+ CreateSP(4) + "本培训课程标准费用为 ";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = price;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "元整(￥";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = priceCN;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = ")，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = orderPrice;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = "元整(￥";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = orderPriceCN;
						ti.setDisp(1);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}

					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = ")。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客分（子）公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客分（子）公司内部协商代退费事宜，与乙方无关。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.3"
								+ CreateSP(4) + "参加培训期间，食宿均由乙方自理，甲方不予负责。";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
				}

				if (true) {
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = CreateBR(2) + CreateSP(4) + "1.4"
								+ CreateSP(4) + " 培训地点：";
						ti.setDisp(0);
						ti.setText(TextContent);
						listTextInfo.add(ti);
					}
					if (true) {
						TextInfo ti = new TextInfo();
						TextContent = price;
						ti.setDisp(1);
						ti.setText(studyAddr);
						listTextInfo.add(ti);
					}
				}
				SetPDFParagraph(ParagraphTitle, listTextInfo);

			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第二条" + CreateSP(4)
						+ "培训费支付条款";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "2.1" + CreateSP(4)
						+ "乙方必须在开课前向甲方一次性交清培训费。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "2.2"
						+ CreateSP(4)
						+ "培训费支付方式：现金、刷卡、转账。上述培训费用必须支付到甲方公司账户，如乙方将上述费用支付给个人（包括甲方员工）的，视为乙方未缴费。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "2.3" + CreateSP(4)
						+ "培训课程结束后对乙方已经支付的学费不予退还。";
				ParagraphContent += CreateBR(1);
				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第三条" + CreateSP(4)
						+ "培训费逾期规定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "3.1" + CreateSP(4)
						+ "对于开课前未交清培训费的学员，甲方有权禁止其参加培训课程。";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第四条" + CreateSP(4)
						+ "其他";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ "4.1"
						+ CreateSP(4)
						+ "乙方须如实提供相应的照片、身份证、个人居住地址、手机号码、紧急联系人及联系方式等信息，同时保证所提供信息完全属实。上述信息如有变更，须在三日内提供其更新的信息给甲方，如未及时提供，甲方有权联系乙方亲属。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.2"
						+ CreateSP(4)
						+ "乙方自参加培训班时起，有义务遵守甲方的教学管理规章制度，不得有影响班级教学和影响他人学习的不良行为，如出现严重违反规章制度情形，甲方有权利按照规章制度的具体规定停止对乙方提供培训服务,乙方已缴纳费用将不予退还。相关教学管理规章制度详情见附件《传智播客.黑马程序员入学协议》，该附件为本协议的有效组成部分。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.3"
						+ CreateSP(4)
						+ "乙方有义务主动配合老师完成教学或其他教育教学工作，按照甲方的规章制度、教学大纲全力认真完成培训期间的学习。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "4.4" + CreateSP(4)
						+ "乙方不可自行将学习名额转让给他人。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "4.5" + CreateSP(4)
						+ "乙方基础班毕业后，须通过相关考核才能顺利升级就业班，具体考核标准以甲方对应学科所提供的相关规定为准。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.6"
						+ CreateSP(4)
						+ "如甲方发现乙方为与甲方有竞争关系公司的员工，或乙方与上述公司具有特殊关系可能导致商业秘密泄露的，甲方有权随时终止本培训协议。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.7"
						+ CreateSP(4)
						+ "乙方参加培训所需的各种软件，由乙方自行购买、安装、使用，如果因相关软件侵犯第三方知识产权的，由乙方自行负责，甲方不承担责任。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "4.8"
						+ CreateSP(4)
						+ "甲方提供的培训为高强度培训，不适合患有或曾经患有高血压、心脏病、抑郁症或其他精神类疾病、传染性疾病及其他相关病症的人群学习。乙方未向甲方提前作书面说明的，则签署本协议即视为乙方确认无上述疾病或病史并且能适应培训强度。乙方在培训期间发病或因此产生其他后果的，因此产生的责任由乙方自行承担，甲方不承担任何责任。 ";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}

			if (true) {
				String ParagraphTitle = CreateBR(2) + "第五条" + CreateSP(4)
						+ "转班、重读、退学规定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "5.1" + CreateSP(4)
						+ "转班、重读 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "对于遵守甲方相关规定，认真学习，但由于不可抗力因素造成短期内不能来校学习的，乙方有课程续读的权利，但需提前办理转班或重读手续。且需要注意以下内容：";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "（1）乙方在课程学习过程中出现下列情形之一的：累计旷课2天；累计迟到、早退合计5次；累计请事假3次，甲方有权取消乙方转班或重读资格。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（2）乙方重读需额外缴纳500元费用，且该费用缴纳后不予退还。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(4)
						+ "（3）乙方申请重读或转班时，如遇目标班级人数（新生及升级学员人数）达到教室容量上限，甲方有权将乙方的重读或转班时间顺延到下期开班时间（最多顺延一期）。乙方进入目标班级的学时为其原就读班级的剩余学时。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（4）乙方在甲方安排的课程学习期间仅拥有一次转班或重读机会。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（5）如学员参加基础班，毕业后参加其他课程培训的，则转班重读以甲方相应管理规定为准。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（6）乙方重读应在乙方开课之日起两年内向甲方提出申请，逾期无效。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（7）甲方收到申请后，有义务告知乙方重读时间，如果乙方未按要求的时间报到进班，则视为自动放弃重读资格。";
				ParagraphContent += CreateBR(1);

				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "5.2" + CreateSP(4) + "退学";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（1）开课后，无特殊情况，不允许退学。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（2）开课7天内遇以下特殊情况，可申请退学。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(6)
						+ "经三级甲等医院确诊，患有精神病、癫痫、麻疯、传染性疾病或其他严重疾病，不能坚持学习或遇不可抗力因素造成无法继续学习。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ CreateSP(4)
						+ CreateSP(6)
						+ "对于校方批准退学的、符合退学条件的学员，乙方须立即支付已学习课程的实际培训费并承担退费手续费后，方可办理退学。实际培训费数额参照以下公式：实际培训费=(应交学费 / 标准总课时) * 已学课时 * 130%；退费手续费数额参照以下公式：（已收学费-实际培训费）*0.8%。例如：某学员应交学费2000元，开学前一次性付清（即已收学费2000元），标准总课时27天，学习2天课时后因病退学， 则实际培训费为（2000/ 27 )* 2 * 130%=193元；退费手续费为（2000-193）*0.8%=14.46元，实际将退学费为2000-193-14.46=1792.54元。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(6)
						+ "对于不符合退学条件的学员，乙方已缴纳费用不予退还。";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
						+ "（3）开课7天后，乙方不允许以任何理由退学、退费，遇以上特殊情况只可做转班处理。 ";
				ParagraphContent += CreateBR(1);

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}

			if (true) {
				String ParagraphTitle = CreateBR(2) + "第六条" + CreateSP(4)
						+ "协议的终止";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "6.1" + CreateSP(4)
						+ "自双方履行完毕各自义务之日，本协议自动失效。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "6.2"
						+ CreateSP(4)
						+ "双方确认，本协议的签署、生效和履行以不违反中华人民共和国的法律法规为前提。如本协议中任意一条款违反相关法律法规，则该条款将被视为无效，但并不影响本协议其他条款的效力。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "6.3"
						+ CreateSP(4)
						+ "由于无法预见的不可抗力事件，如战争、地震、罢工、动乱、网络黑客的侵犯致使网络不能正常运行等事件发生，导致任何一方不能执行协议中的义务时，应及时通知对方，对方可根据实际情况部分或全部免除其应承担责任和义务。";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第七条" + CreateSP(4)
						+ "保密条款";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4)
						+ "7.1"
						+ CreateSP(4)
						+ "乙方有义务对在培训过程中获悉的甲方教材、教案、讲义、习题、案例、教学视频、PPT等教学产品或其他作品进行保密。除个人学习使用外，不得以任何方式透露给任何第三方。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "7.2"
						+ CreateSP(4)
						+ "乙方有义务对获悉的甲方（包括甲方关联公司）的其他商业机密、人事信息、薪资标准、培训信息、教学模式等保密，不得任何方式透露给任何第三方。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "7.3" + CreateSP(4)
						+ "乙方违反上述保密义务给甲方造成损失的，由乙方负责赔偿，甲方保留追究乙方法律责任的权利。 ";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			if (true) {
				String ParagraphTitle = CreateBR(2) + "第八条" + CreateSP(4)
						+ "双方共同商定";
				String ParagraphContent = "";
				ParagraphContent += CreateBR(1);
				ParagraphContent += CreateSP(4) + "8.1" + CreateSP(4)
						+ "本协议自甲方盖章、乙方签字之日起生效。";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4) + "8.2" + CreateSP(4)
						+ "本协议一式二份，双方各执一份。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "8.3"
						+ CreateSP(4)
						+ "对本协议的所有条款，乙方均有权要求甲方进行解释说明，甲方有义务根据乙方要求对涉及到乙方利益的条款进行解释，但本协议一经乙方签署，即视为乙方对本协议条款已充分理解，甲方已履行解释说明义务。 ";
				ParagraphContent += CreateBR(2);
				ParagraphContent += CreateSP(4)
						+ "8.4"
						+ CreateSP(4)
						+ "本协议条款履行中如发生纠纷，各方应协商解决，协商不成，应当将争议提交到合同签订地人民法院通过诉讼方式解决。 ";

				SetPDFParagraph(ParagraphTitle, ParagraphContent);
			}
			/*
			 * if(true) { String ParagraphTitle = CreateBR(2) + "第三条" +
			 * CreateSP(4) + "培训条款"; String TextContent = ""; List<TextInfo>
			 * listTextInfo = new LinkedList<TextInfo>(); if(true){ TextInfo ti
			 * = new TextInfo(); TextContent = CreateBR(1) + CreateSP(4) + "1.1"
			 * + CreateSP(4) + "对于开课前未交清培训费的学员，甲方有权禁止其参加培训课程。"; ti.setDisp(1);
			 * ti.setText(TextContent); listTextInfo.add(ti); } if(true){
			 * TextInfo ti = new TextInfo(); TextContent = CreateBR(1) +
			 * CreateSP(4) + "1.2" + CreateSP(4) +
			 * "本培训课程标准费用为                  元整(￥             )，根据甲方现有的学费优惠政策（具体优惠政策以甲方相关规定为准），乙方实际应交纳的培训费用为                  元整(￥          )。上述费用由传智播客公司全部代为收取，收取后代乙方向传智播客XX分公司支付全部费用的30%作为其辅助教学的费用。如发生退费，由传智播客公司统一向乙方支付（乙方使用贷款方式缴纳学费的，退费方式按照甲乙双方与相关方的约定处理），传智播客公司及传智播客XX分公司内部协商代退费事宜，与乙方无关。"
			 * ; ti.setDisp(1); ti.setText(TextContent); listTextInfo.add(ti); }
			 * if(true){ TextInfo ti = new TextInfo(); TextContent = CreateBR(1)
			 * + CreateSP(4) + "1.3" + CreateSP(4) + "参加培训期间，食宿均由乙方自理，甲方不予负责。";
			 * ti.setDisp(1); ti.setDisp(0); ti.setText(TextContent);
			 * listTextInfo.add(ti); } if(true){ TextInfo ti = new TextInfo();
			 * TextContent = CreateBR(1) + CreateSP(4) + "1.4" + CreateSP(4) +
			 * " 培训地点：传智播客XX分公司所在地。"; TextContent += CreateBR(1); ti.setDisp(0);
			 * ti.setText(TextContent); listTextInfo.add(ti); }
			 * 
			 * SetPDFParagraph(ParagraphTitle,listTextInfo); }
			 */
		}

		if (true) {
			SetPDFBottom(signLocation, studentName, studentTel,
					studentIdentify, studentAddress, nowAddress, signDate);
		}
		CreateAE();
		doc.close();
	}

	private void CreateAE() throws Exception {
		doc.newPage();
		if (true) {
			Font font = new Font(bfChinese, 10, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(CreateSP(4) + "附件", font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 26, Font.BOLD);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_CENTER);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk("入学协议" + CreateBR(2), font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			Font font = new Font(bfChinese, 12, Font.NORMAL);
			Paragraph paragraph = new Paragraph();
			paragraph.setAlignment(Element.ALIGN_LEFT);
			Phrase phrase = new Phrase();
			Chunk chunk = new Chunk(
					CreateSP(10)
							+ "乙方自愿报名参加甲方的传智播客.黑马程序员培训班，为进一步明确甲乙双方的权利与义务，经甲乙双方友好协商，就入学相关事宜达成如下协议：",
					font);
			phrase.add(chunk);
			paragraph.add(phrase);
			doc.add(paragraph);
		}
		if (true) {
			String ParagraphTitle = CreateBR(2) + "第一条" + CreateSP(4)
					+ "甲方的权利和义务";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "1.1" + CreateSP(4)
					+ "甲方负责为乙方提供教学场地、教学设施和相应的教学设备，并负责进行维护，保证教学工作的正常开展。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "1.2"
					+ CreateSP(4)
					+ "甲方制定规章制度及安全注意事项，且有权利和义务对学员进行安全教育、监管和保护，在可预见范围内采取必要的安全措施。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "1.3" + CreateSP(4)
					+ "甲方有义务对乙方在学习纪律及学习成绩方面进行管理及考核。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "1.4" + CreateSP(4)
					+ "甲方不负责乙方的食宿，因食宿问题产生的相关责任甲方不予承担。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "1.5"
					+ CreateSP(4)
					+ "如乙方不遵守甲方教学管理规章制度，出现过激或违法行为，经劝诫后仍不改正，或出现本协议约定的其他违约情形，甲方有权利终止向乙方提供培训服务。对于出现上述情形的，先付费学员培训费不予退还。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "1.6" + CreateSP(4)
					+ "非因甲方原因导致的乙方人身损害及财产损失的，甲方不承担任何责任。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}

		if (true) {
			String ParagraphTitle = CreateBR(2) + "第二条" + CreateSP(4)
					+ "甲方的权利和义务";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "2.1" + CreateSP(4)
					+ "乙方有义务遵守国家的法律法规、社会公德和甲方的教学管理制度。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "2.2" + CreateSP(4)
					+ "乙方自行解决食宿，注意个人住宿安全及饮食卫生。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "2.3" + CreateSP(4)
					+ "如培训过程中出现不可预料的问题，应及时联系甲方班主任或由甲方指定的相关工作人员，乙方不得私自解决。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "2.4"
					+ CreateSP(4)
					+ "如甲方工作人员以外的人员对乙方造成人身损害的（包括学员之间相互造成人身损害），由侵权人承担侵权责任，甲方不承担任何责任。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "2.5" + CreateSP(4)
					+ "乙方有义务遵守以下规定：";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4)
					+ "（1）乙方有义务尊师重教，不得无端指责老师，当面顶撞、恶意诋毁老师。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4)
					+ "（2）保持衣着整洁，教学区内不准穿拖鞋。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（3）乙方有义务在进入教学区域时，佩戴听课证，如若没有佩戴，甲方有权进行监督检查，对未佩戴听课证的学员，每次罚款5元，听课证遗失须向班主任申请补办，补办费用为10元；";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（4）乙方应保持教室环境卫生，服从班级值日安排，教学区内禁止随地乱扔垃圾，把垃圾堆放到指定位置，不得在教室内吃饭。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（5）乙方有义务保管好甲方提供的教学设备，如有损坏，乙方必须按照市场价向甲方进行赔偿，不得给租用的电脑私设密码，一旦电脑出现损坏，必须按照市场价向甲方进行赔偿。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4)
					+ "（6）乙方在培训期间，须保管好个人物品，贵重物品要随身携带，如发生丢失，责任自负。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4)
					+ "（7）学习期间，乙方应保证正常出勤。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
					+ "① 如未在上课前进入教室，视为迟到，每次罚款5元，如迟到超过半小时，视同旷课，每次罚款20元；";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4) + CreateSP(4)
					+ "② 如在下课前离开教室，视为早退，每次罚款5元，如早退超过半小时，视同旷课，每次罚款20元；";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ CreateSP(4)
					+ "③ 乙方事假须在请假日之前向班主任申请。事假课时数列入缺勤记录；乙方如遇身体不适请病假，须向班主任提交申请，若病假3天以上，康复后须出示医院的相关证明，病假课时数不列入缺勤记录，否则按照事假处理，列入缺勤记录；如无请假记录，视同旷课处理，每次罚款20元；";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ CreateSP(4)
					+ "④ 迟到、早退合计超过N次（N=培训总天数*10%）；旷课天数合计超过N天（N=培训总天数*5%），或请假天数合计超过N天（N=培训总天数*10%），以上三种情况出现任意一种，甲方有权终止对学员的培训服务，学员培训费不予退还。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（8）乙方不得在教学区内做与学习无关的事，如大声喧哗、网络聊天、玩游戏、看电影、睡觉、玩手机等，上课期间不得打水、取快递、随意走动，一经发现将严肃处理。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + CreateSP(4)
					+ "（9）乙方如遇毕业答辨或不可抗力因素，必须请假的，须亲自到校予以说明，并填写假条，否则按旷课处理。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（10）乙方有义务团结同学，禁止打架斗殴，若出现此类情况甲方有权终止向乙方提供培训服务，学员培训费不予退还，情节严重者，移送有关机关处理。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（11）乙方不得在有屋顶的地方吸烟（北京市控制吸烟管理条例），在指定抽烟区吸烟时，也一定要将烟头掐灭并扔到指定位置；随地乱扔烟头或因为烟头没熄灭引起火灾者，视经济损失大小，乙方需要进行相应赔偿，情节严重者，移送有关机关处理。拒不服从管理者，甲方有权终止向乙方提供培训服务，学员培训费不予退还。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ CreateSP(4)
					+ "（12）乙方有义务遵守各自校区各自班级的班纪班规，拒不服从且言行过激者，甲方有权终止向乙方提供培训服务，学员培训费不予退还。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}
		if (true) {
			String ParagraphTitle = CreateBR(2) + "第三条" + CreateSP(4) + "其他";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "3.1"
					+ CreateSP(4)
					+ "甲方的所有培训课程属于高强度培训，不适合有高血压、心脏病、癫痫、抑郁症、其他传染性疾病等病史及一些遗传病史的人员学习，因隐瞒病史所产生的一切后果由乙方全权负责。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "3.2" + CreateSP(4)
					+ "乙方在读期间快递、邮件等由本人领取，甲方不代为收取或代为保管。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}
		if (true) {
			String ParagraphTitle = CreateBR(2) + "第四条" + CreateSP(4) + "协议的终止";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "4.1" + CreateSP(4)
					+ "自双方履行完毕各自义务之日，本协议自动失效。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}
		if (true) {
			String ParagraphTitle = CreateBR(2) + "第五条" + CreateSP(4) + "保密条款";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "5.1"
					+ CreateSP(4)
					+ "乙方有义务对在培训过程中获悉的甲方教材、教案、讲义、习题、案例、教学视频、PPT等教学产品或其他作品进行保密。除个人学习使用外，不得以任何方式透露给任何第三方。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "5.2"
					+ CreateSP(4)
					+ "乙方有义务对获悉的甲方（包括甲方关联公司）的其他商业机密、人事信息、薪资标准、培训信息、教学模式等保密，不得任何方式透露给任何第三方。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "5.3" + CreateSP(4)
					+ "乙方违反上述保密义务给甲方造成损失的，由乙方负责赔偿，甲方保留追究乙方法律责任的权利。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}
		if (true) {
			String ParagraphTitle = CreateBR(2) + "第六条" + CreateSP(4)
					+ "双方共同商定";
			String ParagraphContent = "";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4) + "6.1" + CreateSP(4)
					+ "本协议为双方签署的《培训协议》的组成部分。";
			ParagraphContent += CreateBR(1);
			ParagraphContent += CreateSP(4)
					+ "6.2"
					+ CreateSP(4)
					+ "对本协议的所有条款，乙方均有权要求甲方进行解释说明，甲方有义务根据乙方要求对涉及到乙方利益的条款进行解释，但本协议一经乙方签署，即视为乙方对本协议条款已充分理解，甲方已履行解释说明义务。";

			SetPDFParagraph(ParagraphTitle, ParagraphContent);
		}
	}

	// 构造函数；
	public PDFUtil() {
		InitPDFVar();
	}

	// 执行入口
	public static void main(String[] args) throws Exception {
		/*PDFUtil pdfUtilEmployment = new PDFUtil();
		pdfUtilEmployment.createPDFEmployment("D://就业班.pdf");
		PDFUtil pdfUtilBase = new PDFUtil();
		pdfUtilBase.createPDFBase("D://基础班.pdf");*/
	}

}