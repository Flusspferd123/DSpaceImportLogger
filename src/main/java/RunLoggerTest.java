import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RunLoggerTest {

    public static void main(String[] args) throws IOException {
        Logger logger = LoggerFactory.getLogger("fileLogger");
     /*   logger.warn("This is a test - and anything test dead man - ninjaman a say");
        System.out.println("Wrote something to log file");*/

        //String importFolder =  "C:/Users/gruber/Desktop/dSpaceImportFolder/";
        String importFolder = "/home/tiss/jgruber/dSpaceImportFolder/";
        //DSpace 6
        //String dspaceBinFolderPath = "/opt/dspace/dspacework/bin";
        //DSpace 7
        String dspaceBinFolderPath = "/opt/dspace7/dspace7work/bin";
        SingleZIPfileImporter.importToDSPaceFilebyFile(importFolder,dspaceBinFolderPath );


    }
}
