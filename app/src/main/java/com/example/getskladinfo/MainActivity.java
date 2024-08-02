package com.example.getskladinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button scanBtn;
    TextView messageText, messageFormat;
    private static String ip = "192.168.0.198";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "TRXSQL3";// the data base name
    private static String username = "litvinov";// the user name
    private static String password = "lv23";// the password
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database; // the connection url string
    private Connection connection = null;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
          scanBtn = findViewById(R.id.scanBtn);
          messageText = findViewById(R.id.skladID);
          //messageFormat= findViewById(R.id.Nme);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btn=(Button)findViewById(R.id.btnGetData );
          scanBtn.setOnClickListener(this);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //start(v);
            }
        });
    }
    @Override
    public void onClick(View v) {
        // we need to create the object
        // of IntentIntegrator class
        // which is the class of QR library
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        //intentIntegrator.setPrompt("Scan a barcode or QR Code");
        //intentIntegrator.setOrientationLocked(true);
        intentIntegrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                messageText.setText(intentResult.getContents());
                //messageFormat.setText(intentResult.getFormatName());
                String s,stmt;
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                try {
                    Class.forName(Classes);
                    TextView tstmt = findViewById(R.id.skladID);
                    Toast.makeText(this, tstmt.getText(), Toast.LENGTH_SHORT).show();
                    connection = DriverManager.getConnection(url, username,password);
                    Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
                    Statement stat=connection.createStatement();
                    stmt = "SELECT [Наименование] FROM trxsql3.dbo.sklad where [код] = " + tstmt.getText();
                    ResultSet result=stat.executeQuery(stmt);
                    TextView tv=(TextView)findViewById(R.id.Nme);

                    while (result.next()){
                        tv.setText(result.getString(1));
                        Toast.makeText(this, result.getString(1), Toast.LENGTH_SHORT).show();
                    }
                    connection.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Connected no", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
//    public void start(View view) {
//          String s,stmt;
//        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//        try {
//            Class.forName(Classes);
//            TextView tstmt = findViewById(R.id.skladID);
//            Toast.makeText(this, tstmt.getText(), Toast.LENGTH_SHORT).show();
//            connection = DriverManager.getConnection(url, username,password);
//            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
//            Statement stat=connection.createStatement();
//            stmt = "SELECT [Наименование] FROM trxsql3.dbo.sklad where [код] = " + tstmt.getText();
//            ResultSet result=stat.executeQuery(stmt);
//            TextView tv=(TextView)findViewById(R.id.Nme);
//
//            while (result.next()){
//                tv.setText(result.getString(1));
//                Toast.makeText(this, result.getString(1), Toast.LENGTH_SHORT).show();
//            }
//            connection.close();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Class fail", Toast.LENGTH_SHORT).show();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Connected no", Toast.LENGTH_SHORT).show();
//        }
//    }

//    public void connection(){
//        try {
//
//            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
//
//            Connection conn=DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.198/SQLTOREX/Trxsql7;","litvinov","lv23");
//            Log.w("Connection", "Error");
//
//            Statement stat=conn.createStatement();
//            ResultSet result=stat.executeQuery("SELECT [Наименование] FROM dbo.sklad where код = 11814");
//
//            TextView tv=(TextView)findViewById(R.id.Nme);
//            tv.setText(result.getString(0));
//            conn.close();
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            Log.e("Error",e.getMessage());
//            Toast.makeText(getApplicationContext(), "Error"+e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//
//    }
}