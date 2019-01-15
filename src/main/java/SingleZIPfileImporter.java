import com.google.common.base.Stopwatch;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SingleZIPfileImporter {
    static Logger loggerSuccess = LoggerFactory.getLogger("successfullLogger");
    static Logger loggerException = LoggerFactory.getLogger("exceptionLogger");
    static String nl = System.getProperty("line.separator");


    public static void importToDSPaceFilebyFile(String importFolder, String dspaceBinFolderPath) throws IOException {
        //String dirName = "C:/Users/gruber/Desktop/dSpaceImportFolder";
        //String dspaceBinFolderPath = "C:/idedspace/bin";
        String[] extensions = new String[1];
        extensions[0] = "zip";

        //List<File> files = (List<File>) FileUtils.listFiles(new File(importFolder), extensions, false);

        Collection<File> files = FileUtils.listFiles(new File(importFolder), extensions, false);

        int proccessedFilesCounter = 1;
        int numFiles = files.size();

        System.out.println("********* Starting import to DSpace ***********");
        System.out.println("There are " + numFiles + " zip files in folder: " + importFolder);



        for (File file : files) {

            String dspaceImportCommand = "./dspace packager -e julius.gruber@tuwien.ac.at  -t AIP --submit -w  -o validate=false ";
            String pathImportZIP = importFolder + file.getName();
            //ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "cd " + dspaceBinFolderPath + " && " + dspaceImportCommand + pathImportZIP);
            //ProcessBuilder builder = new ProcessBuilder( "cd " + dspaceBinFolderPath + " && " + dspaceImportCommand + pathImportZIP);
            //ProcessBuilder builder = new ProcessBuilder( "cd " + dspaceBinFolderPath );
            ProcessBuilder builder = new ProcessBuilder(    "/bin/bash", "-c", "cd " + dspaceBinFolderPath + " && " + dspaceImportCommand + pathImportZIP);




                    //builder.redirectErrorStream(true);

            System.out.println(file.getName());

            Stopwatch stopwatch = Stopwatch.createStarted();
            Process p = builder.start();

            BufferedReader readerSuccess = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader readerException = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            StringBuffer stringBufferSucess = new StringBuffer();
            StringBuffer stringBufferException = new StringBuffer();
            String lineSuccess;
            String lineException;

            boolean fileImportSucessfull = false;

            while (true) {
                lineSuccess = readerSuccess.readLine();



                if(! (lineSuccess == null) && !lineSuccess.contains("Using DSpace installation in")){
                    //System.out.println("contains nl escape sequence: " + line.contains(nl));
                    stringBufferSucess.append(lineSuccess+nl);
                }

                if(! (lineSuccess == null) && lineSuccess.contains("CREATED new DSpace ITEM")){
                    fileImportSucessfull = true;

                }



                if (lineSuccess == null) {
                    long timeElapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
                    stringBufferSucess.insert(0,"******************************************Processing file number "+proccessedFilesCounter + " of " +numFiles+
                            " files. Filename: "+ file.getName() + "***************************************************************" );
                    stringBufferSucess.append("Time needed for import in MilliSeconds: "+ timeElapsed+nl);
                    loggerSuccess.warn(stringBufferSucess.toString());


                    break;
                }


            }



            while(true && !fileImportSucessfull ){
                lineException = readerException.readLine();
                if(! (lineException == null) ){

                    stringBufferException.append(lineException+nl);
                }
                if(lineException == null ) {
                    stringBufferException.insert(0, " ************************************** Exception for file: "+file.getName() + ". File number "+proccessedFilesCounter + " of " +numFiles+" ******************************************" +
                            "**********************************************************"+nl);
                    loggerException.warn(stringBufferException.toString());
                    break;
                }



            }

            proccessedFilesCounter++;
        }
    }
}
