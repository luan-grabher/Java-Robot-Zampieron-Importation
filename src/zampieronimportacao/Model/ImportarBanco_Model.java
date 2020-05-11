package zampieronimportacao.Model;

import Auxiliar.LctoTemplate;
import Dates.Dates;
import OFX.OFX;
import TemplateContabil.ComparacaoTemplates;
import TemplateContabil.Template;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImportarBanco_Model {
    private int codigoEmpresa = 1;
    private int nroBanco = 706;
    private int filial = 0;
    private int hpDeb = 1005;
    private int hpCred = 1002;
    
    private String obs = "";
    
    private File pastaEscrituracaoMensal = new File("\\\\HEIMERDINGER\\DOCS\\Contábil\\Clientes\\Zampieron & Dalallacorte\\Escrituração Mensal");
    private String pastaExtratos = pastaEscrituracaoMensal.getAbsolutePath() + "\\*ano*\\Extratos\\*mes*.*ano*";
    private File templatePadrao = Selector.Pasta.procura_arquivo(pastaEscrituracaoMensal, "Template;Importacao;Padrao;SCI.xlsm");
    private File filePastaPrincipal = null;
    
    private int mes;
    private int ano;
    private String mesAbrevidado = Dates.getMonthAbbr_PtBr(1);
    
    private List<LctoTemplate> ofxLctos = new ArrayList<>();
    private List<LctoTemplate> financeiroLctos = new ArrayList<>();

    public ImportarBanco_Model(int mes, int ano,int nroBanco) {
        this.mes = mes;
        this.ano = ano;
        mesAbrevidado =  Dates.getMonthAbbr_PtBr(mes);
        this.nroBanco = nroBanco;
        
    }

    public String getObs() {
        return obs;
    }
    
    public File setFilePastaPrincipal(String nomePastaBanco){
        pastaExtratos = pastaExtratos.replaceAll("\\*mes\\*", (mes<10?"0":"") + mes);
        pastaExtratos = pastaExtratos.replaceAll("\\*ano\\*", "" + ano);
        
        filePastaPrincipal = new File(pastaExtratos + "\\" + nomePastaBanco).getAbsoluteFile();
        
        return filePastaPrincipal;
    }
    
    public boolean setLctosOFX(){
        String arquivoProcurado = mesAbrevidado + ";.ofx";
        File fileOFX = Selector.Pasta.procura_arquivo(filePastaPrincipal,arquivoProcurado);
        if(fileOFX != null){
            ofxLctos = OFX.getListaLctos(fileOFX);
            return true;
        }else{
            obs = arquivoProcurado;
            return false;
        }
    }
    
    public boolean setLctosInternos(){
        String arquivoProcurado = mesAbrevidado + ";.csv";
        File fileFinanceiro = Selector.Pasta.procura_arquivo(filePastaPrincipal,arquivoProcurado);
        if(fileFinanceiro != null){
            Financeiro_Model financeiro = new Financeiro_Model(fileFinanceiro);
            financeiro.setLctos();
            financeiroLctos = financeiro.getLctos();
            return true;
        }else{
            obs = arquivoProcurado;
            return false;
        }
    }
    
    public boolean lctosOfxIguaisAosInternos(){
        obs = ComparacaoTemplates.getComparacaoString("OFX", "Financeiro", ofxLctos, financeiroLctos);
        return obs.equals("");
    }
    
    public Template colocaFinanceiroNoTemplate(){
        File templateSalvo = new File(filePastaPrincipal.getAbsolutePath() + "\\Template banco " + nroBanco + ".xlsm");
        return new Template(mes, ano, templatePadrao, templateSalvo, codigoEmpresa, filial, hpDeb, hpCred, nroBanco, financeiroLctos);
    }
}
