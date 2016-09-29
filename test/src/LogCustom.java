/**
 * Created by clear2020 on 2014/12/21.
 */

public class LogCustom {

    public float LogGetMax (int[] a)
    {
        int max = 0;
        double Logmax = 0;
        int i = 0;
        for(;i < a.length;i++){

            if(max < a[i]){

                max = a[i];
            }


        }

        Logmax = Math.log(max);

        return (float) Logmax;

    }


    public float LogGetAverage (int[] a){
        float average;
        int i =0;
        float sum = 0;
        average = 0;

        for(;i < a.length;i++){
            sum += a[i];

        }

        average = sum / a.length;

        return (float) Math.log(average);


    }

    public float[] LogCustom (int[] x){
        LogCustom lc = new LogCustom();


        int i = 0;
        float log_max = lc.LogGetMax(x);
        float log_ave = lc.LogGetAverage(x);

        float trance[] = new float[x.length];

        float temp = 0;

        for(;i < x.length;i++){
            temp = (float) Math.log(x[i]) - log_ave;
            trance[i] = (temp / log_max)*1000;

        }



        return trance;
    }
}
