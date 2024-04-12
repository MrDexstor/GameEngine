package ru.gameengine.GamesAPI.data;


import java.util.ArrayList;

public class PackedStoryData {
    public String id = "";
    public String compiler = "plotter";
    public String version = "3";
    
    public ArrayList<PackedScriptData> scripts = new ArrayList<PackedScriptData>();
    public ArrayList<PackedLibData> libs = new ArrayList<PackedLibData>();
}
