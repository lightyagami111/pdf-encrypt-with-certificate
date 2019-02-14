/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivaylorusev.encryptpdf;

import com.itextpdf.text.DocumentException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 *
 * @author ivaylo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, DocumentException, GeneralSecurityException {
        // TODO code application logic here
        String filename = "/home/ivaylo/Downloads/lambdaj-2.0-presentation.pdf";
        EncryptWithCertificate ewc = new EncryptWithCertificate();
        //ByteArrayOutputStream bos = ewc.createPdf(fileName);
        
        File pdfFile = new File(filename);
        FileInputStream fis = new FileInputStream(pdfFile);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        ewc.encryptPDFWithCertificate(bos);
    }
    
}
