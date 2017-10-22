package com.dyx.asca.model.realm;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Author：dayongxin
 * Function：
 */
public class Contact extends RealmObject {
    public String name;
    public RealmList<WeiXin> weiXins;
}
