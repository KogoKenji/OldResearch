import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateFile {
//	テキストファイルの生成
	public void createFile(int array[][], String fileName){
		try{
			File file = new File(fileName +".txt");
			file.createNewFile();

			if (checkBeforeWritefile(file)){
				PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));

				for(int a=0; a<array.length; a++){
					pw.println(array[a][0]);
					pw.println(array[a][1]);
				}

				pw.close();

			}else{
				System.out.println("Cannot write notes in text file");
			}

		}catch(IOException e){
			System.out.println(e);
		}
	}

	private static boolean checkBeforeWritefile(File file){
		if (file.exists()){
			if (file.isFile() && file.canWrite()){
				return true;
			}
		}
		return false;
	}

}
