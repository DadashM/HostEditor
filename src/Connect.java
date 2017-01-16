
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

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
    NtlmPasswordAuthentication ntlm;
    SmbFile smbFile;
    SmbFileOutputStream smbOut;
    SmbFileInputStream smbIn;
    BufferedReader br;
    BufferedWriter bw;
    Gui gui;

    String line;

    Pattern pattern;
    Matcher matcher;

    public void connect(String login, String pass) {
        try {
            path = "smb://" + Gui.ip + "/C$/Windows/System32/drivers/etc/hosts";
            text = System.lineSeparator() + "62.212.252.29 youtube.com";
            //b = text.getBytes(Charset.forName("UTF-8"));

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);
            smbOut = new SmbFileOutputStream(smbFile, true);
            bw = new BufferedWriter(new OutputStreamWriter(smbOut));

            bw.write(text);
            bw.flush();
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
                Gui.loading.dispose();
                e.printStackTrace();
                JOptionPane.showMessageDialog(gui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void denyAction(String login, String pass) {
        try {
            path = "smb://" + Gui.ip + "/C$/a.txt";
            text = "62.212.252.29 youtube.com";

            StringBuilder stb = new StringBuilder();

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);
            smbIn = new SmbFileInputStream(smbFile);
            br = new BufferedReader(new InputStreamReader(smbIn));

            while ((line = br.readLine()) != null) {
                stb.append(line);
                stb.append(System.lineSeparator());
            }
            
            pattern = Pattern.compile(stb.toString());
            matcher = pattern.matcher(text);
            System.out.println("begin");
            while (matcher.matches()) {
                System.out.println("if");
                System.out.println(matcher.group());
            }
            System.out.println("end");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (smbIn != null) {
                    smbIn.close();
                }
                if (smbIn != null) {
                    smbIn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
