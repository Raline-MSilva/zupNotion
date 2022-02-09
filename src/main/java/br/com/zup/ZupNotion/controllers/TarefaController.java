package br.com.zup.ZupNotion.controllers;

import br.com.zup.ZupNotion.components.TarefaImportacaoCSV;
import br.com.zup.ZupNotion.config.ResponseMessage;
import br.com.zup.ZupNotion.models.Tarefa;
import br.com.zup.ZupNotion.models.dtos.*;
import br.com.zup.ZupNotion.services.TarefaService;
import br.com.zup.ZupNotion.services.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarefas")
@Api(value = "API para gerenciamento de tarefas")
@CrossOrigin(origins = "*")
public class TarefaController {

    @Autowired
    private TarefaService tarefaService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private TarefaImportacaoCSV importacaoCSV;

    @PostMapping
    @ApiOperation(value = "Método responsável por cadastrar uma tarefa")
    @ResponseStatus(HttpStatus.CREATED)
    public RespostaTarefaDTO cadastrar(@RequestBody @Valid CadastroTarefaDTO cadastroTarefaDTO) {
        Tarefa tarefa = modelMapper.map(cadastroTarefaDTO, Tarefa.class);
        return modelMapper.map(tarefaService.cadastrarTarefa(tarefa, cadastroTarefaDTO.getEmail()), RespostaTarefaDTO.class);
    }

    @GetMapping
    @ApiOperation(value = "Método responsável por listar todas as tarefas")
    public Page<TarefaResumoDTO> buscarTarefas(@RequestParam(required = false) String status,
                                               @RequestParam(required = false) String prioridade,
                                               Pageable pageable) {

        List<TarefaResumoDTO> tarefas = tarefaService.buscarTarefas(status, prioridade, pageable).stream()
                .map(tarefa -> modelMapper.map(tarefa, TarefaResumoDTO.class)).collect(Collectors.toList());

        return new PageImpl<>(tarefas, pageable, tarefas.size());
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Método responsável por apagar uma tarefa")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletarTarefa(@PathVariable Integer id) {
        tarefaService.deletarTarefa(id);
    }

    @PatchMapping("/{id}")
    @ApiOperation(value = "Método responsável por alterar título/descrição de uma tarefa")
    public void alterarTarefaPorId(@RequestBody @Valid AlterarDadosTarefaDTO alterarDadosTarefaDTO,
                                   @PathVariable Integer id) {
        tarefaService.alterarDadosTarefa(id, alterarDadosTarefaDTO.getTitulo(),
                alterarDadosTarefaDTO.getDescricao());
    }

    @PatchMapping("/alterarStatus/{id}")
    @ApiOperation(value = "Método responsável por alterar o status de uma tarefa")
    public void alterarStatusTarefa(@RequestBody @Valid AlterarStatusTarefaDTO alterarStatusTarefaDTO,
                                    @PathVariable Integer id) {
        tarefaService.alterarStatusTarefa(id, alterarStatusTarefaDTO.getStatus());
    }

    @PostMapping("/arquivosCSV")
    @ApiOperation(value = "Método responsável por fazer upload de um arquivo CSV")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseMessage> importarCSV(@RequestParam("file") MultipartFile file) {
        String message;

        if (tarefaService.validarSeArquivoCSV(file)) {
            tarefaService.salvarCSV(file);
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
