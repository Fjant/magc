package com.xincheng.utils;

import org.apache.commons.lang.RandomStringUtils;

/**
 * �ַ�İ�����
 * @author tiger
 *
 */
public class StringUtil {
	/**
	 * �ѷǷ���unicode�ַ��滻Ϊ�ո�
	 * *  Verify that no character has a hex value greater than 0xFFFD, or less than 0x20.
	 * Check that the character is not equal to the tab ("t), the newline ("n), the carriage return ("r), 
	 * or is an invalid XML character below the range of 0x20. 
	 * If any of these characters occur, an exception is thrown.<br/>
	 * <a href="http://msdn.microsoft.com/en-us/library/k1y7hyy9.aspx">��Դ(΢��msdn��̳)</a>
	 * @param value
	 * @return
	 */
	public static String removeInvalidUnicodeChar(String value){
		char[] chars= value.toCharArray();
		for (int i=0; i < value.length(); i++) {
        if (chars[i] > 0xFFFD){
//            throw new Exception("Invalid Unicode");//����ֱ���滻��0x0 
        	chars[i]=' ';
        }else if (chars[i] < 0x20 && chars[i] != '\t' & chars[i] != '\n' & chars[i] != '\r'){
//            throw new Exception("Invalid Xml Characters");//����ֱ���滻��0x0 
        	chars[i]=' ';
        }
    }
		return new String(chars);
	}
	
	
//		mps.put("A10","��ɽ�������ڽ���·121�Ŷ�������29¥");
//		mps.put("B10","��ɽ����ɽ��·35�ŲƸ�����5¥501/506/507/509/510/518");
//		mps.put("C10","��ݸ���ϳ���踣·200�ź��¹㳡1��13¥ȫ��");
//		mps.put("D10","����ʡ�Ͼ�������·19�Ž�����601-608��");
//		mps.put("E10","�����и����������������ų��й㳡�칫¥��������·1093�����Ŵ��ã���6��601��Ԫ");
//		mps.put("F10","��������ɽ·159��ʱ�����Ĵ���20¥");
//		mps.put("G10","�㶫ʡ������ӭ�������118���ݺ��Ƶ������͹㳡9¥03-05��07-10��Ԫ");
//		mps.put("H10","ɽ��ʡ��������������ɽ·110���������5��");
//		mps.put("I10","��Զ���³�����·��ɳ�������10¥A02a��A02b��A03��Ԫ");
//		mps.put("J10","�麣������������·68�ţ��ϴ��Է��12��3��303��Ԫ");
//		mps.put("K10","�й������³������·96�ţ�֮�����ã�23¥");
//		mps.put("L10","����ʡ�˲����Ƽ�·45-1�ű���һ��23��");
//		mps.put("M10","��������¥��㻯��20�ŷ᳼���ù㳡3¥303-305��");
//		mps.put("N10","�й��㽭ʡ�����н�������·17���Ϻ����д���11-1��");
//		mps.put("O10","����ʡ��ͨ�г紨������·6����������B��6¥");
//		mps.put("P10","����к������Ͼ�·20�Ž�ʴ���39��");
//		mps.put("Q10","����׳����������������������·63�Ž�Դ�ִ��6¥����");
//		mps.put("R10","����ʡ�����н��·53�ź����㳡A��8¥");
//		mps.put("S10","�㽭ʡ�����г�վ����˺ͼ�԰1-2��201��");
//		mps.put("210","������Խ����Խ��·112��34��35��36��38ȫ��");
//		mps.put("211","�ൺ�������������·22�Żƽ�㳡��¥���Ŵ��õ�11��");
//		mps.put("212","�����н�������һ·������ų���ʱ��1��Ԫ10��1003��1007��1008");
//		mps.put("213","�㽭ʡ������ҫ�ǹ㳡11��12��12-802��");
//		mps.put("214","����ʡ������ɳ�����Ӷ�·�񻪴���5��¥���A��");
//		mps.put("215","����ʡ�����й�¥��Ķ��ֵ��۷�ͤ��6�ź�����������7��01��03��04��05�칫");
//		mps.put("216","��̨��֥����·25������100���й㳡A��2803-2807");
//		mps.put("217","����׳�������������о���·19�ŷ������õ�8��9��");
//		mps.put("218","�ӱ�ʡʯ��ׯ�Ŷ������ϴ��6����������6��");
//		mps.put("219","�����л������ൺ��·2�����Ŵ���15¥");
//		mps.put("220","������ɽ��·381����ɽ����14¥");
//		mps.put("222","����ʡ�����������ݹ���10��׿Խ����17¥");
//		mps.put("223","Ȫ�������������·�����ֽ�����춼�㳡I��1102-1103��Ԫ");
//		mps.put("224","����׳��������������������콭·26�Ź�չ���﹫԰5��4��5��6��7��Ԫ");
//		mps.put("225","��������ɽ���ȸɽ·10�ſ�Ԫ�ϳǹ��10¥");
//		mps.put("226","�ӱ�ʡ������ʱ��·56�Ź�ó����6��");
//		mps.put("227","�ӱ�ʡ��ɽ���»�����32���»�ó����д��¥16��");
//		mps.put("228","������������·69�Ŷ�������3¥");
//		mps.put("229","��Ǩ�з�չ���64����Ǩ�ձ��縱¥4¥");
//		mps.put("230","�ع���䥽���·1�ŲƸ��㳡18F09-12��");
//		mps.put("231","�㽭ʡ������Խ�������������¥10��");
//		mps.put("232","����ʡ��ʯ������ɽ���·39�ž�Ӣ���ã��������ϼ��Ժ)10¥");
//		mps.put("233","����ʡ�����а��ƴ��27�Ű����ž�1��1��2��");
//		mps.put("234","�ĳ��ж���������·����·���ڳ�ʯ���̰�¥4¥");
//		mps.put("235","����ʡ֣���н�ˮ����·��26��˼���������3¥���ࡢ10¥�ϲ�");
//		mps.put("236","����׳������������������·110����ͨ����4��5��");
//		mps.put("237","�����й�����··���˺ӳ���ҵ����B��10��");
//		mps.put("238","�㽭ʡ��������·1118�Ŵ��´���7��701��703��705��707��");
//		mps.put("239","����ʡ��˳��˳����˳�ǽ��³�·�ж�31��������13��");
//		mps.put("241","�Ͳ����ŵ���������·25��������԰5��C��4¥");
//		mps.put("242","����׳��������������ɽ��·�����������8��807��808��809��");
//		mps.put("243","����ʡ��ɽ��������ʤ��·40�Ž������20¥2001��2003��2005-2011��");
//		mps.put("244","�ӱ�ʡ�ػʵ��к�����ӭ��·83�ŷ��Ǵ���12��");
//		mps.put("245","�����г�����԰��·1133����������3��칫��3��");
//		mps.put("246","ɽ��ʡ̫ԭ��ӭ����ӭ����319��ʡ�˴���¥4��5��");
//		mps.put("247","��������������������·55���Ͻ����16A����1601��1603����)");
//		mps.put("248","����ʡ�����б���·��ҵ��8���л��㳡403��404��");
//		mps.put("249","����ʡ̩���к�����ô���·220�����д��¥��¥");
//		mps.put("250","ɽ��ʡ�����н����·28�ų���绯��������19¥");
//		mps.put("251","ɽ��ʡ�ٷ���������·�жν����ʴ���7��");
//		mps.put("252","����ʡ��«������������·41��¥3��");
//		mps.put("310","�����ж��������������138���¶����㳡д��¥������10��");
//		mps.put("410","���������������·111�ų������8��805-808��");
//		mps.put("510","�Ϻ��к�����Ĵ���·859�����Ź㳡25¥2503��2504��");
//		mps.put("610","����ʡ�人�н�����·299�ŷ������SOHO��7��¥25��3-4��");
//	}
}
