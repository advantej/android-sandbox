package com.advantej.mobile.android.tests.content;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by IntelliJ IDEA.
 * User: Tejas
 * Date: 7/25/12
 * Time: 11:42 AM
 */
public class DBContract {

    public static final String AUTHORITY = "com.advantej.mobile.android";

    public static final String TABLE1 = "mytable";
    public static final Uri CONTENT_URI_TABLE1 = Uri.parse("content://" + AUTHORITY + "/" + TABLE1);
    public class Table1Columns implements BaseColumns {
        private Table1Columns() {}
        public static final String TABLE1COL1 = "text";
    }





    public static final String TABLE2 = "mytable2";
    public static final Uri CONTENT_URI_TABLE2 = Uri.parse("content://" + AUTHORITY + "/" + TABLE2);
    public class Table2Columns implements BaseColumns {
        private Table2Columns() {}
        public static final String TABLE2COL1 = "text";
    }


}
