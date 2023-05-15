package com.example.barcode_scanner_to_excell;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class Excel_activity extends AppCompatActivity {

    TextInputEditText streetName, apartmentName,houseNumber, sensorName;
    Button btnCreate ;
    ImageButton btnGaytadan ;
    TextView txtbegli;
    String fileName = "";

    File filePath;
    activity_scanned_barcode textView = new activity_scanned_barcode();

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");



        streetName =  (TextInputEditText)findViewById(R.id.streetName);
        apartmentName =  (TextInputEditText)findViewById(R.id.apartmentName);
        houseNumber =  (TextInputEditText)findViewById(R.id.houseNumber);
        sensorName =  (TextInputEditText)findViewById(R.id.sensorName);

        btnCreate = findViewById(R.id.btnCreate);
        btnGaytadan = findViewById(R.id.btngaytadan);
        txtbegli = findViewById(R.id.beglitxt);
        txtbegli.setText(str);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}
                , PackageManager.PERMISSION_GRANTED);

        String recipientUsername2 = String.valueOf(textView.txtBarcodeValue);

        btnGaytadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Excel_activity.this, activity_scanned_barcode.class));
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
                fileName = sharedPreferences.getString("message","");

                filePath = new File(Environment.getExternalStorageDirectory() + "/" + fileName + ".xls");

                try {
//                    streetData(0.0)
//                    apartmentData(0.1)
//                    sensorData(0.2)
//                    barCodeData(0.3)
                    if (!filePath.exists()) {
                        HSSFWorkbook streetData = new HSSFWorkbook();
                        HSSFWorkbook apartmentData = new HSSFWorkbook();
                        HSSFWorkbook houseDate = new HSSFWorkbook();
                        HSSFWorkbook sensorData = new HSSFWorkbook();
                        HSSFWorkbook barCodeData = new HSSFWorkbook();
                        HSSFSheet hssfSheet = streetData.createSheet("MySheet");

                        HSSFRow hssfRow = hssfSheet.createRow(0);
                        HSSFCell hssfCell = hssfRow.createCell(0);

                        HSSFCell apartmentcell = hssfRow.createCell(1);
                        HSSFCell houseCell = hssfRow.createCell(2);
                        HSSFCell sensorCell = hssfRow.createCell(3);
                        HSSFCell barcodeCell = hssfRow.createCell(4);


                        hssfCell.setCellValue(streetName.getText().toString());
                        apartmentcell.setCellValue(apartmentName.getText().toString());
                        houseCell.setCellValue(houseNumber.getText().toString());
                        sensorCell.setCellValue(sensorName.getText().toString());
                        barcodeCell.setCellValue(str);
                        filePath.createNewFile();
                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                        streetData.write(fileOutputStream);
                        apartmentData.write(fileOutputStream);
                        houseDate.write(fileOutputStream);
                        sensorData.write(fileOutputStream);
                        barCodeData.write(fileOutputStream);
                        streetName.setText("");
                        apartmentName.setText("");
                        houseNumber.setText("");
                        sensorName.setText("");
                        txtbegli.setText("indiki");
                        Toast.makeText(Excel_activity.this, "File üstünlikli doredildi", Toast.LENGTH_SHORT).show();

                        if (fileOutputStream != null) {
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        }
                    }

                    else{

                        FileInputStream fileInputStream = new FileInputStream(filePath);

                        //HSSFWorkbook hssfWorkbookStreet = new HSSFWorkbook(fileInputStream);
                        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(fileInputStream);

                        HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);

                        int lastRowNum = hssfSheet.getLastRowNum();


                        HSSFRow hssfRowSteet = hssfSheet.createRow(++lastRowNum);
                        HSSFRow hssfRowApartment = hssfSheet.createRow(lastRowNum);
                        HSSFRow hssfRowHouse = hssfSheet.createRow(lastRowNum);
                        HSSFRow hssfRowSensor = hssfSheet.createRow(lastRowNum);
                        HSSFRow hssfRow = hssfSheet.createRow(lastRowNum);

                        hssfRowSteet.createCell(0).setCellValue(streetName.getText().toString());
                        hssfRowApartment.createCell(1).setCellValue(apartmentName.getText().toString());
                        hssfRowHouse.createCell(2).setCellValue(houseNumber.getText().toString());
                        hssfRowSensor.createCell(3).setCellValue(sensorName.getText().toString());
                        hssfRow.createCell(4).setCellValue(str);

                        fileInputStream.close();

                        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

                        hssfWorkbook.write(fileOutputStream);
                        hssfWorkbook.write(fileOutputStream);
                        hssfWorkbook.write(fileOutputStream);
                        hssfWorkbook.write(fileOutputStream);
                        hssfWorkbook.write(fileOutputStream);
                        streetName.setText("");
                        apartmentName.setText("");
                        houseNumber.setText("");
                        sensorName.setText("");
                        txtbegli.setText("indiki");
                        Toast.makeText(Excel_activity.this, "File üstünlikli Tazelendi", Toast.LENGTH_SHORT).show();
                        fileOutputStream.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        });
    }

    public void showDiolog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("File Name");

        final View customLayout = getLayoutInflater().inflate(R.layout.edittext_diolog, null);
        builder.setView(customLayout);

        builder.setPositiveButton("OK", (dialog, which) -> {
            // send data from the AlertDialog to the Activity
            EditText editText = customLayout.findViewById(R.id.editText);
            sendDialogDataToActivity(editText.getText().toString());
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Do something with the data coming from the AlertDialog
    private void sendDialogDataToActivity(String data) {
        SharedPreferences sharedPreferences = getSharedPreferences("userMessage", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("message", data);
        editor.apply();
        Toast.makeText(this, "üstünlikli", Toast.LENGTH_SHORT).show();
    }
}