package hierarchy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/*

        Name: Davit Morgoshia
        Date: 18/05/2022

        Purpose of Program: Print hierarchy of given directory with
            file/folder size;

*/

public class DirectoryStructure {

    private static final String BYTES = " bytes";

//    returns hierarchy including root file
    private static String currentDirectory(Path path) throws IOException {

        String directoryPath = path.toString();
        String[] rootFil = directoryPath.split("\\\\");
        String hierarchy;
        hierarchy = rootFil[rootFil.length - 1] + " " + getFolderSize(new File(directoryPath)) + BYTES + "\n";
        List<String> states = new ArrayList<>();
        try {
            hierarchy = hierarchy + listDirectoryContents(directoryPath, states);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hierarchy;
    }

//    return hierarchy without root file
    private static String listDirectoryContents(String path, List<String> states) throws IOException {

        StringBuilder sb = new StringBuilder();

        String[] contents = new File(path).list();

        contents = sortContents(contents);

        for (String fileEntry : contents) {

            StringBuilder prefixBuilder = new StringBuilder();

            if (fileEntry.equals(contents[contents.length - 1])) { // bolo shvili
                states.add("   ");
            } else { // ara bolo shvili
                states.add("│  ");
            }

            for (int i = 0; i < states.size() - 1; i++) {
                prefixBuilder.append(states.get(i));
            }

            if (states.get(states.size() - 1).equals("   ")) {
                prefixBuilder.append("└─ ");
            } else {
                prefixBuilder.append("├─ ");
            }

            if (!fileEntry.contains(".")) {

                File f = new File(path, fileEntry);
                sb.append(prefixBuilder)
                        .append(fileEntry)
                        .append(" ")
                        .append(getFolderSize(f))
                        .append(BYTES)
                        .append("\n")
                        .append(listDirectoryContents(path + "\\" + fileEntry, states));


            } else {

                Path p = Paths.get(path + "\\" + fileEntry);

                sb.append(prefixBuilder)
                        .append(fileEntry)
                        .append(" ")
                        .append(Files.size(p))
                        .append(BYTES)
                        .append("\n");
            }

            states.remove(states.size() - 1);
        }

        return sb.toString();
    }

//    sorting contents of folder to ensure all directories are ahead of files
    private static String[] sortContents(String[] contents) {
        for (int i = 1; i < contents.length; i++) {
            if (!contents[i].contains(".")) {
                int j = i;
                while (j > 0 && contents[j - 1].contains(".")) {
                    String tmp = contents[j - 1];
                    contents[j - 1] = contents[j];
                    contents[j] = tmp;
                    j--;
                }
            }
        }
        return contents;
    }

//    returns folder size
    private static long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();

        assert files != null;
        for (File file : files) {
            if (file.isFile()) {
                length += file.length();
            } else {
                length += getFolderSize(file);
            }
        }
        return length;
    }

}
