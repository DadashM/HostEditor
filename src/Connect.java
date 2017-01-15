
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
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
    byte[] b;
    NtlmPasswordAuthentication ntlm;
    SmbFile smbFile;
    SmbFileOutputStream smbOut;
    SmbFileInputStream smbIn;
    BufferedReader br;
    Gui gui;

    String line;

    Pattern pattern;
    Matcher matcher;

    public void connect(String login, String pass) {
        try {
            path = "smb://" + Gui.ip + "/C$/Windows/System32/drivers/etc/hosts";
            text = System.lineSeparator() + "62.212.252.29 youtube.com";
            b = text.getBytes(Charset.forName("UTF-8"));

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);
            smbOut = new SmbFileOutputStream(smbFile, true);

            smbOut.write(b);
            smbOut.flush();
            smbOut.close();
            Gui.loading.dispose();
        } catch (Exception e) {
            Gui.loading.dispose();
            JOptionPane.showMessageDialog(gui, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (smbOut != null) {
                    smbOut.flush();
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
            text = "/n62.212.252.29 youtube.com";
            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);
            smbIn = new SmbFileInputStream(smbFile);
            br = new BufferedReader(new InputStreamReader(smbIn));

            pattern = Pattern.compile(text);
            matcher = pattern.matcher(path);

            while ((line = br.readLine()) != null) {

            }

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
