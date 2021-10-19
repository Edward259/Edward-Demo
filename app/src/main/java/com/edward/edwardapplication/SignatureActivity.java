package com.edward.edwardapplication;

import android.Manifest;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.edward.edwardapplication.databinding.ActivitySignatureBinding;
import com.onyx.android.sdk.device.EnvironmentUtil;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class SignatureActivity extends AppCompatActivity {

    private static final int REQUEST_READ_AND_WRITE = 101;
    public static final String RELATIVE_PATH = "Books";
    private ActivitySignatureBinding binding;
    private String[] checkApps = new String[]{
            "org.unicorn.whiteboard_onyx",
            "com.strongene.ssestudent",
            "com.onyx.android.note",
            "com.onyx"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signature);
        if (!requestPermissions()) {
            return;
        }
        initView();
    }

    private void initView() {
        for (String checkApp : checkApps) {
            Button button = new Button(this);
            button.setText(checkApp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    check(checkApp);
                }
            });
            binding.container.addView(button);
        }
    }

    private void check(String checkApp) {
        boolean allowedSignature = isAllowedSignature(getSingInfo(checkApp));
        Toast.makeText(this, "app: " + checkApp + "   isAllowedSignature: " + allowedSignature, Toast.LENGTH_LONG).show();
        Log.e("edward", "onCreate: " + "app: " + checkApp + "   isAllowedSignature: " + allowedSignature);
    }

    public Signature[] getSingInfo(String packageName) {
        Signature[] signs = null;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            signs = packageInfo.signatures;
            Signature sign = signs[0];
            parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signs;
    }

    public void parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public void key() {
        String jksPath = ""; //jks file path
        String jksPassword = ""; // jks keyStore password
        String certAlias = ""; // cert alias
        String certPassword = ""; // cert password
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(jksPath), jksPassword.toCharArray());
            Certificate certificate = keyStore.getCertificate(certAlias);
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(certAlias, certPassword.toCharArray());
        } catch (Exception e) {

        }
    }

    public static boolean isAllowedSignature(Signature[] signatures) {
        List<Signature> allowedSignatureList = loadAllowedSignature();
        // if empty allowedSignatureList exist,everything would be disallowed to
        // install.
        if (allowedSignatureList.size() == 0) {
            return false;
        }
        for (Signature allowedSign : allowedSignatureList) {
            for (Signature s : signatures) {
                if (s != null) {
                    if (s.equals(allowedSign)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static List<Signature> loadAllowedSignature() {
        List allowedSignatureList = new ArrayList();
        if (allowedSignatureList != null && allowedSignatureList.size() > 0) {
            return allowedSignatureList;
        }
        allowedSignatureList = new ArrayList<Signature>();
        FileInputStream inputStream = null;
        try {
            File[] fileList = new File(EnvironmentUtil.getExternalStorageDirectory() , RELATIVE_PATH).listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return (file.getName().endsWith(".cer"));
                }
            });
            CertificateFactory certificateFactory =
                    CertificateFactory.getInstance("X.509");
            for (File file : fileList) {
                inputStream = new FileInputStream(file.getAbsolutePath());
                Certificate certificate =
                        certificateFactory.generateCertificate(inputStream);
                allowedSignatureList.add(
                        new Signature(certificate.getEncoded()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return allowedSignatureList;
        }
    }

    private boolean requestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_READ_AND_WRITE);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_AND_WRITE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initView();
                }
                break;
        }
    }
}