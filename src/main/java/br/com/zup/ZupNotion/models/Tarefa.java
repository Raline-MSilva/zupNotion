package br.com.zup.ZupNotion.models;

import lombok.Data;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "tarefas")
@Data
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(nullable = false)
    private String titulo;
    @Column(nullable = false)
    private String descricao;
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name = "data_de_cadastro")
    private LocalDateTime dataDeCadastro;
    @Column(name = "estimativa_em_horas")
    private Duration estimativaEmHoras;

}
