package at.fancycardgame.aauno;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.UserService;

import at.fancycardgame.aauno.toolbox.Tools;

/**
 * Created by Christian on 01.06.2015.
 */
public class ChangePwActivity extends Activity implements View.OnClickListener {

View.OnClickListener mainOnClickListener = this;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_changepwd_page);
        findViewById(R.id.btnChangePWD).setOnClickListener(mainOnClickListener);

    }


    public void onClick(View clickedView){
        int clickedID = clickedView.getId();

        if(clickedID == R.id.btnChangePWD){

                String oldPwd = ((TextView) findViewById(R.id.txtBoxOldPwd)).getText().toString();
                String newPwd = ((TextView) findViewById(R.id.txtBoxNewPwd)).getText().toString();
                changePassword(User.getUsername(), oldPwd, newPwd);
                Tools.showToast("Password successfully Changed", Toast.LENGTH_SHORT);
                ChangePwActivity.this.finish();




        }
    }

    private void changePassword(String username, String oldPwd, String newPwd) {
        UserService userService = App42API.buildUserService();
        userService.changeUserPassword(username, oldPwd, newPwd, new App42CallBack() {
           @Override
            public void onSuccess(Object o) {
                //show User a message that password has changed successfully
            }
            @Override
            public void onException(Exception e) {
                //show User a message that password has not changed
            }
        });
    }

}
