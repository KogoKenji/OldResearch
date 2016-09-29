public class LevenshteinDistance {
	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public static int computeLevenshteinDistance(String str1,String str2) {
		int[][] distance = new int[str1.length() + 1][str2.length() + 1];
		//int div1 = str1.length();
		//int div2 = str2.length();
		for (int i = 0; i <= str1.length(); i++)
			distance[i][0] = i;
		for (int j = 1; j <= str2.length(); j++)
			distance[0][j] = j;

		for (int i = 1; i <= str1.length(); i++)
			for (int j = 1; j <= str2.length(); j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1]+ ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
		
		
		
		
		
		if(str1.length()>=str2.length()){
			double res = (double)distance[str1.length()][str2.length()]/(double)str1.length();
			System.out.println("str1::" + str1.length());
			System.out.println( (double)distance[str1.length()][str2.length()]);
			System.out.println(res);
			System.out.println(1-res);
			}
		else if(str2.length()>str1.length()){
			double res = (double)distance[str1.length()][str2.length()]/(double)str2.length();
			System.out.println("str2::" + str2.length());
			System.out.println((double)distance[str1.length()][str2.length()]);
			System.out.println(res);
			System.out.println(1-res);
				};
		return distance[str1.length()][str2.length()];

	}

	public static void main(String[] args){
		String string1 = "拝啓　時下ますますご清栄のこととお慶び申し上げます。この度、Ο月Ο日をもちまして弊社ΟΟ支店勤務を命ぜられ過日着任いたしました。皆様には、ΟΟ在勤中に格別のご厚情を賜り深く感謝いたしております。新任地におきましても、一層の努力をしてまいる所存でございますので、今後ともご指導ご鞭撻を賜りますようお願い申し上げます。略儀ながら書中をもちましてお礼旁々ご挨拶申し上げます 。 ";
		String string2 = "拝啓　時下ますますご清栄のこととお慶び申し上げます。この度、Ο月Ο日付でΟΟΟ部への異動を命ぜられ過日着任いたしました。ΟΟ様にはΟΟΟ部在勤中、公私共々格別のご厚情を賜り心より感謝いたしております。（これからは/このうえは）、（心機一転新たな気持ちで/皆様からご指導いただいたことを肝に銘じ、）一層の努力をしてまいる所存です。 今後とも、倍旧のご（交誼/厚情）を賜りますようお願い申し上げます。略儀ながら書中をもちましてお礼旁々ご挨拶申し上げます 。";
		LevenshteinDistance ld = new LevenshteinDistance();
		ld.computeLevenshteinDistance(string1, string2);


	}
}