package com.example.meibd1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private NumberPicker mNumberPicker;
    private RadioGroup mRadioGroup;
    private RadioButton mBtnAdicionar, mBtnExcluir;
    private EditText mEditText;
    private TextView mTextView, mTextView2;
    private Button mButton;

    public static final String MEU_BANCO_DADOS = "MinhaBaseDeDados"; //MinhaBaseDeDados.xml

    public static final int AND_MIN = 2015;
    public static final int AND_MAX = 2030;

    private NumberPicker.OnValueChangeListener valueAlteradoListener = new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker numberPicker, int anoAntigo, int anoAtual) {
            System.out.println(anoAtual);
            exibirSaldo(anoAntigo);
            mTextView2.setText("");
            mBtnAdicionar.setChecked(false);
            mBtnExcluir.setChecked(false);
        }
    };

    private Button.OnClickListener mButtonConfirmaClick = new Button.OnClickListener(){
        @Override
        public void onClick(View view){
            if(!mEditText.getText().toString().isEmpty()){
                float valor = Float.parseFloat(mEditText.getText().toString());
                int ano = mNumberPicker.getValue();

                switch (mRadioGroup.getCheckedRadioButtonId()){
                    case R.id.idRadioButtonAdicionar:
                        adicionarValor(ano, valor);
                        mTextView2.setText("Adicionado no ano " + ano + " o valor de " + String.format("R$ %.2f", valor));
                        break;
                    case R.id.idRadioButtonExcluir:
                        excluirValor(ano, valor);
                        mTextView2.setText("Excluido no ano " + ano + " o valor de " + String.format("R$ %.2f",valor));
                        break;
                    default:
                        mTextView2.setText("Marque uma opcao Adicionar ou Excluir");
                }
                exibirSaldo(ano);
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNumberPicker = findViewById(R.id.numberPicker);
        mNumberPicker.setMinValue(AND_MIN);
        mNumberPicker.setMaxValue(AND_MAX);
        mNumberPicker.setOnValueChangedListener(valueAlteradoListener);

        mRadioGroup = findViewById(R.id.idRadioGroup);
        mBtnAdicionar = findViewById(R.id.idRadioButtonAdicionar);
        mBtnExcluir = findViewById(R.id.idRadioButtonExcluir);
        mEditText = findViewById(R.id.idEditText);
        mTextView = findViewById(R.id.idTextView);
        mTextView2 = findViewById(R.id.idTextView2);
        mButton = findViewById(R.id.idButtonConfirmar);
        exibirSaldo(mNumberPicker.getValue());
        mButton.setOnClickListener(mButtonConfirmaClick);

    }

    private void adicionarValor(int ano, float valor){
        SharedPreferences sharedPreferences = getSharedPreferences(MEU_BANCO_DADOS, Context.MODE_PRIVATE);
        float valorAtual = sharedPreferences.getFloat(String.valueOf(ano), 0);
        float novoValor = valorAtual + valor;
        sharedPreferences.edit().putFloat(String.valueOf(ano), novoValor).apply();;
        mEditText.setText("");


    }
    private void excluirValor(int ano, float valor){
        SharedPreferences sharedPreferences = getSharedPreferences(MEU_BANCO_DADOS, Context.MODE_PRIVATE);
        float valorAtual = sharedPreferences.getFloat(String.valueOf(ano), 0);
        float novoValor = valorAtual - valor;
        if(novoValor < 0 ){
            novoValor = 0;
            mTextView2.setText("Exclusão não permitida!");

        }
        sharedPreferences.edit().putFloat(String.valueOf(ano), novoValor).apply();
        mEditText.setText("");
    }
    private void exibirSaldo(int ano){
        SharedPreferences sharedPreferences = getSharedPreferences(MEU_BANCO_DADOS, Context.MODE_PRIVATE);
        float saldo = sharedPreferences.getFloat(String.valueOf(ano), 0);
        mTextView.setText(String.format("R$ %.2f",saldo));
    }

}