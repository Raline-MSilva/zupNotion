package br.com.zup.ZupNotion.models;

import br.com.zup.ZupNotion.exceptions.ErroAoLerArquivoException;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TarefaImportacaoCSV {
    @Autowired
    TarefaRepository tarefaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"Titulo", "Descricao", "Estimativa em horas"};

    public boolean verificarFormatoCSV(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public List<Tarefa> salvarTarefasComUsuario(String usuarioId, InputStream is) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        List<Tarefa> listaTarefa = capturarListaTarefasDoArquivoCSV(is).stream().map(tarefa ->
                tarefaRepository.save(tarefa)).collect(Collectors.toList());

        if (usuario.isPresent()) {
            for (Tarefa tarefa : listaTarefa) {
                usuario.get().getTarefas().add(tarefa);
                tarefa.setUsuario(usuario.get());
                tarefaRepository.save(tarefa);
            }
        }
        return listaTarefa;
    }

    public List<Tarefa> capturarListaTarefasDoArquivoCSV(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                     .withIgnoreHeaderCase().withTrim());) {

            List<Tarefa> listaTarefas = new ArrayList<Tarefa>();

            Iterable<CSVRecord> registrosCSV = csvParser.getRecords();

            for (CSVRecord csvRecord : registrosCSV) {

                Tarefa tarefa = new Tarefa(
                        csvRecord.get("titulo"),
                        csvRecord.get("descricao"),
                        Integer.parseInt(csvRecord.get("Estimativa em horas"))
                );

                listaTarefas.add(tarefa);
            }
            return listaTarefas;

        } catch (IOException e) {
            throw new ErroAoLerArquivoException("Erro ao ler o arquivo: " + e.getMessage());
        }
    }

}
