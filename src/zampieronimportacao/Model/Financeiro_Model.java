package zampieronimportacao.Model;

import Auxiliar.LctoTemplate;
import Auxiliar.Valor;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import main.Arquivo;

public class Financeiro_Model {

    private File arquivo;
    private List<LctoTemplate> lctos = new ArrayList<>();

    public Financeiro_Model(File arquivo) {
        this.arquivo = arquivo;
    }

    public void setLctos() {
        int colunaData = 0;
        int colunaOperacao = 1;
        int colunaBeneficiario = 2;
        int colunaNotaFiscal = 4;
        int colunaEntrada = 5;
        int colunaSaida = 6;

        String textoArquivo = Arquivo.ler(arquivo.getAbsolutePath());
        String[] linhas = textoArquivo.split("\r\n");
        for (String linha : linhas) {
            String[] colunas = linha.split(";");
            if (colunas.length >= 7) {
                Valor data = new Valor(colunas[colunaData]);
                String operacao = colunas[colunaOperacao];
                String beneficiario = colunas[colunaBeneficiario];
                String notaFiscal = colunas[colunaNotaFiscal];
                Valor entrada = new Valor(colunas[colunaEntrada]);
                Valor saida = new Valor(colunas[colunaSaida]);

                if (data.éUmaDataValida()
                        && !operacao.equals("")
                        && (entrada.getBigDecimal().compareTo(BigDecimal.ZERO) == 1
                        || saida.getBigDecimal().compareTo(BigDecimal.ZERO) == 1)) {

                    Valor valor = entrada.getBigDecimal().compareTo(BigDecimal.ZERO) == 1 ? entrada : new Valor(saida.getBigDecimal().multiply(new BigDecimal("-1")));
                    lctos.add(
                            new LctoTemplate(
                                    data.getString(),
                                    filtraDocumento(notaFiscal),
                                    "BEN- " + beneficiario,
                                    "OP- " + operacao,
                                    valor
                            )
                    );
                }
            }
        }
    }
    
    public String filtraDocumento(String docOriginal){
        if(docOriginal.contains("-")){
            String maiorString = ""; 
            
            String[] divs = docOriginal.split("-");
            for (String div : divs) {
                div = div.trim();
                if(div.length() > maiorString.length()){
                    maiorString = div;
                }
            }
            
            return maiorString.replaceAll(" ", "");
        }else{
            return docOriginal;
        }
    }

    public List<LctoTemplate> getLctos() {
        return lctos;
    }
}
