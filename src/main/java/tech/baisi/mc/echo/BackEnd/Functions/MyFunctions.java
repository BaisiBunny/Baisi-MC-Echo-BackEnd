package tech.baisi.mc.echo.BackEnd.Functions;

import org.apache.catalina.mapper.Mapper;

import java.text.SimpleDateFormat;

public class MyFunctions {
    public static boolean IsLegalNameOrPassword(String name){ //a_97-z_122 A_65-Z_90 0_48-9_57 __95
        if(name.length() < 2 || name.length() > 20){
            return false;
        }

        boolean hasIllegalChar = false;
        for (char c : name.toCharArray()){
            if(!(((int)c >=97&& (int)c <=122)||((int)c >=65&& (int)c <=90)||((int)c >=48&& (int)c <=57)|| (int)c ==95)){
                hasIllegalChar = true;
            }
        }
        if(hasIllegalChar){
            return false;
        }

        return true;
    }


    private static String randomStringBase = "abcdefghijklmnpqrstuvwxyz0123456789";
    public static String GenerateRandomString(int length){
        String toReturn = "";
        for(int i = 0; i < length; i++){
            toReturn = toReturn.concat(String.valueOf(randomStringBase.charAt((int)(Math.random()*35))));
        }
        return toReturn;
    }

    public static String GetTimeNow(){
        Long time = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(time);
    }
}
