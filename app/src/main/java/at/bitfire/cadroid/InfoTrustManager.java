package at.bitfire.cadroid;

import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;
import at.bitfire.cadroid.ConnectionInfo;


public class InfoTrustManager implements X509TrustManager {
    private static final String TAG = "CAdroid.InfoTrustManager";

    final ConnectionInfo info;

    InfoTrustManager(ConnectionInfo info) {
        this.info = info;
    }


    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        throw new CertificateException("Client certificates are not supported");
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certChain, String authType) throws CertificateException {
        Log.d(TAG, "checkServerTrusted");
        ArrayUtils.reverse(certChain);
        info.setCertificates(certChain);
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        Log.d(TAG, "getAcceptedIssuers");
        return null;
    }

}
