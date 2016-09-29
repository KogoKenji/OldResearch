import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class SearchPhrase{
	static double originalSong[][] = new double[200][23];

	//	ファイルの拡張子を削除するメソッド
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


	// 検索フレーズに対して該当するフレーズを全て取得する
	public double[][] getPhrase(double resultArray[][], int songNo, List useFileName){
		int x = 0;	// xの初期化
		int y = 0;	// yの初期化

		//MakeMusic.javaによって入力された値(resultArray[][])をsearch[][]にコピー
		double search[][] = new double [3][2];
		for(int array1=0; array1<3; array1++){
			for(int array2=0; array2<2; array2++){
				search[array1][array2] = resultArray[array1][array2];
			}
		}

		// resultArrayの初期化
		for(int a=0; a<resultArray.length; a++){
			for(int b=0; b<resultArray[0].length; b++){
				resultArray[a][b] = 0;
			}
		}
		// originalSongの初期化
		for(int a=0; a<originalSong.length; a++){
			for(int b=0; b<originalSong[0].length; b++){
				originalSong[a][b] = 0;
			}
		}


		try{
			//テキストの読み込み
			System.out.println();
			int n = 0;
			double division[] = new double[3];

			for (int i = 0; i < useFileName.size(); i++) {	//pathで指定したディレクトリ内のファイル名を全て取得する
				File file = new File("./EVENT/"+ removeFileExtension(String.valueOf(useFileName.get(i))) +".txt");
				String name = file.getName();
				if(name.endsWith(".txt")){
//					System.out.println((n+1) + ":  " + removeFileExtension(name) + ".mid");

    				//EventとGateの書き込まれたテキストファイルを配列に代入
					File FileName = new File("./EVENT/" + name);

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

					//楽曲から指定されたフレーズを検索
					int k = 0;
					int total = 0;

					while(k < count/2){
						if(eventArray2[k][0] == search[0][0] && eventArray2[k+1][0] == search[1][0] && eventArray2[k+2][0] == search[2][0]){

							division[0] = search[0][1] / eventArray2[k][1];		// 1音目の音長の割合
							division[1] = search[1][1] / eventArray2[k+1][1];	// 2音目の音長の割合
							division[2] = search[2][1] / eventArray2[k+2][1];	// 3音目の音長の割合

							//音長の差をソート
							for(int sort=0; sort<division.length-1; sort++){	// 最後の要素を除いて、すべての要素を並べ替え
								for(int s=division.length-1; s>sort; s--){	// 下から上に順番に比較
									if(division[s]<division[s-1]){	// 上の方が大きいときは互いに入れ替え
										double tmp = division[s];
										division[s] = division[s-1];
										division[s-1] = tmp;
									}
								}
							}
//							System.out.println(division[0]);
//							System.out.println(division[1]);
//							System.out.println(division[2]);
//
//							System.out.println("result"+ (total+1) +"    ("+ (division[0]+division[1]+division[2]) +") <<"+ (division[2]-division[0]) +">>");
//							System.out.println("["+ (k+1) +"] Event:"+ (int)eventArray2[k][0] + "  Gate:"+ (int)eventArray2[k][1]);
//							System.out.println("["+ (k+2) +"] Event:"+ (int)eventArray2[k+1][0] + "  Gate:"+ (int)eventArray2[k+1][1]);
//							System.out.println("["+ (k+3) +"] Event:"+ (int)eventArray2[k+2][0] + "  Gate:"+ (int)eventArray2[k+2][1] +"\n");


							if(n == songNo){	// 検索フレーズと原曲が同じ場合
								originalSong[y][0] = n+1;	//曲番号
								originalSong[y][1] = k+1;	//1音目は何番目の音か
								originalSong[y][2] = eventArray2[k][0];		//1音目の音高
								originalSong[y][3] = eventArray2[k][1];		//1音目の音長
								originalSong[y][4] = eventArray2[k+1][0];	//2音目の音高
								originalSong[y][5] = eventArray2[k+1][1];	//2音目の音長
								originalSong[y][6] = eventArray2[k+2][0];	//3音目の音高
								originalSong[y][7] = eventArray2[k+2][1];	//3音目の音長
								originalSong[y][8] = eventArray2[k+3][0];	//4音目の音高
								originalSong[y][9] = eventArray2[k+3][1];	//4音目の音長
								originalSong[y][10] = eventArray2[k+4][0];	//5音目の音高
								originalSong[y][11] = eventArray2[k+4][1];	//5音目の音長
								originalSong[y][12] = eventArray2[k+5][0];	//6音目の音高
								originalSong[y][13] = eventArray2[k+5][1];	//6音目の音長
								originalSong[y][20] = division[0]+division[1]+division[2];	//それぞれの音長の変化割合の合計
								originalSong[y][21] = division[2]-division[0];	//音長の差の最大値と最小値の差の絶対値

								y++;

							}else{	//検索フレーズと原曲が異なる場合
								resultArray[x][0] = i+1;	//曲番号
								resultArray[x][1] = k+1;	//1音目は何番目の音か
								resultArray[x][2] = eventArray2[k][0];		//1音目の音高
								resultArray[x][3] = eventArray2[k][1];		//1音目の音長
								resultArray[x][4] = eventArray2[k+1][0];	//2音目の音高
								resultArray[x][5] = eventArray2[k+1][1];	//2音目の音長
								resultArray[x][6] = eventArray2[k+2][0];	//3音目の音高
								resultArray[x][7] = eventArray2[k+2][1];	//3音目の音長
								resultArray[x][8] = eventArray2[k+3][0];	//4音目の音高
								resultArray[x][9] = eventArray2[k+3][1];	//4音目の音長
								resultArray[x][10] = eventArray2[k+4][0];	//5音目の音高
								resultArray[x][11] = eventArray2[k+4][1];	//5音目の音長
								resultArray[x][12] = eventArray2[k+5][0];	//6音目の音高
								resultArray[x][13] = eventArray2[k+5][1];	//6音目の音長
								resultArray[x][20] = division[0]+division[1]+division[2];	//それぞれの音長の変化割合の合計
								resultArray[x][21] = division[2]-division[0];	//音長の差の最大値と最小値の差の絶対値

								x++;
							}
							total++;
						}
						k++;
					}

					if(total == 0){
//						System.out.println("該当するフレーズがありません\n");
					}

				}else{
					System.out.println((n+1) + ":  " + name);
					System.out.println("拡張子が[.txt]でないため、読み込めません");
				}

				n++;
			}

			if(x == 0){	// 原曲以外の楽曲に検索フレーズが存在しない場合
				for(int a=0; a<y; a++){	// originalSongをresultArrayに代入
					for(int b=0; b<23; b++){
						resultArray[a][b] = originalSong[a][b];
					}
				}

				// 該当フレーズを音長によってソート
				System.out.println("**************** 同曲から使用 ***************");
				for(int sort2=0; sort2<y-1; sort2++){	// 最後の要素を除いて、すべての要素を並べ替え
					for(int s2=y-1; s2>sort2; s2--){	// 下から上に順番に比較
						if(resultArray[s2][20]<resultArray[s2-1][20]){	// 上の方が大きいときは互いに入れ替え

							for(int change=0; change<22; change++){	//resultArrayの全ての要素を上の値と入れ替える
								double tmp2 = resultArray[s2][change];
								resultArray[s2][change] = resultArray[s2-1][change];
								resultArray[s2-1][change] = tmp2;
							}
						}
					}
				}

				for(int length=0; length<resultArray.length; length++){
					resultArray[length][22] = y;
				}

			// 該当フレーズを音長によってソート
			}else{
				System.out.println("**************** 異曲から使用 ***************");
				//それぞれの音長の差の絶対値の合計を基準にresultArrayをソート
				for(int sort2=0; sort2<x-1; sort2++){	// 最後の要素を除いて、すべての要素を並べ替え
					for(int s2=x-1; s2>sort2; s2--){	// 下から上に順番に比較
						if(resultArray[s2][20]<resultArray[s2-1][20]){	// 上の方が大きいときは互いに入れ替え

							for(int change=0; change<22; change++){	//resultArrayの全ての要素を上の値と入れ替える
								double tmp2 = resultArray[s2][change];
								resultArray[s2][change] = resultArray[s2-1][change];
								resultArray[s2-1][change] = tmp2;
							}
						}
					}
				}

				if(resultArray[0][20] == resultArray[1][20]){	// resultArray[][20]で最小値が複数存在する場合
					int arrayLength = 0; //	resultArray[][8]が最小値であるものの個数
					for(int a=0; resultArray[a][20]==resultArray[a+1][20]; a++){
						arrayLength++;
					}

					for(int sort3=0; sort3<arrayLength-1; sort3++){	// 最後の要素を除いて、すべての要素を並べ替え
						for(int s3=arrayLength-1; s3>sort3; s3--){	// 下から上に順番に比較
							if(resultArray[s3][21]<resultArray[s3-1][21]){	// 上の方が大きいときは互いに入れ替え

								for(int change3=0; change3<22; change3++){	//resultArrayの全ての要素を上の値と入れ替える
									double tmp3 = resultArray[s3][change3];
									resultArray[s3][change3] = resultArray[s3-1][change3];
									resultArray[s3-1][change3] = tmp3;
								}
							}
						}
					}
				}

				for(int length=0; length<resultArray.length; length++){
					resultArray[length][22] = x;
				}
			}

		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}

		return resultArray;
	}

}