package com.dyx.asca;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Author：dayongxin
 * Function：
 */
public class XApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.addLogAdapter(new AndroidLogAdapter());
        Stetho.initializeWithDefaults(this);
        supportTlsv12();
        initRealm();
    }

    private void initRealm() {
        //初始化Realm
        Realm.init(this);
        //配置Realm
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("dyx.realm")
                .deleteRealmIfMigrationNeeded()
                .build();
        //应用配置
        Realm.setDefaultConfiguration(configuration);
    }

    private void supportTlsv12() {
//        try {
//            ProviderInstaller.installIfNeeded(getApplicationContext());
//            SSLContext sslContext;
//            sslContext = SSLContext.getInstance("TLSv1.2");
//            sslContext.init(null, null, null);
//            sslContext.createSSLEngine();
//        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException
//                | NoSuchAlgorithmException | KeyManagementException e) {
//            e.printStackTrace();
//        }
    }


}
