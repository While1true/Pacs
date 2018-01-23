package coms.pacs.pacs.Utils

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import coms.pacs.pacs.App
import coms.pacs.pacs.Model.Progress
import coms.pacs.pacs.Room.DownStatu
import coms.pacs.pacs.Room.DownloadDao.Companion.downDao
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import java.io.File


/**
 * Created by 不听话的好孩子 on 2018/1/23.
 */
class DownLoadUtils {
    class DownObserver : ObservableOnSubscribe<Progress> {
        val id: Long
        val wait: Long
        val progress: Progress

        constructor(id: Long, wait: Long) {
            this.id = id
            this.wait = wait
            progress = Progress(id, 0L, 0L, "")
        }

        constructor(id: Long) : this(id, 1000)

        override fun subscribe(e: ObservableEmitter<coms.pacs.pacs.Model.Progress>) {
            val query = DownloadManager.Query()
            query.setFilterById(id)
            val path = downDao.get(id).path
            progress.file = path
            var cursor = downloadManager.query(query)

            if (cursor == null) {
                Thread.sleep(1000)
                cursor = downloadManager.query(query)
            }
            if (cursor == null && !e.isDisposed) {
                e.onError(Throwable("获取不到记录"))
                return
            }
            if (!cursor!!.moveToFirst() && !e.isDisposed) {
                e.onError(Throwable("获取不到记录"))
                return
            }
            var state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            while (state != DownloadManager.STATUS_FAILED) {
                var current = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                var total = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                progress.total = total
                progress.current = current
                if (total != -1L)
                    e.onNext(progress)
                if (state == DownloadManager.STATUS_SUCCESSFUL) {
                    break
                }
                cursor.close()
                Thread.sleep(wait)
                cursor = downloadManager.query(query)
                cursor.moveToFirst()
                state = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }
            if (state == DownloadManager.STATUS_SUCCESSFUL) {
                e.onComplete()
            } else {
                e.onError(Throwable("下载失败"))
            }
            cursor.close()
        }
    }

    companion object {
        private val downloadManager: DownloadManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { App.app.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager }

        fun download(url: String, downloadFile: File, showNotify: Boolean, notifyName: String, notifyDescription: String): Long {
            val uriStr = Uri.parse(url)
            val request = DownloadManager.Request(uriStr)
            request.setAllowedOverRoaming(false)

            val isshow = if (showNotify) DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED else DownloadManager.Request.VISIBILITY_HIDDEN
            request.setNotificationVisibility(isshow)
            request.setTitle(notifyName)
            request.setDescription(notifyDescription)
            request.setVisibleInDownloadsUi(true)

            request.setDestinationInExternalPublicDir(downloadFile.parent, downloadFile.name)
            val enqueue = downloadManager.enqueue(request)

            //room保存状态
            downDao.insert(DownStatu(enqueue, downloadFile.name, downloadFile.absolutePath, url))


            return enqueue
        }

        fun download(url: String, downloadFile: File, shownotify: Boolean): Long {
            return download(url, downloadFile, shownotify, downloadFile.name, "正在下载中...")
        }

        fun download(url: String, shownotify: Boolean): Long {
            var name = ""
            val lastIndexOf = url.lastIndexOf("/")
            if (lastIndexOf == -1)
                name += System.currentTimeMillis()
            else
                name += url.substring(lastIndexOf + 1)
            return download(url, File(Environment.DIRECTORY_DOWNLOADS, name), shownotify)
        }

        fun download(url: String): Long {
            return download(url, true)
        }

    }


}