package coms.pacs.pacs.Utils.MI;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import java.util.List;
import coms.pacs.pacs.Utils.K2JUtils;

public class MiPushReceiver extends PushMessageReceiver {
    String mRegId;

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        }
        super.onCommandResult(context, message);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                K2JUtils.put("miID",mRegId);
            }
        }
    }


    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        String mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            String mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            String mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            String mUserAccount = message.getUserAccount();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        String mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            String mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            String mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            String mUserAccount = message.getUserAccount();
        }

    }


    /**
     * @param context
     * @param message
     */

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        String mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            String mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            String mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            String mUserAccount = message.getUserAccount();
        }

    }

}
