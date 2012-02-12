/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 *
 * @author why
 */
public class JavaApplication1 {

    /**
     * @param args the command line arguments
     * @throws ClientProtocolException
     * @throws IOException
     * @throws ParseException
     * @throws Exception
     */
    public static void main(String[] args) throws ClientProtocolException, IOException, ParseException, Exception {
        // TODO code application logic here
        System.out.println("Hello Java");
        connDB();

        for (int i = Integer.parseInt(args[1]); i < Integer.parseInt(args[2]); i++) {
            String form_url = NewClass.readKey("form_url");
            getResponse( form_url + "forum.php?mod=forumdisplay&fid=" + args[0] + "&page=" + i);
        }
        if (!conn.isClosed()) {
            System.out.println("Succeeded close the Database!");
            conn.close();
        }
    }

    //下载
    public static void getResponse(String url) throws Exception {

        // (1) 创建HttpGet实例
        HttpGet get = new HttpGet(url);
        System.out.println(get.getURI().toString());
        get.setHeader("User-agent", "Mozilla/4.0");
        get.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");

        // (2) 使用HttpClient发送get请求，获得返回结果HttpResponse
        HttpClient http = new DefaultHttpClient();
        HttpResponse response = http.execute(get);

        // (3) 读取返回结果
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            readResponse(in);
        }
    }

    //得到字符串
    public static void readResponse(InputStream in) throws Exception {

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "gb2312"));
        String line = null;
        String s = null;
        while ((line = reader.readLine()) != null) {
            s += line;
            //System.out.println(regexString(line));
        }
        getIdTitleStr(s);
        //System.out.println(s);
    }

    //正则
    public static void getIdTitleStr(String htmlStr) throws SQLException, UnsupportedEncodingException {

        Pattern p_1;
        Matcher m_1;
        //List<String> idtitles = new ArrayList<String>();
        String regEx_1 = "<a href=\".*?tid=(\\d+)&amp;.*?\" .+? class=\"xst\" >(.*?)</a>"; //图片链接地址
        p_1 = Pattern.compile(regEx_1, Pattern.CASE_INSENSITIVE);
        m_1 = p_1.matcher(htmlStr);
        int i = 0;
        while (m_1.find()) {
            insertDB(m_1.group(1), m_1.group(2));
            i++;
        }
        System.out.println("," + i);
    }

    public static void insertDB(String id, String title) throws SQLException, UnsupportedEncodingException {

        String sql = "insert ignore into news(tid, title) values(?, ?)";//
        PreparedStatement preparedStmt = conn.prepareStatement(sql);
        preparedStmt.setString(1, id);
        preparedStmt.setString(2, title);
        preparedStmt.execute();
        System.out.print(preparedStmt.getUpdateCount());
    }
    /**
     *
     */
    public static Connection conn = null;

    public static void connDB() throws ClassNotFoundException, SQLException {


        String server = NewClass.readKey("server");
        String port = NewClass.readKey("port");
        String database = NewClass.readKey("database");
        String dbuser = NewClass.readKey("dbuser");
        String dbpw = NewClass.readKey("dbpw");

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://" + server + ":" + port + "/" + database + "?characterEncoding=gb2312";
        String user = dbuser;
        String password = dbpw;

        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
        if (!conn.isClosed()) {
            System.out.println("Succeeded connecting to the Database!");
            Statement statement = conn.createStatement();
            statement.execute("SET NAMES gb2312");
        }
    }

    public static String regexString(String s) throws Exception {

        Pattern pattern = Pattern.compile("<title>(.+?)</title>");
        Matcher matcher = pattern.matcher(s);
        String temp = null;
        while (matcher.find()) {
            temp = matcher.group();
        }
        return temp;
    }
}
