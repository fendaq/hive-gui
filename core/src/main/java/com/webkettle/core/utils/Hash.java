package com.webkettle.core.utils;

import com.webkettle.core.utils.base64.Base64Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class Hash {

    /**
     * Base64操作工具类
     */
    public static final Base64Util BASE_UTIL = new Base64Util() {

        private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
                .toCharArray();
        private byte[] codes = new byte[256];

        private boolean isInit = false;

        void init (){
            if (!isInit) {
                for (int i = 0; i < 256; i++) {
                    codes[i] = -1;
                    // LoggerUtil.debug(i + "&" + codes[i] + " ");
                }
                for (int i = 'A'; i <= 'Z'; i++) {
                    codes[i] = (byte) (i - 'A');
                    // LoggerUtil.debug(i + "&" + codes[i] + " ");
                }

                for (int i = 'a'; i <= 'z'; i++) {
                    codes[i] = (byte) (26 + i - 'a');
                    // LoggerUtil.debug(i + "&" + codes[i] + " ");
                }
                for (int i = '0'; i <= '9'; i++) {
                    codes[i] = (byte) (52 + i - '0');
                    // LoggerUtil.debug(i + "&" + codes[i] + " ");
                }
                codes['+'] = 62;
                codes['/'] = 63;
                isInit = true;
            }
        }

        private byte[] readBytes(File file) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] b = null;
            InputStream fis = null;
            InputStream is = null;
            try {
                fis = new FileInputStream(file);
                is = new BufferedInputStream(fis);
                int count = 0;
                byte[] buf = new byte[16384];
                while ((count = is.read(buf)) != -1) {
                    if (count > 0) {
                        baos.write(buf, 0, count);
                    }
                }
                b = baos.toByteArray();

            } finally {
                try {
                    if (fis != null)
                        fis.close();
                    if (is != null)
                        is.close();
                    if (baos != null)
                        baos.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            return b;
        }

        private char[] readChars(File file) throws IOException {
            CharArrayWriter caw = new CharArrayWriter();
            Reader fr = null;
            Reader in = null;
            try {
                fr = new FileReader(file);
                in = new BufferedReader(fr);
                int count = 0;
                char[] buf = new char[16384];
                while ((count = in.read(buf)) != -1) {
                    if (count > 0) {
                        caw.write(buf, 0, count);
                    }
                }

            } finally {
                try {
                    if (caw != null)
                        caw.close();
                    if (in != null)
                        in.close();
                    if (fr != null)
                        fr.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }

            return caw.toCharArray();
        }

        private void writeBytes(File file, byte[] data) throws IOException {
            OutputStream fos = null;
            OutputStream os = null;
            try {
                fos = new FileOutputStream(file);
                os = new BufferedOutputStream(fos);
                os.write(data);

            } finally {
                try {
                    if (os != null)
                        os.close();
                    if (fos != null)
                        fos.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        private void writeChars(File file, char[] data) throws IOException {
            Writer fos = null;
            Writer os = null;
            try {
                fos = new FileWriter(file);
                os = new BufferedWriter(fos);
                os.write(data);

            } finally {
                try {
                    if (os != null)
                        os.close();
                    if (fos != null)
                        fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }




        @Override
        public void encode(File file) {
            // TODO Auto-generated method stub
            init();
            if (!file.exists()) {

                System.exit(0);

            }else {

                byte[] decoded;
                try {
                    decoded = readBytes(file);
                    char[] encoded = encode(decoded);
                    writeChars(file, encoded);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            file = null;
        }

        @Override
        public char[] encode(byte[] data) {
            // TODO Auto-generated method stub
            init();
            char[] out = new char[((data.length + 2) / 3) * 4];
            for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
                boolean quad = false;
                boolean trip = false;

                int val = (0xFF & (int) data[i]);
                val <<= 8;
                if ((i + 1) < data.length) {
                    val |= (0xFF & (int) data[i + 1]);
                    trip = true;
                }
                val <<= 8;
                if ((i + 2) < data.length) {
                    val |= (0xFF & (int) data[i + 2]);
                    quad = true;
                }
                out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
                val >>= 6;
                out[index + 1] = alphabet[val & 0x3F];
                val >>= 6;
                out[index + 0] = alphabet[val & 0x3F];
            }
            return out;

        }

        @Override
        public String encode(String data) {
            // TODO Auto-generated method stub
            return new String(encode(data.getBytes()));
        }


        @Override
        public void decode(File file) {
            // TODO Auto-generated method stub
            init();
            if (!file.exists()) {
                System.exit(0);
            } else {
                char[] encoded;
                try {
                    encoded = readChars(file);
                    byte[] decoded = decode(encoded);
                    writeBytes(file, decoded);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            file = null;
        }

        @Override
        public byte[] decode(char[] data) {
            // TODO Auto-generated method stub
            init();
            int tempLen = data.length;
            for (int ix = 0; ix < data.length; ix++) {
                if ((data[ix] > 255) || codes[data[ix]] < 0) {
                    --tempLen; // ignore non-valid chars and padding
                }
            }
            // calculate required length:
            // -- 3 bytes for every 4 valid base64 chars
            // -- plus 2 bytes if there are 3 extra base64 chars,
            // or plus 1 byte if there are 2 extra.

            int len = (tempLen / 4) * 3;
            if ((tempLen % 4) == 3) {
                len += 2;
            }
            if ((tempLen % 4) == 2) {
                len += 1;

            }
            byte[] out = new byte[len];

            int shift = 0; // # of excess bits stored in accum
            int accum = 0; // excess bits
            int index = 0;

            // we now go through the entire array (NOT using the 'tempLen' value)
            for (int ix = 0; ix < data.length; ix++) {
                int value = (data[ix] > 255) ? -1 : codes[data[ix]];

                if (value >= 0) { // skip over non-code
                    accum <<= 6; // bits shift up by 6 each time thru
                    shift += 6; // loop, with new bits being put in
                    accum |= value; // at the bottom.
                    if (shift >= 8) { // whenever there are 8 or more shifted in,
                        shift -= 8; // write them out (from the top, leaving any
                        out[index++] = // excess at the bottom for next iteration.
                                (byte) ((accum >> shift) & 0xff);
                    }
                }
            }

            // if there is STILL something wrong we just have to throw up now!
            if (index != out.length) {
                throw new Error("Miscalculated data length (wrote " + index
                        + " instead of " + out.length + ")");
            }

            return out;

        }

        @Override
        public String decode(String data) {
            // TODO Auto-generated method stub
            return new String(decode(data.toCharArray()));
        }

        @Override
        public String encode(String data, String charsetName) {
            // TODO Auto-generated method stub
            String text = null;
            try {
                text = new String(encode(data.getBytes(charsetName)));
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return text;
        }

        @Override
        public String decode(String data, String charsetName) {
            // TODO Auto-generated method stub
            try {
                return new String(decode(data.toCharArray()), charsetName);
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return "";
        }
    };
}