package br.com.zup.ZupNotion.config;

import br.com.zup.ZupNotion.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<MensagemDeErro> tratarExcecoesValidacao(MethodArgumentNotValidException exception) {
        List<MensagemDeErro> mensagemDeErros = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            mensagemDeErros.add(new MensagemDeErro(fieldError.getDefaultMessage()));
        }
        return mensagemDeErros;
    }
    @ExceptionHandler(EmailJaExistenteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoEmailJaExistenteException(EmailJaExistenteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(DominioInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoDominioInvalidoException(DominioInvalidoException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoSenhaInvalidaException(SenhaInvalidaException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoExisteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MensagemDeErro tratarExcecaoUsuarioNaoExisteException(UsuarioNaoExisteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(PerfilInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MensagemDeErro tratarExcecaoPerfilInvalido(PerfilInvalidoException exception) {
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(TarefaNaoExisteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MensagemDeErro tratarTarefaNaoExisteException(TarefaNaoExisteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MensagemDeErro manipularExcecaoDeEnumInvalido(HttpMessageNotReadableException exception){
        if(exception.getLocalizedMessage().contains("br.com.zup.ZupNotion.models.enums.Prioridade")){
            return new MensagemDeErro("Prioridade deve ser BAIXA, MEDIA ou ALTA");
        }
        if(exception.getLocalizedMessage().contains("br.com.zup.ZupNotion.models.enums.Status")){
            return new MensagemDeErro("Status deve ser A_FAZER, EM_ANDAMENTO ou CONCLUIDA");
        }
        return new MensagemDeErro(exception.getLocalizedMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity arquivoMaiorQueOPermitido(MaxUploadSizeExceededException exc) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                new ResponseMessage("Arquivo maior que 2Mb não permitido!"));
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity requisicaoSemAnexo(MultipartException exc){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseMessage("Adicione um arquivo .csv para upload!"));
    }

}
