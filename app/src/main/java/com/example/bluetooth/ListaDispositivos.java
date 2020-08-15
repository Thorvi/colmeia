package com.example.bluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class ListaDispositivos extends ListActivity {

    private BluetoothAdapter meuBT = null;

    static String ENDEREÇO_MAC = null;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        meuBT = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = meuBT.getBondedDevices();

        if(dispositivosPareados.size() > 0)
        {
            for(BluetoothDevice dispositivo : dispositivosPareados){
                String nomeBT = dispositivo.getName();
                String macBT = dispositivo.getAddress();
                ArrayBluetooth.add(nomeBT + "\n" + macBT);
            }
        }
        setListAdapter(ArrayBluetooth);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();

        //Toast.makeText(getApplicationContext(), "Info: "+ informacaoGeral, Toast.LENGTH_LONG).show();

        String endereçoMac = informacaoGeral.substring(informacaoGeral.length() - 17);

        //Toast.makeText(getApplicationContext(), "MAC: "+ endereçoMac, Toast.LENGTH_LONG).show();

        Intent retornaMac = new Intent();
        retornaMac.putExtra(ENDEREÇO_MAC, endereçoMac);

        setResult(RESULT_OK, retornaMac);

        finish();
    }
}
