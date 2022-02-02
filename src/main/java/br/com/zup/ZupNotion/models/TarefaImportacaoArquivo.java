package br.com.zup.ZupNotion.models;

import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.repositories.TarefaRepository;
import br.com.zup.ZupNotion.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TarefaImportacaoArquivo {
    @Autowired
    TarefaRepository tarefaRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void salvarTarefasComUsuario(String caminho, String usuarioId) {
        Optional<Usuario> usuario = usuarioRepository.findById(usuarioId);

        List<Tarefa> listaTarefa = capturarListaTarefasDoArquivoCSV(caminho).stream().map(tarefa ->
                tarefaRepository.save(tarefa)).collect(Collectors.toList());

        if(usuario.isPresent()){
            for (Tarefa tarefa: listaTarefa) {
                usuario.get().getTarefas().add(tarefa);
                tarefa.setUsuario(usuario.get());
                tarefaRepository.save(tarefa);
            }
        }
    }

    private List<Tarefa> capturarListaTarefasDoArquivoCSV(String caminho) {
        Locale.setDefault(Locale.US);

        List<Tarefa> list = new ArrayList<Tarefa>();

        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String itemCsv = br.readLine();
            while (itemCsv != null) {

                String[] fields = itemCsv.split(",");
                String titulo = fields[0];
                String descricao = fields[1];
                Prioridade prioridade = Prioridade.valueOf(fields[2]);
                int estimativaEmHoras = Integer.parseInt(fields[3]);

                list.add(new Tarefa(titulo, descricao, prioridade, estimativaEmHoras));

                itemCsv = br.readLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        }
        return list;
    }

}
