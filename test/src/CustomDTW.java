import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import edu.gatech.gart.ml.weka.DTW;

public class CustomDTW {




	public int GetMax (int[] a)
	{
		int max = 0;
		int i = 0;
		for(;i < a.length;i++){

			if(max < a[i]){

				max = a[i];
			}


		}

		return max;

	}


	public float GetAverage (int[] a){
		float average;
		int i =0;
		float sum = 0;
		average = 0;

		for(;i < a.length;i++){
			sum += a[i];

		}

		average = sum / a.length;

		return average;


	}

	public float[] CustomDTW (int[] x){
		CustomDTW cd = new CustomDTW(); 


		int i = 0;
		int d_max = cd.GetMax(x);
		float d_ave = cd.GetAverage(x);

		float trance[] = new float[x.length];

		float temp = 0;

		for(;i < x.length;i++){
			temp = (x[i] - d_ave)*100;
			trance[i] = temp / d_max;

		}



		return trance;
	}
}

