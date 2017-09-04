package com.instadownloader.instadpdownloader.Utils;

import java.io.File;
import java.util.List;

/**
 * Created by ayush on 2/9/17.
 */

public class ObjectClass {
    private Object object1;
    private Object object2;
    private Object object3;
    private Object object4;

    public ObjectClass(Object object1, Object object2) {
        this.object1 = object1;
        this.object2 = object2;
    }

    public ObjectClass(Object object1, Object object2,Object object3, Object object4) {
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
        this.object4 = object4;
    }

    public Object getObject1() {
        return object1;
    }
    public Object getObject2() {
        return object2;
    }
    public Object getObject3() {
        return object3;
    }
    public Object getObject4() {
        return object4;
    }

}
