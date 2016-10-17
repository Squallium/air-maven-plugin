package com.squallium;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;
import java.util.Iterator;

/**
 * Says "Hi" to the user.
 */
@Mojo(name = "sayhi")
public class MyMojo extends AbstractMojo {

    /**
     * The greeting to display.
     */
    @Parameter(property = "sayhi.greeting", defaultValue = "Hello World!")
    private String greeting;

    /**
     * @parameter
     *     default-value="log.txt"
     *     expression="${environment.filename}"
     */
    private String fileName;

    /**
     * @parameter
     *     default-value="C:\\temp"
     *     expression="${environment.base_dir}"
     */
    private File baseDirectory;

    /**
     * @parameter
     *     default-value="true"
     *     expression="${environment.loggingRequired}"
     */
    private boolean loggingRequired;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Hello, world.");

        StringBuilder fileContents = new StringBuilder();

        Map<String, String> environment = System.getenv();
        Iterator<Map.Entry<String, String>> entries = environment.entrySet().iterator();

        fileContents.append("Environment Information:" + newLine());
        fileContents.append("-----------------------------------------------------------" + newLine());

        while (entries.hasNext()){

            Map.Entry<String, String> entry = entries.next();
            fileContents.append(entry.getKey() + "------------->" + entry.getValue() + newLine());
        }

        fileContents.append("-----------------------------------------------------------" + newLine());
        writeToFile(fileContents);
        getLog().info(fileContents);
        getLog().info("File contents written");
    }

    private void log(String message){

        if (loggingRequired){
            System.out.println(message);
        }
    }

    private static String newLine(){
        return System.getProperty("line.separator");
    }

    private void writeToFile(StringBuilder fileContents){

        FileWriter fWriter = null;
        BufferedWriter bWriter = null;

        try{
            fWriter = new FileWriter(baseDirectory + File.separator + fileName);
            bWriter = new BufferedWriter(fWriter);
            bWriter.write(fileContents.toString());
        }catch (Exception e){
            log("Error in writing the contents to file ->" + e.getMessage());
        }finally{
            try{
                if (bWriter != null){
                    bWriter.close();
                }
                if (fWriter != null){
                    fWriter.close();
                }
            }catch (Exception e){
                log("Error in closing the resources ->" + e.getMessage());
            }
        }
    }

}
