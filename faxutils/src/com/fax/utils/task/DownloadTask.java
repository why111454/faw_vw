package com.fax.utils.task;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.fax.utils.http.HttpUtils;
import com.fax.utils.http.HttpUtils.DownloadListener;
import com.fax_utils.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

/**文件下载任务 */
public abstract class DownloadTask extends ResultAsyncTask<File> {
	HttpRequestBase request;
	File file;
	boolean isContinueDown;
	boolean isShowSize = true;
	Handler handler;
    public DownloadTask(Context context, String url){
        this(context, new HttpGet(url), createTempDownFile(context, url), false);
    }
	public DownloadTask(Context context, String url, File file){
        this(context, new HttpGet(url), file, false);
    }
    public DownloadTask(Context context, String url, File file, boolean isContinueDown){
        this(context, new HttpGet(url), file, isContinueDown);
    }
    public DownloadTask(Context context, HttpRequestBase request, File file, boolean isContinueDown){
        super(context);
        this.request=request;
        this.file = file;
        this.isContinueDown = isContinueDown;
        handler = new Handler(Looper.getMainLooper());
        setToast(R.string.Task_DownloadComplete, R.string.Task_DownloadFail);
    }
    public void setShowSize(boolean isShowSize) {
		this.isShowSize = isShowSize;
	}
	@Override
    protected boolean performCancel(boolean mayInterruptIfRunning) {
        try {
            if(request!=null) request.abort();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.performCancel(mayInterruptIfRunning);
    }
	@Override
	protected File doInBackground(Void... params) {
		return HttpUtils.reqForDownload(request, file, isContinueDown, new DownloadListener() {
			long lastTime;
			@Override
			public void onDownloading(final long loaded,final  long total) {
				long nowTime = System.currentTimeMillis();
				if(nowTime - lastTime > 1000){//最多1s一次回调
					lastTime = nowTime;
					final int percent = (int) (loaded * 100 /total);
					handler.post(new Runnable() {
						@Override
						public void run() {
							getProgressDialog().setProgress(percent);
							String message = context.getString(R.string.Task_Downloading);
							if(isShowSize){
								message += sizeToString(loaded)+"/"+sizeToString(total);
							}
							getProgressDialog().setMessage(message);
						}
					});
				}
			}
			@Override
			public void onDownloadFinish(File file) {
				//已经return回去了。不管
			}
			@Override
			public void onDownloadError(String error) {
//				setToast(null, context.getString(resId))
			}
		});
	}

    @Override
	public ResultAsyncTask<File> setProgressDialog(boolean cancelAble, String waitMsg) {
    	ResultAsyncTask<File> task = super.setProgressDialog(cancelAble, waitMsg);
    	
    	if(cancelAble){//可以被取消就加入停止按钮
    		getProgressDialog().setButton(DialogInterface.BUTTON_NEGATIVE, 
    				context.getString(R.string.Task_StopDownload), new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
    	}
    	return task;
	}
	@Override
	public ResultAsyncTask<File> setProgressDialog(ProgressDialog pd) {
		pd.setTitle(R.string.Task_PleaseWait);
		pd.setMessage(context.getString(R.string.Task_Downloading));
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setOnDismissListener(new DialogInterface.OnDismissListener(){
			@Override
			public void onDismiss(DialogInterface dialog) {
				cancel(true);
			}
		});
		return super.setProgressDialog(pd);
	}
	/**下载到储存卡的应用缓存目录 */
	public static File createTempDownFile(Context context, String url) {
    	try {
			String fileName = new File(new URL(url).getFile()).getName();
			return new File(context.getExternalCacheDir(), fileName);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String[] sizeunits={"B","KB","MB","GB","TB"};
	public static String sizeToString(float size){
		if(size<0) return "?";
    	int i=0;
    	for (i=0;i<5;i++){
    		if(size<1024) break;
    		size=size/1024;
    	}
    	return String.format("%.1f", size)+sizeunits[i];
	}
}
