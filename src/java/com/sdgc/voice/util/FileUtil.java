/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sdgc.voice.util;



import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.Part;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author ranjeetr
 */
public class FileUtil {

    
    

    /*
     * writeFileForTP(Collection<Part> fileGroup, UserInfo uInfo) will write
     * file on file system for Third part training and validation
     */

 

    public String writeAudioFiles(Collection<Part> fileGroup, String email) throws IOException {

        Path path = createFolder(email);
        String userFolderLoc = path.toString();
        System.out.println("path.getParent().toString()::" + path.getParent().toString());
        
        //int index = 1;
        String strFileNameInSource = "";
        System.out.println("FileGroup isize in writeAudioFiles::" + fileGroup.size());
        for (Part p : fileGroup) {
            if (p.getContentType() != null) {
                System.out.println("p.getContentType:::" + p.getContentType());
                /*
                code commented as no separation required between enrol and update process as on 10th July2017
                if(uInfo.getCurrentProcess().equals(VoiceAuthConstant.ACTION_TYPE_ENROL)){
                strFileNameInSource = getFileName(p);
                }else if(uInfo.getCurrentProcess().equals(VoiceAuthConstant.ACTION_TYPE_UPDATE)){
                    int newIndex = nextSuffixInt(userFolderLoc);
                    strFileNameInSource = String.valueOf(newIndex);                    
                }
                 */
                strFileNameInSource = getFileName(p);
                System.out.println("strFileNameInSource:::" + strFileNameInSource);
                String currentFileName = writeFilePart(p, removeFileExtn(strFileNameInSource), path);
                
            }
        }
        
       return userFolderLoc;

    }

   

    

   
    public String removeFileExtn(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf('.') == -1 ? fileName.length() : fileName.lastIndexOf('.'));
    }

   

   

   

    private Path createFolder(String email){

        String mainFolderLoc = Config.getProps().getProperty("voiceAuth.usersvoice.location");
        if(mainFolderLoc==null || mainFolderLoc.isEmpty())
            mainFolderLoc="D:\\UserVoices";

        String userNameFolder = email;//.replace("@", "_");
        String userFolder = mainFolderLoc + File.separatorChar + userNameFolder;

        System.out.println("userNameFolder" + userNameFolder);
       
        Path userPath = Paths.get(userFolder);
        Path newPath = userPath;
        
        //if directory exists?
        if (!Files.exists(userPath)) {
            try {
                
                System.out.println("UserPath in createFolder::" + userPath.toString());
                newPath = Files.createDirectories(userPath);
                System.out.println("newPath in createFolder::" + newPath.toString());
                

            } catch (IOException e) {
                //fail to create directory
                System.err.println(e.getMessage());
            }
        }
        return newPath;

    }

   



    

    
    public List<String> getListOfDirectories(String mainDirectoryLoc) {
        File file = new File(mainDirectoryLoc);
        String[] directories = file.list(new FilenameFilter() {

            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isDirectory();
            }
        });
        List<String> listOfDirectories = new ArrayList<String>();
        if (directories != null && directories.length > 0) {
            listOfDirectories.addAll(Arrays.asList(directories));
        }
        return listOfDirectories;
    }

    public List<String> getListOfFiles(String mainDirectoryLoc) {
        File file = new File(mainDirectoryLoc);
        String[] directories = file.list(new FilenameFilter() {

            @Override
            public boolean accept(File current, String name) {
                return new File(current, name).isFile();
            }
        });
        List<String> listOfFiles = new ArrayList<String>();
        if (directories != null && directories.length > 0) {
            listOfFiles.addAll(Arrays.asList(directories));
        }
        return listOfFiles;
    }

    private static boolean canCreateFolder(List<String> list, int x) {
        boolean flag = true;
        if (list.size() >= x) {
            flag = false;
        }
        return flag;
    }

    public List<File> getDirectoryContents(File dir) {
        List<File> allfiles = new ArrayList<File>();
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    getDirectoryContents(file);
                } else {
                    allfiles.add(file);
                }
            }
        } catch (Exception e) {
            System.out.println("Excetpion occoured");
        }

        return allfiles;
    }

    public int nextSuffixInt(String directoryPath) {
        int x = 1, i = 0;
        List<String> listOfFiles = getListOfFiles(directoryPath);
        if (listOfFiles.size() == 0) {
            return x;
        }
        List<Integer> indexList = new ArrayList<Integer>();
        for (String str : listOfFiles) {

            str = str.substring(0, str.lastIndexOf('.'));
            String[] tokens = str.split("(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)");

            String strIndex = tokens[tokens.length - 1];
            try {
                indexList.add(Integer.parseInt(strIndex));
            } catch (Exception ignore) {
            }
            System.out.println("strIndex:::::" + strIndex);

        }
        indexList.sort(null);

        // index[i]=Integer.parseInt(strIndex);
        x = indexList.get(indexList.size() - 1) + 1;

        return x;
    }

    private static String getNextFolderName(String lastFolderName) {
        if (lastFolderName != null && lastFolderName.equals("")) {
            return "model1";
        }
        String[] tokens = lastFolderName.split("(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)");
        String str = tokens[tokens.length - 1];
        Integer newSuffix = Integer.parseInt(str) + 1;
        int len = str.length();
        String tempStr = lastFolderName.substring(0, lastFolderName.length() - len);
        String newFolderName = tempStr + newSuffix;
        System.out.println(newFolderName);
        return newFolderName;
    }

    private static String getPreviousFolderName(String lastFolderName) {
        String[] tokens = lastFolderName.split("(?<=\\d)(?=\\D)|(?=\\d)(?<=\\D)");
        String str = tokens[tokens.length - 1];
        Integer newSuffix = Integer.parseInt(str) - 1;
        int len = str.length();
        String tempStr = lastFolderName.substring(0, lastFolderName.length() - len);
        String newFolderName = tempStr + newSuffix;
        System.out.println(newFolderName);
        return newFolderName;
    }

    private String getFileName(final Part part) {
        final String partHeader = part.getHeader("content-disposition");
        for (String content : part.getHeader("content-disposition").split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(
                        content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    private String getFileExtension(final Part part) {
        String fileName = getFileName(part);
        return fileName.substring(fileName.indexOf(".") + 1).trim().replace("\"", "");
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.indexOf(".") + 1).trim().replace("\"", "");
    }

    private String writeFilePart(final Part part, String name, final Path path) {
        OutputStream out = null;
        InputStream filecontent = null;
        String fileExt = getFileExtension(part);
        System.out.println("File extension discovered from received file is::" + fileExt);
        String fileName = name + ".wav";
        try {
            File f1 = new File(path + File.separator
                    + fileName);
            out = new FileOutputStream(f1);
            filecontent = part.getInputStream();
            byte[] fileBase64=IOUtils.toByteArray(filecontent);
            //System.out.write(fileBase64);
            byte[] wavData = new Base64Util().decode(fileBase64);
            out.write(wavData);
//            int read;
//            final byte[] bytes = new byte[1024];
//
//            while ((read = filecontent.read(bytes)) != -1) {
//                out.write(bytes, 0, read);
 //           }
        } catch (Exception fne) {
            System.err.println("Error in writing file::" + fne.getMessage());

        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            } catch (Exception exe) {
                System.err.println("ignore::" + exe.getMessage());
            }
        }

        return fileName;
    }

    void writeFile(String path, List<String> lst, String fileName) {
        FileWriter fW = null;
        try {
            File f1 = new File(path + File.separator
                    + fileName);
            fW = new FileWriter(f1);
            for (String str : lst) {
                fW.write(str + "\n");
            }
        } catch (Exception fne) {
            System.err.println("Error in writing traing files::" + fne.getMessage());

        } finally {
            try {
                if (fW != null) {
                    fW.close();
                }

            } catch (Exception exe) {
                System.err.println("ignore::" + exe.getMessage());
            }
        }
    }

   

  

  
    private static void copyFolderToFolder(String srcDirPath, String destDirPath) throws IOException {
        File srcDir = new File(srcDirPath);
        File destDir = new File(destDirPath);
        FileUtils.copyDirectory(srcDir, destDir, null, false);
    }

    private static void copyDefaultFolderTo(File destDir) throws IOException {
        String parentFolder = Config.getProps().getProperty("voiceAuth.systemVoice.location");
        System.out.println("In copy default folder to:: source folder" + parentFolder);
        File srcDir = new File(parentFolder);

        FileUtils.copyDirectory(srcDir, destDir, null, false);

    }

   

    void deleteFolder(String folderPath) {
        System.out.println("folderPath in deleteFolder::" + folderPath);
        FileUtils.deleteQuietly(new File(folderPath));
    }

    private static void copyDefaultFileTo(Path path) throws IOException {
        String parentFolder = Config.getProps().getProperty("voiceAuth.trainFile.location");
        Path sourcePath = Paths.get(parentFolder);
        Files.copy(sourcePath, path.resolve(sourcePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }
}
