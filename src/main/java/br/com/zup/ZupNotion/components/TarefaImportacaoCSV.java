package br.com.zup.ZupNotion.components;

import br.com.zup.ZupNotion.exceptions.ErroAoLerArquivoException;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.Usuario;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.services.UsuarioService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TarefaImportacaoCSV {

    @Autowired
    TarefaRepository tarefaRepository;
    @Autowired
    private UsuarioService usuarioService;

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Titulo", "Descricao", "Prioridade", "Estimativa em horas"};

    public boolean verificarFormatoCSV(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    public List<Tarefa> salvarTarefasComUsuario(InputStream is) {

        Usuario usuario = usuarioService.buscarUsuarioLogado();

        List<Tarefa> listaTarefa = capturarListaTarefasDoArquivoCSV(is).stream().map(tarefa ->
                tarefaRepository.save(tarefa)).collect(Collectors.toList());

        for (Tarefa tarefa : listaTarefa) {
            usuario.getTarefas().add(tarefa);
            tarefa.setUsuario(usuario);
            tarefaRepository.save(tarefa);
        }
        return listaTarefa;
    }

    public List<Tarefa> capturarListaTarefasDoArquivoCSV(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withIgnoreHeaderCase().withTrim())) {

            List<Tarefa> listaTarefas = new ArrayList<>();

            Iterable<CSVRecord> registrosCSV = csvParser.getRecords();

            for (CSVRecord csvRecord : registrosCSV) {

                Tarefa tarefa = new Tarefa(
                        csvRecord.get("titulo"),
                        csvRecord.get("descricao"),
                        csvRecord.get("prioridade"),
                        Integer.parseInt(csvRecord.get("Estimativa em horas")));

                listaTarefas.add(tarefa);
            }
            return listaTarefas;

        } catch (IOException e) {
            throw new ErroAoLerArquivoException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

}
