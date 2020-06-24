package com.example.jumpropetuts;

public class Workouts {
    public static String[] workouts ={"BASIC SKIP","ALTERNATE FOOT","CRISS CROSS","DOUBLE UNDER"};
    public static int[] workimgs = {R.drawable.basic_skip,R.drawable.alternate_foot,R.drawable.criss_cross,R.drawable.double_unders};
    public static String[] descs = {"1 to 2 minutes","30 seconds to 1.5 minutes","10 to 30 seconds","5 to 10 seconds"};
    public static  String[] videolinks = {"https://www.youtube.com/watch?v=FJmRQ5iTXKE&t=1s","https://www.youtube.com/watch?v=s_nuLdHYmBY","https://www.youtube.com/watch?v=BUouJzDSpJQ","https://www.youtube.com/watch?v=4tsT6hDB6Aw"};
    public static double[] met = {11.8,11.8,11.8,12.3};
    public static double calcCalories(double met,int weight,long time){
        double cal = (met*weight*3.5)/200;
        cal = (cal*time)/60;
        return  cal;
    }
}
