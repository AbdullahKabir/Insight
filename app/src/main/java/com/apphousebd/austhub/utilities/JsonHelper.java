package com.apphousebd.austhub.utilities;

import android.content.Context;
import android.util.Log;

import com.apphousebd.austhub.dataBase.RoutineDatabase;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineServerJsonModel;
import com.apphousebd.austhub.dataModel.routineDataModel.RoutineTableConstants;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Asif Imtiaz Shaafi on 07-Dec-16.
 * Email: a15shaafi.209@gmail.com
 */

public class JsonHelper {

    private static final String TAG = "JsonHelper";

    public static String encodeToJson(Context context, List<RoutineServerJsonModel> routineStructure) {

        Routines routines = new Routines();
        routines.setRoutines(routineStructure);

        Gson gson = new Gson();

        String result =
                gson.toJson(routines);

        Log.i(TAG, "encodeToJson: \n" + result);
//
//        //adding to database
//        RoutineDatabase routineDatabase = new RoutineDatabase(context);
//
//        routineDatabase.openDatabase();
//
//        //delete all first
//        routineDatabase.deleteAllItem();
//
//        if (routineDatabase.getTableItemCount(RoutineTableConstants.TABLE_NAME) == 0)
//            for (RoutineServerJsonModel structure : routineStructure) {
////                routineDatabase.insertItem(structure);
//            }
//
//        routineDatabase.closeDatabase();

        return result;
    }

    public static void decodeJsonData(Context context, String jsonString) {
        Routines routines;

        RoutineDatabase source = new RoutineDatabase(context);
        source.openDatabase();


        //decoding
        Gson gson = new Gson();
        routines = gson.fromJson(jsonString, Routines.class);

        Log.i("routine", "decodeJsonData: " + routines);
        if (routines != null && routines.getRoutines().size() > 0) {

            source.deleteAllItem();

            int i = 0;
            for (RoutineServerJsonModel r : routines.getRoutines()) {
                Log.i(TAG, "decodeJsonData: " + (i++) + ": " + r);
            }

            if (source.getTableItemCount(RoutineTableConstants.TABLE_NAME) == 0) {
                for (RoutineServerJsonModel s : routines.getRoutines()) {
                    Log.i("routine", s.toString() + "\n");

                    source.insertItem(s);
                }
            }
        }
        source.closeDatabase();

    }

    private static class Routines {
        List<RoutineServerJsonModel> mRoutines;

        public Routines() {
        }

        List<RoutineServerJsonModel> getRoutines() {
            return mRoutines;
        }

        public void setRoutines(List<RoutineServerJsonModel> routines) {
            mRoutines = routines;
        }
    }

}
