package com.gcj.common;

import java.util.Vector;

/**
 * Created by gaochuanjun on 14-1-15.
 */
public class JabberServer {

    public static String END = "END";

    public static Vector<String> contentList;

    public static int contentSize;

    public static int pos = 0;

    public static volatile boolean isPush = false;

}