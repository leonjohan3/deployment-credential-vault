package org.dcv.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static java.security.cert.CertificateFactory.getInstance;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Slf4j
@Getter
@ApplicationScoped
public class EncryptCipherProvider {

    private static final String RSA_ALGORITHM_AND_TRANSFORMATION = "RSA/ECB/PKCS1Padding";

    @Inject
    private ConfigurationProvider configuration;

    //    private final Cipher cipher = Cipher.getInstance("RSA");
    private Cipher cipher;

//    public EncryptCipherProvider() throws NoSuchPaddingException, NoSuchAlgorithmException {
//    }

//    public EncryptCipherProvider() throws IOException, CertificateException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
//
////        try (final InputStream inputStream = new FileInputStream(configuration.getPublicKeyPath())) {
//        try (final InputStream inputStream = new FileInputStream("/opt/app-root/src/etc/apache2/cert.pem")) {
//            final CertificateFactory certificateFactory = getInstance("X.509");
//            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//            final PublicKey publicKey = certificate.getPublicKey();
////            cipher = Cipher.getInstance("RSA");
//            cipher.init(ENCRYPT_MODE, publicKey);
//        }
//    }

    @PostConstruct
    public void initCipher() {
        log.debug("start initCipher");

//        log.debug("initC: {}", configuration.getPublicKeyPath().toString());
        try (final InputStream inputStream = new FileInputStream(configuration.getPublicKeyPath())) {
//        try (final InputStream inputStream = new FileInputStream("/opt/app-root/src/etc/apache2/cert.pem")) {
            final CertificateFactory certificateFactory = getInstance("X.509");
            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            final PublicKey publicKey = certificate.getPublicKey();
            cipher = Cipher.getInstance(RSA_ALGORITHM_AND_TRANSFORMATION);
            cipher.init(ENCRYPT_MODE, publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("finish initCipher");
    }
}
