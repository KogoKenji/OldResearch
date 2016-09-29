import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

class MakeMusic{
	static double searchArray[][] = new double [200][23];
	static double QueryArray[][] = new double[6][2];
	static int Random = 0;
	static int NearNo = 0;


	public List MakeMusic_main(List soundList, int row, int PhraseCount, List useFileName){
		System.out.println("Running MakeMusic.java\n");
		int CountNo = 0;
		String FirstFileName = "";

		for(int a=0; a<useFileName.size(); a++){
			System.out.println("[midiFileName] "+ (a+1) +": "+ useFileName.get(a));
		}

		MakeMusic mm = new MakeMusic();
		mm.RandomSelect(FirstFileName, useFileName);	// ランダム選曲を行う

		mm.GetFirstPhrase(removeFileExtension(String.valueOf(useFileName.get(Random)))+".txt");

		for(int a=0; a<6; a++){
			System.out.println("["+ (a+1) +"] Event:"+ (int)QueryArray[a][0] + "  Gate:"+ (int)QueryArray[a][1]
			                      + "  Step:"+ mm.ReadStepFile(String.valueOf(useFileName.get(Random)), a));
		}

		// ComposeSystem.javaでテーブルに出力する配列
		for(int a=0; a<3; a++){
			soundList.add((int)QueryArray[a][0]);
			soundList.add((int)QueryArray[a][1]);
			soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get(Random)), a));
		}

		/*	ループが起こった場合にシフトしたフレーズを手入力で検索
		QueryArray[0][0] = 74;
		QueryArray[0][1] = 235;
		QueryArray[1][0] = 67;
		QueryArray[1][1] = 61;
		QueryArray[2][0] = 72;
		QueryArray[2][1] = 29;
		QueryArray[3][0] = 72;
		QueryArray[3][1] = 56;
		QueryArray[4][0] = 70;
		QueryArray[4][1] = 55;
		QueryArray[5][0] = 72;
		QueryArray[5][1] = 122;
*/

		mm.ComposePhrase(Random, QueryArray, useFileName);

		// ComposeSystem.javaでテーブルに出力する配列
		soundList.add((int)searchArray[NearNo][2]);
		soundList.add((int)searchArray[NearNo][3]);
		soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]-1));
		soundList.add((int)searchArray[NearNo][4]);
		soundList.add((int)searchArray[NearNo][5]);
		soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]));
		soundList.add((int)searchArray[NearNo][6]);
		soundList.add((int)searchArray[NearNo][7]);
		soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]+1));

		while(CountNo < PhraseCount-1){
			System.out.println("\n\n************** NEXT QueryArray **************");
			System.out.println("楽曲No."+ (int)searchArray[NearNo][0] +"："+ useFileName.get((int)searchArray[NearNo][0]-1));
			System.out.println("["+ (int)(searchArray[NearNo][1]+3) +"] Event:"+ (int)searchArray[NearNo][8] + "  Gate:"+ (int)searchArray[NearNo][9]);
			System.out.println("["+ (int)(searchArray[NearNo][1]+4) +"] Event:"+ (int)searchArray[NearNo][10] + "  Gate:"+ (int)searchArray[NearNo][11]);
			System.out.println("["+ (int)(searchArray[NearNo][1]+5) +"] Event:"+ (int)searchArray[NearNo][12] + "  Gate:"+ (int)searchArray[NearNo][13]);
			System.out.println();

			if(searchArray[NearNo][12] == 0){
				System.out.println("楽曲の最終フレーズが使用されたため終了します");
				break;
			}


			for(int a=0; a<QueryArray.length; a++){	// 次のクエリフレーズを代入
				QueryArray[a][0] = searchArray[NearNo][2*a+2];
				QueryArray[a][1] = searchArray[NearNo][2*a+3];
			}

			mm.ComposePhrase((int)searchArray[NearNo][0]-1, QueryArray, useFileName);

			// ComposeSystem.javaでテーブルに出力する配列
			soundList.add((int)searchArray[NearNo][2]);
			soundList.add((int)searchArray[NearNo][3]);
			soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]-1));
			soundList.add((int)searchArray[NearNo][4]);
			soundList.add((int)searchArray[NearNo][5]);
			soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]));
			soundList.add((int)searchArray[NearNo][6]);
			soundList.add((int)searchArray[NearNo][7]);
			soundList.add(mm.ReadStepFile(String.valueOf(useFileName.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]+1));

			CountNo++;
		}

		System.out.println(">>>>>Finish<<<<<\n");
		return soundList;
	}


	public static String removeFileExtension(String filename) {
		int lastDotPos = filename.lastIndexOf('.');

	    if (lastDotPos == -1) {
	      return filename;
	    } else if (lastDotPos == 0) {
	      return filename;
	    } else {
	      return filename.substring(0, lastDotPos);
	    }
	}


	public String RandomSelect(String firstFileName, List usefilename){
		while(true){	// 0～39の乱数を発生
			int ran = (int)(Math.random()*100);

			if(ran < usefilename.size()){
				Random = ran;
				break;
			}
		}

//		Random = 16;
		System.out.println("Random>>"+ Random);

		System.out.println("**************** FirstPhrase ****************");
		System.out.println("[楽曲No．"+ (Random+1) +"]"+ removeFileExtension(String.valueOf(usefilename.get(Random))) + ".mid");

		return firstFileName;
	}


	public void GetFirstPhrase(String firstFileName){
		try{
			// EventとGateの書き込まれたテキストファイルを配列に代入
			File FileName = new File("./EVENT/" + firstFileName);

			BufferedReader br_tmp = new BufferedReader(new FileReader(FileName));
			String str_tmp = br_tmp.readLine();

			//テキストファイルの行数を取得
			int count = 0;
			while(str_tmp != null){
				str_tmp = br_tmp.readLine();
				count++;
			}

			BufferedReader br = new BufferedReader(new FileReader(FileName));
			String str = br.readLine();

			int eve = 0;
			double int_str = 0;
			double eventArray1[] = new double [count];
			while(str != null){	//EventとGateを一次元配列eventArray1[]に代入
				int_str = Integer.parseInt(str);
				eventArray1[eve] = int_str;
				str = br.readLine();
				eve++;
			}

			double eventArray2[][] = new double[count/2+5][2];
			int a = 0;
			while(a < count/2){	//一次元配列eventArray1[]のEventとGateを二次配列eventArray2[][]に代入
				//System.out.println("eve="+eve);
				eventArray2[a][0] = eventArray1[2*a];
				eventArray2[a][1] = eventArray1[2*a+1];
				a++;
			}

			for(int b=0; b<6; b++){	// 後に続く3音を含めた先頭フレーズの代入
				for(int c=0; c<2; c++){
					QueryArray[b][c] = eventArray2[b][c];
				}
			}

		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}

	}


	// 該当フレーズの中から最適フレーズを取得する
	public double[][] ComposePhrase(int SoundNo, double queryArray[][], List usefilename){

		for(int a=0; a<3; a++){	// 先頭フレーズの３音を代入
			for(int b=0; b<2; b++){
				searchArray[a][b] = queryArray[a+3][b];
			}
		}

		// 入力されたフレーズを検索
		SearchPhrase sp = new SearchPhrase();
		sp.getPhrase(searchArray, SoundNo, usefilename);

		// 該当フレーズから最適なフレーズ(類似度Ｄの近似値をもつフレーズ)を検索
		int low = 0;
		int hi = 0;

		hi = (int)searchArray[0][22] - 1;

		if(searchArray[low][20] <= 3 && low < hi){	// 3より小さい側の近似値を探す
			while(searchArray[low][20] <= 3 && low < hi){
				low++;
//				System.out.println("low>>"+ low);
			}
			low--;
			if(searchArray[hi][20] <= 3){
				low = hi;
			}
		}

		if(0 <= hi){
			if(3 <= searchArray[hi][20]){	// 3より大きい側の近似値を探す

				while(3 <= searchArray[hi][20] && 0 < hi){
					hi--;
				}
				hi++;
				if(3 <= searchArray[0][20]){
					hi = 0;
				}
			}
		}

		if(3 - searchArray[low][20] < searchArray[hi][20] - 3){	// lowのときの近似値の方が小さい場合
			NearNo = low;

		}else if(searchArray[hi][20] - 3 < 3 - searchArray[low][20]){	// hiのときの近似値の方が小さい場合
			NearNo = hi;

		}else{	// lowとhiの値が同じ(近似値が3である)場合
			NearNo = hi;
		}

		MakeMusic mm = new MakeMusic();
		// 最適フレーズの出力
		System.out.println("最適なフレーズは:");
		System.out.println("楽曲No."+ (int)searchArray[NearNo][0] +"："+ usefilename.get((int)searchArray[NearNo][0]-1));

		System.out.println("["+ (int)searchArray[NearNo][1] +"] Event:"+ (int)searchArray[NearNo][2] + "  Gate:"+ (int)searchArray[NearNo][3]
		                      + "  Step:"+ mm.ReadStepFile(String.valueOf(usefilename.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]-1));

		System.out.println("["+ (int)(searchArray[NearNo][1] + 1) +"] Event:"+ (int)searchArray[NearNo][4] + "  Gate:"+ (int)searchArray[NearNo][5]
		                      + "  Step:"+ mm.ReadStepFile(String.valueOf(usefilename.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]));

		System.out.println("["+ (int)(searchArray[NearNo][1]+2) +"] Event:"+ (int)searchArray[NearNo][6] + "  Gate:"+ (int)searchArray[NearNo][7]
		                      + "  Step:"+ mm.ReadStepFile(String.valueOf(usefilename.get((int)searchArray[NearNo][0]-1)), (int)searchArray[NearNo][1]+1));
		System.out.println();
		System.out.println("類似度Ｄ＝"+ searchArray[NearNo][20]);
		System.out.println("音長変化割合の最大値と最小値の差＝"+ searchArray[NearNo][21]);

		return searchArray;
	}


	public int ReadStepFile(String name, int soundNomber){
		try{
			File FileName = new File("./STEP/" + removeFileExtension(name) + ".txt");

			BufferedReader br_tmp = new BufferedReader(new FileReader(FileName));
			String str_tmp = br_tmp.readLine();

			//テキストファイルの行数を取得
			int count = 0;
			while(str_tmp != null){
				str_tmp = br_tmp.readLine();
				count++;
			}

			BufferedReader br = new BufferedReader(new FileReader(FileName));
			String str = br.readLine();

			int eve = 0;
			double int_str = 0;
			double eventArray[] = new double [count];
			while(str != null){	//EventとGateを一次元配列eventArray1[]に代入
				int_str = Integer.parseInt(str);
				eventArray[eve] = int_str;
				str = br.readLine();
				eve++;
			}

			soundNomber = (int)eventArray[2*soundNomber+1];

		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}

		return soundNomber;
	}
}
