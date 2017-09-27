package com.mobile2016.common.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    final static int BUFFER_SIZE = 4096;

    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        while((count = in.read(data,0,BUFFER_SIZE)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }


}
