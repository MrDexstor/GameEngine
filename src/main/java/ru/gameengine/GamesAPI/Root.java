package ru.gameengine.GamesAPI;

import net.minecraft.world.storage.FolderName;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import ru.gameengine.Utils.LogUtils;
import ru.gameengine.GamesAPI.Utils.FileManager;
import ru.gameengine.GamesAPI.Utils.Filters;
import ru.gameengine.GamesAPI.data.FSData;
import ru.gameengine.GamesAPI.data.PackedLibData;
import ru.gameengine.GamesAPI.data.PackedScriptData;
import ru.gameengine.GamesAPI.data.PackedStoryData;

import java.io.File;
import java.io.OutputStreamWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
    public class Root {

        public static HashMap<String, Game> storiesList = new HashMap<String, Game>();

        public static Game tickingStory;
        public static int ticks = 0;

        @SuppressWarnings("all")
        public static void reloadStories() {

            storiesList.clear();

            Path worldPath = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldPath(FolderName.ROOT);
            File worldFolder = worldPath.toFile();
            File storiesFolder = new File(worldFolder, "ge_games");
            storiesFolder.mkdir();

            File buildFolder = new File(worldFolder, "ge_builds");
            buildFolder.mkdir();

            LogUtils.sendLog(LogUtils.udcRombus + "Reloading stories...");

            File[] storyFolders = storiesFolder.listFiles(Filters.onlyDir);
            for (File story : storyFolders)
                addStory(story);

            File[] buildsFolders = buildFolder.listFiles(Filters.onlyStory);
            for (File build : buildsFolders)
                unbuildStory(build);

            tickingStory = getActiveStory();
        }

        public static void buildStory();

        public static void unbuildStory(File f) {
            try {
                Path worldPath = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldPath(FolderName.ROOT);
                File worldFolder = worldPath.toFile();
                File storiesFolder = new File(worldFolder, "ge_game");
                storiesFolder.mkdir();
                PackedStoryData data = FileManager.jsonToJava(f, PackedStoryData.class);
                String storyId = f.getName();
                File storyFolder = new File(storiesFolder, storyId);
                storyFolder.mkdir();
                File scenesFolder = new File(storyFolder, "scene");
                scenesFolder.mkdir();
                File scriptsFolder = new File(storyFolder, "js");
                scriptsFolder.mkdir();
                File libsFolder = new File(storyFolder, "lib");
                libsFolder.mkdir();

                data.scenes.forEach((json) -> FileManager.javaToJson(new File(scenesFolder, json.id + ".json"), json));
                data.scripts.forEach((scr) -> {
                    try {
                        File fscr = new File(scriptsFolder, scr.id);
                        fscr.createNewFile();
                        OutputStreamWriter writer = FileManager.createOutput(fscr);
                        writer.write(scr.content);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                data.libs.forEach((lib) -> {
                    try {
                        File fscr = new File(libsFolder, lib.id);
                        fscr.createNewFile();
                        OutputStreamWriter writer = FileManager.createOutput(fscr);
                        writer.write(lib.content);
                        writer.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

                Game story = new Game(storyId, storyFolder);
                story.isBuilded = true;
                if (
                        data.compiler == PlotterEnvironment.envId &&
                                data.version == PlotterEnvironment.version
                ) addStory(story);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public static void tick() {
            if (ticks % 100 == 0) tickingStory = getActiveStory();
            if (tickingStory != null) tickingStory.tick();
            ticks++;
        }

        public static void setActiveStory(String id) {
            FSData root = getRootData();
            root.selectedStory = id;
            writeRootData(root);
        }

        public static Game getActiveStory() {
            FSData root = getRootData();
            return storiesList.get(root.selectedStory);
        }

        public static FSData getRootData() {
            Path worldPath = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldPath(FolderName.ROOT);
            File worldFolder = worldPath.toFile();
            File fsDataFile = new File(worldFolder, "gameengine.json");
            try {
                return FileManager.jsonToJava(fsDataFile, FSData.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return new FSData();
        }

        public static void writeRootData(FSData data) {
            Path worldPath = Objects.requireNonNull(ServerLifecycleHooks.getCurrentServer()).getWorldPath(FolderName.ROOT);
            File worldFolder = worldPath.toFile();
            File fsDataFile = new File(worldFolder, "gameengine.json");
            FileManager.javaToJson(fsDataFile, data);
        }

        public static boolean addStory(File storyFile) {
            if (storyFile.getName().endsWith(".game")) return false;
            Game story = new Game(storyFile.getName(), storyFile);
            createDir(storyFile, "act");
            createDir(storyFile, "lib");
            createDir(storyFile, "js");
            File dataF = createDir(storyFile, "data");
            createDir(dataF, "player");
            createDir(dataF, "script");
            createDir(storyFile, "scene");
            storiesList.put(storyFile.getName(), story);
            buildStory(story);
            LogUtils.sendLog(LogUtils.udcRFaceArrow + LogUtils.udcRFaceArrow + "Loaded story: " + story.storyId.toUpperCase());
            return true;
        }

        public static boolean addStory(Game story) {
            File storyFile = story.storyFolder;
            createDir(storyFile, "act");
            createDir(storyFile, "lib");
            createDir(storyFile, "js");
            File dataF = createDir(storyFile, "data");
            createDir(dataF, "player");
            createDir(dataF, "script");
            createDir(storyFile, "scene");
            storiesList.put(storyFile.getName(), story);
            LogUtils.sendLog(LogUtils.udcRFaceArrow + LogUtils.udcRFaceArrow + "Loaded story: " + story.storyId.toUpperCase());
            return true;
        }

        public static File createDir(File file, String dir) {
            File d = new File(file, dir);
            d.mkdir();
            return d;
        }

    }
