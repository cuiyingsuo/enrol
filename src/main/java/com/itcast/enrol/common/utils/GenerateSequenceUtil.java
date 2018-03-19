package com.itcast.enrol.common.utils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Random;

/**
 * 生成唯一的20数字序列号;
 *
 * @author yanghui <br>
 *         2016年3月28日 上午11:40:33
 */
public class GenerateSequenceUtil {

   /* private static int spec = 0;

    static {
        Random r = new Random();
        spec = r.nextInt(99);
        while (spec < 10) {
            spec = r.nextInt(99);
        }
    }*/

    public static String getDateTimeStr() {
        Calendar CD = Calendar.getInstance();

        int YY = CD.get(Calendar.YEAR);
        // add by 周铭株解决分布式下id重复的可能
        //YY = Integer.parseInt(String.valueOf(YY).replaceFirst("20", String.valueOf(spec)));

        int MM = CD.get(Calendar.MONTH) + 1;
        int DD = CD.get(Calendar.DATE);
        /*int HH = CD.get(Calendar.HOUR_OF_DAY);
        int NN = CD.get(Calendar.MINUTE);
        int SS = CD.get(Calendar.SECOND);*/
        // int MI = CD.get(Calendar.MILLISECOND);

        return String.format("%04d%02d%02d", YY, MM, DD);
    }

    private static String day="";
    private static int seq = 0;
    private static final int MAX = 9999;

    public static synchronized long generateSequenceNo() {

        String strPart1 = getDateTimeStr();
        if(!strPart1.equals(day)){
        	day=strPart1;
        	seq=0;
        }
        
        String strPart2 = String.format("%04d", seq);

        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }
        // modify by 周铭株提升效率有一点算一点
        Long lValue = Long.parseLong(strPart1 + strPart2);
        // Long lValue = new Long(strPart1 + strPart2);
        return lValue;
    }

    public static void main(String[] args) {
        // System.out.println(new Random().nextInt(99));
        for (int i = 0; i < 100; i++) {
            long KeyID = generateSequenceNo();
            BigDecimal bd = new BigDecimal(KeyID);
            System.out.println("KeyID=" + KeyID);// System.out.println("bd="+bd);
        }
    }

}
