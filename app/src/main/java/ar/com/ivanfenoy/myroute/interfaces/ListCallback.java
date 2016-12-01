package ar.com.ivanfenoy.myroute.interfaces;

import java.util.ArrayList;

/**
 * Created by ivanj on 7/11/2016.
 */

public interface ListCallback {
    void done(Exception e, ArrayList<?> list);
}
