import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class getGate{

	public void toArray(String fName){
		ArrayList<Integer> onArray = new ArrayList<Integer>();
		ArrayList<Integer> offArray = new ArrayList<Integer>();
		int on = 0;
		int off = 0;
		int int_on = 0;
		int int_off = 0;

		try{
			//NoteOnの書き込まれたテキストファイルを配列に代入
			File OnFileName = new File("./OnOff_EVENT/on_" + fName + ".txt");
			BufferedReader br_on = new BufferedReader(new FileReader(OnFileName));
			String str_on = br_on.readLine();

			while(str_on != null){
				int_on = Integer.parseInt(str_on);
				onArray.add(int_on);

				str_on = br_on.readLine();
				on++;
			}

			//NoteOffの書き込まれたテキストファイルを配列に代入
			File OffFileName = new File("./OnOff_EVENT/off_" + fName + ".txt");
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
			System.out.println("Writing note\n["+ fName + ".mid] >> ["+ fName +".txt]");
			for(int a=0; a<on/2; a++){
				Array[a][0] = onArray.get(2*a);
				Array[a][1] = offArray.get(2*a+1) - onArray.get(2*a+1);
			}


			CreateFile cf = new CreateFile();
			cf.createFile(Array, "./EVENT/" + fName);

			onArray.clear();
			offArray.clear();
			System.out.println("Done\n");

		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}

	}

}
