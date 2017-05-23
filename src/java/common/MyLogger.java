/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
/**
 *
 * @author Chappy
 */
public class MyLogger {
    public static String _LogFileName = "LoggerTester.log";
    
    private String LogPath = "LoggerTester.log";
    private String LogName = "LoggerTester.log";
    private static Logger _LOGGER = null;
    private FileHandler _fh = null;
    private int _log_level =0;
            
    public MyLogger(){
        _LOGGER = Logger.getLogger(this.getClass().getName());
    }
    
    public void putLogPath(String h_path){
        LogPath = h_path;
    }
    
    public void putLogName(String h_name){
        LogName = h_name;
        java.util.Date a_date = new java.util.Date();
        //String a_sRet = new SimpleDateFormat("yyyyMMdd").format(a_date);
        String a_sRet = new SimpleDateFormat("yyyyMM").format(a_date);
        _LogFileName = LogPath + a_sRet+ "_" + LogName;
        //_LogFileName = LogPath + "\\" + a_sRet+ "_" + LogName;
    }

    public void putLogLevel(int h_level){
        _log_level = h_level;
    }

    //詳細レベル（高）
    public void finest(String msg) {
        if (_log_level >= 7){
            StartLog(Level.FINEST); //[2016.06.02]
            _LOGGER.finest(msg);
            EndLog();
        }
    }

    //詳細レベル（中）
    public void finer(String msg) {
        if (_log_level >= 6){
            StartLog(Level.FINER);  //[2016.06.02]
            _LOGGER.finer(msg);
            EndLog();
        }
    }

    //詳細レベル（小）
    public void fine(String msg) {
        if (_log_level >= 5){
            StartLog(Level.FINE);   //[2016.06.02]
            _LOGGER.fine(msg);
            EndLog();
        }
    }

    //設定
    public void config(String msg) {
        if (_log_level >= 4){
            StartLog(Level.CONFIG); //[2016.06.02]
            _LOGGER.config(msg);
            EndLog();
        }
    }

    //情報
    public void info(String msg) {
        if (_log_level >= 3){
            StartLog(Level.INFO);   //[2016.06.02]
            _LOGGER.info(msg);
            EndLog();
        }
    }
    
    //警告
    public void warning(String msg) {
        if (_log_level >= 2){
            StartLog(Level.WARNING);    //[2016.06.02]
            _LOGGER.warning(msg);
            EndLog();
        }
    }

    //致命的
    public void severe(String msg) {
        if (_log_level >= 1){
            StartLog(Level.SEVERE); //[2016.06.02]
            _LOGGER.severe(msg);
            EndLog();
        }
    }

    //[2016.06.02]
    private void StartLog(Level h_level) {
        try {
            // 出力ファイルを追加モードで指定する
            _fh = new FileHandler(_LogFileName, true);
            // 出力フォーマットを指定する
            _fh.setFormatter(new java.util.logging.SimpleFormatter());
            _LOGGER.addHandler(_fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 出力レベルをCONFIG以上に設定する
        //_LOGGER.setLevel(Level.CONFIG);
        //_LOGGER.setLevel(Level.INFO);
        _LOGGER.setLevel(h_level);
    }
    
    private void EndLog(){
        _LOGGER.removeHandler(_fh);
        _fh.close();
    }
}
