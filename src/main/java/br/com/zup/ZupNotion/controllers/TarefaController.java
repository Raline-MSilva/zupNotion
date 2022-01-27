package br.com.zup.ZupNotion.controllers;

import br.com.zup.ZupNotion.config.security.JWT.UsuarioLogado;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.dtos.*;
import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import br.com.zup.ZupNotion.services.TarefaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    public String pegarId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UsuarioLogado usuarioLogado = (UsuarioLogado) principal;
        return usuarioLogado.getId();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaTarefaDTO cadastrar (@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO) {
        Tarefa tarefa = modelMapper.map(cadastroTarefaDTO, Tarefa.class);
        return modelMapper.map(tarefaService.cadastrarTarefa(tarefa, pegarId()), RespostaTarefaDTO.class);
    }

    @GetMapping
    public List<TarefaResumoDTO> buscarTarefas(@RequestParam(required = false) Status status,
                                               @RequestParam(required = false)Prioridade prioridade){
        List<TarefaResumoDTO> tarefas = new ArrayList<>();

        for (Tarefa tarefa : tarefaService.buscarTarefas(pegarId(), status, prioridade)){
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


}
