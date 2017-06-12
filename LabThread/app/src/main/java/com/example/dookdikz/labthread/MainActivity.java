package com.example.dookdikz.labthread;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {
    int counter = 0;
    TextView tvCounter;
    Thread thread;
    Handler handler;
    HandlerThread backgroundHandlerThread;
    Handler backgroundHandler;
    Handler mainHandler;
    simpleAsyncTask simpleAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvCounter = (TextView) findViewById(R.id.tvCounter);

//Thread Method1
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //background Thread
//                for (int i = 0; i < 100; i++) {
//                    counter++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        return;
//
//                    }
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //MainThread = UIThread
//                            tvCounter.setText(String.valueOf(counter));
//                        }
//                    });
//
//                }
//            }
//        });
//        thread.start();


//Thread Method2
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //Run in MainThread
//                tvCounter.setText(String.valueOf(msg.arg1));
//            }
//        };
//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                //background Thread
//                for (int i = 0; i < 100; i++) {
//                    counter++;
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        return;
//
//                    }
//                    Message message = new Message();
//                    message.arg1 = counter;
//                    handler.sendMessage(message);
//
//                }
//            }
//        });
//        thread.start();


//Thread Method 3
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //Run in MainThread
//                counter++;
//                tvCounter.setText(counter + "");
//                if (counter < 100) {
//                    sendEmptyMessageDelayed(0, 1000);
//                }
//
//            }
//        };
//        handler.sendEmptyMessageDelayed(0, 1000);


        //Thread Method 4
//        backgroundHandlerThread = new HandlerThread("BackgroundHandlerThread");
//        backgroundHandlerThread.start();
//        backgroundHandler = new Handler(backgroundHandlerThread.getLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //run in background
//                Message msgMain = new Message();
//                msgMain.arg1 = msg.arg1+1;
//                mainHandler.sendMessage(msgMain);
//            }
//        };
//        mainHandler = new Handler(Looper.getMainLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                //run in main
//                tvCounter.setText(msg.arg1+"");
//                if(msg.arg1<100){
//                    Message msgBack = new Message();
//                    msgBack.arg1 = msg.arg1;
//                    backgroundHandler.sendMessageDelayed(msgBack,1000);
//                }
//            }
//        };
//
//        Message msgBack = new Message();
//        msgBack.arg1 = 0;
//        backgroundHandler.sendMessageDelayed(msgBack,1000);


        //Thread Method 5 AsyncTask
//        simpleAsyncTask = new simpleAsyncTask();
//        simpleAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,0,100);
        getSupportLoaderManager().initLoader(1, null, this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //thread.destroy();
//        backgroundHandlerThread.quit();
//        simpleAsyncTask.cancel(true);
    }

    @Override
    public Loader<Object> onCreateLoader(int id, Bundle args) {
        if (id == 1) {
            return new adderAcyncTaskLoader(MainActivity.this, 5, 11);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        if (loader.getId() == 1) {
            Integer result = (Integer) data;
            tvCounter.setText(result + "");
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    static class adderAcyncTaskLoader extends AsyncTaskLoader<Object> {
        int a;
        int b;
        Integer result;
Handler handler;
        public adderAcyncTaskLoader(Context context, int a, int b) {
            super(context);
            this.a = a;
            this.b = b;
        }

        @Override
        protected void onStartLoading() {

            super.onStartLoading();
            if(result != null){
                deliverResult(result);
            }
            if(handler == null){
                handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        a = (int)(Math.random()*100);
                        b = (int)(Math.random()*100);
                        onContentChanged();
                        handler.sendEmptyMessageDelayed(0,3000);


                    }
                };
                handler.sendEmptyMessageDelayed(0,3000);
            }
            if(takeContentChanged() || result == null){
                forceLoad();
            }

        }
        @Override
        public Integer loadInBackground() {
            //do in background;
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//
//            }
            result = a + b;
            return result;
        }

        @Override
        protected void onStopLoading() {
            super.onStopLoading();

        }

        @Override
        protected void onReset() {
            super.onReset();
            if(handler!=null){
                handler.removeCallbacksAndMessages(null);
                handler= null;
            }
        }


    }

    class simpleAsyncTask extends AsyncTask<Integer, Float, Boolean> {

        @Override
        protected Boolean doInBackground(Integer... integers) {
            //run in background
            int start = integers[0];
            int end = integers[1];
            for (int i = start; i < end; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    return false;
                }
                publishProgress(i + 0.0f);
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            float progress = values[0];
            tvCounter.setText(progress + "%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //run in main
            //work with boolean
        }
    }
}
