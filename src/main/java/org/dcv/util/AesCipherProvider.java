package org.dcv.util;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static org.dcv.util.Constants.AES_ALGORITHM_AND_TRANSFORMATION;

@Slf4j
@Getter
@ApplicationScoped
public class AesCipherProvider {


    @Inject
    private EncryptCipherProvider encryptCipherProvider;

    //    private final Cipher cipher = Cipher.getInstance("RSA");
    private Cipher cipher;
    private String encryptedBase64AesKey;

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
    public void initCipherAndEncryptedKey() {
        log.debug("start initCipherAndEncryptedKey");

        try {

            // generate a AES key
            final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM_AND_TRANSFORMATION);
            keyGenerator.init(128); // The AES key size in number of bits
            final SecretKey aesKey = keyGenerator.generateKey();

            // init cipher
            cipher = Cipher.getInstance(AES_ALGORITHM_AND_TRANSFORMATION);
            cipher.init(ENCRYPT_MODE, aesKey);

            // set encryptedBase64AesKey
            final byte[] cipherText = encryptCipherProvider.getCipher().doFinal(aesKey.getEncoded());
            encryptedBase64AesKey = getEncoder().encodeToString(cipherText);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("finish initCipherAndEncryptedKey");


////        log.debug("initC: {}", configuration.getPublicKeyPath().toString());
//        try (final InputStream inputStream = new FileInputStream(configuration.getPublicKeyPath())) {
////        try (final InputStream inputStream = new FileInputStream("/opt/app-root/src/etc/apache2/cert.pem")) {
//            final CertificateFactory certificateFactory = getInstance("X.509");
//            final X509Certificate certificate = (X509Certificate) certificateFactory.generateCertificate(inputStream);
//            final PublicKey publicKey = certificate.getPublicKey();
//            cipher = Cipher.getInstance("RSA");
//            cipher.init(ENCRYPT_MODE, publicKey);
    }
}
