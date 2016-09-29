import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getStep {

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


	public void toArray(String FileName){
		ArrayList<Integer> onArray = new ArrayList<Integer>();
		ArrayList<Integer> offArray = new ArrayList<Integer>();
		int on = 0;
		int off = 0;
		int int_on = 0;
		int int_off = 0;

		try{
			//NoteOnの書き込まれたテキストファイルを配列に代入
			File OnFileName = new File("./OnOff_EVENT/on_" + FileName + ".txt");
			BufferedReader br_on = new BufferedReader(new FileReader(OnFileName));
			String str_on = br_on.readLine();

			while(str_on != null){
				int_on = Integer.parseInt(str_on);
				onArray.add(int_on);

				str_on = br_on.readLine();
				on++;
			}

			//NoteOffの書き込まれたテキストファイルを配列に代入
			File OffFileName = new File("./OnOff_EVENT/off_" + FileName + ".txt");
			BufferedReader br_off = new BufferedReader(new FileReader(OffFileName));
			String str_off = br_off.readLine();

			while(str_off != null){
				int_off = Integer.parseInt(str_off);
				offArray.add(int_off);

				str_off = br_off.readLine();
				off++;
			}

			br_on.close();
			br_off.close();

			//EVENTとGATEを配列に代入
			int Array[][] = new int[on/2][2];
			int onArray2[][] = new int[on/2][2];
			int offArray2[][] = new int[off/2][2];

			System.out.println("Writing note\n["+ FileName + ".mid] >> ["+ FileName +".txt]");
			for(int a=0; a<on/2; a++){	// 二次配列に代入
				onArray2[a][0] = onArray.get(2*a);
				onArray2[a][1] = onArray.get(2*a+1);

				offArray2[a][0] = offArray.get(2*a);
				offArray2[a][1] = offArray.get(2*a+1);
			}

			for(int a=0; a<on/2; a++){
				Array[a][0] = onArray2[a][0];
				if(a < on/2-1){
					Array[a][1] = onArray2[a+1][1] - offArray2[a][1];
				}
			}
			CreateFile cf = new CreateFile();
			cf.createFile(Array, "./STEP/" + FileName);

			System.out.println("Done\n");

		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}

	}

}

