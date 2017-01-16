
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author admin
 */
public class Connect {

    String path;
    String text;
    StringBuilder stb;
    NtlmPasswordAuthentication ntlm;
    SmbFile smbFile;
    SmbFileOutputStream smbOut;
    SmbFileInputStream smbIn;
    BufferedReader br;
    BufferedWriter bw;
    Gui gui;

    String line;
    boolean findFlag;
    boolean lineExistFlag;
    Pattern pattern;
    Matcher matcher;

    public void connect(String login, String pass) {
        try {
            lineExistFlag = false;
            path = "smb://" + Gui.ip + "/C$/Windows/System32/drivers/etc/hosts";
            text = System.lineSeparator() + "62.212.252.29 youtube.com";
            //b = text.getBytes(Charset.forName("UTF-8"));

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);

            //Input Stream
            smbIn = new SmbFileInputStream(smbFile);
            br = new BufferedReader(new InputStreamReader(smbIn));

            fileToString(br);

            //Finding line on StringBuilder
            pattern = Pattern.compile("62.212.252.29 youtube.com");
            matcher = pattern.matcher(stb.toString());

            while (matcher.find()) {
                lineExistFlag = true;
            }

            if (!lineExistFlag) {
                //Output Stream
                smbFile = new SmbFile(path, ntlm);
                smbOut = new SmbFileOutputStream(smbFile, true);
                bw = new BufferedWriter(new OutputStreamWriter(smbOut));
                bw.write(text);
                bw.flush();
            } else{
                JOptionPane.showMessageDialog(gui, "Host record all ready exist", "Exist", JOptionPane.WARNING_MESSAGE);
            }
            Gui.loading.dispose();
        } catch (Exception e) {
            Gui.loading.dispose();
            JOptionPane.showMessageDialog(gui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    smbOut.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(gui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void denyAction(String login, String pass) {
        try {
            findFlag = false;
            path = "smb://" + Gui.ip + "/C$/Windows/System32/drivers/etc/hosts";
            text = "62.212.252.29 youtube.com";

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);

            //Input Stream
            smbIn = new SmbFileInputStream(smbFile);
            br = new BufferedReader(new InputStreamReader(smbIn));

            fileToString(br);

            pattern = Pattern.compile(text);
            matcher = pattern.matcher(stb.toString());

            while (matcher.find()) {
                //Output Stream
                smbFile = new SmbFile(path, ntlm);
                smbOut = new SmbFileOutputStream(smbFile);
                bw = new BufferedWriter(new OutputStreamWriter(smbOut));
                stb = new StringBuilder(matcher.replaceAll(""));
                findFlag = true;
            }

            if (findFlag) {
                stb.toString().trim();
                bw.write(stb.toString());
                bw.flush();
            } else {
                JOptionPane.showMessageDialog(gui, "No host records found", "Not Found", JOptionPane.WARNING_MESSAGE);
            }
            Gui.loading.dispose();
        } catch (Exception ex) {
            Gui.loading.dispose();
            JOptionPane.showMessageDialog(gui, ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (br != null) {
                    br.close();
                    smbIn.close();
                }
                if (bw != null) {
                    bw.close();
                    smbOut.close();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(gui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public void fileToString(BufferedReader br) throws IOException {
        stb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            stb.append(line);
            stb.append(System.lineSeparator());
        }
    }

}
