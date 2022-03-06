package com.houcy7;

import com.houcy7.action.Download;
import com.houcy7.action.Generate;

/**
 * @author hou
 */
public class Application {

    public static void main(String... args) throws Exception {
        String arg = args[0];
        switch (arg){
            case "download":
                Download.download();
                break;
            case "generate":
                Generate.generate();
                break;
        }
    }
}
