package com.orange.oglite_glitter

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import java.io.*

class FileUtil {
    private val path = Environment.getExternalStorageDirectory().toString() + "/"
    private val TAG = FileUtil::class.java.name
    var sDPATH: String? = null
        private set
    private val context: Context? = null

    constructor(context: Context?) {
        sDPATH = Environment.getExternalStorageDirectory().toString() + "/"
    }

    constructor(SDPATH: String?) {
        //得到外部存储设备的目录（/SDCARD）
        var SDPATH = SDPATH
        SDPATH = Environment.getExternalStorageDirectory().toString() + "/"
    }

    /**
     * 在SD卡上创建文件
     * @param fileName
     * @return
     * @throws java.io.IOException
     */
    @Throws(IOException::class)
    fun createSDFile(fileName: String): File {
        Log.d(TAG, "createSDFile: " + sDPATH + fileName)
        val file = File(sDPATH + fileName)
        file.createNewFile()
        return file
    }

    /**
     * 在SD卡上创建目录
     * @param dirName 目录名字
     * @return 文件目录
     */
    fun createDir(dirName: String): File {
        val dir = File(sDPATH + dirName)
        if (!dir.exists()) {
            dir.mkdir()
        }
        return dir
    }

    /**
     * 判断文件是否存在
     * @param fileName
     * @return
     */
    fun isFileExist(fileName: String): Boolean {
        val file = File(sDPATH + fileName)
        return file.exists()
    }

    interface FilePrograss {
        fun progress(total: Int, progress: Int)
        fun finish(total: Int)
        fun start(total: Int)
        fun fail(msg: String?)
    }

    constructor() {
        val file = File(path)
        /**
         * 如果文件夹不存在就创建
         */
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    /**
     * 创建一个文件
     * @param FileName 文件名
     * @return
     */
    fun createFile(dir: String, FileName: String): File {
        val myPath = path + dir
        Log.d(TAG, "download myPath:$myPath,filename:$FileName")
        return File(myPath, FileName)
    }

    /**
     * 获取字节流
     * @param path
     * @param fileName
     * @param input
     * @return
     */
    fun write2SDFromInput(path: String, fileName: String, input: InputStream, filePrograss: FilePrograss): File? {
        var file: File? = null
        var output: OutputStream? = null
        Log.d(TAG, String.format("path:%s,fileName:%s", path, fileName))
        try {
            val totalByte = input.available()
            filePrograss.start(totalByte)
            createDir(path)
            file = createSDFile(path + fileName)
            output = FileOutputStream(file)
            val buffer = ByteArray(4 * 1024)
            var readByte = 0 // 已经读到的字节数
            var hasReadByte = 0 // 已经完成的字节数
            while (input.read(buffer).also { readByte = it } != -1) {
                hasReadByte += readByte
                output.write(buffer)
                output.flush()
                filePrograss.progress(totalByte, hasReadByte)
            }
            filePrograss.finish(totalByte)
        } catch (e: IOException) {
            filePrograss.fail(e.message)
            e.printStackTrace()
        } finally {
            try {
                output!!.close()
            } catch (e: IOException) {
                filePrograss.fail(e.message)
                e.printStackTrace()
            }
        }
        return file
    }

    companion object {
        /**
         * 覆盖复制单个文件
         *
         * @param oldPathName String 原文件路径+文件名 如：data/user/0/com.test/files/abc.txt
         * @param newPathName String 复制后路径+文件名 如：data/user/0/com.test/cache/abc.txt
         * @return 复制结果
         */
        fun copyFile(oldPathName: String?, newPathName: String?): Boolean {
            val oldFile = File(oldPathName)
            val newFile = File(newPathName)
            try {
                //删除目标文件
                if (newFile.exists()) {
                    newFile.delete()
                }
                //创建目标文件
                if (!newFile.exists()) {
                    newFile.createNewFile()
                }
                val inStream = FileInputStream(oldFile)
                val outStream = FileOutputStream(newFile)
                val inChannel = inStream.channel
                val outChannel = outStream.channel
                inChannel.transferTo(0, inChannel.size(), outChannel)
                inStream.close()
                outStream.close()
                //复制完删除源文件
                oldFile.delete()
            } catch (e: IOException) {
                e.printStackTrace()
                return false
            }
            return true
        }

        fun getPath(context: Context, uri: Uri): String? {
            val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

            // DocumentProvider
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    val docId = DocumentsContract.getDocumentId(uri)
                    //                Log.i(TAG,"isExternalStorageDocument***"+uri.toString());
//                Log.i(TAG,"docId***"+docId);
//                以下是打印示例：
//                isExternalStorageDocument***content://com.android.externalstorage.documents/document/primary%3ATset%2FROC2018421103253.wav
//                docId***primary:Test/ROC2018421103253.wav
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    if ("primary".equals(type, ignoreCase = true)) {
                        return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                    }
                } else if (isDownloadsDocument(uri)) {
//                Log.i(TAG,"isDownloadsDocument***"+uri.toString());
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                    return getDataColumn(context, contentUri, null, null)
                } else if (isMediaDocument(uri)) {
//                Log.i(TAG,"isMediaDocument***"+uri.toString());
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split = docId.split(":").toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf(split[1])
                    return getDataColumn(context, contentUri, selection, selectionArgs)
                }
            } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//            Log.i(TAG,"content***"+uri.toString());
                return getDataColumn(context, uri, null, null)
            } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            Log.i(TAG,"file***"+uri.toString());
                return uri.path
            }
            return null
        }

        /**
         * Get the value of the data column for this Uri. This is useful for
         * MediaStore Uris, and other file-based ContentProviders.
         *
         * @param context       The context.
         * @param uri           The Uri to query.
         * @param selection     (Optional) Filter used in the query.
         * @param selectionArgs (Optional) Selection arguments used in the query.
         * @return The value of the _data column, which is typically a file path.
         */
        fun getDataColumn(
            context: Context, uri: Uri?, selection: String?,
            selectionArgs: Array<String>?
        ): String? {
            var cursor: Cursor? = null
            val column = "_data"
            val projection = arrayOf(column)
            try {
                cursor = context.contentResolver.query(
                    uri!!, projection, selection, selectionArgs,
                    null
                )
                if (cursor != null && cursor.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(column)
                    return cursor.getString(column_index)
                }
            } finally {
                cursor?.close()
            }
            return null
        }

        fun isExternalStorageDocument(uri: Uri): Boolean {
            return "com.android.externalstorage.documents" == uri.authority
        }

        fun isDownloadsDocument(uri: Uri): Boolean {
            return "com.android.providers.downloads.documents" == uri.authority
        }

        fun isMediaDocument(uri: Uri): Boolean {
            return "com.android.providers.media.documents" == uri.authority
        }
        fun getFileFromMediaUri(ac: Context, uri: Uri): File? {
            if (uri.scheme.toString().compareTo("content") == 0) {
                val cr: ContentResolver = ac.contentResolver
                val cursor: Cursor? = cr.query(uri, null, null, null, null) // 根據Uri從資料庫中找
                if (cursor != null) {
                    cursor.moveToFirst()
                    val filePath = cursor.getString(cursor.getColumnIndex("_data")) // 獲取圖片路徑
                    cursor.close()
                    if (filePath != null) {
                        return File(filePath)
                    }
                }
            } else if (uri.scheme.toString().compareTo("file") == 0) {
                return File(uri.toString().replace("file://", ""))
            }
            return null
        }

        /**
         * 質量壓縮方法
         *
         * @param image
         * @return
         */
        fun compressImage(image: Bitmap): Bitmap? {
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos) //質量壓縮方法，這裡100表示不壓縮，把壓縮後的資料存放到baos中
            var options = 100
            while (baos.toByteArray().size / 1024 > 50) { //迴圈判斷如果壓縮後圖片是否大於100kb,大於繼續壓縮
                baos.reset() //重置baos即清空baos
                //第一個引數 ：圖片格式 ，第二個引數： 圖片質量，100為最高，0為最差 ，第三個引數：儲存壓縮後的資料的流
                image.compress(Bitmap.CompressFormat.JPEG, options, baos) //這裡壓縮options%，把壓縮後的資料存放到baos中
                options -= 10 //每次都減少10
            }
            val isBm =
                ByteArrayInputStream(baos.toByteArray()) //把壓縮後的資料baos存放到ByteArrayInputStream中
            return BitmapFactory.decodeStream(isBm, null, null)
        }

        /**
         * 通過uri獲取圖片並進行壓縮
         *
         * @param uri
         */
        @Throws(FileNotFoundException::class, IOException::class)
        fun getBitmapFormUri(ac: Context, uri: Uri): Bitmap? {
            var input: InputStream? = ac.getContentResolver().openInputStream(uri)
            val onlyBoundsOptions = BitmapFactory.Options()
            onlyBoundsOptions.inJustDecodeBounds = true
            onlyBoundsOptions.inDither = true //optional
            onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            BitmapFactory.decodeStream(input, null, onlyBoundsOptions)
            input?.close()
            val originalWidth = onlyBoundsOptions.outWidth
            val originalHeight = onlyBoundsOptions.outHeight
            if (originalWidth == -1 || originalHeight == -1) return null
            //圖片解析度以480x800為標準
            val hh = 800f //這裡設定高度為800f
            val ww = 480f //這裡設定寬度為480f
            //縮放比。由於是固定比例縮放，只用高或者寬其中一個資料進行計算即可
            var be = 1 //be=1表示不縮放
            if (originalWidth > originalHeight && originalWidth > ww) { //如果寬度大的話根據寬度固定大小縮放
                be = (originalWidth / ww).toInt()
            } else if (originalWidth < originalHeight && originalHeight > hh) { //如果高度高的話根據寬度固定大小縮放
                be = (originalHeight / hh).toInt()
            }
            if (be <= 0) be = 1
            //比例壓縮
            val bitmapOptions = BitmapFactory.Options()
            bitmapOptions.inSampleSize = be //設定縮放比例
            bitmapOptions.inDither = true //optional
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888 //optional
            input = ac.getContentResolver().openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions)
            input?.close()
            return compressImage(bitmap!!) //再進行質量壓縮
        }
    }
}