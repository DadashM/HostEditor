
import java.nio.charset.Charset;
import javax.swing.JOptionPane;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
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
    Gui gui;

    public void connect(String login, String pass) {
        try {
            path = "smb://" + Gui.ip + "/C$/Windows/System32/drivers/etc/hosts";
            text = "62.212.252.29 youtube.com";
            b = text.getBytes(Charset.forName("UTF-8"));

            ntlm = new NtlmPasswordAuthentication("", login, pass);
            smbFile = new SmbFile(path, ntlm);
            smbOut = new SmbFileOutputStream(smbFile, true);
            smbOut.write(b);
            smbOut.flush();
            smbOut.close();
            System.out.println("dispose1");
            Gui.loading.dispose();
        } catch (Exception e) {
            System.out.println("dispose2");
            Gui.loading.dispose();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (smbOut != null) {
                    smbOut.flush();
                    smbOut.close();
                }
            } catch (Exception e) {
                System.out.println("dispose3");
                Gui.loading.dispose();
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, e.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
