package Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myproject.MainActivity;
import com.example.myproject.R;
import com.example.myproject.TaskDAO.Task;
import com.example.myproject.bottomsheetFragment;
import com.example.myproject.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TaskAdapter  extends RecyclerView.Adapter<TaskAdapter.MyHolder>{


    private FragmentActivity context;
    Date date = null;
    public List<Task> taskList;
    public SimpleDateFormat dateFormat = new SimpleDateFormat("EE dd MMM yyyy", Locale.US);
    public SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-M-yyyy", Locale.US);
    String outputDateString = null;
    List<Task> taskSource;
    private Timer timer;


public TaskAdapter(){

}

    public TaskAdapter(FragmentActivity context,List<Task> taskList) {
        this.context=context;
        this.taskList=taskList;
        taskSource=taskList;


    }

    @NonNull
    @Override
    public TaskAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View v= layoutInflater.inflate(R.layout.item_task,parent,false);
        MyHolder holder=new MyHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskAdapter.MyHolder holder, int position) {

        if(taskList.size()>0) {
            Task task = taskList.get(position);
            holder.title.setText(task.getTaskTitle());
            holder.description.setText(task.getTaskDescription());
            holder.time.setText(task.getLastAlarm());
            holder.status.setText(task.isComplete() ? "COMPLETED" : "UPCOMING");
            holder.options.setOnClickListener(view -> showPopUpMenu(view, position));

            try {
                date = inputDateFormat.parse(task.getDate());
                outputDateString = dateFormat.format(date);

                String[] items1 = outputDateString.split(" ");
                String day = items1[0];
                String dd = items1[1];
                String month = items1[2];

                holder.day.setText(day);
                holder.date.setText(dd);
                holder.month.setText(month);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public int getItemCount() {
        return taskList.size()
                ;
    }
    public class MyHolder extends RecyclerView.ViewHolder{
        TextView day;
        TextView date;
        TextView month;
        TextView title;
        TextView description;
        TextView time;
        TextView status;
        ImageView options;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.day);
            date=itemView.findViewById(R.id.date);
            month=itemView.findViewById(R.id.month);
            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            time=itemView.findViewById(R.id.time);
            status=itemView.findViewById(R.id.status);
            options=itemView.findViewById(R.id.options);



        }
    }

    public void showPopUpMenu(View view, int position) {
        final Task task = taskList.get(position);
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.optionsmenu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menuDelete:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    alertDialogBuilder.setTitle(R.string.delete_confirmation).setMessage(R.string.sureToDelete).
                            setPositiveButton(R.string.yes, (dialog, which) -> {
                                DatabaseHelper dh=new DatabaseHelper(context);
                                dh.deleteTask(task.getTaskId());
                                taskList.remove(position);
                                //notifyItemRemoved(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Task Deleted Successfully!!", Toast.LENGTH_LONG).show();


                            })
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;

                case R.id.menuUpdate:
                    bottomsheetFragment createTaskBottomSheetFragment = new bottomsheetFragment();
                    createTaskBottomSheetFragment.setTaskId(task.getTaskId(), true,  context);
                    createTaskBottomSheetFragment.show(context.getSupportFragmentManager(), createTaskBottomSheetFragment.getTag());

                    break;

                case R.id.menuComplete:
                    AlertDialog.Builder completeAlertDialog = new AlertDialog.Builder(context, R.style.AppTheme_Dialog);
                    completeAlertDialog.setTitle(R.string.confirmation).setMessage(R.string.sureToMarkAsComplete).
                            setPositiveButton(R.string.yes, (dialog, which) -> showCompleteDialog(task.getTaskId(), position))
                            .setNegativeButton(R.string.no, (dialog, which) -> dialog.cancel()).show();
                    break;

            }
            return false;
        });
        popupMenu.show();
    }

    public void showCompleteDialog(int taskId, int position) {
        Dialog dialog = new Dialog(context, R.style.AppTheme);
        dialog.setContentView(R.layout.dialog_completed_theme);
        Button close = dialog.findViewById(R.id.closeButton);
        close.setOnClickListener(view -> {
            DatabaseHelper dh=new DatabaseHelper(context);
            dh.deleteTask(taskId);
            taskList.remove(position);
            //notifyItemRemoved(position);
            notifyDataSetChanged();
            dialog.dismiss();
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }


    public void searchNotes(final String searchKeyword){
        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (searchKeyword.trim().isEmpty()){
                    taskList=taskSource;
                }else{
                    ArrayList<Task> temp=new ArrayList<>();
                    for (Task task:taskSource){
                        if (task.getTaskTitle().toLowerCase().contains(searchKeyword.toLowerCase())
                                || task.getTaskDescription().toLowerCase().contains(searchKeyword.toLowerCase())
                                ){
                            temp.add(task);
                        }
                    }
                    taskList=temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        },500);
    }

    public void cancelTimer(){
        if(timer != null){
            timer.cancel();
        }
    }

}
