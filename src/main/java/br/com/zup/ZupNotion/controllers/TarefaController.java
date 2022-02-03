package br.com.zup.ZupNotion.controllers;


import br.com.zup.ZupNotion.config.ResponseMessage;
import br.com.zup.ZupNotion.components.ConversorDeTarefasComPaginacao;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.TarefaImportacaoCSV;
import br.com.zup.ZupNotion.models.dtos.AlterarStatusDTO;
import br.com.zup.ZupNotion.models.dtos.CadastroTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.RespostaTarefaDTO;
import br.com.zup.ZupNotion.models.dtos.TarefaResumoDTO;
import br.com.zup.ZupNotion.services.TarefaService;
import br.com.zup.ZupNotion.services.UsuarioLogadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

    @Autowired
    private TarefaImportacaoCSV importacaoCSV;

    @Autowired
    private ConversorDeTarefasComPaginacao conversorDeTarefasComPaginacao;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaTarefaDTO cadastrar(@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO) {
        Tarefa tarefa = modelMapper.map(cadastroTarefaDTO, Tarefa.class);
        return modelMapper.map(tarefaService.cadastrarTarefa(tarefa, usuarioLogadoService.pegarId()), RespostaTarefaDTO.class);
    }

    @GetMapping
    public Page<TarefaResumoDTO> buscarTarefas(@RequestParam(required = false) String status,
                                               @RequestParam(required = false) String prioridade,
                                               @PageableDefault(page = 0, size = 2) Pageable pageable) {


        var tarefasService = tarefaService.buscarTarefas(usuarioLogadoService.pegarId(), status, prioridade, pageable);
        List<TarefaResumoDTO> tarefas = conversorDeTarefasComPaginacao.converterPaginaEmLista(tarefasService);
        return new PageImpl<>(tarefas, pageable, tarefas.size());
    }


    @PatchMapping("/{id}")
    public void alterarStatusTarefa(@RequestBody @Valid AlterarStatusDTO alterarStatusDTO,
                                    @PathVariable Integer id) {
        tarefaService.alterarStatusTarefa(id, usuarioLogadoService.pegarId(), alterarStatusDTO.getStatus());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarTarefa(@PathVariable Integer id) {
        tarefaService.deletarTarefa(id, usuarioLogadoService.pegarId());
    }

    @PutMapping("/{id}")
    public void alterarTarefaPorId(@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO,
                                   @PathVariable Integer id) {
        tarefaService.alterarDadosTarefa(id, usuarioLogadoService.pegarId(), cadastroTarefaDTO.getTitulo(),
                cadastroTarefaDTO.getDescricao());
    }

    @PostMapping("/arquivosCSV")
    public ResponseEntity<ResponseMessage> importarCSV(@RequestParam("file") MultipartFile file) {
        String message = "";

        if (tarefaService.validarSeArquivoCSV(file)) {
                tarefaService.salvarCSV(file, usuarioLogadoService.pegarId());
                message = "Upload realizado com sucesso: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }else if(!tarefaService.validarSeArquivoCSV(file)){
                message = "Não foi possível realizar o upload do arquivo: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            } else{
        message = "Adicione um arquivo .csv para upload!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
        }
    }

}
