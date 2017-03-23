package com.mn2square.videolistingmvp.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by nitinagarwal on 3/15/17.
 */

public class FolderListGenerator {

    public static void generateFolderHashMap(ArrayList<String> videoList, HashMap<String, List<String>> folderListHashMap)
    {
        String videoFullPath;
        String folderName;

        for (int i = 0; i < videoList.size(); i++) {
            videoFullPath = videoList.get(i);
            if(videoFullPath.lastIndexOf('/') > 0)
            {
                folderName = videoFullPath.substring(0, videoFullPath.lastIndexOf('/'));
            }
            else
            {
                folderName = "";
            }

            if(folderListHashMap.get(folderName) == null)
            {
                ArrayList<String> innerFolderVideosList = new ArrayList<String>();
                innerFolderVideosList.add(videoFullPath);
                folderListHashMap.put(folderName, innerFolderVideosList);
            }
            else
            {
                List<String> innerFolderVideosList = folderListHashMap.get(folderName);
                innerFolderVideosList.add(videoFullPath);
            }
        }
    }

    public static List<String> getSavedVideoListFromFolderHashMap(HashMap<String, List<String>> folderListHashMap)

    {
        Set<String> folderNames = folderListHashMap.keySet();
        for(String key: folderListHashMap.keySet())
        {
            if(key.endsWith("/VSMP"))
                return folderListHashMap.get(key);
        }
        return null;
    }

}
