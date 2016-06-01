package com.abin.lee.pagani.common.util;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

/**
 * Created with IntelliJ IDEA.
 * User: tinkpad
 * Date: 16-6-1
 * Time: 下午9:32
 * To change this template use File | Settings | File Templates.
 */
public class AtomicUtils {
    public static void main(String[] args) {
        Class cls = Person.class;
        AtomicLongFieldUpdater atomicLongFieldUpdater = AtomicLongFieldUpdater.newUpdater(cls, "id");
        Person person = new Person(123456L);
        atomicLongFieldUpdater.compareAndSet(person, 123456L, 1000);
        System.out.println("id="+person.getId());
    }



    static class Person {
        volatile long id;
        public Person(long id) {
            this.id = id;
        }
        public void setId(long id) {
            this.id = id;
        }
        public long getId() {
            return id;
        }
    }
}
