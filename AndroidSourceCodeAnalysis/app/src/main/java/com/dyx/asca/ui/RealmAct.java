package com.dyx.asca.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.dyx.asca.R;
import com.dyx.asca.model.realm.Person;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Author：dayongxin
 * Function：
 */
public class RealmAct extends AppCompatActivity {
    Realm realm = Realm.getDefaultInstance();
    @BindView(R.id.btn_add)
    Button btnAdd;
    private Unbinder mUnbinder;

    private RealmAsyncTask alterTask;
    private RealmAsyncTask addTask;
    private RealmResults<Person> persons;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm);
        mUnbinder = ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_add)
    public void onViewClicked() {
        /**
         * 1、增
         */
        //新建一个对象，并进行存储
//        newObjectAndSaveBySync();
        newObjectAndSaveByAsyn(getPerson());
    }

    private Person getPerson() {
        Person person = new Person("dayongxin");
        person.setAge(18);
        person.setCompany("NB");
        return person;
    }

    private void newObjectAndSaveByAsyn(final Person person) {
        addTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(person);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Logger.d("Success!");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Logger.d(error.getMessage());
            }
        });
    }

    private void initView() {
        /**
         * 增
         */
//        add();
        /**
         * 删
         */
        // delete();
        /**
         * 改
         */
        alter(1);
        /**
         * 查
         */
        query();
    }

    private void query() {
        //查询全部
        queryAllPersons();
        queryAllPersonsByAsyn();
        //条件查询
        queryPersonById(1);
        queryPersonByIdByAsyn(1);
        //对查询结果进行排序
        queryAllPersonsSort(Sort.DESCENDING);
        queryAllPersonsSortByAsyn(Sort.DESCENDING);
        //其他查询
        //平均年龄
        queryAverageAge();
        queryAverageAgeByAsyn();
        //查询总年龄
        queryAllAges();
        queryAllAgesAsyn();
        //查询最大年龄
        queryMaxAge();
        queryMaxAgeAsyn();
    }


    private int queryMaxAge() {
        Number number = realm.where(Person.class).findAll().max("age");
        return number.intValue();
    }

    private int queryMaxAgeAsyn() {
        Number number = realm.where(Person.class).findAllAsync().max("age");
        return number.intValue();
    }

    private int queryAllAges() {
        Number number = realm.where(Person.class).findAll().sum("age");
        return number.intValue();
    }

    private int queryAllAgesAsyn() {
        Number number = realm.where(Person.class).findAllAsync().sum("age");
        return number.intValue();
    }


    private double queryAverageAge() {
        return realm.where(Person.class).findAll().average("age");
    }

    private double queryAverageAgeByAsyn() {
        return realm.where(Person.class).findAllAsync().average("age");
    }


    private List<Person> queryAllPersonsSort(Sort sort) {
        RealmResults<Person> persons = realm.where(Person.class).findAll();
        persons = persons.sort("id", sort);
        return realm.copyFromRealm(persons);
    }

    private void queryAllPersonsSortByAsyn(final Sort sort) {
        RealmResults<Person> persons = realm.where(Person.class).findAllAsync();
        persons.addChangeListener(new RealmChangeListener<RealmResults<Person>>() {
            @Override
            public void onChange(RealmResults<Person> persons) {
                persons = persons.sort("id", sort);
                List<Person> personList = realm.copyFromRealm(persons);
            }
        });
    }

    private Person queryPersonById(int id) {
        Person person = realm.where(Person.class).equalTo("id", id).findFirst();
        return person;
    }

    private Person queryPersonByIdByAsyn(int id) {
        Person person = realm.where(Person.class).equalTo("id", id).findFirstAsync();
        return person;
    }

    private List<Person> queryAllPersons() {
        RealmResults<Person> persons = realm.where(Person.class).findAll();
        return realm.copyFromRealm(persons);
    }

    private void queryAllPersonsByAsyn() {
        persons = realm.where(Person.class).findAllAsync();
        persons.addChangeListener(new RealmChangeListener<RealmResults<Person>>() {
            @Override
            public void onChange(RealmResults<Person> persons) {
                //查询结果
                List<Person> results = realm.copyFromRealm(persons);
            }
        });
    }

    private void alter(int id) {
        alterBySync(id);
        alterByAsyn(id);
    }

    private void alterByAsyn(final int id) {
        alterTask = realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = realm.where(Person.class).equalTo("id", id).findFirst();
                person.setAge(19);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }


    private void alterBySync(int id) {
        Person person = realm.where(Person.class).equalTo("id", 1).findFirst();
        realm.beginTransaction();
        person.setName("dyx");
        person.setCompany("NNN");
        person.setAge(20);
        realm.commitTransaction();
    }

    private void delete() {
        deleteBySync();
        deleteByAsyn();
    }

    private void deleteByAsyn() {
        final RealmResults<Person> persons = realm.where(Person.class).findAll();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = persons.get(1);
                //删除
                person.deleteFromRealm();
                //删除第一条
                persons.deleteFirstFromRealm();
                //删除最后一条
                persons.deleteLastFromRealm();
                //删除位置为1
                persons.deleteFromRealm(1);
                //删除所有
                persons.deleteAllFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {

            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {

            }
        });
    }

    private void deleteBySync() {
        final RealmResults<Person> persons = realm.where(Person.class).findAll();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Person person = persons.get(1);
                //删除
                person.deleteFromRealm();
                //删除第一条
                persons.deleteFirstFromRealm();
                //删除最后一条
                persons.deleteLastFromRealm();
                //删除位置为1
                persons.deleteFromRealm(1);
                //删除所有
                persons.deleteAllFromRealm();
            }
        });
    }

    private void add() {
        /**
         * 1、事务操作：方法一
         */
        //1、新建一个对象，并进行存储
//        newObjectAndSave();
        //2、复制一个对象到Realm数据库
        copyObjectAndSave();
        /**
         * 1、事务操作：方法二
         */
        //使用事务块
        useTransaction();
    }

    private void useTransaction() {
        final Person person = new Person("dayongxin");
        person.setAge(19);
        person.setCompany("NNBB");
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(person);
            }
        });
    }

    private void copyObjectAndSave() {
        Person person = new Person("dayongxin");
        person.setAge(17);
        person.setCompany("NB");
        realm.beginTransaction();
        realm.copyToRealm(person);
        realm.commitTransaction();
    }

    private void newObjectAndSaveBySync() {
        realm.beginTransaction();
        Number value = realm.where(Person.class).max("id");
        long pk = (value != null) ? value.longValue() + 1 : 0;
        Person person = realm.createObject(Person.class, pk++);
        person.setName("dayongxin");
        person.setAge(18);
        person.setCompany("nb");
        realm.commitTransaction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (alterTask != null && !addTask.isCancelled()) {
            alterTask.cancel();
        }
        if (persons != null && persons.size() > 0) {
            persons.removeAllChangeListeners();
        }
    }
}
