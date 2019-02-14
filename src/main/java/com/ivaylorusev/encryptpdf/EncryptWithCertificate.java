/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivaylorusev.encryptpdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


/**
 *
 * @author ivaylo
 */
public class EncryptWithCertificate {

    public EncryptWithCertificate() {
        Security.addProvider(new BouncyCastleProvider());
    }
    
    

    public ByteArrayOutputStream createPdf(String filename) throws IOException, DocumentException, GeneralSecurityException {        
        // step 1
        Document document = new Document();
        // step 2
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
        PdfWriter writer = PdfWriter.getInstance(document, bos);
        
        String pathToKeyStore = "/home/ivaylo/projects/mobilityplatform/mobility-platform/src/main/resources/encryption/ConnectedVan.p12";
        String password = "NY2G6IGW";
        Certificate cert = getPKCS12Certificate(pathToKeyStore, password);
        
        writer.setEncryption(new Certificate[]{cert},
            new int[]{PdfWriter.ALLOW_PRINTING}, PdfWriter.ENCRYPTION_AES_128);
        
        document.open();
        // step 4
        document.add(new Paragraph("Hello World!"));
        // step 5
        document.close();
                
        return bos;
    }
    

    public Certificate getPKCS12Certificate(String pathToKeyStore, String password) throws IOException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
        FileInputStream is = new FileInputStream(pathToKeyStore);
        
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(is, password.toCharArray());  
        String alias = ks.aliases().nextElement();
        X509Certificate cert = (X509Certificate) ks.getCertificate(alias);

        return cert;
    }
    
    public ByteArrayOutputStream encryptPDFWithCertificate(ByteArrayOutputStream baosPDF) {
        ByteArrayOutputStream encryptedBaosPDF = new ByteArrayOutputStream();
        
        try {
            String pathToKeyStore = "/home/ivaylo/projects/mobilityplatform/mobility-platform/src/main/resources/encryption/ConnectedVan.p12";
            String password = "NY2G6IGW";
            Certificate cert = getPKCS12Certificate(pathToKeyStore, password);

            PdfReader reader = new PdfReader(baosPDF.toByteArray());
            PdfStamper stamper = new PdfStamper(reader, encryptedBaosPDF);
            stamper.setEncryption(new Certificate[]{cert}, new int[]{PdfWriter.ALLOW_PRINTING}, PdfWriter.ENCRYPTION_AES_128);
            stamper.close();
            reader.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return encryptedBaosPDF;
    }
    
}
