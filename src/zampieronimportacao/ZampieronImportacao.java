package zampieronimportacao;

import Robo.AppRobo;
import java.util.Calendar;
import zampieronimportacao.Control.ImportarBanco_Control;

public class ZampieronImportacao {

    public static Calendar referencia = Calendar.getInstance(); 
    public static int mes = 12;
    public static int ano = 2019;
    public static String banco = "706#Itau 79391";
    
    public static void main(String[] args) {
        String nomeApp = "Zampieron Importação";
        
        AppRobo robo = new AppRobo(nomeApp);
        
        robo.definirParametros();
        mes = robo.getParametro("mes").getInteger();
        ano = robo.getParametro("ano").getInteger();
        banco = robo.getParametro("nomePastaBanco").getString();
        
        referencia.set(Calendar.MONTH, mes-1);
        referencia.set(Calendar.YEAR, ano);
        
        robo.setNome("Banco " + banco + " - " + nomeApp + " " + mes  +"/" + ano);
        robo.executar(ImportarBanco_Control.run(referencia, mes, ano, banco)
        );
    }
    
}
