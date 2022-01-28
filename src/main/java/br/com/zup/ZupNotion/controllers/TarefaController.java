package br.com.zup.ZupNotion.controllers;

import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.dtos.AlterarStatusDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.RespostaTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.TarefaResumoDTO;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.services.TarefaService;
import br.com.zup.ZupNotion.services.UsuarioLogadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    @Autowired
    private TarefaService tarefaService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaTarefaDTO cadastrar (@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO) {
        Tarefa tarefa = modelMapper.map(cadastroTarefaDTO, Tarefa.class);
        return modelMapper.map(tarefaService.cadastrarTarefa(tarefa, usuarioLogadoService.pegarId()), RespostaTarefaDTO.class);
    }

    @GetMapping
    public List<TarefaResumoDTO> buscarTarefas(@RequestParam(required = false) Status status,
                                               @RequestParam(required = false)Prioridade prioridade){
        List<TarefaResumoDTO> tarefas = new ArrayList<>();

        for (Tarefa tarefa : tarefaService.buscarTarefas(usuarioLogadoService.pegarId(), status, prioridade)){
            TarefaResumoDTO tarefaResumoDTO = modelMapper.map(tarefa, TarefaResumoDTO.class);
            tarefas.add(tarefaResumoDTO);
        }
        return tarefas;
    }

    @PatchMapping("/{id}")
    public void alterarStatusTarefa (@RequestBody @Valid AlterarStatusDTO alterarStatusDTO,
                                                  @PathVariable Integer id){
        Tarefa tarefa = tarefaService.localizarTarefaPorId(id);
        tarefa.setStatus(alterarStatusDTO.getStatus());
        tarefaService.salvarTarefa(tarefa);
    }

    @DeleteMapping("/{id}")
    public void deletarTarefa (@PathVariable Integer id){
        tarefaService.deletarTarefa(id);
    }

    @PutMapping("/{id}")
    public void alterarTarefaPorId (@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO,
                                    @PathVariable Integer id){
        Tarefa tarefa = tarefaService.localizarTarefaPorId(id);
        tarefa.setTitulo(cadastroTarefaDTO.getTitulo());
        tarefa.setDescricao(cadastroTarefaDTO.getDescricao());
        tarefaService.salvarTarefa(tarefa);
    }

}
