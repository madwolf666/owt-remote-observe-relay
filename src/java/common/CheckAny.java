/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;

/**
 *
 * @author Chappy
 */
public class CheckAny {
    private static final String _OS_NAME = System.getProperty("os.name").toLowerCase();
    
    public static boolean isLinux() {
        return _OS_NAME.startsWith("linux");
    }

    public static boolean isMac() {
        return _OS_NAME.startsWith("mac");
    }

    public static boolean isWindows() {
        return _OS_NAME.startsWith("windows");
    }

    public static boolean isSunOS() {
        return _OS_NAME.startsWith("sunos");
    }
    
}
