/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package browser.termallod.utils;

import browser.termallod.constants.FileAndCategory;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author elahi
 */
public class TbxUtil implements FileAndCategory{

    private static String filepath = GENTERM_PATH + "tbx/";

    public static void main(String[] args) throws IOException, Exception {
        String[] fileList = new File(filepath).list();
        TbxUtil tbxUtil = new TbxUtil();
        for (String fileName : fileList) {
            String contents = FileUtils.readFileToString(new File(filepath + fileName), "UTF-8");
            String modified=tbxUtil.filereadAndWrite(fileName, contents);
            System.out.println(modified);
            break;
        }

    }

    private String filereadAndWrite(String fileName, String fileString) throws FileNotFoundException, IOException {
        String fileStringModified = "";
        Integer index = 1;
        //File fout = new File(filepath +fileName.replace(".xml", "_corrected.xml"));
        //BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fout.getAbsolutePath()), StandardCharsets.UTF_8));
        System.out.println(fileString);

        try {
            Scanner scanner = new Scanner(fileString);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //System.out.println(line);

                if (line.contains("<termEntry>")) {
                    line = line.replace("<termEntry>", "< t e r m E n t r y  " + "id=" + "\"" + "GENTERM" + (index++) + "\"" + "> ");
                    line = line + "\n";
                    System.out.println(line);

                }

                fileStringModified += line;
                //out.write(fileStringModified);

            }

            scanner.close();
            //out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileStringModified;

    }

}
