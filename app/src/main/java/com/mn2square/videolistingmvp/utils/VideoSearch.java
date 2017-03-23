package com.mn2square.videolistingmvp.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nitinagarwal on 3/14/17.
 */

public class VideoSearch {

    public static ArrayList<String> SearchResult(String searchQuery, List<String> videoList)
    {

        searchQuery=searchQuery.toLowerCase();
            ArrayList<String> newList= new ArrayList<>();
            for(String fileName:videoList){
                String tempFileName=fileName;
                if(fileName.contains(File.separator)){
                    int index=fileName.lastIndexOf(File.separator);
                    tempFileName=fileName.substring(index,fileName.length());
                }
                if(tempFileName.toLowerCase().contains(searchQuery)){
                    newList.add(fileName);
                }
            }

            return newList;
    }

    public static HashMap<String, List<String>> SearchResult(String searchQuery, HashMap<String, List<String>> folderListHashMap)
    {
        HashMap<String, List<String>> result = new HashMap<>();
        for(String key:folderListHashMap.keySet()) {
            searchQuery = searchQuery.toLowerCase();
            List<String> fileNames = folderListHashMap.get(key);
            List newList = new LinkedList();
            for (String fileName : fileNames) {
                String tempFileName = fileName;
                if (fileName.contains(File.separator)) {
                    int index = fileName.lastIndexOf(File.separator);
                    tempFileName = fileName.substring(index, fileName.length());
                }
                if (tempFileName.toLowerCase().contains(searchQuery)) {
                    newList.add(fileName);
                }

                if(!newList.isEmpty())
                {
                    result.put(key, newList);
                }
            }
        }
        return result;
    }
}
