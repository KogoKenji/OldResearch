import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

import edu.gatech.gart.ml.weka.DTW;

public class TextDistance {

	int StrInt;
	int count;


	private static int minimum(int a, int b, int c) {
		return Math.min(Math.min(a, b), c);
	}

	public String[] GetEvent(String fileName) {
		try {
			TextDistance td = new TextDistance();
			int num = td.GetColumn(fileName) / 2;
			int limit = td.GetColumn(fileName);

			int column = 0;
			String[] str1 = new String[num];
			String str2 = "";
			int counter1 = 0;
			File file = new File("./EVENT/" + fileName +"");
			BufferedReader br = new BufferedReader(new FileReader(file));

			while (counter1 != limit) {
				if (counter1 % 2 == 0) {

					str1[column] = br.readLine();
					column++;
				} else {
					str2 = br.readLine();

				}

				counter1++;
			}
			br.close();
			return str1;
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		String[] error = new String[1];
		error[0] = "error";
		return error;
	}

	public String[] GetGate(String fileName) {
		try {
			TextDistance td = new TextDistance();
			int num = td.GetColumn(fileName) / 2;
			int limit = td.GetColumn(fileName);

			int column = 0;
			String str1 = "";
			String[] str2 = new String[num];
			int counter1 = 0;
			File file = new File("./EVENT/" + fileName +"");
			BufferedReader br = new BufferedReader(new FileReader(file));

			while (counter1 != limit) {
				if (counter1 % 2 == 0) {

					str1 = br.readLine();
				} else {
					str2[column] = br.readLine();
					System.out.println("Event: " + str2[column]);
					column++;
				}

				counter1++;
			}
			br.close();
			return str2;
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		String[] error = new String[1];
		error[0] = "error";
		return error;
	}


	//Whileの中で偶数奇数判定を行えば読み飛ばし可能
	public int GetColumn(String fileName) {
		try {
			count = 0;
			File file = new File("./EVENT/" + fileName +"");
			BufferedReader br = new BufferedReader(new FileReader(file));

			String str1 = br.readLine();
			while (str1 != null) {
				str1 = br.readLine();
				count++;

			}
			br.close();
			return count;
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return -1;
	}

	public int[] PurseInt(String[] x, String fileName) {
		int[] num = new int[1];
		num[0] = -1;
		int column;
		int i = 0;


		TextDistance td = new TextDistance();
		column = td.GetColumn(fileName) / 2;

		int[] StrNum = new int[column];
		while (i != column) {
			StrNum[i] = Integer.parseInt(x[i]);
			i++;
		}
		//str = Integer.parseInt(td.ReadText());

		return StrNum;
	}

	public float[] PurseFloat(String[] x, String fileName) {
		float[] num = new float[1];
		num[0] = -1;
		int column;
		int i = 0;


		TextDistance td = new TextDistance();
		column = td.GetColumn(fileName) / 2;

		float[] StrNum = new float[column];
		while (i != column) {
			StrNum[i] = Float.parseFloat(x[i]);
			i++;
		}
		//str = Integer.parseInt(td.ReadText());

		return StrNum;
	}

	public int[] PhraseDegree(int[] phraseInt, String fileName) {

		TextDistance td = new TextDistance();
		int column = td.GetColumn(fileName) / 2;
		int[] degree = new int[column - 1];
		int limit = 0;

		while (limit != column - 1) {

			degree[limit] = phraseInt[limit + 1] - phraseInt[limit];
			limit++;
		}


		return degree;
	}


	public double DegreeIntLevenshtein(String fileName1, String fileName2, int[] x, int[] y) {
		int column1;
		int column2;

		TextDistance td = new TextDistance();
		column1 = (td.GetColumn(fileName1) / 2) - 1;
		column2 = (td.GetColumn(fileName2) / 2) - 1;

		int[][] distance = new int[column1 + 1][column2 + 1];
		//int div1 = column1();
		//int div2 = column2();
		for (int i = 0; i <= column1; i++)
			distance[i][0] = i;
		for (int j = 1; j <= column2; j++)
			distance[0][j] = j;

		for (int i = 1; i <= column1; i++)
			for (int j = 1; j <= column2; j++)
				distance[i][j] = minimum(
						distance[i - 1][j] + 1,
						distance[i][j - 1] + 1,
						distance[i - 1][j - 1] + ((x[i - 1] == y[j - 1]) ? 0 : 1));


		if (column1 >= column2) {
			double res = (double) distance[column1][column2] / (double) column1;
//			System.out.println("str1::" + column1);
//			System.out.println((double) distance[column1][column2]);
//			System.out.println("不一致度: " + res);
//			System.out.println("一致度: " + (1 - res));
		} else if (column2 > column1) {
			double res = (double) distance[column1][column2] / (double) column2;
//			System.out.println("str2::" + column2);
//			System.out.println((double) distance[column1][column2]);
//			System.out.println("不一致度: " + res);
//			System.out.println("一致度: " + (1 - res));
		}

		return distance[column1][column2];

	}


	public static void main(String[] args) throws IOException {
		TextDistance td = new TextDistance();
		CustomDTW cd = new CustomDTW();
		LogCustom lc = new LogCustom();



		String path = "./EVENT";
		File dir = new File(path);
		File[] files = dir.listFiles();
		for (int i = 1; i < files.length; i++) {

			String file1 = files[i].getName(); //ファイル名取得

			int column1 = td.GetColumn(file1);//行数取得
			//System.out.println(file1 + "  "+ column1);

			String[] EventValue1 = new String[column1 /2];//イベント値取得
			EventValue1 = td.GetEvent(file1);
			int[] PurseIntResultFile1 = td.PurseInt(EventValue1, file1); //Int値に変換

			float[] customValue1 = new float[PurseIntResultFile1.length]; //カスタム値の定義
			customValue1 = cd.CustomDTW(PurseIntResultFile1); // カスタムに変換


			float[] LogcustomValue1 = new float[PurseIntResultFile1.length];
			LogcustomValue1 = lc.LogCustom(PurseIntResultFile1);


			for (int j = 1; j < files.length; j++) {

				String file2 = files[j].getName();

				int column2 = td.GetColumn(file2);
				String[] EventValue2 = new String[column2 /2]; //イベント値取得
				EventValue2 = td.GetEvent(file2);
				int[] PurseIntResultFile2 = td.PurseInt(EventValue2, file2); //Int値に変換
				float[] customValue2 = new float[PurseIntResultFile1.length]; //カスタム値の定義
				customValue2 = cd.CustomDTW(PurseIntResultFile2); // カスタムに変換

				float[] LogcustomValue2 = new float[PurseIntResultFile2.length];
				LogcustomValue2 = lc.LogCustom(PurseIntResultFile2);

				if(i==j){

				}
					else {
					DTW DtwResult1 = new DTW(customValue1, customValue2); // DTW実行
					int fileNum1 = 15 + i;
					int fileNum2 = 15 + j;


					File file3 = new File("./DTWResult/DTWResult_" + fileNum1 + "_" + fileNum2 + ".txt");
					PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(file3)));
					pw1.println(DtwResult1);// 結果出力

					pw1.println("\n");

					DTW DtwResult2 = new DTW(LogcustomValue1, LogcustomValue2); // DTW実行
					pw1.println(DtwResult2);

					double distance = td.DegreeIntLevenshtein(file1,file2,PurseIntResultFile1,PurseIntResultFile2);
					double res = (distance / (double) column1)*100;

					pw1.println("\n\n");

					pw1.println("不一致度 " + res +"%");// 結果出力
					pw1.println("編集距離" + distance);
					pw1.close();




				}




			}
		}

	}
}