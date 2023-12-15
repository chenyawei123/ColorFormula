package com.net.netrequest.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 */

public class Byte2Hex {
    /**
     * 16进制中的字符集
     */
    private static final String HEX_CHAR = "0123456789ABCDEF";

    /**
     * 16进制中的字符集对应的字节数组
     */
    private static final byte[] HEX_STRING_BYTE = HEX_CHAR.getBytes();

    /**
     * 10进制字节数组转换为16进制字节数组
     * <p>
     * byte用二进制表示占用8位，16进制的每个字符需要用4位二进制位来表示，则可以把每个byte
     * 转换成两个相应的16进制字符，即把byte的高4位和低4位分别转换成相应的16进制字符，再取对应16进制字符的字节
     *
     * @param b 10进制字节数组
     * @return 16进制字节数组
     */
    public static byte[] byte2hex(byte[] b) {
        int length = b.length;
        byte[] b2 = new byte[length << 1];
        int pos;
        for (int i = 0; i < length; i++) {
            pos = 2 * i;
            b2[pos] = HEX_STRING_BYTE[(b[i] & 0xf0) >> 4];
            b2[pos + 1] = HEX_STRING_BYTE[b[i] & 0x0f];
        }
        return b2;
    }

    /**
     * 16进制字节数组转换为10进制字节数组
     * <p>
     * 两个16进制字节对应一个10进制字节，则将第一个16进制字节对应成16进制字符表中的位置(0~15)并向左移动4位，
     * 再与第二个16进制字节对应成16进制字符表中的位置(0~15)进行或运算，则得到对应的10进制字节
     *
     * @param b 10进制字节数组
     * @return 16进制字节数组
     */
    public static byte[] hex2byte(byte[] b) {
        if (b.length % 2 != 0) {
            throw new IllegalArgumentException("byte array length is not even!");
        }

        int length = b.length >> 1;
        byte[] b2 = new byte[length];
        int pos;
        for (int i = 0; i < length; i++) {
            pos = i << 1;
            b2[i] = (byte) (HEX_CHAR.indexOf(b[pos]) << 4 | HEX_CHAR.indexOf(b[pos + 1]));
        }
        return b2;
    }

    public static Integer byteToInteger(Byte b) {
        return 0xff & b;
    }

    /**
     * 将16进制字节数组转成10进制字符串
     *
     * @param b 16进制字节数组
     * @return 10进制字符串
     */
    public static String hex2Str(byte[] b) {
        return new String(hex2byte(b));
    }

    /**
     * 将10进制字节数组转成16进制字符串
     *
     * @param b 10进制字节数组
     * @return 16进制字符串
     */
    public static String byte2HexStr(byte[] b) {
        return Integer.toHexString(Integer.parseInt(new String(b)));
    }

    public static void main(String[] args) {
        System.out.println(hex2Str(byte2hex("60".getBytes())));
        System.out.println(byte2HexStr("60".getBytes()));
    }

    /*
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";

        for (int i = 0; i < b.length; i++) {

            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
            if (b[i] == 3) {//-17
                break;
            }
        }

        return r;
    }


    /**
     * 　　* 将byte转为16进制
     * 　　* @param bytes
     * 　　* @return
     */
    public static String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0; i < bytes.length; i++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
//1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

    /**
     * 16进制的字符串表示转成字节数组
     *
     * @param hexString 16进制格式的字符串
     * @return 转换后的字节数组
     **/
    public static byte[] toByteArray(String hexString) {
        // hexString = hexString.replaceAll(" ", "");
        final byte[] byteArray = new byte[hexString.length() / 2];
        int k = 0;
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);
            byteArray[i] = (byte) (high << 4 | low);
            k += 2;
        }
        return byteArray;
    }

    /*
     * 十进制int转16进制字符串
     */
    public static String int2HexString(int b) {
        Integer num = new Integer(b);
        String numHex = Integer.toHexString(num);
        return numHex;
    }

    /*
     * 16进制字符串转十进制int
     */
    public static int HexString2int(String numHex) {
        int num = Integer.parseInt(numHex, 16);
        return num;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static byte[] int2ByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

//    public static byte[] int2ByteArray(int i) {
//        byte[] result = new byte[3];
//        result[0] = (byte) ((i >> 16) & 0xFF);
//        result[1] = (byte) ((i >> 8) & 0xFF);
//        result[2] = (byte) (i & 0xFF);
//        return result;
//    }

    public static byte[] int2ByteArray2(int i) {
        byte[] result = new byte[2];
        result[0] = (byte) ((i >> 16) & 0xFF);
        result[1] = (byte) ((i >> 8) & 0xFF);
        //result[2] = (byte) (i & 0xFF);
        return result;
    }

    public static int getHeight4(byte data) {//获取高四位
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    public static int getLow4(byte data) {//获取低四位
        int low;
        low = (data & 0x0f);
        return low;
    }

    //十进制ascii转字符串
    public static String dec2Str(String ascii) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ascii.length() - 1; i += 2) {
            String h = ascii.substring(i, (i + 2));
            // 这里第二个参数传10表10进制
            int decimal = Integer.parseInt(h, 10);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

//// 结果为ABCDEF
//System.out.println(dec2Str("656667686970"));

    ///String 转十进制ascii
    public static String str2Dec(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 第二个参数10表示10进制
            sb.append(Integer.toString(c, 10));
            // 或者省略第二个参数，默认为10进制
            // sb.append(Integer.toString(c));
        }
        return sb.toString();
    }

//// 结果为656667686970
//System.out.println(str2Dec("ABCDEF"));

    //ASCII码hex字符串转String明文
//代码很简单，就是每两个字符表示的16进制ASCII码解析成一个明文字符
    public static String hex2Str(String hex) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hex.length() - 1; i += 2) {
            String h = hex.substring(i, (i + 2));
            int decimal = Integer.parseInt(h, 16);
            sb.append((char) decimal);
        }
        return sb.toString();
    }

//// 输出结果为ABCDEF
//System.out.println(hex2Str("414243444546"));


    //String明文转ASCII码hex字符串
//代码很简单，就是一个明文字符生成两个字符表示的16进制ASCII码
    public static String str2Hex(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            // 这里的第二个参数16表示十六进制
            sb.append(Integer.toString(c, 16));
            // 或用toHexString方法直接转成16进制
            // sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }

//// 输出结果为414243444546
//System.out.println(str2Hex("ABCDEF"));

    /**
     * 大端序 int转字节数组
     *
     * @param i
     * @return
     */
    public static byte[] bigEndian(int i) {
        int byte1 = i & 0XFF;
        int byte2 = (i & 0XFFFF) >>> 8;
        int byte3 = (i & 0XFFFFFF) >>> 16;
        int byte4 = (i & 0XFFFFFFFF) >>> 24;
        return new byte[]{(byte) byte4, (byte) byte3, (byte) byte2, (byte) byte1};
    }


    /**
     * 高效写法 16进制字符串转成byte数组
     *
     * @param hex 16进制字符串，支持大小写
     * @return byte数组
     */
    public static byte[] hexStringToBytes(String hex) {
        byte[] result = new byte[hex.length() / 2];
        char[] chars = hex.toCharArray();
        for (int i = 0, j = 0; i < result.length; i++) {
            result[i] = (byte) (toByte(chars[j++]) << 4 | toByte(chars[j++]));
        }
        return result;
    }

    private static int toByte(char c) {
        if (c >= '0' && c <= '9') return (c - '0');
        if (c >= 'A' && c <= 'F') return (c - 'A' + 0x0A);
        if (c >= 'a' && c <= 'f') return (c - 'a' + 0x0a);
        throw new RuntimeException("invalid hex char '" + c + "'");
    }

    public static byte[] long2bytes(long value) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeLong(value);
        } catch (IOException e) {
            e.printStackTrace();
        }

        byte[] buf = baos.toByteArray();
        return buf;

    }

    public static long bytes2long(byte[] buf) {
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        DataInputStream dis = new DataInputStream(bais);
        long l = 0;
        try {
            l = dis.readLong();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    public static byte[] longToBytes(long num) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) (num & 0xff);//取num最低8位
            num >>= 8;//右移8位，继续处理高8位
        }
        return bytes;
    }




    /**
     * 将16进制 ASCII转成字符串
     *
     * @param hexValue
     * @return
     */
    public static String asciiToString(String hexValue) {
        StringBuffer sbu = new StringBuffer();
        for (int i = 0; i < hexValue.length(); i += 2) {
            sbu.append((char) Integer.parseInt(hexValue.substring(i, i + 2), 16));
        }
        return sbu.toString();
    }

    /**
     * 将字符串转成ASCII
     *
     * @param strValue
     * @return
     */
    public static String stringToAscii(String strValue) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = strValue.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }


}
