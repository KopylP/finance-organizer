package com.kopyl.financeorganaizer.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ExpenseSlideService extends IntentService {

    public final static String INTENT_FILTER_ID_1 = "intent filter id 1";
    private static final String ACTION_SLIDE = "com.kopyl.financeorganaizer.Services.action.slide";
    private static final String ACTION_STOP = "com.kopyl.financeorganaizer.Services.action.stop";

    public static final String EXTRA_OUT_STOP = "com.kopyl.financeorganaizer.Services.action.OUT_STOP";
    private boolean mStopped;
    private int mCount;


    private static final String EXTRA_COUNT = "com.kopyl.financeorganaizer.Services.extra.COUNT";


    public ExpenseSlideService() {
        super("ExpenseSlideService");
    }


    public static Intent startActionSlide(Context context, int count) {
        Intent intent = new Intent(context, ExpenseSlideService.class);
        intent.setAction(ACTION_SLIDE);
        intent.putExtra(EXTRA_COUNT, count);
        context.startService(intent);
        return intent;
    }

    public static void stopActionSlide(Context context, Intent intent) {
        //context.stopService(new Intent(context, ExpenseSlideService.class));

        //intent1.setAction(ACTION_STOP);
        intent.setAction(ACTION_STOP);
        context.stopService(intent);
        context.startService(intent);
         //context.startService(intent1);

    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SLIDE.equals(action)) {
                mCount = intent.getIntExtra(EXTRA_COUNT, 0);
                try {
                    handleActionSlide(intent);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(ACTION_STOP.equals(action)){
                mStopped = true;
                stopSelf();
                //onDestroy();
            }
        }
    }


    private void handleActionSlide(Intent intent) throws InterruptedException {
        Intent out_intent = null;
        for(int i = 0; i < mCount; i++) {
            String action = intent.getAction();
            if(action.equals(ACTION_STOP) || mStopped){
                stopSelf();
                break;
            }
            Thread.sleep(2000);
            out_intent = new Intent(INTENT_FILTER_ID_1);
            sendBroadcast(out_intent);
        }
        out_intent = new Intent(INTENT_FILTER_ID_1);
        out_intent.putExtra(EXTRA_OUT_STOP, "Stop");
        sendBroadcast(out_intent);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        mStopped = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mStopped = true;
    }
}
