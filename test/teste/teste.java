
package teste;

import java.util.Calendar;
import zampieronimportacao.Control.ImportarBanco_Control;


public class teste {

    public static void main(String[] args) {
        int mes = 3;
        int ano = 2020;
        String banco = "706#Itau 79391";
        Calendar referencia = Calendar.getInstance();
        referencia.set(Calendar.MONTH, mes + 1);
        referencia.set(Calendar.YEAR, ano);
        
        System.out.println(ImportarBanco_Control.run(referencia, mes, ano, banco).replaceAll("<br>", "\n"));
        
    }
    
}
