package com.example.gestionebudget;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.security.PrivilegedAction;

public class MainActivity extends AppCompatActivity
{
    private boolean isBudgetSet = false;
    enum typeInput
    {
        food,
        transport,
        other,
        ricavi
    }

    //Per decidere il tipo di input che stai inserendo
    private typeInput typeIn;

    private double budgetStart = 0;
    private double foodTot = 0;
    private double foodPC = 0;

    private double transportTot = 0;
    private double transportPC = 0;

    private double otherTot = 0;
    private double otherPC = 0;


    private double ricavi = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Rende invisibili le cose che non servono per ora
        EditText descrizione = (EditText) findViewById(R.id.descriptionEditTextText);
        EditText importo = (EditText) findViewById(R.id.outEditTextText);
        Button submit = (Button) findViewById(R.id.submitButton);

        descrizione.setVisibility(View.INVISIBLE);
        importo.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
    }

    public void ClickType(View v)
    {
        //Rende visibile le cose necessarie
        EditText descrizione = (EditText) findViewById(R.id.descriptionEditTextText);
        EditText importo = (EditText) findViewById(R.id.outEditTextText);
        Button submit = (Button) findViewById(R.id.submitButton);

        descrizione.setVisibility(View.VISIBLE);
        importo.setVisibility(View.VISIBLE);
        submit.setVisibility(View.VISIBLE);

        Button aux = (Button) v;

        switch (aux.getText().toString())
        {
            case "Food":
                typeIn = typeInput.food;
                break;
            case "Transport":
                typeIn = typeInput.transport;
                break;
            case "Other":
                typeIn = typeInput.other;
                break;
            case "Ricavi":
                typeIn = typeInput.ricavi;
                break;
        }
    }

    public void ClickSubmit(View v)
    {
        if(!isBudgetSet)
        {
            Toast toast = Toast.makeText(this, "Crea il budget", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        //Prendo per vedere l'importo
        EditText aux = findViewById(R.id.outEditTextText);

        double a = 0;

        try
        {
            a = Double.valueOf(aux.getText().toString());
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Inserisci numeri", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(a > budgetStart - foodTot - transportTot - otherTot + ricavi && typeIn != typeInput.ricavi)
        {
            Toast toast = Toast.makeText(this, "Spesa troppo alta", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        if(a < 0)
        {
            Toast toast = Toast.makeText(this, "Somma invalida,\n numero positivo richiesto", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(typeIn == typeInput.food)
        {
            foodTot += foodTot + a;
            foodPC = foodTot * 100 / budgetStart;
            TextView text = (TextView) findViewById(R.id.foodStatTextView);

            text.setText(String.valueOf(foodTot) + " (" + String.valueOf(foodPC) + "%)");
        }
        else if (typeIn == typeInput.transport)
        {
            transportTot += transportTot + a;
            transportPC = transportTot * 100 / budgetStart;
            TextView text = (TextView) findViewById(R.id.transportStatTextView);

            text.setText(String.valueOf(transportTot) + " (" + String.valueOf(transportPC) + "%)");
        }
        else if (typeIn == typeInput.other)
        {
            otherTot += otherTot + a;
            otherPC = otherTot * 100 / budgetStart;
            TextView text = (TextView) findViewById(R.id.otherStatTextView);

            text.setText(String.valueOf(otherTot) + " (" + String.valueOf(otherPC) + "%)");
        }
        else if (typeIn == typeInput.ricavi)
        {
            ricavi += ricavi + a;
            TextView text = (TextView) findViewById(R.id.ricaviStatTextView);

            text.setText(String.valueOf(ricavi));
        }

        TextView text = (TextView) findViewById(R.id.moneyLeftStatTextView);
        text.setText(String.valueOf(budgetStart - foodTot - transportTot - otherTot + ricavi));
    }

    public void ClickBudget(View v)
    {
        EditText aux = findViewById(R.id.budgetEditTextText);
        double a = 0;

        try
        {
            a = Double.valueOf(aux.getText().toString());
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(this, "Inserisci numeri", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(!isBudgetSet && a > 0)
        {
            isBudgetSet = true;
            budgetStart = a;
        }
        else if(isBudgetSet)
        {
            Toast toast = Toast.makeText(this, "non puoi cambiare il budget", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(a <= 0)
        {
            Toast toast = Toast.makeText(this, "inserisci numeri validi", Toast.LENGTH_SHORT);
            toast.show();
        }

        TextView help = (TextView) findViewById(R.id.budgetStatTextView);
        help.setText(String.valueOf(budgetStart));
        help = (TextView) findViewById(R.id.moneyLeftStatTextView);
        help.setText(String.valueOf(budgetStart));
    }
}