package org.dcv.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import static java.security.KeyFactory.getInstance;
import static javax.crypto.Cipher.DECRYPT_MODE;

@Slf4j
@Getter
@ApplicationScoped
public class DecryptCipherProvider {

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

//        final File privateKeyFile = new File("/Users/leon/telstra/lean-ms/jax-rs-tomcat/pkcs8_key.pem");
        final byte[] privateKeyBytes = new byte[(int) configuration.getPrivateKeyPath().length()];

        try (final InputStream inputStream = new FileInputStream(configuration.getPrivateKeyPath())) {

            inputStream.read(privateKeyBytes);
//        try {
//            bis = new BufferedInputStream(new FileInputStream(privKeyFile));
//        } catch(FileNotFoundException e) {
//            throw new Exception("Could not locate keyfile at '" + keyPath + "'", e);
//        }
//        bis.close();
            final KeyFactory keyFactory = getInstance("RSA");
            final KeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            final RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);

//        log.info(privateKey.getAlgorithm());
            cipher = Cipher.getInstance("RSA");
            cipher.init(DECRYPT_MODE, privateKey);

//        try (final InputStream inputStream = new FileInputStream(configuration.getPrivateKeyPath())) {
////        try (final InputStream inputStream = new FileInputStream("/opt/app-root/src/etc/apache2/cert.pem")) {
//            final CertificateFactory certificateFactory = getInstance("X.509");
//            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//            final PublicKey publicKey = certificate.getPublicKey();
//            cipher = Cipher.getInstance("RSA");
//            cipher.init(ENCRYPT_MODE, publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("finish initCipher");
    }
}
