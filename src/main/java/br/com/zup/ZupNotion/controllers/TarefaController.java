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
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    private UsuarioLogadoService usuarioLogadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaTarefaDTO cadastrar (@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO) {
        Tarefa tarefa = modelMapper.map(cadastroTarefaDTO, Tarefa.class);
        return modelMapper.map(tarefaService.cadastrarTarefa(tarefa, usuarioLogadoService.pegarId()), RespostaTarefaDTO.class);
    }

//    @GetMapping
//    public List<TarefaResumoDTO> buscarTarefas(@RequestParam(required = false) Status status,
//                                               @RequestParam(required = false) Prioridade prioridade){
//        List<TarefaResumoDTO> tarefas = new ArrayList<>();
//
//        for (Tarefa tarefa : tarefaService.buscarTarefas(usuarioLogadoService.pegarId(), status, prioridade)){
//            TarefaResumoDTO tarefaResumoDTO = modelMapper.map(tarefa, TarefaResumoDTO.class);
//            tarefas.add(tarefaResumoDTO);
//        }
//        return tarefas;
//    }

    @GetMapping
    public ResponseEntity<Page<TarefaResumoDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "3") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "titulo") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        Page<Tarefa> list = tarefaService.findPage(page, linesPerPage, orderBy, direction);
        Page<TarefaResumoDTO> listDto = list.map(TarefaResumoDTO::new);
        return ResponseEntity.ok().body(listDto);
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
        tarefaService.deletarTarefa(id, usuarioLogadoService.pegarId());
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
