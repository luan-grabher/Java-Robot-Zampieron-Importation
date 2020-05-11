package zampieronimportacao.Control;

import Auxiliar.Valor;
import java.io.File;
import java.util.Calendar;
import zampieronimportacao.Model.ImportarBanco_Model;

public class ImportarBanco_Control {

    public static String run(Calendar referencia, int mes, int ano, String banco) {
        String r;

        try {
            
            if(mes > 0 && ano > 2017 && !"".equals(banco)){
                //Separa nome pasta do numero do banco
                String[] bancoSplit = banco.split("#");
                int nroBanco = new Valor(bancoSplit[0].trim()).getInteger();
                String pastaBancoString = bancoSplit[1].trim();

                //Definir modelo Importacao
                ImportarBanco_Model modeloImportacao = new ImportarBanco_Model(mes, ano,nroBanco);

                //Verificar se pasta existe
                File pastaBanco = modeloImportacao.setFilePastaPrincipal(pastaBancoString);
                if (pastaBanco.exists()) {
                    //Pegar Ofx
                    if (modeloImportacao.setLctosOFX()) {
                        //Pegar Banco Interno
                        if (modeloImportacao.setLctosInternos()) {
                            //Confrontar Banco Interno com OFX
                            modeloImportacao.lctosOfxIguaisAosInternos();

                            //Coloca Financeiro no template
                            modeloImportacao.colocaFinanceiroNoTemplate();
                            
                            //REtorna observação caso tenha dado algum erro na comparação.
                            r = modeloImportacao.getObs();
                            r += "<br>Template salvo na pasta <a href='" + pastaBanco.getAbsolutePath() + "'>" + pastaBanco.getName() + "</a>";
                        } else {
                            r = "Arquivo financeiro do banco(" + modeloImportacao.getObs() + ") não encontrado na pasta <a href='" + pastaBanco.getAbsolutePath() + "'>" + pastaBanco.getName() + "</a>";
                        }
                    } else {
                        r = "Arquivo OFX do banco (" + modeloImportacao.getObs() + ") não encontrado na pasta <a href='" + pastaBanco.getAbsolutePath() + "'>" + pastaBanco.getName() + "</a>";
                    }
                } else {
                    r = "A pasta <a href='" + pastaBanco.getAbsolutePath() + "'>" + pastaBanco.getName() + "</a> não existe nos meses informados!";
                }
            }else{
                r = "Algum parâmetro não foi passado!";
            }
        } catch (Exception e) {
            r = "Ocorreu um erro inesperado no Java: " + e + "<br>";
            e.printStackTrace();
        }

        return r;
    }
}
