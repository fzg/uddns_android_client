package foo.bar.uddns;

import at.bitfire.cadroid.ConnectionInfo;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import static at.bitfire.cadroid.ConnectionInfo.fetch;

public class CertMgr {
    final Context ctx;
    SSLContext sslContext;
    KeyStore trustStore;
    private final String pass;

    public CertMgr(Context c, String p) {
        ctx = c;
        pass = p;
        sslContext = null;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Long, X509Certificate[]> {
        protected X509Certificate[] doInBackground(URL... u) {
            try {
                ConnectionInfo c = fetch(u[0]);
                return c.getCertificates();
            } catch (java.net.ConnectException e) {
                final String str = e.toString();
                Log.e("DlEr", str);
            } catch (Exception e) { e.printStackTrace(); }
            return null;
        }
        protected void onPostExecute(X509Certificate[] result) {
            try {
                Log.w("onPostExec", String.valueOf(result.length) + " certs got.");
                if (result[0] != null) addCert(result[0], "alias"); // FIXME: on ignore les pas-premier.
            } catch (NullPointerException e) {
                    Toast.makeText(ctx, "Error getting certificate", Toast.LENGTH_LONG).show(); return;
            } catch (Exception e) {e.printStackTrace(); return;}
            Toast.makeText(ctx, "Got the certificate!", Toast.LENGTH_LONG).show();
            //TODO: verifier que les certs sont effectivement utilisés.
        }
    }

    public void fetchCertFor(String host, String port) throws IOException {
        String addr = "https://" + host + ":" + port + "/a";
        java.net.URL u = new java.net.URL(addr);


        //TODO: Use the InfoHostnameVerifier here; How do we present it to the user?
        DownloadFilesTask dln = new DownloadFilesTask();
        dln.execute(u);
    }

    public void addCert(X509Certificate c, String alias) throws IOException, CertificateException, NoSuchAlgorithmException {
        if (trustStore == null) {
            try {
                do_getContext();
            //    trustStore = KeyStore.getInstance("BKS");
            //    trustStore.load(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            trustStore.setCertificateEntry(alias, c);
            File x = ctx.getFilesDir();

            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(x.getCanonicalPath() + "BKS"));
            trustStore.store(outputStream, pass.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return;
    }

    public SSLContext getContext() throws IOException {
        if (null == sslContext) do_getContext();
        return sslContext;
    }

    private void do_getContext() throws IOException {
        BufferedInputStream inputStream = null;
        File x = ctx.getFilesDir();
        int i = 0;
        while (i < 2) {
            try {
                if (i == 1) return;
                ++i;
                inputStream = new BufferedInputStream(new FileInputStream(x.getCanonicalPath() + "/BKS"));
                break;
            } catch (FileNotFoundException e) {
                File f = new File(x.getCanonicalPath() + "/BKS");
                // f.getParentFile().mkdirs();
                //noinspection ResultOfMethodCallIgnored
                f.createNewFile();
            }
        }
        try {
            trustStore = KeyStore.getInstance("BKS");
            trustStore.load(inputStream, pass.toCharArray());
        } catch (java.io.EOFException e) {     // store was just created
            try {
                trustStore.load(null, null);
            } catch (Exception ge) {
                Log.e("KEYST.INSD", ge.toString());
            }
        } catch (Exception e) {
            Log.w("KEYST.RE0", e.toString());
        }
        try {
            Log.w("LoadingTS", trustStore.getCertificate("alias").toString());
        } catch (Exception e) {
            Log.e("arh", e.toString());
        }
        try {
            if (inputStream != null) inputStream.close();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
            tmf.init(trustStore);
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
        } catch (Exception e) {
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        }
    }
}