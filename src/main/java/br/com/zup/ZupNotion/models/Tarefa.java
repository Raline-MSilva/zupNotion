package br.com.zup.ZupNotion.models;

import br.com.zup.ZupNotion.models.enums.Prioridade;
import br.com.zup.ZupNotion.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String descricao;
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    private Status status = Status.A_FAZER;
    @Column(name = "data_de_cadastro")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataDeCadastro = LocalDateTime.now();
    @Column(name = "estimativa_em_horas")
    private Integer estimativaEmHoras;
    @ManyToOne
    private Usuario usuario;

    public Tarefa(String titulo, String descricao, String prioridade, Integer estimativaEmHoras) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = Prioridade.valueOf(prioridade);
        this.estimativaEmHoras = estimativaEmHoras;
    }

}
