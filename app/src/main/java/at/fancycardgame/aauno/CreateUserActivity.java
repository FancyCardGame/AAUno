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
public class CreateUserActivity extends Activity implements View.OnClickListener {

    private View.OnClickListener mainOnClickListener = this;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_createuser_page);
        findViewById(R.id.btnCreateUser).setOnClickListener(mainOnClickListener);

 }

    public void onClick(View clickedView){
        int clickedID = clickedView.getId();

        if(clickedID == R.id.btnCreateUser){

            String username = ((TextView)findViewById(R.id.txtBoxUsername)).getText().toString();
            String password = ((TextView)findViewById(R.id.txtBoxPwd)).getText().toString();
            String email = ((TextView)findViewById(R.id.txtBoxMail)).getText().toString();
            this.createUser(username, password, email);
        }


    }

    private void createUser(String username, String password, String email) {

        UserService userService = App42API.buildUserService();
        userService.createUser(username, password, email, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {

                Tools.showToast("User successfully created.", Toast.LENGTH_SHORT);
                //User user = (User)response;
                //Toast.makeText(getApplicationContext(),"Successfully created User.", Toast.LENGTH_SHORT);

                //
            }
            @Override
            public void onException(Exception ex) {
                Tools.showToast("Error creating user. ERROR: " + ex.getMessage(), Toast.LENGTH_SHORT);
            }

        });

}


}