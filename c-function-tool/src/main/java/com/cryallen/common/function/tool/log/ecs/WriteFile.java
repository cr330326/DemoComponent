package com.cryallen.common.function.tool.log.ecs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

/**
 * Created by chenran3 on 2018/1/3.
 */

public class WriteFile {

    private String filePath = "";
    private long max_size = 10485760L;
    private boolean createFile = true;

    public WriteFile(String filePath, int max_size) {
        this.filePath = filePath;
        this.max_size = (long)max_size;
    }

    protected void write(String sb) {
        File file = this.checkFile(this.filePath);
        if(file != null) {
            RandomAccessFile raf = null;

            try {
                raf = new RandomAccessFile(file, "rw");
                raf.seek(file.length());
                raf.write(sb.getBytes());
            } catch (FileNotFoundException var15) {
                var15.printStackTrace();
            } catch (IOException var16) {
                var16.printStackTrace();
            } finally {
                if(raf != null) {
                    try {
                        raf.close();
                    } catch (IOException var14) {
                        var14.printStackTrace();
                    }
                }

            }

        }
    }

    private File checkFile(String filePath) {
        if(filePath != null && !"".equals(filePath) && this.createFile) {
            String fileName = filePath.endsWith("/")?filePath + ECS.LOG_FILE_NAME:filePath + "/" + ECS.LOG_FILE_NAME;
            File file = new File(fileName);
            if(!file.exists()) {
                try {
                    if(!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }

                    boolean result = file.createNewFile();
                    if(!result) {
                        return null;
                    }
                } catch (IOException var6) {
                    var6.printStackTrace();
                    this.createFile = false;
                }
            }

            long size = file.length();
            if(size > this.max_size) {
                file = createCpyFile(file, true);
            }

            return file.exists()?file:null;
        } else {
            return null;
        }
    }

    public static File createCpyFile(File oldFile, boolean deleteOld) {
        if(oldFile == null) {
            return null;
        } else {
            File newFile = null;

            try {
                newFile = new File(oldFile.getCanonicalPath() + ".bak");
                if(newFile.exists()) {
                    newFile.delete();
                }

                copyFile(oldFile, newFile);
                if(deleteOld) {
                    oldFile.delete();
                }
            } catch (IOException var4) {
                var4.printStackTrace();
            }

            return oldFile;
        }
    }

    public static void inputStreamToFile(InputStream is, File file) {
        if(is != null && file != null) {
            BufferedReader br = null;
            BufferedWriter bw = null;
            String line = "";

            try {
                br = new BufferedReader(new InputStreamReader(is));
                bw = new BufferedWriter(new FileWriter(file));

                while((line = br.readLine()) != null) {
                    bw.write(line);
                    bw.write("\n");
                }

                bw.flush();
            } catch (IOException var9) {
                var9.printStackTrace();
            } finally {
                closeBufferedFileReader(br);
                closeBufferedFileWriter(bw);
            }

        }
    }

    public static void copyFile(File oldFile, File newFile) {
        if(oldFile != null && newFile != null) {
            try {
                inputStreamToFile(new FileInputStream(oldFile), newFile);
            } catch (FileNotFoundException var3) {
                var3.printStackTrace();
            }

        }
    }

    public static File createFile(String file) {
        if(file != null && !"".equals(file)) {
            File createFile = new File(file);
            if(createFile.exists()) {
                return createFile;
            } else {
                File parent = createFile.getParentFile();
                if(!parent.exists()) {
                    parent.mkdir();
                }

                try {
                    createFile.createNewFile();
                } catch (IOException var4) {
                    var4.printStackTrace();
                }

                return createFile;
            }
        } else {
            return null;
        }
    }

    private static void closeBufferedFileWriter(BufferedWriter bw) {
        if(bw != null) {
            try {
                bw.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }

    private static void closeBufferedFileReader(BufferedReader br) {
        if(br != null) {
            try {
                br.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }

    }
}
