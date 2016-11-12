package it.slyce.messaging.utils.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import it.slyce.messaging.message.Message;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.messageItem.MessageItem;
import it.slyce.messaging.message.messageItem.MessageRecyclerAdapter;
import it.slyce.messaging.utils.CustomSettings;
import it.slyce.messaging.utils.MessageUtils;

/**
 * @Author Matthew Page
 * @Date 7/13/16
 */
public class AddNewMessageTask extends AsyncTask {
    private List<Message> messages;
    private List<MessageItem> mMessageItems;
    private MessageRecyclerAdapter mRecyclerAdapter;
    private RecyclerView mRecyclerView;
    private Context context;
    private CustomSettings customSettings;
    private int rangeStartingPoint;
    private String snackbarString;
    private boolean isFirst;

    public AddNewMessageTask(
            List<Message> messages,
            String snackbarString,
            List<MessageItem> mMessageItems,
            MessageRecyclerAdapter mRecyclerAdapter,
            RecyclerView mRecyclerView,
            Context context,
            CustomSettings customSettings,
            boolean isFirst) {
        this.messages = messages;
        this.snackbarString = snackbarString;
        this.mMessageItems = mMessageItems;
        this.mRecyclerAdapter = mRecyclerAdapter;
        this.mRecyclerView = mRecyclerView;
        this.context = context;
        this.customSettings = customSettings;
        this.isFirst = isFirst;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        this.rangeStartingPoint = mMessageItems.size() - 1;
        for (Message message : messages) {
            if (context == null) {
                return null;
            }
            mMessageItems.add(message.toMessageItem(context)); // this call is why we need the AsyncTask
        }

        for (int i = rangeStartingPoint; i < mMessageItems.size(); i++) {
            MessageUtils.markMessageItemAtIndexIfFirstOrLastFromSource(i, mMessageItems);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if (o != null)
            return;
        boolean isAtBottom = !mRecyclerView.canScrollVertically(1);
        boolean isAtTop = !mRecyclerView.canScrollVertically(-1);
        if (isFirst) {
            mRecyclerAdapter.notifyItemRangeInserted(rangeStartingPoint + 1, messages.size() - rangeStartingPoint - 1);
            mRecyclerAdapter.notifyItemChanged(rangeStartingPoint);
        } else {
            mRecyclerAdapter.sortListByDateAndNotify();
        }
        if (isAtBottom || messages.get(messages.size() - 1).getSource() == MessageSource.LOCAL_USER)
            mRecyclerView.scrollToPosition(mRecyclerAdapter.getItemCount() - 1);
        else {
            /*if (isAtTop) {
                ScrollUtils.scrollToTopAfterDelay(mRecyclerView, mRecyclerAdapter);
            }*/
            LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (manager.findFirstCompletelyVisibleItemPosition() <= 45) {
                // This is to keep the position
                mRecyclerView.scrollToPosition(manager.findFirstVisibleItemPosition() + 200);
            }
            Snackbar snackbar = Snackbar.make(mRecyclerView, snackbarString, Snackbar.LENGTH_SHORT)
                    .setAction("VIEW", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mRecyclerView.smoothScrollToPosition(mRecyclerAdapter.getItemCount() - 1);
                        }
                    }).setActionTextColor(customSettings.snackbarButtonColor);
            ViewGroup group = (ViewGroup) snackbar.getView();
            for (int i = 0; i < group.getChildCount(); i++) {
                View v = group.getChildAt(i);
                if (v instanceof TextView) {
                    TextView textView = (TextView) v;
                    textView.setTextColor(customSettings.snackbarTitleColor);
                }
            }
            snackbar.getView().setBackgroundColor(customSettings.snackbarBackground);
            snackbar.show();
        }
    }
}
