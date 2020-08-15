package com.example.bluetooth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DirectAction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.azeesoft.lib.colorpicker.ColorPickerDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter meuBT = null;
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket = null;

    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    ConnectedThread connectedThread;

    private static final int ATIVA_BLUETOOTH = 1;
    private static final int SOLICITA_CONEXAO = 2;
    private static String MAC = null;

    boolean conexao = false;

    int[] vetorRGB;
    String coress [] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.button);

        meuBT = BluetoothAdapter.getDefaultAdapter();
        if(meuBT == null)
        {
            Toast.makeText(getApplicationContext(),"SEM bluetooth", Toast.LENGTH_LONG).show();
        }
        else if (!meuBT.isEnabled())
        {
            Intent enableBtintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtintent, ATIVA_BLUETOOTH);
        }
    }

    public void cor(View view)
    {
        ColorPickerDialog colorPickerDialog= ColorPickerDialog.createColorPickerDialog(this,ColorPickerDialog.DARK_THEME);
        colorPickerDialog.setOnColorPickedListener(new ColorPickerDialog.OnColorPickedListener() {
            @Override
            public void onColorPicked(int color, String colorStr) {
                //Your code here
                System.out.println("Got color: " + color);
                System.out.println("Got color in hex form: " + colorStr);

                vetorRGB = getRGB(colorStr);

                for (int n: vetorRGB)
                {
                    System.out.println("Valor RGB: "+n);
                    Toast.makeText(MainActivity.this, "Valor RGB: "+n, Toast.LENGTH_LONG).show();

                }
            }
        });
        colorPickerDialog.show(); //mostra a caixa de dialogo para selecionar as cores
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case SOLICITA_CONEXAO:
            if (resultCode == Activity.RESULT_OK){
                MAC = data.getExtras().getString(ListaDispositivos.ENDEREÇO_MAC);

                meuDevice = meuBT.getRemoteDevice(MAC);

                try {
                    meuSocket = meuDevice.createRfcommSocketToServiceRecord(MEU_UUID);

                    meuSocket.connect();
                    conexao = true;

                    connectedThread = new ConnectedThread(meuSocket);
                    connectedThread.start();


                    Toast.makeText(getApplicationContext(),"Conectado à: "+MAC, Toast.LENGTH_LONG).show();

                } catch(IOException erro){
                    Toast.makeText(getApplicationContext(),"Ocorreu um erro " +erro, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

        public void conectar (View view)
        {
            if (conexao) {
                try{
                    meuSocket.close();
                    conexao = false;

                    Toast.makeText(getApplicationContext(),"Bluetooth desconectado", Toast.LENGTH_LONG).show();

                } catch(IOException erro) {
                    Toast.makeText(getApplicationContext(),"Ocorreu um erro", Toast.LENGTH_LONG).show();
                }

            } else {
                Intent abreLista = new Intent(MainActivity.this, ListaDispositivos.class);
                startActivityForResult(abreLista, SOLICITA_CONEXAO);
            }
        }

        public void converte(){
            int n = 10;
            String conv = Integer.toString(n);

            String brilho = Integer.toString(vetorRGB[0]);

            coress[0] = Integer.toString(n);
            System.out.println("o valor convertido é"+coress[0]);
        }

        public void H1(View view)
        {

            connectedThread.enviar("5 0 0 0 0");
            converte();

//            for(String i: cores){
//                connectedThread.enviar(""+i);
//            }
        }
        public void H2(View view)
        {
            connectedThread.enviar("2 0 150 0");
        }

        public void H3(View view)
        {
            connectedThread.enviar("3 0 150 0");
        }
        public void H4(View view)
        {
            connectedThread.enviar("4 0 150 0");
        }

        public void H5(View view)
        {
            connectedThread.enviar("5 0 150 0");
        }

        public void H6(View view)
        {
            connectedThread.enviar("5 0 150 0");
        }

        public void H7(View view)
        {
            connectedThread.enviar("5 0 150 0");
        }

        public void H8(View view)
        {
            connectedThread.enviar("5 0 150 0");
        }

    public static int[] getRGB(String rgb)
    {
        rgb = rgb.replace("#", "");
        final int[] ret = new int[4];
        for (int i = 0; i <= 3; i++)
        {
            ret[i] = Integer.parseInt(rgb.substring(i * 2, i * 2 + 2), 16);
        }
        return ret;
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {

            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) { }

            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

//        public void run() {
////            mmBuffer = new byte[1024];
////            int numBytes; // bytes returned from read()
////
////            // Keep listening to the InputStream until an exception occurs.
////            while (true) {
////                try {
////                    // Read from the InputStream.
////                    numBytes = mmInStream.read(mmBuffer);
////                    // Send the obtained bytes to the UI activity.
////                    Message readMsg = handler.obtainMessage(
////                            MessageConstants.MESSAGE_READ, numBytes, -1,
////                            mmBuffer);
////                    readMsg.sendToTarget();
////                } catch (IOException e) {
////                    Log.d(TAG, "Input stream was disconnected", e);
////                    break;
////                }
////            }
////        }

        public void enviar(String dadosEnviar) {
            byte[] msgBuffer = dadosEnviar.getBytes();

            try {
                mmOutStream.write(msgBuffer);

            } catch (IOException e) { }
        }
    }
}